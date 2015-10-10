package org.mann.libsvm;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.util.Random;
import java.util.StringTokenizer;
import java.util.logging.Logger;

import org.mann.libsvm.SvmParameter.KernelType;
import org.mann.libsvm.SvmParameter.SvmType;
import org.mann.libsvm.kernel.Kernel;
import org.mann.libsvm.kernel.ONE_CLASS_Q;
import org.mann.libsvm.kernel.QMatrix;
import org.mann.libsvm.kernel.SVC_Q;
import org.mann.libsvm.kernel.SVR_Q;
import org.mann.ui.ResultCollector;
import org.mann.ui.SvmPrintInterface;


// An SMO algorithm in Fan et al., JMLR 6(2005), p. 1889--1918
// Solves:
//
// min 0.5(\alpha^T Q \alpha) + p^T \alpha
//
// y^T \alpha = \delta
// y_i = +1 or -1
// 0 <= alpha_i <= Cp for y_i = 1
// 0 <= alpha_i <= Cn for y_i = -1
//
// Given:
//
// Q, p, y, Cp, Cn, and an initial feasible point \alpha
// l is the size of vectors and matrices
// eps is the stopping tolerance
//
// solution will be put in \alpha, objective value will be put in obj
//
class Solver {
	int activeSize;
	byte[] y;
	double[] g; // gradient of objective function
	static final byte LOWER_BOUND = 0;
	static final byte UPPER_BOUND = 1;
	static final byte FREE = 2;
	byte[] alphaStatus; // LOWER_BOUND, UPPER_BOUND, FREE
	double[] alpha;
	QMatrix Q;
	double[] QD;
	double eps;
	double Cp, Cn;
	double[] p;
	int[] activeSet;
	double[] gBar; // gradient, if we treat free variables as 0
	int l;
	boolean unshrink; // XXX
	static final double INF = Double.POSITIVE_INFINITY;
	
	double get_C(int i) {
		return (y[i] > 0) ? Cp : Cn;
	}

	private void updateAlphaStatus(int i) {
		if (alpha[i] >= get_C(i))
			alphaStatus[i] = UPPER_BOUND;
		else if (alpha[i] <= 0)
			alphaStatus[i] = LOWER_BOUND;
		else
			alphaStatus[i] = FREE;
	}

	boolean isUpperbound(int i) {
		return alphaStatus[i] == UPPER_BOUND;
	}

	boolean isLowerbound(int i) {
		return alphaStatus[i] == LOWER_BOUND;
	}

	boolean is_free(int i) {
		return alphaStatus[i] == FREE;
	}

	// java: information about solution except alpha,
	// because we cannot return multiple values otherwise...
	static class SolutionInfo {
		double obj;
		double rho;
		double upper_bound_p;
		double upper_bound_n;
		double r; // for Solver_NU
	}

	//TODO: Look into these do whiles
	//Expires Nov 3rd 2015
	public void swapIndex(int i, int j) {
		Q.swap_index(i, j);
		do {
		 swapByteIndex(y, i, j);
		} while (false);
		do {
		  swapDoubleIndex(g, i, j);
		} while (false);
		do {
		  swapByteIndex(alphaStatus, i, j);
		} while (false);
		do {
		  swapDoubleIndex(alpha, i, j);
		} while (false);
		do {
		  swapDoubleIndex(p, i, j);
		} while (false);
		do {
		  swapIntegerIndex(activeSet, i, j);		 
		} while (false);
		do {
		  swapDoubleIndex(gBar, i, j);
		} while (false);
	}

  private void swapIntegerIndex(int[] integerArray, int i, int j) {
    int temp = integerArray[i];
    integerArray[i] = integerArray[j];
    integerArray[j] = temp;
  }

  private void swapDoubleIndex(double[] doubleArray, int i, int j) {
    double temp = doubleArray[i];
    doubleArray[i] = doubleArray[j];
    doubleArray[j] = temp;
  }

  private void swapByteIndex(byte[] byteArray, int i, int j) {
    byte temp = byteArray[i];
    byteArray[i] = byteArray[j];
    byteArray[j] = temp;
  }

	void reconstruct_gradient() {
		// reconstruct inactive elements of g from G_bar and free variables

		if (activeSize == l){
		  System.out.println("active size equals length: returning from svm.reconstructGradient");
		  return;		  
		}

		int i, j;
		int nr_free = 0;

		for (j = activeSize; j < l; j++)
			g[j] = gBar[j] + p[j];

		for (j = 0; j < activeSize; j++)
			if (is_free(j))
				nr_free++;

		if (2 * nr_free < activeSize)
			System.err.print("\nWARNING: using -h 0 may be faster\n");

		if (nr_free * l > 2 * activeSize * (l - activeSize)) {
			for (i = activeSize; i < l; i++) {
				float[] Q_i = Q.get_Q(i, activeSize);
				for (j = 0; j < activeSize; j++)
					if (is_free(j))
						g[i] += alpha[j] * Q_i[j];
			}
		} else {
			for (i = 0; i < activeSize; i++)
				if (is_free(i)) {
					float[] Q_i = Q.get_Q(i, l);
					double alpha_i = alpha[i];
					for (j = activeSize; j < l; j++)
						g[j] += alpha_i * Q_i[j];
				}
		}
	}

	void Solve(int l, QMatrix Q, double[] p_, byte[] y_, double[] alpha_,
			double Cp, double Cn, double eps, SolutionInfo si, int shrinking) {
		this.l = l;
		this.Q = Q;
		QD = Q.get_QD();
		p = (double[]) p_.clone();
		y = (byte[]) y_.clone();
		alpha = (double[]) alpha_.clone();
		this.Cp = Cp;
		this.Cn = Cn;
		this.eps = eps;
		this.unshrink = false;

		// initialize alphaStatus
		initializeAlphaStatus(l);
		
		// initialize active set (for shrinking)
		initializeActiveSet(l);
		
		// initialize gradient
		initializeGradient(l, Q);

		// optimization step

		int iter = 0;
		int max_iter = Math.max(10000000,
				l > Integer.MAX_VALUE / 100 ? Integer.MAX_VALUE : 100 * l);
		int counter = Math.min(l, 1000) + 1;
		int[] working_set = new int[2];

		while (iter < max_iter) {
			// show progress and do shrinking

			if (--counter == 0) {
				counter = Math.min(l, 1000);
				if (shrinking != 0){
					do_shrinking();					
				}
				svm.addResult(".", "progress");
			}

			if (select_working_set(working_set) != 0) {
				// reconstruct the whole gradient
				reconstruct_gradient();
				// reset active set size and check
				activeSize = l;
				svm.addResult("*", "progress");
				if (select_working_set(working_set) != 0)
					break;
				else
					counter = 1; // do shrinking next iteration
			}

			int i = working_set[0];
			int j = working_set[1];

			++iter;

			// update alpha[i] and alpha[j], handle bounds carefully

			float[] Q_i = Q.get_Q(i, activeSize);
			float[] Q_j = Q.get_Q(j, activeSize);

			double C_i = get_C(i);
			double C_j = get_C(j);

			double old_alpha_i = alpha[i];
			double old_alpha_j = alpha[j];

			if (y[i] != y[j]) {
				double quad_coef = QD[i] + QD[j] + 2 * Q_i[j];
				if (quad_coef <= 0)
					quad_coef = 1e-12;
				double delta = (-g[i] - g[j]) / quad_coef;
				double diff = alpha[i] - alpha[j];
				alpha[i] += delta;
				alpha[j] += delta;

				if (diff > 0) {
					if (alpha[j] < 0) {
						alpha[j] = 0;
						alpha[i] = diff;
					}
				} else {
					if (alpha[i] < 0) {
						alpha[i] = 0;
						alpha[j] = -diff;
					}
				}
				if (diff > C_i - C_j) {
					if (alpha[i] > C_i) {
						alpha[i] = C_i;
						alpha[j] = C_i - diff;
					}
				} else {
					if (alpha[j] > C_j) {
						alpha[j] = C_j;
						alpha[i] = C_j + diff;
					}
				}
			} else {
				double quad_coef = QD[i] + QD[j] - 2 * Q_i[j];
				if (quad_coef <= 0)
					quad_coef = 1e-12;
				double delta = (g[i] - g[j]) / quad_coef;
				double sum = alpha[i] + alpha[j];
				alpha[i] -= delta;
				alpha[j] += delta;

				if (sum > C_i) {
					if (alpha[i] > C_i) {
						alpha[i] = C_i;
						alpha[j] = sum - C_i;
					}
				} else {
					if (alpha[j] < 0) {
						alpha[j] = 0;
						alpha[i] = sum;
					}
				}
				if (sum > C_j) {
					if (alpha[j] > C_j) {
						alpha[j] = C_j;
						alpha[i] = sum - C_j;
					}
				} else {
					if (alpha[i] < 0) {
						alpha[i] = 0;
						alpha[j] = sum;
					}
				}
			}

			// update g

			double delta_alpha_i = alpha[i] - old_alpha_i;
			double delta_alpha_j = alpha[j] - old_alpha_j;

			for (int k = 0; k < activeSize; k++) {
				g[k] += Q_i[k] * delta_alpha_i + Q_j[k] * delta_alpha_j;
			}

			// update alphaStatus and G_bar

			{
				boolean ui = isUpperbound(i);
				boolean uj = isUpperbound(j);
				updateAlphaStatus(i);
				updateAlphaStatus(j);
				int k;
				if (ui != isUpperbound(i)) {
					Q_i = Q.get_Q(i, l);
					if (ui)
						for (k = 0; k < l; k++)
						  gBar[k] -= C_i * Q_i[k];
					else
						for (k = 0; k < l; k++)
						  gBar[k] += C_i * Q_i[k];
				}

				if (uj != isUpperbound(j)) {
					Q_j = Q.get_Q(j, l);
					if (uj)
						for (k = 0; k < l; k++)
						  gBar[k] -= C_j * Q_j[k];
					else
						for (k = 0; k < l; k++)
						  gBar[k] += C_j * Q_j[k];
				}
			}

		}

		if (iter >= max_iter) {
			if (activeSize < l) {
				// reconstruct the whole gradient to calculate objective value
				reconstruct_gradient();
				activeSize = l;
				svm.addResult("*", "progress");
			}
			System.err.print("\nWARNING: reaching max number of iterations\n");
		}

		// calculate rho

		si.rho = calculate_rho();

		// calculate objective value
		calculateObjectiveValue(l, si);

		// put back the solution
		//TODO: remove anon block
		{
			for (int i = 0; i < l; i++){
			  alpha_[activeSet[i]] = alpha[i];			  
			}
		}

		si.upper_bound_p = Cp;
		si.upper_bound_n = Cn;
		
		svm.addResult("optimization finished, #iter = " + iter, "console output");
		svm.addResult(String.valueOf(iter), null);
		svm.addResult(iter, "iterations"); 
	}

