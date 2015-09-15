package org.mann.validation.commandline;

import java.util.Arrays;
import java.util.List;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.Option;
import org.mann.libsvm.SvmParameter.KernelType;
import org.mann.libsvm.SvmParameter.SvmType;


public class CommandLineWrapper {
	private CommandLine cmd;
	
	public CommandLineWrapper(CommandLine cmd) {
		this.cmd = cmd;
	}

	public List<String> getArgsList(){
		return cmd.getArgList();
	}
	
	public CommandLine getCommandLine(){
		return cmd;
	}
	public Option getOption(String opt) {
		opt = stripLeadingHyphens(opt);
		for (Option option : cmd.getOptions()) {
			
			if (opt.equals(option.getOpt())) {
				return option;
			}

			if (opt.equals(option.getLongOpt())) {
				return option;
			}

		}
		return null;
	}

	private String stripLeadingHyphens(String option) {
		if (option == null) {
			return null;
		}
		if (option.startsWith("--")) {
			return option.substring(2);
		} else if (option.startsWith("-")) {
			return option.substring(1);
		}
		return option;

	}
	
	//TODO: Too brittle perhaps; look for alternative
		//also its not that easy to option
		//Expires 15th October 2015
		public Object getOptionValue(String optionName) {
			Option option = getOption(optionName);
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
			if(option.getType()==int[].class){
				int[] weight = new int[]{
						Integer.parseInt(option.getValue(0)), 
						Integer.parseInt(option.getValue(1))}; //only weight at the moment - change variable name if expand
				
				return weight;
			}
			return option.getValue();
		}
	
}
