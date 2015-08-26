package helpers;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertThat;
import libsvm.SvmNode;
import libsvm.SvmParameter;
import libsvm.svm_problem;

import org.junit.Test;

public class NuCheckerTest {

	@Test
	public void checkNuShouldReturnErrorWhenLessThanZeroOrGreaterThanOne(){
		String errorMessage = new NuChecker(new ParameterValidationManager(new StringBuilder())).checkNu(0);
		assertThat(errorMessage, equalTo("ERROR: nu <= 0 or nu > 1\n"));
	}
	
	@Test
	public void checkFeasibilityOfNuReturnsErrorMessageWhenUnfeasible(){
		svm_problem dataset = createSvmProblem();
		SvmParameter param = new SvmParameter();
		//.88 is the largest nu (to 2 dp) that is feasible for these data
		param.nu = .89;
		
		String errorMessage = new NuChecker(new ParameterValidationManager(new StringBuilder())).checkFeasibilityOfNu(SvmParameter.NU_SVC, dataset, param);
		assertThat(errorMessage, equalTo("ERROR: "+param.nu +" is not a feasible nu for these data"));
		
		param.nu = .88;
		String successMessage = new NuChecker(new ParameterValidationManager(new StringBuilder())).checkFeasibilityOfNu(SvmParameter.NU_SVC, dataset, param);
		assertThat(successMessage, equalTo("Nu = " + param.nu + ": feasibility checked and is OK"));
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

}
