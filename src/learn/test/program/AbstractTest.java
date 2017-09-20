package learn.test.program;

/**
 * 抽象类继承测试
 * @Date 2017-3-5上午11:26:51
 */
public class AbstractTest {

	public static void main(String[] args) {
		new Children();
	}
}

abstract class AbstractParent {
	String name = "这是父类Parent的变量";
	
	public AbstractParent() {
		System.out.println("调用了父类AbstractParent的构造方法");
		method();//此时调用的是子类的实现方法,此时num值为0
	}
	
	public void test() {
		System.out.println("调用了父类Parent的test方法");
	}
	
	public final void testFinal() {
		System.out.println("调用了父类Parent的testFinal方法");
	}
	
	public abstract void method();
}

class Children extends AbstractParent {

	private int num = 10;
	
	public Children() {
		System.out.println("num="+num);
		num = 500;
		System.out.println("num="+num);	
	}
	
	@Override
	public void method() {
		testFinal();
		System.out.println("调用了子类Children的method方法"+num);
	}
}