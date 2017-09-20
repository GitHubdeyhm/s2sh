package learn.frame.common.easyui;

import java.io.Serializable;

import learn.frame.utils.JSONUtil;

/**
 * easyui的下拉列表数据模型
 * @Date 2016-8-6 下午12:27:42
 */
public class Combobox implements Serializable {

	private static final long serialVersionUID = 1L;
	
	/**下拉选项的值*/
	private String value;
	/**下拉选项的文本*/
	private String text;
	/**下拉选项分组*/
	private String group;
	/**下拉选项中是否默认选中--固定名称*/
	private boolean selected;
	
	public Combobox() {
		
	}
	
	public Combobox(String value, String text) {
		this.value = value;
		this.text = text;
	}

	public Combobox(String value, String text, boolean selected) {
		this.value = value;
		this.text = text;
		this.selected = selected;
	}

	public Combobox(String value, String text, String group, boolean selected) {
		this.value = value;
		this.text = text;
		this.group = group;
		this.selected = selected;
	}
	
	public String getValue() {
		return value;
	}
	public void setValue(String value) {
		this.value = value;
	}
	
	public String getText() {
		return text;
	}
	public void setText(String text) {
		this.text = text;
	}
	
	public String getGroup() {
		return group;
	}
	public void setGroup(String group) {
		this.group = group;
	}
	
	public boolean isSelected() {
		return selected;
	}
	public void setSelected(boolean selected) {
		this.selected = selected;
	}
	
	@Override
	public String toString() {
		return JSONUtil.toJSONStr(this);
	}
}
