.SUFFIXES: .class .java
FILES = org.mann.libsvm/svm.class org.mann.libsvm/svm_model.class org.mann.libsvm/svm_node.class \
		org.mann.libsvm/svm_parameter.class org.mann.libsvm/svm_problem.class \
		org.mann.libsvm/svm_print_interface.class \
		svm_train.class svm_predict.class svm_toy.class svm_scale.class

#JAVAC = jikes
JAVAC_FLAGS = -target 1.5 -source 1.5
JAVAC = javac
# JAVAC_FLAGS =

all: $(FILES)
	jar cvf org.mann.libsvm.jar *.class org.mann.libsvm/*.class

.java.class:
	$(JAVAC) $(JAVAC_FLAGS) $<

org.mann.libsvm/svm.java: org.mann.libsvm/svm.m4
	m4 org.mann.libsvm/svm.m4 > org.mann.libsvm/svm.java

clean:
	rm -f org.mann.libsvm/*.class *.class *.jar org.mann.libsvm/*~ *~ org.mann.libsvm/svm.java

dist: clean all
	rm *.class org.mann.libsvm/*.class
