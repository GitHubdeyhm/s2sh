package learn.frame.action;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import learn.frame.common.ResposeResult;
import learn.frame.utils.JSONUtil;

import org.apache.log4j.Logger;
import org.apache.struts2.ServletActionContext;

import com.opensymphony.xwork2.ActionSupport;

/**
 * 所有action的基类
 */
public class BaseAction extends ActionSupport {
	
	private static final long serialVersionUID = 1L;
	
	protected static final Logger log = Logger.getLogger(BaseAction.class);
	
	/**防止Struts2的开发模式下表单名称没设置造成的错误，其值是生成的随机数防止缓存*/
	private String _;
	//id主键
	private String id;
	
	/**
	 * 得到HttpServletRequest
	 */
	protected HttpServletRequest getRequest() {
		return ServletActionContext.getRequest();		
	}
	/**
	 * 得到HttpServletResponse
	 */
	protected HttpServletResponse getResponse() {
		return ServletActionContext.getResponse();
	}
	/**
	 * 取得HttpRequest中Parameter的简化方法.
	 */
	protected String getParameter(String name) {
		return getRequest().getParameter(name);
	}
	/**
	 * 取得HttpSession简化方法.
	 */
	protected HttpSession getSession() {
		return getRequest().getSession();
	}
	/**
	 * 以ajax响应请求数据
	 * @param content：数据内容
	 * @param type：返回的mime类型
	 */
	private void ajaxResponse(String content, String type) {
		PrintWriter out = null;
		try {
			HttpServletResponse response = getResponse();
			response.setContentType(type + ";charset=UTF-8");//设置相应mime类型
			//以下三条一起使用可以设置浏览器不缓存，并不是所有的浏览器都完全支持这三个响应头，所以最好同时使用
			response.setHeader("Pragma", "No-cache");
			response.setHeader("Cache-Control", "no-cache");//设置不缓存
			response.setDateHeader("Expires", 0);//设置过期时间
			out = response.getWriter();
			out.write(content);
			out.flush();
		} catch (IOException e) {
			log.error("响应流异常："+e.getMessage());
		} finally {
			if(out != null){
				out.close();
			}
		}
	}
	
	/**
	 * 以json的形式返回对象数据，该对象里不包含对Date对象的解析
	 * @Date 2016-8-20下午6:31:31
	 * @param obj 对象
	 */
	protected void renderJSON(Object obj) {
		renderJSON(obj, false);
	}
	
	/**
	 * 以json的形式返回对象数据，该对象里含有Date对象的解析
	 * 对于响应application/json格式，在IE下可能是响应一个json文件
	 * @Date 2016-8-20下午6:31:31
	 * @param obj 对象
	 * @param isParseDate 解析Date对象
	 */
	protected void renderJSON(Object obj, boolean isParseDate) {
		String jsonStr = JSONUtil.toJSONStr(obj, isParseDate);
		ajaxResponse(jsonStr, "application/json");
	}
	
	/**
	 * 以html的形式返回对象数据
	 * @Date 2016-12-18下午9:40:37
	 * @param html html格式的字符串
	 */
	protected void renderHTML(String html) {
		ajaxResponse(html, "text/html");
	}
	/**
	 * 以xml的形式返回对象数据
	 * @Date 2016-12-18下午9:40:57
	 * @param xml xml格式的字符串
	 */
	protected void renderXML(String xml) {
		ajaxResponse(xml, "text/xml");
	}
	/**
	 * 以纯文本的形式返回对象数据
	 * @Date 2016-12-18下午9:41:15
	 * @param text 内容
	 */
	protected void renderText(String text) {
		ajaxResponse(text, "text/plain");
	}
	/**
	 * 以纯文本的形式返回对象数据，常用于action返回结果
	 * @Date 2016-12-18下午9:41:28
	 * @param rr ResposeResult对象
	 */
	protected void renderText(ResposeResult rr) {
		ajaxResponse(JSONUtil.toJSONStr(rr), "text/plain");
	}
	
	/**
	 * 保存功能
	 * @Date 2017-2-18下午10:58:33
	 * @return
	 * @throws Exception
	 */
	public String save() throws Exception {
		return null;
	}
	/**
	 * 删除
	 * @Date 2017-2-18下午10:59:05
	 * @return
	 * @throws Exception
	 */
	public String delete() throws Exception {
		return null;
	}
	/**
	 * 分页查询，用于数据表格展示
	 * @Date 2017-2-18下午10:59:17
	 * @return
	 * @throws Exception
	 */
	public String showList() throws Exception {
		return null;
	}
	/**
	 * 跳转到主界面页面
	 * @Date 2017-2-18下午10:59:17
	 * @throws Exception
	 */
	public String list() throws Exception {
		return SUCCESS;
	}
	/**
	 * 跳转到新增页面
	 * @Date 2017-2-18下午10:59:17
	 * @throws Exception
	 */
	public String input() throws Exception {
		return INPUT;
	}
	
	/**
	 * struts2框架的默认方法是execute()方法
	 * @Date 2017-2-18下午10:59:17
	 */
	@Override
	public String execute() throws Exception {
		return list();
	}
	
	/**
	 * 取得客户端登陆ip地址
	 */
	public String getClientIp() {
		HttpServletRequest  request = getRequest();
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
	
	
	public String get_() {
		return _;
	}

	public void set_(String _) {
		this._ = _;
	}
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
}
