package com.bx.erp.model.report;

import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import com.bx.erp.action.bo.BaseBO;
import com.bx.erp.test.Shared;
import com.bx.erp.util.FieldFormat;

public class RetailTradeDailyReportTest {
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
		RetailTradeDailyReport r = new RetailTradeDailyReport();
		String error = "";
		error = r.checkCreate(BaseBO.INVALID_CASE_ID);
		Assert.assertEquals(error, "");
	}
	
	@Test
	public void checkDelete() {
		Shared.printTestMethodStartInfo();
		RetailTradeDailyReport r = new RetailTradeDailyReport();
		String error = "";
		error = r.checkDelete(BaseBO.INVALID_CASE_ID);
		Assert.assertEquals(error, "");
	}
	
	@Test
	public void checkRetrieveN() {
		Shared.printTestMethodStartInfo();
		RetailTradeDailyReport r = new RetailTradeDailyReport();
		r.setPageIndex(1);
		r.setPageSize(10);
		String error = r.checkRetrieveN(BaseBO.INVALID_CASE_ID);
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
		RetailTradeDailyReport r = new RetailTradeDailyReport();
		String error = "";
		error = r.checkUpdate(BaseBO.INVALID_CASE_ID);
		Assert.assertEquals(error, "");
	}
	
	@Test
	public void checkRetrieve1() {
		Shared.printTestMethodStartInfo();
		RetailTradeDailyReport r = new RetailTradeDailyReport();
		String error = "";
		error = r.checkRetrieve1(BaseBO.INVALID_CASE_ID);
		Assert.assertEquals(error, "");
	}
}
