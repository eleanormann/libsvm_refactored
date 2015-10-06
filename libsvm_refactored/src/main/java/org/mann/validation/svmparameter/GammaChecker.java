package org.mann.validation.svmparameter;

import org.mann.libsvm.SvmParameter;
import org.mann.validation.Checker;

public class GammaChecker implements Checker {
private ParameterValidationManager manager;

	public GammaChecker(ParameterValidationManager parameterValidationManager) {
		this.manager = parameterValidationManager;
	}
	
	public void checkGamma(double gamma) {
		StringBuilder validationMessage = manager.getValidationMessage();
		if(gamma < 0) {
			validationMessage.append( "ERROR: gamma less than zero\n");
		}else{
			validationMessage.append("Gamma = ").append(gamma).append("\n");			
		}
	}

	public Checker checkParameter(SvmParameter params) {
		checkGamma(params.getGamma());
		return manager.runCheckAndGetResponse("Degree", params);
	}

}
