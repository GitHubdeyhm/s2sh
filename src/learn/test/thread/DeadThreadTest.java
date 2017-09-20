package learn.test.thread;

/**
 * 测试线程的死锁问题
 * @Date 2017-4-6下午4:31:23
 */
public class DeadThreadTest implements Runnable {
	
	private String userName;
	private Object lock1 = new Object();
	private Object lock2 = new Object();
	
	public void setUserName(String userName) {
		this.userName = userName;
	}

	@Override
	public void run() {
		if ("a".equals(userName)) {
			synchronized (lock1) {
				try {
					System.out.println("userName="+userName);
					Thread.sleep(5000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				synchronized (lock2) {
					System.out.println("执行顺序lock1->lock2完成--------");
				}
			}
		}
		if ("b".equals(userName)) {
			synchronized (lock2) {
				try {
					System.out.println("userName="+userName);
					Thread.sleep(5000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				synchronized (lock1) {
					System.out.println("执行顺序lock2->lock1完成--------");
				}
			}
		}
	}
	
	public static void main(String[] args) throws InterruptedException {
		//模拟死锁现象
		//通过jps查询进程ID然后jstack 进程ID可以查询程序是否有死锁现象
		DeadThreadTest test = new DeadThreadTest();
		test.setUserName("a");
		new Thread(test).start();
		Thread.sleep(100);
		test.setUserName("b");
		new Thread(test).start();
	}
}
