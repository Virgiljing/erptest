package cn.itcast.erp.dao;

import cn.itcast.erp.entity.Emp;

/**
 * 员工数据访问接口
 *
 */
public interface IEmpDao extends IBaseDao<Emp>{

	Emp findByUsernameAndPwd(String username, String pwd);

	void updatePwd(String newPwd, Long uuid);

}
