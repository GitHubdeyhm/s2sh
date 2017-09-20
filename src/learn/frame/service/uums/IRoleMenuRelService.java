package learn.frame.service.uums;

import java.util.List;

import learn.frame.common.base.IBaseService;
import learn.frame.entity.uums.RoleMenuRelEntity;

public interface IRoleMenuRelService extends IBaseService<RoleMenuRelEntity> {
	/**
	 * 通过角色ID删除
	 * @Date 2015-10-24下午10:30:42
	 * @param roleId
	 */
	public void deleteByRoleId(String roleId);
	/**
	 * 通过菜单编码删除，包括子菜单
	 * @Date 2015-10-24下午10:30:56
	 * @param menuCode
	 */
	public void deleteByMenuCode(String menuCode);
	/**
	 * 通过角色ID查询关联
	 * @Date 2015-10-24下午10:31:13
	 * @param roleId
	 * @return
	 */
	public List<RoleMenuRelEntity> findByRoleId(String roleId);
	/**
	 * 通过多个角色IDs查询关联，返回菜单编码列表
	 * @Date 2015-10-24下午10:31:13
	 * @param roleIds：角色ID列表，多个角色Id以英文逗号隔开
	 * @return
	 */
	public List<String> findByRoleIds(String roleIds);
	/**
	 * 通过菜单编码查询，不包括子菜单
	 * @Date 2015-10-24下午10:31:25
	 * @param menuCode
	 * @return
	 */
	public List<RoleMenuRelEntity> findByMenuCode(String menuCode);
	/**
	 * 保存角色菜单关联
	 * @Date 2015-10-24下午10:32:14
	 * @param roleId：角色ID
	 * @param menuCodes：菜单编码，多个编码以英文逗号隔开
	 */
	public void save(String roleId, String menuCodes);
	/**
	 * 通过角色ID数组删除角色按钮关联
	 * @Date 2016-8-20下午11:13:24
	 * @param roleIds 角色ID数组
	 */
	void deleteByRoleIds(String... roleIds);
	/**
	 * 通过角色ID更新角色菜单关联
	 * @Date 2017-2-19下午9:57:57
	 * @param roleId 角色ID
	 * @param menuCodes 菜单编码数组
	 */
	void updateByRoleId(String roleId, String menuCodes);
}
