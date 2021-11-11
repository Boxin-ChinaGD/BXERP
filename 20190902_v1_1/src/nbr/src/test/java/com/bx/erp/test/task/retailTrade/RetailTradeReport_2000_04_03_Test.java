package com.bx.erp.test.task.retailTrade;

import org.springframework.test.context.web.WebAppConfiguration;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import com.bx.erp.test.Shared;

@WebAppConfiguration
public class RetailTradeReport_2000_04_03_Test extends BaseRetailTradeReportTest {

	@BeforeClass
	public void beforeClass() {
		super.setUp();
	}

	@AfterClass
	public void afterClass() {
		super.tearDown();
	}
	
	@Test
	public void retailTradeReport_2000_04_03_Test() throws Exception {
		Shared.printTestMethodStartInfo();
		
		Shared.caseLog("2000年04月03日零售日报表测试");
		
		String dtStart = "2000-04-03 00:00:00"; // 开始时间
		String dtEnd = "2000-04-03 23:59:59"; // 结束时间
		// 创建零售单日报表
		createDailyReport(dtStart, dtEnd);
		// 验证零售单日报表汇总
		double[] saleData = {142445.85,57213.74,25,4748.20,1907.12,0.83}; // 需要验证的销售总额，销售毛利，销售总笔数，日均销售总额，日均销售毛利，日均销售总笔数
		verifyRetailTradeDailyReportSummary(saleData);
		// 验证各商品大类占比
		String categoryParentIDAndSaleAmount = "1,2,3,4,5,6,7,8,9,11,12;13814.32,103211.46,8088.07,143.00,1067.00,2218.00,7982.00,4114.00,928.00,270.00,610.00"; // 需要验证的商品大类ID;商品大类销售额
		verifyRetailTradeDailyReportByCategoryParent(categoryParentIDAndSaleAmount);
		// 验证各员工的销售总额、销售毛利、销售笔数：
		String saleStaff = "7,8,9;35566.38,22395.74,84483.73;15117.48,8936.84,33159.42;8,9,8"; // 需要验证的收银员ID;销售总额;销售毛利;单数
		verifyRetailTradeDailyReportByStaff(saleStaff);
		// 验证销售量前十一的商品         ...,216,228,...
		String saleCommodity = "208,225,197,210,191,221,217,195,215,228,216;120,119,110,81,73,68,55,44,37,36,36;0;1"; // 商品ID;销售数量;降序排序(为0时)或升序排序(为1时);根据销售金额(0)或销售数量(1)或销售毛利(2)进行排序 
		verifyRetailTradeDailyReportByCommodity_RanKing(saleCommodity);
		// 验证模糊查询商品
		String queryKeyword = "剑,长虹剑;363,36;65492.43,659.52;0,299.88;0;0"; // 模糊搜索条件;销售数量;销售额;销售毛利;降序排序(为0时)或升序排序(为1时);根据销售金额(0)或销售数量(1)或销售毛利(2)进行排序 
		verifyRetailTradeDailyReportByCommodityRetrieve(queryKeyword);
	}
}
