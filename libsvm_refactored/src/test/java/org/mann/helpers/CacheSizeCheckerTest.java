package org.mann.helpers;

import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertThat;

import org.junit.Test;
import org.mann.helpers.CacheSizeChecker;
import org.mann.helpers.ParameterValidationManager;
import org.mann.libsvm.SvmParameter;

public class CacheSizeCheckerTest {

	@Test
	public void checkCacheSizeShouldReturnErrorWhenEqualToOrLessThanZero(){
		ParameterValidationManager manager = new ParameterValidationManager(new StringBuilder()); 
		new CacheSizeChecker(manager).checkCacheSize(0);
		assertThat(manager.getValidationMessage().toString(), equalTo("ERROR: cache size <= 0\n"));
	}

	@Test
	public void checkCacheSizeShouldReturnCacheSizeWhenValid(){
		ParameterValidationManager manager = new ParameterValidationManager(new StringBuilder()); 
		new CacheSizeChecker(manager).checkCacheSize(1);
		assertThat(manager.getValidationMessage().toString(), equalTo("Cache size: 1.0\n"));
	}
	
	@Test
	public void checkParameterShouldReturnCacheSizeWhenValid(){
		ParameterValidationManager manager = new ParameterValidationManager(new StringBuilder()); 
		new CacheSizeChecker(manager).checkParameter(createSvmParameter());
		assertThat(manager.getValidationMessage().toString(), containsString("Cache size: 1.0\n"));
	}
	
	@Test
	public void checkParameterShouldReturnCacheSizeWhenValid(){
		ParameterValidationManager manager = new ParameterValidationManager(new StringBuilder()); 
		new CacheSizeChecker(manager).checkParameter(createSvmParameter());
		assertThat(manager.getValidationMessage().toString(), containsString("Cache size: 1.0\n"));
	}
	
	private SvmParameter createSvmParameter() {
		SvmParameter params = new SvmParameter();
		params.cache_size = 1;
		return params;
	}
}
