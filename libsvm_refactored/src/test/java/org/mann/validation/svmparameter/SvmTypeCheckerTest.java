package org.mann.validation.svmparameter;

import org.junit.Test;
import org.mann.libsvm.SvmParameter;
import org.mann.libsvm.SvmParameter.SvmType;
import org.mann.validation.svmparameter.SvmTypeChecker;

import static org.hamcrest.CoreMatchers.containsString;
import static org.junit.Assert.assertThat;

public class SvmTypeCheckerTest {

	@Test
	public void checkParameterShouldAddInfoMessageStatingSvmType() {
		ParameterValidationManager manager = new ParameterValidationManager(new StringBuilder());
		new SvmTypeChecker(manager).checkParameter(createSvmParameter(SvmType.c_svc));
		assertThat(manager.getValidationMessage().toString(), containsString("Svm type: c_svc\n"));
	}
	
	@Test(expected = IllegalArgumentException.class)
	public void checkParameterShouldAddErrorWhenSvmTypeNotRecognised() {
		ParameterValidationManager manager = new ParameterValidationManager(new StringBuilder());
		new SvmTypeChecker(manager).checkParameter(createSvmParameter(SvmType.valueOf("Unknown")));
	}
	
	@Test
	public void checkParameterShouldThrowExceptionWhenSvmTypeNotRecognised() {
		ParameterValidationManager manager = new ParameterValidationManager(new StringBuilder());
		new SvmTypeChecker(manager).checkParameter(createSvmParameter(null));
		assertThat(manager.getValidationMessage().toString(), containsString("ERROR: Svm type not set\n"));
	}
	
	public SvmParameter createSvmParameter(SvmType svmType){
		SvmParameter params = new SvmParameter();
		params.svmType = svmType;
		return params;
	}

}
