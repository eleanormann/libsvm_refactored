package org.mann.libsvm;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertThat;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.ParseException;
import org.junit.Test;
import org.mann.libsvm.SvmParameter;
import org.mann.libsvm.SvmParameter.KernelType;
import org.mann.libsvm.SvmParameter.SvmType;
import org.mann.validation.commandline.SvmTrainOptionsValidator;


public class SvmParameterTest {
	
	@Test
	public void checkDefaultValuesSet(){
		
		SvmParameter param = new SvmParameter()
		.svmType("c_svc")
		.kernelType("rbf")
		.degree("3")
		.gamma("0") // 1/num_features
		.coef0("0")
		.nu("0.5")
		.cacheSize("100")
		.costC("1")
		.epsilonTolerance("1e-3")
		.epsilonLossFunction("0.1")
		.shrinking("1")
		.probability("0")
		.nrWeight(0)
		.weightLabel(new int[0])
		.weight(new double[0]);
		
		SvmParameter actualParam = new SvmParameter();
		actualParam.setDefaultValues();
		assertThat(actualParam.getSvmType(), equalTo(SvmType.c_svc));
		assertThat(actualParam.getKernelType(), equalTo(KernelType.rbf));
		assertThat(actualParam.getDegree(), equalTo(3));
		assertThat(actualParam.getGamma(), equalTo(0.0));
		assertThat(actualParam.getCoef0(), equalTo(0.0));
		assertThat(actualParam.getNu(), equalTo(0.5));
		assertThat(actualParam.getCache_size(), equalTo(100.0));
		assertThat(actualParam.getCostC(), equalTo(1.0));
		assertThat(actualParam.getEpsilonTolerance(), equalTo(1e-3));
		assertThat(actualParam.getEpsilonLossFunction(), equalTo(0.1));
		assertThat(actualParam.getShrinking(), equalTo(1));
		assertThat(actualParam.getProbability(), equalTo(0));
		assertThat(actualParam.getNr_weight(), equalTo(0));
		assertThat(actualParam.getWeight_label(), equalTo(new int[]{}));
		assertThat(actualParam.getWeight(), equalTo(new double[]{}));
	}
	
	@Test
	public void setSvmParameterFieldsShouldSetFieldsCorrectly() throws ParseException{
		SvmParameter actualParam = new SvmParameter();
		String[] options = new String[]{"-s", "nu_svr", "-t", "linear", "-d", "2", "-g", "1", "-m", "200", };
		SvmTrainOptionsValidator optionsParser = new SvmTrainOptionsValidator();
		CommandLine cmd = optionsParser.parseCommandLine(options);
		actualParam.initializeFields(cmd);
		assertThat(actualParam.getSvmType(), equalTo(SvmType.nu_svr));
		assertThat(actualParam.getKernelType(), equalTo(KernelType.linear));
		assertThat(actualParam.getDegree(), equalTo(2));
		assertThat(actualParam.getGamma(), equalTo(1.0));
		assertThat(actualParam.getCoef0(), equalTo(0.0));
		assertThat(actualParam.getNu(), equalTo(0.5));
		assertThat(actualParam.getCache_size(), equalTo(200.0));
		assertThat(actualParam.getCostC(), equalTo(1.0));
		assertThat(actualParam.getEpsilonTolerance(), equalTo(1e-3));
		assertThat(actualParam.getEpsilonLossFunction(), equalTo(0.1));
		assertThat(actualParam.getShrinking(), equalTo(1));
		assertThat(actualParam.getProbability(), equalTo(0));
		assertThat(actualParam.getNr_weight(), equalTo(0));
		assertThat(actualParam.getWeight_label(), equalTo(new int[]{}));
		assertThat(actualParam.getWeight(), equalTo(new double[]{}));
	}
}
