package learn.frame.service.common;

import learn.frame.common.base.IBaseService;
import learn.frame.entity.common.DataDictionaryEntity;

/**
 * @Date 2016-10-22下午1:25:47
 */
public interface IDataDictionaryService extends IBaseService<DataDictionaryEntity> {

	/**
	 * 通过字典键值得到值
	 * @Date 2016-10-22下午2:07:43
	 * @param key
	 * @return
	 */
	String findByDictKey(String key);

}
