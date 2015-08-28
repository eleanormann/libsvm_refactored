package org.mann.helpers;

import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.CoreMatchers.instanceOf;
import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;
import org.mann.helpers.ParameterValidationManager;
import org.mann.libsvm.SvmNode;
import org.mann.libsvm.SvmParameter;
import org.mann.libsvm.svm;
import org.mann.libsvm.svm_problem;
import org.mann.libsvm.SvmParameter.SvmType;

public class ParameterValidationManagerTest {
	private ParameterValidationManager manager; 
	
	@Before
	public void setup(){
		manager = new ParameterValidationManager(new StringBuilder());
	}
	
	@Test(expected = NullPointerException.class)
	public void checkSvmParameterShouldReturnExceptionWhenSvmTypeNotSet(){
		svm.svm_check_parameter(null, new SvmParameter());
	}
	
	@Test
	public void parameterCheckerShouldReturnSvmTypeWhenRequested() {
		checkValidationMessage("SvmType", "Svm Type: NU_SVC");
	}

	@Test
	public void parameterCheckerShouldReturnKernelTypeWhenRequested() {
		checkValidationMessage("Kernel", "kernel type: 1\n");
	}
	
	@Test
	public void parameterCheckerShouldReturnGammaWhenRequested() {
		checkValidationMessage("Gamma", "Gamma = 1.0\n");
	}
	
	@Test
	public void parameterCheckerShouldReturnDegreeWhenRequested() {
		checkValidationMessage("Degree", "Degree = 1\n");
	}
	
	@Test
	public void parameterCheckerShouldReturnCacheSizeWhenRequested() {
		checkValidationMessage("Cache", "Cache size: 1.0\n");
	}
	
	@Test
	public void parameterCheckerShouldReturnEpsWhenRequested() {
		checkValidationMessage("Eps", "Eps = 1.0\n");
	}
	
	@Test
	public void parameterCheckerShouldReturnCWhenRequested() {
		checkValidationMessage("C", "C = 1.0\n");
	}
	
	@Test
	public void parameterCheckerShouldReturnNuWhenRequested() {
		checkValidationMessage("Nu", "Nu = 1.0\n");
	}
	
	@Test
	public void parameterCheckerShouldReturnPWhenRequested() {
		checkValidationMessage("P", "p = 1.0\n");
	}
	
	@Test
	public void parameterCheckerShouldReturnShrinkingWhenRequested() {
		checkValidationMessage("Shrinking", "Shrinking = 1\n");
	}
	
	@Test
	public void parameterCheckerShouldReturnProbabilityWhenRequested() {
		checkValidationMessage("Probability","Probability = 1\n");
	}
	
	@Test
	public void parameterCheckerShouldReturnFeasibilityOfNuWhenRequested() {
		//TODO fix this so it fails if nu is not set
		checkValidationMessage("Feasibility of Nu","Nu = 0.0: feasibility checked and is OK");
	}
	
	private void checkValidationMessage(String checkType, String expectedMessage) {
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

