package org.mann.ui;

import org.junit.Test;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.*;

public class SvmTrainOutputTest {
  
  @Test
  public void addErrorWillReturnErrorString(){
    SvmTrainOutput output = new SvmTrainOutput();
    String error = output.addError("No file has been specified", new IllegalArgumentException());
    assertThat(error, equalTo("ERROR: java.lang.IllegalArgumentException; No file has been specified"));
  }
}
