package com.bx.erp.model.trade;

import static org.testng.Assert.assertEquals;

import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import com.bx.erp.action.BaseAction;
import com.bx.erp.action.bo.BaseBO;
import com.bx.erp.test.Shared;
import com.bx.erp.util.FieldFormat;

public class RetailTradePromotingFlowTest {
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

		RetailTradePromotingFlow rPromotingFlow = new RetailTradePromotingFlow();

		// 检查retailTradePromotingID
		rPromotingFlow.setPromotionID(1);
		rPromotingFlow.setProcessFlow("checkCreateTest");
		assertEquals(rPromotingFlow.checkCreate(BaseBO.INVALID_CASE_ID), RetailTradePromotingFlow.FIELD_ERROR_retailTradePromotingID);
		//
		rPromotingFlow.setRetailTradePromotingID(BaseAction.INVALID_ID);
		assertEquals(rPromotingFlow.checkCreate(BaseBO.INVALID_CASE_ID), RetailTradePromotingFlow.FIELD_ERROR_retailTradePromotingID);
		//
		rPromotingFlow.setRetailTradePromotingID(1);
		assertEquals(rPromotingFlow.checkCreate(BaseBO.INVALID_CASE_ID), "");
		// 检查PromotionID
		rPromotingFlow.setPromotionID(BaseAction.INVALID_ID);
		assertEquals(rPromotingFlow.checkCreate(BaseBO.INVALID_CASE_ID), RetailTradePromotingFlow.FIELD_ERROR_promotionID);
		//
		rPromotingFlow.setPromotionID(0);
		assertEquals(rPromotingFlow.checkCreate(BaseBO.INVALID_CASE_ID), "");
		//
		rPromotingFlow.setPromotionID(1);
		assertEquals(rPromotingFlow.checkCreate(BaseBO.INVALID_CASE_ID), "");
		// 检查ProcessFlow
		rPromotingFlow.setProcessFlow(null);
		assertEquals(rPromotingFlow.checkCreate(BaseBO.INVALID_CASE_ID), RetailTradePromotingFlow.FIELD_ERROR_processFlow);
		//
		rPromotingFlow.setProcessFlow("");
		assertEquals(rPromotingFlow.checkCreate(BaseBO.INVALID_CASE_ID), RetailTradePromotingFlow.FIELD_ERROR_processFlow);
		//
		rPromotingFlow.setProcessFlow("checkCreateTest");
		assertEquals(rPromotingFlow.checkCreate(BaseBO.INVALID_CASE_ID), "");
	}

	@Test
	public void checkRetrieveN() {
		RetailTradePromotingFlow rPromotingFlow = new RetailTradePromotingFlow();

		// 检查retailTradePromotingID
		rPromotingFlow.setPromotionID(1);
		rPromotingFlow.setProcessFlow("checkCreateTest");
		assertEquals(rPromotingFlow.checkRetrieveN(BaseBO.INVALID_CASE_ID), RetailTradePromotingFlow.FIELD_ERROR_retailTradePromotingID);
		//
		rPromotingFlow.setRetailTradePromotingID(BaseAction.INVALID_ID);
		assertEquals(rPromotingFlow.checkRetrieveN(BaseBO.INVALID_CASE_ID), RetailTradePromotingFlow.FIELD_ERROR_retailTradePromotingID);
		//
		rPromotingFlow.setRetailTradePromotingID(1);
		assertEquals(rPromotingFlow.checkRetrieveN(BaseBO.INVALID_CASE_ID), "");
		// 检查page indx
		rPromotingFlow.setPageIndex(-1);
		rPromotingFlow.setPageSize(BaseAction.PAGE_SIZE);
		assertEquals(rPromotingFlow.checkRetrieveN(BaseBO.INVALID_CASE_ID), FieldFormat.FIELD_ERROR_Paging);
		//
		rPromotingFlow.setPageIndex(BaseAction.PAGE_StartIndex);
		rPromotingFlow.setPageSize(-1);
		assertEquals(rPromotingFlow.checkRetrieveN(BaseBO.INVALID_CASE_ID), FieldFormat.FIELD_ERROR_Paging);
		//
		rPromotingFlow.setPageIndex(BaseAction.PAGE_StartIndex);
		rPromotingFlow.setPageSize(BaseAction.PAGE_SIZE);
		assertEquals(rPromotingFlow.checkRetrieveN(BaseBO.INVALID_CASE_ID), "");
	}

	@Test
	public void checkRetrieve1() {

	}

}
