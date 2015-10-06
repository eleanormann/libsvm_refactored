package org.mann.validation.svmparameter;

import java.util.Arrays;

import org.mann.libsvm.SvmParameter;
import org.mann.libsvm.SvmParameter.SvmType;
import org.mann.libsvm.SvmProblem;
import org.mann.validation.Checker;

public class NuChecker implements Checker {

	private ParameterValidationManager manager;

	public NuChecker(ParameterValidationManager parameterValidationManager) {
		this.manager = parameterValidationManager;
	}

	protected void checkNu(double nu) {
		if (nu <= 0 || nu > 1) {
			manager.getValidationMessage().append("ERROR: nu <= 0 or nu > 1\n");
		} else {
			manager.getValidationMessage().append("Nu = ").append(nu).append("\n");
		}
	}

	public void checkFeasibilityOfNu(SvmProblem prob, SvmParameter param) {
		if (param.getSvmType() == SvmType.nu_svc) {
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

			double nu = param.getNu();
			for (i = 0; i < currentIndexInProblem; i++) {
				int n1 = count[i];
				for (int j = i + 1; j < currentIndexInProblem; j++) {
					int n2 = count[j];
					if (nu * (n1 + n2) / 2 > Math.min(n1, n2)) {
						manager.getValidationMessage().append("ERROR: ").append(nu)
								.append(" is not a feasible nu for these data");
						return;
					}
				}
			}
			manager.getValidationMessage().append("Nu = ").append(nu).append(": feasibility checked and is OK\n");
		}
	}

	public static int[] extendArrayLength(int[] originalArray) {
		int doubledLength = originalArray.length * 2;
		int[] newArray = Arrays.copyOf(originalArray, doubledLength);
		return newArray;
	}

	public Checker checkParameter(SvmParameter params) {
		SvmType svmType = params.getSvmType();
		if (svmType == SvmType.one_class || svmType == SvmType.nu_svr) {
			checkNu(params.getNu());
		}
		return manager.runCheckAndGetResponse("P", params);
	}

	public void runFeasibilityCheckThenCheckParameter(SvmProblem prob, SvmParameter params) {
		checkFeasibilityOfNu(prob, params);
	}

}
