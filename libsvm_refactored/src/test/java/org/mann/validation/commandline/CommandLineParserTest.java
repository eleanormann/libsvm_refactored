package org.mann.validation.commandline;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertThat;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.ParseException;
import org.apache.commons.cli.UnrecognizedOptionException;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.mann.libsvm.SvmParameter.KernelType;
import org.mann.libsvm.SvmParameter.SvmType;

public class CommandLineParserTest {
	private SvmTrainCommandLineParser validator;
	
	@Rule 
	public ExpectedException thrown = ExpectedException.none();
	
	@Before
	public void createSvmTrain(){
		validator = new SvmTrainCommandLineParser();
	}
	
	@Test
	public void parseCommandLinethrowsExceptionWhenOptionNotRecognized() throws ParseException{
		thrown.expect(UnrecognizedOptionException.class);
		validator.parseCommandLine(new String[]{"-u", "0"});
	}
	
	@Test
	public void parseCommandLineSetsSvmType() throws ParseException {
		SvmCommandLine output = validator.parseCommandLine(new String[]{"-s", "c_svc"});
		Option option = output.getOption("s");
		assertThat((SvmType)validator.getOptionValue(option), equalTo(SvmType.c_svc));
	}
	
	@Test
	public void parseCommandLineSetsKernelType() throws ParseException {
		SvmCommandLine output = validator.parseCommandLine(new String[]{"-t", "linear"});
		Option option = output.getOption("t");
		assertThat((KernelType)validator.getOptionValue(option), equalTo(KernelType.linear));
	}

	@Test
	public void parseCommandLineSetsDegree() throws ParseException {
		SvmCommandLine output = validator.parseCommandLine(new String[]{"-d", "1"});
		Option option = output.getOption("d");
		assertThat((Integer) validator.getOptionValue(option), equalTo(1));
	}
	
	@Test
	public void parseCommandLineSetsGamma() throws ParseException {
		SvmCommandLine output = validator.parseCommandLine(new String[]{"-g", "2"});
		Option option = output.getOption("g");
		assertThat((Double) validator.getOptionValue(option), equalTo(2.0));
	}
	
	@Test
	public void parseCommandLineSetsCoef0() throws ParseException {
		SvmCommandLine output = validator.parseCommandLine(new String[]{"-r", "1.4"});
		Option option = output.getOption("r");
		assertThat((Double) validator.getOptionValue(option), equalTo(1.4));
	}
	
	@Test
	public void parseCommandLineSetsCost() throws ParseException {
		SvmCommandLine output = validator.parseCommandLine(new String[]{"-c", "1.5"});
		Option option = output.getOption("c");
		assertThat((Double) validator.getOptionValue(option), equalTo(1.5));
	}
	
	@Test
	public void parseCommandLineSetsEpsilonInLossFunction() throws ParseException {
		SvmCommandLine output = validator.parseCommandLine(new String[]{"-p", "2"});
		Option option = output.getOption("p");
		assertThat((Double) validator.getOptionValue(option), equalTo(2.0));
	}
	
	@Test
	public void parseCommandLineSetsCacheSize() throws ParseException {
		SvmCommandLine output = validator.parseCommandLine(new String[]{"-m", "101.0"});
		Option option = output.getOption("m");
		assertThat((Double) validator.getOptionValue(option), equalTo(101.0));
	}
	
	@Test
	public void parseCommandLineSetsEpsilonTolerance() throws ParseException {
		SvmCommandLine output = validator.parseCommandLine(new String[]{"-e", "0.003"});
		Option option = output.getOption("e");
		assertThat((Double) validator.getOptionValue(option), equalTo(0.003));
	}
	
	@Test
	public void parseCommandLineSetsShrinking() throws ParseException {
		SvmCommandLine output = validator.parseCommandLine(new String[]{"-h", "0"});
		Option option = output.getOption("h");
		assertThat((Integer) validator.getOptionValue(option), equalTo(0));
	}
	
	@Test
	public void parseCommandLineSetsProbabilityEstimates() throws ParseException {
		SvmCommandLine output = validator.parseCommandLine(new String[]{"-b", "1"});
		Option option = output.getOption("b");
		assertThat((Integer) validator.getOptionValue(option), equalTo(1));
	}
	
	@Test
	public void parseCommandLineSetsWeight() throws ParseException {
		SvmCommandLine output = validator.parseCommandLine(new String[]{"-w","1", "0.9"});
		int[] optionValue = (int[]) validator.getOptionValue(output.getOption("w"));
		assertThat(optionValue[0], equalTo(1));
		assertThat(optionValue[1], equalTo(0));
	}
	
	@Test
	public void parseCommandLineSetsNOfCrossValidationMode() throws ParseException {
		SvmCommandLine output = validator.parseCommandLine(new String[]{"-v", "2"});
		Option option = output.getOption("v");
		assertThat((Integer) validator.getOptionValue(option), equalTo(2));
	}
	
	@Test
	public void parseCommandLineSetsQuietMode() throws ParseException {
		SvmCommandLine output = validator.parseCommandLine(new String[]{"-q"});
		assertThat(output.hasOption("q"), equalTo(true));
	}

	@Test
	public void parseCommandLineParsesMultipleOptions() throws ParseException{
		String[] options = new String[]{"-s", "c_svc", "-t", "rbf", "-m", "200", "-q", "-w", "1", "2"};
		SvmCommandLine output = validator.parseCommandLine(options);
		assertThat((SvmType)validator.getOptionValue(output.getOption("s")), equalTo(SvmType.c_svc));
		assertThat((KernelType)validator.getOptionValue(output.getOption("t")), equalTo(KernelType.rbf));
		assertThat((Double)validator.getOptionValue(output.getOption("m")), equalTo(200.0));
		assertThat(output.hasOption("q"), equalTo(true));
		int[] optionValue = (int[]) validator.getOptionValue(output.getOption("w"));
		assertThat(optionValue[0], equalTo(1));
		assertThat(optionValue[1], equalTo(2));	
	}
	
}

