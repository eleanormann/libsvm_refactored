package org.mann.libsvm.kernel;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertThat;

import org.junit.Test;
import org.mann.libsvm.SvmNode;
import org.mann.libsvm.SvmParameter;
import org.mann.libsvm.SvmParameter.KernelType;
import org.mann.libsvm.SvmProblem;
import org.mann.libsvm.kernel.Kernel;

public class KernelTest {

	@Test
	public void kernelConstructorShouldCreateKernelSettingXSquareToNullWhenKernelIsNotRBF(){
		SvmNode[][] problemX = createExpectedSvmProblemX();
		Kernel kernel = new Kernel(problemX.length, problemX, new SvmParameter());
		assertThat(kernel.getXSquare(), equalTo(null));
		assertThat(kernel.getX(), equalTo(problemX));
	}
	
	@Test
	public void kernelConstructorShouldCreateKernelSettingXSquareWhenKernelIsRBF(){
		SvmNode[][] problemX = createExpectedSvmProblemX();
		SvmParameter param = new SvmParameter();
		param.setKernelType(KernelType.rbf);
		double[] xSquare = calculateExpectedXSquare(problemX);
		Kernel kernel = new Kernel(problemX.length, problemX, param);
		assertThat(kernel.getXSquare(), equalTo(xSquare));
		assertThat(kernel.getX(), equalTo(problemX));
	}

	@Test
	public void dotShouldReturnCorrectValue__UnevenIndices() {
		SvmNode[] trainingInstance1 = new SvmNode[] { new SvmNode(1, 2), new SvmNode(2, 3), };
		SvmNode[] trainingInstance2 = new SvmNode[] { new SvmNode(2, 3) };
		assertThat(Kernel.dot(trainingInstance1, trainingInstance2), equalTo(9.0));
	}
	
	@Test
	public void dotShouldReturnCorrectValue__MoreValues() {
		SvmNode[] trainingInstance1 = new SvmNode[] { new SvmNode(1, 2), new SvmNode(2, 3), new SvmNode(3, 5) };
		SvmNode[] trainingInstance2 = new SvmNode[] { new SvmNode(2, 3), new SvmNode(3, 4)};
		assertThat(Kernel.dot(trainingInstance1, trainingInstance2), equalTo(29.0));
	}
	
	@Test
	public void kernelFunctionShouldReturnDotWhenLinear(){
		SvmParameter param = new SvmParameter();
		param.setKernelType(KernelType.linear);
		SvmNode[][] trainingData = new SvmNode[][]{ //repetition but keeping this here so I can see the data used
				{ new SvmNode(1, 2), new SvmNode(2, 3), new SvmNode(3, 5) },
				{ new SvmNode(2, 3), new SvmNode(3, 4)}
		};
		Kernel kernel = new Kernel(2, trainingData, param);
		assertThat(kernel.kernel_function(0, 1), equalTo(29.0));
	}
	
	@Test 
	public void kernelFunctionShouldReturnCorrectValueWhenPoly(){
		SvmParameter defaultParam = new SvmParameter();
		defaultParam.setDefaultValues();
		defaultParam.setKernelType(KernelType.poly);
		SvmNode[][] trainingData = new SvmNode[][]{
				{ new SvmNode(1, 2), new SvmNode(2, 3), new SvmNode(3, 5)},
				{ new SvmNode(2, 3), new SvmNode(3, 4)}
		};
		Kernel kernel = new Kernel(2, trainingData, defaultParam);
		//powi(0 * dot(trainingData[0], trainingData[1]) +coef, 3) 
		assertThat(kernel.kernel_function(0, 1), equalTo(0.0)); 
	}
		
	@Test 
	public void kernelFunctionShouldReturnCorrectValueWhenRbf(){
		SvmParameter defaultParam = new SvmParameter();
		defaultParam.setDefaultValues();
		defaultParam.setKernelType(KernelType.rbf);
		SvmNode[][] trainingData = new SvmNode[][]{
				{ new SvmNode(1, 2), new SvmNode(2, 3), new SvmNode(3, 5) },
				{ new SvmNode(2, 3), new SvmNode(3, 4)}
		};
		Kernel kernel = new Kernel(2, trainingData, defaultParam);
		//Math.exp(-0 * (x_square[i] + x_square[j] - 2 * dot(x[i], x[j])));
		assertThat(kernel.kernel_function(0, 1), equalTo(1.0)); //TODO: calculate this by hand
	}
	
