package wpos.model;


import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import wpos.BaseModelTest;
import wpos.bo.BaseSQLiteBO;
import wpos.utils.FieldFormat;
import wpos.utils.Shared;

import static org.testng.AssertJUnit.assertEquals;

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
        cg.setID((ConfigGeneral.UsalableCommodityTaskScanEndTime));
        cg.setValue("2");
        Assert.assertEquals(cg.checkUpdate(BaseSQLiteBO.INVALID_CASE_ID), ConfigGeneral.FIELD_ERROR_value1);
        cg.setID((ConfigGeneral.UsalableCommodityTaskScanEndTime));
        cg.setValue("23:00");
        Assert.assertEquals(cg.checkUpdate(BaseSQLiteBO.INVALID_CASE_ID), ConfigGeneral.FIELD_ERROR_value1);
        //
        cg.setID((ConfigGeneral.UsalableCommodityTaskScanEndTime));
        cg.setValue("13:18:00");
        Assert.assertEquals(cg.checkUpdate(BaseSQLiteBO.INVALID_CASE_ID), "");

        // Check PurchasingTimeoutTaskScanEndTime
        cg.setID((ConfigGeneral.PurchasingTimeoutTaskScanEndTime));
        cg.setValue("2");
        Assert.assertEquals(cg.checkUpdate(BaseSQLiteBO.INVALID_CASE_ID), ConfigGeneral.FIELD_ERROR_value1);
        cg.setID((ConfigGeneral.PurchasingTimeoutTaskScanEndTime));
        cg.setValue("23:00");
        Assert.assertEquals(cg.checkUpdate(BaseSQLiteBO.INVALID_CASE_ID), ConfigGeneral.FIELD_ERROR_value1);
        //
        cg.setID((ConfigGeneral.PurchasingTimeoutTaskScanEndTime));
        cg.setValue("13:18:00");
        Assert.assertEquals(cg.checkUpdate(BaseSQLiteBO.INVALID_CASE_ID), "");

        // Check UsalableCommodityTaskScanStartTime
        cg.setID((ConfigGeneral.UsalableCommodityTaskScanStartTime));
        cg.setValue("2");
        Assert.assertEquals(cg.checkUpdate(BaseSQLiteBO.INVALID_CASE_ID), ConfigGeneral.FIELD_ERROR_value1);
        cg.setID((ConfigGeneral.UsalableCommodityTaskScanStartTime));
        cg.setValue("23:00");
        Assert.assertEquals(cg.checkUpdate(BaseSQLiteBO.INVALID_CASE_ID), ConfigGeneral.FIELD_ERROR_value1);
        //
        cg.setID((ConfigGeneral.UsalableCommodityTaskScanStartTime));
        cg.setValue("13:18:00");
        Assert.assertEquals(cg.checkUpdate(BaseSQLiteBO.INVALID_CASE_ID), "");

        // Check RetailTradeDailyReportSummaryTaskScanStartTime
        cg.setID((ConfigGeneral.RetailTradeDailyReportSummaryTaskScanStartTime));
        cg.setValue("2");
        Assert.assertEquals(cg.checkUpdate(BaseSQLiteBO.INVALID_CASE_ID), ConfigGeneral.FIELD_ERROR_value1);
        cg.setID((ConfigGeneral.RetailTradeDailyReportSummaryTaskScanStartTime));
        cg.setValue("23:00");
        Assert.assertEquals(cg.checkUpdate(BaseSQLiteBO.INVALID_CASE_ID), ConfigGeneral.FIELD_ERROR_value1);
        //
        cg.setID((ConfigGeneral.RetailTradeDailyReportSummaryTaskScanStartTime));
        cg.setValue("13:18:00");
        Assert.assertEquals(cg.checkUpdate(BaseSQLiteBO.INVALID_CASE_ID), "");

        // Check RetailTradeDailyReportSummaryTaskScanEndTime
        cg.setID((ConfigGeneral.RetailTradeDailyReportSummaryTaskScanEndTime));
        cg.setValue("2");
        Assert.assertEquals(cg.checkUpdate(BaseSQLiteBO.INVALID_CASE_ID), ConfigGeneral.FIELD_ERROR_value1);
        cg.setID((ConfigGeneral.RetailTradeDailyReportSummaryTaskScanEndTime));
        cg.setValue("23:00");
        Assert.assertEquals(cg.checkUpdate(BaseSQLiteBO.INVALID_CASE_ID), ConfigGeneral.FIELD_ERROR_value1);
        //
        cg.setID((ConfigGeneral.RetailTradeDailyReportSummaryTaskScanEndTime));
        cg.setValue("13:18:00");
        Assert.assertEquals(cg.checkUpdate(BaseSQLiteBO.INVALID_CASE_ID), "");

        // Check ShelfLifeTaskScanStartTime
        cg.setID((ConfigGeneral.ShelfLifeTaskScanStartTime));
        cg.setValue("2");
        Assert.assertEquals(cg.checkUpdate(BaseSQLiteBO.INVALID_CASE_ID), ConfigGeneral.FIELD_ERROR_value1);
        cg.setID((ConfigGeneral.ShelfLifeTaskScanStartTime));
        cg.setValue("23:00");
        Assert.assertEquals(cg.checkUpdate(BaseSQLiteBO.INVALID_CASE_ID), ConfigGeneral.FIELD_ERROR_value1);
        //
        cg.setID((ConfigGeneral.ShelfLifeTaskScanStartTime));
        cg.setValue("13:18:00");
        Assert.assertEquals(cg.checkUpdate(BaseSQLiteBO.INVALID_CASE_ID), "");

        // Check ShelfLifeTaskScanEndTime
        cg.setID((ConfigGeneral.ShelfLifeTaskScanEndTime));
        cg.setValue("2");
        Assert.assertEquals(cg.checkUpdate(BaseSQLiteBO.INVALID_CASE_ID), ConfigGeneral.FIELD_ERROR_value1);
        cg.setID((ConfigGeneral.ShelfLifeTaskScanEndTime));
        cg.setValue("23:00");
        Assert.assertEquals(cg.checkUpdate(BaseSQLiteBO.INVALID_CASE_ID), ConfigGeneral.FIELD_ERROR_value1);
        //
        cg.setID((ConfigGeneral.ShelfLifeTaskScanEndTime));
        cg.setValue("13:18:00");
        Assert.assertEquals(cg.checkUpdate(BaseSQLiteBO.INVALID_CASE_ID), "");

        // Check RetailTradeMonthlyReportSummaryTaskScanStartTime
        cg.setID((ConfigGeneral.RetailTradeMonthlyReportSummaryTaskScanStartTime));
        cg.setValue("2");
        Assert.assertEquals(cg.checkUpdate(BaseSQLiteBO.INVALID_CASE_ID), ConfigGeneral.FIELD_ERROR_value1);
        cg.setID((ConfigGeneral.RetailTradeMonthlyReportSummaryTaskScanStartTime));
        cg.setValue("23:00");
        Assert.assertEquals(cg.checkUpdate(BaseSQLiteBO.INVALID_CASE_ID), ConfigGeneral.FIELD_ERROR_value1);
        //
        cg.setID((ConfigGeneral.RetailTradeMonthlyReportSummaryTaskScanStartTime));
        cg.setValue("13:18:00");
        Assert.assertEquals(cg.checkUpdate(BaseSQLiteBO.INVALID_CASE_ID), "");

        // Check RetailTradeMonthlyReportSummaryTaskScanEndTime
        cg.setID((ConfigGeneral.RetailTradeMonthlyReportSummaryTaskScanEndTime));
        cg.setValue("2");
        Assert.assertEquals(cg.checkUpdate(BaseSQLiteBO.INVALID_CASE_ID), ConfigGeneral.FIELD_ERROR_value1);
        cg.setID((ConfigGeneral.RetailTradeMonthlyReportSummaryTaskScanEndTime));
        cg.setValue("23:00");
        Assert.assertEquals(cg.checkUpdate(BaseSQLiteBO.INVALID_CASE_ID), ConfigGeneral.FIELD_ERROR_value1);
        //
        cg.setID((ConfigGeneral.RetailTradeMonthlyReportSummaryTaskScanEndTime));
        cg.setValue("13:18:00");
        Assert.assertEquals(cg.checkUpdate(BaseSQLiteBO.INVALID_CASE_ID), "");

        // Check RetailTradeDailyReportByCategoryParentTaskScanStartTime
        cg.setID((ConfigGeneral.RetailTradeDailyReportByCategoryParentTaskScanStartTime));
        cg.setValue("2");
        Assert.assertEquals(cg.checkUpdate(BaseSQLiteBO.INVALID_CASE_ID), ConfigGeneral.FIELD_ERROR_value1);
        cg.setID((ConfigGeneral.RetailTradeDailyReportByCategoryParentTaskScanStartTime));
        cg.setValue("23:00");
        Assert.assertEquals(cg.checkUpdate(BaseSQLiteBO.INVALID_CASE_ID), ConfigGeneral.FIELD_ERROR_value1);
        //
        cg.setID((ConfigGeneral.RetailTradeDailyReportByCategoryParentTaskScanStartTime));
        cg.setValue("13:18:00");
        Assert.assertEquals(cg.checkUpdate(BaseSQLiteBO.INVALID_CASE_ID), "");

        // Check RetailTradeDailyReportByCategoryParentTaskScanEndTime
        cg.setID((ConfigGeneral.RetailTradeDailyReportByCategoryParentTaskScanEndTime));
        cg.setValue("2");
        Assert.assertEquals(cg.checkUpdate(BaseSQLiteBO.INVALID_CASE_ID), ConfigGeneral.FIELD_ERROR_value1);
        cg.setID((ConfigGeneral.RetailTradeDailyReportByCategoryParentTaskScanEndTime));
        cg.setValue("23:00");
        Assert.assertEquals(cg.checkUpdate(BaseSQLiteBO.INVALID_CASE_ID), ConfigGeneral.FIELD_ERROR_value1);
        //
        cg.setID((ConfigGeneral.RetailTradeDailyReportByCategoryParentTaskScanEndTime));
        cg.setValue("13:18:00");
        Assert.assertEquals(cg.checkUpdate(BaseSQLiteBO.INVALID_CASE_ID), "");

        // Check RetailTradeDailyReportByStaffTaskScanStartTime
        cg.setID((ConfigGeneral.RetailTradeDailyReportByStaffTaskScanStartTime));
        cg.setValue("2");
        Assert.assertEquals(cg.checkUpdate(BaseSQLiteBO.INVALID_CASE_ID), ConfigGeneral.FIELD_ERROR_value1);
        cg.setID((ConfigGeneral.RetailTradeDailyReportByStaffTaskScanStartTime));
        cg.setValue("23:00");
        Assert.assertEquals(cg.checkUpdate(BaseSQLiteBO.INVALID_CASE_ID), ConfigGeneral.FIELD_ERROR_value1);
        //
        cg.setID((ConfigGeneral.RetailTradeDailyReportByStaffTaskScanStartTime));
        cg.setValue("13:18:00");
        Assert.assertEquals(cg.checkUpdate(BaseSQLiteBO.INVALID_CASE_ID), "");

        // Check RetailTradeDailyReportByStaffTaskScanEndTime
        cg.setID((ConfigGeneral.RetailTradeDailyReportByStaffTaskScanEndTime));
        cg.setValue("2");
        Assert.assertEquals(cg.checkUpdate(BaseSQLiteBO.INVALID_CASE_ID), ConfigGeneral.FIELD_ERROR_value1);
        cg.setID((ConfigGeneral.RetailTradeDailyReportByStaffTaskScanEndTime));
        cg.setValue("23:00");
        Assert.assertEquals(cg.checkUpdate(BaseSQLiteBO.INVALID_CASE_ID), ConfigGeneral.FIELD_ERROR_value1);
        //
        cg.setID((ConfigGeneral.RetailTradeDailyReportByStaffTaskScanEndTime));
        cg.setValue("13:18:00");
        Assert.assertEquals(cg.checkUpdate(BaseSQLiteBO.INVALID_CASE_ID), "");

        // 正整数格式型配置行测试
        // check PAGESIZE
        cg.setID((ConfigGeneral.PAGESIZE));
        cg.setValue("博昕");
        Assert.assertEquals(cg.checkUpdate(BaseSQLiteBO.INVALID_CASE_ID), ConfigGeneral.FIELD_ERROR_value2);
        cg.setID((ConfigGeneral.PAGESIZE));
        cg.setValue("0");
        Assert.assertEquals(cg.checkUpdate(BaseSQLiteBO.INVALID_CASE_ID), ConfigGeneral.FIELD_ERROR_value2);
        cg.setID((ConfigGeneral.PAGESIZE));
        cg.setValue("-99");
        Assert.assertEquals(cg.checkUpdate(BaseSQLiteBO.INVALID_CASE_ID), ConfigGeneral.FIELD_ERROR_value2);
        //
        cg.setID((ConfigGeneral.PAGESIZE));
        cg.setValue("1");
        Assert.assertEquals(cg.checkUpdate(BaseSQLiteBO.INVALID_CASE_ID), "");

        // check ACTIVE_SMALLSHEET_ID
        cg.setID((ConfigGeneral.ACTIVE_SMALLSHEET_ID));
        cg.setValue("博昕");
        Assert.assertEquals(cg.checkUpdate(BaseSQLiteBO.INVALID_CASE_ID), ConfigGeneral.FIELD_ERROR_value2);
        cg.setID((ConfigGeneral.ACTIVE_SMALLSHEET_ID));
        cg.setValue("0");
        Assert.assertEquals(cg.checkUpdate(BaseSQLiteBO.INVALID_CASE_ID), ConfigGeneral.FIELD_ERROR_value2);
        cg.setID((ConfigGeneral.ACTIVE_SMALLSHEET_ID));
        cg.setValue("-99");
        Assert.assertEquals(cg.checkUpdate(BaseSQLiteBO.INVALID_CASE_ID), ConfigGeneral.FIELD_ERROR_value2);
        //
        cg.setID((ConfigGeneral.ACTIVE_SMALLSHEET_ID));
        cg.setValue("1");
        Assert.assertEquals(cg.checkUpdate(BaseSQLiteBO.INVALID_CASE_ID), "");

        // check PurchasingTimeoutTaskFlag
        cg.setID((ConfigGeneral.PurchasingTimeoutTaskFlag));
        cg.setValue("博昕");
        Assert.assertEquals(cg.checkUpdate(BaseSQLiteBO.INVALID_CASE_ID), ConfigGeneral.FIELD_ERROR_value2);
        cg.setID((ConfigGeneral.PurchasingTimeoutTaskFlag));
        cg.setValue("0");
        Assert.assertEquals(cg.checkUpdate(BaseSQLiteBO.INVALID_CASE_ID), ConfigGeneral.FIELD_ERROR_value2);
        cg.setID((ConfigGeneral.PurchasingTimeoutTaskFlag));
        cg.setValue("-99");
        Assert.assertEquals(cg.checkUpdate(BaseSQLiteBO.INVALID_CASE_ID), ConfigGeneral.FIELD_ERROR_value2);
        //
        cg.setID((ConfigGeneral.PurchasingTimeoutTaskFlag));
        cg.setValue("1");
        Assert.assertEquals(cg.checkUpdate(BaseSQLiteBO.INVALID_CASE_ID), "");


        // check UsalableCommodityTaskFlag
        cg.setID((ConfigGeneral.UsalableCommodityTaskFlag));
        cg.setValue("博昕");
        Assert.assertEquals(cg.checkUpdate(BaseSQLiteBO.INVALID_CASE_ID), ConfigGeneral.FIELD_ERROR_value2);
        cg.setID((ConfigGeneral.UsalableCommodityTaskFlag));
        cg.setValue("0");
        Assert.assertEquals(cg.checkUpdate(BaseSQLiteBO.INVALID_CASE_ID), ConfigGeneral.FIELD_ERROR_value2);
        cg.setID((ConfigGeneral.UsalableCommodityTaskFlag));
        cg.setValue("-99");
        Assert.assertEquals(cg.checkUpdate(BaseSQLiteBO.INVALID_CASE_ID), ConfigGeneral.FIELD_ERROR_value2);
        //
        cg.setID((ConfigGeneral.UsalableCommodityTaskFlag));
        cg.setValue("1");
        Assert.assertEquals(cg.checkUpdate(BaseSQLiteBO.INVALID_CASE_ID), "");

        // check ShelfLifeTaskFlag
        cg.setID((ConfigGeneral.ShelfLifeTaskFlag));
        cg.setValue("博昕");
        Assert.assertEquals(cg.checkUpdate(BaseSQLiteBO.INVALID_CASE_ID), ConfigGeneral.FIELD_ERROR_value2);
        cg.setID((ConfigGeneral.ShelfLifeTaskFlag));
        cg.setValue("0");
        Assert.assertEquals(cg.checkUpdate(BaseSQLiteBO.INVALID_CASE_ID), ConfigGeneral.FIELD_ERROR_value2);
        cg.setID((ConfigGeneral.ShelfLifeTaskFlag));
        cg.setValue("-99");
        Assert.assertEquals(cg.checkUpdate(BaseSQLiteBO.INVALID_CASE_ID), ConfigGeneral.FIELD_ERROR_value2);
        //
        cg.setID((ConfigGeneral.ShelfLifeTaskFlag));
        cg.setValue("1");
        Assert.assertEquals(cg.checkUpdate(BaseSQLiteBO.INVALID_CASE_ID), "");

        // check MaxVicePackageUnit
        cg.setID((ConfigGeneral.MaxVicePackageUnit));
        cg.setValue("博昕");
        Assert.assertEquals(cg.checkUpdate(BaseSQLiteBO.INVALID_CASE_ID), ConfigGeneral.FIELD_ERROR_value2);
        cg.setID((ConfigGeneral.MaxVicePackageUnit));
        cg.setValue("0");
        Assert.assertEquals(cg.checkUpdate(BaseSQLiteBO.INVALID_CASE_ID), ConfigGeneral.FIELD_ERROR_value2);
        cg.setID((ConfigGeneral.MaxVicePackageUnit));
        cg.setValue("-99");
        Assert.assertEquals(cg.checkUpdate(BaseSQLiteBO.INVALID_CASE_ID), ConfigGeneral.FIELD_ERROR_value2);
        //
        cg.setID((ConfigGeneral.MaxVicePackageUnit));
        cg.setValue("1");
        Assert.assertEquals(cg.checkUpdate(BaseSQLiteBO.INVALID_CASE_ID), "");

        // check MaxProviderNOPerCommodity
        cg.setID( ConfigGeneral.MaxProviderNOPerCommodity);
        cg.setValue("博昕");
        Assert.assertEquals(cg.checkUpdate(BaseSQLiteBO.INVALID_CASE_ID), ConfigGeneral.FIELD_ERROR_value2);
        cg.setID( ConfigGeneral.MaxProviderNOPerCommodity);
        cg.setValue("0");
        Assert.assertEquals(cg.checkUpdate(BaseSQLiteBO.INVALID_CASE_ID), ConfigGeneral.FIELD_ERROR_value2);
        cg.setID( ConfigGeneral.MaxProviderNOPerCommodity);
        cg.setValue("-99");
        Assert.assertEquals(cg.checkUpdate(BaseSQLiteBO.INVALID_CASE_ID), ConfigGeneral.FIELD_ERROR_value2);
        //
        cg.setID( ConfigGeneral.MaxProviderNOPerCommodity);
        cg.setValue("1");
        Assert.assertEquals(cg.checkUpdate(BaseSQLiteBO.INVALID_CASE_ID), "");

        // check MaxPurchasingOrderCommodityNO
        cg.setID( ConfigGeneral.MaxPurchasingOrderCommodityNO);
        cg.setValue("博昕");
        Assert.assertEquals(cg.checkUpdate(BaseSQLiteBO.INVALID_CASE_ID), ConfigGeneral.FIELD_ERROR_value2);
        cg.setID( ConfigGeneral.MaxPurchasingOrderCommodityNO);
        cg.setValue("0");
        Assert.assertEquals(cg.checkUpdate(BaseSQLiteBO.INVALID_CASE_ID), ConfigGeneral.FIELD_ERROR_value2);
        cg.setID( ConfigGeneral.MaxPurchasingOrderCommodityNO);
        cg.setValue("-99");
        Assert.assertEquals(cg.checkUpdate(BaseSQLiteBO.INVALID_CASE_ID), ConfigGeneral.FIELD_ERROR_value2);
        //
        cg.setID( ConfigGeneral.MaxPurchasingOrderCommodityNO);
        cg.setValue("1");
        Assert.assertEquals(cg.checkUpdate(BaseSQLiteBO.INVALID_CASE_ID), "");

        // check CommodityLogoVolumeMax
        cg.setID( ConfigGeneral.CommodityLogoVolumeMax);
        cg.setValue("博昕");
        Assert.assertEquals(cg.checkUpdate(BaseSQLiteBO.INVALID_CASE_ID), ConfigGeneral.FIELD_ERROR_value2);
        cg.setID( ConfigGeneral.CommodityLogoVolumeMax);
        cg.setValue("0");
        Assert.assertEquals(cg.checkUpdate(BaseSQLiteBO.INVALID_CASE_ID), ConfigGeneral.FIELD_ERROR_value2);
        cg.setID( ConfigGeneral.CommodityLogoVolumeMax);
        cg.setValue("-99");
        Assert.assertEquals(cg.checkUpdate(BaseSQLiteBO.INVALID_CASE_ID), ConfigGeneral.FIELD_ERROR_value2);
        //
        cg.setID( ConfigGeneral.CommodityLogoVolumeMax);
        cg.setValue("1");
        Assert.assertEquals(cg.checkUpdate(BaseSQLiteBO.INVALID_CASE_ID), "");

        // check MaxWarehousingCommodityNO
        cg.setID( ConfigGeneral.MaxWarehousingCommodityNO);
        cg.setValue("博昕");
        Assert.assertEquals(cg.checkUpdate(BaseSQLiteBO.INVALID_CASE_ID), ConfigGeneral.FIELD_ERROR_value2);
        cg.setID( ConfigGeneral.MaxWarehousingCommodityNO);
        cg.setValue("0");
        Assert.assertEquals(cg.checkUpdate(BaseSQLiteBO.INVALID_CASE_ID), ConfigGeneral.FIELD_ERROR_value2);
        cg.setID( ConfigGeneral.MaxWarehousingCommodityNO);
        cg.setValue("-99");
        Assert.assertEquals(cg.checkUpdate(BaseSQLiteBO.INVALID_CASE_ID), ConfigGeneral.FIELD_ERROR_value2);
        //
        cg.setID( ConfigGeneral.MaxWarehousingCommodityNO);
        cg.setValue("1");
        Assert.assertEquals(cg.checkUpdate(BaseSQLiteBO.INVALID_CASE_ID), "");
    }

    @Test
    public void test_b_checkRetrieve1() {
        Shared.printTestMethodStartInfo();
        //
        ConfigGeneral cg = new ConfigGeneral();
        cg.setID(-99);
        assertEquals(cg.checkRetrieve1(BaseSQLiteBO.INVALID_CASE_ID), FieldFormat.FIELD_ERROR_ID);
        cg.setID(0);
        assertEquals(cg.checkRetrieve1(BaseSQLiteBO.INVALID_CASE_ID), FieldFormat.FIELD_ERROR_ID);
        //
        cg.setID(1);
        assertEquals(cg.checkRetrieve1(BaseSQLiteBO.INVALID_CASE_ID), "");
    }
    
}
