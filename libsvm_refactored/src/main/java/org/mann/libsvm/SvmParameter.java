package org.mann.libsvm;


public class SvmParameter implements Cloneable,java.io.Serializable
{
	
	public enum SvmType{
		c_svc, nu_svc, one_class, epsilon_svr, nu_svr
	}
	
	public enum KernelType {
		linear, poly, rbf, sigmoid, precomputed
	}

	public SvmType svmType;
	public KernelType kernelType;
	public int degree;	// for poly
	public double gamma;	// for poly/rbf/sigmoid
	public double coef0;	// for poly/sigmoid

	// these are for training only
	public double cache_size; // in MB
	public double epsilonTolerance;	// stopping criteria
	public double costC;	// for C_SVC, EPSILON_SVR and NU_SVR
	public int nr_weight;		// for C_SVC
	public int[] weight_label;	// for C_SVC
	public double[] weight;		// for C_SVC
	public double nu;	// for NU_SVC, ONE_CLASS, and NU_SVR
	public double epsilonLossFunction;	// for EPSILON_SVR
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

	public void setDefaultValues() {
		svmType = SvmType.c_svc;
		kernelType = KernelType.rbf;
		degree = 3;
		gamma = 0; // 1/num_features
		coef0 = 0;
		nu = 0.5;
		cache_size = 100;
		costC = 1;
		epsilonTolerance = 1e-3;
		epsilonLossFunction = 0.1;
		shrinking = 1;
		probability = 0;
		nr_weight = 0;
		weight_label = new int[0];
		weight = new double[0];
		
	}

}
