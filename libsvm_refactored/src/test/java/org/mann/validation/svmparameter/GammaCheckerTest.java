package org.mann.validation.svmparameter;

import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertThat;

import org.junit.Test;
import org.mann.libsvm.SvmParameter;
import org.mann.validation.svmparameter.GammaChecker;

public class GammaCheckerTest {

	@Test
	public void checkGammaShouldAddErrorMessageWhenGammaLessThanZero(){
		ParameterValidationManager manager = new ParameterValidationManager(new StringBuilder()); 
		new GammaChecker(manager).checkGamma(-1);
		assertThat(manager.getValidationMessage().toString(), equalTo("ERROR: gamma less than zero\n"));
	}

	@Test
	public void checkParameterShouldReturnErrorWhenLessThanZero(){
		ParameterValidationManager manager = new ParameterValidationManager(new StringBuilder()); 
		new GammaChecker(manager).checkParameter(createSvmParameter(-1));
		assertThat(manager.getValidationMessage().toString(), containsString("ERROR: gamma less than zero\n"));
	}
	
	@Test
	public void checkGammaShouldAddInfoMessageWhenGammaZeroOrMore(){
		ParameterValidationManager manager = new ParameterValidationManager(new StringBuilder()); 
		new GammaChecker(manager).checkGamma(1);
		assertThat(manager.getValidationMessage().toString(), equalTo("Gamma = 1.0\n"));
	}

	@Test
	public void checkParameterShouldAddInforMassageWhenGammaZeroOrMore(){
		ParameterValidationManager manager = new ParameterValidationManager(new StringBuilder()); 
		new GammaChecker(manager).checkParameter(createSvmParameter(1));
		assertThat(manager.getValidationMessage().toString(), containsString("Gamma = 1.0\n"));
	}
	
	private SvmParameter createSvmParameter(double gamma) {
		SvmParameter params = new SvmParameter();
		params.setGamma(gamma);
		return params;
	}
}
