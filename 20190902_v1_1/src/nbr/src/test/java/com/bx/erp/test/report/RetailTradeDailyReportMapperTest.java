package com.bx.erp.test.report;

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
import com.bx.erp.model.report.RetailTradeDailyReport;
import com.bx.erp.model.report.RetailTradeDailyReportByCommodity;
import com.bx.erp.model.report.RetailTradeDailyReportSummary;
import com.bx.erp.test.BaseMapperTest;
import com.bx.erp.test.Shared;
import com.bx.erp.util.DataSourceContextHolder;

public class RetailTradeDailyReportMapperTest extends BaseMapperTest {

	private SimpleDateFormat sdf;

	@BeforeClass
	public void setup() {
		super.setUp();

		sdf = new SimpleDateFormat(BaseAction.DATETIME_FORMAT_Default2);
	}

	@AfterClass
	public void tearDown() {
		super.tearDown();
	}

	private List<List<BaseModel>> createExRetailTradeDailyReport(RetailTradeDailyReport retailTradeDailyReport) {
		String err = retailTradeDailyReport.checkCreate(BaseBO.INVALID_CASE_ID); // Model中实际上没有对该字段的检查
		Assert.assertEquals(err, "");
		//
		Map<String, Object> params = retailTradeDailyReport.getCreateParamEx(BaseBO.INVALID_CASE_ID, retailTradeDailyReport);
		//
		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		List<List<BaseModel>> list = retailTradeDailyReportMapper.createEx(params);
		System.out.println("list：" + list);
		if (list.size() > 0) { // 创建成功
			Assert.assertTrue(EnumErrorCode.values()[Integer.parseInt(params.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())] == EnumErrorCode.EC_NoError, params.get(BaseAction.SP_OUT_PARAM_sErrorMsg).toString());
			//
			for (List<BaseModel> bmList : list) {
				for (BaseModel bm : bmList) {
					if (bm instanceof RetailTradeDailyReport) {
						err = ((RetailTradeDailyReport) bm).checkCreate(BaseBO.INVALID_CASE_ID);
						Assert.assertEquals(err, "");
					} else if (bm instanceof RetailTradeDailyReportSummary) {
						err = ((RetailTradeDailyReportSummary) bm).checkCreate(BaseBO.INVALID_CASE_ID);
						Assert.assertEquals(err, "");
					} else {
						if(bm != null) {
							err = ((RetailTradeDailyReportByCommodity) bm).checkCreate(BaseBO.INVALID_CASE_ID);
							Assert.assertEquals(err, "");
						}
					}
				}
			}
			//
			System.out.println("创建对象成功： " + list);
		} else { // 创建失败
			Assert.assertTrue(list.size() == 0 && EnumErrorCode.values()[Integer.parseInt(params.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())] == EnumErrorCode.EC_NoError, params.get(BaseAction.SP_OUT_PARAM_sErrorMsg).toString());
			//
			System.out.println("创建对象失败： " + list);
		}
		return list;
	}

	@Test
	public void createExTest_CASE1() throws Exception {
		Shared.printTestMethodStartInfo();

		Shared.caseLog("case1 零售单有销售记录");

		RetailTradeDailyReport retailTradeDailyReport = new RetailTradeDailyReport();
		retailTradeDailyReport.setSaleDatetime(sdf.parse("2019-1-15 00:00:00"));
		retailTradeDailyReport.setShopID(Shared.DEFAULT_Shop_ID);
		retailTradeDailyReport.setDeleteOldData(1);
		createExRetailTradeDailyReport(retailTradeDailyReport);
	}

	@Test
	public void createExTest_CASE2() throws Exception {
		Shared.printTestMethodStartInfo();

		Shared.caseLog("case2 零售单没有销售记录");

		RetailTradeDailyReport retailTradeDailyReport = new RetailTradeDailyReport();
		retailTradeDailyReport.setSaleDatetime(sdf.parse("2029-1-15 00:00:00"));
		retailTradeDailyReport.setShopID(Shared.DEFAULT_Shop_ID);
		retailTradeDailyReport.setDeleteOldData(1);
		createExRetailTradeDailyReport(retailTradeDailyReport);
	}

	@Test
	public void createExTest_CASE3() throws Exception {
		Shared.printTestMethodStartInfo();

		Shared.caseLog("case3 零售单有销售记录，也有退货单记录,有全部退货，隔天退货，部分退货");

		RetailTradeDailyReport retailTradeDailyReport = new RetailTradeDailyReport();
		retailTradeDailyReport.setSaleDatetime(sdf.parse("2019-1-16 00:00:00"));
		retailTradeDailyReport.setShopID(Shared.DEFAULT_Shop_ID);
		retailTradeDailyReport.setDeleteOldData(1);
		createExRetailTradeDailyReport(retailTradeDailyReport);
	}

	@Test
	public void createExTest_CASE4() throws Exception {
		Shared.printTestMethodStartInfo();

		Shared.caseLog("case4 零售单中商品未入库,进货价为0，毛利等于销售额");

		RetailTradeDailyReport retailTradeDailyReport = new RetailTradeDailyReport();
		retailTradeDailyReport.setSaleDatetime(sdf.parse("2019-5-19 00:00:00"));
		retailTradeDailyReport.setShopID(Shared.DEFAULT_Shop_ID);
		retailTradeDailyReport.setDeleteOldData(1);
		List<List<BaseModel>> list = createExRetailTradeDailyReport(retailTradeDailyReport);
		//
		for (BaseModel bm : list.get(1)) {
			Assert.assertTrue(((RetailTradeDailyReportSummary) bm).getTotalAmount() == ((RetailTradeDailyReportSummary) bm).getTotalGross());
		}
		//
		for (BaseModel bm : list.get(2)) {
			Assert.assertTrue(((RetailTradeDailyReportByCommodity) bm).getTotalAmount() == ((RetailTradeDailyReportByCommodity) bm).getGrossMargin());
		}
	}
	
	@Test
	public void createExTest_CASE5() throws Exception {
		Shared.printTestMethodStartInfo();

		Shared.caseLog("case5 当天没有任何零售记录进行创建日报表");

		RetailTradeDailyReport retailTradeDailyReport = new RetailTradeDailyReport();
		retailTradeDailyReport.setSaleDatetime(sdf.parse("2099-7-8 00:00:00"));
		retailTradeDailyReport.setShopID(Shared.DEFAULT_Shop_ID);
		retailTradeDailyReport.setDeleteOldData(1);
		
		Map<String, Object> params = retailTradeDailyReport.getCreateParamEx(BaseBO.INVALID_CASE_ID, retailTradeDailyReport);
		//
		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		List<List<BaseModel>> list = retailTradeDailyReportMapper.createEx(params);
		//
		for (BaseModel bm : list.get(1)) {
			Assert.assertTrue(((RetailTradeDailyReportSummary) bm).getTotalAmount() == 0);
		}
	}

}
