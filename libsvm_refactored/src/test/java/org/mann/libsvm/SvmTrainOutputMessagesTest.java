package org.mann.libsvm;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;
import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.CoreMatchers.equalTo;

import java.io.IOException;

import mockit.Mock;
import mockit.MockUp;
import mockit.integration.junit4.JMockit;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mann.helpers.HelpMessages;
import org.mann.ui.ResultCollector;

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
	public void parseCommandLineShouldAddErrorMessageWhenNoFilenameInputWithGoodOptions() throws IOException {
		train.run(new String[] { "-v", "3" }, result);
		assertThat(result.getResult(), containsString("ERROR: No file has been specified\n"));
	}

	@Test
	public void parseCommandLineShouldAddErrorMessageWhenNFoldLessThanTwo() throws IOException {
		new svm_train().run(new String[] { "-v", "1" }, result);
		assertThat(result.getResult(), containsString("ERROR: n-fold cross validation: n must >= 2\n"));
	}

	@Test
	public void parseCommandLineShouldAddExceptionMessageWhenOptionNotRecognised() throws IOException {
		train.run(new String[] { "-u", "0" }, result);
		assertThat(result.getResult(), containsString("ERROR: org.apache.commons.cli.UnrecognizedOptionException: Unrecognized option: -u"));
	}

	@Test
	public void readProblemShouldAddErrorMessageWhenInvalidFilePassed() throws IOException {

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
		assertThat(result.getResult(), containsString("ERROR: java.io.FileNotFoundException: invalid filename (No such file or directory)\n"));
	}
}
