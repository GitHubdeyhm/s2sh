package learn.frame.service.uums.impl;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import learn.frame.common.Constant;
import learn.frame.common.MessageConstant;
import learn.frame.common.Page;
import learn.frame.common.dao.impl.BaseDaoHibernate;
import learn.frame.common.easyui.TreeNode;
import learn.frame.entity.uums.UnitEntity;
import learn.frame.exception.ServiceException;
import learn.frame.service.uums.IUnitService;
import learn.frame.service.uums.IUserUnitRelService;
import learn.frame.utils.CodeUtil;
import learn.frame.utils.StringUtil;

import org.hibernate.SessionFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 部门service
 * @Date 2016-8-6下午9:59:36
 */
@Service
@Transactional(readOnly=true)
public class UnitService implements IUnitService {
	
	private BaseDaoHibernate<UnitEntity, String> baseDao;//dao
	
	@Resource
	private IUserUnitRelService userUnitRelService;
	
	@Resource
	public void setSessionFactory(SessionFactory sessionFactory){
		baseDao = new BaseDaoHibernate<UnitEntity, String>(sessionFactory, 
				UnitEntity.class);
	}

	@Override
	public UnitEntity findById(String id) {
		if (StringUtil.isNullOrEmpty(id)) {
			return null;
		}
		//查询为删除的数据
		String hql = "from UnitEntity where id = ? and deleteFlag = ?";
		return baseDao.get(hql, id, Constant.DELETE_FLAG_FALSE);
	}

	@Override
	@Transactional(readOnly=false)
	public void delete(String id) {
	}
	
	@Override
	@Transactional(readOnly=false)
	public void deleteByCode(String unitCode) {
		if (!StringUtil.isNullOrEmpty(unitCode)) {
			try {
				//删除用户部门关联
				userUnitRelService.deleteByUnitCode(unitCode);
				
				//删除部门会删除该部门下所有子部门（逻辑删除）
				String hql = "update UnitEntity u set deleteFlag = ? where u.unitCode like ?";
				baseDao.executeHql(hql, Constant.DELETE_FLAG_TRUE, unitCode+"%");
			} catch (Exception e) {
				log.error(e);
				throw new ServiceException(MessageConstant.DB_IO_ERROR);
			}
		}
	}


	@Override
	@Transactional(readOnly=false)
	public UnitEntity saveOrUpdate(UnitEntity t) {
		if (t != null) {
			if (StringUtil.isNullOrEmpty(t.getUnitName())) {
				throw new ServiceException("部门名称不能为空");
			}
			try {
				if (isExistName(t.getId(), t.getUnitName())) {
					throw new ServiceException("部门名称不能重复");
				}
				//新增时生成编码
				if (StringUtil.isNullOrEmpty(t.getId())) {
					t.setUnitCode(generateNewCode(t.getUnitCode()));
				}
				baseDao.saveOrUpdate(t);
			} catch (ServiceException e) {
				throw e;
			} catch (Exception e) {
				log.error(e);
				throw new ServiceException(MessageConstant.DB_IO_ERROR);
			}
		}
		return t;
	}

	@Override
	public Page<UnitEntity> findByPage(Page<UnitEntity> p, UnitEntity unit) {
		List<Object> paramList = new ArrayList<Object>();
		StringBuilder hql = new StringBuilder("from UnitEntity u where deleteFlag = ?");
		paramList.add(Constant.DELETE_FLAG_FALSE);
		if (unit != null) {
			if (!StringUtil.isNullOrEmpty(unit.getUnitName())) {
				unit.setUnitName(StringUtil.hqlEscape(unit.getUnitName()));
				hql.append(" and u.unitName like ? escape '/'");
				paramList.add("%"+unit.getUnitName().trim()+"%");
			}
			if (!StringUtil.isNullOrEmpty(unit.getUnitCode())) {
				hql.append(" and u.unitCode like ?");//部门编码，查询所有子部门
				paramList.add(unit.getUnitCode()+"%");
			}
		}
		hql.append(" order by u.unitOrder");
		return baseDao.findByPage(p, hql.toString(), paramList.toArray());
	}
	
	@Override
	public List<UnitEntity> findByName(String name) {
		if (StringUtil.isNullOrEmpty(name)) {
			return null;
		}
		String hql = "from UnitEntity u where u.unitName = ? and deleteFlag = ?";
		return baseDao.find(hql, name, Constant.DELETE_FLAG_FALSE);
	}

