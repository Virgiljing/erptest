package cn.itcast.erp.action;



import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.subject.Subject;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import cn.itcast.erp.entity.Emp;
import cn.itcast.erp.util.WebUtil;
@Controller("loginAction")
@Scope("prototype")
@ParentPackage("struts-default")
@Namespace("/")
@Action("login")
public class LoginAction{
	private String username;
	private String pwd;
	public void setUsername(String username) {
		this.username = username;
	}
	public void setPwd(String pwd) {
		this.pwd = pwd;
	}
	/**
	 * 登录
	 */
	public void checkUser() {
		try {
			UsernamePasswordToken upt = new UsernamePasswordToken(username,pwd);
			Subject subject = SecurityUtils.getSubject();
			subject.login(upt);
			WebUtil.ajaxReturn(true, "登录成功");
		} catch (AuthenticationException e) {
			e.printStackTrace();
			WebUtil.ajaxReturn(false, "用户名或密码错误");
		} catch (Exception e) {
			e.printStackTrace();
			WebUtil.ajaxReturn(false, "登录失败");
		}
	}
	/**
	 * 主页显示用户名
	 */
	public void showUser() {
		Emp loginUser = WebUtil.getLoginUser();
		if (null == loginUser) {
			WebUtil.ajaxReturn(false, "用户名或密码错误");
		}else {
			WebUtil.ajaxReturn(true, loginUser.getName());
		}
	}
	/**
	 * 退出登录
	 */
	public void loginOut() {
		SecurityUtils.getSubject().logout();
	}
}
