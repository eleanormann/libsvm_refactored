package helpers;

import libsvm.svm;

public class DegreeChecker {

	private ParameterValidationManager manager;

	public DegreeChecker(ParameterValidationManager parameterValidationManager) {
		this.manager = parameterValidationManager;
	}

	public DegreeChecker() {
		// TODO Auto-generated constructor stub
	}

	public String checkDegreeOfPolynomialKernel(int degree) {
		if (degree < 0){
			return "ERROR: degree of polynomial kernel < 0\n";
		}
		return "Degree = " + degree + "\n";
	}

}
