package learn.test.xml;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.Date;

import javax.xml.transform.OutputKeys;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactoryConfigurationError;
import javax.xml.transform.sax.SAXSource;
import javax.xml.transform.sax.SAXTransformerFactory;
import javax.xml.transform.stream.StreamResult;

import learn.frame.vo.DemoVo;
import learn.frame.vo.UserVo;

import org.xml.sax.InputSource;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;
import com.thoughtworks.xstream.annotations.XStreamOmitField;
import com.thoughtworks.xstream.io.xml.StaxDriver;

/**
 * DOM、sax、stax技术关注的是xml文档结构方面，但是对于一些应用程序需要关注xml文件本身的数据。
 * 于是出现了oxm（对象xml关系映射）技术。常见的oxm技术有xstream、jaxb、xmlbeans。
 * 使用xstream.jar包测试类，实现java对象转换为xml文件或者xml文件转为java对象
 * xstream.jar包java对象转化为xml文件说明：
 * 1、xml文件的根元素为类的全限定类名
 * 2、转换为xml文件的元素节点的名称为对象的字段名称，且都是元素节点，没有属性节点。
 * 3、可以通过注解自定义元素节点名称，包括设置为属性节点
 * 4、对象里的null值字段不会映射为xml文件的一个元素节点
 * @Date 2017-3-13 上午11:28:01
 */
public class XStreamTest {

	public static void main(String[] args) {
		XStreamTest test = new XStreamTest();
		/**
		 * 创建XStream对象并指定xml解析器。StaxDriver类代表使用stax流的方式解析xml
		 * 如果没有指定解析器则默认采用XPP解析器，需要引入相应的jar包
		 */
		XStream xst = new XStream(new StaxDriver());
		//注册一个类型转换器
		xst.registerConverter(new XStreamDateConveter());
		//将对象格式为xml字符串，对于对象里的null值字段不会映射为xml的一个元素
		String xml = xst.toXML(test.getDemo());
		System.out.println(formateXML(xml));
		try {
			//将对象格式化输出到指定文件
			xst.toXML(test.getDemo(), new FileWriter("XStream.xml"));
			
			Object obj = xst.fromXML(xml);
			System.out.println(obj);
			
			/**
			 * 设置以注解的方式解析类，常用注解说明
			 * XStreamAlias 取别名
			 * XStreamAsAttribute 标示为属性节点
			 * XStreamOmitField 转为xml文件时忽略该字段
			 * XStreamConverter 注入转换器
			 */
			xst.autodetectAnnotations(true);
			System.out.println(xst.toXML(new XStreamTestInner("名称", 12, new Date())));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public DemoVo getDemo() {
		DemoVo demo = new DemoVo();
		demo.setAge(15);
		demo.setCreateTime(new Date());
		demo.setDoubleNum(78.02);
		demo.setFloatNum(100.39f);
		demo.setPrice(new BigDecimal("199.99"));
		UserVo user = new UserVo();
		user.setRealName("姓名");
		demo.setUser(user);
		return demo;
	}
	
	/**
	 * 格式化xml字符串输出，每个元素换行并且有两个空白字符缩进
	 * @Date 2017-3-14下午9:23:45
	 * @param xml xml格式的字符串
	 * @return
	 */
	public static String formateXML(String xml) {
		try {
			//Transformer抽象类的实例能够将源树转换为结果树，实现格式化输出。非线程安全
			Transformer trans = SAXTransformerFactory.newInstance().newTransformer();
			trans.setOutputProperty(OutputKeys.INDENT, "yes");//设置空白字符缩进
			trans.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "2");
			//要转换的xml格式的字符串
			Source xmlSource = new SAXSource(new InputSource(new ByteArrayInputStream(xml.getBytes())));
			StreamResult res = new StreamResult(new ByteArrayOutputStream());
			//调用transform()方法转换后的结果保留到Result对象里
			trans.transform(xmlSource, res);
			ByteArrayOutputStream byteArray = (ByteArrayOutputStream)res.getOutputStream();
			return new String(byteArray.toByteArray());
		} catch (TransformerFactoryConfigurationError e) {
			e.printStackTrace();
		} catch (TransformerException e) {
			e.printStackTrace();
		}
		return null;
	}
}

@XStreamAlias("XStream")
class XStreamTestInner {
	
	private String id;
	@XStreamAlias("rn")//通过注解定义映射为xml文件的元素名
	private String realName;
	@XStreamAsAttribute//定义该字段作为xml元素的属性解析
	private Integer age;
	private Date createTime;
	private Float floatNum;
	@XStreamOmitField//该注解代表转换为xml时忽略该字段，即该字段不会生成xml元素
	private Double doubleNum;
	private BigDecimal price;
	
	private UserVo user;
	
	
	public XStreamTestInner(String realName, Integer age, Date createTime) {
		this.realName = realName;
		this.age = age;
		this.createTime = createTime;
	}
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getRealName() {
		return realName;
	}
	public void setRealName(String realName) {
		this.realName = realName;
	}
	public Integer getAge() {
		return age;
	}
	public void setAge(Integer age) {
		this.age = age;
	}
	public Date getCreateTime() {
		return createTime;
	}
	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}
	public Float getFloatNum() {
		return floatNum;
	}
	public void setFloatNum(Float floatNum) {
		this.floatNum = floatNum;
	}
	public Double getDoubleNum() {
		return doubleNum;
	}
	public void setDoubleNum(Double doubleNum) {
		this.doubleNum = doubleNum;
	}
	public BigDecimal getPrice() {
		return price;
	}
	public void setPrice(BigDecimal price) {
		this.price = price;
	}
	public UserVo getUser() {
		return user;
	}
	public void setUser(UserVo user) {
		this.user = user;
	}
}

