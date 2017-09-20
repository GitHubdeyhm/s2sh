package learn.frame.vo;

import java.io.Serializable;

/**
 * 封装用户、部门、角色
 * @Date 2016-9-10下午2:10:07
 */
public class UserVo implements Serializable {

	private static final long serialVersionUID = 1L;
	
	/**用户ID*/
	private String id;
	/**用户名*/
	private String userName;
	/**真实姓名*/
	private String realName;
	/**性别（0：男，1：女，2：保密（未知））*/
	private Integer gender;
	/**手机号码*/
	private String phone;
	/**邮箱*/
	private String email;
	/**用户注册时间*/
	private String createTime;
	/**上次登录IP*/
	private String loginIP;
	/**用户最近一次登录时间*/
	private String loginTime;
	//是否删除（0：否，1：是），逻辑删除
	private Integer deleteFlag;
	//头像路径
	private String headImgUrl;
	
	//性别字符串
	private String genderText;
	
	//角色ID，多个以英文逗号隔开，下同
	private String roleIds;
	//角色名称
	private String roleNames;
	//部门ID
	private String unitIds;
	//部门编码
	private String unitCodes;
	//部门名称
	private String unitNames;
	
	/**
	 * 设置性别字符串
	 * @Date 2016-9-10下午8:00:09
	 * @param gender
	 */
	public void setGender(Integer gender) {
		if (gender == null) {
			this.genderText = null;
		} else if (gender == 0) {
			this.genderText = "男";
		} else if (gender == 1) {
			this.genderText = "女";
		} else {
			this.genderText = "未知";
		}
		this.gender = gender;
	}
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public String getRealName() {
		return realName;
	}
	public void setRealName(String realName) {
		this.realName = realName;
	}
	public Integer getGender() {
		return gender;
	}
	public String getPhone() {
		return phone;
	}
	public void setPhone(String phone) {
		this.phone = phone;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getCreateTime() {
		return createTime;
	}
	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}
	public String getLoginIP() {
		return loginIP;
	}
	public void setLoginIP(String loginIP) {
		this.loginIP = loginIP;
	}
	public String getLoginTime() {
		return loginTime;
	}
	public void setLoginTime(String loginTime) {
		this.loginTime = loginTime;
	}
	public Integer getDeleteFlag() {
		return deleteFlag;
	}
	public void setDeleteFlag(Integer deleteFlag) {
		this.deleteFlag = deleteFlag;
	}
	public String getGenderText() {
		return genderText;
	}
	public String getRoleIds() {
		return roleIds;
	}
	public void setRoleIds(String roleIds) {
		this.roleIds = roleIds;
	}
	public String getRoleNames() {
		return roleNames;
	}
	public void setRoleNames(String roleNames) {
		this.roleNames = roleNames;
	}
	
	public String getHeadImgUrl() {
		return headImgUrl;
	}

	public void setHeadImgUrl(String headImgUrl) {
		this.headImgUrl = headImgUrl;
	}

	public String getUnitIds() {
		return unitIds;
	}
	public void setUnitIds(String unitIds) {
		this.unitIds = unitIds;
	}
	public String getUnitCodes() {
		return unitCodes;
	}
	public void setUnitCodes(String unitCodes) {
		this.unitCodes = unitCodes;
	}
	public String getUnitNames() {
		return unitNames;
	}
	public void setUnitNames(String unitNames) {
		this.unitNames = unitNames;
	}
}
