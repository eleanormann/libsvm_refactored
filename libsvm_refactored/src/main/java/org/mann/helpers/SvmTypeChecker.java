package org.mann.helpers;

import org.mann.libsvm.SvmParameter;
import org.mann.libsvm.svm;
import org.mann.libsvm.SvmParameter.SvmType;

public class SvmTypeChecker implements Checker{

	private ParameterValidationManager manager;
	
	public SvmTypeChecker(){};
	
	public SvmTypeChecker(ParameterValidationManager parameterValidationManager){
		this.manager = parameterValidationManager;
	}

	public Checker checkParameter(SvmParameter params) {
		manager.getValidationMessage().append("Svm Type: " + params.svmType + "\n");
		return manager.runCheckAndGetResponse("Kernel", manager, params);
	}

	protected ParameterValidationManager getManager() {
		return manager;
	}
}
