package operater;

import org.jscience.mathematics.number.Complex;
import org.jscience.mathematics.vector.ComplexMatrix;

import Quantum.Qubit;
import util.BinaryToTen;
import util.TenToBinary;

public class CcnotGateOperator {
	//toffoli门可用其他门组合出来
	public static Qubit excute(ComplexMatrix matrix, Qubit qs, int loc1,int loc2,int loc3) {
		//获得概率值
        Complex[] qbitP = qs.getPossibles();
        //长度
        int length = qs.Length();
        //copy用
        Complex[] qbitcopy = new Complex[length];
        for(int i=0; i<length; i++) {
            String qbit = TenToBinary.ttb(i, length);
            char[] qbitChar = qbit.substring(1, qbit.length()-1).toCharArray();

            if(qbitChar[loc1]=='1' && qbitChar[loc2]=='1') {

                if(qbitChar[loc3]=='1') {
                    qbitChar[loc3]='0';
                }else if(qbitChar[loc3]=='0'){
                    qbitChar[loc3]='1';
                }
            }

            double newLoc = BinaryToTen.btt(qbitChar);
            qbitcopy[(int) newLoc] = qbitP[i];

        }
        qs.setPossibles(qbitcopy);
        return qs;
	}
}
