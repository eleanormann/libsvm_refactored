package org.mann.validation.svmparameter;

import org.mann.helpers.Checker;
import org.mann.libsvm.SvmParameter;
import org.mann.libsvm.svm;
public class KernelChecker implements Checker {
	private ParameterValidationManager manager;
	
	public KernelChecker(){};
	
	public KernelChecker(ParameterValidationManager parameterValidationManager){
		this.manager = parameterValidationManager;
	}
	
	public void checkKernelType(int kernelType) {
		if (kernelType != SvmParameter.LINEAR 
				&& kernelType != SvmParameter.POLY 
				&& kernelType != SvmParameter.RBF
				&& kernelType != SvmParameter.SIGMOID 
				&& kernelType != SvmParameter.PRECOMPUTED) {
			manager.getValidationMessage().append("ERROR: unknown kernel type\n");
		} else {
			manager.getValidationMessage().append( "kernel type: " + kernelType + "\n");
		}
	}

	public Checker checkParameter(SvmParameter params) {
		checkKernelType(params.kernel_type);
		return manager.runCheckAndGetResponse("Gamma", manager, params);
	}
}
