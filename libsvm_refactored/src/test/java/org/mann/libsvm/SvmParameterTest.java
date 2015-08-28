package org.mann.libsvm;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertThat;

import org.junit.Test;
import org.mann.libsvm.SvmParameter;
import org.mann.libsvm.SvmParameter.SvmType;


public class SvmParameterTest {
	
	@Test
	public void getIntEquivalentOfSvmTypeShouldReturnCorrectInt(){
		//Arrange
		SvmParameter sp = new SvmParameter();

		//Assert
		assertThat(sp.getIntEquivalentOfSvmType(SvmType.C_SVC), equalTo(0));
		assertThat(sp.getIntEquivalentOfSvmType(SvmType.NU_SVC), equalTo(1));
		assertThat(sp.getIntEquivalentOfSvmType(SvmType.ONE_CLASS), equalTo(2));
		assertThat(sp.getIntEquivalentOfSvmType(SvmType.EPSILON_SVR), equalTo(3));
		assertThat(sp.getIntEquivalentOfSvmType(SvmType.NU_SVR), equalTo(4));
	}
	
	@Test
	public void getOldSvmTypeShouldReturnCorrectSvmTypeEnum(){
		//Arrange
		SvmParameter sp = new SvmParameter();
		
		//Assert
		assertThat(sp.getSvmTypeFromSvmParameter(0), equalTo(SvmType.C_SVC));
		assertThat(sp.getSvmTypeFromSvmParameter(1), equalTo(SvmType.NU_SVC));
		assertThat(sp.getSvmTypeFromSvmParameter(2), equalTo(SvmType.ONE_CLASS));
		assertThat(sp.getSvmTypeFromSvmParameter(3), equalTo(SvmType.EPSILON_SVR));
		assertThat(sp.getSvmTypeFromSvmParameter(4), equalTo(SvmType.NU_SVR));
	}	
}
