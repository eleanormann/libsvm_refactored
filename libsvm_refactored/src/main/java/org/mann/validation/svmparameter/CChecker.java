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
			manager.getValidationMessage().append("C = ").append(c).append("\n");
		}
	}

	public Checker checkParameter(SvmParameter params) {
		SvmType svmType = params.getSvmType();
		if (svmType == SvmType.c_svc || svmType == SvmType.epsilon_svr || svmType == SvmType.nu_svr) {
			checkC(params.costC);
		}
		return manager.runCheckAndGetResponse("Nu", params);
	}

}