  private void calculateObjectiveValue(int l, SolutionInfo si) {
    double v = 0;
    int i;
    for (i = 0; i < l; i++){
      v += alpha[i] * (g[i] + p[i]);			  
    }

    si.obj = v / 2;
  }

  private void initializeGradient(int l, QMatrix Q) {
    g = new double[l];
    gBar = new double[l];
    
    for (int i = 0; i < l; i++) {
    	g[i] = p[i];
    	gBar[i] = 0;
    }
    
    for (int i = 0; i < l; i++){
      if (!isLowerbound(i)) {
        float[] Q_i = Q.get_Q(i, l);
        double alpha_i = alpha[i];
        
        for (int j = 0; j < l; j++){
          g[j] += alpha_i * Q_i[j];          
        }
    
        if (isUpperbound(i)){
          for (int j = 0; j < l; j++){
            gBar[j] += get_C(i) * Q_i[j];                      
          }
        }
      }      
    }
  }



  private void initializeActiveSet(int length) {
      activeSet = new int[length];
        for (int i = 0; i < length; i++)
          activeSet[i] = i;
        activeSize = length;
  }

  private void initializeAlphaStatus(int length) {
    alphaStatus = new byte[length];
    for (int i = 0; i < length; i++){
      updateAlphaStatus(i);      
    }
  }

  // return 1 if already optimal, return 0 otherwise
	int select_working_set(int[] working_set) {
		// return i,j such that
		// i: maximizes -y_i * grad(f)_i, i in I_up(\alpha)
		// j: mimimizes the decrease of obj value
		// (if quadratic coefficeint <= 0, replace it with tau)
		// -y_j*grad(f)_j < -y_i*grad(f)_i, j in I_low(\alpha)
	
		double Gmax = Double.NEGATIVE_INFINITY;
		double Gmax2 = Double.NEGATIVE_INFINITY;
		int Gmax_idx = -1;
		int Gmin_idx = -1;
		double obj_diff_min = Double.POSITIVE_INFINITY;

		for (int t = 0; t < activeSize; t++)
			if (y[t] == +1) {
				if (!isUpperbound(t))
					if (-g[t] >= Gmax) {
						Gmax = -g[t];
						Gmax_idx = t;
					}
			} else {
				if (!isLowerbound(t))
					if (g[t] >= Gmax) {
						Gmax = g[t];
						Gmax_idx = t;
					}
			}

		int i = Gmax_idx;
		float[] Q_i = null;
		if (i != -1) // null Q_i not accessed: Gmax=-INF if i=-1
			Q_i = Q.get_Q(i, activeSize);

		for (int j = 0; j < activeSize; j++) {
			if (y[j] == +1) {
				if (!isLowerbound(j)) {
					double grad_diff = Gmax + g[j];
					if (g[j] >= Gmax2)
						Gmax2 = g[j];
					if (grad_diff > 0) {
						double obj_diff;
						double quad_coef = QD[i] + QD[j] - 2.0 * y[i] * Q_i[j];
						if (quad_coef > 0)
							obj_diff = -(grad_diff * grad_diff) / quad_coef;
						else
							obj_diff = -(grad_diff * grad_diff) / 1e-12;

						if (obj_diff <= obj_diff_min) {
							Gmin_idx = j;
							obj_diff_min = obj_diff;
						}
					}
				}
			} else {
				if (!isUpperbound(j)) {
					double grad_diff = Gmax - g[j];
					if (-g[j] >= Gmax2)
						Gmax2 = -g[j];
					if (grad_diff > 0) {
						double obj_diff;
						double quad_coef = QD[i] + QD[j] + 2.0 * y[i] * Q_i[j];
						if (quad_coef > 0)
							obj_diff = -(grad_diff * grad_diff) / quad_coef;
						else
							obj_diff = -(grad_diff * grad_diff) / 1e-12;

						if (obj_diff <= obj_diff_min) {
							Gmin_idx = j;
							obj_diff_min = obj_diff;
						}
					}
				}
			}
		}

		if (Gmax + Gmax2 < eps)
			return 1;

		working_set[0] = Gmax_idx;
		working_set[1] = Gmin_idx;
		return 0;
	}

	private boolean be_shrunk(int i, double Gmax1, double Gmax2) {
		if (isUpperbound(i)) {
			if (y[i] == +1)
				return (-g[i] > Gmax1);
			else
				return (-g[i] > Gmax2);
		} else if (isLowerbound(i)) {
			if (y[i] == +1)
				return (g[i] > Gmax2);
			else
				return (g[i] > Gmax1);
		} else
			return (false);
	}

	void do_shrinking() {
		int i;
		double Gmax1 = Double.NEGATIVE_INFINITY; // max { -y_i * grad(f)_i | i in I_up(\alpha) }
		double Gmax2 = Double.NEGATIVE_INFINITY; // max { y_i * grad(f)_i | i in I_low(\alpha) }

		// find maximal violating pair first
		for (i = 0; i < activeSize; i++) {
			if (y[i] == +1) {
				if (!isUpperbound(i)) {
					if (-g[i] >= Gmax1)
						Gmax1 = -g[i];
				}
				if (!isLowerbound(i)) {
					if (g[i] >= Gmax2)
						Gmax2 = g[i];
				}
			} else {
				if (!isUpperbound(i)) {
					if (-g[i] >= Gmax2)
						Gmax2 = -g[i];
				}
				if (!isLowerbound(i)) {
					if (g[i] >= Gmax1)
						Gmax1 = g[i];
				}
			}
		}

		if (unshrink == false && Gmax1 + Gmax2 <= eps * 10) {
			unshrink = true;
			reconstruct_gradient();
			activeSize = l;
		}

		for (i = 0; i < activeSize; i++)
			if (be_shrunk(i, Gmax1, Gmax2)) {
			  activeSize--;
				while (activeSize > i) {
					if (!be_shrunk(activeSize, Gmax1, Gmax2)) {
						swapIndex(i, activeSize);
						break;
					}
					activeSize--;
				}
			}
	}

	double calculate_rho() {
		double r;
		int nr_free = 0;
		double upperBound = Double.POSITIVE_INFINITY;
		double lowerBound = Double.NEGATIVE_INFINITY;
		double sum_free = 0;
		for (int i = 0; i < activeSize; i++) {
			double yG = y[i] * g[i];

			if (isLowerbound(i)) {
				if (y[i] > 0)
					upperBound = Math.min(upperBound, yG);
				else
					lowerBound = Math.max(lowerBound, yG);
			} else if (isUpperbound(i)) {
				if (y[i] < 0)
					upperBound = Math.min(upperBound, yG);
				else
					lowerBound = Math.max(lowerBound, yG);
			} else {
				++nr_free;
				sum_free += yG;
			}
		}

		if (nr_free > 0)
			r = sum_free / nr_free;
		else
			r = (upperBound + lowerBound) / 2;

		return r;
	}

}

//
// Solver for nu-svm classification and regression
//
// additional constraint: e^T \alpha = constant
//
final class Solver_NU extends Solver {
	
	private SolutionInfo solutionInfo;
	
	@Override
	void Solve(int l, QMatrix Q, double[] p, byte[] y, double[] alpha,
			double Cp, double Cn, double eps, SolutionInfo si, int shrinking) {
		this.solutionInfo = si;
		super.Solve(l, Q, p, y, alpha, Cp, Cn, eps, si, shrinking);
	}

