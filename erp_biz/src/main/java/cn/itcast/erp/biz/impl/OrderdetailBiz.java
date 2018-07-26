package cn.itcast.erp.biz.impl;

import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.redsun.bos.ws.impl.IWayBillWs;

import cn.itcast.erp.biz.IOrderdetailBiz;
import cn.itcast.erp.dao.IOrderdetailDao;
import cn.itcast.erp.dao.IStoredetailDao;
import cn.itcast.erp.dao.ISupplierDao;
import cn.itcast.erp.dao.impl.StoreoperDao;
import cn.itcast.erp.entity.Orderdetail;
import cn.itcast.erp.entity.Orders;
import cn.itcast.erp.entity.Storedetail;
import cn.itcast.erp.entity.Storeoper;
import cn.itcast.erp.entity.Supplier;
import cn.itcast.erp.exception.ErpException;

/**
 * 订单明细业务逻辑类
 *
 */
@Service("orderdetailBiz")
public class OrderdetailBiz extends BaseBiz<Orderdetail> implements IOrderdetailBiz {
	@Autowired
	private IWayBillWs wayBillWs;
	@Autowired
	private ISupplierDao supplierDao;
    private IOrderdetailDao orderdetailDao;
    @Autowired
    private IStoredetailDao storedetailDao;
    @Autowired
    private StoreoperDao storeoperDao;
    @Resource(name="orderdetailDao")
    public void setOrderdetailDao(IOrderdetailDao orderdetailDao) {
        this.orderdetailDao = orderdetailDao;
        super.setBaseDao(this.orderdetailDao);
    }

	@Override
	@Transactional
	public void doInStore(Long storeuuid, Long uuid,Long empUuid) {
		Orderdetail orderdetail = get(uuid);
		if (!Orderdetail.STATE_NOT_IN.equals(orderdetail.getState())) {
			 throw new ErpException("订单该条记录已入库");
		}
		//1. 明细表orderdetail 状态修改
		orderdetail.setEndtime(new Date());
		orderdetail.setEnder(empUuid);
		orderdetail.setStoreuuid(storeuuid);
		orderdetail.setState(Orderdetail.STATE_IN);
		
		//2. 库存表storedetail 状态修改
		
		Storedetail storedetailTemp = new Storedetail();
		storedetailTemp.setGoodsuuid(orderdetail.getGoodsuuid());
		storedetailTemp.setStoreuuid(storeuuid);
		List<Storedetail> list = storedetailDao.getList(storedetailTemp, null, null);
		if (list.size()>0) {
			Storedetail storedetail = list.get(0);
			storedetail.setNum(storedetail.getNum()+orderdetail.getNum());
		}else {
			storedetailTemp.setNum(orderdetail.getNum());
			storedetailDao.add(storedetailTemp);
		}
		//3. 日志记录storeoper
		Storeoper storeoper = new Storeoper();
		storeoper.setOpertime(orderdetail.getEndtime());
		storeoper.setEmpuuid(empUuid);
		storeoper.setStoreuuid(storeuuid);
		storeoper.setGoodsuuid(orderdetail.getGoodsuuid());
		storeoper.setNum(orderdetail.getNum());
		storeoper.setType(Storeoper.TYPE_IN);
		storeoperDao.add(storeoper);
		//4. 订单表orders
		Orders orders = orderdetail.getOrders();
		Orderdetail queryParam = new Orderdetail();
		queryParam.setOrders(orders);
		queryParam.setState(Orderdetail.STATE_NOT_IN);
		Long count = orderdetailDao.getCount(queryParam, null, null);
		if (count==0) {
			orders.setEndtime(orderdetail.getEndtime());
			orders.setEnder(empUuid);
			orders.setState(Orders.STATE_END);
		}
	}

