package Quantum;

import java.util.Arrays;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.ForkJoinTask;
import java.util.concurrent.RecursiveAction;

import org.jscience.mathematics.number.Complex;
import org.jscience.mathematics.vector.ComplexMatrix;
import org.jscience.mathematics.vector.ComplexVector;

import com.aparapi.Kernel;
import com.aparapi.Range;

public class MatrixOperation {
	private int IlDimension; 
	private int IrDimension; 
	private Complex[][] uGateArr; 
	private Complex[] qsArr;
	private int uGateDimension;
	
	private int len;
	private Complex[] qsArrTemp;
	
	
	public MatrixOperation(int IlDimension, int IrDimension, Complex[][] uGateArr, Complex[] qsArr) {
		this.IlDimension = IlDimension;
		this.IrDimension = IrDimension;
		this.uGateArr = uGateArr;
		this.qsArr = qsArr;
		this.uGateDimension = uGateArr[0].length;
		this.len = IrDimension * uGateDimension;
		this.qsArrTemp = copyComplexArr(qsArr);
	}
	
	//I tensor U tensor I
	public void ITensorUTensorI() {
		for(int i = 0; i < IlDimension; i++) {
			Complex[] temp = intercept(qsArr, i * (IrDimension * uGateDimension), (i + 1) * (IrDimension * uGateDimension));
			
			//ThreadUTensorI(IrDimension, uGateArr, temp);
			UTensorI(IrDimension, uGateArr, temp);
			
			copyTo(temp, qsArr, 0, i * (IrDimension * uGateDimension));
		}
		
	}
	public void ITensorUTensorIMultiThread() {
		//fork/join
		int poolSize = 3;
		ForkJoinPool pool = new ForkJoinPool(poolSize);
		
		/*
		for (int i = 0; i < poolSize; i++) {
			int start = i * (IlDimension / poolSize);
			int end = (i + 1) * (IlDimension / poolSize);
			ForkJoinTask<?> taski = new MatrixCalculate(start, end);
			pool.submit(taski);
		}*/
		int dimension = qsArr.length;
		
		for (int i = 0; i < poolSize; i++) {
			int start = i * dimension / poolSize;
			int end = (i + 1) * dimension / poolSize;
			ForkJoinTask<?> taski = new MatrixCalculate(start, end);
			pool.submit(taski);
		}
		
		pool.shutdown();
		do {
			
		} while (!pool.isTerminated());
	}
	
	private class MatrixCalculate extends RecursiveAction {
		private static final long serialVersionUID = 1L;
		
		private int start;
		private int end;
		
		public MatrixCalculate(int start, int end) {
			this.start = start;
			this.end = end;
			
		}

		@Override
		protected void compute() {
			for (int index = start; index < end; index++) {
				// i 为左边 I 的第  i 行
				int i = index / (uGateDimension * IrDimension);
				
				// j 为uGate的第 j 行
				int j = index % (uGateDimension * IrDimension) / IrDimension;
				
				// k 为右边  I 的第  k 行
				int k = index % IrDimension;
				
				Complex val = Complex.ZERO;
				
				for(int uColIndex = 0; uColIndex < uGateDimension; uColIndex++) {
					val = val.plus(uGateArr[j][uColIndex].times(qsArrTemp[i * len + uColIndex * IrDimension + k]));
				}
				qsArr[index] = val;
				
			}
			
//			for(int i = start; i < end; i++) {
//				Complex[] temp = intercept(qsArr, i * len, (i + 1) * len);
//				UTensorI(IrDimension, uGateArr, temp);
//				copyTo(temp, qsArr, 0, i * len);
//			}
		}
		
	}
	
	private void UTensorIOpt() {
		Complex[] tempArr = copyComplexArr(qsArr);
		
		int uGateArrDimension = uGateArr[0].length;
		
		for(int i = 0; i < uGateArrDimension; i++) {
			for(int j = 0; j < IrDimension; j++) {
				Complex val = Complex.ZERO;
				for(int k = 0; k < uGateArrDimension; k++) {
					val = val.plus(uGateArr[i][k].times(tempArr[j + k * IrDimension]));
				}
				qsArr[i * IrDimension + j] = val;
			}
		}
		
	}
	
