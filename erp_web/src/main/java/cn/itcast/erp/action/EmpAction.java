package cn.itcast.erp.action;

import java.util.List;

import javax.annotation.Resource;

import org.apache.struts2.ServletActionContext;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import cn.itcast.erp.biz.IEmpBiz;
import cn.itcast.erp.entity.Emp;
import cn.itcast.erp.entity.Tree;
import cn.itcast.erp.exception.ErpException;
import cn.itcast.erp.util.WebUtil;

@Controller("empAction")
@Scope("prototype")
@ParentPackage("struts-default")
@Namespace("/")
@Action("emp")
public class EmpAction extends BaseAction<Emp> {

    private IEmpBiz empBiz;
    private String oldPwd;
    private String newPwd;
    private String ids;
    
    
    public void setIds(String ids) {
		this.ids = ids;
	}
	public void setOldPwd(String oldPwd) {
		this.oldPwd = oldPwd;
	}
	public void setNewPwd(String newPwd) {
		this.newPwd = newPwd;
	}
	@Resource(name="empBiz")
    public void setEmpBiz(IEmpBiz empBiz) {
        this.empBiz = empBiz;
        super.setBaseBiz(this.empBiz);
    }
    /**
     * 修改密码
     */
    public void updatePwd() {
    	Emp loginUser = WebUtil.getLoginUser();
    	if (loginUser == null) {
			WebUtil.ajaxReturn(false, "请先登录再修改密码");
			return;
		}
    	try {
			empBiz.updatePwd(oldPwd,newPwd,loginUser);
			WebUtil.ajaxReturn(true, "密码修改成功");
			ServletActionContext.getRequest().getSession().removeAttribute("loginUser");
		}catch(ErpException e) {
			WebUtil.ajaxReturn(false, e.getMessage());
		} 
    	catch (Exception e) {
			e.printStackTrace();
			WebUtil.ajaxReturn(false, "密码修改失败");
		}
    }
    /**
     * 重置密码
     */
    public void updatePwd_reset() {
    	try {
			empBiz.updatePwd_reset(newPwd,getId());
			WebUtil.ajaxReturn(true, "重置密码成功");
		} catch (Exception e) {
			WebUtil.ajaxReturn(false, "重置密码失败");
			e.printStackTrace();
		}
    }
    /**
     * 读取角色列表
     */
    public void readEmpRoles() {
    	List<Tree> empRolse = empBiz.readEmpRolse(getId());
    	WebUtil.write(empRolse);
    }
    /**
     * 更新用户角色权限
     */
    public void updateEmpRoles() {
    	try {
			empBiz.updateEmpRolse(getId(), ids);
			WebUtil.ajaxReturn(true, "更新成功");
		} catch (Exception e) {
			e.printStackTrace();
			WebUtil.ajaxReturn(false, "更新失败");
		}
    }
}
