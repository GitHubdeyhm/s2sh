package learn.frame.entity.common;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

import learn.frame.entity.uums.UserEntity;

import org.hibernate.annotations.GenericGenerator;

/**
 * 日志实体类
 * @Date 2016-7-1 上午10:53:30
 */
@Entity
@Table(name="t_base_log")
public class LogEntity implements Serializable {

	private static final long serialVersionUID = 1L;
	
	private String id;
	//日志名称（模块名称）
	private String logName;
	//日志编码
	private String logCode;
	//日志内容
	private String logContent;
	//日志操作标识:（1：登陆，2：退出系统，3：新增，4：修改，5：删除，6：查询，7：导入，8：导出）
	private Integer optType;
	//操作url标示
	private String optUrl;
	//操作请求方法（get、post）
	private String optRequestMethod;
	//操作状态（0：操作失败，1：操作成功，2：警告）
	private Integer optStatus;
	//操作人
	private UserEntity optUser;
	//操作时间
	private Date createTime;
	//操作人IP地址
	private String userIP;
	
	//操作时间
	private String beginTime;
	private String endTime;
	//操作人
	private String realName;
	
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
	
	@Column(name="log_name")
	public String getLogName() {
		return logName;
	}
	public void setLogName(String logName) {
		this.logName = logName;
	}
	
	@Column(name="log_code")
	public String getLogCode() {
		return logCode;
	}
	public void setLogCode(String logCode) {
		this.logCode = logCode;
	}
	
	@ManyToOne(fetch=FetchType.EAGER)
	@JoinColumn(name="user_id")
	public UserEntity getOptUser() {
		return optUser;
	}
	public void setOptUser(UserEntity optUser) {
		this.optUser = optUser;
	}
	
	@Column(name="create_time")
	public Date getCreateTime() {
		return createTime;
	}
	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}
	
	@Column(name="user_ip")
	public String getUserIP() {
		return userIP;
	}
	public void setUserIP(String userIP) {
		this.userIP = userIP;
	}
	
	@Column(name="opt_url")
	public String getOptUrl() {
		return optUrl;
	}
	public void setOptUrl(String optUrl) {
		this.optUrl = optUrl;
	}
	
	@Column(name="opt_request_method")
	public String getOptRequestMethod() {
		return optRequestMethod;
	}
	public void setOptRequestMethod(String optRequestMethod) {
		this.optRequestMethod = optRequestMethod;
	}
	
	@Column(name="log_content")
	public String getLogContent() {
		return logContent;
	}
	public void setLogContent(String logContent) {
		this.logContent = logContent;
	}
	
	@Column(name="opt_type")
	public Integer getOptType() {
		return optType;
	}
	public void setOptType(Integer optType) {
		this.optType = optType;
	}
	
	@Column(name="opt_status")
	public Integer getOptStatus() {
		return optStatus;
	}
	public void setOptStatus(Integer optStatus) {
		this.optStatus = optStatus;
	}
	
	@Transient
	public String getBeginTime() {
		return beginTime;
	}
	public void setBeginTime(String beginTime) {
		this.beginTime = beginTime;
	}
	
	@Transient
	public String getEndTime() {
		return endTime;
	}
	public void setEndTime(String endTime) {
		this.endTime = endTime;
	}
	
	@Transient
	public String getRealName() {
		return realName;
	}
	public void setRealName(String realName) {
		this.realName = realName;
	}
}