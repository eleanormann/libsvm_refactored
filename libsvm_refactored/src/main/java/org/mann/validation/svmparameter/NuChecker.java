package org.mann.validation.svmparameter;

import java.util.Arrays;

import org.mann.libsvm.SvmParameter;
import org.mann.libsvm.SvmParameter.SvmType;
import org.mann.libsvm.svm_problem;
import org.mann.validation.Checker;

public class NuChecker implements Checker {

	private ParameterValidationManager manager;

	public NuChecker(ParameterValidationManager parameterValidationManager) {
		this.manager = parameterValidationManager;
	}
	
	public void checkNu(double nu) {
		if (nu <= 0 || nu > 1){
			manager.getValidationMessage().append( "ERROR: nu <= 0 or nu > 1\n");			
		}else{
			manager.getValidationMessage().append("Nu = " + nu + "\n");
		}
	}

	public void checkFeasibilityOfNu(svm_problem prob, SvmParameter param) {
		if (param.svmType == SvmType.NU_SVC) {
			int problemLength = prob.length;
			int arrayLength = 16;
			int currentIndexInProblem = 0;
			int[] label = new int[arrayLength];
			int[] count = new int[arrayLength];

			int i;
			for (i = 0; i < problemLength; i++) { // 9 = problemLength
				int currentYpoint = (int) prob.y[i]; // 1,1,0,0,1,0
				int j;
				for (j = 0; j < currentIndexInProblem; j++) {
					if (currentYpoint == label[j]) {
						++count[j];
						break;
					}
				}

				if (j == currentIndexInProblem) {
					if (currentIndexInProblem == arrayLength) {
						label = extendArrayLength(label);
						count = extendArrayLength(count);
					}
					label[currentIndexInProblem] = currentYpoint;
					count[currentIndexInProblem] = 1;
					++currentIndexInProblem;
				}
			}

			for (i = 0; i < currentIndexInProblem; i++) {
				int n1 = count[i];
				for (int j = i + 1; j < currentIndexInProblem; j++) {
					int n2 = count[j];
					if (param.nu * (n1 + n2) / 2 > Math.min(n1, n2)){
						manager.getValidationMessage().append("ERROR: " + param.nu + " is not a feasible nu for these data");
						return;
					}	
				}
			}
			manager.getValidationMessage().append( "Nu = " + param.nu + ": feasibility checked and is OK\n");
		}
	}

	public static int[] extendArrayLength(int[] originalArray) {
		int doubledLength = originalArray.length * 2;
		int[] newArray = Arrays.copyOf(originalArray, doubledLength);
		return newArray;
	}

	public Checker checkParameter(SvmParameter params) {
		if (params.svmType == SvmType.ONE_CLASS || params.svmType == SvmType.NU_SVR) {
			checkNu(params.nu);	
		}
		return manager.runCheckAndGetResponse("P", manager, params);
	}

	public void runFeasibilityCheckThenCheckParameter(svm_problem prob, SvmParameter params) {
		checkFeasibilityOfNu(prob, params);
	}

}
