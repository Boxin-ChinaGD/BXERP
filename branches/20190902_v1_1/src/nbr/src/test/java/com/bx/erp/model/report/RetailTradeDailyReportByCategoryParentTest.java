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

public class RetailTradeDailyReportByCategoryParentTest {
	@BeforeClass
	public void setup() {
		Shared.printTestClassStartInfo();
	}

	@AfterClass
	public void tearDown() {
		Shared.printTestClassEndInfo();
	}

	@Test
	public void checkCreate() throws Exception {
		Shared.printTestMethodStartInfo();
		RetailTradeDailyReportByCategoryParent r = new RetailTradeDailyReportByCategoryParent();
		r.setSaleDatetime(new Date());
		String error = r.checkCreate(BaseBO.INVALID_CASE_ID);
		Assert.assertEquals(error, "");
		//测试saleDatetime...
		SimpleDateFormat sdf = new SimpleDateFormat(BaseAction.DATETIME_FORMAT_Default_Chinese);
		String date = "1970年00月00日  00:00:00";
		r.setSaleDatetime(sdf.parse(date));
		error = r.checkCreate(BaseBO.INVALID_CASE_ID);
		Assert.assertEquals(error, "");
	}

	@Test
	public void checkDelete() {
		Shared.printTestMethodStartInfo();
		RetailTradeDailyReportByCategoryParent r = new RetailTradeDailyReportByCategoryParent();
		String error = "";
		error = r.checkDelete(BaseBO.INVALID_CASE_ID);
		Assert.assertEquals(error, "");
	}

	@Test
	public void checkRetrieveN() throws Exception {
		Shared.printTestMethodStartInfo();

		RetailTradeDailyReportByCategoryParent r = new RetailTradeDailyReportByCategoryParent();
		r.setDatetimeStart(new Date());
		r.setDatetimeEnd(new Date());
		r.setPageIndex(1);
		r.setPageSize(10);
		String error = r.checkRetrieveN(BaseBO.INVALID_CASE_ID);
		Assert.assertEquals(error, "");
		//测试datetimeStart
		SimpleDateFormat sdf = new SimpleDateFormat(BaseAction.DATETIME_FORMAT_Default_Chinese);
		String datetimeStart = "1970年00月00日  00:00:00";
		r.setDatetimeStart(sdf.parse(datetimeStart));
		error = r.checkRetrieveN(BaseBO.INVALID_CASE_ID);
		Assert.assertEquals(error, "");
		//测试datetimeEnd
		String datetimeEnd = "1970年00月00日  00:00:00";
		r.setDatetimeEnd(sdf.parse(datetimeEnd));
		error = r.checkRetrieveN(BaseBO.INVALID_CASE_ID);
		Assert.assertEquals(error, "");
		//测试PageIndex
		r.setPageIndex(0);
		error = r.checkRetrieveN(BaseBO.INVALID_CASE_ID);
		Assert.assertEquals(error, FieldFormat.FIELD_ERROR_Paging);
		r.setPageIndex(2);
		//测试PageSize
		r.setPageSize(0);
		error = r.checkRetrieveN(BaseBO.INVALID_CASE_ID);
		Assert.assertEquals(error, FieldFormat.FIELD_ERROR_Paging);
		r.setPageSize(5);
	}

	@Test
	public void checkUpdate() {
		Shared.printTestMethodStartInfo();
		RetailTradeDailyReportByCategoryParent r = new RetailTradeDailyReportByCategoryParent();
		String error = "";
		error = r.checkUpdate(BaseBO.INVALID_CASE_ID);
		Assert.assertEquals(error, "");
	}

	@Test
	public void checkRetrieve1() {
		Shared.printTestMethodStartInfo();
		RetailTradeDailyReportByCategoryParent r = new RetailTradeDailyReportByCategoryParent();
		String error = "";
		error = r.checkRetrieve1(BaseBO.INVALID_CASE_ID);
		Assert.assertEquals(error, "");
	}
}
