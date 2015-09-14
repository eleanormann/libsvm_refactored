package org.mann.ui;

import org.mann.helpers.HelpMessages;

public class ResultCollector {
	private static final String NEW_LINE = "\n";
	private static final String ERROR = "ERROR: ";
	private StringBuilder result;
	
	
	public ResultCollector(){
		this.result = new StringBuilder();
	}
	
	public void addException(Throwable exception) {
		result.append(ERROR).append(exception).append(NEW_LINE);
	}
	
	public String getResult(){
		return result.toString();
	}

	public void addHelpMessage() {
		result.append(HelpMessages.TRAIN_HELP_MESSAGE_ON_BAD_INPUT).append(NEW_LINE);
	}

	public void addError(String errorMessage) {
		result.append(ERROR).append(errorMessage).append(NEW_LINE);
	}

	public void addInfo(String infoMessage) {
		result.append(infoMessage).append(NEW_LINE);

	}
}
