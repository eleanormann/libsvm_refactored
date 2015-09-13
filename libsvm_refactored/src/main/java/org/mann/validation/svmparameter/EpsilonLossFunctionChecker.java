package org.mann.validation.svmparameter;

import org.mann.libsvm.SvmParameter;
import org.mann.libsvm.SvmParameter.SvmType;
import org.mann.validation.Checker;

public class EpsilonLossFunctionChecker implements Checker {

	private ParameterValidationManager manager;

	public EpsilonLossFunctionChecker(ParameterValidationManager parameterValidationManager) {
		this.manager = parameterValidationManager;
	}

	public void checkEpsilonLossFunction(double epsilonLossFunct) {
		if (epsilonLossFunct < 0){
			manager.getValidationMessage().append("ERROR: Epsilon (loss function) < 0\n");			
		}else{
			manager.getValidationMessage().append("Epsilon (Loss Function) = " + epsilonLossFunct + "\n");
		}
	}

	public Checker checkParameter(SvmParameter params) {
		if(params.svmType == SvmType.epsilon_svr){
			checkEpsilonLossFunction(params.epsilonLossFunction);			
		}
		return manager.runCheckAndGetResponse("Shrinking", manager, params);
	}

}
