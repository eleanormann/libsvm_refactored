package org.mann.libsvm;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.*;

import org.junit.Test;
import org.mann.libsvm.SvmModel;
import org.mann.libsvm.SvmParameter;
import org.mann.libsvm.svm;
import org.mann.libsvm.SvmParameter.SvmType;


public class SvmTest {

	@Test
	public void svmGetSvmTypeC_SVCShouldReturnCorrectInt() {
		//Arrange
		SvmModel model = createModel(SvmType.C_SVC);
		//Act
		SvmType svmType = svm.getSvmTypeFromModel(model);
		//Assert
		assertThat(svmType, equalTo(SvmType.C_SVC));	
	}
	
	@Test
	public void svmGetSvmTypeOneClassShouldReturnCorrectInt() {
		//Arrange
		SvmModel model = createModel(SvmType.ONE_CLASS);
		//Act
		SvmType svmType = svm.getSvmTypeFromModel(model);
		//Assert
		assertThat(svmType, equalTo(SvmType.ONE_CLASS));	
	}

	@Test
	public void svmCheckParameterShouldReturnValidationMessageWithNuFeasibilityWhenNuSvc(){
		String expectedMessage = "Nu = 1.0: feasibility checked and is OK\nSvm type: NU_SVC\nkernel type: 1\nGamma = 1.0\nDegree = 1\nCache size: 1.0\n"
				+ "Eps = 1.0\nShrinking = 1\nProbability = 1\n";
		String validationMessage = new svm().checkSvmParameter(new svm_problem(), createSvmParameter(SvmType.NU_SVC));
		assertThat(validationMessage, equalTo(expectedMessage));
	}

	@Test
	public void svmCheckParameterShouldReturnValidationMessageWithNuWhenNuSvr(){
		String expectedMessage = "Svm type: NU_SVR\nkernel type: 1\nGamma = 1.0\nDegree = 1\nCache size: 1.0\n"
				+ "Eps = 1.0\nC = 1.0\nNu = 1.0\nShrinking = 1\nProbability = 1\n";
		String validationMessage = new svm().checkSvmParameter(new svm_problem(), createSvmParameter(SvmType.NU_SVR));
		assertThat(validationMessage, equalTo(expectedMessage));
	}
	private SvmModel createModel(SvmType svmType) {
		SvmModel model = new SvmModel();
		SvmParameter param = new SvmParameter();
		param.svmType = svmType;
		model.setParam(param);
		return model;
	}

	private SvmParameter createSvmParameter(SvmType svmType) {
		SvmParameter params = new SvmParameter();
		params.svmType = svmType;
		params.kernel_type = 1;
		params.C = 1;
		params.cache_size = 1;
		params.degree = 1;
		params.eps = 1;
		params.gamma = 1;
		params.nu = 1;
		params.nr_weight = 1;
		params.p = 1;
		params.probability = 1;
		params.shrinking=1;
		return params;
	}
}
