package org.mann.libsvm;

import static org.junit.Assert.*;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.mock;

import org.junit.Test;
import org.mann.libsvm.kernel.QMatrix;
import org.mann.libsvm.kernel.SVC_Q;

public class SolverTest {

  
  @Test
  public void swapIndexShouldSwapCorrectFieldsIndices() {
    QMatrix svcQ = mock(SVC_Q.class);
    Solver solver = new Solver();
    setSolverFields(svcQ, solver);
    solver.swapIndex(0, 1);
    checkAllArraysHaveSwapped(solver);
  }

 
  private void checkAllArraysHaveSwapped(Solver solver) {
    assertThat(solver.y[0], equalTo((byte)1));
    assertThat(solver.y[1], equalTo((byte)0));
    assertThat(solver.y[2], equalTo((byte)0));
    assertThat(solver.y[3], equalTo((byte)1));
    assertThat(solver.alphaStatus[0], equalTo((byte)1));
    assertThat(solver.alphaStatus[1], equalTo((byte)0));
    assertThat(solver.alphaStatus[2], equalTo((byte)0));
    assertThat(solver.alphaStatus[3], equalTo((byte)1));
    assertThat(solver.g[0], equalTo(1.0));
    assertThat(solver.g[1], equalTo(0.5));
    assertThat(solver.g[2], equalTo(1.5));
    assertThat(solver.g[3], equalTo(2.0));
    assertThat(solver.alpha[0], equalTo(1.0));
    assertThat(solver.alpha[1], equalTo(0.5));
    assertThat(solver.alpha[2], equalTo(1.5));
    assertThat(solver.alpha[3], equalTo(2.0));
    assertThat(solver.p[0], equalTo(1.0));
    assertThat(solver.p[1], equalTo(0.5));
    assertThat(solver.p[2], equalTo(1.5));
    assertThat(solver.p[3], equalTo(2.0));
    assertThat(solver.gBar[0], equalTo(1.0));
    assertThat(solver.gBar[1], equalTo(0.5));
    assertThat(solver.gBar[2], equalTo(1.5));
    assertThat(solver.gBar[3], equalTo(2.0));
    assertThat(solver.activeSet[0], equalTo(2));
    assertThat(solver.activeSet[1], equalTo(1));
    assertThat(solver.activeSet[2], equalTo(3));
    assertThat(solver.activeSet[3], equalTo(4));
  }

  private void setSolverFields(QMatrix svcQ, Solver solver) {
    solver.Q = svcQ;
    doNothing().when(svcQ).swap_index(0, 1);
    solver.y = new byte[]{0, 1, 0, 1};
    solver.g = new double[]{0.5, 1.0, 1.5, 2.0};
    solver.alphaStatus = new byte[]{0, 1, 0, 1};
    solver.alpha  = new double[]{0.5, 1.0, 1.5, 2.0};
    solver.p = new double[] {0.5, 1.0, 1.5, 2.0};
    solver.activeSet = new int[] {1, 2, 3, 4};
    solver.gBar = new double[] {0.5, 1.0, 1.5, 2.0};
  }
}
