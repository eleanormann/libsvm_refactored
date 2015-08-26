package helpers;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertThat;

import org.junit.Test;

public class EpsCheckerTest {

	@Test
	public void checkEpsShouldReturnErrorWhenEqualToOrLessThanZero(){
		String errorMessage = new EpsChecker(new ParameterValidationManager(new StringBuilder())).checkEps(0);
		assertThat(errorMessage, equalTo("ERROR: eps <= 0\n"));
	}

}
