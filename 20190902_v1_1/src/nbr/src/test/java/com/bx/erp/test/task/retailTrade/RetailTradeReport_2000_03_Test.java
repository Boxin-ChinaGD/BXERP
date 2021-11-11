package com.bx.erp.test.task.retailTrade;

import static org.testng.Assert.assertTrue;
import org.springframework.test.context.web.WebAppConfiguration;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import com.bx.erp.model.BaseModel;
import com.bx.erp.model.report.RetailTradeDailyReportSummary;
import com.bx.erp.model.report.RetailTradeMonthlyReportSummary;
import com.bx.erp.test.Shared;

@WebAppConfiguration
public class RetailTradeReport_2000_03_Test extends BaseRetailTradeMonthlyReportTest {

	@BeforeClass
	public void beforeClass() {
		super.setUp();
	}

	@AfterClass
	public void afterClass() {
		super.tearDown();
	}

	@Test
	public void retailTradeReport2000_03_01Test() throws Exception {
		Shared.printTestMethodStartInfo();

		Shared.caseLog("2000年03月01日零售日报表测试");

		String dtStart = "2000-03-01 00:00:00"; // 开始时间
		String dtEnd = "2000-03-01 23:59:59"; // 结束时间
		// 创建零售单日报表
		createDailyReport(dtStart);
		//
		RetailTradeDailyReportSummary rtdp = retrieveRetailTradeDailyReport(dtStart, dtEnd);
		assertTrue(rtdp != null, "查询不到当天日报表！");
		// 校验日报表数据
		double totalAmount = 1624.000000d;
		double totalGross = 950.000000d;
		assertTrue((rtdp.getTotalAmount() - totalAmount) < BaseModel.TOLERANCE && (rtdp.getTotalGross() - totalGross) < BaseModel.TOLERANCE, "查询不到当天日报表！");
	}

	@Test(dependsOnMethods = "retailTradeReport2000_03_01Test")
	public void retailTradeReport2000_03_02Test() throws Exception {
		Shared.printTestMethodStartInfo();

		Shared.caseLog("2000年03月02日零售日报表测试");

		String dtStart = "2000-03-02 00:00:00"; // 开始时间
		String dtEnd = "2000-03-02 23:59:59"; // 结束时间
		// 创建零售单日报表
		createDailyReport(dtStart);
		//
		RetailTradeDailyReportSummary rtdp = retrieveRetailTradeDailyReport(dtStart, dtEnd);
		assertTrue(rtdp != null, "查询不到当天日报表！");
		// 校验日报表数据
		double totalAmount = 2689.000000d;
		double totalGross = 930.000000d;
		assertTrue((rtdp.getTotalAmount() - totalAmount) < BaseModel.TOLERANCE && (rtdp.getTotalGross() - totalGross) < BaseModel.TOLERANCE, "查询不到当天日报表！");
	}

	@Test(dependsOnMethods = "retailTradeReport2000_03_02Test")
	public void retailTradeReport2000_03_03Test() throws Exception {
		Shared.printTestMethodStartInfo();

		Shared.caseLog("2000年03月03日零售日报表测试");

		String dtStart = "2000-03-03 00:00:00"; // 开始时间
		String dtEnd = "2000-03-03 23:59:59"; // 结束时间
		// 创建零售单日报表
		createDailyReport(dtStart);
		//
		RetailTradeDailyReportSummary rtdp = retrieveRetailTradeDailyReport(dtStart, dtEnd);
		assertTrue(rtdp != null, "查询不到当天日报表！");
		// 校验日报表数据
		double totalAmount = 8660.000000d;
		double totalGross = 5526.000000d;
		assertTrue((rtdp.getTotalAmount() - totalAmount) < BaseModel.TOLERANCE && (rtdp.getTotalGross() - totalGross) < BaseModel.TOLERANCE, "查询不到当天日报表！");
	}

