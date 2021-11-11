package com.bx.erp.model.report;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import com.bx.erp.action.BaseAction;
import com.bx.erp.action.bo.BaseBO;
import com.bx.erp.test.Shared;
import com.bx.erp.util.FieldFormat;


public class RetailTradeReportByCommodityTest {
	@BeforeClass
	public void setup() {
		Shared.printTestClassStartInfo();
	}

	@AfterClass
	public void tearDown() {
		Shared.printTestClassEndInfo();
	}

	@Test
	public void checkCreate() {
		Shared.printTestMethodStartInfo();
		RetailTradeReportByCommodity r = new RetailTradeReportByCommodity();
		String error = "";
		error = r.checkCreate(BaseBO.INVALID_CASE_ID);
		Assert.assertEquals(error, "");
	}

	@Test
	public void checkDelete() {
		Shared.printTestMethodStartInfo();
		RetailTradeReportByCommodity r = new RetailTradeReportByCommodity();
		String error = "";
		error = r.checkDelete(BaseBO.INVALID_CASE_ID);
		Assert.assertEquals(error, "");
	}

	@Test
	public void checkRetrieveN() throws Exception {
		Shared.printTestMethodStartInfo();

		RetailTradeReportByCommodity r = new RetailTradeReportByCommodity();
		r.setQueryKeyword("这是商品名称");
		r.setIsASC(0);
		r.setiOrderBy(0);
		r.setbIgnoreZeroNO(0);
		r.setiCategoryID(1);
		r.setPageIndex(1);
		r.setPageSize(1);
		r.setDatetimeStart(new Date());
		r.setDatetimeEnd(new Date());
		String error = r.checkRetrieveN(BaseBO.INVALID_CASE_ID);
		Assert.assertEquals(error, "");
		// 测试QueryKeyword
		r.setQueryKeyword("1234567812345678123456781234567812345678");
		error = r.checkRetrieveN(BaseBO.INVALID_CASE_ID);
		Assert.assertEquals(error, RetailTradeReportByCommodity.FIELD_ERROR_queryKeyword);
		r.setQueryKeyword("这是商品名称");
		// 测试升序
		r.setIsASC(9);
		error = r.checkRetrieveN(BaseBO.INVALID_CASE_ID);
		Assert.assertEquals(error, RetailTradeReportByCommodity.FIELD_ERROR_isASC);
		r.setIsASC(0);
		// 测试排序
		r.setiOrderBy(9);
		error = r.checkRetrieveN(BaseBO.INVALID_CASE_ID);
		Assert.assertEquals(error, RetailTradeReportByCommodity.FIELD_ERROR_iOrderBy);
		r.setiOrderBy(0);
		// 测试int2
		r.setbIgnoreZeroNO(9);
		error = r.checkRetrieveN(BaseBO.INVALID_CASE_ID);
		Assert.assertEquals(error, RetailTradeReportByCommodity.FIELD_ERROR_bIgnoreZeroNO);
		r.setbIgnoreZeroNO(0);
		// 测试int3
		r.setiCategoryID(BaseAction.INVALID_ID);// 为-1时代表不根据该字段查询
		error = r.checkRetrieveN(BaseBO.INVALID_CASE_ID);
		Assert.assertEquals(error, "");
		r.setiCategoryID(-2);
		error = r.checkRetrieveN(BaseBO.INVALID_CASE_ID);
		Assert.assertEquals(error, RetailTradeReportByCommodity.FIELD_ERROR_iCategoryID);
		r.setiCategoryID(1);
		// 测试PageIndex
		r.setPageIndex(0);
		error = r.checkRetrieveN(BaseBO.INVALID_CASE_ID);
		Assert.assertEquals(error, FieldFormat.FIELD_ERROR_Paging);
		r.setPageIndex(1);
		// 测试PageSize
		r.setPageSize(0);
		error = r.checkRetrieveN(BaseBO.INVALID_CASE_ID);
		Assert.assertEquals(error, FieldFormat.FIELD_ERROR_Paging);
		r.setPageSize(1);
		// 测试datetimeStart
		SimpleDateFormat sdf = new SimpleDateFormat(BaseAction.DATETIME_FORMAT_Default_Chinese);
		String datetimeStart = "1970年00月00日  00:00:00";
		r.setDatetimeStart(sdf.parse(datetimeStart));
		error = r.checkRetrieveN(BaseBO.INVALID_CASE_ID);
		Assert.assertEquals(error, "");
		// 测试datetimeEnd
		String datetimeEnd = "1970年00月00日  00:00:00";
		r.setDatetimeEnd(sdf.parse(datetimeEnd));
		error = r.checkRetrieveN(BaseBO.INVALID_CASE_ID);
		Assert.assertEquals(error, "");
	}

	@Test
	public void checkUpdate() {
		Shared.printTestMethodStartInfo();
		RetailTradeReportByCommodity r = new RetailTradeReportByCommodity();
		String error = "";
		error = r.checkUpdate(BaseBO.INVALID_CASE_ID);
		Assert.assertEquals(error, "");
	}

	@Test
	public void checkRetrieve1() {
		Shared.printTestMethodStartInfo();
		RetailTradeReportByCommodity r = new RetailTradeReportByCommodity();
		String error = "";
		error = r.checkRetrieve1(BaseBO.INVALID_CASE_ID);
		Assert.assertEquals(error, "");
	}
}
