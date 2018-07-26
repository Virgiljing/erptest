package cn.itcast.erp.realm;

import java.util.List;

import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.springframework.beans.factory.annotation.Autowired;

import com.alibaba.fastjson.JSON;

import cn.itcast.erp.biz.IEmpBiz;
import cn.itcast.erp.biz.IMenuBiz;
import cn.itcast.erp.entity.Emp;
import cn.itcast.erp.entity.Menu;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

public class ErpRealm extends AuthorizingRealm{
	@Autowired
	private IEmpBiz empBiz;
	@Autowired
	private IMenuBiz menuBiz;
	@Autowired
	private JedisPool jedisPool;
	/**
	 * 授权方法
	 */
	@Override
	protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {
		Emp emp = (Emp) principals.getPrimaryPrincipal();
		Jedis jedis = null;
		String menus = null;
		List<Menu> menusByEmpuuid = null;
		String key = Menu.MENUS_KEY + emp.getUuid();
		try {
			jedis = jedisPool.getResource();
			menus = jedis.get(key);
			if (null == menus) {
				menusByEmpuuid = menuBiz.getMenusByEmpuuid(emp.getUuid());
				menus = JSON.toJSONString(menusByEmpuuid);
				jedis.set(key, menus);
			}
		} catch (Exception e) {
			e.printStackTrace();
			if (jedis != null) {
				jedis.close();
			}
		}
		System.out.println(key);
		if (null == menus) {
			menusByEmpuuid = menuBiz.getMenusByEmpuuid(emp.getUuid());
		}else {
			menusByEmpuuid = JSON.parseArray(menus, Menu.class);
		}
		SimpleAuthorizationInfo info = new SimpleAuthorizationInfo();
		for (Menu menu : menusByEmpuuid) {
			info.addStringPermission(menu.getMenuname());
		}
		return info;
	}

	/**
	 * 认证方法
	 */
	@Override
	protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken token) throws AuthenticationException {
		UsernamePasswordToken upt = (UsernamePasswordToken)token;
		char[] password = upt.getPassword();
		String pwd = new String(password);
		String username = upt.getUsername();
		Emp emp = empBiz.findByUsernameAndPwd(username, pwd);
		if (emp != null) {
			return new SimpleAuthenticationInfo(emp,pwd,getName());
		}
		return null;
	}

}
