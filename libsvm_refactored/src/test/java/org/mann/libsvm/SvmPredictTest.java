package org.mann.libsvm;

import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintStream;

import mockit.Mock;
import mockit.MockUp;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mann.libsvm.SvmModel;
import org.mann.libsvm.SvmParameter.SvmType;
import org.mann.libsvm.svm;
import org.mann.libsvm.svm_predict;

public class SvmPredictTest {
	
	//TODO: remove this way of checking and replace with mockito methods
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
	public void mainShouldReachPredictMethodWhenInputIsValid() throws IOException{
		setupMocksSoMethodReachesPredictCall();
		svm_predict.main(new String[] {"input file", "model file", "output file"});
		assertEquals("Accuracy = NaN% (0/0) (classification)\n", outContent.toString());
	}
	
	@Test
	public void predictShouldPrintWhenSvmTypeIsEpsilon() throws IOException{
		DataOutputStream output = mock(DataOutputStream.class);
		BufferedReader input = mock(BufferedReader.class);
		when(input.readLine()).thenReturn(null);
		SvmModel model = mockSvmModel(SvmType.epsilon_svr); 
		svm_predict.predict(input, output, model, 0);
		assertEquals("Mean squared error = NaN (regression)\nSquared correlation coefficient = NaN (regression)\n", outContent.toString());
	}

	@Test
	public void predictShouldPrintWhenSvmTypeIsCSvc() throws IOException{
		DataOutputStream output = mock(DataOutputStream.class);
		BufferedReader input = mock(BufferedReader.class);
		when(input.readLine()).thenReturn(null);
		SvmModel model = mockSvmModel(SvmType.c_svc); 
		svm_predict.predict(input, output, model, 0);
		assertEquals("Accuracy = NaN% (0/0) (classification)\n", outContent.toString());
	}
	
	private void setupMocksSoMethodReachesPredictCall() {
		final BufferedReader input = mock(BufferedReader.class);
		new MockUp<svm_predict>(){
			@Mock
			private BufferedReader createReader(String filename) throws FileNotFoundException {
				return input;
			}
		};
		mockSvmModel(null);
	}

	private SvmModel mockSvmModel(final SvmType type) {
		final SvmModel model = mock(SvmModel.class);
		new MockUp<svm>(){

			@Mock
			public SvmModel svm_load_model(String model_file_name) throws IOException {
				return model;	
			}
			
			@Mock
			public int svm_check_probability_model(SvmModel model){
				return 0;
			}
			
			@Mock
			public SvmType getSvmTypeFromModel(SvmModel model){
				return type;
			}

		};
		return model;
	}
}
