package learn.frame.utils.file;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.util.Scanner;

/**
 * IO工具类
 * @Date 2016-10-23下午2:44:45
 */
public class IOUtil {
	
	/**
	 * 实现重定向输出流到一个文件，默认输出是屏幕，输入是键盘
	 * @Date 2016-10-23下午2:58:22
	 * @throws IOException
	 */
	public static void redirectOut() {
		try (
				PrintStream ps = new PrintStream(new FileOutputStream("out.txt")))
		{
			System.setOut(ps);//重定向输出流到一个文件
			System.out.println("重定向流到文件");
			System.out.println(new IOUtil());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	/**
	 * 实现重定向输入流为一个文件，默认输出是屏幕，输入是键盘
	 * @Date 2016-10-23下午3:02:53
	 * @throws IOException
	 */
	public static void redirectIn() throws IOException {
		try (
				FileInputStream fis = new FileInputStream("test.txt"))
		{
			System.setIn(fis);//重定向输入流为一个文件
			Scanner sc = new Scanner(System.in);
			sc.useDelimiter("\n");
			while (sc.hasNext()) {
				System.out.println(sc.next());
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void main(String[] args) throws IOException {
		System.out.println(System.getProperty("user.dir"));
		//redirectOut();
		redirectIn();
	}

}
