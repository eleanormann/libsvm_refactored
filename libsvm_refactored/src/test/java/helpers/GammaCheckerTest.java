package helpers;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertThat;

import org.junit.Test;

public class GammaCheckerTest {
	

	@Test
	public void checkGammaShouldReturnErrorWhenLessThanZero(){
		String errorMessage = new GammaChecker(new ParameterValidationManager(new StringBuilder())).checkGamma(-1);
		assertThat(errorMessage, equalTo("ERROR: gamma less than zero\n"));
	}

}
