package learn.frame.entity.uums;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

/**
 * 按钮权限实体类，某个角色只能查看某个菜单的某个按钮
 * @Date 2016-2-4 下午10:06:47
 */
@Entity
@Table(name="t_uums_role_button_rel")
public class RoleButtonRelEntity implements Serializable {

	private static final long serialVersionUID = 1L;
	
	private String id;
	//按钮ID
	private String buttonId;
	//角色编码
	private String roleId;
	
	public RoleButtonRelEntity() {
		
	}

	public RoleButtonRelEntity(String roleId, String buttonId) {
		this.buttonId = buttonId;
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

	@Column(name="button_id")
	public String getButtonId() {
		return buttonId;
	}

	public void setButtonId(String buttonId) {
		this.buttonId = buttonId;
	}

	@Column(name="role_id")
	public String getRoleId() {
		return roleId;
	}

	public void setRoleId(String roleId) {
		this.roleId = roleId;
	}
}
