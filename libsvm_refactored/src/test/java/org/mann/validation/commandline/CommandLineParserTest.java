package org.mann.validation.commandline;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.fail;

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
		CommandLine output = validator.parseCommandLine(new String[]{"-s", "c_svc"});
		Option option = output.getOptions()[0];
		assertThat((SvmType)validator.getOptionValue(option), equalTo(SvmType.c_svc));
	}
	
	@Test
	public void parseCommandLineSetsKernelType() throws ParseException {
		CommandLine output = validator.parseCommandLine(new String[]{"-t", "linear"});
		Option option = output.getOptions()[0];
		assertThat((KernelType)validator.getOptionValue(option), equalTo(KernelType.linear));
	}

	@Test
	public void parseCommandLineSetsDegree() throws ParseException {
		CommandLine output = validator.parseCommandLine(new String[]{"-d", "1"});
		Option option = output.getOptions()[0];
		assertThat((Integer) validator.getOptionValue(option), equalTo(1));
	}
	
	@Test
	public void parseCommandLineSetsGamma() throws ParseException {
		CommandLine output = validator.parseCommandLine(new String[]{"-g", "2"});
		Option option = output.getOptions()[0];
		assertThat((Double) validator.getOptionValue(option), equalTo(2.0));
	}
	
	@Test
	public void parseCommandLineSetsCoef0() throws ParseException {
		CommandLine output = validator.parseCommandLine(new String[]{"-r", "1.4"});
		Option option = output.getOptions()[0];
		assertThat((Double) validator.getOptionValue(option), equalTo(1.4));
	}
	
	@Test
	public void parseCommandLineSetsCost() throws ParseException {
		CommandLine output = validator.parseCommandLine(new String[]{"-c", "1.5"});
		Option option = output.getOptions()[0];
		assertThat((Double) validator.getOptionValue(option), equalTo(1.5));
	}
	
	@Test
	public void parseCommandLineSetsEpsilonInLossFunction() throws ParseException {
		CommandLine output = validator.parseCommandLine(new String[]{"-p", "2"});
		Option option = output.getOptions()[0];
		assertThat((Double) validator.getOptionValue(option), equalTo(2.0));
	}
	
	@Test
	public void parseCommandLineSetsCacheSize() throws ParseException {
		CommandLine output = validator.parseCommandLine(new String[]{"-m", "101.0"});
		Option option = output.getOptions()[0];
		assertThat((Double) validator.getOptionValue(option), equalTo(101.0));
	}
	
	@Test
	public void parseCommandLineSetsEpsilonTolerance() throws ParseException {
		CommandLine output = validator.parseCommandLine(new String[]{"-e", "0.003"});
		Option option = output.getOptions()[0];
		assertThat((Double) validator.getOptionValue(option), equalTo(0.003));
	}
	
	@Test
	public void parseCommandLineSetsShrinking() throws ParseException {
		CommandLine output = validator.parseCommandLine(new String[]{"-h", "0"});
		Option option = output.getOptions()[0];
		assertThat((Integer) validator.getOptionValue(option), equalTo(0));
	}
	
	@Test
	public void parseCommandLineSetsProbabilityEstimates() throws ParseException {
		CommandLine output = validator.parseCommandLine(new String[]{"-b", "1"});
		Option option = output.getOptions()[0];
		assertThat((Integer) validator.getOptionValue(option), equalTo(1));
	}
	
	@Test
	public void parseCommandLineSetsWeight() throws ParseException {
		CommandLine output = validator.parseCommandLine(new String[]{"-w","1", "0.9"});
		assertThat(output.getOptionValues("w")[0], equalTo("1"));
		assertThat(output.getOptionValues("w")[1], equalTo("0.9"));
	}
	
	@Test
	public void parseCommandLineSetsNOfCrossValidationMode() throws ParseException {
		CommandLine output = validator.parseCommandLine(new String[]{"-v", "2"});
		Option option = output.getOptions()[0];
		assertThat((Integer) validator.getOptionValue(option), equalTo(2));
	}
	
	@Test
	public void parseCommandLineSetsQuietMode() throws ParseException {
		CommandLine output = validator.parseCommandLine(new String[]{"-q"});
		assertThat(output.hasOption("q"), equalTo(true));
	}

	@Test
	public void parseCommandLineParsesMultipleOptions() throws ParseException{
		String[] options = new String[]{"-s", "c_svc", "-t", "rbf", "-m", "200", "-q", "-w", "1", "2"};
		CommandLine output = validator.parseCommandLine(options);
		fail("change to test of type too");
		assertThat(output.getOptionValue("s"), equalTo("c_svc"));
		assertThat(output.getOptionValue("t"), equalTo("rbf"));
		assertThat(output.getOptionValue("m"), equalTo("200"));
		assertThat(output.hasOption("q"), equalTo(true));
		assertThat(output.getOptionValues("w")[0], equalTo("1"));
		assertThat(output.getOptionValues("w")[1], equalTo("2"));	
	}
	
	@Test
	public void getValueShouldReturnIntegerWhenTypeIsInteger() throws ParseException{
		CommandLine output = validator.parseCommandLine(new String[]{"-v", "2"});
		Option option = output.getOptions()[0];
		assertThat((Integer)validator.getOptionValue(option), equalTo(2));
	}
	
}

