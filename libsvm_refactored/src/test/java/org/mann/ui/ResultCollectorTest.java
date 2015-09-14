package org.mann.ui;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.fail;

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
	public void resultCollectorShouldEndProcess(){
		fail("not yet implemented");
	}
}
