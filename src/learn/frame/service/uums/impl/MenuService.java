package learn.frame.service.uums.impl;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import learn.frame.common.Constant;
import learn.frame.common.MessageConstant;
import learn.frame.common.Page;
import learn.frame.common.dao.IBaseDaoHibernate;
import learn.frame.common.dao.impl.BaseDaoHibernate;
import learn.frame.common.easyui.TreeNode;
import learn.frame.entity.uums.MenuEntity;
import learn.frame.exception.ServiceException;
import learn.frame.service.uums.IButtonService;
import learn.frame.service.uums.IMenuService;
import learn.frame.service.uums.IRoleMenuRelService;
import learn.frame.utils.CodeUtil;
import learn.frame.utils.StringUtil;

import org.hibernate.SessionFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 菜单service
 * @Date 2016-8-6下午11:07:49
 */
@Service
@Transactional(readOnly=true)
public class MenuService implements IMenuService {
	
	private IBaseDaoHibernate<MenuEntity, String> baseDao;//dao
	
	@Resource
	public void setSessionFactory(SessionFactory sessionFactory){
		baseDao = new BaseDaoHibernate<MenuEntity, String>(sessionFactory, 
				MenuEntity.class);
	}
	
	@Resource
	private IRoleMenuRelService roleMenuRelService;
	@Resource
	private IButtonService buttonService;

	@Override
	public MenuEntity findById(String id) {
		if (StringUtil.isNullOrEmpty(id)) {
			return null;
		}
		return baseDao.get(id);
	}

	@Override
	public List<TreeNode> findForCodeTree(MenuEntity menu) {
		List<TreeNode> nodeList = new ArrayList<TreeNode>();
		TreeNode root = new TreeNode(Constant.TREENODE_ROOT_ID, Constant.TREENODE_ROOT_TEXT);
		root.setState(TreeNode.STATE_OPEN);
		root.getAttributes().put("menuId", Constant.TREENODE_ROOT_ID);//ID作为树的一个属性值
		nodeList.add(root);
		List<MenuEntity> menuList = findPrivilMenus(menu);
		if (menuList != null && menuList.size() > 0) {
			for (MenuEntity m : menuList) {
				String code = m.getMenuCode();
				//代表的是项目编号
				if (!m.getMenuProjectCode().equals(code)) {
					int codeLen = code.length();//得到编码的长度
					//如果编码不规范，则忽略该行数据，防止报错
					if (codeLen >= Constant.CODE_LEVEL_LENGTH) {
						TreeNode node = new TreeNode(code, m.getMenuName());//编码code作为树的值
						//根节点
						if (codeLen == Constant.CODE_LEVEL_LENGTH) {
							node.setParentId(Constant.TREENODE_ROOT_ID);//编码长度为2的话设置其父节点ID为项目代号
						} else {
							node.setParentId(code.substring(0, codeLen - Constant.CODE_LEVEL_LENGTH));//设置父节点
						}
						node.setState(TreeNode.STATE_OPEN);
						node.getAttributes().put("menuId", m.getId());//ID作为树的一个属性值
						node.getAttributes().put("menuType", m.getMenuType());//菜单类型作为树的一个属性值
						nodeList.add(node);
					} else {
						log.error("菜单编码错误："+m.getId()+"--"+m.getMenuName()+"--"+m.getMenuCode());
					}
				}
			}
		}
		return nodeList;
	}
	
	@Override
	@Transactional(readOnly=false)
	public void deleteByCode(String menuCode) {
		if (!StringUtil.isNullOrEmpty(menuCode)) {
			try {
				//删除角色菜单关联
				roleMenuRelService.deleteByMenuCode(menuCode);
				//删除按钮
				buttonService.deleteByMenuCode(menuCode);
				//删除菜单
				String hql = "delete from MenuEntity m where m.menuCode like ?";
				baseDao.executeHql(hql, menuCode+"%");
			} catch (Exception e) {
				log.error(e);
				throw new ServiceException(MessageConstant.DB_IO_ERROR);
			}
		}
	}
	
	@Override
	public void delete(String id) {
		
	}

