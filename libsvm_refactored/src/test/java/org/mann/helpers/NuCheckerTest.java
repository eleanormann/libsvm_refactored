package org.mann.helpers;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.containsString;
import static org.junit.Assert.assertThat;

import org.junit.Test;
import org.mann.helpers.NuChecker;
import org.mann.helpers.ParameterValidationManager;
import org.mann.libsvm.SvmNode;
import org.mann.libsvm.SvmParameter;
import org.mann.libsvm.svm_problem;
import org.mann.libsvm.SvmParameter.SvmType;

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
		ParameterValidationManager managerForErrorTest = new ParameterValidationManager(new StringBuilder()); 
		new NuChecker(managerForErrorTest).checkFeasibilityOfNu(SvmParameter.NU_SVC, dataset, param);
		assertThat(managerForErrorTest.getValidationMessage().toString(), equalTo("ERROR: "+param.nu +" is not a feasible nu for these data"));	
	}
	
	@Test
	public void checkFeasibilityofNuReturnsSuccessMessageWhenNuIsFeasible(){
		ParameterValidationManager managerForSuccessTest = new ParameterValidationManager(new StringBuilder()); 
		svm_problem dataset = createSvmProblem();
		SvmParameter param = new SvmParameter();
		param.nu = .88;
		 new NuChecker(managerForSuccessTest).checkFeasibilityOfNu(SvmParameter.NU_SVC, dataset, param);
		assertThat(managerForSuccessTest.getValidationMessage().toString(), equalTo("Nu = " + param.nu + ": feasibility checked and is OK"));
	}
	
	@Test
	public void checkParameterShouldReturnErrorWhenNuLessThanZeroOrGreaterThanOne(){
		ParameterValidationManager manager = new ParameterValidationManager(new StringBuilder()); 
		new NuChecker(manager).checkParameter(createSvmParameter());
		assertThat(manager.getValidationMessage().toString(), containsString("Nu = 1.0"));
	}

	@Test
	public void extendArrayLengthShouldReturnCopyWithDoubleLength(){
		int[] originalArray = {1,2,3,4,5};
		int[] expectedArray = {1,2,3,4,5,0,0,0,0,0};
		originalArray = NuChecker.extendArrayLength(originalArray);
		assertThat(originalArray, equalTo(expectedArray));
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


	private SvmParameter createSvmParameter() {
		SvmParameter params = new SvmParameter();
		params.svmType = SvmType.NU_SVC;
		params.nu = 1.0;
		return params;
	}
}
