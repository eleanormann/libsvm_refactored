package org.mann.validation.svmproblem;

import org.mann.libsvm.SvmProblem;

public class PrecomputedKernelChecker {

  public String checkPrecomputedKernelProperlySet(SvmProblem problem, int maxIndex) {
    for (int i = 0; i < problem.length; i++) {
      if (problem.x[i][0].index != 0) {
        return "ERROR: Wrong kernel matrix: first column must be 0:sample_serial_number";
      }
      if ((int) problem.x[i][0].value <= 0 || (int) problem.x[i][0].value > maxIndex) {
        return "ERROR: Wrong kernel input format: sample_serial_number out of range";
      }
    }
    return "precomputed kernel correctly formated\n";
  }
}
