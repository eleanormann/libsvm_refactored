package org.mann.validation.svmparameter;

import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertThat;

import org.junit.Test;
import org.mann.libsvm.SvmParameter;
import org.mann.libsvm.SvmParameter.SvmType;
import org.mann.validation.svmparameter.ProbabilityChecker;

public class ProbabilityCheckerTest {
	
	@Test
	public void checkProbabilityShouldAddErrorMessageWhenNeitherZeroNorOne(){
		ParameterValidationManager manager = new ParameterValidationManager(new StringBuilder()); 
		new ProbabilityChecker(manager).checkProbability(-1, SvmType.c_svc);
		assertThat(manager.getValidationMessage().toString(), equalTo("ERROR: Probability is neither 0 nor 1\n"));
	}
	
	@Test
	public void checkProbabilityShouldAddErrorMessageWhenOneAndOneClass(){
		ParameterValidationManager manager = new ParameterValidationManager(new StringBuilder()); 
		new ProbabilityChecker(manager).checkProbability(1, SvmType.one_class);
		assertThat(manager.getValidationMessage().toString(), equalTo("ERROR: one-class SVM probability output not supported yet"));
	}
	
	@Test
	public void checkProbabilityShouldAddInfoMessageWhenZeroAndOneClass(){
		ParameterValidationManager manager = new ParameterValidationManager(new StringBuilder()); 
		new ProbabilityChecker(manager).checkProbability(0, SvmType.one_class);
		assertThat(manager.getValidationMessage().toString(), equalTo("Probability = 0\n"));
	}

	@Test
	public void checkParameterShouldAddInfoMessageWhenZeroAndOneClass(){
		ParameterValidationManager manager = new ParameterValidationManager(new StringBuilder()); 
		new ProbabilityChecker(manager).checkParameter(createSvmParameter(0, SvmType.one_class));
		assertThat(manager.getValidationMessage().toString(), containsString("Probability = 0\n"));
	}

	private SvmParameter createSvmParameter(int probability, SvmType svmType) {
		SvmParameter params = new SvmParameter();
		params.svmType = svmType;
		params.probability = probability;
		return params;
	}
}
