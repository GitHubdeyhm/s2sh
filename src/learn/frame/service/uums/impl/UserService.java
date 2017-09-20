/**
 * 用户权限管理包
 */
package learn.frame.service.uums.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import learn.frame.common.Constant;
import learn.frame.common.MessageConstant;
import learn.frame.common.Page;
import learn.frame.common.dao.IBaseDaoHibernate;
import learn.frame.common.dao.impl.BaseDaoHibernate;
import learn.frame.entity.uums.UserEntity;
import learn.frame.exception.ServiceException;
import learn.frame.service.uums.IUserRoleRelService;
import learn.frame.service.uums.IUserService;
import learn.frame.service.uums.IUserUnitRelService;
import learn.frame.utils.DateUtil;
import learn.frame.utils.StringUtil;
import learn.frame.utils.TypeConverUtil;
import learn.frame.utils.file.FileUploadUtil;
import learn.frame.vo.UserVo;

import org.hibernate.NonUniqueResultException;
import org.hibernate.SessionFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 用户service类
 * @Date 2016-8-6 下午7:52:28
 */
@Service
@Transactional(readOnly=true)
public class UserService implements IUserService {
	
	private IBaseDaoHibernate<UserEntity, String> baseDao;//dao
	
	@Resource
	private IUserRoleRelService userRoleRelService;
	@Resource
	private IUserUnitRelService userUnitRelService;
	
	@Resource
	public void setSessionFactory(SessionFactory sessionFactory){
		baseDao = new BaseDaoHibernate<UserEntity, String>(sessionFactory, 
				UserEntity.class);
	}

	@Override
	public UserEntity findById(String id) {
		if (StringUtil.isNullOrEmpty(id)) {
			return null;
		}
		//查询为删除的数据
		String hql = "from UserEntity where id = ? and deleteFlag = ?";
		return baseDao.get(hql, id, Constant.DELETE_FLAG_FALSE);
	}

	/**
	 * 通过用户ID列表删除，多个用户ID以英文逗号隔开
	 */
	@Override
	@Transactional(readOnly=false)
	public void delete(String ids) {
		if (!StringUtil.isNullOrEmpty(ids)) {
			String[] idArr = ids.split(",");
			try {
				//删除用户角色关联
				userRoleRelService.deleteByUserIds(idArr);
				//删除用户部门关联
				userUnitRelService.deleteByUserIds(idArr);
				//删除用户
				int size = idArr.length;
				if (size > 0) {
					//删除用户
					StringBuilder hql = new StringBuilder("update UserEntity set deleteFlag = ?");
					hql.append(" where id in (");
					for (int i = 0; i < size; i++) {
						if (i+1 == size) {
							hql.append(" '"+idArr[i]+"')");
						} else {
							hql.append(" '"+idArr[i]+"',");
						}
					}
					baseDao.executeHql(hql.toString(), Constant.DELETE_FLAG_TRUE);
				}
			} catch (Exception e) {
				log.error(e);
				throw new ServiceException(MessageConstant.DB_IO_ERROR);
			}
		}
	}

	@Override
	@Transactional(readOnly=false)
	public UserEntity saveOrUpdate(UserEntity t) throws ServiceException {
		return null;
	}
	
