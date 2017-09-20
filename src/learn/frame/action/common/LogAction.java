package learn.frame.action.common;

import javax.annotation.Resource;

import learn.frame.action.BaseAction;
import learn.frame.common.Constant;
import learn.frame.common.Page;
import learn.frame.common.ResposeResult;
import learn.frame.common.easyui.Datagrid;
import learn.frame.entity.common.LogEntity;
import learn.frame.exception.ServiceException;
import learn.frame.service.common.ILogService;

/**
 * @Date 2016-9-4下午12:10:00
 */
public class LogAction extends BaseAction {

	private static final long serialVersionUID = 1L;
	
	@Resource
	private ILogService logService;
	
	//日志名称（模块名称）
	private String logName;
	//日志编码
	private String logCode;
	//日志内容
	private String logContent;
	//日志操作标识:（1：登陆，2：退出系统，3：新增，4：修改，5：删除，6：查询，7：导入，8：导出）
	private Integer optType;
	//操作url标示
	private String optUrl;
	//操作请求方法（get、post）
	private String optRequestMethod;
	//操作状态（0：操作失败，1：操作成功，2：警告）
	private Integer optStatus;
	//操作人
	private String realName;
	//操作时间
	private String beginTime;
	private String endTime;
	//操作人IP地址
	private String userIP;
	
	/**easyui固定字段--每页显示的记录数*/
	private Integer rows;
	/**easyui固定字段--当前页码*/
	private Integer page;
	
	@Override
	public String showList() throws Exception {
		//设置分页对象
		int pageSize = (rows == null || rows < 1) ? Constant.PAGE_SIZE : rows;
		int pageNum = (page == null || page < 1) ? 1 : page;
		Page<LogEntity> pageBo = new Page<LogEntity>(pageNum, pageSize);
		//设置查询条件
		LogEntity sysLog = new LogEntity();
		sysLog.setLogContent(logContent);
		sysLog.setLogName(logName);
		sysLog.setOptType(optType);
		sysLog.setBeginTime(beginTime);
		sysLog.setEndTime(endTime);
		sysLog.setRealName(realName);
		try {
			pageBo = logService.findByPage(pageBo, sysLog);
		} catch (Exception e) {
			log.error(e);
		}
		Datagrid<LogEntity> dg = new Datagrid<LogEntity>(pageBo.getTotalNum(), pageBo.getList());
		renderJSON(dg, true);
		return null;
	}
	
	@Override
	public String delete() throws Exception {
		ResposeResult rr = ResposeResult.successResult();
		try {
			logService.delete(getId());
		} catch (ServiceException e) {
			rr.setMsg(e.getMessage());
			rr.setResultCode(ResposeResult.ERROR_CODE);
		}
		renderText(rr.toString());
		return null;
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

	public ILogService getLogService() {
		return logService;
	}

	public void setLogService(ILogService logService) {
		this.logService = logService;
	}

	public String getLogName() {
		return logName;
	}

	public void setLogName(String logName) {
		this.logName = logName;
	}

	public String getLogCode() {
		return logCode;
	}

	public void setLogCode(String logCode) {
		this.logCode = logCode;
	}

	public String getLogContent() {
		return logContent;
	}

	public void setLogContent(String logContent) {
		this.logContent = logContent;
	}

	public Integer getOptType() {
		return optType;
	}

	public void setOptType(Integer optType) {
		this.optType = optType;
	}

	public String getOptUrl() {
		return optUrl;
	}

	public void setOptUrl(String optUrl) {
		this.optUrl = optUrl;
	}

	public String getOptRequestMethod() {
		return optRequestMethod;
	}

	public void setOptRequestMethod(String optRequestMethod) {
		this.optRequestMethod = optRequestMethod;
	}

	public Integer getOptStatus() {
		return optStatus;
	}

	public void setOptStatus(Integer optStatus) {
		this.optStatus = optStatus;
	}

	public String getRealName() {
		return realName;
	}

	public void setRealName(String realName) {
		this.realName = realName;
	}

	public String getBeginTime() {
		return beginTime;
	}

	public void setBeginTime(String beginTime) {
		this.beginTime = beginTime;
	}

	public String getEndTime() {
		return endTime;
	}

	public void setEndTime(String endTime) {
		this.endTime = endTime;
	}

	public String getUserIP() {
		return userIP;
	}

	public void setUserIP(String userIP) {
		this.userIP = userIP;
	}
}
