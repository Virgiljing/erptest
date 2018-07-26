package cn.itcast.erp.biz.impl;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.List;

import javax.annotation.Resource;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.itcast.erp.biz.ISupplierBiz;
import cn.itcast.erp.dao.ISupplierDao;
import cn.itcast.erp.entity.Supplier;
import cn.itcast.erp.exception.ErpException;

/**
 * 供应商业务逻辑类
 *
 */
@Service("supplierBiz")
public class SupplierBiz extends BaseBiz<Supplier> implements ISupplierBiz {

    private ISupplierDao supplierDao;

    @Resource(name="supplierDao")
    public void setSupplierDao(ISupplierDao supplierDao) {
        this.supplierDao = supplierDao;
        super.setBaseDao(this.supplierDao);
    }

	@Override
	public void export(OutputStream os, Supplier t1) {
		List<Supplier> list = super.getList(t1, null, null);
		HSSFWorkbook workbook = new HSSFWorkbook();
		HSSFSheet sheet = null;
		
		if (Supplier.TYPE_SUPPLIER.equals(t1.getType())) {
			sheet = workbook.createSheet("供应商");
		}
		if (Supplier.TYPE_CUSTOMER.equals(t1.getType())) {
			sheet = workbook.createSheet("客户");
		}
		
		HSSFRow row = sheet.createRow(0);
		String[] headerNames = {"名称","地址","联系人","电话","Email"};
		int[] columnWidths = {4000,8000,2000,3000,8000};
		HSSFCell cell = null;
		for (int i = 0; i < headerNames.length; i++) {
			cell = row.createCell(i);
			cell.setCellValue(headerNames[i]);
			sheet.setColumnWidth(i, columnWidths[i]);
		}
		int i = 1;
		for (Supplier supplier : list) {
			row = sheet.createRow(i);
			cell = row.createCell(i);
			row.createCell(0).setCellValue(supplier.getName());
			row.createCell(1).setCellValue(supplier.getAddress());
			row.createCell(2).setCellValue(supplier.getContact());
			row.createCell(3).setCellValue(supplier.getTele());
			row.createCell(4).setCellValue(supplier.getEmail());
			i ++;
		}
		try {
			workbook.write(os);
		} catch (IOException e) {
			e.printStackTrace();
		}finally {
			try {
				workbook.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	@SuppressWarnings("resource")
	@Override
	@Transactional
	public void doImport(InputStream is) throws IOException {
		HSSFWorkbook book = new HSSFWorkbook(is);
		HSSFSheet sheet = book.getSheetAt(0);
		String type = "";
		if ("供应商".equals(sheet.getSheetName())) {
			type = Supplier.TYPE_SUPPLIER;
		}else if ("客户".equals(sheet.getSheetName())) {
			type = Supplier.TYPE_CUSTOMER;
		}else {
			throw new ErpException("工作表名称格式不正确");
		}
		
		int lastRow = sheet.getLastRowNum();
		Supplier supplier = null;
		for(int i = 1;i <= lastRow;i++) {
			supplier = new Supplier();
			supplier.setName(sheet.getRow(i).getCell(0).getStringCellValue());
			List<Supplier> list = supplierDao.getList(null, supplier, null);
			if (list.size()>0) {
				supplier = list.get(0);
			}
			supplier.setAddress(sheet.getRow(i).getCell(1).getStringCellValue());
			supplier.setContact(sheet.getRow(i).getCell(2).getStringCellValue());
			supplier.setTele(sheet.getRow(i).getCell(3).getStringCellValue());
			supplier.setEmail(sheet.getRow(i).getCell(4).getStringCellValue());
			if (list.size()==0) {
				supplier.setType(type);
				supplierDao.add(supplier);
			}
		}
		book.close();
	}
    
}
