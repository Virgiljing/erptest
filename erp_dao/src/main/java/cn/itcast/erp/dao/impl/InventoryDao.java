package cn.itcast.erp.dao.impl;

import org.apache.commons.lang3.StringUtils;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import cn.itcast.erp.dao.IInventoryDao;
import cn.itcast.erp.entity.Inventory;

/**
 * 盘盈盘亏数据访问类
 *
 */
@Repository("inventoryDao")
public class InventoryDao extends BaseDao<Inventory> implements IInventoryDao {

    /**
     * 构建查询条件
     * @param t1
     * @param t2
     * @param param
     * @return
     */
    @Override
    public DetachedCriteria getDetachedCriteria(Inventory inventory1,Inventory inventory2,Object param){
        DetachedCriteria dc=DetachedCriteria.forClass(Inventory.class);
        if(inventory1!=null){
            if(!StringUtils.isEmpty(inventory1.getType())){
                dc.add(Restrictions.like("type", inventory1.getType(), MatchMode.ANYWHERE));
            }
            if(!StringUtils.isEmpty(inventory1.getState())){
                dc.add(Restrictions.like("state", inventory1.getState(), MatchMode.ANYWHERE));
            }
            if(!StringUtils.isEmpty(inventory1.getRemark())){
                dc.add(Restrictions.like("remark", inventory1.getRemark(), MatchMode.ANYWHERE));
            }

        }
        return dc;
    }
}
