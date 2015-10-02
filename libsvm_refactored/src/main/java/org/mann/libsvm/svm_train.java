package org.mann.libsvm;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.List;
import java.util.StringTokenizer;
import java.util.Vector;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.mann.helpers.HelpMessages;
import org.mann.libsvm.SvmParameter.KernelType;
import org.mann.libsvm.SvmParameter.SvmType;
import org.mann.libsvm.crossvalidation.CrossValidator;
import org.mann.ui.ResultCollector;
import org.mann.ui.SvmPrintInterface;
import org.mann.ui.SvmPrinterFactory;
import org.mann.ui.SvmPrinterFactory.PrintMode;
import org.mann.validation.commandline.CommandLineWrapper;
import org.mann.validation.commandline.SvmTrainCommandLineParser;
import org.mann.validation.svmparameter.ParameterValidationManager;

public class svm_train {

	private SvmParameter param; // set by parse_command_line
	private SvmProblem prob; // set by read_problem
	private SvmModel model;
	private String inputFilename; // set by parse_command_line
	private String model_file_name; // set by parse_command_line
	private String error_msg;
	private int cross_validation;
	private int nFold;

	protected SvmParameter getSvmParameter(){
		return param;
	}
	
	public SvmProblem getSvmProblem(){
		return prob;
	}
	
	protected SvmModel getSvmModel(){
		return model;
	}
	
	public void run(String argv[], ResultCollector result) throws IOException {
		try{
			parse_command_line(argv, result);
			read_problem(result);
			
			//TODO: these two collecting parameters don't integrate
			//Expires 12th October
			error_msg = checkSvmParameter(prob, param);
			if(error_msg == null || error_msg.contains("ERROR")){
				result.addError(error_msg);
			}
			
			
			if (cross_validation != 0) {
				new CrossValidator(nFold, result).doCrossValidation(prob, param);;
			} else {
				model = svm.svm_train(prob, param);
				svm.svm_save_model(model_file_name, model);
			}
		}catch(Exception e){
			result.addException(e);
		}
	}

	private static double atof(String s) {
		double d = Double.valueOf(s).doubleValue();
		if (Double.isNaN(d) || Double.isInfinite(d)) {
			System.err.print("NaN or Infinity in input\n");
			System.exit(1);
		}
		return (d);
	}

	protected void parse_command_line(String argv[], ResultCollector result) throws ParseException {
		int i;
		SvmPrintInterface print_func = null; // default printing to stdout
		SvmTrainCommandLineParser optionsValidator = new SvmTrainCommandLineParser();
		CommandLineWrapper options = optionsValidator.parseCommandLine(argv);
		CommandLine cmd = options.getCommandLine();
		param = new SvmParameter();
		param.initializeFields(options, optionsValidator);
		cross_validation = 0;
		
		if(cmd.hasOption('q')){
			print_func = SvmPrinterFactory.getPrinter(PrintMode.QUIET);
		}
		if(cmd.hasOption('v')){
			cross_validation = 1;
			nFold = (int) options.getOptionValue("v");
			checkNFold(result);
		}
		if(cmd.hasOption("w")){ 
			++param.nr_weight;
			{
				int[] old = param.getWeight_label();
				param.setWeightLabel(new int[param.getNr_weight()]); //new array with 1 value
				System.arraycopy(old, 0, param.getWeight_label(), 0, param.getNr_weight() - 1);
			}
			
			{
				double[] old = param.getWeight();
				param.setWeight(new double[param.nr_weight]);
				System.arraycopy(old, 0, param.getWeight(), 0, param.nr_weight - 1);
			}
			
			param.getWeight_label()[param.nr_weight - 1] = ((int[]) options.getOptionValue("w"))[0];
			param.getWeight()[param.nr_weight - 1] = ((int[])options.getOptionValue("w"))[1];
		}
			

		svm.svm_set_print_string_function(print_func);

		// determine filenames
		List<String> filenames = options.getArgsList();
		if(filenames.isEmpty()){
			result.addError("No file has been specified");
		}else{
			inputFilename = filenames.get(0);
			if(filenames.size()>1){
				model_file_name = filenames.get(1) + ".model";
			}
		}

	}

	private void checkNFold(ResultCollector result) {
		if (nFold < 2) {
			result.addError("n-fold cross validation: n must >= 2");
		}else{
			result.addInfo(nFold + "-fold cross validation");
		}
	}

	// read in a problem (in svmlight format)

	public void read_problem(ResultCollector result) throws IOException {

		try (BufferedReader fp = new BufferedReader(new FileReader(inputFilename))) {
			Vector<Double> vy = new Vector<Double>();
			Vector<SvmNode[]> vx = new Vector<SvmNode[]>();
			int max_index = 0;

			while (true) {
				String line = fp.readLine();
				if (line == null)
					break;

				StringTokenizer st = new StringTokenizer(line, " \t\n\r\f:");

				vy.addElement(atof(st.nextToken()));
				int max = st.countTokens() / 2;
				SvmNode[] x = new SvmNode[max];
				for (int j = 0; j < max; j++) {
					x[j] = new SvmNode();
					x[j].index = Integer.parseInt(st.nextToken());
					x[j].value = atof(st.nextToken());
				}
				if (max > 0) {
					max_index = Math.max(max_index, x[max - 1].index);
				}
				vx.addElement(x);
			}

			prob = new SvmProblem();
			prob.length = vy.size();
			
			prob.x = new SvmNode[prob.length][];
			for (int i = 0; i < prob.length; i++){
				prob.x[i] = vx.elementAt(i);				
			}
			
			prob.y = new double[prob.length];
			for (int i = 0; i < prob.length; i++){				
				prob.y[i] = vy.elementAt(i);
			}

			if (param.getGamma() == 0 && max_index > 0){				
				param.setGamma(1.0 / max_index);
			}

			if (param.getKernelType() == KernelType.precomputed) {

				checkPrecomputedKernelProperlySet(result, max_index);
			}
		}
	}

	private ResultCollector checkPrecomputedKernelProperlySet(ResultCollector result, int max_index) {
		for (int i = 0; i < prob.length; i++) {
			if (prob.x[i][0].index != 0) {
				// TODO: put this check in the validation section
				// Expires 6th October 2015
				result.addError("Wrong kernel matrix: first column must be 0:sample_serial_number");
				return result;
			}
			if ((int) prob.x[i][0].value <= 0 || (int) prob.x[i][0].value > max_index) {
				// TODO: put this check in the validation section
				// Expires 6th October 2015
				result.addError("Wrong input format: sample_serial_number out of range");
				return result;
			}
		}
		result.addInfo("precomputed kernel correctly formated\n");
		return result;
	}

	public String checkSvmParameter(SvmProblem prob, SvmParameter param) {
		ParameterValidationManager paramValManager = new ParameterValidationManager(new StringBuilder());
		paramValManager.checkNuThenRunCheckAndGetResponse("Svm Type", paramValManager, param, prob);
		return paramValManager.getValidationMessage().toString();
	}
	
	public static void main(String argv[]) throws IOException {
		svm_train t = new svm_train();
		t.run(argv, new ResultCollector());
	}

	public void setInputFile(String filename) {
		this.inputFilename = filename;
	}

	public void setNrFold(int nFold) {
		this.nFold = nFold;
	}

	public void setParam(SvmParameter svmParam) {
		this.param = svmParam;	
	}

	
}
