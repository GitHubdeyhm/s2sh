package learn.frame.entity.uums;

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
 * 部门实体类
 * @Date 2016-8-6下午9:59:18
 */
@Entity
@Table(name="t_uums_unit")
public class UnitEntity implements Serializable {

	private static final long serialVersionUID = 1L;
	
	/**部门ID*/
	private String id;
	/**部门编号*/
	private String unitCode;
	/**部门名称*/
	private String unitName;
	//部门排序
	private Integer unitOrder;
	/**上级部门ID*/
	private String parentId;
	/**部门说明*/
	private String unitNote;
	//创建时间
	private Date createTime;
	//是否删除（0：否，1：是），逻辑删除
	private Integer deleteFlag;
	
	/**临时变量--用于判断在生成树形结构展示时，某个节点下是否存在子节点，默认为不存在false*/
	private boolean isHasChild;
	
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
	
	@Column(name="unit_code")
	public String getUnitCode() {
		return unitCode;
	}
	public void setUnitCode(String unitCode) {
		this.unitCode = unitCode;
	}
	
	@Column(name="unit_name")
	public String getUnitName() {
		return unitName;
	}
	public void setUnitName(String unitName) {
		this.unitName = unitName;
	}
	
	@Column(name="parent_id", length=32)
	public String getParentId() {
		return parentId;
	}
	public void setParentId(String parentId) {
		this.parentId = parentId;
	}
	
	@Column(name="unit_order")
	public Integer getUnitOrder() {
		return unitOrder;
	}
	public void setUnitOrder(Integer unitOrder) {
		this.unitOrder = unitOrder;
	}
	
	@Column(name="create_time")
	public Date getCreateTime() {
		return createTime;
	}
	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}
	
	@Column(name="delete_flag")
	public Integer getDeleteFlag() {
		return deleteFlag;
	}
	public void setDeleteFlag(Integer deleteFlag) {
		this.deleteFlag = deleteFlag;
	}
	
	@Column(name="unit_note")
	public String getUnitNote() {
		return unitNote;
	}
	public void setUnitNote(String unitNote) {
		this.unitNote = unitNote;
	}
	
	@Transient
	public boolean isHasChild() {
		return isHasChild;
	}
	public void setHasChild(boolean isHasChild) {
		this.isHasChild = isHasChild;
	}
}
