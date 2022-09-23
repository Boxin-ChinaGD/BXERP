package com.test.bx.app.model;


import com.base.BaseAndroidTestCase;
import com.bx.erp.bo.BaseSQLiteBO;
import com.bx.erp.model.PromotionScope;
import com.bx.erp.utils.FieldFormat;
import com.bx.erp.utils.Shared;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

public class PromotionScopeTest extends BaseAndroidTestCase {
    public static final String PAGE_SIZE = "10";
    public static final String PAGE_StartIndex = "1";
    public static final int INVALID_ID = -1;

    @BeforeClass
    public void setup() {
        Shared.printTestClassStartInfo();
    }

    @AfterClass
    public void tearDown() {
        Shared.printTestClassEndInfo();
    }

    @Test
    public void test_checkCreate() {
        PromotionScope promotionScope = new PromotionScope();
        promotionScope.setPromotionID(1);
        promotionScope.setCommodityID(1);
        // 检查PromotionID
        promotionScope.setPromotionID(0);
        assertEquals(promotionScope.checkCreate(BaseSQLiteBO.INVALID_CASE_ID), PromotionScope.FIELD_ERROR_promotionID);
        //
        promotionScope.setPromotionID(INVALID_ID);
        assertEquals(promotionScope.checkCreate(BaseSQLiteBO.INVALID_CASE_ID), PromotionScope.FIELD_ERROR_promotionID);
        //
        promotionScope.setPromotionID(1);
        assertEquals(promotionScope.checkCreate(BaseSQLiteBO.INVALID_CASE_ID), "");
        // 检查CommodityID
        promotionScope.setCommodityID(0);
        assertEquals(promotionScope.checkCreate(BaseSQLiteBO.INVALID_CASE_ID), PromotionScope.FIELD_ERROR_commodityID);
        //
        promotionScope.setCommodityID(INVALID_ID);
        assertEquals(promotionScope.checkCreate(BaseSQLiteBO.INVALID_CASE_ID), PromotionScope.FIELD_ERROR_commodityID);
        //
        promotionScope.setCommodityID(1);
        assertEquals(promotionScope.checkCreate(BaseSQLiteBO.INVALID_CASE_ID), "");
    }

    @Test
    public void test_checkRetrieveN() {
        PromotionScope promotionScope = new PromotionScope();
        // 检查PromotionID
        assertEquals(promotionScope.checkRetrieveN(BaseSQLiteBO.INVALID_CASE_ID), PromotionScope.FIELD_ERROR_promotionID);
        //
        promotionScope.setPromotionID(INVALID_ID);
        assertEquals(promotionScope.checkRetrieveN(BaseSQLiteBO.INVALID_CASE_ID), PromotionScope.FIELD_ERROR_promotionID);
        //
        promotionScope.setPromotionID(1);
        assertEquals(promotionScope.checkRetrieveN(BaseSQLiteBO.INVALID_CASE_ID), "");
        // 检查page indx
        promotionScope.setPageIndex("-1");
        promotionScope.setPageSize(PAGE_SIZE);
        assertEquals(promotionScope.checkRetrieveN(BaseSQLiteBO.INVALID_CASE_ID), FieldFormat.FIELD_ERROR_Paging);
        //
        promotionScope.setPageIndex(PAGE_StartIndex);
        promotionScope.setPageSize("-1");
        assertEquals(promotionScope.checkRetrieveN(BaseSQLiteBO.INVALID_CASE_ID), FieldFormat.FIELD_ERROR_Paging);
        //
        promotionScope.setPageIndex(PAGE_StartIndex);
        promotionScope.setPageSize(PAGE_SIZE);
        assertEquals(promotionScope.checkRetrieveN(BaseSQLiteBO.INVALID_CASE_ID), "");
    }

}
