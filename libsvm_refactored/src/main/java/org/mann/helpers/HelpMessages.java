package org.mann.helpers;

public class HelpMessages {
	public static final String TRAIN_HELP_MESSAGE_ON_BAD_INPUT = "Usage: svm_train [options] training_set_file [model_file]\n"
		+"options:\n"
		
		
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
	
	public static final String SVM_TYPE = "-s svm_type : set type of SVM (default C-SVC)\n"
			+"	c_svc 		-- C-SVC		(multi-class classification)\n"
			+"	nu_svc 		-- nu-SVC		(multi-class classification)\n"
			+"	one_class	-- one-class SVM\n"
			+"	epsilon_svr -- epsilon-SVR	(regression)\n"
			+"	nu_svr	 	-- nu-SVR		(regression)\n";

	public static final String KERNEL = "-t kernel_type : set type of kernel function (default 2)\n"
			+"	linear 		-- linear: u'*v\n"
			+"	poly		-- polynomial: (gamma*u'*v + coef0)^degree\n"
			+"	rbf			-- radial basis function: exp(-gamma*|u-v|^2)\n"
			+"	sigmoid 	-- sigmoid: tanh(gamma*u'*v + coef0)\n"
			+"	precomputed	-- precomputed kernel (kernel values in training_set_file)\n";

	public static final String GAMMA = "-g gamma : set gamma in kernel function (default 1/num_features)";

	public static final String DEGREE = "-d degree : set degree in kernel function (default 3)";

	public static final String COEFF0 = "-r coef0 : set coef0 in kernel function (default 0)";

	public static final String COST = "-c cost : set the parameter C of C-SVC, epsilon-SVR, and nu-SVR (default 1)";

	public static final String NU = "-n nu : set the parameter nu of nu-SVC, one-class SVM, and nu-SVR (default 0.5)";

	public static final String EPSILON_LOSS_FUNCT = "-p epsilon : set the epsilon in loss function of epsilon-SVR (default 0.1)";

	public static final String CACHE_SIZE = "-m cachesize : set cache memory size in MB (default 100)";

	public static final String EPSILON_TOLERANCE = "-e epsilon : set tolerance of termination criterion (default 0.001)";

	public static final String SHRINKING = "-h shrinking : whether to use the shrinking heuristics, 0 or 1 (default 1)";

	public static final String PROBABILITY_ESTIMATES = "-b probability_estimates : whether to train a SVC or SVR model for probability estimates, 0 or 1 (default 0)";

	public static final String WEIGHT = "-w i weight : set the parameter C of class i to weight*C, for C-SVC (default 1)";

	public static final String N_FOLD_CROSS_VALIDATION = "-v n : n-fold cross validation mode";

	public static final String QUIET_MODE = "-q : quiet mode (no outputs)";
	
	public static void exitWithHelp(boolean isTrain) {
		System.err.print(isTrain ? TRAIN_HELP_MESSAGE_ON_BAD_INPUT : PREDICT_HELP_MESSAGE_ON_BAD_INPUT);
		System.exit(1);
	}
}