	@Override
	@Transactional(readOnly=false)
	public MenuEntity saveOrUpdate(MenuEntity t) {
		if (t != null) {
			if (StringUtil.isNullOrEmpty(t.getMenuName())) {
				throw new ServiceException("菜单名称不能为空");//菜单名称允许重复
			}
			try {
				if (isExistMenuMark(t.getId(), t.getMenuMark())) {
					throw new ServiceException("菜单标示不能重复");
				}
				//新增时生成编码
				if (StringUtil.isNullOrEmpty(t.getId())) {
					t.setMenuCode(generateNewCode(t.getMenuCode()));
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
	public Page<MenuEntity> findByPage(Page<MenuEntity> p, MenuEntity t) {
		return null;
	}
	
	@Override
	public List<MenuEntity> findByAll(MenuEntity t) {
		return null;
	}
	
	@Override
	public List<MenuEntity> findByName(String menuName) {
		if (StringUtil.isNullOrEmpty(menuName)) {
			return null;
		}
		return baseDao.find("from MenuEntity where menuName = ?", menuName);
	}

	@Override
	public List<MenuEntity> findPrivilMenus(MenuEntity menu) {
		//没有菜单权限
		if (menu == null) {
			return null;
		}
		String privilCodes = menu.getPrivilMenuCodes();
		String[] codes = null;
		if (!StringUtil.isNullOrEmpty(privilCodes)) {
			codes = privilCodes.split(",");
		}
		int size = (codes == null) ? 0 : codes.length;
		//没有菜单权限
		if (size < 1) {
			return null;
		}
		List<Object> paramList = new ArrayList<Object>();
		StringBuilder hql = new StringBuilder("from MenuEntity m where 1=1");
		//不是超级管理员，超级管理员拥有所有权限，其权限编码为-1
		if (!String.valueOf(Constant.ADMIN_ROLE_LEVEL).equals(codes[0])) {
			hql.append(" and m.menuCode in ( ");
			for (int i = 0; i < size; i++) {
				if (i+1 == size) {
					hql.append(" ?)");
				} else {
					hql.append(" ?,");
				}
				paramList.add(codes[i]);
			}
		}
		if (!StringUtil.isNullOrEmpty(menu.getMenuProjectCode())) {
			hql.append(" and m.menuProjectCode = ?");
			paramList.add(menu.getMenuProjectCode());
		}
		if (menu.getIsEnable() != null) {
			hql.append(" and m.isEnable = ?");
			paramList.add(menu.getIsEnable());
		}
		if (menu.getMenuType() != null) {
			hql.append(" and m.menuType = ?");
			paramList.add(menu.getMenuType());
		}
		hql.append(" order by m.menuOrder");
		return baseDao.find(hql.toString(), paramList.toArray());
	}
	
	@Override
	public List<TreeNode> findForCodeTree(String roleId) {
		List<TreeNode> nodeList = new ArrayList<TreeNode>();
		TreeNode root = new TreeNode(Constant.TREENODE_ROOT_ID, Constant.TREENODE_ROOT_TEXT);
		root.setState(TreeNode.STATE_OPEN);
		root.getAttributes().put("menuId", Constant.TREENODE_ROOT_ID);//ID作为树的一个属性值
		nodeList.add(root);
		List<MenuEntity> menuList = this.findByRoleIds(roleId);
		if (menuList != null && menuList.size() > 0) {
			for (MenuEntity m : menuList) {
				String code = m.getMenuCode();
				//代表的是项目编号
				if (!m.getMenuProjectCode().equals(code)) {
					int codeLen = code.length();//得到编码的长度
					//如果编码不规范，则忽略该行数据，防止报错
					if (codeLen >= Constant.CODE_LEVEL_LENGTH) {
						TreeNode node = new TreeNode(code, m.getMenuName());//编码code作为树的值
						//根节点
						if (codeLen == Constant.CODE_LEVEL_LENGTH) {
							node.setParentId(Constant.TREENODE_ROOT_ID);//编码长度为2的话设置其父节点ID为项目代号
						} else {
							node.setParentId(code.substring(0, codeLen - Constant.CODE_LEVEL_LENGTH));//设置父节点
						}
						node.setState(TreeNode.STATE_OPEN);
						node.getAttributes().put("menuId", m.getId());//ID作为树的一个属性值
						node.getAttributes().put("menuIcon", m.getMenuIcon());//图标路径作为树的一个属性值
						node.getAttributes().put("menuType", m.getMenuType());//菜单类型作为树的一个属性值
						nodeList.add(node);
					} else {
						log.error("菜单编码错误："+m.getId()+"--"+m.getMenuName()+"--"+m.getMenuCode());
					}
				}
			}
		}
		return nodeList;
	}
	
	@Override
	public List<MenuEntity> findByRoleIds(String... roleIds) {
		StringBuilder hql = new StringBuilder("select distinct menu");
		hql.append(" from MenuEntity menu, RoleMenuRelEntity rmr");
		hql.append(" where menu.menuCode=rmr.menuCode and rmr.roleId in (");
		int size = roleIds.length;
		for (int i = 0; i < size; i++) {
			if (i+1 == size) {
				hql.append(" ?)");
			} else {
				hql.append(" ?,");
			}
		}
		return baseDao.find(hql.toString(), roleIds);
	}
	
	@Override
	public String generateNewCode(String parentCode) {
		String hql = "select max(menuCode) from MenuEntity where menuCode like ?";
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
		String maxChildCode = (String)baseDao.findUnique(hql, param);
		return CodeUtil.generateCode(parentCode, maxChildCode);
	}
	
	@Override
	public int generateOrder(String pcode) {
		StringBuilder hql = new StringBuilder("select max(menuOrder) from MenuEntity");
		hql.append(" where menuCode like ");
		StringBuilder codeLevel = new StringBuilder();
		for (int i = 0; i < Constant.CODE_LEVEL_LENGTH; i++) {
			codeLevel.append("_");
		}
		//查询父编码下第一级子编码的最大排序号
		if (StringUtil.isNullOrEmpty(pcode)) {
			hql.append(" '"+codeLevel.toString()+"'");
		} else {
			hql.append(" '"+pcode+codeLevel.toString()+"'");
		}
		Integer maxOrder = (Integer)baseDao.findUnique(hql.toString());
		int order = (maxOrder == null) ? 1 : maxOrder+1;
		//最大排序号不能超过999999
		if (order > Constant.MAX_ORDER) {
			order = Constant.MAX_ORDER;
		}
		return order;
	}
	
	@Override
	public List<MenuEntity> findByMenuMark(String menuMark) {
		if (StringUtil.isNullOrEmpty(menuMark)) {
			return null;
		}
		return baseDao.find("from MenuEntity where menuMark = ?", menuMark);
	}
	
	/**
	 * 判断菜单标示是否重复，重复返回true，否则为false
	 * @Date 2015-8-12 下午01:10:13 
	 * @param id 页面ID
	 * @param menuMark 菜单唯一标示
	 * @return 重复返回true，否则为false
	 */
	private boolean isExistMenuMark(String id, String menuMark) {
		List<MenuEntity> menuList = findByMenuMark(menuMark);
		if (menuList == null || menuList.size() == 0) {
			return false;
		}
		if ((menuList.size() == 1) && (menuList.get(0).getId().equals(id))) {
			return false;
		}
		return true;
	}
}
