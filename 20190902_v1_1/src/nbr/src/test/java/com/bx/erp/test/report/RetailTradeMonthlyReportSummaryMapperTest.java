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
import com.bx.erp.model.report.RetailTradeMonthlyReportSummary;
import com.bx.erp.test.BaseMapperTest;
import com.bx.erp.test.Shared;
import com.bx.erp.util.DataSourceContextHolder;

public class RetailTradeMonthlyReportSummaryMapperTest extends BaseMapperTest {

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

	protected RetailTradeMonthlyReportSummary createRetailTradeMonthlyReportSummary(RetailTradeMonthlyReportSummary retailTradeMonthlyReportSummary) {
		String err = retailTradeMonthlyReportSummary.checkCreate(BaseBO.INVALID_CASE_ID); // Model中实际上没有对该字段的检查
		Assert.assertEquals(err, "");
		//
		Map<String, Object> paramsCreate = retailTradeMonthlyReportSummary.getCreateParam(BaseBO.INVALID_CASE_ID, retailTradeMonthlyReportSummary);
		//
		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		RetailTradeMonthlyReportSummary rtmrsCreate = (RetailTradeMonthlyReportSummary) retailTradeMonthlyReportSummaryMapper.create(paramsCreate);
		//
		Map<String, Object> paramsRN = retailTradeMonthlyReportSummary.getRetrieveNParam(BaseBO.INVALID_CASE_ID, retailTradeMonthlyReportSummary);
		//
		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		List<BaseModel> list = retailTradeMonthlyReportSummaryMapper.retrieveN(paramsRN);
		//
		if (rtmrsCreate != null || list.size() != 0) { // 当传入时间段没有销售数据时，月报查询为空，当不为空时，验证数据合法性，为空时，证明没有数据，不进行验证数据合法性
			Assert.assertTrue(EnumErrorCode.values()[Integer.parseInt(paramsRN.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())] == EnumErrorCode.EC_NoError, paramsRN.get(BaseAction.SP_OUT_PARAM_sErrorMsg).toString());
			for (int i = 0; i < list.size(); i++) {
				err = ((RetailTradeMonthlyReportSummary) list.get(i)).checkCreate(BaseBO.INVALID_CASE_ID);
				Assert.assertEquals(err, "");
			}
			//
			System.out.println("创建月报表成功: " + list);
		} else { //
			Assert.assertTrue(rtmrsCreate == null && EnumErrorCode.values()[Integer.parseInt(paramsCreate.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())] == EnumErrorCode.EC_BusinessLogicNotDefined,
					paramsCreate.get(BaseAction.SP_OUT_PARAM_sErrorMsg).toString());
			//
			System.out.println("创建月报表失败 ： " + paramsCreate.get("sErrorMsg"));
		}
		return rtmrsCreate;
	}

	protected List<BaseModel> retrieveNRetailTradeMonthlyReportSummary(RetailTradeMonthlyReportSummary retailTradeMonthlyReportSummary) {
		Map<String, Object> params = retailTradeMonthlyReportSummary.getRetrieveNParam(BaseBO.INVALID_CASE_ID, retailTradeMonthlyReportSummary);
		//
		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		List<BaseModel> list = retailTradeMonthlyReportSummaryMapper.retrieveN(params);
		if (list.size() != 0) { // 查询成功
			Assert.assertTrue(list.size() > 0//
					&& EnumErrorCode.values()[Integer.parseInt(params.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())] == EnumErrorCode.EC_NoError, //
					params.get(BaseAction.SP_OUT_PARAM_sErrorMsg).toString());
			String err;
			for (int i = 0; i < list.size(); i++) {
				err = ((RetailTradeMonthlyReportSummary) list.get(i)).checkCreate(BaseBO.INVALID_CASE_ID);
				Assert.assertEquals(err, "");
			}
			//
			System.out.println("查询对象成功 ：" + list);
		} else { // 查询到0条数据或查询失败
			Assert.assertTrue(list.size() == 0//
					&& EnumErrorCode.values()[Integer.parseInt(params.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())] == EnumErrorCode.EC_NoError, //
					params.get(BaseAction.SP_OUT_PARAM_sErrorMsg).toString());
			//
			System.out.println("查询到0条数据 ：" + list + "错误码是： " + params.get("sErrorMsg"));
		}
		return list;
	}

