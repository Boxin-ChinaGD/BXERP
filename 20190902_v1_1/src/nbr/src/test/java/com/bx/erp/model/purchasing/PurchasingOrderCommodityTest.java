package com.bx.erp.model.purchasing;

import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import com.bx.erp.action.bo.BaseBO;
import com.bx.erp.test.Shared;
import com.bx.erp.util.FieldFormat;

public class PurchasingOrderCommodityTest {
	@BeforeClass
	public void setup() {
		Shared.printTestClassStartInfo();
	}

	@AfterClass
	public void tearDown() {
		Shared.printTestClassEndInfo();
	}

	@Test
	public void checkRetrieveN() {
		Shared.printTestMethodStartInfo();
		PurchasingOrderCommodity p = new PurchasingOrderCommodity();
		String error = "";
		//
		p.setPurchasingOrderID(0);
		error = p.checkRetrieveN(BaseBO.CASE_PurchasingOrderCommodityRetrieveNNoneWarhousing);
		Assert.assertEquals(error, PurchasingOrderCommodity.FIELD_ERROR_PurchasingOrderID);
		p.setPurchasingOrderID(-1);
		error = p.checkRetrieveN(BaseBO.CASE_PurchasingOrderCommodityRetrieveNNoneWarhousing);
		Assert.assertEquals(error, PurchasingOrderCommodity.FIELD_ERROR_PurchasingOrderID);
		p.setPurchasingOrderID(1);
		//
		error = p.checkRetrieveN(BaseBO.CASE_PurchasingOrderCommodityRetrieveNNoneWarhousing);
		Assert.assertEquals(error, "");

		// 普通case
		p.setPurchasingOrderID(0);
		error = p.checkRetrieveN(BaseBO.INVALID_CASE_ID);
		Assert.assertEquals(error, PurchasingOrderCommodity.FIELD_ERROR_PurchasingOrderID);
		p.setPurchasingOrderID(-1);
		error = p.checkRetrieveN(BaseBO.INVALID_CASE_ID);
		Assert.assertEquals(error, PurchasingOrderCommodity.FIELD_ERROR_PurchasingOrderID);
		p.setPurchasingOrderID(1);
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
	public void checkRetrieveNEx() {
		Shared.printTestMethodStartInfo();
		PurchasingOrderCommodity p = new PurchasingOrderCommodity();
		String error = "";
		//
		p.setPurchasingOrderID(0);
		error = p.checkRetrieveNEx(BaseBO.CASE_PurchasingOrderCommodityRetrieveNWarhousing);
		Assert.assertEquals(error, PurchasingOrderCommodity.FIELD_ERROR_PurchasingOrderID);
		p.setPurchasingOrderID(-1);
		error = p.checkRetrieveNEx(BaseBO.CASE_PurchasingOrderCommodityRetrieveNWarhousing);
		Assert.assertEquals(error, PurchasingOrderCommodity.FIELD_ERROR_PurchasingOrderID);
		p.setPurchasingOrderID(1);
		//
		error = p.checkRetrieveNEx(BaseBO.CASE_PurchasingOrderCommodityRetrieveNWarhousing);
		Assert.assertEquals(error, "");
	}

	@Test
	public void checkCreate() {
		Shared.printTestMethodStartInfo();
		PurchasingOrderCommodity p = new PurchasingOrderCommodity();
		p.setCommodityNO(1);
		p.setPurchasingOrderID(1);
		p.setCommodityID(1);
		p.setBarcodeID(1);
		p.setPriceSuggestion(0.0d);
		String error = "";
		//
		p.setPurchasingOrderID(0);
		error = p.checkCreate(BaseBO.INVALID_CASE_ID);
		Assert.assertEquals(error, PurchasingOrderCommodity.FIELD_ERROR_PurchasingOrderID);
		p.setPurchasingOrderID(-1);
		error = p.checkCreate(BaseBO.INVALID_CASE_ID);
		Assert.assertEquals(error, PurchasingOrderCommodity.FIELD_ERROR_PurchasingOrderID);
		p.setPurchasingOrderID(1);
		//
		p.setCommodityID(0);
		error = p.checkCreate(BaseBO.INVALID_CASE_ID);
		Assert.assertEquals(error, PurchasingOrderCommodity.FIELD_ERROR_CommodityID);
		p.setCommodityID(-1);
		error = p.checkCreate(BaseBO.INVALID_CASE_ID);
		Assert.assertEquals(error, PurchasingOrderCommodity.FIELD_ERROR_CommodityID);
		p.setCommodityID(1);
		//
		p.setBarcodeID(0);
		error = p.checkCreate(BaseBO.INVALID_CASE_ID);
		Assert.assertEquals(error, PurchasingOrderCommodity.FIELD_ERROR_BarcodeID);
		p.setBarcodeID(-1);
		error = p.checkCreate(BaseBO.INVALID_CASE_ID);
		Assert.assertEquals(error, PurchasingOrderCommodity.FIELD_ERROR_BarcodeID);
		p.setBarcodeID(1);
		//
		p.setCommodityNO(-1);
		error = p.checkCreate(BaseBO.INVALID_CASE_ID);
		Assert.assertEquals(error, PurchasingOrderCommodity.FIELD_ERROR_CommodityNO);
		p.setCommodityNO(0);
		error = p.checkCreate(BaseBO.INVALID_CASE_ID);
		Assert.assertEquals(error, PurchasingOrderCommodity.FIELD_ERROR_CommodityNO);
		p.setCommodityNO(FieldFormat.MAX_OneCommodityNO + 1);
		error = p.checkCreate(BaseBO.INVALID_CASE_ID);
		Assert.assertEquals(error, PurchasingOrderCommodity.FIELD_ERROR_CommodityNO);
		p.setCommodityNO(FieldFormat.MAX_OneCommodityNO);
		error = p.checkCreate(BaseBO.INVALID_CASE_ID);
		Assert.assertEquals(error, "");
		p.setCommodityNO(1);
		//
		p.setPriceSuggestion(-0.000001d);
		error = p.checkCreate(BaseBO.INVALID_CASE_ID);
		Assert.assertEquals(error, PurchasingOrderCommodity.FIELD_ERROR_PriceSuggestion);
		p.setPriceSuggestion(FieldFormat.MAX_OneCommodityPrice + 0.01);
		error = p.checkCreate(BaseBO.INVALID_CASE_ID);
		Assert.assertEquals(error, PurchasingOrderCommodity.FIELD_ERROR_PriceSuggestion);
		p.setPriceSuggestion(FieldFormat.MAX_OneCommodityPrice);
		error = p.checkCreate(BaseBO.INVALID_CASE_ID);
		Assert.assertEquals(error, "");
		p.setPriceSuggestion(0.0d);
		//
		error = p.checkCreate(BaseBO.INVALID_CASE_ID);
		Assert.assertEquals(error, "");
	}

	@Test
	public void checkDelete() {
		Shared.printTestMethodStartInfo();
		PurchasingOrderCommodity p = new PurchasingOrderCommodity();
		String error = "";
		//
		p.setPurchasingOrderID(0);
		error = p.checkDelete(BaseBO.INVALID_CASE_ID);
		Assert.assertEquals(error, PurchasingOrderCommodity.FIELD_ERROR_PurchasingOrderID);
		p.setPurchasingOrderID(-1);
		error = p.checkDelete(BaseBO.INVALID_CASE_ID);
		Assert.assertEquals(error, PurchasingOrderCommodity.FIELD_ERROR_PurchasingOrderID);
		p.setPurchasingOrderID(1);
		//
		error = p.checkDelete(BaseBO.INVALID_CASE_ID);
		Assert.assertEquals(error, "");
	}
}
