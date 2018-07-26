package cn.itcast.erp.biz;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import cn.itcast.erp.entity.Supplier;
/**
 * 供应商业务逻辑层接口
 *
 */
public interface ISupplierBiz extends IBaseBiz<Supplier>{
	/**
	 * 导出excel文件
	 * @param os
	 * @param t1
	 */
	void export(OutputStream os,Supplier t1);
	/**
	 * 导入客户信息
	 * @param os
	 * @throws IOException 
	 */
	void doImport(InputStream is) throws IOException;
}

