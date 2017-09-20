package learn.frame.service.uums.impl;

import java.util.List;

import javax.annotation.Resource;

import learn.frame.common.Constant;
import learn.frame.common.MessageConstant;
import learn.frame.common.Page;
import learn.frame.common.dao.IBaseDaoHibernate;
import learn.frame.common.dao.impl.BaseDaoHibernate;
import learn.frame.entity.uums.UserRoleRelEntity;
import learn.frame.exception.ServiceException;
import learn.frame.service.uums.IUserRoleRelService;
import learn.frame.utils.StringUtil;

import org.hibernate.SessionFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
/**
 * 用户角色关联Service
 * @Date 2016-2-5 下午4:56:49
 */
@Service
@Transactional(readOnly=true)
public class UserRoleRelService implements IUserRoleRelService {
	
	private IBaseDaoHibernate<UserRoleRelEntity, String> baseDao;//dao
	
	@Resource
	public void setSessionFactory(SessionFactory sessionFactory){
		baseDao = new BaseDaoHibernate<UserRoleRelEntity, String>(sessionFactory, 
				UserRoleRelEntity.class);
	}

	@Override
	public UserRoleRelEntity findById(String id) {
		if (StringUtil.isNullOrEmpty(id)) {
			return null;
		}
		return baseDao.get(id);
	}

	@Override
	public void delete(String id) {
		
	}

	@Override
	public UserRoleRelEntity saveOrUpdate(UserRoleRelEntity t) {
		return null;
	}
	
	@Override
	@Transactional(readOnly=false)
	public void save(String userId, String roleIds) {
		if (!StringUtil.isAnyNullOrEmpty(userId, roleIds)) {
			try {
				String[] roleIdArr = roleIds.split(", ");
				for (String roleId : roleIdArr) {
					if (!Constant.COMBO_DEFAULT_VALUE.equals(roleId)) {
						baseDao.save(new UserRoleRelEntity(userId, roleId));
					}
				}
			} catch (Exception e) {
				log.error(e);
				throw new ServiceException(MessageConstant.DB_IO_ERROR);
			}
		} 
	}

	@Override
	public Page<UserRoleRelEntity> findByPage(
			Page<UserRoleRelEntity> p, UserRoleRelEntity t) {
		return null;
	}

	@Override
	public List<UserRoleRelEntity> findByAll(UserRoleRelEntity t) {
		return null;
	}
	
	@Override
	@Transactional(readOnly=false)
	public void deleteByRoleId(String roleId) {
		if (!StringUtil.isNullOrEmpty(roleId)) {
			String hql = "delete from UserRoleRelEntity r where r.roleId = ?";
			try {
				baseDao.executeHql(hql, roleId);
			} catch (Exception e) {
				log.error(e);
				throw new ServiceException(MessageConstant.DB_IO_ERROR);
			}
		}
	}
	
	@Override
	@Transactional(readOnly=false)
	public void deleteByRoleIds(String... roleIds) {
		int size = (roleIds == null) ? 0 : roleIds.length;
		if (size > 0) {
			//删除角色
			StringBuilder hql = new StringBuilder("delete from UserRoleRelEntity r");
			hql.append(" where r.roleId in (");
			for (int i = 0; i < size; i++) {
				if (i+1 == size) {
					hql.append(" '"+roleIds[i]+"')");
				} else {
					hql.append(" '"+roleIds[i]+"',");
				}
			}
			try {
				baseDao.executeHql(hql.toString());
			} catch (Exception e) {
				log.error(e);
				throw new ServiceException(MessageConstant.DB_IO_ERROR);
			}
		}
	}
	
	@Override
	@Transactional(readOnly=false)
	public void deleteByUserIds(String... userIds) {
		int size = (userIds == null) ? 0 : userIds.length;
		if (size > 0) {
			//删除用户
			StringBuilder hql = new StringBuilder("delete from UserRoleRelEntity r where r.userId in (");
			for (int i = 0; i < size; i++) {
				if (i+1 == size) {
					hql.append(" '"+userIds[i]+"')");
				} else {
					hql.append(" '"+userIds[i]+"',");
				}
			}
			try {
				baseDao.executeHql(hql.toString());
			} catch (Exception e) {
				log.error(e);
				throw new ServiceException(MessageConstant.DB_IO_ERROR);
			}
		}
	}
	
	@Override
	public List<UserRoleRelEntity> findByRoleId(String roleId) {
		if (StringUtil.isNullOrEmpty(roleId)) {
			return null;
		}
		return baseDao.find("from UserRoleRelEntity r where r.roleId = ?", roleId);
	}
	
	@Override
	public List<UserRoleRelEntity> findByUserId(String userId) {
		if (StringUtil.isNullOrEmpty(userId)) {
			return null;
		}
		return baseDao.find("from UserRoleRelEntity r where r.userId = ?", userId);
	}
	
	@Override
	public int findUserCountByRoleId(String roleIds) {
		if (!StringUtil.isNullOrEmpty(roleIds)) {
			String[] ids = roleIds.split(",");
			int size = (ids == null) ? 0 : ids.length;
			if (size > 0) {
				//删除用户
				StringBuilder hql = new StringBuilder("SELECT count(*)");
				hql.append(" FROM t_uums_user_role_rel");
				hql.append(" WHERE role_id in (");
				for (int i = 0; i < size; i++) {
					if (i+1 == size) {
						hql.append(" '"+ids[i]+"')");
					} else {
						hql.append(" '"+ids[i]+"',");
					}
				}
				hql.append(" GROUP BY role_id HAVING count(*) > 1");
				try {
					//baseDao.executeHql(hql.toString());
				} catch (Exception e) {
					log.error(e);
					throw new ServiceException(MessageConstant.DB_IO_ERROR);
				}
			}
		}
		return 1;
	}
}
