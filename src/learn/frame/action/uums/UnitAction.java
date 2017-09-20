package learn.frame.action.uums;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import learn.frame.action.BaseAction;
import learn.frame.common.Constant;
import learn.frame.common.MessageConstant;
import learn.frame.common.Page;
import learn.frame.common.ResposeResult;
import learn.frame.common.easyui.Datagrid;
import learn.frame.common.easyui.TreeNode;
import learn.frame.entity.uums.UnitEntity;
import learn.frame.exception.ServiceException;
import learn.frame.service.uums.IUnitService;
import learn.frame.utils.JSONUtil;
import learn.frame.utils.StringUtil;

/**
 * 部门action
 * @Date 2016-3-19 下午11:01:53
 */
public class UnitAction extends BaseAction {

	private static final long serialVersionUID = 1L;
	
	@Resource
	private IUnitService unitService;
	
	/**部门ID*/
	private String unitId;
	/**部门编号*/
	private String unitCode;
	/**部门名称*/
	private String unitName;
	/**上级部门*/
	private String parentId;
	/**部门说明*/
	private String unitNote;
	//部门排序
	private Integer unitOrder;
	
	private Integer page;
	private Integer rows;
	
	@Override
	public String showList() throws Exception {
		//设置分页对象
		int pageSize = (rows == null || rows < 1) ? 1 : rows;
		int pageNum = (page == null || page < 1) ? 1 : page;
		Page<UnitEntity> pageBo = new Page<UnitEntity>(pageNum, pageSize);
		UnitEntity unit = new UnitEntity();
		unit.setUnitCode(unitCode);
		unit.setUnitName(unitName);
		try {
			pageBo = unitService.findByPage(pageBo, unit);
		} catch (ServiceException e) {
			e.printStackTrace();
		}
		Datagrid<UnitEntity> dg = new Datagrid<UnitEntity>(pageBo.getTotalNum(), pageBo.getList());
		renderJSON(dg);
		return null;
	}
	
	/**
	 * 显示部门树信息--编码code为树的值，一次加载
	 * @Date 2016-1-10下午2:35:16
	 * @return
	 * @throws Exception
	 */
	public String showCodeTree() throws Exception {
		renderJSON(unitService.findForCodeTree());
		return null;
	}
	
	/**
	 * 显示部门信息--异步加载树，部门编码code为值
	 * @Date 2015-10-24下午6:52:50
	 * @return
	 * @throws Exception
	 */
	public String showCodeLazyTree() throws Exception {
		String jsonStr = null;
		List<TreeNode> nodeList = unitService.findForCodeLazyTree(getId());
		//第一次加载树时添加根节点
		if (StringUtil.isNullOrEmpty(getId())) {
			List<TreeNode> nodes = new ArrayList<TreeNode>();
			TreeNode root = new TreeNode(Constant.TREENODE_ROOT_ID, Constant.TREENODE_ROOT_TEXT);
			root.setState(TreeNode.STATE_OPEN);
			root.getAttributes().put("unitId", Constant.TREENODE_ROOT_ID);//根节点的部门ID为-1
			root.setChildren(nodeList);
			nodes.add(root);
			jsonStr = JSONUtil.toJSONStr(nodes);
		} else {
			jsonStr = JSONUtil.toJSONStr(nodeList);
		}
		renderText(jsonStr);
		return null;
	}
	
	/**
	 * 显示部门信息--id为值，一次加载
	 * @Date 2016-1-10下午2:42:01
	 * @return
	 * @throws Exception
	 */
	public String showIdTree() throws Exception {
		renderJSON(unitService.findForIdTree());
		return null;
	}
	/**
	 * 显示部门信息--异步加载树，id为值
	 * @Date 2016-1-10下午2:43:57
	 * @return
	 * @throws Exception
	 */
	public String showIdLazyTree() throws Exception {
		String jsonStr = null;
		List<TreeNode> nodeList = unitService.findForIdLazyTree(getId());
		if (StringUtil.isNullOrEmpty(getId())) {
			List<TreeNode> nodes = new ArrayList<TreeNode>();
			TreeNode root = new TreeNode(Constant.TREENODE_ROOT_ID, Constant.TREENODE_ROOT_TEXT);
			root.setState(TreeNode.STATE_OPEN);
			root.getAttributes().put("unitCode", Constant.TREENODE_ROOT_ID);//根节点的部门编码为-1
			root.setChildren(nodeList);
			nodes.add(root);
			jsonStr = JSONUtil.toJSONStr(nodes);
		} else {
			jsonStr = JSONUtil.toJSONStr(nodeList);
		}
		renderText(jsonStr);
		return null;
	}
	
