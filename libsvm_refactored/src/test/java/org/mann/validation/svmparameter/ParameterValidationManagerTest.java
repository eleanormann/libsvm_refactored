package org.mann.validation.svmparameter;

import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertThat;

import org.junit.Before;
import org.junit.Test;
import org.mann.libsvm.SvmParameter;
import org.mann.libsvm.SvmParameter.KernelType;
import org.mann.libsvm.SvmParameter.SvmType;
import org.mann.libsvm.svm_train;

public class ParameterValidationManagerTest {
	private ParameterValidationManager manager;
	private svm_train train;

	@Before
	public void setup() {
		manager = new ParameterValidationManager(new StringBuilder());
		train = new svm_train();
	}

	@Test
	public void checkValidationMessageIsComplete() {
		String expectedMessage = "Svm type: nu_svr\nKernel type: poly\nGamma = 1.0\nDegree = 1\nCache size: 1.0\n"
				+ "Epsilon (tolerance) = 1.0\nC = 1.0\nNu = 1.0\nShrinking = 1\nProbability = 1\n";
		manager.runCheckAndGetResponse("Svm Type", createSvmParameter(SvmType.nu_svr));
		assertThat(manager.getValidationMessage().toString(), equalTo(expectedMessage));
	}

	@Test
	public void checkSvmParameterShouldReturnErrorWhenSvmTypeNotSet() {
		String expectedMessage = train.checkSvmParameter(null, new SvmParameter());
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
		checkValidationMessageContainsString("Eps", "Epsilon (tolerance) = 1.0\n", null);
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
		checkValidationMessageContainsString("P", "Epsilon (Loss Function) = 1.0\n", SvmType.epsilon_svr);
	}

	@Test
	public void parameterCheckerShouldReturnShrinkingWhenRequested() {
		checkValidationMessageContainsString("Shrinking", "Shrinking = 1\n", null);
	}

	@Test
	public void parameterCheckerShouldReturnProbabilityWhenRequested() {
		checkValidationMessageContainsString("Probability", "Probability = 1\n", null);
	}

	@Test(expected = IllegalArgumentException.class)
	public void parameterCheckerShouldReturnExceptionWhenCheckTypeNotRecognised() {
		manager.runCheckAndGetResponse("Feasibility of Nu", createSvmParameter(SvmType.c_svc));
	}

	private void checkValidationMessageContainsString(String checkType, String expectedMessage, SvmType svmType) {
		manager.runCheckAndGetResponse(checkType, createSvmParameter(svmType));
		assertThat(manager.getValidationMessage().toString(), containsString(expectedMessage));
	}

	private SvmParameter createSvmParameter(SvmType svmType) {
		SvmParameter params = new SvmParameter();
		params.setSvmType(svmType);
		params.setKernelType(KernelType.poly);
		params.setCostC(1);
		params.setCacheSize(1);
		params.setDegree(1);
		params.setEpsilonTolerance(1);
		params.setGamma(1);
		params.setNu(1);
		params.setNrWeight(1);
		params.setEpsilonLossFunction(1);
		params.setProbability(1);
		params.setShrinking(1);
		return params;
	}
}
