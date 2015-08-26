package helpers;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertThat;

import org.junit.Test;

public class DegreeCheckerTest {

	@Test
	public void checkDegreeShouldReturnErrorWhenLessThanZero(){
		String errorMessage = new DegreeChecker(new ParameterValidationManager(new StringBuilder())).checkDegreeOfPolynomialKernel(-1);
		assertThat(errorMessage, equalTo("ERROR: degree of polynomial kernel < 0\n"));
	}

}
