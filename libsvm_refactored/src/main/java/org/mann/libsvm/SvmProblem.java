package org.mann.libsvm;

//TODO: Either implement properly or remove Serializable
//Expires 7th November 2015
public class SvmProblem implements java.io.Serializable
{
	public int length;
	public double[] y;
	public SvmNode[][] x;
}
