package org.mann.validation.svmparameter;

import static org.junit.Assert.*;

import org.junit.Test;
import org.mann.helpers.Checker;
import org.mann.libsvm.SvmParameter;
import org.mann.libsvm.svm;

public class GammaChecker implements Checker {
private ParameterValidationManager manager;
	
	public GammaChecker(){};
	
	public GammaChecker(ParameterValidationManager parameterValidationManager) {
		this.manager = parameterValidationManager;
	}
	
	public void checkGamma(double gamma) {
		StringBuilder validationMessage = manager.getValidationMessage();
		if(gamma < 0) {
			validationMessage.append( "ERROR: gamma less than zero\n");
		}else{
			validationMessage.append(  "Gamma = " + gamma + "\n");			
		}
	}

	public Checker checkParameter(SvmParameter params) {
		checkGamma(params.gamma);
		return manager.runCheckAndGetResponse("Degree", manager, params);
	}

}
