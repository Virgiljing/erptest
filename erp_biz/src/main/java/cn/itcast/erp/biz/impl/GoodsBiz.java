package cn.itcast.erp.biz.impl;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.ServletOutputStream;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.itcast.erp.biz.IGoodsBiz;
import cn.itcast.erp.dao.IGoodsDao;
import cn.itcast.erp.dao.IGoodstypeDao;
import cn.itcast.erp.entity.Goods;
import cn.itcast.erp.entity.Goodstype;
import cn.itcast.erp.exception.ErpException;
import net.sf.jxls.transformer.XLSTransformer;

/**
 * 商品业务逻辑类
 *
 */
@Service("goodsBiz")
public class GoodsBiz extends BaseBiz<Goods> implements IGoodsBiz {

    private IGoodsDao goodsDao;

    @Resource(name="goodsDao")
    public void setGoodsDao(IGoodsDao goodsDao) {
        this.goodsDao = goodsDao;
        super.setBaseDao(this.goodsDao);
    }
    @Autowired
    private IGoodstypeDao goodstypeDao;
	@Override
	@Transactional
	public void doImportGoods(FileInputStream is) throws IOException {
		// 读取工作簿
        Workbook wk = new HSSFWorkbook(is);
        try {
            Sheet sht = wk.getSheetAt(0);
            // 最后一行的下标
            Row row = null;
            String name = null;
            Goods goods = null;
            List<Goods> list = null;
            
            // i=1是要忽略表头行
            for(int i = 2; i <= sht.getLastRowNum(); i++) {
                row = sht.getRow(i);
                if (null == row.getCell(0)) {
					break;
				}
                // 供应商名称
                name = row.getCell(0).getStringCellValue();
                goods = new Goods();
                goods.setName(name);
                // 判断是否存在
                list = goodsDao.getList(null, goods, null);
                if(list.size() > 0) {
                    // 存在
                    goods = list.get(0); // 进入持久化状
                }
				goods.setOrigin(row.getCell(1).getStringCellValue());
				goods.setProducer(row.getCell(2).getStringCellValue());
				goods.setUnit(row.getCell(3).getStringCellValue());
				goods.setInprice(row.getCell(4).getNumericCellValue());
				goods.setOutprice(row.getCell(5).getNumericCellValue());
				//构建查询条件
				Goodstype goodstype = new Goodstype();
				goodstype.setName(row.getCell(6).getStringCellValue());
				List<Goodstype> listGoodstype = goodstypeDao.getList(null, goodstype, null);
				if (listGoodstype.size() == 0) {
					throw new ErpException("导入商品类型异常");
				}
				goods.setGoodstype(listGoodstype.get(0));
                if(list.size() == 0) {
                    //不存在
                    goodsDao.add(goods);
                }
            }
        } finally {
            try {
                wk.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
	}

	@Override
	public void exportGoods(ServletOutputStream os, Goods t1) {

		List<Goods> list = goodsDao.getList(t1, null, null);
		Workbook book = null;
		 try {
			book = new HSSFWorkbook(new ClassPathResource("商品.xls").getInputStream());
			Map<String,Object> dataModel = new HashMap<String,Object>();
			dataModel.put("list", list);
			XLSTransformer transformer = new XLSTransformer();
			transformer.transformWorkbook(book, dataModel);
			book.write(os);
		} catch (Exception e) {
			e.printStackTrace();
		}finally {
			if (null != book) {
				try {
					book.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
			
	}
	
	@Override
    @Transactional
    public void add(Goods t) {
		//权限控制
        Subject subject = SecurityUtils.getSubject();
        if(!subject.isPermitted("商品")) {
            throw new ErpException("没有权限");
        }
        super.add(t);
    }
    
}
