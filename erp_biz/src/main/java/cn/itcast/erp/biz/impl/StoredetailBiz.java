package cn.itcast.erp.biz.impl;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import javax.annotation.Resource;
import javax.mail.MessagingException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import cn.itcast.erp.biz.IStoredetailBiz;
import cn.itcast.erp.dao.IGoodsDao;
import cn.itcast.erp.dao.IStoreDao;
import cn.itcast.erp.dao.IStoredetailDao;
import cn.itcast.erp.entity.StoreAlert;
import cn.itcast.erp.entity.Storedetail;
import cn.itcast.erp.exception.ErpException;
import cn.itcast.erp.util.MailUtil;

/**
 * 仓库库存业务逻辑类
 *
 */
@Service("storedetailBiz")
public class StoredetailBiz extends BaseBiz<Storedetail> implements IStoredetailBiz {

    private IStoredetailDao storedetailDao;
    @Autowired
    private IGoodsDao goodsDao;
    @Autowired
    private IStoreDao storeDao;
    @Resource(name="storedetailDao")
    public void setStoredetailDao(IStoredetailDao storedetailDao) {
        this.storedetailDao = storedetailDao;
        super.setBaseDao(this.storedetailDao);
    }
    @Override
    public List<Storedetail> getListByPage(Storedetail t1, Storedetail t2, Object obj, int startRow, int maxResults) {
    	List<Storedetail> list = super.getListByPage(t1, t2, obj, startRow, maxResults);
    	for (Storedetail storedetail : list) {
			storedetail.setGoodsName(goodsDao.get(storedetail.getGoodsuuid()).getName());
			storedetail.setStoreName(storeDao.get(storedetail.getStoreuuid()).getName());
		}
    	return list;
    }
	@Override
	public List<StoreAlert> getStoreAlertList() {
		return storedetailDao.getStoreAlertList();
	}
	@Autowired
	private MailUtil mailUtil;
	@Value("${mail.storealert_title}")
	private String title;
	@Value("${mail.storealert_to}")
	private String to;
	@Value("${mail.storealert_content}")
	private String content;
	@Override
	public void sendStorealertMail() throws MessagingException {
		List<StoreAlert> list = storedetailDao.getStoreAlertList();
		if (null!=list&&list.size()>0) {
			SimpleDateFormat sdf = new SimpleDateFormat("yy-MM-dd");
			String _title = title.replace("[time]", sdf.format(new Date()));
			String _content = content.replace("[count]", list.size()+"");
			mailUtil.sendMail(to, _title, _content);
		}else {
			throw new ErpException("没有需要预警的商品库存");
		}
	}
}
