/**
 * 考勤模块相关服务模块包
 */
package learn.frame.service.attendman.impl;

import java.util.List;

import javax.annotation.Resource;

import learn.frame.common.Page;
import learn.frame.common.dao.IBaseDaoHibernate;
import learn.frame.common.dao.impl.BaseDaoHibernate;
import learn.frame.entity.attendman.DutyEntity;
import learn.frame.service.attendman.IDutyService;
import learn.frame.utils.StringUtil;

import org.hibernate.SessionFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 统计一周的值班安排表信息
 * @Date 2016-4-10 下午7:12:11
 */
@Service
@Transactional(readOnly=true)
public class DutyService implements IDutyService {
	
	private IBaseDaoHibernate<DutyEntity, String> baseDao;//dao
	
	@Resource
	public void setSessionFactory(SessionFactory sessionFactory){
		baseDao = new BaseDaoHibernate<DutyEntity, String>(sessionFactory, 
				DutyEntity.class);
	}

	@Override
	public DutyEntity findById(String id) {
		return null;
	}

	@Override
	public void delete(String id) {
	}

	@Override
	public DutyEntity saveOrUpdate(DutyEntity t) {
		return null;
	}

	@Override
	public Page<DutyEntity> findByPage(Page<DutyEntity> p, DutyEntity t) {
		return null;
	}

	@Override
	public List<DutyEntity> findByAll(DutyEntity t) {
		return null;
	}
	
	@Override
	public List<DutyEntity> findByWeeks(String weeks) {
		if (!StringUtil.isNullOrEmpty(weeks)) {
			String[] weekArr = weeks.split(",");
			int size = weekArr.length;
			StringBuffer hql = new StringBuffer("from DutyEntity where dutyTime in (");
			for (int i = 0; i < size; i++) {
				if (i + 1 == size) {
					hql.append(" ?)");
				} else {
					hql.append(" ?,");
				}
			}
			return baseDao.find(hql.toString(), weekArr);
		}
		return null;
	}
}
