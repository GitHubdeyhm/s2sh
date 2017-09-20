package learn.frame.action.uums;

import java.util.List;

import javax.annotation.Resource;

import learn.frame.action.BaseAction;
import learn.frame.common.Constant;
import learn.frame.common.ResposeResult;
import learn.frame.common.UserSession;
import learn.frame.common.easyui.Datagrid;
import learn.frame.entity.uums.MenuEntity;
import learn.frame.exception.ServiceException;
import learn.frame.service.uums.IMenuService;
import learn.frame.utils.StringUtil;

/**
 * 菜单action
 * @Date 2016-8-7上午9:39:19
 */
public class MenuAction extends BaseAction {

	private static final long serialVersionUID = 1L;
	
	@Resource
	private IMenuService menuService;
	
	/**菜单名称*/
	private String menuName;
	/**菜单编码*/
	private String menuCode;
	/**菜单url*/
	private String menuUrl;
	/**菜单图标地址*/
	private String menuIcon;
	/**菜单说明*/
	private String menuNote;
	/**菜单所属项目编码--可以为每个项目取一个编号，项目编号以p开头*/
	private String mpc;
	/**菜单类型，预留字段*/
	private Integer menuType;
	/**菜单排序*/
	private Integer menuOrder;
	//菜单标示，唯一，用户自定义，通过该菜单标示定位具体菜单
	private String menuMark;
	/**是否启用菜单（0：未启用，1：已启用）--通过该字段可以决定项目是否展示该菜单*/
	private String enableStr;
	//父级编码
	private String parentCode;
	
	/**easyui固定字段--每页显示的记录数*/
	private Integer rows;
	/**easyui固定字段--当前页码*/
	private Integer page;
	
	
	@Override
	public String showList() throws Exception {
		List<MenuEntity> menuList = menuService.findPrivilMenus(setPrivilMenus());
		Datagrid<MenuEntity> dg = new Datagrid<MenuEntity>();
		dg.setRows(menuList);
		renderJSON(dg);
		return null;
	}
	
	
	@Override
	public String save() throws Exception {
		ResposeResult rr = ResposeResult.successResult();
		try {
			MenuEntity menu = menuService.findById(getId());
			if (menu == null) {
				menu = new MenuEntity();
				menu.setMenuProjectCode(mpc);
				if (!Constant.COMBO_DEFAULT_VALUE.equals(parentCode)) {
					menu.setMenuCode(parentCode);
				}
			}
			menu.setMenuName(menuName);
			menu.setMenuIcon(menuIcon);
			menu.setMenuOrder(menuOrder);
			menu.setMenuUrl(menuUrl);
			menu.setMenuType(menuType);
			menu.setMenuMark(menuMark);
			menu.setMenuNote(menuNote);
			boolean isEnable = ("1".equals(enableStr)) ? true : false; 
			menu.setIsEnable(isEnable);
			menuService.saveOrUpdate(menu);
		} catch(ServiceException e) {
			rr.setResultCode(ResposeResult.ERROR_CODE);
			rr.setMsg(e.getMessage());
		}
		renderText(rr.toString());
		return null;
	}
	
	/**
	 * 
	 * @Date 2016-4-13下午10:39:34
	 * @return
	 * @throws Exception
	 */
	public String pc() throws Exception {
		return "pc";
	}
	
	
	@Override
	public String delete() throws Exception {
		ResposeResult rr = ResposeResult.successResult();
		try {
			String codes = getId();
			if (!StringUtil.isNullOrEmpty(codes)) {
				for (String menuCode : codes.split(",")) {
					menuService.deleteByCode(menuCode);
				}
			}
		} catch (ServiceException e) {
			rr.setResultCode(ResposeResult.ERROR_CODE);
			rr.setMsg(e.getMessage());
		}
		renderText(rr.toString());
		return null;
	}
	
