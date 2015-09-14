package org.mann.validation.commandline;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertThat;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.ParseException;
import org.apache.commons.cli.UnrecognizedOptionException;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

public class CommandLineParserTest {
	private SvmTrainOptionsValidator validator;
	
	@Rule 
	public ExpectedException thrown = ExpectedException.none();
	
	@Before
	public void createSvmTrain(){
		validator = new SvmTrainOptionsValidator();
	}
	
	@Test
	public void parseCommandLinethrowsExceptionWhenOptionNotRecognized() throws ParseException{
		thrown.expect(UnrecognizedOptionException.class);
		validator.parseCommandLine(new String[]{"-u", "0"});
	}
	
	@Test
	public void parseCommandLineSetsSvmType() throws ParseException {
		CommandLine output = validator.parseCommandLine(new String[]{"-s", "0"});
		assertThat(output.getOptionValue("s"), equalTo("0"));
	}
	
	@Test
	public void parseCommandLineSetsKernelType() throws ParseException {
		CommandLine output = validator.parseCommandLine(new String[]{"-t", "0"});
		assertThat(output.getOptionValue("t"), equalTo("0"));
	}

	@Test
	public void parseCommandLineSetsDegree() throws ParseException {
		CommandLine output = validator.parseCommandLine(new String[]{"-d", "1"});
		assertThat(output.getOptionValue("d"), equalTo("1"));
	}
	
	@Test
	public void parseCommandLineSetsGamma() throws ParseException {
		CommandLine output = validator.parseCommandLine(new String[]{"-g", "2"});
		assertThat(output.getOptionValue("g"), equalTo("2"));
	}
	
	@Test
	public void parseCommandLineSetsCoef0() throws ParseException {
		CommandLine output = validator.parseCommandLine(new String[]{"-r", "1.4"});
		assertThat(output.getOptionValue("r"), equalTo("1.4"));
	}
	
	@Test
	public void parseCommandLineSetsCost() throws ParseException {
		CommandLine output = validator.parseCommandLine(new String[]{"-c", "1.5"});
		assertThat(output.getOptionValue("c"), equalTo("1.5"));
	}
	
	@Test
	public void parseCommandLineSetsEpsilonInLossFunction() throws ParseException {
		CommandLine output = validator.parseCommandLine(new String[]{"-p", "2"});
		assertThat(output.getOptionValue("p"), equalTo("2"));
	}
	
	@Test
	public void parseCommandLineSetsCacheSize() throws ParseException {
		CommandLine output = validator.parseCommandLine(new String[]{"-m", "101"});
		assertThat(output.getOptionValue("m"), equalTo("101"));
	}
	
	@Test
	public void parseCommandLineSetsEpsilonTolerance() throws ParseException {
		CommandLine output = validator.parseCommandLine(new String[]{"-e", "0.003"});
		assertThat(output.getOptionValue("e"), equalTo("0.003"));
	}
	
	@Test
	public void parseCommandLineSetsShrinking() throws ParseException {
		CommandLine output = validator.parseCommandLine(new String[]{"-h", "0"});
		assertThat(output.getOptionValue("h"), equalTo("0"));
	}
	
	@Test
	public void parseCommandLineSetsProbabilityEstimates() throws ParseException {
		CommandLine output = validator.parseCommandLine(new String[]{"-b", "0.87"});
		assertThat(output.getOptionValue("b"), equalTo("0.87"));
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
		assertThat(output.getOptionValue("v"), equalTo("2"));
	}
	
	@Test
	public void parseCommandLineSetsQuietMode() throws ParseException {
		CommandLine output = validator.parseCommandLine(new String[]{"-q"});
		assertThat(output.hasOption("q"), equalTo(true));
	}
	
	@Test
	public void parseCommandLineParsesMultipleOptions() throws ParseException{
		String[] options = new String[]{"-s", "C-SVC", "-t", "rbf", "-m", "200", "-q", "-w", "1", "2"};
		CommandLine output = validator.parseCommandLine(options);
		assertThat(output.getOptionValue("s"), equalTo("C-SVC"));
		assertThat(output.getOptionValue("t"), equalTo("rbf"));
		assertThat(output.getOptionValue("m"), equalTo("200"));
		assertThat(output.hasOption("q"), equalTo(true));
		assertThat(output.getOptionValues("w")[0], equalTo("1"));
		assertThat(output.getOptionValues("w")[1], equalTo("2"));	
	}
	
}

