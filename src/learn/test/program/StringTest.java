package learn.test.program;



/**
 * 字符串测试
 * @Date 2017-3-2下午9:43:02
 */
public class StringTest {

	public static void main(String[] args) {
		String a = "hello";
		String b = "hello";
		String c = b;
		/**
		 * 1、直接new创建的字符串不会放入对象池中
		 * 2、实际上创建两个对象，new会创建一个及字符串hello本身会创建一个
		 */
		String d = new String("hello");
		String f = "hel"+"lo";
		String g = new String("hello").intern();//调用intern()方法会将字符串放入对象池中
		
		System.out.println(a == b);//true
		System.out.println(b == c);//true
		System.out.println(a == d);//false
		System.out.println(f == a);//true
		System.out.println(g == a);//true
		
		StringTest st = new StringTest();
		System.out.println(st.test() == a);//false
		
	}
	
	public String test() {
		StringBuilder sb = new StringBuilder();
		sb.append("hello");
		return sb.toString();
	}
}
