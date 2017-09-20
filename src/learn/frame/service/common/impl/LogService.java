package learn.frame.service.common.impl;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import learn.frame.common.MessageConstant;
import learn.frame.common.Page;
import learn.frame.common.dao.IBaseDaoHibernate;
import learn.frame.common.dao.impl.BaseDaoHibernate;
import learn.frame.entity.common.LogEntity;
import learn.frame.exception.ServiceException;
import learn.frame.service.common.ILogService;
import learn.frame.utils.StringUtil;

import org.hibernate.SessionFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 日志service类
 * @Date 2016-8-6 下午7:35:44
 */
@Service
@Transactional(readOnly=true)
public class LogService implements ILogService {
	
	private IBaseDaoHibernate<LogEntity, String> baseDao;//dao
	
	@Resource
	public void setSessionFactory(SessionFactory sessionFactory){
		baseDao = new BaseDaoHibernate<LogEntity, String>(sessionFactory, 
				LogEntity.class);
	}

	@Override
	public LogEntity findById(String id) {
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
			StringBuilder hql = new StringBuilder("delete from LogEntity where id in (");
			int size = idArr.length;
			for (int i = 0; i < size; i++) {
				if (i+1 == size) {
					hql.append(" '"+idArr[i]+"')");
				} else {
					hql.append(" '"+idArr[i]+"',");
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
	public LogEntity saveOrUpdate(LogEntity t) {
		if (t != null) {
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
	public Page<LogEntity> findByPage(Page<LogEntity> p, LogEntity t) {
		List<Object> paramList = new ArrayList<Object>();
		StringBuilder hql = new StringBuilder("from LogEntity where 1=1");
		if (t != null) {
			if (t.getOptType() != null) {
				hql.append(" and optType = ?");
				paramList.add(t.getOptType());
			}
			if (!StringUtil.isNullOrEmpty(t.getLogName())) {
				t.setLogName(StringUtil.hqlEscape(t.getLogName()));
				hql.append(" and logName like ? escape '/'");
				paramList.add("%"+t.getLogName()+"%");
			}
			if (!StringUtil.isNullOrEmpty(t.getLogContent())) {
				t.setLogContent(StringUtil.hqlEscape(t.getLogContent()));
				hql.append(" and logContent like ? escape '/'");
				paramList.add("%"+t.getLogContent()+"%");
			}
			if (!StringUtil.isNullOrEmpty(t.getBeginTime())) {
				hql.append(" and createTime >= '"+t.getBeginTime()+"'");
			}
			if (!StringUtil.isNullOrEmpty(t.getEndTime())) {
				hql.append(" and createTime <= '"+t.getEndTime()+"'");
			}
		}
		hql.append(" order by createTime desc");
		return baseDao.findByPage(p, hql.toString(), paramList.toArray());
	}

	@Override
	public List<LogEntity> findByAll(LogEntity t) {
		return null;
	}
}
