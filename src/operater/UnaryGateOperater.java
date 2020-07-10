package operater;

import Quantum.MatrixOperation;
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

        int IlDimension = 1 << loc;
        int IrDimension = 1 << (quantumBits - 1 - loc);
        
        Complex[][] uGateArr = MatrixOperation.complexMatrixToComplexArr(matrix);
        Complex[] qsArr = MatrixOperation.complexVectorToComplexArr(complexVector);
        
        MatrixOperation matrixOperation = new MatrixOperation(IlDimension, IrDimension, uGateArr, qsArr);
        
        if (quantumBits < 20) {
        	matrixOperation.ITensorUTensorI();
        } else {
        	matrixOperation.ITensorUTensorIMultiThread();
        }
        
        qs.setPossibles(qsArr);
    }
    
}
