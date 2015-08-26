package helpers;

import libsvm.svm;

public class ShrinkingChecker {

	private ParameterValidationManager manager;

	public ShrinkingChecker(ParameterValidationManager parameterValidationManager) {
		this.manager = parameterValidationManager;
	}

	public ShrinkingChecker() {
		// TODO Auto-generated constructor stub
	}

	public String checkShrinking(int shrinking) {
		if (shrinking != 0 && shrinking != 1){
			return "ERROR: shrinking is neither 0 nor 1\n";
		}else{
			return "Shrinking = " + shrinking + "\n";
		}
	}

}
