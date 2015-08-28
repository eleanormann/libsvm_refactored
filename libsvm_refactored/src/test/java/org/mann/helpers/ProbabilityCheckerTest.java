package org.mann.helpers;

import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertThat;

import org.junit.Test;
import org.mann.helpers.ParameterValidationManager;
import org.mann.helpers.ProbabilityChecker;
import org.mann.libsvm.SvmParameter;
import org.mann.libsvm.SvmParameter.SvmType;

public class ProbabilityCheckerTest {
	
	@Test
	public void checkProbabilityShouldReturnErrorWhenNeitherZeroNorOne(){
		ParameterValidationManager manager = new ParameterValidationManager(new StringBuilder()); 
		new ProbabilityChecker(manager).checkProbability(-1, SvmType.C_SVC);
		assertThat(manager.getValidationMessage().toString(), equalTo("ERROR: Probability is neither 0 nor 1\n"));
	}
	
	@Test
	public void checkProbabilityShouldReturnErrorWhenOneAndOneClass(){
		ParameterValidationManager manager = new ParameterValidationManager(new StringBuilder()); 
		new ProbabilityChecker(manager).checkProbability(1, SvmType.ONE_CLASS);
		assertThat(manager.getValidationMessage().toString(), equalTo("ERROR: one-class SVM probability output not supported yet"));
	}
	
	@Test
	public void checkProbabilityShouldNotReturnErrorWhenZeroAndOneClass(){
		ParameterValidationManager manager = new ParameterValidationManager(new StringBuilder()); 
		new ProbabilityChecker(manager).checkProbability(0, SvmType.ONE_CLASS);
		assertThat(manager.getValidationMessage().toString(), equalTo("Probability = 0\n"));
	}

	@Test
	public void checkParameterShouldNotReturnErrorWhenZeroAndOneClass(){
		ParameterValidationManager manager = new ParameterValidationManager(new StringBuilder()); 
		new ProbabilityChecker(manager).checkParameter(createSvmParameter(0, SvmType.ONE_CLASS));
		assertThat(manager.getValidationMessage().toString(), containsString("Probability = 0\n"));
	}

	private SvmParameter createSvmParameter(int probability, SvmType svmType) {
		SvmParameter params = new SvmParameter();
		params.svmType = svmType;
		params.probability = probability;
		return params;
	}
}
