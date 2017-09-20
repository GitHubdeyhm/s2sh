package learn.test.thread.lock;

import java.util.concurrent.locks.ReentrantLock;

/**
 * ReentrantLock对象的方法测试
 * @Date 2017-4-12下午2:11:10
 */
public class ReentrantLockMethodTest {
	
	public static void main(String[] args) throws InterruptedException {
		final ReentrantLockMethodInnerService service = new ReentrantLockMethodInnerService();
		Runnable run = new Runnable() {
			@Override
			public void run() {
				service.waitMethod();
			}
		};
		new Thread(run).start();
		Thread.sleep(500);
		Thread th = new Thread(run);
		th.start();
		//中断线程，在调用lockInterruptibly()方法时出现异常
		th.interrupt();
		System.out.println("main结束");
	}

}
class ReentrantLockMethodInnerService {
	
	private final ReentrantLock lock = new ReentrantLock();
	
	public void waitMethod() {
		try {
			//lock.lock();
			lock.lockInterruptibly();
			System.out.println("已加锁："+Thread.currentThread());
			for (int i = 0; i < Integer.MAX_VALUE/10; i++) {
				Math.random();
			}
			System.out.println("加锁结束："+Thread.currentThread());
		} catch (InterruptedException e) {
			e.printStackTrace();
		} finally {
			if (lock.isHeldByCurrentThread()) {
				lock.unlock();
			}
		}
	}
}
