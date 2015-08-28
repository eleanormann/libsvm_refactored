package org.mann.helpers;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertThat;

import org.junit.Test;
import org.mann.helpers.ParameterValidationManager;
import org.mann.helpers.ShrinkingChecker;
import org.mann.libsvm.SvmParameter;
import org.mann.libsvm.SvmParameter.SvmType;

public class ShrinkingCheckerTest {

	@Test
	public void checkShrinkingShouldReturnErrorWhenNeitherZeroNorOne(){
		ParameterValidationManager manager = new ParameterValidationManager(new StringBuilder()); 
		new ShrinkingChecker(manager).checkShrinking(-1);
		assertThat(manager.getValidationMessage().toString(), equalTo("ERROR: shrinking is neither 0 nor 1\n"));
	}

	

	private SvmParameter createSvmParameter() {
		SvmParameter params = new SvmParameter();
		params.shrinking=1;
		return params;
	}
}
