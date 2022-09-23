package com.bx.erp.task;

import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.bx.erp.action.bo.BaseBO;
import com.bx.erp.action.bo.report.RetailTradeDailyReportByStaffBO;
import com.bx.erp.cache.BaseCache;
import com.bx.erp.cache.CacheManager;
import com.bx.erp.model.BaseModel;
import com.bx.erp.model.Company;
import com.bx.erp.model.ErrorInfo;
import com.bx.erp.model.Shop;
import com.bx.erp.model.CacheType.EnumCacheType;
import com.bx.erp.model.ErrorInfo.EnumErrorCode;
import com.bx.erp.model.config.ConfigGeneral;
import com.bx.erp.model.report.RetailTradeDailyReportByStaff;
import com.bx.erp.util.DataSourceContextHolder;
import com.bx.erp.util.DatetimeUtil;

@Component("retailTradeDailyReportByStaffTaskThread")
@Scope("prototype")
public class RetailTradeDailyReportByStaffTaskThread extends TaskThread {
	private Log logger = LogFactory.getLog(RetailTradeDailyReportSummaryTaskThread.class);

	@Resource
	private RetailTradeDailyReportByStaffBO retailTradeDailyReportByStaffBO;

	/** 为了使测试代码能够在任何时间驱动夜间任务，而不必真的等待到夜间才开始夜间任务，特设置本变量。 默认值为null。 <br />
	 * 1、值为null时，生成当前日期前一天的报表。这是用于功能代码的值，功能代码不能设置其它值！ <br />
	 * 2、为其它值时，生成reportDateForTest-1天的报表 */
	public static Date reportDateForTest;
	
	/** 将此值设置为true，夜间任务将无条件执行。运行一次后，如果不想再运行，可以设置为false */
	public static boolean runOnce;
	
	public RetailTradeDailyReportByStaffTaskThread() {
		reportDateForTest = null;
		name = "员工业绩报表";
	}

	public Date getReportDateForTest() {
		return reportDateForTest;
	}
	
	@Override
	protected Date getStartDatetime(String dbName) {
		ErrorInfo ecOut = new ErrorInfo();
		ConfigGeneral cgStart = (ConfigGeneral) CacheManager.getCache(dbName, EnumCacheType.ECT_ConfigGeneral).read1(BaseCache.RetailTradeDailyReportByStaffTaskScanStartTime, BaseBO.SYSTEM, ecOut, dbName);
		return DatetimeUtil.getNextDayWithTime(new Date(), cgStart.getValue());
	}

	@Override
	public boolean canRunOnceForTest() {
		return runOnce;
	}

	@Override
	protected void doTaskForEveryCompany(Company company) {
		// logger.debug(this.getName() + "--正在检查是否到达定时任务执行时期段（DB名称=" + dbName + "）...");

		String dbName = company.getDbName();
		logger.info("定时任务执行时间段已到，开始运行任务：查询当天" + name + "并推送 \t本线程hashcode=" + this.hashCode() + "\t 执行的dbName=" + dbName);
		RetailTradeDailyReportByStaff retailTradeDailyReportByStaff = new RetailTradeDailyReportByStaff();
		Date targetDate = (reportDateForTest == null ? DatetimeUtil.getDays(new Date(), -1) : DatetimeUtil.getDays(reportDateForTest, -1));
		// reportDateForTest==null时获取当前时间的前一天，reportDateForTest!=null时，生成reportDateForTest当天报表
		// 获取所有门店信息，循环生成所有门店的日报表
		List<BaseModel> shopList = CacheManager.getCache(company.getDbName(), EnumCacheType.ECT_Shop).readN(true, false); // 从缓存中拿到所有门店
		for(BaseModel bm : shopList) {
			Shop shop  = (Shop) bm;
			retailTradeDailyReportByStaff.setShopID(shop.getID());
			retailTradeDailyReportByStaff.setSaleDatetime(targetDate);
			DataSourceContextHolder.setDbName(dbName);
			List<List<BaseModel>> bmList = retailTradeDailyReportByStaffBO.createObjectEx(BaseBO.SYSTEM, BaseBO.INVALID_CASE_ID, retailTradeDailyReportByStaff);
			if (retailTradeDailyReportByStaffBO.getLastErrorCode() == EnumErrorCode.EC_NoError) {
				logger.info("生成【" + name + "】成功：" + bmList);
			} else {
				logger.error("生成【" + name + "】失败！错误码：" + retailTradeDailyReportByStaffBO.printErrorInfo(dbName, retailTradeDailyReportByStaff));
				return;
			}
		}
	}

}
