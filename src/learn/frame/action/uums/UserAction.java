package learn.frame.action.uums;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import learn.frame.action.BaseAction;
import learn.frame.common.Constant;
import learn.frame.common.MessageConstant;
import learn.frame.common.Page;
import learn.frame.common.ResposeResult;
import learn.frame.common.UserSession;
import learn.frame.common.easyui.Datagrid;
import learn.frame.entity.uums.UserEntity;
import learn.frame.entity.uums.UserRoleRelEntity;
import learn.frame.entity.uums.UserUnitRelEntity;
import learn.frame.exception.ServiceException;
import learn.frame.service.uums.IRoleMenuRelService;
import learn.frame.service.uums.IUserRoleRelService;
import learn.frame.service.uums.IUserService;
import learn.frame.service.uums.IUserUnitRelService;
import learn.frame.utils.DateUtil;
import learn.frame.utils.ExcelUtil;
import learn.frame.utils.StringUtil;
import learn.frame.utils.file.FileUploadUtil;
import learn.frame.vo.UserVo;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

/**
 * 用户action
 * @Date 2016-8-7上午10:06:04
 */
public class UserAction extends BaseAction {

	private static final long serialVersionUID = 1L;
	
	@Resource
	private IUserService userService;
	@Resource
	private IUserRoleRelService userRoleRelService;
	@Resource
	private IUserUnitRelService userUnitRelService;
	@Resource
	private IRoleMenuRelService roleMenuRelService;
	
	/**用户名*/
	private String userName;
	/**真实姓名*/
	private String realName;
	/**密码*/
	private String userPwd;
	/**手机号码*/
	private String phone;
	/**邮箱*/
	private String email;
	/**部门编码*/
	private String unitCodes;
	/**角色ID，多个角色以逗号隔开*/
	private String roleIds;
	/**性别（0：男，1：女，2：保密）*/
	private String genderStr;
	
	//头像文件
	private File headImgUrl;
	private String headImgUrlFileName;
	
	/**临时字段--新密码，用于用户修改密码时*/
	private String newPwd;
	/**临时字段--确认新密码，用于用户修改密码时*/
	private String againPwd;
	
	/**easyui固定字段--每页显示的记录数*/
	private Integer rows;
	/**easyui固定字段--当前页码*/
	private Integer page;

	
	/**
	 * 分页查询用户列表，包含用户所属部门和角色
	 * @Date 2016-9-11下午1:30:39
	 */
	@Override
	public String showList() throws Exception {
		//设置分页对象
		int pageSize = (rows == null || rows < 1) ? Constant.PAGE_SIZE : rows;
		int pageNum = (page == null || page < 1) ? 1 : page;
		Page<UserVo> pageBo = new Page<UserVo>(pageNum, pageSize);
		//设置查询条件
		UserVo user = new UserVo();
		user.setRealName(realName);
		user.setUserName(userName);
		if (!Constant.TREENODE_ROOT_ID.equals(unitCodes)) {
			user.setUnitCodes(unitCodes);
		}
		if (!Constant.COMBO_DEFAULT_VALUE.equals(roleIds)) {
			user.setRoleIds(roleIds);
		}
		try {
			pageBo = userService.findByPage(pageBo, user);
		} catch (Exception e) {
			log.error(e);
		}
		List<UserVo> list = pageBo.getList();
		Datagrid<UserVo> dg = new Datagrid<UserVo>(pageBo.getTotalNum(), list);
		renderJSON(dg);
		return null;
	}
	
	/**
	 * 用户登录方法
	 * @Date 2016-8-7上午9:49:16
	 */
	public String login() throws Exception {
		ResposeResult rr = ResposeResult.errorResult();
		if (StringUtil.isAnyNullOrEmpty(userName, userPwd)) {
			rr.setMsg("用户名或密码不能为空");
		} else {
			try {
				UserEntity user = userService.findLoginUser(userName, userPwd);
				if (user == null) {
					rr.setMsg("用户名或密码不正确");
				} else {
					//保存session信息
					saveUserSession(user);
					rr.setResultCode(ResposeResult.SUCCESS_CODE);
				}
			} catch (ServiceException e) {
				rr.setMsg(e.getMessage());
			}
		}
		renderText(rr.toString());
		return null;
	}
	
