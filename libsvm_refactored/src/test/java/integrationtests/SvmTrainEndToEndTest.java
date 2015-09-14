package integrationtests;

import static org.junit.Assert.*;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.PrintStream;

import org.mann.libsvm.svm;
import org.mann.libsvm.svm_train;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class SvmTrainEndToEndTest {
	private final ByteArrayOutputStream outContent = new ByteArrayOutputStream();

	@Before
	public void setUpStreams() {
		System.setOut(new PrintStream(outContent));
	}

	@After
	public void cleanUpStreams() {
		System.setOut(null);
	}

	@Test
	public void svmTrainShouldProduceExpectedResults() throws IOException {
		String expected = "......*\n"
				+ "optimization finished, #iter = 6197\n"
				+ "nu = 0.31647823832979366\n"
				+ "obj = -1300.2536635959898, rho = 0.28709349878580875\n"
				+ "nSV = 2448, nBSV = 1183\n"
				+ "Total nSV = 2448\n";
		svm_train.main(new String[]{"-s", "c_svc", "-w", "2", "34","src/test/resources/testdata/hfmTrainingData.train"});
		assertEquals(expected, outContent.toString());
	}
	
}
