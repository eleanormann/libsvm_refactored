package org.mann.validation.svmparameter;

import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertThat;

import org.junit.Before;
import org.junit.Test;
import org.mann.libsvm.SvmParameter;
import org.mann.libsvm.SvmParameter.KernelType;
import org.mann.libsvm.SvmParameter.SvmType;
import org.mann.libsvm.svm;

public class ParameterValidationManagerTest {
	private ParameterValidationManager manager; 
	
	@Before
	public void setup(){
		manager = new ParameterValidationManager(new StringBuilder());
	}
	
	@Test
	public void checkValidationMessageIsComplete(){
		String expectedMessage = "Svm type: nu_svr\nKernel type: poly\nGamma = 1.0\nDegree = 1\nCache size: 1.0\n"
				+ "Eps = 1.0\nC = 1.0\nNu = 1.0\nShrinking = 1\nProbability = 1\n";
		manager.runCheckAndGetResponse("Svm Type", manager, createSvmParameter(SvmType.nu_svr));
		assertThat(manager.getValidationMessage().toString(), equalTo(expectedMessage));
	}
	
	@Test
	public void checkSvmParameterShouldReturnExceptionWhenSvmTypeNotSet(){
		String expectedMessage = new svm().checkSvmParameter(null, new SvmParameter());
		assertThat(expectedMessage, containsString("ERROR: Svm type not set"));
	}
	
	@Test
	public void parameterCheckerShouldReturnSvmTypeWhenRequested() {
		checkValidationMessageContainsString("Svm Type", "Svm type: nu_svc", SvmType.nu_svc);
	}

	@Test
	public void parameterCheckerShouldReturnKernelTypeWhenRequested() {
		checkValidationMessageContainsString("Kernel", "Kernel type: poly\n", null);
	}
	
	@Test
	public void parameterCheckerShouldReturnGammaWhenRequested() {
		checkValidationMessageContainsString("Gamma", "Gamma = 1.0\n", null);
	}
	
	@Test
	public void parameterCheckerShouldReturnDegreeWhenRequested() {
		checkValidationMessageContainsString("Degree", "Degree = 1\n", null);
	}
	
	@Test
	public void parameterCheckerShouldReturnCacheSizeWhenRequested() {
		checkValidationMessageContainsString("Cache", "Cache size: 1.0\n", null);
	}
	
	@Test
	public void parameterCheckerShouldReturnEpsWhenRequested() {
		checkValidationMessageContainsString("Eps", "Eps = 1.0\n", null);
	}
	
	@Test
	public void parameterCheckerShouldReturnCWhenRequested() {
		checkValidationMessageContainsString("C", "C = 1.0\n", SvmType.c_svc);
	}
	
	@Test
	public void parameterCheckerShouldReturnNuWhenRequested() {
		checkValidationMessageContainsString("Nu", "Nu = 1.0\n", SvmType.one_class);
	}
	
	@Test
	public void parameterCheckerShouldReturnPWhenRequested() {
		checkValidationMessageContainsString("P", "p = 1.0\n", SvmType.epsilon_svr);
	}
	
	@Test
	public void parameterCheckerShouldReturnShrinkingWhenRequested() {
		checkValidationMessageContainsString("Shrinking", "Shrinking = 1\n", null);
	}
	
	@Test
	public void parameterCheckerShouldReturnProbabilityWhenRequested() {
		checkValidationMessageContainsString("Probability","Probability = 1\n", null);
	}
	
	@Test(expected=IllegalArgumentException.class)
	public void parameterCheckerShouldReturnExceptionWhenCheckTypeNotRecognised() {
		manager.runCheckAndGetResponse("Feasibility of Nu", manager, createSvmParameter(SvmType.c_svc));
	}
	
	private void checkValidationMessageContainsString(String checkType, String expectedMessage, SvmType svmType) {
		manager.runCheckAndGetResponse(checkType, manager, createSvmParameter(svmType));
		assertThat(manager.getValidationMessage().toString(), containsString(expectedMessage));
	}
	
	private SvmParameter createSvmParameter(SvmType svmType) {
		SvmParameter params = new SvmParameter();
		params.svmType = svmType;
		params.kernelType = KernelType.poly;
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

