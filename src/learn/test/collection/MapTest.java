package learn.test.collection;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

/**
 * Map集合测试
 * @Date 2017-4-30下午11:03:36
 */
public class MapTest {
	
	public static void main(String[] args) {
		String empty = null;
		Map<String, String> map = new HashMap<>();
		map.put(empty, "null");
		map.put(empty, "empty");
		map.put("key", "value");
		System.out.println(map);
		Set<String> set = map.keySet();
		System.out.println(set.size());
		for (String str : set) {
			System.out.println(map.get(str));
		}
		
		map = new LinkedHashMap<>();
		
	}

}
