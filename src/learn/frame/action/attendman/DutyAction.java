package learn.frame.action.attendman;

import java.util.List;

import javax.annotation.Resource;

import learn.frame.action.BaseAction;
import learn.frame.entity.attendman.DutyEntity;
import learn.frame.service.attendman.IDutyService;

import org.apache.struts2.convention.annotation.ParentPackage;

/**
 * 周值班表
 * @Date 2015-11-25 下午9:45:58
 * @author huangxl
 */
public class DutyAction extends BaseAction {

	private static final long serialVersionUID = 1L;
	
	@Resource
	private IDutyService dutyService;
	
	/**
	 * 显示周值班表表格
	 * @Date 2016-2-20下午10:15:29
	 * @return
	 * @throws Exception
	 */
	public String showDutyTable() throws Exception {
		String dateStr = getRequest().getParameter("dateStr");
		List<DutyEntity> dutyList = dutyService.findByWeeks(dateStr);
		renderJSON(dutyList);
		return null;
	}
}
