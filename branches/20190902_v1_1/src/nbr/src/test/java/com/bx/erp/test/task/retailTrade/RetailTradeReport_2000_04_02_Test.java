package com.bx.erp.test.task.retailTrade;

import org.springframework.test.context.web.WebAppConfiguration;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import com.bx.erp.test.Shared;

@WebAppConfiguration
public class RetailTradeReport_2000_04_02_Test extends BaseRetailTradeReportTest {

	@BeforeClass
	public void beforeClass() {
		super.setUp();
	}

	@AfterClass
	public void afterClass() {
		super.tearDown();
	}
	
	@Test
	public void retailTradeReport2000_04_02Test() throws Exception {
		Shared.printTestMethodStartInfo();
		
		Shared.caseLog("2000年04月02日零售日报表测试");
		
		String dtStart = "2000-04-02 00:00:00"; // 开始时间
		String dtEnd = "2000-04-02 23:59:59"; // 结束时间
		// 创建零售单日报表
		createDailyReport(dtStart, dtEnd);
		// 验证零售单日报表汇总
		double[] saleData = {30464.2,6878.12,18,1015.47,229.27,0.60}; // 需要验证的销售总额，销售毛利，销售总笔数，日均销售总额，日均销售毛利，日均销售总笔数
		verifyRetailTradeDailyReportSummary(saleData);
		// 验证各商品大类占比
		String categoryParentIDAndSaleAmount = "1,2,3;15590.62,13199.54,1674.04"; // 需要验证的商品大类ID;商品大类销售额
		verifyRetailTradeDailyReportByCategoryParent(categoryParentIDAndSaleAmount);
		// 验证各员工的销售总额、销售毛利、销售笔数：
		String saleStaff = "7,9;7480.68,22983.52;1814.53,5063.59;9,9"; // 需要验证的收银员ID;销售总额;销售毛利;单数
		verifyRetailTradeDailyReportByStaff(saleStaff);
		// 验证销售量前十的商品             ...,221,222,...
		String saleCommodity = "230,229,227,220,222,221,218,217,228,216;30,27,26,26,22,22,21,20,18,17;0;1"; // 商品ID;销售数量;降序排序(为0时)或升序排序(为1时);根据销售金额(0)或销售数量(1)或销售毛利(2)进行排序 
		verifyRetailTradeDailyReportByCommodity_RanKing(saleCommodity);
		// 验证模糊查询商品
		String queryKeyword = "冰,冰天雪地;50,30;12058.2,10826.4;0,1455.6;0;0"; // 模糊搜索条件;销售数量;销售额;销售毛利;降序排序(为0时)或升序排序(为1时);根据销售金额(0)或销售数量(1)或销售毛利(2)进行排序 
		verifyRetailTradeDailyReportByCommodityRetrieve(queryKeyword);
	}
}
