package learn.frame.common;

import java.io.Serializable;

/**
 * 此session保存用户登录信息
 * @Date 2016-8-7上午9:53:31
 */
public class UserSession implements Serializable {

	private static final long serialVersionUID = 1L;
	
	/**用户ID*/
	private String userId;
	/**用户登录名*/
	private String userName;
	/**用户昵称*/
	private String realName;
	/**角色IDs，多个角色ID以英文逗号隔开*/
	private String roleIds;
	/**部门编码，多个部门编码code以英文逗号隔开*/
	private String unitCodes;
	/**用户拥有的菜单权限编码，多个以英文逗号隔开*/
	private String menuCodePrivils;
	
	
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
	
	public String getRealName() {
		return realName;
	}
	public void setRealName(String realName) {
		this.realName = realName;
	}
	
	public String getRoleIds() {
		return roleIds;
	}
	public void setRoleIds(String roleIds) {
		this.roleIds = roleIds;
	}
	
	public String getUnitCodes() {
		return unitCodes;
	}
	public void setUnitCodes(String unitCodes) {
		this.unitCodes = unitCodes;
	}
	
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	
	public String getMenuCodePrivils() {
		return menuCodePrivils;
	}
	public void setMenuCodePrivils(String menuCodePrivils) {
		this.menuCodePrivils = menuCodePrivils;
	}
}
