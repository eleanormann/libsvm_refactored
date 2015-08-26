package helpers;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertThat;

import org.junit.Test;

public class KernelCheckerTest {

	@Test
	public void checkKernelTypeShouldReturnErrorWhenNotRecognised(){
		String errorMessage = new KernelChecker(new ParameterValidationManager(new StringBuilder())).checkKernelType(-1);
		assertThat(errorMessage, equalTo("ERROR: unknown kernel type\n"));
	}

}
