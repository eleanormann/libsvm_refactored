package org.mann.libsvm;

import static org.hamcrest.CoreMatchers.endsWith;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintStream;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mann.helpers.HelpMessages;

public class SvmPredictOutputMessagesTest {

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
	public void mainShouldPrintBadInputMessageTooFewParametersPassed() throws IOException {
		svm_predict.main(new String[] { "Only one parameter" });
		assertEquals(HelpMessages.PREDICT_HELP_MESSAGE_ON_BAD_INPUT +"\n", outContent.toString());
	}

	@Test
	public void shouldPrintErrorWhenModelFileCannotBeFound() throws IOException {
		svm_predict.main(new String[] { "src/main/resources/heart_scale", "dummy", "dummy" });
		assertThat(errContent.toString(), endsWith("can't open model file dummy"));
	}

	@Test
	public void mainShouldPrintUnknownOptionMessageAndBadInputMessageWhenUnknownOptionAdded() throws IOException {
		svm_predict.main(new String[] { "-u" });
		assertEquals("Unknown option: -u\n", errContent.toString());
		assertEquals(HelpMessages.PREDICT_HELP_MESSAGE_ON_BAD_INPUT +"\n", outContent.toString());
	}

	@Test
	public void mainShouldPrintBadInputMessageWhenFileCouldNotBeLoaded() throws IOException {
		svm_predict.main(new String[] { "badinput.train", "anyString", "anyString" });
		assertEquals(new FileNotFoundException().toString() + ": badinput.train (No such file or directory)",
				errContent.toString());
		assertEquals(HelpMessages.PREDICT_HELP_MESSAGE_ON_BAD_INPUT +"\n", outContent.toString());
	}

}
