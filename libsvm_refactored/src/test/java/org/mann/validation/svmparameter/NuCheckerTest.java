package org.mann.validation.svmparameter;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.containsString;
import static org.junit.Assert.assertThat;

import org.junit.Test;
import org.mann.libsvm.SvmNode;
import org.mann.libsvm.SvmParameter;
import org.mann.libsvm.svm_problem;
import org.mann.libsvm.SvmParameter.SvmType;
import org.mann.validation.svmparameter.NuChecker;

public class NuCheckerTest {

	@Test
	public void checkNuShouldReturnErrorWhenLessThanZeroOrGreaterThanOne(){
		ParameterValidationManager manager = new ParameterValidationManager(new StringBuilder()); 
		new NuChecker(manager).checkNu(0);
		assertThat(manager.getValidationMessage().toString(), equalTo("ERROR: nu <= 0 or nu > 1\n"));
	}
	
	@Test
	public void checkFeasibilityOfNuReturnsErrorMessageWhenUnfeasible(){
		svm_problem dataset = createSvmProblem();
		SvmParameter param = new SvmParameter();
		//.88 is the largest nu (to 2 dp) that is feasible for these data
		param.nu = .89;
		param.svmType = SvmType.NU_SVC;
		ParameterValidationManager managerForErrorTest = new ParameterValidationManager(new StringBuilder()); 
		new NuChecker(managerForErrorTest).checkFeasibilityOfNu(dataset, param);
		assertThat(managerForErrorTest.getValidationMessage().toString(), equalTo("ERROR: "+param.nu +" is not a feasible nu for these data"));	
	}
	
	@Test
	public void checkFeasibilityofNuReturnsSuccessMessageWhenNuIsFeasible(){
		ParameterValidationManager managerForSuccessTest = new ParameterValidationManager(new StringBuilder()); 
		svm_problem dataset = createSvmProblem();
		SvmParameter param = new SvmParameter();
		param.nu = .88;
		param.svmType = SvmType.NU_SVC;
		 new NuChecker(managerForSuccessTest).checkFeasibilityOfNu(dataset, param);
		assertThat(managerForSuccessTest.getValidationMessage().toString(), equalTo("Nu = " + param.nu + ": feasibility checked and is OK\n"));
	}
	
	@Test
	public void checkParameterShouldReturnErrorWhenNuLessThanZeroOrGreaterThanOneAndEligibleSvmType(){
		ParameterValidationManager manager = new ParameterValidationManager(new StringBuilder()); 
		new NuChecker(manager).checkParameter(createSvmParameter(1, SvmType.NU_SVR));
		assertThat(manager.getValidationMessage().toString(), containsString("Nu = 1.0"));
	}


	@Test
	public void extendArrayLengthShouldReturnCopyWithDoubleLength(){
		int[] originalArray = {1,2,3,4,5};
		int[] expectedArray = {1,2,3,4,5,0,0,0,0,0};
		originalArray = NuChecker.extendArrayLength(originalArray);
		assertThat(originalArray, equalTo(expectedArray));
	}
	
	
	@Test
	public void checkFeasibiltyOfNuThenCheckParameterShouldCheckFeasibilityOfNuWhenSvmTypeNuSvc(){
		ParameterValidationManager manager = new ParameterValidationManager(new StringBuilder()); 
		SvmParameter param = createSvmParameter(.88, SvmType.NU_SVC);
		new NuChecker(manager).runFeasibilityCheckThenCheckParameter(createSvmProblem(), param);
		assertThat(manager.getValidationMessage().toString(), equalTo("Nu = " + param.nu + ": feasibility checked and is OK\n"));
		
	}
	private svm_problem createSvmProblem() {
		svm_problem dataset = new svm_problem();
		dataset.y = new double[]{1,1,1,1,0,0,0,1,0};
		dataset.x = new SvmNode[][] {
				{ new SvmNode(1, 5), new SvmNode(2, 6), new SvmNode(3, 4), new SvmNode(4, 4), new SvmNode(-1, 0) },
				{ new SvmNode(5, 10), new SvmNode(6, 9), new SvmNode(7, 8), new SvmNode(8, 10), new SvmNode(9, 9) }};
		dataset.length = dataset.y.length;
		return dataset;
	}
	
	private SvmParameter createSvmParameter(double nu, SvmType svmType) {
		SvmParameter param = new SvmParameter();
		param.svmType = svmType;
		param.nu = nu;
		return param;
	}
}
