package com.bx.erp.test;

import static org.testng.Assert.assertTrue;

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Map;

import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import com.bx.erp.action.BaseAction;
import com.bx.erp.action.bo.BaseBO;
import com.bx.erp.model.BaseModel;
import com.bx.erp.model.ErrorInfo.EnumErrorCode;
import com.bx.erp.model.report.RetailTradeDailyReportSummary;
import com.bx.erp.util.DataSourceContextHolder;

public class RetailTradeDailyReportSummaryMapperTest extends BaseMapperTest {

	@BeforeClass
	public void setup() {
		super.setUp();
	}

	@AfterClass
	public void tearDown() {
		super.tearDown();
	}

	@Test
	public void retrieve1Test() throws Exception {
		Shared.printTestMethodStartInfo();

		System.out.println("\n------------------------ retrieveN RetailTradeDailyReport Test ------------------------");
		SimpleDateFormat sdf = new SimpleDateFormat(BaseAction.DATETIME_FORMAT_Default4);

		System.out.println("\n---------------------------------case1:查询一天的数据-----------------------------------------");
		RetailTradeDailyReportSummary dr = new RetailTradeDailyReportSummary();
		dr.setShopID(Shared.DEFAULT_Shop_ID);
		dr.setDatetimeStart(sdf.parse("2019/01/11 00:00:00"));
		dr.setDatetimeEnd(sdf.parse("2019/01/11 23:59:59"));

		Map<String, Object> params = dr.getRetrieve1ParamEx(BaseBO.INVALID_CASE_ID, dr);

		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		List<List<BaseModel>> list = retailTradeDailyReportSummaryMapper.retrieve1Ex(params);
		RetailTradeDailyReportSummary rtdrs = (RetailTradeDailyReportSummary) list.get(1).get(0);
		// float2,string2 代表的是销售最高的销售金额和商品的名称
		System.out.println("topSalesAmount:" + rtdrs.getTopSalesAmount() + ",topCommodityName" + rtdrs.getTopCommodityName());

		Assert.assertNotNull(list);
		Assert.assertTrue(EnumErrorCode.values()[Integer.parseInt(params.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())] == EnumErrorCode.EC_NoError);

		System.out.println("\n---------------------------------case2:查询一个月的数据-----------------------------------------");
		RetailTradeDailyReportSummary dr2 = new RetailTradeDailyReportSummary();
		dr2.setShopID(Shared.DEFAULT_Shop_ID);
		dr2.setDatetimeStart(sdf.parse("2019/01/01 00:00:00"));
		dr2.setDatetimeEnd(sdf.parse("2019/01/31 23:59:59"));

		Map<String, Object> params2 = dr2.getRetrieve1ParamEx(BaseBO.INVALID_CASE_ID, dr2);

		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		List<List<BaseModel>> list2 = retailTradeDailyReportSummaryMapper.retrieve1Ex(params2);
		rtdrs = (RetailTradeDailyReportSummary) list2.get(1).get(0);
		System.out.println("topSalesAmount:" + rtdrs.getTopSalesAmount() + ",topCommodityName" + rtdrs.getTopCommodityName());

		Assert.assertNotNull(list2);
		Assert.assertTrue(EnumErrorCode.values()[Integer.parseInt(params2.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())] == EnumErrorCode.EC_NoError);

		System.out.println("\n---------------------------------case3:查询某一时间段的数据-----------------------------------------");
		RetailTradeDailyReportSummary dr3 = new RetailTradeDailyReportSummary();
		dr3.setShopID(Shared.DEFAULT_Shop_ID);
		dr3.setDatetimeStart(sdf.parse("2019/01/11 00:00:00"));
		dr3.setDatetimeEnd(sdf.parse("2019/01/14 23:59:59"));

		Map<String, Object> params3 = dr3.getRetrieve1ParamEx(BaseBO.INVALID_CASE_ID, dr3);

		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		List<List<BaseModel>> list3 = retailTradeDailyReportSummaryMapper.retrieve1Ex(params3);
		rtdrs = (RetailTradeDailyReportSummary) list3.get(1).get(0);
		System.out.println("topSalesAmount:" + rtdrs.getTopSalesAmount() + ",topCommodityName" + rtdrs.getTopCommodityName());

		Assert.assertNotNull(list3);
		Assert.assertTrue(EnumErrorCode.values()[Integer.parseInt(params3.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())] == EnumErrorCode.EC_NoError);

		System.out.println("\n---------------------------------case4:查询一个不存在时间的数据-----------------------------------------");
		RetailTradeDailyReportSummary dr4 = new RetailTradeDailyReportSummary();
		dr4.setShopID(Shared.DEFAULT_Shop_ID);
		dr4.setDatetimeStart(sdf.parse("2020/01/01 00:00:00"));
		dr4.setDatetimeEnd(sdf.parse("2020/01/31 23:59:59"));

		Map<String, Object> params4 = dr4.getRetrieve1ParamEx(BaseBO.INVALID_CASE_ID, dr4);

		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		List<List<BaseModel>> list4 = retailTradeDailyReportSummaryMapper.retrieve1Ex(params4);

		assertTrue(list4.size() == 0);
		Assert.assertTrue(EnumErrorCode.values()[Integer.parseInt(params4.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())] == EnumErrorCode.EC_NoSuchData);
		
		System.out.println("\n---------------------------------case5:查询一个不存在时间的数据-----------------------------------------");
		RetailTradeDailyReportSummary dr5 = new RetailTradeDailyReportSummary();
		dr5.setShopID(BaseAction.INVALID_ID);
		dr5.setDatetimeStart(sdf.parse("2020/01/01 00:00:00"));
		dr5.setDatetimeEnd(sdf.parse("2020/01/31 23:59:59"));

		Map<String, Object> params5 = dr5.getRetrieve1ParamEx(BaseBO.INVALID_CASE_ID, dr5);

		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		List<List<BaseModel>> list5 = retailTradeDailyReportSummaryMapper.retrieve1Ex(params5);

		assertTrue(list5.size() == 0);
		Assert.assertTrue(EnumErrorCode.values()[Integer.parseInt(params5.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())] == EnumErrorCode.EC_NoSuchData);

	}

