package org.mann.validation.svmparameter;

import org.mann.libsvm.SvmParameter;
import org.mann.validation.Checker;

public class SvmTypeChecker implements Checker{

	private ParameterValidationManager manager;

	public SvmTypeChecker(ParameterValidationManager parameterValidationManager){
		this.manager = parameterValidationManager;
	}

	public Checker checkParameter(SvmParameter params) {
		if(params.svmType == null){
			manager.getValidationMessage().append("ERROR: Svm type not set\n");
		}else{
			manager.getValidationMessage().append("Svm type: " + params.svmType + "\n");			
		}
		return manager.runCheckAndGetResponse("Kernel", manager, params);
	}

	protected ParameterValidationManager getManager() {
		return manager;
	}
}
