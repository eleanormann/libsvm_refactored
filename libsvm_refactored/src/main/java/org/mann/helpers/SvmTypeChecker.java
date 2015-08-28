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

	// TODO: decide whether this int needs to be changed to SvmType earlier in the workflow
	// Comment expires 28th September 2015
	public void checkSvmType(int svm_type) {
		try{
			SvmType svmType = new SvmParameter().getSvmTypeFromSvmParameter(svm_type);
			manager.getValidationMessage().append("Svm Type: " + svmType);
		}catch(IllegalArgumentException e){
			manager.getValidationMessage().append("ERROR: Not yet implemented\n");
			e.printStackTrace();
		}
	}

	public Checker checkParameter(SvmParameter params) {
		checkSvmType(-1);
		return manager.runCheckAndGetResponse("Kernel", manager, params);
	}

	protected ParameterValidationManager getManager() {
		return manager;
	}
}