	/**
	 * 保存登陆用户的用户信息
	 * @Date 2016-8-7上午9:52:04
	 * @param user 用户实体类
	 */
	private void saveUserSession(UserEntity user) {
		String userId = user.getId();
		UserSession userSess = new UserSession();
		userSess.setUserName(user.getUserName());
		userSess.setRealName(user.getRealName());
		userSess.setUserId(userId);
		//超级管理员
		if (Constant.ADMIN_USER_ID.equals(userId)) {
			String adminRole = String.valueOf(Constant.ADMIN_ROLE_LEVEL);
			userSess.setRoleIds(adminRole);
			userSess.setUnitCodes(adminRole);
			userSess.setMenuCodePrivils(adminRole);
		} else {
			//根据用户ID查询角色
			List<UserRoleRelEntity> urList = userRoleRelService.findByUserId(userId);
			if (urList != null && urList.size() > 0) {
				StringBuilder roleIds = new StringBuilder();
				for (UserRoleRelEntity ur : urList) {
					roleIds.append(ur.getRoleId()+",");
				}
				userSess.setRoleIds(roleIds.substring(0, roleIds.length()-1));
			}
			//根据用户ID查询部门信息
			List<UserUnitRelEntity> uuList = userUnitRelService.findByUserId(userId);
			if (uuList != null && uuList.size() > 0) {
				StringBuilder unitCodes = new StringBuilder();
				for (UserUnitRelEntity uu : uuList) {
					unitCodes.append(uu.getUnitCode()+",");
				}
				userSess.setUnitCodes(unitCodes.substring(0, unitCodes.length()-1));
			}
			//为用户添加菜单权限
			List<String> menuCodeList = roleMenuRelService.findByRoleIds(userSess.getRoleIds());
			if (menuCodeList != null && menuCodeList.size() > 0) {
				StringBuilder menuCodePrivils = new StringBuilder();
				for (String menuCode : menuCodeList) {
					menuCodePrivils.append(menuCode+",");
				}
				userSess.setMenuCodePrivils(menuCodePrivils.substring(0, menuCodePrivils.length()-1));
			}
		}
		getSession().setAttribute(Constant.USER_SESSION, userSess);
	}
	
	@Override
	public String save() throws Exception {
		ResposeResult rr = ResposeResult.errorResult();
		if (StringUtil.isNullOrEmpty(userName)) {
			rr.setMsg("用户名不能为空");
		} else {
			try {
				//验证文件
				String tipMsg = validateUploadFile();
				if (tipMsg != null) {
					rr.setMsg(tipMsg);
					renderJSON(rr);
					return null;
				}
				UserEntity user = userService.findById(getId());
				if (user == null) {
					user = new UserEntity();
					user.setCreateTime(new Date());
					user.setDeleteFlag(Constant.DELETE_FLAG_FALSE);
					user.setUserPwd(Constant.USER_RESET_PWD);
				}
				user.setRealName(realName);
				user.setUserName(userName);
				user.setPhone(phone);
				user.setEmail(email);
				user.setHeadImgUrl(headImgUrlFileName);
				if ("0".equals(genderStr) || "1".equals(genderStr)) {
					user.setGender(Integer.parseInt(genderStr));
				}
				user.setHeadImgUrlFile(headImgUrl);
				//保存用户基本信息
				userService.saveOrUpdate(user, roleIds, unitCodes);
				
				rr.setResultCode(ResposeResult.SUCCESS_CODE);
				rr.setMsg(ResposeResult.SUCCESS_MSG);
			} catch (ServiceException e) {
				rr.setResultCode(ResposeResult.ERROR_CODE);
				rr.setMsg(e.getMessage());
			}
		}
		renderText(rr);
		return null;
	}
	
	/**
	 * 验证上传的文件是否符合要求
	 * @Date 2016-9-11下午1:46:08
	 * @return
	 * @throws Exception
	 */
	private String validateUploadFile() throws IOException {
		String tipMsg = null;
		if (!FileUploadUtil.isEmpty(headImgUrl)) {
			if (FileUploadUtil.isAllowImageSize(headImgUrl)) {
				String extName = headImgUrlFileName.substring(headImgUrlFileName.lastIndexOf(".")+1);
				if (!FileUploadUtil.isAllowImageExtName(extName)) {
					tipMsg = FileUploadUtil.IMAGE_EXTNAME_MSG;
				}
			} else {
				tipMsg = FileUploadUtil.IMAGE_MAX_SIZE_MSG;
			}
		}
		return tipMsg;
	}
	
	@Override
	public String delete() throws Exception {
		ResposeResult rr = ResposeResult.successResult();
		try {
			System.out.println("------------s");
			//userService.delete(getId());
		} catch (Exception e) {
			rr.setResultCode(ResposeResult.ERROR_CODE);
			rr.setMsg(e.getMessage());
		}
		renderText(rr);
		return null;
	}
	
