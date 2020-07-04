package test;

import java.util.Arrays;
import java.util.Deque;
import java.util.LinkedList;
import java.util.Queue;

import org.jscience.mathematics.number.Complex;
import org.jscience.mathematics.vector.ComplexMatrix;

import com.aparapi.Kernel;
import com.aparapi.Range;


import util.ConjugateTranspose;
import util.UnaryGate;


public class Test {
//	static {
//		System.loadLibrary("OpenCL64");
//	}
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
    	//new Test().validPalindrome("eeccccbebaeeabebccceea");
    	new Test().surfaceArea(new int[][] {{2}});
    }
  
    public int surfaceArea(int[][] grid) {
        int res = 0;

        int m = grid.length, n = grid[0].length;
        for (int i = 0; i < m; i++) {
            for (int j = 0; j < n; j++) {
                int t = grid[i][j];
                res += (t << 2  + (grid[i][j] == 0 ? 0 : 2));
                if (i > 0) {
                    res -= Math.min(grid[i][j], grid[i - 1][j]);
                }
                if (i < m - 1) {
                    res -= Math.min(grid[i][j], grid[i + 1][j]);
                }

                if (j > 0) {
                    res -= Math.min(grid[i][j], grid[i][j - 1]);
                }

                if (j < n - 1) {
                    res -= Math.min(grid[i][j], grid[i][j + 1]);
                }
            }
        }

        return res;
    }
}
