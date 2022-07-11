package com.bx.erp.scheduledTask;

import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.bx.erp.action.BaseAction;
import com.bx.erp.action.RetailTradeAction;
import com.bx.erp.cache.CacheManager;
import com.bx.erp.model.BaseModel;
import com.bx.erp.model.Company;
import com.bx.erp.model.RealTimeSalesStatisticsToday;
import com.bx.erp.model.CacheType.EnumCacheType;

@Component
@EnableScheduling
public class SalesStatistics {
	private Log logger = LogFactory.getLog(SalesStatistics.class);
	
	@Scheduled(cron = "0 0 0 * * ?")
	public void reset() {
		logger.debug("正在进行重置每日销售总额");
		// 遍历所有公司的DB名称，加载所有私有DB的相关数据到缓存
		List<BaseModel> list = CacheManager.getCache(BaseAction.DBName_Public, EnumCacheType.ECT_Company).readN(false, false);
		for (BaseModel bm : list) {
			Company com = (Company) bm;
			RealTimeSalesStatisticsToday realTimeSST = RetailTradeAction.todaysSaleCache.get(com.getDbName());
			if (realTimeSST == null ) {
				if(!com.getDbName().equals(BaseAction.DBName_Public)) {
					logger.error("获取公司当天销售数据失败，dbName:" + com.getDbName());
				}
				continue;	
			}
			realTimeSST.setTotalAmountToday(0);
			realTimeSST.setTotalNOToday(0);
		}
	}
}
