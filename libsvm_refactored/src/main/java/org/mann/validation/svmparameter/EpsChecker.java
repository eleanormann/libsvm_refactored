package org.mann.validation.svmparameter;

import org.mann.libsvm.SvmParameter;
import org.mann.validation.Checker;

public class EpsChecker implements Checker {

	private ParameterValidationManager manager;

	public EpsChecker(ParameterValidationManager parameterValidationManager) {
		this.manager = parameterValidationManager;
	}

	public void checkEps(double eps) {
		if (eps <= 0){
			manager.getValidationMessage().append("ERROR: eps <= 0\n");			
		}else{
			manager.getValidationMessage().append("Eps = " + eps + "\n");
		}
	}

	public Checker checkParameter(SvmParameter params) {
		checkEps(params.eps);
		return manager.runCheckAndGetResponse("C", manager, params);
	}

}