	//I tensor U tensor I
	public void ITensorUTensorIGpu() {
		int uGateDimension = uGateArr[0].length;
		
		for(int i = 0; i < IlDimension; i++) {
			Complex[] temp = intercept(qsArr, i * (IrDimension * uGateDimension), (i + 1) * (IrDimension * uGateDimension));
			
			UTensorIGpu(IrDimension, uGateArr, temp);
			copyTo(temp, qsArr, 0, i * (IrDimension * uGateDimension));
		}
		
	}
	
	//截取
	public static Complex[] intercept(Complex[] arr, int start, int end) {
		Complex[] temp = new Complex[end - start];
		for(int i = start; i < end; i++) {
			temp[i - start] = arr[i].copy();
		}
		return temp;
	}
	
	//I tensor U
	public static void ITensorU(int IDimension, Complex[][] uGateArr, Complex[] qsArr) {
		
		int uGateDimension = uGateArr[0].length;
		
		for(int i = 0; i < IDimension; i++) {
			Complex[] temp = Arrays.copyOfRange(qsArr, i * uGateDimension, (i + 1) * uGateDimension);
			Complex[] res = matrixMulti(uGateArr, temp);
			copyTo(res, qsArr, i * uGateDimension, i * uGateDimension);
		}
		
	}
	
	public static void copyTo(Complex[] from, Complex[] to, int fromStartPoint, int toStartPoint) {
		for(int i = fromStartPoint, j = toStartPoint; i < from.length; i++, j++) {
			to[j] = from[i];
		}
	}
	
	//数组模拟矩阵乘
	public static Complex[] matrixMulti(Complex[][] uGateArr, Complex[] qsArr) {
		Complex[] res = new Complex[qsArr.length];
		for(int i = 0; i < uGateArr.length; i++) {
			Complex sum = Complex.ZERO;
			for(int j = 0; j < uGateArr[0].length; j++) {
				sum = sum.plus(uGateArr[i][j].times(qsArr[j]));
			}
			res[i] = sum.copy();
		}
		
		return res;
	}
	
	//U tensor I(m = IDimension,n = uGateArrDimension)
	public static void UTensorI(int IDimension, Complex[][] uGateArr, Complex[] qsArr) {
		Complex[] tempArr = copyComplexArr(qsArr);
		
		int uGateArrDimension = uGateArr[0].length;
		
		for(int i = 0; i < uGateArrDimension; i++) {
			for(int j = 0; j < IDimension; j++) {
				Complex val = Complex.ZERO;
				for(int k = 0; k < uGateArrDimension; k++) {
					val = val.plus(uGateArr[i][k].times(tempArr[j + k * IDimension]));
				}
				qsArr[i * IDimension + j] = val;
			}
		}
		
	}
	
	public static void UTensorIGpu(int IDimension, Complex[][] uGateArr, Complex[] qsArr) {
		Complex[] tempArr = copyComplexArr(qsArr);
		int uGateArrDimension = uGateArr[0].length;
		
		Kernel kernel = new Kernel() {
			@Override
			public void run() {
				int globalId = getGlobalId();
				System.out.println(globalId);
				
				int i = globalId / IDimension;
				int j = globalId % IDimension;
				
				Complex val = Complex.ZERO;
				for(int k = 0; k < uGateArrDimension; k++) {
					val = val.plus(uGateArr[i][k].times(tempArr[j + k * IDimension]));
				}
				qsArr[i * IDimension + j] = val;
			}
		};
		
		// Array size for GPU to know
		Range range = Range.create(uGateArrDimension * IDimension);
		kernel.execute(range);
	}
	
