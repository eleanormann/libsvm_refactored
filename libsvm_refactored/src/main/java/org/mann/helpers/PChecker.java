package org.mann.helpers;

import org.mann.libsvm.SvmParameter;
import org.mann.libsvm.svm;

public class PChecker implements Checker {

	private ParameterValidationManager manager;

	public PChecker(ParameterValidationManager parameterValidationManager) {
		this.manager = parameterValidationManager;
	}

	public PChecker() {
		// TODO Auto-generated constructor stub
	}

	public void checkP(double p) {
		if (p < 0){
			manager.getValidationMessage().append("ERROR: p < 0\n");			
		}else{
			manager.getValidationMessage().append("p = " + p + "\n");
		}
	}

	public Checker checkParameter(SvmParameter params) {
		checkP(params.p);
		return manager.runCheckAndGetResponse("Shrinking", manager, params);
	}

}
