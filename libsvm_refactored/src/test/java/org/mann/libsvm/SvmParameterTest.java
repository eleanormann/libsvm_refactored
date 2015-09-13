package org.mann.libsvm;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertThat;

import org.junit.Test;
import org.mann.libsvm.SvmParameter;
import org.mann.libsvm.SvmParameter.KernelType;
import org.mann.libsvm.SvmParameter.SvmType;


public class SvmParameterTest {
	
	@Test
	public void checkDefaultValuesSet(){
		
		SvmParameter param = new SvmParameter();
		param.svmType = SvmType.c_svc;
		param.kernelType = KernelType.rbf;
		param.degree = 3;
		param.gamma = 0; // 1/num_features
		param.coef0 = 0;
		param.nu = 0.5;
		param.cache_size = 100;
		param.costC = 1;
		param.epsilonTolerance = 1e-3;
		param.epsilonLossFunction = 0.1;
		param.shrinking = 1;
		param.probability = 0;
		param.nr_weight = 0;
		param.weight_label = new int[0];
		param.weight = new double[0];
		
		SvmParameter actualParam = new SvmParameter();
		actualParam.setDefaultValues();
		assertThat(actualParam.svmType, equalTo(SvmType.c_svc));
		assertThat(actualParam.kernelType, equalTo(KernelType.rbf));
		assertThat(actualParam.degree, equalTo(3));
		assertThat(actualParam.gamma, equalTo(0.0));
		assertThat(actualParam.coef0, equalTo(0.0));
		assertThat(actualParam.nu, equalTo(0.5));
		assertThat(actualParam.cache_size, equalTo(100.0));
		assertThat(actualParam.costC, equalTo(1.0));
		assertThat(actualParam.epsilonTolerance, equalTo(1e-3));
		assertThat(actualParam.epsilonLossFunction, equalTo(0.1));
		assertThat(actualParam.shrinking, equalTo(1));
		assertThat(actualParam.probability, equalTo(0));
		assertThat(actualParam.nr_weight, equalTo(0));
		assertThat(actualParam.weight_label, equalTo(new int[]{}));
		assertThat(actualParam.weight, equalTo(new double[]{}));
	}
	
	@Test
	public void setSvmParameterFieldsShouldSetFieldsCorrectly(){
		
	}
}
