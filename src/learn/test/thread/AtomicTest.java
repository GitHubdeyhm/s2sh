package learn.test.thread;

import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

/**
 * 原子类操作测试
 * @Date 2017-4-7下午2:13:24
 */
public class AtomicTest extends Thread {
	
	//定义一个原子整数类
	private AtomicInteger count = new AtomicInteger(0);
	
	//定义一个原子类，说明原子类并不是总是线程安全的。
	public AtomicLong notSafe = new AtomicLong();

	@Override
	public void run() {
		//线程安全
		for (int i = 0; i < 1000; i++) {
			System.out.println(count.incrementAndGet());
		}
		
		//模拟对变量notSafe线程不安全的情况
		System.out.println(Thread.currentThread()+"---"+notSafe.addAndGet(1000));
		//再次调用原子方法
		notSafe.addAndGet(1);
	}
	
	public static void main(String[] args) throws InterruptedException {
		AtomicTest test = new AtomicTest();
		//多个线程访问共享的原子类对象count是线程安全的
		new Thread(test).start();
		new Thread(test).start();
		new Thread(test).start();
		new Thread(test).start();
		new Thread(test).start();
		new Thread(test).start();
		new Thread(test).start();
		
		Thread.sleep(1000);
		
		System.out.println("多次调用原子方法在多线程环境下的值："+test.notSafe.get());
	}
}
