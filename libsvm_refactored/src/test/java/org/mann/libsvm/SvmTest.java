package org.mann.libsvm;

import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintStream;

import mockit.Mock;
import mockit.MockUp;
import mockit.integration.junit4.JMockit;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mann.libsvm.SvmParameter.KernelType;
import org.mann.libsvm.SvmParameter.SvmType;
import org.mann.ui.ResultCollector;

@RunWith(JMockit.class)
public class SvmTest {

	private final ByteArrayOutputStream outContent = new ByteArrayOutputStream();
	private final ByteArrayOutputStream errContent = new ByteArrayOutputStream();

	@Before
	public void setUpStreams() {
	    System.setOut(new PrintStream(outContent));
	    System.setErr(new PrintStream(errContent));
	}

	@After
	public void cleanUpStreams() {
	    System.setOut(null);
	    System.setErr(null);
	}
	
	@Test
	public void svmGetSvmTypeC_SVCShouldReturnCorrectInt() {
		//Arrange
		SvmModel model = createModel(SvmType.c_svc, null);
		//Act
		SvmType svmType = svm.getSvmTypeFromModel(model);
		//Assert
		assertThat(svmType, equalTo(SvmType.c_svc));	
	}
	
	@Test
	public void svmGetSvmTypeOneClassShouldReturnCorrectInt() {
		//Arrange
		SvmModel model = createModel(SvmType.one_class, null);
		//Act
		SvmType svmType = svm.getSvmTypeFromModel(model);
		//Assert
		assertThat(svmType, equalTo(SvmType.one_class));	
	}

	@Test
	public void svmSaveModelShouldWriteSvmTypeUsingEnumReference() throws IOException{
		
		new MockUp<DataOutputStream>(){
			@Mock public void writeBytes(String s){
				System.out.println(s);
			}
		};
		svm.svm_save_model("dummy file name", createModel(SvmType.c_svc, KernelType.linear));
		assertThat(outContent.toString(), containsString("svm_type c_svc"));	
	}
	
	@Test
	public void svmSaveModelShouldWriteKernelTypeUsingEnumReference() throws IOException{
		
		new MockUp<DataOutputStream>(){
			@Mock public void writeBytes(String s){
				System.out.println(s);
			}
		};
		svm.svm_save_model("dummy file name", createModel(SvmType.c_svc, KernelType.linear));
		assertThat(outContent.toString(), containsString("kernel_type linear"));		
	}
	
	@Test
	public void svmSaveModelShouldWriteKernelTypeUsingEnumReferenceWhenTypeIsPoly() throws IOException{
		
		new MockUp<DataOutputStream>(){
			@Mock public void writeBytes(String s){
				System.out.println(s);
			}
		};
		
		svm.svm_save_model("dummy file name", createModel(SvmType.c_svc, KernelType.precomputed));
		assertThat(outContent.toString(), containsString("kernel_type precomputed"));
		assertThat(outContent.toString(), containsString("0:0"));
	}
	
	@Test
	public void readModelHeaderShouldReadSvmTypeUsingEnumReference() throws IOException{
		SvmModel model = new SvmModel();
		
		BufferedReader fp = mock(BufferedReader.class);
		when(fp.readLine()).thenReturn("svm_type nu_svc").thenReturn("");
		
		svm.read_model_header(fp, model);
		assertThat(model.getParam().getSvmType(), equalTo(SvmType.nu_svc));	
	}
	
	@Test
	public void readModelHeaderShouldThrowExceptionWhenSvmTypeNotRecognised() throws IOException{
		SvmModel model = new SvmModel();
		
		BufferedReader fp = mock(BufferedReader.class);
		when(fp.readLine()).thenReturn("svm_type badgers");
		
		boolean isSuccessfulRead = svm.read_model_header(fp, model);
		assertThat(errContent.toString(), equalTo("Unknown svm type.\n"));
		assertThat(isSuccessfulRead, equalTo(false));	
	}
	
	@Test
	public void readModelHeaderShouldThrowExceptionWhenSvmTypeNull() throws IOException{
		SvmModel model = new SvmModel();
		
		BufferedReader fp = mock(BufferedReader.class);
		when(fp.readLine()).thenReturn("svm_type ");
		
		boolean isSuccessfulRead = svm.read_model_header(fp, model);
		assertThat(model.getParam().getSvmType(), equalTo(null));
		assertThat(errContent.toString(), equalTo("Unknown svm type.\n"));
		assertThat(isSuccessfulRead, equalTo(false));	
	}
	
	@Test
	public void readModelHeaderShouldReadKernelTypeUsingEnumReference() throws IOException{
		SvmModel model = new SvmModel();
		
		BufferedReader fp = mock(BufferedReader.class);
		when(fp.readLine()).thenReturn("kernel_type poly").thenReturn("");
		
		svm.read_model_header(fp, model);
		assertThat(model.getParam().getKernelType(), equalTo(KernelType.poly));	
	}
	
	@Test
	public void readModelHeaderShouldThrowExceptionWhenKernelTypeNotRecognised() throws IOException{
		SvmModel model = new SvmModel();
		
		BufferedReader fp = mock(BufferedReader.class);
		when(fp.readLine()).thenReturn("kernel_type badgers");
		
		boolean isSuccessfulRead = svm.read_model_header(fp, model);
		assertThat(errContent.toString(), equalTo("Unknown kernel function.\n"));
		assertThat(isSuccessfulRead, equalTo(false));	
	}
	
	@Test
	public void readModelHeaderShouldThrowExceptionWhenKernelTypeNull() throws IOException{
		SvmModel model = new SvmModel();
		
		BufferedReader fp = mock(BufferedReader.class);
		when(fp.readLine()).thenReturn("kernel_type ");
		
		boolean isSuccessfulRead = svm.read_model_header(fp, model);
		assertThat(model.getParam().getKernelType(), equalTo(null));
		assertThat(errContent.toString(), equalTo("Unknown kernel function.\n"));
		assertThat(isSuccessfulRead, equalTo(false));	
	}
	
	private SvmModel createModel(SvmType svmType, KernelType kernelType) {
		SvmModel model = new SvmModel();
		SvmParameter param = new SvmParameter();
		param.setSvmType(svmType);
		param.setKernelType(kernelType);
		model.setParam(param);
		model.nr_class = 2;
		model.rho = new double[]{0,0};
		model.l = 1;
		model.sv_coef = new double[][]{{0},{0}};
		model.SV = new SvmNode[][]{{new SvmNode()},{new SvmNode()}};
		return model;
	}

	
	
}
