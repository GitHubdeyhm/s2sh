package learn.frame.common.base;

import java.util.List;

import org.apache.log4j.Logger;

import learn.frame.common.Page;
import learn.frame.exception.ServiceException;

/**
 * 基础实体服务接口
 * @Date 2016-8-6 下午12:29:48
 */
public interface IBaseService<T> {
	
	public final Logger log = Logger.getLogger(IBaseService.class);
	/**
	 * 通过主键ID查询
	 */
	public T findById(String id);
	/**
	 * 通过主键ID删除,多个ID以英文逗号隔开
	 */
	public void delete(String ids) throws ServiceException;
	/**
	 * 保存或更新实体类
	 */
	public T saveOrUpdate(T t) throws ServiceException;
	/**
	 * 分页查询数据
	 * @Date 2017-2-18下午10:38:29
	 * @param p 分页对象
	 * @param t 实体对象
	 * @return 分页列表
	 */
	public Page<T> findByPage(Page<T> p, T t);
	/**
	 * 查询所有数据
	 */
	public List<T> findByAll(T t);
	
}
