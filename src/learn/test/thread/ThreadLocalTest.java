package learn.test.thread;

/**
 * ThreadLocal类用法
 * @Date 2017-4-8下午11:40:12
 */
public class ThreadLocalTest {
	
	public static final ThreadLocal<String> local = new ThreadLocal<String>(){
		@Override
		protected String initialValue() {
            return "初始值";
		}
	};
	
	public static void main(String[] args) throws InterruptedException {
		//验证虽然3个线程都向变量local里set数据值，但是每个线程依然能够取得各自的数据，保证了数据的隔离性
		new ThreadLocalInnerA().start();
		new ThreadLocalInnerB().start();
		
		for (int i = 0; i < 100; i++) {
			ThreadLocalTest.local.set("main-set-"+i);
			System.out.println("main-get-----"+ThreadLocalTest.local.get());
			try {
				Thread.sleep(200);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
}

class ThreadLocalInnerA extends Thread {
	
	@Override
	public void run() {
		for (int i = 0; i < 100; i++) {
			//ThreadLocalTest.local.set("ThreadLocalInnerA-set-"+i);
			System.out.println("ThreadLocalInnerA-get-----"+ThreadLocalTest.local.get());
			try {
				Thread.sleep(200);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
}

class ThreadLocalInnerB extends Thread {
	
	@Override
	public void run() {
		for (int i = 0; i < 100; i++) {
			ThreadLocalTest.local.set("ThreadLocalInnerB-set-"+i);
			System.out.println("ThreadLocalInnerB-get-----"+ThreadLocalTest.local.get());
			try {
				Thread.sleep(200);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
}
