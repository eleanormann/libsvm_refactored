package org.mann.libsvm.performancetests;

import static org.junit.Assert.*;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

import org.apache.commons.io.FileUtils;
import org.junit.Ignore;
import org.junit.Test;
import org.mann.libsvm.svm_train;

@Ignore
public class SolverPerformanceTest {
  
  private byte[] y;
  private double[] g;
  private byte[] alphaStatus;
  private double[] alpha;
  private double[] p;
  private int[] activeSet;
  private double[] gBar;

//utterly useless as it only tests a single adjacent swap  
  //practically the same (~3.5 seconds for 20000000
  @Test
  public void testOriginalSwapIndexTime() throws IOException {  
    int length = 20000000;
    createTestData(length);
    for(int i =1; i<length; i++){
      originalSwapIndex(i, i-1);
    }
  }

  @Test
  public void testRefactoredSwapIndexTime() throws IOException {  
    int length = 20000000;
    createTestData(length);
    for(int i =1; i<length; i++){
      refactoredSwapIndex(i, i-1);
    }
  }
  
  private void createTestData(int length) {
    y = new byte[length];
    g = new double[length];
    alphaStatus = new byte[length];
    alpha = new double[length];
    p = new double[length];
    activeSet = new int[length];
    gBar = new double[length];
    for(int i = 0; i< length; i++){
      int integerValue = new Random().nextInt(1);
      byte binaryValue = (byte) integerValue;
      double doubleValue = new Random().nextDouble();
      y[i] = binaryValue;
      g[i] = doubleValue;
      alphaStatus[i] = binaryValue;
      alpha[i] = doubleValue;
      p[i] = doubleValue;
      activeSet[i] = integerValue;
      gBar[i] = doubleValue;
    }
  }

  public void originalSwapIndex(int i, int j) {
    dummyQSwapIndex(i, j);
    do {
      swapByteIndex(y, i, j);
    } while (false);
    do {
      double temp = g[i];
      g[i] = g[j];
      g[j] = temp;  
    } while (false);
    do {
      byte temp = alphaStatus[i];
      alphaStatus[i] = alphaStatus[j];
      alphaStatus[j] = temp;  
    } while (false);
    do {
      double temp = alpha[i];
      alpha[i] = alpha[j];
      alpha[j] = temp;   
    } while (false);
    do {
      double temp = p[i];
      p[i] = p[j];
      p[j] = temp;    
    } while (false);
    do {
      int temp = activeSet[i];
      activeSet[i] = activeSet[j];
      activeSet[j] = temp;   
    } while (false);
    do {
      double temp = gBar[i];
      gBar[i] = gBar[j];
      gBar[j] = temp;   
    } while (false);
}

  public void refactoredSwapIndex(int i, int j) {
    dummyQSwapIndex(i, j);
    do {
      swapByteIndex(y, i, j);
      swapDoubleIndex(g, i, j);
      swapByteIndex(alphaStatus, i, j);
      swapDoubleIndex(alpha, i, j);
      swapDoubleIndex(p, i, j);
      swapIntegerIndex(activeSet, i, j);
      swapDoubleIndex(gBar, i, j);
    } while (false);
  }
  
  private void swapIntegerIndex(int[] integerArray, int i, int j) {
    int temp = integerArray[i];
    integerArray[i] = integerArray[j];
    integerArray[j] = temp;
  }

  private void swapDoubleIndex(double[] doubleArray, int i, int j) {
    double temp = doubleArray[i];
    doubleArray[i] = doubleArray[j];
    doubleArray[j] = temp;
  }

  private void swapByteIndex(byte[] byteArray, int i, int j) {
    byte temp = byteArray[i];
    byteArray[i] = byteArray[j];
    byteArray[j] = temp;
  }

  private void dummyQSwapIndex(int i, int j) {
    //do nothing
  }
}
