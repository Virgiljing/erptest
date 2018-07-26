package cn.itcast.erp.biz.impl;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import cn.itcast.erp.biz.IReturnordersBiz;
import cn.itcast.erp.dao.IReturnordersDao;
import cn.itcast.erp.entity.Returnorders;

/**
 * 退货订单业务逻辑类
 *
 */
@Service("returnordersBiz")
public class ReturnordersBiz extends BaseBiz<Returnorders> implements IReturnordersBiz {

    private IReturnordersDao returnordersDao;

    @Resource(name="returnordersDao")
    public void setReturnordersDao(IReturnordersDao returnordersDao) {
        this.returnordersDao = returnordersDao;
        super.setBaseDao(this.returnordersDao);
    }
    
}
