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
	public void checkPShouldReturnErrorWhenLessThanZero(){
		ParameterValidationManager manager = new ParameterValidationManager(new StringBuilder()); 
		new PChecker(manager).checkP(-1);
		assertThat(manager.getValidationMessage().toString(), equalTo("ERROR: p < 0\n"));
	}

	@Test
	public void checkParameterShouldReturnErrorWhenLessThanZero(){
		ParameterValidationManager manager = new ParameterValidationManager(new StringBuilder()); 
		new PChecker(manager).checkParameter(createSvmParameter());
		assertThat(manager.getValidationMessage().toString(), containsString("p = 1.0\n"));
	}

	private SvmParameter createSvmParameter() {
		SvmParameter params = new SvmParameter();
		params.p = 1;
		return params;
	}
}
