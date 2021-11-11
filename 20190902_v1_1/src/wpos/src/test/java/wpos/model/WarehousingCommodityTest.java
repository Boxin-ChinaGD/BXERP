package wpos.model;


import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import wpos.base.BaseTestCase;
import wpos.bo.BaseSQLiteBO;
import wpos.utils.FieldFormat;
import wpos.utils.Shared;

import static org.testng.AssertJUnit.assertEquals;

public class WarehousingCommodityTest  extends BaseTestCase {

    @BeforeClass
    public void setUp() {
        Shared.printTestClassStartInfo();
    }

    @AfterClass
    public void tearDown() {
        Shared.printTestClassEndInfo();
    }

    @Test
    public void test_a_checkUpdate() {
        WarehousingCommodity warehousingCommodity = new WarehousingCommodity();
        warehousingCommodity.setCommodityID(1);
        warehousingCommodity.setNO(1);
        warehousingCommodity.setPrice(3.2f);
        warehousingCommodity.setAmount(2);

        // 检查warehouseID
        assertEquals(warehousingCommodity.checkUpdate(BaseSQLiteBO.INVALID_CASE_ID), WarehousingCommodity.FIELD_ERROR_warehousingID);
        //
        warehousingCommodity.setWarehousingID(BaseSQLiteBO.INVALID_INT_ID);
        assertEquals(warehousingCommodity.checkUpdate(BaseSQLiteBO.INVALID_CASE_ID), WarehousingCommodity.FIELD_ERROR_warehousingID);
        //
        warehousingCommodity.setWarehousingID(1);
        assertEquals(warehousingCommodity.checkUpdate(BaseSQLiteBO.INVALID_CASE_ID), "");
        // 检查NO
        warehousingCommodity.setNO(-10);
        assertEquals(warehousingCommodity.checkUpdate(BaseSQLiteBO.INVALID_CASE_ID), WarehousingCommodity.FIELD_ERROR_no);
        //
        warehousingCommodity.setNO(0);
        assertEquals(warehousingCommodity.checkUpdate(BaseSQLiteBO.INVALID_CASE_ID), WarehousingCommodity.FIELD_ERROR_no);
        //
        warehousingCommodity.setNO(1);
        assertEquals(warehousingCommodity.checkUpdate(BaseSQLiteBO.INVALID_CASE_ID), "");
        // 检查price
        warehousingCommodity.setPrice(-1);
        assertEquals(warehousingCommodity.checkUpdate(BaseSQLiteBO.INVALID_CASE_ID), WarehousingCommodity.FIELD_ERROR_price);
        //
        warehousingCommodity.setPrice(1);
        assertEquals(warehousingCommodity.checkUpdate(BaseSQLiteBO.INVALID_CASE_ID), "");
        // 检查amount
        warehousingCommodity.setAmount(-1);
        assertEquals(warehousingCommodity.checkUpdate(BaseSQLiteBO.INVALID_CASE_ID), WarehousingCommodity.FIELD_ERROR_amount);
        //
        warehousingCommodity.setAmount(1);
        assertEquals(warehousingCommodity.checkUpdate(BaseSQLiteBO.INVALID_CASE_ID), "");
        // 检查commodityID
        warehousingCommodity.setCommodityID(BaseSQLiteBO.INVALID_INT_ID);
        assertEquals(warehousingCommodity.checkUpdate(BaseSQLiteBO.INVALID_CASE_ID), WarehousingCommodity.FIELD_ERROR_commodityID);
        //
        warehousingCommodity.setCommodityID(1);
        assertEquals(warehousingCommodity.checkUpdate(BaseSQLiteBO.INVALID_CASE_ID), "");
    }

