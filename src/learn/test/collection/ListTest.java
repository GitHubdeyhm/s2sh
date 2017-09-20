package learn.test.collection;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;
import java.util.Vector;

import learn.test.json.TestVo;

/**
 * List集合测试
 * @Date 2017-4-27下午5:26:09
 */
public class ListTest {
	
	public static void main(String[] args) {
		TestVo tv = null;
		TestVo test = new TestVo();
		List<TestVo> list = new ArrayList<>();
		list.add(TestVo.getTestVo());
		list.add(test);
		list.add(test);
		list.add(tv);
		Iterator<TestVo> it = list.iterator();
		while (it.hasNext()) {
			TestVo t = it.next();
			//list.remove(t);//抛出异常
			//it.remove();//正常删除的写法
		}
//		for (TestVo t : list) {
//			list.remove(t);//抛出异常
//		}
		System.out.println(list.size());
		
		Set<Object> set = new HashSet<>();
		set.add(tv);
		set.add(test);
		set.add(test);//无法添加重复的元素
		set.add(tv);
		set.add(new InnerEquals());
		set.add(new InnerEquals());
		set.add(new InnerHashCode());
		System.out.println(set);
		System.out.println(set.contains(new InnerHashCode()));
		
		Set<String> strSet = new HashSet<>();
		strSet.add(new String("aaaa"));
		strSet.add(new String("aaaa"));//HashSet集合通过equals方法判断是否重复
		System.out.println("strSet集合大小："+strSet.size());
		
		set = new LinkedHashSet<>();
		set.add(tv);
		System.out.println(set.add(tv));
		set.add("aaa");
		set.add(new InnerHashCodeEquals());
		set.add(new InnerEquals());
		set.add(new InnerEquals());
		System.out.println("LinkedHashSet-->"+set);
		
		
		Set<InnerEquals> treeSet = new TreeSet<>();
		//treeSet.add(tv);//不能添加null对象
		treeSet.add(new InnerEquals());
		treeSet.add(new InnerEquals());
		System.out.println(treeSet.add(new InnerEquals()));
		System.out.println("TreeSet-->"+treeSet);
		
//		treeSet = new TreeSet<>(new Comparator<InnerEquals>() {
//			@Override
//			public int compare(InnerEquals o1, InnerEquals o2) {
//				return 0;
//			}
//		});
//		treeSet.add(new InnerEquals());
//		treeSet.add(new InnerEquals());
//		System.out.println(treeSet.add(new InnerEquals()));
//		System.out.println("TreeSet-->"+treeSet);
		List<String> arr = Arrays.asList("a", "b", "c");
		for (String str : arr) {
			System.out.println(str);
		}
		arr.remove("a");
	}

}
//重写hashcode()方法
class InnerHashCode {
	@Override
	public int hashCode() {
		return 1;
	}
}
//重写equals()方法
class InnerEquals implements Comparable{
	@Override
	public boolean equals(Object obj) {
		return true;
	}

	@Override
	public int compareTo(Object o) {
		return 0;
	}
}
//同时重写hashcode()和equals()方法
class InnerHashCodeEquals {
	@Override
	public int hashCode() {
		return 1;
	}
	@Override
	public boolean equals(Object obj) {
		return true;
	}
}
