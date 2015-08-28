package org.mann.libsvm;

import static org.junit.Assert.*;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mann.libsvm.svm_predict;

public class SvmPredictRealInputTests {
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
	public void mainShouldPrintStandardOutputWhenRealInputIsGood() throws IOException {
		svm_predict.main(new String[] {"src/main/resources/heart_scale", 
				"src/test/resources/hfmTrainingData.train.model",
				"dummyout" });
		String successfulOutput = "^Accuracy = \\d+?.\\d+?% \\(\\d+?/\\d+?\\) \\(classification\\)";
		Pattern p = Pattern.compile(successfulOutput);
		Matcher m = p.matcher(outContent.toString());
		assertTrue("Expecting an accuracy result but got " + outContent.toString(), m.find());
	}
	
	@Test
	public void mainShouldPrintNothingWhenRealInputIsGoodAndSetToQuietMode() throws IOException {
		svm_predict.main(new String[] {"-q", "src/main/resources/heart_scale", 
				"src/test/resources/hfmTrainingData.train.model",
		"dummyout" });
		assertEquals("", outContent.toString());
		assertEquals("", errContent.toString());
	}

}