	@Override
	@Transactional
	public void doOutStore(Long storeuuid, Long uuid, Long empUuid) {
//		出库：
//		1. 明细表: 
		Orderdetail orderdetail = get(uuid);
		if (!Orderdetail.STATE_NOT_OUT.equals(orderdetail.getState())) {
//			1.1 不能重复出库
			throw new ErpException("不能重复出库");
		}
//			1.2 结束日期       系统时间
		orderdetail.setEndtime(new Date());
//			1.3 库管员         登陆用户
		orderdetail.setEnder(empUuid);
//			1.4 仓库编号       前端传过来
		orderdetail.setStoreuuid(storeuuid);
//			1.5 状态           1: 已出库
		orderdetail.setState(Orderdetail.STATE_OUT);
//		2. 库存表
		//构建查询条件
		Storedetail storedetailTemp = new Storedetail();
		storedetailTemp.setGoodsuuid(orderdetail.getGoodsuuid());
		storedetailTemp.setStoreuuid(storeuuid);
		List<Storedetail> list = storedetailDao.getList(storedetailTemp, null, null);
//			2.1 判断库存是否存在
		Long num = -1L;
		if (list.size()>0) {
			Storedetail storedetail = list.get(0);
			num = storedetail.getNum() - orderdetail.getNum();
			storedetail.setNum(num);
		}
//			2.2 如果存在库存信息
//				判断数量是否足够 库存数-明细的数量 >= 0
//				   数量充足：更新库存数量=库存数-明细的数量
//				   数量不够：报错：库存不足
		if (num < 0) {
			throw new ErpException("库存不足");
		}
//				条件: 
//					商品编号	明细里有
//					仓库编号	前端传过来
//				getList
//		    2.3 不存在库存信息
//				报错：库存不足
//		3. 商品库存变更记录
		Storeoper storeoper = new Storeoper();
//			插入记录
//		    3.1 操作员工编号    登陆用户
		storeoper.setEmpuuid(empUuid);
//			3.2 操作日期        系统时间
		storeoper.setOpertime(orderdetail.getEndtime());
//			3.3 仓库编号 前端传过来 
		storeoper.setStoreuuid(storeuuid);
//			3.4 商品编号 明细有
		storeoper.setGoodsuuid(orderdetail.getGoodsuuid());
//			3.5 数量     明细的数量
		storeoper.setNum(orderdetail.getNum());
//			3.6 操作的类型    2:出库
		storeoper.setType(Storeoper.TYPE_OUT);
		storeoperDao.add(storeoper);
//		4. 订单表
//			4.1 判断该订单下是否所有明细都出库了?
//				通过查询明细表中属于该订单的未出库的明细数量 count
//				条件: 该订单, 状态:0
		Orderdetail queryParam = new Orderdetail();
		//构建查询条件
		queryParam.setOrders(orderdetail.getOrders());
		queryParam.setState(Orderdetail.STATE_NOT_OUT);
		Long count = orderdetailDao.getCount(queryParam, null, num);
//			4.3 count = 0 
//				更新：
//					库管员     登陆用户
//					出库日期   系统时间
//					状态       1: 已出库
		if (count == 0) {
			Orders orders = orderdetail.getOrders();
			orders.setEnder(empUuid);
			orders.setEndtime(orderdetail.getEndtime());
			orders.setState(Orders.STATE_OUT);
			Supplier supplier = supplierDao.get(orders.getSupplieruuid());
			/**
			 * 在线下单预约
			 * @param userId
			 * @param toAddress
			 * @param addressee
			 * @param tele
			 * @param info
			 * @return
			 */
			Long waybillsn = wayBillWs.addWayBill(1L, supplier.getAddress(), supplier.getName(), supplier.getTele(), "--");
			orders.setWaybillsn(waybillsn);
		}
//			4.2 count > 0 不需要
	}

	@Override
	public Map<String, Object> doOutStoreSPC(Long empuuid, Long storeuuid, Long uuid) {
		return orderdetailDao.doOutStoreSPC(empuuid, storeuuid, uuid);
	}
    
}
