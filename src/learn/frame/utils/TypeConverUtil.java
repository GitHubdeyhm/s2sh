package learn.frame.utils;

import java.math.BigDecimal;

/**
 * 类型转换工具
 * @Date 2016-3-12 下午3:22:46
 */
public class TypeConverUtil {
	/**
	 * 对象转换成字符串，如果对象为null则返回null
	 * @Date 2016-3-9下午10:11:44
	 * @param obj
	 * @return
	 */
	public static String objToNullStr(Object obj) {
		return obj == null ? null : obj.toString();
	}
	
	/**
	 * 对象转换成字符串，如果对象为null，则返回""空字符串
	 * @Date 2016-3-9下午10:13:12
	 * @param obj
	 * @return
	 */
	public static String objToEmptyStr(Object obj) {
		return obj == null ? "" : obj.toString();
	}
	
	/**
	 * 对象转换成int型，如果对象为null则返回0
	 * @Date 2016-3-12下午3:24:59
	 * @param obj
	 * @return
	 */
	public static int objToInt(Object obj) {
		return obj == null ? 0 : Integer.parseInt(obj.toString());
	}
	/**
	 * 对象转换成long型，如果对象为null则返回0
	 * @Date 2016-3-12下午3:24:59
	 * @param obj
	 * @return
	 */
	public static long objToLong(Object obj) {
		return obj == null ? 0L : Long.parseLong(obj.toString());
	}
	
	/**
	 * 字符串转成BigDecimal对象
	 * @Date 2016-8-13下午3:28:04
	 * @param str 字符串
	 * @return 如果字符串为空或者null则返回null，或者创建一个BigDecimal对象
	 */
	public static BigDecimal strToBigDecimal(String str) {
		return StringUtil.isNullOrEmpty(str) ? null : new BigDecimal(str);
	}
}
