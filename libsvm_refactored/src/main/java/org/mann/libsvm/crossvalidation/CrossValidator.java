package org.mann.libsvm.crossvalidation;

import org.mann.helpers.HelpMessages;
import org.mann.libsvm.SvmParameter;
import org.mann.libsvm.svm;
import org.mann.libsvm.SvmParameter.SvmType;
import org.mann.libsvm.svm_problem;
import org.mann.ui.ResultCollector;

public class CrossValidator {

	private int nFold;
	private ResultCollector crossValidationResults;

	public CrossValidator(int nFold) {
		this.nFold = nFold;
	}

	public CrossValidator(int nFold, ResultCollector crossValResults) {
		this(nFold);
		this.crossValidationResults = crossValResults;
	}

	public void checkNfold() {
		if (nFold < 2) {
			crossValidationResults.addError("n-fold cross validation: n was "+ nFold + " but must be >= 2");
		}else{
			crossValidationResults.addInfo(nFold + "-fold cross validation");
		}
	}

	public void doCrossValidation(svm_problem trainingDataset, SvmParameter param) {
		double[] target = new double[trainingDataset.length];
		svm.setResultCollector(crossValidationResults);
		svm.svm_cross_validation(trainingDataset, param, nFold, target);
		
		SvmType svmType = param.getSvmType();
		if (svmType == SvmType.epsilon_svr || svmType == SvmType.nu_svr) {
			double total_error = 0;
			double sumv = 0, sumy = 0, sumvv = 0, sumyy = 0, sumvy = 0;
			for (int i = 0; i < trainingDataset.length; i++) {
				double y = trainingDataset.y[i];
				double v = target[i];
				total_error += (v - y) * (v - y);
				sumv += v;
				sumy += y;
				sumvv += v * v;
				sumyy += y * y;
				sumvy += v * y;
			}
			double meanSqError = total_error / trainingDataset.length;
			double rSquared = ((trainingDataset.length * sumvy - sumv * sumy) * (trainingDataset.length * sumvy - sumv * sumy))
					/ ((trainingDataset.length * sumvv - sumv * sumv) * (trainingDataset.length * sumyy - sumy * sumy));
			
			crossValidationResults.addCrossValResult(String.format(HelpMessages.CROSS_VALIDATION_MSE, meanSqError));
			crossValidationResults.addCrossValResult(String.format(HelpMessages.CROSS_VALIDATION_RSQ, rSquared));
		} else {
			int total_correct = 0;
			for (int i = 0; i < trainingDataset.length; i++) {
				if (target[i] == trainingDataset.y[i]){
					++total_correct;					
				}				
			}
			double accuracy = 100.0 * total_correct / trainingDataset.length;
			crossValidationResults.addCrossValResult(String.format(HelpMessages.CROSS_VALIDATION_ACCURACY, accuracy, "%"));
		}
		
		
	}

}
