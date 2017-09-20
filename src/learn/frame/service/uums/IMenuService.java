package learn.frame.service.uums;

import java.util.List;

import learn.frame.common.base.IBaseService;
import learn.frame.common.easyui.TreeNode;
import learn.frame.entity.uums.MenuEntity;

/**
 * 菜单service
 */
public interface IMenuService extends IBaseService<MenuEntity> {
	/**
	 * 根据菜单编码生成树形结构--全部加载数据，分项目加载菜单
	 * @Date 2015-10-21下午8:54:39
	 * @param menu
	 * @return
	 */
	public List<TreeNode> findForCodeTree(MenuEntity menu);
	/**
	 * 根据用户拥有的菜单编码权限查询所有菜单，菜单编码3位一级
	 * @Date 2015-10-21下午8:55:02
	 * @param menu
	 * @return
	 */
	public List<MenuEntity> findPrivilMenus(MenuEntity menu);
	/**
	 * 通过菜单编码删除，包括子菜单
	 * @Date 2015-10-21下午9:02:01
	 * @param menuCode
	 */
	public void deleteByCode(String menuCode);
	/**
	 * 得到新增菜单时的编码值
	 * @Date 2016-3-27下午10:10:30
	 * @param parentCode 父级菜单编码
	 * @return
	 */
	public String generateNewCode(String parentCode);
	/**
	 * 通过角色ID查询菜单树信息
	 * @Date 2016-5-4下午10:33:55
	 * @param roleId 角色ID
	 * @return
	 */
	public List<TreeNode> findForCodeTree(String roleId);
	/**
	 * 通过多个角色ID数组查询所有菜单信息
	 * @Date 2016-5-4下午10:42:32
	 * @param roleIds 角色ID数组
	 * @return
	 */
	public List<MenuEntity> findByRoleIds(String... roleIds);
	/**
	 * 通过菜单名称查询菜单信息
	 * @Date 2016-8-6下午11:14:39
	 * @param menuName 菜单名称
	 * @return
	 */
	List<MenuEntity> findByName(String menuName);
	/**
	 * 根据父编码生成排序号。如果父编码为为空则生成第一级最大排序号，否则生成该父编码下第一级子节点的最大排序号
	 * @Date 2016-8-20下午11:47:21
	 * @param pcode 父编码
	 * @return 排序号
	 */
	int generateOrder(String pcode);
	/**
	 * 通过菜单标示查询菜单信息
	 * @Date 2016-8-21下午10:11:36
	 * @param menuMark 菜单标示
	 * @return
	 */
	List<MenuEntity> findByMenuMark(String menuMark);
}
