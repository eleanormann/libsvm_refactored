package org.mann.libsvm;

import org.apache.commons.cli.CommandLine;
import org.mann.libsvm.SvmParameter.KernelType;
import org.mann.libsvm.SvmParameter.SvmType;
import org.mann.validation.commandline.CommandLineWrapper;
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

	public void initializeFields(CommandLineWrapper cmdWrapper, SvmTrainCommandLineParser parser) {
		//TODO: change this terrible implementation
		//Expires October 14th 2015
		setDefaultValues();
		CommandLine cmd = cmdWrapper.getCommandLine();
		svmType = cmd.hasOption("s") ? (SvmType)cmdWrapper.getOptionValue("s") : SvmType.c_svc;
		kernelType = cmd.hasOption("t") ? (KernelType)cmdWrapper.getOptionValue("t") : KernelType.rbf;
		degree = cmd.hasOption("d") ? (Integer)cmdWrapper.getOptionValue("d") : 3;
		gamma = cmd.hasOption("g") ? (Double)cmdWrapper.getOptionValue("g") : 0;
		coef0 = cmd.hasOption("r") ? (Double)cmdWrapper.getOptionValue("r") : 0;
		nu = cmd.hasOption("n") ? (Double)cmdWrapper.getOptionValue("n") : 0.5;
		cache_size = cmd.hasOption("m") ? (Double)cmdWrapper.getOptionValue("m") : 100;
		costC = cmd.hasOption("c") ? (Double)cmdWrapper.getOptionValue("c") : 1;
		epsilonTolerance = cmd.hasOption("p") ? (Double)cmdWrapper.getOptionValue("p") : 1e-3;
		epsilonLossFunction = cmd.hasOption("e") ? (Double)cmdWrapper.getOptionValue("e") : 0.1;
		shrinking = cmd.hasOption("h") ? (Integer)cmdWrapper.getOptionValue("h") : 1;
		probability = cmd.hasOption("b") ? (Integer)cmdWrapper.getOptionValue("b") : 0;
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

	public void setEpsilonLossFunction(double epsLossFunction) {
		this.epsilonLossFunction = epsLossFunction;
	}

	public void setWeightLabel(int[] weightLabel) {
		this.weight_label = weightLabel;
	}

	public void setWeight(double[] weight) {
		this.weight = weight;
	}

	public void setCoef0(double coef0) {
		this.coef0 = coef0;
	}

}
