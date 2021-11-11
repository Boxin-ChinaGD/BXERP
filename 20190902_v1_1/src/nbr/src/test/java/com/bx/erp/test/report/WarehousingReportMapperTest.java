package com.bx.erp.test.report;

import java.text.DateFormat;
import java.text.ParseException;
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
import com.bx.erp.model.report.ReportSummaryWarehousing;
import com.bx.erp.model.report.WarehousingReport;
import com.bx.erp.test.BaseMapperTest;
import com.bx.erp.test.Shared;
import com.bx.erp.util.DataSourceContextHolder;

public class WarehousingReportMapperTest extends BaseMapperTest {

	/** -1代表查询所有分类 */
	public static final int RETRIEVEN_ALL_CATEGORY = -1;

	/** 按照类别ID为1查询报表 */
	public static final int RETRIEVEN_BY_CATEGORY_ID1 = 1;

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
		private static WarehousingReport ReportInput = null;

		protected static final WarehousingReport getReport() throws CloneNotSupportedException, InterruptedException, ParseException {
			DateFormat df = new SimpleDateFormat(BaseAction.DATETIME_FORMAT_Default2);
			String dt1 = "2018-01-01 00:00:00";
			String dt2 = "2020-11-17 00:00:00";
			ReportInput = new WarehousingReport();
			ReportInput.setDate1(df.parse(dt1));
			ReportInput.setDate2(df.parse(dt2));
			ReportInput.setiOrderBy(0);
			ReportInput.setIsASC(0);
			ReportInput.setQueryKeyword("");

			return (WarehousingReport) ReportInput.clone();
		}
	}

	protected List<List<BaseModel>> retrieveNWarehousingReport(WarehousingReport r) {

		Map<String, Object> params = r.getRetrieveNParamEx(BaseBO.INVALID_CASE_ID, r);
		//
		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		List<List<BaseModel>> list = warehousingReportMapper.retrieveNEx(params);
		//
		Assert.assertTrue(list.size() > 0 && EnumErrorCode.values()[Integer.parseInt(params.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())] == EnumErrorCode.EC_NoError, //
				params.get(BaseAction.SP_OUT_PARAM_sErrorMsg).toString());
		String err;
		for (List<BaseModel> bmList : list) { //
			for (BaseModel bm : bmList) {
				System.out.println("查询的数据: " + bm);
				if (bm instanceof WarehousingReport) {
					err = ((WarehousingReport) bm).checkCreate(BaseBO.INVALID_CASE_ID);
					Assert.assertEquals(err, "");
				} else {
					err = ((ReportSummaryWarehousing) bm).checkCreate(BaseBO.INVALID_CASE_ID);
					Assert.assertEquals(err, "");
				}
			}
		}
		//
		System.out.println("查询数据成功: " + list);
		//
		return list;
	}

	@Test
	public void retrieveNTest_CASE1() throws CloneNotSupportedException, InterruptedException, ParseException {
		Shared.printTestMethodStartInfo();

		Shared.caseLog("CASE1: iOrderBy和IsASC同时为0时按ID降序排序");

		WarehousingReport r = DataInput.getReport();
		r.setiCategoryID(RETRIEVEN_ALL_CATEGORY);
		r.setPageSize(100);
		r.setPageIndex(1);
		List<List<BaseModel>> list = retrieveNWarehousingReport(r);
		//
		if (list.get(0).size() > 1) {
			int i1 = list.get(0).get(0).getID();
			int i2 = list.get(0).get(1).getID();
			Assert.assertTrue(i1 > i2, "查询的数据不是按ID降序排序");
		}
	}

	@Test
	public void retrieveNTest_CASE2() throws CloneNotSupportedException, InterruptedException, ParseException {
		Shared.printTestMethodStartInfo();

		Shared.caseLog("CASE2:iOrderBy为0，IsASC为1时按ID升序排序");
		WarehousingReport r = DataInput.getReport();
		r.setiOrderBy(0);
		r.setIsASC(1);
		r.setiCategoryID(RETRIEVEN_ALL_CATEGORY);
		List<List<BaseModel>> list = retrieveNWarehousingReport(r);
		//
		if (list.get(0).size() > 1) {
			int i1 = list.get(0).get(0).getID();
			int i2 = list.get(0).get(1).getID();
			Assert.assertTrue(i1 < i2, "查询的数据不是按ID升序排序");
		}
	}

	@Test
	public void retrieveNTest_CASE3() throws CloneNotSupportedException, InterruptedException, ParseException {
		Shared.printTestMethodStartInfo();

		Shared.caseLog("CASE3: iOrderBy为1和IsASC为1时按数量升序排序");

		WarehousingReport r = DataInput.getReport();
		r.setiOrderBy(1);
		r.setIsASC(1);
		r.setiCategoryID(RETRIEVEN_ALL_CATEGORY);
		r.setPageSize(BaseAction.PAGE_SIZE_Infinite);
		List<List<BaseModel>> list = retrieveNWarehousingReport(r);
		//
		if (list.get(0).size() > 1) {
			int i1 = ((WarehousingReport) list.get(0).get(0)).getNO();
			int i2 = ((WarehousingReport) list.get(0).get(1)).getNO();
			Assert.assertTrue(i1 <= i2, "查询的数据不是按数量升序排序");
		}
	}

	@Test
	public void retrieveNTest_CASE4() throws CloneNotSupportedException, InterruptedException, ParseException {
		Shared.printTestMethodStartInfo();

		Shared.caseLog("CASE4: iOrderBy为1和IsASC为0时按数量降序排序");

		WarehousingReport r = DataInput.getReport();
		r.setiOrderBy(1);
		r.setIsASC(0);
		r.setiCategoryID(RETRIEVEN_ALL_CATEGORY);
		List<List<BaseModel>> list = retrieveNWarehousingReport(r);
		//
		if (list.get(0).size() > 1) {
			int i1 = ((WarehousingReport) list.get(0).get(0)).getNO();
			int i2 = ((WarehousingReport) list.get(0).get(1)).getNO();
			Assert.assertTrue(i1 >= i2, "查询的数据不是按数量降序排序");
		}
	}

	@Test
	public void retrieveNTest_CASE5() throws CloneNotSupportedException, InterruptedException, ParseException {
		Shared.printTestMethodStartInfo();

		Shared.caseLog("CASE5: iOrderBy为2和IsASC为1时按商品总额升序排序");

		WarehousingReport r = DataInput.getReport();
		r.setiOrderBy(2);
		r.setIsASC(1);
		r.setiCategoryID(RETRIEVEN_ALL_CATEGORY);
		r.setPageSize(BaseAction.PAGE_SIZE_Infinite);
		List<List<BaseModel>> list = retrieveNWarehousingReport(r);
		//
		if (list.get(0).size() > 1) {
			double d1 = ((WarehousingReport) list.get(0).get(0)).getAmount();
			double d2 = ((WarehousingReport) list.get(0).get(1)).getAmount();
			Assert.assertTrue(d1 <= d2, "查询的数据不是按商品总额升序排序");
		}
	}

	@Test
	public void retrieveNTest_CASE6() throws CloneNotSupportedException, InterruptedException, ParseException {
		Shared.printTestMethodStartInfo();

		Shared.caseLog("CASE6: iOrderBy为2和IsASC为0时按purchasingOrder降序排序");

		WarehousingReport r = DataInput.getReport();
		r.setiOrderBy(2);
		r.setIsASC(0);
		r.setiCategoryID(RETRIEVEN_ALL_CATEGORY);
		List<List<BaseModel>> list = retrieveNWarehousingReport(r);
		//
		if (list.get(0).size() > 1) {
			double d1 = ((WarehousingReport) list.get(0).get(2)).getAmount();
			double d2 = ((WarehousingReport) list.get(0).get(3)).getAmount();
			Assert.assertTrue(d1 >= d2, "查询的数据不是按商品总额降序排列");
		}
	}

	@Test
	public void retrieveNTest_CASE7() throws CloneNotSupportedException, InterruptedException, ParseException {
		Shared.printTestMethodStartInfo();

		Shared.caseLog("CASE7:QueryKeyword输入商品名称，按照商品名称模糊查询报表");

		WarehousingReport r = DataInput.getReport();
		r.setQueryKeyword("百事可乐");
		r.setiCategoryID(RETRIEVEN_ALL_CATEGORY);
		retrieveNWarehousingReport(r);
	}

	@Test
	public void retrieveNTest_CASE8() throws CloneNotSupportedException, InterruptedException, ParseException {
		Shared.printTestMethodStartInfo();

		Shared.caseLog("CASE8:QueryKeyword输入完整条形码，按照商品条形码精确查询报表");

		WarehousingReport r = DataInput.getReport();
		r.setQueryKeyword("123456789");
		r.setiCategoryID(RETRIEVEN_ALL_CATEGORY);
		retrieveNWarehousingReport(r);
	}

	@Test
	public void retrieveNTest_CASE9() throws CloneNotSupportedException, InterruptedException, ParseException {
		Shared.printTestMethodStartInfo();

		Shared.caseLog("CASE9:QueryKeyword输入商品名称，按照商品名称模糊查询报表");

		WarehousingReport r = DataInput.getReport();
		r.setQueryKeyword("雪晶灵肌密水");
		r.setbIgnoreZeroNO(IGNORE_ZERO_NO);
		r.setiCategoryID(RETRIEVEN_ALL_CATEGORY);
		retrieveNWarehousingReport(r);
	}

	@Test
	public void retrieveNTest_CASE10() throws CloneNotSupportedException, InterruptedException, ParseException {
		Shared.printTestMethodStartInfo();

		Shared.caseLog("CASE10:bIgnoreZeroNO=1忽略0进货量，目前11为0进货量");

		WarehousingReport r = DataInput.getReport();
		r.setbIgnoreZeroNO(IGNORE_ZERO_NO);
		r.setiCategoryID(RETRIEVEN_ALL_CATEGORY);
		retrieveNWarehousingReport(r);
	}

	@Test
	public void retrieveNTest_CASE11() throws CloneNotSupportedException, InterruptedException, ParseException {
		Shared.printTestMethodStartInfo();

		Shared.caseLog("CASE11:输入类别ID，按照类别查询报表");

		WarehousingReport r = DataInput.getReport();
		r.setiCategoryID(RETRIEVEN_BY_CATEGORY_ID1);
		retrieveNWarehousingReport(r);
	}
}
