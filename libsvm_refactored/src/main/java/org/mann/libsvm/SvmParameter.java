package org.mann.libsvm;

import org.apache.commons.cli.CommandLine;
import org.mann.libsvm.SvmParameter.KernelType;
import org.mann.libsvm.SvmParameter.SvmType;
import org.mann.validation.commandline.SvmCommandLine;
import org.mann.validation.commandline.SvmTrainCommandLineParser;

public class SvmParameter implements Cloneable, java.io.Serializable {

	public enum SvmType {
		c_svc, nu_svc, one_class, epsilon_svr, nu_svr;
	}

	public enum KernelType {
		linear, poly, rbf, sigmoid, precomputed;
	}

	private SvmType svmType;
	private KernelType kernelType;
	private int degree; // for poly
	private double gamma; // for poly/rbf/sigmoid
	private double coef0; // for poly/sigmoid

	// these are for training only
	private double cache_size; // in MB
	private double epsilonTolerance; // stopping criteria
	private double nu; // for NU_SVC, ONE_CLASS, and NU_SVR
	private double epsilonLossFunction; // for EPSILON_SVR
	private int shrinking; // use the shrinking heuristics

	// TODO: tdd the changes needed to make these private
	// Expires 13th October 2015
	public int nr_weight; // for C_SVC
	private int[] weight_label; // for C_SVC
	private double[] weight; // for C_SVC
	private int probability; // do probability estimates
	public double costC; // for C_SVC, EPSILON_SVR and NU_SVR

//	private SvmParameter(SvmParameter svmParameter) {
//	}
//
//	public SvmParameter(){};
//	
//	public SvmParameter svmType(SvmType svmType) {
//		this.svmType = svmType;
//		return this;
//	}
//
//	public SvmParameter kernelType(KernelType kernelType) {
//		this.kernelType = kernelType;
//		return this;
//	}
//
//	public SvmParameter degree(int degree) {
//		this.degree = degree;
//		return this;
//	}
//
//	public SvmParameter gamma(double gamma) {
//		this.gamma = gamma;
//		return this;
//	}
//
//	public SvmParameter coef0(double coef0) {
//		this.coef0 = coef0;
//		return this;
//	}
//
//	public SvmParameter cacheSize(double cacheSize) {
//		this.cache_size = cacheSize;
//		return this;
//	}
//
//	public SvmParameter epsilonTolerance(double epsTolerance) {
//		this.epsilonTolerance = epsTolerance;
//		return this;
//	}
//
//	public SvmParameter epsilonLossFunction(double epsLossFunction) {
//		this.epsilonLossFunction = epsLossFunction;
//		return this;
//	}
//
//	public SvmParameter costC(double costC) {
//		this.costC = costC;
//		return this;
//	}
//
//	public SvmParameter shrinking(int shrinking) {
//		this.shrinking = shrinking;
//		return this;
//	}
//
//	public SvmParameter probability(int probability) {
//		this.probability = probability;
//		return this;
//	}
//
//	public SvmParameter nu(double nu) {
//		this.nu = nu;
//		return this;
//	}
//
//	public SvmParameter nrWeight(int nrWeight) {
//		this.nr_weight = nrWeight;
//		return this;
//	}
//
//	public SvmParameter weightLabel(int[] weightLabel) {
//		this.weight_label = weightLabel;
//		return this;
//	}
//
//	public SvmParameter build(){
//		return new SvmParameter(this);
//	}
//	
//	public SvmParameter weight(double[] weight) {
//		this.weight = weight;
//		return this;
//	}

	public SvmType getSvmType() {
		return svmType;
	}

	public KernelType getKernelType() {
		return kernelType;
	}

	public int getDegree() {
		return degree;
	}

	public double getGamma() {
		return gamma;
	}

	public double getCoef0() {
		return coef0;
	}

	public double getCache_size() {
		return cache_size;
	}

	public double getEpsilonTolerance() {
		return epsilonTolerance;
	}

	public double getCostC() {
		return costC;
	}

	public int getNr_weight() {
		return nr_weight;
	}

	public int[] getWeight_label() {
		return weight_label;
	}

	public double[] getWeight() {
		return weight;
	}

	public double getNu() {
		return nu;
	}

	public double getEpsilonLossFunction() {
		return epsilonLossFunction;
	}

	public int getShrinking() {
		return shrinking;
	}

	public int getProbability() {
		return probability;
	}

	public Object clone() {
		try {
			return super.clone();
		} catch (CloneNotSupportedException e) {
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

	public void initializeFields(SvmCommandLine cmd, SvmTrainCommandLineParser parser) {
		//TODO: change this terrible implementation
		//Expires October 14th 2015
		setDefaultValues();
		svmType = cmd.hasOption("s") ? (SvmType)parser.getOptionValue(cmd.getOption("s")) : SvmType.c_svc;
		kernelType = cmd.hasOption("t") ? (KernelType)parser.getOptionValue(cmd.getOption("t")) : KernelType.rbf;
		degree = cmd.hasOption("d") ? (Integer)parser.getOptionValue(cmd.getOption("d")) : 3;
		gamma = cmd.hasOption("g") ? (Double)parser.getOptionValue(cmd.getOption("g")) : 0;
		coef0 = cmd.hasOption("r") ? (Double)parser.getOptionValue(cmd.getOption("r")) : 0;
		nu = cmd.hasOption("n") ? (Double)parser.getOptionValue(cmd.getOption("n")) : 0.5;
		cache_size = cmd.hasOption("m") ? (Double)parser.getOptionValue(cmd.getOption("m")) : 100;
		costC = cmd.hasOption("c") ? (Double)parser.getOptionValue(cmd.getOption("c")) : 1;
		epsilonTolerance = cmd.hasOption("p") ? (Double)parser.getOptionValue(cmd.getOption("p")) : 1e-3;
		epsilonLossFunction = cmd.hasOption("e") ? (Double)parser.getOptionValue(cmd.getOption("e")) : 0.1;
		shrinking = cmd.hasOption("h") ? (Integer)parser.getOptionValue(cmd.getOption("h")) : 1;
		probability = cmd.hasOption("b") ? (Integer)parser.getOptionValue(cmd.getOption("b")) : 0;
	}

	public void setGamma(double gamma) {
		this.gamma = gamma;
	}

	public void setSvmType(SvmType svmType) {
		this.svmType = svmType;
	}

	public void setCacheSize(double cacheSize) {
		this.cache_size = cacheSize;
	}

	public void setEpsilonTolerance(double eps) {
		this.epsilonTolerance = eps;
	}

	public void setKernelType(KernelType kernelType) {
		this.kernelType = kernelType;
	}

	public void setNu(double nu) {
		this.nu = nu;
	}

	public void setProbability(int probability) {
		this.probability = probability;
	}

	public void setCostC(double costC) {
		this.costC = costC;
	}

	public void setDegree(int degree) {
		this.degree = degree;
	}

	public void setShrinking(int shrinking) {
		this.shrinking = shrinking;
	}

	public void setNrWeight(int nrWeight) {
		this.nr_weight = nrWeight;
	}

	public void setEpsilonLossFunction(int epsLossFunction) {
		this.epsilonLossFunction = epsLossFunction;
	}

	public void setWeightLabel(int[] weightLabel) {
		this.weight_label = weightLabel;
	}

	public void setWeight(double[] weight) {
		this.weight = weight;
	}

}
