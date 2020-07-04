package operater;

import Quantum.Qubit;
import org.jscience.mathematics.number.Complex;
import org.jscience.mathematics.vector.ComplexMatrix;
import util.BinaryGate;
import util.BinaryToTen;
import util.TenToBinary;


public class BinaryGateOperator {
	public static void excute(ComplexMatrix matrix, Qubit qs, int loc1,int loc2) {
		//excuteOrg(matrix, qs, loc1, loc2);
		excuteOpt(matrix, qs, loc1, loc2);
	}
	
    public static void excuteOrg(ComplexMatrix matrix, Qubit qs, int loc1,int loc2) {
        if(matrix.equals(BinaryGate.CNOT)){
            //获得概率值
            Complex[] qbitP = qs.getPossibles();
            //长度
            int length = qs.Length();
            //copy用
            Complex[] qbitcopy = new Complex[length];
            for(int i=0; i<length; i++) {
                String qbit = TenToBinary.ttb(i, length);
                char[] qbitChar = qbit.substring(1, qbit.length()-1).toCharArray();
            	if(qbitChar[loc1]=='1') {
                    if(qbitChar[loc2]=='1') {
                        qbitChar[loc2]='0';
                    }else if(qbitChar[loc2]=='0'){
                        qbitChar[loc2]='1';
                    }
                }
                double newLoc = BinaryToTen.btt(qbitChar);
                qbitcopy[(int)newLoc] = qbitP[i];
            }
            qs.setPossibles(qbitcopy);
        }
    }
    
    public static void excuteOpt(ComplexMatrix matrix, Qubit qs, int loc1,int loc2) {
        if(matrix.equals(BinaryGate.CNOT)){
            //获得概率值
            Complex[] qbitP = qs.getPossibles();
            //长度
            int length = qs.Length();
            //量子比特数
            int qubits = qs.number();
                    
            Complex[] qbitcopy = new Complex[length];
            for(int i = 0; i < length; i++) {
            	int newLoc = i;
            	if (((i >> (qubits - loc1 - 1)) & 1) == 1) {
            		int moveNum = qubits - loc2 - 1;
            		if (((i >> moveNum) & 1) == 1) {
            			newLoc -= (1 << moveNum);
                	} else {
                		newLoc += (1 << moveNum);
                	}
            	}
            	qbitcopy[newLoc] = qbitP[i];
            }
            qs.setPossibles(qbitcopy);
        }
    }
}
