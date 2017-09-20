package learn.test.thread;

/**
 * 此类测试当在调用System.out.println()方法时使用“i--”在多线程环境下是非线程安全的
 * @Date 2017-4-4下午1:19:57
 */
public class PrintlnTest extends Thread {
	//定义共享变量i
	private int i = 5;
	
	@Override
	public void run() {
		/**
		 * 在System.out.println()方法内部直接写“i--”是非线程安全的。
		 * 尽管println方法内部是线程安全的，但是“i--”的操作是在System.out.println()方法前执行的，所以
		 * 出现线程安全问题。
		 */
		System.out.println("i="+(i--)+"---"+Thread.currentThread());
	}
	
	public static void main(String[] args) {
		//共享test对象
		PrintlnTest test = new PrintlnTest();
		new Thread(test).start();
		new Thread(test).start();
		new Thread(test).start();
		new Thread(test).start();
		new Thread(test).start();
	}
}
