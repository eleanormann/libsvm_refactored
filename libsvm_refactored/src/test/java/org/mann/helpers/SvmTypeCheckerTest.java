package org.mann.helpers;

import org.junit.Test;
import org.mann.libsvm.SvmParameter;
import org.mann.libsvm.SvmParameter.SvmType;
import static org.hamcrest.CoreMatchers.containsString;
import static org.junit.Assert.assertThat;

public class SvmTypeCheckerTest {

	@Test
	public void checkParameterShouldAddInfoMessageStatingSvmType() {
		ParameterValidationManager manager = new ParameterValidationManager(new StringBuilder());
		new SvmTypeChecker(manager).checkParameter(createSvmParameter());
		assertThat(manager.getValidationMessage().toString(), containsString("Svm Type: C_SVC\n"));
	}
	
	public SvmParameter createSvmParameter(){
		SvmParameter params = new SvmParameter();
		params.svmType = SvmType.C_SVC;
		return params;
	}

}
