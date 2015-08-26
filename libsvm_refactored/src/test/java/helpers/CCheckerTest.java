package helpers;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertThat;

import org.junit.Test;

public class CCheckerTest {

	@Test
	public void checkCShouldReturnErrorWhenEqualToOrLessThanZero(){
		String errorMessage = new CChecker(new ParameterValidationManager(new StringBuilder())).checkC(0);
		assertThat(errorMessage, equalTo("ERROR: C <= 0\n"));
	}

}