	// return 1 if already optimal, return 0 otherwise
	int select_working_set(int[] working_set) {
		// return i,j such that y_i = y_j and
		// i: maximizes -y_i * grad(f)_i, i in I_up(\alpha)
		// j: minimizes the decrease of obj value
		// (if quadratic coefficeint <= 0, replace it with tau)
		// -y_j*grad(f)_j < -y_i*grad(f)_i, j in I_low(\alpha)

		double Gmaxp = Double.NEGATIVE_INFINITY;
		double Gmaxp2 = Double.NEGATIVE_INFINITY;
		int Gmaxp_idx = -1;

		double Gmaxn = Double.NEGATIVE_INFINITY;
		double Gmaxn2 = Double.NEGATIVE_INFINITY;
		int Gmaxn_idx = -1;

		int Gmin_idx = -1;
		double obj_diff_min = Double.POSITIVE_INFINITY;

		for (int t = 0; t < activeSize; t++)
			if (y[t] == +1) {
				if (!isUpperbound(t))
					if (-g[t] >= Gmaxp) {
						Gmaxp = -g[t];
						Gmaxp_idx = t;
					}
			} else {
				if (!isLowerbound(t))
					if (g[t] >= Gmaxn) {
						Gmaxn = g[t];
						Gmaxn_idx = t;
					}
			}

		int ip = Gmaxp_idx;
		int in = Gmaxn_idx;
		float[] Q_ip = null;
		float[] Q_in = null;
		if (ip != -1) // null Q_ip not accessed: Gmaxp=-INF if ip=-1
			Q_ip = Q.get_Q(ip, activeSize);
		if (in != -1)
			Q_in = Q.get_Q(in, activeSize);

		for (int j = 0; j < activeSize; j++) {
			if (y[j] == +1) {
				if (!isLowerbound(j)) {
					double grad_diff = Gmaxp + g[j];
					if (g[j] >= Gmaxp2)
						Gmaxp2 = g[j];
					if (grad_diff > 0) {
						double obj_diff;
						double quad_coef = QD[ip] + QD[j] - 2 * Q_ip[j];
						if (quad_coef > 0)
							obj_diff = -(grad_diff * grad_diff) / quad_coef;
						else
							obj_diff = -(grad_diff * grad_diff) / 1e-12;

						if (obj_diff <= obj_diff_min) {
							Gmin_idx = j;
							obj_diff_min = obj_diff;
						}
					}
				}
			} else {
				if (!isUpperbound(j)) {
					double grad_diff = Gmaxn - g[j];
					if (-g[j] >= Gmaxn2)
						Gmaxn2 = -g[j];
					if (grad_diff > 0) {
						double obj_diff;
						double quad_coef = QD[in] + QD[j] - 2 * Q_in[j];
						if (quad_coef > 0)
							obj_diff = -(grad_diff * grad_diff) / quad_coef;
						else
							obj_diff = -(grad_diff * grad_diff) / 1e-12;

						if (obj_diff <= obj_diff_min) {
							Gmin_idx = j;
							obj_diff_min = obj_diff;
						}
					}
				}
			}
		}

		if (Math.max(Gmaxp + Gmaxp2, Gmaxn + Gmaxn2) < eps)
			return 1;

		if (y[Gmin_idx] == +1)
			working_set[0] = Gmaxp_idx;
		else
			working_set[0] = Gmaxn_idx;
		working_set[1] = Gmin_idx;

		return 0;
	}

	private boolean be_shrunk(int i, double Gmax1, double Gmax2, double Gmax3,
			double Gmax4) {
		if (isUpperbound(i)) {
			if (y[i] == +1)
				return (-g[i] > Gmax1);
			else
				return (-g[i] > Gmax4);
		} else if (isLowerbound(i)) {
			if (y[i] == +1)
				return (g[i] > Gmax2);
			else
				return (g[i] > Gmax3);
		} else
			return (false);
	}

	void do_shrinking() {
		double Gmax1 = Double.NEGATIVE_INFINITY; // max { -y_i * grad(f)_i | y_i = +1, i in
								// I_up(\alpha) }
		double Gmax2 = Double.NEGATIVE_INFINITY; // max { y_i * grad(f)_i | y_i = +1, i in
								// I_low(\alpha) }
		double Gmax3 = Double.NEGATIVE_INFINITY; // max { -y_i * grad(f)_i | y_i = -1, i in
								// I_up(\alpha) }
		double Gmax4 = Double.NEGATIVE_INFINITY; // max { y_i * grad(f)_i | y_i = -1, i in
								// I_low(\alpha) }

		// find maximal violating pair first
		int i;
		for (i = 0; i < activeSize; i++) {
			if (!isUpperbound(i)) {
				if (y[i] == +1) {
					if (-g[i] > Gmax1)
						Gmax1 = -g[i];
				} else if (-g[i] > Gmax4)
					Gmax4 = -g[i];
			}
			if (!isLowerbound(i)) {
				if (y[i] == +1) {
					if (g[i] > Gmax2)
						Gmax2 = g[i];
				} else if (g[i] > Gmax3)
					Gmax3 = g[i];
			}
		}

		if (unshrink == false
				&& Math.max(Gmax1 + Gmax2, Gmax3 + Gmax4) <= eps * 10) {
			unshrink = true;
			reconstruct_gradient();
			activeSize = l;
		}

		for (i = 0; i < activeSize; i++)
			if (be_shrunk(i, Gmax1, Gmax2, Gmax3, Gmax4)) {
			  activeSize--;
				while (activeSize > i) {
					if (!be_shrunk(activeSize, Gmax1, Gmax2, Gmax3, Gmax4)) {
						swapIndex(i, activeSize);
						break;
					}
					activeSize--;
				}
			}
	}

	double calculateRho() {
		int hitNumFree = 0; 
		int missNumFree = 0;
		double hitUpperbound = Double.POSITIVE_INFINITY; 
		double missUpperbound = Double.POSITIVE_INFINITY;
		double hitLowerBound = Double.NEGATIVE_INFINITY;
		double missLowerbound = Double.NEGATIVE_INFINITY;
		
		double hitSumOfNumFree = 0;
		double missSumOfNumFree = 0;

		for (int i = 0; i < activeSize; i++) {
			if (y[i] == +1) {
				if (isLowerbound(i)){
				  hitUpperbound = Math.min(Double.POSITIVE_INFINITY, g[i]);					
				}else if (isUpperbound(i)){
					hitLowerBound = Math.max(Double.NEGATIVE_INFINITY, g[i]);					
				}else {
					++hitNumFree;
					hitSumOfNumFree += g[i];
				}
			} else {
				if (isLowerbound(i)) {
				  missUpperbound = Math.min( Double.POSITIVE_INFINITY, g[i]);					
				} else if (isUpperbound(i)) {
				  missLowerbound = Math.max(Double.NEGATIVE_INFINITY, g[i]);					
				} else {
					++missNumFree;
					missSumOfNumFree += g[i];
				}
			}
		}


		//y[i] == +1, i.e. a 'hit'
		double rForHits;
		if (hitNumFree > 0){
		  rForHits = hitSumOfNumFree / hitNumFree;			
		} else{			
		  rForHits = (hitUpperbound + hitLowerBound) / 2;
		}

		//y[i] != +1, i.e. a 'miss'
		double rForMisses;
		if (missNumFree > 0){
		  rForMisses = missSumOfNumFree / missNumFree;			
		} else {
		  rForMisses = (missUpperbound + missLowerbound) / 2;			
		}

		solutionInfo.r = (rForHits + rForMisses) / 2;
		return solutionInfo.r;
	}
}





public class svm {
	private static ResultCollector resultCollector = new ResultCollector();	
	//
	// construct and solve various formulations
	//
	public static final int LIBSVM_VERSION = 320;
	public static final Random rand = new Random();

	private static SvmPrintInterface svm_print_stdout = new SvmPrintInterface() {
		public void print(String s) {
			System.out.print(s);
			System.out.flush();
		}
	};

	private static SvmPrintInterface svm_print_string = svm_print_stdout;
	
	public static void setResultCollector(ResultCollector crossValidationResults) {
		resultCollector = crossValidationResults;
	}
	static void info(String s) {
		svm_print_string.print(s);
	}
//just where resultcollector cannot be seen
	public static <T> void addResult(T result, String resultType) {
		if("iterations".equals(resultType)){	
			resultCollector.addIteration((Integer) result);
		}else if("progress".equals(resultType)){
			//TODO: handle progress
		}else if("console output".equals(resultType)){
			resultCollector.addConsoleOutput(null,(String) result, true);
		}else{
			resultCollector.addCrossValResult((String) result);			
		}
	}

	private static void solve_c_svc(SvmProblem prob, SvmParameter param,
			double[] alpha, Solver.SolutionInfo si, double Cp, double Cn) {
		int l = prob.length;
		double[] minus_ones = new double[l];
		byte[] y = new byte[l];

		int i;

		for (i = 0; i < l; i++) {
			alpha[i] = 0;
			minus_ones[i] = -1;
			if (prob.y[i] > 0)
				y[i] = +1;
			else
				y[i] = -1;
		}

		Solver s = new Solver();
		s.Solve(l, new SVC_Q(prob, param, y), minus_ones, y, alpha, Cp, Cn,
				param.getEpsilonTolerance(), si, param.getShrinking());

		double sum_alpha = 0;
		for (i = 0; i < l; i++)
			sum_alpha += alpha[i];

		if (Cp == Cn) {
			double nu = sum_alpha / (Cp * prob.length);
			resultCollector.addConsoleOutput("nu = ", String.valueOf(nu), true);
			resultCollector.addNu(nu);
			resultCollector.addCrossValResult(nu + "");
		}
		
		for (i = 0; i < l; i++){
			alpha[i] *= y[i];			
		}
	}

