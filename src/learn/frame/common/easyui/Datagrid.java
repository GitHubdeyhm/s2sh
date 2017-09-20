package learn.frame.common.easyui;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import learn.frame.utils.JSONUtil;

/**
 * eayui的数据表格模型--其中字段名称必须是下面的固定名称
 * @Date 2016-8-6 下午12:26:57
 */
public class Datagrid<T> implements Serializable {

	private static final long serialVersionUID = 1L;
	
	/**当数据为null时，初始化list容量的大小*/
	private static final int initSize = 1;
	
	/**数据的总行数*/
	private long total;
	/**数据行，数据行不能为null*/
	private List<T> rows;
	/**页脚*/
	private List<T> footer;
	
	
	
	public Datagrid() {
		
	}
	
	public Datagrid(List<T> rows) {
		this(0L, rows);
	}
	
	public Datagrid(long total, List<T> rows) {
		this(total, rows, null);
	}
	
	public Datagrid(long total, List<T> rows, List<T> footer) {
		if (rows == null) {
			rows = new ArrayList<T>(initSize);
		}
		this.total = total < 0L ? 0L : total;
		this.rows = rows;
		this.footer = footer;
	}

	public long getTotal() {
		return total;
	}
	public void setTotal(long total) {
		this.total = total < 0L ? 0L : total;
	}
	
	public List<T> getRows() {
		return rows;
	}
	public void setRows(List<T> rows) {
		if (rows == null) {
			rows = new ArrayList<T>(initSize);
		}
		this.rows = rows;
	}
	
	public List<T> getFooter() {
		return footer;
	}
	public void setFooter(List<T> footer) {
		this.footer = footer;
	}
	
	@Override
	public String toString() {
		return JSONUtil.toJSONStr(this, true);
	}
}
