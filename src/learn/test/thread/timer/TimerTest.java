package learn.test.thread.timer;

import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

import learn.frame.utils.DateUtil;

/**
 * 定时器测试
 * @Date 2017-4-18下午11:52:13
 */
public class TimerTest {
	
	//定义一个定时器，创建Timer类实际上会创建一个线程。
	private static final Timer timer = new Timer();
	
	public static void main(String[] args) throws InterruptedException {
		/*TimerTaskTest task = new TimerTaskTest();
		Calendar cal = Calendar.getInstance();
		//cal.setTime(new Date(100000000000L));//过去某个时间
		cal.setTime(new Date(cal.getTime().getTime()+5000));//5秒后
		System.out.println("执行任务的时间："+DateUtil.formateTime(cal.getTime()));
		//代码运行完实际上程序并没有终止，
		timer.schedule(task, cal.getTime(), 1000);*/
		
		/*Calendar cal = Calendar.getInstance();
		//cal.setTime(new Date(cal.getTime().getTime()+2000));//一秒后
		TimerTaskA taskA = new TimerTaskA();
		TimerTaskB taskB = new TimerTaskB();
		//如果同时执行多个任务则可能出现延迟的情况
		timer.schedule(taskA, cal.getTime(), 1000);
		timer.schedule(taskB, cal.getTime(), 1000);*/
		
		/*TimerTaskA taskA = new TimerTaskA();
		TimerTaskB taskB = new TimerTaskB();
		//如果同时执行多个任务则可能出现延迟的情况
		timer.schedule(taskA, 1000);
		timer.schedule(taskB, 1000, 1000);*/
		
		TimerTaskA taskA = new TimerTaskA();
		TimerTaskB taskB = new TimerTaskB();
		timer.scheduleAtFixedRate(taskA, 1000, 1000);
		timer.scheduleAtFixedRate(taskB, 1000, 1000);
		
	}
	
	static class TimerTaskA extends TimerTask {
		@Override
		public void run() {
			System.out.println("执行任务A时间："+DateUtil.formateTime(new Date()));
			try {
				//模拟长时间任务
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
	static class TimerTaskB extends TimerTask {
		@Override
		public void run() {
			System.out.println("执行任务B时间："+DateUtil.formateTime(new Date()));
			//timer.cancel();//取消定时器中的所有任务，结束程序
		}
	}

}