	/**
	 * 根据权限菜单编码查询菜单信息
	 * @Date 2016-1-10下午4:23:54
	 * @return
	 * @throws Exception
	 */
	public String showPrivilMenus() throws Exception {
		renderJSON(menuService.findPrivilMenus(setPrivilMenus()));
		return null;
	}
	
	/**
	 * 查询所有菜单信息，以树形结构显示菜单信息，编码作为树的值
	 * @Date 2016-1-10下午4:26:07
	 */
	public String showCodeTree() throws Exception {
		renderJSON(menuService.findForCodeTree(setPrivilMenus()));
		return null;
	}
	
	/**
	 * 通过角色编码查询该角色的所有菜单权限，封装成菜单树形结构
	 * @Date 2016-5-4下午10:30:50
	 * @return
	 * @throws Exception
	 */
	public String showCodeTreeByRole() throws Exception {
		String roleId = getRequest().getParameter("roleId");
		renderJSON(menuService.findForCodeTree(roleId));
		return null;
	}
	
	/**
	 * 新增菜单时根据父编码自动生成排序号
	 * @Date 2016-9-3下午10:24:15
	 * @return
	 * @throws Exception
	 */
	public String showOrder() throws Exception {
		if (Constant.TREENODE_ROOT_ID.equals(menuCode)) {
			menuCode = null;
		}
		int order = menuService.generateOrder(menuCode);
		renderText(String.valueOf(order));
		return null;
	}
	
	/**
	 * 设置菜单权限
	 * @Date 2016-4-21下午11:46:27
	 * @return
	 */
	private MenuEntity setPrivilMenus() {
		String menuProjectCode = getRequest().getParameter("mpc");
		MenuEntity menu = new MenuEntity();
		menu.setMenuName(menuName);
		menu.setMenuProjectCode(menuProjectCode);
		if ("1".equals(enableStr)) {
			menu.setIsEnable(true);
		} else if ("0".equals(enableStr)) {
			menu.setIsEnable(false);
		}
		UserSession sess = (UserSession)getSession().getAttribute(Constant.USER_SESSION);
		menu.setPrivilMenuCodes(sess.getMenuCodePrivils());
		return menu;
	}

	public String getMenuName() {
		return menuName;
	}

	public void setMenuName(String menuName) {
		this.menuName = menuName;
	}

	public String getMenuCode() {
		return menuCode;
	}

	public void setMenuCode(String menuCode) {
		this.menuCode = menuCode;
	}

	public String getMenuUrl() {
		return menuUrl;
	}

	public void setMenuUrl(String menuUrl) {
		this.menuUrl = menuUrl;
	}

	public String getMenuIcon() {
		return menuIcon;
	}

	public void setMenuIcon(String menuIcon) {
		this.menuIcon = menuIcon;
	}

	public String getMenuNote() {
		return menuNote;
	}

	public void setMenuNote(String menuNote) {
		this.menuNote = menuNote;
	}

	public String getMpc() {
		return mpc;
	}

	public void setMpc(String mpc) {
		this.mpc = mpc;
	}

	public Integer getMenuType() {
		return menuType;
	}

	public void setMenuType(Integer menuType) {
		this.menuType = menuType;
	}

	public Integer getMenuOrder() {
		return menuOrder;
	}

	public void setMenuOrder(Integer menuOrder) {
		this.menuOrder = menuOrder;
	}

	public String getMenuMark() {
		return menuMark;
	}

	public void setMenuMark(String menuMark) {
		this.menuMark = menuMark;
	}

	public String getEnableStr() {
		return enableStr;
	}

	public void setEnableStr(String enableStr) {
		this.enableStr = enableStr;
	}

	public Integer getRows() {
		return rows;
	}

	public void setRows(Integer rows) {
		this.rows = rows;
	}

	public Integer getPage() {
		return page;
	}

	public void setPage(Integer page) {
		this.page = page;
	}
	
	public String getParentCode() {
		return parentCode;
	}

	public void setParentCode(String parentCode) {
		this.parentCode = parentCode;
	}
}
