package learn.test.jvm;

import java.util.ArrayList;
import java.util.List;

/**
 * 模拟内存溢出各种错误
 * @Date 2017-1-1下午10:00:14
 */
public class TestJVM {
	
	private int stackLength = 0;
	
	public void stackLeak() {
		stackLength++;
		stackLeak();
	}
	
	/**
	 * @Date 2017-1-1下午10:42:59
	 * @param args
	 * @throws Throwable
	 */
	/**
	 * @Date 2017-1-1下午10:43:18
	 * @param args
	 * @throws Throwable
	 */
	public static void main(String[] args) throws Throwable {
		//1、产生堆内存溢出
		//jvm参数：-verbose:gc -Xms100M -Xmx100M
		//Exception in thread "main" java.lang.OutOfMemoryError: Java heap space
		/*List<TestThread> list = new ArrayList<TestThread>();
		while(true) {
			list.add(new TestThread());
			System.out.println(list.size());
		}*/
		
		//2、栈溢出
		//Exception in thread "main" java.lang.StackOverflowError
		/*TestJVM tj = new TestJVM();
		try {
			tj.stackLeak();
		} catch (Throwable e) {
			System.out.println("调用方法的次数stackLength="+tj.stackLength);
			throw e;
		}*/
		//
		//方法区内存溢出
		//jdk6抛出Exception in thread "main" java.lang.OutOfMemoryError: PermGen space
		List<String> list = new ArrayList<String>();
		int i = 0;
		while(true) {
			list.add(String.valueOf(i).intern());
			i++;
		}
		
		
	}
}
