package org.mann.validation.svmparameter;

import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertThat;

import org.junit.Test;
import org.mann.libsvm.SvmParameter;
import org.mann.libsvm.SvmParameter.SvmType;

public class EpsilonLossFunctionCheckerTest {

	@Test
	public void checkPShouldAddErrorMessageWhenPLessThanZero(){
		ParameterValidationManager manager = new ParameterValidationManager(new StringBuilder()); 
		new EpsilonLossFunctionChecker(manager).checkEpsilonLossFunction(-1);
		assertThat(manager.getValidationMessage().toString(), equalTo("ERROR: Epsilon (loss function) < 0\n"));
	}

	@Test
	public void checkPShouldAddInfoMessageWhenPGreaterThanZero(){
		ParameterValidationManager manager = new ParameterValidationManager(new StringBuilder()); 
		new EpsilonLossFunctionChecker(manager).checkEpsilonLossFunction(1);
		assertThat(manager.getValidationMessage().toString(), equalTo("Epsilon (Loss Function) = 1.0\n"));
	}
	
	@Test
	public void checkParameterShouldAddInfoMessageWhenPGreaterThanZeroAndSvmTypeIsEpsilon(){
		ParameterValidationManager manager = new ParameterValidationManager(new StringBuilder()); 
		new EpsilonLossFunctionChecker(manager).checkParameter(createSvmParameter(1, SvmType.epsilon_svr));
		assertThat(manager.getValidationMessage().toString(), containsString("Epsilon (Loss Function) = 1.0\n"));
	}

	@Test
	public void checkParameterShouldAddErrorMessageWhenPLessThanZeroAndSvmTypeIsEpsilon(){
		ParameterValidationManager manager = new ParameterValidationManager(new StringBuilder()); 
		new EpsilonLossFunctionChecker(manager).checkParameter(createSvmParameter(-1, SvmType.epsilon_svr));
		assertThat(manager.getValidationMessage().toString(), containsString("ERROR: Epsilon (loss function) < 0\n"));
	}
	
	@Test
	public void checkParameterShouldNotAddMessagesWhenSvmTypeIsNotEpsilon(){
		ParameterValidationManager manager = new ParameterValidationManager(new StringBuilder()); 
		new EpsilonLossFunctionChecker(manager).checkParameter(createSvmParameter(1, SvmType.nu_svr));
		assertThat(manager.getValidationMessage().toString(), not(containsString("ERROR: Epsilon (loss function) < 0\n")));
		assertThat(manager.getValidationMessage().toString(), not(containsString("Epsilon (Loss Function) = 1\n")));
	}
	
	@Test
	public void checkParameterShouldAddErrorMessageWhenPLessThanZeroAndSvmTypeIsNotEpsilon(){
		ParameterValidationManager manager = new ParameterValidationManager(new StringBuilder()); 
		new EpsilonLossFunctionChecker(manager).checkParameter(createSvmParameter(-1, SvmType.nu_svc));
		assertThat(manager.getValidationMessage().toString(), not(containsString("ERROR: Epsilon (loss function) < 0\n")));
	}
	
	private SvmParameter createSvmParameter(int value, SvmType svmType) {
		SvmParameter params = new SvmParameter();
		params.epsilonLossFunction = value;
		params.svmType = svmType;
		return params;
	}
}
