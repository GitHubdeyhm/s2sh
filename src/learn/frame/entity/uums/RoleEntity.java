package learn.frame.entity.uums;

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

import org.hibernate.annotations.GenericGenerator;
/**
 * 角色实体类
 * @Date 2016-8-6下午10:43:29
 */
@Entity
@Table(name="t_uums_role")
public class RoleEntity implements Serializable {

	private static final long serialVersionUID = 1L;
	
	private String id;
	/**角色名称*/
	private String roleName;
	/**
	 * 角色级别：超级管理员的角色级别为-1，系统默认角色的角色级别为0，其它用户的角色级别大于0
	 */
	private Integer roleLevel;
	//角色排序
	private Integer roleOrder;
	//创建角色的人
	private UserEntity createUser;
	/**创建时间*/
	private Date createTime;
	/**角色描述*/
	private String roleNote;
	//是否删除（0：否，1：是），逻辑删除
	private Integer deleteFlag;
	
	//菜单编码
	private String menuPrivils;
	
	/**
	 * 默认构造器
	 * @Date 2016-8-20下午6:54:01
	 */
	public RoleEntity() {
		
	}
	/**
	 * 是否删除构造器
	 * @Date 2016-8-20下午6:53:45
	 * @param deleteFlag
	 */
	public RoleEntity(Integer deleteFlag) {
		this.deleteFlag = deleteFlag;
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
	
	@Column(name="role_name", nullable=false, length=40)
	public String getRoleName() {
		return roleName;
	}
	public void setRoleName(String roleName) {
		this.roleName = roleName;
	}
	
	@Column(name="role_level")
	public Integer getRoleLevel() {
		return roleLevel;
	}
	public void setRoleLevel(Integer roleLevel) {
		this.roleLevel = roleLevel;
	}
	
	@ManyToOne(fetch=FetchType.EAGER)
	@JoinColumn(name="create_user_id")
	public UserEntity getCreateUser() {
		return createUser;
	}
	public void setCreateUser(UserEntity createUser) {
		this.createUser = createUser;
	}
	
	@Column(name="create_time")
	public Date getCreateTime() {
		return createTime;
	}
	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}
	
	@Column(name="role_order")
	public Integer getRoleOrder() {
		return roleOrder;
	}
	public void setRoleOrder(Integer roleOrder) {
		this.roleOrder = roleOrder;
	}
	
	@Column(name="role_note")
	public String getRoleNote() {
		return roleNote;
	}
	public void setRoleNote(String roleNote) {
		this.roleNote = roleNote;
	}
	
	@Column(name="delete_flag")
	public Integer getDeleteFlag() {
		return deleteFlag;
	}
	public void setDeleteFlag(Integer deleteFlag) {
		this.deleteFlag = deleteFlag;
	}
	
	@Transient
	public String getMenuPrivils() {
		return menuPrivils;
	}
	public void setMenuPrivils(String menuPrivils) {
		this.menuPrivils = menuPrivils;
	}	
}