	@Test(dependsOnMethods = "retailTradeReport2000_03_03Test")
	public void retailTradeReport2000_03_04Test() throws Exception {
		Shared.printTestMethodStartInfo();

		Shared.caseLog("2000年03月04日零售日报表测试");

		String dtStart = "2000-03-04 00:00:00"; // 开始时间
		String dtEnd = "2000-03-04 23:59:59"; // 结束时间
		// 创建零售单日报表
		createDailyReport(dtStart);
		//
		RetailTradeDailyReportSummary rtdp = retrieveRetailTradeDailyReport(dtStart, dtEnd);
		assertTrue(rtdp != null, "查询不到当天日报表！");
		// 校验日报表数据
		double totalAmount = 1110.000000d;
		double totalGross = 800.000000d;
		assertTrue((rtdp.getTotalAmount() - totalAmount) < BaseModel.TOLERANCE && (rtdp.getTotalGross() - totalGross) < BaseModel.TOLERANCE, "查询不到当天日报表！");
	}

	@Test(dependsOnMethods = "retailTradeReport2000_03_04Test")
	public void retailTradeReport2000_03_05Test() throws Exception {
		Shared.printTestMethodStartInfo();

		Shared.caseLog("2000年03月05日零售日报表测试");

		String dtStart = "2000-03-05 00:00:00"; // 开始时间
		String dtEnd = "2000-03-05 23:59:59"; // 结束时间
		// 创建零售单日报表
		createDailyReport(dtStart);
		//
		RetailTradeDailyReportSummary rtdp = retrieveRetailTradeDailyReport(dtStart, dtEnd);
		assertTrue(rtdp != null, "查询不到当天日报表！");
		// 校验日报表数据
		double totalAmount = 2284.000000d;
		double totalGross = 910.000000d;
		assertTrue((rtdp.getTotalAmount() - totalAmount) < BaseModel.TOLERANCE && (rtdp.getTotalGross() - totalGross) < BaseModel.TOLERANCE, "查询不到当天日报表！");
	}

	@Test(dependsOnMethods = "retailTradeReport2000_03_05Test")
	public void retailTradeReport2000_03_06Test() throws Exception {
		Shared.printTestMethodStartInfo();

		Shared.caseLog("2000年03月06日零售日报表测试");

		String dtStart = "2000-03-06 00:00:00"; // 开始时间
		String dtEnd = "2000-03-06 23:59:59"; // 结束时间
		// 创建零售单日报表
		createDailyReport(dtStart);
		//
		RetailTradeDailyReportSummary rtdp = retrieveRetailTradeDailyReport(dtStart, dtEnd);
		assertTrue(rtdp != null, "查询不到当天日报表！");
		// 校验日报表数据
		double totalAmount = 7730.000000d;
		double totalGross = 3920.000000d;
		assertTrue((rtdp.getTotalAmount() - totalAmount) < BaseModel.TOLERANCE && (rtdp.getTotalGross() - totalGross) < BaseModel.TOLERANCE, "查询不到当天日报表！");
	}

	@Test(dependsOnMethods = "retailTradeReport2000_03_06Test")
	public void retailTradeReport2000_03_07Test() throws Exception {
		Shared.printTestMethodStartInfo();

		Shared.caseLog("2000年03月07日零售日报表测试");

		String dtStart = "2000-03-07 00:00:00"; // 开始时间
		String dtEnd = "2000-03-07 23:59:59"; // 结束时间
		// 创建零售单日报表
		createDailyReport(dtStart);
		//
		RetailTradeDailyReportSummary rtdp = retrieveRetailTradeDailyReport(dtStart, dtEnd);
		assertTrue(rtdp != null, "查询不到当天日报表！");
		// 校验日报表数据
		double totalAmount = 3950.000000d;
		double totalGross = 2840.000000d;
		assertTrue((rtdp.getTotalAmount() - totalAmount) < BaseModel.TOLERANCE && (rtdp.getTotalGross() - totalGross) < BaseModel.TOLERANCE, "查询不到当天日报表！");
	}

	@Test(dependsOnMethods = "retailTradeReport2000_03_07Test")
	public void retailTradeReport2000_03_08Test() throws Exception {
		Shared.printTestMethodStartInfo();

		Shared.caseLog("2000年03月08日零售日报表测试");

		String dtStart = "2000-03-08 00:00:00"; // 开始时间
		String dtEnd = "2000-03-08 23:59:59"; // 结束时间
		// 创建零售单日报表
		createDailyReport(dtStart);
		//
		RetailTradeDailyReportSummary rtdp = retrieveRetailTradeDailyReport(dtStart, dtEnd);
		assertTrue(rtdp != null, "查询不到当天日报表！");
		// 校验日报表数据
		double totalAmount = 1782.000000d;
		double totalGross = 860.000000d;
		assertTrue((rtdp.getTotalAmount() - totalAmount) < BaseModel.TOLERANCE && (rtdp.getTotalGross() - totalGross) < BaseModel.TOLERANCE, "查询不到当天日报表！");
	}

