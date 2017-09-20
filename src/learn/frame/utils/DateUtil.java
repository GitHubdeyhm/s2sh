package learn.frame.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * 日期格式化工具类
 * @Date 2016-12-28下午9:12:23
 */
public class DateUtil {
	
	/**日期格式化字符串*/
	public static final String DATE_PATTERN = "yyyy-MM-dd";
	/**时间格式化字符串，不包括秒*/
	public static final String TIME_NOSECONDS_PATTERN = "yyyy-MM-dd HH:mm";
	/**时间格式化字符串，包括秒*/
	public static final String TIME_PATTERN = "yyyy-MM-dd HH:mm:ss";
	/**时分秒格式化字符串*/
	public static final String HOUR_MINUTE_SECONDS_PATTERN = "HH:mm:ss";
	/**毫秒格式化字符串*/
	public static final String MILLISECOND_PATTERN = "yyyyMMddHHmmssSSS";
	/**一周星期数组*/
	public static final String[] WEEKS_DAY = {"星期日", "星期一", "星期二", "星期三", "星期四", "星期五", "星期六"};
	/**格式化时间分隔符*/
	public static final String TIME_SEPARATOR = "-";
	
	
	/**
	 * 格式化时间，返回时间字符串格式，如果日期对象为null则返回null
	 * @param date：日期对象
	 * @return 格式化后的日期字符串：yyyy-MM-dd HH:mm:ss
	 */
	public static String formateTime(Date date) {
		if (date != null) {
			return new SimpleDateFormat(TIME_PATTERN).format(date);
		}
		return null;
	}
	
	/**
	 * 格式化时间，返回时间字符串格式，不包括秒，如果日期对象为null则返回null
	 * @Date 2016-7-19下午03:33:03
	 * @param date 日期对象
	 * @return 格式化后的日期字符串 yyyy-MM-dd HH:mm
	 */
	public static String formateTimeNoSeconds(Date date) {
		if (date != null) {
			return new SimpleDateFormat(TIME_NOSECONDS_PATTERN).format(date);
		}
		return null;
	}
	
	/**
	 * 格式化时间，返回日期字符串格式，如果日期对象为null则返回null
	 * @param date：日期值
	 * @return 格式化后的日期字符串：yyyy-MM-dd
	 */
	public static String formateDate(Date date) {
		if (date != null) {
			return new SimpleDateFormat(DATE_PATTERN).format(date);
		}
		return null;
	}
	
	/**
	 * 格式毫秒时间
	 * @Date 2017-1-10下午9:58:13
	 * @param date 日期值
	 * @return
	 */
	public static String formatMillisecond(Date date) {
		if (date != null) {
			return new SimpleDateFormat(MILLISECOND_PATTERN).format(date);
		}
		return null;
	}
	
	/**
	 * 根据字符串得到日期，字符串的格式为：yyyy-MM-dd HH:mm:ss或者yyyy-MM-dd HH:mm或者yyyy-MM-dd
	 * <p>如果字符为null或者不能解析为日期，则返回null</p>
	 * @Date 2016-5-14下午3:17:47
	 * @param timeStr 日期字符串
	 * @return 字符串表示的日期值
	 */
	public static Date parseTime(String timeStr) {
		if (!StringUtil.isNullOrEmpty(timeStr)) {
			try {
				int len = timeStr.length();
				if (len == 19) {
					//字符串格式：yyyy-MM-dd HH:mm:ss
					return new SimpleDateFormat(TIME_PATTERN).parse(timeStr);
				} else if (len == 16) {
					// yyyy-MM-dd HH:mm
					return new SimpleDateFormat(TIME_NOSECONDS_PATTERN).parse(timeStr);
				} else if (len == 10) {
					//字符串格式：yyyy-MM-dd
					return new SimpleDateFormat(DATE_PATTERN).parse(timeStr);
				}
			} catch (ParseException e) {
				e.printStackTrace();
			}
		}
		return null;
	}
	
	/**
	 * 根据日期对象得到某年某月的第一天代表的日期，格式为yyyy-MM-dd
	 * @Date 2016-8-27下午2:50:38
	 * @param date 日期对象
	 * @return 如果日期对象为空，则获取当前系统时间。返回某年某月的第一天
	 */
	public static String getMonthFirstDay(Date date) {
		Calendar cal = Calendar.getInstance();
		if (date != null) {
			cal.setTime(date);
		}
		int year = cal.get(Calendar.YEAR);
		int month = cal.get(Calendar.MONTH)+1;
		String monthStr = month < 10 ? ("0"+month) : String.valueOf(month);
		return year + TIME_SEPARATOR + monthStr + "-01";
	}
	
	/**
	 * 根据日期对象得到某年某月的最后一天代表的日期，格式为yyyy-MM-dd
	 * @Date 2016-8-27下午10:23:40
	 * @param date 日期对象
	 * @return 如果日期对象为空，则获取当前系统时间。返回某年某月的最后一天
	 */
	public static String getMonthLastDay(Date date) {
		Calendar cal = Calendar.getInstance();
		if (date != null) {
			cal.setTime(date);
		}
		int year = cal.get(Calendar.YEAR);
		int month = cal.get(Calendar.MONTH)+1;
		String monthStr = month < 10 ? ("0"+month) : String.valueOf(month);
		int lastDay = getDaysOfYM(date);//得到当月最后一天
		return year + TIME_SEPARATOR + monthStr + TIME_SEPARATOR + lastDay;
	}
	