	//多线程,CPU多线程貌似不可行
	public static void ThreadUTensorI(int IDimension, Complex[][] uGateArr, Complex[] qsArr) {
		Complex[] tempArr = copyComplexArr(qsArr);
		
		//Thread th1 = new LinearAlgebraCalThread(0, IDimension/4, uGateArr, qsArr, tempArr, IDimension);
		//Thread th2 = new LinearAlgebraCalThread(IDimension/4, IDimension/2, uGateArr, qsArr, tempArr, IDimension);
		//Thread th3 = new LinearAlgebraCalThread(IDimension/2, (IDimension/4)*3, uGateArr, qsArr, tempArr, IDimension);
		//Thread th4 = new LinearAlgebraCalThread((IDimension/4)*3, IDimension, uGateArr, qsArr, tempArr, IDimension);
		int uGateArrDimension = uGateArr[0].length;
		
		ExecutorService executorService = Executors.newFixedThreadPool(4); 
		
//		executorService.execute(th1);
//		executorService.execute(th2);
//		executorService.execute(th3);
//		executorService.execute(th4);
		
		for (int i = 0; i < uGateArrDimension; i++) {
			Thread th = new LinearAlgebraCalThread(uGateArr, qsArr, tempArr, IDimension, i);
			executorService.execute(th);
		}
		 
		executorService.shutdown();
		
		while (true) { 
			if(executorService.isTerminated()){
				break;
			}
		}
		
	}
	
	//复制一个一维Complex数组
	public static Complex[] copyComplexArr(Complex[] complexArr) {
		Complex[] tempArr = new Complex[complexArr.length];
		for(int i = 0; i < complexArr.length; i++) {
			tempArr[i] = complexArr[i].copy();
		}
		
		return tempArr;
	}
	
	//ComplexMatrix转换为二维Complex数组
	public static Complex[][] complexMatrixToComplexArr(ComplexMatrix complexMatrix) {
		int rowDimension = complexMatrix.getNumberOfRows();
		int colDimension = complexMatrix.getNumberOfColumns();
		
		Complex[][] complexArr = new Complex[rowDimension][colDimension];
		
		for(int i = 0; i < rowDimension; i++) {
			for(int j = 0; j < colDimension; j++) {
				complexArr[i][j] = complexMatrix.get(i, j);
			}
		}
				
		return complexArr;
	}
	
	
	//ComplexVector转换为一维Complex数组
	public static Complex[] complexVectorToComplexArr(ComplexVector complexVector) {
		int dimension = complexVector.getDimension();
		
		Complex[] complexArr = new Complex[dimension];
		
		for(int i = 0; i < dimension; i++) {
			complexArr[i] = complexVector.get(i);
		}
				
		return complexArr;
	}
	
	private static class LinearAlgebraCalThread extends Thread {
		//private int start;
		//private int end;
		private int i;
		private int IDimension;
		private int uGateArrDimension;
		private Complex[][] uGateArr;
		private Complex[] qsArr;
		private Complex[] tempArr;
		
		public LinearAlgebraCalThread(Complex[][] uGateArr, Complex[] qsArr, Complex[] tempArr, int IDimension,  int i) {
			this.i = i;
			this.uGateArr = uGateArr;
			this.qsArr = qsArr;
			this.tempArr = tempArr;
			
			uGateArrDimension = uGateArr[0].length;
			this.IDimension = IDimension;
		}

//		@Override
//		public void run() {
//			for(int i = 0; i < uGateArrDimension; i++) {
//				for(int j = start; j < end; j++) {
//					Complex val = Complex.ZERO;
//					for(int k = 0; k < uGateArrDimension; k++) {
//						val = val.plus(uGateArr[i][k].times(tempArr[j + k * IDimension]));
//					}
//					qsArr[i * IDimension + j] = val;
//				}
//			}
//		}
		
		@Override
		public void run() {
			
			for(int j = 0; j < IDimension; j++) {
				System.out.println(i);
				Complex val = Complex.ZERO;
				for(int k = 0; k < uGateArrDimension; k++) {
					val = val.plus(uGateArr[i][k].times(tempArr[j + k * IDimension]));
				}
				qsArr[i * IDimension + j] = val;
			}
			
			
		}
	}
}
