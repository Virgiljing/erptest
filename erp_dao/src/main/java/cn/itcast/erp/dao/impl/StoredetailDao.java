package cn.itcast.erp.dao.impl;

import java.util.List;

import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;

import cn.itcast.erp.dao.IStoredetailDao;
import cn.itcast.erp.entity.StoreAlert;
import cn.itcast.erp.entity.Storedetail;

/**
 * 仓库库存数据访问类
 *
 */
@Repository("storedetailDao")
@SuppressWarnings("unchecked")
public class StoredetailDao extends BaseDao<Storedetail> implements IStoredetailDao {

    /**
     * 构建查询条件
     * @param t1
     * @param t2
     * @param param
     * @return
     */
    @Override
    public DetachedCriteria getDetachedCriteria(Storedetail storedetail1,Storedetail storedetail2,Object param){
        DetachedCriteria dc=DetachedCriteria.forClass(Storedetail.class);
        if(storedetail1!=null){
        	if (!StringUtils.isEmpty(storedetail1.getGoodsuuid())) {
        		dc.add(Restrictions.eq("goodsuuid", storedetail1.getGoodsuuid()));
			}
        	if (!StringUtils.isEmpty(storedetail1.getStoreuuid())) {
        		dc.add(Restrictions.eq("storeuuid", storedetail1.getStoreuuid()));
			}
        }
        return dc;
    }

	
	@Override
	public List<StoreAlert> getStoreAlertList() {
		String hql = "from StoreAlert where storenum<outnum ";
		return (List<StoreAlert>) this.getHibernateTemplate().find(hql);
	}
}
