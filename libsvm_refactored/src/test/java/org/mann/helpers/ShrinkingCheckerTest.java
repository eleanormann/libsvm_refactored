package org.mann.helpers;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.containsString;
import static org.junit.Assert.assertThat;

import org.junit.Test;
import org.mann.helpers.ParameterValidationManager;
import org.mann.helpers.ShrinkingChecker;
import org.mann.libsvm.SvmParameter;

public class ShrinkingCheckerTest {

	@Test
	public void checkShrinkingShouldReturnErrorWhenNeitherZeroNorOne(){
		ParameterValidationManager manager = new ParameterValidationManager(new StringBuilder()); 
		new ShrinkingChecker(manager).checkShrinking(-1);
		assertThat(manager.getValidationMessage().toString(), equalTo("ERROR: shrinking is neither 0 nor 1\n"));
	}

	@Test
	public void checkShrinkingShouldAddInfoMessageWhenShrinkingIsZero(){
		ParameterValidationManager manager = new ParameterValidationManager(new StringBuilder()); 
		new ShrinkingChecker(manager).checkShrinking(0);
		assertThat(manager.getValidationMessage().toString(), equalTo("Shrinking = 0\n"));
	}
	
	@Test
	public void checkShrinkingShouldAddInfoMessageWhenShrinkingIsOne(){
		ParameterValidationManager manager = new ParameterValidationManager(new StringBuilder()); 
		new ShrinkingChecker(manager).checkShrinking(1);
		assertThat(manager.getValidationMessage().toString(), equalTo("Shrinking = 1\n"));
	}

	@Test
	public void checkParameterShouldAddErrorMessageWhenShrinkingIsNeitherZeroNorOne(){
		ParameterValidationManager manager = new ParameterValidationManager(new StringBuilder()); 
		new ShrinkingChecker(manager).checkParameter(createSvmParameter(-1));
		assertThat(manager.getValidationMessage().toString(), containsString("ERROR: shrinking is neither 0 nor 1\n"));
	}
	
	@Test
	public void checkParameterShouldAddInfoMessageWhenShrinkingIsZero(){
		ParameterValidationManager manager = new ParameterValidationManager(new StringBuilder()); 
		new ShrinkingChecker(manager).checkParameter(createSvmParameter(0));
		assertThat(manager.getValidationMessage().toString(), containsString("Shrinking = 0\n"));
	}
	
	@Test
	public void checkParameterShouldAddInfoMessageWhenShrinkingIsOne(){
		ParameterValidationManager manager = new ParameterValidationManager(new StringBuilder()); 
		new ShrinkingChecker(manager).checkParameter(createSvmParameter(1));
		assertThat(manager.getValidationMessage().toString(), containsString("Shrinking = 1\n"));
	}
	
	private SvmParameter createSvmParameter(int value) {
		SvmParameter params = new SvmParameter();
		params.shrinking=value;
		return params;
	}
}
