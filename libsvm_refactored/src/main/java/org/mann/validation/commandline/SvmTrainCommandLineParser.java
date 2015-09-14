package org.mann.validation.commandline;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.mann.helpers.HelpMessages;
import org.mann.libsvm.SvmParameter.KernelType;
import org.mann.libsvm.SvmParameter.SvmType;

public class SvmTrainCommandLineParser {

	public CommandLine parseCommandLine(String[] commandLine) throws ParseException {
		Options options = new Options();
		options.addOption(buildSvmTypeOption());
		options.addOption(buildKernelTypeOption());
		options.addOption(buildDegreeOption());
		options.addOption(buildGammaOption());
		options.addOption(buildCoeff0Option());
		options.addOption(buildCostOption());
		options.addOption(buildNuOption());
		options.addOption(buildEpsilonLossOption());
		options.addOption(buildCacheSizeOption());
		options.addOption(buildEpsilonToleranceOption());
		options.addOption(buildShrinkingOption());
		options.addOption(buildProbabilityEstOption());
		options.addOption(buildWeightOption());
		options.addOption(buildCrosValNumOption());
		options.addOption(buildQuietModeOption());
		CommandLineParser parser = new DefaultParser();
		return parser.parse(options, commandLine);
	}

	private Option buildQuietModeOption() {
		return Option.builder("q").hasArg(false).required(false)
				.longOpt("Quiet mode").desc(HelpMessages.QUIET_MODE).build();
	}

	private Option buildCrosValNumOption() {
		return Option.builder("v").hasArg(true).required(false).type(Integer.class)
				.longOpt("N-fold cross-validation").desc(HelpMessages.N_FOLD_CROSS_VALIDATION).build();
	}

	private Option buildWeightOption() {
		return Option.builder("w").numberOfArgs(2).required(false)
				.longOpt("Weight").desc(HelpMessages.WEIGHT).build();
	}

	private Option buildProbabilityEstOption() {
		return Option.builder("b").hasArg(true).required(false).type(Integer.class)
				.longOpt("Probability estimates").desc(HelpMessages.PROBABILITY_ESTIMATES).build();
	}

	private Option buildShrinkingOption() {
		return Option.builder("h").hasArg(true).required(false).type(Integer.class)
				.longOpt("Shrinking").desc(HelpMessages.SHRINKING).build();
	}

	private Option buildEpsilonToleranceOption() {
		return Option.builder("e").hasArg(true).required(false).type(Double.class)
				.longOpt("Epsilon (tolerance)").desc(HelpMessages.EPSILON_TOLERANCE).build();
	}

	private Option buildCacheSizeOption() {
		return Option.builder("m").hasArg(true).required(false).type(Double.class)
				.longOpt("Cache size").desc(HelpMessages.CACHE_SIZE).build();
	}

	private Option buildEpsilonLossOption() {
		return Option.builder("p").hasArg(true).required(false).type(Double.class)
				.longOpt("Epsilon in loss function").desc(HelpMessages.EPSILON_LOSS_FUNCT).build();
	}

	private Option buildNuOption() {
		return Option.builder("n").hasArg(true).required(false).type(Double.class)
				.longOpt("Nu").desc(HelpMessages.NU).build();
	}

	private Option buildCostOption() {
		return Option.builder("c").hasArg(true).required(false).type(Double.class)
				.longOpt("Cost (C)").desc(HelpMessages.COST).build();
	}

	private Option buildCoeff0Option() {
		return Option.builder("r").hasArg(true).required(false).type(Double.class)
				.longOpt("Coeff0").desc(HelpMessages.COEFF0).build();
	}

	private Option buildGammaOption() {
		return Option.builder("g").hasArg(true).required(false).type(Double.class)
				.longOpt("Gamma").desc(HelpMessages.GAMMA).build();
	}

	private Option buildDegreeOption() {
		return Option.builder("d").hasArg(true).required(false).type(Integer.class)
				.longOpt("Degree").desc(HelpMessages.DEGREE).build();
	}

	private Option buildKernelTypeOption() {
		return Option.builder("t").hasArg(true).required(false).type(KernelType.class)
				.longOpt("Kernel type").desc(HelpMessages.KERNEL).build();
	}

	private Option buildSvmTypeOption() {
		return Option.builder("s").hasArg(true).required(false).type(SvmType.class)
				.longOpt("Svm type").desc(HelpMessages.SVM_TYPE).build();
	}

	public Object getOptionValue(Option option) {
		if(option.getType().equals(Integer.class)){
			return Integer.parseInt(option.getValue());			
		}
		if(option.getType()==Double.class){
			return Double.parseDouble(option.getValue());
		}
		if(option.getType()==SvmType.class){
			return SvmType.valueOf(option.getValue());
		}
		if(option.getType()==KernelType.class){
			return KernelType.valueOf(option.getValue());
		}
		return option.getValue();
	}
}
