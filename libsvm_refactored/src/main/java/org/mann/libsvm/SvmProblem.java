package org.mann.libsvm;

public class SvmProblem implements java.io.Serializable
{
	public int length;
	public double[] y;
	public SvmNode[][] x;
}
