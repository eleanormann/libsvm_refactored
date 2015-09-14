package org.mann.validation.svmparameter;

import static org.hamcrest.CoreMatchers.containsString;
import static org.junit.Assert.assertThat;

import org.junit.Test;
import org.mann.libsvm.SvmParameter;
import org.mann.libsvm.SvmParameter.KernelType;
import org.mann.validation.svmparameter.KernelChecker;

public class KernelCheckerTest {

	//Shouldn't happen because exception would be thrown when creating SvmParameter
	@Test(expected = IllegalArgumentException.class)
	public void checkParameterShouldThrowExceptionWhenKernelNotRecognised(){
		ParameterValidationManager manager = new ParameterValidationManager(new StringBuilder()); 
		new KernelChecker(manager).checkParameter(createSvmParameter(KernelType.valueOf("badgers")));
	}

	@Test
	public void checkParameterShouldAddErrorMessageWhenKernelNull(){
		ParameterValidationManager manager = new ParameterValidationManager(new StringBuilder()); 
		new KernelChecker(manager).checkParameter(createSvmParameter(null));
		assertThat(manager.getValidationMessage().toString(), containsString("ERROR: kernel type not set\n"));
	}
	
	@Test
	public void checkParameterShouldAddInfoMessageWhenKernelRecognised(){
		ParameterValidationManager manager = new ParameterValidationManager(new StringBuilder()); 
		new KernelChecker(manager).checkParameter(createSvmParameter(KernelType.poly));
		assertThat(manager.getValidationMessage().toString(), containsString("Kernel type: poly\n"));
	}

	private SvmParameter createSvmParameter(KernelType kernelType) {
		SvmParameter params = new SvmParameter();
		params.setKernelType(kernelType);
		return params;
	}
}
