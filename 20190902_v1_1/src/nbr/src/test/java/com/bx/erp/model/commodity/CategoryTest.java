package com.bx.erp.model.commodity;

import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import com.bx.erp.action.bo.BaseBO;
import com.bx.erp.test.Shared;
import com.bx.erp.util.FieldFormat;


public class CategoryTest {
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

		// 检查小类名称,其余已在FieldFormatTest.checkCategoryName()已经作了充分测试
		Category c=new Category();
		c.setName("饮料");
		c.setParentID(1);
		String err = "";
		err = c.checkCreate(BaseBO.INVALID_CASE_ID);
		Assert.assertEquals(err, "");
		
		c.setName("abcde123456饮料");
		err = c.checkCreate(BaseBO.INVALID_CASE_ID);
		Assert.assertEquals(err, Category.FIELD_ERROR_name);
		
		c.setName("饮料..@#");
		err = c.checkCreate(BaseBO.INVALID_CASE_ID);
		Assert.assertEquals(err, Category.FIELD_ERROR_name);
		
		c.setName("饮料");
		c.setParentID(-1);
		err = c.checkCreate(BaseBO.INVALID_CASE_ID);
		Assert.assertEquals(err, Category.FIELD_ERROR_parentID);
	}
	
	@Test
	public void checkUpdate() {
		Shared.printTestMethodStartInfo();

		// 检查小类名称,其余已在FieldFormatTest.checkCategoryName()已经作了充分测试
		Category c= new Category();
		c.setID(1);
		c.setParentID(1);
		c.setName("饮料");
		String err = c.checkUpdate(BaseBO.INVALID_CASE_ID);
		Assert.assertEquals(err, "");
		
		c.setID(-1);
		err = c.checkUpdate(BaseBO.INVALID_CASE_ID);
		Assert.assertEquals(err, FieldFormat.FIELD_ERROR_ID);
		
		c.setID(1);
		c.setParentID(-1);
		err = c.checkUpdate(BaseBO.INVALID_CASE_ID);
		Assert.assertEquals(err, Category.FIELD_ERROR_parentID);
		
		c.setParentID(1);
		c.setName("12345abcde饮料");
		err = c.checkUpdate(BaseBO.INVALID_CASE_ID);
		Assert.assertEquals(err, Category.FIELD_ERROR_name);
		
		c.setParentID(1);
		c.setName("饮料..%@");
		err = c.checkUpdate(BaseBO.INVALID_CASE_ID);
		Assert.assertEquals(err, Category.FIELD_ERROR_name);
	}
	
	@Test
	public void checkRetrieve1() {
		Shared.printTestMethodStartInfo();
		
		Category c = new Category();
		c.setID(1);
		String err = c.checkRetrieve1(BaseBO.INVALID_CASE_ID);
		Assert.assertEquals(err, "");
		
		c.setID(-1);
		err = c.checkRetrieve1(BaseBO.INVALID_CASE_ID);
		Assert.assertEquals(err, FieldFormat.FIELD_ERROR_ID);
		
	}
	
	@Test
	public void checkRetrieveN_CASE1() {
		Shared.printTestMethodStartInfo();
		
		Category c = new Category();
		c.setFieldToCheckUnique(1);
		c.setUniqueField("饮料");
		String err = c.checkRetrieveN(BaseBO.CASE_CheckUniqueField);
		Assert.assertEquals(err, "");
		//测试name
		c.setUniqueField("12345678123456781234567812345678饮料");
		err = c.checkRetrieveN(BaseBO.CASE_CheckUniqueField);
		Assert.assertEquals(err, Category.FIELD_ERROR_checkUniqueField);
		
		c.setUniqueField("!@#&$^...");
		err = c.checkRetrieveN(BaseBO.CASE_CheckUniqueField);
		Assert.assertEquals(err, Category.FIELD_ERROR_checkUniqueField);
		c.setUniqueField("饮料");
		//测试int1
		c.setFieldToCheckUnique(0);
		err = c.checkRetrieveN(BaseBO.CASE_CheckUniqueField);
		Assert.assertEquals(err, Category.FIELD_ERROR_checkUniqueField);
	}
	
	@Test
	public void checkRetrieveN_CASE2() {
		Shared.printTestMethodStartInfo();
		
		Category c = new Category();
		c.setName("饮料");
		String err = c.checkRetrieveN(BaseBO.INVALID_CASE_ID);
		Assert.assertEquals(err, "");
		
		c.setName("12345678123456781234567812345678饮料");
		err = c.checkRetrieveN(BaseBO.INVALID_CASE_ID);
		Assert.assertEquals(err, Category.FIELD_ERROR_name);
		
		c.setName("!@#&$^...");
		err = c.checkRetrieveN(BaseBO.INVALID_CASE_ID);
		Assert.assertEquals(err, "");
	}
	
	@Test
	public void checkDelete() {
		Shared.printTestMethodStartInfo();
		
		Category c = new Category();
		c.setID(1);
		String err = c.checkDelete(BaseBO.INVALID_CASE_ID);
		Assert.assertEquals(err, "");
		
		c.setID(-1);
		err = c.checkDelete(BaseBO.INVALID_CASE_ID);
		Assert.assertEquals(err, FieldFormat.FIELD_ERROR_ID);
	}
}
