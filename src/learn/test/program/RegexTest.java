package learn.test.program;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * java中使用正则表达式
 * @Date 2017-6-7下午11:11:21
 */
public class RegexTest {
	
	public static void main(String[] args) {
		Pattern p = Pattern.compile("a*b");
		Matcher m = p.matcher("aaab");
		System.out.println(m.matches());//true
		System.out.println(m.find());
		
		System.out.println(m.start());
		
		String str = "**ab*c****";
		System.out.println(str.replaceAll("^\\*+|\\*+$", ""));
		
		spliteWord("java is very good");
	}

	static void spliteWord(String words) {
		Matcher m = Pattern.compile("\\w+").matcher(words);
		while (m.find()) {
			System.out.println(m.start()+"----"+m.end()+"----"+m.group());//得到匹配的字符串
		}
	}
}
