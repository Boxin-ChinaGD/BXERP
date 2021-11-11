package com.bx.erp.model.trade;

import static org.testng.Assert.assertEquals;


import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import com.bx.erp.action.BaseAction;
import com.bx.erp.action.bo.BaseBO;
import com.bx.erp.test.BaseTestNGSpringContextTest;
import com.bx.erp.test.Shared;
import com.bx.erp.util.FieldFormat;

public class PromotionScopeTest extends BaseTestNGSpringContextTest {
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
		PromotionScope promotionScope = new PromotionScope();
		promotionScope.setPromotionID(1);
		promotionScope.setCommodityID(1);
		// 检查PromotionID
		promotionScope.setPromotionID(0);
		assertEquals(promotionScope.checkCreate(BaseBO.INVALID_CASE_ID), PromotionScope.FIELD_ERROR_promotionID);
		//
		promotionScope.setPromotionID(BaseAction.INVALID_ID);
		assertEquals(promotionScope.checkCreate(BaseBO.INVALID_CASE_ID), PromotionScope.FIELD_ERROR_promotionID);
		//
		promotionScope.setPromotionID(1);
		assertEquals(promotionScope.checkCreate(BaseBO.INVALID_CASE_ID), "");
		// 检查CommodityID
		promotionScope.setCommodityID(0);
		assertEquals(promotionScope.checkCreate(BaseBO.INVALID_CASE_ID), PromotionScope.FIELD_ERROR_commodityID);
		//
		promotionScope.setCommodityID(BaseAction.INVALID_ID);
		assertEquals(promotionScope.checkCreate(BaseBO.INVALID_CASE_ID), PromotionScope.FIELD_ERROR_commodityID);
		//
		promotionScope.setCommodityID(1);
		assertEquals(promotionScope.checkCreate(BaseBO.INVALID_CASE_ID), "");
	}

	@Test
	public void checkRetrieveN() {
		PromotionScope promotionScope = new PromotionScope();
		// 检查PromotionID
		assertEquals(promotionScope.checkRetrieveN(BaseBO.INVALID_CASE_ID), PromotionScope.FIELD_ERROR_promotionID);
		//
		promotionScope.setPromotionID(BaseAction.INVALID_ID);
		assertEquals(promotionScope.checkRetrieveN(BaseBO.INVALID_CASE_ID), PromotionScope.FIELD_ERROR_promotionID);
		//
		promotionScope.setPromotionID(1);
		assertEquals(promotionScope.checkRetrieveN(BaseBO.INVALID_CASE_ID), "");
		// 检查page indx
		promotionScope.setPageIndex(-1);
		promotionScope.setPageSize(BaseAction.PAGE_SIZE);
		assertEquals(promotionScope.checkRetrieveN(BaseBO.INVALID_CASE_ID), FieldFormat.FIELD_ERROR_Paging);
		//
		promotionScope.setPageIndex(BaseAction.PAGE_StartIndex);
		promotionScope.setPageSize(-1);
		assertEquals(promotionScope.checkRetrieveN(BaseBO.INVALID_CASE_ID), FieldFormat.FIELD_ERROR_Paging);
		//
		promotionScope.setPageIndex(BaseAction.PAGE_StartIndex);
		promotionScope.setPageSize(BaseAction.PAGE_SIZE);
		assertEquals(promotionScope.checkRetrieveN(BaseBO.INVALID_CASE_ID), "");
	}
	
	
	
	
	
	
}