	@Test(dependsOnMethods = "retailTradeReport2000_03_08Test")
	public void retailTradeReport2000_03_09Test() throws Exception {
		Shared.printTestMethodStartInfo();

		Shared.caseLog("2000年03月09日零售日报表测试");

		String dtStart = "2000-03-09 00:00:00"; // 开始时间
		String dtEnd = "2000-03-09 23:59:59"; // 结束时间
		// 创建零售单日报表
		createDailyReport(dtStart);
		//
		RetailTradeDailyReportSummary rtdp = retrieveRetailTradeDailyReport(dtStart, dtEnd);
		assertTrue(rtdp != null, "查询不到当天日报表！");
		// 校验日报表数据
		double totalAmount = 3226.000000d;
		double totalGross = 1300.000000d;
		assertTrue((rtdp.getTotalAmount() - totalAmount) < BaseModel.TOLERANCE && (rtdp.getTotalGross() - totalGross) < BaseModel.TOLERANCE, "查询不到当天日报表！");
	}

	@Test(dependsOnMethods = "retailTradeReport2000_03_09Test")
	public void retailTradeReport2000_03_10Test() throws Exception {
		Shared.printTestMethodStartInfo();

		Shared.caseLog("2000年03月10日零售日报表测试");

		String dtStart = "2000-03-10 00:00:00"; // 开始时间
		String dtEnd = "2000-03-10 23:59:59"; // 结束时间
		// 创建零售单日报表
		createDailyReport(dtStart);
		//
		RetailTradeDailyReportSummary rtdp = retrieveRetailTradeDailyReport(dtStart, dtEnd);
		assertTrue(rtdp != null, "查询不到当天日报表！");
		// 校验日报表数据
		double totalAmount = 6425.000000d;
		double totalGross = 3850.000000d;
		assertTrue((rtdp.getTotalAmount() - totalAmount) < BaseModel.TOLERANCE && (rtdp.getTotalGross() - totalGross) < BaseModel.TOLERANCE, "查询不到当天日报表！");
	}

	@Test(dependsOnMethods = "retailTradeReport2000_03_10Test")
	public void retailTradeReport2000_03_11Test() throws Exception {
		Shared.printTestMethodStartInfo();

		Shared.caseLog("2000年03月11日零售日报表测试");

		String dtStart = "2000-03-11 00:00:00"; // 开始时间
		String dtEnd = "2000-03-11 23:59:59"; // 结束时间
		// 创建零售单日报表
		createDailyReport(dtStart);
		//
		RetailTradeDailyReportSummary rtdp = retrieveRetailTradeDailyReport(dtStart, dtEnd);
		assertTrue(rtdp != null, "查询不到当天日报表！");
		// 校验日报表数据
		double totalAmount = 1420.000000d;
		double totalGross = 840.000000d;
		assertTrue((rtdp.getTotalAmount() - totalAmount) < BaseModel.TOLERANCE && (rtdp.getTotalGross() - totalGross) < BaseModel.TOLERANCE, "查询不到当天日报表！");
	}

	@Test(dependsOnMethods = "retailTradeReport2000_03_11Test")
	public void retailTradeReport2000_03_12Test() throws Exception {
		Shared.printTestMethodStartInfo();

		Shared.caseLog("2000年03月12日零售日报表测试");

		String dtStart = "2000-03-12 00:00:00"; // 开始时间
		String dtEnd = "2000-03-12 23:59:59"; // 结束时间
		// 创建零售单日报表
		createDailyReport(dtStart);
		//
		RetailTradeDailyReportSummary rtdp = retrieveRetailTradeDailyReport(dtStart, dtEnd);
		assertTrue(rtdp != null, "查询不到当天日报表！");
		// 校验日报表数据
		double totalAmount = 2545.000000d;
		double totalGross = 900.000000d;
		assertTrue((rtdp.getTotalAmount() - totalAmount) < BaseModel.TOLERANCE && (rtdp.getTotalGross() - totalGross) < BaseModel.TOLERANCE, "查询不到当天日报表！");
	}

