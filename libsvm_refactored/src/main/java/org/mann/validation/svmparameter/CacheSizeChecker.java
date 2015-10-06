package org.mann.validation.svmparameter;

import org.mann.libsvm.SvmParameter;
import org.mann.validation.Checker;


public class CacheSizeChecker implements Checker {

	private ParameterValidationManager manager;

	public CacheSizeChecker(ParameterValidationManager parameterValidationManager) {
		this.manager = parameterValidationManager;
	}
	
	public void checkCacheSize(double cache_size) {
		if (cache_size <= 0) {
			manager.getValidationMessage().append("ERROR: cache size <= 0\n");
		} else {
			manager.getValidationMessage().append("Cache size: ").append(cache_size).append("\n");
		}
	}

	public Checker checkParameter(SvmParameter params) {
		checkCacheSize(params.getCache_size());
		return manager.runCheckAndGetResponse("Eps", params);
	}
}