	private static void solve_nu_svc(SvmProblem prob, SvmParameter param,
			double[] alpha, Solver.SolutionInfo si) {
		int i;
		int l = prob.length;
		double nu = param.getNu();

		byte[] y = new byte[l];

		for (i = 0; i < l; i++)
			if (prob.y[i] > 0)
				y[i] = +1;
			else
				y[i] = -1;

		double sum_pos = nu * l / 2;
		double sum_neg = nu * l / 2;

		for (i = 0; i < l; i++)
			if (y[i] == +1) {
				alpha[i] = Math.min(1.0, sum_pos);
				sum_pos -= alpha[i];
			} else {
				alpha[i] = Math.min(1.0, sum_neg);
				sum_neg -= alpha[i];
			}

		double[] zeros = new double[l];

		for (i = 0; i < l; i++)
			zeros[i] = 0;

		Solver_NU s = new Solver_NU();
		s.Solve(l, new SVC_Q(prob, param, y), zeros, y, alpha, 1.0, 1.0,
				param.getEpsilonTolerance(), si, param.getShrinking());
		double r = si.r;
		double c = 1 / r;
		
		//TODO: remove this reference
		svm.info("C = " + c + "\n");
		resultCollector.addConsoleOutput("C = ", String.valueOf(c), true);
		
		for (i = 0; i < l; i++)
			alpha[i] *= y[i] / r;

		si.rho /= r;
		si.obj /= (r * r);
		si.upper_bound_p = 1 / r;
		si.upper_bound_n = 1 / r;
	}

	private static void solve_one_class(SvmProblem prob, SvmParameter param,
			double[] alpha, Solver.SolutionInfo si) {
		int l = prob.length;
		double[] zeros = new double[l];
		byte[] ones = new byte[l];
		int i;

		int n = (int) (param.getNu() * prob.length); // # of alpha's at upper bound

		for (i = 0; i < n; i++)
			alpha[i] = 1;
		if (n < prob.length)
			alpha[n] = param.getNu() * prob.length - n;
		for (i = n + 1; i < l; i++)
			alpha[i] = 0;

		for (i = 0; i < l; i++) {
			zeros[i] = 0;
			ones[i] = 1;
		}

		Solver s = new Solver();
		s.Solve(l, new ONE_CLASS_Q(prob, param), zeros, ones, alpha, 1.0, 1.0,
				param.getEpsilonTolerance(), si, param.getShrinking());
	}

	private static void solve_epsilon_svr(SvmProblem prob, SvmParameter param,
			double[] alpha, Solver.SolutionInfo si) {
		int l = prob.length;
		double[] alpha2 = new double[2 * l];
		double[] linear_term = new double[2 * l];
		byte[] y = new byte[2 * l];
		int i;

		for (i = 0; i < l; i++) {
			alpha2[i] = 0;
			linear_term[i] = param.getEpsilonLossFunction() - prob.y[i];
			y[i] = 1;

			alpha2[i + l] = 0;
			linear_term[i + l] = param.getEpsilonLossFunction() + prob.y[i];
			y[i + l] = -1;
		}

		Solver s = new Solver();
		s.Solve(2 * l, new SVR_Q(prob, param), linear_term, y, alpha2, param.getCostC(),
				param.getCostC(), param.getEpsilonTolerance(), si, param.getShrinking());

		double sum_alpha = 0;
		for (i = 0; i < l; i++) {
			alpha[i] = alpha2[i] - alpha2[i + l];
			sum_alpha += Math.abs(alpha[i]);
		}
		double nu = sum_alpha / (param.getCostC() * l);
		//TODO: remove this reference
		svm.info("nu = " + nu + "\n");
		resultCollector.addConsoleOutput("nu = ", String.valueOf(nu), true);
	}

	private static void solve_nu_svr(SvmProblem prob, SvmParameter param,
			double[] alpha, Solver.SolutionInfo si) {
		int l = prob.length;
		double C = param.getCostC();
		double[] alpha2 = new double[2 * l];
		double[] linear_term = new double[2 * l];
		byte[] y = new byte[2 * l];
		int i;

		double sum = C * param.getNu() * l / 2;
		for (i = 0; i < l; i++) {
			alpha2[i] = alpha2[i + l] = Math.min(sum, C);
			sum -= alpha2[i];

			linear_term[i] = -prob.y[i];
			y[i] = 1;

			linear_term[i + l] = prob.y[i];
			y[i + l] = -1;
		}

		Solver_NU s = new Solver_NU();
		s.Solve(2 * l, new SVR_Q(prob, param), linear_term, y, alpha2, C, C,
				param.getEpsilonTolerance(), si, param.getShrinking());

		//TODO: remove this reference
		svm.info("epsilon = " + (-si.r) + "\n");
		resultCollector.addConsoleOutput("epsilon (in solve_nu_svr) = ", String.valueOf(-si.r), true);
		
		for (i = 0; i < l; i++)
			alpha[i] = alpha2[i] - alpha2[i + l];
	}

	//
	// decision_function
	//
	static class decision_function {
		double[] alpha;
		double rho;
	};

	static decision_function svm_train_one(SvmProblem prob,
			SvmParameter param, double Cp, double Cn) {
		double[] alpha = new double[prob.length];
		Solver.SolutionInfo si = new Solver.SolutionInfo();
		switch (param.getSvmType()) {
		case c_svc:
			solve_c_svc(prob, param, alpha, si, Cp, Cn);
			break;
		case nu_svc:
			solve_nu_svc(prob, param, alpha, si);
			break;
		case one_class:
			solve_one_class(prob, param, alpha, si);
			break;
		case epsilon_svr:
			solve_epsilon_svr(prob, param, alpha, si);
			break;
		case nu_svr:
			solve_nu_svr(prob, param, alpha, si);
			break;
		}

		resultCollector.addConsoleOutput("obj = ", String.valueOf(si.obj), false);
		resultCollector.addConsoleOutput(", rho = ", String.valueOf(si.rho), true);
		resultCollector.addCrossValResult(si.obj + "");
		resultCollector.addCrossValResult("" + si.rho);
		resultCollector.addObj(si.obj);
		resultCollector.addRho(si.rho);
		// output SVs

		int nSV = 0;
		int nBSV = 0;
		for (int i = 0; i < prob.length; i++) {
			if (Math.abs(alpha[i]) > 0) {
				++nSV;
				if (prob.y[i] > 0) {
					if (Math.abs(alpha[i]) >= si.upper_bound_p)
						++nBSV;
				} else {
					if (Math.abs(alpha[i]) >= si.upper_bound_n)
						++nBSV;
				}
			}
		}

		resultCollector.addConsoleOutput("nSV = " , String.valueOf(nSV), false);
		resultCollector.addConsoleOutput(", nBSV = ", String.valueOf(nBSV), true);
		if(param.getSvmType().equals(SvmType.nu_svr)){
		  System.out.println(resultCollector.getConsoleOutput());
		}
		resultCollector.addCrossValResult("" + nSV);
		resultCollector.addCrossValResult(""+ nBSV);
		resultCollector.addNSv(nSV);
		resultCollector.addNBSv(nBSV);
		
		decision_function f = new decision_function();
		f.alpha = alpha;
		f.rho = si.rho;
		return f;
	}

