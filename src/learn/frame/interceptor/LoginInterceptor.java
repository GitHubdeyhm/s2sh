package learn.frame.interceptor;

import java.util.Date;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import learn.frame.common.Constant;
import learn.frame.common.UserSession;
import learn.frame.entity.common.LogEntity;
import learn.frame.entity.uums.UserEntity;
import learn.frame.service.common.ILogService;
import learn.frame.utils.RequestUtil;
import learn.frame.utils.StringUtil;

import org.apache.struts2.ServletActionContext;

import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ActionInvocation;
import com.opensymphony.xwork2.ActionProxy;
import com.opensymphony.xwork2.interceptor.MethodFilterInterceptor;

/**
 * 项目登陆拦截器
 * 继承AbstractInterceptor抽象类会拦截action里的所有方法
 * 如果不想拦截action里的所有方法，则可以继承MethodFilterInterceptor类，设置不需要拦截的方法名
 */
public class LoginInterceptor extends MethodFilterInterceptor {

	private static final long serialVersionUID = 1L;
	
	@Resource
	private ILogService logService;
	
	@Override
	public String doIntercept(ActionInvocation invocation) throws Exception {
//		System.out.println("==========进入到登录拦截器方法===========");
		ActionContext ac = invocation.getInvocationContext();
		//得到request对象
		HttpServletRequest request = (HttpServletRequest)ac.get(ServletActionContext.HTTP_REQUEST);
		//得到session对象
		UserSession userSess = (UserSession)request.getSession().getAttribute(Constant.USER_SESSION);
		//未登录
		if (userSess == null) {
			//是ajax请求，ajax请求session失效时需返回null才能获取到响应头信息sessionInvalid
			if (RequestUtil.isAjaxRequest()) {
				HttpServletResponse response = (HttpServletResponse)ac.get(ServletActionContext.HTTP_RESPONSE);
				response.setHeader("sessionInvalid", "sessionInvalid");//设置session失效时的响应头信息
				return null;
			}
			return "login";//登录方法名称是：login
		} else {
			//记录操作日志
			LogEntity log = new LogEntity();
			log.setCreateTime(new Date());
			log.setOptUser(new UserEntity());
			log.getOptUser().setId(userSess.getUserId());
			log.setUserIP(RequestUtil.getClientIP(request));
			log.setOptStatus(3);
			log.setOptUrl(request.getRequestURL().toString());
			log.setOptRequestMethod(request.getMethod());
			log.setOptType(2);
			ActionProxy ap = invocation.getProxy();
			String methodName = ap.getMethod();
			log.setLogName(ap.getActionName()+"Action#"+methodName);
			if (StringUtil.isNullOrEmpty(methodName)) {
				log.setLogContent("未知方法");
			} else {
				if (methodName.startsWith("save")) {
					log.setLogContent("保存");
				} else if (methodName.startsWith("show")) {
					log.setLogContent("查询");
				} else if (methodName.startsWith("login")) {
					log.setLogContent("登陆");
				} else if (methodName.startsWith("logout")) {
					log.setLogContent("退出系统");
				} else if (methodName.startsWith("delete")) {
					log.setLogContent("删除");
				} else {
					log.setLogContent("其它");
				}
			}
			logService.saveOrUpdate(log);
		}
		return invocation.invoke();
	}
}
