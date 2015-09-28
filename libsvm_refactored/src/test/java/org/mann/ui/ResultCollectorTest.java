package org.mann.ui;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.hasItems;

import static org.junit.Assert.assertThat;
import static org.junit.Assert.fail;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.nio.CharBuffer;

import org.hamcrest.CoreMatchers;
import org.junit.Before;
import org.junit.Test;
import org.mann.helpers.HelpMessages;

public class ResultCollectorTest {
	private ResultCollector collector;

	@Before
	public void createResultCollector() {
		collector = new ResultCollector();
	}

	@Test
	public void resultCollectorShouldAddError() {
		collector.addError("");
		assertThat(collector.getResult(), equalTo("ERROR: \n"));
	}

	@Test
	public void resultCollectorShouldAddHelpMessage() {
		collector.addHelpMessage();
		assertThat(collector.getResult(), equalTo(HelpMessages.TRAIN_HELP_MESSAGE_ON_BAD_INPUT + "\n"));
	}

	@Test
	public void resultCollectorShouldAddExceptionMessage() {
		Throwable exception = new Exception();
		collector.addException(exception);
		assertThat(collector.getResult(), equalTo("ERROR: java.lang.Exception\n"));
	}
	
	@Test
	public void resultCollectorShouldAddInfoMessage(){
		collector.addInfo("Parameter set to x");
		assertThat(collector.getResult(), equalTo("Parameter set to x\n"));
	}
		
	@Test
	public void resultCollectorShouldWriteToFile() throws IOException{
		collector.addInfo("hello");
		collector.writeToFile("test.txt");
		String output = null;
		try(BufferedReader fp = new BufferedReader(new FileReader("target/output/test.txt"))){
			output = fp.readLine();
		}
		assertThat(output, equalTo("hello"));
	}
	
	@Test
	public void resultCollectorShouldAddIterationsToList(){
		collector.addIteration(234);
		collector.addIteration(342);
		assertThat(collector.getIterations(), hasItems(234, 342));
	}
}
