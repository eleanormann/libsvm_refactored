package org.mann.validation.svmparameter;

import org.mann.libsvm.SvmParameter;
import org.mann.libsvm.svm_problem;
import org.mann.validation.Checker;

public class ParameterValidationManager {
	private StringBuilder validationMessage;
	
	public ParameterValidationManager(StringBuilder validationMessage){
		this.validationMessage = validationMessage;
	}

	public StringBuilder getValidationMessage() {
		return validationMessage;
	}
	
	public void checkNuThenRunCheckAndGetResponse(String checkType, ParameterValidationManager manager, SvmParameter params,
			svm_problem prob) {
		new NuChecker(this).checkFeasibilityOfNu(prob, params);
		runCheckAndGetResponse(checkType, this, params);
	}
	
	public Checker runCheckAndGetResponse(String checkType, ParameterValidationManager manager, SvmParameter params) {
		Checker checker = null;
		switch(checkType){
			case "Svm Type":
				checker = new SvmTypeChecker(this);
				break;
			case "Kernel":
				checker = new KernelChecker(this);
				break;
			case "Gamma":
				checker = new GammaChecker(this);
				break;
			case "Degree":
				checker =  new DegreeChecker(this);
				break;
			case "Cache":
				checker =  new CacheSizeChecker(this);
				break;
			case "Eps":
				checker = new EpsChecker(this);
				break;
			case "Nu":
				checker =  new NuChecker(this);
				break;
			case "C":
				checker =  new CChecker(this);
				break;
			case "P":
				checker =  new PChecker(this);
				break;
			case "Shrinking":
				checker =  new ShrinkingChecker(this);
				break;
			case "Probability":
				checker =  new ProbabilityChecker(this);
				break;
			default:
				throw new IllegalArgumentException("Check type '" + checkType + "' Not recognised");
		}
		return checker.checkParameter(params);
	}

}
