package org.mann.libsvm.crossvalidation;

import static org.junit.Assert.*;
import static org.hamcrest.CoreMatchers.both;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.number.OrderingComparison.greaterThanOrEqualTo;
import static org.hamcrest.number.OrderingComparison.lessThanOrEqualTo;

import java.io.IOException;

import org.junit.Test;
import org.mann.libsvm.SvmParameter;
import org.mann.libsvm.SvmParameter.SvmType;
import org.mann.libsvm.SvmProblem;
import org.mann.libsvm.svm;
import org.mann.libsvm.svm_train;
import org.mann.ui.ResultCollector;

public class CrossValidatorTest {

	@Test
	public void crossValidatorShouldSetNfoldMessageWhenValidNfold() {
		ResultCollector result = new ResultCollector();
		CrossValidator cv = new CrossValidator(2, result);
		cv.checkNfold();
		assertThat(result.getResult(), equalTo("2-fold cross validation\n"));
	}

	@Test
	public void crossValidatorShouldSetErrorMessageWhenNfoldNotValid() {
		ResultCollector result = new ResultCollector();
		CrossValidator cv = new CrossValidator(1, result);
		cv.checkNfold();
		assertThat(result.getResult(), equalTo("ERROR: n-fold cross validation: n was 1 but must be >= 2\n"));
	}
	
	//TODO:need to add a persistent count of fails and check within expected
	//Expires 28th October 2015
	@Test
	public void defaultOutputsShouldBeInExpectedRange() throws IOException{
		SvmParameter svmParam = new SvmParameter();
		svmParam.setDefaultValues();
		svm_train train = setUpCrossValidation(svmParam);
		SvmProblem svmProblem = train.getSvmProblem();
		
		ResultCollector crossValResults = setUpResultCollector();
		int nFold = 2;
		CrossValidator cv = new CrossValidator(nFold, crossValResults);
		
		cv.doCrossValidation(svmProblem, svmParam);
		
		int[] iterationBounds = new int[]{3002,3782} ;
		double[] nuBounds = new double[]{0.3466453321,0.3767591501};
		double[] objBounds = new double[]{-757.7887143019, -687.0801262167};
		double[] rhoBounds = new double[]{0.1571931457,	0.2575432294};
		int[] nSvBounds = new int[]{1381,1463};
		int[] nBsvBounds = new int[]{570,658};
		int[] totalNsvBounds = new int[]{1381,1463};
		for(int iteration : crossValResults.getIterations()){			
			assertThat(iteration, both(greaterThanOrEqualTo(iterationBounds[0])).and(lessThanOrEqualTo(iterationBounds[1])));
		}
		for(double nu : crossValResults.getNus()){
			assertThat(nu, both(greaterThanOrEqualTo(nuBounds[0])).and(lessThanOrEqualTo(nuBounds[1])));
		}
		for(double obj : crossValResults.getObjs()){
			assertThat(obj, both(greaterThanOrEqualTo(objBounds[0])).and(lessThanOrEqualTo(objBounds[1])));
		}
		for(double rho : crossValResults.getRhos()){
			assertThat(rho, both(greaterThanOrEqualTo(rhoBounds[0])).and(lessThanOrEqualTo(rhoBounds[1])));
		}
		for(int nSv : crossValResults.getNSvs()){
			assertThat(nSv, both(greaterThanOrEqualTo(nSvBounds[0])).and(lessThanOrEqualTo(nSvBounds[1])));
		}
		for(int nBsv : crossValResults.getNBsvs()){
			assertThat(nBsv, both(greaterThanOrEqualTo(nBsvBounds[0])).and(lessThanOrEqualTo(nBsvBounds[1])));
		}
		for(int totalNsv : crossValResults.getTotalNsvs()){
			assertThat(totalNsv, both(greaterThanOrEqualTo(totalNsvBounds[0])).and(lessThanOrEqualTo(totalNsvBounds[1])));
		}
	}

	@Test
	public void crossValidationShouldCalculateCorrectMeanSquaredErrorWhenNuSvrAndNfold2() throws IOException {
		SvmParameter svmParam = setUpParam();
		svm_train train = setUpCrossValidation(svmParam);
		SvmProblem svmProblem = train.getSvmProblem();

		ResultCollector crossValResults = setUpResultCollector();
		int nFold = 2;
		CrossValidator cv = new CrossValidator(nFold, crossValResults);
		double[] meanSquaredErrorBounds = {0.0827474974, 0.0937332518136174};
		double[] rSquaredBounds = {0.5645463396112467, 0.6121409908};
		
		for(int i = 0; i<1; i++){

			cv.doCrossValidation(svmProblem, svmParam);
			System.out.println(crossValResults.getMeanSquaredError());
			System.out.println(crossValResults.getRSquared());
			assertThat(crossValResults.getMeanSquaredError(),
					both(greaterThanOrEqualTo(meanSquaredErrorBounds[0])).and(lessThanOrEqualTo(meanSquaredErrorBounds[1])));
			assertThat(crossValResults.getRSquared(),both(greaterThanOrEqualTo(rSquaredBounds[0])).and(lessThanOrEqualTo(rSquaredBounds[1])));
			
		}
	}
	
	@Test
	public void meanSquaredErrorCalculatedUsingApacheMathIsEqualToOriginalMethod() throws IOException{
		//calculateMeanSqErrorUsingOriginalMethod();
		fail("Not yet implemented");
		double meanSquaredError = 0;
	
	}

	private void calculateMeanSqErrorUsingOriginalMethod() throws IOException {
		SvmParameter svmParam = setUpParam();
		svm_train train = setUpCrossValidation(svmParam);
		SvmProblem svmProblem = train.getSvmProblem();

		ResultCollector crossValResults = setUpResultCollector();
		int nFold = 2;
		CrossValidator cv = new CrossValidator(nFold, crossValResults);
		
		cv.doCrossValidation(svmProblem, svmParam);
	}
	
	

	private SvmParameter setUpParam() {
		SvmParameter svmParam = new SvmParameter();
		svmParam.setDefaultValues();
		svmParam.setSvmType(SvmType.nu_svr);
		return svmParam;
	}
	
	private ResultCollector setUpResultCollector() {
		ResultCollector crossValResults = new ResultCollector();
		svm.setResultCollector(crossValResults);
		return crossValResults;
	}

	private svm_train setUpCrossValidation(SvmParameter svmParam) throws IOException {
		svm_train train = new svm_train();
		ResultCollector inputValidationResults = new ResultCollector();
		
		train.setParam(svmParam);
		train.setInputFile("src/test/resources/testdata/hfmTrainingData.train");
		train.read_problem(inputValidationResults);
		return train;
	}
}
