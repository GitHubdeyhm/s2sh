package learn.frame.entity.uums;

import java.io.File;
import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.hibernate.annotations.GenericGenerator;

/**
 * 用户实体类
 * @Date 2016-8-6下午8:07:33
 */
@Entity
@Table(name="t_uums_user")
public class UserEntity implements Serializable {

	private static final long serialVersionUID = 1L;
	
	/**用户ID*/
	private String id;
	/**用户名*/
	private String userName;
	/**真实姓名*/
	private String realName;
	/**密码*/
	private String userPwd;
	/**性别（0：男，1：女，2：保密（未知））*/
	private Integer gender;
	/**手机号码*/
	private String phone;
	/**邮箱*/
	private String email;
	/**用户注册时间*/
	private Date createTime;
	/**上次登录IP*/
	private String loginIP;
	/**用户最近一次登录时间*/
	private Date loginTime;
	//是否删除（0：否，1：是），逻辑删除
	private Integer deleteFlag;
	//头像
	private String headImgUrl;
	
	/**临时字段--新密码，用于用户修改密码时*/
	private String newPwd;
	/**临时字段--确认新密码，用于用户修改密码时*/
	private String againPwd;
	//上传临时文件
	private File headImgUrlFile;
	
	/**
	 * 无参数构造器
	 * @Date 2016-8-6下午8:07:53
	 */
	public UserEntity () {
	}
	
	/**
	 * 用户名和密码构造器
	 * @Date 2016-8-6下午8:07:53
	 * @param userName 用户名（登录名）
	 * @param userPwd 密码
	 */
	public UserEntity (String userName, String userPwd) {
		this.userName = userName;
		this.userPwd = userPwd;
	}
	
	/**
	 * 用户登录IP和登陆时间构造器
	 * @Date 2016-8-6下午8:07:53
	 * @param loginIP 登陆IP
	 * @param loginTime 登陆时间
	 */
	public UserEntity (String loginIP, Date loginTime) {
		this.loginIP = loginIP;
		this.loginTime = loginTime;
	}

	
	@Id
	@GeneratedValue(generator="idGenerator")
	@GenericGenerator(name="idGenerator", strategy="uuid")
	@Column(name="id", nullable=false, unique=true, length=32)
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	
	@Column(name="user_name", nullable=false, length=40)
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	
	@Column(name="real_name", length=40)
	public String getRealName() {
		return realName;
	}
	public void setRealName(String realName) {
		this.realName = realName;
	}
	
	@Column(name="user_pwd")
	public String getUserPwd() {
		return userPwd;
	}
	public void setUserPwd(String userPwd) {
		this.userPwd = userPwd;
	}
	
	@Column(name="login_ip")
	public String getLoginIP() {
		return loginIP;
	}
	public void setLoginIP(String loginIP) {
		this.loginIP = loginIP;
	}
	
	@Column(name="create_time")
	public Date getCreateTime() {
		return createTime;
	}
	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}
	
	@Column(name="head_img_url")
	public String getHeadImgUrl() {
		return headImgUrl;
	}

	public void setHeadImgUrl(String headImgUrl) {
		this.headImgUrl = headImgUrl;
	}

	@Column(name="login_time")
	public Date getLoginTime() {
		return loginTime;
	}
	public void setLoginTime(Date loginTime) {
		this.loginTime = loginTime;
	}
	
	@Column(name="gender")
	public Integer getGender() {
		return gender;
	}
	public void setGender(Integer gender) {
		this.gender = gender;
	}
	
	@Column(name="phone")
	public String getPhone() {
		return phone;
	}
	public void setPhone(String phone) {
		this.phone = phone;
	}
	
	@Column(name="email")
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	
	@Column(name="delete_flag")
	public Integer getDeleteFlag() {
		return deleteFlag;
	}
	public void setDeleteFlag(Integer deleteFlag) {
		this.deleteFlag = deleteFlag;
	}
	
	@Transient
	public String getNewPwd() {
		return newPwd;
	}
	public void setNewPwd(String newPwd) {
		this.newPwd = newPwd;
	}
	
	@Transient
	public String getAgainPwd() {
		return againPwd;
	}
	public void setAgainPwd(String againPwd) {
		this.againPwd = againPwd;
	}

	@Transient
	public File getHeadImgUrlFile() {
		return headImgUrlFile;
	}

	public void setHeadImgUrlFile(File headImgUrlFile) {
		this.headImgUrlFile = headImgUrlFile;
	}
}
