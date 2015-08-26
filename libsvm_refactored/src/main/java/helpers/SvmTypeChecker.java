package helpers;

import libsvm.SvmParameter;
import libsvm.svm;

public class SvmTypeChecker {

	private ParameterValidationManager manager;
	
	public SvmTypeChecker(){};
	
	public SvmTypeChecker(ParameterValidationManager parameterValidationManager){
		this.manager = parameterValidationManager;
	}

	// TODO: change this local svm_type to a SvmType
	// Comment expires 17th September 2015
	public String checkSvmType(int svm_type) {

		if (svm_type != SvmParameter.C_SVC && svm_type != SvmParameter.NU_SVC && svm_type != SvmParameter.ONE_CLASS
				&& svm_type != SvmParameter.EPSILON_SVR && svm_type != SvmParameter.NU_SVR) {

			return "ERROR: unknown svm type\n";
		}
		return new SvmParameter().getSvmTypeFromSvmParameter(svm_type).toString();
	}
}