	@Test(dependsOnMethods = "retailTradeReport2000_03_12Test")
	public void retailTradeReport2000_03_13Test() throws Exception {
		Shared.printTestMethodStartInfo();

		Shared.caseLog("2000年03月13日零售日报表测试");

		String dtStart = "2000-03-13 00:00:00"; // 开始时间
		String dtEnd = "2000-03-13 23:59:59"; // 结束时间
		// 创建零售单日报表
		createDailyReport(dtStart);
		//
		RetailTradeDailyReportSummary rtdp = retrieveRetailTradeDailyReport(dtStart, dtEnd);
		assertTrue(rtdp != null, "查询不到当天日报表！");
		// 校验日报表数据
		double totalAmount = 10200.000000d;
		double totalGross = 6506.000000d;
		assertTrue((rtdp.getTotalAmount() - totalAmount) < BaseModel.TOLERANCE && (rtdp.getTotalGross() - totalGross) < BaseModel.TOLERANCE, "查询不到当天日报表！");
	}

	@Test(dependsOnMethods = "retailTradeReport2000_03_13Test")
	public void retailTradeReport2000_03_14Test() throws Exception {
		Shared.printTestMethodStartInfo();

		Shared.caseLog("2000年03月14日零售日报表测试");

		String dtStart = "2000-03-14 00:00:00"; // 开始时间
		String dtEnd = "2000-03-14 23:59:59"; // 结束时间
		// 创建零售单日报表
		createDailyReport(dtStart);
		//
		RetailTradeDailyReportSummary rtdp = retrieveRetailTradeDailyReport(dtStart, dtEnd);
		assertTrue(rtdp != null, "查询不到当天日报表！");
		// 校验日报表数据
		double totalAmount = 1205.000000d;
		double totalGross = 900.000000d;
		assertTrue((rtdp.getTotalAmount() - totalAmount) < BaseModel.TOLERANCE && (rtdp.getTotalGross() - totalGross) < BaseModel.TOLERANCE, "查询不到当天日报表！");
	}

	@Test(dependsOnMethods = "retailTradeReport2000_03_14Test")
	public void retailTradeReport2000_03_15Test() throws Exception {
		Shared.printTestMethodStartInfo();

		Shared.caseLog("2000年03月15日零售日报表测试");

		String dtStart = "2000-03-15 00:00:00"; // 开始时间
		String dtEnd = "2000-03-15 23:59:59"; // 结束时间
		// 创建零售单日报表
		createDailyReport(dtStart);
		//
		RetailTradeDailyReportSummary rtdp = retrieveRetailTradeDailyReport(dtStart, dtEnd);
		assertTrue(rtdp != null, "查询不到当天日报表！");
		// 校验日报表数据
		double totalAmount = 2000.000000d;
		double totalGross = 800.000000d;
		assertTrue((rtdp.getTotalAmount() - totalAmount) < BaseModel.TOLERANCE && (rtdp.getTotalGross() - totalGross) < BaseModel.TOLERANCE, "查询不到当天日报表！");
	}

	@Test(dependsOnMethods = "retailTradeReport2000_03_15Test")
	public void retailTradeReport2000_03_16Test() throws Exception {
		Shared.printTestMethodStartInfo();

		Shared.caseLog("2000年03月16日零售日报表测试");

		String dtStart = "2000-03-16 00:00:00"; // 开始时间
		String dtEnd = "2000-03-16 23:59:59"; // 结束时间
		// 创建零售单日报表
		createDailyReport(dtStart);
		//
		RetailTradeDailyReportSummary rtdp = retrieveRetailTradeDailyReport(dtStart, dtEnd);
		assertTrue(rtdp != null, "查询不到当天日报表！");
		// 校验日报表数据
		double totalAmount = 7996.000000d;
		double totalGross = 4050.000000d;
		assertTrue((rtdp.getTotalAmount() - totalAmount) < BaseModel.TOLERANCE && (rtdp.getTotalGross() - totalGross) < BaseModel.TOLERANCE, "查询不到当天日报表！");
	}

