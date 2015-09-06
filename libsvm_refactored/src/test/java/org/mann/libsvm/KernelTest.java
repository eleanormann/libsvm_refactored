package org.mann.libsvm;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertThat;

import org.junit.Test;

public class KernelTest {

	
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
}
