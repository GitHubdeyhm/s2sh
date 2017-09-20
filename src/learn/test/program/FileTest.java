package learn.test.program;

import java.io.File;

/**
 * @Date 2017-4-22下午11:29:00
 */
public class FileTest {
	public static void main(String[] args) {
		System.out.println(File.separator);
		File dir = new File("F:\\learn");
		File[] files = dir.listFiles();
		for (File f : files) {
			System.out.println(f.getName());
		}
	}

}
