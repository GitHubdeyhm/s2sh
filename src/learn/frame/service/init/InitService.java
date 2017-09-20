package learn.frame.service.init;

import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import javax.annotation.Resource;

import learn.frame.entity.common.DataDictionaryEntity;
import learn.frame.service.common.IDataDictionaryService;
import learn.frame.utils.CacheUtil;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Service;

/**
 * 项目初始化service
 * @Date 2016-6-11 下午11:04:02
 */
@Service
public class InitService implements InitializingBean {
	
	private static final Logger log = Logger.getLogger(InitService.class);
	
	@Resource
	private IDataDictionaryService dataDictionaryService;
	
	/**通过map对象模拟缓存--可以存放json串*/
	public static final Map<String, String> cache = new ConcurrentHashMap<String, String>();

	@Override
	public void afterPropertiesSet() throws Exception {
		log.info("------项目初始化开始---------");
		
		//显示系统属性
		getSystemInfo();
		
		//初始化缓存信息
		initCache();
		
		log.info("\n初始化缓存信息为："+cache+"\n");
		
		log.info("------项目初始化完成---------");
	}
	
	/**
	 * 初始化项目用到的缓存
	 * @Date 2016-12-3下午10:39:07
	 * @return
	 */
	private boolean initCache() {
		List<DataDictionaryEntity> dictList = dataDictionaryService.findByAll(null); 
		int size = (dictList == null) ? 0 : dictList.size();
		for (int i = 0; i < size; i++) {
			DataDictionaryEntity dict = dictList.get(i);
			CacheUtil.putCacheData(CacheUtil.CACHE_KEY_DICT+dict.getDictKey(), dict.getDictValue());
		}
		return true;
	}
	
	/**
	 * 获取系统相关的属性
	 * @Date 2016-10-15下午9:36:22
	 */
	public void getSystemInfo() {
		System.out.println("----------------------系统属性：------------------------------------");
		Properties pro = System.getProperties();
		Set<Object> s = pro.keySet();
		for (Object obj : s) {
			System.out.println(obj+"="+pro.getProperty((String)obj));
		}
		System.out.println("----------------------------------------------------------");
	}
}
