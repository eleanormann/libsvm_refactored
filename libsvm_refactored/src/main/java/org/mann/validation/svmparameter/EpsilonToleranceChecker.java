package org.mann.validation.svmparameter;

import org.mann.libsvm.SvmParameter;
import org.mann.validation.Checker;

public class EpsilonToleranceChecker implements Checker {

	private ParameterValidationManager manager;

	public EpsilonToleranceChecker(ParameterValidationManager parameterValidationManager) {
		this.manager = parameterValidationManager;
	}

	public void checkEps(double eps) {
		if (eps <= 0){
			manager.getValidationMessage().append("ERROR: Epsilon (tolerance) <= 0\n");			
		}else{
			manager.getValidationMessage().append("Epsilon (tolerance) = " + eps + "\n");
		}
	}

	public Checker checkParameter(SvmParameter params) {
		checkEps(params.epsilonTolerance);
		return manager.runCheckAndGetResponse("C", manager, params);
	}

}
