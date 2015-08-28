package org.mann.helpers;

import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertThat;

import org.junit.Test;
import org.mann.helpers.GammaChecker;
import org.mann.helpers.ParameterValidationManager;
import org.mann.libsvm.SvmParameter;

public class GammaCheckerTest {
	

	@Test
	public void checkGammaShouldReturnErrorWhenLessThanZero(){
		ParameterValidationManager manager = new ParameterValidationManager(new StringBuilder()); 
		new GammaChecker(manager).checkGamma(-1);
		assertThat(manager.getValidationMessage().toString(), equalTo("ERROR: gamma less than zero\n"));
	}

	@Test
	public void checkParameterShouldReturnErrorWhenLessThanZero(){
		ParameterValidationManager manager = new ParameterValidationManager(new StringBuilder()); 
		new GammaChecker(manager).checkParameter(createSvmParameter());
		assertThat(manager.getValidationMessage().toString(), containsString("Gamma = 1.0\n"));
	}
	

	private SvmParameter createSvmParameter() {
		SvmParameter params = new SvmParameter();
		params.gamma = 1;
		return params;
	}
}
