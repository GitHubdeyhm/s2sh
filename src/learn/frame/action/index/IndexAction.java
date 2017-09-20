package learn.frame.action.index;

import learn.frame.action.BaseAction;

import org.apache.struts2.convention.annotation.Result;
import org.apache.struts2.convention.annotation.Results;

@Results({
	@Result(name="main", location="/WEB-INF/jsp/index/index.jsp"),
	@Result(name="south", location="/WEB-INF/jsp/index/south.jsp"),
	@Result(name="north", location="/WEB-INF/jsp/index/north.jsp"),
	@Result(name="center", location="/WEB-INF/jsp/index/center.jsp"),
	@Result(name="west", location="/WEB-INF/jsp/index/west.jsp")})//通过注解方式配置页面跳转
public class IndexAction extends BaseAction {

	private static final long serialVersionUID = 1L;
	/**
	 * 跳转到首页
	 */
	public String main() throws Exception {
		return "main";
	}
	/**
	 * 布局--跳转到west.jsp
	 */
	public String west() throws Exception {
		return "west";
	}
	/**
	 * 布局--跳转到north.jsp
	 */
	public String north() throws Exception {
		return "north";
	}
	/**
	 * 布局--跳转到south.jsp
	 */
	public String south() throws Exception {
		return "south";
	}
	/**
	 * 布局--跳转到east.jsp
	 */
	public String east() throws Exception {
		return "east";
	}
	/**
	 * 布局--跳转到center.jsp
	 */
	public String center() throws Exception {
		return "center";
	}
	
	@Override
	public String execute() throws Exception {
		return main();
	}
}
