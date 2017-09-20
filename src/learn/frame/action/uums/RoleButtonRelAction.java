package learn.frame.action.uums;

import java.util.List;

import javax.annotation.Resource;

import learn.frame.action.BaseAction;
import learn.frame.common.Constant;
import learn.frame.common.ResposeResult;
import learn.frame.common.easyui.Datagrid;
import learn.frame.entity.uums.ButtonEntity;
import learn.frame.entity.uums.RoleButtonRelEntity;
import learn.frame.exception.ServiceException;
import learn.frame.service.uums.IButtonService;
import learn.frame.service.uums.IRoleButtonRelService;
import learn.frame.utils.StringUtil;

/**
 * @Date 2016-5-1 下午3:11:51
 */
public class RoleButtonRelAction extends BaseAction {
	
	private static final long serialVersionUID = 1L;
	
	@Resource
	private IRoleButtonRelService roleButtonRelService;
	
	//按钮ID
	private String buttonIds;
	//角色ID
	private String roleId;
	//角色编码
	private String menuCode;

	
	@Override
	public String showList() throws Exception {
		List<ButtonEntity> btnList = null;
		try {
			btnList = roleButtonRelService.findByRoleIdAndMenuCode(roleId, menuCode);
		} catch (Exception e) {
			e.printStackTrace();
		}
		//菜单编码不能为空
		Datagrid<ButtonEntity> dg = new Datagrid<ButtonEntity>(btnList);
		renderJSON(dg);
		return null;
	}
	
	@Override
	public String save() throws Exception {
		ResposeResult rr = ResposeResult.successResult();
		try {
			RoleButtonRelEntity bbr = roleButtonRelService.findById(getId());
			if (bbr == null) {
				bbr = new RoleButtonRelEntity();
			}
//			bbr.setButtonId(buttonId);
//			bbr.setRoleId(roleId);
//			buttonService.saveOrUpdate(btn);
		} catch(ServiceException e) {
			rr.setResultCode(ResposeResult.ERROR_CODE);
			rr.setMsg(e.getMessage());
		}
		renderJSON(rr);
		return null;
	}
	
	@Override
	public String delete() throws Exception {
		ResposeResult rr = ResposeResult.successResult();
		try {
			roleButtonRelService.delete(getId());
		} catch (ServiceException e) {
			rr.setResultCode(ResposeResult.ERROR_CODE);
			rr.setMsg(e.getMessage());
		}
		renderJSON(rr);
		return null;
	}

	public String getButtonIds() {
		return buttonIds;
	}

	public void setButtonIds(String buttonIds) {
		this.buttonIds = buttonIds;
	}

	public String getRoleId() {
		return roleId;
	}

	public void setRoleId(String roleId) {
		this.roleId = roleId;
	}

	public String getMenuCode() {
		return menuCode;
	}

	public void setMenuCode(String menuCode) {
		this.menuCode = menuCode;
	}
}