	@Override
	public List<UnitEntity> findByAll(UnitEntity t) {
		List<Object> paramList = new ArrayList<Object>();
		StringBuilder hql = new StringBuilder("from UnitEntity u where deleteFlag = ?");
		paramList.add(Constant.DELETE_FLAG_FALSE);
		if (t != null) {
			if (!StringUtil.isNullOrEmpty(t.getUnitName())) {
				t.setUnitName(StringUtil.hqlEscape(t.getUnitName()));
				hql.append(" and u.unitName like ? escape '/'");
				paramList.add(t.getUnitName());
			}
			if (!StringUtil.isNullOrEmpty(t.getUnitCode())) {
				hql.append(" and u.unitCode like ?");
				paramList.add(t.getUnitCode()+"%");
			}
		}
		return baseDao.find(hql.toString(), paramList.toArray());
	}
	
	@Override
	public List<UnitEntity> findAllChildByParentCode(String parentCode) {
		String hql = "from UnitEntity u where u.unitCode like ? and deleteFlag = ?";
		return baseDao.find(hql, parentCode+"%", Constant.DELETE_FLAG_FALSE);
	}

	@Override
	public List<TreeNode> findForCodeTree() {
		List<TreeNode> nodeList = new ArrayList<TreeNode>();
		TreeNode root = new TreeNode(Constant.TREENODE_ROOT_ID, Constant.TREENODE_ROOT_TEXT, TreeNode.STATE_OPEN);
		root.getAttributes().put("unitId", Constant.TREENODE_ROOT_ID);//ID作为树的一个属性值
		nodeList.add(root);
		List<UnitEntity> unitList = baseDao.find("from UnitEntity where deleteFlag = ?", Constant.DELETE_FLAG_FALSE);
		if (unitList != null && unitList.size() > 0) {
			for (UnitEntity u : unitList) {
				String code = u.getUnitCode();
				int codeLen = code.length();//得到编码的长度
				if (codeLen >= Constant.CODE_LEVEL_LENGTH) {
					TreeNode node = new TreeNode(code, u.getUnitName(), TreeNode.STATE_OPEN);//编码code作为树的值
					//根节点
					if (codeLen == Constant.CODE_LEVEL_LENGTH) {
						node.setParentId(Constant.TREENODE_ROOT_ID);//编码长度为3的话设置其父节点ID为-1
					} else {
						node.setParentId(code.substring(0, codeLen - Constant.CODE_LEVEL_LENGTH));//设置父节点
					}
					node.getAttributes().put("unitId", u.getId());//ID作为树的一个属性值
					nodeList.add(node);
				} else {
					log.error("部门编码错误："+u.getId()+"--"+u.getUnitName()+"--"+u.getUnitCode());
				}
			}
		}
		return nodeList;
	}
	
	@Override
	public List<TreeNode> findForCodeLazyTree(String parentCode)  {
		List<TreeNode> nodeList = null;
		List<UnitEntity> unitList = this.findTwoChildByParentCode(parentCode);//此处的nodeId代表编码
		if (unitList != null && unitList.size() > 0) {
			nodeList = new ArrayList<TreeNode>();
			for (UnitEntity u : unitList) {
				TreeNode node = new TreeNode(u.getUnitCode(), u.getUnitName());
				if (!u.isHasChild()) {
					node.setState(TreeNode.STATE_OPEN);
				}
				node.getAttributes().put("unitId", u.getId());//编码作为树的一个属性值
				node.getAttributes().put("unitOrder", u.getUnitOrder());//编码作为树的一个属性值
				node.getAttributes().put("unitNote", u.getUnitNote());//描述信息作为树的一个属性值
				nodeList.add(node);
			}
		}
		return nodeList;
	}
	
