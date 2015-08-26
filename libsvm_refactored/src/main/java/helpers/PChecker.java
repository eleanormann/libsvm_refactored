package helpers;

import libsvm.svm;

public class PChecker {

	private ParameterValidationManager manager;

	public PChecker(ParameterValidationManager parameterValidationManager) {
		this.manager = parameterValidationManager;
	}

	public PChecker() {
		// TODO Auto-generated constructor stub
	}

	public String checkP(double p) {
		if (p < 0){
			return "ERROR: p < 0\n";			
		}else{
			return "p = " + p + "\n";
		}
	}

}