	@Test(dependsOnMethods = "retailTradeReport2000_03_16Test")
	public void retailTradeReport2000_03_17Test() throws Exception {
		Shared.printTestMethodStartInfo();

		Shared.caseLog("2000年03月17日零售日报表测试");

		String dtStart = "2000-03-17 00:00:00"; // 开始时间
		String dtEnd = "2000-03-17 23:59:59"; // 结束时间
		// 创建零售单日报表
		createDailyReport(dtStart);
		//
		RetailTradeDailyReportSummary rtdp = retrieveRetailTradeDailyReport(dtStart, dtEnd);
		assertTrue(rtdp != null, "查询不到当天日报表！");
		// 校验日报表数据
		double totalAmount = 6160.000000d;
		double totalGross = 4660.000000d;
		assertTrue((rtdp.getTotalAmount() - totalAmount) < BaseModel.TOLERANCE && (rtdp.getTotalGross() - totalGross) < BaseModel.TOLERANCE, "查询不到当天日报表！");
	}

	@Test(dependsOnMethods = "retailTradeReport2000_03_17Test")
	public void retailTradeReport2000_03_18Test() throws Exception {
		Shared.printTestMethodStartInfo();

		Shared.caseLog("2000年03月18日零售日报表测试");

		String dtStart = "2000-03-18 00:00:00"; // 开始时间
		String dtEnd = "2000-03-18 23:59:59"; // 结束时间
		// 创建零售单日报表
		createDailyReport(dtStart);
		//
		RetailTradeDailyReportSummary rtdp = retrieveRetailTradeDailyReport(dtStart, dtEnd);
		assertTrue(rtdp != null, "查询不到当天日报表！");
		// 校验日报表数据
		double totalAmount = 1765.000000d;
		double totalGross = 830.000000d;
		assertTrue((rtdp.getTotalAmount() - totalAmount) < BaseModel.TOLERANCE && (rtdp.getTotalGross() - totalGross) < BaseModel.TOLERANCE, "查询不到当天日报表！");
	}

	@Test(dependsOnMethods = "retailTradeReport2000_03_18Test")
	public void retailTradeReport2000_03_19Test() throws Exception {
		Shared.printTestMethodStartInfo();

		Shared.caseLog("2000年03月19日零售日报表测试");

		String dtStart = "2000-03-19 00:00:00"; // 开始时间
		String dtEnd = "2000-03-19 23:59:59"; // 结束时间
		// 创建零售单日报表
		createDailyReport(dtStart);
		//
		RetailTradeDailyReportSummary rtdp = retrieveRetailTradeDailyReport(dtStart, dtEnd);
		assertTrue(rtdp != null, "查询不到当天日报表！");
		// 校验日报表数据
		double totalAmount = 3193.000000d;
		double totalGross = 1300.000000d;
		assertTrue((rtdp.getTotalAmount() - totalAmount) < BaseModel.TOLERANCE && (rtdp.getTotalGross() - totalGross) < BaseModel.TOLERANCE, "查询不到当天日报表！");
	}

	@Test(dependsOnMethods = "retailTradeReport2000_03_19Test")
	public void retailTradeReport2000_03_20Test() throws Exception {
		Shared.printTestMethodStartInfo();

		Shared.caseLog("2000年03月20日零售日报表测试");

		String dtStart = "2000-03-20 00:00:00"; // 开始时间
		String dtEnd = "2000-03-20 23:59:59"; // 结束时间
		// 创建零售单日报表
		createDailyReport(dtStart);
		//
		RetailTradeDailyReportSummary rtdp = retrieveRetailTradeDailyReport(dtStart, dtEnd);
		assertTrue(rtdp != null, "查询不到当天日报表！");
		// 校验日报表数据
		double totalAmount = 8990.000000d;
		double totalGross = 5604.000000d;
		assertTrue((rtdp.getTotalAmount() - totalAmount) < BaseModel.TOLERANCE && (rtdp.getTotalGross() - totalGross) < BaseModel.TOLERANCE, "查询不到当天日报表！");
	}

	@Test(dependsOnMethods = "retailTradeReport2000_03_20Test")
	public void retailTradeReport2000_03_21Test() throws Exception {
		Shared.printTestMethodStartInfo();

		Shared.caseLog("2000年03月21日零售日报表测试");

		String dtStart = "2000-03-21 00:00:00"; // 开始时间
		String dtEnd = "2000-03-21 23:59:59"; // 结束时间
		// 创建零售单日报表
		createDailyReport(dtStart);
		//
		RetailTradeDailyReportSummary rtdp = retrieveRetailTradeDailyReport(dtStart, dtEnd);
		assertTrue(rtdp != null, "查询不到当天日报表！");
		// 校验日报表数据
		double totalAmount = 1574.000000d;
		double totalGross = 920.000000d;
		assertTrue((rtdp.getTotalAmount() - totalAmount) < BaseModel.TOLERANCE && (rtdp.getTotalGross() - totalGross) < BaseModel.TOLERANCE, "查询不到当天日报表！");
	}

