package com.bx.erp.model;

import static org.testng.Assert.assertEquals;

import java.text.ParseException;

import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import com.bx.erp.action.BaseAction;
import com.bx.erp.action.bo.BaseBO;
import com.bx.erp.test.Shared;
import com.bx.erp.util.FieldFormat;

public class ReturnCommoditySheetCommodityTest {

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

		ReturnCommoditySheetCommodity returnCommoditySheetCommodity = new ReturnCommoditySheetCommodity();
		returnCommoditySheetCommodity.setReturnCommoditySheetID(1);
		returnCommoditySheetCommodity.setCommodityID(1);
		returnCommoditySheetCommodity.setBarcodeID(1);
		returnCommoditySheetCommodity.setNO(1);
		returnCommoditySheetCommodity.setSpecification("1");
		returnCommoditySheetCommodity.setPurchasingPrice(1.1);

		// 检查ReturnCommoditySheetID
		returnCommoditySheetCommodity.setReturnCommoditySheetID(BaseAction.INVALID_ID);
		assertEquals(returnCommoditySheetCommodity.checkCreate(BaseBO.INVALID_CASE_ID), ReturnCommoditySheetCommodity.FIELD_ERROR_returnCommoditySheetID);
		//
		returnCommoditySheetCommodity.setReturnCommoditySheetID(0);
		assertEquals(returnCommoditySheetCommodity.checkCreate(BaseBO.INVALID_CASE_ID), ReturnCommoditySheetCommodity.FIELD_ERROR_returnCommoditySheetID);
		//
		returnCommoditySheetCommodity.setReturnCommoditySheetID(1);
		assertEquals(returnCommoditySheetCommodity.checkCreate(BaseBO.INVALID_CASE_ID), "");

		// 检查commodityID
		returnCommoditySheetCommodity.setCommodityID(BaseAction.INVALID_ID);
		assertEquals(returnCommoditySheetCommodity.checkCreate(BaseBO.INVALID_CASE_ID), ReturnCommoditySheetCommodity.FIELD_ERROR_commodityID);
		//
		returnCommoditySheetCommodity.setCommodityID(0);
		assertEquals(returnCommoditySheetCommodity.checkCreate(BaseBO.INVALID_CASE_ID), ReturnCommoditySheetCommodity.FIELD_ERROR_commodityID);
		//
		returnCommoditySheetCommodity.setCommodityID(1);
		assertEquals(returnCommoditySheetCommodity.checkCreate(BaseBO.INVALID_CASE_ID), "");

		// 检查BarcodeID
		returnCommoditySheetCommodity.setBarcodeID(BaseAction.INVALID_ID);
		assertEquals(returnCommoditySheetCommodity.checkCreate(BaseBO.INVALID_CASE_ID), ReturnCommoditySheetCommodity.FIELD_ERROR_barcodeID);
		//
		returnCommoditySheetCommodity.setBarcodeID(0);
		assertEquals(returnCommoditySheetCommodity.checkCreate(BaseBO.INVALID_CASE_ID), ReturnCommoditySheetCommodity.FIELD_ERROR_barcodeID);
		//
		returnCommoditySheetCommodity.setBarcodeID(1);
		assertEquals(returnCommoditySheetCommodity.checkCreate(BaseBO.INVALID_CASE_ID), "");

		// 检查NO
		returnCommoditySheetCommodity.setNO(BaseAction.INVALID_NO);
		assertEquals(returnCommoditySheetCommodity.checkCreate(BaseBO.INVALID_CASE_ID), ReturnCommoditySheetCommodity.FIELD_ERROR_no);
		//
		returnCommoditySheetCommodity.setNO(0);
		assertEquals(returnCommoditySheetCommodity.checkCreate(BaseBO.INVALID_CASE_ID), ReturnCommoditySheetCommodity.FIELD_ERROR_no);
		returnCommoditySheetCommodity.setNO(FieldFormat.MAX_OneCommodityNO + 1);
		assertEquals(returnCommoditySheetCommodity.checkCreate(BaseBO.INVALID_CASE_ID), ReturnCommoditySheetCommodity.FIELD_ERROR_no);
		//
		returnCommoditySheetCommodity.setNO(1);
		assertEquals(returnCommoditySheetCommodity.checkCreate(BaseBO.INVALID_CASE_ID), "");
		returnCommoditySheetCommodity.setNO(FieldFormat.MAX_OneCommodityNO);
		assertEquals(returnCommoditySheetCommodity.checkCreate(BaseBO.INVALID_CASE_ID), "");

