package cn.itcast.erp.biz;
import java.util.List;

import cn.itcast.erp.entity.Emp;
/**
 * 员工业务逻辑层接口
 *
 */
import cn.itcast.erp.entity.Tree;
public interface IEmpBiz extends IBaseBiz<Emp>{

	/**
	 * 登录
	 * @param username
	 * @param pwd
	 * @return
	 */
	Emp findByUsernameAndPwd(String username, String pwd);

	/**
	 * 用户修改密码
	 * @param oldPwd
	 * @param newPwd
	 * @param loginUser
	 */
	void updatePwd(String oldPwd, String newPwd, Emp loginUser);

	/**
	 * 管理员设置密码
	 * @param newPwd
	 * @param id
	 */
	void updatePwd_reset(String newPwd, Long id);
	
	/**
	 * 用户角色列表
	 * @param uuid
	 * @return
	 */
	List<Tree> readEmpRolse(Long uuid);
	
	/**
	 * 用户角色更新
	 * @param uuid
	 * @param ids
	 */
	void updateEmpRolse(Long uuid,String ids);

}

