package org.mann.libsvm;

import java.io.IOException;

import org.junit.Test;

public class SvmToyTest {

	@Test
	public void svmToyShouldCompile() throws IOException{
		svm_toy svmToy = new svm_toy();
		svmToy.button_run_clicked("");
	}

}
