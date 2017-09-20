package learn.frame.utils.file;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import learn.frame.utils.StringUtil;

/**
 * 文件工具类，封装对文件的常用操作，包括对属性文件的操作
 * @Date 2016-7-3 下午10:04:02
 */
public class PropertiesUtil {
	
	private static final String PROPERTIES_FILE_suffix = ".properties";

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
			InputStream in = PropertiesUtil.class.getResourceAsStream("/"+fileName+PROPERTIES_FILE_suffix);
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
	 * 向属性文件存储值
	 * @Date 2016-12-3下午10:20:34
	 * @param fileName 文件名称
	 * @param key 键
	 * @param value 值
	 */
	public static void storePropertiesValue(String fileName, String key, String value) {
		Properties prop = loadPropertiesFile(fileName);
		if (prop != null) {
			prop.setProperty(key, value);
			FileOutputStream fos = null;
			try {
				fos = new FileOutputStream("/"+fileName+PROPERTIES_FILE_suffix);
				prop.store(fos, "");
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			} finally {
				if (fos != null) {
					try {
						fos.close();
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			}
		}
	}
	
	public static void main(String[] args) {
		System.out.println(getPropertiesValueByKey("jdbc", "jdbssc.url", "sss"));
		
		storePropertiesValue("system", "key", "value");
		
	}
}
