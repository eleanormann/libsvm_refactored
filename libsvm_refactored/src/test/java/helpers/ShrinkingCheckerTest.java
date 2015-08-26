package helpers;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertThat;

import org.junit.Test;

public class ShrinkingCheckerTest {

	@Test
	public void checkShrinkingShouldReturnErrorWhenNeitherZeroNorOne(){
		String errorMessage = new ShrinkingChecker(new ParameterValidationManager(new StringBuilder())).checkShrinking(-1);
		assertThat(errorMessage, equalTo("ERROR: shrinking is neither 0 nor 1\n"));
	}

}
