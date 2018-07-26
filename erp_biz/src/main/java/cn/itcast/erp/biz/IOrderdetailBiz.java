package cn.itcast.erp.biz;
import java.util.Map;

import cn.itcast.erp.entity.Orderdetail;
/**
 * 订单明细业务逻辑层接口
 *
 */
public interface IOrderdetailBiz extends IBaseBiz<Orderdetail>{

	/**
	 * 订单入库
	 * @param storeuuid
	 * @param uuid
	 * @param empUuid 
	 */
	void doInStore(Long storeuuid, Long uuid, Long empUuid);

	/**
	 * 订单出库
	 * @param storeuuid
	 * @param uuid
	 * @param empUuid
	 */
	void doOutStore(Long storeuuid, Long uuid, Long empUuid);
	
	Map<String,Object> doOutStoreSPC(Long empuuid, Long storeuuid, Long uuid);

}

