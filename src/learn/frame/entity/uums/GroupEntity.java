package learn.frame.entity.uums;

import java.io.Serializable;

public class GroupEntity implements Serializable {

	private static final long serialVersionUID = 1L;
	
	private String groupId;
	private String groupName;
	private String groupCode;
	private Integer groupLevel;
	private String parentGroupId;
	private String groupNote;
	
	
	public String getGroupId() {
		return groupId;
	}
	public void setGroupId(String groupId) {
		this.groupId = groupId;
	}
	public String getGroupName() {
		return groupName;
	}
	public void setGroupName(String groupName) {
		this.groupName = groupName;
	}
	public String getGroupCode() {
		return groupCode;
	}
	public void setGroupCode(String groupCode) {
		this.groupCode = groupCode;
	}
	public Integer getGroupLevel() {
		return groupLevel;
	}
	public void setGroupLevel(Integer groupLevel) {
		this.groupLevel = groupLevel;
	}
	public String getParentGroupId() {
		return parentGroupId;
	}
	public void setParentGroupId(String parentGroupId) {
		this.parentGroupId = parentGroupId;
	}
	public String getGroupNote() {
		return groupNote;
	}
	public void setGroupNote(String groupNote) {
		this.groupNote = groupNote;
	}
}
