package learn.frame.service.uums;

import java.util.List;

import learn.frame.common.base.IBaseService;
import learn.frame.entity.uums.ButtonEntity;
import learn.frame.entity.uums.RoleButtonRelEntity;

public interface IRoleButtonRelService extends IBaseService<RoleButtonRelEntity> {
	/**
	 * 通过角色ID删除
	 * @Date 2015-10-24下午10:30:42
	 * @param roleId
	 */
	public void deleteByRoleId(String roleId);
	/**
	 * 通过角色ID查询关联
	 * @Date 2015-10-24下午10:31:13
	 * @param roleId
	 * @return
	 */
	public List<RoleButtonRelEntity> findByRoleId(String roleId);
	/**
	 * 通过多个角色IDs查询关联，返回菜单编码列表
	 * @Date 2015-10-24下午10:31:13
	 * @param roleIds：角色ID列表，多个角色Id以英文逗号隔开
	 * @return
	 */
	public List<String> findByRoleIds(String roleIds);
	/**
	 * 保存角色按钮关联
	 * @Date 2015-10-24下午10:32:14
	 * @param roleId：角色ID
	 * @param buttonIds：按钮ID，多个ID以英文逗号隔开
	 */
	public void save(String roleId, String buttonIds);
	/**
	 * 通过角色ID和菜单编码查询按钮
	 * @Date 2016-5-6下午11:17:09
	 * @param roleId 角色ID
	 * @param menuCode 菜单编码
	 * @return
	 */
	List<ButtonEntity> findByRoleIdAndMenuCode(String roleId, String menuCode);
	/**
	 * 通过多个角色ID删除
	 * @Date 2016-8-20下午11:06:04
	 * @param roleIds 角色ID数组
	 */
	void deleteByRoleIds(String... roleIds);
}
