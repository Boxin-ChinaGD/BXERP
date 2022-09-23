package com.bx.erp.model;

import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import com.bx.erp.action.bo.BaseBO;
import com.bx.erp.test.Shared;
import com.bx.erp.util.FieldFormat;

public class RetailTradeCommodityTest {

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

		RetailTradeCommodity retailTradeCommodity = new RetailTradeCommodity();
		String error = "";
		retailTradeCommodity.setNOCanReturn(1);
		retailTradeCommodity.setNO(1);
		retailTradeCommodity.setPriceReturn(1d);
		//
		retailTradeCommodity.setTradeID(0);
		error = retailTradeCommodity.checkCreate(BaseBO.INVALID_CASE_ID);
		Assert.assertEquals(error, RetailTradeCommodity.FIELD_ERROR_TradeID);
		retailTradeCommodity.setTradeID(-66);
		error = retailTradeCommodity.checkCreate(BaseBO.INVALID_CASE_ID);
		Assert.assertEquals(error, RetailTradeCommodity.FIELD_ERROR_TradeID);
		retailTradeCommodity.setTradeID(1);
		//
		retailTradeCommodity.setCommodityID(0);
		error = retailTradeCommodity.checkCreate(BaseBO.INVALID_CASE_ID);
		Assert.assertEquals(error, RetailTradeCommodity.FIELD_ERROR_CommodityID);
		retailTradeCommodity.setCommodityID(-66);
		error = retailTradeCommodity.checkCreate(BaseBO.INVALID_CASE_ID);
		Assert.assertEquals(error, RetailTradeCommodity.FIELD_ERROR_CommodityID);
		retailTradeCommodity.setCommodityID(1);
		//
		retailTradeCommodity.setBarcodeID(0);
		error = retailTradeCommodity.checkCreate(BaseBO.INVALID_CASE_ID);
		Assert.assertEquals(error, RetailTradeCommodity.FIELD_ERROR_BarcodeID);
		retailTradeCommodity.setBarcodeID(-66);
		error = retailTradeCommodity.checkCreate(BaseBO.INVALID_CASE_ID);
		Assert.assertEquals(error, RetailTradeCommodity.FIELD_ERROR_BarcodeID);
		retailTradeCommodity.setBarcodeID(1);
		//
		retailTradeCommodity.setPriceOriginal(0d);
		error = retailTradeCommodity.checkCreate(BaseBO.INVALID_CASE_ID);
		Assert.assertEquals(error, "");
		retailTradeCommodity.setPriceOriginal(-66d);
		error = retailTradeCommodity.checkCreate(BaseBO.INVALID_CASE_ID);
		Assert.assertEquals(error, RetailTradeCommodity.FIELD_ERROR_Price);
		retailTradeCommodity.setPriceOriginal(1.00d);
		//
		// retailTradeCommodity.setIsGift(-99);
		// error = retailTradeCommodity.checkCreate(BaseBO.INVALID_CASE_ID);
		// Assert.assertEquals(error, RetailTradeCommodity.FIELD_ERROR_isGift);
		// retailTradeCommodity.setIsGift(1);
		//
		retailTradeCommodity.setNO(0);
		error = retailTradeCommodity.checkCreate(BaseBO.INVALID_CASE_ID);
		Assert.assertEquals(error, RetailTradeCommodity.FIELD_ERROR_NO);
		retailTradeCommodity.setNO(-66);
		error = retailTradeCommodity.checkCreate(BaseBO.INVALID_CASE_ID);
		Assert.assertEquals(error, RetailTradeCommodity.FIELD_ERROR_NO);
		retailTradeCommodity.setNO(10000);
		error = retailTradeCommodity.checkCreate(BaseBO.INVALID_CASE_ID);
		Assert.assertEquals(error, RetailTradeCommodity.FIELD_ERROR_NO);
		retailTradeCommodity.setNO(1);
		//
		retailTradeCommodity.setNOCanReturn(-1);
		error = retailTradeCommodity.checkCreate(BaseBO.INVALID_CASE_ID);
		Assert.assertEquals(error, RetailTradeCommodity.FIELD_ERROR_NOCanReturn);
		retailTradeCommodity.setNOCanReturn(-99);
		error = retailTradeCommodity.checkCreate(BaseBO.INVALID_CASE_ID);
		Assert.assertEquals(error, RetailTradeCommodity.FIELD_ERROR_NOCanReturn);
		retailTradeCommodity.setNOCanReturn(10000);
		error = retailTradeCommodity.checkCreate(BaseBO.INVALID_CASE_ID);
		Assert.assertEquals(error, RetailTradeCommodity.FIELD_ERROR_NOCanReturn);
		retailTradeCommodity.setNOCanReturn(1);
		//
		retailTradeCommodity.setPriceReturn(-0.01d);
		error = retailTradeCommodity.checkCreate(BaseBO.INVALID_CASE_ID);
		Assert.assertEquals(error, RetailTradeCommodity.FIELD_ERROR_PriceReturn);
		retailTradeCommodity.setPriceReturn(-66d);
		error = retailTradeCommodity.checkCreate(BaseBO.INVALID_CASE_ID);
		Assert.assertEquals(error, RetailTradeCommodity.FIELD_ERROR_PriceReturn);
		retailTradeCommodity.setPriceReturn(1.00d);
		// 现阶段不理会员价
		// retailTradeCommodity.setPriceVIPOriginal(0d);
		// error = retailTradeCommodity.checkCreate(BaseBO.INVALID_CASE_ID);
		// Assert.assertEquals(error,
		// RetailTradeCommodity.FIELD_ERROR_PriceVIPOriginal);
		// retailTradeCommodity.setPriceVIPOriginal(-66d);
		// error = retailTradeCommodity.checkCreate(BaseBO.INVALID_CASE_ID);
		// Assert.assertEquals(error,
		// RetailTradeCommodity.FIELD_ERROR_PriceVIPOriginal);
		// retailTradeCommodity.setPriceVIPOriginal(1.00d);
		//
		error = retailTradeCommodity.checkCreate(BaseBO.INVALID_CASE_ID);
		Assert.assertEquals(error, "");
	}

	@Test
	public void testCheckRetrieve1() {
		Shared.printTestMethodStartInfo();

		RetailTradeCommodity retailTradeCommodity = new RetailTradeCommodity();
		String error = "";
		error = retailTradeCommodity.checkRetrieve1(BaseBO.INVALID_CASE_ID);
		Assert.assertEquals(error, "");
	}

	@Test
	public void testCheckRetrieveN() {
		Shared.printTestMethodStartInfo();

		RetailTradeCommodity retailTradeCommodity = new RetailTradeCommodity();
		String error = "";
		//
		retailTradeCommodity.setPageSize(0);
		error = retailTradeCommodity.checkRetrieveN(BaseBO.INVALID_CASE_ID);
		Assert.assertEquals(error, FieldFormat.FIELD_ERROR_Paging);
		retailTradeCommodity.setPageSize(0);
		error = retailTradeCommodity.checkRetrieveN(BaseBO.INVALID_CASE_ID);
		Assert.assertEquals(error, FieldFormat.FIELD_ERROR_Paging);
		retailTradeCommodity.setPageSize(1);
		//
		retailTradeCommodity.setPageIndex(0);
		error = retailTradeCommodity.checkRetrieveN(BaseBO.INVALID_CASE_ID);
		Assert.assertEquals(error, FieldFormat.FIELD_ERROR_Paging);
		retailTradeCommodity.setPageIndex(0);
		error = retailTradeCommodity.checkRetrieveN(BaseBO.INVALID_CASE_ID);
		Assert.assertEquals(error, FieldFormat.FIELD_ERROR_Paging);
		retailTradeCommodity.setPageIndex(1);
		//
		error = retailTradeCommodity.checkRetrieveN(BaseBO.INVALID_CASE_ID);
		Assert.assertEquals(error, "");
	}

	@Test
	public void testCheckUpdate() {
		Shared.printTestMethodStartInfo();

		RetailTradeCommodity retailTradeCommodity = new RetailTradeCommodity();
		String error = "";
		error = retailTradeCommodity.checkUpdate(BaseBO.INVALID_CASE_ID);
		Assert.assertEquals(error, "");
	}

	@Test
	public void testCheckDelete() {
		Shared.printTestMethodStartInfo();

		RetailTradeCommodity retailTradeCommodity = new RetailTradeCommodity();
		String error = "";
		//
		retailTradeCommodity.setTradeID(0);
		error = retailTradeCommodity.checkDelete(BaseBO.INVALID_CASE_ID);
		Assert.assertEquals(error, RetailTradeCommodity.FIELD_ERROR_TradeID);
		retailTradeCommodity.setTradeID(-66);
		error = retailTradeCommodity.checkDelete(BaseBO.INVALID_CASE_ID);
		Assert.assertEquals(error, RetailTradeCommodity.FIELD_ERROR_TradeID);
		retailTradeCommodity.setTradeID(1);
		//
		error = retailTradeCommodity.checkDelete(BaseBO.INVALID_CASE_ID);
		Assert.assertEquals(error, "");
	}
}
