package learn.frame.utils.file;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.RandomAccessFile;
import java.net.URL;
import java.net.URLConnection;
import java.util.Properties;

import learn.frame.common.MessageConstant;
import learn.frame.exception.ServiceException;
import learn.frame.utils.StringUtil;

import org.apache.log4j.Logger;

/**
 * 文件工具类，封装对文件的常用操作，包括对属性文件的操作
 * @Date 2016-7-3 下午10:04:02
 */
public class FileUtil {
	
	private static final Logger log = Logger.getLogger(FileUtil.class);

	/**
	 * 从类路径下加载属性配置文件
	 * @Date 2016-6-29下午04:12:27
	 * @param fileName 属性文件名称
	 * @return 属性对象。如果文件名称为null或者指定文件名不存在则返回null
	 */
	public static Properties loadPropertiesFile(String fileName) {
		Properties prop = null;
		if (!StringUtil.isNullOrEmpty(fileName)) {
			//加载类路径下的属性文件
			InputStream in = FileUtil.class.getResourceAsStream("/"+fileName+".properties");
			if (in != null) {
				prop = new Properties();
				try {
					prop.load(in);
				} catch (IOException e) {
					prop = null;
				}
			}
		}
		return prop;
	}
	
	/**
	 * 得到属性文件的指定属性键对应的值
	 * @Date 2016-7-3下午10:09:55
	 * @param fileName 文件名称
	 * @param key 键
	 * @return 如果指定文件名或键不存在，则返回null
	 */
	public static String getPropertiesValueByKey(String fileName, String key) {
		if (!StringUtil.isNullOrEmpty(key)) {
			Properties prop = loadPropertiesFile(fileName);
			if (prop != null) {
				return prop.getProperty(key);
			}
		}
		return null;
	}
	
	/**
	 * 得到属性文件的指定属性键对应的值，可以对不存在指定键时指定默认值
	 * @Date 2016-7-3下午10:09:55
	 * @param fileName 文件名称
	 * @param key 键
	 * @param defaultValue 当指定键不存在时的默认值
	 * @return 如果指定文件名不存在，则返回null
	 */
	public static String getPropertiesValueByKey(String fileName, String key, String defaultValue) {
		if (!StringUtil.isNullOrEmpty(key)) {
			Properties prop = loadPropertiesFile(fileName);
			if (prop != null) {
				return prop.getProperty(key, defaultValue);
			}
		}
		return null;
	}
	/**
	 * 在指定文件的末尾添加指定内容
	 * @Date 2016-10-12下午10:26:50
	 * @param f File对象
	 * @param content 添加的内容
	 * @return
	 */
	public static boolean append(File f, String content) throws IOException {
		//读写方式打开文件
		RandomAccessFile raf = null;
		try {
			raf = new RandomAccessFile(f, "rw");
			raf.seek(raf.length());//定位到文件的末尾
			raf.write(content.getBytes());
		} catch (IOException e) {
			throw e;
		} finally {
			if (raf != null) {
				raf.close();
			}
		}
		return true;
	}
	
