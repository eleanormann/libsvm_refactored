package org.mann.libsvm;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertThat;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.ParseException;
import org.junit.Test;
import org.mann.libsvm.SvmParameter.KernelType;
import org.mann.libsvm.SvmParameter.SvmType;
import org.mann.validation.commandline.CommandLineWrapper;
import org.mann.validation.commandline.SvmTrainCommandLineParser;

public class SvmParameterTest {
	
	@Test
	public void checkDefaultValuesSet(){
		
		SvmParameter param = new SvmParameter();
		param.setSvmType(SvmType.c_svc);
		param.setKernelType(KernelType.rbf);
		param.setDegree(3);
		param.setGamma(0);
		param.setCoef0(0);
		param.setNu(0.5);
		param.setCacheSize(100);
		param.setCostC(1);
		param.setEpsilonTolerance(1e-3);
		param.setEpsilonLossFunction(0.1);
		param.setShrinking(1);
		param.setProbability(0);
		param.setNrWeight(0);
		param.setWeightLabel(new int[0]);
		param.setWeight(new double[0]);
		
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
		SvmTrainCommandLineParser optionsParser = new SvmTrainCommandLineParser();
		CommandLineWrapper cmdWrapper = optionsParser.parseCommandLine(options);
		actualParam.initializeFields(cmdWrapper, optionsParser);
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
