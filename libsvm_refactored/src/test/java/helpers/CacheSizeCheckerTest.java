package helpers;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertThat;

import org.junit.Test;

public class CacheSizeCheckerTest {

	@Test
	public void checkCacheSizeShouldReturnErrorWhenEqualToOrLessThanZero(){
		String errorMessage = new CacheSizeChecker(new ParameterValidationManager(new StringBuilder())).checkCacheSize(0);
		assertThat(errorMessage, equalTo("ERROR: cache size <= 0\n"));
	}

}
