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
import com.bx.erp.model.RetailTrade;
import com.bx.erp.model.RetailTradeCommodity;
import com.bx.erp.model.CommodityType.EnumCommodityType;
import com.bx.erp.model.ErrorInfo.EnumErrorCode;
import com.bx.erp.model.commodity.Commodity;
import com.bx.erp.model.report.RetailTradeReportByCommodity;
import com.bx.erp.test.BaseCommodityTest;
import com.bx.erp.test.BaseMapperTest;
import com.bx.erp.test.BaseRetailTradeTest;
import com.bx.erp.test.Shared;
import com.bx.erp.util.DataSourceContextHolder;

@WebAppConfiguration
public class RetailTradeReportByCommodityMapperTest extends BaseMapperTest {

	/** -1代表查询所有的商品分类 */
	public static final int RETRIEVEN_ALL_COMMODITY_CATEGORY = -1;

	/** 按照类别ID为1进行查询 */
	public static final int RETRIEVEN_BY_COMMODITY_CATEGORY_ID1 = 1;

	/** 不显示销售数量为0的商品 */
	public static final int IGNORE_ZERO_NO = 1;

	@BeforeClass
	public void setup() {

		super.setUp();
	}

	@AfterClass
	public void tearDown() {

		super.tearDown();
	}

	public static class DataInput {
		private static RetailTradeReportByCommodity reportInput = null;

		protected static final RetailTradeReportByCommodity getReport() throws ParseException, CloneNotSupportedException {
			DateFormat df = new SimpleDateFormat(BaseAction.DATETIME_FORMAT_Default2);
			String dt1 = "2018-01-01 00:00:00";
			String dt2 = "2020-11-17 00:00:00";
			reportInput = new RetailTradeReportByCommodity();
			reportInput.setDatetimeStart(df.parse(dt1));
			reportInput.setDatetimeEnd(df.parse(dt2));
			reportInput.setSaleDatetime(new Date());
			reportInput.setiOrderBy(0);
			reportInput.setIsASC(0);
			reportInput.setQueryKeyword("");
			reportInput.setiCategoryID(RETRIEVEN_ALL_COMMODITY_CATEGORY);
			reportInput.setPageIndex(1);
			reportInput.setPageSize(100);

			return (RetailTradeReportByCommodity) reportInput.clone();
		}
	}

	protected List<BaseModel> retrieveNRetailTradeReportByCommodity(RetailTradeReportByCommodity r) {
		Map<String, Object> params = r.getRetrieveNParam(BaseBO.INVALID_CASE_ID, r);
		//
		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		List<BaseModel> list = retailTradeReportByCommodityMapper.retrieveN(params);
		//
		if (list.size() != 0) { // 根据条件有查询到数据，检查字段的合法性
			Assert.assertTrue(list.size() > 0 && EnumErrorCode.values()[Integer.parseInt(params.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())] == EnumErrorCode.EC_NoError, //
					params.get(BaseAction.SP_OUT_PARAM_sErrorMsg).toString());
			String err;
			for (int i = 0; i < list.size(); i++) {
				err = ((RetailTradeReportByCommodity) list.get(i)).checkCreate(BaseBO.INVALID_CASE_ID);
				Assert.assertEquals(err, "");
			}
			//
			System.out.println("查询数据成功: " + list);
		} else { // 根据条件没有查询到数据，则不检查字段的合法性
			Assert.assertTrue(list.size() == 0 && EnumErrorCode.values()[Integer.parseInt(params.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())] == EnumErrorCode.EC_NoError, //
					params.get(BaseAction.SP_OUT_PARAM_sErrorMsg).toString());
			//
			System.out.println("查询到0条数据: " + list);
		}
		//
		return list;
	}

	@Test
	public void retrieveNTest_CASE1() throws ParseException, CloneNotSupportedException {
		Shared.printTestMethodStartInfo();

		Shared.caseLog("CASE1: 按商品销售金额降序排列 返回错误码为0");

		RetailTradeReportByCommodity r = DataInput.getReport();
		List<BaseModel> list = retrieveNRetailTradeReportByCommodity(r);
		//
		if (list.size() > 1) {
			double d1 = ((RetailTradeReportByCommodity) list.get(0)).getAmount();
			double d2 = ((RetailTradeReportByCommodity) list.get(1)).getAmount();
			Assert.assertTrue(d1 >= d2, "查询的数据的商品销售金额不是降序排列");
		}
	}

	@Test
	public void retrieveNTest_CASE2() throws ParseException, CloneNotSupportedException {
		Shared.printTestMethodStartInfo();

		Shared.caseLog("CASE2: 按商品销售金额升序排列 返回错误码为0");

		RetailTradeReportByCommodity r = DataInput.getReport();
		r.setIsASC(1);
		r.setiOrderBy(0);
		List<BaseModel> list = retrieveNRetailTradeReportByCommodity(r);
		//
		if (list.size() > 1) {
			double d1 = ((RetailTradeReportByCommodity) list.get(0)).getAmount();
			double d2 = ((RetailTradeReportByCommodity) list.get(1)).getAmount();
			Assert.assertTrue(d1 <= d2, "查询的数据的商品销售金额不是升序排列");
		}
	}