		// 检查PurchasingPrice
		returnCommoditySheetCommodity.setPurchasingPrice(-1);
		assertEquals(returnCommoditySheetCommodity.checkCreate(BaseBO.INVALID_CASE_ID), ReturnCommoditySheetCommodity.FIELD_ERROR_purchasingPrice);
		returnCommoditySheetCommodity.setPurchasingPrice(FieldFormat.MAX_OneCommodityPrice + 0.01);
		assertEquals(returnCommoditySheetCommodity.checkCreate(BaseBO.INVALID_CASE_ID), ReturnCommoditySheetCommodity.FIELD_ERROR_purchasingPrice);
		//
		returnCommoditySheetCommodity.setPurchasingPrice(0);
		assertEquals(returnCommoditySheetCommodity.checkCreate(BaseBO.INVALID_CASE_ID), "");
		//
		returnCommoditySheetCommodity.setPurchasingPrice(FieldFormat.MAX_OneCommodityPrice);
		assertEquals(returnCommoditySheetCommodity.checkCreate(BaseBO.INVALID_CASE_ID), "");

		// 检查Specification
		returnCommoditySheetCommodity.setSpecification("@#^*$");
		assertEquals(returnCommoditySheetCommodity.checkCreate(BaseBO.INVALID_CASE_ID), ReturnCommoditySheetCommodity.FIELD_ERROR_specification);
		//
		returnCommoditySheetCommodity.setSpecification("12345678a");
		assertEquals(returnCommoditySheetCommodity.checkCreate(BaseBO.INVALID_CASE_ID), ReturnCommoditySheetCommodity.FIELD_ERROR_specification);
		//
		returnCommoditySheetCommodity.setSpecification("");
		assertEquals(returnCommoditySheetCommodity.checkCreate(BaseBO.INVALID_CASE_ID), ReturnCommoditySheetCommodity.FIELD_ERROR_specification);
		//
		returnCommoditySheetCommodity.setSpecification("个");
		assertEquals(returnCommoditySheetCommodity.checkCreate(BaseBO.INVALID_CASE_ID), "");
	}

	@Test
	public void testCheckRetrieve1() {
		Shared.printTestMethodStartInfo();

	}

	@Test
	public void testCheckRetrieveN() throws ParseException {
		Shared.printTestMethodStartInfo();

		ReturnCommoditySheetCommodity returnCommoditySheetCommodity = new ReturnCommoditySheetCommodity();

		// 检查ReturnCommoditySheetID
		returnCommoditySheetCommodity.setReturnCommoditySheetID(BaseAction.INVALID_ID);
		assertEquals(returnCommoditySheetCommodity.checkRetrieveN(BaseBO.INVALID_CASE_ID), ReturnCommoditySheetCommodity.FIELD_ERROR_returnCommoditySheetID);
		//
		returnCommoditySheetCommodity.setReturnCommoditySheetID(0);
		assertEquals(returnCommoditySheetCommodity.checkRetrieveN(BaseBO.INVALID_CASE_ID), ReturnCommoditySheetCommodity.FIELD_ERROR_returnCommoditySheetID);
		//
		returnCommoditySheetCommodity.setReturnCommoditySheetID(1);
		assertEquals(returnCommoditySheetCommodity.checkRetrieveN(BaseBO.INVALID_CASE_ID), "");
	}

	@Test
	public void testCheckUpdate() {
		Shared.printTestMethodStartInfo();

	}

	@Test
	public void testCheckDelete() {
		Shared.printTestMethodStartInfo();

		ReturnCommoditySheetCommodity returnCommoditySheetCommodity = new ReturnCommoditySheetCommodity();
		returnCommoditySheetCommodity.setReturnCommoditySheetID(1);
		returnCommoditySheetCommodity.setCommodityID(1);

		// 检查ReturnCommoditySheetID
		returnCommoditySheetCommodity.setReturnCommoditySheetID(BaseAction.INVALID_ID);
		assertEquals(returnCommoditySheetCommodity.checkDelete(BaseBO.INVALID_CASE_ID), ReturnCommoditySheetCommodity.FIELD_ERROR_returnCommoditySheetID);
		//
		returnCommoditySheetCommodity.setReturnCommoditySheetID(0);
		assertEquals(returnCommoditySheetCommodity.checkDelete(BaseBO.INVALID_CASE_ID), ReturnCommoditySheetCommodity.FIELD_ERROR_returnCommoditySheetID);
		//
		returnCommoditySheetCommodity.setReturnCommoditySheetID(1);
		assertEquals(returnCommoditySheetCommodity.checkDelete(BaseBO.INVALID_CASE_ID), "");

		// 检查CommodityID
		// returnCommoditySheetCommodity.setCommodityID(BaseAction.INVALID_ID);
		// assertEquals(returnCommoditySheetCommodity.checkDelete(BaseBO.INVALID_CASE_ID),
		// ReturnCommoditySheetCommodity.FIELD_ERROR_commodityID);
		// //
		// returnCommoditySheetCommodity.setCommodityID(0);
		// assertEquals(returnCommoditySheetCommodity.checkDelete(BaseBO.INVALID_CASE_ID),
		// ReturnCommoditySheetCommodity.FIELD_ERROR_commodityID);
		// //
		// returnCommoditySheetCommodity.setCommodityID(1);
		// assertEquals(returnCommoditySheetCommodity.checkDelete(BaseBO.INVALID_CASE_ID),
		// "");

	}
}
