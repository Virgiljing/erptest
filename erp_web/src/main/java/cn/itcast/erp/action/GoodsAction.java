package cn.itcast.erp.action;

import javax.annotation.Resource;

import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import cn.itcast.erp.biz.IGoodsBiz;
import cn.itcast.erp.entity.Goods;

@Controller("goodsAction")
@Scope("prototype")
@ParentPackage("struts-default")
@Namespace("/")
@Action("goods")
public class GoodsAction extends BaseAction<Goods> {

    private IGoodsBiz goodsBiz;

    @Resource(name="goodsBiz")
    public void setGoodsBiz(IGoodsBiz goodsBiz) {
        this.goodsBiz = goodsBiz;
        super.setBaseBiz(this.goodsBiz);
    }
}
