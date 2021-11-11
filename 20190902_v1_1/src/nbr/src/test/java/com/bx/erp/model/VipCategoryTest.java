package com.bx.erp.model;

import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import com.bx.erp.action.BaseAction;
import com.bx.erp.action.bo.BaseBO;
import com.bx.erp.test.Shared;
import com.bx.erp.util.FieldFormat;

public class VipCategoryTest {
	
	@BeforeClass
	public void setUp() {
		Shared.printTestClassStartInfo();
	}

	@AfterClass
	public void tearDown() {
		Shared.printTestClassEndInfo();
	}

	@Test
	public void testCheckCreate() {
		Shared.printTestMethodStartInfo();
		
		VipCategory vipCategory = new VipCategory();
		vipCategory.setName("超级会员");
		String error = vipCategory.checkCreate(BaseBO.INVALID_CASE_ID);
		Assert.assertEquals(error, "");
		Shared.caseLog("测试名称");
		vipCategory.setName("^&*@&%^%");
		error = vipCategory.checkCreate(BaseBO.INVALID_CASE_ID);
		Assert.assertEquals(error, VipCategory.FIELD_ERROR_name);
		vipCategory.setName("1234567890123456789012345678901234567890");
		error = vipCategory.checkCreate(BaseBO.INVALID_CASE_ID);
		Assert.assertEquals(error, VipCategory.FIELD_ERROR_name);
		vipCategory.setName("超级会员");
	}

	@Test
	public void testCheckRetrieve1() {
		Shared.printTestMethodStartInfo();
		
		VipCategory vipCategory = new VipCategory();
		vipCategory.setID(1);
		String error = vipCategory.checkRetrieve1(BaseBO.INVALID_CASE_ID);
		Assert.assertEquals(error, "");
		Shared.caseLog("测试ID");
		vipCategory.setID(BaseAction.INVALID_ID);
		error = vipCategory.checkRetrieve1(BaseBO.INVALID_CASE_ID);
		Assert.assertEquals(error, FieldFormat.FIELD_ERROR_ID);
		vipCategory.setID(1);
	}

	@Test
	public void testCheckRetrieveN() {
		Shared.printTestMethodStartInfo();
		
		VipCategory vipCategory = new VipCategory();
		vipCategory.setPageIndex(1);
		vipCategory.setPageSize(10);
		String error = vipCategory.checkRetrieveN(BaseBO.INVALID_CASE_ID);
		Assert.assertEquals(error, "");
		Shared.caseLog("测试PageIndex");
		vipCategory.setPageIndex(0);
		error = vipCategory.checkRetrieveN(BaseBO.INVALID_CASE_ID);
		Assert.assertEquals(error, FieldFormat.FIELD_ERROR_Paging);
		vipCategory.setPageIndex(1);
		Shared.caseLog("测试PageSize");
		vipCategory.setPageSize(0);
		error = vipCategory.checkRetrieveN(BaseBO.INVALID_CASE_ID);
		Assert.assertEquals(error, FieldFormat.FIELD_ERROR_Paging);
		vipCategory.setPageSize(10);
	}

	@Test
	public void testCheckUpdate() {
		Shared.printTestMethodStartInfo();
		
		VipCategory vipCategory = new VipCategory();
		vipCategory.setID(1);
		vipCategory.setName("超级会员");
		String error = vipCategory.checkUpdate(BaseBO.INVALID_CASE_ID);
		Assert.assertEquals(error, "");
		Shared.caseLog("测试名称");
		vipCategory.setName("^&*@&%^%");
		error = vipCategory.checkUpdate(BaseBO.INVALID_CASE_ID);
		Assert.assertEquals(error, VipCategory.FIELD_ERROR_name);
		vipCategory.setName("1234567890123456789012345678901234567890");
		error = vipCategory.checkUpdate(BaseBO.INVALID_CASE_ID);
		Assert.assertEquals(error, VipCategory.FIELD_ERROR_name);
		vipCategory.setName("超级会员");
		Shared.caseLog("测试ID");
		vipCategory.setID(BaseAction.INVALID_ID);
		error = vipCategory.checkUpdate(BaseBO.INVALID_CASE_ID);
		Assert.assertEquals(error, FieldFormat.FIELD_ERROR_ID);
		vipCategory.setID(1);
	}

	@Test
	public void testCheckDelete() {
		Shared.printTestMethodStartInfo();
		
		VipCategory vipCategory = new VipCategory();
		vipCategory.setID(1);
		String error = vipCategory.checkDelete(BaseBO.INVALID_CASE_ID);
		Assert.assertEquals(error, "");
		Shared.caseLog("测试ID");
		vipCategory.setID(BaseAction.INVALID_ID);
		error = vipCategory.checkDelete(BaseBO.INVALID_CASE_ID);
		Assert.assertEquals(error, FieldFormat.FIELD_ERROR_ID);
		vipCategory.setID(1);
	}
}
