package org.mann.helpers;

import org.mann.libsvm.SvmParameter;
import org.mann.libsvm.svm;

public class ShrinkingChecker implements Checker {

	private ParameterValidationManager manager;

	public ShrinkingChecker(ParameterValidationManager parameterValidationManager) {
		this.manager = parameterValidationManager;
	}

	public ShrinkingChecker() {
		// TODO Auto-generated constructor stub
	}

	public void checkShrinking(int shrinking) {
		if (shrinking != 0 && shrinking != 1){
			manager.getValidationMessage().append( "ERROR: shrinking is neither 0 nor 1\n");
		}else{
			manager.getValidationMessage().append("Shrinking = " + shrinking + "\n");
		}
	}

	public Checker checkParameter(SvmParameter params) {
		checkShrinking(params.shrinking);
		return manager.runCheckAndGetResponse("Probability", manager, params);
	}

}
