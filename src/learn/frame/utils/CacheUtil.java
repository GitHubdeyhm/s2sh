package learn.frame.utils;

import learn.frame.service.init.InitService;

/**
 * 项目缓存工具操作类
 * @Date 2016-9-25下午8:12:19
 */
public class CacheUtil {
	/**数据字典的缓存键前缀*/
	public static final String CACHE_KEY_DICT = "dict.";

	/**
	 * 根据键值对放入缓存
	 * @Date 2016-9-25下午8:19:20
	 * @param key 键
	 * @param value 值
	 * @return
	 */
	public static String putCacheData(String key, String value) {
		return InitService.cache.put(key, value);
	}
	
	/**
	 * 根据键从缓存中获取对应的值
	 * @Date 2016-9-25下午8:17:18
	 * @param key 键
	 * @return 键对应的值
	 */
	public static String getCacheData(String key) {
		return InitService.cache.get(key);
	}
	
	/**
	 * 从缓存中删除指定键对应的值
	 * @Date 2016-9-25下午8:20:46
	 * @param key 键
	 * @return
	 */
	public static String removeCacheData(String key) {
		return InitService.cache.remove(key);
	}
}
