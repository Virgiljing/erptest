package cn.itcast.erp.biz;

import java.util.Date;
import java.util.List;
import java.util.Map;

public interface IReportBiz {
	/**
	 * 销售统计
	 * @param startDate
	 * @param endDate
	 * @return
	 */
	List<Map<String,Object>> orderReport(Date startDate,Date endDate);
	
	/**
	 * 销售趋势
	 * @param year
	 * @return
	 */
	List<Map<String,Object>> trendReport(int year);
}
