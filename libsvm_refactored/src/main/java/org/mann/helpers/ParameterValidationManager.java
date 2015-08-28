package org.mann.helpers;

import org.mann.libsvm.SvmParameter;

public class ParameterValidationManager {
	private StringBuilder validationMessage;
	
	ParameterValidationManager(StringBuilder validationMessage){
		this.validationMessage = validationMessage;
	}

	public StringBuilder getValidationMessage() {
		return validationMessage;
	}
	
	public Checker runCheckAndGetResponse(String checkType, ParameterValidationManager manager, SvmParameter params) {
		Checker checker = null;
		switch(checkType){
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
			case "Feasibility of Nu":
				checker =  new NuChecker(this);
				break;
			default:
				throw new IllegalArgumentException("Check type " + checkType + " Not recognised");
		}
		return checker.checkParameter(params);
	}

}
