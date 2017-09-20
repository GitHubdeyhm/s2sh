package learn.test.thread;

import java.util.ArrayList;
import java.util.List;

/**
 * 线程的等待通知方法使用
 * @Date 2017-4-7下午3:23:07
 */
public class WaitNotifyTest {
	
	public static final List<Integer> list = new ArrayList<>();
	
	public static void main(String[] args) throws InterruptedException {
		//测试等待和通知
		Object lock = new Object();
		WaitNotifyInnerA a = new WaitNotifyInnerA(lock);
		a.start();
		
		Thread.sleep(100);
		
		//当线程调用wait()方法处于等待状态时调用线程的interrupt()方法会抛出InterruptedException异常。
		//a.interrupt();
		
		new Thread(new WaitNotifyInnerB(lock)).start();
		
		System.out.println("main执行完毕------");
	}
}

class WaitNotifyInnerA extends Thread {
	
	private Object lock;//将该对象当做锁
	
	public WaitNotifyInnerA(Object lock) {
		this.lock = lock;
	}
	
	@Override
	public void run() {
		//模拟等待
		synchronized (lock) {
			if (WaitNotifyTest.list.size() != 7) {
				try {
					System.out.println("开始等待："+System.currentTimeMillis());
					//调用wait()方法后会让该线程会立即释放锁并停止执行，当接到通知后需要重新竞争获取锁才执行wait()后面的代码。
					lock.wait();
					System.out.println("结束等待："+System.currentTimeMillis());
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}
	}
}
class WaitNotifyInnerB implements Runnable {
	
	private Object lock;//将该对象当做锁
	
	public WaitNotifyInnerB(Object lock) {
		this.lock = lock;
	}
	
	@Override
	public void run() {
		//模拟等待
		synchronized (lock) {
			for (int i = 0; i < 10; i++) {
				WaitNotifyTest.list.add(i);
				if (WaitNotifyTest.list.size() == 7) {
					System.out.println("开始发出通知----");
					//notify()方法在调用后该线程不会立即释放锁。需要等待该同步方法或者同步代码块执行完后才会释放同步锁
					lock.notify();
					System.out.println("发送通知完成----");
				}
				System.out.println("i="+i);
			}
		}
	}
}
