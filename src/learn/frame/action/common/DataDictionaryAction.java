package learn.frame.action.common;

import java.util.Date;

import javax.annotation.Resource;

import learn.frame.action.BaseAction;
import learn.frame.common.Constant;
import learn.frame.common.Page;
import learn.frame.common.ResposeResult;
import learn.frame.common.easyui.Datagrid;
import learn.frame.entity.common.DataDictionaryEntity;
import learn.frame.exception.ServiceException;
import learn.frame.service.common.IDataDictionaryService;

/**
 * 数据字典设置action
 * @Date 2016-10-22下午1:32:06
 */
public class DataDictionaryAction extends BaseAction {

	private static final long serialVersionUID = 1L;
	
	@Resource
	private IDataDictionaryService dataDictionaryService;
	
	private String id;
	//字典项名称
	private String dictName;
	//字典键
	private String dictKey;
	//字典值
	private String dictValue;
	//用户是否能够编辑，代表该项可以提供用户修改 （0：否，1：是）
	private Integer dictUserEdit;
	//字典描述信息
	private String dictNote;
	
	/**easyui固定字段--每页显示的记录数*/
	private Integer rows;
	/**easyui固定字段--当前页码*/
	private Integer page;
	/**easyui固定字段--排序字段*/

	@Override
	public String showList() throws Exception {
		//设置分页对象
		int pageSize = (rows == null || rows < 1) ? Constant.PAGE_SIZE : rows;
		int pageNum = (page == null || page < 1) ? 1 : page;
		Page<DataDictionaryEntity> pageBo = new Page<DataDictionaryEntity>(pageNum, pageSize);
		//设置查询条件
		DataDictionaryEntity dict = new DataDictionaryEntity();
		dict.setDictName(dictName);
		dict.setDictKey(dictKey);
		dict.setDictValue(dictValue);
		dict.setDictUserEdit(dictUserEdit);
		try {
			pageBo = dataDictionaryService.findByPage(pageBo, dict);
		} catch (Exception e) {
			log.error(e);
		}
		Datagrid<DataDictionaryEntity> dg = new Datagrid<DataDictionaryEntity>(pageBo.getTotalNum(), pageBo.getList());
		renderJSON(dg, true);
		return null;
	}
	
	
	@Override
	public String save() throws Exception {
		ResposeResult rr = ResposeResult.successResult();
		try {
			DataDictionaryEntity dict = dataDictionaryService.findById(getId());
			if (dict == null) {
				dict = new DataDictionaryEntity();
				dict.setCreateTime(new Date());
			}
			dict.setDictName(dictName);
			dict.setDictKey(dictKey);
			dict.setDictValue(dictValue);
			dict.setDictUserEdit(dictUserEdit);
			dict.setDictNote(dictNote);
			dataDictionaryService.saveOrUpdate(dict);
		} catch(ServiceException e) {
			rr.setResultCode(ResposeResult.ERROR_CODE);
			rr.setMsg(e.getMessage());
		}
		renderText(rr.toString());
		return null;
	}
	
	@Override
	public String delete() throws Exception {
		ResposeResult rr = ResposeResult.successResult();
		try {
			dataDictionaryService.delete(getId());
		} catch (ServiceException e) {
			rr.setResultCode(ResposeResult.ERROR_CODE);
			rr.setMsg(e.getMessage());
		}
		renderJSON(rr);
		return null;
	}
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getDictKey() {
		return dictKey;
	}
	public void setDictKey(String dictKey) {
		this.dictKey = dictKey;
	}
	public String getDictValue() {
		return dictValue;
	}
	public void setDictValue(String dictValue) {
		this.dictValue = dictValue;
	}
	public Integer getDictUserEdit() {
		return dictUserEdit;
	}
	public void setDictUserEdit(Integer dictUserEdit) {
		this.dictUserEdit = dictUserEdit;
	}
	public String getDictNote() {
		return dictNote;
	}
	public void setDictNote(String dictNote) {
		this.dictNote = dictNote;
	}

	public Integer getRows() {
		return rows;
	}

	public void setRows(Integer rows) {
		this.rows = rows;
	}

	public Integer getPage() {
		return page;
	}

	public void setPage(Integer page) {
		this.page = page;
	}

	public String getDictName() {
		return dictName;
	}

	public void setDictName(String dictName) {
		this.dictName = dictName;
	}
}
