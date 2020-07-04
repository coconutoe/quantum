package test;

import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.ForkJoinTask;
import java.util.concurrent.RecursiveAction;
import java.util.concurrent.TimeUnit;

public class MatrixVectorMultiplication2 {
	
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
		
		
		//fork/join
		ForkJoinPool pool = new ForkJoinPool(4);
		MatrixVectorMultiplication2 mvm = new MatrixVectorMultiplication2();
		ForkJoinTask<?> task = mvm.new SumTask(u, v, res4, iDimension, 0, iDimension - 1);
		long start = System.currentTimeMillis();
		System.out.println("Starting fork/join computation（递归方式）");
		pool.submit(task);
		do {
			
		} while (!task.isDone());
		//pool.shutdown();
		System.out.println("Task finished in " + (System.currentTimeMillis() - start) + "ms");
		
		
		start = System.currentTimeMillis();
		int n = 3;
		ForkJoinTask<?> task1 = mvm.new MatrxCalculate(u, v, res5, iDimension, 0, iDimension / n);
		ForkJoinTask<?> task2 = mvm.new MatrxCalculate(u, v, res5, iDimension, iDimension / n + 1, iDimension * 2 / n);
		ForkJoinTask<?> task3 = mvm.new MatrxCalculate(u, v, res5, iDimension, iDimension * 2 / n + 1, iDimension * 3 / n);
		//ForkJoinTask<?> task4 = mvm.new SumTask(u, v, res5, iDimension, iDimension * 3 / n + 1, iDimension * 4 / n);
		System.out.println("Starting fork/join computation（3个线程）");
		pool.submit(task1); pool.submit(task2); pool.submit(task3);
		do {
			
		} while (!(task1.isDone() && task2.isDone() && task3.isDone()));
		System.out.println("Task finished in " + (System.currentTimeMillis() - start) + "ms");
		for (int i = 0; i < vDimension; i++) {
			if (res2[i] != res5[i]) {
				System.out.println("error");
				return;
			}
		}
		
		start = System.currentTimeMillis();
		n = 4;
		ForkJoinTask<?> task4 = mvm.new MatrxCalculate(u, v, res5, iDimension, 0, iDimension / n);
		ForkJoinTask<?> task5 = mvm.new MatrxCalculate(u, v, res5, iDimension, iDimension / n + 1, iDimension * 2 / n);
		ForkJoinTask<?> task6 = mvm.new MatrxCalculate(u, v, res5, iDimension, iDimension * 2 / n + 1, iDimension * 3 / n);
		ForkJoinTask<?> task7 = mvm.new MatrxCalculate(u, v, res5, iDimension, iDimension * 3 / n + 1, iDimension * 4 / n);
		System.out.println("Starting fork/join computation（4个线程）");
		pool.submit(task4); pool.submit(task5); pool.submit(task6);pool.submit(task7);
		do {
			
		} while (!(task4.isDone() && task5.isDone() && task6.isDone() && task7.isDone()));
		//pool.shutdown();
		System.out.println("Task finished in " + (System.currentTimeMillis() - start) + "ms");
		res5[0] = 0.3;
		
		System.out.println("Starting fork/join computation（n个线程）");
		start = System.currentTimeMillis();
		n = 3;
		for (int i = 0; i < n; i++) {
			int st = (i == 0 ? 0 : i * iDimension / n + 1);
			int en = (i + 1) * iDimension / n;
			ForkJoinTask<?> taski = mvm.new MatrxCalculate(u, v, res5, iDimension, st, en);
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
	
	class MatrxCalculate extends RecursiveAction {
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
				SumTask task1 = new SumTask(u, v, res, iDimension, start, (start + end) / 3);
				SumTask task2 = new SumTask(u, v, res, iDimension, (start + end) / 3 + 1, (start + end) * 2 / 3);
				SumTask task3 = new SumTask(u, v, res, iDimension, (start + end) * 2 / 3 + 1, end);
				//执行任务
				task1.fork();
				task2.fork();
				task3.fork();
				do {
					
				} while (!(task1.isDone() && task2.isDone() && task3.isDone()));
			}
		}
	}
	
}
