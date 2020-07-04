package test;

import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.ForkJoinTask;
import java.util.concurrent.RecursiveAction;


public class MultiThreadTest {
	
	public static void main(String[] args) throws InterruptedException {
		
		int iDimension = 1 << 23;
		int uDimension = 2;
		int vDimension = iDimension * uDimension;
		
		double[][] u = new double[uDimension][uDimension];
		double[] v = new double[vDimension];
		
		double[] res2 = new double[vDimension];
		double[] res4 = new double[vDimension];
		double[] res5 = new double[vDimension];
			
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
		
		
		MultiThreadTest mtt = new MultiThreadTest();
		//fork/join
		long start = System.currentTimeMillis();
		
		ForkJoinPool pool = new ForkJoinPool(3);
		
		System.out.println("Starting fork/join computation（n个线程）");
		start = System.currentTimeMillis();
		int n = 4;
		for (int i = 1; i < n; i++) {
			//int st = (i == 0 ? 0 : i * iDimension / n + 1);
			int st = i * iDimension / n + 1;
			int en = (i + 1) * iDimension / n;
			ForkJoinTask<?> taski = mtt.new MatrxCalculate(u, v, res4, iDimension, st, en);
			pool.submit(taski);
		}
		pool.shutdown();
		mtt.matrixMultiVector(u, v, res4, iDimension, 0, iDimension / n);
		
		do {
			
		} while (!pool.isTerminated());
		System.out.println("Task finished in " + (System.currentTimeMillis() - start) + "ms");
		

		System.out.println("Starting fork/join computation（n个线程）");
		start = System.currentTimeMillis();
		pool = new ForkJoinPool(3);
		n = 3;
		for (int i = 0; i < n; i++) {
			int st = (i == 0 ? 0 : i * iDimension / n + 1);
			int en = (i + 1) * iDimension / n;
			ForkJoinTask<?> taski = mtt.new MatrxCalculate(u, v, res5, iDimension, st, en);
			pool.submit(taski);
		}
		pool.shutdown();
		do {
			
		} while (!pool.isTerminated());
		System.out.println("Task finished in " + (System.currentTimeMillis() - start) + "ms");
		
		
		System.out.println("end");
		for (int i = 0; i < vDimension; i++) {
			if (res2[i] != res5[i]) {
				System.out.println("error");
				return;
			}
		}
	}
	
	private void matrixMultiVector(double[][] u, double[] v, double[] res, int iDimension, int start, int end) {
		int uDimension = u.length;
		for (int i = start; i <= end; i++) {
			for (int j = 0; j < uDimension; j++) {
				double val = 0;
				for (int k = 0; k < uDimension; k++) {
					val += u[j][k] * v[uDimension * i + k];
				}
				res[uDimension * i + j] = val;
			}
		}
	}
	
	
	private class MatrxCalculate extends RecursiveAction {
		private static final long serialVersionUID = 1L;
		
		double[][] u;
		double[] v;
		double[] res;
		
		int start;
		int end;
		int iDimension; 
		int uDimension; 
		
		public MatrxCalculate(double[][] u, double[] v, double[] res, int iDimension, int start, int end) {
			this.u = u;
			this.v = v;
			this.res = res;
			
			this.start = start;
			this.end = end;
			
			this.iDimension = iDimension;
			this.uDimension = u.length;
		}

		@Override
		protected void compute() {
			matrixMultiVector(u, v, res, iDimension, start, end);
		}
	}
	
}
