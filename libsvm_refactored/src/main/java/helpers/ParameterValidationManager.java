package helpers;

import libsvm.SvmParameter;
import libsvm.svm_problem;
import libsvm.SvmParameter.SvmType;

public class ParameterValidationManager {
	StringBuilder validationMessage;
	
	ParameterValidationManager(StringBuilder validationMessage){
		this.validationMessage = validationMessage;
	}

	public StringBuilder getValidationMessage() {
		return validationMessage;
	}
	
	public String runCheckAndGetResponse(String checkType, Object...params) {
		if(checkType.equals("SvmType")){
			return new SvmTypeChecker(this).checkSvmType((Integer) params[0]);
		}
		if(checkType.equals("Kernel")){
			return new KernelChecker(this).checkKernelType((Integer) params[0]);
		}
		if(checkType.equals("Gamma")){
			return new GammaChecker(this).checkGamma((Double) params[0]);
		}
		if(checkType.equals("Degree")){
			return new DegreeChecker(this).checkDegreeOfPolynomialKernel((Integer) params[0]);
		}
		if(checkType.equals("Cache")){
			return new CacheSizeChecker(this).checkCacheSize((Double) params[0]);
		}
		if(checkType.equals("Eps")){
			return new EpsChecker(this).checkEps((Double) params[0]);
		}
		if(checkType.equals("C")){
			return new CChecker(this).checkC((Double) params[0]);
		}
		if(checkType.equals("Nu")){
			return new NuChecker(this).checkNu((Double) params[0]);
		}
		if(checkType.equals("P")){
			return new PChecker(this).checkP((Double) params[0]);
		}
		if(checkType.equals("Shrinking")){
			return new ShrinkingChecker(this).checkShrinking((Integer) params[0]);
		}
		if(checkType.equals("Probability")){
			return new ProbabilityChecker(this).checkProbability((Integer) params[0], (SvmType) params[1]);
		}
		if(checkType.equals("Feasibility of Nu")){
			return new NuChecker(this).checkFeasibilityOfNu((Integer) params[0], (svm_problem) params[1], (SvmParameter) params[2]);
		}
		return null;
	}
}
