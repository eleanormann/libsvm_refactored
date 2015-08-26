package helpers;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertThat;

import org.junit.Test;

public class SvmTypeCheckerTest {
	
	@Test
	public void checkSvmTypeShouldReturnErrorWhenNotRecognised(){
		SvmTypeChecker checker = new SvmTypeChecker(new ParameterValidationManager(new StringBuilder()));
		String errorMessage = checker.checkSvmType(-1);
		assertThat(errorMessage, equalTo("ERROR: unknown svm type\n"));
	}
	
	
	
}
