package learn.frame.service.uums;

import java.util.List;

import learn.frame.common.base.IBaseService;
import learn.frame.common.easyui.Combobox;
import learn.frame.entity.uums.RoleEntity;

/**
 * 角色service
 * @Date 2016-8-6下午10:50:19
 */
public interface IRoleService extends IBaseService<RoleEntity> {
	
	/**
	 * 以下拉框显示角色数据，defaultText为默认值
	 * @Date 2015-10-21下午9:09:40
	 * @param defaultText 默认文本值，比如为：全部或者请选择
	 * @return
	 */
	public List<Combobox> findForCombobox(String defaultText);

	/**
	 * 得到新增时生成排序号
	 * @Date 2016-8-7下午12:31:40
	 * @return
	 */
	int generateOrder();

}
