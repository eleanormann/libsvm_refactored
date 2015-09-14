package org.mann.validation.svmparameter;

import org.mann.libsvm.SvmParameter;
import org.mann.libsvm.SvmParameter.KernelType;
import org.mann.validation.Checker;
public class KernelChecker implements Checker {
	private ParameterValidationManager manager;
	
	public KernelChecker(ParameterValidationManager parameterValidationManager){
		this.manager = parameterValidationManager;
	}
	
	public Checker checkParameter(SvmParameter params) {
		KernelType kernelType = params.getKernelType();
		if(kernelType==null){
			manager.getValidationMessage().append("ERROR: kernel type not set\n");
		}else{ 			
			manager.getValidationMessage().append("Kernel type: ").append(kernelType).append("\n");
		}
		return manager.runCheckAndGetResponse("Gamma", manager, params);
	}
}
