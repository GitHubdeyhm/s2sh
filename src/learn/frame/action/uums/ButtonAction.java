package learn.frame.action.uums;

import java.util.List;

import javax.annotation.Resource;

import learn.frame.action.BaseAction;
import learn.frame.common.Constant;
import learn.frame.common.ResposeResult;
import learn.frame.common.easyui.Datagrid;
import learn.frame.entity.uums.ButtonEntity;
import learn.frame.exception.ServiceException;
import learn.frame.service.uums.IButtonService;
import learn.frame.utils.StringUtil;

/**
 * @Date 2016-5-1 下午3:11:51
 */
public class ButtonAction extends BaseAction {
	
	@Resource
	private IButtonService buttonService;
	
	//按钮名称，标示，统一定好的，不能随意输入
	private String buttonName;
	//按钮标识
	private String buttonMark;
	//菜单编码
	private String menuCode;
	//按钮排序
	private String buttonOrder;
	//按钮说明
	private String buttonNote;

	private static final long serialVersionUID = 1L;
	
	@Override
	public String showList() throws Exception {
		List<ButtonEntity> btnList = null;
		//菜单编码不能为空
		if (!StringUtil.isNullOrEmpty(menuCode) &&  !Constant.COMBO_DEFAULT_VALUE.equals(menuCode)) {
			ButtonEntity btn = new ButtonEntity();
			btn.setButtonMark(buttonMark);
			btn.setMenuCode(menuCode);
			try {
				btnList = buttonService.findByAll(btn);
			} catch (Exception e) {
				log.error(e);
			}
		}
		Datagrid<ButtonEntity> dg = new Datagrid<ButtonEntity>();
		dg.setRows(btnList);
		renderJSON(dg);
		return null;
	}
	
	@Override
	public String save() throws Exception {
		ResposeResult rr = ResposeResult.successResult();
		try {
			ButtonEntity btn = buttonService.findById(getId());
			if (btn == null) {
				btn = new ButtonEntity();
				btn.setMenuCode(menuCode);
			}
			btn.setButtonName(buttonName);
			btn.setButtonMark(buttonMark);
			btn.setButtonNote(buttonNote);
			btn.setButtonOrder(2);
			buttonService.saveOrUpdate(btn);
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
			buttonService.delete(getId());
		} catch (ServiceException e) {
			rr.setResultCode(ResposeResult.ERROR_CODE);
			rr.setMsg(e.getMessage());
		}
		renderJSON(rr);
		return null;
	}

	public IButtonService getButtonService() {
		return buttonService;
	}

	public void setButtonService(IButtonService buttonService) {
		this.buttonService = buttonService;
	}

	public String getButtonName() {
		return buttonName;
	}

	public void setButtonName(String buttonName) {
		this.buttonName = buttonName;
	}

	public String getButtonMark() {
		return buttonMark;
	}

	public void setButtonMark(String buttonMark) {
		this.buttonMark = buttonMark;
	}

	public String getMenuCode() {
		return menuCode;
	}

	public void setMenuCode(String menuCode) {
		this.menuCode = menuCode;
	}

	public String getButtonOrder() {
		return buttonOrder;
	}

	public void setButtonOrder(String buttonOrder) {
		this.buttonOrder = buttonOrder;
	}

	public String getButtonNote() {
		return buttonNote;
	}

	public void setButtonNote(String buttonNote) {
		this.buttonNote = buttonNote;
	}

}
