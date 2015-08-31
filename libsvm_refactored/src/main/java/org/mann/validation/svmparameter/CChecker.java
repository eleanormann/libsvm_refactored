package org.mann.validation.svmparameter;

import org.mann.libsvm.SvmParameter;
import org.mann.libsvm.SvmParameter.SvmType;
import org.mann.validation.Checker;

public class CChecker implements Checker {

	private ParameterValidationManager manager;

	public CChecker(ParameterValidationManager parameterValidationManager) {
		this.manager = parameterValidationManager;
	}

	public void checkC(double c) {
		if (c <= 0) {
			manager.getValidationMessage().append("ERROR: C <= 0\n");
		} else {
			manager.getValidationMessage().append("C = " + c + "\n");
		}
	}

	public Checker checkParameter(SvmParameter params) {
		if (params.svmType == SvmType.C_SVC || params.svmType == SvmType.EPSILON_SVR || params.svmType == SvmType.NU_SVR) {
			checkC(params.C);
		}
		return manager.runCheckAndGetResponse("Nu", manager, params);
	}

}
