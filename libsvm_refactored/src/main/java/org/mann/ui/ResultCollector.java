package org.mann.ui;

import org.mann.helpers.HelpMessages;

public class ResultCollector {
	private StringBuilder result;
	
	
	public ResultCollector(){
		this.result = new StringBuilder();
	}
	
	public void addException(Throwable exception) {
		result.append("ERROR: " + exception + "\n");
	}
	
	public String getResult(){
		return result.toString();
	}

	public void addHelpMessage() {
		result.append(HelpMessages.TRAIN_HELP_MESSAGE_ON_BAD_INPUT + "\n");
	}

	public void addError(String errorMessage) {
		result.append("ERROR: " + errorMessage + "\n");
	}
}
