package learn.frame.listener;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionListener;

import learn.frame.utils.file.PropertiesUtil;
/**
 * 监听器，实现这三个监听器需要在web.xml文件注册
 * 若有多个监听器则按照web.xml文件配置的顺序执行
 * @author Administrator
 */
public class UserListener implements ServletContextListener, HttpSessionListener {
	/**
	 * 此变量保存系统当前时间段的在线人数
	 * 此方法统计在线人数并不能及时显示当前时刻的登陆人数，而是显示一段时间的在线人数
	 * session超时的时间设置越短则更精确
	 */
	private int count;
	
//	private static Map<String, String> userMap = new HashMap<String, String>();
	
	public UserListener(){
		System.out.println("--------初始化userListener监听器中。。。");
	}

	@Override
	public void sessionCreated(HttpSessionEvent se) {
		count++;
		ServletContext context = se.getSession().getServletContext();
		context.setAttribute("count", new Integer(count));
		System.out.println(count+"===session对象创建中===="+se.getSession().getId()+"。。。。。");
	}

	@Override
	public void sessionDestroyed(HttpSessionEvent se) {
		count--;
		ServletContext context = se.getSession().getServletContext();
		context.setAttribute("count", new Integer(count));
		String sessionId = se.getSession().getId();
//		userMap.remove(sessionId);
		System.out.println(count+"===session对象销毁中"+sessionId+"。。。。。");
	}

	@Override
	public void contextDestroyed(ServletContextEvent sce) {
		System.out.println("===application对象销毁中。。。。。");
	}

	@Override
	public void contextInitialized(ServletContextEvent sce) {
		System.out.println("===application对象初始化中。。。。。");
		boolean userToRoles = new Boolean(PropertiesUtil.getPropertiesValueByKey("system", "userToRoles"));
		if (userToRoles) {
			System.out.println("允许一个用户拥有多个角色！");
		} else {
			System.out.println("一个用户只能拥有一个角色！");
		}
		boolean userToEmptyRole = new Boolean(PropertiesUtil.getPropertiesValueByKey("system", "userToEmptyRole"));
		if (userToEmptyRole) {
			System.out.println("用户可以不属于任何角色！");
		} else {
			System.out.println("用户必须属于至少一个角色！");
		}
		//部门相关
		boolean userToUnits = new Boolean(PropertiesUtil.getPropertiesValueByKey("system", "userToUnits"));
		if (userToUnits) {
			System.out.println("允许一个用户属于多个部门！");
		} else {
			System.out.println("一个用户只能属于一个部门！");
		}
		boolean userToLeafUnit = new Boolean(PropertiesUtil.getPropertiesValueByKey("system", "userToLeafUnit"));
		if (userToLeafUnit) {
			System.out.println("用户只能输入最末级部门！");
		}
		boolean userToEmptyUnit = new Boolean(PropertiesUtil.getPropertiesValueByKey("system", "userToEmptyUnit"));
		if (userToEmptyUnit) {
			System.out.println("允许用户可以不属于任何部门！");
		} else {
			System.out.println("用户必须属于至少一个部门！");
		}
		//初始化系统用户相关配置
		ServletContext context = sce.getServletContext();
		context.setAttribute("userToRoles", userToRoles);
		context.setAttribute("userToEmptyRole", userToEmptyRole);
		context.setAttribute("userToUnits", userToUnits);
		context.setAttribute("userToLeafUnit", userToLeafUnit);
		context.setAttribute("userToEmptyUnit", userToEmptyUnit);
	}
	
	/**
	 * 后登录的用户可以登录，前一个同账号用户被强迫下线
	 * @Title replaceRepeatUserSession 
	 * @Description
	 * @Date 2015-10-27 上午11:05:26 
	 * @param userName
	 */
	public static void replaceUserSession(String sessionId, String userName) {
		/*if (sessionId != null && userName != null) {
			if (userMap.containsValue(userName)) {
				Iterator<String> it = userMap.keySet().iterator();
				while (it.hasNext()) {
					String key = it.next();
					if (userName.equals(userMap.get(key))) {
						userMap.remove(key);
						break;
					}
				}
			}
			userMap.put(sessionId, userName);
		}*/
	}
	
	/**
	 * 判断用户是否被迫下线
	 * @Title isForceOffline 
	 * @Description
	 * @Date 2015-10-27 下午03:51:58 
	 * @param sessionId
	 * @return
	 */
	public static boolean isForceOffline(HttpSession session) {
		/*if (session != null) {
			if (!userMap.containsKey(session.getId())) {
				session.setAttribute("offline", "offline");
				return true;
			}
		}*/
		return false;
	}
}
