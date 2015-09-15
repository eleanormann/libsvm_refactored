package org.mann.validation.commandline;

import java.util.Arrays;
import java.util.List;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.Option;


public class SvmCommandLine extends CommandLine {
	private static final long serialVersionUID = 1L;
	private List<Option> options;
	
	public SvmCommandLine(CommandLine cmd) {
		this.options = Arrays.asList(cmd.getOptions());
	}

	public Option getOption(String opt) {
		opt = stripLeadingHyphens(opt);
		for (Option option : options) {
			
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
	
}
