package org.mann.helpers;

import org.mann.libsvm.SvmParameter;
import org.mann.libsvm.SvmParameter.SvmType;

public class ProbabilityChecker implements Checker {

	private ParameterValidationManager manager;

	public ProbabilityChecker(ParameterValidationManager parameterValidationManager) {
		this.manager = parameterValidationManager;
	}

	public void checkProbability(int probability, SvmType svmType) {
		if (probability != 0 && probability != 1){
			manager.getValidationMessage().append( "ERROR: Probability is neither 0 nor 1\n");	
		}else if (probability == 1 && svmType == SvmType.ONE_CLASS){
			manager.getValidationMessage().append( "ERROR: one-class SVM probability output not supported yet");
		}else{
			manager.getValidationMessage().append( "Probability = " + probability + "\n");			
		}
	}

	public Checker checkParameter(SvmParameter params) {
		checkProbability(params.probability, params.svmType);
		return null;
	}

}
