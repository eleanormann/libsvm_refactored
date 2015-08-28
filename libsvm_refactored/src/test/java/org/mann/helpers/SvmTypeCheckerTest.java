package org.mann.helpers;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.instanceOf;
import static org.junit.Assert.assertThat;

import org.junit.Before;
import org.junit.Test;
import org.mann.helpers.ParameterValidationManager;
import org.mann.helpers.SvmTypeChecker;
import org.mann.libsvm.SvmParameter;
import org.mann.libsvm.SvmParameter.SvmType;

public class SvmTypeCheckerTest {
	private SvmTypeChecker checker;

	@Before
	public void setup() {
		checker = new SvmTypeChecker(new ParameterValidationManager(new StringBuilder()));
	}

	@Test
	public void checkSvmTypeShouldReturnErrorWhenNotRecognised() {
		checker.checkSvmType(-1);
		assertThat(checker.getManager().getValidationMessage().toString(), equalTo("ERROR: unknown svm type\n"));
	}

	@Test
	public void checkParameterShouldReturnParameterValidationManager() {
		checker.checkParameter(new SvmParameter());
		assertThat(checker.getManager().getValidationMessage().toString(), equalTo("ERROR: unknown svm type\n"));
	}

	

	private SvmParameter createSvmParameter() {
		SvmParameter params = new SvmParameter();
		params.svmType = SvmType.NU_SVC;
		return params;
	}
}
