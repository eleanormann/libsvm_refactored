package libsvm;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import libsvm.SvmParameter.SvmType;

import org.junit.Test;


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
	

	@Test(expected = NullPointerException.class)
	public void checkSvmParameterShouldReturnExceptionWhenSvmTypeNotSet(){
		svm.svm_check_parameter(null, new SvmParameter());
	}
	
	@Test
	public void checkSvmTypeShouldReturnErrorWhenNotRecognised(){
		String errorMessage = svm.checkSvmType(-1);
		assertThat(errorMessage, equalTo("ERROR: unknown svm type\n"));
	}
	
	@Test
	public void checkKernelTypeShouldReturnErrorWhenNotRecognised(){
		String errorMessage = svm.checkKernelType(-1);
		assertThat(errorMessage, equalTo("ERROR: unknown kernel type\n"));
	}
	
	@Test
	public void checkGammaShouldReturnErrorWhenLessThanZero(){
		String errorMessage = svm.checkGamma(-1);
		assertThat(errorMessage, equalTo("ERROR: gamma less than zero\n"));
	}
	
	@Test
	public void checkDegreeShouldReturnErrorWhenLessThanZero(){
		String errorMessage = svm.checkDegreeOfPolynomialKernel(-1);
		assertThat(errorMessage, equalTo("ERROR: degree of polynomial kernel < 0\n"));
	}
	
	@Test
	public void checkCacheSizeShouldReturnErrorWhenEqualToOrLessThanZero(){
		String errorMessage = svm.checkCacheSize(0);
		assertThat(errorMessage, equalTo("ERROR: cache size <= 0\n"));
	}
	
	@Test
	public void checkEpsShouldReturnErrorWhenEqualToOrLessThanZero(){
		String errorMessage = svm.checkEps(0);
		assertThat(errorMessage, equalTo("ERROR: eps <= 0\n"));
	}
	
	@Test
	public void checkCShouldReturnErrorWhenEqualToOrLessThanZero(){
		String errorMessage = svm.checkC(0);
		assertThat(errorMessage, equalTo("ERROR: C <= 0\n"));
	}
	
	@Test
	public void checkNuShouldReturnErrorWhenLessThanZeroOrGreaterThanOne(){
		String errorMessage = svm.checkNu(0);
		assertThat(errorMessage, equalTo("ERROR: nu <= 0 or nu > 1\n"));
	}
	
	@Test
	public void checkPShouldReturnErrorWhenLessThanZero(){
		String errorMessage = svm.checkP(-1);
		assertThat(errorMessage, equalTo("ERROR: p < 0\n"));
	}
	
	@Test
	public void checkShrinkingShouldReturnErrorWhenNeitherZeroNorOne(){
		String errorMessage = svm.checkShrinking(-1);
		assertThat(errorMessage, equalTo("ERROR: shrinking is neither 0 nor 1\n"));
	}
	
	@Test
	public void checkProbabilityShouldReturnErrorWhenNeitherZeroNorOne(){
		String errorMessage = svm.checkProbability(-1, SvmType.C_SVC);
		assertThat(errorMessage, equalTo("ERROR: Probability is neither 0 nor 1\n"));
	}
	
	@Test
	public void checkProbabilityShouldReturnErrorWhenOneAndOneClass(){
		String errorMessage = svm.checkProbability(1, SvmType.ONE_CLASS);
		assertThat(errorMessage, equalTo("ERROR: one-class SVM probability output not supported yet"));
	}
	
	@Test
	public void checkProbabilityShouldNotReturnErrorWhenZeroAndOneClass(){
		String errorMessage = svm.checkProbability(0, SvmType.ONE_CLASS);
		assertThat(errorMessage, equalTo("Probability = 0\n"));
	}
	
	private SvmModel createModel(SvmType svmType) {
		SvmModel model = new SvmModel();
		SvmParameter param = new SvmParameter();
		param.svmType = svmType;
		model.setParam(param);
		return model;
	}

}
