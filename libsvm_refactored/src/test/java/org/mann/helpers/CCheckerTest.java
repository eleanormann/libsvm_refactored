package org.mann.helpers;

import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertThat;

import org.junit.Test;
import org.mann.helpers.CChecker;
import org.mann.helpers.ParameterValidationManager;
import org.mann.libsvm.SvmParameter;

public class CCheckerTest {

	@Test
	public void checkCShouldReturnErrorWhenEqualToOrLessThanZero(){
		ParameterValidationManager manager = new ParameterValidationManager(new StringBuilder()); 
		new CChecker(manager).checkC(0);
		assertThat(manager.getValidationMessage().toString(), equalTo("ERROR: C <= 0\n"));
	}

	@Test
	public void chcekParmeterShouldReturnErrorWhenEqualToOrLessThanZero(){
		ParameterValidationManager manager = new ParameterValidationManager(new StringBuilder()); 
		new CChecker(manager).checkParameter(createSvmParameter());
		assertThat(manager.getValidationMessage().toString(), containsString("C = 1.0\n"));
	}
	
	private SvmParameter createSvmParameter() {
		SvmParameter params = new SvmParameter();
		params.C = 1;
		return params;
	}
}
