package com.bx.erp.test.report;

import static org.testng.Assert.assertEquals;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.springframework.test.context.web.WebAppConfiguration;
import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import com.bx.erp.action.BaseAction;
import com.bx.erp.action.bo.BaseBO;
import com.bx.erp.model.Barcodes;
import com.bx.erp.model.BaseModel;
import com.bx.erp.model.ErrorInfo.EnumErrorCode;
import com.bx.erp.model.RetailTrade;
import com.bx.erp.model.RetailTradeCommodity;
import com.bx.erp.model.commodity.Commodity;
import com.bx.erp.model.report.RetailTradeDailyReport;
import com.bx.erp.model.report.RetailTradeDailyReportByCommodity;
import com.bx.erp.model.report.RetailTradeDailyReportSummary;
import com.bx.erp.test.BaseCommodityTest;
import com.bx.erp.test.BaseMapperTest;
import com.bx.erp.test.BaseRetailTradeTest;
import com.bx.erp.test.Shared;
import com.bx.erp.util.DataSourceContextHolder;

@WebAppConfiguration
public class RetailTradeDailyReportByCommodityMapperTest extends BaseMapperTest {

	private DateFormat df;

	/** -1代表查询所有的商品分类 */
	public static final int RETRIEVEN_ALL_COMMODITY_CATEGORY = -1;

	/** 按照类别ID为1进行查询 */
	public static final int RETRIEVEN_BY_COMMODITY_CATEGORY_ID1 = 1;

	/** 不显示销售数量为0的商品 */
	public static final int IGNORE_ZERO_NO = 1;

	@BeforeClass
	public void setup() {
		super.setUp();

		df = new SimpleDateFormat(BaseAction.DATETIME_FORMAT_Default2);
	}

	@AfterClass
	public void tearDown() {
		super.tearDown();
	}

	public static class DataInput {
		private static RetailTradeDailyReportByCommodity reportInput = null;

		protected static final RetailTradeDailyReportByCommodity getReport() throws ParseException, CloneNotSupportedException {
			DateFormat df = new SimpleDateFormat(BaseAction.DATETIME_FORMAT_Default2);
			String dt1 = "2018-02-11 00:00:00";
			String dt2 = "2018-02-11 23:59:59";
			reportInput = new RetailTradeDailyReportByCommodity();
			reportInput.setDatetimeStart(df.parse(dt1));
			reportInput.setDatetimeEnd(df.parse(dt2));
			reportInput.setDatetime(new Date());
			reportInput.setiOrderBy(0);
			reportInput.setIsASC(0);
			reportInput.setQueryKeyword("");
			reportInput.setiCategoryID(RETRIEVEN_ALL_COMMODITY_CATEGORY);
			reportInput.setPageIndex(1);
			reportInput.setPageSize(100);
			reportInput.setShopID(Shared.DEFAULT_Shop_ID);

			return (RetailTradeDailyReportByCommodity) reportInput.clone();
		}

	}

	protected List<BaseModel> rNRetailTradeDailyReportByCommodity(RetailTradeDailyReportByCommodity r) {
		Map<String, Object> params = r.getRetrieveNParam(BaseBO.INVALID_CASE_ID, r);
		//
		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		List<BaseModel> list = retailTradeDailyReportByCommodityMapper.retrieveN(params);
		//
		Assert.assertTrue(list.size() >= 0 && EnumErrorCode.values()[Integer.parseInt(params.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())] == EnumErrorCode.EC_NoError, //
				params.get(BaseAction.SP_OUT_PARAM_sErrorMsg).toString());
		//
		for (int i = 0; i < list.size(); i++) {
			String err = ((RetailTradeDailyReportByCommodity) list.get(i)).checkCreate(BaseBO.INVALID_CASE_ID);
			Assert.assertEquals(err, "");
		}
		//
		System.out.println("查询对象成功： " + list);
		//
		return list;
	}

