package learn.frame.utils.collection;

import java.util.ArrayList;
import java.util.List;

/**
 * List集合工具类
 * @Date 2017-3-11下午10:33:41
 */
public class ListUtil {
	
	/**默认分隔符为英文逗号*/
	private static final String DEFAULT_SEPARATOR = ",";

	/**
	 * 判断一个List是否为null或者大小为0
	 * @Date 2017-3-11下午10:36:51
	 * @param list List集合
	 * @return 如果list为null或者大小为0则返回true，否则返回false。
	 */
	public static boolean isNullOrEmpty(List<?> list) {
		if (list == null) {
			return true;
		}
		return (list.size() == 0) ? true : false;
	}
	
	/**
	 * 将一个字符串List集合拆分成带有指定分隔符的字符串，分隔符默认为英文逗号
	 * @Date 2017-4-5上午12:24:05
	 * @param list 字符串集合
	 * @return 带有分隔符的字符串，如果list集合为null或者大小为0返回空字符串
	 */
	public static String join(List<String> list) {
		return join(list, DEFAULT_SEPARATOR);
	}
	
	/**
	 * 将一个字符串List集合拆分成带有指定分隔符的字符串
	 * @Date 2017-4-5上午12:25:19
	 * @param list 字符串集合
	 * @param separator 分隔符，默认为英文逗号
	 * @return 带有分隔符的字符串，如果list集合为null或者大小为0返回空字符串
	 */
	public static String join(List<String> list, String separator) {
		if (isNullOrEmpty(list) || (separator == null)) {
			return "";
		}
		StringBuilder sb = new StringBuilder();
		for (String str : list) {
			sb.append(separator);
			sb.append(str);
		}
		return sb.substring(separator.length());
	}
	
	public static void main(String[] args) {
		List<String> strList = new ArrayList<>();
		strList.add("123");
		System.out.println(join(strList));
		strList.add("abc");
		strList.add("xyz");
		System.out.println(join(strList, "@#"));
	}
}
