package libsvm;

import static org.junit.Assert.*;
import libsvm.svm_parameter.SvmType;
import static org.hamcrest.CoreMatchers.equalTo;
import org.junit.Test;

public class SvmParameterTest {

	@Test
	public void svmTypeShouldReturnItsValue() {
		assertThat(SvmType.C_SVC.getValue(), equalTo(0));
		assertThat(SvmType.NU_SVC.getValue(), equalTo(1));
		assertThat(SvmType.ONE_CLASS.getValue(), equalTo(2));
		assertThat(SvmType.EPSILON_SVR.getValue(), equalTo(3));
		assertThat(SvmType.NU_SVR.getValue(), equalTo(4));
	}

}
