package org.mann.libsvm;

import static org.junit.Assert.assertEquals;

import java.io.IOException;

import mockit.Mock;
import mockit.MockUp;
import mockit.integration.junit4.JMockit;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mann.helpers.HelpMessages;
import org.mann.ui.ResultCollector;
import org.mann.ui.SvmTrainRunner;

@RunWith(JMockit.class)
public class SvmTrainOutputMessagesTest {
	private ResultCollector result;
	private svm_train train;

	@Before
	public void setupResultCollector() {
		result = new ResultCollector();
		train = new svm_train();
	}

	@Test
	public void parseCommandLineShouldPrintBadInputMessageWhenNoFilenameInputWithGoodOptions() throws IOException {
		train.run(new String[] { "-v", "3" }, result);
		assertEquals("ERROR: java.lang.IllegalArgumentException: ERROR: No file has been specified\n"
				+ HelpMessages.TRAIN_HELP_MESSAGE_ON_BAD_INPUT + "\n", result.getResult());
	}

	@Test
	public void parseCommandLineShouldPrintBadInputMessageWhenOptionOnItsOwnPassed() throws IOException {
		train.run(new String[] { "-q" }, result);
		assertEquals("ERROR: java.lang.IllegalArgumentException: ERROR: option on its own is not valid input\n"
				+ HelpMessages.TRAIN_HELP_MESSAGE_ON_BAD_INPUT + "\n", result.getResult());
	}

	@Test
	public void parseCommandLineShouldPrintBadInputMessageWhenNFoldLessThanTwo() throws IOException {
		new svm_train().run(new String[] { "-v", "1" }, result);
		assertEquals("ERROR: java.lang.IllegalArgumentException: ERROR: n-fold cross validation: n must >= 2\n"
				+ HelpMessages.TRAIN_HELP_MESSAGE_ON_BAD_INPUT + "\n", result.getResult());
	}

	@Test
	public void parseCommandLineShouldPrintBadInputMessageWhenOptionNotRecognised() throws IOException {
		train.run(new String[] { "-u", "0" }, result);
		assertEquals("ERROR: java.lang.IllegalArgumentException: ERROR: Unknown option: -u\n"
				+ HelpMessages.TRAIN_HELP_MESSAGE_ON_BAD_INPUT + "\n", result.getResult());
	}

	@Test
	public void readProblemShouldPrintBadInputWhenInvalidFilePassed() throws IOException {

		new MockUp<svm>() {
			@Mock
			public SvmModel svm_train(svm_problem prob, SvmParameter param) {
				return new SvmModel();
			}

			@Mock
			public void svm_save_model(String model_file_name, SvmModel model) throws IOException {
			}
		};

		train.run(new String[] { "invalid filename" }, result);
		assertEquals("ERROR: java.io.FileNotFoundException: invalid filename (No such file or directory)\n"
				+ HelpMessages.TRAIN_HELP_MESSAGE_ON_BAD_INPUT + "\n", result.getResult());
	}
}
