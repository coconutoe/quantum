package test;

import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.ForkJoinTask;
import java.util.concurrent.RecursiveAction;
import java.util.concurrent.TimeUnit;

public class MatrixVectorMultiplication2 {
	
	public static void main(String[] args) throws InterruptedException {
		
		int iDimension = 1 << 24;
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
		
		
		//fork/join
		ForkJoinPool pool = new ForkJoinPool(4);
		MatrixVectorMultiplication2 mvm = new MatrixVectorMultiplication2();
		ForkJoinTask<?> task = mvm.new SumTask(u, v, res4, iDimension, 0, iDimension - 1);
		long start = System.currentTimeMillis();
		System.out.println("Starting fork/join computation");
		pool.submit(task);
		do {
			
		} while (!task.isDone());
		pool.shutdown();

		System.out.println("Task finished in " + (System.currentTimeMillis() - start) + "ms");
		System.out.println("end");
		
		
		for (int i = 0; i < vDimension; i++) {
			if (res2[i] != res4[i]) {
				System.out.println("error");
				return;
			}
		}
	}
	
	
	class SumTask extends RecursiveAction {
		private static final long serialVersionUID = 1L;
		
		double[][] u;
		double[] v;
		double[] res;
		
		int start;
		int end;
		int iDimension; 
		int uDimension; 
		
		public SumTask(double[][] u, double[] v, double[] res, int iDimension, int start, int end) {
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
			//如果计算量小于1000，那么分配一个线程执行if中的代码块，并返回执行结果
			if(end - start < iDimension/2) {
				for (int i = start; i <= end; i++) {
					for (int j = 0; j < uDimension; j++) {
						double val = 0;
						for (int k = 0; k < uDimension; k++) {
							val += u[j][k] * v[uDimension * i + k];
						}
						res[uDimension * i + j] = val;
					}
				}
			} else {
				SumTask task1 = new SumTask(u, v, res, iDimension, start, (start + end) / 2);
				SumTask task2 = new SumTask(u, v, res, iDimension, (start + end) / 2 + 1, end);
				//执行任务
				task1.fork();
				task2.fork();
				do {
					
				} while (!(task1.isDone() && task2.isDone()));
			}
		}
	}
	
}
