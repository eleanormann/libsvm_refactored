package org.mann.helpers;

import org.mann.libsvm.SvmParameter;


public class CacheSizeChecker implements Checker {

	private ParameterValidationManager manager;

	public CacheSizeChecker(ParameterValidationManager parameterValidationManager) {
		this.manager = parameterValidationManager;
	}

	public CacheSizeChecker() {
		// TODO Auto-generated constructor stub
	}

	public void checkCacheSize(double cache_size) {
		if (cache_size <= 0) {
			manager.getValidationMessage().append("ERROR: cache size <= 0\n");
		} else {
			manager.getValidationMessage().append("Cache size: " + cache_size + "\n");
		}
	}

	public Checker checkParameter(SvmParameter params) {
		checkCacheSize(params.cache_size);
		return manager.runCheckAndGetResponse("Eps", manager, params);
	}
}
