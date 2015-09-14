package org.mann.libsvm.kernel;

import java.util.Arrays;

import org.mann.libsvm.SvmNode;
import org.mann.libsvm.SvmParameter;
import org.mann.libsvm.SvmParameter.KernelType;

//
//Kernel evaluation
//
//the static method k_function is for doing single kernel evaluation
//the constructor of Kernel prepares to calculate the l*l kernel matrix
//the member function get_Q is for getting one column from the Q Matrix
//

public class Kernel extends QMatrix {
	private SvmNode[][] x;
	
	//TODO: this is only needed for rbf
	private final double[] x_square;

	// SvmParameter
	private final KernelType kernelType;
	private final int degree;
	private final double gamma;
	private final double coef0;

	public Kernel(int l, SvmNode[][] x_, SvmParameter param) {
		this.kernelType = param.getKernelType();
		this.degree = param.getDegree();
		this.gamma = param.getGamma();
		this.coef0 = param.getCoef0();
		
		x = (SvmNode[][]) x_.clone();
		
		if (kernelType == KernelType.rbf) {
			x_square = new double[l];
			for (int i = 0; i < l; i++){
				x_square[i] = dot(x[i], x[i]);				
			}
		} else
			x_square = null;
	}

	//TODO: change to return a copy to preserve immutability
	public SvmNode[][] getX(){
		return x;
	}
	
	public double[] getXSquare(){
		return x_square == null ? null : Arrays.copyOf(x_square, x.length);
	}
	
	@Override
	public void swap_index(int i, int j) {
		do {
			SvmNode[] temp = x[i];
			x[i] = x[j];
			x[j] = temp;
		} while (false);
		if (x_square != null) {
			do {
				double temp = x_square[i];
				x_square[i] = x_square[j];
				x_square[j] = temp;
			} while (false);
		}	
	}

	protected static double powi(double base, int times) {
		double tmp = base, ret = 1.0;

		for (int t = times; t > 0; t /= 2) {
			if (t % 2 == 1)
				ret *= tmp;
			tmp = tmp * tmp;
		}
		return ret;
	}
	
	//TODO: test these with new calc
	double kernel_function(int i, int j) {
		switch (kernelType) {
		case linear:
			return dot(x[i], x[j]);
		case poly:
			return powi(gamma * dot(x[i], x[j]) + coef0, degree);
		case rbf:
			return Math.exp(-gamma
					* (x_square[i] + x_square[j] - 2 * dot(x[i], x[j])));
		case sigmoid:
			return Math.tanh(gamma * dot(x[i], x[j]) + coef0);
		case precomputed:
			return x[i][(int) (x[j][0].value)].value;
		default:
			return 0; // java
		}
	}

	static double dot(SvmNode[] trainingInstance1, SvmNode[] trainingInstance2) {
		double sum = 0;
		int t1Length = trainingInstance1.length; //how important is it to be stateless?
		int t2Length= trainingInstance2.length; 
		int i = 0;
		int j = 0;
		while (i < t1Length && j < t2Length) { 
			if (trainingInstance1[i].index == trainingInstance2[j].index) {
				sum += trainingInstance1[i++].value * trainingInstance2[j++].value; 
			}else {
				if (trainingInstance1[i].index > trainingInstance2[j].index){
					j++;					
				} else{					
					i++;
				}
			}
		}
		return sum;
	}

	public static double k_function(SvmNode[] x, SvmNode[] y, SvmParameter param) {
		switch (param.getKernelType()) {
		case linear:
			return dot(x, y);
		case poly:
			return powi(param.getGamma() * dot(x, y) + param.getCoef0(), param.getDegree());
		case rbf: {
			double sum = 0;
			int xlen = x.length;
			int ylen = y.length;
			int i = 0;
			int j = 0;
			while (i < xlen && j < ylen) {
				if (x[i].index == y[j].index) {
					double d = x[i++].value - y[j++].value;
					sum += d * d;
				} else if (x[i].index > y[j].index) {
					sum += y[j].value * y[j].value;
					++j;
				} else {
					sum += x[i].value * x[i].value;
					++i;
				}
			}

			while (i < xlen) {
				sum += x[i].value * x[i].value;
				++i;
			}

			while (j < ylen) {
				sum += y[j].value * y[j].value;
				++j;
			}

			return Math.exp(-param.getGamma() * sum);
		}
		case sigmoid:
			return Math.tanh(param.getGamma() * dot(x, y) + param.getCoef0());
		case precomputed:
			return x[(int) (y[0].value)].value;
		default:
			return 0; // java
		}
	}

	@Override
	public float[] get_Q(int column, int len) {
		throw new UnsupportedOperationException("get_Q not implemented in Kernel");
	}

	@Override
	public double[] get_QD() {
		throw new UnsupportedOperationException("get_QD not implemented in Kernel");
	}
}
