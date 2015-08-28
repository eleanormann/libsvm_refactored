package org.mann.helpers;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.containsString;
import static org.junit.Assert.assertThat;

import org.junit.Test;
import org.mann.helpers.EpsChecker;
import org.mann.helpers.ParameterValidationManager;
import org.mann.libsvm.SvmParameter;
import org.mann.libsvm.SvmParameter.SvmType;

public class EpsCheckerTest {

	@Test
	public void checkEpsShouldAddErrorMessageWhenEpsIsZero(){
		ParameterValidationManager manager = new ParameterValidationManager(new StringBuilder()); 
		new EpsChecker(manager).checkEps(0);
		assertThat(manager.getValidationMessage().toString(), equalTo("ERROR: eps <= 0\n"));
	}
	
	@Test
	public void checkEpsShouldAddErrorMessageWhenEpsIsLessThanZero(){
		ParameterValidationManager manager = new ParameterValidationManager(new StringBuilder()); 
		new EpsChecker(manager).checkEps(-1);
		assertThat(manager.getValidationMessage().toString(), equalTo("ERROR: eps <= 0\n"));
	}
	
	@Test
	public void checkEpsShouldAddInfoMessageWhenEpsIsMoreThanZero(){
		ParameterValidationManager manager = new ParameterValidationManager(new StringBuilder()); 
		new EpsChecker(manager).checkEps(1);
		assertThat(manager.getValidationMessage().toString(), equalTo("Eps = 1.0\n"));
	}
	
	@Test
	public void checkParameterShouldAddErrorMessageWhenEpsIsZero(){
		ParameterValidationManager manager = new ParameterValidationManager(new StringBuilder()); 
		new EpsChecker(manager).checkParameter(createSvmParameter(0));
		assertThat(manager.getValidationMessage().toString(), containsString("ERROR: eps <= 0\n"));
	}

	@Test
	public void checkParameterShouldAddErrorMessageWhenEpsIsLessThanZero(){
		ParameterValidationManager manager = new ParameterValidationManager(new StringBuilder()); 
		new EpsChecker(manager).checkParameter(createSvmParameter(-1));
		assertThat(manager.getValidationMessage().toString(), containsString("ERROR: eps <= 0\n"));
	}
	
	@Test
	public void checkParameterShouldAddErrorMessageWhenEpsIsMoreThanZero(){
		ParameterValidationManager manager = new ParameterValidationManager(new StringBuilder()); 
		new EpsChecker(manager).checkParameter(createSvmParameter(1));
		assertThat(manager.getValidationMessage().toString(), containsString("Eps = 1.0\n"));
	}
	
	private SvmParameter createSvmParameter(double eps) {
		SvmParameter params = new SvmParameter();
		params.eps = eps;
		return params;
	}

}
