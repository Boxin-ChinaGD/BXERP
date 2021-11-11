package com.test.bx.app.model;

import com.bx.erp.bo.BaseSQLiteBO;
import com.bx.erp.model.ConfigGeneral;
import com.bx.erp.utils.FieldFormat;
import com.bx.erp.utils.Shared;
import com.test.bx.app.BaseModelTest;

import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

public class ConfigGeneralTest extends BaseModelTest {

    @BeforeClass
    public void setup() {
        Shared.printTestClassStartInfo();
    }

    @AfterClass
    public void tearDown() {
        Shared.printTestClassEndInfo();
    }

    @Test
    public void test_a_checkUpdate() {
        Shared.printTestClassEndInfo();

        ConfigGeneral cg = new ConfigGeneral();

        // 时间字段型配置行测试
        // Check UsalableCommodityTaskScanEndTime
        cg.setID(Long.valueOf(ConfigGeneral.UsalableCommodityTaskScanEndTime));
        cg.setValue("2");
        Assert.assertEquals(cg.checkUpdate(BaseSQLiteBO.INVALID_CASE_ID), ConfigGeneral.FIELD_ERROR_value1);
        cg.setID(Long.valueOf(ConfigGeneral.UsalableCommodityTaskScanEndTime));
        cg.setValue("23:00");
        Assert.assertEquals(cg.checkUpdate(BaseSQLiteBO.INVALID_CASE_ID), ConfigGeneral.FIELD_ERROR_value1);
        //
        cg.setID(Long.valueOf(ConfigGeneral.UsalableCommodityTaskScanEndTime));
        cg.setValue("13:18:00");
        Assert.assertEquals(cg.checkUpdate(BaseSQLiteBO.INVALID_CASE_ID), "");

        // Check PurchasingTimeoutTaskScanEndTime
        cg.setID(Long.valueOf(ConfigGeneral.PurchasingTimeoutTaskScanEndTime));
        cg.setValue("2");
        Assert.assertEquals(cg.checkUpdate(BaseSQLiteBO.INVALID_CASE_ID), ConfigGeneral.FIELD_ERROR_value1);
        cg.setID(Long.valueOf(ConfigGeneral.PurchasingTimeoutTaskScanEndTime));
        cg.setValue("23:00");
        Assert.assertEquals(cg.checkUpdate(BaseSQLiteBO.INVALID_CASE_ID), ConfigGeneral.FIELD_ERROR_value1);
        //
        cg.setID(Long.valueOf(ConfigGeneral.PurchasingTimeoutTaskScanEndTime));
        cg.setValue("13:18:00");
        Assert.assertEquals(cg.checkUpdate(BaseSQLiteBO.INVALID_CASE_ID), "");

        // Check UsalableCommodityTaskScanStartTime
        cg.setID(Long.valueOf(ConfigGeneral.UsalableCommodityTaskScanStartTime));
        cg.setValue("2");
        Assert.assertEquals(cg.checkUpdate(BaseSQLiteBO.INVALID_CASE_ID), ConfigGeneral.FIELD_ERROR_value1);
        cg.setID(Long.valueOf(ConfigGeneral.UsalableCommodityTaskScanStartTime));
        cg.setValue("23:00");
        Assert.assertEquals(cg.checkUpdate(BaseSQLiteBO.INVALID_CASE_ID), ConfigGeneral.FIELD_ERROR_value1);
        //
        cg.setID(Long.valueOf(ConfigGeneral.UsalableCommodityTaskScanStartTime));
        cg.setValue("13:18:00");
        Assert.assertEquals(cg.checkUpdate(BaseSQLiteBO.INVALID_CASE_ID), "");

        // Check RetailTradeDailyReportSummaryTaskScanStartTime
        cg.setID(Long.valueOf(ConfigGeneral.RetailTradeDailyReportSummaryTaskScanStartTime));
        cg.setValue("2");
        Assert.assertEquals(cg.checkUpdate(BaseSQLiteBO.INVALID_CASE_ID), ConfigGeneral.FIELD_ERROR_value1);
        cg.setID(Long.valueOf(ConfigGeneral.RetailTradeDailyReportSummaryTaskScanStartTime));
        cg.setValue("23:00");
        Assert.assertEquals(cg.checkUpdate(BaseSQLiteBO.INVALID_CASE_ID), ConfigGeneral.FIELD_ERROR_value1);
        //
        cg.setID(Long.valueOf(ConfigGeneral.RetailTradeDailyReportSummaryTaskScanStartTime));
        cg.setValue("13:18:00");
        Assert.assertEquals(cg.checkUpdate(BaseSQLiteBO.INVALID_CASE_ID), "");

        // Check RetailTradeDailyReportSummaryTaskScanEndTime
        cg.setID(Long.valueOf(ConfigGeneral.RetailTradeDailyReportSummaryTaskScanEndTime));
        cg.setValue("2");
        Assert.assertEquals(cg.checkUpdate(BaseSQLiteBO.INVALID_CASE_ID), ConfigGeneral.FIELD_ERROR_value1);
        cg.setID(Long.valueOf(ConfigGeneral.RetailTradeDailyReportSummaryTaskScanEndTime));
        cg.setValue("23:00");
        Assert.assertEquals(cg.checkUpdate(BaseSQLiteBO.INVALID_CASE_ID), ConfigGeneral.FIELD_ERROR_value1);
        //
        cg.setID(Long.valueOf(ConfigGeneral.RetailTradeDailyReportSummaryTaskScanEndTime));
        cg.setValue("13:18:00");
        Assert.assertEquals(cg.checkUpdate(BaseSQLiteBO.INVALID_CASE_ID), "");

        // Check ShelfLifeTaskScanStartTime
        cg.setID(Long.valueOf(ConfigGeneral.ShelfLifeTaskScanStartTime));
        cg.setValue("2");
        Assert.assertEquals(cg.checkUpdate(BaseSQLiteBO.INVALID_CASE_ID), ConfigGeneral.FIELD_ERROR_value1);
        cg.setID(Long.valueOf(ConfigGeneral.ShelfLifeTaskScanStartTime));
        cg.setValue("23:00");
        Assert.assertEquals(cg.checkUpdate(BaseSQLiteBO.INVALID_CASE_ID), ConfigGeneral.FIELD_ERROR_value1);
        //
        cg.setID(Long.valueOf(ConfigGeneral.ShelfLifeTaskScanStartTime));
        cg.setValue("13:18:00");
        Assert.assertEquals(cg.checkUpdate(BaseSQLiteBO.INVALID_CASE_ID), "");

        // Check ShelfLifeTaskScanEndTime
        cg.setID(Long.valueOf(ConfigGeneral.ShelfLifeTaskScanEndTime));
        cg.setValue("2");
        Assert.assertEquals(cg.checkUpdate(BaseSQLiteBO.INVALID_CASE_ID), ConfigGeneral.FIELD_ERROR_value1);
        cg.setID(Long.valueOf(ConfigGeneral.ShelfLifeTaskScanEndTime));
        cg.setValue("23:00");
        Assert.assertEquals(cg.checkUpdate(BaseSQLiteBO.INVALID_CASE_ID), ConfigGeneral.FIELD_ERROR_value1);
        //
        cg.setID(Long.valueOf(ConfigGeneral.ShelfLifeTaskScanEndTime));
        cg.setValue("13:18:00");
        Assert.assertEquals(cg.checkUpdate(BaseSQLiteBO.INVALID_CASE_ID), "");

        // Check RetailTradeMonthlyReportSummaryTaskScanStartTime
        cg.setID(Long.valueOf(ConfigGeneral.RetailTradeMonthlyReportSummaryTaskScanStartTime));
        cg.setValue("2");
        Assert.assertEquals(cg.checkUpdate(BaseSQLiteBO.INVALID_CASE_ID), ConfigGeneral.FIELD_ERROR_value1);
        cg.setID(Long.valueOf(ConfigGeneral.RetailTradeMonthlyReportSummaryTaskScanStartTime));
        cg.setValue("23:00");
        Assert.assertEquals(cg.checkUpdate(BaseSQLiteBO.INVALID_CASE_ID), ConfigGeneral.FIELD_ERROR_value1);
        //
        cg.setID(Long.valueOf(ConfigGeneral.RetailTradeMonthlyReportSummaryTaskScanStartTime));
        cg.setValue("13:18:00");
        Assert.assertEquals(cg.checkUpdate(BaseSQLiteBO.INVALID_CASE_ID), "");

        // Check RetailTradeMonthlyReportSummaryTaskScanEndTime
        cg.setID(Long.valueOf(ConfigGeneral.RetailTradeMonthlyReportSummaryTaskScanEndTime));
        cg.setValue("2");
        Assert.assertEquals(cg.checkUpdate(BaseSQLiteBO.INVALID_CASE_ID), ConfigGeneral.FIELD_ERROR_value1);
        cg.setID(Long.valueOf(ConfigGeneral.RetailTradeMonthlyReportSummaryTaskScanEndTime));
        cg.setValue("23:00");
        Assert.assertEquals(cg.checkUpdate(BaseSQLiteBO.INVALID_CASE_ID), ConfigGeneral.FIELD_ERROR_value1);
        //
        cg.setID(Long.valueOf(ConfigGeneral.RetailTradeMonthlyReportSummaryTaskScanEndTime));
        cg.setValue("13:18:00");
        Assert.assertEquals(cg.checkUpdate(BaseSQLiteBO.INVALID_CASE_ID), "");

        // Check RetailTradeDailyReportByCategoryParentTaskScanStartTime
        cg.setID(Long.valueOf(ConfigGeneral.RetailTradeDailyReportByCategoryParentTaskScanStartTime));
        cg.setValue("2");
        Assert.assertEquals(cg.checkUpdate(BaseSQLiteBO.INVALID_CASE_ID), ConfigGeneral.FIELD_ERROR_value1);
        cg.setID(Long.valueOf(ConfigGeneral.RetailTradeDailyReportByCategoryParentTaskScanStartTime));
        cg.setValue("23:00");
        Assert.assertEquals(cg.checkUpdate(BaseSQLiteBO.INVALID_CASE_ID), ConfigGeneral.FIELD_ERROR_value1);
        //
        cg.setID(Long.valueOf(ConfigGeneral.RetailTradeDailyReportByCategoryParentTaskScanStartTime));
        cg.setValue("13:18:00");
        Assert.assertEquals(cg.checkUpdate(BaseSQLiteBO.INVALID_CASE_ID), "");

        // Check RetailTradeDailyReportByCategoryParentTaskScanEndTime
        cg.setID(Long.valueOf(ConfigGeneral.RetailTradeDailyReportByCategoryParentTaskScanEndTime));
        cg.setValue("2");
        Assert.assertEquals(cg.checkUpdate(BaseSQLiteBO.INVALID_CASE_ID), ConfigGeneral.FIELD_ERROR_value1);
        cg.setID(Long.valueOf(ConfigGeneral.RetailTradeDailyReportByCategoryParentTaskScanEndTime));
        cg.setValue("23:00");
        Assert.assertEquals(cg.checkUpdate(BaseSQLiteBO.INVALID_CASE_ID), ConfigGeneral.FIELD_ERROR_value1);
        //
        cg.setID(Long.valueOf(ConfigGeneral.RetailTradeDailyReportByCategoryParentTaskScanEndTime));
        cg.setValue("13:18:00");
        Assert.assertEquals(cg.checkUpdate(BaseSQLiteBO.INVALID_CASE_ID), "");

        // Check RetailTradeDailyReportByStaffTaskScanStartTime
        cg.setID(Long.valueOf(ConfigGeneral.RetailTradeDailyReportByStaffTaskScanStartTime));
        cg.setValue("2");
        Assert.assertEquals(cg.checkUpdate(BaseSQLiteBO.INVALID_CASE_ID), ConfigGeneral.FIELD_ERROR_value1);
        cg.setID(Long.valueOf(ConfigGeneral.RetailTradeDailyReportByStaffTaskScanStartTime));
        cg.setValue("23:00");
        Assert.assertEquals(cg.checkUpdate(BaseSQLiteBO.INVALID_CASE_ID), ConfigGeneral.FIELD_ERROR_value1);
        //
        cg.setID(Long.valueOf(ConfigGeneral.RetailTradeDailyReportByStaffTaskScanStartTime));
        cg.setValue("13:18:00");
        Assert.assertEquals(cg.checkUpdate(BaseSQLiteBO.INVALID_CASE_ID), "");

        // Check RetailTradeDailyReportByStaffTaskScanEndTime
        cg.setID(Long.valueOf(ConfigGeneral.RetailTradeDailyReportByStaffTaskScanEndTime));
        cg.setValue("2");
        Assert.assertEquals(cg.checkUpdate(BaseSQLiteBO.INVALID_CASE_ID), ConfigGeneral.FIELD_ERROR_value1);
        cg.setID(Long.valueOf(ConfigGeneral.RetailTradeDailyReportByStaffTaskScanEndTime));
        cg.setValue("23:00");
        Assert.assertEquals(cg.checkUpdate(BaseSQLiteBO.INVALID_CASE_ID), ConfigGeneral.FIELD_ERROR_value1);
        //
        cg.setID(Long.valueOf(ConfigGeneral.RetailTradeDailyReportByStaffTaskScanEndTime));
        cg.setValue("13:18:00");
        Assert.assertEquals(cg.checkUpdate(BaseSQLiteBO.INVALID_CASE_ID), "");

        // 正整数格式型配置行测试
        // check PAGESIZE
        cg.setID(Long.valueOf(ConfigGeneral.PAGESIZE));
        cg.setValue("博昕");
        Assert.assertEquals(cg.checkUpdate(BaseSQLiteBO.INVALID_CASE_ID), ConfigGeneral.FIELD_ERROR_value2);
        cg.setID(Long.valueOf(ConfigGeneral.PAGESIZE));
        cg.setValue("0");
        Assert.assertEquals(cg.checkUpdate(BaseSQLiteBO.INVALID_CASE_ID), ConfigGeneral.FIELD_ERROR_value2);
        cg.setID(Long.valueOf(ConfigGeneral.PAGESIZE));
        cg.setValue("-99");
        Assert.assertEquals(cg.checkUpdate(BaseSQLiteBO.INVALID_CASE_ID), ConfigGeneral.FIELD_ERROR_value2);
        //
        cg.setID(Long.valueOf(ConfigGeneral.PAGESIZE));
        cg.setValue("1");
        Assert.assertEquals(cg.checkUpdate(BaseSQLiteBO.INVALID_CASE_ID), "");

        // check ACTIVE_SMALLSHEET_ID
        cg.setID(Long.valueOf(ConfigGeneral.ACTIVE_SMALLSHEET_ID));
        cg.setValue("博昕");
        Assert.assertEquals(cg.checkUpdate(BaseSQLiteBO.INVALID_CASE_ID), ConfigGeneral.FIELD_ERROR_value2);
        cg.setID(Long.valueOf(ConfigGeneral.ACTIVE_SMALLSHEET_ID));
        cg.setValue("0");
        Assert.assertEquals(cg.checkUpdate(BaseSQLiteBO.INVALID_CASE_ID), ConfigGeneral.FIELD_ERROR_value2);
        cg.setID(Long.valueOf(ConfigGeneral.ACTIVE_SMALLSHEET_ID));
        cg.setValue("-99");
        Assert.assertEquals(cg.checkUpdate(BaseSQLiteBO.INVALID_CASE_ID), ConfigGeneral.FIELD_ERROR_value2);
        //
        cg.setID(Long.valueOf(ConfigGeneral.ACTIVE_SMALLSHEET_ID));
        cg.setValue("1");
        Assert.assertEquals(cg.checkUpdate(BaseSQLiteBO.INVALID_CASE_ID), "");

        // check PurchasingTimeoutTaskFlag
        cg.setID(Long.valueOf(ConfigGeneral.PurchasingTimeoutTaskFlag));
        cg.setValue("博昕");
        Assert.assertEquals(cg.checkUpdate(BaseSQLiteBO.INVALID_CASE_ID), ConfigGeneral.FIELD_ERROR_value2);
        cg.setID(Long.valueOf(ConfigGeneral.PurchasingTimeoutTaskFlag));
        cg.setValue("0");
        Assert.assertEquals(cg.checkUpdate(BaseSQLiteBO.INVALID_CASE_ID), ConfigGeneral.FIELD_ERROR_value2);
        cg.setID(Long.valueOf(ConfigGeneral.PurchasingTimeoutTaskFlag));
        cg.setValue("-99");
        Assert.assertEquals(cg.checkUpdate(BaseSQLiteBO.INVALID_CASE_ID), ConfigGeneral.FIELD_ERROR_value2);
        //
        cg.setID(Long.valueOf(ConfigGeneral.PurchasingTimeoutTaskFlag));
        cg.setValue("1");
        Assert.assertEquals(cg.checkUpdate(BaseSQLiteBO.INVALID_CASE_ID), "");


        // check UsalableCommodityTaskFlag
        cg.setID(Long.valueOf(ConfigGeneral.UsalableCommodityTaskFlag));
        cg.setValue("博昕");
        Assert.assertEquals(cg.checkUpdate(BaseSQLiteBO.INVALID_CASE_ID), ConfigGeneral.FIELD_ERROR_value2);
        cg.setID(Long.valueOf(ConfigGeneral.UsalableCommodityTaskFlag));
        cg.setValue("0");
        Assert.assertEquals(cg.checkUpdate(BaseSQLiteBO.INVALID_CASE_ID), ConfigGeneral.FIELD_ERROR_value2);
        cg.setID(Long.valueOf(ConfigGeneral.UsalableCommodityTaskFlag));
        cg.setValue("-99");
        Assert.assertEquals(cg.checkUpdate(BaseSQLiteBO.INVALID_CASE_ID), ConfigGeneral.FIELD_ERROR_value2);
        //
        cg.setID(Long.valueOf(ConfigGeneral.UsalableCommodityTaskFlag));
        cg.setValue("1");
        Assert.assertEquals(cg.checkUpdate(BaseSQLiteBO.INVALID_CASE_ID), "");

        // check ShelfLifeTaskFlag
        cg.setID(Long.valueOf(ConfigGeneral.ShelfLifeTaskFlag));
        cg.setValue("博昕");
        Assert.assertEquals(cg.checkUpdate(BaseSQLiteBO.INVALID_CASE_ID), ConfigGeneral.FIELD_ERROR_value2);
        cg.setID(Long.valueOf(ConfigGeneral.ShelfLifeTaskFlag));
        cg.setValue("0");
        Assert.assertEquals(cg.checkUpdate(BaseSQLiteBO.INVALID_CASE_ID), ConfigGeneral.FIELD_ERROR_value2);
        cg.setID(Long.valueOf(ConfigGeneral.ShelfLifeTaskFlag));
        cg.setValue("-99");
        Assert.assertEquals(cg.checkUpdate(BaseSQLiteBO.INVALID_CASE_ID), ConfigGeneral.FIELD_ERROR_value2);
        //
        cg.setID(Long.valueOf(ConfigGeneral.ShelfLifeTaskFlag));
        cg.setValue("1");
        Assert.assertEquals(cg.checkUpdate(BaseSQLiteBO.INVALID_CASE_ID), "");

        // check MaxVicePackageUnit
        cg.setID(Long.valueOf(ConfigGeneral.MaxVicePackageUnit));
        cg.setValue("博昕");
        Assert.assertEquals(cg.checkUpdate(BaseSQLiteBO.INVALID_CASE_ID), ConfigGeneral.FIELD_ERROR_value2);
        cg.setID(Long.valueOf(ConfigGeneral.MaxVicePackageUnit));
        cg.setValue("0");
        Assert.assertEquals(cg.checkUpdate(BaseSQLiteBO.INVALID_CASE_ID), ConfigGeneral.FIELD_ERROR_value2);
        cg.setID(Long.valueOf(ConfigGeneral.MaxVicePackageUnit));
        cg.setValue("-99");
        Assert.assertEquals(cg.checkUpdate(BaseSQLiteBO.INVALID_CASE_ID), ConfigGeneral.FIELD_ERROR_value2);
        //
        cg.setID(Long.valueOf(ConfigGeneral.MaxVicePackageUnit));
        cg.setValue("1");
        Assert.assertEquals(cg.checkUpdate(BaseSQLiteBO.INVALID_CASE_ID), "");

        // check MaxProviderNOPerCommodity
        cg.setID(Long.valueOf(ConfigGeneral.MaxProviderNOPerCommodity));
        cg.setValue("博昕");
        Assert.assertEquals(cg.checkUpdate(BaseSQLiteBO.INVALID_CASE_ID), ConfigGeneral.FIELD_ERROR_value2);
        cg.setID(Long.valueOf(ConfigGeneral.MaxProviderNOPerCommodity));
        cg.setValue("0");
        Assert.assertEquals(cg.checkUpdate(BaseSQLiteBO.INVALID_CASE_ID), ConfigGeneral.FIELD_ERROR_value2);
        cg.setID(Long.valueOf(ConfigGeneral.MaxProviderNOPerCommodity));
        cg.setValue("-99");
        Assert.assertEquals(cg.checkUpdate(BaseSQLiteBO.INVALID_CASE_ID), ConfigGeneral.FIELD_ERROR_value2);
        //
        cg.setID(Long.valueOf(ConfigGeneral.MaxProviderNOPerCommodity));
        cg.setValue("1");
        Assert.assertEquals(cg.checkUpdate(BaseSQLiteBO.INVALID_CASE_ID), "");

        // check MaxPurchasingOrderCommodityNO
        cg.setID(Long.valueOf(ConfigGeneral.MaxPurchasingOrderCommodityNO));
        cg.setValue("博昕");
        Assert.assertEquals(cg.checkUpdate(BaseSQLiteBO.INVALID_CASE_ID), ConfigGeneral.FIELD_ERROR_value2);
        cg.setID(Long.valueOf(ConfigGeneral.MaxPurchasingOrderCommodityNO));
        cg.setValue("0");
        Assert.assertEquals(cg.checkUpdate(BaseSQLiteBO.INVALID_CASE_ID), ConfigGeneral.FIELD_ERROR_value2);
        cg.setID(Long.valueOf(ConfigGeneral.MaxPurchasingOrderCommodityNO));
        cg.setValue("-99");
        Assert.assertEquals(cg.checkUpdate(BaseSQLiteBO.INVALID_CASE_ID), ConfigGeneral.FIELD_ERROR_value2);
        //
        cg.setID(Long.valueOf(ConfigGeneral.MaxPurchasingOrderCommodityNO));
        cg.setValue("1");
        Assert.assertEquals(cg.checkUpdate(BaseSQLiteBO.INVALID_CASE_ID), "");

        // check CommodityLogoVolumeMax
        cg.setID(Long.valueOf(ConfigGeneral.CommodityLogoVolumeMax));
        cg.setValue("博昕");
        Assert.assertEquals(cg.checkUpdate(BaseSQLiteBO.INVALID_CASE_ID), ConfigGeneral.FIELD_ERROR_value2);
        cg.setID(Long.valueOf(ConfigGeneral.CommodityLogoVolumeMax));
        cg.setValue("0");
        Assert.assertEquals(cg.checkUpdate(BaseSQLiteBO.INVALID_CASE_ID), ConfigGeneral.FIELD_ERROR_value2);
        cg.setID(Long.valueOf(ConfigGeneral.CommodityLogoVolumeMax));
        cg.setValue("-99");
        Assert.assertEquals(cg.checkUpdate(BaseSQLiteBO.INVALID_CASE_ID), ConfigGeneral.FIELD_ERROR_value2);
        //
        cg.setID(Long.valueOf(ConfigGeneral.CommodityLogoVolumeMax));
        cg.setValue("1");
        Assert.assertEquals(cg.checkUpdate(BaseSQLiteBO.INVALID_CASE_ID), "");

        // check MaxWarehousingCommodityNO
        cg.setID(Long.valueOf(ConfigGeneral.MaxWarehousingCommodityNO));
        cg.setValue("博昕");
        Assert.assertEquals(cg.checkUpdate(BaseSQLiteBO.INVALID_CASE_ID), ConfigGeneral.FIELD_ERROR_value2);
        cg.setID(Long.valueOf(ConfigGeneral.MaxWarehousingCommodityNO));
        cg.setValue("0");
        Assert.assertEquals(cg.checkUpdate(BaseSQLiteBO.INVALID_CASE_ID), ConfigGeneral.FIELD_ERROR_value2);
        cg.setID(Long.valueOf(ConfigGeneral.MaxWarehousingCommodityNO));
        cg.setValue("-99");
        Assert.assertEquals(cg.checkUpdate(BaseSQLiteBO.INVALID_CASE_ID), ConfigGeneral.FIELD_ERROR_value2);
        //
        cg.setID(Long.valueOf(ConfigGeneral.MaxWarehousingCommodityNO));
        cg.setValue("1");
        Assert.assertEquals(cg.checkUpdate(BaseSQLiteBO.INVALID_CASE_ID), "");
    }

    @Test
    public void test_b_checkRetrieve1() {
        Shared.printTestMethodStartInfo();
        //
        ConfigGeneral cg = new ConfigGeneral();
        cg.setID(-99l);
        assertEquals(cg.checkRetrieve1(BaseSQLiteBO.INVALID_CASE_ID), FieldFormat.FIELD_ERROR_ID);
        cg.setID(0l);
        assertEquals(cg.checkRetrieve1(BaseSQLiteBO.INVALID_CASE_ID), FieldFormat.FIELD_ERROR_ID);
        //
        cg.setID(1l);
        assertEquals(cg.checkRetrieve1(BaseSQLiteBO.INVALID_CASE_ID), "");
    }
    
}
