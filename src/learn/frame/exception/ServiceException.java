package learn.frame.exception;

import java.util.HashMap;
import java.util.Map;

import learn.frame.utils.file.PropertiesUtil;

/**
 * 自定义异常类，实现业务逻辑的异常提示
 * @Date 2016-11-20上午11:16:26
 */
public class ServiceException extends RuntimeException {

	private static final long serialVersionUID = 1L;
	//缓存页面提示信息
	private static final Map<String, String> MESSAGE_CACHE = new HashMap<String, String>();
	
	/**
	 * 无参数构造方法
	 */
	public ServiceException() {
		super();
	}
	
	public ServiceException(String message) {
		super(message);
	}
	
	public ServiceException(Throwable cause) {
		super(cause);
	}
	
	public ServiceException(String message, Throwable cause) {
		super(message, cause);
	}
	
	/**
	 * 向页面提示信息。
	 * 先从缓存MESSAGE_CACHE中获取页面提示信息，如果值为null则从配置文件中获取
	 */
	@Override
	public String getMessage() {
		String key = super.getMessage();
		String message = MESSAGE_CACHE.get(key);
		if (message == null) {
			message = PropertiesUtil.getPropertiesValueByKey("message-zh_CN", key);
			MESSAGE_CACHE.put(key, message);
		}
		return message;
	}
}
