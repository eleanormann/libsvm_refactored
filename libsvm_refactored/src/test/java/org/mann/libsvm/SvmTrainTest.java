package org.mann.libsvm;

import static org.junit.Assert.*;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintStream;

import mockit.Mock;
import mockit.MockUp;
import mockit.Mocked;
import mockit.integration.junit4.JMockit;
import static org.hamcrest.CoreMatchers.equalTo;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mann.helpers.HelpMessages;
import org.mann.libsvm.SvmParameter.KernelType;

public class SvmTrainTest {
	private static final String BASE_PATH = "src/test/resources/testdata/";
	private final ByteArrayOutputStream outContent = new ByteArrayOutputStream();
	private final ByteArrayOutputStream errContent = new ByteArrayOutputStream();
	
	@Before
	public void setUpStreams() {
		System.setOut(new PrintStream(outContent));
		System.setErr(new PrintStream(errContent));
	}

	@After
	public void cleanUpStreams() {
		System.setOut(null);
		System.setErr(null);
	}
	
	@Test
	public void readProblemShouldSetDataWhenKernelRbf() throws IOException{
		//Arrange
		createSvmMock();
		svm_train train = new svm_train();
		
		//Act
		train.run(new String[]{BASE_PATH + "fakeTrainingData.train"});
		
		//Assert
		assertThat(train.getSvmParameter().kernelType, equalTo(KernelType.rbf));
		assertThat(train.getSvmProblem().length, equalTo(11));
		assertThatSvmProblemDataValuesSetCorrectly(train, 0);
	}

	@Test
	public void readProblemShouldSetDatacorrectlyWhenKernelIsPrecomputed() throws IOException {
		// Arrange
		createSvmMock();
		svm_train train = new svm_train();

		// Act
		train.run(new String[] {"-t", "4", BASE_PATH + "fakeTrainingDataWithPrecomputedKernel.train" });

		//Assert
		assertThat(train.getSvmParameter().kernelType, equalTo(KernelType.precomputed));
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

	@Test(expected = IllegalArgumentException.class)
	public void readProblemShouldThrowExceptionWhenKernelIsSetAsPrecomputedButNotIncludedInTrainingData() throws IOException {
		// Arrange
		createSvmMock();
		svm_train train = new svm_train();

		// Act
		train.run(new String[] {"-t", "4", BASE_PATH +"fakeTrainingData.train" });

		//Assert
		assertThat(train.getSvmParameter().kernelType, equalTo(KernelType.precomputed));
		assertThat(errContent.toString(), equalTo("Wrong kernel matrix: first column must be 0:sample_serial_number"));
		assertThat(outContent.toString(), equalTo(HelpMessages.TRAIN_HELP_MESSAGE_ON_BAD_INPUT + "\n"));
		
	}
	
	@Test(expected = IllegalArgumentException.class)
	public void readProblemShouldThrowExceptionWhenKernelIsPrecomputedButOutOfRange() throws IOException {
		// Arrange
		createSvmMock();
		svm_train train = new svm_train();
		

		// Act
		train.run(new String[] {"-t", "4",BASE_PATH + "fakeTrainingDataWithKernelOutOfRange.train" });

		//Assert
		assertThat(train.getSvmParameter().kernelType, equalTo(KernelType.precomputed));
		assertThat(errContent.toString(), equalTo("Wrong kernel matrix: first column must be 0:sample_serial_number"));
		assertThat(outContent.toString(), equalTo(HelpMessages.TRAIN_HELP_MESSAGE_ON_BAD_INPUT + "\n"));
		
	}
	
	private void assertThatSvmProblemDataValuesSetCorrectly(svm_train train, int index) {
		assertThat(train.getSvmProblem().x[0][index].index, equalTo(1));
		assertThat(train.getSvmProblem().x[1][index].index, equalTo(1));
		assertThat(train.getSvmProblem().x[2][index].index, equalTo(1));
		assertThat(train.getSvmProblem().x[3][index+1].index, equalTo(2));
		assertThat(train.getSvmProblem().x[4][index+1].index, equalTo(2));
		assertThat(train.getSvmProblem().x[5][index+1].index, equalTo(2));
		assertThat(train.getSvmProblem().x[6][index].value, equalTo(0.04));
		assertThat(train.getSvmProblem().x[7][index].value, equalTo(0.43));
		assertThat(train.getSvmProblem().x[8][index].value, equalTo(0.2));
		assertThat(train.getSvmProblem().x[9][index].value, equalTo(0.9));
		assertThat(train.getSvmProblem().x[10][index].value, equalTo(0.6));
		assertThat(train.getSvmProblem().x[0][index+1].value, equalTo(0.2));
		assertThat(train.getSvmProblem().x[1][index+1].value, equalTo(0.1));
	}

	private void createSvmMock() {
		new MockUp<svm>(){
			@Mock
			public String checkSvmParameter(svm_problem prob, SvmParameter param) {
				return "a string";
			}
			
			@Mock
			public SvmModel svm_train(svm_problem prob, SvmParameter param) {
				return new SvmModel();
			}
			
			@Mock
			public void svm_save_model(String model_file_name, SvmModel model)
					throws IOException {
				return;
			}
		};
	}

	
	private void createExpectedSvmProblem() {
		SvmNode[][] expectedProblemX = new SvmNode[][]{
			{new SvmNode(1, 0.3), new SvmNode(2, 0.2)},
			{new SvmNode(1, 0.5), new SvmNode(2, 0.1)},
			{new SvmNode(1, 0.6), new SvmNode(2, 0.001)},
			{new SvmNode(1, 0.001), new SvmNode(2, 0.4)},
			{new SvmNode(1, 0.1), new SvmNode(2, 0.7)},
			{new SvmNode(1, 0.55), new SvmNode(2,0.023)},
			{new SvmNode(1, 0.04), new SvmNode(2,0.3)},
			{new SvmNode(1, 0.43), new SvmNode(2,0.1)},
			{new SvmNode(1, 0.2)},
			{new SvmNode(1, 0.9)},
			{new SvmNode(2, 0.6)}	
		};
	}
	

}
