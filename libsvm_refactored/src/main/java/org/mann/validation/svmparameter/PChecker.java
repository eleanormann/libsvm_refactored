package org.mann.validation.svmparameter;

import org.mann.libsvm.SvmParameter;
import org.mann.libsvm.SvmParameter.SvmType;
import org.mann.validation.Checker;

public class PChecker implements Checker {

	private ParameterValidationManager manager;

	public PChecker(ParameterValidationManager parameterValidationManager) {
		this.manager = parameterValidationManager;
	}

	public void checkP(double p) {
		if (p < 0){
			manager.getValidationMessage().append("ERROR: p < 0\n");			
		}else{
			manager.getValidationMessage().append("p = " + p + "\n");
		}
	}

	public Checker checkParameter(SvmParameter params) {
		if(params.svmType == SvmType.epsilon_svr){
			checkP(params.p);			
		}
		return manager.runCheckAndGetResponse("Shrinking", manager, params);
	}

}
