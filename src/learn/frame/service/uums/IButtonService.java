package learn.frame.service.uums;

import java.util.List;

import learn.frame.common.base.IBaseService;
import learn.frame.entity.uums.ButtonEntity;

/**
 * 按钮service
 * @Date 2016-5-1 下午4:18:24
 */
public interface IButtonService extends IBaseService<ButtonEntity>  {

	/**
	 * 通过菜单编码查询
	 * @Date 2016-5-2上午12:30:05
	 * @param menuCode 菜单编码
	 * @return
	 */
	List<ButtonEntity> findByMenuCode(String menuCode);

	/**
	 * 通过菜单编码删除，包括子菜单
	 * @Date 2016-5-2下午7:27:31
	 * @param menuCode 菜单编码
	 */
	void deleteByMenuCode(String menuCode);

}
