package org.mann.libsvm.crossvalidation;

import static org.junit.Assert.*;
import static org.hamcrest.CoreMatchers.both;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.number.OrderingComparison.greaterThanOrEqualTo;
import static org.hamcrest.number.OrderingComparison.lessThanOrEqualTo;

import java.io.IOException;

import org.junit.Test;
import org.mann.libsvm.SvmParameter;
import org.mann.libsvm.svm;
import org.mann.libsvm.svm_problem;
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
	public void iterationsShouldBeInExpectedRange() throws IOException{
		svm_train train = new svm_train();
		ResultCollector inputValidationResults = new ResultCollector();
		
		SvmParameter svmParam = new SvmParameter();
		svmParam.setDefaultValues();
		train.setParam(svmParam);
		train.setInputFile("src/test/resources/testdata/hfmTrainingData.train");
		train.read_problem(inputValidationResults);
		svm_problem svmProblem = train.getSvmProblem();
		
		ResultCollector crossValResults = new ResultCollector();
		svm.setResultCollector(crossValResults);
		int nFold = 2;
		CrossValidator cv = new CrossValidator(nFold, crossValResults);
		
		cv.doCrossValidation(svmProblem, svmParam);
		int[] iterationBounds = new int[]{3002,3782} ;
		double[] nuBounds = new double[]{0.3466453321,0.3767591501};
		double[] objBounds = new double[]{};
		double[] rhoBounds = new double[]{};
		int[] nSvBounds = new int[]{};
		int[] nBsvBounds = new int[]{};
		int[] totalNsvBounds = new int[]{};
		for(int iteration : crossValResults.getIterations()){			
			assertThat(iteration, both(greaterThanOrEqualTo(iterationBounds[0])).and(lessThanOrEqualTo(iterationBounds[1])));
		}
		for(double nu : crossValResults.getNu()){
			assertThat(nu, both(greaterThanOrEqualTo(nuBounds[0])).and(lessThanOrEqualTo(nuBounds[1])));
		}
		for(double obj : crossValResults.getObj()){
			assertThat(obj, both(greaterThanOrEqualTo(objBounds[0])).and(lessThanOrEqualTo(objBounds[1])));
		}
		for(double rho : crossValResults.getRho()){
			assertThat(rho, both(greaterThanOrEqualTo(rhoBounds[0])).and(lessThanOrEqualTo(rhoBounds[1])));
		}
		for(int nSv : crossValResults.getNSv()){
			assertThat(nSv, both(greaterThanOrEqualTo(nSvBounds[0])).and(lessThanOrEqualTo(nSvBounds[1])));
		}
		for(int nBsv : crossValResults.getNBsv()){
			assertThat(nBsv, both(greaterThanOrEqualTo(nBsvBounds[0])).and(lessThanOrEqualTo(nBsvBounds[1])));
		}
		for(int totalNsv : crossValResults.getTotalNsv()){
			assertThat(totalNsv, both(greaterThanOrEqualTo(totalNsvBounds[0])).and(lessThanOrEqualTo(totalNsvBounds[1])));
		}
	}
	
	@Test
	public void rhoShouldBeInExpectedRange(){
		
	}
	
}
