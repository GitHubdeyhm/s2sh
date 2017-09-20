package learn.test.thread.lock;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * 通过Condition接口实现指定唤醒某个线程
 * @Date 2017-4-11下午11:24:18
 */
public class ConditionTest {
	
	public static void main(String[] args) throws InterruptedException {
		ConditionInnerService service = new ConditionInnerService();
		new ConditionInnerA(service).start();
		new ConditionInnerB(service).start();
		
		Thread.sleep(1000);
		service.signalA();//只唤醒线程A
	}

}
class ConditionInnerService {
	
	private final Lock lock = new ReentrantLock();
	//创建两个Condition对象
	private final Condition conA = lock.newCondition();
	private final Condition conB = lock.newCondition();
	
	public void awaitA() {
		lock.lock();
		try {
			System.out.println("awaitA时间："+System.currentTimeMillis());
			conA.await();
			System.out.println("执行了awaitA方法");
		} catch (InterruptedException e) {
			e.printStackTrace();
		} finally {
			lock.unlock();//确保释放锁
		}
	}
	
	public void awaitB() {
		lock.lock();
		try {
			System.out.println("awaitB时间："+System.currentTimeMillis());
			conB.await();
			System.out.println("执行了awaitB方法");
		} catch (InterruptedException e) {
			e.printStackTrace();
		} finally {
			lock.unlock();//确保释放锁
		}
	}
	
	public void signalA() throws InterruptedException {
		lock.lock();
		try {
			System.out.println("signalAll-A时间："+System.currentTimeMillis());
			conA.signalAll();
			System.out.println("执行了signalAll-A方法");
		} finally {
			lock.unlock();//确保释放锁
		}
	}
	
	public void signalB() throws InterruptedException {
		lock.lock();
		try {
			System.out.println("signalAll-B时间："+System.currentTimeMillis());
			conB.signalAll();
			System.out.println("执行了signalAll-B方法");
		} finally {
			lock.unlock();//确保释放锁
		}
	}
}

class ConditionInnerA extends Thread {
	private ConditionInnerService service;
	
	public ConditionInnerA(ConditionInnerService service) {
		this.service = service;
	}
	
	@Override
	public void run() {
		service.awaitA();
	}
}

class ConditionInnerB extends Thread {
	private ConditionInnerService service;
	
	public ConditionInnerB(ConditionInnerService service) {
		this.service = service;
	}
	
	@Override
	public void run() {
		service.awaitB();
	}
}
