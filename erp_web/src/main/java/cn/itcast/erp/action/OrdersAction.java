package cn.itcast.erp.action;

import java.io.IOException;
import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts2.ServletActionContext;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import com.alibaba.fastjson.JSON;
import com.redsun.bos.ws.Waybilldetail;
import com.redsun.bos.ws.impl.IWayBillWs;

import cn.itcast.erp.biz.IOrdersBiz;
import cn.itcast.erp.entity.Emp;
import cn.itcast.erp.entity.Orderdetail;
import cn.itcast.erp.entity.Orders;
import cn.itcast.erp.exception.ErpException;
import cn.itcast.erp.util.WebUtil;

@Controller("ordersAction")
@Scope("prototype")
@ParentPackage("struts-default")
@Namespace("/")
@Action("orders")
public class OrdersAction extends BaseAction<Orders> {

    private IOrdersBiz ordersBiz;
    private String json;
    
    @Autowired
    private IWayBillWs wayBillWs;
    //运单号
    private Long waybillSn;
    
    public Long getWaybillSn() {
		return waybillSn;
	}
	public void setWaybillSn(Long waybillSn) {
		this.waybillSn = waybillSn;
	}
	public void setJson(String json) {
		this.json = json;
	}
	@Resource(name="ordersBiz")
    public void setOrdersBiz(IOrdersBiz ordersBiz) {
        this.ordersBiz = ordersBiz;
        super.setBaseBiz(this.ordersBiz);
    }
    /**
     * 采购订单申请
     * @see cn.itcast.erp.action.BaseAction#add()
     */
    @Override
    public void add() {
    	Emp loginUser = WebUtil.getLoginUser();
    	if (null == loginUser) {
			WebUtil.ajaxReturn(false, "亲！你还没有登录");
			return;
		}
    	try {
			Orders orders = getT();
			orders.setCreater(loginUser.getUuid());
			List<Orderdetail> orderdetails = JSON.parseArray(json, Orderdetail.class);
			orders.setOrderdetails(orderdetails);
			//设置订单类型为采购
			orders.setType(Orders.TYPE_IN);
			ordersBiz.add(orders);
			WebUtil.ajaxReturn(true, "添加订单成功");
		} catch (ErpException e) {
			e.printStackTrace();
			WebUtil.ajaxReturn(false, e.getMessage());
		} catch (Exception e) {
			e.printStackTrace();
			WebUtil.ajaxReturn(false, "订单添加失败");
		}
    }
    /**
     * 销售订单申请
     */
    public void add_out() {
    	Emp loginUser = WebUtil.getLoginUser();
    	if (null == loginUser) {
    		WebUtil.ajaxReturn(false, "亲！你还没有登录");
    		return;
    	}
    	try {
    		Orders orders = getT();
    		orders.setCreater(loginUser.getUuid());
    		List<Orderdetail> orderdetails = JSON.parseArray(json, Orderdetail.class);
    		orders.setOrderdetails(orderdetails);
    		//设置订单类型为销售
    		orders.setType(Orders.TYPE_OUT);
    		ordersBiz.add(orders);
    		WebUtil.ajaxReturn(true, "添加销售订单成功");
    	} catch (ErpException e) {
			e.printStackTrace();
			WebUtil.ajaxReturn(false, e.getMessage());
		} catch (Exception e) {
    		e.printStackTrace();
    		WebUtil.ajaxReturn(false, "销售订单添加失败");
    	}
    }
    /**
     * 订单审核
     */
    public void doCheck() {
    	Emp loginUser = WebUtil.getLoginUser();
    	if (null == loginUser) {
			WebUtil.ajaxReturn(false, "亲！你没有登录，请登录后再审核");
			return;
		}
    	try {
			Long uuid = getId();
			ordersBiz.doCheck(loginUser.getUuid(),uuid);
			WebUtil.ajaxReturn(true, "审核成功");
		}catch(ErpException e) {
			e.printStackTrace();
			WebUtil.ajaxReturn(false, e.getMessage());
		} 
    	catch (Exception e) {
			e.printStackTrace();
			WebUtil.ajaxReturn(false, "未知错误");
		}
    	
    }
    /**
     * 采购订单确认
     */
    public void doStart() {
    	Emp loginUser = WebUtil.getLoginUser();
    	if (null == loginUser) {
    		WebUtil.ajaxReturn(false, "亲！你没有登录，请登录后再确认");
    		return;
    	}
    	try {
    		Long uuid = getId();
    		ordersBiz.doStart(loginUser.getUuid(),uuid);
    		WebUtil.ajaxReturn(true, "确认成功");
    	}catch(ErpException e) {
    		e.printStackTrace();
    		WebUtil.ajaxReturn(false, e.getMessage());
    	} 
    	catch (Exception e) {
    		e.printStackTrace();
    		WebUtil.ajaxReturn(false, "未知错误");
    	}
    	
    }
    
    /**
     * 我的订单
     */
    public void myListByPage() {
    	if (null == getT1()) {
			setT1(new Orders());
		}
    	Emp loginUser = WebUtil.getLoginUser();
    	if (null == loginUser) {
    		WebUtil.ajaxReturn(false, "亲！你没有登录，请登录后再确认");
    		return;
    	}
    	getT1().setCreater(loginUser.getUuid());
    	super.listByPage();
    }
    /**
     * 订单导出
     */
    public void exportById() {
    	HttpServletResponse response = ServletActionContext.getResponse();
    	String filename = String.format("orders_%03d.xls", getId());
    	try {
    		response.setHeader("Content-Disposition", "attachment;filename="+filename);
			ordersBiz.exportById(response.getOutputStream(), getId());
		} catch (IOException e) {
			e.printStackTrace();
		}
    }
    
    public void waybilldetailList() {
    	if(null == waybillSn) {
    		return ;
    	}
    	List<Waybilldetail> list = wayBillWs.waybilldetailList(waybillSn);
    	WebUtil.write(list);
    }
}