	@Test
	public void createTest_CASE1() throws Exception {
		Shared.printTestMethodStartInfo();

		Shared.caseLog("Case1:正常添加  ");

		RetailTradeMonthlyReportSummary retailTradeMonthlyReportSummary = new RetailTradeMonthlyReportSummary();
		retailTradeMonthlyReportSummary.setDatetimeStart(sdf.parse("2019-01-01 00:00:00"));
		retailTradeMonthlyReportSummary.setDatetimeEnd(sdf.parse("2019-01-31 23:59:59"));
		retailTradeMonthlyReportSummary.setShopID(Shared.DEFAULT_Shop_ID);
		createRetailTradeMonthlyReportSummary(retailTradeMonthlyReportSummary);
	}

	@Test
	public void createTest_CASE2() throws Exception {
		Shared.printTestMethodStartInfo();

		Shared.caseLog("Case2:添加不存在的时间 (根据日报创建月报，当日报没有数据不会创建当月的月报)");

		RetailTradeMonthlyReportSummary retailTradeMonthlyReportSummary = new RetailTradeMonthlyReportSummary();
		retailTradeMonthlyReportSummary.setDatetimeStart(sdf.parse("2099-01-01 00:00:00"));
		retailTradeMonthlyReportSummary.setDatetimeEnd(sdf.parse("2099-01-31 23:59:59"));
		retailTradeMonthlyReportSummary.setShopID(Shared.DEFAULT_Shop_ID);
		createRetailTradeMonthlyReportSummary(retailTradeMonthlyReportSummary);
	}

	@Test
	public void createTest_CASE3() throws Exception {
		Shared.printTestMethodStartInfo();

		Shared.caseLog("Case3:添加一个时间不存在销售数据");

		RetailTradeMonthlyReportSummary retailTradeMonthlyReportSummary = new RetailTradeMonthlyReportSummary();
		retailTradeMonthlyReportSummary.setDatetimeStart(sdf.parse("1970-04-06 00:00:00"));
		retailTradeMonthlyReportSummary.setDatetimeEnd(sdf.parse("1970-04-30 23:59:59"));
		retailTradeMonthlyReportSummary.setShopID(Shared.DEFAULT_Shop_ID);
		createRetailTradeMonthlyReportSummary(retailTradeMonthlyReportSummary);
	}

	@Test
	public void retrieveNTest_CASE1() throws Exception {
		Shared.printTestMethodStartInfo();

		Shared.caseLog("Case1:正常查询");

		RetailTradeMonthlyReportSummary retailTradeMonthlyReportSummary = new RetailTradeMonthlyReportSummary();
		retailTradeMonthlyReportSummary.setDatetimeStart(sdf.parse("2019-1-01 00:00:00"));
		retailTradeMonthlyReportSummary.setDatetimeEnd(sdf.parse("2019-12-31 23:59:59"));
		retailTradeMonthlyReportSummary.setShopID(Shared.DEFAULT_Shop_ID);
		retrieveNRetailTradeMonthlyReportSummary(retailTradeMonthlyReportSummary);
	}

	@Test
	public void retrieveNTest_CASE2() throws Exception {
		Shared.printTestMethodStartInfo();

		Shared.caseLog("Case2:查询不存在的时间");

		RetailTradeMonthlyReportSummary retailTradeMonthlyReportSummary = new RetailTradeMonthlyReportSummary();
		retailTradeMonthlyReportSummary.setDatetimeStart(sdf.parse("2099-1-01 00:00:00"));
		retailTradeMonthlyReportSummary.setDatetimeEnd(sdf.parse("2099-12-31 23:59:59"));
		retailTradeMonthlyReportSummary.setShopID(Shared.DEFAULT_Shop_ID);
		retrieveNRetailTradeMonthlyReportSummary(retailTradeMonthlyReportSummary);
	}
	
	@Test
	public void retrieveNTest_CASE3() throws Exception {
		Shared.printTestMethodStartInfo();

		Shared.caseLog("Case3:查询不存在的门店ID");

		RetailTradeMonthlyReportSummary retailTradeMonthlyReportSummary = new RetailTradeMonthlyReportSummary();
		retailTradeMonthlyReportSummary.setDatetimeStart(sdf.parse("2099-1-01 00:00:00"));
		retailTradeMonthlyReportSummary.setDatetimeEnd(sdf.parse("2099-12-31 23:59:59"));
		retailTradeMonthlyReportSummary.setShopID(BaseAction.INVALID_ID);
		retrieveNRetailTradeMonthlyReportSummary(retailTradeMonthlyReportSummary);
	}

}
