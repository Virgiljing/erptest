package cn.itcast.erp.action;

import java.util.List;

import javax.annotation.Resource;
import javax.mail.MessagingException;

import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import cn.itcast.erp.biz.IStoredetailBiz;
import cn.itcast.erp.entity.StoreAlert;
import cn.itcast.erp.entity.Storedetail;
import cn.itcast.erp.exception.ErpException;
import cn.itcast.erp.util.WebUtil;

@Controller("storedetailAction")
@Scope("prototype")
@ParentPackage("struts-default")
@Namespace("/")
@Action("storedetail")
public class StoredetailAction extends BaseAction<Storedetail> {
	private static final Logger log = LoggerFactory.getLogger(StoredetailAction.class); 
    private IStoredetailBiz storedetailBiz;

    @Resource(name="storedetailBiz")
    public void setStoredetailBiz(IStoredetailBiz storedetailBiz) {
        this.storedetailBiz = storedetailBiz;
        super.setBaseBiz(this.storedetailBiz);
    }
    public void getStoreAlertList() {
    	List<StoreAlert> alertList = storedetailBiz.getStoreAlertList();
    	WebUtil.write(alertList);
    }
    public void sendStorealertMail() {
    	try {
			storedetailBiz.sendStorealertMailTemplate();
			WebUtil.ajaxReturn(true, "预警邮件发送成功");
		} catch (MessagingException e) {
			log.error("预警邮件发送失败",e);
			WebUtil.ajaxReturn(false, "预警邮件发送失败");
		}catch (ErpException e) {
			log.error(e.getMessage(),e);
			WebUtil.ajaxReturn(false, e.getMessage());
		}catch (Exception e) {
			log.error("未知错误请联系后台管理人员",e);
			WebUtil.ajaxReturn(false, "未知错误请联系后台管理人员");
		}
    }
}
