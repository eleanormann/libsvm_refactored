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
import org.mann.libsvm.integrationtests.OutputStreamHandler;

public class SvmPredictRealInputTests {
  //set to false for data collecting mode
  private final OutputStreamHandler outputHandler = new OutputStreamHandler(true); 
  
  @Before
  public void setUpOutputStream() {
      outputHandler.setUpOutputStream();
      outputHandler.setUpErrorStream();
  }

  @After
  public void cleanUpObjects() {
      outputHandler.resetOutput();
      outputHandler.resetErrorOutput();
  }
	@Test
	public void mainShouldPrintStandardOutputWhenRealInputIsGood() throws IOException {
		svm_predict.main(new String[] {"src/main/resources/heart_scale", 
				"src/test/resources/hfmTrainingData.train.model",
				"dummyout" });
		String successfulOutput = "^Accuracy = \\d+?.\\d+?% \\(\\d+?/\\d+?\\) \\(classification\\)";
		Pattern p = Pattern.compile(successfulOutput);
		String output = outputHandler.getOutContent().toString();
        Matcher m = p.matcher(output);
		assertTrue("Expecting an accuracy result but got " + output, m.find());
	}
	
	@Test
	public void mainShouldPrintNothingWhenRealInputIsGoodAndSetToQuietMode() throws IOException {
		svm_predict.main(new String[] {"-q", "src/main/resources/heart_scale", 
				"src/test/resources/hfmTrainingData.train.model",
		"dummyout" });
		assertEquals("", outputHandler.getOutContent().toString());
		assertEquals("", outputHandler.getErrContent().toString());
	}

}