	@Test
	public void retrieveNTest_CASE1() throws ParseException, CloneNotSupportedException {
		Shared.printTestMethodStartInfo();

		Shared.caseLog("CASE1: 按商品销售金额降序排列 返回错误码为0");

		RetailTradeDailyReportByCommodity r = DataInput.getReport();
		List<BaseModel> list = rNRetailTradeDailyReportByCommodity(r);

		if (list.size() > 1) {
			double d1 = ((RetailTradeDailyReportByCommodity) list.get(0)).getTotalAmount();
			double d2 = ((RetailTradeDailyReportByCommodity) list.get(1)).getTotalAmount();
			Assert.assertTrue(d1 >= d2, "查询的数据的商品销售金额不是降序排列");
		}
	}

	@Test
	public void retrieveNTest_CASE2() throws ParseException, CloneNotSupportedException {
		Shared.printTestMethodStartInfo();

		Shared.caseLog("Case2: 按商品销售金额升序排列 ");

		RetailTradeDailyReportByCommodity r = DataInput.getReport();
		r.setIsASC(1);
		List<BaseModel> list = rNRetailTradeDailyReportByCommodity(r);

		if (list.size() > 1) {
			double d1 = ((RetailTradeDailyReportByCommodity) list.get(0)).getTotalAmount();
			double d2 = ((RetailTradeDailyReportByCommodity) list.get(1)).getTotalAmount();
			Assert.assertTrue(d1 <= d2, "查询的数据的商品销售金额不是升序排列");
		}
	}

	@Test
	public void retrieveNTest_CASE3() throws ParseException, CloneNotSupportedException {
		Shared.printTestMethodStartInfo();

		Shared.caseLog(" Case3:按销售数量降序排列 ");

		RetailTradeDailyReportByCommodity r = DataInput.getReport();
		r.setiOrderBy(1);
		List<BaseModel> list = rNRetailTradeDailyReportByCommodity(r);

		if (list.size() > 1) {
			Integer i1 = ((RetailTradeDailyReportByCommodity) list.get(0)).getNO();
			Integer i2 = ((RetailTradeDailyReportByCommodity) list.get(1)).getNO();
			Assert.assertTrue(i1 >= i2, "查询的数据的销售数量不是降序排列 ");
		}
	}

	@Test
	public void retrieveNTest_CASE4() throws ParseException, CloneNotSupportedException {
		Shared.printTestMethodStartInfo();

		Shared.caseLog(" Case4: 按毛利降序排列 ");

		RetailTradeDailyReportByCommodity r = DataInput.getReport();
		r.setiOrderBy(2);
		List<BaseModel> list = rNRetailTradeDailyReportByCommodity(r);

		Assert.assertNotNull(list);

		if (list.size() > 1) {
			double d1 = ((RetailTradeDailyReportByCommodity) list.get(0)).getGrossMargin();
			double d2 = ((RetailTradeDailyReportByCommodity) list.get(1)).getGrossMargin();
			Assert.assertTrue(d1 >= d2, "查询的数据的毛利不是降序排列");
		}
	}

	@Test
	public void retrieveNTest_CASE5() throws ParseException, CloneNotSupportedException {
		Shared.printTestMethodStartInfo();

		Shared.caseLog(" Case5: 按名称进行模糊查询并按金额降序排列 ");

		RetailTradeDailyReportByCommodity r = DataInput.getReport();
		r.setQueryKeyword("可");
		List<BaseModel> list = rNRetailTradeDailyReportByCommodity(r);

		if (list.size() > 1) {
			double d1 = ((RetailTradeDailyReportByCommodity) list.get(0)).getTotalAmount();
			double d2 = ((RetailTradeDailyReportByCommodity) list.get(1)).getTotalAmount();
			Assert.assertTrue(d1 >= d2, "查询的数据的金额不是降序排列");
		}
	}

