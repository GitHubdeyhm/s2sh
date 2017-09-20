package learn.frame.utils;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Workbook;

/** 
 * 导出Excel工具类，使用poi工具实现
 * 实现统一导出Excel的字体、大小、边框等样式
 * @Date 2016-1-12 下午04:13:12
 */
public class ExcelUtil {
	
	/**导出Excel的字体名称*/
	public static final String FONT_NAME = "宋体";
	/**Excel文件的MIME类型*/
	public static final String EXCEL_MIME_TYPE = "application/vnd.ms-excel";
	
	/**
	 * 得到表格头部样式，内容水平和垂直方向居中
	 * <p>便于项目中统一导出Excel文件的字体、大小等样式</p>
	 * @Date 2016-1-12 下午04:14:30
	 */
	public static CellStyle getHeadStyle(Workbook wb) {
		//表头字体样式--不要在循环中创建字体
		Font headF = wb.createFont();
		headF.setFontName("宋体");//字体
		//headF.setItalic(true);//倾斜字体
		//headF.setStrikeout(true);//删除线
		headF.setColor(IndexedColors.GREEN.getIndex());//字体颜色
		headF.setFontHeightInPoints((short)14);//字体大小
		headF.setBoldweight(Font.BOLDWEIGHT_BOLD);//粗体
		CellStyle headCs = getCellStyle(wb, headF, CellStyle.BORDER_THIN);
		return headCs;
	}
	
	/**
	 * 得到表格主体部分样式，内容水平和垂直方向居中
	 * <p>便于项目中统一导出Excel文件的字体、大小等样式</p>
	 * @Date 2016-1-12 下午04:14:30
	 */
	public static CellStyle getBodyStyle(Workbook wb) {
		//表头字体样式--不要在循环中创建字体
		Font bodyF = wb.createFont();
		bodyF.setFontName("宋体");//字体
		bodyF.setFontHeightInPoints((short)12);//字体大小
		CellStyle bodyCs = getCellStyle(wb, bodyF, CellStyle.BORDER_THIN);
		return bodyCs;
	}
	
	/**
	 * 得到列的样式
	 * @Date 2016-1-12 下午05:48:18
	 * @param wb 工作簿对象
	 * @param font 字体对象
	 * @param halign 水平对齐方式
	 * @param valign 垂直对齐方式
	 * @param border 边框线
	 */
	public static CellStyle getCellStyle(Workbook wb, Font font, short halign, short valign, short border) {
		//设置列的样式
		CellStyle cs = wb.createCellStyle();
		cs.setFont(font);
		cs.setAlignment(halign);//水平位置
		cs.setVerticalAlignment(valign);//垂直位置
		cs.setWrapText(true);//自动换行
		//设置单元格的边框线
		cs.setBorderTop(border);
		cs.setBorderRight(border);
		cs.setBorderBottom(border);
		cs.setBorderLeft(border);
		return cs;
	}
	
	/**
	 * 得到列的样式，单元格的内容对齐方式是水平和垂直方向居中
	 * @Date 2016-1-12 下午05:48:18
	 * @param wb 工作簿对象
	 * @param font 字体对象
	 * @param border 边框线
	 */
	public static CellStyle getCellStyle(Workbook wb, Font font, short border) {
		//设置列的样式
		CellStyle cs = wb.createCellStyle();
		cs.setFont(font);
		cs.setAlignment(CellStyle.ALIGN_CENTER);//水平居中
		cs.setVerticalAlignment(CellStyle.VERTICAL_CENTER);//垂直居中
		cs.setWrapText(true);//自动换行
		//设置单元格的背景色
		//cs.setFillForegroundColor(IndexedColors.ORANGE.getIndex());
		//cs.setFillPattern(CellStyle.SOLID_FOREGROUND);
		//设置单元格的边框线
		cs.setBorderTop(border);
		cs.setBorderRight(border);
		cs.setBorderBottom(border);
		cs.setBorderLeft(border);
		return cs;
	}
	
	/**
	 * 创建表格列
	 * @Date 2016-1-12 下午06:09:02
	 * @param row 行对象
	 * @param column 列索引，从0开始，不能超出short类型的范围，此处写成int类型是为了便于调用
	 * @param cs 列样式
	 * @param content 单元格的内容
	 */
	public static void createCell(Row row, int column, CellStyle cs, String content) {
        //创建列
		Cell cell = row.createCell((short)column);
        cell.setCellValue(content);
      //headRow.createCell(0).setCellValue("用户名")//支持链式写法
        cell.setCellStyle(cs);
    }	
	
	/**
	 * 得到某列值
	 * @Date 2016-12-18下午1:45:29
	 * @param cell 列对象
	 * @return 值
	 */
	public static String getCellValue(Cell cell) {
		String value = null;
		//当excel输入空格时列可能为null
		if (cell != null) {
			System.out.println(cell.getCellType()+"--直接列："+cell.toString());
			switch (cell.getCellType()) {
				case Cell.CELL_TYPE_STRING:
					value = cell.getStringCellValue();
					break;
				case Cell.CELL_TYPE_NUMERIC:
					value = cell.getNumericCellValue()+"";//只要是纯数字就会被视为数字类型
					break;
				case Cell.CELL_TYPE_BLANK:
					value = "";
					break;
				case Cell.CELL_TYPE_BOOLEAN:
					value = String.valueOf(cell.getBooleanCellValue());
					break;
				default:
					break;
			}
		}
		System.out.println("列值："+value);
		return value;
	}
}
