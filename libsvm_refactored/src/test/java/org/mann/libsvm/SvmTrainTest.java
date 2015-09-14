package org.mann.libsvm;

import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertThat;

import java.io.IOException;

import org.junit.Test;
import org.mann.libsvm.SvmParameter.KernelType;
import org.mann.libsvm.SvmParameter.SvmType;
import org.mann.ui.ResultCollector;

public class SvmTrainTest {
	private static final String BASE_PATH = "src/test/resources/testdata/";

	@Test
	public void parseCommandLineShouldParseCommandLine(){
		
	}
	
	@Test
	public void readProblemShouldSetDataWhenKernelRbf() throws IOException {
		// Arrange
		svm_train train = new svm_train();

		// Act
		train.run(new String[] { BASE_PATH + "fakeTrainingData.train" }, new ResultCollector());

		// Assert
		assertThat(train.getSvmParameter().getKernelType(), equalTo(KernelType.rbf));
		assertThat(train.getSvmProblem().length, equalTo(11));
		assertThatSvmProblemDataValuesSetCorrectly(train, 0);
	}

	@Test
	public void readProblemShouldSetDatacorrectlyWhenKernelIsPrecomputed() throws IOException {
		// Arrange
		svm_train train = new svm_train();

		// Act
		train.run(new String[] { "-t", "precomputed", BASE_PATH + "fakeTrainingDataWithPrecomputedKernel.train" }, new ResultCollector());

		// Assert
		assertThat(train.getSvmParameter().getKernelType(), equalTo(KernelType.precomputed));
		assertThat(train.getSvmParameter().getGamma(), equalTo(1.0 / 2)); 													
		assertThatPrecomputedKernelSetCorrectly(train);
		assertThatSvmProblemDataValuesSetCorrectly(train, 1);
	}

	private void assertThatPrecomputedKernelSetCorrectly(svm_train train) {
		assertThat(train.getSvmProblem().x[0][0].index, equalTo(0));
		assertThat(train.getSvmProblem().x[1][0].index, equalTo(0));
		assertThat(train.getSvmProblem().x[2][0].index, equalTo(0));
		assertThat(train.getSvmProblem().x[3][0].value, equalTo(1.3));
		assertThat(train.getSvmProblem().x[4][0].value, equalTo(1.4));
		assertThat(train.getSvmProblem().x[5][0].value, equalTo(1.0));
	}

	@Test
	public void readProblemShouldThrowExceptionWhenKernelIsSetAsPrecomputedButNotIncludedInTrainingData() throws IOException {
		// Arrange
		svm_train train = new svm_train();

		// Act
		ResultCollector result = new ResultCollector();
		train.run(new String[] { "-t", "precomputed", BASE_PATH + "fakeTrainingData.train" }, result);

		// Assert
		assertThat(train.getSvmParameter().getKernelType(), equalTo(KernelType.precomputed));
		assertThat(result.getResult(), containsString("ERROR: Wrong kernel matrix: first column must be 0:sample_serial_number\n"));
	}

	@Test
	public void readProblemShouldThrowExceptionWhenKernelIsPrecomputedButOutOfRange() throws IOException {
		// Arrange
		svm_train train = new svm_train();
		ResultCollector result = new ResultCollector();

		// Act
		train.run(new String[] { "-t", "precomputed", BASE_PATH + "fakeTrainingDataWithKernelOutOfRange.train" }, result);

		// Assert
		assertThat(train.getSvmParameter().getKernelType(), equalTo(KernelType.precomputed));
		assertThat(result.getResult(), containsString("ERROR: Wrong input format: sample_serial_number out of range\n"));
	}

	@Test
	public void checkSvmParameterShouldReturnExceptionWhenSvmTypeNotSet(){;
		String message = new svm_train().checkSvmParameter(null, new SvmParameter());
		assertThat(message, containsString("ERROR: Svm type not set\n"));
	}

	@Test
	public void svmCheckParameterShouldReturnValidationMessageWithNuFeasibilityWhenNuSvc(){
		svm_train train  = new svm_train();
		String expectedMessage = "Nu = 1.0: feasibility checked and is OK\nSvm type: nu_svc\nKernel type: linear\nGamma = 1.0\nDegree = 1\nCache size: 1.0\n"
				+ "Epsilon (tolerance) = 1.0\nShrinking = 1\nProbability = 1\n";
		String validationMessage = train.checkSvmParameter(new svm_problem(), createSvmParameter(SvmType.nu_svc));
		assertThat(validationMessage, equalTo(expectedMessage));
	}

	@Test
	public void svmCheckParameterShouldReturnValidationMessageWithNuWhenNuSvr(){
		svm_train train  = new svm_train();
		String expectedMessage = "Svm type: nu_svr\nKernel type: linear\nGamma = 1.0\nDegree = 1\nCache size: 1.0\n"
				+ "Epsilon (tolerance) = 1.0\nC = 1.0\nNu = 1.0\nShrinking = 1\nProbability = 1\n";
		String validationMessage = train.checkSvmParameter(new svm_problem(), createSvmParameter(SvmType.nu_svr));
		assertThat(validationMessage, equalTo(expectedMessage));
	}
	
	private void assertThatSvmProblemDataValuesSetCorrectly(svm_train train, int index) {
		assertThat(train.getSvmProblem().x[0][index].index, equalTo(1));
		assertThat(train.getSvmProblem().x[1][index].index, equalTo(1));
		assertThat(train.getSvmProblem().x[2][index].index, equalTo(1));
		assertThat(train.getSvmProblem().x[3][index + 1].index, equalTo(2));
		assertThat(train.getSvmProblem().x[4][index + 1].index, equalTo(2));
		assertThat(train.getSvmProblem().x[5][index + 1].index, equalTo(2));
		assertThat(train.getSvmProblem().x[6][index].value, equalTo(0.04));
		assertThat(train.getSvmProblem().x[7][index].value, equalTo(0.43));
		assertThat(train.getSvmProblem().x[8][index].value, equalTo(0.2));
		assertThat(train.getSvmProblem().x[9][index].value, equalTo(0.9));
		assertThat(train.getSvmProblem().x[10][index].value, equalTo(0.6));
		assertThat(train.getSvmProblem().x[0][index + 1].value, equalTo(0.2));
		assertThat(train.getSvmProblem().x[1][index + 1].value, equalTo(0.1));
	}

	private SvmParameter createSvmParameter(SvmType svmType) {
		SvmParameter params = new SvmParameter();
		params.setSvmType(svmType);
		params.setKernelType(KernelType.linear);
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