	@Test
	public void retrieveNTest_CASE3() throws ParseException, CloneNotSupportedException {
		Shared.printTestMethodStartInfo();

		Shared.caseLog("CASE3: 按销售数量降序排列 返回错误码为0");

		RetailTradeReportByCommodity r = DataInput.getReport();
		r.setIsASC(0);
		r.setiOrderBy(1);
		List<BaseModel> list = retrieveNRetailTradeReportByCommodity(r);
		//
		if (list.size() > 1) {
			int i1 = ((RetailTradeReportByCommodity) list.get(0)).getNO();
			int i2 = ((RetailTradeReportByCommodity) list.get(1)).getNO();
			Assert.assertTrue(i1 >= i2, "查询的数据的销售数量不是降序排列");
		}
	}

	@Test
	public void retrieveNTest_CASE4() throws ParseException, CloneNotSupportedException {
		Shared.printTestMethodStartInfo();

		Shared.caseLog("CASE4: 按销售数量升序排列 返回错误码为0");

		RetailTradeReportByCommodity r = DataInput.getReport();
		r.setIsASC(1);
		r.setiOrderBy(1);
		List<BaseModel> list = retrieveNRetailTradeReportByCommodity(r);
		//
		if (list.size() > 1) {
			int i1 = ((RetailTradeReportByCommodity) list.get(0)).getNO();
			int i2 = ((RetailTradeReportByCommodity) list.get(1)).getNO();
			Assert.assertTrue(i1 <= i2, "查询的数据的销售数量不是升序排列");
		}
	}

	@Test
	public void retrieveNTest_CASE5() throws ParseException, CloneNotSupportedException {
		Shared.printTestMethodStartInfo();

		Shared.caseLog("CASE5: 按毛利率降序排列 返回错误码为0");
		//
		RetailTradeReportByCommodity r = DataInput.getReport();
		r.setIsASC(0);
		r.setiOrderBy(2);
		List<BaseModel> list = retrieveNRetailTradeReportByCommodity(r);
		//
		if (list.size() > 1) {
			double d1 = ((RetailTradeReportByCommodity) list.get(0)).getGrossMargin();
			double d2 = ((RetailTradeReportByCommodity) list.get(1)).getGrossMargin();
			Assert.assertTrue(d1 >= d2, "查询的数据的毛利率不是降序排列");
		}
	}

	@Test
	public void retrieveNTest_CASE6() throws ParseException, CloneNotSupportedException {
		Shared.printTestMethodStartInfo();

		Shared.caseLog("CASE6: 按毛利率升序排列 返回错误码为0");

		RetailTradeReportByCommodity r = DataInput.getReport();
		r.setIsASC(1);
		r.setiOrderBy(2);
		List<BaseModel> list = retrieveNRetailTradeReportByCommodity(r);
		//
		if (list.size() > 1) {
			double d1 = ((RetailTradeReportByCommodity) list.get(0)).getGrossMargin();
			double d2 = ((RetailTradeReportByCommodity) list.get(1)).getGrossMargin();
			Assert.assertTrue(d1 <= d2, "查询的数据的毛利率不是升序排列");
		}
	}

	@Test
	public void retrieveNTest_CASE7() throws ParseException, CloneNotSupportedException {
		Shared.printTestMethodStartInfo();

		Shared.caseLog("CASE7: 按名称进行模糊查询并按金额降序排列 返回错误码为0");
		//
		RetailTradeReportByCommodity r = DataInput.getReport();
		r.setQueryKeyword("可");
		r.setIsASC(0);
		r.setiOrderBy(0);
		List<BaseModel> list = retrieveNRetailTradeReportByCommodity(r);
		//
		if (list.size() > 1) {
			double d1 = ((RetailTradeReportByCommodity) list.get(0)).getAmount();
			double d2 = ((RetailTradeReportByCommodity) list.get(1)).getAmount();
			Assert.assertTrue(d1 >= d2, "查询的数据的金额不是降序排列");
		}
	}

	@Test
	public void retrieveNTest_CASE8() throws ParseException, CloneNotSupportedException {
		Shared.printTestMethodStartInfo();

		Shared.caseLog("CASE8: 按名称进行模糊查询并按金额升序排列 返回错误码为0");
		//
		RetailTradeReportByCommodity r = DataInput.getReport();
		r.setQueryKeyword("可");
		r.setIsASC(1);
		r.setiOrderBy(0);
		List<BaseModel> list = retrieveNRetailTradeReportByCommodity(r);
		//
		if (list.size() > 1) {
			double d1 = ((RetailTradeReportByCommodity) list.get(0)).getAmount();
			double d2 = ((RetailTradeReportByCommodity) list.get(1)).getAmount();
			Assert.assertTrue(d1 <= d2, "查询的数据的金额不是升序排列");
		}
	}

