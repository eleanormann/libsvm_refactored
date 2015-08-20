package libsvm;

import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;

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
		assertEquals("Reached predict method when input passed the validation checks", outContent.toString().trim());
	}

	private void setupMocksSoMethodReachesPredictCall() {
		final BufferedReader input = mock(BufferedReader.class);
		new MockUp<svm_predict>(){
			@Mock
			private BufferedReader createReader(String filename) throws FileNotFoundException {
				return input;
			}
			
			@Mock
			private void predict(BufferedReader input, DataOutputStream output, SvmModel model, int predict_probability)
					throws IOException {
				System.out.println("Reached predict method when input passed the validation checks");
			}
		};
		
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

		};
	}
}
