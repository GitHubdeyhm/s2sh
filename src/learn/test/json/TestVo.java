package learn.test.json;

import java.math.BigDecimal;
import java.util.Date;

import learn.frame.vo.UserVo;

/**
 * 测试Vo类，包括java的常用的数据类型
 * @Date 2017-3-15下午10:36:40
 */
public class TestVo {
	private String id;
	private String realName;
	private Integer age;
	private Date createTime;
	private Float floatNum;
	private Double doubleNum;
	private BigDecimal price;
	private Boolean isSuccess;
	
	private UserVo user;
	
	
	
	
	public static TestVo getTestVo() {
		TestVo test = new TestVo();
		test.setAge(18);
		test.setRealName("米老鼠");
		test.setCreateTime(new Date());
		test.setFloatNum(100.89f);
		test.setDoubleNum(999.999);
		test.setIsSuccess(true);
		test.setPrice(new BigDecimal("199.99"));
		UserVo user = new UserVo();
		user.setId("id");
		user.setRealName("加菲猫");
		test.setUser(user);
		return test;
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
	 * @return the isSuccess
	 */
	public Boolean getIsSuccess() {
		return isSuccess;
	}

	/**
	 * @param isSuccess the isSuccess to set
	 */
	public void setIsSuccess(Boolean isSuccess) {
		this.isSuccess = isSuccess;
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