	/**
	 * 封装查询参数
	 * @Date 2016-3-19下午11:53:37
	 * @return
	 */
	private UserEntity getSearchParam() {
		UserEntity user = new UserEntity();
		user.setRealName(realName);
//		user.setRoleIds(roleIds);
//		user.setUnitCodes(unitCodes);
//		user.setGender(gender);
		user.setUserName(userName);
		return user;
	}
	
	/**
	 * 重置用户的密码
	 * @Date 2016-10-8下午10:21:23
	 * @return
	 * @throws Exception
	 */
	public String resetPwd() throws Exception {
		ResposeResult rr = ResposeResult.successResult();
		try {
			userService.resetPassword(getId(), Constant.USER_RESET_PWD);
		} catch (Exception e) {
			rr.setResultCode(ResposeResult.ERROR_CODE);
			rr.setMsg(e.getMessage());
		}
		renderJSON(rr);
		return null;
	}
	
	/**
	 * 导出excel表格数据
	 * @Date 2016-1-17上午11:19:55
	 * @return
	 * @throws Exception
	 */
	public String exportExcel() throws Exception {
		long time = System.currentTimeMillis();
		//设置查询条件
		UserVo u = new UserVo();
		u.setRealName(realName);
		u.setUserName(userName);
		if (!Constant.TREENODE_ROOT_ID.equals(unitCodes)) {
			u.setUnitCodes(unitCodes);
		}
		if (!Constant.COMBO_DEFAULT_VALUE.equals(roleIds)) {
			u.setRoleIds(roleIds);
		}
		OutputStream out = null;
		try {
			List<UserVo> userList = userService.findByAll(u);
			int size = (userList == null) ? 0 : userList.size();
			if (size < 1) {
				throw new ServiceException(MessageConstant.EXPORT_EXCEL_EMPTY_MSG);//没有可以导出的数据
			} else if (size > Constant.EXPORT_ALLOW_COUNT) {
				throw new ServiceException(MessageConstant.EXPORT_EXCEL_MUCH_MSG);//导出的数据量太多
			} else {
				out = getResponse().getOutputStream();//得到响应流
				getResponse().setContentType(ExcelUtil.EXCEL_MIME_TYPE);//设置Excel的mime类型
				
				String name = DateUtil.formateDate(new Date())+"-用户列表";//导出文件名称
				String fileName = new String((name+".xls").getBytes("UTF-8"), "ISO-8859-1");//需要进行转码
				getResponse().setHeader("Content-disposition", "attachment;filename=" + fileName);//设定输出文件头
				
				//创建一个工作簿
				Workbook wb = new HSSFWorkbook();
				//创建一张excel表格
				Sheet sheet = wb.createSheet(name);
				//设置默认行高，对于没有数据的行也会设置高度
				//sheet.setDefaultRowHeight((short)500);
				
				//创建行对象
				Row headRow = sheet.createRow(0);
				CellStyle headCs = ExcelUtil.getHeadStyle(wb);
				ExcelUtil.createCell(headRow, 0, headCs, "用户名");
				ExcelUtil.createCell(headRow, 1, headCs, "真实性名");
				ExcelUtil.createCell(headRow, 2, headCs, "性别");
				ExcelUtil.createCell(headRow, 3, headCs, "手机号码");
				ExcelUtil.createCell(headRow, 4, headCs, "邮箱");
				ExcelUtil.createCell(headRow, 5, headCs, "所属部门");
				ExcelUtil.createCell(headRow, 6, headCs, "所属角色");
				ExcelUtil.createCell(headRow, 7, headCs, "注册时间");
				
				CellStyle bodyCs = ExcelUtil.getBodyStyle(wb);
				//一张excel表格最多只有65536行
				//不要在循环中创建字体
				for (int i = 0; i < size; i++) {
					UserVo user = userList.get(i);
					//创建行对象
					Row row = sheet.createRow(i+1);
					row.setHeightInPoints(30);//设置每行的高度
					
					ExcelUtil.createCell(row, 0, bodyCs, user.getUserName());
					ExcelUtil.createCell(row, 1, bodyCs, user.getRealName());
					ExcelUtil.createCell(row, 2, bodyCs, user.getGenderText());
					ExcelUtil.createCell(row, 3, bodyCs, user.getPhone());
					ExcelUtil.createCell(row, 4, bodyCs, user.getEmail());
					ExcelUtil.createCell(row, 5, bodyCs, user.getUnitNames());
					ExcelUtil.createCell(row, 6, bodyCs, user.getRoleNames());
					ExcelUtil.createCell(row, 7, bodyCs, user.getCreateTime());
				}
				//设置列的宽度
				for (int j = 0; j < 8; j++) {
					sheet.setColumnWidth(j, 4800);
				}
				wb.write(out);
				System.out.println("导出excel文件成功--"+(System.currentTimeMillis()-time));
			}
		} catch (IOException e) {
			log.error("导出excel数据出错："+e);
		} finally {
			if (out != null) {
				try {
					out.close();
				} catch (IOException e) {
					log.error("关闭导出Excel流错误："+e);
				}
			}
		}
		return null;
	}
	
