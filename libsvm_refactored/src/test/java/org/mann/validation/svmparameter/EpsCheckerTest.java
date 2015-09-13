package org.mann.validation.svmparameter;

import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertThat;

import org.junit.Test;
import org.mann.libsvm.SvmParameter;

public class EpsCheckerTest {

	@Test
	public void checkEpsShouldAddErrorMessageWhenEpsIsZero(){
		ParameterValidationManager manager = new ParameterValidationManager(new StringBuilder()); 
		new EpsilonToleranceChecker(manager).checkEps(0);
		assertThat(manager.getValidationMessage().toString(), equalTo("ERROR: Epsilon (tolerance) <= 0\n"));
	}
	
	@Test
	public void checkEpsShouldAddErrorMessageWhenEpsIsLessThanZero(){
		ParameterValidationManager manager = new ParameterValidationManager(new StringBuilder()); 
		new EpsilonToleranceChecker(manager).checkEps(-1);
		assertThat(manager.getValidationMessage().toString(), equalTo("ERROR: Epsilon (tolerance) <= 0\n"));
	}
	
	@Test
	public void checkEpsShouldAddInfoMessageWhenEpsIsMoreThanZero(){
		ParameterValidationManager manager = new ParameterValidationManager(new StringBuilder()); 
		new EpsilonToleranceChecker(manager).checkEps(1);
		assertThat(manager.getValidationMessage().toString(), equalTo("Epsilon (tolerance) = 1.0\n"));
	}
	
	@Test
	public void checkParameterShouldAddErrorMessageWhenEpsIsZero(){
		ParameterValidationManager manager = new ParameterValidationManager(new StringBuilder()); 
		new EpsilonToleranceChecker(manager).checkParameter(createSvmParameter(0));
		assertThat(manager.getValidationMessage().toString(), containsString("ERROR: Epsilon (tolerance) <= 0\n"));
	}

	@Test
	public void checkParameterShouldAddErrorMessageWhenEpsIsLessThanZero(){
		ParameterValidationManager manager = new ParameterValidationManager(new StringBuilder()); 
		new EpsilonToleranceChecker(manager).checkParameter(createSvmParameter(-1));
		assertThat(manager.getValidationMessage().toString(), containsString("ERROR: Epsilon (tolerance) <= 0\n"));
	}
	
	@Test
	public void checkParameterShouldAddErrorMessageWhenEpsIsMoreThanZero(){
		ParameterValidationManager manager = new ParameterValidationManager(new StringBuilder()); 
		new EpsilonToleranceChecker(manager).checkParameter(createSvmParameter(1));
		assertThat(manager.getValidationMessage().toString(), containsString("Epsilon (tolerance) = 1.0\n"));
	}
	
	private SvmParameter createSvmParameter(double eps) {
		SvmParameter params = new SvmParameter();
		params.epsilonTolerance = eps;
		return params;
	}

}
