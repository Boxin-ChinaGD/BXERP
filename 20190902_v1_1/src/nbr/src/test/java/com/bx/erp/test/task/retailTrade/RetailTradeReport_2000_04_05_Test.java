package com.bx.erp.test.task.retailTrade;

import org.springframework.test.context.web.WebAppConfiguration;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import com.bx.erp.test.Shared;

@WebAppConfiguration
public class RetailTradeReport_2000_04_05_Test extends BaseRetailTradeReportTest {

	@BeforeClass
	public void beforeClass() {
		super.setUp();
	}

	@AfterClass
	public void afterClass() {
		super.tearDown();
	}
	
	@Test
	public void retailTradeReport_2000_04_05_Test() throws Exception {
		Shared.printTestMethodStartInfo();
		
		Shared.caseLog("2000年04月05日零售日报表测试");
		
		String dtStart = "2000-04-05 00:00:00"; // 开始时间
		String dtEnd = "2000-04-05 23:59:59"; // 结束时间
		// 创建零售单日报表
		createDailyReport(dtStart, dtEnd);
		// 验证零售单日报表汇总
		double[] saleData = {4846.36,2030.05,2,161.55,67.67,0.07}; // 需要验证的销售总额，销售毛利，销售总笔数，日均销售总额，日均销售毛利，日均销售总笔数
		verifyRetailTradeDailyReportSummary(saleData);
		// 验证各商品大类占比
		String categoryParentIDAndSaleAmount = "6,8,9,10,11,12;420.30,274.66,66.22,3099.58,0.00,985.60"; // 需要验证的商品大类ID;商品大类销售额
		verifyRetailTradeDailyReportByCategoryParent(categoryParentIDAndSaleAmount);
		// 验证各员工的销售总额、销售毛利、销售笔数：
		String saleStaff = "8;4846.36;2030.05;2"; // 需要验证的收银员ID;销售总额;销售毛利;单数
		verifyRetailTradeDailyReportByStaff(saleStaff);
		// 验证销售量前九的商品         
		String saleCommodity = "258,259,257,261,263,264,265,260;23,10,5,3,2,2,2,1;0;1"; // 商品ID;销售数量;降序排序(为0时)或升序排序(为1时);根据销售金额(0)或销售数量(1)或销售毛利(2)进行排序 
		verifyRetailTradeDailyReportByCommodity_RanKing(saleCommodity);
		// 验证模糊查询商品
		String queryKeyword = "翼,血翼飞龙;2,2;66.22,66.22;0,21.56;0;0"; // 模糊搜索条件;销售数量;销售额;销售毛利;降序排序(为0时)或升序排序(为1时);根据销售金额(0)或销售数量(1)或销售毛利(2)进行排序 
		verifyRetailTradeDailyReportByCommodityRetrieve(queryKeyword);
	}
}
