package learn.frame.entity.uums;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

/**
 * 用户部门关联service
 * @Date 2016-8-6下午11:28:07
 */
@Entity
@Table(name="t_uums_user_unit_rel")
public class UserUnitRelEntity implements Serializable {

	private static final long serialVersionUID = 1L;
	
	private String id;
	//用户ID
	private String userId;
	//部门编码
	private String unitCode;
	
	public UserUnitRelEntity() {
		
	}
	
	public UserUnitRelEntity(String userId, String unitCode) {
		this.userId = userId;
		this.unitCode = unitCode;
	}

	@Id
	@GeneratedValue(generator="idGenerator")
	@GenericGenerator(name="idGenerator", strategy="uuid.hex")
	@Column(name="id", nullable=false, unique=true, length=32)
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	
	@Column(name="user_id")
	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	
	@Column(name="unit_code")
	public String getUnitCode() {
		return unitCode;
	}

	public void setUnitCode(String unitCode) {
		this.unitCode = unitCode;
	}
}
