package org.mann.libsvm;
public class SvmNode implements java.io.Serializable
{
	public int index;
	public double value;
	
	public SvmNode(){};
	
	public SvmNode(int i, double j) {
		this.index = i;
		this.value = j;				
	}
}
