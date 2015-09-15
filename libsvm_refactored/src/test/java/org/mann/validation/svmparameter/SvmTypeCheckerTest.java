package org.mann.validation.svmparameter;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.mann.libsvm.SvmParameter;
import org.mann.libsvm.SvmParameter.SvmType;
import org.mann.validation.Checker;
import org.mann.validation.svmparameter.SvmTypeChecker;

import static org.hamcrest.CoreMatchers.containsString;
import static org.junit.Assert.assertThat;

public class SvmTypeCheckerTest {

	@Rule
    public ExpectedException thrown = ExpectedException.none();
	
	@Test
	public void checkParameterShouldAddInfoMessageStatingSvmType() {
		ParameterValidationManager manager = new ParameterValidationManager(new StringBuilder());
		new SvmTypeChecker(manager).checkParameter(createSvmParameter(SvmType.c_svc));
		assertThat(manager.getValidationMessage().toString(), containsString("Svm type: c_svc\n"));
	}
	
	@Test
	public void checkParameterShouldThrowExceptionWhenSvmTypeNotRecognised() {
		thrown.expect(IllegalArgumentException.class);
		thrown.expectMessage("No enum constant org.mann.libsvm.SvmParameter.SvmType.Unknown");
		ParameterValidationManager manager = new ParameterValidationManager(new StringBuilder());
		SvmType svmType = SvmType.valueOf(SvmType.class, "Unknown");
		new SvmTypeChecker(manager).checkParameter(createSvmParameter(svmType));
	}
	
	@Test
	public void checkParameterShouldThrowExceptionWhenSvmTypeNull() {
		ParameterValidationManager manager = new ParameterValidationManager(new StringBuilder());
		new SvmTypeChecker(manager).checkParameter(createSvmParameter(null));
		assertThat(manager.getValidationMessage().toString(), containsString("Svm type not set\n"));
	}
	
	public SvmParameter createSvmParameter(SvmType svmType){
		SvmParameter params = new SvmParameter();
		params.setSvmType(svmType);
		return params;
	}

}
