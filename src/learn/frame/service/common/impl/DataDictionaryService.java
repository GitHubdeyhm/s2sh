package learn.frame.service.common.impl;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import learn.frame.common.MessageConstant;
import learn.frame.common.Page;
import learn.frame.common.dao.IBaseDaoHibernate;
import learn.frame.common.dao.impl.BaseDaoHibernate;
import learn.frame.entity.common.DataDictionaryEntity;
import learn.frame.exception.ServiceException;
import learn.frame.service.common.IDataDictionaryService;
import learn.frame.utils.CacheUtil;
import learn.frame.utils.StringUtil;

import org.hibernate.SessionFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 数据字典设置service类
 * @Date 2016-10-22下午1:26:02
 */
@Service
@Transactional(readOnly=true)
public class DataDictionaryService implements IDataDictionaryService {
	
	private IBaseDaoHibernate<DataDictionaryEntity, String> baseDao;//dao
	
	@Resource
	public void setSessionFactory(SessionFactory sessionFactory){
		baseDao = new BaseDaoHibernate<DataDictionaryEntity, String>(sessionFactory, 
				DataDictionaryEntity.class);
	}

	@Override
	public DataDictionaryEntity findById(String id) {
		if (StringUtil.isNullOrEmpty(id)) {
			return null;
		}
		return baseDao.get(id);
	}

	@Override
	@Transactional(readOnly=false)
	public void delete(String ids) throws ServiceException {
		if (!StringUtil.isNullOrEmpty(ids)) {
			String[] idArr = ids.split(",");
			try {
				StringBuilder hql = new StringBuilder("delete from DataDictionaryEntity");
				hql.append(" where id in (");
				int size = (idArr == null) ? 0 : idArr.length;
				for (int i = 0; i < size; i++) {
					if (i + 1 == size) {
						hql.append("'"+idArr[i]+"')");
					} else {
						hql.append("'"+idArr[i]+"',");
					}
				}
				baseDao.executeHql(hql.toString());
			} catch (Exception e) {
				log.error(e);
				throw new ServiceException(MessageConstant.DB_IO_ERROR);
			}
		}
	}

	@Override
	@Transactional(readOnly=false)
	public DataDictionaryEntity saveOrUpdate(DataDictionaryEntity t)
			throws ServiceException {
		if (t != null) {
			try {
				if (isExistKey(t.getId(), t.getDictKey())) {
					throw new ServiceException("dict_key_exist_msg");
				}
				baseDao.saveOrUpdate(t);
				
				//将数据字典放入缓存
				CacheUtil.putCacheData(CacheUtil.CACHE_KEY_DICT+t.getDictKey(), t.getDictValue());
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
	public Page<DataDictionaryEntity> findByPage(Page<DataDictionaryEntity> p,
			DataDictionaryEntity t) {
		List<Object> paramList = new ArrayList<Object>();
		StringBuilder hql = new StringBuilder("from DataDictionaryEntity dict where 1=1");
		if (t != null) {
			if (!StringUtil.isNullOrEmpty(t.getDictName())) {
				hql.append(" and dict.dictName like ? escape '/'");
				String es = StringUtil.hqlEscape(t.getDictName());
				paramList.add("%"+es+"%");
			}
			if (!StringUtil.isNullOrEmpty(t.getDictKey())) {
				hql.append(" and dict.dictKey like ? escape '/'");
				String es = StringUtil.hqlEscape(t.getDictKey()).trim();
				paramList.add("%"+es+"%");
			}
			if (!StringUtil.isNullOrEmpty(t.getDictValue())) {
				hql.append(" and dict.dictValue like ? escape '/'");
				String es = StringUtil.hqlEscape(t.getDictValue());
				paramList.add("%"+es+"%");
			}
		}
		hql.append(" order by dict.createTime desc");
		return baseDao.findByPage(p, hql.toString(), paramList.toArray());
	}

	@Override
	public List<DataDictionaryEntity> findByAll(DataDictionaryEntity t) {
		StringBuilder hql = new StringBuilder("from DataDictionaryEntity where 1=1");
		hql.append(" order by createTime desc");
		return baseDao.find(hql.toString());
	}
	
	@Override
	public String findByDictKey(String key) {
		StringBuilder hql = new StringBuilder("select dictKey from DataDictionaryEntity");
		hql.append(" where dictKey = ?");
		return (String)baseDao.findUnique(hql.toString(), key);
	}

	/**
	 * 判断字典键是否重复，重复返回true，否则为false
	 * @Date 2015-8-12 下午01:10:13 
	 * @param id：页面ID
	 * @param name：页面名称
	 * @return 重复返回true，否则为false
	 */
	private boolean isExistKey(String id, String key) {
		String hql = "from DataDictionaryEntity where dictKey = ?";
		List<DataDictionaryEntity> list = baseDao.find(hql, key);
		if (list == null || list.size() == 0) {
			return false;
		}
		if ((list.size() == 1) && (list.get(0).getId().equals(id))) {
			return false;
		}
		return true;
	}
}
