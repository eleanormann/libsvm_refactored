package helpers;

import libsvm.SvmParameter;
import libsvm.svm;
public class KernelChecker {
	private ParameterValidationManager manager;
	
	public KernelChecker(){};
	
	public KernelChecker(ParameterValidationManager parameterValidationManager){
		this.manager = parameterValidationManager;
	}
	
	public String checkKernelType(int kernelType) {
		if (kernelType != SvmParameter.LINEAR 
				&& kernelType != SvmParameter.POLY 
				&& kernelType != SvmParameter.RBF
				&& kernelType != SvmParameter.SIGMOID 
				&& kernelType != SvmParameter.PRECOMPUTED) {
			return "ERROR: unknown kernel type\n";
		} else {
			return "kernel type: " + kernelType + "\n";
		}
	}
}
