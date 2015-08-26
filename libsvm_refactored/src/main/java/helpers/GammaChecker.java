package helpers;

import static org.junit.Assert.*;
import libsvm.svm;

import org.junit.Test;

public class GammaChecker {
private ParameterValidationManager manager;
	
	public GammaChecker(){};
	
	public GammaChecker(ParameterValidationManager parameterValidationManager) {
		this.manager = parameterValidationManager;
	}
	
	public String checkGamma(double gamma) {
		if(gamma < 0) {
			return "ERROR: gamma less than zero\n";
		}
		return "Gamma = " + gamma + "\n";
	}

}
