package org.mann.validation.svmparameter;

import org.mann.libsvm.SvmParameter;
import org.mann.validation.Checker;
public class KernelChecker implements Checker {
	private ParameterValidationManager manager;
	
	public KernelChecker(ParameterValidationManager parameterValidationManager){
		this.manager = parameterValidationManager;
	}
	
	public Checker checkParameter(SvmParameter params) {
		if(params.kernelType==null){
			manager.getValidationMessage().append( "ERROR: kernel type not set\n");
		}else{ 			
			manager.getValidationMessage().append( "Kernel type: " + params.kernelType + "\n");
		}
		return manager.runCheckAndGetResponse("Gamma", manager, params);
	}
}
