package ui;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;
import helpers.HelpMessages;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import ui.SvmPrinterFactory.PrintMode;

public class SvmPrinterTest {

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
	public void quietModeShouldPrintNothing() {
		SvmPrinterFactory.getPrinter(PrintMode.QUIET).print("anything");
		assertEquals("", outContent.toString());
	}
	
	@Test 
	public void inputErrorForPredictShouldPrintPredictInputErrorMessage(){
		SvmPrinterFactory.getPrinter(PrintMode.PREDICT_BAD_INPUT).print("anything");
		assertEquals("anything", errContent.toString());
		assertEquals(HelpMessages.PREDICT_HELP_MESSAGE_ON_BAD_INPUT, outContent.toString().trim());
	}

	@Test
	public void standardModeShouldPrintParameterString() {
		SvmPrinterFactory.getPrinter(PrintMode.STANDARD).print("anything");
		assertEquals("anything", outContent.toString().trim());
	}
}
