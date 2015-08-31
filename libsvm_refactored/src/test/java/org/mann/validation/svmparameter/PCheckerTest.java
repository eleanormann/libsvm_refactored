package org.mann.validation.svmparameter;

import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertThat;

import org.junit.Test;
import org.mann.libsvm.SvmParameter;
import org.mann.libsvm.SvmParameter.SvmType;
import org.mann.validation.svmparameter.PChecker;

public class PCheckerTest {

	@Test
	public void checkPShouldAddErrorMessageWhenPLessThanZero(){
		ParameterValidationManager manager = new ParameterValidationManager(new StringBuilder()); 
		new PChecker(manager).checkP(-1);
		assertThat(manager.getValidationMessage().toString(), equalTo("ERROR: p < 0\n"));
	}

	@Test
	public void checkPShouldAddInfoMessageWhenPGreaterThanZero(){
		ParameterValidationManager manager = new ParameterValidationManager(new StringBuilder()); 
		new PChecker(manager).checkP(1);
		assertThat(manager.getValidationMessage().toString(), equalTo("p = 1.0\n"));
	}
	
	@Test
	public void checkParameterShouldAddInfoMessageWhenPGreaterThanZeroAndSvmTypeIsEpsilon(){
		ParameterValidationManager manager = new ParameterValidationManager(new StringBuilder()); 
		new PChecker(manager).checkParameter(createSvmParameter(1, SvmType.EPSILON_SVR));
		assertThat(manager.getValidationMessage().toString(), containsString("p = 1.0\n"));
	}

	@Test
	public void checkParameterShouldAddErrorMessageWhenPLessThanZeroAndSvmTypeIsEpsilon(){
		ParameterValidationManager manager = new ParameterValidationManager(new StringBuilder()); 
		new PChecker(manager).checkParameter(createSvmParameter(-1, SvmType.EPSILON_SVR));
		assertThat(manager.getValidationMessage().toString(), containsString("ERROR: p < 0\n"));
	}
	
	@Test
	public void checkParameterShouldNotAddMessagesWhenSvmTypeIsNotEpsilon(){
		ParameterValidationManager manager = new ParameterValidationManager(new StringBuilder()); 
		new PChecker(manager).checkParameter(createSvmParameter(1, SvmType.NU_SVR));
		assertThat(manager.getValidationMessage().toString(), not(containsString("ERROR: p < 0\n")));
		assertThat(manager.getValidationMessage().toString(), not(containsString("p = 1\n")));
	}
	
	@Test
	public void checkParameterShouldAddErrorMessageWhenPLessThanZeroAndSvmTypeIsNotEpsilon(){
		ParameterValidationManager manager = new ParameterValidationManager(new StringBuilder()); 
		new PChecker(manager).checkParameter(createSvmParameter(-1, SvmType.NU_SVC));
		assertThat(manager.getValidationMessage().toString(), not(containsString("ERROR: p < 0\n")));
	}
	
	private SvmParameter createSvmParameter(int value, SvmType svmType) {
		SvmParameter params = new SvmParameter();
		params.p = value;
		params.svmType = svmType;
		return params;
	}
}
