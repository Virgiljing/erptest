package cn.itcast.erp.action;

import javax.annotation.Resource;

import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import cn.itcast.erp.biz.IInventoryBiz;
import cn.itcast.erp.entity.Inventory;

@Controller("inventoryAction")
@Scope("prototype")
@ParentPackage("struts-default")
@Namespace("/")
@Action("inventory")
public class InventoryAction extends BaseAction<Inventory> {

    private IInventoryBiz inventoryBiz;

    @Resource(name="inventoryBiz")
    public void setInventoryBiz(IInventoryBiz inventoryBiz) {
        this.inventoryBiz = inventoryBiz;
        super.setBaseBiz(this.inventoryBiz);
    }
}
