package learn.frame.service.uums.impl;

import java.util.List;

import javax.annotation.Resource;

import learn.frame.common.Constant;
import learn.frame.common.MessageConstant;
import learn.frame.common.Page;
import learn.frame.common.dao.IBaseDaoHibernate;
import learn.frame.common.dao.impl.BaseDaoHibernate;
import learn.frame.entity.uums.UserUnitRelEntity;
import learn.frame.exception.ServiceException;
import learn.frame.service.uums.IUserUnitRelService;
import learn.frame.utils.StringUtil;

import org.hibernate.SessionFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 用户部门Service
 * @Date 2016-2-5 下午4:59:52
 */
@Service
@Transactional(readOnly=true)
public class UserUnitRelService implements IUserUnitRelService {
	
	private IBaseDaoHibernate<UserUnitRelEntity, String> baseDao;//dao
	
	@Resource
	public void setSessionFactory(SessionFactory sessionFactory){
		baseDao = new BaseDaoHibernate<UserUnitRelEntity, String>(sessionFactory, 
				UserUnitRelEntity.class);
	}

	@Override
	public UserUnitRelEntity findById(String id) {
		return null;
	}

	@Override
	public void delete(String id) {
	}

	@Override
	public UserUnitRelEntity saveOrUpdate(UserUnitRelEntity t) {
		return null;
	}

	@Override
	public Page<UserUnitRelEntity> findByPage(Page<UserUnitRelEntity> p,
			UserUnitRelEntity t) {
		return null;
	}

	@Override
	public List<UserUnitRelEntity> findByAll(UserUnitRelEntity t) {
		return null;
	}

	@Override
	@Transactional(readOnly=false)
	public void deleteByUserIds(String... userIds) {
		int size = (userIds == null) ? 0 : userIds.length;
		if (size > 0) {
			//删除用户
			StringBuilder hql = new StringBuilder("delete from UserUnitRelEntity where userId in (");
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
	@Transactional(readOnly=false)
	public void deleteByUnitCode(String unitCode) {
		if (!StringUtil.isNullOrEmpty(unitCode)) {
			try {
				String hql = "delete from UserUnitRelEntity uu where uu.unitCode like ?";
				baseDao.executeHql(hql, unitCode+"%");
			} catch (Exception e) {
				log.error(e);
				throw new ServiceException(MessageConstant.DB_IO_ERROR);
			}
		}
	}

	@Override
	public List<UserUnitRelEntity> findByUserId(String userId) {
		if (StringUtil.isNullOrEmpty(userId)) {
			return null;
		}
		return baseDao.find("from UserUnitRelEntity u where u.userId = ?", userId);
	}

	@Override
	public List<UserUnitRelEntity> findByUnitCode(String unitCode) {
		if (StringUtil.isNullOrEmpty(unitCode)) {
			return null;
		}
		return baseDao.find("from UserUnitRelEntity u where u.unitCode = ?", unitCode);
	}

	@Override
	@Transactional(readOnly=false)
	public void save(String userId, String unitCodes) {
		if (!StringUtil.isAnyNullOrEmpty(userId, unitCodes)) {
			try {
				String[] codes = unitCodes.split(", ");
				for (String code : codes) {
					if (!Constant.COMBO_DEFAULT_VALUE.equals(code)) {
						baseDao.save(new UserUnitRelEntity(userId, code));
					}
				}
			} catch (Exception e) {
				log.error(e);
				throw new ServiceException(MessageConstant.DB_IO_ERROR);
			}
		}
	}
}
