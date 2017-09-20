package learn.frame.utils;

import javax.servlet.http.HttpServletRequest;

import learn.frame.common.Constant;
import learn.frame.common.UserSession;

import org.apache.struts2.ServletActionContext;

/**
 * 客户端请求工具类
 * @Date 2016-8-4 下午11:40:12
 */
public class RequestUtil {
	
	/**ajax请求参数名称*/
	public static final String AJAX_REQUEST_HEAD_NAME = "X-Requested-With";
	/**ajax请求头参数值*/
	public static final String AJAX_REQUEST_HEAD_VALUE = "XMLHttpRequest";
	
	/**
	 * 获取客户端IP地址
	 * @Date 2016-8-4下午11:39:06
	 * @return IP地址
	 */
	public static String getClientIP(HttpServletRequest request) {
//		HttpServletRequest request = getRequest();
		String ip = request.getHeader("x-forwarded-for");
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getHeader("Proxy-Client-IP");
		}
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getHeader("WL-Proxy-Client-IP");
		}
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getRemoteAddr();
		}
		return ip;		
	}
	
	/**
	 * 获取当前登录人的ID
	 * @Date 2016-8-6下午11:32:23
	 * @return 用户ID
	 */
	public static String getCurrentUserId() {
		UserSession userSess = (UserSession)getRequest().getSession().getAttribute(Constant.USER_SESSION);
		if (userSess != null) {
			return userSess.getUserId();
		}
		return null;
	}
	
	/**
	 * 判断一个请求是否是ajax请求。
	 * 1、对于当前的js框架的ajax请求都会发一个X-Requested-With请求头， 当值为XMLHttpRequest代表是ajax请求。
	 * 2、对于自己实现的ajax请求不会有X-Requested-With请求头，可以增加该请求头来实现区分ajax。
	 * @Date 2017-2-20下午9:46:47
	 * @return 是ajax请求返回true，否则为false
	 */
	public static boolean isAjaxRequest() {
		//获取请求头
		String ajaxRequest = getRequest().getHeader(AJAX_REQUEST_HEAD_NAME);
		//相等则代表是ajax请求
		if (AJAX_REQUEST_HEAD_VALUE.equals(ajaxRequest)) {
			return true;
		}
		return false;
	}
	
	/**
	 * 得到HttpServletRequest对象
	 * @Date 2016-7-18下午04:41:06
	 * @return
	 */
	private static HttpServletRequest getRequest() {
		return ServletActionContext.getRequest();		
	}
}