	@Test
	public void retrieveNTest_CASE6() throws ParseException, CloneNotSupportedException {
		Shared.printTestMethodStartInfo();

		Shared.caseLog("Case6: 按条形码进行精确查询");

		RetailTradeDailyReportByCommodity r = DataInput.getReport();
		r.setQueryKeyword("3548293894545");
		rNRetailTradeDailyReportByCommodity(r);
	}

	@Test
	public void retrieveNTest_CASE7() throws ParseException, CloneNotSupportedException {
		Shared.printTestMethodStartInfo();

		Shared.caseLog(" Case7: 按单号进行查询 ");

		RetailTradeDailyReportByCommodity r = DataInput.getReport();
		r.setQueryKeyword("1");
		rNRetailTradeDailyReportByCommodity(r);
	}

	@Test
	public void retrieveNTest_CASE8() throws ParseException, CloneNotSupportedException {
		Shared.printTestMethodStartInfo();

		Shared.caseLog(" Case8: bIgnoreZeroNO=1忽略0进货量 ");

		RetailTradeDailyReportByCommodity r = DataInput.getReport();
		r.setbIgnoreZeroNO(IGNORE_ZERO_NO);// 1不显示销售数量为0的商品
		rNRetailTradeDailyReportByCommodity(r);
	}

	@Test
	public void retrieveNTest_CASE9() throws ParseException, CloneNotSupportedException {
		Shared.printTestMethodStartInfo();

		Shared.caseLog(" Case9: 按照类别进行查询 ");

		RetailTradeDailyReportByCommodity r = DataInput.getReport();
		r.setiCategoryID(RETRIEVEN_BY_COMMODITY_CATEGORY_ID1);
		rNRetailTradeDailyReportByCommodity(r);
	}

	@Test
	public void retrieveNTest_CASE10() throws ParseException, CloneNotSupportedException {
		Shared.printTestMethodStartInfo();

		Shared.caseLog("CASE10: 查询一段时间的数据 按商品销售金额降序排列 返回错误码为0 ");

		RetailTradeDailyReportByCommodity r = DataInput.getReport();
		r.setDatetimeStart(df.parse("2018-02-11 00:00:00"));
		r.setDatetimeEnd(df.parse("2018-02-14 23:59:59"));
		List<BaseModel> list = rNRetailTradeDailyReportByCommodity(r);
		if (list.size() > 1) {
			double d1 = ((RetailTradeDailyReportByCommodity) list.get(0)).getTotalAmount();
			double d2 = ((RetailTradeDailyReportByCommodity) list.get(1)).getTotalAmount();
			Assert.assertTrue(d1 >= d2, "查询的数据的商品销售金额不是降序排列");
		}
	}

	@Test
	public void retrieveNTest_CASE11() throws ParseException, CloneNotSupportedException {
		Shared.printTestMethodStartInfo();

		Shared.caseLog("CASE11: 查询一段时间的数据 按商品销售金额降序排列 返回错误码为0");

		RetailTradeDailyReportByCommodity r = DataInput.getReport();
		r.setDatetimeStart(df.parse("2025-02-11 00:00:00"));
		r.setDatetimeEnd(df.parse("2025-02-14 23:59:59"));
		rNRetailTradeDailyReportByCommodity(r);
	}

