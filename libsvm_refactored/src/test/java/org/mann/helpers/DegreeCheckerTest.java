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
	public void checkDegreeShouldAddErrorMessageWhenDegreeLessThanZero(){
		ParameterValidationManager manager = new ParameterValidationManager(new StringBuilder()); 
		new DegreeChecker(manager).checkDegreeOfPolynomialKernel(-1);
		assertThat(manager.getValidationMessage().toString(), equalTo("ERROR: degree of polynomial kernel < 0\n"));
	}
	
	@Test
	public void checkDegreeShouldAddInfoMessageWhenDegreeZero(){
		ParameterValidationManager manager = new ParameterValidationManager(new StringBuilder()); 
		new DegreeChecker(manager).checkDegreeOfPolynomialKernel(0);
		assertThat(manager.getValidationMessage().toString(), equalTo("Degree = 0\n"));
	}
	
	@Test
	public void checkDegreeShouldAddInfoMessageWhenDegreeMoreThanZero(){
		ParameterValidationManager manager = new ParameterValidationManager(new StringBuilder()); 
		new DegreeChecker(manager).checkDegreeOfPolynomialKernel(1);
		assertThat(manager.getValidationMessage().toString(), equalTo("Degree = 1\n"));
	}
	
	@Test
	public void checkParameterShouldReturnErrorWhenLessThanZero(){
		ParameterValidationManager manager = new ParameterValidationManager(new StringBuilder()); 
		new DegreeChecker(manager).checkParameter(createSvmParameter(-1));
		assertThat(manager.getValidationMessage().toString(), containsString("ERROR: degree of polynomial kernel < 0\n"));
	}
	
	@Test
	public void checkParameterShouldReturnInfoMessageWhenDegreeIsZero(){
		ParameterValidationManager manager = new ParameterValidationManager(new StringBuilder()); 
		new DegreeChecker(manager).checkParameter(createSvmParameter(0));
		assertThat(manager.getValidationMessage().toString(), containsString("Degree = 0\n"));
	}
	
	@Test
	public void checkParameterShouldReturnInfoMessageWhenDegreeIsGreaterThanZero(){
		ParameterValidationManager manager = new ParameterValidationManager(new StringBuilder()); 
		new DegreeChecker(manager).checkParameter(createSvmParameter(1));
		assertThat(manager.getValidationMessage().toString(), containsString("Degree = 1\n"));
	}
	
	private SvmParameter createSvmParameter(int degree) {
		SvmParameter params = new SvmParameter();
		params.degree = degree;
		return params;
	}

}
