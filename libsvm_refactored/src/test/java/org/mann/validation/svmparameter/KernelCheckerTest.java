package org.mann.validation.svmparameter;

import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertThat;

import org.junit.Test;
import org.mann.libsvm.SvmParameter;
import org.mann.validation.svmparameter.KernelChecker;

public class KernelCheckerTest {

	@Test
	public void checkKernelTypeShouldAddErrorMessageWhenKernelNotRecognised(){
		ParameterValidationManager manager = new ParameterValidationManager(new StringBuilder()); 
		new KernelChecker(manager).checkKernelType(-1);
		assertThat(manager.getValidationMessage().toString(), equalTo("ERROR: unknown kernel type\n"));
	}

	@Test
	public void checkParameterShouldAddErrorMessageWhenKernelNotRecognised(){
		ParameterValidationManager manager = new ParameterValidationManager(new StringBuilder()); 
		new KernelChecker(manager).checkParameter(createSvmParameter(-1));
		assertThat(manager.getValidationMessage().toString(), containsString("ERROR: unknown kernel type\n"));
	}
	
	@Test
	public void checkKernelTypeShouldAddInfoMessageWhenKernelRecognised(){
		ParameterValidationManager manager = new ParameterValidationManager(new StringBuilder()); 
		new KernelChecker(manager).checkKernelType(1);
		assertThat(manager.getValidationMessage().toString(), equalTo("kernel type: 1\n"));
	}

	@Test
	public void checkParameterShouldAddInfoMessageWhenKernelRecognised(){
		ParameterValidationManager manager = new ParameterValidationManager(new StringBuilder()); 
		new KernelChecker(manager).checkParameter(createSvmParameter(1));
		assertThat(manager.getValidationMessage().toString(), containsString("kernel type: 1\n"));
	}

	private SvmParameter createSvmParameter(int kernelType) {
		SvmParameter params = new SvmParameter();
		params.kernel_type = kernelType;
		return params;
	}
}
