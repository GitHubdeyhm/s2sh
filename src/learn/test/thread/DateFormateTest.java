package learn.test.thread;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 用类SimpleDateFormat做为共享变量格式化日期会出现多线程问题
 * 类SimpleDateFormat非线程安全
 * @Date 2017-3-13下午9:34:28
 */
public class DateFormateTest extends Thread {

	//日期字符串
	private String dateStr;
	
	public DateFormateTest(String dateStr) {
		this.dateStr = dateStr;
	}
	
	@Override
	public void run() {
		new TestDateFormateInner().pareseDate(dateStr);
	}
	
	public static void main(String[] args) {
		String[] dates = new String[]{"2000-01-01", "2016-12-01","2016-12-31"};
		for (int i = 0; i < dates.length; i++) {
			new DateFormateTest(dates[0]).start();//创建多个线程：对于日期格式化及容易出现多线程问题
		}
	}
}
class TestDateFormateInner {
	//定义静态变量让多个线程共享
	public static final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
	
	/**
	 * 解析日期，如果SimpleDateFormat做为共享变量则在多线程环境下解析的日期
	 * 可能出现不正确或者抛出异常的情况。
	 * @Date 2017-3-13下午9:38:08
	 * @param dateStr
	 */
	public void pareseDate(String dateStr) {
		try {
			Date d = sdf.parse(dateStr);
			String fmtStr = sdf.format(d);
			if (!dateStr.equals(fmtStr)) {
				System.out.println("日期格式化错误："+Thread.currentThread()+"--将日期"+dateStr+"---》格式化为："+fmtStr);
			} else {
				System.out.println("日期格式化正确");
			}
		} catch (ParseException e) {
			e.printStackTrace();
		}
	}
}

