package learn.test.xml;

import java.io.ByteArrayInputStream;
import java.io.FileWriter;
import java.io.IOException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Comment;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.w3c.dom.bootstrap.DOMImplementationRegistry;
import org.w3c.dom.ls.DOMImplementationLS;
import org.w3c.dom.ls.LSOutput;
import org.w3c.dom.ls.LSSerializer;
import org.xml.sax.ErrorHandler;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;

/**
 * java解析xml文档的api称为JAXP（java api for xml），标准的解析方式有DOM和SAX两种。
 * 使用org.w3c.dom包解析文档。
 * 通过DOM文档对象模型的思想解析xml文档，文档对象模型会使解析出来的xml文档得到一个树形结构。
 * DOM解析xml文档说明：
 * 1、因DOM api会将xml文档解析成树形结构，所以效率较sax慢。
 * 2、DOM会将解析的xml文档常驻内存，适用于重复读取。
 * 3、由于DOM模型会一次性将整个xml文档全部读入内存，所以可以任意访问文档的元素，灵活性较高。 但是这样会使DOM树会占用太多的内存。
 * 4、  DOM因为会占用太多的内存，所以一般当xml文档内容较少时才使用DOM解析。
 *   典型DOM的实现使用10字节的存储器以表示XML的一个字节
 * @Date 2017-3-10下午11:08:54
 */
public class DOMTest {

	public static void main(String[] args) {
		DOMTest dom = new DOMTest();
		StringBuilder sb = new StringBuilder();
		sb.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
		sb.append("<root>");
		sb.append("<user>");
		sb.append("<realName>真实名称</realName>");
		sb.append("<age>100</age>");
		sb.append("</user>");
		sb.append("<!-- 注释节点 -->");
		sb.append("<role id=\"role_name\" name=\"角色名称\"></role>");
		sb.append("<role name=\"项目经理\"></role>");
		sb.append("<role name=\"部门经理\"></role>");
		sb.append("<role name=\"系统管理员\"></role>");
		
		sb.append("<teacher><student name=\"米 老 鼠\"></student><student name=\"加菲猫\"></student></teacher>");
		sb.append("</root>");
		
		dom.parseXML(sb.toString());
		
		//dom.parseDTDXML(sb.toString());
		
		//dom.createXML();
	}
	