	// Platt's binary SVM Probablistic Output: an improvement from Lin et al.
	private static void sigmoid_train(int l, double[] dec_values, 
			double[] labels, double[] probAB) {
		double A, B;
		double prior1 = 0, prior0 = 0;
		int i;

		for (i = 0; i < l; i++)
			if (labels[i] > 0)
				prior1 += 1;
			else
				prior0 += 1;

		int max_iter = 100; // Maximal number of iterations
		double min_step = 1e-10; // Minimal step taken in line search
		double sigma = 1e-12; // For numerically strict PD of Hessian
		double eps = 1e-5;
		double hiTarget = (prior1 + 1.0) / (prior1 + 2.0);
		double loTarget = 1 / (prior0 + 2.0);
		double[] t = new double[l];
		double fApB, p, q, h11, h22, h21, g1, g2, det, dA, dB, gd, stepsize;
		double newA, newB, newf, d1, d2;
		int iter;

		// Initial Point and Initial Fun Value
		A = 0.0;
		B = Math.log((prior0 + 1.0) / (prior1 + 1.0));
		double fval = 0.0;

		for (i = 0; i < l; i++) {
			if (labels[i] > 0)
				t[i] = hiTarget;
			else
				t[i] = loTarget;
			fApB = dec_values[i] * A + B;
			if (fApB >= 0)
				fval += t[i] * fApB + Math.log(1 + Math.exp(-fApB));
			else
				fval += (t[i] - 1) * fApB + Math.log(1 + Math.exp(fApB));
		}
		for (iter = 0; iter < max_iter; iter++) {
			// Update Gradient and Hessian (use H' = H + sigma I)
			h11 = sigma; // numerically ensures strict PD
			h22 = sigma;
			h21 = 0.0;
			g1 = 0.0;
			g2 = 0.0;
			for (i = 0; i < l; i++) {
				fApB = dec_values[i] * A + B;
				if (fApB >= 0) {
					p = Math.exp(-fApB) / (1.0 + Math.exp(-fApB));
					q = 1.0 / (1.0 + Math.exp(-fApB));
				} else {
					p = 1.0 / (1.0 + Math.exp(fApB));
					q = Math.exp(fApB) / (1.0 + Math.exp(fApB));
				}
				d2 = p * q;
				h11 += dec_values[i] * dec_values[i] * d2;
				h22 += d2;
				h21 += dec_values[i] * d2;
				d1 = t[i] - p;
				g1 += dec_values[i] * d1;
				g2 += d1;
			}

			// Stopping Criteria
			if (Math.abs(g1) < eps && Math.abs(g2) < eps)
				break;

			// Finding Newton direction: -inv(H') * g
			det = h11 * h22 - h21 * h21;
			dA = -(h22 * g1 - h21 * g2) / det;
			dB = -(-h21 * g1 + h11 * g2) / det;
			gd = g1 * dA + g2 * dB;

			stepsize = 1; // Line Search
			while (stepsize >= min_step) {
				newA = A + stepsize * dA;
				newB = B + stepsize * dB;

				// New function value
				newf = 0.0;
				for (i = 0; i < l; i++) {
					fApB = dec_values[i] * newA + newB;
					if (fApB >= 0)
						newf += t[i] * fApB + Math.log(1 + Math.exp(-fApB));
					else
						newf += (t[i] - 1) * fApB
								+ Math.log(1 + Math.exp(fApB));
				}
				// Check sufficient decrease
				if (newf < fval + 0.0001 * stepsize * gd) {
					A = newA;
					B = newB;
					fval = newf;
					break;
				} else
					stepsize = stepsize / 2.0;
			}

			if (stepsize < min_step) {
				svm.info("Line search fails in two-class probability estimates\n");
				break;
			}
		}

		if (iter >= max_iter)
			svm.info("Reaching maximal iterations in two-class probability estimates\n");
		probAB[0] = A;
		probAB[1] = B;
	}

	private static double sigmoid_predict(double decision_value, double A,
			double B) {
		double fApB = decision_value * A + B;
		if (fApB >= 0)
			return Math.exp(-fApB) / (1.0 + Math.exp(-fApB));
		else
			return 1.0 / (1 + Math.exp(fApB));
	}

	// Method 2 from the multiclass_prob paper by Wu, Lin, and Weng
	private static void multiclass_probability(int k, double[][] r, double[] p) {
		int t, j;
		int iter = 0, max_iter = Math.max(100, k);
		double[][] Q = new double[k][k];
		double[] Qp = new double[k];
		double pQp, eps = 0.005 / k;

		for (t = 0; t < k; t++) {
			p[t] = 1.0 / k; // Valid if k = 1
			Q[t][t] = 0;
			for (j = 0; j < t; j++) {
				Q[t][t] += r[j][t] * r[j][t];
				Q[t][j] = Q[j][t];
			}
			for (j = t + 1; j < k; j++) {
				Q[t][t] += r[j][t] * r[j][t];
				Q[t][j] = -r[j][t] * r[t][j];
			}
		}
		for (iter = 0; iter < max_iter; iter++) {
			// stopping condition, recalculate QP,pQP for numerical accuracy
			pQp = 0;
			for (t = 0; t < k; t++) {
				Qp[t] = 0;
				for (j = 0; j < k; j++)
					Qp[t] += Q[t][j] * p[j];
				pQp += p[t] * Qp[t];
			}
			double max_error = 0;
			for (t = 0; t < k; t++) {
				double error = Math.abs(Qp[t] - pQp);
				if (error > max_error)
					max_error = error;
			}
			if (max_error < eps)
				break;

			for (t = 0; t < k; t++) {
				double diff = (-Qp[t] + pQp) / Q[t][t];
				p[t] += diff;
				pQp = (pQp + diff * (diff * Q[t][t] + 2 * Qp[t])) / (1 + diff)
						/ (1 + diff);
				for (j = 0; j < k; j++) {
					Qp[j] = (Qp[j] + diff * Q[t][j]) / (1 + diff);
					p[j] /= (1 + diff);
				}
			}
		}
		if (iter >= max_iter)
			svm.info("Exceeds max_iter in multiclass_prob\n");
	}

	// Cross-validation decision values for probability estimates
	private static void svm_binary_svc_probability(SvmProblem prob,
			SvmParameter param, double Cp, double Cn, double[] probAB) {
		int i;
		int nr_fold = 5;
		int[] perm = new int[prob.length];
		double[] dec_values = new double[prob.length];

		// random shuffle
		for (i = 0; i < prob.length; i++)
			perm[i] = i;
		for (i = 0; i < prob.length; i++) {
			int j = i + rand.nextInt(prob.length - i);
			do {
				int _ = perm[i];
				perm[i] = perm[j];
				perm[j] = _;
			} while (false);
		}
		for (i = 0; i < nr_fold; i++) {
			int begin = i * prob.length / nr_fold;
			int end = (i + 1) * prob.length / nr_fold;
			int j, k;
			SvmProblem subprob = new SvmProblem();

			subprob.length = prob.length - (end - begin);
			subprob.x = new SvmNode[subprob.length][];
			subprob.y = new double[subprob.length];

			k = 0;
			for (j = 0; j < begin; j++) {
				subprob.x[k] = prob.x[perm[j]];
				subprob.y[k] = prob.y[perm[j]];
				++k;
			}
			for (j = end; j < prob.length; j++) {
				subprob.x[k] = prob.x[perm[j]];
				subprob.y[k] = prob.y[perm[j]];
				++k;
			}
			int p_count = 0, n_count = 0;
			for (j = 0; j < k; j++)
				if (subprob.y[j] > 0)
					p_count++;
				else
					n_count++;

			if (p_count == 0 && n_count == 0)
				for (j = begin; j < end; j++)
					dec_values[perm[j]] = 0;
			else if (p_count > 0 && n_count == 0)
				for (j = begin; j < end; j++)
					dec_values[perm[j]] = 1;
			else if (p_count == 0 && n_count > 0)
				for (j = begin; j < end; j++)
					dec_values[perm[j]] = -1;
			else {
				SvmParameter subparam = (SvmParameter) param.clone();
				subparam.setProbability(0);
				subparam.costC = 1.0;
				subparam.nr_weight = 2;
				subparam.setWeightLabel(new int[2]);
				subparam.setWeight(new double[2]);
				subparam.getWeight_label()[0] = +1;
				subparam.getWeight_label()[1] = -1;
				subparam.getWeight()[0] = Cp;
				subparam.getWeight()[1] = Cn;
				SvmModel submodel = svm_train(subprob, subparam);
				for (j = begin; j < end; j++) {
					double[] dec_value = new double[1];
					svm_predict_values(submodel, prob.x[perm[j]], dec_value);
					dec_values[perm[j]] = dec_value[0];
					// ensure +1 -1 order; reason not using CV subroutine
					dec_values[perm[j]] *= submodel.classLabel[0];
				}
			}
		}
		sigmoid_train(prob.length, dec_values, prob.y, probAB);
	}

	// Return parameter of a Laplace distribution
	private static double svm_svr_probability(SvmProblem prob, SvmParameter param) {
		int i;
		int nr_fold = 5;
		double[] ymv = new double[prob.length];
		double mae = 0;

		SvmParameter newparam = (SvmParameter) param.clone();
		newparam.setProbability(0);
		svm_cross_validation(prob, newparam, nr_fold, ymv);
		for (i = 0; i < prob.length; i++) {
			ymv[i] = prob.y[i] - ymv[i];
			mae += Math.abs(ymv[i]);
		}
		mae /= prob.length;
		double std = Math.sqrt(2 * mae * mae);
		int count = 0;
		mae = 0;
		for (i = 0; i < prob.length; i++)
			if (Math.abs(ymv[i]) > 5 * std)
				count = count + 1;
			else
				mae += Math.abs(ymv[i]);
		mae /= (prob.length - count);
		svm.info("Prob. model for test data: target value = predicted value + z,\nz: Laplace distribution e^(-|z|/sigma)/(2sigma),sigma="
				+ mae + "\n");
		return mae;
	}

