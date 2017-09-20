package learn.frame.common;

/**
 * 用于响应页面的提示信息，保存常用的提示信息
 * @Date 2016-2-5 下午8:56:43
 */
public class MessageConstant {
	/**出现未知错误的提示信息*/
	public static final String UNKNOWN_ERROR = "unknown_error";
	/**数据库读写错误的提示信息*/
	public static final String DB_IO_ERROR = "db_io_error";	
	/**数据库查询错误的提示信息*/
	public static final String DB_QUERY_ERROR = "db_query_error";
	
	/** 导出excel数据的数据量过多的提示信息*/
	public static final String EXPORT_EXCEL_MUCH_MSG = "export_excel_much_msg";
	/** 导出excel数据的数据量没有数据时提示信息*/
	public static final String EXPORT_EXCEL_EMPTY_MSG = "export_excel_empty_msg";
	/**导入excel表格数据的格式错误提示信息*/
	public static final String IMPORT_EXCEL_EXTNAME_MSG = "import_excel_extname_msg";
	
	/**图片格式错误的提示信息*/
	public static final String IMAGE_EXTNAME_MSG = "image_extname_msg";
	/**上传图片文件的最大值*/
	public static final String IMAGE_MAXSIZE_MSG = "image_maxsize_msg";
	/**上传文件名称不能太长*/
	public static final String FILENAME_LENGTH_MSG = "filename_length_msg";
	/**上传文件不能为空*/
	public static final String FILE_EMPTY_MSG = "file_empty_msg";
	/**文件不存在*/
	public static final String FILE_NOT_EXIST_MSG ="file_not_exist_msg";
	
	

}
