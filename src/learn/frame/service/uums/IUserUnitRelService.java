package learn.frame.service.uums;

import java.util.List;

import learn.frame.common.base.IBaseService;
import learn.frame.entity.uums.UserUnitRelEntity;

public interface IUserUnitRelService extends IBaseService<UserUnitRelEntity> {
	/**
	 * 通过用户ID删除用户部门关联
	 * @Date 2015-10-24下午7:58:05
	 * @param userIds 用户ID列表
	 */
	public void deleteByUserIds(String... userIds);
	/**
	 * 根据部门编码删除，包括子部门
	 * @Date 2015-10-24下午7:59:11
	 * @param unitCode 部门编码
	 */
	public void deleteByUnitCode(String unitCode);
	/**
	 * 根据用户ID查询
	 * @Date 2015-10-24下午7:59:33
	 * @param userId
	 * @return
	 */
	public List<UserUnitRelEntity> findByUserId(String userId);
	/**
	 * 根据编码查询，不包括子部门
	 * @Date 2015-10-24下午7:59:56
	 * @param unitCode
	 * @return
	 */
	public List<UserUnitRelEntity> findByUnitCode(String unitCode);
	/**
	 * 保存用户部门关联关系
	 * @Date 2015-10-24下午8:00:07
	 * @param userId：用户ID
	 * @param unitCodes：部门编码，多个部门编码以英文逗号隔开
	 */
	public void save(String userId, String unitCodes);
}
