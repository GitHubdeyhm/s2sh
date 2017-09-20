package learn.frame.utils;

import java.util.Date;

import learn.frame.common.Constant;
import learn.frame.exception.ServiceException;

/**
 * 生成编码工具类
 */
public class CodeUtil {
	
	/**格式化字符串，3位一级，不足补0*/
	private static final String[] CODE_FORMATE = {"%1$03d", "999"};
	
	/**
	 * 根据最大子编码和父编码自动生成编码的方法
	 * <p>生成规则：编码3位一级，编码的取值范围为：001--999</p>
	 * <p>当编码超出范围时返回null，提示编码超出最大范围</p>
	 */
	public static String generateCode(String parentCode, String maxChildCode) {
		String maxCode = "0";
		if (!StringUtil.isNullOrEmpty(maxChildCode)) {
			maxCode = maxChildCode.substring(maxChildCode.length()-Constant.CODE_LEVEL_LENGTH);
		}
		int codeVal = Integer.parseInt(maxCode)+1;
		//编码超出了最大范围，返回null
		if (codeVal > Integer.parseInt(CODE_FORMATE[1])) {
			//System.out.println("编码超出范围");
			throw new ServiceException("编码超出最大值范围");
		}
		StringBuffer codeBuf = new StringBuffer();
		if (!StringUtil.isNullOrEmpty(parentCode)) {
			codeBuf.append(parentCode);
		}
		codeBuf.append(String.format(CODE_FORMATE[0], codeVal));
		return codeBuf.toString();
	}
	
	/**
	 * 常用于通过当前时间自动生成编码，需精确到毫秒，可以通过加一个前缀表明
	 * @param preffix ：编码前缀
	 * @return 编码
	 */
	public static String getTimeCode(String preffix) {
		return preffix + DateUtil.formatMillisecond(new Date());
	}
	
	public static void main(String[] args) {
		System.out.println(generateCode("", null));
		System.out.println(generateCode(null, null));
		System.out.println(generateCode(null, "919"));
		System.out.println(generateCode(null, "001"));
		System.out.println(generateCode(null, "301"));
		System.out.println(generateCode(null, "999"));
		System.out.println("-----------------------");
		System.out.println(generateCode("001002", ""));
		System.out.println(generateCode("001002", null));
		System.out.println(generateCode("001002", "001002001"));
		System.out.println(generateCode("001002", "001002198"));
		System.out.println(generateCode("001002", "001002998"));
		System.out.println(generateCode("001002", "001002999"));
	}
}
