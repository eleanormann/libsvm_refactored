package org.mann.libsvm.integrationtests;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

public class OutputStreamHandler {
	private ByteArrayOutputStream outContent;
	private boolean useHandler;
	
	public OutputStreamHandler(boolean useHandler){
		this.useHandler = useHandler;
		if(useHandler){
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

	public boolean useHandler() {
		return useHandler;
	}
}
