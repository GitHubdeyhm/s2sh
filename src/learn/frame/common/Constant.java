package learn.frame.common;

/**
 * 保存项目用到的常量类
 */
public final class Constant {
	
	/**时分秒的拼凑--开始时间 00:00:00*/
	public static final String TIME_BEGIN = " 00:00:00";
	/**时分秒的拼凑--结束时间 23:59:59*/
	public static final String TIME_END = " 23:59:59";
	
	/**删除标示（0：否，1：是）-表数据的逻辑删除*/
	public static final int DELETE_FLAG_TRUE = 1;
	/**删除标示（0：否，1：是）-表数据的逻辑删除*/
	public static final int DELETE_FLAG_FALSE = 0;
	
	/**分页初始化的每页查询的记录数*/
	public static final int PAGE_SIZE = 15;
	
	/**自动生成编码级别的长度*/
	public static final int CODE_LEVEL_LENGTH = 3;
	/**最大编码长度值，以3位一级相当于只能生成8级编码*/
	public static final int MAX_CODE_LENGTH = 24;
	
	/**下拉列表默认文本选中常量*/
	public static final String COMBO_ALL_TEXT = "全部";
	/**下拉列表默认选中常量*/
	public static final String COMBO_SELECT_TEXT = "请选择";
	/**下拉列表的默认值*/
	public static final String COMBO_DEFAULT_VALUE = "-1";
	/**树形控件根节点的值*/
	public static final String TREENODE_ROOT_ID = "-1";
	/**树形控件根节点名称*/
	public static final String TREENODE_ROOT_TEXT = "全部";
	
	/**用户登陆session名*/
	public static final String USER_SESSION = "userSession";
	/**新增用户的初始化密码和重置密码时的密码*/
	public static final String USER_RESET_PWD = "123456";
	/**超级管理员的用户ID，与数据库保持一致*/
	public static final String ADMIN_USER_ID = "0e797fa55c4711e6aa88e89a8f132246";
	/**超级管理员的角色级别为-1，系统默认角色级别为0，其它角色级别的值大于0，值越小代表拥有的权限越大*/
	public static final int ADMIN_ROLE_LEVEL = -1;
	/**系统默认角色级别0*/
	public static final int DEFAULT_ROLE_LEVEL = 0;
	/**是否允许一个用户属于多个部门，默认为false（一个用户只能属于一个部门）*/
	public static final boolean USER_TO_UNITS = true;
	/**是否允许一个用户拥有多个角色，默认为false（一个用户只能拥有一个角色）*/
	public static final boolean USER_TO_ROLES = true;
	/**是否开启区域设备类型权限，默认为false（不开启区域设备类型权限）*/
	public static final boolean DMN_CAT_PRIVIL = true;
	
	/**排序号的最大值（6位数）*/
	public static final int MAX_ORDER = 999999;
	
	/** 导出excel数据的数据量如果超出20000条数据则认为过多*/
	public static final int EXPORT_ALLOW_COUNT = 20000;
	/**上传文件时保存到数据库的文件文件名的允许最大长度值*/
	public static final int FILENAME_ALLOW_LENGTH = 200;
}
