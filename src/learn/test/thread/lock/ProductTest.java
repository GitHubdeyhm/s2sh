package learn.test.thread.lock;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @Date 2017-4-11下午11:40:49
 */
public class ProductTest {

	public static void main(String[] args) {
		//实现交替打印的效果
		ProductInnerService service = new ProductInnerService();
		new ProductInnerA(service).start();
		new ProductInnerB(service).start();
	}
}

class ProductInnerService {
	
	private final Lock lock = new ReentrantLock();
	//创建两个Condition对象
	private final Condition con = lock.newCondition();
	private boolean hasValue = false;
	
	public void set() {
		lock.lock();
		try {
			while (hasValue) {
				con.await();
			}
			System.out.println("###################");
			hasValue = true;
			con.signal();
		} catch (InterruptedException e) {
			e.printStackTrace();
		} finally {
			lock.unlock();//确保释放锁
		}
	}
	
	public void get() {
		lock.lock();
		try {
			while (!hasValue) {
				con.await();
			}
			System.out.println("***************");
			hasValue = false;
			con.signal();
		} catch (InterruptedException e) {
			e.printStackTrace();
		} finally {
			lock.unlock();//确保释放锁
		}
	}
}

class ProductInnerA extends Thread {
	private ProductInnerService service;
	
	public ProductInnerA(ProductInnerService service) {
		this.service = service;
	}
	
	@Override
	public void run() {
		for (int i = 0; i < Integer.MAX_VALUE; i++) {
			service.set();
		}
	}
}

class ProductInnerB extends Thread {
	private ProductInnerService service;
	
	public ProductInnerB(ProductInnerService service) {
		this.service = service;
	}
	
	@Override
	public void run() {
		for (int i = 0; i < Integer.MAX_VALUE; i++) {
			service.get();
		}
	}
}
