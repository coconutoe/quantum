package test;

import org.jscience.mathematics.number.Complex;
import org.jscience.mathematics.vector.ComplexMatrix;

import Quantum.MatrixOP;

import util.ConjugateTranspose;
import util.UnaryGate;




public class Test {
    public static final ComplexMatrix U(double a, double b, double c) {

        double temp = b + c;
        double temp1 = b - c;



        Complex temp3 = cx(Math.cos(temp/2)/Math.cos(a/2),Math.sin(temp/2)/Math.cos(a/2));
        return ComplexMatrix.valueOf(new Complex[][]{
                {cx(Math.cos(a / 2) * Math.cos(temp / -2), Math.cos(a / 2) * Math.sin(temp / -2)),
                        cx(-Math.sin(a / 2) * Math.cos(temp1 / -2), -Math.sin(a / 2) * Math.sin(temp1 / -2))},
                {cx(Math.sin(a / 2) * Math.cos(temp1 / 2), Math.sin(a / 2) * Math.sin(temp1 / 2)),
                        cx(Math.cos(a / 2) * Math.cos(temp / 2), Math.cos(a / 2) * Math.sin(temp / 2))}
        }).times(temp3);

    }

    private static Complex cx(double real, double imaginary) {
        return Complex.valueOf(real, imaginary);
    }

    public static  void  main(String[] args){
    	//ComplexMatrix identyMatrix = UnaryGate.I;
    	//ComplexMatrix hMatrix = UnaryGate.H;
    	System.out.println(System.getProperty("user.dir"));
    	//Complex[][] uGateArr = new Complex[][]{{Complex.valueOf(Math.sqrt(2) / 2, 0), Complex.valueOf(Math.sqrt(2) / 2, 0)},{Complex.valueOf(Math.sqrt(2) / 2, 0), Complex.valueOf(Math.sqrt(2) / (-2), 0)}};
    	//Complex[] qsArr = new Complex[]{Complex.valueOf(1, 0),Complex.valueOf(0, 0),Complex.valueOf(0, 0),Complex.valueOf(0, 0),Complex.valueOf(0, 0),Complex.valueOf(0, 0),Complex.valueOf(0, 0),Complex.valueOf(0, 0)};
    	//Complex[] qsArr = new Complex[]{Complex.valueOf(1, 0),Complex.valueOf(0, 0),Complex.valueOf(0, 0),Complex.valueOf(0, 0)};
    	
    	//MatrixOP.ITensorU(2, uGateArr, qsArr);
    	//MatrixOP.UTensorI(2, uGateArr, qsArr);
    	//MatrixOP.ITensorUTensorI(2, 2, uGateArr, qsArr);
    }
}
