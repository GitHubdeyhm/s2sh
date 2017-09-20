package learn.test.thread.timer;

import java.util.Date;
import java.util.TimerTask;

import learn.frame.utils.DateUtil;

/**
 * 任务类
 * @Date 2017-4-18下午11:53:46
 */
public class TimerTaskTest extends TimerTask {

	@Override
	public void run() {
		String now = DateUtil.formateTime(new Date());
		System.out.println("运行任务类："+now);
	}

}
