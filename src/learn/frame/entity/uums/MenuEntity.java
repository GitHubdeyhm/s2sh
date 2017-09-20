package learn.frame.entity.uums;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

import learn.frame.common.Constant;
import learn.frame.utils.StringUtil;

import org.hibernate.annotations.GenericGenerator;

/**
 * 菜单实体类
 */
@Entity
@Table(name="t_uums_menu")
public class MenuEntity implements Serializable {

	private static final long serialVersionUID = 1L;
	
	/**菜单ID*/
	private String id;
	/**菜单名称*/
	private String menuName;
	/**菜单编码*/
	private String menuCode;
	/**菜单url*/
	private String menuUrl;
	/**菜单图标地址*/
	private String menuIcon;
	//菜单标示，唯一，用户自定义，通过该菜单标示定位具体菜单
	private String menuMark;
	/**是否启用菜单（0：未启用，1：已启用）--通过该字段可以决定项目是否展示该菜单*/
	private Boolean isEnable;
	/**菜单所属项目编码--可以为每个项目取一个编号，项目编号以p开头*/
	private String menuProjectCode;
	/**菜单类型，预留字段*/
	private Integer menuType;
	/**菜单排序*/
	private Integer menuOrder;
	/**菜单说明*/
	private String menuNote;
	
	//生成树形表格临时字段
	private String _parentId;
	
	//临时字段--拥有的菜单编码，多个编码以英文逗号隔开
	private String privilMenuCodes;
	
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
	
	@Column(name="menu_name", nullable=false, length=40)
	public String getMenuName() {
		return menuName;
	}
	public void setMenuName(String menuName) {
		this.menuName = menuName;
	}
	
	@Column(name="menu_code", nullable=false)
	public String getMenuCode() {
		return menuCode;
	}
	
	/**
	 * 设置菜单编码，包括设置了父级菜单编码
	 * @Date 2016-8-21下午10:47:47
	 * @param menuCode 菜单编码
	 */
	public void setMenuCode(String menuCode) {
		if (!StringUtil.isNullOrEmpty(menuCode)) {
			int len = menuCode.length();
			if (len > Constant.CODE_LEVEL_LENGTH) {
				//设置树形表格的父节点
				this._parentId = menuCode.substring(0, len-Constant.CODE_LEVEL_LENGTH);
			}
		}
		this.menuCode = menuCode;
	}
	
	@Column(name="menu_url")
	public String getMenuUrl() {
		return menuUrl;
	}
	public void setMenuUrl(String menuUrl) {
		this.menuUrl = menuUrl;
	}
	
	@Column(name="menu_icon")
	public String getMenuIcon() {
		return menuIcon;
	}
	public void setMenuIcon(String menuIcon) {
		this.menuIcon = menuIcon;
	}
	
	@Column(name="is_enable", nullable=false)
	public Boolean getIsEnable() {
		return isEnable;
	}
	public void setIsEnable(Boolean isEnable) {
		this.isEnable = isEnable;
	}
	
	@Column(name="menu_project_code")
	public String getMenuProjectCode() {
		return menuProjectCode;
	}
	
	public void setMenuProjectCode(String menuProjectCode) {
		this.menuProjectCode = menuProjectCode;
	}
	
	@Column(name="menu_type")
	public Integer getMenuType() {
		return menuType;
	}
	public void setMenuType(Integer menuType) {
		this.menuType = menuType;
	}
	
	@Column(name="menu_order")
	public Integer getMenuOrder() {
		return menuOrder;
	}
	public void setMenuOrder(Integer menuOrder) {
		this.menuOrder = menuOrder;
	}
	
	@Column(name="menu_mark")
	public String getMenuMark() {
		return menuMark;
	}
	public void setMenuMark(String menuMark) {
		this.menuMark = menuMark;
	}
	
	@Column(name="menu_note")
	public String getMenuNote() {
		return menuNote;
	}
	public void setMenuNote(String menuNote) {
		this.menuNote = menuNote;
	}
	
	@Transient
	public String getPrivilMenuCodes() {
		return privilMenuCodes;
	}
	public void setPrivilMenuCodes(String privilMenuCodes) {
		this.privilMenuCodes = privilMenuCodes;
	}
	
	@Transient
	public String get_parentId() {
		return _parentId;
	}
}
