package org.mann.validation.svmparameter;

import org.mann.helpers.Checker;
import org.mann.libsvm.SvmParameter;
import org.mann.libsvm.svm;

public class CChecker implements Checker {

	private ParameterValidationManager manager;

	public CChecker() {
		// TODO Auto-generated constructor stub
	}

	public CChecker(ParameterValidationManager parameterValidationManager) {
		this.manager = parameterValidationManager;
	}

	public void checkC(double c){
		if (c <= 0){
			manager.getValidationMessage().append( "ERROR: C <= 0\n");
		}else{
			manager.getValidationMessage().append("C = " + c + "\n");
		}
	}

	public Checker checkParameter(SvmParameter params) {
		checkC(params.C);
		return manager.runCheckAndGetResponse("Nu", manager, params);
	}

}
