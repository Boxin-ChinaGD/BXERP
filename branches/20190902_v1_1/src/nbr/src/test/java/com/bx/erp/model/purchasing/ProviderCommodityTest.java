package com.bx.erp.model.purchasing;

import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import com.bx.erp.action.bo.BaseBO;
import com.bx.erp.test.Shared;
import com.bx.erp.util.FieldFormat;

public class ProviderCommodityTest {
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

		ProviderCommodity p = new ProviderCommodity();
		String error = "";
		//
		p.setCommodityID(-99);
		error = p.checkCreate(BaseBO.INVALID_CASE_ID);
		Assert.assertEquals(error, ProviderCommodity.FIELD_ERROR_CommodityID);
		p.setCommodityID(0);
		error = p.checkCreate(BaseBO.INVALID_CASE_ID);
		Assert.assertEquals(error, ProviderCommodity.FIELD_ERROR_CommodityID);
		p.setCommodityID(1);
		//
		p.setProviderID(-99);
		error = p.checkCreate(BaseBO.INVALID_CASE_ID);
		Assert.assertEquals(error, ProviderCommodity.FIELD_ERROR_ProviderID);
		p.setProviderID(0);
		error = p.checkCreate(BaseBO.INVALID_CASE_ID);
		Assert.assertEquals(error, ProviderCommodity.FIELD_ERROR_ProviderID);
		p.setProviderID(1);
		//
		error = p.checkCreate(BaseBO.INVALID_CASE_ID);
		Assert.assertEquals(error, "");
	}

	@Test
	public void checkDelete() {
		Shared.printTestMethodStartInfo();
		
		ProviderCommodity p = new ProviderCommodity();
		String error = "";
		//
		p.setCommodityID(-99);
		error = p.checkDelete(BaseBO.INVALID_CASE_ID);
		Assert.assertEquals(error, ProviderCommodity.FIELD_ERROR_CommodityID);
		p.setCommodityID(0);
		error = p.checkDelete(BaseBO.INVALID_CASE_ID);
		Assert.assertEquals(error, ProviderCommodity.FIELD_ERROR_CommodityID);
		p.setCommodityID(1);
		//
		p.setProviderID(-99);
		error = p.checkDelete(BaseBO.INVALID_CASE_ID);
		Assert.assertEquals(error, ProviderCommodity.FIELD_ERROR_ProviderID);
		p.setProviderID(0);
		//
		error = p.checkDelete(BaseBO.INVALID_CASE_ID);
		Assert.assertEquals(error, "");
	}

	@Test
	public void checkRetrieveN() {
		Shared.printTestMethodStartInfo();

		ProviderCommodity p = new ProviderCommodity();
		String error = "";
		//
		p.setCommodityID(-99);
		error = p.checkRetrieveN(BaseBO.INVALID_CASE_ID);
		Assert.assertEquals(error, ProviderCommodity.FIELD_ERROR_CommodityID);
		p.setCommodityID(0);
		error = p.checkRetrieveN(BaseBO.INVALID_CASE_ID);
		Assert.assertEquals(error, ProviderCommodity.FIELD_ERROR_CommodityID);
		p.setCommodityID(1);
		//
		p.setPageIndex(-666);
		error = p.checkRetrieveN(BaseBO.INVALID_CASE_ID);
		Assert.assertEquals(error, FieldFormat.FIELD_ERROR_Paging);
		p.setPageIndex(0);
		error = p.checkRetrieveN(BaseBO.INVALID_CASE_ID);
		Assert.assertEquals(error, FieldFormat.FIELD_ERROR_Paging);
		p.setPageIndex(1);
		//
		p.setPageSize(-666);
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
}
