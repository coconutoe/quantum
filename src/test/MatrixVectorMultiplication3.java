package test;

import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.ForkJoinTask;
import java.util.concurrent.RecursiveAction;

import test.MatrixVectorMultiplication2.SumTask;

public class MatrixVectorMultiplication3 {
	public static void main(String[] args) {
		
		// U 乘以 V
		int uDimension = 5000;
		int vDimension = uDimension;
		
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
		
		long time = System.currentTimeMillis();
		System.out.println("Starting original CPU computation");
		for (int i = 0; i < uDimension; i++) {
			double val = 0;
			for (int j = 0; j < uDimension; j++) {
				val += u[i][j] * v[j]; 
			}
			res2[i] = val;
		}
		System.out.println("Task finished in " + (System.currentTimeMillis() - time) + "ms");
	
		//fork/join
		ForkJoinPool pool = new ForkJoinPool(4);
		MatrixVectorMultiplication3 mvm = new MatrixVectorMultiplication3();
		ForkJoinTask<?> task = mvm.new SumTask(u, v, res4, uDimension, 0, uDimension - 1);
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
		int dimension; 
		
		public SumTask(double[][] u, double[] v, double[] res, int dimension, int start, int end) {
			this.u = u;
			this.v = v;
			this.res = res;
			
			this.start = start;
			this.end = end;
			
			this.dimension = dimension;
		}

		@Override
		protected void compute() {
			//如果计算量小于1000，那么分配一个线程执行if中的代码块，并返回执行结果
			if(end - start < dimension/4) {
				for (int i = start; i <= end; i++) {
					double val = 0;
					for (int j = 0; j < dimension; j++) {
						val += u[i][j] * v[j]; 
					}
					res[i] = val;
				}
			} else {
				SumTask task1 = new SumTask(u, v, res, dimension, start, (start + end) / 2);
				SumTask task2 = new SumTask(u, v, res, dimension, (start + end) / 2 + 1, end);
				//执行任务
				task1.fork();
				task2.fork();
				do {
					
				} while (!(task1.isDone() && task2.isDone()));
			}
		}
	}
}
