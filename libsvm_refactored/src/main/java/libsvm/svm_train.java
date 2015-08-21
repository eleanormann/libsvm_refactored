package libsvm;

import helpers.HelpMessages;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.StringTokenizer;
import java.util.Vector;

import libsvm.SvmParameter.SvmType;
import ui.SvmPrintInterface;
import ui.SvmPrinterFactory;
import ui.SvmPrinterFactory.PrintMode;

class svm_train {

	private SvmParameter param; // set by parse_command_line
	private svm_problem prob; // set by read_problem
	private SvmModel model;
	private String input_file_name; // set by parse_command_line
	private String model_file_name; // set by parse_command_line
	private String error_msg;
	private int cross_validation;
	private int nr_fold;

	private void run(String argv[]) throws IOException {
		boolean hasBadInput = parse_command_line(argv);
		if(hasBadInput){
			return;
		}
		read_problem();
		error_msg = svm.svm_check_parameter(prob, param);

		if (error_msg != null) {
			System.err.print("ERROR: " + error_msg + "\n");
			System.exit(1);
		}

		if (cross_validation != 0) {
			do_cross_validation();
		} else {
			model = svm.svm_train(prob, param);
			svm.svm_save_model(model_file_name, model);
		}
	}
	
	private void do_cross_validation() {
		int i;
		int total_correct = 0;
		double total_error = 0;
		double sumv = 0, sumy = 0, sumvv = 0, sumyy = 0, sumvy = 0;
		double[] target = new double[prob.length];

		svm.svm_cross_validation(prob, param, nr_fold, target);
		if (param.svmType == SvmType.EPSILON_SVR || param.svmType == SvmType.NU_SVR) {
			for (i = 0; i < prob.length; i++) {
				double y = prob.y[i];
				double v = target[i];
				total_error += (v - y) * (v - y);
				sumv += v;
				sumy += y;
				sumvv += v * v;
				sumyy += y * y;
				sumvy += v * y;
			}
			System.out.printf(HelpMessages.CROSS_VALIDATION_MSE, total_error / prob.length);
			System.out.printf(HelpMessages.CROSS_VALIDATION_RSQ,
					((prob.length * sumvy - sumv * sumy) * (prob.length * sumvy - sumv * sumy))
							/ ((prob.length * sumvv - sumv * sumv) * (prob.length * sumyy - sumy * sumy)));
		} else {
			for (i = 0; i < prob.length; i++)
				if (target[i] == prob.y[i])
					++total_correct;
			System.out.printf(HelpMessages.CROSS_VALIDATION_ACCURACY, 100.0 * total_correct / prob.length);
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

	private boolean parse_command_line(String argv[]) {
		boolean hasBadInput = false; 
		int i;
		SvmPrintInterface print_func = null; // default printing to stdout

		param = new SvmParameter();
		// default values
		param.svmType = SvmType.C_SVC;
		param.kernel_type = SvmParameter.RBF;
		param.degree = 3;
		param.gamma = 0; // 1/num_features
		param.coef0 = 0;
		param.nu = 0.5;
		param.cache_size = 100;
		param.C = 1;
		param.eps = 1e-3;
		param.p = 0.1;
		param.shrinking = 1;
		param.probability = 0;
		param.nr_weight = 0;
		param.weight_label = new int[0];
		param.weight = new double[0];
		cross_validation = 0;

		// parse options
		for (i = 0; i < argv.length; i++) {
			if (argv[i].charAt(0) != '-'){
				break;		
			}
			if (++i >= argv.length){
				SvmPrinterFactory.getPrinter(PrintMode.TRAIN_BAD_INPUT).print("option on its own is not valid input");
				hasBadInput = true;
				return hasBadInput;
			}
			switch (argv[i - 1].charAt(1)) {
			case 's':
				param.svmType = param.getSvmTypeFromSvmParameter(Integer.parseInt(argv[i]));
				break;
			case 't':
				param.kernel_type = Integer.parseInt(argv[i]);
				break;
			case 'd':
				param.degree = Integer.parseInt(argv[i]);
				break;
			case 'g':
				param.gamma = atof(argv[i]);
				break;
			case 'r':
				param.coef0 = atof(argv[i]);
				break;
			case 'n':
				param.nu = atof(argv[i]);
				break;
			case 'm':
				param.cache_size = atof(argv[i]);
				break;
			case 'c':
				param.C = atof(argv[i]);
				break;
			case 'e':
				param.eps = atof(argv[i]);
				break;
			case 'p':
				param.p = atof(argv[i]);
				break;
			case 'h':
				param.shrinking = Integer.parseInt(argv[i]);
				break;
			case 'b':
				param.probability = Integer.parseInt(argv[i]);
				break;
			case 'q':
				print_func = SvmPrinterFactory.getPrinter(PrintMode.QUIET);
				i--;
				break;
			case 'v':
				cross_validation = 1;
				nr_fold = Integer.parseInt(argv[i]);
				if (nr_fold < 2) {
					SvmPrinterFactory.getPrinter(PrintMode.TRAIN_BAD_INPUT)
					.print("n-fold cross validation: n must >= 2");
					hasBadInput = true;
					return hasBadInput;
				}
				break;
			case 'w':
				++param.nr_weight;
				{
					int[] old = param.weight_label;
					param.weight_label = new int[param.nr_weight];
					System.arraycopy(old, 0, param.weight_label, 0, param.nr_weight - 1);
				}

				{
					double[] old = param.weight;
					param.weight = new double[param.nr_weight];
					System.arraycopy(old, 0, param.weight, 0, param.nr_weight - 1);
				}

				param.weight_label[param.nr_weight - 1] = Integer.parseInt(argv[i - 1].substring(2));
				param.weight[param.nr_weight - 1] = atof(argv[i]);
				break;
			default:
				SvmPrinterFactory.getPrinter(PrintMode.TRAIN_BAD_INPUT)
				.print("Unknown option: " + argv[i - 1]);
				hasBadInput = true;
				return hasBadInput;
			}
		}

		svm.svm_set_print_string_function(print_func);

		// determine filenames

		if (i >= argv.length){
			SvmPrinterFactory.getPrinter(PrintMode.TRAIN_BAD_INPUT).print("No file has been specified");
			return true;
		}
		
		input_file_name = argv[i];

		if (i < argv.length - 1)
			model_file_name = argv[i + 1];
		else {
			int p = argv[i].lastIndexOf('/');
			++p; // whew...
			model_file_name = argv[i].substring(p) + ".model";
		}
		return hasBadInput;
	}

	// read in a problem (in svmlight format)

	protected void read_problem() throws IOException {
		try {
			BufferedReader fp = new BufferedReader(new FileReader(input_file_name));

			Vector<Double> vy = new Vector<Double>();
			Vector<SvmNode[]> vx = new Vector<SvmNode[]>();
			int max_index = 0;

			while (true) {
				String line = fp.readLine();
				if (line == null)
					break;

				StringTokenizer st = new StringTokenizer(line, " \t\n\r\f:");

				vy.addElement(atof(st.nextToken()));
				int m = st.countTokens() / 2;
				SvmNode[] x = new SvmNode[m];
				for (int j = 0; j < m; j++) {
					x[j] = new SvmNode();
					x[j].index = Integer.parseInt(st.nextToken());
					x[j].value = atof(st.nextToken());
				}
				if (m > 0)
					max_index = Math.max(max_index, x[m - 1].index);
				vx.addElement(x);
			}

			prob = new svm_problem();
			prob.length = vy.size();
			prob.x = new SvmNode[prob.length][];
			for (int i = 0; i < prob.length; i++)
				prob.x[i] = vx.elementAt(i);
			prob.y = new double[prob.length];
			for (int i = 0; i < prob.length; i++)
				prob.y[i] = vy.elementAt(i);

			if (param.gamma == 0 && max_index > 0)
				param.gamma = 1.0 / max_index;

			if (param.kernel_type == SvmParameter.PRECOMPUTED)
				for (int i = 0; i < prob.length; i++) {
					if (prob.x[i][0].index != 0) {
						System.err.print("Wrong kernel matrix: first column must be 0:sample_serial_number\n");
						System.exit(1);
					}
					if ((int) prob.x[i][0].value <= 0 || (int) prob.x[i][0].value > max_index) {
						System.err.print("Wrong input format: sample_serial_number out of range\n");
						System.exit(1);
					}
				}

			fp.close();
		} catch (FileNotFoundException ex) {
			SvmPrinterFactory.getPrinter(PrintMode.TRAIN_BAD_INPUT).print(ex.getMessage());
			return;
		}
	}

	public static void main(String argv[]) throws IOException {
		svm_train t = new svm_train();
		t.run(argv);
	}
}
