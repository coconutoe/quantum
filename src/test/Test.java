package test;

import java.util.Arrays;
import org.jscience.mathematics.number.Complex;

public class Test {
    public static  void  main(String[] args){
    	char[] ch = new char[5];
    	Arrays.fill(ch, '0');
    	Complex c = Complex.ZERO;
    	System.out.println(c.equals(Complex.valueOf(0, 0)));
    	String s = "333";
    	s.equals("ee");
    	//ComplexMatrix identyMatrix = UnaryGate.I;
    	//ComplexMatrix hMatrix = UnaryGate.H;
    	//System.out.println(System.getProperty("user.dir"));
    	//Complex[][] uGateArr = new Complex[][]{{Complex.valueOf(Math.sqrt(2) / 2, 0), Complex.valueOf(Math.sqrt(2) / 2, 0)},{Complex.valueOf(Math.sqrt(2) / 2, 0), Complex.valueOf(Math.sqrt(2) / (-2), 0)}};
    	//Complex[] qsArr = new Complex[]{Complex.valueOf(1, 0),Complex.valueOf(0, 0),Complex.valueOf(0, 0),Complex.valueOf(0, 0),Complex.valueOf(0, 0),Complex.valueOf(0, 0),Complex.valueOf(0, 0),Complex.valueOf(0, 0)};
    	//Complex[] qsArr = new Complex[]{Complex.valueOf(1, 0),Complex.valueOf(0, 0),Complex.valueOf(0, 0),Complex.valueOf(0, 0)};
    	
    	//MatrixOP.ITensorU(2, uGateArr, qsArr);
    	//MatrixOP.UTensorI(2, uGateArr, qsArr);
    	//MatrixOP.ITensorUTensorI(2, 2, uGateArr, qsArr);
//    	int num = 100000000;
//    	int[] a = new int[num];
//    	a[0] = 3;a[1] = 2;a[2] = 43;a[3] = 6;a[4] = 2;
//    	int[] b = new int[num];
//    	b[0] = 34;b[1] = 22;b[2] = 4;b[3] = 6;b[4] = 1;
//    	int[] res = new int[num];
//    	long start1 = System.currentTimeMillis();
//    	for (int i = 0; i < num; i++) {
//    		res[i] = a[i] + b[i];
//    	}
//    	System.out.println(System.currentTimeMillis() - start1);
//    	
//    	Kernel kernel = new Kernel() {
//    		@Override
//    		public void run() {
//    			int i = getGlobalId();
//    			res[i] = a[i] + b[i];
//    		}
//    	};
//    	
//    	Range range = Range.create(res.length);
//    	long start2 = System.currentTimeMillis();
//    	kernel.execute(range);
//    	System.out.println(System.currentTimeMillis() - start2);
    	
    }
  

}
