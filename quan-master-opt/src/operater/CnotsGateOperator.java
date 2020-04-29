package operater;

import java.util.List;

import org.jscience.mathematics.number.Complex;
import org.jscience.mathematics.vector.ComplexMatrix;

import Quantum.Qubit;
import util.BinaryToTen;
import util.TenToBinary;

public class CnotsGateOperator {
	public static Qubit excute(Qubit qs, List<Integer> arguments) {
		//获得概率值
        Complex[] qbitP = qs.getPossibles();
        //长度
        int length = qs.Length();
        //copy用
        Complex[] qbitcopy = new Complex[length];
        
        int size = arguments.size();
        for(int i=0; i<length; i++) {
            String qbit = TenToBinary.ttb(i, length);
            char[] qbitChar = qbit.substring(1, qbit.length()-1).toCharArray();
            
            boolean control = true;
            for(int j = 0; j < size - 1; j++) {
            	if(qbitChar[arguments.get(j)] != '1') {
            		control = false;
            		break;
            	}
            }
            
            if(control) {

                if(qbitChar[arguments.get(size - 1)]=='1') {
                    qbitChar[arguments.get(size - 1)]='0';
                }else if(qbitChar[arguments.get(size - 1)]=='0'){
                    qbitChar[arguments.get(size - 1)]='1';
                }
            }

            double newLoc = BinaryToTen.btt(qbitChar);
            qbitcopy[(int) newLoc] = qbitP[i];

        }
        qs.setPossibles(qbitcopy);
        return qs;
	}
}