	@Test(dependsOnMethods = "retailTradeReport2000_03_21Test")
	public void retailTradeReport2000_03_22Test() throws Exception {
		Shared.printTestMethodStartInfo();

		Shared.caseLog("2000年03月22日零售日报表测试");

		String dtStart = "2000-03-22 00:00:00"; // 开始时间
		String dtEnd = "2000-03-22 23:59:59"; // 结束时间
		// 创建零售单日报表
		createDailyReport(dtStart);
		//
		RetailTradeDailyReportSummary rtdp = retrieveRetailTradeDailyReport(dtStart, dtEnd);
		assertTrue(rtdp != null, "查询不到当天日报表！");
		// 校验日报表数据
		double totalAmount = 2623.000000d;
		double totalGross = 930.000000d;
		assertTrue((rtdp.getTotalAmount() - totalAmount) < BaseModel.TOLERANCE && (rtdp.getTotalGross() - totalGross) < BaseModel.TOLERANCE, "查询不到当天日报表！");
	}

	@Test(dependsOnMethods = "retailTradeReport2000_03_22Test")
	public void retailTradeReport2000_03_23Test() throws Exception {
		Shared.printTestMethodStartInfo();

		Shared.caseLog("2000年03月23日零售日报表测试");

		String dtStart = "2000-03-23 00:00:00"; // 开始时间
		String dtEnd = "2000-03-23 23:59:59"; // 结束时间
		// 创建零售单日报表
		createDailyReport(dtStart);
		//
		RetailTradeDailyReportSummary rtdp = retrieveRetailTradeDailyReport(dtStart, dtEnd);
		assertTrue(rtdp != null, "查询不到当天日报表！");
		// 校验日报表数据
		double totalAmount = 9375.000000d;
		double totalGross = 6030.000000d;
		assertTrue((rtdp.getTotalAmount() - totalAmount) < BaseModel.TOLERANCE && (rtdp.getTotalGross() - totalGross) < BaseModel.TOLERANCE, "查询不到当天日报表！");
	}

	@Test(dependsOnMethods = "retailTradeReport2000_03_23Test")
	public void retailTradeReport2000_03_24Test() throws Exception {
		Shared.printTestMethodStartInfo();

		Shared.caseLog("2000年03月24日零售日报表测试");

		String dtStart = "2000-03-24 00:00:00"; // 开始时间
		String dtEnd = "2000-03-24 23:59:59"; // 结束时间
		// 创建零售单日报表
		createDailyReport(dtStart);
		//
		RetailTradeDailyReportSummary rtdp = retrieveRetailTradeDailyReport(dtStart, dtEnd);
		assertTrue(rtdp != null, "查询不到当天日报表！");
		// 校验日报表数据
		double totalAmount = 1120.000000d;
		double totalGross = 850.000000d;
		assertTrue((rtdp.getTotalAmount() - totalAmount) < BaseModel.TOLERANCE && (rtdp.getTotalGross() - totalGross) < BaseModel.TOLERANCE, "查询不到当天日报表！");
	}

	@Test(dependsOnMethods = "retailTradeReport2000_03_24Test")
	public void retailTradeReport2000_03_25Test() throws Exception {
		Shared.printTestMethodStartInfo();

		Shared.caseLog("2000年03月25日零售日报表测试");

		String dtStart = "2000-03-25 00:00:00"; // 开始时间
		String dtEnd = "2000-03-25 23:59:59"; // 结束时间
		// 创建零售单日报表
		createDailyReport(dtStart);
		//
		RetailTradeDailyReportSummary rtdp = retrieveRetailTradeDailyReport(dtStart, dtEnd);
		assertTrue(rtdp != null, "查询不到当天日报表！");
		// 校验日报表数据
		double totalAmount = 2367.000000d;
		double totalGross = 940.000000d;
		assertTrue((rtdp.getTotalAmount() - totalAmount) < BaseModel.TOLERANCE && (rtdp.getTotalGross() - totalGross) < BaseModel.TOLERANCE, "查询不到当天日报表！");
	}

