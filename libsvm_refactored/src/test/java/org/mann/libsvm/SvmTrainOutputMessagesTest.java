package org.mann.libsvm;

import static org.junit.Assert.assertEquals;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintStream;

import mockit.Mock;
import mockit.MockUp;
import mockit.integration.junit4.JMockit;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mann.helpers.HelpMessages;
import org.mann.libsvm.SvmModel;
import org.mann.libsvm.SvmParameter;
import org.mann.libsvm.svm;
import org.mann.libsvm.svm_problem;
import org.mann.libsvm.svm_train;

@RunWith(JMockit.class)
public class SvmTrainOutputMessagesTest {

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
	public void parseCommandLineShouldPrintBadInputMessageWhenNoFilenameInputWithGoodOptions() throws IOException {
		svm_train.main(new String[] { "-v", "3" });
		assertEquals("No file has been specified", errContent.toString().trim());
		assertEquals(HelpMessages.TRAIN_HELP_MESSAGE_ON_BAD_INPUT, outContent.toString().trim());
	}

	@Test
	public void parseCommandLineShouldPrintBadInputMessageWhenOptionOnItsOwnPassed() throws IOException {
		svm_train.main(new String[] {"-q"});
		assertEquals("option on its own is not valid input", errContent.toString().trim());
		assertEquals(HelpMessages.TRAIN_HELP_MESSAGE_ON_BAD_INPUT, outContent.toString().trim());
	}
	
	@Test
	public void parseCommandLineShouldPrintBadInputMessageWhenNFoldLessThanTwo() throws IOException {
		svm_train.main(new String[] {"-v", "1"});
		assertEquals("n-fold cross validation: n must >= 2", errContent.toString().trim());
		assertEquals(HelpMessages.TRAIN_HELP_MESSAGE_ON_BAD_INPUT, outContent.toString().trim());
	}
	
	@Test
	public void parseCommandLineShouldPrintBadInputMessageWhenOptionNotRecognised() throws IOException{
		svm_train.main(new String[] {"-u", "0"});
		assertEquals("Unknown option: -u", errContent.toString().trim());
		assertEquals(HelpMessages.TRAIN_HELP_MESSAGE_ON_BAD_INPUT, outContent.toString().trim());
	}
	
	@Test
	public void readProblemShouldPrintBadInputWhenInvalidFilePassed() throws IOException{

		new MockUp<svm>(){
			@Mock public SvmModel svm_train(svm_problem prob, SvmParameter param) {
				return new SvmModel();
			}
			
			@Mock public void svm_save_model(String model_file_name, SvmModel model) throws IOException {}
		};
			 
		svm_train.main(new String[]{"invalid filename"});
		assertEquals("invalid filename (No such file or directory)", errContent.toString().trim());
		assertEquals(HelpMessages.TRAIN_HELP_MESSAGE_ON_BAD_INPUT, outContent.toString().trim());
	}
}
