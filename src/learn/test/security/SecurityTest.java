package learn.test.security;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.Provider;
import java.security.Security;

/**
 * @Date 2017-6-11下午11:43:02
 */
public class SecurityTest {
	
	public static void main(String[] args) {
		//获取jdk提供者列表
		/*Provider[] pros = Security.getProviders();
		for (Provider pro : pros) {
			System.out.println(pro);
		}*/
		
		try {
			//获取指定的算法实例
			MessageDigest sha = MessageDigest.getInstance("SHA");
			//更新摘要信息
			sha.update("sha".getBytes());
			byte[] output = sha.digest();
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
	}
}
