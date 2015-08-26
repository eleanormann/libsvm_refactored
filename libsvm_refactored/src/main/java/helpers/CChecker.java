package helpers;

import libsvm.svm;

public class CChecker {

	private ParameterValidationManager manager;

	public CChecker() {
		// TODO Auto-generated constructor stub
	}

	public CChecker(ParameterValidationManager parameterValidationManager) {
		this.manager = parameterValidationManager;
	}

	public String checkC(double c){
		if (c <= 0){
			return "ERROR: C <= 0\n";
		}else{
			return "C = " + c + "\n";
		}
	}

}
