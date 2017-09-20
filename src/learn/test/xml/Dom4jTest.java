package learn.test.xml;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Iterator;
import java.util.List;

import org.dom4j.Attribute;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.dom4j.Node;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.SAXReader;
import org.dom4j.io.XMLWriter;

/**
 * 使用dom4j.jar包解析xml文档。
 * dom4j类似与DOM，也会将xml文档解析成一个DOM树，但处理方式比DOM更简单。
 * 1、dom4j是面向接口编程
 * @Date 2017-3-10下午11:10:46
 */
public class Dom4jTest {	
	
	public static void main(String[] args) throws DocumentException {
		Dom4jTest test = new Dom4jTest();
		String path = "src/learn/test/xml/demo.xml";
		
		//test.parseXML(path);
		
		test.parseXMLByXPath(path);
			
		//test.parseBigXML(path);
			
		//test.createDocument();
			
			
		//根据一个xml格式的字符串解析为xml文档保存
		StringBuilder sb = new StringBuilder();
		sb.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
		sb.append("<root>");
		sb.append("<user><realName>真实名称</realName>");
		sb.append("<age>100</age></user>");
		sb.append("<role id=\"role_name\" name=\"角色名称\"></role>");
		sb.append("<role name=\"项目经理\"></role>");
		sb.append("<role name=\"部门经理\"></role>");
		sb.append("<role name=\"系统管理员\"></role>");
		sb.append("<teacher><student name=\"米老鼠\"></student><student name=\"加菲猫\"></student></teacher>");
		sb.append("</root>");
		test.formatWrite(sb.toString());
	}
	
	/**
	 * 使用dom4j解析xml文档
	 * @throws DocumentException 
	 * @Date 2017-7-16下午9:47:56
	 */
	void parseXML(String path) throws DocumentException {
		//通过SAXReader类创建文档
		SAXReader sax = new SAXReader();
		//得到文档对象
		Document doc = sax.read(new File(path));
		//将一个xml文档转换为字符串输出
		//System.out.println(doc.asXML());
		
		//得到文档的根元素
		Element root = doc.getRootElement();
		
		//遍历根元素下的所有第一级子元素节点
		System.out.println("遍历元素下的所有第一级子元素节点");
		for (Iterator<?> it = root.elementIterator(); it.hasNext();) {
			Element ele = (Element)it.next();
			System.out.println(ele.getName());
		}
		//遍历根元素下指定元素名称的第一级子元素节点
		System.out.println("遍历根元素下指定元素名称的第一级子元素节点");
		for (Iterator<?> it = root.elementIterator("role"); it.hasNext();) {
			Element ele = (Element)it.next();
			System.out.println(ele.getName());
		}
		//遍历一个元素的所有属性节点
		for (Iterator<?> it = root.attributeIterator(); it.hasNext();) {
			Attribute attr = (Attribute)it.next();
            System.out.println(root.getName()+"【"+attr.getName()+"="+attr.getValue()+"】");
        }
	}
	
	void parseXMLByXPath(String path) throws DocumentException {
		//通过SAXReader类创建文档
		SAXReader sax = new SAXReader();
		//得到文档对象
		Document doc = sax.read(new File(path));
		//将一个xml文档转换为字符串输出
		//System.out.println(doc.asXML());
		
//		List<?> nodeList = doc.selectNodes("//age");
//		for (int i = 0; i < nodeList.size(); i++) {
//			Node node = (Node)nodeList.get(i);
//			System.out.println(node.getNodeTypeName()+"----"+node.getName());
//		}
//		for (Iterator<?> iter = nodeList.iterator(); iter.hasNext(); ) {
//            Attribute attribute = (Attribute) iter.next();
//            String url = attribute.getValue();
//        }
	}
	
	/**
	 * 遍历大型的xml文档，使用以下方法性能较高
	 * @Date 2017-3-11下午12:00:28
	 * @param ele
	 * @throws DocumentException 
	 */
	public void parseBigXML(String path) throws DocumentException {
		//通过SAXReader类创建文档
		SAXReader sax = new SAXReader();
		//得到文档对象
		Document doc = sax.read(new File(path));
		
		//得到文档的根元素
		Element ele = doc.getRootElement();
		System.out.println("-------遍历大型xml文档-------");
		int count = ele.nodeCount();
		System.out.println("节点数量："+count);
		for (int i = 0; i < count; i++) {
			Node node = ele.node(i);
			System.out.println("节点类型："+node.getNodeTypeName()+"----节点名称："+node.getName());
			//元素节点
			if (node instanceof Element) {
				System.out.println("----元素节点名称："+node.getName());
			}
		}
	}
	
	/**
	 * 创建xml文档，使用Document接口的write方法写出到一个xml文件。
	 * write()方法输出的xml文档没有良好的格式，所有元素都在一行。
	 * @Date 2017-3-11下午12:11:16
	 * @return
	 */
	public Document createDocument() {
		System.out.println("-----创建文档----");
		Document doc = DocumentHelper.createDocument();
		Element root = doc.addElement("root");
		root.addElement("user").addAttribute("userName", "用户名").addText("content");
		Element ele = root.addElement("role").addAttribute("roleName", "角色名称");
		root.addElement("role").addAttribute("roleName", "角色名称2");
		ele.addElement("test").addText("内容节点");
		FileWriter out = null;
		try {
			out = new FileWriter("test.xml");
			doc.write(out);//不具备良好的格式
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
		return doc;
	}
	
	/**
	 * 根据一个xml格式的字符串解析为xml文档保存，保存为xml文档时有良好的格式
	 * @Date 2017-3-11下午10:43:08
	 * @param xml xml格式的字符串
	 */
	public void formatWrite(String xml) {
		XMLWriter out = null;
		try {
			//解析一个xml格式的字符串为xml文档对象
			Document doc = DocumentHelper.parseText(xml);
			/**
			 * OutputFormat.createPrettyPrint()方法设置输出xml文档的格式，格式说明如下：
			 * 1、每个元素换行显示
			 * 2、包含2个空白字符的缩进
			 */
			OutputFormat formate = OutputFormat.createPrettyPrint();
			out = new XMLWriter(new FileWriter("XMLWriter.xml"), formate);
			out.write(doc);//通过XMLWriter类实现格式化输出
		} catch (IOException | DocumentException e) {
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
	}

}
