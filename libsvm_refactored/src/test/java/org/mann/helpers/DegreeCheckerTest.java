package org.mann.helpers;

import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertThat;

import org.junit.Test;
import org.mann.helpers.DegreeChecker;
import org.mann.helpers.ParameterValidationManager;
import org.mann.libsvm.SvmParameter;

public class DegreeCheckerTest {

	@Test
	public void checkDegreeShouldReturnErrorWhenLessThanZero(){
		ParameterValidationManager manager = new ParameterValidationManager(new StringBuilder()); 
		new DegreeChecker(manager).checkDegreeOfPolynomialKernel(-1);
		assertThat(manager.getValidationMessage().toString(), equalTo("ERROR: degree of polynomial kernel < 0\n"));
	}
	
	@Test
	public void checkParameterShouldReturnErrorWhenLessThanZero(){
		ParameterValidationManager manager = new ParameterValidationManager(new StringBuilder()); 
		new DegreeChecker(manager).checkParameter(createSvmParameter());
		assertThat(manager.getValidationMessage().toString(), containsString("Degree = 1\n"));
	}
	
	private SvmParameter createSvmParameter() {
		SvmParameter params = new SvmParameter();
		params.degree = 1;
		return params;
	}

}
