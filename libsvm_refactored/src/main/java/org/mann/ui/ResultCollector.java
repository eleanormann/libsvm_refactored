package org.mann.ui;

import java.io.BufferedOutputStream;
import java.io.DataOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.mann.helpers.HelpMessages;
import org.mann.libsvm.svm;

public class ResultCollector {
	private static final String NEW_LINE = "\n";
	private static final String ERROR = "ERROR: ";
	private static final String COMMA = ",";
	private StringBuilder result;

	private List<Integer> iterations;
	private List<Double> nus;
	private List<Double> objs;
	private List<Double> rhos;
	private List<Integer> nSvs;
	private List<Integer> nBSvs;
	private List<Integer> totalNSvs;
	private double meanSquaredError;
	private double rSquared;
	private double totalAccuracy;
	private StringBuilder svmPrintString;

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

	public void addDeprecatedTotalNsv(String totalNsv) {
		result.append(totalNsv).append(NEW_LINE);
	}

	public void writeToFile(String filename) {
		try (FileOutputStream fos = new FileOutputStream("target/output/" + filename, true);
				DataOutputStream out = new DataOutputStream((new BufferedOutputStream(fos)))) {
			out.writeBytes(result.toString());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void addIteration(int iteration) {
		if (iterations == null) {
			iterations = new ArrayList<>();
		}
		iterations.add(iteration);
	}

	public List<Integer> getIterations() {
		return iterations;
	}

	public void addNu(double nu) {
		if (nus == null) {
			nus = new ArrayList<>();
		}
		nus.add(nu);
	}

	public List<Double> getNus() {
		return nus;
	}

	public void addObj(double obj) {
		if (objs == null) {
			objs = new ArrayList<>();
		}
		objs.add(obj);
	}

	public List<Double> getObjs() {
		return objs;
	}

	public void addRho(double rho) {
		if (rhos == null) {
			rhos = new ArrayList<>();
		}
		rhos.add(rho);
	}

	public List<Double> getRhos() {
		return rhos;
	}

	public void addNSv(int nSv) {
		if (nSvs == null) {
			nSvs = new ArrayList<>();
		}
		nSvs.add(nSv);
	}

	public List<Integer> getNSvs() {
		return nSvs;
	}

	public void addNBSv(int nBSv) {
		if (nBSvs == null) {
			nBSvs = new ArrayList<>();
		}
		nBSvs.add(nBSv);
	}

	public List<Integer> getNBsvs() {
		return nBSvs;
	}

	public void addTotalNSv(int totalNSv) {
		if (totalNSvs == null) {
			totalNSvs = new ArrayList<>();
		}
		totalNSvs.add(totalNSv);
	}

	public List<Integer> getTotalNsvs() {
		return totalNSvs;
	}

	public void addMeanSqError(double meanSqError) {
		this.meanSquaredError = meanSqError;
	}

	public void addRSquared(double rSquared) {
		this.rSquared = rSquared;
	}

	public double getMeanSquaredError() {
		return meanSquaredError;
	}

	public double getRSquared() {
		return rSquared;
	}

	public void addTotalAccuracy(double accuracy) {
		this.totalAccuracy = accuracy;
	}

	public double getTotalAccuracy() {
		return totalAccuracy;
	}

	public void addConsoleOutput(String descriptor, String value, boolean newline) {
		if (svmPrintString == null) {
			svmPrintString = new StringBuilder();
		}
		svmPrintString.append(descriptor == null ? "" : descriptor)
					  .append(value)
					  .append(newline == true ? NEW_LINE : "");
	}

	public String getConsoleOutput() {
		return svmPrintString.toString();
	}
}
