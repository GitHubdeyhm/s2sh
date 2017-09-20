package learn.test.thread.lock;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * 通过锁Lock实现线程的同步
 * @Date 2017-4-10下午11:53:40
 */
public class LockTest extends Thread {
	
	//定义锁对象
	private final Lock lock = new ReentrantLock();
	
	//定义共享变量
	private int num = 5;
	
	private LockInnerService service;
	
	public LockTest() {
		
	}
	
	public LockTest(LockInnerService service) {
		this.service = service;
	}
	
	@Override
	public void run() {
		lock.lock();
		Condition con = lock.newCondition();
		con.signal();
		System.out.println("----获取锁成功---");
		System.out.println(Thread.currentThread()+"-----"+(num--));
		System.out.println("----释放锁---");
		//实现等待通知机制
		if (service != null) {
			service.await();
		}
		lock.unlock();
		
	}
	
	public static void main(String[] args) throws InterruptedException {
		LockTest test = new LockTest();
		new Thread(test).start();
		new Thread(test).start();
		new Thread(test).start();
		new Thread(test).start();
		new Thread(test).start();
		
		LockInnerService service = new LockInnerService();
		new LockTest(service).start();
		Thread.sleep(800);
		service.signal();
	}
}

class LockInnerService {
	
	private final Lock lock = new ReentrantLock();
	private final Condition con = lock.newCondition();
	
	public void await() {
		lock.lock();
		try {
			System.out.println("await时间："+System.currentTimeMillis());
			con.await();
			System.out.println("执行了await方法");
		} catch (InterruptedException e) {
			e.printStackTrace();
		} finally {
			lock.unlock();//确保释放锁
		}
	}
	
	public void signal() throws InterruptedException {
		lock.lock();
		try {
			System.out.println("signalAll时间："+System.currentTimeMillis());
			con.signalAll();
			System.out.println("执行了signalAll方法");
		} finally {
			lock.unlock();//确保释放锁
		}
	}
}
