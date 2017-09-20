package learn.frame.service.uums.impl;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import learn.frame.common.Constant;
import learn.frame.common.MessageConstant;
import learn.frame.common.Page;
import learn.frame.common.dao.impl.BaseDaoHibernate;
import learn.frame.common.easyui.Combobox;
import learn.frame.entity.uums.RoleEntity;
import learn.frame.exception.ServiceException;
import learn.frame.service.uums.IRoleButtonRelService;
import learn.frame.service.uums.IRoleMenuRelService;
import learn.frame.service.uums.IRoleService;
import learn.frame.service.uums.IUserRoleRelService;
import learn.frame.utils.StringUtil;

import org.hibernate.SessionFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 角色service类
 * @Date 2016-7-17 下午1:36:16
 */
@Service
@Transactional(readOnly=true)
public class RoleService implements IRoleService {
	
	private BaseDaoHibernate<RoleEntity, String> baseDao;//dao
	
	@Resource
	private IRoleButtonRelService roleButtonRelService;
	@Resource
	private IRoleMenuRelService roleMenuRelService;
	@Resource
	private IUserRoleRelService userRoleRelService;
	
	@Resource
	public void setSessionFactory(SessionFactory sessionFactory){
		baseDao = new BaseDaoHibernate<RoleEntity, String>(sessionFactory, 
				RoleEntity.class);
	}
	
	@Override
	public RoleEntity findById(String id) {
		if (StringUtil.isNullOrEmpty(id)) {
			return null;
		}
		//查询为删除的数据
		String hql = "from RoleEntity where id = ? and deleteFlag = ?";
		return baseDao.get(hql, id, Constant.DELETE_FLAG_FALSE);
	}

	@Override
	@Transactional(readOnly=false)
	public void delete(String ids) throws ServiceException {
		if (!StringUtil.isNullOrEmpty(ids)) {
			String[] roleIds = ids.split(",");
			try {
				//删除角色按钮关联
				roleButtonRelService.deleteByRoleIds(roleIds);
				//删除角色菜单关联
				roleMenuRelService.deleteByRoleIds(roleIds);
				//删除用户角色关联
				userRoleRelService.deleteByRoleIds(roleIds);
				//更新删除表示，角色逻辑删除
				updateDeleteFlag(roleIds);
			} catch (Exception e) {
				log.error(e);
				throw new ServiceException(MessageConstant.DB_IO_ERROR);
			}
		}
	}

	@Override
	@Transactional(readOnly=false)
	public RoleEntity saveOrUpdate(RoleEntity t) throws ServiceException {
		if (t != null) {
			if (StringUtil.isNullOrEmpty(t.getRoleName())) {
				throw new ServiceException("role_name_empty");
			}
			try {
				if (isExistName(t.getId(), t.getRoleName())) {
					throw new ServiceException("role_name_exist");
				}
				//修改时先根据角色ID删除再保存
				if (!StringUtil.isNullOrEmpty(t.getId())) {
					roleMenuRelService.deleteByRoleId(t.getId());
				}
				//保存角色
				baseDao.saveOrUpdate(t);
				//保存角色菜单关联
				roleMenuRelService.save(t.getId(), t.getMenuPrivils());
			} catch (ServiceException e) {
				throw e;
			} catch (Exception e) {
				log.error(e);
				throw new ServiceException(MessageConstant.DB_IO_ERROR);
			}
		}
		return t;
	}

	@Override
	public List<Combobox> findForCombobox(String defaultText) {
		List<Combobox> comboList = new ArrayList<Combobox>();
		comboList.add(new Combobox(Constant.COMBO_DEFAULT_VALUE, defaultText));
		List<RoleEntity> roleList = this.findByAll(null);
		if (roleList != null && roleList.size() > 0) {
			for (RoleEntity role : roleList) {
				comboList.add(new Combobox(role.getId(), role.getRoleName()));
			}
		}
		return comboList;
	}

	@Override
	public Page<RoleEntity> findByPage(Page<RoleEntity> p, RoleEntity t) {
		List<Object> paramList = new ArrayList<Object>();
		StringBuilder hql = new StringBuilder("from RoleEntity r where deleteFlag = ?");
		paramList.add(Constant.DELETE_FLAG_FALSE);
		if (t != null) {
			if (!StringUtil.isNullOrEmpty(t.getRoleName())) {
				hql.append(" and r.roleName like ? escape '/'");
				String es = StringUtil.hqlEscape(t.getRoleName());
				paramList.add("%"+es.trim()+"%");
			}
		}
		String sortWay = StringUtil.isNullOrEmpty(p.getSortWay()) ? Page.ORDER_DESC : p.getSortWay();
		hql.append(" order by roleOrder ");
		hql.append(sortWay);//排序方式
		return baseDao.findByPage(p, hql.toString(), paramList.toArray());
	}

	@Override
	public List<RoleEntity> findByAll(RoleEntity t) {
		String hql = "from RoleEntity where deleteFlag = ? order by roleOrder desc";
		return baseDao.find(hql, Constant.DELETE_FLAG_FALSE);
	}
	
	@Override
	public int generateOrder() {
		String hql = "select max(roleOrder) from RoleEntity where deleteFlag = ?";
		Integer maxOrder = (Integer)baseDao.findUnique(hql, Constant.DELETE_FLAG_FALSE);
		int order = (maxOrder == null) ? 1 : maxOrder+1;
		//最大排序号不能超过999999
		if (order > Constant.MAX_ORDER) {
			order = Constant.MAX_ORDER;
		}
		return order;
	}
	
	/**
	 * 通过名称查询
	 * @Date 2015-11-8下午9:21:06
	 * @param name 角色名称
	 * @return
	 */
	public List<RoleEntity> findByName(String name) {
		if (StringUtil.isNullOrEmpty(name)) {
			return null;
		}
		String hql = "from RoleEntity r where r.roleName = ? and deleteFlag = ?";
		return baseDao.find(hql, name, Constant.DELETE_FLAG_FALSE);
	}
	
	/**
	 * 判断名称是否重复，重复返回true，否则为false
	 * @Date 2015-8-12 下午01:10:13 
	 * @param id：页面ID
	 * @param name：页面名称
	 * @return 重复返回true，否则为false
	 */
	private boolean isExistName(String id, String name) {
		List<RoleEntity> roleList = this.findByName(name);
		if (roleList == null || roleList.size() == 0) {
			return false;
		}
		if ((roleList.size() == 1) && (roleList.get(0).getId().equals(id))) {
			return false;
		}
		return true;
	}
	
	/**
	 * 根据角色ID更新删除标示为删除状态，逻辑删除
	 * @Date 2016-7-17下午1:52:54
	 * @param ids 多个值以英文逗号隔开
	 */
	private void updateDeleteFlag(String... ids) throws Exception {
		int size = (ids == null) ? 0 : ids.length;
		StringBuilder hql = new StringBuilder("update RoleEntity");
		hql.append(" set deleteFlag = ? where id in (");
		for (int i = 0; i < size; i++) {
			if (i + 1 == size) {
				hql.append("'"+ids[i]+"')");
			} else {
				hql.append("'"+ids[i]+"',");
			}
		}
		baseDao.executeHql(hql.toString(), Constant.DELETE_FLAG_TRUE);
	}
}
