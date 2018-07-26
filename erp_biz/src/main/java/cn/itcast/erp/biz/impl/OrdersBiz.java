package cn.itcast.erp.biz.impl;

import java.io.IOException;
import java.io.OutputStream;
import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFDataFormat;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.itcast.erp.biz.IOrdersBiz;
import cn.itcast.erp.dao.IEmpDao;
import cn.itcast.erp.dao.IOrdersDao;
import cn.itcast.erp.dao.ISupplierDao;
import cn.itcast.erp.entity.Emp;
import cn.itcast.erp.entity.Orderdetail;
import cn.itcast.erp.entity.Orders;
import cn.itcast.erp.exception.ErpException;

/**
 * 订单业务逻辑类
 *
 */
@Service("ordersBiz")
public class OrdersBiz extends BaseBiz<Orders> implements IOrdersBiz {

    private IOrdersDao ordersDao;
    @Autowired
    private IEmpDao empDao;
    @Autowired
    private ISupplierDao supplierDao;
    @Resource(name="ordersDao")
    public void setOrdersDao(IOrdersDao ordersDao) {
        this.ordersDao = ordersDao;
        super.setBaseDao(this.ordersDao);
    }
    @Override
    @Transactional
    public void add(Orders orders) {
    	Subject subject = SecurityUtils.getSubject();
    	if (Orders.TYPE_IN.equals(orders.getType())) {
			if (!subject.isPermitted("我的采购订单")) {
				throw new ErpException("当前用户没有权限");
			}
		}else if (Orders.TYPE_OUT.equals(orders.getType())) {
			if (!subject.isPermitted("我的销售订单")) {
				throw new ErpException("当前用户没有权限");
			}
		}else {
			throw new ErpException("非法参数！");
		}
    	orders.setCreatetime(new Date());
    	//orders.setType(Orders.TYPE_IN);
    	orders.setState(Orders.STATE_CREATE);
    	Double totalmoney = 0D;
    	List<Orderdetail> orderdetails = orders.getOrderdetails();
    	String type = orders.getType();
    	for (Orderdetail orderdetail : orderdetails) {
    		totalmoney += orderdetail.getMoney();
    		if (type.equals(Orders.TYPE_IN)) {
    			orderdetail.setState(Orderdetail.STATE_NOT_IN);
			}
    		if (type.equals(Orders.TYPE_OUT)) {
    			orderdetail.setState(Orderdetail.STATE_NOT_OUT);
    		}
			orderdetail.setOrders(orders);
		}
    	orders.setTotalmoney(totalmoney);
    	ordersDao.add(orders);
    }
    
    @Override
    public List<Orders> getListByPage(Orders order1, Orders order2, Object obj, int startRow, int maxResults) {
    	List<Orders> list = ordersDao.getListByPage(order1, order2, obj, startRow, maxResults);
    	for (Orders orders : list) {
			orders.setCreaterName(empDao.get(orders.getCreater()).getName());
			orders.setStarterName(getEmpName(orders.getStarter()));
			orders.setEnderName(getEmpName(orders.getEnder()));
			orders.setCheckerName(getEmpName(orders.getChecker()));
			orders.setSupplierName(supplierDao.get(orders.getSupplieruuid()).getName());
		}
    	return list;
    }
    
