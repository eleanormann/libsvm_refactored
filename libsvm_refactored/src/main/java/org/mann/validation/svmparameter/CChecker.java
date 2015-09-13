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
		if (params.svmType == SvmType.c_svc || params.svmType == SvmType.epsilon_svr || params.svmType == SvmType.nu_svr) {
			checkC(params.costC);
		}
		return manager.runCheckAndGetResponse("Nu", manager, params);
	}

}
