package org.mann.libsvm.integrationtests;

import static org.junit.Assert.*;
import static org.hamcrest.CoreMatchers.equalTo;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintStream;

import org.mann.libsvm.svm;
import org.mann.libsvm.svm_train;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class SvmTrainEndToEndTest {
	//private final ByteArrayOutputStream outContent = new ByteArrayOutputStream();
	private File resultsFile = new File("target/output/cross-validation-output.csv");
	
	@Before
	public void setUpOutputStream() {
		//System.setOut(new PrintStream(outContent));
		if(resultsFile.exists()){
			resultsFile.delete();
		}
	}

	@After
	public void cleanUpObjects() {
	//	System.setOut(null);	
	}

	@Test
	public void svmTrainShouldProduceExpectedResults() throws IOException {
		String expected = "......*\n"
				+ "optimization finished, #iter = 6197\n"
				+ "nu = 0.31647823832979366\n"
				+ "obj = -1300.2536635959898, rho = 0.28709349878580875\n"
				+ "nSV = 2448, nBSV = 1183\n"
				+ "Total nSV = 2448\n";
		svm_train.main(new String[]{"-s", "c_svc", "-w", "2", "34","src/test/resources/testdata/hfmTrainingData.train"});
	//	assertEquals(expected, outContent.toString());
	}
	
	
	/*
	 * poly, degree, coef0, gamma 
	 */
	
	@Test 
	public void svmTrainShouldProduceExpectedResultsForPolyKernel() throws IOException{
		svm_train.main(new String[]{"-t", "poly", "-d", "2", "-g", "0.33", "-r", "0.3", "src/test/resources/testdata/hfmTrainingData.train" });
		//assertEquals("", outContent.toString());
	}
	/*Cross validation
	 * 
	 */
	
	//I only have one training set at the moment so this needs more robust param setting; 
	//just setting up the infrastructure 
	//TODO: create confidence intervals from multiple training sets
	//Expires 20th October 2015
	@Test
	public void crossValidationShouldReturnResultsWithinConfidenceIntervals() throws IOException{
		svm_train.main(setCrossValidationConfig(5));
		
	}

	@Test
	public void svmTrainShouldProducedExpectedResultsForCrossValidation() throws IOException{
//		svm_train.main(setCrossValidationConfig(50));	
//		StringBuilder results = loadCrossValidationResults();
//		assertThat(results.toString(), equalTo(""));
	}

	private String[] setCrossValidationConfig(int times){
		return new String[]{"-v", String.valueOf(times), "src/test/resources/testdata/hfmTrainingData.train"};
	}

	private StringBuilder loadCrossValidationResults() throws IOException, FileNotFoundException {
		StringBuilder results = new StringBuilder();
		try (BufferedReader br = new BufferedReader(new FileReader(resultsFile))){
			String line = br.readLine();
			while(line != null){
				results.append(line).append("\n");
				line = br.readLine();
			}
		}
		return results;
	}
}
