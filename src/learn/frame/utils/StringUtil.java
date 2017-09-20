package learn.frame.utils;

import java.util.UUID;

/**
 * 字符串处理工具类
 * @Date 2016-8-4 下午11:32:07
 */
public class StringUtil {
	
	/**
	 * hibernate的hql语句需要转义的特殊字符。
	 * 对于jdbc方式的sql语句待测试。
	 */
	private static final String hqlEscape = "_,%"; 
	
	/**
	 * <p>判断一个字符串是否为空,例：</p>
	 *     ""       true
	 *     "   "    true
	 *     null     true
	 *     "12  "   false
	 * @Date 2016-8-4下午11:33:25
	 * @param str 字符串对象
	 * @return
	 */
	public static boolean isNullOrEmpty(String str) {
		if (str == null) {
			return true;
		}
		return "".equals(str.trim());
	}
	/**
	 * 判断一个字符串数组中是否存在至少一个元素为空、""、"  "。存在返回true，否则为false。
	 * @Date 2016-8-4下午11:33:25
	 * @param str 字符串数组
	 * @return 存在空返回true，否则为false。
	 */
	public static boolean isAnyNullOrEmpty(String... str) {
		for (String s : str) {
			if ((s == null) || (s.trim().length() == 0)) {
				return true;
			}
		}
		return false;
	}
	/**
	 * 将hql特殊字符转义,默认转义字符为：\，可以通过escape关键字指定转义字符，escape关键字
	 * 只能用于模糊查询（like），不适用=号。
	 * 				特殊字符： _、%
	 * @Date 2016-8-4下午11:33:25
	 * @param escapeStr ：要转义的字符串
	 * @return ：转义后的字符串
	 */
	public static String hqlEscape(String escapeStr) {
		if (!isNullOrEmpty(escapeStr)) {
			//得到要转义的sql特殊字符
			for (String es : hqlEscape.split(",")) {
				escapeStr = escapeStr.replace(es, "/"+es);
			}
		}
		return escapeStr;
	}
	/**
	 * 得到随机生成的32位UUID值
	 * @Date 2016-8-4下午11:33:25
	 * @return ：32位的随机字符串值
	 */
	public static String getUUID() {
		 UUID uuid = UUID.randomUUID();
		 return uuid.toString().replace("-", "");
	}
	
	public static void main(String[] args) {
		System.out.println(hqlEscape("/&&_%%"));
		
		System.out.println(isNullOrEmpty(""));
		System.out.println(isNullOrEmpty(null));
		System.out.println(isNullOrEmpty("   "));
		System.out.println(isNullOrEmpty("sdf"));
		System.out.println(isNullOrEmpty(" dsf s  "));
		
	    System.out.println(getUUID());
	    
	    String s = "from dd from";
	    System.out.println(s.substring(s.indexOf("from")));
	}
}