	@Test
	public void retrieveNTest_CASE9() throws ParseException, CloneNotSupportedException {
		Shared.printTestMethodStartInfo();

		System.out.println("CASE9: 按条形码进行精确查询并按数量降序排列 返回错误码为0");
		RetailTradeReportByCommodity r = DataInput.getReport();
		r.setQueryKeyword("3548293894545");
		r.setIsASC(0);
		r.setiOrderBy(1);
		retrieveNRetailTradeReportByCommodity(r);
	}

	@Test
	public void retrieveNTest_CASE10() throws ParseException, CloneNotSupportedException {
		Shared.printTestMethodStartInfo();

		System.out.println("CASE10: 按单号进行查询并按销售毛利降序排列 返回错误码为0");
		RetailTradeReportByCommodity r = DataInput.getReport();
		r.setQueryKeyword("7");
		r.setIsASC(0);
		r.setiOrderBy(2);
		List<BaseModel> list = retrieveNRetailTradeReportByCommodity(r);
		//
		if (list.size() > 1) {
			double d1 = ((RetailTradeReportByCommodity) list.get(0)).getGrossMargin();
			double d2 = ((RetailTradeReportByCommodity) list.get(1)).getGrossMargin();
			Assert.assertTrue(d1 >= d2, "查询的数据的毛利率不是降序排列");
		}
	}

	@Test
	public void retrieveNTest_CASE11() throws ParseException, CloneNotSupportedException {
		Shared.printTestMethodStartInfo();

		Shared.caseLog("CASE11: bIgnoreZeroNO=1忽略0进货量 返回错误码为0");

		RetailTradeReportByCommodity r = DataInput.getReport();
		r.setbIgnoreZeroNO(IGNORE_ZERO_NO);
		retrieveNRetailTradeReportByCommodity(r);
	}

	@Test
	public void retrieveNTest_CASE12() throws ParseException, CloneNotSupportedException {
		Shared.printTestMethodStartInfo();

		Shared.caseLog("Case12: 按照类别进行查询 返回错误码为0");
		//
		RetailTradeReportByCommodity r = DataInput.getReport();
		r.setiCategoryID(RETRIEVEN_BY_COMMODITY_CATEGORY_ID1);
		retrieveNRetailTradeReportByCommodity(r);
	}

	@Test
	public void retrieveNTest_CASE13() throws ParseException, CloneNotSupportedException, InterruptedException {
		Shared.printTestMethodStartInfo();

		Shared.caseLog("Case13:根据输入_特殊字符商品名称查询商品零售报表");
		//
		// 创建商品
		Commodity comm1 = BaseCommodityTest.DataInput.getCommodity(EnumCommodityType.ECT_Normal.getIndex());
		comm1.setName("dhdh营养快_kahd" + System.currentTimeMillis());
		List<List<BaseModel>> bmList = BaseCommodityTest.createCommodityViaMapper(BaseBO.CASE_Commodity_CreateSingle, comm1);
		Commodity commCreated = (Commodity) bmList.get(0).get(0);
		Barcodes barcodes = (Barcodes) bmList.get(1).get(0);
		// 创建零售单
		RetailTrade rt1 = BaseRetailTradeTest.DataInput.getRetailTrade();

		RetailTrade retailTrade = BaseRetailTradeTest.createRetailTrade(rt1);
		// 创建零售单商品
		RetailTradeCommodity rtc1 = BaseRetailTradeTest.DataInput.getRetailTradeCommodity();
		rtc1.setCommodityID(commCreated.getID());
		rtc1.setBarcodeID(barcodes.getID());
		rtc1.setTradeID(retailTrade.getID());
		rtc1.setOperatorStaffID(4);
		rtc1.setNO(1);

		Map<String, Object> params = rtc1.getCreateParam(BaseBO.INVALID_CASE_ID, rtc1);
		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		RetailTradeCommodity rtcCreate = (RetailTradeCommodity) retailTradeCommodityMapper.create(params);
		rtc1.setIgnoreIDInComparision(true);
		if (rtc1.compareTo(rtcCreate) != 0) {
			Assert.assertTrue(false, "创建的对象的字段与DB读出的不相等");
		}
		Assert.assertTrue(rtcCreate != null && EnumErrorCode.values()[Integer.parseInt(params.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())] == EnumErrorCode.EC_NoError, "创建对象失败");
		//
		String errorMsg = rtcCreate.checkCreate(BaseBO.INVALID_CASE_ID);
		assertEquals(errorMsg, "");

		// 模糊查询商品零售报表
		RetailTradeReportByCommodity r = DataInput.getReport();
		r.setQueryKeyword("_");
		// 查询结果验证
		List<BaseModel> list = retrieveNRetailTradeReportByCommodity(r);
		for (BaseModel result : list) {
			RetailTradeReportByCommodity rrByCommodity = (RetailTradeReportByCommodity) result;
			Assert.assertTrue(rrByCommodity.getName().contains("_"), "查询商品名称没有包含_特殊字符");
		}

	}

}
