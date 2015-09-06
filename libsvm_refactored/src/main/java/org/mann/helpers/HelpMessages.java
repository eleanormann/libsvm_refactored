package org.mann.helpers;

public class HelpMessages {
	public static final String TRAIN_HELP_MESSAGE_ON_BAD_INPUT = "Usage: svm_train [options] training_set_file [model_file]\n"
		+"options:\n"
		+"-s svm_type : set type of SVM (default 0)\n"
		+"	0 -- C-SVC		(multi-class classification)\n"
		+"	1 -- nu-SVC		(multi-class classification)\n"
		+"	2 -- one-class SVM\n"
		+"	3 -- epsilon-SVR	(regression)\n"
		+"	4 -- nu-SVR		(regression)\n"
		+"-t kernel_type : set type of kernel function (default 2)\n"
		+"	0 -- linear: u'*v\n"
		+"	1 -- polynomial: (gamma*u'*v + coef0)^degree\n"
		+"	2 -- radial basis function: exp(-gamma*|u-v|^2)\n"
		+"	3 -- sigmoid: tanh(gamma*u'*v + coef0)\n"
		+"	4 -- precomputed kernel (kernel values in training_set_file)\n"
		+"-d degree : set degree in kernel function (default 3)\n"
		+"-g gamma : set gamma in kernel function (default 1/num_features)\n"
		+"-r coef0 : set coef0 in kernel function (default 0)\n"
		+"-c cost : set the parameter C of C-SVC, epsilon-SVR, and nu-SVR (default 1)\n"
		+"-n nu : set the parameter nu of nu-SVC, one-class SVM, and nu-SVR (default 0.5)\n"
		+"-p epsilon : set the epsilon in loss function of epsilon-SVR (default 0.1)\n"
		+"-m cachesize : set cache memory size in MB (default 100)\n"
		+"-e epsilon : set tolerance of termination criterion (default 0.001)\n"
		+"-h shrinking : whether to use the shrinking heuristics, 0 or 1 (default 1)\n"
		+"-b probability_estimates : whether to train a SVC or SVR model for probability estimates, 0 or 1 (default 0)\n"
		+"-wi weight : set the parameter C of class i to weight*C, for C-SVC (default 1)\n"
		+"-v n : n-fold cross validation mode\n"
		+"-q : quiet mode (no outputs)";
	
	public static final String PREDICT_HELP_MESSAGE_ON_BAD_INPUT = "usage: svm_predict [options] test_file model_file output_file\n"
			+"options:\n"
			+"-b probability_estimates: whether to predict probability estimates, 0 or 1 (default 0); one-class SVM not supported yet\n"
			+"-q : quiet mode (no outputs)";
	
	public static final String CROSS_VALIDATION_MSE = "Cross Validation Mean squared error = %s";

	public static final String CROSS_VALIDATION_RSQ = "Cross Validation Squared correlation coefficient = %s";

	public static final String CROSS_VALIDATION_ACCURACY = "Cross Validation Accuracy = %s%";
	
	public static void exitWithHelp(boolean isTrain) {
		System.err.print(isTrain ? TRAIN_HELP_MESSAGE_ON_BAD_INPUT : PREDICT_HELP_MESSAGE_ON_BAD_INPUT);
		System.exit(1);
	}
}
