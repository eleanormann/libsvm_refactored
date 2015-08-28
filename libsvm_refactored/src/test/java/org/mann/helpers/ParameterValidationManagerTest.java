package org.mann.helpers;

import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertThat;

import org.junit.Before;
import org.junit.Test;
import org.mann.libsvm.SvmNode;
import org.mann.libsvm.SvmParameter;
import org.mann.libsvm.SvmParameter.SvmType;
import org.mann.libsvm.svm;
import org.mann.libsvm.svm_problem;

public class ParameterValidationManagerTest {
	private ParameterValidationManager manager; 
	
	@Before
	public void setup(){
		manager = new ParameterValidationManager(new StringBuilder());
	}
	
	@Test
	public void checkValidationMessageIsComplete(){
		String expectedMessage = "Svm type: NU_SVC\nkernel type: 1\nGamma = 1.0\nDegree = 1\nCache size = 1.0\n"
				+ "Eps = 1.0\nC = 1.0\nNu = 1.0\np = 1.0\nShrinking = 1\nProbability = 1\n";
		manager.runCheckAndGetResponse("SvmType", manager, createSvmParameter());
		assertThat(manager.getValidationMessage().toString(), equalTo(expectedMessage));
	}
	
	@Test(expected = NullPointerException.class)
	public void checkSvmParameterShouldReturnExceptionWhenSvmTypeNotSet(){
		svm.svm_check_parameter(null, new SvmParameter());
	}
	
	@Test
	public void parameterCheckerShouldReturnSvmTypeWhenRequested() {
		checkValidationMessageContainsString("SvmType", "Svm Type: NU_SVC");
	}

	@Test
	public void parameterCheckerShouldReturnKernelTypeWhenRequested() {
		checkValidationMessageContainsString("Kernel", "kernel type: 1\n");
	}
	
	@Test
	public void parameterCheckerShouldReturnGammaWhenRequested() {
		checkValidationMessageContainsString("Gamma", "Gamma = 1.0\n");
	}
	
	@Test
	public void parameterCheckerShouldReturnDegreeWhenRequested() {
		checkValidationMessageContainsString("Degree", "Degree = 1\n");
	}
	
	@Test
	public void parameterCheckerShouldReturnCacheSizeWhenRequested() {
		checkValidationMessageContainsString("Cache", "Cache size: 1.0\n");
	}
	
	@Test
	public void parameterCheckerShouldReturnEpsWhenRequested() {
		checkValidationMessageContainsString("Eps", "Eps = 1.0\n");
	}
	
	@Test
	public void parameterCheckerShouldReturnCWhenRequested() {
		checkValidationMessageContainsString("C", "C = 1.0\n");
	}
	
	@Test
	public void parameterCheckerShouldReturnNuWhenRequested() {
		checkValidationMessageContainsString("Nu", "Nu = 1.0\n");
	}
	
	@Test
	public void parameterCheckerShouldReturnPWhenRequested() {
		checkValidationMessageContainsString("P", "p = 1.0\n");
	}
	
	@Test
	public void parameterCheckerShouldReturnShrinkingWhenRequested() {
		checkValidationMessageContainsString("Shrinking", "Shrinking = 1\n");
	}
	
	@Test
	public void parameterCheckerShouldReturnProbabilityWhenRequested() {
		checkValidationMessageContainsString("Probability","Probability = 1\n");
	}
	
	@Test
	public void parameterCheckerShouldReturnFeasibilityOfNuWhenRequested() {
		//TODO fix this so it fails if nu is not set
		checkValidationMessageContainsString("Feasibility of Nu","Nu = 0.0: feasibility checked and is OK");
	}
	
	private void checkValidationMessageContainsString(String checkType, String expectedMessage) {
		manager.runCheckAndGetResponse(checkType, manager, createSvmParameter());
		assertThat(manager.getValidationMessage().toString(), containsString(expectedMessage));
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
		params.kernel_type = 1;
		params.C = 1;
		params.cache_size = 1;
		params.degree = 1;
		params.eps = 1;
		params.gamma = 1;
		params.nu = 1;
		params.nr_weight = 1;
		params.p = 1;
		params.probability = 1;
		params.shrinking=1;
		return params;
	}
}

