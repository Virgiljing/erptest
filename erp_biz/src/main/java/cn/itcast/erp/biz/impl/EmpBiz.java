package cn.itcast.erp.biz.impl;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import org.apache.shiro.crypto.hash.Md5Hash;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.itcast.erp.biz.IEmpBiz;
import cn.itcast.erp.dao.IEmpDao;
import cn.itcast.erp.dao.IRoleDao;
import cn.itcast.erp.entity.Emp;
import cn.itcast.erp.entity.Menu;
import cn.itcast.erp.entity.Role;
import cn.itcast.erp.entity.Tree;
import cn.itcast.erp.exception.ErpException;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

/**
 * 员工业务逻辑类
 *
 */
@Service("empBiz")
public class EmpBiz extends BaseBiz<Emp> implements IEmpBiz {

    private IEmpDao empDao;
    /**散列次数*/
    private int hashIterations = 3;
    @Autowired
    private JedisPool jedisPool;
    @Resource(name="empDao")
    public void setEmpDao(IEmpDao empDao) {
        this.empDao = empDao;
        super.setBaseDao(this.empDao);
    }
    @Autowired
    private IRoleDao roleDao;
	@Override
	public Emp findByUsernameAndPwd(String username, String pwd) {
		String pwd2 = md5HashPwd(pwd,username);
		System.out.println("所得md5密码"+pwd2);
		return empDao.findByUsernameAndPwd(username,pwd2);
	}
	@Override
	@Transactional
	public void add(Emp emp) {
		String pwd2 = md5HashPwd(emp.getUsername(), emp.getUsername());
		emp.setPwd(pwd2);
		super.add(emp);
	}
    
	private String md5HashPwd(String pwd,String name) {
		Md5Hash md5 = new Md5Hash(pwd, name, hashIterations);
		return md5.toString();
	}

	@Override
	@Transactional
	public void updatePwd(String oldPwd, String newPwd, Emp loginUser) {
		String pwd = loginUser.getPwd();
		String oldHashPwd = md5HashPwd(oldPwd,loginUser.getUsername());
		if (!oldHashPwd.equals(pwd)) {
			throw new ErpException("原密码不正确");
		}
		String hashPwd = md5HashPwd(newPwd,loginUser.getUsername());
		empDao.updatePwd(hashPwd,loginUser.getUuid());
	}

	@Override
	@Transactional
	public void updatePwd_reset(String newPwd, Long id) {
		Emp emp = empDao.get(id);
		String hashPwd = md5HashPwd(newPwd,emp.getUsername());
		empDao.updatePwd(hashPwd, id);
	}

	@Override
	public List<Tree> readEmpRolse(Long uuid) {
		List<Tree> result = new ArrayList<>();
		Emp emp = empDao.get(uuid);
		List<Role> list = roleDao.getList(null, null, null);
		List<Role> roles = emp.getRoles();
		for (Role role : list) {
			Tree tree = new Tree();
			tree.setId(role.getUuid()+"");
			tree.setText(role.getName());
			if (roles.contains(role)) {
				tree.setChecked(true);
			}
			result.add(tree);
		}
		return result;
	}

	@Override
	@Transactional
	public void updateEmpRolse(Long uuid, String ids) {
		Emp emp = empDao.get(uuid);
		emp.setRoles(new ArrayList<Role>());
		String[] split = ids.split(",");
		for (String roleuuid : split) {
			Role role = roleDao.get(Long.parseLong(roleuuid));
			emp.getRoles().add(role);
		}
		Jedis jedis = null;
		try {
			jedis = jedisPool.getResource();
			jedis.del(Menu.MENUS_KEY+emp.getUuid());
		} catch (Exception e) {
			e.printStackTrace();
			jedis.close();
		}
	}
}
