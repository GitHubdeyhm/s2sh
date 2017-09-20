package learn.frame.utils;

import java.util.Random;

/** 
 * 颜色相关工具类，常用于生成图表时的颜色值
 * @Date 2016-12-1 上午11:27:42
 */
public class ColorUtil {
	/**十六进制字符数组*/
	public static final char[] HEX_CHARS = {'0', '1', '2', '3', '4', '5', '6', '7', 
			'8', '9', 'a', 'b', 'c', 'd', 'e', 'f'};
	/**
	 * 常用15中颜色值，当图表需要的颜色值超出15种时随机生成颜色值绘制图表
	 */
	public static final String[] COLORS = {"#7cb5ec", "#434348", "#90ed7d", "#f7a35c", "#8085e9", 
	        "#f15c80", "#e4d354",  "#2f7ed8",  "#8bbc21", "#910000", "#1aadce", "#492970",  "#77a1e5", "#c42525", "#a6c96a"};
	
	/**
	 * 得到随机颜色的十六进制值。可以供图标插件展示的颜色值使用
	 * @auther huangXiaoLin.huangxl@strongit.com.cn
	 * 2016-12-1上午11:39:21
	 * @return
	 */
	public static String getRandColorValue() {
		StringBuilder color = new StringBuilder("#");
		//随机生成颜色
		Random rand = new Random();
		int count = 0;
		//十六进制只需循环6次
		while (count < 6) {
			color.append(HEX_CHARS[rand.nextInt(16)]);
			count++;
		}
    	return color.toString();
	}
	
	public static void main(String[] args) {
		int num = 0;
		System.out.println(Integer.toHexString(255));
		while (num < 20) {
			int count = 10;
			long t1 = System.currentTimeMillis();
			for (int i = 0; i < count; i++) {
				System.out.println(getRandColorValue());
			}
			System.out.println(System.currentTimeMillis()-t1);
			num++;
		}
	}
}