	/**
	 * 在文件的指定位置添加内容,RandomAccessFile类没有方法可以直接在文件的指定位置
	 * 添加文件内容，如果直接在文件的指定位置写入内容则会覆盖后面的文件内容。因此需要将文件
	 * 指定位置后面的内容读入缓存区，在写入添加的内容，最后将原本文件后面的内容添加到文件末尾
	 * @Date 2016-10-15下午10:03:03
	 * @param f File对象
	 * @param pointer 位置
	 * @param content 添加的内容
	 * @return
	 * @throws IOException
	 */
	public static boolean append(File f, long pointer, String content) throws IOException {
		//读写方式打开文件
		RandomAccessFile raf = null;
		FileInputStream fis = null;
		FileOutputStream fos = null;
		//创建一个临时文件，该文件在java虚拟机退出时自动删除
		File tempFile = File.createTempFile("test", null);//扩展名默认为.tmp
		try {
			raf = new RandomAccessFile(f, "rw");
			raf.seek(pointer);
			//临时文件输入输出流
			fis = new FileInputStream(tempFile);
			fos = new FileOutputStream(tempFile);
			byte[] buf = new byte[1024];
			int hasRead = 0;
			//1、首先将指定位置后的文件内容写入到临时文件中
			while ((hasRead = raf.read(buf)) > 0) {
				fos.write(buf, 0, hasRead);
			}
			//2、重新定位到指定位置
			raf.seek(pointer);
			//3、写入要添加的内容
			raf.write(content.getBytes());
			//4、写入文件后的内容
			while ((hasRead = fis.read(buf)) > 0) {
				raf.write(buf, 0, hasRead);
			}
		} catch (IOException e) {
			throw e;
		} finally {
			if (raf != null) {
				raf.close();
			}
			if (fis != null) {
				fis.close();
			}
			if (fos != null) {
				fos.close();
			}
		}
		return true;
	}
	
	/**
	 * 根据url读取二进制文件，可用于从网络中读取二进制文件
	 * @Date 2016-12-30下午9:43:24
	 * @param url URL对象
	 * @return
	 */
	public static boolean readBinaryFile(URL url) throws ServiceException {
		long beginTime = System.currentTimeMillis();
		String urlFile = url.getFile();
		if (StringUtil.isNullOrEmpty(urlFile)) {
			throw new ServiceException(MessageConstant.FILE_NOT_EXIST_MSG);
		}
		String fileName = urlFile.substring(urlFile.lastIndexOf("/")+1);
		if (StringUtil.isNullOrEmpty(fileName)) {
			throw new ServiceException(MessageConstant.FILE_NOT_EXIST_MSG);
		}
		InputStream in = null;
		OutputStream out = null;
		try {
			URLConnection conn = url.openConnection();
			//得到文件的长度：如果长度未知或者超过int的最大值则返回-1
			int len = conn.getContentLength();
			System.out.println("文件类型："+conn.getContentType()+"文件名："+urlFile+"文件长度："+len);
			System.out.println("保存的文件名："+fileName);
			//System.out.println(System.getProperty("user.dir"));
			if (len <= 0) {
				throw new ServiceException(MessageConstant.FILE_NOT_EXIST_MSG);
			}
			in = conn.getInputStream();
			byte[] data = new byte[len];
			int offset = 0;
			while (offset < len) {
				int count = in.read(data, offset, len-offset);
				if (count == -1) {
					break;
				}
				offset += count;
			}
			if (offset != len) {
				throw new ServiceException(MessageConstant.FILE_NOT_EXIST_MSG);
			}
			out = new FileOutputStream(fileName);
			out.write(data);
		} catch (IOException e) {
			log.error(e);
		} finally {
			if (in != null) {
				try {
					in.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			if (out != null) {
				try {
					out.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		System.out.println("读取耗时："+(System.currentTimeMillis()-beginTime)+"毫秒");
		return true;
	}

	
	public static void main(String[] args) throws IOException {
		//System.out.println(getPropertiesValueByKey("jdbc", "jdbssc.url", "sss"));
//		System.out.println(System.getProperties());
		
//		Properties pro = System.getProperties();
//		Enumeration<?> en = pro.propertyNames();
//		Set<Object> s = pro.keySet();
//		for (Object obj : s) {
//			System.out.println(obj+"="+pro.getProperty((String)obj));
//		}
		
		//test();
		//append(new File("test.txt"), 10L, "\r\n通过IO流的方式在文件中间添加文件内容\r\n");
		
		URL url = new URL("http://192.168.1.7:8888/S2SHFrame/秦时明月之万里长城-第2集.mp4");
		url = new URL("http://www.baidu.com");
		readBinaryFile(url);
	}
}
