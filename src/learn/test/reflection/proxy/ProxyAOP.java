package learn.test.reflection.proxy;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

/**
 * 实现基于JDK代理的aop
 * @Date 2017-5-27下午5:29:14
 */
public class ProxyAOP {
	
	public static void main(String[] args) {
		Dog dog = new RunDog();
		Dog proxyDog = (Dog)ProxyFactory.getProxy(dog);
		Dog proxyDog2 = (Dog)ProxyFactory.getProxy(dog);
		System.out.println(proxyDog == proxyDog2);//false
		//调用方法，则实现了在方法前后分别执行了一段代码
		proxyDog.info();
		proxyDog.run();
	}
}

//定义一个接口，提供两个方法
interface Dog {
	
	void info();
	
	void run();
}

/**
 * 定义该接口的实现类
 * 现在想实现在两个方法info()和run()调用前在调用一段通用的代码，不能硬编码实现。
 * @Date 2017-5-27下午5:34:25
 */
class RunDog implements Dog {

	@Override
	public void info() {
		System.out.println("我是一只小狗info");
	}

	@Override
	public void run() {
		System.out.println("我会奔跑run");
	}
}

//这里模拟要调用的通用代码片段
class DogUtil {
	void before() {
		System.out.println("----在方法前调用----");
	}
	void after() {
		System.out.println("-----在方法后调用----");
	}
}

class ProxyAOPHandler implements InvocationHandler {

	//proxy表示需要被代理的对象
	private Object target;
	
	public ProxyAOPHandler(Object target) {
		this.target = target;
	}
	
	@Override
	public Object invoke(Object proxy, Method method, Object[] args)
			throws Throwable {
		DogUtil du = new DogUtil();
		du.before();
		
		Object res = method.invoke(target, args);
		System.out.println(res);
		
		du.after();
		return null;
	}
}

//生产代理对象的工厂类
class ProxyFactory {
	
	//为指定的接口生成代理实例
	public static Object getProxy(Object target) throws IllegalArgumentException {
		InvocationHandler handler = new ProxyAOPHandler(target);
		Class<?> tc = target.getClass();
		return Proxy.newProxyInstance(tc.getClassLoader(), 
				tc.getInterfaces(), handler);
	}
}
