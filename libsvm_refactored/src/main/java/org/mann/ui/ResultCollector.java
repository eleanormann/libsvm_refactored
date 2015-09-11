package org.mann.ui;

import org.mann.helpers.HelpMessages;

public class ResultCollector {
	private StringBuilder result;
	
	public ResultCollector(){
		this.result = new StringBuilder();
	}
	
	public void addError(Throwable error) {
		result.append("ERROR: " + error + "\n");
		addHelpMessage();
	}
	
	public String getResult(){
		return result.toString();
	}

	public void addHelpMessage() {
		result.append(HelpMessages.TRAIN_HELP_MESSAGE_ON_BAD_INPUT + "\n");
	}
}
