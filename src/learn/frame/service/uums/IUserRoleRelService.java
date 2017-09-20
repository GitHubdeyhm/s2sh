package learn.frame.service.uums;

import java.util.List;

import learn.frame.common.base.IBaseService;
import learn.frame.entity.uums.UserRoleRelEntity;

public interface IUserRoleRelService extends IBaseService<UserRoleRelEntity> {
	/**
	 * 通过用户ID列表删除用户角色关联
	 * @Date 2015-10-21下午8:40:51
	 * @param userIds 用户ID列表
	 */
	public void deleteByUserIds(String... userIds);
	/**
	 * 通过用户ID删除
	 * @Date 2015-10-21下午8:40:51
	 * @param userId
	 */
	public void deleteByRoleIds(String... roleIds);
	/**
	 * 通过角色ID删除
	 * @Date 2015-10-21下午8:42:15
	 * @param roleId
	 */
	public void deleteByRoleId(String roleId);
	/**
	 * 通过用户ID查询
	 * @Date 2015-10-21下午8:46:32
	 * @param userId
	 * @return
	 */
	public List<UserRoleRelEntity> findByUserId(String userId);
	/**
	 * 通过角色ID查询
	 * @Date 2015-10-21下午8:47:01
	 * @param roleId
	 * @return
	 */
	public List<UserRoleRelEntity> findByRoleId(String roleId);
	/**
	 * 保存用户角色关联关系
	 * @Date 2015-10-21下午8:47:26
	 * @param userId：用户ID
	 * @param roleIds：角色IDs，多个角色ID以英文逗号隔开
	 */
	public void save(String userId, String roleIds);
	/**
	 * 通过角色ID查询角色用户数
	 * @Date 2017-2-19上午11:46:33
	 * @param roleIds
	 * @return
	 */
	int findUserCountByRoleId(String roleIds);
}
