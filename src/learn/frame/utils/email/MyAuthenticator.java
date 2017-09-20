package learn.frame.utils.email;

import javax.mail.Authenticator;
import javax.mail.PasswordAuthentication;

/** 
 * 自定义权限认证类
 * @Date 2017-2-14 下午06:10:57
 */
public class MyAuthenticator extends Authenticator {
	
	private String userName;
	private String password;
	
	public MyAuthenticator(String userName, String password) {
		this.userName = userName;
		this.password = password;
	}
	
	@Override
	protected PasswordAuthentication getPasswordAuthentication() {
		return new PasswordAuthentication(userName, password);
	}

}
