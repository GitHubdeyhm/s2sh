/**
 * 存放各模块的demo参考样例
 */
package learn.frame.demo;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;

import learn.frame.utils.ExcelUtil;

import org.apache.poi.EncryptedDocumentException;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.DateUtil;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.apache.poi.ss.util.CellReference;

/** 
 * 用poi实现导入导出excel数据
 * @auther huangXiaoLin.huangxl@strongit.com.cn
 * 2016-1-12 上午11:52:16
 */
public class ExcelDemo {
	
	public static void main(String[] args) {
		//createExcel();
		readExcel();
	}
	
	/**
	 * 创建excel表格，只适用扩展名为xls的excel表格
	 * @auther huangXiaoLin.huangxl@strongit.com.cn
	 * 2016-1-12 下午04:34:48
	 */
	public static boolean createExcel() {
		FileOutputStream out = null;
		try {
			long time = System.currentTimeMillis();
			out = new FileOutputStream("poi创建excel.xls");
			//创建一个工作簿
			Workbook wb = new HSSFWorkbook();
			//创建一张excel表格
			Sheet sheet = wb.createSheet("test");
			//设置默认行高，对于没有数据的行也会设置高度
			//sheet.setDefaultRowHeight((short)500);
			
			//创建行对象
			Row headRow = sheet.createRow(0);
			CellStyle headCs = ExcelUtil.getHeadStyle(wb);
			ExcelUtil.createCell(headRow, 0, headCs, "用户名");
			ExcelUtil.createCell(headRow, 1, headCs, "性别");
			ExcelUtil.createCell(headRow, 2, headCs, "手机号码");
			ExcelUtil.createCell(headRow, 3, headCs, "邮箱");
			ExcelUtil.createCell(headRow, 4, headCs, "注册时间");
			
			CellStyle bodyCs = ExcelUtil.getBodyStyle(wb);
			//一张excel表格最多只有65536行
			//不要在循环中创建字体
			for (int i = 1; i < 20; i++) {
				//创建行对象
				Row row = sheet.createRow(i);
				row.setHeightInPoints(30);//设置每行的高度
				
				ExcelUtil.createCell(row, 0, bodyCs, "用户名"+i);
				ExcelUtil.createCell(row, 1, bodyCs, "性别"+i);
				ExcelUtil.createCell(row, 2, bodyCs, "手机号码"+i);
				ExcelUtil.createCell(row, 3, bodyCs, "邮箱"+i);
				ExcelUtil.createCell(row, 4, bodyCs, "注册时间"+i);
			}
			//设置列的宽度
			for (int j = 0; j < 10; j++) {
				sheet.setColumnWidth(j, 3800);
			}
			wb.write(out);
			System.out.println("导出excel文件成功--"+(System.currentTimeMillis()-time));
			return true;
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (out != null) {
				try {
					out.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		return false;
	}

	/**
	 * 读取excel表格
	 * @auther huangXiaoLin.huangxl@strongit.com.cn
	 * 2016-1-12 下午04:40:29
	 */
	public static boolean readExcel() {
		try {
			long time = System.currentTimeMillis();
			Workbook wb = WorkbookFactory.create(new File("example.xls"));
			//得到第一张表格
			Sheet sheet = wb.getSheetAt(0);
			//得到表格的所使用的行数
			System.out.println(sheet.getFirstRowNum()+"---->"+sheet.getLastRowNum());
			//得到行
			//System.out.println(sheet.getRow(2));
			DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
			
			//得到excel文件的内容
//			ExcelExtractor extractor = new ExcelExtractor((HSSFWorkbook)wb);
//			System.out.println(extractor.getText());
			
			//可以按如下循环表格的行和列，会自动忽略空的行或列
			for (Row row : sheet) {
				for (Cell cell : row) {
					//得到某个单元格在excel表格的位置
					CellReference cellRef = new CellReference(row.getRowNum(), cell.getColumnIndex());
					String ref = cellRef.formatAsString();
					//得到列的数据类型，1：代表字符串，0代表数字，4:布尔
					switch (cell.getCellType()) {
						case Cell.CELL_TYPE_STRING:
							System.out.println(ref+"---->"+cell.getCellType()+"=="+cell.getStringCellValue());
							break;
						case Cell.CELL_TYPE_NUMERIC:
							if (DateUtil.isCellDateFormatted(cell)) {
		                        System.out.println(ref+"---->"+cell.getCellType()+"=="+df.format(cell.getDateCellValue()));
		                    } else {
		                        System.out.println(ref+"---->"+cell.getCellType()+"=="+String.valueOf(cell.getNumericCellValue()));
		                    }
							break;
						case Cell.CELL_TYPE_BOOLEAN:
							 System.out.println(ref+"---->"+cell.getCellType()+"=="+cell.getBooleanCellValue());
							 break;
						default:
							System.out.println(ref+"---->"+cell.getCellType()+"=="+"---");
							break;
					}
				}
				System.out.println("\n");
			}
			System.out.println("读取excel文件成功--"+(System.currentTimeMillis()-time));
			return true;
		} catch (EncryptedDocumentException e) {
			e.printStackTrace();
		} catch (InvalidFormatException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return false;
	}
}