	// label: label name, start: begin of each class, count: #data of classes,
	// perm: indices to the original data
	// perm, length l, must be allocated before calling this subroutine
  private static void svm_group_classes(SvmProblem prob, int[] nr_class_ret, int[][] label_ret,
      int[][] start_ret, int[][] count_ret, int[] perm) {
    int l = prob.length;
    int maxNumClasses = 16;
    int nr_class = 0;
    int[] label = new int[maxNumClasses];
    int[] count = new int[maxNumClasses];
    int[] data_label = new int[l];
    int i;

    for (i = 0; i < l; i++) {
      int this_label = (int) (prob.y[i]);
      int j;

      for (j = 0; j < nr_class; j++) {
        if (this_label == label[j]) {
          ++count[j];
          break;
        }
      }

      data_label[i] = j;
      if (j == nr_class) {
        if (nr_class == maxNumClasses) {
          maxNumClasses *= 2;
          int[] new_data = new int[maxNumClasses];
          System.arraycopy(label, 0, new_data, 0, label.length);
          label = new_data;
          new_data = new int[maxNumClasses];
          System.arraycopy(count, 0, new_data, 0, count.length);
          count = new_data;
        }
        label[nr_class] = this_label;
        count[nr_class] = 1;
        ++nr_class;
      }
    }

    //
    // Labels are ordered by their first occurrence in the training set.
    // However, for two-class sets with -1/+1 labels and -1 appears first,
    // we swap labels to ensure that internally the binary SVM has positive
    // data corresponding to the +1 instances.
    //
    if (nr_class == 2 && label[0] == -1 && label[1] == +1) {
      do {
        int _ = label[0];
        label[0] = label[1];
        label[1] = _;
      } while (false);
      do {
        int _ = count[0];
        count[0] = count[1];
        count[1] = _;
      } while (false);
      for (i = 0; i < l; i++) {
        if (data_label[i] == 0)
          data_label[i] = 1;
        else
          data_label[i] = 0;
      }
    }

    int[] start = new int[nr_class];
    start[0] = 0;
    for (i = 1; i < nr_class; i++)
      start[i] = start[i - 1] + count[i - 1];
    for (i = 0; i < l; i++) {
      perm[start[data_label[i]]] = i;
      ++start[data_label[i]];
    }
    start[0] = 0;
    for (i = 1; i < nr_class; i++)
      start[i] = start[i - 1] + count[i - 1];

    nr_class_ret[0] = nr_class;
    label_ret[0] = label;
    start_ret[0] = start;
    count_ret[0] = count;
  }

	//
	// Interface functions
	//
	public static SvmModel svm_train(SvmProblem prob, SvmParameter param) {
		SvmModel model = new SvmModel();
		model.setParam(param);

		SvmType svmType = param.getSvmType();
		if (svmType == SvmType.one_class
				|| svmType == SvmType.epsilon_svr
				|| svmType == SvmType.nu_svr) {
			// regression or one-class-svm
			model.numClasses = 2;
			model.classLabel = null;
			model.nSV = null;
			model.probA = null;
			model.probB = null;
			model.sv_coef = new double[1][];

			if (param.getProbability() == 1
					&& (svmType == SvmType.epsilon_svr || svmType == SvmType.nu_svr)) {
				model.probA = new double[1];
				model.probA[0] = svm_svr_probability(prob, param);
			}

			decision_function f = svm_train_one(prob, param, 0, 0);
			model.rho = new double[1];
			model.rho[0] = f.rho;

			int nSV = 0;
			int i;
			for (i = 0; i < prob.length; i++)
				if (Math.abs(f.alpha[i]) > 0)
					++nSV;
			model.totalSv = nSV;
			model.SV = new SvmNode[nSV][];
			model.sv_coef[0] = new double[nSV];
			model.sv_indices = new int[nSV];
			int j = 0;
			for (i = 0; i < prob.length; i++)
				if (Math.abs(f.alpha[i]) > 0) {
					model.SV[j] = prob.x[i];
					model.sv_coef[0][j] = f.alpha[i];
					model.sv_indices[j] = i + 1;
					++j;
				}
		} else {
			// classification
			int l = prob.length;
			int[] tmp_nr_class = new int[1];
			int[][] tmp_label = new int[1][];
			int[][] tmp_start = new int[1][];
			int[][] tmp_count = new int[1][];
			int[] perm = new int[l];

			// group training data of the same class
			svm_group_classes(prob, tmp_nr_class, tmp_label, tmp_start,
					tmp_count, perm);
			int nr_class = tmp_nr_class[0];
			int[] label = tmp_label[0];
			int[] start = tmp_start[0];
			int[] count = tmp_count[0];

			if (nr_class == 1)
				svm.info("WARNING: training data in only one class. See README for details.\n");

			SvmNode[][] x = new SvmNode[l][];
			int i;
			for (i = 0; i < l; i++)
				x[i] = prob.x[perm[i]];

			// calculate weighted C

			double[] weighted_C = new double[nr_class];
			for (i = 0; i < nr_class; i++)
				weighted_C[i] = param.getCostC();
			for (i = 0; i < param.nr_weight; i++) {
				int j;
				for (j = 0; j < nr_class; j++)
					if (param.getWeight_label()[i] == label[j])
						break;
				if (j == nr_class)
					System.err.print("WARNING: class label "
							+ param.getWeight_label()[i]
							+ " specified in weight is not found\n");
				else
					weighted_C[j] *= param.getWeight()[i];
			}

			// train k*(k-1)/2 models

			boolean[] nonzero = new boolean[l];
			for (i = 0; i < l; i++)
				nonzero[i] = false;
			decision_function[] f = new decision_function[nr_class
					* (nr_class - 1) / 2];

			double[] probA = null, probB = null;
			if (param.getProbability() == 1) {
				probA = new double[nr_class * (nr_class - 1) / 2];
				probB = new double[nr_class * (nr_class - 1) / 2];
			}

			int p = 0;
			for (i = 0; i < nr_class; i++)
				for (int j = i + 1; j < nr_class; j++) {
					SvmProblem sub_prob = new SvmProblem();
					int si = start[i], sj = start[j];
					int ci = count[i], cj = count[j];
					sub_prob.length = ci + cj;
					sub_prob.x = new SvmNode[sub_prob.length][];
					sub_prob.y = new double[sub_prob.length];
					int k;
					for (k = 0; k < ci; k++) {
						sub_prob.x[k] = x[si + k];
						sub_prob.y[k] = +1;
					}
					for (k = 0; k < cj; k++) {
						sub_prob.x[ci + k] = x[sj + k];
						sub_prob.y[ci + k] = -1;
					}

					if (param.getProbability() == 1) {
						double[] probAB = new double[2];
						svm_binary_svc_probability(sub_prob, param,
								weighted_C[i], weighted_C[j], probAB);
						probA[p] = probAB[0];
						probB[p] = probAB[1];
					}

					f[p] = svm_train_one(sub_prob, param, weighted_C[i],
							weighted_C[j]);
					for (k = 0; k < ci; k++)
						if (!nonzero[si + k] && Math.abs(f[p].alpha[k]) > 0)
							nonzero[si + k] = true;
					for (k = 0; k < cj; k++)
						if (!nonzero[sj + k]
								&& Math.abs(f[p].alpha[ci + k]) > 0)
							nonzero[sj + k] = true;
					++p;
				}

			// build output

			model.numClasses = nr_class;

			model.classLabel = new int[nr_class];
			for (i = 0; i < nr_class; i++)
				model.classLabel[i] = label[i];

			model.rho = new double[nr_class * (nr_class - 1) / 2];
			for (i = 0; i < nr_class * (nr_class - 1) / 2; i++)
				model.rho[i] = f[i].rho;

			if (param.getProbability() == 1) {
				model.probA = new double[nr_class * (nr_class - 1) / 2];
				model.probB = new double[nr_class * (nr_class - 1) / 2];
				for (i = 0; i < nr_class * (nr_class - 1) / 2; i++) {
					model.probA[i] = probA[i];
					model.probB[i] = probB[i];
				}
			} else {
				model.probA = null;
				model.probB = null;
			}

			int totalNSv = 0;
			int[] nz_count = new int[nr_class];
			model.nSV = new int[nr_class];
			for (i = 0; i < nr_class; i++) {
				int nSV = 0;
				for (int j = 0; j < count[i]; j++)
					if (nonzero[start[i] + j]) {
						++nSV;
						++totalNSv;
					}
				model.nSV[i] = nSV;
				nz_count[i] = nSV;
			}
			
			resultCollector.addConsoleOutput("Total nSV = ", String.valueOf(totalNSv), false);
			System.out.println(resultCollector.getConsoleOutput());
			resultCollector.addDeprecatedTotalNsv("" + totalNSv);
			resultCollector.addTotalNSv(totalNSv);
			
			model.totalSv = totalNSv;
			model.SV = new SvmNode[totalNSv][];
			model.sv_indices = new int[totalNSv];
			p = 0;
			for (i = 0; i < l; i++)
				if (nonzero[i]) {
					model.SV[p] = x[i];
					model.sv_indices[p++] = perm[i] + 1;
				}

			int[] nz_start = new int[nr_class];
			nz_start[0] = 0;
			for (i = 1; i < nr_class; i++)
				nz_start[i] = nz_start[i - 1] + nz_count[i - 1];

			model.sv_coef = new double[nr_class - 1][];
			for (i = 0; i < nr_class - 1; i++)
				model.sv_coef[i] = new double[totalNSv];

			p = 0;
			for (i = 0; i < nr_class; i++)
				for (int j = i + 1; j < nr_class; j++) {
					// classifier (i,j): coefficients with
					// i are in sv_coef[j-1][nz_start[i]...],
					// j are in sv_coef[i][nz_start[j]...]

					int si = start[i];
					int sj = start[j];
					int ci = count[i];
					int cj = count[j];

					int q = nz_start[i];
					int k;
					for (k = 0; k < ci; k++)
						if (nonzero[si + k])
							model.sv_coef[j - 1][q++] = f[p].alpha[k];
					q = nz_start[j];
					for (k = 0; k < cj; k++)
						if (nonzero[sj + k])
							model.sv_coef[i][q++] = f[p].alpha[ci + k];
					++p;
				}
		}
		return model;
	}

