package org.mann.libsvm.kernel;

import org.mann.libsvm.Cache;
import org.mann.libsvm.kernel.Kernel;
import org.mann.libsvm.SvmParameter;
import org.mann.libsvm.svm_problem;

//
//Q matrices for various formulations
//
public class SVC_Q extends Kernel {
	private final byte[] y;
	private final Cache cache;
	private final double[] QD;

	public SVC_Q(svm_problem prob, SvmParameter param, byte[] y_) {
		super(prob.length, prob.x, param);
		y = (byte[]) y_.clone();
		cache = new Cache(prob.length, (long) (param.cache_size * (1 << 20)));
		QD = new double[prob.length];
		for (int i = 0; i < prob.length; i++)
			QD[i] = kernel_function(i, i);
	}
	
	@Override
	public float[] get_Q(int i, int len) {
		float[][] data = new float[1][];
		int start, j;
		if ((start = cache.get_data(i, data, len)) < len) {
			for (j = start; j < len; j++)
				data[0][j] = (float) (y[i] * y[j] * kernel_function(i, j));
		}
		return data[0];
	}
	
	@Override
	public double[] get_QD() {
		return QD;
	}
	
	@Override
	public void swap_index(int i, int j) {
		cache.swap_index(i, j);
		super.swap_index(i, j);
		do {
			byte _ = y[i];
			y[i] = y[j];
			y[j] = _;
		} while (false);
		do {
			double _ = QD[i];
			QD[i] = QD[j];
			QD[j] = _;
		} while (false);
	}
}