package cn.itcast.erp.action;

import java.util.Map;

import javax.annotation.Resource;

import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import cn.itcast.erp.biz.IOrderdetailBiz;
import cn.itcast.erp.entity.Emp;
import cn.itcast.erp.entity.Orderdetail;
import cn.itcast.erp.exception.ErpException;
import cn.itcast.erp.util.WebUtil;

@Controller("orderdetailAction")
@Scope("prototype")
@ParentPackage("struts-default")
@Namespace("/")
@Action("orderdetail")
public class OrderdetailAction extends BaseAction<Orderdetail> {

    private IOrderdetailBiz orderdetailBiz;
    private Long storeuuid;
    public void setStoreuuid(Long storeuuid) {
		this.storeuuid = storeuuid;
	}
	@Resource(name="orderdetailBiz")
    public void setOrderdetailBiz(IOrderdetailBiz orderdetailBiz) {
        this.orderdetailBiz = orderdetailBiz;
        super.setBaseBiz(this.orderdetailBiz);
    }
    /**
     * 订单入库
     */
    public void doInStore() {
    	Emp loginUser = WebUtil.getLoginUser();
    	if (null == loginUser) {
			WebUtil.ajaxReturn(false, "亲！你没有登录，请登录后再入库");
			return;
		}
    	try {
    		Long empUuid = loginUser.getUuid();
			Long uuid = getId();
			orderdetailBiz.doInStore(storeuuid,uuid,empUuid);
			WebUtil.ajaxReturn(true, "入库成功");
		} catch (ErpException e) {
			e.printStackTrace();
			WebUtil.ajaxReturn(false, e.getMessage());
		} catch (Exception e) {
			e.printStackTrace();
			WebUtil.ajaxReturn(false, "未知错误");
		}
    	
    }
    /**
     * 销售明细出库
     */
    public void doOutStore() {
    	Emp loginUser = WebUtil.getLoginUser();
    	if (null == loginUser) {
    		WebUtil.ajaxReturn(false, "亲！你没有登录，请登录后再入库");
    		return;
    	}
    	try {
    		Long empUuid = loginUser.getUuid();
    		Long uuid = getId();
    		orderdetailBiz.doOutStore(storeuuid,uuid,empUuid);
    		WebUtil.ajaxReturn(true, "出库成功");
    	} catch (ErpException e) {
    		e.printStackTrace();
    		WebUtil.ajaxReturn(false, e.getMessage());
    	} catch (Exception e) {
    		e.printStackTrace();
    		WebUtil.ajaxReturn(false, "未知错误");
    	}
    	
    }
    
    /**
     * 出库
     */
    public void doOutStoreSPC() {
        // 判断用户是否登陆
        Emp loginUser = WebUtil.getLoginUser();
        if(null == loginUser) {
            WebUtil.ajaxReturn(false, "你还没有登陆");
            return;
        }
        try {
            Map<String, Object> result = orderdetailBiz.doOutStoreSPC(loginUser.getUuid(), storeuuid, getId());
            WebUtil.write(result);
        } catch (ErpException e) {
            e.printStackTrace();
            WebUtil.ajaxReturn(false, e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
            WebUtil.ajaxReturn(false, "出库失败");
        }
    }
}
