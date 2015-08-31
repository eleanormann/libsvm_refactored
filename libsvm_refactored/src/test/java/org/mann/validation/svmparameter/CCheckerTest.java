package org.mann.validation.svmparameter;

import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertThat;

import org.junit.Test;
import org.mann.libsvm.SvmParameter;
import org.mann.libsvm.SvmParameter.SvmType;
import org.mann.validation.svmparameter.CChecker;

public class CCheckerTest {

	@Test
	public void checkCShouldReturnErrorWhenCLessThanZero(){
		ParameterValidationManager manager = new ParameterValidationManager(new StringBuilder()); 
		new CChecker(manager).checkC(0);
		assertThat(manager.getValidationMessage().toString(), equalTo("ERROR: C <= 0\n"));
	}

	@Test
	public void checkCShouldReturnErrorWhenCEqualToZero(){
		ParameterValidationManager manager = new ParameterValidationManager(new StringBuilder()); 
		new CChecker(manager).checkC(0);
		assertThat(manager.getValidationMessage().toString(), equalTo("ERROR: C <= 0\n"));
	}
	
	@Test
	public void checkCShouldAddInfoMessageWhenCMoreThanZero(){
		ParameterValidationManager manager = new ParameterValidationManager(new StringBuilder()); 
		new CChecker(manager).checkC(1);
		assertThat(manager.getValidationMessage().toString(), equalTo("C = 1.0\n"));
	}
	
	@Test
	public void checkParmeterShouldAddErrorWhenCEqualToZero(){
		ParameterValidationManager manager = new ParameterValidationManager(new StringBuilder()); 
		new CChecker(manager).checkParameter(createSvmParameter(0, SvmType.C_SVC));
		assertThat(manager.getValidationMessage().toString(), containsString("ERROR: C <= 0\n"));
	}
	
	@Test
	public void checkParmeterShouldAddErrorMessageWhenCLessThanZero(){
		ParameterValidationManager manager = new ParameterValidationManager(new StringBuilder()); 
		new CChecker(manager).checkParameter(createSvmParameter(-1, SvmType.C_SVC));
		assertThat(manager.getValidationMessage().toString(), containsString("ERROR: C <= 0\n"));
	}
	
	@Test
	public void checkParmeterShouldAddInfoMessageWhenCGreaterThanZeroAndCSvc(){
		ParameterValidationManager manager = new ParameterValidationManager(new StringBuilder()); 
		new CChecker(manager).checkParameter(createSvmParameter(1, SvmType.C_SVC));
		assertThat(manager.getValidationMessage().toString(), containsString("C = 1.0\n"));
	}
	
	@Test
	public void checkParmeterShouldAddInfoMessageWhenCGreaterThanZeroAndEpsilon(){
		ParameterValidationManager manager = new ParameterValidationManager(new StringBuilder()); 
		new CChecker(manager).checkParameter(createSvmParameter(1, SvmType.EPSILON_SVR));
		assertThat(manager.getValidationMessage().toString(), containsString("C = 1.0\n"));
	}
	
	@Test
	public void checkParmeterShouldAddInfoMessageWhenCGreaterThanZeroAndNuSvr(){
		ParameterValidationManager manager = new ParameterValidationManager(new StringBuilder()); 
		new CChecker(manager).checkParameter(createSvmParameter(1, SvmType.NU_SVR));
		assertThat(manager.getValidationMessage().toString(), containsString("C = 1.0\n"));
	}
	
	@Test
	public void checkParmeterShouldAddErrorWhenSvmTypeIsOneClass(){
		ParameterValidationManager manager = new ParameterValidationManager(new StringBuilder()); 
		new CChecker(manager).checkParameter(createSvmParameter(1, SvmType.ONE_CLASS));
		assertThat(manager.getValidationMessage().toString(), not(containsString("ERROR: C <= 0\n")));
		assertThat(manager.getValidationMessage().toString(), not(containsString("C = 1.0\n")));
	}
	
	private SvmParameter createSvmParameter(double c, SvmType svmType) {
		SvmParameter params = new SvmParameter();
		params.C = c;
		params.svmType = svmType;
		return params;
	}
}
