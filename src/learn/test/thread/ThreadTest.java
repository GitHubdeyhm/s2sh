package learn.test.thread;

/**
 * @Date 2017-4-4下午1:35:27
 */
public class ThreadTest extends Thread {
	
	public ThreadTest() {
		System.out.println("ThreadTest--"+Thread.currentThread());
	}
	
	@Override
	public synchronized void run() {
		System.out.println("----开始运行-----");
		System.out.println("run--"+Thread.currentThread());
		try {
			this.wait(2000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		System.out.println("-----运行结束--------");
	}
	
	public static void main(String[] args) throws InterruptedException {
		ThreadTest test = new ThreadTest();
		System.out.println("new线程状态："+test.getState());
		test.start();
		System.out.println("线程状态："+test.getState());
		Thread.sleep(1000);
		System.out.println("线程状态："+test.getState());
	}
}
