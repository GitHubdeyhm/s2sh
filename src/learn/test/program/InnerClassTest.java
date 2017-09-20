package learn.test.program;

/**
 * 内部类语法测试
 * @Date 2017-4-18下午1:08:35
 */
public class InnerClassTest {
	
	private static final int num = 100;
	String name = "米老鼠";
	
	void visit() {
		System.out.println(InnerClassStaticInner.test);
	}
	
	//定义一个局部内部类：在方法中定义一个类
	void classMethod(final String var) {
		class InnerClassMethod {
			void print() {
				System.out.println("调用了局部内部类的打印方法:"+var);
			}
		}
		//只能在定义类的方法中实例化局部内部类，外部无法访问局部内部类
		InnerClassMethod icm = new InnerClassMethod();
		icm.print();
	}
	
	public static void main(String[] args) {
		InnerClassTest test = new InnerClassTest();
		test.visit();
		
		test.classMethod("方法参数");
		final int num = 10000;
		//声明一个匿名内部类
		Runnable run = new Runnable() {
			public void run() {
				System.out.println("run"+num);
			}
		};
		new Thread(run).start();
		
		//实例化内部类
		InnerClassTest.InnerClassInner inner = test.new InnerClassInner();
		System.out.println(inner.desc);//访问内部类成员变量
		inner.test();
		System.out.println(InnerClassInner.test);
		System.out.println(InnerClassStaticInner.test);
		
		
		//实例化静态内部类
		InnerClassStaticInner is = new InnerClassStaticInner();
		System.out.println(is.desc);
		
	}
	
	//定义一个非静态内部类
	class InnerClassInner {
		
		InnerClassInner() {
			System.out.println("初始化InnerClassInner构造器");
		}
		
		private String desc = "非静态内部类";
		private int age;
		private static final String test = "l";
		
		void test() {
			System.out.println("InnerClassInner#test"+num);//可以访问外部类的私有成员
		}		
	}
	
	//定义一个静态内部类
	private static class InnerClassStaticInner {
		
		InnerClassStaticInner() {
			System.out.println("初始化InnerClassStaticInner构造器");
		}
		
		private String desc = "静态内部类";
		private int age;
		private static final String test = "llllll";
		
		void test() {
			System.out.println(num);//可以访问外部类的私有成员
		}		
	}

}