	@Override
	public String save() throws Exception {
		ResposeResult rr = ResposeResult.successResult();//1：修改
		try {
			UnitEntity unit = unitService.findById(unitId);
			if (unit == null) {
				unit = new UnitEntity();//新增
				unit.setParentId(parentId);
				//根节点新增
				if (Constant.TREENODE_ROOT_ID.equals(unitCode)) {
					unitCode = null;
				}
				unit.setUnitCode(unitCode);
				unit.setCreateTime(new Date());
				unit.setDeleteFlag(Constant.DELETE_FLAG_FALSE);
				rr.setResultCode(2);//2：新增
			}
			unit.setUnitNote(unitNote);
			unit.setUnitName(unitName);
			unit.setUnitOrder(unitOrder);
			unitService.saveOrUpdate(unit);
			rr.setObj(unit);
		} catch (ServiceException e) {
			rr.setResultCode(ResposeResult.ERROR_CODE);//0：错误
			rr.setMsg(e.getMessage());
		}
		renderJSON(rr);
		return null;
	}
	
	@Override
	public String delete() throws Exception {
		ResposeResult rr = ResposeResult.successResult();
		try {
			unitService.deleteByCode(unitCode);
		} catch (ServiceException e) {
			rr.setResultCode(ResposeResult.ERROR_CODE);
			rr.setMsg(e.getMessage());
		}
		renderJSON(rr);
		return null;
	}
	
	/**
	 * 新增时显示最大排序号
	 * @Date 2016-8-7上午11:18:21
	 * @return
	 * @throws Exception
	 */
	public String showOrder() throws Exception {
		ResposeResult rr = ResposeResult.successResult();
		try {
			rr.setMsg(String.valueOf(unitService.generateNewOrder()));
		} catch (Exception e) {
			rr.setResultCode(ResposeResult.ERROR_CODE);
			rr.setMsg(e.getMessage());
		}
		renderJSON(rr);
		return null;
	}
	
	/**
	 * 新增部门时生成部门编码
	 */
	public String newUnitCode() throws Exception {
		ResposeResult rr = ResposeResult.successResult();
		try {
			//根节点新增
			if (Constant.TREENODE_ROOT_ID.equals(unitCode)) {
				unitCode = null;
			}
			rr.setMsg(unitService.generateNewCode(unitCode));
		} catch (ServiceException e) {
			rr.setResultCode(ResposeResult.ERROR_CODE);
			rr.setMsg(e.getMessage());
		}
		renderJSON(rr);
		return null;
	}

	public String getUnitId() {
		return unitId;
	}

	public void setUnitId(String unitId) {
		this.unitId = unitId;
	}

	public String getUnitCode() {
		return unitCode;
	}

	public void setUnitCode(String unitCode) {
		this.unitCode = unitCode;
	}

	public String getUnitName() {
		return unitName;
	}

	public void setUnitName(String unitName) {
		this.unitName = unitName;
	}

	public String getParentId() {
		return parentId;
	}

	public void setParentId(String parentId) {
		this.parentId = parentId;
	}

	public String getUnitNote() {
		return unitNote;
	}

	public void setUnitNote(String unitNote) {
		this.unitNote = unitNote;
	}

	public Integer getPage() {
		return page;
	}

	public void setPage(Integer page) {
		this.page = page;
	}

	public Integer getRows() {
		return rows;
	}

	public void setRows(Integer rows) {
		this.rows = rows;
	}

	public Integer getUnitOrder() {
		return unitOrder;
	}

	public void setUnitOrder(Integer unitOrder) {
		this.unitOrder = unitOrder;
	}
}