	//Moved the Kernel.powi method here to keep a record of legacy code removed since 
	//I'm just starting to refactor actual calculations and I am scared
	@Test
	public void kernelPowiMethodShouldReturnSameAsMathPow() {
		double randomBase = Math.random();
		int randomTimes = (int)Math.random();
		assertThat(powi(randomBase, randomTimes), equalTo(Math.pow(randomBase, randomTimes)));
	}
	
	/*Still need to check the following:
	 * If the first argument is finite and less than zero
if the second argument is a finite even integer, the result is equal to the result of raising the absolute value of the first argument to the power of the second argument
if the second argument is a finite odd integer, the result is equal to the negative of the result of raising the absolute value of the first argument to the power of the second argument
if the second argument is finite and not an integer, then the result is NaN.
If both arguments are integers, then the result is exactly equal to the mathematical result of raising the first argument to the power of the second argument if that result can in fact be represented exactly as a double value.
	 * 
	 */
	@Test
	public void kernelPowiMethodShouldHandleSpecialCasesAsMathPowDoes() {
		assertThat(powi(3, -0), equalTo(Math.pow(3, -0)));
		assertThat(powi(3, 0), equalTo(Math.pow(3, 0)));
		assertThat(powi(3, 1), equalTo(Math.pow(3, 1)));
		assertThat(powi(3, (int) Double.NaN), equalTo(Math.pow(3, (int) Double.NaN)));
		assertThat(powi(Double.NaN, 3), equalTo(Math.pow(Double.NaN, 3)));
		assertThat(powi(0,3), equalTo(Math.pow(0, 3)));
		assertThat(powi(-0, 2), equalTo(Math.pow(-0, 2)));
		assertThat(powi(-0, 3), equalTo(Math.pow(-0, 3)));
	}
	
	@Test
	public void kernelPowiMethodShouldHandleSpecialCasesAsMathPowDoesButDoesnt() {
		assertThat(powi(0, -1), equalTo(Kernel.powi(0,-1)));//powi reckons this is 1.0, which is clearly wrong
		assertThat(powi(-0, -2), equalTo(Kernel.powi(-0, -2))); //powi reckons this is 1.0. Does 1 need to be returned to handle error?
		assertThat(powi(-0, -3), equalTo(Kernel.powi(-0,-3)));
	}
	
	private double powi(double base, int times) {
		double tmp = base, ret = 1.0;

		for (int t = times; t > 0; t /= 2) {
			if (t % 2 == 1)
				ret *= tmp;
			tmp = tmp * tmp;
		}
		return ret;
	}
	
	
	private SvmNode[][] createExpectedSvmProblemX() {
		return new SvmNode[][]{
			{new SvmNode(1, 0.3), new SvmNode(2, 0.2)},
			{new SvmNode(1, 0.5), new SvmNode(2, 0.1)},
			{new SvmNode(1, 0.6), new SvmNode(2, 0.001)},
			{new SvmNode(1, 0.001), new SvmNode(2, 0.4)},
			{new SvmNode(1, 0.1), new SvmNode(2, 0.7)},
			{new SvmNode(1, 0.55), new SvmNode(2,0.023)},
			{new SvmNode(1, 0.04), new SvmNode(2,0.3)},
			{new SvmNode(1, 0.43), new SvmNode(2,0.1)},
			{new SvmNode(1, 0.2)},
			{new SvmNode(1, 0.9)},
			{new SvmNode(2, 0.6)}	
		};
	}
	
	//Using this method just tests that any change I make does not change the calculation output
	private double[] calculateExpectedXSquare(SvmNode[][] x) {
		double[] xSquare = new double[x.length];
		for (int i = 0; i < x.length; i++){
			xSquare[i] = Kernel.dot(x[i], x[i]);				
		}
		return xSquare;
	}
}
