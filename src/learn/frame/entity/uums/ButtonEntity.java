package learn.frame.entity.uums;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

/**
 * 按钮类型实体类，每个页面有哪些按钮
 * @Date 2016-5-1 下午3:10:31
 */
@Entity
@Table(name="t_uums_button")
public class ButtonEntity implements Serializable {

	private static final long serialVersionUID = 1L;
	
	private String id;
	//按钮名称，标示，统一定好的，不能随意输入
	private String buttonName;
	//按钮标识
	private String buttonMark;
	//菜单编码
	private String menuCode;
	//按钮排序
	private Integer buttonOrder;
	//按钮说明
	private String buttonNote;
	
	
	public ButtonEntity() {
		
	}

	public ButtonEntity(String buttonName,
			String menuCode, Integer buttonOrder, String buttonNote) {
		this.buttonName = buttonName;
		this.menuCode = menuCode;
		this.buttonOrder = buttonOrder;
		this.buttonNote = buttonNote;
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

	@Column(name="button_name")
	public String getButtonName() {
		return buttonName;
	}

	public void setButtonName(String buttonName) {
		this.buttonName = buttonName;
	}
	
	@Column(name="button_mark")
	public String getButtonMark() {
		return buttonMark;
	}

	public void setButtonMark(String buttonMark) {
		this.buttonMark = buttonMark;
	}

	@Column(name="menu_code")
	public String getMenuCode() {
		return menuCode;
	}

	public void setMenuCode(String menuCode) {
		this.menuCode = menuCode;
	}

	@Column(name="button_order")
	public Integer getButtonOrder() {
		return buttonOrder;
	}

	public void setButtonOrder(Integer buttonOrder) {
		this.buttonOrder = buttonOrder;
	}

	@Column(name="button_note")
	public String getButtonNote() {
		return buttonNote;
	}

	public void setButtonNote(String buttonNote) {
		this.buttonNote = buttonNote;
	}
	
}
