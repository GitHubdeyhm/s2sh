package learn.frame.service.uums.impl;

import java.util.List;

import javax.annotation.Resource;

import learn.frame.common.Constant;
import learn.frame.common.MessageConstant;
import learn.frame.common.Page;
import learn.frame.common.dao.IBaseDaoHibernate;
import learn.frame.common.dao.impl.BaseDaoHibernate;
import learn.frame.entity.uums.RoleMenuRelEntity;
import learn.frame.exception.ServiceException;
import learn.frame.service.uums.IRoleMenuRelService;
import learn.frame.utils.StringUtil;

import org.hibernate.SessionFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 角色菜单关联service
 * @Date 2016-2-5 下午4:51:41
 */
@Service
@Transactional(readOnly=true)
public class RoleMenuRelService implements IRoleMenuRelService {
	
	private IBaseDaoHibernate<RoleMenuRelEntity, String> baseDao;//dao

	@Resource
	public void setSessionFactory(SessionFactory sessionFactory){
		baseDao = new BaseDaoHibernate<RoleMenuRelEntity, String>(sessionFactory, 
				RoleMenuRelEntity.class);
	}

	@Override
	public RoleMenuRelEntity findById(String id) {
		return null;
	}

	@Override
	public void delete(String id) {
	}

	@Override
	public RoleMenuRelEntity saveOrUpdate(RoleMenuRelEntity t) {
		return null;
	}

	@Override
	public Page<RoleMenuRelEntity> findByPage(Page<RoleMenuRelEntity> p,
			RoleMenuRelEntity t) {
		return null;
	}

	@Override
	public List<RoleMenuRelEntity> findByAll(RoleMenuRelEntity t) {
		return null;
	}

	@Override
	@Transactional(readOnly=false)
	public void deleteByRoleId(String roleId) {
		if (!StringUtil.isNullOrEmpty(roleId)) {
			String hql = "delete from RoleMenuRelEntity where roleId = ?";
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
		StringBuilder hql = new StringBuilder("delete from RoleMenuRelEntity");
		hql.append(" where roleId in (");
		int size = (roleIds == null) ? 0 : roleIds.length;
		for (int i = 0; i < size; i++) {
			if (i + 1 == size) {
				hql.append("'"+roleIds[i]+"')");
			} else {
				hql.append("'"+roleIds[i]+"',");
			}
		}
		try {
			baseDao.executeHql(hql.toString());
		} catch (Exception e) {
			log.error(e);
			throw new ServiceException(MessageConstant.DB_IO_ERROR);
		}
	}

	@Override
	@Transactional(readOnly=false)
	public void deleteByMenuCode(String menuCode) {
		if (!StringUtil.isNullOrEmpty(menuCode)) {
			String hql = "delete from RoleMenuRelEntity rm where rm.menuCode like ?";
			try {
				baseDao.executeHql(hql, menuCode+"%");
			} catch (Exception e) {
				log.error(e);
				throw new ServiceException(MessageConstant.DB_IO_ERROR);
			}
		}
	}

	@Override
	public List<RoleMenuRelEntity> findByRoleId(String roleId) {
		if (StringUtil.isNullOrEmpty(roleId)) {
			return null;
		}
		return baseDao.find("from RoleMenuRelEntity where roleId = ?", roleId);
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public List<String> findByRoleIds(String roleIds) {
		if (StringUtil.isNullOrEmpty(roleIds)) {
			return null;
		}
		String[] roleIdArr = roleIds.split(",");
		int size = roleIdArr.length;
		if (size > 0) {
			StringBuffer hql = new StringBuffer("SELECT DISTINCT mr.menuCode FROM RoleMenuRelEntity mr where 1=1");
			if (!String.valueOf(Constant.ADMIN_ROLE_LEVEL).equals(roleIdArr[0])) {
				hql.append(" and mr.roleId in( ");
				for (int i = 0; i < size; i++) {
					if (i+1 == size) {
						hql.append(" '"+roleIdArr[i]+"')");
					} else {
						hql.append(" '"+roleIdArr[i]+"',");
					}
				}
			}
			return baseDao.createQuery(hql.toString()).list();
		}
		return null;
	}

	@Override
	public List<RoleMenuRelEntity> findByMenuCode(String menuCode) {
		if (StringUtil.isNullOrEmpty(menuCode)) {
			return null;
		}
		return baseDao.find("from RoleMenuRelEntity rm where rm.menuCode = ?", menuCode);
	}

	@Override
	@Transactional(readOnly=false)
	public void save(String roleId, String menuCodes) {
		if (!StringUtil.isAnyNullOrEmpty(roleId, menuCodes)) {
			try {
				String[] codes = menuCodes.split(",");
				for (String code : codes) {
					baseDao.save(new RoleMenuRelEntity(roleId, code));
				}
			} catch (Exception e) {
				log.error(e);
				throw new ServiceException(MessageConstant.DB_IO_ERROR);
			}
		}
	}
	
	@Override
	@Transactional(readOnly=false)
	public void updateByRoleId(String roleId, String menuCodes) {
		if (!StringUtil.isAnyNullOrEmpty(roleId, menuCodes)) {
			try {
				//先删除，在添加角色菜单关联关系
				this.deleteByRoleId(roleId);
				//添加
				String[] codes = menuCodes.split(",");
				for (String code : codes) {
					baseDao.save(new RoleMenuRelEntity(roleId, code));
				}
			} catch (Exception e) {
				log.error(e);
				throw new ServiceException(MessageConstant.DB_IO_ERROR);
			}
		}
	}
}
