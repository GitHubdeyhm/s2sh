package learn.test.template;

import java.io.File;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import learn.frame.utils.DateUtil;
import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;

/**
 * Freemarker模板技术测试类
 * @Date 2017-3-28下午1:54:27
 */
public class FreemarkerTest {
	
	public static void main(String[] args) {
		Configuration cfg = new Configuration();
		try {
			System.out.println(System.getProperty("user.dir"));
			//设置加载模板文件的目录
			cfg.setDirectoryForTemplateLoading(new File("src/learn/test/template"));
			
			//对于web项目加载freemarker文件的方法
			//cfg.setServletContextForTemplateLoading(request.getServletContext(), "/WEB-INF/template");
			
			//设置数字格式化，四舍五入
			cfg.setNumberFormat("0.##");
			cfg.setDateFormat(DateUtil.DATE_PATTERN);
			//Configuration对象会缓存Template实例，多次调用getTemplate()方法返回同一个Template对象
			Template temp = cfg.getTemplate("demo.ftl");
			Template temp2 = cfg.getTemplate("demo.ftl");
			System.out.println(temp == temp2);//true
			
			Writer out = new OutputStreamWriter(System.out);
			Map<String, Object> dataMap = new HashMap<String, Object>();
			dataMap.put("id", "abcde");
			dataMap.put("num", 123456.345678);
			dataMap.put("date", new Date());
			dataMap.put("escapeStr", "大量的特殊字符：\\\\<><>{}\"\'");
			dataMap.put("command", "<#if str == \"这是一个包含if指令的字符串\"><span>是</span><#else><span>否</span></#if>");
			temp.process(dataMap, out);
		} catch (IOException | TemplateException e) {
			e.printStackTrace();
		}
	}

}
