package learn.test.program;

import java.util.Date;

import learn.frame.vo.UserVo;

/**
 * 此类实现java的对象克隆。
 * 要实现对象克隆必须实现Cloneable接口，或者会抛出CloneNotSupportedException异常
 * @Date 2017-3-6下午10:34:07
 */
public class CloneTest implements Cloneable {

	private String id;
	private int age;
	private boolean isConfirm;
	private Date createTime;
	private Integer num;
	private UserVo user;
	
	public CloneTest(String id, int age, boolean isConfirm, Date createTime) {
		this.id = id;
		this.age = age;
		this.isConfirm = isConfirm;
		this.createTime = createTime;
	}

	/**
	 * 调用Object类的clone()方法实现对象的克隆。
	 * clone()方法克隆出来的对象与源对象不是同一个对象，但是clone()方法只能实现对象的浅表复制，
	 * 即只对基本数据类型复制值，对于引用类型只是简单的复制这个引用变量，实际上指向的是同一个对象
	 */
	public CloneTest clone() throws CloneNotSupportedException {
		return (CloneTest)super.clone();
	}
	
	public static void main(String[] args) {
		CloneTest test = new CloneTest("id", 100, false, new Date());
		test.setNum(new Integer(888));
		test.setUser(new UserVo());
		try {
			CloneTest cloneObj = test.clone();
			System.out.println(cloneObj == test);//false,克隆出来的对象不与源对象相等
			System.out.println(test.getId().equals(cloneObj.getId()));//true
			System.out.println(test.getId() == cloneObj.getId());//true
			System.out.println(test.getAge() == cloneObj.getAge());//true
			
			System.out.println(test.getCreateTime() == cloneObj.getCreateTime());//true，同一个对象
			System.out.println(test.isConfirm == cloneObj.isConfirm());//true
			System.out.println(test.getNum() == cloneObj.getNum());//true
			
			
			System.out.println(test.getUser() == cloneObj.getUser());//true，复制出来的是同一个对象
			
			System.out.println(test.getUser().getRealName());
			cloneObj.getUser().setRealName("米老鼠");
			System.out.println(test.getUser().getRealName());//米老鼠
		} catch (CloneNotSupportedException e) {
			e.printStackTrace();
		}
	}
	
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public int getAge() {
		return age;
	}

	public void setAge(int age) {
		this.age = age;
	}

	public boolean isConfirm() {
		return isConfirm;
	}

	public void setConfirm(boolean isConfirm) {
		this.isConfirm = isConfirm;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public Integer getNum() {
		return num;
	}

	public void setNum(Integer num) {
		this.num = num;
	}

	public UserVo getUser() {
		return user;
	}

	public void setUser(UserVo user) {
		this.user = user;
	}
}
