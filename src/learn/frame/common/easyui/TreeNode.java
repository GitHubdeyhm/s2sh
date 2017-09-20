package learn.frame.common.easyui;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import learn.frame.utils.JSONUtil;

/**
 * easyui树型控件模型
 * @Date 2016-8-6 下午12:27:59
 */
public class TreeNode implements Serializable {

	private static final long serialVersionUID = 1L;
	
	/** 展开节点 */
	public static final String STATE_OPEN = "open";
	/** 关闭节点 */
	public static final String STATE_CLOASED = "closed";
	/** 叶子节点图标 */
	public static final String LEAF_ICON = "icon-application";
	/** 父节点图标 */
	public static final String PARENT_ICON = "icon-folder";
	
	/** 节点ID */
	private String id;
	/** 节点名称 */
	private String text;
	/** 父节点ID */
	private String parentId; 
	/** 是否选中节点，默认为false */
	private boolean checked;
	/** 自定义属性 */
	private Map<String, Object> attributes = new HashMap<String, Object>();
	/** 子节点 */
	private List<TreeNode> children;
	/** 节点状态是否展开 (open,closed)，默认值:closed*/
	private String state = STATE_CLOASED;

	public TreeNode() {
	}

	public TreeNode(String id, String text) {
		this.id = id;
		this.text = text;
	}
	
	public TreeNode(String id, String text, String state) {
		this.id = id;
		this.text = text;
		this.state = state;
	}

	public TreeNode(String id, String text, Boolean checked, String state) {
		this.id = id;
		this.text = text;
		this.checked = checked;
		this.state = state;
	}

	/**
	 * 添加子节点
	 */
	public void addChild(TreeNode childNode) {
		if (this.children == null) {
			this.children = new ArrayList<TreeNode>();
		}
		this.children.add(childNode);
	}

	/**
	 * 节点id
	 */
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}
	/**
	 * 树节点名称
	 */
	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}
	/**
	 * 是否勾选状态（默认：否）
	 */
	public boolean getChecked() {
		return checked;
	}

	public void setChecked(boolean checked) {
		this.checked = checked;
	}
	/**
	 * 自定义属性
	 */
	public Map<String, Object> getAttributes() {
		return attributes;
	}

	public void setAttributes(Map<String, Object> attributes) {
		this.attributes = attributes;
	}
	/**
	 * 子节点
	 */
	public List<TreeNode> getChildren() {
		return children;
	}

	public void setChildren(List<TreeNode> children) {
		this.children = children;
	}
	/**
	 * 是否展开
	 */
	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}
	
	public String getParentId() {
		return parentId;
	}

	public void setParentId(String parentId) {
		this.parentId = parentId;
	}

	@Override
	public String toString() {
		return JSONUtil.toJSONStr(this);
	}
	
	
	public static void main(String[] args) {
		TreeNode t = new TreeNode();
		t.setId("1212");
		t.setText("ewfwefw");
		t.setParentId("wefew");
		t.setChecked(false);
		t.setState(TreeNode.STATE_OPEN);
		t.setAttributes(new java.util.HashMap<String,Object>());
		System.out.println(t.toString());
	}
}
