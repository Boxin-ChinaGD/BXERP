package com.bx.erp.task;

import java.util.Date;

import javax.annotation.Resource;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.bx.erp.action.bo.BaseBO;
import com.bx.erp.action.bo.VipBO;
import com.bx.erp.cache.BaseCache;
import com.bx.erp.cache.CacheManager;
import com.bx.erp.model.Company;
import com.bx.erp.model.ErrorInfo;
import com.bx.erp.model.CacheType.EnumCacheType;
import com.bx.erp.model.ErrorInfo.EnumErrorCode;
import com.bx.erp.model.Vip;
import com.bx.erp.model.config.ConfigGeneral;
import com.bx.erp.util.DataSourceContextHolder;
import com.bx.erp.util.DatetimeUtil;

@Component("bonusTaskThread")
@Scope("prototype")
public class BonusTaskThread extends TaskThread {
	private Log logger = LogFactory.getLog(BonusTaskThread.class);

	@Resource
	private VipBO vipBO;

	/** 为了使测试代码能够在任何时间驱动夜间任务，而不必真的等待到夜间才开始夜间任务，特设置本变量。 默认值为null。 <br />
	 * 1、值为null时，生成当前日期前一天的报表。这是用于功能代码的值，功能代码不能设置其它值！ <br />
	 * 2、为其它值时，生成reportDateForTest-1天的报表 */
	public static Date reportDateForTest;

	/** 将此值设置为true，夜间任务将无条件执行。运行一次后，如果不想再运行，可以设置为false */
	public static boolean runOnce;

	public Date getReportDateForTest() {
		return reportDateForTest;
	}

	public BonusTaskThread() {
		reportDateForTest = null;
		name = "积分清零检查";
	}

	@Override
	protected Date getStartDatetime(String dbName) {
		ErrorInfo ecOut = new ErrorInfo();
		ConfigGeneral cgStart = (ConfigGeneral) CacheManager.getCache(dbName, EnumCacheType.ECT_ConfigGeneral).read1(BaseCache.BonusTaskScanStartTime, BaseBO.SYSTEM, ecOut, dbName);
		return DatetimeUtil.getNextDayWithTime(new Date(), cgStart.getValue());
	}

	@Override
	public boolean canRunOnceForTest() {
		return runOnce;
	}

	@Override
	protected void doTaskForEveryCompany(Company company) {
		String dbName = company.getDbName();
		logger.info("定时任务执行时间段已到，开始运行任务：查询当天" + name + "并推送 \t本线程hashcode=" + this.hashCode() + "\t 执行的dbName=" + dbName);
		//
		DataSourceContextHolder.setDbName(dbName);
		vipBO.updateObject(BaseBO.SYSTEM, BaseBO.CASE_Vip_ResetBonus, new Vip());
		if (vipBO.getLastErrorCode() != EnumErrorCode.EC_NoError) {
			logger.error("夜间检查任务【" + name + "】积分清零异常," + vipBO.printErrorInfo());
			return;
		}
	}
}