	// Stratified cross validation
	public static void svm_cross_validation(SvmProblem prob, SvmParameter param, int nr_fold, double[] target) {
		int i;
		int[] fold_start = new int[nr_fold + 1];
		int l = prob.length;
		int[] perm = new int[l];

		// stratified cv may not give leave-one-out rate
		// Each class to l folds -> some folds may have zero elements
		SvmType svmType = param.getSvmType();
		
		//TODO: move n_fold check to param/problem validation phase
		if ((svmType == SvmType.c_svc || svmType == SvmType.nu_svc) && nr_fold < l) {
			int[] tmp_nr_class = new int[1];
			int[][] tmp_label = new int[1][];
			int[][] tmp_start = new int[1][];
			int[][] tmp_count = new int[1][];

			svm_group_classes(prob, tmp_nr_class, tmp_label, tmp_start,
					tmp_count, perm);

			int nr_class = tmp_nr_class[0];
			int[] start = tmp_start[0];
			int[] count = tmp_count[0];

			// random shuffle and then data grouped by fold using the array perm
			int[] fold_count = new int[nr_fold];
			int c;
			int[] index = new int[l];
			for (i = 0; i < l; i++)
				index[i] = perm[i];
			for (c = 0; c < nr_class; c++)
				for (i = 0; i < count[c]; i++) {
					int j = i + rand.nextInt(count[c] - i);
					do {
						int _ = index[start[c] + j];
						index[start[c] + j] = index[start[c] + i];
						index[start[c] + i] = _;
					} while (false);
				}
			for (i = 0; i < nr_fold; i++) {
				fold_count[i] = 0;
				for (c = 0; c < nr_class; c++)
					fold_count[i] += (i + 1) * count[c] / nr_fold - i
							* count[c] / nr_fold;
			}
			fold_start[0] = 0;
			for (i = 1; i <= nr_fold; i++)
				fold_start[i] = fold_start[i - 1] + fold_count[i - 1];
			for (c = 0; c < nr_class; c++)
				for (i = 0; i < nr_fold; i++) {
					int begin = start[c] + i * count[c] / nr_fold;
					int end = start[c] + (i + 1) * count[c] / nr_fold;
					for (int j = begin; j < end; j++) {
						perm[fold_start[i]] = index[j];
						fold_start[i]++;
					}
				}
			fold_start[0] = 0;
			for (i = 1; i <= nr_fold; i++)
				fold_start[i] = fold_start[i - 1] + fold_count[i - 1];
		} else {
			for (i = 0; i < l; i++)
				perm[i] = i;
			for (i = 0; i < l; i++) {
				int j = i + rand.nextInt(l - i);
				do {
					int _ = perm[i];
					perm[i] = perm[j];
					perm[j] = _;
				} while (false);
			}
			for (i = 0; i <= nr_fold; i++)
				fold_start[i] = i * l / nr_fold;
		}

		for (i = 0; i < nr_fold; i++) {
			int begin = fold_start[i];
			int end = fold_start[i + 1];
			int j, k;
			SvmProblem subprob = new SvmProblem();

			subprob.length = l - (end - begin);
			subprob.x = new SvmNode[subprob.length][];
			subprob.y = new double[subprob.length];

			k = 0;
			for (j = 0; j < begin; j++) {
				subprob.x[k] = prob.x[perm[j]];
				subprob.y[k] = prob.y[perm[j]];
				++k;
			}
			for (j = end; j < l; j++) {
				subprob.x[k] = prob.x[perm[j]];
				subprob.y[k] = prob.y[perm[j]];
				++k;
			}
			resultCollector.addCrossValResult("" + nr_fold);
			SvmModel submodel = svm_train(subprob, param);
			
			if(begin==fold_start[nr_fold-1]){
				svm.resultCollector.writeToFile("cross-validation-output.csv");
			}
			
			if (param.getProbability() == 1 && (svmType == SvmType.c_svc || svmType == SvmType.nu_svc)) {
				double[] prob_estimates = new double[submodel.numClasses];
				for (j = begin; j < end; j++){
					target[perm[j]] = svm_predict_probability(submodel, prob.x[perm[j]], prob_estimates);
				}
			} else{
				for (j = begin; j < end; j++) {
					target[perm[j]] = svm_predict(submodel, prob.x[perm[j]]);					
				}
			}
		}
	}

	public static SvmType getSvmTypeFromModel(SvmModel model) {
		return model.getParam().getSvmType();
	}

//	public static int svm_get_nr_class(SvmModel model) {
//		return model.numClasses;
//	}
//
	public static void setModelLabels(SvmModel model, int[] label) {
		if (model.classLabel != null){
		  for (int i = 0; i < model.numClasses; i++){
		    label[i] = model.classLabel[i];		  		    
		  }
		}
	}

//	public static void svm_get_sv_indices(SvmModel model, int[] indices) {
//		if (model.sv_indices != null){
//		  for (int i = 0; i < model.totalSv; i++){
//		    indices[i] = model.sv_indices[i];		  		    
//		  }
//		}
//	}

//	public static int svm_get_nr_sv(SvmModel model) {
//		return model.totalSv;
//	}

	public static double svm_get_svr_probability(SvmModel model) {
		if ((model.getParam().getSvmType() == SvmType.epsilon_svr || model
				.getParam().getSvmType() == SvmType.nu_svr) && model.probA != null)
			return model.probA[0];
		else {
			System.err
					.print("Model doesn't contain information for SVR probability inference\n");
			return 0;
		}
	}

	public static double svm_predict_values(SvmModel model, SvmNode[] x,
			double[] dec_values) {
		int i;
		if (model.getParam().getSvmType() == SvmType.one_class
				|| model.getParam().getSvmType() == SvmType.epsilon_svr
				|| model.getParam().getSvmType() == SvmType.nu_svr) {
			double[] sv_coef = model.sv_coef[0];
			double sum = 0;
			for (i = 0; i < model.totalSv; i++)
				sum += sv_coef[i]
						* Kernel.k_function(x, model.SV[i], model.getParam());
			sum -= model.rho[0];
			dec_values[0] = sum;

			if (model.getParam().getSvmType() == SvmType.one_class)
				return (sum > 0) ? 1 : -1;
			else
				return sum;
		} else {
			int numClasses = model.numClasses;
			int l = model.totalSv;

			double[] kvalue = new double[l];
			for (i = 0; i < l; i++)
				kvalue[i] = Kernel.k_function(x, model.SV[i], model.getParam());

			int[] start = new int[numClasses];
			start[0] = 0;
			for (i = 1; i < numClasses; i++)
				start[i] = start[i - 1] + model.nSV[i - 1];

			int[] vote = new int[numClasses];
			for (i = 0; i < numClasses; i++)
				vote[i] = 0;

			int p = 0;
			for (i = 0; i < numClasses; i++)
				for (int j = i + 1; j < numClasses; j++) {
					double sum = 0;
					int si = start[i];
					int sj = start[j];
					int ci = model.nSV[i];
					int cj = model.nSV[j];

					int k;
					double[] coef1 = model.sv_coef[j - 1];
					double[] coef2 = model.sv_coef[i];
					for (k = 0; k < ci; k++)
						sum += coef1[si + k] * kvalue[si + k];
					for (k = 0; k < cj; k++)
						sum += coef2[sj + k] * kvalue[sj + k];
					sum -= model.rho[p];
					dec_values[p] = sum;

					if (dec_values[p] > 0)
						++vote[i];
					else
						++vote[j];
					p++;
				}

			int vote_max_idx = 0;
			for (i = 1; i < numClasses; i++)
				if (vote[i] > vote[vote_max_idx])
					vote_max_idx = i;

			return model.classLabel[vote_max_idx];
		}
	}

	public static double svm_predict(SvmModel model, SvmNode[] x) {
		int numClasses = model.numClasses;
		double[] dec_values;
		if (model.getParam().getSvmType() == SvmType.one_class
				|| model.getParam().getSvmType() == SvmType.epsilon_svr
				|| model.getParam().getSvmType() == SvmType.nu_svr)
			dec_values = new double[1];
		else
			dec_values = new double[numClasses * (numClasses - 1) / 2];
		
		double pred_result = svm_predict_values(model, x, dec_values);
		return pred_result;
	}

