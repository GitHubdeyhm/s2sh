package learn.frame.action.uums;

import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import learn.frame.action.BaseAction;
import learn.frame.common.Constant;
import learn.frame.common.Page;
import learn.frame.common.ResposeResult;
import learn.frame.common.UserSession;
import learn.frame.common.easyui.Datagrid;
import learn.frame.entity.uums.RoleEntity;
import learn.frame.entity.uums.RoleMenuRelEntity;
import learn.frame.entity.uums.UserEntity;
import learn.frame.exception.ServiceException;
import learn.frame.service.uums.IRoleMenuRelService;
import learn.frame.service.uums.IRoleService;
import learn.frame.service.uums.IUserRoleRelService;

/**
 * 角色action
 * @Date 2016-8-7下午12:06:45
 */
public class RoleAction extends BaseAction {

	private static final long serialVersionUID = 1L;
	
	@Resource
	private IRoleService roleService;
	@Resource
	private IRoleMenuRelService roleMenuRelService;
	@Resource
	private IUserRoleRelService userRoleRelService;
	
	/**角色名称*/
	private String roleName;
	private Integer roleLevel;
	/**角色描述*/
	private String roleNote;
	//角色排序
	private Integer roleOrder;
	
	//菜单权限
	private String menuPrivils;
	
	/**easyui固定字段--每页显示的记录数*/
	private Integer rows;
	/**easyui固定字段--当前页码*/
	private Integer page;
	/**easyui固定字段--排序字段*/
	private String sort;
	/**easyui固定字段--排序方式，降序还是升序（desc/asc）*/
	private String order;

	@Override
	public String showList() throws Exception {
		//设置分页对象
		int pageSize = (rows == null || rows < 1) ? Constant.PAGE_SIZE : rows;
		int pageNum = (page == null || page < 1) ? 1 : page;
		Page<RoleEntity> pageBo = new Page<RoleEntity>(pageNum, pageSize);
		pageBo.setSortName(sort);
		pageBo.setSortWay(order);
		//设置查询条件
		RoleEntity role = new RoleEntity();
		role.setRoleName(roleName);
		try {
			pageBo = roleService.findByPage(pageBo, role);
		} catch (Exception e) {
			log.error(e);
		}
		Datagrid<RoleEntity> dg = new Datagrid<RoleEntity>(pageBo.getTotalNum(), pageBo.getList());
		renderJSON(dg, true);
		return null;
	}
	
	/**
	 * 显示角色下拉框
	 */
	public String showCombobox() throws Exception {
		renderJSON(roleService.findForCombobox(Constant.COMBO_ALL_TEXT));
		return null;
	}
	
	@Override
	public String save() throws Exception {
		ResposeResult rr = ResposeResult.successResult();
		try {
			RoleEntity role = roleService.findById(getId());
			if (role == null) {
				role = new RoleEntity(Constant.DELETE_FLAG_FALSE);
				role.setCreateTime(new Date());
				UserSession sess = (UserSession)getSession().getAttribute(Constant.USER_SESSION);
				role.setCreateUser(new UserEntity());
				role.getCreateUser().setId(sess.getUserId());
				role.setRoleLevel(1);//临时设置--保留
			}
			role.setRoleOrder(roleOrder);
			role.setRoleName(roleName);
			role.setRoleNote(roleNote);
			role.setMenuPrivils(menuPrivils);
			
			roleService.saveOrUpdate(role);
		} catch (ServiceException e) {
			rr.setResultCode(ResposeResult.ERROR_CODE);
			rr.setMsg(e.getMessage());
		}
		renderJSON(rr);
		return null;
	}
	
	@Override
	public String delete() throws Exception {
		ResposeResult rr = ResposeResult.successResult();
		boolean userToEmptyRole = (Boolean)getSession().getServletContext().getAttribute("userToEmptyRole");
		if (userToEmptyRole) {
			
		} else {
			//userRoleRelService.findUserCountByRoleId(getId());
		}
		try {
			//roleService.delete(getId());
		} catch (ServiceException e) {
			rr.setResultCode(ResposeResult.ERROR_CODE);
			rr.setMsg(e.getMessage());
		}
		renderJSON(rr);
		return null;
	}
	
	/**
	 * 新增时得到最大排序号
	 * @Date 2016-8-7上午11:18:21
	 * @return
	 * @throws Exception
	 */
	public String showOrder() throws Exception {
		ResposeResult rr = ResposeResult.successResult();
		try {
			rr.setMsg(String.valueOf(roleService.generateOrder()));
		} catch (Exception e) {
			rr.setResultCode(ResposeResult.ERROR_CODE);
			rr.setMsg(e.getMessage());
		}
		renderJSON(rr);
		return null;
	}
	
	@Override
	public String input() throws Exception {
		//修改时得到角色的菜单编码
		List<RoleMenuRelEntity> rmList = roleMenuRelService.findByRoleId(getId());
		int size = (rmList == null) ? 0 : rmList.size();
		if (size > 0) {
			StringBuilder menuCodes = new StringBuilder();
			for (RoleMenuRelEntity rm : rmList) {
				menuCodes.append(rm.getMenuCode()+",");
			}
			getRequest().setAttribute("menuCodes", menuCodes.substring(0, menuCodes.length()-1));
		}
		return super.input();
	}
	
	/**
	 * 跳转到角色授权页面
	 * @Date 2017-2-19上午11:54:43
	 * @return
	 * @throws Exception
	 */
	public String auth() throws Exception {
		//修改时得到角色的菜单编码
		List<RoleMenuRelEntity> rmList = roleMenuRelService.findByRoleId(getId());
		int size = (rmList == null) ? 0 : rmList.size();
		if (size > 0) {
			StringBuilder menuCodes = new StringBuilder();
			for (RoleMenuRelEntity rm : rmList) {
				menuCodes.append(rm.getMenuCode()+",");
			}
			getRequest().setAttribute("menuCodes", menuCodes.substring(0, menuCodes.length()-1));
		}
		return "auth";
	}
	
	/**
	 * 角色授权
	 * @Date 2017-2-19下午9:52:53
	 * @return
	 * @throws Exception
	 */
	public String roleAuth() throws Exception {
		ResposeResult rr = ResposeResult.successResult();
		try {
			roleMenuRelService.updateByRoleId(getId(), menuPrivils);
		} catch (Exception e) {
			rr.setResultCode(ResposeResult.ERROR_CODE);
			rr.setMsg(e.getMessage());
		}
		renderJSON(rr);
		return null;
	}


	public String getRoleName() {
		return roleName;
	}

	public void setRoleName(String roleName) {
		this.roleName = roleName;
	}

	public Integer getRoleLevel() {
		return roleLevel;
	}

	public void setRoleLevel(Integer roleLevel) {
		this.roleLevel = roleLevel;
	}

	public String getRoleNote() {
		return roleNote;
	}

	public void setRoleNote(String roleNote) {
		this.roleNote = roleNote;
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

	public Integer getRoleOrder() {
		return roleOrder;
	}

	public void setRoleOrder(Integer roleOrder) {
		this.roleOrder = roleOrder;
	}

	public String getMenuPrivils() {
		return menuPrivils;
	}

	public void setMenuPrivils(String menuPrivils) {
		this.menuPrivils = menuPrivils;
	}

	public String getSort() {
		return sort;
	}

	public void setSort(String sort) {
		this.sort = sort;
	}

	public String getOrder() {
		return order;
	}

	public void setOrder(String order) {
		this.order = order;
	}
}
