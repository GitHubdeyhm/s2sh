package learn.test.thread;

/**
 * 线程组异常测试：实现当线程组中某个线程出现异常时，该线程组中的其它线程也停止运行的效果
 * @Date 2017-4-14上午12:34:58
 */
public class ThreadGroupExceptionTest extends Thread {
	
	@Override
	public void run() {
		System.out.println("这是一个死循环线程");
		int i = 0;
		while (this.isInterrupted() == false) {
			System.out.println(Thread.currentThread().getName()+"---i="+i);
		}
	}
	
	public static void main(String[] args) {
		ThreadGroup group = new ChlidThreadGroup("线程组");
		for (int i = 0; i < 5; i++) {
			new Thread(group, new ThreadGroupExceptionTest(), i+"").start();
		}
		//在线程组中添加一个产生异常的线程
		new Thread(group, new ExceptionThread(), "异常线程").start();
	}
}

//重写线程组类
class ChlidThreadGroup extends ThreadGroup {
	/**
	 * 提供一个有线程组名称的构造方法
	 * @Date 2017-4-14上午12:36:58
	 * @param name 线程组名称
	 */
	public ChlidThreadGroup(String name) {
		super(name);
	}
	
	//重写异常处理方法，实现自定义异常处理方式
	@Override
	public void uncaughtException(Thread t, Throwable e) {
		super.uncaughtException(t, e);
		this.interrupt();
	}
}

class ExceptionThread extends Thread {
	@Override
	public void run() {
		System.out.println("这是一个会产生异常的线程");
		int num = 2 /0;//产生异常
	}
}