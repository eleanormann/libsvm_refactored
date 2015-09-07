package org.mann.libsvm.kernel;

public abstract class QMatrix {
	public abstract float[] get_Q(int column, int len);

	public abstract double[] get_QD();

	public abstract void swap_index(int i, int j);
}
