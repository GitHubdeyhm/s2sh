package learn.test.thread;

/**
 * 线程类Thread常用方法测试类
 * @Date 2017-4-4下午1:35:27
 */
public class ThreadMethodTest extends Thread {
	
	//构造方法
	public ThreadMethodTest() {
		System.out.println("构造方法调用Thread.currentThread()--"+Thread.currentThread());
		//此时并不是设置的线程名称，是默认的线程名
		System.out.println("构造方法调用this.getName()--"+this.getName());
		System.out.println("构造方法调用Thread.currentThread().getName()--"+Thread.currentThread().getName());
	}
	
	@Override
	public void run() {
		System.out.println("run方法调用Thread.currentThread()--"+Thread.currentThread());
		//此时的线程名称才是代码设置的线程名称
		System.out.println("run方法调用this.getName()--"+this.getName());
		System.out.println("run方法调用Thread.currentThread().getName()--"+Thread.currentThread().getName());
		System.out.println(this == Thread.currentThread());
		
		int i = 0;
		while (i < 10000) {
			i++;
			if (this.isInterrupted()) {
				System.out.println("-------中断了线程------");
				break;
			}
			System.out.println("i="+i);
		}
		System.out.println("run------------------"+Thread.interrupted());//true
		System.out.println("run------------------"+Thread.interrupted());//false
		System.out.println("---线程执行结束------");
	}
	
	public static void main(String[] args) throws InterruptedException {
		//Thread.currentThread()和this关键字调用方法是有差异的
		ThreadMethodTest test = new ThreadMethodTest();
		System.out.println("线程状态："+test.getState());
		test.setName("ThreadMethodTest");
		test.start();
		//new Thread(new ThreadMethodTest(), "ThreadMethodTest").start();
		
		Thread.sleep(1);
		//调用了interrupt()方法并不是立即就终止了线程，只是相当于做了一个中断的标记。
		test.interrupt();
		System.out.println("---执行了interrupt()方法----");

		//interrupted()方法测试当前线程是否中断，即使test.interrupted()也返回false，因为
		//这里的当前线程是main线程
		System.out.println("main-----test.interrupted()--"+test.interrupted());//false,当前线程是main线程
		System.out.println("main---Thread.interrupted()--"+Thread.interrupted());//false,当前线程是main线程
		
		System.out.println("main-----test.isInterrupted()--"+test.isInterrupted());//true
		System.out.println("main-----test.isInterrupted()--"+test.isInterrupted());//true
		
		System.out.println("---执行完main方法----");
		System.out.println("线程状态："+test.getState());
	}

}
