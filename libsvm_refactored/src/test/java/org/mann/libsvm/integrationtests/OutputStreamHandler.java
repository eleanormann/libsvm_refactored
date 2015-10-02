package org.mann.libsvm.integrationtests;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import static org.junit.Assert.assertNotNull;

public class OutputStreamHandler {
	private PrintStream oldPrintStream;
	private ByteArrayOutputStream outContent;
	private boolean useHandler;
	
	public OutputStreamHandler(boolean useHandler){
		this.useHandler = useHandler;
		if(useHandler){
			oldPrintStream = System.out;
			outContent = new ByteArrayOutputStream();
		}
	}

	public ByteArrayOutputStream getOutContent(){
		return outContent;
	}
	
	public void setUpOutputStream(){
		if(useHandler){
			System.setOut(new PrintStream(outContent));				
		}
	}

	public void resetOutput(){
		if(useHandler){
			System.setOut(oldPrintStream);
		}
	}
	
	public boolean useHandler() {
		return useHandler;
	}
}
