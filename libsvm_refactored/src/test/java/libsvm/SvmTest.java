package libsvm;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertThat;
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
	
	@Test
	public void extendArrayLengthShouldReturnCopyWithDoubleLength(){
		int[] originalArray = {1,2,3,4,5};
		int[] expectedArray = {1,2,3,4,5,0,0,0,0,0};
		originalArray = svm.extendArrayLength(originalArray);
		assertThat(originalArray, equalTo(expectedArray));
	}
	
	@Test
	public void checkFeasibilityOfNuReturnsErrorMessageWhenUnfeasible(){
		svm_problem dataset = createSvmProblem();
		SvmParameter param = new SvmParameter();
		//.88 is the largest nu (to 2 dp) that is feasible for these data
		param.nu = .89;
		
		String errorMessage = svm.checkFeasibilityOfNu(SvmParameter.NU_SVC, dataset, param);
		assertThat(errorMessage, equalTo("ERROR: "+param.nu +" is not a feasible nu for these data"));
		
		param.nu = .88;
		String successMessage = svm.checkFeasibilityOfNu(SvmParameter.NU_SVC, dataset, param);
		assertThat(successMessage, equalTo("Nu = " + param.nu + ": feasibility checked and is OK"));
	}

	private svm_problem createSvmProblem() {
		svm_problem dataset = new svm_problem();
		dataset.y = new double[]{1,1,1,1,0,0,0,1,0};
		dataset.x = new SvmNode[][] {
				{ new SvmNode(1, 5), new SvmNode(2, 6), new SvmNode(3, 4), new SvmNode(4, 4), new SvmNode(-1, 0) },
				{ new SvmNode(5, 10), new SvmNode(6, 9), new SvmNode(7, 8), new SvmNode(8, 10), new SvmNode(9, 9) }};
		dataset.length = dataset.y.length;
		return dataset;
	}
	
	private SvmModel createModel(SvmType svmType) {
		SvmModel model = new SvmModel();
		SvmParameter param = new SvmParameter();
		param.svmType = svmType;
		model.setParam(param);
		return model;
	}

}
