package learn.test.reflection.loader;

import java.io.IOException;
import java.net.URL;
import java.util.Enumeration;

/**
 * 类加载器测试
 * @Date 2017-5-24下午5:20:42
 */
public class LoaderTest {
	
	public static void main(String[] args) throws ClassNotFoundException {
		LoaderTest test = new LoaderTest();
		/**
		 * 调用loadClass方法控制台没有打印类InnerLoaderTest初始化块信息
		 * 说明调用ClassLoader类的loadClass方法时只处于加载类的阶段，不会初始化类。
		 */
		/*ClassLoader cl = ClassLoader.getSystemClassLoader();//获取系统类加载器
		cl.loadClass("learn.test.reflection.loader.InnerLoaderTest");*/
		
		//System.out.println(test.getClass().getClassLoader() == ClassLoader.getSystemClassLoader());//true
		//System.out.println(InnerLoaderTest.class == cl.loadClass("learn.test.reflection.loader.InnerLoaderTest"));
		
		/**
		 * 调用Class.forName()方法控制台打印了类InnerLoaderTest初始化块信息
		 * 说明调用Class.forName()方法时初始化了类。
		 */
		//Class.forName("learn.test.reflection.loader.InnerLoaderTest");
		//Class.forName("learn.test.reflection.loader.InnerLoaderTest");//类只初始化一次
		
		System.out.println(System.getProperty("java.ext.dirs"));
		test.printClassLoader();
		
		
	}
	
	void printClassLoader()  {
		//获取系统类加载器
		ClassLoader cl = ClassLoader.getSystemClassLoader();
		System.out.println("系统类加载器："+cl);
		
		Enumeration<URL> em;
		try {
			//获取系统类加载器的所有加载的资源，可以是图片、声音等文件
			em = cl.getResources("");
			while (em.hasMoreElements()) {
				System.out.println(em.nextElement());
			}
			//获取系统类加载器的父类加载器：扩展类加载器
			ClassLoader extLoader = cl.getParent();
			System.out.println("扩展类加载器："+extLoader);
			Enumeration<URL> extEm = extLoader.getResources("");
			while (extEm.hasMoreElements()) {
				System.out.println(extEm.nextElement());
			}
			//一些实现可能使用 null来表示引导类加载器
			System.out.println("引导类加载器："+extLoader.getParent());//null
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

} 

class InnerLoaderTest {
	
	static {
		System.out.println("调用了InnerLoaderTest类的静态块");
	}
}



