package helpers;


public class CacheSizeChecker {

	private ParameterValidationManager manager;

	public CacheSizeChecker(ParameterValidationManager parameterValidationManager) {
		this.manager = parameterValidationManager;
	}

	public CacheSizeChecker() {
		// TODO Auto-generated constructor stub
	}

	public String checkCacheSize(double cache_size) {
		if (cache_size <= 0) {
			return "ERROR: cache size <= 0\n";
		} else {
			return "Cache size: " + cache_size + "\n";
		}
	}
}