	@Test
	public void retrieveNTest_CASE12() throws CloneNotSupportedException, InterruptedException, ParseException {
		Shared.printTestMethodStartInfo();

		Shared.caseLog("Case12:根据输入_特殊字符商品名称查询 商品零售日报表");
		// 创建商品
		Commodity comm1 = BaseCommodityTest.DataInput.getCommodity();
		comm1.setName("零售营养快_kahd" + System.currentTimeMillis());
		List<List<BaseModel>> bmList = BaseCommodityTest.createCommodityViaMapper(BaseBO.CASE_Commodity_CreateSingle, comm1);
		Commodity commCreated = (Commodity) bmList.get(0).get(0);
		Barcodes barcodes = (Barcodes) bmList.get(1).get(0);
		// 创建零售单
		RetailTrade rt1 = BaseRetailTradeTest.DataInput.getRetailTrade();
		RetailTrade retailTrade = BaseRetailTradeTest.createRetailTrade(rt1);
		// 创建零售单商品
		RetailTradeCommodity rtc1 = BaseRetailTradeTest.DataInput.getRetailTradeCommodity();
		rtc1.setCommodityID(commCreated.getID());
		rtc1.setOperatorStaffID(4);
		rtc1.setNO(1);
		rtc1.setBarcodeID(barcodes.getID());
		rtc1.setTradeID(retailTrade.getID());

		// 创建日报表
		RetailTradeDailyReport retailTradeDailyReport = new RetailTradeDailyReport();
		retailTradeDailyReport.setSaleDatetime(df.parse(df.format(new Date())));
		retailTradeDailyReport.setDeleteOldData(1);
		retailTradeDailyReport.setShopID(Shared.DEFAULT_Shop_ID);
		//
		String errorMsg = retailTradeDailyReport.checkCreate(BaseBO.INVALID_CASE_ID);
		assertEquals(errorMsg, "");
		//
		createExRetailTradeDailyReport(retailTradeDailyReport);

		Map<String, Object> params = rtc1.getCreateParam(BaseBO.INVALID_CASE_ID, rtc1);
		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		RetailTradeCommodity rtcCreate = (RetailTradeCommodity) retailTradeCommodityMapper.create(params);
		rtc1.setIgnoreIDInComparision(true);
		if (rtc1.compareTo(rtcCreate) != 0) {
			Assert.assertTrue(false, "创建的对象的字段与DB读出的不相等");
		}
		Assert.assertTrue(rtcCreate != null && EnumErrorCode.values()[Integer.parseInt(params.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())] == EnumErrorCode.EC_NoError, "创建对象失败");
		//
		errorMsg = rtcCreate.checkCreate(BaseBO.INVALID_CASE_ID);
		assertEquals(errorMsg, "");
		// 模糊查询日报表
		RetailTradeDailyReportByCommodity retailTradeDailyReportByCommodity = new RetailTradeDailyReportByCommodity();
		retailTradeDailyReportByCommodity.setQueryKeyword("_");
		retailTradeDailyReportByCommodity.setiCategoryID(-1);
		retailTradeDailyReportByCommodity.setDatetimeStart(df.parse("2000-01-01 00:00:00"));
		retailTradeDailyReportByCommodity.setDatetimeEnd(df.parse(df.format(new Date())));
		retailTradeDailyReportByCommodity.setShopID(Shared.DEFAULT_Shop_ID);
		// 查询出来日报表对比商品名称
		List<BaseModel> list = rNRetailTradeDailyReportByCommodity(retailTradeDailyReportByCommodity);
		for (BaseModel rdrbcResult : list) {
			RetailTradeDailyReportByCommodity retailTradeDailyReportByCommodity2 = (RetailTradeDailyReportByCommodity) rdrbcResult;
			Assert.assertTrue(retailTradeDailyReportByCommodity2.getName().contains("_"), "查询商品名称没有包含_特殊字符");
		}

	}


	private List<List<BaseModel>> createExRetailTradeDailyReport(RetailTradeDailyReport retailTradeDailyReport) {
		String err = retailTradeDailyReport.checkCreate(BaseBO.INVALID_CASE_ID); // Model中实际上没有对该字段的检查
		Assert.assertEquals(err, "");
		//
		Map<String, Object> params = retailTradeDailyReport.getCreateParamEx(BaseBO.INVALID_CASE_ID, retailTradeDailyReport);
		//
		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		List<List<BaseModel>> list = retailTradeDailyReportMapper.createEx(params);
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
						err = ((RetailTradeDailyReportByCommodity) bm).checkCreate(BaseBO.INVALID_CASE_ID);
						Assert.assertEquals(err, "");
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

}
