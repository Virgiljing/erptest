package cn.itcast.erp.dao.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.hibernate.SessionFactory;
import org.springframework.orm.hibernate5.support.HibernateDaoSupport;
import org.springframework.stereotype.Repository;

import cn.itcast.erp.dao.IReportDao;

@SuppressWarnings("unchecked")
@Repository
public class ReportDao extends HibernateDaoSupport implements IReportDao {
	@Resource(name="sessionFactory")
	public void setSf(SessionFactory sf) {
	    super.setSessionFactory(sf);
	}
	@Override
	public List<Map<String, Object>> orderReport(Date startDate, Date endDate) {
		List<Date> list = new ArrayList<>();
		String hql = "select new Map(gt.name as name,sum(od.money) as y) from " + 
				"Orderdetail od ,Orders o,Goods g,Goodstype gt " + 
				"where od.orders=o and o.type=2 " + 
				"and  g.uuid=od.goodsuuid and " + 
				"g.goodstype=gt ";
		if (null != startDate) {
			hql += "and od.endtime>=? ";
			list.add(startDate);
		}
		if (null != endDate) {
			hql += "and od.endtime<=? ";
			list.add(endDate);
		}
		hql += "group by gt.name";
		return (List<Map<String, Object>>) this.getHibernateTemplate().find(hql, list.toArray());
	}
	@Override
	public Map<String, Object> trendReport(int year,int month) {
		String hql = "select new Map(month(o.createtime) as name,sum(od.money) as y) "
				+ "from Orderdetail od,Orders o " 
				+ "where od.orders=o "
				+ "and o.type=2 and "
				+ "year(o.createtime)=? " 
				+ "and month(o.createtime)=? "
				+ "group by month(o.createtime)";
		List<Map<String, Object>> list = (List<Map<String, Object>>) this.getHibernateTemplate().find(hql,year,month);
		if (list != null && list.size()>0) {
			return list.get(0);
		}
		return null;
	}

}
