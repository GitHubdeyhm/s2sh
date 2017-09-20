package learn.frame.common;

import java.io.Serializable;
import java.util.List;
/**
 * 分页
 * @author Administrator
 * @param <T>
 */
public class Page<T> implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	/**排序方式：升序asc*/
	public static final String ORDER_ASC = "asc";
	/**排序方式：降序desc*/
	public static final String ORDER_DESC = "desc";
	
	/**当前页码*/
	private int pageNum = 1;
	/**每页显示的大小*/
	private int pageSize = 15;
	/**总记录数*/
	private long totalNum = 0;
	/**数据列表*/
	private List<T> list;
	/**排序字段名称*/
	private String sortName;
	/**排序方式，降序还是升序（desc/asc）*/
	private String sortWay;
	
	public Page() {
		
	}
	
	public Page(int pageNum, int pageSize) {
		this.pageNum = pageNum < 1 ? 1 : pageNum;
		this.pageSize = pageSize;
	}
	
	public int getPageNum() {
		return pageNum;
	}
	public void setPageNum(int pageNum) {
		this.pageNum = pageNum < 1 ? 1 : pageNum;
	}
	
	public int getPageSize() {
		return pageSize;
	}
	public void setPageSize(int pageSize) {
		this.pageSize = pageSize < 1 ? 1 : pageSize;
	}
	
	public long getTotalNum() {
		return totalNum;
	}
	public void setTotalNum(long totalNum) {
		this.totalNum = totalNum < 0 ? 0 : totalNum;
	}
	
	public List<T> getList() {
		return list;
	}
	public void setList(List<T> list) {
		this.list = list;
	}

	public String getSortName() {
		return sortName;
	}

	public void setSortName(String sortName) {
		this.sortName = sortName;
	}

	public String getSortWay() {
		return sortWay;
	}

	public void setSortWay(String sortWay) {
		this.sortWay = sortWay;
	}
}
