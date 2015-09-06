package org.mann.libsvm;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.util.StringTokenizer;

import org.mann.libsvm.SvmParameter.SvmType;
import org.mann.ui.SvmPrintInterface;
import org.mann.ui.SvmPrinterFactory;
import org.mann.ui.SvmPrinterFactory.PrintMode;

class svm_predict {

	private static SvmPrintInterface svmPrintString = SvmPrinterFactory.getPrinter(PrintMode.STANDARD);

	private static void info(String s) {
		svmPrintString.print(s);
	}

	private static double atof(String s) {
		return Double.valueOf(s).doubleValue();
	}

	protected static void predict(BufferedReader input, DataOutputStream output, SvmModel model, int predict_probability)
			throws IOException {
		int correct = 0;
		int total = 0;
		double error = 0;
		double sumv = 0, sumy = 0, sumvv = 0, sumyy = 0, sumvy = 0;

		SvmType svmType = svm.getSvmTypeFromModel(model);
		int nr_class = svm.svm_get_nr_class(model);
		double[] prob_estimates = null;

		if (predict_probability == 1) {
			if (svmType == SvmType.epsilon_svr || svmType == SvmType.nu_svr) {
				svm_predict.info("Prob. model for test data: target value = predicted value + z,\nz: "
						+ "Laplace distribution e^(-|z|/sigma)/(2sigma),sigma=" + svm.svm_get_svr_probability(model));
			} else {
				int[] labels = new int[nr_class];
				svm.svm_get_labels(model, labels);
				prob_estimates = new double[nr_class];
				output.writeBytes("labels");
				for (int j = 0; j < nr_class; j++)
					output.writeBytes(" " + labels[j]);
				output.writeBytes("\n");
			}
		}
		while (true) {
			String line = input.readLine();
			if (line == null)
				break;

			StringTokenizer st = new StringTokenizer(line, " \t\n\r\f:");

			double target = atof(st.nextToken());
			int m = st.countTokens() / 2;
			SvmNode[] x = new SvmNode[m];
			for (int j = 0; j < m; j++) {
				x[j] = new SvmNode();
				x[j].index = Integer.parseInt(st.nextToken());
				x[j].value = atof(st.nextToken());
			}

			double v;
			if (predict_probability == 1 && (svmType == SvmType.c_svc || svmType == SvmType.nu_svc)) {
				v = svm.svm_predict_probability(model, x, prob_estimates);
				output.writeBytes(v + " ");
				for (int j = 0; j < nr_class; j++)
					output.writeBytes(prob_estimates[j] + " ");
				output.writeBytes("\n");
			} else {
				v = svm.svm_predict(model, x);
				output.writeBytes(v + "\n");
			}

			if (v == target)
				++correct;
			error += (v - target) * (v - target);
			sumv += v;
			sumy += target;
			sumvv += v * v;
			sumyy += target * target;
			sumvy += v * target;
			++total;
		}
		if (svmType == SvmType.epsilon_svr || svmType == SvmType.nu_svr) {
			
			double meanSqError = error / total;
			double rSquared = ((total * sumvy - sumv * sumy) * (total * sumvy - sumv * sumy))
			/ ((total * sumvv - sumv * sumv) * (total * sumyy - sumy * sumy));
			
			svm_predict.info(String.format("Mean squared error = %s (regression)", meanSqError));
			svm_predict.info(String.format("Squared correlation coefficient = %s (regression)", rSquared));
		} else {
			
			double accuracy = (double) correct / total * 100;
			//TODO: find out how to escape %
			svm_predict.info(String.format("Accuracy = %s%s (%s/%s) (classification)", accuracy, "%", correct, total));
		}
	}

	// TODO: handle IOException properly
	public static void main(String argv[]) throws IOException {
		int i = 0;
		int predict_probability = 0;
		svmPrintString = SvmPrinterFactory.getPrinter(PrintMode.STANDARD);

		// parse options
		for (i = 0; i < argv.length; i++) {
			if (argv[i].charAt(0) != '-') {
				break;				
			}
			++i;
			switch (argv[i - 1].charAt(1)) {
			case 'b':
				predict_probability = Integer.parseInt(argv[i]);
				break;
			case 'q':
				svmPrintString = SvmPrinterFactory.getPrinter(PrintMode.QUIET);
				i--;
				break;
			default:
				SvmPrinterFactory.getPrinter(PrintMode.PREDICT_BAD_INPUT)
				.print("Unknown option: " + argv[i - 1] + "\n");
				return;
			}
		}
		if (i >= argv.length - 2) {
			SvmPrinterFactory.getPrinter(PrintMode.PREDICT_BAD_INPUT).print("");
			return;
		}
		try {
			BufferedReader input = createReader(argv[i]);
			DataOutputStream output = new DataOutputStream(new BufferedOutputStream(new FileOutputStream(argv[i + 2])));
			SvmModel model = svm.svm_load_model(argv[i + 1]);
			
			//TODO: refactor this logic to avoid repetition
			if (model == null) {
				System.err.print("can't open model file " + argv[i + 1] + "\n");
				input.close();
				output.close();
				return;
			}
			if (predict_probability == 1) {
				if (svm.svm_check_probability_model(model) == 0) {
					System.err.print("Model does not support probabiliy estimates\n");
					input.close();
					output.close();
					return;
				}
			} else {
				if (svm.svm_check_probability_model(model) != 0) {
					svm_predict.info("Model supports probability estimates, but disabled in prediction.\n");
				}
			}
			predict(input, output, model, predict_probability);
			input.close();
			output.close();
		} catch (FileNotFoundException e) {
			SvmPrinterFactory.getPrinter(PrintMode.PREDICT_BAD_INPUT).print(e.toString());
		} catch (ArrayIndexOutOfBoundsException e) {
			SvmPrinterFactory.getPrinter(PrintMode.PREDICT_BAD_INPUT).print(e.toString());
		}
	}

	private static BufferedReader createReader(String filename) throws FileNotFoundException {
		BufferedReader input = new BufferedReader(new FileReader(filename));
		return input;
	}
}
