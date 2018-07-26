package cn.itcast.erp.action;

import java.util.List;

import javax.annotation.Resource;

import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import com.alibaba.fastjson.JSON;

import cn.itcast.erp.biz.IMenuBiz;
import cn.itcast.erp.entity.Emp;
import cn.itcast.erp.entity.Menu;
import cn.itcast.erp.util.WebUtil;

@Controller("menuAction")
@Scope("prototype")
@ParentPackage("struts-default")
@Namespace("/")
@Action("menu")
public class MenuAction extends BaseAction<Menu> {

    private IMenuBiz menuBiz;

    @Resource(name="menuBiz")
    public void setMenuBiz(IMenuBiz menuBiz) {
        this.menuBiz = menuBiz;
        super.setBaseBiz(this.menuBiz);
    }
    public void getMenu() {
    	Menu menu = menuBiz.get("0");
    	String jsonString = JSON.toJSONString(menu, true);
    	System.out.println();
    	WebUtil.write(jsonString);
    }
    public void getMenuByEmpuuid() {
    	Emp loginUser = WebUtil.getLoginUser();
    	if (null == loginUser) {
			return;
		}
    	List<Menu> list = menuBiz.getMenusByEmpuuid(loginUser.getUuid());
    	WebUtil.write(list);
    }
    public void getMenuTree() {
    	Emp loginUser = WebUtil.getLoginUser();
    	if (null == loginUser) {
    		return;
    	}
    	Menu menu = menuBiz.readMenusByEmpuuid(loginUser.getUuid());
    	WebUtil.write(menu);
    }
}
