package util;

import java.util.List;

public class Function {
	public static int max(int a, int b, int c) {
	    int temp = (a > b) ? a : b;
	    return (temp > c) ? temp : c;
	}
	
	public static int min(int a, int b, int c) {
	    int temp = (a < b) ? a : b;
	    return (temp < c) ? temp : c;
	}
	
	public static int maxElement(List<Integer> Controlsl) {
		int maxElement = Controlsl.get(0);
		for(int i = 1; i < Controlsl.size(); i++) {
			if(Controlsl.get(i) > maxElement) {
				maxElement = Controlsl.get(i);
			}
		}
		
		return maxElement;
	}
	
	public static int minElement(List<Integer> Controlsl) {
		int maxElement = Controlsl.get(0);
		for(int i = 1; i < Controlsl.size(); i++) {
			if(Controlsl.get(i) < maxElement) {
				maxElement = Controlsl.get(i);
			}
		}
		
		return maxElement;
	}
}
