package org.mann.libsvm;

import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.doNothing;

import java.io.IOException;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.mann.helpers.HelpMessages;
import org.mann.libsvm.SvmParameter.KernelType;
import org.mann.libsvm.SvmParameter.SvmType;
import org.mann.ui.ResultCollector;

public class SvmTrainTest {
	private static final String BASE_PATH = "src/test/resources/testdata/";

	@Rule
	public ExpectedException thrown = ExpectedException.none();
	
	@Test
	public void readProblemShouldSetDataWhenKernelRbf() throws IOException {
		// Arrange
		svm_train train = new svm_train();

		// Act
		train.run(new String[] { BASE_PATH + "fakeTrainingData.train" }, new ResultCollector());

		// Assert
		assertThat(train.getSvmParameter().kernelType, equalTo(KernelType.rbf));
		assertThat(train.getSvmProblem().length, equalTo(11));
		assertThatSvmProblemDataValuesSetCorrectly(train, 0);
	}

	@Test
	public void readProblemShouldSetDatacorrectlyWhenKernelIsPrecomputed() throws IOException {
		// Arrange
		svm_train train = new svm_train();

		// Act
		train.run(new String[] { "-t", "4", BASE_PATH + "fakeTrainingDataWithPrecomputedKernel.train" }, new ResultCollector());

		// Assert
		assertThat(train.getSvmParameter().kernelType, equalTo(KernelType.precomputed));
		assertThat(train.getSvmParameter().gamma, equalTo(1.0 / 2)); 													
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
		train.run(new String[] { "-t", "4", BASE_PATH + "fakeTrainingData.train" }, result);

		// Assert
		assertThat(train.getSvmParameter().kernelType, equalTo(KernelType.precomputed));
		assertThat(result.getResult(), equalTo("ERROR: java.lang.IllegalArgumentException: Wrong kernel matrix: first column must be 0:sample_serial_number\n"
				+ HelpMessages.TRAIN_HELP_MESSAGE_ON_BAD_INPUT + "\n"));
	}

	@Test
	public void readProblemShouldThrowExceptionWhenKernelIsPrecomputedButOutOfRange() throws IOException {
		// Arrange
		svm_train train = new svm_train();
		ResultCollector result = new ResultCollector();

		// Act
		train.run(new String[] { "-t", "4", BASE_PATH + "fakeTrainingDataWithKernelOutOfRange.train" }, result);

		// Assert
		assertThat(train.getSvmParameter().kernelType, equalTo(KernelType.precomputed));
		assertThat(result.getResult(), equalTo("ERROR: java.lang.IllegalArgumentException: Wrong input format: sample_serial_number out of range\n"
				+ HelpMessages.TRAIN_HELP_MESSAGE_ON_BAD_INPUT + "\n"));
	}
	
	@Test
	public void checkSvmParameterShouldReturnExceptionWhenSvmTypeNotSet(){;
		String message = new svm_train().checkSvmParameter(null, new SvmParameter());
		assertThat(message, containsString("ERROR: Svm type not set\n"));
	}
	
	@Test
	public void svmTrainShouldCheckTrainParameters() throws IOException{
		svm_train train = mock(svm_train.class);
		doNothing().when(train).parse_command_line(any(String[].class));
		doNothing().when(train).read_problem();
		when(train.checkSvmParameter(any(svm_problem.class), any(SvmParameter.class))).thenCallRealMethod();
		ResultCollector result = new ResultCollector();
		
		train.run(new String[]{}, result);
		assertThat(result.getResult(), equalTo("ERROR: Svm type not set\n"));
		
	}

	@Test
	public void svmCheckParameterShouldReturnValidationMessageWithNuFeasibilityWhenNuSvc(){
		svm_train train  = new svm_train();
		String expectedMessage = "Nu = 1.0: feasibility checked and is OK\nSvm type: nu_svc\nKernel type: linear\nGamma = 1.0\nDegree = 1\nCache size: 1.0\n"
				+ "Eps = 1.0\nShrinking = 1\nProbability = 1\n";
		String validationMessage = train.checkSvmParameter(new svm_problem(), createSvmParameter(SvmType.nu_svc));
		assertThat(validationMessage, equalTo(expectedMessage));
	}

	@Test
	public void svmCheckParameterShouldReturnValidationMessageWithNuWhenNuSvr(){
		svm_train train  = new svm_train();
		String expectedMessage = "Svm type: nu_svr\nKernel type: linear\nGamma = 1.0\nDegree = 1\nCache size: 1.0\n"
				+ "Eps = 1.0\nC = 1.0\nNu = 1.0\nShrinking = 1\nProbability = 1\n";
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
		params.svmType = svmType;
		params.kernelType = KernelType.linear;
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
