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

public class WarehousingReportTest {
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
		WarehousingReport w = new WarehousingReport();
		String error = "";
		error = w.checkCreate(BaseBO.INVALID_CASE_ID);
		Assert.assertEquals(error, "");
	}
	
	@Test
	public void checkDelete() {
		Shared.printTestMethodStartInfo();
		WarehousingReport w = new WarehousingReport();
		String error = "";
		error = w.checkDelete(BaseBO.INVALID_CASE_ID);
		Assert.assertEquals(error, "");
	}
	
	@Test
	public void checkRetrieveN() throws Exception {
		Shared.printTestMethodStartInfo();
		
		WarehousingReport w = new WarehousingReport();
		w.setQueryKeyword("这是商品名称");
		w.setIsASC(0);
		w.setiOrderBy(0);
		w.setbIgnoreZeroNO(0);
		w.setiCategoryID(1);
		w.setPageIndex(1);
		w.setPageSize(1);
		w.setDate1(new Date());
		w.setDate2(new Date());
		String error = w.checkRetrieveN(BaseBO.INVALID_CASE_ID);
		Assert.assertEquals(error, "");
		//测试QueryKeyword
		w.setQueryKeyword("1234567812345678123456781234567812345678");
		error = w.checkRetrieveN(BaseBO.INVALID_CASE_ID);
		Assert.assertEquals(error, WarehousingReport.FIELD_ERROR_queryKeyword);
		w.setQueryKeyword("这是商品名称");
		//测试升序
		w.setIsASC(9);
		error = w.checkRetrieveN(BaseBO.INVALID_CASE_ID);
		Assert.assertEquals(error, WarehousingReport.FIELD_ERROR_isASC);
		w.setIsASC(0);
		//测试排序
		w.setiOrderBy(9);
		error = w.checkRetrieveN(BaseBO.INVALID_CASE_ID);
		Assert.assertEquals(error, WarehousingReport.FIELD_ERROR_iOrderBy);
		w.setiOrderBy(0);
		//测试bIgnoreZeroNO
		w.setbIgnoreZeroNO(9);
		error = w.checkRetrieveN(BaseBO.INVALID_CASE_ID);
		Assert.assertEquals(error, WarehousingReport.FIELD_ERROR_bIgnoreZeroNO);
		w.setbIgnoreZeroNO(0);
		//测试iCategoryID
		w.setiCategoryID(BaseAction.INVALID_ID);//为-1时代表不根据该字段查询
		error = w.checkRetrieveN(BaseBO.INVALID_CASE_ID);
		Assert.assertEquals(error, "");
		w.setiCategoryID(-2);
		error = w.checkRetrieveN(BaseBO.INVALID_CASE_ID);
		Assert.assertEquals(error, WarehousingReport.FIELD_ERROR_iCategoryID);
		w.setiCategoryID(1);
		//测试PageIndex
		w.setPageIndex(0);
		error = w.checkRetrieveN(BaseBO.INVALID_CASE_ID);
		Assert.assertEquals(error, FieldFormat.FIELD_ERROR_Paging);
		w.setPageIndex(1);
		//测试PageSize
		w.setPageSize(0);
		error = w.checkRetrieveN(BaseBO.INVALID_CASE_ID);
		Assert.assertEquals(error, FieldFormat.FIELD_ERROR_Paging);
		w.setPageSize(1);
		//测试date1
		SimpleDateFormat sdf = new SimpleDateFormat(BaseAction.DATETIME_FORMAT_Default_Chinese);
		String date1 = "1970年00月00日  00:00:00";
		w.setDate1(sdf.parse(date1));
		error = w.checkRetrieveN(BaseBO.INVALID_CASE_ID);
		Assert.assertEquals(error, "");
		//测试date1
		String date2 = "1970年00月00日  00:00:00";
		w.setDate2(sdf.parse(date2));
		error = w.checkRetrieveN(BaseBO.INVALID_CASE_ID);
		Assert.assertEquals(error, "");
	}
	
	@Test
	public void checkUpdate() {
		Shared.printTestMethodStartInfo();
		WarehousingReport w = new WarehousingReport();
		String error = "";
		error = w.checkUpdate(BaseBO.INVALID_CASE_ID);
		Assert.assertEquals(error, "");
	}
	
	@Test
	public void checkRetrieve1() {
		Shared.printTestMethodStartInfo();
		WarehousingReport w = new WarehousingReport();
		String error = "";
		error = w.checkRetrieve1(BaseBO.INVALID_CASE_ID);
		Assert.assertEquals(error, "");
	}
	
	@Test
	public void checkRetrieveNEx() {
		Shared.printTestMethodStartInfo();
		WarehousingReport w = new WarehousingReport();
		String error = "";
		error = w.checkRetrieveNEx(BaseBO.INVALID_CASE_ID);
		Assert.assertEquals(error, "");
	}
}
