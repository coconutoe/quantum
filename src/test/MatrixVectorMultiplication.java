package test;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.jscience.mathematics.number.Complex;

import com.aparapi.Kernel;
import com.aparapi.Range;

public class MatrixVectorMultiplication {
	
	public static void main(String[] args) {
		
		int iDimension = 1 << 25;
		int uDimension = 2;
		int vDimension = iDimension * uDimension;
		
		double[][] u = new double[uDimension][uDimension];
		double[] v = new double[vDimension];
		
		double[] res2 = new double[vDimension];
		double[] res4 = new double[vDimension];
		
			
		//u
		for (int i = 0; i < uDimension; i++) {
			for (int j = 0; j < uDimension; j++) {
				u[i][j] = (Math.random() * 100);
			}
		}
		
		//v
		for (int i = 0; i < vDimension; i++) {
			v[i] = (Math.random() * 10);
		}
		
		//opt CPU multiplication
		long time = System.currentTimeMillis();
		System.out.println("Starting original CPU computation");
		for (int i = 0; i < iDimension; i++) {
			for (int j = 0; j < uDimension; j++) {
				double val = 0;
				for (int k = 0; k < uDimension; k++) {
					val += u[j][k] * v[uDimension * i + k];
				}
				res2[uDimension * i + j] = val;
			}
		}
		System.out.println("Task finished in " + (System.currentTimeMillis() - time) + "ms");
		
		
		//opt CPU based GPU multiplication
		Kernel kernel = new Kernel() {
			@Override
			public void run() {
				int i = getGlobalId();
				for (int j = 0; j < uDimension; j++) {
					double val = 0;
					for (int k = 0; k < uDimension; k++) {
						val += u[j][k] * v[uDimension * i + k];
					}
					res4[uDimension * i + j] = val;
				}
			}
		};
		Range range = Range.create(iDimension);
		System.out.println("Starting opt CPU based GPU computation");
		time = System.currentTimeMillis();
		kernel.execute(range); // Running the Kernel
		System.out.println("Task finished in " + (System.currentTimeMillis() - time) + "ms");
		
		
	}
	/*
	public static void main(String[] args) {
		int iDimension = 1 << 4;
		int uDimension = 2;
		
		int vDimension = iDimension * uDimension;
		
		double[][] u = new double[uDimension][uDimension];
		double[][] ITensorU = new double[vDimension][vDimension];		
		double[] v = new double[vDimension];
		
		double[] res1 = new double[vDimension];
		double[] res3 = new double[vDimension];
		
		//u
		for (int i = 0; i < uDimension; i++) {
			for (int j = 0; j < uDimension; j++) {
				u[i][j] = (Math.random() * 100);
			}
		}
		//ITensorU
		for (int i = 0; i < iDimension; i++) {
			ITensorU[i * uDimension][i * uDimension] = u[0][0];
			ITensorU[i * uDimension][i * uDimension + 1] = u[0][1];
			ITensorU[i * uDimension + 1][i * uDimension] = u[1][0];
			ITensorU[i * uDimension + 1][i * uDimension + 1] = u[1][1];
		}
		//v
		for (int i = 0; i < vDimension; i++) {
			v[i] = (Math.random() * 10);
		}
		
		long time = System.currentTimeMillis();
		//original CPU multiplication
		System.out.println("Starting original CPU computation");
		for (int i = 0; i < vDimension; i++) {
			double val = 0;
			for (int j = 0; j < vDimension; j++) {
				val += ITensorU[i][j] * v[j];
			}
			res1[i] = val;
		}
		System.out.println("Task finished in " + (System.currentTimeMillis() - time) + "ms");
		
		//original CPU based GPU multiplication
		Kernel kernel = new Kernel() {
			@Override
			public void run() {
				int row = getGlobalId();
				double val = 0;
				for (int j = 0; j < vDimension; j++) {
					val += ITensorU[row][j] * v[j];
				}
				res3[row] = val;
			}
		};
		Range range = Range.create(vDimension);
		System.out.println("Starting original CPU based computation");
		time = System.currentTimeMillis();
		kernel.execute(range); // Running the Kernel
		System.out.println("Task finished in " + (System.currentTimeMillis() - time) + "ms");

	}
	
	*/
}
