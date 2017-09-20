package learn.test.program;

/**
 * java的继承语法测试
 * @Date 2017-3-4下午11:17:36
 */
public class ExtendTest {

	public static void main(String[] args) {
		Parent p = new Child();
		p.test();//调用了子类Child的test方法
		System.out.println(p.name);//这是父类Parent的变量
	}
}

class Parent {
	String name = "这是父类Parent的变量";
	
	public Parent() {
		System.out.println("调用了父类Parent的构造方法");
	}
	
	public void test() {
		System.out.println("调用了父类Parent的test方法");
	}
}

class Child extends Parent {
	String name = "这是子类Child的变量";
	
	public Child() {
		System.out.println("调用了子类Child的构造方法");
	}
	
	public void test() {
		System.out.println("调用了子类Child的test方法");
	}
}