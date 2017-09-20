package learn.test.xml;

import java.io.File;
import java.io.IOException;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;
import org.xml.sax.helpers.DefaultHandler;

/**
 * 使用org.xml.sax和javax.xml.parsers包解析xml文档。
 * sax解析器的思想是通过事件的机制来实现解析文档。
 * 说明：
 * 1、sax解析器会将xml文档从上到下线性读取，构成一个结构完整的XML文档的标记。sax方式不需要将整个xml文档一次性读入，
 *   效率相对dom来说较高。
 * 2、sax解析器可以用来处理非常大的xml文档，而使用dom解析时会占用大量的内存。
 * 3、使用sax解析的xml的文档不应该是深层次的嵌套。
 * 4、sax解析器解析的数据是可用的，这样的sax可以很好地用于到达流的XML文档
 * 5、但是使用sax解析器缺少灵活性，于是出现了stax
 * 6、sax通常用于解析节点，而不能新增或修改节点。
 * @Date 2017-3-10下午11:30:48
 */
public class SAXTest {

	public static void main(String[] args) {
		SAXTest sax = new SAXTest();
		sax.parse();
	}
	
	/**
	 * 使用sax的方式解析xml文档
	 * @Date 2017-7-15下午10:10:00
	 */
	public void parse() {
		SAXParserFactory factory = SAXParserFactory.newInstance();
		//设置DTD验证。
		factory.setValidating(true);
		try {
			SAXParser sax = factory.newSAXParser();
			sax.parse(new File("src/learn/test/xml/demo.xml"), new xmlHander());
		} catch (ParserConfigurationException e) {
			e.printStackTrace();
		} catch (SAXException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
/**
 * 通过继承DefaultHandler类实现事件处理
 * @Date 2017-3-10下午11:40:34
 */
class xmlHander extends DefaultHandler {
	
	@Override
	public void startDocument() throws SAXException {
		System.out.println("-----开始解析文档-----");
	}
	
	/**
	 * 解析每个元素节点开始的时候调用该方法，包括子元素
	 * @param qName 元素名称
	 * @param attributes 属性对象，获取元素属性的值
	 */
	@Override
	public void startElement(String uri, String localName, String qName,
			Attributes attributes) throws SAXException {
		//System.out.println(uri+"---"+localName+"---"+qName+"---"+attributes);
		/*if (qName.equalsIgnoreCase("role")) {
			System.out.println(attributes.getValue("name"));//获取属性值
		}*/
		System.out.println("-------调用了startElement()-----"+qName);
	}
	
	@Override
	public void endElement(String uri, String localName, String qName)
			throws SAXException {
		System.out.println("------调用了endElement()----"+qName);
	}
	
	@Override
	//处理文本数据时触发
	public void characters(char[] ch, int start, int length)
			throws SAXException {
		System.out.println("------调用了characters()-------");
	}
	
	@Override
	public void processingInstruction(String target, String data)
			throws SAXException {
		System.out.println(data);
	}
	
	@Override
	public void endDocument() throws SAXException {
		System.out.println("-----解析文档结束-----");
	}
	
	@Override
	public void error(SAXParseException e) throws SAXException {
		System.out.println("解析xml文档错误"+e);
	}
}