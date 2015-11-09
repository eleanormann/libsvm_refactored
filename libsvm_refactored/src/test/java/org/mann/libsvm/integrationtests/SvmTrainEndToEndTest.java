package org.mann.libsvm.integrationtests;

import static org.hamcrest.CoreMatchers.containsString;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.fail;

import java.io.File;
import java.io.IOException;

import org.junit.After;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.mann.libsvm.svm_train;

public class SvmTrainEndToEndTest {
	// set to false for data collecting mode
	private final OutputStreamHandler outputHandler = new OutputStreamHandler(true); 
	private File resultsFile = new File("target/output/cross-validation-output.csv");

	@Before
	public void setUpOutputStream() {
		outputHandler.setUpOutputStream();
		
		//TODO: remove this comment or handle resultsFile
		//Expires 2nd November 2015
		// if(resultsFile.exists()){
		// resultsFile.delete();
		// }
	}

	@After
	public void cleanUpObjects() {
		outputHandler.resetOutput();
	}

	@Test
	public void svmTrainShouldProduceExpectedResultsForCSvc() throws IOException {
		String expected = "optimization finished, #iter = 6197\n" + "nu = 0.31647823832979366\n"
				+ "obj = -1300.2536635959898, rho = 0.28709349878580875\n" + "nSV = 2448, nBSV = 1183\n" + "Total nSV = 2448\n";
		svm_train.main(new String[] { "-s", "c_svc", "-w", "2", "34", "src/test/resources/testdata/hfmTrainingData.train" });
		assertResultsAsExpected(expected);
	}

	private void assertResultsAsExpected(String expected) {
		if (outputHandler.useHandler()) {
			assertThat(outputHandler.getOutContent().toString(), containsString(expected));
		} else {
			fail("not tested, in data gathering mode");
		}
	}

	/*
	 * poly, degree, coef0, gamma
	 */

	@Test
	public void svmTrainShouldProduceExpectedResultsForPolyKernel() throws IOException {
		String expected = "optimization finished, #iter = 3839665\n" + "nu = 0.05349656802828698\n"
				+ "obj = -48.317229205434955, rho = -0.2320645138964536\n" + "nSV = 72, nBSV = 45\n" + "Total nSV = 72";
		svm_train.main(new String[] { "-t", "poly", "-d", "2", "-g", "0.33", "-r", "0.3",
				"src/test/resources/testdata/shortTrainingData.train" });
		assertResultsAsExpected(expected);
	}

	// Stricly speaking it doesn't matter, but might it be a better test to use
	// meaning param values?
	@Test
	public void svmTrainShouldProduceExpectedResultsForPolyKernelWithDegree() throws IOException {
		String expected = "optimization finished, #iter = 10000000\n" + "nu = 0.024251491118899824\n"
				+ "obj = -20.162845990463914, rho = -6.120012701852188\n" + "nSV = 47, nBSV = 16\n" + "Total nSV = 47\n";
		svm_train.main(new String[] { "-t", "poly", "-d", "3", "-g", "0.25", "-r", "0.3",
				"src/test/resources/testdata/shortTrainingData.train" });
		assertResultsAsExpected(expected);
	}

	@Test
	public void svmTrainShouldProduceExpectedResultsForPolyKernelWithGamma() throws IOException {
		String expected = "optimization finished, #iter = 68638\n" + "nu = 0.08017765890693218\n"
				+ "obj = -74.56627030178744, rho = -2.0219653384371146\n" + "nSV = 93, nBSV = 76\n" + "Total nSV = 93\n";
		svm_train.main(new String[] { "-t", "poly", "-d", "2", "-g", "0.05", "-r", "0.3",
				"src/test/resources/testdata/shortTrainingData.train" });
		assertResultsAsExpected(expected);
	}

	@Test
	public void svmTrainShouldProduceExpectedResultsForPolyKernelWithCoef0() throws IOException {
		String expected = "optimization finished, #iter = 3363616\n" + "nu = 0.05309974823831803\n"
				+ "obj = -48.00871613983692, rho = -0.019962102846128996\n" + "nSV = 72, nBSV = 46\n" + "Total nSV = 72";
		svm_train.main(new String[] { "-t", "poly", "-d", "2", "-g", "0.33", "-r", "0.75",
				"src/test/resources/testdata/shortTrainingData.train" });
		assertResultsAsExpected(expected);
	}

	@Test
	public void svmTrainShouldProduceExpectedResultsForSigmoidKernel() throws IOException {
		String expected = "optimization finished, #iter = 1739\n" + "nu = 0.6088935574229691\n"
				+ "obj = -4480.018808152527, rho = -0.9999987185001373\n" + "nSV = 3478, nBSV = 3478\n" + "Total nSV = 3478";
		svm_train.main(new String[] { "-t", "sigmoid", "-g", "0.1", "-r", "0.75",
				"src/test/resources/testdata/hfmTrainingData.train" });
		assertResultsAsExpected(expected);
	}

