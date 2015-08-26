package helpers;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.*;
import libsvm.SvmNode;
import libsvm.SvmParameter;
import libsvm.svm;
import libsvm.svm_problem;
import libsvm.SvmParameter.SvmType;

import org.junit.Before;
import org.junit.Test;

public class ParameterValidationManagerTest {
	private ParameterValidationManager manager; 
	
	@Before
	public void setup(){
		manager = new ParameterValidationManager(new StringBuilder());
	}
	
	@Test(expected = NullPointerException.class)
	public void checkSvmParameterShouldReturnExceptionWhenSvmTypeNotSet(){
		svm.svm_check_parameter(null, new SvmParameter());
	}
	
	@Test
	public void parameterCheckerShouldReturnSvmTypeWhenRequested() {
		String checkType = "SvmType";
		int type = 1;
		String response = manager.runCheckAndGetResponse(checkType, type);
		assertThat(response, equalTo("NU_SVC"));
	}
	
	@Test
	public void parameterCheckerShouldReturnKernelTypeWhenRequested() {
		String checkType = "Kernel";
		int type = 1;
		
		String response = manager.runCheckAndGetResponse(checkType, type);
		assertThat(response, equalTo("kernel type: 1\n"));
	}
	
	@Test
	public void parameterCheckerShouldReturnGammaWhenRequested() {
		String checkType = "Gamma";
		double type = 1;
		String response = manager.runCheckAndGetResponse(checkType, type);
		assertThat(response, equalTo("Gamma = 1.0\n"));
	}
	
	@Test
	public void parameterCheckerShouldReturnDegreeWhenRequested() {
		String checkType = "Degree";
		int type = 1;
		String response = manager.runCheckAndGetResponse(checkType, type);
		assertThat(response, equalTo("Degree = 1\n"));
	}
	
	@Test
	public void parameterCheckerShouldReturnCacheSizeWhenRequested() {
		String checkType = "Cache";
		double type = 1;
		String response = manager.runCheckAndGetResponse(checkType, type);
		assertThat(response, equalTo("Cache size: 1.0\n"));
	}
	
	@Test
	public void parameterCheckerShouldReturnEpsWhenRequested() {
		String checkType = "Eps";
		double type = 1;
		String response = manager.runCheckAndGetResponse(checkType, type);
		assertThat(response, equalTo("Eps = 1.0\n"));
	}
	
	@Test
	public void parameterCheckerShouldReturnCWhenRequested() {
		String checkType = "C";
		double type = 1;
		String response = manager.runCheckAndGetResponse(checkType, type);
		assertThat(response, equalTo("C = 1.0\n"));
	}
	
	@Test
	public void parameterCheckerShouldReturnNuWhenRequested() {
		String checkType = "Nu";
		double type = 1;
		String response = manager.runCheckAndGetResponse(checkType, type);
		assertThat(response, equalTo("Nu = 1.0\n"));
	}
	
	@Test
	public void parameterCheckerShouldReturnPWhenRequested() {
		
		String checkType = "P";
		double type = 1;
		String response = manager.runCheckAndGetResponse(checkType, type);
		assertThat(response, equalTo("p = 1.0\n"));
	}
	
	@Test
	public void parameterCheckerShouldReturnShrinkingWhenRequested() {
		String checkType = "Shrinking";
		int type = 1;
		String response = manager.runCheckAndGetResponse(checkType, type);
		assertThat(response, equalTo("Shrinking = 1\n"));
	}
	
	@Test
	public void parameterCheckerShouldReturnProbabilityWhenRequested() {
		String checkType = "Probability";
		int type = 1;
		String response = manager.runCheckAndGetResponse(checkType, type, SvmType.C_SVC);
		assertThat(response, equalTo("Probability = 1\n"));
	}
	
	@Test
	public void parameterCheckerShouldReturnFeasibilityOfNuWhenRequested() {
		String checkType = "Feasibility of Nu";
		String response = manager.runCheckAndGetResponse(checkType, SvmParameter.C_SVC, createSvmProblem(), new SvmParameter());
		//TODO fix this so it fails if nu is not set
		assertThat(response, equalTo("Nu = 0.0: feasibility checked and is OK"));
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
}