	@Override
	@Transactional(readOnly=false)
	public void saveOrUpdate(UserEntity user, String roleIds, String unitCodes) throws ServiceException {
		if (user != null) {
			try {
				if (isExistUserName(user.getId(), user.getUserName())) {
					throw new ServiceException("user_name_exist_msg");
				}
				String imgUrl = user.getHeadImgUrl();
				String fileName = null;
				if (!StringUtil.isNullOrEmpty(imgUrl)) {
					//重新命名文件名称
					fileName = StringUtil.getUUID()+imgUrl.substring(imgUrl.lastIndexOf("."));
					user.setHeadImgUrl(fileName+"#"+imgUrl);
					//上传文件名称过长
					if (user.getHeadImgUrl().length() > Constant.FILENAME_ALLOW_LENGTH) {
						throw new ServiceException(MessageConstant.FILENAME_LENGTH_MSG);
					}
				}
				//保存用户
				baseDao.saveOrUpdate(user);
				
				String userId = user.getId();//持久状态的ID
				//先根据用户ID删除用户角色关联
				userRoleRelService.deleteByUserIds(userId);
				userUnitRelService.deleteByUserIds(userId);
				//保存用户角色关联
				userRoleRelService.save(userId, roleIds);
				//保存用户部门关联
				userUnitRelService.save(userId, unitCodes);
				
				//保存图片信息
				if (fileName != null) {
					FileUploadUtil.saveFile(user.getHeadImgUrlFile(), fileName);
				}
			} catch (ServiceException e) {
				throw e;
			} catch (Exception e) {
				log.error(e);
				throw new ServiceException(MessageConstant.DB_IO_ERROR);
			}
		}
	}

