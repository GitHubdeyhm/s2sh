package learn.frame.entity.uums;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

/**
 * 用户角色关联实体类
 * @Date 2016-8-6下午11:24:15
 */
@Entity
@Table(name="t_uums_user_role_rel")
public class UserRoleRelEntity implements Serializable {

	private static final long serialVersionUID = 1L;
	
	private String id;
	//用户ID
	private String userId;
	//角色ID
	private String roleId;
	
	public UserRoleRelEntity() {
		
	}
	
	public UserRoleRelEntity(String userId, String roleId) {
		this.userId = userId;
		this.roleId = roleId;
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
	
	@Column(name="role_id")
	public String getRoleId() {
		return roleId;
	}
	public void setRoleId(String roleId) {
		this.roleId = roleId;
	}
	
	@Column(name="user_id")
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
}
