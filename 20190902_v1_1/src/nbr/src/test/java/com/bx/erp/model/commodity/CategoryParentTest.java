package com.bx.erp.model.commodity;

import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import com.bx.erp.action.bo.BaseBO;
import com.bx.erp.test.Shared;
import com.bx.erp.util.FieldFormat;


public class CategoryParentTest {
	
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

		// 检查大类名称,其余已在FieldFormatTest.checkCategoryName()已经作了充分测试
		CategoryParent c=new CategoryParent();
		c.setName("手机");
		String err = c.checkCreate(BaseBO.INVALID_CASE_ID);
		Assert.assertEquals(err, "");
		
		c.setName("");
		err = c.checkCreate(BaseBO.INVALID_CASE_ID);
		Assert.assertEquals(err, CategoryParent.FIELD_ERROR_name);
		
		c.setName("   ");
		err = c.checkCreate(BaseBO.INVALID_CASE_ID);
		Assert.assertEquals(err, CategoryParent.FIELD_ERROR_name);
		
		c.setName("#@%%");
		err = c.checkCreate(BaseBO.INVALID_CASE_ID);
		Assert.assertEquals(err, CategoryParent.FIELD_ERROR_name);
		
		c.setName("239fwho测试");
		err = c.checkCreate(BaseBO.INVALID_CASE_ID);
		Assert.assertEquals(err, "");
	}
	
	@Test
	public void checkUpdate() {
		Shared.printTestMethodStartInfo();

		// 检查大类名称,其余已在FieldFormatTest.checkCategoryName()已经作了充分测试
		CategoryParent c=new CategoryParent();
		c.setID(1);
		c.setName("手机");
		String err = c.checkUpdate(BaseBO.INVALID_CASE_ID);
		Assert.assertEquals(err, "");
		
		c.setName("小米手机MIX35G8G128G");
		err = c.checkUpdate(BaseBO.INVALID_CASE_ID);
		Assert.assertEquals(err, CategoryParent.FIELD_ERROR_name);
		
		c.setName("小米手机!%%");
		err = c.checkUpdate(BaseBO.INVALID_CASE_ID);
		Assert.assertEquals(err, CategoryParent.FIELD_ERROR_name);
		
		c.setName("小米手机");
		c.setID(-1);
		err = c.checkUpdate(BaseBO.INVALID_CASE_ID);
		Assert.assertEquals(err, FieldFormat.FIELD_ERROR_ID);
	}
	
	@Test
	public void checkRetrieve1() {
		Shared.printTestMethodStartInfo();
		
		CategoryParent cp = new CategoryParent();
		cp.setID(1);
		String err = cp.checkRetrieve1(BaseBO.INVALID_CASE_ID);
		Assert.assertEquals(err, "");
		
		cp.setID(-1);
		err = cp.checkRetrieve1(BaseBO.INVALID_CASE_ID);
		Assert.assertEquals(err, FieldFormat.FIELD_ERROR_ID);
	}
	
	@Test
	public void checkRetrieveN() {
		Shared.printTestMethodStartInfo();
		
		CategoryParent cp = new CategoryParent();
		cp.setName("手机");
		cp.setPageIndex(1);
		cp.setPageSize(10);
		String err = cp.checkRetrieveN(BaseBO.INVALID_CASE_ID);
		Assert.assertEquals(err, "");
		//测试name
		cp.setName("@#^^636");
		err = cp.checkRetrieveN(BaseBO.INVALID_CASE_ID);
		Assert.assertEquals(err, "");
		
		cp.setName("小米手机MIX35G8G128G");
		err = cp.checkRetrieveN(BaseBO.INVALID_CASE_ID);
		Assert.assertEquals(err, CategoryParent.FIELD_ERROR_name);
		cp.setName("手机");
		//测试PageIndex
		cp.setPageIndex(0);
		err = cp.checkRetrieveN(BaseBO.INVALID_CASE_ID);
		Assert.assertEquals(err, FieldFormat.FIELD_ERROR_Paging);
		cp.setPageIndex(1);
		//测试PageSize
		cp.setPageSize(0);
		err = cp.checkRetrieveN(BaseBO.INVALID_CASE_ID);
		Assert.assertEquals(err, FieldFormat.FIELD_ERROR_Paging);
		cp.setPageSize(5);
	}
	
	@Test
	public void checkDelete() {
		Shared.printTestMethodStartInfo();
		
		CategoryParent cp = new CategoryParent();
		cp.setID(1);
		String err = cp.checkDelete(BaseBO.INVALID_CASE_ID);
		Assert.assertEquals(err, "");
		
		cp.setID(-1);
		err = cp.checkDelete(BaseBO.INVALID_CASE_ID);
		Assert.assertEquals(err, FieldFormat.FIELD_ERROR_ID);
	}
	
}
