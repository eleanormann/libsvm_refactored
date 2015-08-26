package helpers;

import libsvm.svm;

public class EpsChecker {

	private ParameterValidationManager manager;

	public EpsChecker(ParameterValidationManager parameterValidationManager) {
		this.manager = parameterValidationManager;
	}

	public EpsChecker() {
		// TODO Auto-generated constructor stub
	}

	public String checkEps(double eps) {
		if (eps <= 0){
			return "ERROR: eps <= 0\n";			
		}else{
			return "Eps = " + eps + "\n";
		}
	}

}