	@Test
	public void retrieveNForChartTest() throws Exception {
		Shared.printTestMethodStartInfo();

		System.out.println("\n------------------------ retrieveNForChart RetailTradeDailyReport Test ------------------------");
		SimpleDateFormat sdf = new SimpleDateFormat(BaseAction.DATETIME_FORMAT_Default4);

		System.out.println("\n---------------------------------case1:从 商品零售日报表里查询一天的销售总额、销售毛利和日期-----------------------------------------");
		RetailTradeDailyReportSummary dr1 = new RetailTradeDailyReportSummary();
		dr1.setDatetimeStart(sdf.parse("2019/01/14 00:00:00"));
		dr1.setDatetimeEnd(sdf.parse("2019/01/14 23:59:59"));
		dr1.setShopID(Shared.DEFAULT_Shop_ID);

		Map<String, Object> params1 = dr1.getRetrieveNParamEx(BaseBO.CASE_RetailTradeDailyReportSummary_RetrieveNForChart, dr1);

		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		List<BaseModel> list1 = retailTradeDailyReportSummaryMapper.retrieveNForChart(params1);
		System.out.println("返回的对象集合：" + list1.toString());
		System.out.println("每天的日期、销售总额、销售毛利");
		for (BaseModel bm : list1) {
			RetailTradeDailyReportSummary dr = (RetailTradeDailyReportSummary) bm;
			System.out.println("日期：" + dr.getDateTime() + ",销售总额：" + dr.getTotalAmount() + ",销售毛利:" + dr.getTotalGross());
		}

		Assert.assertNotNull(list1);
		Assert.assertTrue(EnumErrorCode.values()[Integer.parseInt(params1.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())] == EnumErrorCode.EC_NoError);

		System.out.println("\n---------------------------------case2:从 商品零售日报表里查询一个月内的销售总额、销售毛利和日期-----------------------------------------");
		RetailTradeDailyReportSummary dr2 = new RetailTradeDailyReportSummary();
		dr2.setDatetimeStart(sdf.parse("2019/01/01 00:00:00"));
		dr2.setDatetimeEnd(sdf.parse("2019/01/31 23:59:59"));
		dr2.setShopID(Shared.DEFAULT_Shop_ID);

		Map<String, Object> params2 = dr2.getRetrieveNParamEx(BaseBO.CASE_RetailTradeDailyReportSummary_RetrieveNForChart, dr2);

		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		List<BaseModel> list2 = retailTradeDailyReportSummaryMapper.retrieveNForChart(params2);
		System.out.println("返回的对象集合：" + list2.toString());
		System.out.println("每天的日期、销售总额、销售毛利");
		for (BaseModel bm : list2) {
			RetailTradeDailyReportSummary dr = (RetailTradeDailyReportSummary) bm;
			System.out.println("日期：" + dr.getDateTime() + ",销售总额：" + dr.getTotalAmount() + ",销售毛利:" + dr.getTotalGross());
		}

		Assert.assertNotNull(list2);
		Assert.assertTrue(EnumErrorCode.values()[Integer.parseInt(params2.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())] == EnumErrorCode.EC_NoError);

		System.out.println("\n---------------------------------case3:从 商品零售日报表里查询不存在的时间内的销售总额、销售毛利和日期-----------------------------------------");
		RetailTradeDailyReportSummary dr3 = new RetailTradeDailyReportSummary();
		dr3.setDatetimeStart(sdf.parse("2099/01/01 00:00:00"));
		dr3.setDatetimeEnd(sdf.parse("2099/01/31 23:59:59"));
		dr3.setShopID(Shared.DEFAULT_Shop_ID);
		
		Map<String, Object> params3 = dr3.getRetrieveNParamEx(BaseBO.CASE_RetailTradeDailyReportSummary_RetrieveNForChart, dr3);

		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		List<BaseModel> list3 = retailTradeDailyReportSummaryMapper.retrieveNForChart(params3);
		System.out.println("返回的对象集合：" + list3.toString());
		System.out.println("每天的日期、销售总额、销售毛利");
		for (BaseModel bm : list3) {
			RetailTradeDailyReportSummary dr = (RetailTradeDailyReportSummary) bm;
			System.out.println("日期：" + dr.getDateTime() + ",销售总额：" + dr.getTotalAmount() + ",销售毛利:" + dr.getTotalGross());
		}

		Assert.assertNotNull(list3);
		Assert.assertTrue(EnumErrorCode.values()[Integer.parseInt(params3.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())] == EnumErrorCode.EC_NoError);
	
		System.out.println("\n---------------------------------case4:查询一个不存在的门店ID的销售总额和销售毛利-----------------------------------------");
		RetailTradeDailyReportSummary dr4 = new RetailTradeDailyReportSummary();
		dr4.setDatetimeStart(sdf.parse("2019/01/01 00:00:00"));
		dr4.setDatetimeEnd(sdf.parse("2019/01/31 23:59:59"));
		dr4.setShopID(BaseAction.INVALID_ID);
		
		Map<String, Object> params4 = dr4.getRetrieveNParamEx(BaseBO.CASE_RetailTradeDailyReportSummary_RetrieveNForChart, dr4);

		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		List<BaseModel> list4 = retailTradeDailyReportSummaryMapper.retrieveNForChart(params4);
		System.out.println("返回的对象集合：" + list4.toString());
		System.out.println("每天的日期、销售总额、销售毛利");

		Assert.assertNotNull(list4);
		Assert.assertTrue(EnumErrorCode.values()[Integer.parseInt(params4.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())] == EnumErrorCode.EC_NoError);

	}
}
