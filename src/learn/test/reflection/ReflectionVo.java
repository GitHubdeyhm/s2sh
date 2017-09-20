package learn.test.reflection;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.Table;

import learn.frame.entity.uums.RoleEntity;

/**
 * @Date 2017-5-25下午5:09:43
 */
@Entity
@Table
public class ReflectionVo extends RoleEntity implements Cloneable,Serializable {

	private static final long serialVersionUID = 1L;
	
	private String userId;
	private Integer age;
	
	public String name;
	
	public ReflectionVo() {
		System.out.println("调用了无参构造方法");
	}
	
	private ReflectionVo(Integer age) {
		this.age = age;
	}

	/**
	 * @Date 2017-5-25下午5:10:54
	 * @param userId
	 * @param age
	 */
	public ReflectionVo(String userId, Integer age) {
		System.out.println("调用了构造方法ReflectionVo(String userId, Integer age)");
		this.userId = userId;
		this.age = age;
	}
	
	public static void getStat() {
		System.out.println("调用了静态方法");
	}
	
	private void getPrivate() {
		System.out.println("调用了私有方法getPrivate()");
	}

	/**
	 * @return the userId
	 */
	public String getUserId() {
		System.out.println("调用了方法getUserId()");
		return userId;
	}

	/**
	 * @param userId the userId to set
	 */
	public void setUserId(String userId) {
		this.userId = userId;
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
}
