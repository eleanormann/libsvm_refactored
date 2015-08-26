package helpers;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertThat;
import libsvm.SvmParameter.SvmType;

import org.junit.Test;

public class ProbabilityCheckerTest {

	@Test
	public void checkProbabilityShouldReturnErrorWhenNeitherZeroNorOne(){
		String errorMessage = new ProbabilityChecker(new ParameterValidationManager(new StringBuilder())).checkProbability(-1, SvmType.C_SVC);
		assertThat(errorMessage, equalTo("ERROR: Probability is neither 0 nor 1\n"));
	}
	
	@Test
	public void checkProbabilityShouldReturnErrorWhenOneAndOneClass(){
		String errorMessage = new ProbabilityChecker(new ParameterValidationManager(new StringBuilder())).checkProbability(1, SvmType.ONE_CLASS);
		assertThat(errorMessage, equalTo("ERROR: one-class SVM probability output not supported yet"));
	}
	
	@Test
	public void checkProbabilityShouldNotReturnErrorWhenZeroAndOneClass(){
		String errorMessage = new ProbabilityChecker(new ParameterValidationManager(new StringBuilder())).checkProbability(0, SvmType.ONE_CLASS);
		assertThat(errorMessage, equalTo("Probability = 0\n"));
	}

}
