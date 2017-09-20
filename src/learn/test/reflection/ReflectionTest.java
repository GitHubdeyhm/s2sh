package learn.test.reflection;

import java.io.Serializable;
import java.lang.annotation.Annotation;
import java.lang.reflect.Array;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;

/**
 * 反射测试类
 * @Date 2017-5-24下午4:33:52
 */
public class ReflectionTest {
	
	public static void main(String[] args) throws Exception {
		//ReflectionTest test = new ReflectionTest();
		//test.testClass();
		String name = "learn.test.reflection.ReflectionVo";
		Class<?> clazz = Class.forName(name);
		//通过构造方法创建对象
		Constructor<?> cons = clazz.getConstructor(String.class, Integer.class);
		Object obj = cons.newInstance("名称", 15);
		ReflectionVo ref = (ReflectionVo)obj;
		System.out.println(obj);
		
		//调用指定对象的方法，静态方法设置为null
		Method m = clazz.getMethod("getUserId");
		m.invoke(ref);
		Method statM = clazz.getDeclaredMethod("getStat");
		statM.invoke(null);
		//调用私有方法默认没有权限
		Method pri = clazz.getDeclaredMethod("getPrivate");
		pri.setAccessible(true);//需设置为true才能访问私有方法
		pri.invoke(ref);
		
		//访问指定的字段
		Field[] fields = clazz.getDeclaredFields();
		for (Field f : fields) {
			f.setAccessible(true);
			if ("serialVersionUID".equals(f.getName())) {
				System.out.println(f.get(null));
			} else {
				//f.setAccessible(true);
				System. out.println(f.get(ref));
			}
		}
		
		//操作数组
		Object arr = Array.newInstance(String.class, 4);
		Array.set(arr, 0, "a");
		Array.set(arr, 1, "b");
		Object a = Array.get(arr, 0);
		System.out.println(a);
	}
	
	
	//测试该类的基本方法
	public void testClass() throws ClassNotFoundException, InstantiationException, IllegalAccessException {
		String str = "hello";
		System.out.println(str.getClass() == str.getClass());//true
		//通过反射查看类的信息
		String name = "learn.test.reflection.ReflectionVo";
		
		Class<?> clazz = Class.forName(name);
		System.out.println(clazz.newInstance());//创建一个实例
		
		System.out.println(clazz.getClassLoader());
		
		//字段
		Field[] fields = clazz.getFields();
		for (Field f : fields) {
			System.out.println(f.getType()+"--"+f.getName());
		}
		Field[] fields2 = clazz.getDeclaredFields();
		for (Field f : fields2) {
			System.out.println(f.getType()+"--"+f.getName());
		}
		
		//构造方法
		Constructor<?>[] cons = clazz.getConstructors();
		for (Constructor<?> c : cons) {
			System.out.println(c);
		}
		Constructor<?>[] cons2 = clazz.getDeclaredConstructors();
		for (Constructor<?> c : cons2) {
			System.out.println(c);
		}
		
		System.out.println("\n-------普通方法--------\n");
		Method[] mes = clazz.getMethods();
		for (Method m : mes) {
			System.out.println(m);
		}
		Method[] mes2 = clazz.getDeclaredMethods();
		for (Method m : mes2) {
			System.out.println(m);
		}
		
		System.out.println("\n-------类注解--------\n");
		Annotation[] anns = clazz.getAnnotations();
		for (Annotation a : anns) {
			System.out.println(a);
		}
		Annotation[] anns2 = clazz.getDeclaredAnnotations();
		for (Annotation a : anns2) {
			System.out.println(a);
		}
		//Entity anns3 = clazz.getAnnotation(Entity.class);
		
		Class<?>[] cl = clazz.getDeclaredClasses();
		System.out.println(cl.length);
		
		System.out.println("\n------继承的父类和接口--------\n");
		Class<?>[] inters = clazz.getInterfaces();
		for (Class<?> i : inters) {
			System.out.println(i);
		}
		System.out.println(clazz.getSuperclass());
		System.out.println(clazz.getPackage());
		System.out.println(clazz.getName()+"--"+clazz.getSimpleName());
		
		
		System.out.println("\n------一些判断方法--------\n");
		System.out.println(clazz.isAnnotation());
		System.out.println(Serializable.class.isInterface());
		System.out.println(String[].class.isArray());
		System.out.println(clazz.isAnnotationPresent(Override.class));
		System.out.println(clazz.isInstance(new ReflectionVo()));
		
		System.out.println(Serializable.class.isAssignableFrom(clazz));
		
		
	}

}