	/**
	 * 通过DOM文档对象模型的思想解析xml文档
	 * @Date 2017-3-10下午11:19:28
	 * @param content xml格式的字符串
	 */
	public void parseXML(String content) {
		//默认创建的文档不会验证dtd和schema
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		//设置文档解析时忽略注释节点
		factory.setIgnoringComments(true);
		try {
			//通过DocumentBuilder对象来获取Document对象
			DocumentBuilder db = factory.newDocumentBuilder();
			//ByteArrayInputStream输入流不需要关闭，调用close()也无效
			ByteArrayInputStream byteInput = new ByteArrayInputStream(content.getBytes("UTF-8"));
			//通过Document对象操作xml文档的内容
			Document doc = db.parse(byteInput);
			
			//得到文档的根元素
			Element ele = doc.getDocumentElement();
			System.out.println(ele.getTagName());
			
			//得到user节点
			NodeList rootNode = doc.getElementsByTagName("user");
			//如果设置了忽略注释节点，则下一个节点就是role节点，否则就是注释节点
			Node nextNode = rootNode.item(0).getNextSibling();
			//注释节点的话就打印节点内容
			if (nextNode.getNodeType() == Node.COMMENT_NODE) {
				System.out.println(nextNode.getNodeValue());
			}
			
			//通过标签名获取元素，字符“*”代表获取文档的所有元素
			NodeList list = doc.getElementsByTagName("teacher");
			int len = list.getLength();
			System.out.println("元素的个数："+len);
			for (int i = 0; i < len; i++) {
				//获取元素的第一个子节点
				Node firstNode = list.item(i).getFirstChild();
				//获取节点的所有属性
				NamedNodeMap attrs = firstNode.getAttributes();
				//得到指定的属性节点
				Node attrNode = attrs.getNamedItem("name");
				
				System.out.println("第一个子节点："+attrNode.getNodeName()+"="+attrNode.getNodeValue());
			}
			
			//直接写一个id属性无法获取元素
			Element ele2 = doc.getElementById("role_name");
			System.out.println(ele2);//null
			
			//获取元素的第一个子节点
			Node node = doc.getFirstChild();
			System.out.println("元素的第一个子节点："+node.getNodeName());
		} catch (ParserConfigurationException e) {
			e.printStackTrace();
		} catch (SAXException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 解析dtd验证的xml文档
	 * @Date 2017-7-10下午9:00:36
	 * @param content
	 */
	public void parseDTDXML(String content) {
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		//设置文档解析时忽略注释节点
		factory.setIgnoringComments(true);
		//设置进行dtd验证，不会对schema验证。此时需要保证xml文档有dtd声明
		factory.setValidating(true);
		try {
			//通过DocumentBuilder对象来获取Document对象
			DocumentBuilder db = factory.newDocumentBuilder();
			//当设置了dtd验证时，需要注册一个错误处理器
			db.setErrorHandler(new ErrorHandler() {
				@Override
				//警告
				public void warning(SAXParseException exception) throws SAXException {
					System.out.println("-----warning----");
				}
				
				@Override
				//不可恢复的错误
				public void fatalError(SAXParseException exception) throws SAXException {
					System.out.println("-----fatalError----");
					throw exception;
				}
				
				@Override
				//可以恢复的错误
				public void error(SAXParseException exception) throws SAXException {
					System.out.println("-----error----");
					throw exception;
				}
			});
			
			//ByteArrayInputStream输入流不需要关闭，调用close()也无效
			ByteArrayInputStream byteInput = new ByteArrayInputStream(content.getBytes("UTF-8"));
			//通过Document对象操作xml文档的内容
			db.parse(byteInput);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 使用DOM创建xml文档
	 * @Date 2017-7-10下午9:18:47
	 * @param content
	 */
	public void createXML() {
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		try {
			//通过DocumentBuilder对象来获取Document对象
			DocumentBuilder db = factory.newDocumentBuilder();
			//创建一个DOM树
			Document doc = db.newDocument();
			doc.setXmlVersion("1.0");//xml版本
			
			Comment comment = doc.createComment("这是通过DOM创建的xml文档注释");
			doc.appendChild(comment);
			//根节点
			Element root = doc.createElement("root");
			doc.appendChild(root);
			
			Element users = doc.createElement("users");
			root.appendChild(users);
			
			Element mls = doc.createElement("user");
			mls.setAttribute("性别", "男");
			mls.setAttribute("年龄", "88");
			Element jfm = doc.createElement("user");
			jfm.setAttribute("性别", "女");
			jfm.setAttribute("年龄", "82");
			users.appendChild(mls);
			users.appendChild(jfm);
			
			Element role = doc.createElement("role");
			root.appendChild(role);
			
			Element department = doc.createElement("department");
			department.appendChild(doc.createTextNode("这是一段文本"));
			root.appendChild(department);
			
			
			//以下设置格式化输入xml文档
			DOMImplementationRegistry registry = DOMImplementationRegistry.newInstance();
			DOMImplementationLS domImpl = (DOMImplementationLS)registry.getDOMImplementation("LS");
			//LSSerializer 提供了将 DOM 文档序列化（编写）为 XML 的 API
			LSSerializer serial = domImpl.createLSSerializer();
			serial.getDomConfig().setParameter("format-pretty-print", true);
			LSOutput out = domImpl.createLSOutput();
			out.setEncoding("UTF-8");
			out.setCharacterStream(new FileWriter("dom-create.xml"));
			serial.write(doc, out);
			System.out.println("创建文档成功！"+System.getProperty("user.dir"));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
