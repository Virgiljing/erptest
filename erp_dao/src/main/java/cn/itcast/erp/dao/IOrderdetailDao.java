package cn.itcast.erp.dao;

import java.util.Map;

import cn.itcast.erp.entity.Orderdetail;

/**
 * 订单明细数据访问接口
 *
 */
public interface IOrderdetailDao extends IBaseDao<Orderdetail>{
	/**
	 * 通过存储过程处理订单出库
	 * @param empuuid
	 * @param storeuuid
	 * @param uuid
	 * @return
	 */
	Map<String,Object> doOutStoreSPC(Long empuuid, Long storeuuid, Long uuid);
}
