package learn.test.reflection.proxy;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

import learn.frame.service.uums.IUserService;

/**
 * jdk代理测试
 * @Date 2017-5-26下午1:34:47
 */
public class ProxyTest {

	public static void main(String[] args) throws Exception {
		
		InvocationHandler handler = new ProxyHandler();
		//创建一个代理类
		Class<?> proxy = Proxy.getProxyClass(IUserService.class.getClassLoader(), 
					new Class[]{IUserService.class});
		System.out.println(proxy);
		Constructor<?> con = proxy.getConstructor(InvocationHandler.class);
		IUserService us = (IUserService)con.newInstance(handler);
		us.delete("2");
		
		/*IUserService service = (IUserService)Proxy.newProxyInstance(IUserService.class.getClassLoader(), 
				new Class[]{IUserService.class}, handler);
		service.delete("a");*/
	}
}

class ProxyHandler implements InvocationHandler {

	/**
	 * @param proxy 代理接口的创建的对象
	 * @param method 调用的方法
	 * @param args 传人方法的参数
	 * 
	 */
	@Override
	public Object invoke(Object proxy, Method method, Object[] args)
			throws Throwable {
		System.out.println("----"+method.getName());
		return null;
	}
	
}
