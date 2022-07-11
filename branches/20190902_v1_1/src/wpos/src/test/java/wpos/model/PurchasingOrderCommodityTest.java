package wpos.model;


import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import wpos.base.BaseTestCase;
import wpos.bo.BaseSQLiteBO;
import wpos.utils.FieldFormat;
import wpos.utils.Shared;

public class PurchasingOrderCommodityTest extends BaseTestCase {

    @BeforeClass
    public void setUp() {
        Shared.printTestClassStartInfo();
    }

    @AfterClass
    public void tearDown() {
        Shared.printTestClassEndInfo();
    }

    @Test
    public void test_a_checkCreate() {
        Shared.printTestMethodStartInfo();
        PurchasingOrderCommodity p = new PurchasingOrderCommodity();
        p.setCommodityNO(1);
        p.setPurchasingOrderID(1);
        p.setCommodityID(1);
        p.setBarcodeID(1);
        p.setPriceSuggestion(0.0f);
        String error = "";
        // checkCreate已注释掉PurchasingOrderID检查，因为pos发送请求创建入库单时主表尚未创建，此时的从表中的该字段为空，不应检查该字段。(nbr中是从前端拿到数据然后分别插入主从表)
//        p.setPurchasingOrderID(0);
//        error = p.checkCreate(BaseSQLiteBO.INVALID_CASE_ID);
//        Assert.assertEquals(error, PurchasingOrderCommodity.FIELD_ERROR_PurchasingOrderID);
//        p.setPurchasingOrderID(-1);
//        error = p.checkCreate(BaseSQLiteBO.INVALID_CASE_ID);
//        Assert.assertEquals(error, PurchasingOrderCommodity.FIELD_ERROR_PurchasingOrderID);
//        p.setPurchasingOrderID(1);
        //
        p.setCommodityID(0);
        error = p.checkCreate(BaseSQLiteBO.INVALID_CASE_ID);
        Assert.assertEquals(error, PurchasingOrderCommodity.FIELD_ERROR_CommodityID);
        p.setCommodityID(-1);
        error = p.checkCreate(BaseSQLiteBO.INVALID_CASE_ID);
        Assert.assertEquals(error, PurchasingOrderCommodity.FIELD_ERROR_CommodityID);
        p.setCommodityID(1);
        //
        p.setBarcodeID(0);
        error = p.checkCreate(BaseSQLiteBO.INVALID_CASE_ID);
        Assert.assertEquals(error, PurchasingOrderCommodity.FIELD_ERROR_BarcodeID);
        p.setBarcodeID(-1);
        error = p.checkCreate(BaseSQLiteBO.INVALID_CASE_ID);
        Assert.assertEquals(error, PurchasingOrderCommodity.FIELD_ERROR_BarcodeID);
        p.setBarcodeID(1);
        //	
        p.setCommodityNO(-1);
        error = p.checkCreate(BaseSQLiteBO.INVALID_CASE_ID);
        Assert.assertEquals(error, PurchasingOrderCommodity.FIELD_ERROR_CommodityNO);
        p.setCommodityNO(1);
        //
        p.setPriceSuggestion(-0.000001f);
        error = p.checkCreate(BaseSQLiteBO.INVALID_CASE_ID);
        Assert.assertEquals(error, PurchasingOrderCommodity.FIELD_ERROR_PriceSuggestion);
        p.setPriceSuggestion(0.0f);
        //
        error = p.checkCreate(BaseSQLiteBO.INVALID_CASE_ID);
        Assert.assertEquals(error, "");
    }

    @Test
    public void test_b_checkDelete() {
        Shared.printTestMethodStartInfo();
        PurchasingOrderCommodity p = new PurchasingOrderCommodity();
        String error = "";
        //
        p.setPurchasingOrderID(0);
        error = p.checkDelete(BaseSQLiteBO.INVALID_CASE_ID);
        Assert.assertEquals(error, PurchasingOrderCommodity.FIELD_ERROR_PurchasingOrderID);
        p.setPurchasingOrderID(-1);
        error = p.checkDelete(BaseSQLiteBO.INVALID_CASE_ID);
        Assert.assertEquals(error, PurchasingOrderCommodity.FIELD_ERROR_PurchasingOrderID);
        p.setPurchasingOrderID(1);
        //
        error = p.checkDelete(BaseSQLiteBO.INVALID_CASE_ID);
        Assert.assertEquals(error, "");
    }

    @Test
    public void test_c_checkRetrieveN() {
        Shared.printTestMethodStartInfo();
        PurchasingOrderCommodity p = new PurchasingOrderCommodity();
        String error = "";

        // 普通case
        p.setPurchasingOrderID(0);
        error = p.checkRetrieveN(BaseSQLiteBO.INVALID_CASE_ID);
        Assert.assertEquals(error, PurchasingOrderCommodity.FIELD_ERROR_PurchasingOrderID);
        p.setPurchasingOrderID(-1);
        error = p.checkRetrieveN(BaseSQLiteBO.INVALID_CASE_ID);
        Assert.assertEquals(error, PurchasingOrderCommodity.FIELD_ERROR_PurchasingOrderID);
        p.setPurchasingOrderID(1);
        //
        p.setPageIndex("-88");
        p.setPageSize("10");
        error = p.checkRetrieveN(BaseSQLiteBO.INVALID_CASE_ID);
        Assert.assertEquals(error, FieldFormat.FIELD_ERROR_Paging);
        p.setPageIndex("0");
        p.setPageSize("10");
        error = p.checkRetrieveN(BaseSQLiteBO.INVALID_CASE_ID);
        Assert.assertEquals(error, FieldFormat.FIELD_ERROR_Paging);
        p.setPageIndex("1");
        p.setPageSize("10");
        //
        // checkPageSize
        p.setPageSize("-88");
        p.setPageIndex("1");
        error = p.checkRetrieveN(BaseSQLiteBO.INVALID_CASE_ID);
        Assert.assertEquals(error, FieldFormat.FIELD_ERROR_Paging);
        p.setPageSize("0");
        p.setPageIndex("1");
        error = p.checkRetrieveN(BaseSQLiteBO.INVALID_CASE_ID);
        Assert.assertEquals(error, FieldFormat.FIELD_ERROR_Paging);
        p.setPageSize("1");
        p.setPageIndex("1");
        //
        error = p.checkRetrieveN(BaseSQLiteBO.INVALID_CASE_ID);
        Assert.assertEquals(error, "");
    }
    
}