	@Override
	@Transactional(readOnly=false)
	public UserEntity updateLogin(String id, String loginIP, Date loginTime) {
		if (!StringUtil.isNullOrEmpty(id) && loginTime != null) {
			String hql = "update UserEntity u set u.loginIP = ?, u.loginTime = ? where id = ?";
			try {
				baseDao.executeHql(hql, loginIP, loginTime, id);
			} catch (Exception e) {
				log.error(e);
				throw new ServiceException(MessageConstant.DB_IO_ERROR);
			}
		}
		return null;
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public Page<UserVo> findByPage(Page<UserVo> page, UserVo user) {
		StringBuilder sql = new StringBuilder();
		List<Object> paramList = new ArrayList<>();
		sql.append("SELECT tu.* FROM t_uums_user tu");
		if (user != null) {
			if (!StringUtil.isNullOrEmpty(user.getRoleIds())) {
				sql.append(" JOIN (");
				sql.append(" SELECT user_id FROM t_uums_user_role_rel");
				sql.append(" WHERE role_id = ?) tur ON tu.id=tur.user_id");
				paramList.add(user.getRoleIds());
			}
			if (!StringUtil.isNullOrEmpty(user.getUnitCodes())) {
				sql.append(" JOIN (");
				sql.append(" SELECT DISTINCT user_id FROM t_uums_user_unit_rel WHERE unit_code like ?");
				sql.append(" ) tuu ON tu.id=tuu.user_id");
				paramList.add(user.getUnitCodes()+"%");
			}
			sql.append(" WHERE tu.delete_flag = ?");//未删除用户
			paramList.add(Constant.DELETE_FLAG_FALSE);
			if (!StringUtil.isNullOrEmpty(user.getUserName())) {
				sql.append(" AND tu.user_name like ? escape '/'");
				String userName = StringUtil.hqlEscape(user.getUserName()).trim();
				paramList.add("%"+userName+"%");
			}
			if (!StringUtil.isNullOrEmpty(user.getRealName())) {
				sql.append(" AND tu.real_name like ? escape '/'");
				String realName = StringUtil.hqlEscape(user.getRealName()).trim();
				paramList.add("%"+realName+"%");
			}
		}
		String queryStr = sql.toString();
		List<UserEntity> userList = baseDao.createSQLQuery(queryStr+" ORDER BY tu.create_time DESC", paramList.toArray())
			.addEntity(UserEntity.class)
			.setFirstResult(page.getPageSize()*(page.getPageNum()-1))
			.setMaxResults(page.getPageSize()).list();
		//设置分页记录数
		page.setList(addUnitRoles(userList));
		//查询总记录数
		if (userList != null && userList.size() > 0) {
			String countSql = "select count(*) " + queryStr.substring(queryStr.indexOf("FROM"));
			Object obj = baseDao.createSQLQuery(countSql, paramList.toArray()).uniqueResult();
			page.setTotalNum(TypeConverUtil.objToLong(obj));
		}
		return page;
	}

	/**
	 * 只查询人员相关信息，不查询出部门和角色
	 * @Date 2016-9-10下午11:20:28
	 * @param p 分页对象
	 * @param t 用户对象实体
	 */
	public Page<UserEntity> findByPage(Page<UserEntity> p, UserEntity t) {
		List<Object> paramList = new ArrayList<>();
		//不查出超级管理员用户
		StringBuilder hql = new StringBuilder("from UserEntity u where 1=1 ");
		if (t != null) {
			if (!StringUtil.isNullOrEmpty(t.getRealName())) {
				t.setRealName(StringUtil.hqlEscape(t.getRealName()));
				hql.append(" and u.realName like ? ");
				paramList.add("%"+t.getRealName().trim()+"%");
			}
			if (t.getGender() != null) {
				hql.append(" and u.gender = ?");
				paramList.add(t.getGender());
			}
		}
		hql.append(" order by u.createTime desc");
		return baseDao.findByPage(p, hql.toString(), paramList.toArray());
		
		/*if (t != null) {
			StringBuilder sql = new StringBuilder("SELECT tu.* from t_user tu");
			//添加角色查询
			if (!StringUtil.isNullOrEmpty(t.getRoleIds())) {
				sql.append(" join t_user_role_rel tur on tur.user_id=tu.user_id");
				sql.append(" and tur.role_id = ?");
				paramList.add(t.getRoleIds());
			}
			//添加部门查询--包括子部门
			if (!StringUtil.isNullOrEmpty(t.getUnitCodes())) {
				sql.append(" join (select DISTINCT user_id from t_user_unit_rel");
				sql.append(" where unit_code like ?) tuu on tuu.user_id=tu.user_id");
				paramList.add(t.getUnitCodes()+"%");
			}
			sql.append(" where 1=1");//动态添加查询条件
			//真实姓名模糊查询
			if (!StringUtil.isNullOrEmpty(t.getRealName())) {
				t.setRealName(StringUtil.hqlEscape(t.getRealName()));
				sql.append(" and u.realName like ? ");
				paramList.add("%"+t.getRealName().trim()+"%");
			}
			sql.append(" and tu.user_id <> ?");//不查出超级管理员
			paramList.add(Constant.ADMIN_USER_ID);
			sql.append(" order by tu.create_time desc");
			
			String queryString = sql.toString();
			try {
				List<UserEntity> userList = baseDao.createSQLQuery(queryString, paramList.toArray()).addEntity(UserEntity.class).
						setFirstResult((p.getPageNum()-1)*p.getPageSize()).setMaxResults(p.getPageSize()).list();
				if (userList != null && userList.size() > 0) {
					//查询总记录数
					String qs = "select count(*) " + queryString.substring(queryString.indexOf("from"));
					Object count = baseDao.createSQLQuery(qs, paramList.toArray()).uniqueResult();
					p.setTotalNum(TypeConverUtil.obj2Long(count));
					p.setList(addUnitRoles(userList));//为查询出来的用户添加角色
				}
			} catch (Exception e) {
				log.error(e.getMessage());
				throw new ServiceException(MessageConstant.DB_QUERY_EXCEPTION);
			}*/
//		}
	}
	
	/**
	 * 为查询出来的用户列表添加部门和角色，存在多个部门和多个角色
	 * @Date 2016-3-13上午1:40:13
	 * @param userList 用户列表
	 * @return 封装的用户列表
	 */
	@SuppressWarnings("unchecked")
	private List<UserVo> addUnitRoles(List<UserEntity> userList) {
		List<UserVo> userVoList = null;
		int size = (userList == null) ? 0 : userList.size();
		if (size > 0) {
			userVoList = new ArrayList<>();
			//根据用户ID查询用户所属角色
			StringBuilder sql = new StringBuilder("SELECT tur.user_id, tr.id, tr.role_name");
				sql.append(" FROM t_uums_role tr JOIN t_uums_user_role_rel tur ON tr.id=tur.role_id");
				sql.append(" WHERE tur.user_id in ( ");
				
			//根据用户ID查询用户所属部门
			StringBuilder unitSql = new StringBuilder("SELECT tuu.user_id, tu.id, tu.unit_name, tu.unit_code ");
				unitSql.append(" FROM t_uums_unit tu JOIN t_uums_user_unit_rel tuu ON tu.unit_code=tuu.unit_code");
				unitSql.append(" WHERE tu.delete_flag=0 AND tuu.user_id in ( ");
				
			for (int i = 0; i < size; i++) {
				String userId = userList.get(i).getId();
				if (i + 1 == size) {
					sql.append("'"+userId+"')");
					unitSql.append("'"+userId+"')");
				} else {
					sql.append("'"+userId+"', ");
					unitSql.append("'"+userId+"', ");
				}
			}
			List<Object[]> roleList = baseDao.createSQLQuery(sql.toString()).list();
			List<Object[]> unitList = baseDao.createSQLQuery(unitSql.toString()).list();
			
			int roleSize = (roleList == null) ? 0 : roleList.size();
			int unitSize = (unitList == null) ? 0 : unitList.size();
			
			for (UserEntity user : userList) {
				UserVo userVo = new UserVo();
				String userId = user.getId();
				
				//设置用户基础信息
				userVo.setId(user.getId());
				userVo.setUserName(user.getUserName());
				userVo.setRealName(user.getRealName());
				userVo.setGender(user.getGender());
				userVo.setEmail(user.getEmail());
				userVo.setPhone(user.getPhone());
				userVo.setLoginIP(user.getLoginIP());
				userVo.setHeadImgUrl(user.getHeadImgUrl());
				userVo.setLoginTime(DateUtil.formateTime(user.getLoginTime()));
				userVo.setCreateTime(DateUtil.formateTime(user.getCreateTime()));
				
				//保存用户所属角色和部门
				StringBuilder roleIds = new StringBuilder();
				StringBuilder roleNames = new StringBuilder();
				StringBuilder unitIds = new StringBuilder();
				StringBuilder unitCodes = new StringBuilder();
				StringBuilder unitNames = new StringBuilder();
				
				for (int i = 0; i < roleSize; i++) {
					Object[] obj = roleList.get(i);
					//为用户添加角色
					if (userId.equals(obj[0])) {
						roleIds.append(obj[1]+",");
						roleNames.append(obj[2]+",");
					}
				}
				for (int i = 0; i < unitSize; i++) {
					Object[] obj = unitList.get(i);
					//为用户添加部门
					if (userId.equals(obj[0])) {
						unitIds.append(obj[1]+",");
						unitNames.append(obj[2]+",");
						unitCodes.append(obj[3]+",");
					}
				}
				if (roleIds.length() > 1) {
					userVo.setRoleIds(roleIds.substring(0, roleIds.length()-1));
				}
				if (roleNames.length() > 1) {
					userVo.setRoleNames(roleNames.substring(0, roleNames.length()-1));
				}
				if (unitIds.length() > 1) {
					userVo.setUnitIds(unitIds.substring(0, unitIds.length()-1));
				}
				if (unitCodes.length() > 1) {
					userVo.setUnitCodes(unitCodes.substring(0, unitCodes.length()-1));
				}
				if (unitNames.length() > 1) {
					userVo.setUnitNames(unitNames.substring(0, unitNames.length()-1));
				}
				userVoList.add(userVo);
			}
		}
		return userVoList;
	}

	/**
	 * 查询用户列表的所有数据，只包括用户相关的数据，不包括部门和角色
	 * @Date 2016-10-7下午6:45:52
	 */
	@Override
	public List<UserEntity> findByAll(UserEntity t) {
		List<Object> paramList = new ArrayList<Object>();
		StringBuilder hql = new StringBuilder("from UserEntity u where 1=1 ");
		if (t != null) {
			if (!StringUtil.isNullOrEmpty(t.getRealName())) {
				hql.append(" and u.realName like ? escape '/'");
				String realName = StringUtil.hqlEscape(t.getRealName()).trim();
				paramList.add("%"+realName+"%");
			}
			if (t.getGender() != null) {
				hql.append(" and u.gender = ?");
				paramList.add(t.getGender());
			}
		}
		hql.append(" order by u.createTime desc");
		return baseDao.find(hql.toString(), paramList.toArray());
	}
	
	@Override
	@SuppressWarnings("unchecked")
	public List<UserVo> findByAll(UserVo user) {
		StringBuilder sql = new StringBuilder();
		List<Object> paramList = new ArrayList<>();
		sql.append("SELECT tu.* FROM t_uums_user tu");
		if (user != null) {
			if (!StringUtil.isNullOrEmpty(user.getRoleIds())) {
				sql.append(" JOIN (");
				sql.append(" SELECT user_id FROM t_uums_user_role_rel");
				sql.append(" WHERE role_id = ?) tur ON tu.id=tur.user_id");
				paramList.add(user.getRoleIds());
			}
			if (!StringUtil.isNullOrEmpty(user.getUnitCodes())) {
				sql.append(" JOIN (");
				sql.append(" SELECT DISTINCT user_id FROM t_uums_user_unit_rel WHERE unit_code like ?");
				sql.append(" ) tuu ON tu.id=tuu.user_id");
				paramList.add(user.getUnitCodes()+"%");
			}
			sql.append(" WHERE tu.delete_flag = ?");//未删除用户
			paramList.add(Constant.DELETE_FLAG_FALSE);
			if (!StringUtil.isNullOrEmpty(user.getUserName())) {
				sql.append(" AND tu.user_name like ? escape '/'");
				String userName = StringUtil.hqlEscape(user.getUserName()).trim();
				paramList.add("%"+userName+"%");
			}
			if (!StringUtil.isNullOrEmpty(user.getRealName())) {
				sql.append(" AND tu.real_name like ? escape '/'");
				String realName = StringUtil.hqlEscape(user.getRealName()).trim();
				paramList.add("%"+realName+"%");
			}
		}
		String queryStr = sql.toString();
		List<UserEntity> userList = baseDao.createSQLQuery(queryStr+" ORDER BY tu.create_time DESC", paramList.toArray())
			.addEntity(UserEntity.class).list();
		return addUnitRoles(userList);
	}
	
	@Override
	public UserEntity findLoginUser(String userName, String password) {
		UserEntity user = null;
		if (!StringUtil.isAnyNullOrEmpty(userName, password)) {
			String hql = "from UserEntity u where u.userName = ? and u.userPwd = ? and deleteFlag = ?";
			try {
				user = (UserEntity)baseDao.findUnique(hql, userName, password, Constant.DELETE_FLAG_FALSE);
			} catch (NonUniqueResultException e) {
				log.error("用户名["+userName+"]重复："+e);
				throw new ServiceException("用户名存在重复，请联系系统管理员");
			} catch (Exception e) {
				log.error(e);
				throw new ServiceException(MessageConstant.DB_QUERY_ERROR);
			}
		}
		return user;
	}
	
	@Override
	@Transactional(readOnly=false)
	public void resetPassword(String ids, String password) {
		if (!StringUtil.isAnyNullOrEmpty(ids, password)) {
			String[] idArr = ids.split(",");
			int len = idArr.length;
			if (len > 0) {
				StringBuilder hql = new StringBuilder("update UserEntity u");
				hql.append(" set u.userPwd = ? where u.id in ( ");
				for (int i = 0; i < len; i++) {
					if (i + 1 == len) {
						hql.append(" '"+idArr[i]+"')");
					} else {
						hql.append(" '"+idArr[i]+"',");
					}
				}
				try {
					baseDao.executeHql(hql.toString(), password);
				} catch (Exception e) {
					log.error(e);
					throw new ServiceException(MessageConstant.DB_IO_ERROR);
				}
			}
		}
	}
	
	@Override
	public UserEntity findByUserName(String userName) {
		if (!StringUtil.isNullOrEmpty(userName)) {
			String hql = "from UserEntity u where u.userName = ? and deleteFlag = ?";
			try {
				baseDao.get(hql, userName, Constant.DELETE_FLAG_FALSE);
			} catch (Exception e) {
				log.error(e);
				throw new ServiceException(MessageConstant.DB_QUERY_ERROR);
			}
		}
		return null;
	}
	
	/**
	 * 判断用户名是否重复，重复返回true，否则为false
	 * @Date 2016-8-6下午8:31:44
	 * @param id 页面传递的用户ID
	 * @param userName 用户名
	 * @return 重复返回true，否则为false
	 */
	private boolean isExistUserName(String id, String userName) {
		String hql = "from UserEntity u where u.userName = ? and deleteFlag = ?";
		List<UserEntity> userList = baseDao.find(hql, userName, Constant.DELETE_FLAG_FALSE);
		if (userList == null || userList.size() == 0) {
			return false;
		}
		if ((userList.size() == 1) && (userList.get(0).getId().equals(id))) {
			return false;
		}
		return true;
	}
	
	@Override
	public UserEntity findUnitRoleByUserId(String userId) {
		/*StringBuilder sql = new StringBuilder("select u.id, u.user_name, u.real_name");
		sql.append(" ,r.id, r.role_name, r.role_level, n.id, n.unit_code, n.unit_name");
		sql.append(" from t_uums_user u");
		sql.append(" left join t_uums_user_role_rel ur on u.id=ur.user_id");
		sql.append(" left join t_uums_role r on ur.role_id=r.role_id");
		sql.append(" left join t_user_unit_rel uu on uu.user_id=u.user_id");
		sql.append(" left join t_uums_unit n on n.unit_id=uu.unit_code");
		sql.append(" where u.user_id = ?");
		List<Object[]> objList = baseDao.createSQLQuery(sql.toString(), userId).list();
		if (objList != null && objList.size() > 0) {
			List<UserEntity> userList = new ArrayList<UserEntity>();
			for (Object[] obj : objList) {
				UserEntity user = new UserEntity();
				user.setUserId((String)obj[0]);
				user.setUserName((String)obj[1]);
				user.setRealName((String)obj[2]);
				user.setRoleIds((String)obj[3]);
				user.setRoleNames((String)obj[4]);
				user.setRoleLevel((Integer)obj[5]);
				user.setUnitIds((String)obj[6]);
				user.setUnitCodes((String)obj[7]);
				user.setUnitNames((String)obj[8]);
				userList.add(user);
			}
			if (userList.size() > 0) {
				return userList.get(0);
			}
		}*/
		return null;
	}
	
	@Override
	public Page<UserEntity> findByPageOneToOne(Page<UserEntity> page, UserEntity t) {
		/*List<Object> paramList = new ArrayList<Object>();
		StringBuilder sql = new StringBuilder("select role.role_id, role.role_code, role.role_name");
		sql.append(" ,unit.unit_id, unit.unit_code, unit.unit_name");
		sql.append(" ,u.user_id, u.user_name, u.real_name, u.is_gender, u.create_time, u.modify_time, u.card_code, u.address");//用户
		sql.append(" ,u.email, u.birthday, u.telphone, u.login_ip, cast(u.duty as signed), cast(u.certificate_type as signed), u.certificate_no");
		sql.append(" from t_ibms_user u");
		sql.append(" join (select ur.user_id, r.role_id, r.role_code, r.role_name, r.role_level");
		sql.append(" from t_ibms_ur_relation ur join t_ibms_role r on r.role_code=ur.role_id) as role on role.user_id=u.user_id");
		sql.append(" left join (SELECT uu.user_id, n.unit_id, n.unit_code, n.unit_name");
		sql.append(" from t_ibms_uu_relation uu join t_ibms_unit n on n.unit_code=uu.unit_id) as unit on unit.user_id=u.user_id");
		sql.append(" where role.role_level > ?");
		paramList.add(Constant.ADMIN_ROLE_LEVEL);//过滤掉超级管理员用户
		if (t != null) {
			if (!StringUtil.isNullOrEmpty(t.getRoleIds())) {
				sql.append(" and role.role_id like ?");
				paramList.add(t.getRoleIds());
			}
			if (!StringUtil.isNullOrEmpty(t.getUnitCodes())) {
				sql.append(" and unit.unit_code like ?");
				paramList.add(t.getUnitCodes()+"%");
			}
			if (!StringUtil.isNullOrEmpty(t.getRealName())) {
				t.setRealName(StringUtil.hqlEscape(t.getRealName()));
				sql.append(" and u.real_name like ? escape '/'");
				paramList.add("%"+t.getRealName().trim()+"%");
			}
			if (!StringUtil.isNullOrEmpty(t.getUserName())) {
				t.setUserName(StringUtil.hqlEscape(t.getUserName()));
				sql.append(" and u.user_name like ? escape '/'");
				paramList.add("%"+t.getUserName().trim()+"%");
			}
			if (t.getGender() != null) {
				sql.append(" and u.is_gender = ?");
				paramList.add(t.getGender());
			}
		}
		String queryString = sql.toString();
		List<Object[]>	objList = baseDao.createSQLQuery(queryString, paramList.toArray()).
			setFirstResult((page.getPageNum()-1)*page.getPageSize()).setMaxResults(page.getPageSize()).list();
		if (objList != null && objList.size() > 0) {
			//查询总记录数
			String qs = "select count(*) " + queryString.substring(queryString.indexOf("from"));
			Object count = baseDao.createSQLQuery(qs, paramList.toArray()).uniqueResult();
			page.setTotalNum((count == null) ? 0 : Long.parseLong(count.toString()));
			//page.setResult(getUserList(objList));
		}*/
		return null;
//		return page;
	}
	
	@Override
	public long findCount(UserEntity user) {
		List<Object> paramList = new ArrayList<Object>();
		//不查出超级管理员用户
		StringBuilder hql = new StringBuilder("select count(*) from UserEntity u where 1=1 ");
//		paramList.add(Constant.ADMIN_ROLE_LEVEL);
		if (user != null) {
			if (!StringUtil.isNullOrEmpty(user.getRealName())) {
				user.setRealName(StringUtil.hqlEscape(user.getRealName()));
				hql.append(" and u.realName like ? ");
				paramList.add("%"+user.getRealName().trim()+"%");
			}
			if (user.getGender() != null) {
				hql.append(" and u.gender = ?");
				paramList.add(user.getGender());
			}
		}
		return baseDao.getTotalNum(hql.toString(), paramList.toArray());
	}
	
	/**
	 * 根据查询出来的对象数组列表转换为对象列表。顺序需保持一致
	 * @Title getUserVoList 
	 * @Description
	 * @Date 2015-10-13 下午05:23:08 
	 * @param userList
	 * @return
	 */
	/*private List<UserEntity> getUserVoList(List<Object[]> objList) {
		List<UserEntity> userVoList = null;
		if (objList != null && objList.size() > 0) {
			userVoList = new ArrayList<UserEntity>();
			for (Object[] obj : objList) {
				UserEntity userVo = new UserEntity();
				userVo.setRoleIds((String)obj[0]);
				userVo.setRoleCodes((String)obj[1]);
				userVo.setRoleNames((String)obj[2]);
				userVo.setUnitIds((String)obj[3]);
				userVo.setUnitCodes((String)obj[4]);
				userVo.setUnitNames((String)obj[5]);
				userVo.setUserId((String)obj[6]);
				userVo.setUserName((String)obj[7]);
				userVo.setRealName((String)obj[8]);
				if (obj[9] != null) {
					userVo.setIsGender((Boolean)obj[9]);
					userVo.setGenderStr(userVo.getIsGender() ? "女" : "男");
				}
				userVo.setCreateTime((Date)obj[10]);
				userVo.setModifyTime((Date)obj[11]);
				userVo.setCardCode((String)obj[12]);
				userVo.setAddress((String)obj[13]);
				userVo.setEmail((String)obj[14]);
				userVo.setBirthday((Date)obj[15]);
				userVo.setTelphone((String)obj[16]);
				userVo.setLoginIp((String)obj[17]);
				userVo.setCertificateNo((String)obj[20]);
				
				userVoList.add(userVo);
			}
		}
		return userVoList;
	}*/
}
