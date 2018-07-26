package cn.itcast.erp.biz;
import java.util.List;

import cn.itcast.erp.entity.Menu;
/**
 * 菜单业务逻辑层接口
 *
 */
public interface IMenuBiz extends IBaseBiz<Menu>{
	/**
	 * 根据员工号获取菜单
	 * @param uuid
	 * @return
	 */
	List<Menu> getMenusByEmpuuid(Long uuid);
	/**
	 * 根据员工编号湖区菜单
	 * @param uuid
	 * @return
	 */
	Menu readMenusByEmpuuid(Long uuid);
}

