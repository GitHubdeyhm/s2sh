package learn.frame.entity.uums;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

/**
 * 角色菜单关联实体类
 * @Date 2016-8-6下午11:22:24
 */
@Entity
@Table(name="t_uums_role_menu_rel")
public class RoleMenuRelEntity implements Serializable {

	private static final long serialVersionUID = 1L;
	
	private String id;
	/**角色ID*/
	private String roleId;
	/**菜单编码*/
	private String menuCode;
	
	public RoleMenuRelEntity() {
		
	}
	
	public RoleMenuRelEntity(String roleId, String menuCode) {
		this.roleId = roleId;
		this.menuCode = menuCode;
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

	
	@Column(name="menu_code")
	public String getMenuCode() {
		return menuCode;
	}

	public void setMenuCode(String menuCode) {
		this.menuCode = menuCode;
	}
}
