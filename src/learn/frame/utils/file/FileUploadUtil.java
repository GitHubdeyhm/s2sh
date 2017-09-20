package learn.frame.utils.file;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;

import learn.frame.common.MessageConstant;
import learn.frame.exception.ServiceException;
import learn.frame.utils.StringUtil;

import org.apache.struts2.ServletActionContext;

/** 
 * 基于struts2的文件上传工具类
 * @Date 2016-7-27 下午05:10:08
 */
public class FileUploadUtil {
	
	/**上传图片允许的格式*/
	public static final String[] IMAGE_ALLOW_EXT = {"jpg", "jpeg", "png", "gif"};
	/**允许上传图片的最大值*/
	public static final long IMAGE_ALLOW_MAXSIZE = 10485760L;//10M=1024*1024*10
	/**图片格式错误的提示信息*/
	public static final String IMAGE_EXTNAME_MSG = "只允许上传jpg、jpeg、png、gif格式的图片！";
	/**上传图片文件的最大值*/
	public static final String IMAGE_MAX_SIZE_MSG = "最大允许上传10M的图片文件！";
	
	/**
	 * 判断上传的图片文件是否属于允许的图片格式，可以同时验证多个图片格式
	 * @Date 2016-7-27下午05:13:46
	 * @param extNames 文件扩展名数组
	 * @return 返回true代表所有图片格式都验证通过，返回false代表至少有一个图片格式验证不通过
	 */
	public static boolean isAllowImageExtName(String... extNames) {
		for (String extName : extNames) {
			boolean isAllow = false;
			for (String ext : IMAGE_ALLOW_EXT) {
				//忽略大小写与扩展名相等
				if (ext.equalsIgnoreCase(extName)) {
					isAllow = true;
					break;
				}
			}
			if (!isAllow) {
				return false;
			}
		}
		return true;
	}
	
	/**
	 * 判断上传的图片文件大小是否超过了最大值，可以同时验证多个图片文件
	 * @Date 2016-8-20下午5:26:12
	 * @param files 文件数组
	 * @return 返回true代表所有图片都没有超过最大值，返回false代表至少有一个图片大小超过了最大值
	 */
	public static boolean isAllowImageSize(File... files) {
		for (File f : files) {
			long size = (f == null) ? 0L : f.length();
			if (size > IMAGE_ALLOW_MAXSIZE) {
				return false;
			}
		}
		return true;
	}
	
	/**
	 * 设置上传图片文件保存在服务器的路径：根路径下的image文件夹
	 * @Date 2016-7-27下午05:35:31
	 * @param savePath 文件路径，基于根路径下的image文件夹
	 * @return
	 */
	public static String getSavePath(String savePath) {
		//路径名为空代表图片存放在image路径下
		if (StringUtil.isNullOrEmpty(savePath)) {
			savePath = "";
		}
		String serverPath = ServletActionContext.getServletContext().getRealPath("/");
		//文件上传路径不存在就创建文件夹
		File path = new File(serverPath+"/upload/image/"+savePath);
		if (!path.exists()) {
			path.mkdirs();
		}	
		return path.toString();
	}
	
	/**
	 * 保存文件到服务器路径下
	 * @Date 2016-8-3下午02:14:59
	 * @param file 文件对象
	 * @param pathName 文件夹路径，基于根路径下的image文件夹
	 * @param fileName 文件名称，包括扩展名
	 * @return
	 */
	public static boolean saveFile(File file, String pathName, String fileName) {
		boolean isSave = false;
		if ((!isEmpty(file)) && (!StringUtil.isNullOrEmpty(fileName))) {			
			//随机生成的uuid文件名称作为保存在服务器端的文件名
//			String rename = UUID.randomUUID() + fileName.substring(fileName.lastIndexOf("."));
			FileInputStream fis = null;
			FileOutputStream fos = null;
			try {
				fis = new FileInputStream(file);
				fos = new FileOutputStream(getSavePath(pathName)+"/"+fileName);
				byte[] buffer = new byte[1024];
				int len = 0;
				while ((len = fis.read(buffer)) != -1) {
					fos.write(buffer, 0, len);
				}
				isSave = true;
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				try {
					if (fis != null) {
						fis.close();
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
				try {
					if (fos != null) {
						fos.close();
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
		return isSave;
	}
	/**
	 * 保存文件到服务器路径下，放在根路径下的image文件夹
	 * @Date 2016-7-27下午06:12:15
	 * @param file 文件对象
	 * @param fileName 文件名称，包括扩展名
	 * @return
	 */
	public static boolean saveFile(File file, String fileName) {
		return saveFile(file, null, fileName);
	}
	
	/**
	 * 判断一个文件对象是否为空，当文件对象为null或者文件大小小于等于0返回true，否则返回false
	 * @Date 2016-7-28上午11:20:22
	 * @param file 文件对象
	 * @return 当文件对象为null或者文件大小小于等于0返回true，否则返回false
	 */
	public static boolean isEmpty(File file) {
		if (file == null || file.length() <= 0) {
			return true;
		}
		return false;
	}
	
	/**
	 * 导入excel表格数据的时候验证上传文件
	 * @Date 2016-12-17下午11:11:40
	 * @return
	 * @throws IOException
	 */
	public static boolean validateExcelFile(File file, String fileName) throws ServiceException {
		if (isEmpty(file)) {
			throw new ServiceException(MessageConstant.FILE_EMPTY_MSG);
		}
		String extName = fileName.substring(fileName.lastIndexOf(".")+1);
		if (!"xls".equals(extName) && !"xlsx".equals(extName)) {
			throw new ServiceException(MessageConstant.IMPORT_EXCEL_EXTNAME_MSG);
		}
		return true;
	}
	
	public static void main(String[] args) {
		System.out.println(getSavePath(""));
	}
}