	@Test
	public void svmTrainShouldProduceExpectedResultsForSigmoidKernelWithGamma() throws IOException {
		String expected = "optimization finished, #iter = 1739\n" + "nu = 0.6088935574229691\n"
				+ "obj = -4480.566593065858, rho = -0.9999981820583344\n" + "nSV = 3478, nBSV = 3478\n" + "Total nSV = 3478";
		svm_train.main(new String[] { "-t", "sigmoid", "-g", "0.125", "-r", "0.75",
				"src/test/resources/testdata/hfmTrainingData.train" });
		assertResultsAsExpected(expected);
	}

	@Test
	public void svmTrainShouldProduceExpectedResultsForSigmoidKernelWithCoef0() throws IOException {
		String expected = "optimization finished, #iter = 1739\n" + "nu = 0.6088935574229691\n"
				+ "obj = -4483.108260884881, rho = -0.9999945163726807\n" + "nSV = 3478, nBSV = 3478\n" + "Total nSV = 3478";
		svm_train.main(new String[] { "-t", "sigmoid", "-g", "0.1", "-r", "0.006",
				"src/test/resources/testdata/hfmTrainingData.train" });
		assertResultsAsExpected(expected);
	}

	@Test
	public void svmTrainShouldProduceExpectedResultsForNuSvcWithDefaultParam() throws IOException {
		String expected = "optimization finished, #iter = 1320\n" + "C = 0.13562496944471591\n"
				+ "obj = 24.25684136703166, rho = 0.7808778353619084\n" + "nSV = 622, nBSV = 387\n" + "Total nSV = 622";
		svm_train.main(new String[] { "-s", "nu_svc", "src/test/resources/testdata/shortTrainingData.train" });
		assertResultsAsExpected(expected);
	}

	@Test
	public void svmTrainShouldProduceExpectedResultsForNuSvcWithNu() throws IOException {
		String expected = "optimization finished, #iter = 491\n" + "C = 0.0678619405250941\n"
				+ "obj = 16.198231881533992, rho = 0.7415731684547857\n" + "nSV = 757, nBSV = 670\n" + "Total nSV = 757";
		svm_train.main(new String[] { "-s", "nu_svc", "-n", "0.7", "src/test/resources/testdata/shortTrainingData.train" });
		assertResultsAsExpected(expected);
	}

	@Test
	public void svmTrainShouldProduceExpectedResultsForNuSvrWithDefaultParam() throws IOException {
		String expected = "optimization finished, #iter = 88950\n" + "epsilon (in solve_nu_svr) = 3.3687377371632854E-4\n"
				+ "obj = -47.268314606432924, rho = -0.8474714871285831\n" + "nSV = 892, nBSV = 295\n";
		svm_train.main(new String[] { "-s", "nu_svr", "src/test/resources/testdata/shortTrainingData.train" });
		assertResultsAsExpected(expected);
	}

	@Test
	public void svmTrainShouldProduceExpectedResultsForNuSvrWithNu() throws IOException {
		String expected = "optimization finished, #iter = 1445\n" + "epsilon (in solve_nu_svr) = 0.19327946909867955\n"
				+ "obj = -41.15589019217956, rho = -0.7204229747169973\n" + "nSV = 428, nBSV = 59\n";
		svm_train.main(new String[] { "-s", "nu_svr", "-n", "0.1", "src/test/resources/testdata/shortTrainingData.train" });
		assertResultsAsExpected(expected);
	}

	@Test
	//c_svc, nu_svc, one_class, epsilon_svr, nu_svr
	public void svmTrainShouldTrainWithOneClass() throws IOException{
	  String expected = "optimization finished, #iter = 2366\n"
	      + "obj = 16295.933791201422, rho = 18.10620658361271\n"
	      + "nSV = 2957, nBSV = 2763\n";
	  svm_train.main(new String[]{"-s", "one_class", "src/test/resources/testdata/hfmTrainingData.train"});
	  assertResultsAsExpected(expected);
	}
	
	@Test
    //c_svc, nu_svc, one_class, epsilon_svr, nu_svr
    public void svmTrainShouldTrainWithEpsilonSvr() throws IOException{
      String expected = "optimization finished, #iter = 7923\n"
          + "nu = 0.23611580871154153\n"
          + "obj = -365.4275800568434, rho = -0.5956074383555992\n"
          + "nSV = 2397, nBSV = 819";
      svm_train.main(new String[]{"-s", "epsilon_svr", "src/test/resources/testdata/hfmTrainingData.train"});
      assertResultsAsExpected(expected);
    }
	
	@Test
	public void runShouldRunCrossValidation() throws IOException {
		fail("Not yet implemented");
	}

	@Ignore
	@Test
	public void collectResultsForCrossValidationChecking() throws IOException {
		for (int i = 0; i < 11; i++) {
			svm_train.main(setCrossValidationConfig(9));
		}
	}

	private String[] setCrossValidationConfig(int times) {
		return new String[] { "-v", String.valueOf(times), "src/test/resources/testdata/hfmTrainingData.train" };
	}

}
