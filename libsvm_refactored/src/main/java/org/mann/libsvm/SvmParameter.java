package org.mann.libsvm;

import org.mann.libsvm.SvmParameter.SvmType;

public class SvmParameter implements Cloneable,java.io.Serializable
{
	
	public enum SvmType{
		C_SVC, NU_SVC, ONE_CLASS, EPSILON_SVR, NU_SVR
	}
	
	public enum KernelType {
		linear, poly, rbf, sigmoid, precomputed
	}
		
	/* svm_type 
	 * TODO replace all refs with SvmType and remove
	 * Comment expires 18th September 2015*/
	public static final int C_SVC = 0;
	public static final int NU_SVC = 1;
	public static final int ONE_CLASS = 2;
	public static final int EPSILON_SVR = 3;
	public static final int NU_SVR = 4;

	/* kernel_type */
	public static final int LINEAR = 0;
	public static final int POLY = 1;
	public static final int RBF = 2;
	public static final int SIGMOID = 3;
	public static final int PRECOMPUTED = 4;

	public SvmType svmType;
	public int kernel_type;
	public int degree;	// for poly
	public double gamma;	// for poly/rbf/sigmoid
	public double coef0;	// for poly/sigmoid

	// these are for training only
	public double cache_size; // in MB
	public double eps;	// stopping criteria
	public double C;	// for C_SVC, EPSILON_SVR and NU_SVR
	public int nr_weight;		// for C_SVC
	public int[] weight_label;	// for C_SVC
	public double[] weight;		// for C_SVC
	public double nu;	// for NU_SVC, ONE_CLASS, and NU_SVR
	public double p;	// for EPSILON_SVR
	public int shrinking;	// use the shrinking heuristics
	public int probability; // do probability estimates

	public Object clone() 
	{
		try 
		{
			return super.clone();
		} catch (CloneNotSupportedException e) 
		{
			return null;
		}
	}

	public int getIntEquivalentOfSvmType(SvmType svmType) {
		switch(svmType){
		case C_SVC:
			return C_SVC;
		case NU_SVC:
			return NU_SVC;
		case ONE_CLASS:
			return ONE_CLASS;
		case EPSILON_SVR:
			return EPSILON_SVR;
		case NU_SVR:
			return NU_SVR;
		default:
			throw new IllegalArgumentException("SVM type not recognised");	
		}
	}
	
	//TODO move to UI handler and rework out of a switch
	//Comment expires 30th September 2015
    public SvmType getSvmTypeFromSvmParameter(int oldSvmType) {
    	switch(oldSvmType){
    		case C_SVC:
				return SvmType.C_SVC;
			case 1:
				return SvmType.NU_SVC;
			case 2:
				return SvmType.ONE_CLASS;
			case 3:
				return SvmType.EPSILON_SVR;
			case 4: 
				return SvmType.NU_SVR;
			default:
				throw new IllegalArgumentException("SVM type not recognised");			
		}
	}

}
