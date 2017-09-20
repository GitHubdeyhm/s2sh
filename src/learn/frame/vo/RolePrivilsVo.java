package learn.frame.vo;

import java.io.Serializable;

/**
 * 角色拥有的权限vo类
 * @Date 2016-5-7 下午11:12:19
 */
public class RolePrivilsVo implements Serializable {

	private static final long serialVersionUID = 1L;
	
	//角色ID
	private String roleId;
	/**角色名称*/
	private String roleName;
	/**菜单ID*/
	private String menuId;
	/**菜单名称*/
	private String menuName;
	/**菜单编码*/
	private String menuCode;
	//按钮ID
	private String buttonId;
	//按钮名称，标示，统一定好的，不能随意输入
	private String buttonName;
	//按钮标识
	private String buttonMark;
	
	

}
