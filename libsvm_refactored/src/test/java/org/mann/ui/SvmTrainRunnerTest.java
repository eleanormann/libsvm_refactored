package org.mann.ui;

import java.io.IOException;

import org.junit.Test;

import static org.junit.Assert.assertThat;

import org.mann.helpers.HelpMessages;
import org.mann.libsvm.svm_train;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.any;

public class SvmTrainRunnerTest {
	
	@Test
	public void runnerShouldCallSvmTrainRunMethod() throws IOException{
		//Arrange
		SvmTrainRunner runner = new SvmTrainRunner();
		svm_train train = mock(svm_train.class);
		
		//Act
		runner.run(train, new String[]{});
		
		//Assert
		verify(train).run(any(String[].class), any(ResultCollector.class));
	}
	
	@Test
	public void runnerShouldReturnResult() throws IOException{
		// Arrange
		SvmTrainRunner runner = new SvmTrainRunner();
		runner.run(new svm_train(), null);
		
		String result = runner.getResultCollector().getResult();
		assertThat(result, equalTo("ERROR: java.lang.NullPointerException\n"));
	}
}
