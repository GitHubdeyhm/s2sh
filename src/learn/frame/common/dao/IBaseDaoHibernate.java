package learn.frame.common.dao;

import java.io.Serializable;
import java.util.List;

import learn.frame.common.Page;

import org.hibernate.Query;
import org.hibernate.SQLQuery;

/**
 * 基础数据库dao操作类
 * @Date 2016-8-6 下午12:35:21
 */
public interface IBaseDaoHibernate<T, PK extends Serializable> {
	
	/**
	 * 保存一个对象--将一个对象持久化
	 * @return 返回保存的对象，可以得到刚保存对象的ID
	 */
	public PK save(T obj);
	/**
	 * 删除一个对象--必须是持久化状态的对象
	 */
	public void delete(T obj);
	/**
	 * 批量删除
	 */
	public void delete(List<T> deleteList);
	/**
	 * 修改一个对象--必须是持久化状态的对象
	 */
	public void update(T obj);
	/**
	 * 保存或更新一个对象
	 * @param obj
	 */
	public void saveOrUpdate(T obj);
	/**
	 * 刷新
	 */
	public void flush();
	/**
	 * 清楚指定的缓存对象
	 */
	public void evict(T obj);
	/**
	 * 清楚所有的缓存对象
	 */
	public void clear();
	/**
	 * 根据条件得到该对象
	 */
	public T get(String hql, Object... param);
	/**
	 * 根据主键得到该对象
	 */
	public T get(PK id);
	/**
	 * 根据主键得到该对象--延迟加载
	 */
	public T load(PK id);
	/**
	 * 根据条件查找所有数据，返回唯一结果
	 */
	public Object findUnique(String hql, Object... param);
	/**
	 * 根据条件查找所有数据
	 */
	public List<T> find(String hql, Object... param);
	/**
	 * 分页查询得到数据
	 * @param pageNum：当前页码
	 * @param pageSize：一次查找的数据量
	 * @param param：传入的参数值
	 * @return
	 */
	public List<T> findByPage(String hql, int pageNum, int pageSize, Object... param);
	/**
	 * 分页查询数据，统计了总记录数
	 * @param p ：分页对象
	 * @param hql ：查询语句
	 * @param param ：传人的参数值
	 * @return
	 */
	public Page<T> findByPage(Page<T> p, String hql, Object... param);
	/**
	 * 根据条件查询得到数据的记录数
	 */
	public long getTotalNum(String hql, Object... param);
	/**
	 * hibernate执行hql的update和delete语句方法，即通过写hql语句来更新或删除
	 * 通过hibernate的update()/delete()方法来更新/删除要求传人的对象是持久化对象，即先查询得到该对象放入内存中再删除极大的降低了效率
	 * 因此通过写hql来更新或删除可以提高程序的运行效率
	 * @return 更新/删除的记录数
	 */
	public int executeHql(String hql,  Object... param);
	/**
	 * 类似Query接口的createQuery方法，区别是为hql语句赋了参数值
	 * @return Query接口
	 */
	public Query createQuery(String hql, Object... param);
	/**
	 * 创建sql语句查询，调用hibernate框架提供的sql语句查询方法
	 * <p>hibernate提供的sql语句查询方法注意事项：不能为数据库列取别名，且多表查询时列名
	 * 不能重复，重复的话只能获取第一个值，后面重复的列名不能获取值（获取的值是第一个相同列名的值）</p>
	 * @Date 2016-8-6下午12:36:04
	 * @param sql sql语句
	 * @param param 查询参数
	 * @return SQLQuery接口
	 */
	public SQLQuery createSQLQuery(String sql, Object... param);
	/**
	 * 
	 * @param obj
	 * @return
	 */
	public T merge(T obj);
}
