package org.mann.ui;

import static org.junit.Assert.assertEquals;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mann.helpers.HelpMessages;
import org.mann.libsvm.integrationtests.OutputStreamHandler;
import org.mann.ui.SvmPrinterFactory;
import org.mann.ui.SvmPrinterFactory.PrintMode;

public class SvmPrinterTest {
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
	public void quietModeShouldPrintNothing() {
		SvmPrinterFactory.getPrinter(PrintMode.QUIET).print("anything");
		assertEquals("", outputHandler.getOutContent().toString());
	}
	
	@Test 
	public void inputErrorForPredictShouldPrintPredictInputErrorMessage(){
		SvmPrinterFactory.getPrinter(PrintMode.PREDICT_BAD_INPUT).print("anything");
		assertEquals("anything", outputHandler.getErrContent().toString());
		assertEquals(HelpMessages.PREDICT_HELP_MESSAGE_ON_BAD_INPUT, outputHandler.getOutContent().toString().trim());
	}

	@Test
	public void standardModeShouldPrintParameterString() {
		SvmPrinterFactory.getPrinter(PrintMode.STANDARD).print("anything");
		SvmPrinterFactory.getPrinter(PrintMode.STANDARD).print("on");
		SvmPrinterFactory.getPrinter(PrintMode.STANDARD).print("new line");
		assertEquals("anything\non\nnew line", outputHandler.getOutContent().toString().trim());
	}
	
	@Test
	public void inputErrorForTrainModeShouldPrintTrainErrorMessage() {
		SvmPrinterFactory.getPrinter(PrintMode.TRAIN_BAD_INPUT).print("anything");
		assertEquals("anything", outputHandler.getErrContent().toString().trim());
		assertEquals(HelpMessages.TRAIN_HELP_MESSAGE_ON_BAD_INPUT, outputHandler.getOutContent().toString().trim());
	}
}
