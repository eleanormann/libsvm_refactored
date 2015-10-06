package org.mann.validation.svmproblem;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertThat;

import org.junit.Test;
import org.mann.libsvm.SvmNode;
import org.mann.libsvm.SvmProblem;

public class PrecomputedKernelCheckerTest {

  @Test
  public void checkPrecomputedKernelCorrectlySetShouldAddValidationMessageWhenCorrectlySet() {
    //Arrange
    SvmProblem problem = createSvmProblem(new SvmNode(0, 1), new SvmNode(0, 1.4));
    PrecomputedKernelChecker kernelChecker = new PrecomputedKernelChecker();
    
    //Act
    String output = kernelChecker.checkPrecomputedKernelProperlySet(problem, 1);
    
    //Assert
    assertThat(output, equalTo("precomputed kernel correctly formated\n"));
  }

  @Test
  public void checkPrecomputedKernelCorrectlySetShouldAddValidationMessageWhenNotFoundInSvmProblem() {
    //Arrange
    SvmProblem problem =createSvmProblem(new SvmNode(1, 1),new SvmNode(0, 1.4));
    PrecomputedKernelChecker kernelChecker = new PrecomputedKernelChecker();

    //Act
    String output = kernelChecker.checkPrecomputedKernelProperlySet(problem, 1);
    
    //Assert
    assertThat(output, equalTo("ERROR: Wrong kernel matrix: first column must be 0:sample_serial_number"));
  }

  @Test
  public void checkPrecomputedKernelCorrectlySetShouldAddValidationMessageWhenKernelValueLessThanOne() {
    // Arrange
    SvmProblem problem = createSvmProblem(new SvmNode(0, 0.4), new SvmNode(1, 1.4));
    PrecomputedKernelChecker kernelChecker = new PrecomputedKernelChecker();

    // Act
    String output = kernelChecker.checkPrecomputedKernelProperlySet(problem, 2);

    // Assert
    assertThat(output, equalTo("ERROR: Wrong kernel input format: sample_serial_number out of range"));
  }

  @Test
  public void checkPrecomputedKernelCorrectlySetShouldAddValidationMessageWhenKernelGreaterThanMaxIndex() {
    //Arrange
    SvmProblem problem = createSvmProblem(new SvmNode(0, 1.4), new SvmNode(0, 10.4));
    PrecomputedKernelChecker kernelChecker = new PrecomputedKernelChecker();

    //Act
    String output =  kernelChecker.checkPrecomputedKernelProperlySet(problem, 2);
    
    //Assert
    assertThat(output, equalTo("ERROR: Wrong kernel input format: sample_serial_number out of range"));
  }

  private SvmProblem createSvmProblem(SvmNode firstNode, SvmNode secondNode) {
    SvmProblem problem = new SvmProblem();
    problem.length = 3;
    problem.x = new SvmNode[][] { {firstNode}, {secondNode}, {new SvmNode(0, 1.4)}};
    return problem;
  }

}
