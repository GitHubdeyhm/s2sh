package learn.test.thread;


/**
 * @Date 2017-4-6下午1:44:42
 */
public class ThreadMethodTest2 extends Thread {

	@Override
	public void run() {
		System.out.println("---运行开始--");
		for (int i = 0; i < 2000; i++) {
			System.out.println("i="+i);
		}
		try {
			Thread.sleep(5000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		System.out.println("---运行结束--"+this.getPriority());
	}
	
	public static void main(String[] args) throws InterruptedException {
		//Thread.currentThread().setPriority(8);
		ThreadMethodTest2 test = new ThreadMethodTest2();
		test.setName("ThreadMethodTest2");
		test.start();
		
		Thread.sleep(100);
		//调用了interrupt()方法并不是立即就终止了线程，只是相当于做了一个中断的标记。
		//test.interrupt();
		System.out.println("-----main优先级-----"+Thread.currentThread().getPriority());
		
		ThreadB b = new ThreadB();
		Thread a = new Thread(new ThreadA(b));
		a.start();
		b.start();
		b.join(2000);
		System.out.println("----main结束-----");
	}	
}

class ThreadA implements Runnable {
	
	private ThreadB b;
	
	public ThreadA(ThreadB b) {
		this.b = b;
	}

	@Override
	public void run() {
		synchronized (b) {
			try {
				System.out.println("开始执行ThreadA--------");
				Thread.sleep(5000);
				System.out.println("结束执行ThreadA--------");
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
}

class ThreadB extends Thread {

	@Override
	public synchronized void run() {
		try {
			System.out.println("开始执行ThreadB--------");
			Thread.sleep(5000);
			System.out.println("结束执行ThreadB--------");
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
}
