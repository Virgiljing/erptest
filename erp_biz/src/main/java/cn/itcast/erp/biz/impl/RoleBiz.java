package cn.itcast.erp.biz.impl;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.itcast.erp.biz.IRoleBiz;
import cn.itcast.erp.dao.IMenuDao;
import cn.itcast.erp.dao.IRoleDao;
import cn.itcast.erp.entity.Emp;
import cn.itcast.erp.entity.Menu;
import cn.itcast.erp.entity.Role;
import cn.itcast.erp.entity.Tree;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

/**
 * 角色业务逻辑类
 *
 */
@Service("roleBiz")
public class RoleBiz extends BaseBiz<Role> implements IRoleBiz {
    private IRoleDao roleDao;
	@Autowired
    private IMenuDao menuDao;
	@Autowired
	private JedisPool jedisPool;
    @Resource(name="roleDao")
    public void setRoleDao(IRoleDao roleDao) {
        this.roleDao = roleDao;
        super.setBaseDao(this.roleDao);
    }

	@Override
	public List<Tree> readRoleMenus(Long uuid) {
		List<Tree> result = new ArrayList<>();
		Role role = roleDao.get(uuid);
		List<Menu> menus = role.getMenus();
		Menu root = menuDao.get("0");
		for(Menu l1menu:root.getMenus()) {
			Tree t1 = createTree(l1menu);
			for(Menu l2menu : l1menu.getMenus()) {
				Tree t2 = createTree(l2menu);
				t1.getChildren().add(t2);
				if (menus.contains(l2menu)) {
					t2.setChecked(true);
				}
			}
			result.add(t1);
		}
		return result;
	}
	
	private Tree createTree(Menu menu) {
		Tree t = new Tree();
		t.setText(menu.getMenuname());
		t.setId(menu.getMenuid());
		t.setChildren(new ArrayList<Tree>());
		return t;
	}

	@Override
	@Transactional
	public void updateRoleMenus(Long uuid, String ids) {
		
		Role role = roleDao.get(uuid);
		role.setMenus(new ArrayList<Menu>());
		String[] menuIds = ids.split(",");
		for (String menuid : menuIds) {
			Menu menu = menuDao.get(menuid);
			role.getMenus().add(menu);
		}
		Jedis jedis = null;
		try {
			jedis = jedisPool.getResource();
			List<Emp> emps = role.getEmps();
			for (Emp emp : emps) {
				jedis.del(Menu.MENUS_KEY+emp.getUuid());
			}
		} catch (Exception e) {
			e.printStackTrace();
			if (null != jedis) {
				jedis.close();
			}
		}
		
	}
    
}
