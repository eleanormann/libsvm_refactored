package org.mann.validation.commandline;

import static org.junit.Assert.*;
import static org.hamcrest.CoreMatchers.instanceOf;

import org.apache.commons.cli.Option;
import org.apache.commons.cli.ParseException;
import org.junit.Test;

public class CommandLineWrapperTest {

	@Test
	public void svmCommandLineShouldReturnOptionForShortName() throws ParseException {
		String[] options = new String[]{"-s", "c_svc", "-t", "rbf", "-m", "200", "-q", "-w", "1", "2"};
		SvmTrainCommandLineParser parser = new SvmTrainCommandLineParser();
		CommandLineWrapper cmd = parser.parseCommandLine(options);
		assertThat(cmd.getOption("s"), instanceOf(Option.class));
	}

}
