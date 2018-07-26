package cn.itcast.erp.dao;

import java.util.List;

import cn.itcast.erp.entity.StoreAlert;
import cn.itcast.erp.entity.Storedetail;

/**
 * 仓库库存数据访问接口
 *
 */
public interface IStoredetailDao extends IBaseDao<Storedetail>{
	/**
	 * 库存警告列表
	 * @return
	 */
	public List<StoreAlert> getStoreAlertList();
}
