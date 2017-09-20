package learn.test.json;

import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import learn.frame.vo.UserVo;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * 本类使用jackson.jar包实现解析json串和java对象之间的转换关系
 * @Date 2017-3-15下午10:28:38
 */
public class JacksonTest {
	
	public static void main(String[] args) {
		JacksonTest test = new JacksonTest();
		/** 
         * ObjectMapper是JSON操作的核心，Jackson的所有JSON操作都是在ObjectMapper中实现。 
         * ObjectMapper有多个JSON序列化的方法，可以把JSON字符串保存File、OutputStream等不同的介质中。 
         * writeValue(File arg0, Object arg1)把arg1转成json序列，并保存到arg0文件中。 
         * writeValue(OutputStream arg0, Object arg1)把arg1转成json序列，并保存到arg0输出流中。 
         * writeValueAsBytes(Object arg0)把arg0转成json序列，并把结果输出成字节数组。 
         * writeValueAsString(Object arg0)把arg0转成json序列，并把结果输出成字符串。
         */
		ObjectMapper mapper = new ObjectMapper();
		//不解析为null的字段值
		mapper.setSerializationInclusion(Include.NON_NULL);
		
		test.toJSONStr(mapper, TestVo.getTestVo());
		
		String jsonStr = "{\"id\":null,\"realName\":null,\"age\":15,\"createTime\":1489546587465,\"floatNum\":100.39,\"doubleNum\":78.02}";
		test.parseJSONStr(mapper, jsonStr);
		
		
		test.annotation(mapper);
	}
	
	/**
	 * 将对象转换为json串
	 * Jackson包将对象转换为json串说明：
     * 1、默认对于为null的字段转换后的json串包括该字段且值也为null，可以通过方法
     *   ObjectMapper#setSerializationInclusion(Include.NON_NULL);不解析为null值的字段
     * 2、对于为null的数值类型转换后的json串也为null，不会赋予默认值0
     * 3、对于日期类型Date转换后的json串显示的是该时间的毫秒数
     */
	String toJSONStr(ObjectMapper mapper, Object obj) {
		try {
			//将转换的json串写入文件
			mapper.writeValue(new File("Jackson.json"), obj);
			//将对象转换为json串
			System.out.println("转换普通对象：");
			String jsonStr = mapper.writeValueAsString(obj);
			System.out.println(jsonStr);
			
			System.out.println("\n转换List集合：");
			List<TestVo> list = new ArrayList<TestVo>();
			list.add(TestVo.getTestVo());
			list.add(TestVo.getTestVo());
			System.out.println(mapper.writeValueAsString(list));
			
			System.out.println("\n转换Map集合：");
			Map<String, TestVo> testMap = new HashMap<String, TestVo>();
			testMap.put("testA", TestVo.getTestVo());
			System.out.println(mapper.writeValueAsString(testMap));
			return jsonStr;
		} catch (JsonProcessingException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	/**
	 * 解析json格式的字符串
	 * @Date 2017-3-15上午11:01:03
	 */
	void parseJSONStr(ObjectMapper mapper, String jsonStr) {
		try {
			//从文件中读取json串然后转换为对象
			TestVo test = mapper.readValue(new File("Jackson.json"), TestVo.class);
			System.out.println(test.getAge());
			
			//读取json串转换为对象
			Map map = mapper.readValue(jsonStr, Map.class);
			System.out.println(map.get("age"));
		} catch (JsonParseException e) {
			e.printStackTrace();
		} catch (JsonMappingException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Jackson包的注解使用
	 * @Date 2017-3-15上午11:25:09
	 * @param mapper
	 */
	void annotation(ObjectMapper mapper) {
		JacksonTestInner json = new JacksonTestInner("米老鼠", 15, new Date());
		try {
			System.out.println(mapper.writeValueAsString(json));
		} catch (JsonProcessingException e) {
			e.printStackTrace();
		}
	}
}

class JacksonTestInner {
	private String id;
	@JsonIgnore//代表转换为json串时忽略该字段
	private String realName;
	private Integer age;
	@JsonFormat(pattern="yyyy年MM月dd日")//日期格式化
	private Date createTime;
	@JsonProperty("num")//取别名
	private Float floatNum;
	private Double doubleNum;
	private BigDecimal price;
	
	private UserVo user;

	/**
	 * @Date 2017-3-15下午10:45:04
	 * @param realName
	 * @param age
	 * @param createTime
	 */
	public JacksonTestInner(String realName, Integer age, Date createTime) {
		super();
		this.realName = realName;
		this.age = age;
		this.createTime = createTime;
	}

	/**
	 * @return the id
	 */
	public String getId() {
		return id;
	}

	/**
	 * @param id the id to set
	 */
	public void setId(String id) {
		this.id = id;
	}

	/**
	 * @return the realName
	 */
	public String getRealName() {
		return realName;
	}

	/**
	 * @param realName the realName to set
	 */
	public void setRealName(String realName) {
		this.realName = realName;
	}

	/**
	 * @return the age
	 */
	public Integer getAge() {
		return age;
	}

	/**
	 * @param age the age to set
	 */
	public void setAge(Integer age) {
		this.age = age;
	}

	/**
	 * @return the createTime
	 */
	public Date getCreateTime() {
		return createTime;
	}

	/**
	 * @param createTime the createTime to set
	 */
	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	/**
	 * @return the floatNum
	 */
	public Float getFloatNum() {
		return floatNum;
	}

	/**
	 * @param floatNum the floatNum to set
	 */
	public void setFloatNum(Float floatNum) {
		this.floatNum = floatNum;
	}

	/**
	 * @return the doubleNum
	 */
	public Double getDoubleNum() {
		return doubleNum;
	}

	/**
	 * @param doubleNum the doubleNum to set
	 */
	public void setDoubleNum(Double doubleNum) {
		this.doubleNum = doubleNum;
	}

	/**
	 * @return the price
	 */
	public BigDecimal getPrice() {
		return price;
	}

	/**
	 * @param price the price to set
	 */
	public void setPrice(BigDecimal price) {
		this.price = price;
	}

	/**
	 * @return the user
	 */
	public UserVo getUser() {
		return user;
	}

	/**
	 * @param user the user to set
	 */
	public void setUser(UserVo user) {
		this.user = user;
	}
}
