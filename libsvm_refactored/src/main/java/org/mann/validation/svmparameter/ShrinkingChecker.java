package org.mann.validation.svmparameter;

import org.mann.libsvm.SvmParameter;
import org.mann.validation.Checker;

public class ShrinkingChecker implements Checker {

	private ParameterValidationManager manager;

	public ShrinkingChecker(ParameterValidationManager parameterValidationManager) {
		this.manager = parameterValidationManager;
	}

	public void checkShrinking(int shrinking) {
		if (shrinking != 0 && shrinking != 1){
			manager.getValidationMessage().append("ERROR: shrinking is neither 0 nor 1\n");
		}else{
			manager.getValidationMessage().append("Shrinking = ").append(shrinking).append("\n");
		}
	}

	public Checker checkParameter(SvmParameter params) {
		checkShrinking(params.getShrinking());
		return manager.runCheckAndGetResponse("Probability", params);
	}

}