	public static double svm_predict_probability(SvmModel model, SvmNode[] x,
			double[] prob_estimates) {
		if ((model.getParam().getSvmType() == SvmType.c_svc || model.getParam().getSvmType() == SvmType.nu_svc)
				&& model.probA != null && model.probB != null) {
			int i;
			int nr_class = model.numClasses;
			double[] dec_values = new double[nr_class * (nr_class - 1) / 2];
			svm_predict_values(model, x, dec_values);

			double min_prob = 1e-7;
			double[][] pairwise_prob = new double[nr_class][nr_class];

			int k = 0;
			for (i = 0; i < nr_class; i++)
				for (int j = i + 1; j < nr_class; j++) {
					pairwise_prob[i][j] = Math.min(Math.max(
							sigmoid_predict(dec_values[k], model.probA[k],
									model.probB[k]), min_prob), 1 - min_prob);
					pairwise_prob[j][i] = 1 - pairwise_prob[i][j];
					k++;
				}
			multiclass_probability(nr_class, pairwise_prob, prob_estimates);

			int prob_max_idx = 0;
			for (i = 1; i < nr_class; i++)
				if (prob_estimates[i] > prob_estimates[prob_max_idx])
					prob_max_idx = i;
			return model.classLabel[prob_max_idx];
		} else
			return svm_predict(model, x);
	}

	public static void svm_save_model(String model_file_name, SvmModel model)
			throws IOException {
		DataOutputStream fp = new DataOutputStream(new BufferedOutputStream(
				new FileOutputStream(model_file_name)));

		SvmParameter param = model.getParam();

		fp.writeBytes("svm_type " + param.getSvmType().toString() + "\n");
		fp.writeBytes("kernel_type " + param.getKernelType().toString() + "\n");

		if (param.getKernelType() == KernelType.poly)
			fp.writeBytes("degree " + param.getDegree() + "\n");

		if (param.getKernelType() == KernelType.poly
				|| param.getKernelType() == KernelType.rbf
				|| param.getKernelType() == KernelType.sigmoid)
			fp.writeBytes("gamma " + param.getGamma() + "\n");

		if (param.getKernelType() == KernelType.poly
				|| param.getKernelType() == KernelType.sigmoid)
			fp.writeBytes("coef0 " + param.getCoef0() + "\n");

		int nr_class = model.numClasses;
		int l = model.totalSv;
		fp.writeBytes("nr_class " + nr_class + "\n");
		fp.writeBytes("total_sv " + l + "\n");

		{
			fp.writeBytes("rho");
			for (int i = 0; i < nr_class * (nr_class - 1) / 2; i++)
				fp.writeBytes(" " + model.rho[i]);
			fp.writeBytes("\n");
		}

		if (model.classLabel != null) {
			fp.writeBytes("label");
			for (int i = 0; i < nr_class; i++)
				fp.writeBytes(" " + model.classLabel[i]);
			fp.writeBytes("\n");
		}

		if (model.probA != null) { // regression has probA only
			fp.writeBytes("probA");
			for (int i = 0; i < nr_class * (nr_class - 1) / 2; i++)
				fp.writeBytes(" " + model.probA[i]);
			fp.writeBytes("\n");
		}
		if (model.probB != null) {
			fp.writeBytes("probB");
			for (int i = 0; i < nr_class * (nr_class - 1) / 2; i++)
				fp.writeBytes(" " + model.probB[i]);
			fp.writeBytes("\n");
		}

		if (model.nSV != null) {
			fp.writeBytes("nr_sv");
			for (int i = 0; i < nr_class; i++)
				fp.writeBytes(" " + model.nSV[i]);
			fp.writeBytes("\n");
		}

		fp.writeBytes("SV\n");
		double[][] sv_coef = model.sv_coef;
		SvmNode[][] SV = model.SV;

		for (int i = 0; i < l; i++) {
			for (int j = 0; j < nr_class - 1; j++)
				fp.writeBytes(sv_coef[j][i] + " ");

			SvmNode[] p = SV[i];
			
			if (param.getKernelType() == KernelType.precomputed)
				fp.writeBytes("0:" + (int) (p[0].value));
			else
				for (int j = 0; j < p.length; j++)
					fp.writeBytes(p[j].index + ":" + p[j].value + " ");
			fp.writeBytes("\n");
		}

		fp.close();
	}

	
	protected static boolean read_model_header(BufferedReader fp, SvmModel model) {
		SvmParameter param = new SvmParameter();
		model.setParam(param);
		try {
			while (true) {
				String cmd = fp.readLine();
				String arg = cmd.substring(cmd.indexOf(' ') + 1);

				if (cmd.startsWith("svm_type")) {
					try{
						param.setSvmType(SvmType.valueOf(arg));
						if(param.getSvmType() == null){
							throw new IllegalArgumentException();
						}
					}catch(IllegalArgumentException e){
						//TODO: handle exception
						System.err.print("Unknown svm type.\n");
						return false;
					}
				} else if (cmd.startsWith("kernel_type")) {
					try{
						param.setKernelType(KernelType.valueOf(arg));
						if(param.getKernelType() == null){
							throw new IllegalArgumentException();
						}
					}catch(IllegalArgumentException e){		
						//TODO: handle exception
						System.err.print("Unknown kernel function.\n");
						return false;
					}
				} else if (cmd.startsWith("degree"))
					param.setDegree(Integer.parseInt(arg));
				else if (cmd.startsWith("gamma"))
					param.setGamma(Double.parseDouble(arg));
				else if (cmd.startsWith("coef0"))
					param.setCoef0(Double.parseDouble(arg));
				else if (cmd.startsWith("nr_class"))
					model.numClasses = Integer.parseInt(arg);
				else if (cmd.startsWith("total_sv"))
					model.totalSv = Integer.parseInt(arg);
				else if (cmd.startsWith("rho")) {
					int n = model.numClasses * (model.numClasses - 1) / 2;
					model.rho = new double[n];
					StringTokenizer st = new StringTokenizer(arg);
					for (int i = 0; i < n; i++)
						model.rho[i] = Double.parseDouble(st.nextToken());
				} else if (cmd.startsWith("label")) {
					int n = model.numClasses;
					model.classLabel = new int[n];
					StringTokenizer st = new StringTokenizer(arg);
					for (int i = 0; i < n; i++)
						model.classLabel[i] = Integer.parseInt(st.nextToken());
				} else if (cmd.startsWith("probA")) {
					int n = model.numClasses * (model.numClasses - 1) / 2;
					model.probA = new double[n];
					StringTokenizer st = new StringTokenizer(arg);
					for (int i = 0; i < n; i++)
						model.probA[i] = Double.parseDouble(st.nextToken());
				} else if (cmd.startsWith("probB")) {
					int n = model.numClasses * (model.numClasses - 1) / 2;
					model.probB = new double[n];
					StringTokenizer st = new StringTokenizer(arg);
					for (int i = 0; i < n; i++)
						model.probB[i] = Double.parseDouble(st.nextToken());
				} else if (cmd.startsWith("nr_sv")) {
					int n = model.numClasses;
					model.nSV = new int[n];
					StringTokenizer st = new StringTokenizer(arg);
					for (int i = 0; i < n; i++)
						model.nSV[i] = Integer.parseInt(st.nextToken());
				} else if (cmd.startsWith("SV")) {
					break;
				} else {
					System.err.print("unknown text in model file: [" + cmd
							+ "]\n");
					return false;
				}
			}
		} catch (Exception e) {
			return false;
		}
		return true;
	}

	public static SvmModel svm_load_model(String model_file_name)
			throws IOException {
		return svm_load_model(new BufferedReader(
				new FileReader(model_file_name)));
	}

	public static SvmModel svm_load_model(BufferedReader fp) throws IOException {
		// read parameters

		SvmModel model = new SvmModel();
		model.rho = null;
		model.probA = null;
		model.probB = null;
		model.classLabel = null;
		model.nSV = null;

		if (read_model_header(fp, model) == false) {
			System.err.print("ERROR: failed to read model\n");
			return null;
		}

		// read sv_coef and SV

		int m = model.numClasses - 1;
		int l = model.totalSv;
		model.sv_coef = new double[m][l];
		model.SV = new SvmNode[l][];

		for (int i = 0; i < l; i++) {
			String line = fp.readLine();
			StringTokenizer st = new StringTokenizer(line, " \t\n\r\f:");

			for (int k = 0; k < m; k++)
				model.sv_coef[k][i] = Double.parseDouble(st.nextToken());
			int n = st.countTokens() / 2;
			model.SV[i] = new SvmNode[n];
			for (int j = 0; j < n; j++) {
				model.SV[i][j] = new SvmNode();
				model.SV[i][j].index = Integer.parseInt(st.nextToken());
				model.SV[i][j].value = Double.parseDouble(st.nextToken());
			}
		}

		fp.close();
		return model;
	}

	
	
	public static int svm_check_probability_model(SvmModel model) {
		SvmType svmType = model.getParam().getSvmType();
		if (((svmType == SvmType.c_svc || svmType == SvmType.nu_svc)
				&& model.probA != null && model.probB != null)
				|| ((svmType == SvmType.epsilon_svr ||svmType == SvmType.nu_svr) && model.probA != null))
			return 1;
		else
			return 0;
	}

	public static void svm_set_print_string_function(
			SvmPrintInterface print_func) {
		if (print_func == null)
			svm_print_string = svm_print_stdout;
		else
			svm_print_string = print_func;
	}
	public static ResultCollector getResultCollector() {
		return resultCollector;
	}
	

	
}
