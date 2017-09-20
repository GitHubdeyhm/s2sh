package learn.test.program;

import java.util.ArrayList;

/**
 * @Date 2017-5-10下午12:20:40
 */
public class ProgramTest {
	public static void main(String[] args) {
		/*int a = 100;
		float b = 99.99f;
		long c = 2000L;
		
		System.out.println(a/3);//33
		System.out.println(3%2);//1
		System.out.println(b/0);//Infinity
		
		System.out.println(new ArrayList<String>().getClass() == new ArrayList<Integer>().getClass());*/
	
		
		int num_212 = 0B11010100;
		System.out.println(num_212);
		int num_a=0b10000000000000000000000000000011;
		System.out.println(num_a);
		long num_aa = 0b10000000000000000000000000000011L;
		System.out.println(num_aa);
		
		double num_b = 521E2;//是double类型
		System.out.println(num_b / 0);//Infinity
		System.out.println(-3.14/0);//-Infinity
		System.out.println(0.0/0.0);//NaN
		System.out.println((num_b/0) == (3.14/0));
		int x = 100_00;
		System.out.println(x);
		double y = 100.00_234;
		System.out.println(y);
		
		int a = 12;
		double da = a;
		System.out.println(da);
		byte ba = (byte)384;
		System.out.println(ba);
		byte c = (byte)0b1000_0001;
		System.out.println(c);
		
		
		int d = (int)32.66f;
		System.out.println(d);

	}

}
