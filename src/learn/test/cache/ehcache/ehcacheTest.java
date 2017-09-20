package learn.test.cache.ehcache;

import net.sf.ehcache.Cache;
import net.sf.ehcache.CacheManager;
import net.sf.ehcache.Element;

/**
 * @Date 2017-6-11下午8:52:25
 */
public class ehcacheTest {
	
	public static void main(String[] args) throws InterruptedException {
		CacheManager cm = CacheManager.create("src/learn/test/cache/ehcache/ehcache.xml");
		Cache cache = cm.getCache("sampleCache1");
		System.out.println(cache);
		Cache cache2 = cm.getCache("sampleCache1");
		System.out.println(cache == cache2);
		Element ele = new Element("c1", "c1_value");
		cache.put(ele);
		Element e = cache.get("c1");
		System.out.println(e.getObjectValue());
		
		for (int i = 0; i < 200; i++) {
			cache.put(new Element("c"+i, "value_"+i));
		}
		Thread.sleep(30000);
		System.out.println(cache.get("c0"));
		
		//不存在返回null
		System.out.println(cache.get("a"));
		
		//刷新缓存
		cache.flush();
		//关闭缓存管理器
		cm.shutdown();
	}
}
