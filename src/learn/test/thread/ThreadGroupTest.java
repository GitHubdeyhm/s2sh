package learn.test.thread;


/**
 * 线程组测试
 * @Date 2017-4-13下午3:28:44
 */
public class ThreadGroupTest {

	public static void main(String[] args) {
		//main线程所属线程组名称为main
		System.out.println(Thread.currentThread().getThreadGroup().getParent().getName());
		ThreadGroupService service = new ThreadGroupService();
		ThreadGroup group = new ThreadGroup("线程组");
		ThreadGroup childGroup = new ThreadGroup(group, "子线程组");
		ThreadGroupInnerA a = new ThreadGroupInnerA(service);
		ThreadGroupInnerB b = new ThreadGroupInnerB(service);
		ThreadGroupInnerC c = new ThreadGroupInnerC(service);
		ThreadGroupInnerD d = new ThreadGroupInnerD(service);
		Thread ta = new Thread(group, a);
		ta.start();
		new Thread(group, b).start();
		new Thread(group, c).start();
		new Thread(childGroup, d).start();
		
		System.out.println("线程组："+group.activeCount()+"--"+group.activeGroupCount()+"--"+
				childGroup.getParent().getName());
		
		System.out.println("main:"+Thread.currentThread().getThreadGroup().activeCount());
		
		System.out.println(Thread.activeCount());
		
		System.out.println("----main结束----");
	}
}

class ThreadGroupService {
	
} 

class ThreadGroupInnerA extends Thread {
	
	private ThreadGroupService service;
	
	public ThreadGroupInnerA(ThreadGroupService service) {
		this.service = service;
	}
	@Override
	public void run() {
		System.out.println("a---");
		int s = 2/0;
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
}
class ThreadGroupInnerB extends Thread {
	
	private ThreadGroupService service;
	
	public ThreadGroupInnerB(ThreadGroupService service) {
		this.service = service;
	}
	@Override
	public void run() {
		System.out.println("b---");
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
}
class ThreadGroupInnerC extends Thread {
	
	private ThreadGroupService service;
	
	public ThreadGroupInnerC(ThreadGroupService service) {
		this.service = service;
	}
	@Override
	public void run() {
		System.out.println("c---");
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
}
class ThreadGroupInnerD extends Thread {
	
	private ThreadGroupService service;
	
	public ThreadGroupInnerD(ThreadGroupService service) {
		this.service = service;
	}
	@Override
	public void run() {
		System.out.println("d---");
	}
}