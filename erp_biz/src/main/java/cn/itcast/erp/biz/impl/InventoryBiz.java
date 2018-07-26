package cn.itcast.erp.biz.impl;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import cn.itcast.erp.biz.IInventoryBiz;
import cn.itcast.erp.dao.IInventoryDao;
import cn.itcast.erp.entity.Inventory;

/**
 * 盘盈盘亏业务逻辑类
 *
 */
@Service("inventoryBiz")
public class InventoryBiz extends BaseBiz<Inventory> implements IInventoryBiz {

    private IInventoryDao inventoryDao;

    @Resource(name="inventoryDao")
    public void setInventoryDao(IInventoryDao inventoryDao) {
        this.inventoryDao = inventoryDao;
        super.setBaseDao(this.inventoryDao);
    }
    
}
