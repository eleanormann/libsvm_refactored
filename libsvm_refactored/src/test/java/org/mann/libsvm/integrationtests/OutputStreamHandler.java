package org.mann.libsvm.integrationtests;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

public class OutputStreamHandler {
  private PrintStream oldPrintStream;
  private PrintStream oldErrorStream;
  private ByteArrayOutputStream outContent;
  private ByteArrayOutputStream errorContent;

  private boolean useHandler;

  public OutputStreamHandler(boolean useHandler) {
    this.useHandler = useHandler;
    if (useHandler) {
      oldPrintStream = System.out;
      oldErrorStream = System.err;
    }
  }

  public ByteArrayOutputStream getOutContent() {
    return outContent;
  }

  public ByteArrayOutputStream getErrContent() {
    return errorContent;
  }

  public void setUpOutputStream() {
    if (useHandler) {
      outContent = new ByteArrayOutputStream();
      System.setOut(new PrintStream(outContent));
    }
  }

  public void setUpErrorStream() {
    if (useHandler) {
      errorContent = new ByteArrayOutputStream();
      System.setErr(new PrintStream(errorContent));
    }
  }

  public void resetOutput() {
    if (useHandler) {
      System.setOut(oldPrintStream);
    }
  }

  public void resetErrorOutput() {
    if (useHandler) {
      System.setOut(oldErrorStream);
    }
  }

  public boolean useHandler() {
    return useHandler;
  }

}
