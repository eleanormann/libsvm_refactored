package org.mann.helpers;

import org.mann.libsvm.SvmParameter;
import org.mann.libsvm.svm;

public class DegreeChecker implements Checker {

	private ParameterValidationManager manager;

	public DegreeChecker(ParameterValidationManager parameterValidationManager) {
		this.manager = parameterValidationManager;
	}

	public DegreeChecker() {
		// TODO Auto-generated constructor stub
	}

	public void checkDegreeOfPolynomialKernel(int degree) {
		if (degree < 0){
			manager.getValidationMessage().append("ERROR: degree of polynomial kernel < 0\n");
		}else{			
			manager.getValidationMessage().append("Degree = " + degree + "\n");
		}
	}

	public Checker checkParameter(SvmParameter params) {
		checkDegreeOfPolynomialKernel(params.degree);
		return manager.runCheckAndGetResponse("Cache", manager, params);
	}

}
