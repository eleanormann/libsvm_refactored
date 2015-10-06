package org.mann.ui;

import java.io.IOException;

import org.mann.libsvm.svm_train;

public class SvmTrainRunner {
	private ResultCollector resultCollector;
	
	
	protected ResultCollector createResultCollector(){
		return new ResultCollector();
	}
	
	public void run(svm_train train, String[] runConfig) throws IOException{
		resultCollector = createResultCollector();
		train.run(runConfig, resultCollector);
	}

	public void addError(ResultCollector resultCollector) {
		System.out.println(resultCollector);
	}
	
	public ResultCollector getResultCollector(){
		return resultCollector;
	}

}
