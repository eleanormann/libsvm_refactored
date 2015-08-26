package helpers;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertThat;

import org.junit.Test;

public class PCheckerTest {

	@Test
	public void checkPShouldReturnErrorWhenLessThanZero(){
		String errorMessage = new PChecker(new ParameterValidationManager(new StringBuilder())).checkP(-1);
		assertThat(errorMessage, equalTo("ERROR: p < 0\n"));
	}

}
