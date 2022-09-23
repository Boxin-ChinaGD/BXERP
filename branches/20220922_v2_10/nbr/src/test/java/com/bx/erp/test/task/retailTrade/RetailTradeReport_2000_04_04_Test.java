package com.bx.erp.test.task.retailTrade;

import org.springframework.test.context.web.WebAppConfiguration;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import com.bx.erp.test.Shared;

@WebAppConfiguration
public class RetailTradeReport_2000_04_04_Test extends BaseRetailTradeReportTest {

	@BeforeClass
	public void beforeClass() {
		super.setUp();
	}

	@AfterClass
	public void afterClass() {
		super.tearDown();
	}
	
	@Test
	public void retailTradeReport_2000_04_04_Test() throws Exception {
		Shared.printTestMethodStartInfo();
		
		Shared.caseLog("2000年04月04日零售日报表测试");
		
		String dtStart = "2000-04-04 00:00:00"; // 开始时间
		String dtEnd = "2000-04-04 23:59:59"; // 结束时间
		// 创建零售单日报表
		createDailyReport(dtStart, dtEnd);
		// 验证零售单日报表汇总
		double[] saleData = {2097.61,2097.61,5,69.92,69.92,0.17}; // 需要验证的销售总额，销售毛利，销售总笔数，日均销售总额，日均销售毛利，日均销售总笔数
		verifyRetailTradeDailyReportSummary(saleData);
		// 验证各商品大类占比
		String categoryParentIDAndSaleAmount = "3,5,7,10;551.33,739.26,573.92,233.10"; // 需要验证的商品大类ID;商品大类销售额
		verifyRetailTradeDailyReportByCategoryParent(categoryParentIDAndSaleAmount);
		// 验证各员工的销售总额、销售毛利、销售笔数：
		String saleStaff = "7;2097.61;2097.61;5"; // 需要验证的收银员ID;销售总额;销售毛利;单数
		verifyRetailTradeDailyReportByStaff(saleStaff);
		// 验证销售量前十的商品
		String saleCommodity = "254,255,253,256;34,18,15,13;0;1"; // 商品ID;销售数量;降序排序(为0时)或升序排序(为1时);根据销售金额(0)或销售数量(1)或销售毛利(2)进行排序 
		verifyRetailTradeDailyReportByCommodity_RanKing(saleCommodity);
		// 验证模糊查询商品
		String queryKeyword = "一,一生二;49,34;977.96,573.92;0,573.92;0;0"; // 模糊搜索条件;销售数量;销售额;销售毛利;降序排序(为0时)或升序排序(为1时);根据销售金额(0)或销售数量(1)或销售毛利(2)进行排序 
		verifyRetailTradeDailyReportByCommodityRetrieve(queryKeyword);
	}
}
