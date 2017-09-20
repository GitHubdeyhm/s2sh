package learn.test.xml;

import java.util.Date;

import learn.frame.utils.DateUtil;

import com.thoughtworks.xstream.converters.Converter;
import com.thoughtworks.xstream.converters.MarshallingContext;
import com.thoughtworks.xstream.converters.UnmarshallingContext;
import com.thoughtworks.xstream.io.HierarchicalStreamReader;
import com.thoughtworks.xstream.io.HierarchicalStreamWriter;

/** 
 * XStream.jar包类型转换器，需实现Converter接口
 * @auther huangXiaoLin.huangxl@strongit.com.cn
 * 2017-3-14 下午05:35:22
 */
public class XStreamDateConveter implements Converter {

	/**
	 * 是否能够转换
	 */
	@SuppressWarnings("rawtypes")
	public boolean canConvert(Class cls) {
		return Date.class.isAssignableFrom(cls);
	}

	/**
	 * java对象到xml文件转换逻辑
	 */
	public void marshal(Object obj, HierarchicalStreamWriter writer,
			MarshallingContext context) {
		System.out.println(obj);
		if (obj instanceof Date) {
			writer.setValue(DateUtil.formateTime((Date)obj));
		}
	}

	/**
	 * xml文件转换为java对象逻辑
	 */
	public Object unmarshal(HierarchicalStreamReader reader,
			UnmarshallingContext context) {
		return null;
	}
}
