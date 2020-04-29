package operater;

import Quantum.MatrixOP;
import Quantum.Qubit;

import org.jscience.mathematics.number.Complex;
import org.jscience.mathematics.vector.ComplexMatrix;
import org.jscience.mathematics.vector.ComplexVector;

import util.UnaryGate;

//H Bob1
public class UnaryGateOperater {
    public static void excute(ComplexMatrix matrix, Qubit qs, int loc) {
    	//optBefore(matrix, qs, loc);
    	optAfter(matrix, qs, loc);
    }
    
    public static void optBefore(ComplexMatrix matrix, Qubit qs, int loc) {
    	//Qbtis-->矩阵
        ComplexMatrix temp = qs.vector();
        // 得到状态是几量子比特
        int number = qs.number();
        // 单位矩阵
        ComplexMatrix identyMatrix = UnaryGate.I;

        //张量后的矩阵
        ComplexMatrix tensorMatrix = matrix;

        // 使得测量矩阵与单位矩阵张量
        int i = 0;
        while (i < loc) {
            tensorMatrix = identyMatrix.tensor(tensorMatrix);
            i++;
        }
//        //如果没有左边张量
//        if(tensorMatrix == null){
//            tensorMatrix = matrix;
//        }
        while (i < number-1) {
            tensorMatrix = tensorMatrix.tensor(identyMatrix);
            i++;
        }

        ComplexMatrix temp1 = tensorMatrix.times(temp);
        //更新qs
        qs.matrixToQbits(temp1);
    }
    
    public static void optAfter(ComplexMatrix matrix, Qubit qs, int loc) {
    	//Qbtis-->向量
        ComplexVector complexVector = qs.qsToVector();
        
        // 量子比特数
        int quantumBits = qs.number();

        int IlDimension = (int) Math.pow(2, loc);
        int IrDimension = (int) Math.pow(2, quantumBits - 1 - loc);
        
        Complex[][] uGateArr = MatrixOP.complexMatrixToComplexArr(matrix);
        Complex[] qsArr = MatrixOP.complexVectorToComplexArr(complexVector);
        		
        MatrixOP.ITensorUTensorI(IlDimension, IrDimension, uGateArr, qsArr);
        
        qs.setPossibles(qsArr);
    }
}
