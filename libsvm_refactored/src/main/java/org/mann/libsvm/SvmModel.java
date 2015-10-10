//
// svm_model
//
package org.mann.libsvm;
public class SvmModel implements java.io.Serializable
{
	private SvmParameter param;	// parameter
	public int numClasses;		// number of classes, = 2 in regression/one class svm
	public int totalSv;			// total #SV
	public SvmNode[][] SV;	// SVs (SV[l])
	public double[][] sv_coef;	// coefficients for SVs in decision functions (sv_coef[k-1][l])
	public double[] rho;		// constants in decision functions (rho[k*(k-1)/2])
	public double[] probA;         // pariwise probability information
	public double[] probB;
	public int[] sv_indices;       // sv_indices[0,...,nSV-1] are values in [1,...,num_traning_data] to indicate SVs in the training set

	// for classification only

	public int[] classLabel;		// label of each class (label[k])
	public int[] nSV;		// number of SVs for each class (nSV[k])
				// nSV[0] + nSV[1] + ... + nSV[k-1] = l
	
	public SvmParameter getParam() {
		return param;
	}
	
	public void setParam(SvmParameter param) {
		this.param = param;
	}
	
	
};
