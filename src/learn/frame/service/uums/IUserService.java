package learn.frame.service.uums;

import java.util.Date;
import java.util.List;

import learn.frame.common.Page;
import learn.frame.common.base.IBaseService;
import learn.frame.entity.uums.UserEntity;
import learn.frame.exception.ServiceException;
import learn.frame.vo.UserVo;

/**
 * 用户service类
 * @Date 2016-3-12 下午11:42:17
 */
public interface IUserService extends IBaseService<UserEntity> {
	/**
	 * 根据用户名和密码得到登陆用户，如果用户名存在重复则抛出异常提示
	 * @Date 2016-8-6下午8:21:16
	 * @param userName 用户名
	 * @param password 密码
	 * @return
	 */
	public UserEntity findLoginUser(String userName, String password);
	/**
	 * 系统管理员重置用户密码，当用户忘记密码时可用联系系统管理员重置密码，然后个人再修改密码
	 * @Date 2016-8-6下午8:25:22
	 * @param ids 用户ID列表,多个用户ID以英文逗号隔开
	 * @param password 重置用户的密码
	 */
	public void resetPassword(String ids, String password);
	/**
	 * 根据用户名查询未删除的用户
	 * @Date 2016-8-6下午8:25:22
	 * @param userName 用户名
	 * @return
	 */
	public UserEntity findByUserName(String userName);
	/**
	 * 保存用户，包括用户所属角色和部门关联
	 * @Date 2016-9-10下午11:09:08
	 * @param user 用户对象
	 * @param roleIds 角色IDs
	 * @param unitCodes 部门编码s
	 * @throws ServiceException
	 */
	public void saveOrUpdate(UserEntity user, String roleIds, String unitCodes) throws ServiceException;
	/**
	 * 更新最近一次登录信息，包括登陆IP和登陆时间信息
	 * @Date 2016-8-6下午8:14:47
	 * @param id 主键
	 * @param loginIP 登陆IP
	 * @param loginTime 登陆时间
	 * @return
	 */
	public UserEntity updateLogin(String id, String loginIP, Date loginTime);
	
	/**
	 * 更新最近一次登录时间
	 * @Date 2015-11-5下午9:52:33
	 * @param t
	 * @return
	 */
	public Page<UserEntity> findByPageOneToOne(Page<UserEntity> page, UserEntity t);
	/**
	 * 分页查询用户列表，包括角色名称和部门名称
	 * @Date 2016-9-10下午2:22:32
	 * @param page 分页对象
	 * @param user 用户对象
	 * @return
	 */
	public Page<UserVo> findByPage(Page<UserVo> page, UserVo user);
	/**
	 * 根据用户ID查询该用户拥有的部门和角色信息
	 * @Date 2016-8-7上午9:56:37
	 * @param userId 用户ID
	 * @return
	 */
	public UserEntity findUnitRoleByUserId(String userId);
	/**
	 * 查询用户数量
	 * @Date 2016-1-17上午11:27:06
	 * @param user
	 * @return
	 */
	long findCount(UserEntity user);
	/**
	 * 查询所有用户列表，包括部门和角色信息
	 * @Date 2016-10-7下午8:52:09
	 * @param user 
	 * @return
	 */
	List<UserVo> findByAll(UserVo user);

}
