package learn.frame.vo;

import java.math.BigDecimal;
import java.util.Date;

/**
 * @Date 2017-3-14下午9:21:13
 */
public class DemoVo {
	
	private String id;
	private String realName;
	private Integer age;
	private Date createTime;
	private Float floatNum;
	private Double doubleNum;
	private BigDecimal price;
	
	private UserVo user;
	
	public DemoVo() {
		
	}
	
	public DemoVo(String realName, Integer age, Date createTime) {
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
