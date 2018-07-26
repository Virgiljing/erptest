package cn.itcast.erp.action;

import javax.annotation.Resource;

import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import cn.itcast.erp.biz.IStoreBiz;
import cn.itcast.erp.entity.Emp;
import cn.itcast.erp.entity.Store;
import cn.itcast.erp.util.WebUtil;

@Controller("storeAction")
@Scope("prototype")
@ParentPackage("struts-default")
@Namespace("/")
@Action("store")
public class StoreAction extends BaseAction<Store> {

    private IStoreBiz storeBiz;

    @Resource(name="storeBiz")
    public void setStoreBiz(IStoreBiz storeBiz) {
        this.storeBiz = storeBiz;
        super.setBaseBiz(this.storeBiz);
    }
    public void myList() {
    	Emp loginUser = WebUtil.getLoginUser();
    	if (null == loginUser) {
			WebUtil.ajaxReturn(false, "亲！你没有登录，请登录后再审核");
			return;
		}
    	try {
			Store store = getT1();
			if (store==null) {
				store = new Store();
				setT1(store);
			}
			store.setEmpuuid(loginUser.getUuid());
			super.list();
		} catch (Exception e) {
			e.printStackTrace();
		}
    	
    }
}