	@Test(dependsOnMethods = "retailTradeReport2000_03_25Test")
	public void retailTradeReport2000_03_26Test() throws Exception {
		Shared.printTestMethodStartInfo();

		Shared.caseLog("2000年03月26日零售日报表测试");

		String dtStart = "2000-03-26 00:00:00"; // 开始时间
		String dtEnd = "2000-03-26 23:59:59"; // 结束时间
		// 创建零售单日报表
		createDailyReport(dtStart);
		//
		RetailTradeDailyReportSummary rtdp = retrieveRetailTradeDailyReport(dtStart, dtEnd);
		assertTrue(rtdp != null, "查询不到当天日报表！");
		// 校验日报表数据
		double totalAmount = 7780.000000d;
		double totalGross = 3920.000000d;
		assertTrue((rtdp.getTotalAmount() - totalAmount) < BaseModel.TOLERANCE && (rtdp.getTotalGross() - totalGross) < BaseModel.TOLERANCE, "查询不到当天日报表！");
	}

	@Test(dependsOnMethods = "retailTradeReport2000_03_26Test")
	public void retailTradeReport2000_03_27Test() throws Exception {
		Shared.printTestMethodStartInfo();

		Shared.caseLog("2000年03月27日零售日报表测试");

		String dtStart = "2000-03-27 00:00:00"; // 开始时间
		String dtEnd = "2000-03-27 23:59:59"; // 结束时间
		// 创建零售单日报表
		createDailyReport(dtStart);
		//
		RetailTradeDailyReportSummary rtdp = retrieveRetailTradeDailyReport(dtStart, dtEnd);
		assertTrue(rtdp != null, "查询不到当天日报表！");
		// 校验日报表数据
		double totalAmount = 4450.000000d;
		double totalGross = 3460.000000d;
		assertTrue((rtdp.getTotalAmount() - totalAmount) < BaseModel.TOLERANCE && (rtdp.getTotalGross() - totalGross) < BaseModel.TOLERANCE, "查询不到当天日报表！");
	}

	@Test(dependsOnMethods = "retailTradeReport2000_03_27Test")
	public void retailTradeReport2000_03_28Test() throws Exception {
		Shared.printTestMethodStartInfo();

		Shared.caseLog("2000年03月28日零售日报表测试");

		String dtStart = "2000-03-28 00:00:00"; // 开始时间
		String dtEnd = "2000-03-28 23:59:59"; // 结束时间
		// 创建零售单日报表
		createDailyReport(dtStart);
		//
		RetailTradeDailyReportSummary rtdp = retrieveRetailTradeDailyReport(dtStart, dtEnd);
		assertTrue(rtdp != null, "查询不到当天日报表！");
		// 校验日报表数据
		double totalAmount = 1835.000000d;
		double totalGross = 900.000000d;
		assertTrue((rtdp.getTotalAmount() - totalAmount) < BaseModel.TOLERANCE && (rtdp.getTotalGross() - totalGross) < BaseModel.TOLERANCE, "查询不到当天日报表！");
	}

	@Test(dependsOnMethods = "retailTradeReport2000_03_28Test")
	public void retailTradeReport2000_03_29Test() throws Exception {
		Shared.printTestMethodStartInfo();

		Shared.caseLog("2000年03月29日零售日报表测试");

		String dtStart = "2000-03-29 00:00:00"; // 开始时间
		String dtEnd = "2000-03-29 23:59:59"; // 结束时间
		// 创建零售单日报表
		createDailyReport(dtStart);
		//
		RetailTradeDailyReportSummary rtdp = retrieveRetailTradeDailyReport(dtStart, dtEnd);
		assertTrue(rtdp != null, "查询不到当天日报表！");
		// 校验日报表数据
		double totalAmount = 3312.000000d;
		double totalGross = 1330.000000d;
		assertTrue((rtdp.getTotalAmount() - totalAmount) < BaseModel.TOLERANCE && (rtdp.getTotalGross() - totalGross) < BaseModel.TOLERANCE, "查询不到当天日报表！");
	}