	/**
	 * 根据日期得到年月，格式为yyyy-MM
	 * @Date 2016-9-24上午11:23:02
	 * @param date 日期对象 
	 * @return 年月，格式为yyyy-MM
	 */
	public static String getYearMonth(Date date) {
		Calendar cal = Calendar.getInstance();
		if (date != null) {
			cal.setTime(date);
		}
		int year = cal.get(Calendar.YEAR);
		int month = cal.get(Calendar.MONTH)+1;
		String monthStr = month < 10 ? ("0"+month) : String.valueOf(month);
		return year + TIME_SEPARATOR + monthStr;
	}
	
	/**
	 * 根据日期得到某年某月的最后日期，也可判断某年某月有多少天
	 * @param year 年
	 * @param month 月份
	 * @return 某年某月的天数
	 */
	public static int getDaysOfYM(Date date) {
		Calendar cal = Calendar.getInstance();
		if (date != null) {
			cal.setTime(date);
		}
		int year = cal.get(Calendar.YEAR);
		int month = cal.get(Calendar.MONTH)+1;
		if (month == 4 || month == 6 || month == 9 || month == 11) {
			return 30;
		} else if (month == 2) {
		    int days = 28;
		    //闰年的判断方法
		    if ((year % 400 == 0) || (year % 4 == 0 && year % 100 != 0)) {
		    	days = 29;
		    }
		    return days;
		}
		return 31;
	}
	/**
	 * 根据日期得到当天是星期几，返回格式为“星期几”
	 * @Date 2015-11-17下午11:10:06
	 * @param date 日期对象
	 * @return 如果日期对象为空则返回当前日期的“星期几”，否则返回格式为“星期几”
	 */
	public static String getWeekByDate(Date date) {
		Calendar cal = Calendar.getInstance();
		if (date != null) {
			cal.setTime(date);
		}
		int days = cal.get(Calendar.DAY_OF_WEEK);//一周的第几天：星期天为第一天，星期六为第七天
		return WEEKS_DAY[days-1];
	}
	/**
	 * 根据日期得到当天所属一周范围内的日期，日期格式为yyyy-MM-dd
	 * <p>说明：此方法获取的一周中星期一为第一天，星期天为一周的最后一天，升序输出</p>
	 * @Date 2015-11-17下午11:16:27
	 * @param date 日期对象
	 * @return 一周内的日期列表，星期一代表的日期在数组的第一个元素，星期天为数组的最后一个元素
	 */
	public static String[] getWeeksByDate(Date date) {
		Calendar cal = Calendar.getInstance();
		if (date != null) {
			cal.setTime(date);
		}
		int days = cal.get(Calendar.DAY_OF_WEEK);//一周的第几天：星期天为第一天，星期六为第七天
		if (days == 1) {//星期天
			cal.add(Calendar.DAY_OF_MONTH, -6);
		} else {
			cal.add(Calendar.DAY_OF_MONTH, 2-days);//保证从周一开始
		}
		String[] weekDays = new String[7];//保存周日期数组
		SimpleDateFormat sdf = new SimpleDateFormat(DATE_PATTERN);
		for (int i = 0; i < 7; i++) {
			weekDays[i] = sdf.format(cal.getTime());
			cal.add(Calendar.DAY_OF_MONTH, 1);//循环一次加一天
		}
		return weekDays;
	}
	
	/**
	 * 比较两个日期对象，如果d1大于d2返回1，d1小于d2返回-1，相等返回0
	 * @Date 2016-8-27下午1:37:24
	 * @param d1 日期对象
	 * @param d2 日期对象
	 * @return 如果日期对象任何一个为空则返回null
	 */
	public static Integer compareDate(Date d1, Date d2) {
		if (d1 == null || d2 == null) {
			return null;
		}
		if (d1.getTime() > d2.getTime()) {
			return 1;
		} else if (d1.getTime() < d2.getTime()) {
			return -1;
		} else {
			return 0;
		}
	}
	
	/**
	 * 得到相对于date对象前后天数的日期值
	 * @Date 2016-8-27下午2:00:15
	 * @param date 日期对象
	 * @param amount 天数
	 * @return 返回相对于date对象前后的天数的日期值，如果date对象为空则相对于当前时间
	 */
	public static Date getOffsetDay(Date date, int amount) {
		return getOffsetDate(date, Calendar.DAY_OF_MONTH, amount);
	}
	
	/**
	 * 得到相对于date对象前后月数的日期值
	 * @Date 2016-9-16下午9:34:13
	 * @param date 日期对象
	 * @param amount 天数
	 * @return 返回相对于date对象前后的月数的日期值，如果date对象为空则相对于当前时间
	 */
	public static Date getOffsetMonth(Date date, int amount) {
		return getOffsetDate(date, Calendar.MONTH, amount);
	}
	
	/**
	 * 得到相对于date对象的偏移量
	 * @Date 2016-8-27下午1:52:42
	 * @param date 日期对象
	 * @param field 字段
	 * @param amount 数量
	 * @return 返回相对于date对象的偏移量的日期，如果date对象为空则相对于当前时间
	 */
	public static Date getOffsetDate(Date date, int field, int amount) {
		Calendar cal = Calendar.getInstance();
		if (date != null) {
			cal.setTime(date);
		}
		cal.add(field, amount);
		return cal.getTime();
	}
	
	public static void main(String[] args) throws Exception {
//		System.out.println(getTimeCode(""));
//		System.out.println(getTimeCode("sj"));
//		System.out.println(getTimeCode("sf"));
//		System.out.println(getTimeCode("xt"));
//		
		Calendar cal = Calendar.getInstance();
		cal.setTime(getOffsetDay(null, 120));
		System.out.println(cal.get(Calendar.YEAR)+"---"+cal.get(Calendar.MONTH));
		
		System.out.println(getWeekByDate(null));
		System.out.println(getDaysOfYM(new Date()));
//		System.out.println(getWeeksByDay(new Date()));
		
		System.out.println(getWeeksByDate(parseTime("2017-02-06")));
		
		
		
	}
}
