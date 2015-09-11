package org.mann.ui;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertThat;

import org.junit.Test;
import org.mann.helpers.HelpMessages;

public class ResultCollectorTest {

	@Test
	public void resultCollectorShouldAddErrorAndException() {
		ResultCollector collector = new ResultCollector();
		Throwable error = new IllegalArgumentException("Param not recognised");
		collector.addError(error);
		assertThat(collector.getResult(), equalTo("ERROR: java.lang.IllegalArgumentException: Param not recognised\n"
				+ HelpMessages.TRAIN_HELP_MESSAGE_ON_BAD_INPUT + "\n"));
	}

	@Test
	public void resultCollectorShouldAddHelpMessage() {
		ResultCollector collector = new ResultCollector();
		collector.addHelpMessage();
		assertThat(collector.getResult(), equalTo(HelpMessages.TRAIN_HELP_MESSAGE_ON_BAD_INPUT + "\n"));
	}
}
