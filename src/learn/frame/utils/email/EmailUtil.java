package learn.frame.utils.email;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.Date;
import java.util.Properties;

import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.activation.FileDataSource;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import javax.mail.internet.MimeUtility;

/** 
 * @Date 2017-2-13 下午03:39:30
 */
public class EmailUtil {
	
	/**MIME 文本类型*/
	public static final String MIME_TEXT = "text/plain;charset=gb2312";
	/**MIME html类型*/
	public static final String MIME_HTML = "text/html;charset=gb2312";
	
	
	public static void sendEmail(Session session, String from, String to, 
			String subject, Object content, String mimeType) {
		//得到邮件对象
		MimeMessage message = new MimeMessage(session);
		try {
			message.setFrom(new InternetAddress(from));//设置发件人
			message.setRecipients(Message.RecipientType.TO, to);//设置单个收件人
			message.setSentDate(new Date());//设置发送日期
			message.setSubject(subject);//设置邮件主题
			message.setContent(content, mimeType);
			message.saveChanges();//生成邮件内容，当修改了邮件时也需调用一次该方法确保修改生效
			//将邮件内容保存到本地文件，文件扩展名为eml。可以用Foxmail直接打开查看邮件内容
			message.writeTo(new FileOutputStream("f:\\test.eml"));
		} catch (MessagingException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 发送纯文本内容
	 * @auther huangXiaoLin.huangxl@strongit.com.cn
	 * 2017-2-13下午03:41:53
	 * @param from
	 * @param to
	 * @param subject
	 * @param content
	 */
	public static MimeMessage sendTextEmail(Session session, String from, String to, 
			String subject, String content) {
		//设置mime类型也可实现发送纯文本内容
		//sendEmail(from, to, subject, content, "text/plain;charset=gb2312");
		//发送纯文本内容实现方式2
		//得到邮件对象
		MimeMessage message = new MimeMessage(session);
		try {
			message.setFrom(new InternetAddress(from));//设置发件人
			message.setRecipients(Message.RecipientType.TO, to);//设置单个收件人
			message.setSentDate(new Date());//设置发送日期
			message.setSubject(subject);//设置邮件主题
			message.setText(content);//设置邮件内容，该方法只能设置纯文本内容
			message.saveChanges();//生成邮件内容，当修改了邮件是也需调用一次该方法确保修改生效
			message.writeTo(new FileOutputStream("f:\\test.eml"));//将邮件内容保存到本地文件
		} catch (MessagingException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return message;
	}
	
	/**
	 * 发送内容为html格式的邮件，可以使用img标签引入图片
	 * @auther huangXiaoLin.huangxl@strongit.com.cn
	 * @Date 2017-2-13下午03:54:13
	 * @param from 发件人
	 * @param to 收件人
	 * @param subject 邮件主题
	 * @param content 邮件内容
	 */
	public static void sendHTMLEmail(String from, String to, String subject, 
			String content) {
		//sendEmail(from, to, subject, content, MIME_HTML);
	}
	
	public static void sendImageEmail(String from, String to, String subject, 
			String content) {
		Properties props = new Properties();
		Session session = Session.getDefaultInstance(props);
		//得到邮件对象
		MimeMessage message = new MimeMessage(session);
		try {
			message.setFrom(new InternetAddress(from));//设置发件人
			message.setRecipients(Message.RecipientType.TO, to);//设置单个收件人
			message.setSentDate(new Date());//设置发送日期
			message.setSubject(subject);//设置邮件主题
			
			//创建包含复杂邮件内容的对象，包括图片
			MimeMultipart multipart = new MimeMultipart("related");
			multipart.addBodyPart(createHTMLBody(content));//添加的邮件内容
			multipart.addBodyPart(createImageBody("test_jpg_id", "d:\\java\\test.jpg"));
			
			message.setContent(multipart);
			message.saveChanges();//生成邮件内容，当修改了邮件是也需调用一次该方法确保修改生效
			//将邮件内容保存到本地文件，文件扩展名为eml。可以用Foxmail直接打开查看邮件内容
			message.writeTo(new FileOutputStream("f:\\test.eml"));
			System.out.println("---------");
		} catch (MessagingException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	//包含附件的邮件
	public static void sendAttachEmail(String from, String to, String subject, 
			String content, String mimeType) {
		Properties props = new Properties();
		Session session = Session.getDefaultInstance(props);
		//得到邮件对象
		MimeMessage message = new MimeMessage(session);
		try {
			message.setFrom(new InternetAddress(from));//设置发件人
			message.setRecipients(Message.RecipientType.TO, to);//设置单个收件人
			message.setSentDate(new Date());//设置发送日期
			message.setSubject(subject);//设置邮件主题
			
			//创建包含复杂邮件内容的对象，包括图片和附件
			MimeMultipart multipart = new MimeMultipart("related");
			multipart.addBodyPart(createHTMLBody(content));//添加的邮件内容
			multipart.addBodyPart(createImageBody("test_jpg_id", "F:\\IBMS2图标\\btn\\按钮右.png"));//添加图片邮件
			multipart.addBodyPart(createAttachBody("F:\\learn\\web打印控件\\Lodop6.2技术手册.doc"));//添加附件
			
			message.setContent(multipart);
			message.saveChanges();//生成邮件内容，当修改了邮件是也需调用一次该方法确保修改生效
			//将邮件内容保存到本地文件，文件扩展名为eml。可以用Foxmail直接打开查看邮件内容
			message.writeTo(new FileOutputStream("f:\\test.eml"));
			System.out.println("---------");
		} catch (MessagingException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	
	public static MimeBodyPart createTextBody(String content) {
		MimeBodyPart textBody = new MimeBodyPart();
		try {
			textBody.setContent(content, MIME_TEXT);//设置这部分邮件内容以文本格式处理
		} catch (MessagingException e) {
			e.printStackTrace();
		}
		return textBody;
	}

	/**
	 * 生成html格式的邮件片段，可以通过img标签引入外部图片实现包含图片的邮件
	 * @Date 2017-2-14下午10:38:12
	 * @param content html内容
	 */
	public static MimeBodyPart createHTMLBody(String content) throws MessagingException {
		MimeBodyPart htmlBody = new MimeBodyPart();
		htmlBody.setContent(content, MIME_HTML);//设置这部分邮件内容以html格式处理
		return htmlBody;
	}
	
	/**
	 * 生成图片邮件片段
	 * @Date 2017-2-14上午09:14:48
	 * @param cid 
	 * @param path 图片路径
	 */
	public static MimeBodyPart createImageBody(String cid, String path) throws MessagingException {
		MimeBodyPart imageBody = new MimeBodyPart();
		DataSource ds = new FileDataSource(path);//activation.jar
		imageBody.setDataHandler(new DataHandler(ds));//activation.jar
		
		imageBody.setContentID(cid);//设置内容ID，对应img标签的src属性值
		//也可通过setHeader()方法设置内存ID，如下：
		//imageBody.setHeader("Content-ID", "test_jpg_id");
		
		/*
		 * 这里可以不必设置图片的mime类型，因为DataSource对象能够返回数据源的mime类型，
		 * 因此imageBody.setDataHandler()方法能够设置Content-Type消息头
		 */
		//imageBody.setHeader("Content-Type", "image/jpg");
		return imageBody;
	}
	
	/**
	 * 生成附件邮件片段，显示在邮件的附件名为附件的名称
	 * @Date 2017-2-14下午10:35:37
	 * @param path 附件路径
	 */
	public static MimeBodyPart createAttachBody(String path) throws MessagingException, UnsupportedEncodingException {
		MimeBodyPart attachBody = new MimeBodyPart();
		FileDataSource fds = new FileDataSource(path);
		attachBody.setDataHandler(new DataHandler(fds));
		attachBody.setFileName(MimeUtility.encodeText(fds.getName()));//中文乱码
		return attachBody;
	}
	
	
	public static void main(String[] args) {
		String from = "linyouxiang7@163.com";
		String to = "205963064@qq.com";
		String subject = "subject";
		String content = "content中文内容";
		
		String server163 = "smtp.163.com";//163邮箱服务器
//		String serverQQ = "smtp.qq.com";//QQ邮箱服务器，端口为465或587
//		
//		String htmlStr = "<h1>h1标签字体</h1><br/><br/><p>这是一个段落</p><img src=\"https://ss0.bdstatic.com/5aV1bjqh_Q23odCf/static/superman/img/logo/logo_white_fe6da1ec.png\"/>";
//		
//		String MultHtmlStr = "<h1>h1标签字体</h1><br/><br/><p>这是一个段落</p><img src=\"cid:test_jpg_id\"/>";
		//EmailUtil.sendTextEmail(from, to, subject, MultHtmlStr);
//		EmailUtil.sendImageEmail(from, to, subject, MultHtmlStr);
//		EmailUtil.sendAttachEmail(from, to, subject, MultHtmlStr, "");
		
		//设置邮件属性
		Properties props = new Properties();
		props.setProperty("mail.transport.protocol", "smtp");//设置邮件传输协议，smtp协议默认端口号为25
		props.setProperty("mail.smtp.port", "587");//设置smtp协议端口，默认为25
		props.setProperty("mail.smtp.auth", "true");//必须设置权限认证为true才会提交用户认证
		Session session = Session.getInstance(props);
		session.setDebug(true);
		try {
			Transport trans = session.getTransport();
			trans.connect(server163, from, "linYXdemima0");
//			trans.connect(serverQQ, to, "QQdemima0");
			
			MimeMessage message = new MimeMessage(session);
			message.setFrom(new InternetAddress(from));//设置发件人
			message.setRecipients(Message.RecipientType.TO, to);//设置单个收件人
			message.setSentDate(new Date());//设置发送日期
			message.setSubject(subject);//设置邮件主题
			message.setText(content);//设置邮件内容，该方法只能设置纯文本内容
			message.saveChanges();//生成邮件内容，当修改了邮件是也需调用一次该方法确保修改生效
			message.writeTo(new FileOutputStream("f:\\test.eml"));//将邮件内容保存到本地文件
			/**
			 * 
			 */
			trans.sendMessage(message, message.getRecipients(Message.RecipientType.TO));
			trans.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
