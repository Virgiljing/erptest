package cn.itcast.erp.biz;
import java.util.List;

import javax.mail.MessagingException;

import cn.itcast.erp.entity.StoreAlert;
import cn.itcast.erp.entity.Storedetail;
/**
 * 仓库库存业务逻辑层接口
 *
 */
public interface IStoredetailBiz extends IBaseBiz<Storedetail>{
	/**
	 * 库存警告列表
	 * @return
	 */
	List<StoreAlert> getStoreAlertList();

	/**
	 * 发送预警邮件
	 * @throws MessagingException 
	 */
	void sendStorealertMail() throws MessagingException;
}

