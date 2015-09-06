package org.mann.helpers;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertThat;

import org.junit.Test;
import org.mann.helpers.HelpMessages;

public class HelpMessagesTest {

	@Test
	public void crossValMseMessageShouldReturnParameterisedMessage() {
		String crossValMseMsg = String.format(HelpMessages.CROSS_VALIDATION_MSE, "0.034");
		assertThat(crossValMseMsg, equalTo("Cross Validation Mean squared error = 0.034"));
	}

	//unit test exitWithHelp
}
