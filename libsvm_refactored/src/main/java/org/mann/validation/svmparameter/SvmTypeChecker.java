package org.mann.validation.svmparameter;

import org.mann.libsvm.SvmParameter;
import org.mann.libsvm.SvmParameter.SvmType;
import org.mann.validation.Checker;

public class SvmTypeChecker implements Checker{

	private ParameterValidationManager manager;

	public SvmTypeChecker(ParameterValidationManager parameterValidationManager){
		this.manager = parameterValidationManager;
	}

	public Checker checkParameter(SvmParameter params) {
		SvmType svmType = params.getSvmType();
		if(svmType == null){
			manager.getValidationMessage().append("ERROR: Svm type not set\n");
		}else{
			manager.getValidationMessage().append("Svm type: ").append(svmType).append("\n");			
		}
		return manager.runCheckAndGetResponse("Kernel", params);
	}

	protected ParameterValidationManager getManager() {
		return manager;
	}
}
