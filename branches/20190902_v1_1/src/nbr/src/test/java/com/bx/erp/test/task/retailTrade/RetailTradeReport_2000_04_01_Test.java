package com.bx.erp.test.task.retailTrade;

import org.springframework.test.context.web.WebAppConfiguration;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import com.bx.erp.test.Shared;

@WebAppConfiguration
public class RetailTradeReport_2000_04_01_Test extends BaseRetailTradeReportTest {

	@BeforeClass
	public void beforeClass() {
		super.setUp();
	}

	@AfterClass
	public void afterClass() {
		super.tearDown();
	}
	
	@Test
	public void retailTradeReport_2000_04_01_Test() throws Exception {
		Shared.printTestMethodStartInfo();
		
		Shared.caseLog("2000年04月01日零售日报表测试");
		
		String dtStart = "2000-04-01 00:00:00"; // 开始时间
		String dtEnd = "2000-04-01 23:59:59"; // 结束时间
		// 创建零售单日报表
		createDailyReport(dtStart, dtEnd);
		// 验证零售单日报表汇总
		double[] saleData = {18240.0,9440.0,27,608.0,314.67,0.90}; // 需要验证的销售总额，销售毛利，销售总笔数，日均销售总额，日均销售毛利，日均销售总笔数
		verifyRetailTradeDailyReportSummary(saleData);
		// 验证各商品大类占比
		String categoryParentIDAndSaleAmount = "1,2,3,4,5,6,7,8,9,11,12;351.00,5425.00,2184.00,142.00,1012.00,159.00,7621.00,416.00,740.00,140.00,50.00"; // 需要验证的商品大类ID;商品大类销售额
		verifyRetailTradeDailyReportByCategoryParent(categoryParentIDAndSaleAmount);
		// 验证各员工的销售总额、销售毛利、销售笔数：
		String saleStaff = "7,8;10624.00,7616.00;5524.00,3916.00;20,7"; // 需要验证的收银员ID;销售总额;销售毛利;单数
		verifyRetailTradeDailyReportByStaff(saleStaff);
		// 验证销售量前十一的商品         ....,205,197,187,202,....
		String saleCommodity = "204,203,200,181,210,189,177,205,197,187,202;21,19,16,15,14,14,14,13,13,13,13;0;1"; // 商品ID;销售数量;降序排序(为0时)或升序排序(为1时);根据销售金额(0)或销售数量(1)或销售毛利(2)进行排序 
		verifyRetailTradeDailyReportByCommodity_RanKing(saleCommodity);
		// 验证模糊查询商品
		String queryKeyword = "普,普通商品6;118,15;2841.00,240.00;0,150.00;0;0"; // 模糊搜索条件;销售数量;销售额;销售毛利;降序排序(为0时)或升序排序(为1时);根据销售金额(0)或销售数量(1)或销售毛利(2)进行排序 
		verifyRetailTradeDailyReportByCommodityRetrieve(queryKeyword);
	}
}