    @Test
    public void test_b_checkCreate() {
        WarehousingCommodity warehousingCommodity = new WarehousingCommodity();
        warehousingCommodity.setBarcodeID(1);
        warehousingCommodity.setCommodityID(1);
        warehousingCommodity.setNO(1);
        warehousingCommodity.setPrice(3.2f);
        warehousingCommodity.setAmount(2);
        warehousingCommodity.setShelfLife(1);
        warehousingCommodity.setCommodityName("checkCreate");

        // 检查warehouseID
        // checkCreate已注释掉这个检查了，原因：pos发送请求创建入库单时主表尚未创建，此时的从表中的该字段为空，不应检查该字段。(nbr中是从前端拿到数据然后分别插入主从表)
//        assertEquals(warehousingCommodity.checkCreate(BaseSQLiteBO.INVALID_CASE_ID), WarehousingCommodity.FIELD_ERROR_warehousingID);
        //
//        warehousingCommodity.setWarehousingID(BaseSQLiteBO.INVALID_INT_ID);
//        assertEquals(warehousingCommodity.checkCreate(BaseSQLiteBO.INVALID_CASE_ID), WarehousingCommodity.FIELD_ERROR_warehousingID);
        //
//        warehousingCommodity.setWarehousingID(1);
//        assertEquals(warehousingCommodity.checkCreate(BaseSQLiteBO.INVALID_CASE_ID), "");
        // 检查bardcodesID
        warehousingCommodity.setBarcodeID(BaseSQLiteBO.INVALID_INT_ID);
        assertEquals(warehousingCommodity.checkCreate(BaseSQLiteBO.INVALID_CASE_ID), WarehousingCommodity.FIELD_ERROR_barcodeID);
        //
        warehousingCommodity.setBarcodeID(0);
        assertEquals(warehousingCommodity.checkCreate(BaseSQLiteBO.INVALID_CASE_ID), WarehousingCommodity.FIELD_ERROR_barcodeID);
        //
        warehousingCommodity.setBarcodeID(1);
        assertEquals(warehousingCommodity.checkCreate(BaseSQLiteBO.INVALID_CASE_ID), "");
        // 检查commodityID
        warehousingCommodity.setCommodityID(BaseSQLiteBO.INVALID_INT_ID);
        assertEquals(warehousingCommodity.checkCreate(BaseSQLiteBO.INVALID_CASE_ID), WarehousingCommodity.FIELD_ERROR_commodityID);
        //
        warehousingCommodity.setCommodityID(0);
        assertEquals(warehousingCommodity.checkCreate(BaseSQLiteBO.INVALID_CASE_ID), WarehousingCommodity.FIELD_ERROR_commodityID);
        //
        warehousingCommodity.setCommodityID(1);
        assertEquals(warehousingCommodity.checkCreate(BaseSQLiteBO.INVALID_CASE_ID), "");
        // 检查NO
        warehousingCommodity.setNO(-10);
        assertEquals(warehousingCommodity.checkCreate(BaseSQLiteBO.INVALID_CASE_ID), WarehousingCommodity.FIELD_ERROR_no);
        //
        warehousingCommodity.setNO(0);
        assertEquals(warehousingCommodity.checkCreate(BaseSQLiteBO.INVALID_CASE_ID), WarehousingCommodity.FIELD_ERROR_no);
        //
        warehousingCommodity.setNO(1);
        assertEquals(warehousingCommodity.checkCreate(BaseSQLiteBO.INVALID_CASE_ID), "");
        // 检查price
        warehousingCommodity.setPrice(-1);
        assertEquals(warehousingCommodity.checkCreate(BaseSQLiteBO.INVALID_CASE_ID), WarehousingCommodity.FIELD_ERROR_price);
        //
        warehousingCommodity.setPrice(1);
        assertEquals(warehousingCommodity.checkCreate(BaseSQLiteBO.INVALID_CASE_ID), "");
        // 检查amount
        warehousingCommodity.setAmount(-1);
        assertEquals(warehousingCommodity.checkCreate(BaseSQLiteBO.INVALID_CASE_ID), WarehousingCommodity.FIELD_ERROR_amount);
        //
        warehousingCommodity.setAmount(1);
        assertEquals(warehousingCommodity.checkCreate(BaseSQLiteBO.INVALID_CASE_ID), "");
        // 检查shelfLife
        warehousingCommodity.setShelfLife(0);
        assertEquals(warehousingCommodity.checkCreate(BaseSQLiteBO.INVALID_CASE_ID), WarehousingCommodity.FIELD_ERROR_shelfLife);
        //
        warehousingCommodity.setShelfLife(1);
        assertEquals(warehousingCommodity.checkCreate(BaseSQLiteBO.INVALID_CASE_ID), "");

    }

    @Test
    public void test_c_checkDelete() {
        WarehousingCommodity warehousingCommodity = new WarehousingCommodity();
        assertEquals(warehousingCommodity.checkDelete(BaseSQLiteBO.INVALID_CASE_ID), WarehousingCommodity.FIELD_ERROR_warehousingID);
        //
        warehousingCommodity.setWarehousingID(BaseSQLiteBO.INVALID_INT_ID);
        assertEquals(warehousingCommodity.checkDelete(BaseSQLiteBO.INVALID_CASE_ID), WarehousingCommodity.FIELD_ERROR_warehousingID);
        //
        warehousingCommodity.setWarehousingID(1);
        assertEquals(warehousingCommodity.checkDelete(BaseSQLiteBO.INVALID_CASE_ID), "");
    }

    @Test
    public void test_d_checkRetrieveN() {
        WarehousingCommodity warehousingCommodity = new WarehousingCommodity();

        // 检查warehouseID
        assertEquals(warehousingCommodity.checkRetrieveN(BaseSQLiteBO.INVALID_CASE_ID), WarehousingCommodity.FIELD_ERROR_warehousingID);
        //
        warehousingCommodity.setWarehousingID(BaseSQLiteBO.INVALID_INT_ID);
        assertEquals(warehousingCommodity.checkRetrieveN(BaseSQLiteBO.INVALID_CASE_ID), WarehousingCommodity.FIELD_ERROR_warehousingID);
        //
        warehousingCommodity.setWarehousingID(1);
        assertEquals(warehousingCommodity.checkRetrieveN(BaseSQLiteBO.INVALID_CASE_ID), "");
        // 检查pageindex, pagesize
        assertEquals(warehousingCommodity.checkRetrieveN(BaseSQLiteBO.INVALID_CASE_ID), "");
        //
        warehousingCommodity.setPageIndex("0");
        warehousingCommodity.setPageSize("10");
        assertEquals(warehousingCommodity.checkRetrieveN(BaseSQLiteBO.INVALID_CASE_ID), FieldFormat.FIELD_ERROR_Paging);
        //
        warehousingCommodity.setPageIndex("1");
        warehousingCommodity.setPageSize("0");
        assertEquals(warehousingCommodity.checkRetrieveN(BaseSQLiteBO.INVALID_CASE_ID), FieldFormat.FIELD_ERROR_Paging);
    }

}
