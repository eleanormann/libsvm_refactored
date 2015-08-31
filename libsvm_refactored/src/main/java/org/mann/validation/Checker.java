package org.mann.validation;

import org.mann.libsvm.SvmParameter;

public interface Checker {

	Checker checkParameter(SvmParameter params);

}
