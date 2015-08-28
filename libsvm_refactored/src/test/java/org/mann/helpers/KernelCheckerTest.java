package org.mann.helpers;

import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertThat;

import org.junit.Test;
import org.mann.helpers.KernelChecker;
import org.mann.helpers.ParameterValidationManager;
import org.mann.libsvm.SvmParameter;

public class KernelCheckerTest {

	@Test
	public void checkKernelTypeShouldReturnErrorWhenNotRecognised(){
		ParameterValidationManager manager = new ParameterValidationManager(new StringBuilder()); 
		new KernelChecker(manager).checkKernelType(-1);
		assertThat(manager.getValidationMessage().toString(), equalTo("ERROR: unknown kernel type\n"));
	}

	@Test
	public void checkParameterShouldReturnErrorWhenNotRecognised(){
		ParameterValidationManager manager = new ParameterValidationManager(new StringBuilder()); 
		new KernelChecker(manager).checkParameter(createSvmParameter());
		assertThat(manager.getValidationMessage().toString(), containsString("kernel type: 1\n"));
	}
	
	

	private SvmParameter createSvmParameter() {
		SvmParameter params = new SvmParameter();
		params.kernel_type = 1;
		return params;
	}
}