	/**
	 * 根据一个excel模块文件实现批量导入用户数据
	 * @Date 2016-12-17下午11:07:24
	 * @return
	 * @throws Exception
	 */
	public String importExcel() throws Exception {
		ResposeResult rr = ResposeResult.errorResult();
		FileInputStream fis = null;
		try {
			//验证上传的文件
			if (FileUploadUtil.isEmpty(headImgUrl)) {
				throw new ServiceException(MessageConstant.FILE_EMPTY_MSG);
			}
			String extName = headImgUrlFileName.substring(headImgUrlFileName.lastIndexOf(".")+1);
			fis = new FileInputStream(headImgUrl);
			Workbook wb = null;
			if ("xls".equals(extName)) {
				wb = new HSSFWorkbook(fis);//2003
			} else if ("xlsx".equals(extName)) {
				wb = new XSSFWorkbook(fis);//2007
			} else {
				throw new ServiceException(MessageConstant.IMPORT_EXCEL_EXTNAME_MSG);
			}
			Sheet sheet = wb.getSheetAt(0);
			Row row = null;
			List<UserEntity> userList = new ArrayList<>();
			//得到所有有效的行，第0行代表所有标题行
			for (int i = 1; i < sheet.getPhysicalNumberOfRows(); i++) {
				UserEntity user = new UserEntity();
				row = sheet.getRow(i);
				//有效列的长度大于6时
				//row.getCell(num);方法不会报错，当num的值很大时返回null
				user.setUserName(ExcelUtil.getCellValue(row.getCell(0)));
				user.setRealName(ExcelUtil.getCellValue(row.getCell(1)));
				/**性别（0：男，1：女，2：保密（未知））*/
				String genderStr = ExcelUtil.getCellValue(row.getCell(2));
				if ("0".equals(genderStr) || "1".equals(genderStr)) {
					user.setGender(Integer.parseInt(genderStr));
				} else {
					user.setGender(2);
				}
				user.setPhone(ExcelUtil.getCellValue(row.getCell(3)));
				user.setEmail(ExcelUtil.getCellValue(row.getCell(4)));
				user.setCreateTime(DateUtil.parseTime(ExcelUtil.getCellValue(row.getCell(5))));
				userList.add(user);
			}
			rr.setResultCode(ResposeResult.SUCCESS_CODE);
			rr.setMsg(ResposeResult.SUCCESS_MSG);
		} catch (Exception e) {
			if (e instanceof ServiceException) {
				rr.setMsg(e.getMessage());
			} else {
				log.error(e);
				e.printStackTrace();
			}
		} finally {
			if (fis != null) {
				fis.close();
			}
		}
		renderText(rr);
		return null;
	}
	
	
	/**
	 * 退出登录
	 * @return
	 * @throws Exception
	 */
	public String logout() throws Exception {
		getSession().invalidate();
		return "login";
	}
	
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public String getRealName() {
		return realName;
	}
	public void setRealName(String realName) {
		this.realName = realName;
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
	public void setUnitCodes(String unitCodes) {
		this.unitCodes = unitCodes;
	}
	public String getGenderStr() {
		return genderStr;
	}
	public void setGenderStr(String genderStr) {
		this.genderStr = genderStr;
	}
	public String getNewPwd() {
		return newPwd;
	}
	public void setNewPwd(String newPwd) {
		this.newPwd = newPwd;
	}
	public String getAgainPwd() {
		return againPwd;
	}
	public void setAgainPwd(String againPwd) {
		this.againPwd = againPwd;
	}
	public String getRoleIds() {
		return roleIds;
	}
	public void setRoleIds(String roleIds) {
		this.roleIds = roleIds;
	}
	public String getUserPwd() {
		return userPwd;
	}
	public void setUserPwd(String userPwd) {
		this.userPwd = userPwd;
	}
	public String getUnitCodes() {
		return unitCodes;
	}
	public File getHeadImgUrl() {
		return headImgUrl;
	}
	public void setHeadImgUrl(File headImgUrl) {
		this.headImgUrl = headImgUrl;
	}
	public String getHeadImgUrlFileName() {
		return headImgUrlFileName;
	}
	public void setHeadImgUrlFileName(String headImgUrlFileName) {
		this.headImgUrlFileName = headImgUrlFileName;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}
}