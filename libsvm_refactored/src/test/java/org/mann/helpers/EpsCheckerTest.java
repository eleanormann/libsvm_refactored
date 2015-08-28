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
	public void checkEpsShouldReturnErrorWhenEqualToOrLessThanZero(){
		ParameterValidationManager manager = new ParameterValidationManager(new StringBuilder()); 
		new EpsChecker(manager).checkEps(0);
		assertThat(manager.getValidationMessage().toString(), equalTo("ERROR: eps <= 0\n"));
	}
	
	@Test
	public void checkParameterShouldReturnErrorWhenEqualToOrLessThanZero(){
		ParameterValidationManager manager = new ParameterValidationManager(new StringBuilder()); 
		new EpsChecker(manager).checkParameter(createSvmParameter());
		assertThat(manager.getValidationMessage().toString(), containsString("Eps = 1.0\n"));
	}

	private SvmParameter createSvmParameter() {
		SvmParameter params = new SvmParameter();
		params.eps = 1;
		return params;
	}

}
