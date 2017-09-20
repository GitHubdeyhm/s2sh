package learn.frame.service.uums.impl;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import learn.frame.common.Constant;
import learn.frame.common.MessageConstant;
import learn.frame.common.Page;
import learn.frame.common.dao.IBaseDaoHibernate;
import learn.frame.common.dao.impl.BaseDaoHibernate;
import learn.frame.entity.uums.ButtonEntity;
import learn.frame.entity.uums.RoleButtonRelEntity;
import learn.frame.exception.ServiceException;
import learn.frame.service.uums.IRoleButtonRelService;
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
public class RoleButtonRelService implements IRoleButtonRelService {
	
	private IBaseDaoHibernate<RoleButtonRelEntity, String> baseDao;//dao

	@Resource
	public void setSessionFactory(SessionFactory sessionFactory){
		baseDao = new BaseDaoHibernate<RoleButtonRelEntity, String>(sessionFactory, 
				RoleButtonRelEntity.class);
	}

	@Override
	public RoleButtonRelEntity findById(String id) {
		return null;
	}

	@Override
	public void delete(String id) {
	}

	@Override
	public RoleButtonRelEntity saveOrUpdate(RoleButtonRelEntity t) {
		return null;
	}

	@Override
	public Page<RoleButtonRelEntity> findByPage(Page<RoleButtonRelEntity> p,
			RoleButtonRelEntity t) {
		return null;
	}

	@Override
	public List<RoleButtonRelEntity> findByAll(RoleButtonRelEntity t) {
		return null;
	}

	@Override
	@Transactional(readOnly=false)
	public void deleteByRoleId(String roleId) {
		if (!StringUtil.isNullOrEmpty(roleId)) {
			String hql = "delete from RoleButtonRelEntity rm where rm.roleId = ?";
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
		StringBuilder hql = new StringBuilder("delete from RoleButtonRelEntity");
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
	public List<RoleButtonRelEntity> findByRoleId(String roleId) {
		if (StringUtil.isNullOrEmpty(roleId)) {
			return null;
		}
		return baseDao.find("from RoleButtonRelEntity rm where rm.roleId = ?", roleId);
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
			List<Object> paramList = new ArrayList<Object>();
			StringBuffer hql = new StringBuffer("select distinct mr.menuCode from RoleButtonRelEntity mr where 1=1");
			if (!String.valueOf(Constant.ADMIN_ROLE_LEVEL).equals(roleIdArr[0])) {
				hql.append(" and mr.roleId in( ");
				for (int i = 0; i < size; i++) {
					if (i+1 == size) {
						hql.append(" ?)");
					} else {
						hql.append(" ?,");
					}
					paramList.add(roleIdArr[i]);
				}
			}
			return baseDao.createQuery(hql.toString(), paramList.toArray()).list();
		}
		return null;
	}

	@Override
	@Transactional(readOnly=false)
	public void save(String roleId, String buttonIds) {
		if (!StringUtil.isAnyNullOrEmpty(roleId, buttonIds)) {
			try {
				String[] codes = buttonIds.split(",");
				for (String code : codes) {
					baseDao.save(new RoleButtonRelEntity(roleId, code));
				}
			} catch (Exception e) {
				log.error(e);
				throw new ServiceException(MessageConstant.DB_IO_ERROR);
			}
		}
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public List<ButtonEntity> findByRoleIdAndMenuCode(String roleId, String menuCode) {
		if (!StringUtil.isAnyNullOrEmpty(roleId, menuCode)) {
			StringBuilder hql = new StringBuilder("SELECT btn FROM ButtonEntity btn");
			hql.append(" ,RoleButtonRelEntity rbr where rbr.buttonId=btn.id");
			hql.append(" and rbr.roleId = ? and btn.menuCode = ?");
			return baseDao.createQuery(hql.toString(), roleId, menuCode).list();
		}
		return null;
	}
}
