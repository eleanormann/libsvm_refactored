package org.mann.helpers;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertThat;

import org.junit.Before;
import org.junit.Test;
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
	public void checkParameterShouldAddErrorMessageWhenSvmTypeIsInvalid() {
		checker.checkParameter(createSvmParameter());
		assertThat(checker.getManager().getValidationMessage().toString(), equalTo("ERROR: unknown svm type\n"));
	}

	@Test
	public void checkParameterShouldAddValidMessageWhenSvmTypeIsValid() {
		checker.checkParameter(createSvmParameter());
		assertThat(checker.getManager().getValidationMessage().toString(), equalTo("Svm type: NU_SVC\n"));
	}

	private SvmParameter createSvmParameter() {
		SvmParameter params = new SvmParameter();
		params.svmType = SvmType.NU_SVC;
		return params;
	}
}
