package com.bx.erp.model.purchasing;

import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import com.bx.erp.action.bo.BaseBO;
import com.bx.erp.test.Shared;
import com.bx.erp.util.FieldFormat;

public class ProviderDistrictTest {
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
		ProviderDistrict p = new ProviderDistrict();
		String error = "";
		//
		p.setID(0);
		error = p.checkUpdate(BaseBO.INVALID_CASE_ID);
		Assert.assertEquals(error, FieldFormat.FIELD_ERROR_ID);
		p.setID(-99);
		error = p.checkUpdate(BaseBO.INVALID_CASE_ID);
		Assert.assertEquals(error, FieldFormat.FIELD_ERROR_ID);
		p.setID(1);
		//
		p.setName("");
		error = p.checkUpdate(BaseBO.INVALID_CASE_ID);
		Assert.assertEquals(error, ProviderDistrict.FIELD_ERROR_name);
		p.setName("1a2f231asdf153sdf132asd513f132fa135s");
		error = p.checkUpdate(BaseBO.INVALID_CASE_ID);
		Assert.assertEquals(error, ProviderDistrict.FIELD_ERROR_name);
		p.setName("哈哈哈");
		//
		error = p.checkUpdate(BaseBO.INVALID_CASE_ID);
		Assert.assertEquals(error, "");
	}

	@Test
	public void checkRetrieveN() {
		Shared.printTestMethodStartInfo();
		
		ProviderDistrict p = new ProviderDistrict();
		String error = "";
		//
		p.setPageIndex(-88);
		error = p.checkRetrieveN(BaseBO.INVALID_CASE_ID);
		Assert.assertEquals(error, FieldFormat.FIELD_ERROR_Paging);
		p.setPageIndex(0);
		error = p.checkRetrieveN(BaseBO.INVALID_CASE_ID);
		Assert.assertEquals(error, FieldFormat.FIELD_ERROR_Paging);
		p.setPageIndex(1);
		//
		p.setPageSize(-88);
		error = p.checkRetrieveN(BaseBO.INVALID_CASE_ID);
		Assert.assertEquals(error, FieldFormat.FIELD_ERROR_Paging);
		p.setPageSize(0);
		error = p.checkRetrieveN(BaseBO.INVALID_CASE_ID);
		Assert.assertEquals(error, FieldFormat.FIELD_ERROR_Paging);
		p.setPageSize(1);
		//
		error = p.checkRetrieveN(BaseBO.INVALID_CASE_ID);
		Assert.assertEquals(error, "");
	}

	@Test
	public void checkCreate() {
		Shared.printTestMethodStartInfo();
		ProviderDistrict p = new ProviderDistrict();
		String error = "";
		
		p.setName("");
		error = p.checkCreate(BaseBO.INVALID_CASE_ID);
		Assert.assertEquals(error, ProviderDistrict.FIELD_ERROR_name);
		p.setName("1a2f231asdf153sdf132asd513f132fa135s");
		error = p.checkCreate(BaseBO.INVALID_CASE_ID);
		Assert.assertEquals(error, ProviderDistrict.FIELD_ERROR_name);
		p.setName("哈哈哈");
		//
		error = p.checkCreate(BaseBO.INVALID_CASE_ID);
		Assert.assertEquals(error, "");
	}

	@Test
	public void checkRetrieve1() {
		Shared.printTestMethodStartInfo();

		ProviderDistrict p = new ProviderDistrict();
		String error = "";
		//
		p.setID(0);
		error = p.checkRetrieve1(BaseBO.INVALID_CASE_ID);
		Assert.assertEquals(error, FieldFormat.FIELD_ERROR_ID);
		p.setID(-99);
		error = p.checkRetrieve1(BaseBO.INVALID_CASE_ID);
		Assert.assertEquals(error, FieldFormat.FIELD_ERROR_ID);
		//
		p.setID(1);
		error = p.checkRetrieve1(BaseBO.INVALID_CASE_ID);
		Assert.assertEquals(error, "");
	}

	@Test
	public void checkDelete() {
		Shared.printTestMethodStartInfo();

		ProviderDistrict p = new ProviderDistrict();
		String error = "";
		//
		p.setID(0);
		error = p.checkDelete(BaseBO.INVALID_CASE_ID);
		Assert.assertEquals(error, FieldFormat.FIELD_ERROR_ID);
		p.setID(-99);
		error = p.checkDelete(BaseBO.INVALID_CASE_ID);
		Assert.assertEquals(error, FieldFormat.FIELD_ERROR_ID);
		//
		p.setID(1);
		error = p.checkDelete(BaseBO.INVALID_CASE_ID);
		Assert.assertEquals(error, "");
	}

}
