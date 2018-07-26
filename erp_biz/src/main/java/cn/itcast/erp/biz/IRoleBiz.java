package cn.itcast.erp.biz;
import java.util.List;

import cn.itcast.erp.entity.Role;
/**
 * 角色业务逻辑层接口
 *
 */
import cn.itcast.erp.entity.Tree;
public interface IRoleBiz extends IBaseBiz<Role>{
	/**
	 * 根据角色返回角色菜单
	 * @param uuid
	 * @return
	 */
	List<Tree> readRoleMenus(Long uuid);
	
	/**
	 * 更新角色菜单权限
	 * @param uuid
	 * @param ids
	 */
	void updateRoleMenus(Long uuid,String ids);
}

