package com.bx.erp.model.commodity;

import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import com.bx.erp.action.bo.BaseBO;
import com.bx.erp.test.Shared;
import com.bx.erp.util.FieldFormat;


public class BrandTest {
	@BeforeClass
	public void setup() {
		Shared.printTestClassStartInfo();
	}

	@AfterClass
	public void tearDown() {
		Shared.printTestClassEndInfo();
	}
	
	@Test
	public void checkUpdate() {
		Shared.printTestMethodStartInfo();
		
		Brand b = new Brand();
		String err = "";
		// 检查品牌名称,其余已在FieldFormatTest.checkBrandName()已经作了充分测试
		b.setName("小米");
		b.setID(1);
		err = b.checkUpdate(BaseBO.INVALID_CASE_ID);
		Assert.assertEquals(err, "");
		
		b.setName("123zbc%^^");
		err = b.checkUpdate(BaseBO.INVALID_CASE_ID);
		Assert.assertEquals(err, Brand.FIELD_ERROR_name);
		
		b.setName("1234512345abcdeabcde小米");
		err = b.checkUpdate(BaseBO.INVALID_CASE_ID);
		Assert.assertEquals(err, Brand.FIELD_ERROR_name);
		
		b.setName(" 小米  ");
		err = b.checkUpdate(BaseBO.INVALID_CASE_ID);
		Assert.assertEquals(err, Brand.FIELD_ERROR_name);
		
		b.setName("小米a1");
		b.setID(0);
		err = b.checkUpdate(BaseBO.INVALID_CASE_ID);
		Assert.assertEquals(err, FieldFormat.FIELD_ERROR_ID);
	}

	@Test
	public void checkCreate() {
		Shared.printTestMethodStartInfo();

		Brand b = new Brand();
		String err = "";
		// 检查品牌名称,其余已在FieldFormatTest.checkBrandName()已经作了充分测试
		b.setName("小米");
		err = b.checkCreate(BaseBO.INVALID_CASE_ID);
		Assert.assertEquals(err, "");
		
		b.setName("  小米  ");
		err = b.checkCreate(BaseBO.INVALID_CASE_ID);
		Assert.assertEquals(err, Brand.FIELD_ERROR_name);
		
		b.setName("@$%");
		err = b.checkCreate(BaseBO.INVALID_CASE_ID);
		Assert.assertEquals(err, Brand.FIELD_ERROR_name);
		
		b.setName("1234512345abcdeabcde小米");
		err = b.checkCreate(BaseBO.INVALID_CASE_ID);
		Assert.assertEquals(err, Brand.FIELD_ERROR_name);
	}
	
	@Test
	public void checkRetrieve1() {
		Shared.printTestMethodStartInfo();
		
		Brand b = new Brand();
		b.setID(1);
		String err = b.checkRetrieve1(BaseBO.INVALID_CASE_ID);
		Assert.assertEquals(err, "");
		
		b.setID(0);
		err = b.checkRetrieve1(BaseBO.INVALID_CASE_ID);
		Assert.assertEquals(err, FieldFormat.FIELD_ERROR_ID);
	}
	
	@Test
	public void checkRetrieveN() {
		Shared.printTestMethodStartInfo();
		
		Brand b = new Brand();
		b.setName("小米");
		b.setPageIndex(1);
		b.setPageSize(10);
		String err = b.checkRetrieveN(BaseBO.INVALID_CASE_ID);
		Assert.assertEquals(err, "");
		//测试name
		b.setName("12345123451234512345");
		err = b.checkRetrieveN(BaseBO.INVALID_CASE_ID);
		Assert.assertEquals(err, "");
		
		b.setName("");
		err = b.checkRetrieveN(BaseBO.INVALID_CASE_ID);
		Assert.assertEquals(err, "");
		
		b.setName("123451234512345123451");
		err = b.checkRetrieveN(BaseBO.INVALID_CASE_ID);
		Assert.assertEquals(err, Brand.FIELD_ERROR_name);
		
		b.setName("12.345");
		err = b.checkRetrieveN(BaseBO.INVALID_CASE_ID);
		Assert.assertEquals(err, "");
		
		b.setName(" 小米  ");
		err = b.checkRetrieveN(BaseBO.INVALID_CASE_ID);
		Assert.assertEquals(err, "");
		
		b.setName("!@%%");
		err = b.checkRetrieveN(BaseBO.INVALID_CASE_ID);
		Assert.assertEquals(err, "");
		b.setName("小米");
		//测试PageIndex
		b.setPageIndex(0);
		err = b.checkRetrieveN(BaseBO.INVALID_CASE_ID);
		Assert.assertEquals(err, FieldFormat.FIELD_ERROR_Paging);
		b.setPageIndex(2);
		//测试PageSize
		b.setPageSize(0);
		err = b.checkRetrieveN(BaseBO.INVALID_CASE_ID);
		Assert.assertEquals(err, FieldFormat.FIELD_ERROR_Paging);
		b.setPageSize(5);
	}
	
	@Test
	public void checkDelete() {
		Shared.printTestMethodStartInfo();
		
		Brand b = new Brand();
		b.setID(1);
		String err =  b.checkDelete(BaseBO.INVALID_CASE_ID);
		Assert.assertEquals(err, "");
		
		b.setID(0);
		err =  b.checkDelete(BaseBO.INVALID_CASE_ID);
		Assert.assertEquals(err, FieldFormat.FIELD_ERROR_ID);
	}
	
}
