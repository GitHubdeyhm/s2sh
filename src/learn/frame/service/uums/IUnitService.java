package learn.frame.service.uums;

import java.util.List;

import learn.frame.common.base.IBaseService;
import learn.frame.common.easyui.TreeNode;
import learn.frame.entity.uums.UnitEntity;

/**
 * 部门service
 * @Date 2016-8-6下午9:59:50
 */
public interface IUnitService extends IBaseService<UnitEntity> {
	/**
	 * 通过编码封装成树形结构，以编码作为树的值，一次性加载所有数据
	 * @Date 2015-10-21下午9:15:22
	 * @return
	 */
	public List<TreeNode> findForCodeTree();
	/**
	 * 根据编码删除部门--删除部门会删除该部门下的所有子部门，逻辑删除
	 * @Date 2015-10-21下午9:15:47
	 * @param unitCode 部门编码
	 */
	public void deleteByCode(String unitCode);
	/**
	 * 新增时得到新的部门编码
	 * @Date 2015-10-21下午9:16:15
	 * @param parentCode 父级部门编码
	 * @return
	 */
	public String generateNewCode(String parentCode);
	/**
	 * 封装成树形结构，以ID作为树的值，通过编码异步加载树形控件。
	 * 	即点击节点后加载下一级节点，默认展开第一级节点
	 * @param id：树的值
	 * @return
	 */
	public List<TreeNode> findForIdLazyTree(String id);
	/**
	 * 通过ID封装成树形结构，以ID作为树的值，一次性加载所有数据
	 * @Date 2015-10-21下午9:16:49
	 * @return
	 */
	public List<TreeNode> findForIdTree();
	/**
	 * 根据父编码查询两级子节点，返回第一级子节点。
	 * @Date 2015-10-21下午11:06:49
	 * @param parentCode 父级编码
	 * @return
	 */
	public List<UnitEntity> findTwoChildByParentCode(String parentCode);
	/**
	 * 通过部门名称查询列表信息
	 * @Date 2016-8-6下午10:03:46
	 * @param name 部门名称
	 * @return
	 */
	List<UnitEntity> findByName(String name);
	/**
	 * 通过父ID查询子节点
	 * @Date 2015-10-24下午2:22:05
	 * @param parentId 父ID
	 * @return
	 */
	List<UnitEntity> findChildByParentId(String parentId);
	/**
	 * 通过父编码查询所有未删除的子节点
	 * @Date 2015-10-24下午2:31:43
	 * @param parentCode 部门编码
	 * @return
	 */
	List<UnitEntity> findAllChildByParentCode(String parentCode);
	/**
	 * 通过多个编码查询部门信息
	 * @Date 2015-10-24下午2:36:45
	 * @param codeList 编码列表
	 * @return
	 */
	List<UnitEntity> findByCode(List<String> codeList);
	/**
	 * 封装成树形结构，以父编码作为树的值，通过编码异步加载树形控件。
	 * 	即点击节点后加载下一级节点，默认展开第一级节点
	 * @Date 2015-10-24下午6:51:05
	 * @param parentCode 父级编码
	 * @return
	 */
	List<TreeNode> findForCodeLazyTree(String parentCode);
	/**
	 * 此方法是针对树形结构，查询最末级节点即叶子节点。
	 * <p>参数parentCode的意思是：为null代表查询所有叶子节点，不为null代表查询该节点下的所有叶子节点<p>
	 * @Date 2015-10-24下午6:51:05
	 * @param parentCode：父级编码
	 * @return
	 */
	List<UnitEntity> findLeafUnit(String parentCode);
	/**
	 * 新增时获取最大排序号
	 * @Date 2016-8-7上午11:26:08
	 * @return 最大排序号
	 */
	int generateNewOrder();

}