    private String getEmpName(Long uuid) {
    	if (uuid == null) {
    		return "";
		}
    	Emp emp = empDao.get(uuid);
    	if (emp == null) {
			return "";
		}
    	return emp.getName();
    }
	@Override
	@Transactional
	public void doCheck(Long empUuid, Long uuid) {
		Orders orders = ordersDao.get(uuid);
		if (orders == null) {
			throw new ErpException("订单有问题请核对后再审核");
		}
		if (!Orders.STATE_CREATE.equals(orders.getState())) {
			throw new ErpException("该订单已审核");
		}
		//设置审核人
		orders.setChecker(empUuid);
		//设置审核时间
		orders.setChecktime(new Date());
		//修改订单状态
		orders.setState(Orders.STATE_CHECK);
	}
	@Override
	@Transactional
	public void doStart(Long empUuid, Long uuid) {
		Orders orders = ordersDao.get(uuid);
		if (orders == null) {
			throw new ErpException("订单有问题请核对后再确认");
		}
		if (!Orders.STATE_CHECK.equals(orders.getState())) {
			throw new ErpException("该订单已确认");
		}
		//设置确认人
		orders.setStarter(empUuid);
		//设置确认时间
		orders.setStarttime(new Date());
		//修改订单状态
		orders.setState(Orders.STATE_START);
		
	}
	@Override
	public void exportById(OutputStream os, Long uuid) throws IOException {
		Orders orders = ordersDao.get(uuid);
		String title = "";
		String supplier = "";
		if (Orders.TYPE_IN.equals(orders.getType())) {
			title = "采购订单";
			supplier = "供应商";
		}
		if (Orders.TYPE_OUT.equals(orders.getType())) {
			title = "销售订单";
			supplier = "客户";
		}
		HSSFWorkbook book = new HSSFWorkbook();
		HSSFSheet sheet = book.createSheet(title);
		HSSFCellStyle style_content = book.createCellStyle();
		//设置单元格样式有边框的单元格
		style_content.setBorderBottom(CellStyle.BORDER_THIN);
		style_content.setBorderTop(CellStyle.BORDER_THIN);
		style_content.setBorderLeft(CellStyle.BORDER_THIN);
		style_content.setBorderRight(CellStyle.BORDER_THIN);
		

		//设置对其方式和字体
		style_content.setAlignment(CellStyle.ALIGN_CENTER);//水平居中
		style_content.setVerticalAlignment(CellStyle.VERTICAL_CENTER);//上下居中
		
	    
		//是指内容字体
		HSSFFont font_content = book.createFont();
		font_content.setFontName("宋体");
		font_content.setFontHeightInPoints((short)11);
		style_content.setFont(font_content);
		
		//标题字体
		HSSFCellStyle style_title = book.createCellStyle();
		style_title.setAlignment(CellStyle.ALIGN_CENTER);
		style_title.setVerticalAlignment(CellStyle.VERTICAL_CENTER);
		HSSFFont font_title = book.createFont();
		font_title.setFontName("黑体");
		font_title.setBold(true);
		font_title.setFontHeightInPoints((short)18);
		style_title.setFont(font_title);
		
		//设置日期格式
		HSSFCellStyle style_date = book.createCellStyle();
		style_date.cloneStyleFrom(style_content);
		HSSFDataFormat dataFormat = book.createDataFormat();
		style_date.setDataFormat(dataFormat.getFormat("yyyy-MM-dd HH:mm"));
		List<Orderdetail> orderdetails = orders.getOrderdetails();
		int rowCount = orderdetails.size();
		
		for(int i= 2;i< 10+rowCount;i++) {
			HSSFRow row = sheet.createRow(i);
			for(int j=0;j<4;j++) {
				HSSFCell cell = row.createCell(j);
				cell.setCellStyle(style_content);
				if (i>=3&&i<7&&j==1) {
					//设置日期格式
					cell.setCellStyle(style_date);
				}
			}
		}
		//合并单元格
		sheet.addMergedRegion(new CellRangeAddress(0,0,0,3));//标题
		sheet.addMergedRegion(new CellRangeAddress(2,2,1,3));//供应商
		sheet.addMergedRegion(new CellRangeAddress(7,7,0,3));//订单明细
		
		//设置标题内容
		HSSFCell cell = sheet.createRow(0).createCell(0);
		cell.setCellValue(title);
		cell.setCellStyle(style_title);
		//设置供应商内容
		sheet.getRow(2).getCell(0).setCellValue(supplier);
		sheet.getRow(2).getCell(1).setCellValue(supplierDao.get(orders.getSupplieruuid()).getName());
		sheet.getRow(3).getCell(0).setCellValue("下单日期");
		sheet.getRow(4).getCell(0).setCellValue("审核日期");
		sheet.getRow(5).getCell(0).setCellValue("采购日期");
		sheet.getRow(6).getCell(0).setCellValue("入库日期");
		
		
		setDateValue(sheet.getRow(3).getCell(1),(orders.getCreatetime()));
		setDateValue(sheet.getRow(4).getCell(1),(orders.getChecktime()));
		setDateValue(sheet.getRow(5).getCell(1),(orders.getStarttime()));
		setDateValue(sheet.getRow(6).getCell(1),(orders.getEndtime()));
		                        
		
		sheet.getRow(3).getCell(2).setCellValue("经办人");
		sheet.getRow(4).getCell(2).setCellValue("经办人");
		sheet.getRow(5).getCell(2).setCellValue("经办人");
		sheet.getRow(6).getCell(2).setCellValue("经办人");
		
		sheet.getRow(3).getCell(3).setCellValue(getEmpName(orders.getCreater()));
		sheet.getRow(4).getCell(3).setCellValue(getEmpName(orders.getChecker()));
		sheet.getRow(5).getCell(3).setCellValue(getEmpName(orders.getStarter()));
		sheet.getRow(6).getCell(3).setCellValue(getEmpName(orders.getEnder()));
		
		//设置订单明细
		sheet.getRow(7).getCell(0).setCellValue("订单明细");
		sheet.getRow(8).getCell(0).setCellValue("商品名称");
		sheet.getRow(8).getCell(1).setCellValue("数量");
		sheet.getRow(8).getCell(2).setCellValue("价格");
		sheet.getRow(8).getCell(3).setCellValue("金额");
		
		//设置行高列宽
		sheet.getRow(0).setHeight((short)1000);
		for (int i = 2; i < 10 + rowCount; i++) {
			sheet.getRow(i).setHeight((short)500);
		}
		for (int i = 0; i < 4; i++) {
			sheet.setColumnWidth(i, 5000);
		}
		
		int i = 9;
		for (;i < 9+rowCount; i++) {
			HSSFRow row = sheet.getRow(i);
			row.getCell(0).setCellValue(orderdetails.get(i-9).getGoodsname());
			row.getCell(1).setCellValue(orderdetails.get(i-9).getNum());
			row.getCell(2).setCellValue(orderdetails.get(i-9).getPrice());
			row.getCell(3).setCellValue(orderdetails.get(i-9).getMoney());
		}
		HSSFRow row = sheet.getRow(i);
		row.getCell(0).setCellValue("合计");
		row.getCell(3).setCellValue(orders.getTotalmoney());
		book.write(os);
		book.close();
		
	}
	private void setDateValue(Cell cell,Date date) {
		if (null != date) {
			cell.setCellValue(date);
		}
	}
}
