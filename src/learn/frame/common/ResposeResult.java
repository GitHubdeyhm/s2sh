package learn.frame.common;

import java.io.Serializable;

import learn.frame.utils.JSONUtil;

/**
 * 响应用户的请求结果类，给出用户操作结果的提示信息
 */
public class ResposeResult implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	/**失败代码*/
	public static final int ERROR_CODE = 0;
	/**成功代码*/
	public static final int SUCCESS_CODE = 1;
	/**警告代码*/
	public static final int WARN_CODE = 2;
	/**响应成功时的提示信息*/
	public static final String SUCCESS_MSG = "操作成功！";
	/**响应未知时的提示信息*/
	public static final String UNKNOWN_MSG = "操作失败:发生未知异常！";
	
	/**结果编码，只能是以上三种*/
	private int resultCode;
	/**结果信息*/
	private String msg;
	/**其它数据信息（比如跳转地址）*/
	private Object obj;
	
	public ResposeResult() {
		
	}
	
	public ResposeResult(int resultCode, String msg) {
		this.resultCode = resultCode;
		this.msg = msg;
	}
	
	public ResposeResult(int resultCode, String msg, Object obj) {
		this.resultCode = resultCode;
		this.msg = msg;
		this.obj = obj;
	}
	/**
	 * 默认操作成功的方法
	 */
	public static ResposeResult successResult() {
		return new ResposeResult(SUCCESS_CODE, SUCCESS_MSG);
	}
	/**
	 * 默认操作失败的方法
	 */
	public static ResposeResult errorResult() {
		return new ResposeResult(ERROR_CODE, UNKNOWN_MSG);
	}
	
	public int getResultCode() {
		return resultCode;
	}
	public void setResultCode(int resultCode) {
		this.resultCode = resultCode;
	}
	
	public String getMsg() {
		return msg;
	}
	public void setMsg(String msg) {
		this.msg = msg;
	}
	
	public Object getObj() {
		return obj;
	}
	public void setObj(Object obj) {
		this.obj = obj;
	}
	
	@Override
	public String toString() {
		return JSONUtil.toJSONStr(this);
	}
}
