package learn.test.thread;

/**
 * 线程出现异常的处理测试
 * @Date 2017-4-14上午12:02:10
 */
public class ThreadExceptionTest extends Thread {
	
	@Override
	public void run() {
		System.out.println(3/0);//制造一个异常
	}

	public static void main(String[] args) {
		ThreadExceptionTest test = new ThreadExceptionTest();
		//为线程类ThreadExceptionTest所有的线程对象设置默认的异常处理方式
		ThreadExceptionTest.setDefaultUncaughtExceptionHandler(new UncaughtExceptionHandler() {
			@Override
			public void uncaughtException(Thread t, Throwable e) {
				System.out.println("默认异常处理方式");
			}
		});
		test.setName("有异常的线程");
		//对出现的线程异常手动处理，应该在start()方法前调用
		test.setUncaughtExceptionHandler(new UncaughtExceptionHandler() {
			@Override
			public void uncaughtException(Thread t, Throwable e) {
				System.out.println(t.getName()+e);
			}
		});
		test.start();
		System.out.println(test.getUncaughtExceptionHandler());
		//没有进行异常处理的线程--采用默认异常处理方式
		Thread t = new Thread(new ThreadExceptionTest());
		t.start();
		System.out.println(t.getUncaughtExceptionHandler());
		System.out.println("--main---");
	}
}
