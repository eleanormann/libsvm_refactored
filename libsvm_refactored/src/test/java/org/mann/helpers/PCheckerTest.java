package org.mann.helpers;

import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertThat;

import org.junit.Test;
import org.mann.helpers.PChecker;
import org.mann.helpers.ParameterValidationManager;
import org.mann.libsvm.SvmParameter;

public class PCheckerTest {

	@Test
	public void checkPShouldAddErrorMessageWhenPLessThanZero(){
		ParameterValidationManager manager = new ParameterValidationManager(new StringBuilder()); 
		new PChecker(manager).checkP(-1);
		assertThat(manager.getValidationMessage().toString(), equalTo("ERROR: p < 0\n"));
	}

	@Test
	public void checkParameterShouldAddInfoMessageWhenPGreaterThanZero(){
		ParameterValidationManager manager = new ParameterValidationManager(new StringBuilder()); 
		new PChecker(manager).checkParameter(createSvmParameter(1));
		assertThat(manager.getValidationMessage().toString(), containsString("p = 1.0\n"));
	}

	@Test
	public void checkPShouldAddInfoMessageWhenPGreaterThanZero(){
		ParameterValidationManager manager = new ParameterValidationManager(new StringBuilder()); 
		new PChecker(manager).checkP(1);
		assertThat(manager.getValidationMessage().toString(), equalTo("p = 1.0\n"));
	}
	
	@Test
	public void checkParameterShouldAddErrorMessageWhenPLessThanZero(){
		ParameterValidationManager manager = new ParameterValidationManager(new StringBuilder()); 
		new PChecker(manager).checkParameter(createSvmParameter(-1));
		assertThat(manager.getValidationMessage().toString(), containsString("ERROR: p < 0\n"));
	}
	
	private SvmParameter createSvmParameter(int value) {
		SvmParameter params = new SvmParameter();
		params.p = value;
		return params;
	}
}
