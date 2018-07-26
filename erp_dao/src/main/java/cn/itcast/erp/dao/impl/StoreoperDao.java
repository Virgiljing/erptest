package cn.itcast.erp.dao.impl;

import java.util.Calendar;

import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;

import cn.itcast.erp.dao.IStoreoperDao;
import cn.itcast.erp.entity.Storeoper;

/**
 * 仓库操作记录数据访问类
 *
 */
@Repository("storeoperDao")
public class StoreoperDao extends BaseDao<Storeoper> implements IStoreoperDao {

    /**
     * 构建查询条件
     * @param t1
     * @param t2
     * @param param
     * @return
     */
    @Override
    public DetachedCriteria getDetachedCriteria(Storeoper storeoper1,Storeoper storeoper2,Object param){
        DetachedCriteria dc=DetachedCriteria.forClass(Storeoper.class);
        Calendar calendar = Calendar.getInstance();
        if(storeoper1!=null){
        	//操作类型
            if(!StringUtils.isEmpty(storeoper1.getType())){
                dc.add(Restrictions.eq("type", storeoper1.getType()));
            }
            //操作员
            if (!StringUtils.isEmpty(storeoper1.getEmpuuid())) {
				dc.add(Restrictions.eq("empuuid", storeoper1.getEmpuuid()));
			}
            //操作日期
            if (!StringUtils.isEmpty(storeoper1.getOpertime())) {
            	calendar.setTime(storeoper1.getOpertime());
            	calendar.set(Calendar.HOUR, 0);
            	calendar.set(Calendar.MINUTE, 0);
            	calendar.set(Calendar.SECOND, 0);
            	calendar.set(Calendar.MILLISECOND, 0);
            	dc.add(Restrictions.ge("opertime", calendar.getTime()));
            }
            //仓库
            if (!StringUtils.isEmpty(storeoper1.getStoreuuid())) {
            	
				dc.add(Restrictions.eq("storeuuid", storeoper1.getStoreuuid()));
			}
            //商品
            if (!StringUtils.isEmpty(storeoper1.getGoodsuuid())) {
				dc.add(Restrictions.eq("goodsuuid", storeoper1.getGoodsuuid()));
			}
            //数量
            if (!StringUtils.isEmpty(storeoper1.getNum())) {
				dc.add(Restrictions.ge("num", storeoper1.getNum()));
			}
        }
        if (null != storeoper2) {
			if (!StringUtils.isEmpty(storeoper2.getOpertime())) {
				calendar.setTime(storeoper2.getOpertime());
            	calendar.set(Calendar.HOUR, 23);
            	calendar.set(Calendar.MINUTE, 59);
            	calendar.set(Calendar.SECOND, 59);
            	calendar.set(Calendar.MILLISECOND, 999);
				dc.add(Restrictions.le("opertime", calendar.getTime()));
			}
			if (!StringUtils.isEmpty(storeoper2.getNum())) {
				dc.add(Restrictions.le("num", storeoper2.getNum()));
			}
		}
        return dc;
    }
}
