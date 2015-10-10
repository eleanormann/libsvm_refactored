package org.mann.libsvm.crossvalidation;

import org.mann.helpers.HelpMessages;
import org.mann.libsvm.SvmParameter;
import org.mann.libsvm.svm;
import org.mann.libsvm.SvmParameter.SvmType;
import org.mann.libsvm.SvmProblem;
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

	public CrossValidator checkNfold() {
		if (nFold < 2) {
			crossValidationResults.addError("n-fold cross validation: n was "+ nFold + " but must be >= 2");
			return null;
		}else{
			crossValidationResults.addInfo(nFold + "-fold cross validation");
			return this;
		}
	}

	public void doCrossValidation(SvmProblem trainingDataset, SvmParameter param) {
		
	  double[] target = new double[trainingDataset.length];
		svm.setResultCollector(crossValidationResults);
		svm.svm_cross_validation(trainingDataset, param, nFold, target);
		
		SvmType svmType = param.getSvmType();
		if (svmType == SvmType.epsilon_svr || svmType == SvmType.nu_svr) {
			calculateMeanSquaredErrorAndRSquared(trainingDataset, target);
		} else {
			calculateTotalAccurate(trainingDataset, target);
		}
		
		
	}

	private void calculateTotalAccurate(SvmProblem trainingDataset, double[] target) {
		int total_correct = 0;
		for (int i = 0; i < trainingDataset.length; i++) {
			if (target[i] == trainingDataset.y[i]){
				++total_correct;					
			}				
		}
		double accuracy = 100.0 * total_correct / trainingDataset.length;
		crossValidationResults.addTotalAccuracy(accuracy);
		crossValidationResults.addCrossValResult(String.format(HelpMessages.CROSS_VALIDATION_ACCURACY, accuracy, "%"));
	}

	private void calculateMeanSquaredErrorAndRSquared(SvmProblem trainingDataset, double[] target) {
		double totalError = 0;
		double sumPredicted = 0, sumActual = 0, sumPredictedSquared = 0, sumActualSquared = 0, sumPredictedTimesActual = 0;
		for (int i = 0; i < trainingDataset.length; i++) {
			double actualOutcome = trainingDataset.y[i];
			double predictedOutcome = target[i];
			totalError += (predictedOutcome - actualOutcome) * (predictedOutcome - actualOutcome);
			sumPredicted += predictedOutcome;
			sumActual += actualOutcome;
			sumPredictedSquared += predictedOutcome * predictedOutcome;
			sumActualSquared += actualOutcome * actualOutcome;
			sumPredictedTimesActual += predictedOutcome * actualOutcome;
		}
		double meanSqError = totalError / trainingDataset.length;
		double rSquared = ((trainingDataset.length * sumPredictedTimesActual - sumPredicted * sumActual) * (trainingDataset.length * sumPredictedTimesActual - sumPredicted * sumActual))
				/ ((trainingDataset.length * sumPredictedSquared - sumPredicted * sumPredicted) * (trainingDataset.length * sumActualSquared - sumActual * sumActual));
		
		crossValidationResults.addCrossValResult(String.format(HelpMessages.CROSS_VALIDATION_MSE, meanSqError));
		crossValidationResults.addCrossValResult(String.format(HelpMessages.CROSS_VALIDATION_RSQ, rSquared));
		crossValidationResults.addMeanSqError(meanSqError);
		crossValidationResults.addRSquared(rSquared);
	}

}
