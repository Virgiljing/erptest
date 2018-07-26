package cn.itcast.erp.biz;
import java.io.IOException;
import java.io.OutputStream;

import cn.itcast.erp.entity.Orders;
/**
 * 订单业务逻辑层接口
 *
 */
public interface IOrdersBiz extends IBaseBiz<Orders>{

	/**
	 * 审核订单
	 * @param empUuid
	 * @param uuid
	 */
	void doCheck(Long empUuid, Long uuid);

	/**
	 * 确认订单
	 * @param empUuid
	 * @param uuid
	 */
	void doStart(Long empUuid, Long uuid);
	
	/**
	 * 到处订单为excel
	 * @param os
	 * @param uuid
	 */
	void exportById(OutputStream os,Long uuid)throws IOException;
}

