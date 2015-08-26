package helpers;

import libsvm.SvmParameter.SvmType;
import libsvm.svm;

public class ProbabilityChecker {

	private ParameterValidationManager manager;

	public ProbabilityChecker(ParameterValidationManager parameterValidationManager) {
		this.manager = parameterValidationManager;
	}

	public ProbabilityChecker() {
		// TODO Auto-generated constructor stub
	}

	public String checkProbability(int probability, SvmType svmType) {
		if (probability != 0 && probability != 1){
			return "ERROR: Probability is neither 0 nor 1\n";	
		}
		if (probability == 1 && svmType == SvmType.ONE_CLASS){
			return "ERROR: one-class SVM probability output not supported yet";
		}
		return "Probability = " + probability + "\n";
	}

}
