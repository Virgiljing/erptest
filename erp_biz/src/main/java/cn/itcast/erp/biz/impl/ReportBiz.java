package cn.itcast.erp.biz.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.itcast.erp.biz.IReportBiz;
import cn.itcast.erp.dao.IReportDao;

@Service
public class ReportBiz implements IReportBiz {
	@Autowired
	private IReportDao reportDao;
	@Override
	public List<Map<String, Object>> orderReport(Date startDate, Date endDate) {
		
		return reportDao.orderReport(startDate, endDate);
	}
	@Override
	public List<Map<String, Object>> trendReport(int year) {
		List<Map<String, Object>> list = new ArrayList<>();
		for (int i = 1 ;i <= 12;i++) {
			Map<String, Object> map = reportDao.trendReport(year,i);
			if (null == map) {
				map = new HashMap<>();
				map.put("name", i);
				map.put("y", 0);
			}
			list.add(map);
		}
		return list;
	}

}