	@Test(dependsOnMethods = "retailTradeReport2000_03_29Test")
	public void retailTradeReport2000_03_30Test() throws Exception {
		Shared.printTestMethodStartInfo();

		Shared.caseLog("2000年03月30日零售日报表测试");

		String dtStart = "2000-03-30 00:00:00"; // 开始时间
		String dtEnd = "2000-03-30 23:59:59"; // 结束时间
		// 创建零售单日报表
		createDailyReport(dtStart);
		//
		RetailTradeDailyReportSummary rtdp = retrieveRetailTradeDailyReport(dtStart, dtEnd);
		assertTrue(rtdp != null, "查询不到当天日报表！");
		// 校验日报表数据
		double totalAmount = 7725.000000d;
		double totalGross = 4645.000000d;
		assertTrue((rtdp.getTotalAmount() - totalAmount) < BaseModel.TOLERANCE && (rtdp.getTotalGross() - totalGross) < BaseModel.TOLERANCE, "查询不到当天日报表！");
	}

	@Test(dependsOnMethods = "retailTradeReport2000_03_30Test")
	public void retailTradeReport2000_03_31Test() throws Exception {
		Shared.printTestMethodStartInfo();

		Shared.caseLog("2000年03月31日零售日报表测试");

		String dtStart = "2000-03-31 00:00:00"; // 开始时间
		String dtEnd = "2000-03-31 23:59:59"; // 结束时间
		// 创建零售单日报表
		createDailyReport(dtStart);
		//
		RetailTradeDailyReportSummary rtdp = retrieveRetailTradeDailyReport(dtStart, dtEnd);
		assertTrue(rtdp != null, "查询不到当天日报表！");
		// 校验日报表数据
		double totalAmount = 1536.000000d;
		double totalGross = 910.000000d;
		assertTrue((rtdp.getTotalAmount() - totalAmount) < BaseModel.TOLERANCE && (rtdp.getTotalGross() - totalGross) < BaseModel.TOLERANCE, "查询不到当天日报表！");
	}

	@Test // (dependsOnMethods = "retailTradeReport2000_03_31Test")
	public void retailTradeReport2000_03Test() throws Exception {
		Shared.printTestMethodStartInfo();

		Shared.caseLog("2000年03月零售月报表测试");
		String dtStart = "2000-03-01 00:00:00"; // 结束时间
		String dtEnd = "2000-03-31 23:59:59"; // 结束时间
		// 创建零售单月报表
		RetailTradeMonthlyReportSummary rtmrs = createRetailTradeMonthlyReport(dtEnd);
		// 检查月报表数据 (月销售总额，月总销售毛利)
		double[] saleData = { 128651.000000d, 73111.000000d };
		verifyRetailTradeMonthlySummary(rtmrs, saleData);
		// 检查日均销售数据 (销售总笔数，日均销售额，日均销售毛利，日均销售笔数)
		double[] avgDailyData = { 310, 4150.030000d, 2358.420000d, 10 };
		verifyRetailTradeMonthlyDailySummary(avgDailyData);
		// 检查月商品大类占比 (大类ID，大类销售总额，大类占比)
		String monthlyReporyByCategortParentData = "1,2,3,4,5,6,7,8,9,11,12;" //
				+ "12509.000000,33256.000000,17661.000000,4445.000000,6971.000000,5713.000000,32243.000000,6641.000000,6092.000000,1320.000000,1800.000000;" //
				+ "0.0972,0.2585,0.1373,0.0346,0.0542,0.0444,0.2506,0.0516,0.0474,0.0103,0.0140;";
		verifyRetailTradeMonthlyReportByCategoryParent(monthlyReporyByCategortParentData, dtStart, dtEnd);
		// 检查月员工业绩表汇总(员工ID, 员工月销售总额, 员工月总销售毛利, 员工月销售单数)
		String monthlyReporyByStaffData = "7,8,9,10,11;" //
				+ "22688.000000,28652.000000,32824.000000,24137.000000,20350.000000;" //
				+ "14322.000000,15225.000000,18015.000000,13389.000000,12160.000000;" //
				+ "61,56,54,73,66;";
		verifyRetailTradeMonthlyReportByStaff(monthlyReporyByStaffData, dtStart, dtEnd);
		// 验证销售量前十的商品
		String saleCommodity = "184,201,188,182,176,192,211,185,194,185;" // 商品ID
				+ "100,90,89,85,85,84,80,80,80,80;" // 销售数量
				+ "0;" // 降序排序(为0时)或升序排序(为1时)
				+ "1;"; // 根据销售金额(0)或销售数量(1)或销售毛利(2)进行排序
		verifyRetailTradeDailyReportByCommodity_RanKing(saleCommodity, dtStart, dtEnd);
	}

}
