package ui;

import helpers.HelpMessages;

public class SvmPrinterFactory {


	public enum PrintMode {
		QUIET, PREDICT_BAD_INPUT, STANDARD
	}
	
	public static SvmPrintInterface getPrinter(PrintMode printMode) {
		switch(printMode){
			case QUIET:
				return new SvmQuietPrinter();
			case PREDICT_BAD_INPUT:
				return new SvmPredictPrinter();
			case STANDARD:
				return new SvmStandardPrinter();
			default: throw new UnsupportedOperationException("Print mode not recognised");
		}
	}

	private static class SvmQuietPrinter implements SvmPrintInterface{
		public void print(String s) {/*do nothing*/}
	}

	private static class SvmPredictPrinter implements SvmPrintInterface {
		public void print(String s) {
			//TODO: handle no s
			//Expires 20th September 2015
			System.err.print(s);
			System.out.println(HelpMessages.PREDICT_HELP_MESSAGE_ON_BAD_INPUT);
		}	
	}
	
	private static class SvmStandardPrinter implements SvmPrintInterface {
		public void print(String s) {
			System.out.println(s);
		}
	}
}
