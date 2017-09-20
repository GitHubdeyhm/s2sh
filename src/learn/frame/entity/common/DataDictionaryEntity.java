package learn.frame.entity.common;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

/**
 * 数据字典实体类
 * @Date 2016-7-1 上午10:54:59
 */
@Entity
@Table(name="t_base_dictionary")
public class DataDictionaryEntity implements Serializable {

	private static final long serialVersionUID = 1L;
	
	private String id;
	//字典项名称
	private String dictName;
	//字典键
	private String dictKey;
	//字典值
	private String dictValue;
	//字典编码--层级结构
	//private String dictCode;
	//创建时间
	private Date createTime;
	//用户是否能够编辑，代表该项可以提供用户修改 （0：否，1：是）
	private Integer dictUserEdit;
	//字典描述信息
	private String dictNote;
	
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
	
	@Column(name="dict_name")
	public String getDictName() {
		return dictName;
	}
	public void setDictName(String dictName) {
		this.dictName = dictName;
	}
	
	@Column(name="dict_key")
	public String getDictKey() {
		return dictKey;
	}
	public void setDictKey(String dictKey) {
		this.dictKey = dictKey;
	}
	
	@Column(name="dict_value")
	public String getDictValue() {
		return dictValue;
	}
	public void setDictValue(String dictValue) {
		this.dictValue = dictValue;
	}
	
	@Column(name="create_time")
	public Date getCreateTime() {
		return createTime;
	}
	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}
	
	@Column(name="dict_user_edit")
	public Integer getDictUserEdit() {
		return dictUserEdit;
	}
	public void setDictUserEdit(Integer dictUserEdit) {
		this.dictUserEdit = dictUserEdit;
	}
	
	@Column(name="dict_note")
	public String getDictNote() {
		return dictNote;
	}
	public void setDictNote(String dictNote) {
		this.dictNote = dictNote;
	}
}
