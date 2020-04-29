package Quantum;

import java.util.Arrays;
import org.jscience.mathematics.number.Complex;
import org.jscience.mathematics.vector.ComplexMatrix;
import org.jscience.mathematics.vector.ComplexVector;

public class MatrixOP {
	
	//I tensor U tensor I
	public static void ITensorUTensorI(int IlDimension, int IrDimension, Complex[][] uGateArr, Complex[] qsArr) {
		int uGateDimension = uGateArr[0].length;
		
		for(int i = 0; i < IlDimension; i++) {
			Complex[] temp = intercept(qsArr, i * (IrDimension * uGateDimension), (i + 1) * (IrDimension * uGateDimension));
			UTensorI(IrDimension, uGateArr, temp);
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
}
