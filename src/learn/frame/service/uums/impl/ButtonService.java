package learn.frame.service.uums.impl;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import learn.frame.common.MessageConstant;
import learn.frame.common.Page;
import learn.frame.common.dao.IBaseDaoHibernate;
import learn.frame.common.dao.impl.BaseDaoHibernate;
import learn.frame.entity.uums.ButtonEntity;
import learn.frame.exception.ServiceException;
import learn.frame.service.uums.IButtonService;
import learn.frame.utils.StringUtil;

import org.hibernate.SessionFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 按钮service
 * @Date 2016-5-1 下午11:03:03
 */
@Service
@Transactional(readOnly=true)
public class ButtonService implements IButtonService {
	
	private IBaseDaoHibernate<ButtonEntity, String> baseDao;//dao
	
	@Resource
	public void setSessionFactory(SessionFactory sessionFactory){
		baseDao = new BaseDaoHibernate<ButtonEntity, String>(sessionFactory, 
				ButtonEntity.class);
	}

	@Override
	public ButtonEntity findById(String id) {
		if (StringUtil.isNullOrEmpty(id)) {
			return null;
		}
		return baseDao.get(id);
	}

	@Override
	@Transactional(readOnly=false)
	public void delete(String ids) {
		if (!StringUtil.isNullOrEmpty(ids)) {
			String[] idArr = ids.split(",");
			StringBuilder hql = new StringBuilder("delete from ButtonEntity where id in (");
			int size = idArr.length;
			for (int i = 0; i < size; i++) {
				if (i+1 == size) {
					hql.append(" ?)");
				} else {
					hql.append(" ?,");
				}
			}
			try {
				baseDao.executeHql(hql.toString(), idArr);
			} catch (Exception e) {
				log.error(e);
				throw new ServiceException(MessageConstant.DB_IO_ERROR);
			}
		}
	}

	@Override
	@Transactional(readOnly=false)
	public ButtonEntity saveOrUpdate(ButtonEntity t) {
		if (t != null) {
			if (StringUtil.isNullOrEmpty(t.getMenuCode())) {
				throw new ServiceException("所属菜单名称不能为空");
			}
			if (StringUtil.isNullOrEmpty(t.getButtonMark())) {
				throw new ServiceException("请选择按钮名称");
			}
			if (isExistMark(t.getId(), t.getButtonMark(), t.getMenuCode())) {
				throw new ServiceException("同一个菜单的按钮标识不能重复");
			}
			try {
				baseDao.saveOrUpdate(t);
			} catch (Exception e) {
				log.error(e);
				throw new ServiceException(MessageConstant.DB_IO_ERROR);
			}
		}
		return t;
	}

	@Override
	public Page<ButtonEntity> findByPage(Page<ButtonEntity> p, ButtonEntity t) {
		return null;
	}

	@Override
	public List<ButtonEntity> findByAll(ButtonEntity t) {
		List<Object> paramList = new ArrayList<Object>();
		StringBuilder hql = new StringBuilder("from ButtonEntity where 1=1");
		if (t != null) {
			if (!StringUtil.isNullOrEmpty(t.getMenuCode())) {
				hql.append(" and menuCode = ?");
				paramList.add(t.getMenuCode());
			}
			if (!StringUtil.isNullOrEmpty(t.getButtonMark())) {
				hql.append(" and menuMark = ?");
				paramList.add(t.getButtonMark());
			}
		}
		hql.append(" order by buttonOrder");
		return baseDao.find(hql.toString(), paramList.toArray());
	}
	
	@Override
	public List<ButtonEntity> findByMenuCode(String menuCode) {
		if (StringUtil.isNullOrEmpty(menuCode)) {
			return null;
		}
		return baseDao.find("from ButtonEntity where menuCode = ?", menuCode);
	}
	
	@Override
	@Transactional(readOnly=false)
	public void deleteByMenuCode(String menuCode) {
		if (!StringUtil.isNullOrEmpty(menuCode)) {
			String hql = "delete from ButtonEntity where menuCode like ?";
			try {
				baseDao.executeHql(hql, menuCode+"%");
			} catch (Exception e) {
				log.error(e);
				throw new ServiceException(MessageConstant.DB_IO_ERROR);
			}
		}
	}
	
	/**
	 * 同一个菜单的按钮标识不能重复
	 * @Date 2016-5-2下午7:20:19
	 * @param id 页面传人的ID值
	 * @param mark 页面传人的标识值
	 * @return
	 */
	private boolean isExistMark(String id, String mark, String menuCode) {
		String hql = "from ButtonEntity where buttonMark = ? and menuCode = ?";
		List<ButtonEntity> btnList = baseDao.find(hql, mark, menuCode);
		if (btnList == null || btnList.size() == 0) {
			return false;
		}
		if ((btnList.size() == 1) && (btnList.get(0).getId().equals(id))) {
			return false;
		}
		return true;
	}
}
