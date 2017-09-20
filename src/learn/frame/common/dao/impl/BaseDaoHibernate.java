package learn.frame.common.dao.impl;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import learn.frame.common.Page;
import learn.frame.common.dao.IBaseDaoHibernate;

import org.hibernate.Query;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.SessionFactory;

/**
 * 基础dao，数据库访问类
 */
//@Repository("baseDao")
public class BaseDaoHibernate<T, PK extends Serializable> implements IBaseDaoHibernate<T, PK> {
	//当前sessionFactory
	protected SessionFactory sessionFactory;
	//当前运行时类
	protected Class<T> entityClass;
	
	
//	private BaseDaoHibernate() {
//		
//	}
	
	public BaseDaoHibernate(SessionFactory sessionFactory,
			Class<T> entityClass) {
		this.sessionFactory = sessionFactory;
		this.entityClass = entityClass;
	}
	
	public SessionFactory getSessionFactory() {
		return sessionFactory;
	}

	public void setSessionFactory(SessionFactory sessionFactory) {
		this.sessionFactory = sessionFactory;
	}

	/**
	 * 得到session
	 */
	public Session getSession() {
		return sessionFactory.getCurrentSession();
	}
	
	@Override
	public void delete(T obj) {
		getSession().delete(obj);
	}

	@Override
	public void delete(List<T> deleteList) {
		if (deleteList != null) {
			for (T o : deleteList) {
				this.delete(o);
			}
		}
	}
	
	@Override
	@SuppressWarnings("unchecked")
	public T get(PK id) {
		return (T)getSession().get(entityClass, id);
	}
	
	@Override
	@SuppressWarnings("unchecked")
	public T load(PK id) {
		return (T)getSession().load(entityClass, id);
	}
	
	@Override
	@SuppressWarnings("unchecked")
	public T get(String hql, Object... param) {
		return (T)this.createQuery(hql, param).uniqueResult();
	}
	
	@Override
	public Object findUnique(String hql, Object... param) {		
		return this.createQuery(hql, param).uniqueResult();
	}

	@Override
	@SuppressWarnings("unchecked")
	public List<T> find(String hql, Object... param) {
		return this.createQuery(hql, param).list();
	}

	@Override
	@SuppressWarnings("unchecked")
	public List<T> findByPage(String hql, int pageNum, int pageSize, Object... param) {
		Query qu = this.createQuery(hql, param);
		return qu.setFirstResult((pageNum - 1) * pageSize).setMaxResults(pageSize).list();
	}

	@Override
	public long getTotalNum(String hql, Object... param) {
		Long totalNum = (Long)this.createQuery(hql, param).uniqueResult();
		return totalNum == null ? 0 : totalNum;
	}

	@Override
	@SuppressWarnings("unchecked")
	public PK save(T obj) {
		return (PK)getSession().save(obj);		
	}

	@Override
	public void update(T obj) {
		getSession().update(obj);
	}

	@Override
	public void flush() {
		getSession().flush();
	}

	@Override
	public void clear() {
		getSession().clear();
	}

	@Override
	public void evict(T obj) {
		getSession().evict(obj);
	}
	
	@Override
	@SuppressWarnings("unchecked")
	public T merge(T obj) {
		return (T)getSession().merge(obj);
	}
	
	@Override
	@SuppressWarnings("unchecked")
	public Page<T> findByPage(Page<T> p, String hql, Object... param) {
		Query qu = this.createQuery(hql, param);
		List<T> pageList = (ArrayList<T>)qu.setFirstResult((p.getPageNum() - 1) * p.getPageSize()).setMaxResults(p.getPageSize()).list();
		if (pageList != null && pageList.size() > 0) {
			p.setList(pageList);
			//查询总的记录数
			long totalNum = this.getTotalNum("SELECT COUNT(*) " + removeSelect(hql), param);
			p.setTotalNum(totalNum);
		}
		return p;
	}
	
	@Override
	public void saveOrUpdate(T obj) {
		getSession().saveOrUpdate(obj);
	}
	
	@Override
	public int executeHql(String hql,  Object... param) {
		return this.createQuery(hql, param).executeUpdate();
	}
	
	@Override
	public Query createQuery(String hql , Object... param) {
		Query qu = getSession().createQuery(hql);
		if (param != null && param.length > 0) {
			for (int i = 0; i < param.length; i++) {
				qu.setParameter(i, param[i]);
			}
		}
		return qu;
	}
	
	@Override
	public SQLQuery createSQLQuery(String sql, Object... param) {
		SQLQuery sqlQ = getSession().createSQLQuery(sql);
		if (param != null && param.length > 0) {
			for (int i = 0; i < param.length; i++) {
				sqlQ.setParameter(i, param[i]);
			}
		}
		return sqlQ;
	}
	
	/**
	 * 移除hql语句的select字段，让hql语句从from字段开始，用于统计记录数
	 * @param hql ：hql语句
	 * @return
	 */
	private String removeSelect(String hql) {
		return hql.substring(hql.indexOf("from"));
	}
}
