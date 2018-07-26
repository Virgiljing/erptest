package cn.itcast.erp.action;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts2.ServletActionContext;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import cn.itcast.erp.biz.ISupplierBiz;
import cn.itcast.erp.entity.Supplier;
import cn.itcast.erp.exception.ErpException;
import cn.itcast.erp.util.WebUtil;

@Controller("supplierAction")
@Scope("prototype")
@ParentPackage("struts-default")
@Namespace("/")
@Action("supplier")
public class SupplierAction extends BaseAction<Supplier> {
	private String q;
	
	private File file;
	private String fileFileName;
	private String fileContentType;
	
    public void setFile(File file) {
		this.file = file;
	}
	public void setFileFileName(String fileFileName) {
		this.fileFileName = fileFileName;
	}
	public void setFileContentType(String fileContentType) {
		this.fileContentType = fileContentType;
	}
	public void setQ(String q) {
		this.q = q;
	}

	private ISupplierBiz supplierBiz;

    @Resource(name="supplierBiz")
    public void setSupplierBiz(ISupplierBiz supplierBiz) {
        this.supplierBiz = supplierBiz;
        super.setBaseBiz(this.supplierBiz);
    }
    @Override
    public void list() {
    	if (null == getT1()) {
			setT1(new Supplier());
		}
    	getT1().setName(q);
    	super.list();
    }
    /**
     * 导出数据
     */
    public void export() {
    	String fileName = "";
    	if (Supplier.TYPE_SUPPLIER.equals(getT1().getType())) {
			fileName = "供应商.xls";
		}
    	
    	if (Supplier.TYPE_CUSTOMER.equals(getT1().getType())) {
			fileName = "客户.xls";
		}
    	
    	HttpServletResponse response = ServletActionContext.getResponse();
		
		try {
			response.setHeader("Content-Disposition", "attachment;filename="+new String(fileName.getBytes(),"ISO-8859-1"));
		} catch (UnsupportedEncodingException e1) {
			e1.printStackTrace();
		}
			
		try {
			supplierBiz.export(response.getOutputStream(), getT1());
		} catch (IOException e) {
			e.printStackTrace();
		}
    }
    /**
     * 导入数据
     */
    public void doImport() {
    	if (!"application/vnd.ms-excel".equals(fileContentType)) {
    		if (!fileFileName.endsWith(".xls")) {
    			WebUtil.ajaxReturn(false, "上传文件必须为excel文件");
    			return;
			}
		}
    	try {
			supplierBiz.doImport(new FileInputStream(file));
			WebUtil.ajaxReturn(true, "导入成功");
		} catch (IOException e) {
			e.printStackTrace();
			WebUtil.ajaxReturn(false, "导入失败");
		} catch (ErpException e) {
			e.printStackTrace();
			WebUtil.ajaxReturn(false, e.getMessage());
		} catch (Exception e) {
			e.printStackTrace();
			WebUtil.ajaxReturn(false, "导入失败，发生未知错误，请联系管理员");
		}
    }
}