	@Override
	public String generateNewCode(String parentCode) {
		String hql = "select max(unitCode) from UnitEntity where unitCode like ? and deleteFlag = ?";
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < Constant.CODE_LEVEL_LENGTH; i++) {
			sb.append("_");
		}
		String param = null;
		if (StringUtil.isNullOrEmpty(parentCode)) {
			param = sb.toString();
		} else {
			param = parentCode + sb.toString();
		}
		String maxChildCode = (String)baseDao.findUnique(hql, param, Constant.DELETE_FLAG_FALSE);
		return CodeUtil.generateCode(parentCode, maxChildCode);
	}
	
	@Override
	public int generateNewOrder() {
		String hql = "select max(unitOrder) from UnitEntity where deleteFlag = ?";
		Integer maxOrder = (Integer)baseDao.findUnique(hql, Constant.DELETE_FLAG_FALSE);
		int order = (maxOrder == null) ? 1 : maxOrder+1;
		//最大排序号不能超过999999
		if (maxOrder > Constant.MAX_ORDER) {
			order = Constant.MAX_ORDER;
		}
		return order;
	}
	
	@Override
	public List<UnitEntity> findByCode(List<String> codeList) {
		int size = (codeList == null) ? 0 : codeList.size();
		if (size > 0) {
			StringBuilder hql = new StringBuilder("from UnitEntity u where u.unitCode in ( ");
			for (int i = 0; i < size; i++) {
				if (i + 1 == size) {
					hql.append(" ?)");
				} else {
					hql.append(" ?,");
				}
			}
			return baseDao.find(hql.toString(), codeList.toArray());
		}
		return null;
	}
	
	@Override
	public List<UnitEntity> findLeafUnit(String parentCode) {
		List<Object> paramList = new ArrayList<Object>();
		StringBuilder hql = new StringBuilder("from UnitEntity u where u.unitId not in ");
		hql.append(" (select distinct ui.parentId from UnitEntity ui where ui.parentId is not null)");
		if (!StringUtil.isNullOrEmpty(parentCode)) {
			hql.append(" and u.unitCode like ?");
			paramList.add(parentCode+"%");
		}
		hql.append(" and deleteFlag = ?");
		paramList.add(Constant.DELETE_FLAG_FALSE);
		return baseDao.find(hql.toString(), paramList.toArray());
	}


	@Override
	public List<TreeNode> findForIdLazyTree(String id) {
		List<TreeNode> nodeList = null;
		List<UnitEntity> unitList = findChildByParentId(id);
		if (unitList != null && unitList.size() > 0) {
			nodeList = new ArrayList<TreeNode>();
			for (UnitEntity u : unitList) {
				TreeNode node = new TreeNode(u.getId(), u.getUnitName());
				if (!u.isHasChild()) {
					node.setState(TreeNode.STATE_OPEN);
				}
				node.getAttributes().put("unitCode", u.getUnitCode());//编码作为树的一个属性值
				node.getAttributes().put("unitNote", u.getUnitNote());//描述信息作为树的一个属性值
				nodeList.add(node);
			}
		}
		return nodeList;
	}

	@Override
	public List<TreeNode> findForIdTree() {
		List<TreeNode> nodeList = new ArrayList<TreeNode>();
		TreeNode root = new TreeNode(Constant.TREENODE_ROOT_ID, Constant.TREENODE_ROOT_TEXT);
		root.setState(TreeNode.STATE_OPEN);
		root.getAttributes().put("unitCode", Constant.TREENODE_ROOT_ID);//编码作为树的一个属性值
		nodeList.add(root);
		List<UnitEntity> unitList = baseDao.find("from UnitEntity where deleteFlag = ?", Constant.DELETE_FLAG_FALSE);
		if (unitList != null && unitList.size() > 0) {
			for (UnitEntity u : unitList) {
				//代表项目名称
				TreeNode node = new TreeNode(u.getId(), u.getUnitName());//ID作为树的值
				node.setParentId(u.getParentId());
				node.setState(TreeNode.STATE_OPEN);
				node.getAttributes().put("parentId", u.getParentId());//父ID作为树的一个属性值
				node.getAttributes().put("unitCode", u.getUnitCode());//编码作为树的一个属性值
				node.getAttributes().put("unitNote", u.getUnitNote());//描述信息作为树的一个属性值
				nodeList.add(node);
			}
		}
		return nodeList;
	}
	
	@Override
	public List<UnitEntity> findChildByParentId(String parentId) {
		String hql = "from UnitEntity u where u.parentId = ? and deleteFlag = ?";
		if (StringUtil.isNullOrEmpty(parentId)) {
			parentId = Constant.TREENODE_ROOT_ID;
		}
		List<UnitEntity> unitList = baseDao.find(hql, parentId, Constant.DELETE_FLAG_FALSE);
		return addIsHasChildByParentId(unitList);
	}
	
	@Override
	public List<UnitEntity> findTwoChildByParentCode(String parentCode) {
		List<Object> paramList = new ArrayList<Object>();
		StringBuilder hql = new StringBuilder("from UnitEntity u where (u.unitCode like ?");
		hql.append(" or u.unitCode like ?) and u.deleteFlag = ? order by u.unitOrder");
		StringBuilder sb = new StringBuilder();
		boolean isParentEmpty = StringUtil.isNullOrEmpty(parentCode);
		//查出两级子节点
		for (int i = 0; i < 2; i++) {
			for (int j = 0; j < Constant.CODE_LEVEL_LENGTH; j++) {
				sb.append("_");
			}
			if (isParentEmpty) {
				paramList.add(sb.toString());
			} else {
				paramList.add(parentCode + sb.toString());
			}
		}
		paramList.add(Constant.DELETE_FLAG_FALSE);
		List<UnitEntity> unitList = baseDao.find(hql.toString(), paramList.toArray());
		int len = isParentEmpty ? Constant.CODE_LEVEL_LENGTH : (parentCode.length()+Constant.CODE_LEVEL_LENGTH);
		return addIsHasChildByParentCode(unitList, len);
	}
	/**
	 * 根据两级编码分类，找出第一级子节点。
	 * @Date 2015-10-21下午11:04:19
	 * @param unitList 两级子节点的部门列表
	 * @param childCodeLen 一级子节点编码的长度
	 * @return 第一级节点部门列表
	 */
	private List<UnitEntity> addIsHasChildByParentCode(List<UnitEntity> unitList, int childCodeLen) {
		List<UnitEntity> ChildList1 = null;//一级子节点
		if (unitList != null && unitList.size() > 0) {
			ChildList1 = new ArrayList<UnitEntity>();
			List<UnitEntity> ChildList2 = new ArrayList<UnitEntity>();//二级子节点
			//第一次循环分类--找出是第一级子节点还是第二级子节点
			for (UnitEntity unit : unitList) {
				if (unit.getUnitCode().length() == childCodeLen) {
					ChildList1.add(unit);
				} else {
					ChildList2.add(unit);
				}
			}
			//设置是否有子节点并返回第一级子节点
			for (UnitEntity u2 : ChildList2) {
				String u2Code = u2.getUnitCode();
				int codeLen = u2Code.length();
				if (codeLen >= Constant.CODE_LEVEL_LENGTH) {
					String pCode = u2Code.substring(0, codeLen - Constant.CODE_LEVEL_LENGTH);
					for (UnitEntity u1 : ChildList1) {
						if (u1.getUnitCode().equals(pCode)) {
							u1.setHasChild(true);
							break;
						}
					}
				} else {
					log.error("部门编码错误："+u2.getId()+"--"+u2.getUnitName()+"--"+u2.getUnitCode());
				}
			}
		}
		return ChildList1;
	}
	/**
	 * 根据ID查询是否还有子节点
	 * @Date 2015-10-21下午11:09:51
	 * @param unitList 部门列表
	 * @return
	 */
	@SuppressWarnings("unchecked")
	private List<UnitEntity> addIsHasChildByParentId(List<UnitEntity> unitList) {
		int size = (unitList == null) ? 0 : unitList.size();
		if (size > 0) {
			List<Object> paramList = new ArrayList<Object>();
			StringBuilder hql = new StringBuilder("select u.parentId from UnitEntity u where u.parentId in ( ");
			for (int i = 0; i < size; i++) {
				if (i + 1 == size) {
					hql.append("? ");
				} else {
					hql.append("?, ");
				}
				paramList.add(unitList.get(i).getId());
			}
			hql.append(" ) group by u.parentId");
			//查询节点下是否还有子节点--parentIdList保存的是还有子节点的节点
			List<String> parentIdList = baseDao.createQuery(hql.toString(), paramList.toArray()).list();
			//存在某个子节点下还有子节点
			if (parentIdList != null && parentIdList.size() > 0) {
				for (String parentId : parentIdList) {
					for (UnitEntity unit : unitList) {
						if (unit.getId().equals(parentId)) {
							unit.setHasChild(true);//存在子节点
							break;
						}
					}
				}
			}
		}
		return unitList;
	}
	/**
	 * 判断名称是否重复，重复返回true，否则为false
	 * @Date 2015-8-12 下午01:10:13 
	 * @param id：页面ID
	 * @param name：页面名称
	 * @return 重复返回true，否则为false
	 */
	private boolean isExistName(String id, String name) {
		List<UnitEntity> unitList = this.findByName(name);
		if (unitList == null || unitList.size() == 0) {
			return false;
		}
		if ((unitList.size() == 1) && (unitList.get(0).getId().equals(id))) {
			return false;
		}
		return true;
	}
}
