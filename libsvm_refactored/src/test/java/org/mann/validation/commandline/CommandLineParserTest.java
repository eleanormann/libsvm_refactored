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
		CommandLineWrapper output = validator.parseCommandLine(new String[]{"-s", "c_svc"});
		assertThat((SvmType)output.getOptionValue("s"), equalTo(SvmType.c_svc));
	}
	
	@Test
	public void parseCommandLineSetsKernelType() throws ParseException {
		CommandLineWrapper output = validator.parseCommandLine(new String[]{"-t", "linear"});
		assertThat((KernelType)output.getOptionValue("t"), equalTo(KernelType.linear));
	}

	@Test
	public void parseCommandLineSetsDegree() throws ParseException {
		CommandLineWrapper output = validator.parseCommandLine(new String[]{"-d", "1"});
		assertThat((Integer)  output.getOptionValue("d"), equalTo(1));
	}
	
	@Test
	public void parseCommandLineSetsGamma() throws ParseException {
		CommandLineWrapper output = validator.parseCommandLine(new String[]{"-g", "2"});
		assertThat((Double) output.getOptionValue("g"), equalTo(2.0));
	}
	
	@Test
	public void parseCommandLineSetsCoef0() throws ParseException {
		CommandLineWrapper output = validator.parseCommandLine(new String[]{"-r", "1.4"});
		assertThat((Double) output.getOptionValue("r"), equalTo(1.4));
	}
	
	@Test
	public void parseCommandLineSetsCost() throws ParseException {
		CommandLineWrapper output = validator.parseCommandLine(new String[]{"-c", "1.5"});
		assertThat((Double) output.getOptionValue("c"), equalTo(1.5));
	}
	
	@Test
	public void parseCommandLineSetsEpsilonInLossFunction() throws ParseException {
		CommandLineWrapper output = validator.parseCommandLine(new String[]{"-p", "2"});
		assertThat((Double) output.getOptionValue("p"), equalTo(2.0));
	}
	
	@Test
	public void parseCommandLineSetsCacheSize() throws ParseException {
		CommandLineWrapper output = validator.parseCommandLine(new String[]{"-m", "101.0"});
		assertThat((Double) output.getOptionValue("m"), equalTo(101.0));
	}
	
	@Test
	public void parseCommandLineSetsEpsilonTolerance() throws ParseException {
		CommandLineWrapper output = validator.parseCommandLine(new String[]{"-e", "0.003"});
		assertThat((Double) output.getOptionValue("e"), equalTo(0.003));
	}
	
	@Test
	public void parseCommandLineSetsShrinking() throws ParseException {
		CommandLineWrapper output = validator.parseCommandLine(new String[]{"-h", "0"});
		assertThat((Integer) output.getOptionValue("h"), equalTo(0));
	}
	
	@Test
	public void parseCommandLineSetsProbabilityEstimates() throws ParseException {
		CommandLineWrapper output = validator.parseCommandLine(new String[]{"-b", "1"});
		assertThat((Integer) output.getOptionValue("b"), equalTo(1));
	}
	
	@Test
	public void parseCommandLineSetsWeight() throws ParseException {
		CommandLineWrapper output = validator.parseCommandLine(new String[]{"-w","1", "0"});
		assertThat(((int[])output.getOptionValue("w"))[0], equalTo(1));
		assertThat(((int[])output.getOptionValue("w"))[1], equalTo(0));
	}
	
	@Test
	public void parseCommandLineSetsNOfCrossValidationMode() throws ParseException {
		CommandLineWrapper output = validator.parseCommandLine(new String[]{"-v", "2"});
		assertThat((Integer) output.getOptionValue("v"), equalTo(2));
	}
	
	@Test
	public void parseCommandLineSetsQuietMode() throws ParseException {
		CommandLineWrapper output = validator.parseCommandLine(new String[]{"-q"});
		assertThat(output.getCommandLine().hasOption("q"), equalTo(true));
	}

	@Test
	public void parseCommandLineParsesMultipleOptions() throws ParseException{
		String[] options = new String[]{"-s", "c_svc", "-t", "rbf", "-m", "200", "-q", "-w", "1", "2"};
		CommandLineWrapper output = validator.parseCommandLine(options);
		assertThat((SvmType)output.getOptionValue("s"), equalTo(SvmType.c_svc));
		assertThat((KernelType)output.getOptionValue("t"), equalTo(KernelType.rbf));
		assertThat((Double)output.getOptionValue("m"), equalTo(200.0));
		assertThat(output.getCommandLine().hasOption("q"), equalTo(true));
		int[] optionValue = (int[]) output.getOptionValue("w");
		assertThat(optionValue[0], equalTo(1));
		assertThat(optionValue[1], equalTo(2));	
	}
	
}

