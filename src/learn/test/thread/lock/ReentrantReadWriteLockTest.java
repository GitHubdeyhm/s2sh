package learn.test.thread.lock;

import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * 读写锁测试类
 * @Date 2017-4-12下午3:29:19
 */
public class ReentrantReadWriteLockTest {
	
	public static void main(String[] args) {
		ReentrantReadWriteLockService service = new ReentrantReadWriteLockService();
		//测试读锁，由于不互斥，所以几乎是同时获取了读锁的。
//		new ReentrantReadWriteLockInnerA(service).start();
//		new ReentrantReadWriteLockInnerB(service).start();
		
		//测试写锁，互斥效果
//		new ReentrantReadWriteLockInnerC(service).start();
//		new ReentrantReadWriteLockInnerD(service).start();
		
		//测试读写锁，互斥效果
		new ReentrantReadWriteLockInnerA(service).start();
		new ReentrantReadWriteLockInnerC(service).start();
		
	}
}

class ReentrantReadWriteLockService {
	private final ReentrantReadWriteLock lock = new ReentrantReadWriteLock();
	
	//测试读锁
	public void read() {
		try {
			//由于是读锁，所以可以是多个线程获取lock()方法后面的代码
			lock.readLock().lock();
			System.out.println("获取了读锁："+System.currentTimeMillis());
			Thread.sleep(4000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		} finally {
			//多次调用readLock()获取锁对象返回的是同一个对象
			lock.readLock().unlock();
		}
	}
	
	//测试写锁
	public void write() {
		try {
			//由于是写锁，所以多个线程不可以同时访问lock()后面的代码
			lock.writeLock().lock();
			ReentrantReadWriteLock.WriteLock w1 = lock.writeLock();
			ReentrantReadWriteLock.WriteLock w2 = lock.writeLock();
			System.out.println(w1 == w2);
			System.out.println("获取了写锁："+System.currentTimeMillis());
			Thread.sleep(4000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		} finally {
			//多次调用writeLock()获取锁对象返回的是同一个对象
			lock.writeLock().unlock();
		}
	}
}

class ReentrantReadWriteLockInnerA extends Thread {
	
	private ReentrantReadWriteLockService service;
	
	public ReentrantReadWriteLockInnerA(ReentrantReadWriteLockService service) {
		this.service = service;
	}
	@Override
	public void run() {
		service.read();
	}
}

class ReentrantReadWriteLockInnerB extends Thread {
	private ReentrantReadWriteLockService service;
	
	public ReentrantReadWriteLockInnerB(ReentrantReadWriteLockService service) {
		this.service = service;
	}
	@Override
	public void run() {
		service.read();
	}
}

class ReentrantReadWriteLockInnerC extends Thread {
	
	private ReentrantReadWriteLockService service;
	
	public ReentrantReadWriteLockInnerC(ReentrantReadWriteLockService service) {
		this.service = service;
	}
	@Override
	public void run() {
		service.write();
	}
}

class ReentrantReadWriteLockInnerD extends Thread {
	private ReentrantReadWriteLockService service;
	
	public ReentrantReadWriteLockInnerD(ReentrantReadWriteLockService service) {
		this.service = service;
	}
	@Override
	public void run() {
		service.write();
	}
}

