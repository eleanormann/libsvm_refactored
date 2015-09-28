package org.mann.ui;

import java.io.BufferedOutputStream;
import java.io.DataOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.mann.helpers.HelpMessages;

public class ResultCollector {
	private static final String NEW_LINE = "\n";
	private static final String ERROR = "ERROR: ";
	private static final String COMMA = ",";
	private StringBuilder result;
	
	private List<Integer> iterations;

	public ResultCollector() {
		this.result = new StringBuilder();
	}

	public void addException(Throwable exception) {
		result.append(ERROR).append(exception).append(NEW_LINE);
	}

	public String getResult() {
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

	public void addCrossValResult(String crossValOutput) {
		result.append(crossValOutput).append(COMMA);
	}
	
	public void addTotalNsv(String totalNsv) {
		result.append(totalNsv).append(NEW_LINE);
	}

	
	public void writeToFile(String filename){
		try (FileOutputStream fos = new FileOutputStream("target/output/" + filename, true);
				DataOutputStream out = new DataOutputStream((new BufferedOutputStream(fos)))){
			out.writeBytes(result.toString());
		}catch(IOException e){
			e.printStackTrace();
		}
	}

	public void addIteration(int iteration) {
		if(iterations==null){
			iterations = new ArrayList<>();	
		}
		iterations.add(iteration);
	}

	public List<Integer> getIterations() {
		return iterations;
	}
	
}
