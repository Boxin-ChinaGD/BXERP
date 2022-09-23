package com.bx.erp.model.config;

import static org.testng.Assert.assertEquals;

import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import com.bx.erp.action.bo.BaseBO;
import com.bx.erp.cache.BaseCache;
import com.bx.erp.test.Shared;
import com.bx.erp.util.FieldFormat;

public class ConfigGeneralTest {
	@BeforeClass
	public void setup() {
		Shared.printTestClassStartInfo();
	}

	@AfterClass
	public void tearDown() {
		Shared.printTestClassEndInfo();
	}

	@Test
	public void checkUpdate() {
		Shared.printTestClassEndInfo();
		
		ConfigGeneral cg = new ConfigGeneral();

		// 时间字段型配置行测试
		// Check UsalableCommodityTaskScanEndTime
		cg.setID(BaseCache.UsalableCommodityTaskScanEndTime);
		cg.setValue("2");
		Assert.assertEquals(cg.checkUpdate(BaseBO.INVALID_CASE_ID), ConfigGeneral.FIELD_ERROR_value1);
		cg.setID(BaseCache.UsalableCommodityTaskScanEndTime);
		cg.setValue("23:00");
		Assert.assertEquals(cg.checkUpdate(BaseBO.INVALID_CASE_ID), ConfigGeneral.FIELD_ERROR_value1);
		//
		cg.setID(BaseCache.UsalableCommodityTaskScanEndTime);
		cg.setValue("13:18:00");
		Assert.assertEquals(cg.checkUpdate(BaseBO.INVALID_CASE_ID), "");

		// Check PurchasingTimeoutTaskScanEndTime
		cg.setID(BaseCache.PurchasingTimeoutTaskScanEndTime);
		cg.setValue("2");
		Assert.assertEquals(cg.checkUpdate(BaseBO.INVALID_CASE_ID), ConfigGeneral.FIELD_ERROR_value1);
		cg.setID(BaseCache.PurchasingTimeoutTaskScanEndTime);
		cg.setValue("23:00");
		Assert.assertEquals(cg.checkUpdate(BaseBO.INVALID_CASE_ID), ConfigGeneral.FIELD_ERROR_value1);
		//
		cg.setID(BaseCache.PurchasingTimeoutTaskScanEndTime);
		cg.setValue("13:18:00");
		Assert.assertEquals(cg.checkUpdate(BaseBO.INVALID_CASE_ID), "");

		// Check UsalableCommodityTaskScanStartTime
		cg.setID(BaseCache.UsalableCommodityTaskScanStartTime);
		cg.setValue("2");
		Assert.assertEquals(cg.checkUpdate(BaseBO.INVALID_CASE_ID), ConfigGeneral.FIELD_ERROR_value1);
		cg.setID(BaseCache.UsalableCommodityTaskScanStartTime);
		cg.setValue("23:00");
		Assert.assertEquals(cg.checkUpdate(BaseBO.INVALID_CASE_ID), ConfigGeneral.FIELD_ERROR_value1);
		//
		cg.setID(BaseCache.UsalableCommodityTaskScanStartTime);
		cg.setValue("13:18:00");
		Assert.assertEquals(cg.checkUpdate(BaseBO.INVALID_CASE_ID), "");
		
		// Check RetailTradeDailyReportSummaryTaskScanStartTime
		cg.setID(BaseCache.RetailTradeDailyReportSummaryTaskScanStartTime);
		cg.setValue("2");
		Assert.assertEquals(cg.checkUpdate(BaseBO.INVALID_CASE_ID), ConfigGeneral.FIELD_ERROR_value1);
		cg.setID(BaseCache.RetailTradeDailyReportSummaryTaskScanStartTime);
		cg.setValue("23:00");
		Assert.assertEquals(cg.checkUpdate(BaseBO.INVALID_CASE_ID), ConfigGeneral.FIELD_ERROR_value1);
		//
		cg.setID(BaseCache.RetailTradeDailyReportSummaryTaskScanStartTime);
		cg.setValue("13:18:00");
		Assert.assertEquals(cg.checkUpdate(BaseBO.INVALID_CASE_ID), "");
		
		// Check RetailTradeDailyReportSummaryTaskScanEndTime
		cg.setID(BaseCache.RetailTradeDailyReportSummaryTaskScanEndTime);
		cg.setValue("2");
		Assert.assertEquals(cg.checkUpdate(BaseBO.INVALID_CASE_ID), ConfigGeneral.FIELD_ERROR_value1);
		cg.setID(BaseCache.RetailTradeDailyReportSummaryTaskScanEndTime);
		cg.setValue("23:00");
		Assert.assertEquals(cg.checkUpdate(BaseBO.INVALID_CASE_ID), ConfigGeneral.FIELD_ERROR_value1);
		//
		cg.setID(BaseCache.RetailTradeDailyReportSummaryTaskScanEndTime);
		cg.setValue("13:18:00");
		Assert.assertEquals(cg.checkUpdate(BaseBO.INVALID_CASE_ID), "");
		
		// Check ShelfLifeTaskScanStartTime
		cg.setID(BaseCache.ShelfLifeTaskScanStartTime);
		cg.setValue("2");
		Assert.assertEquals(cg.checkUpdate(BaseBO.INVALID_CASE_ID), ConfigGeneral.FIELD_ERROR_value1);
		cg.setID(BaseCache.ShelfLifeTaskScanStartTime);
		cg.setValue("23:00");
		Assert.assertEquals(cg.checkUpdate(BaseBO.INVALID_CASE_ID), ConfigGeneral.FIELD_ERROR_value1);
		//
		cg.setID(BaseCache.ShelfLifeTaskScanStartTime);
		cg.setValue("13:18:00");
		Assert.assertEquals(cg.checkUpdate(BaseBO.INVALID_CASE_ID), "");
		
		// Check ShelfLifeTaskScanEndTime
		cg.setID(BaseCache.ShelfLifeTaskScanEndTime);
		cg.setValue("2");
		Assert.assertEquals(cg.checkUpdate(BaseBO.INVALID_CASE_ID), ConfigGeneral.FIELD_ERROR_value1);
		cg.setID(BaseCache.ShelfLifeTaskScanEndTime);
		cg.setValue("23:00");
		Assert.assertEquals(cg.checkUpdate(BaseBO.INVALID_CASE_ID), ConfigGeneral.FIELD_ERROR_value1);
		//
		cg.setID(BaseCache.ShelfLifeTaskScanEndTime);
		cg.setValue("13:18:00");
		Assert.assertEquals(cg.checkUpdate(BaseBO.INVALID_CASE_ID), "");

		// Check RetailTradeMonthlyReportSummaryTaskScanStartTime
		cg.setID(BaseCache.RetailTradeMonthlyReportSummaryTaskScanStartTime);
		cg.setValue("2");
		Assert.assertEquals(cg.checkUpdate(BaseBO.INVALID_CASE_ID), ConfigGeneral.FIELD_ERROR_value1);
		cg.setID(BaseCache.RetailTradeMonthlyReportSummaryTaskScanStartTime);
		cg.setValue("23:00");
		Assert.assertEquals(cg.checkUpdate(BaseBO.INVALID_CASE_ID), ConfigGeneral.FIELD_ERROR_value1);
		//
		cg.setID(BaseCache.RetailTradeMonthlyReportSummaryTaskScanStartTime);
		cg.setValue("13:18:00");
		Assert.assertEquals(cg.checkUpdate(BaseBO.INVALID_CASE_ID), "");
		
		// Check RetailTradeMonthlyReportSummaryTaskScanEndTime
		cg.setID(BaseCache.RetailTradeMonthlyReportSummaryTaskScanEndTime);
		cg.setValue("2");
		Assert.assertEquals(cg.checkUpdate(BaseBO.INVALID_CASE_ID), ConfigGeneral.FIELD_ERROR_value1);
		cg.setID(BaseCache.RetailTradeMonthlyReportSummaryTaskScanEndTime);
		cg.setValue("23:00");
		Assert.assertEquals(cg.checkUpdate(BaseBO.INVALID_CASE_ID), ConfigGeneral.FIELD_ERROR_value1);
		//
		cg.setID(BaseCache.RetailTradeMonthlyReportSummaryTaskScanEndTime);
		cg.setValue("13:18:00");
		Assert.assertEquals(cg.checkUpdate(BaseBO.INVALID_CASE_ID), "");
		
		// Check RetailTradeDailyReportByCategoryParentTaskScanStartTime
		cg.setID(BaseCache.RetailTradeDailyReportByCategoryParentTaskScanStartTime);
		cg.setValue("2");
		Assert.assertEquals(cg.checkUpdate(BaseBO.INVALID_CASE_ID), ConfigGeneral.FIELD_ERROR_value1);
		cg.setID(BaseCache.RetailTradeDailyReportByCategoryParentTaskScanStartTime);
		cg.setValue("23:00");
		Assert.assertEquals(cg.checkUpdate(BaseBO.INVALID_CASE_ID), ConfigGeneral.FIELD_ERROR_value1);
		//
		cg.setID(BaseCache.RetailTradeDailyReportByCategoryParentTaskScanStartTime);
		cg.setValue("13:18:00");
		Assert.assertEquals(cg.checkUpdate(BaseBO.INVALID_CASE_ID), "");
		
		// Check RetailTradeDailyReportByCategoryParentTaskScanEndTime
		cg.setID(BaseCache.RetailTradeDailyReportByCategoryParentTaskScanEndTime);
		cg.setValue("2");
		Assert.assertEquals(cg.checkUpdate(BaseBO.INVALID_CASE_ID), ConfigGeneral.FIELD_ERROR_value1);
		cg.setID(BaseCache.RetailTradeDailyReportByCategoryParentTaskScanEndTime);
		cg.setValue("23:00");
		Assert.assertEquals(cg.checkUpdate(BaseBO.INVALID_CASE_ID), ConfigGeneral.FIELD_ERROR_value1);
		//
		cg.setID(BaseCache.RetailTradeDailyReportByCategoryParentTaskScanEndTime);
		cg.setValue("13:18:00");
		Assert.assertEquals(cg.checkUpdate(BaseBO.INVALID_CASE_ID), "");
		
		// Check RetailTradeDailyReportByStaffTaskScanStartTime
		cg.setID(BaseCache.RetailTradeDailyReportByStaffTaskScanStartTime);
		cg.setValue("2");
		Assert.assertEquals(cg.checkUpdate(BaseBO.INVALID_CASE_ID), ConfigGeneral.FIELD_ERROR_value1);
		cg.setID(BaseCache.RetailTradeDailyReportByStaffTaskScanStartTime);
		cg.setValue("23:00");
		Assert.assertEquals(cg.checkUpdate(BaseBO.INVALID_CASE_ID), ConfigGeneral.FIELD_ERROR_value1);
		//
		cg.setID(BaseCache.RetailTradeDailyReportByStaffTaskScanStartTime);
		cg.setValue("13:18:00");
		Assert.assertEquals(cg.checkUpdate(BaseBO.INVALID_CASE_ID), "");

		// Check RetailTradeDailyReportByStaffTaskScanEndTime
		cg.setID(BaseCache.RetailTradeDailyReportByStaffTaskScanEndTime);
		cg.setValue("2");
		Assert.assertEquals(cg.checkUpdate(BaseBO.INVALID_CASE_ID), ConfigGeneral.FIELD_ERROR_value1);
		cg.setID(BaseCache.RetailTradeDailyReportByStaffTaskScanEndTime);
		cg.setValue("23:00");
		Assert.assertEquals(cg.checkUpdate(BaseBO.INVALID_CASE_ID), ConfigGeneral.FIELD_ERROR_value1);
		//
		cg.setID(BaseCache.RetailTradeDailyReportByStaffTaskScanEndTime);
		cg.setValue("13:18:00");
		Assert.assertEquals(cg.checkUpdate(BaseBO.INVALID_CASE_ID), "");

		// 正整数格式型配置行测试
		// check PAGESIZE
		cg.setID(BaseCache.PAGESIZE);
		cg.setValue("博昕");
		Assert.assertEquals(cg.checkUpdate(BaseBO.INVALID_CASE_ID), ConfigGeneral.FIELD_ERROR_value2);
		cg.setID(BaseCache.PAGESIZE);
		cg.setValue("0");
		Assert.assertEquals(cg.checkUpdate(BaseBO.INVALID_CASE_ID), ConfigGeneral.FIELD_ERROR_value2);
		cg.setID(BaseCache.PAGESIZE);
		cg.setValue("-99");
		Assert.assertEquals(cg.checkUpdate(BaseBO.INVALID_CASE_ID), ConfigGeneral.FIELD_ERROR_value2);
		//
		cg.setID(BaseCache.PAGESIZE);
		cg.setValue("1");
		Assert.assertEquals(cg.checkUpdate(BaseBO.INVALID_CASE_ID), "");

		// check ACTIVE_SMALLSHEET_ID
		cg.setID(BaseCache.ACTIVE_SMALLSHEET_ID);
		cg.setValue("博昕");
		Assert.assertEquals(cg.checkUpdate(BaseBO.INVALID_CASE_ID), ConfigGeneral.FIELD_ERROR_value2);
		cg.setID(BaseCache.ACTIVE_SMALLSHEET_ID);
		cg.setValue("0");
		Assert.assertEquals(cg.checkUpdate(BaseBO.INVALID_CASE_ID), ConfigGeneral.FIELD_ERROR_value2);
		cg.setID(BaseCache.ACTIVE_SMALLSHEET_ID);
		cg.setValue("-99");
		Assert.assertEquals(cg.checkUpdate(BaseBO.INVALID_CASE_ID), ConfigGeneral.FIELD_ERROR_value2);
		//
		cg.setID(BaseCache.ACTIVE_SMALLSHEET_ID);
		cg.setValue("1");
		Assert.assertEquals(cg.checkUpdate(BaseBO.INVALID_CASE_ID), "");
		
		// check PurchasingTimeoutTaskFlag
		cg.setID(BaseCache.PurchasingTimeoutTaskFlag);
		cg.setValue("博昕");
		Assert.assertEquals(cg.checkUpdate(BaseBO.INVALID_CASE_ID), ConfigGeneral.FIELD_ERROR_value2);
		cg.setID(BaseCache.PurchasingTimeoutTaskFlag);
		cg.setValue("0");
		Assert.assertEquals(cg.checkUpdate(BaseBO.INVALID_CASE_ID), ConfigGeneral.FIELD_ERROR_value2);
		cg.setID(BaseCache.PurchasingTimeoutTaskFlag);
		cg.setValue("-99");
		Assert.assertEquals(cg.checkUpdate(BaseBO.INVALID_CASE_ID), ConfigGeneral.FIELD_ERROR_value2);
		//
		cg.setID(BaseCache.PurchasingTimeoutTaskFlag);
		cg.setValue("1");
		Assert.assertEquals(cg.checkUpdate(BaseBO.INVALID_CASE_ID), "");
	
		
		// check UsalableCommodityTaskFlag
		cg.setID(BaseCache.UsalableCommodityTaskFlag);
		cg.setValue("博昕");
		Assert.assertEquals(cg.checkUpdate(BaseBO.INVALID_CASE_ID), ConfigGeneral.FIELD_ERROR_value2);
		cg.setID(BaseCache.UsalableCommodityTaskFlag);
		cg.setValue("0");
		Assert.assertEquals(cg.checkUpdate(BaseBO.INVALID_CASE_ID), ConfigGeneral.FIELD_ERROR_value2);
		cg.setID(BaseCache.UsalableCommodityTaskFlag);
		cg.setValue("-99");
		Assert.assertEquals(cg.checkUpdate(BaseBO.INVALID_CASE_ID), ConfigGeneral.FIELD_ERROR_value2);
		//
		cg.setID(BaseCache.UsalableCommodityTaskFlag);
		cg.setValue("1");
		Assert.assertEquals(cg.checkUpdate(BaseBO.INVALID_CASE_ID), "");
		
		// check ShelfLifeTaskFlag
		cg.setID(BaseCache.ShelfLifeTaskFlag);
		cg.setValue("博昕");
		Assert.assertEquals(cg.checkUpdate(BaseBO.INVALID_CASE_ID), ConfigGeneral.FIELD_ERROR_value2);
		cg.setID(BaseCache.ShelfLifeTaskFlag);
		cg.setValue("0");
		Assert.assertEquals(cg.checkUpdate(BaseBO.INVALID_CASE_ID), ConfigGeneral.FIELD_ERROR_value2);
		cg.setID(BaseCache.ShelfLifeTaskFlag);
		cg.setValue("-99");
		Assert.assertEquals(cg.checkUpdate(BaseBO.INVALID_CASE_ID), ConfigGeneral.FIELD_ERROR_value2);
		//
		cg.setID(BaseCache.ShelfLifeTaskFlag);
		cg.setValue("1");
		Assert.assertEquals(cg.checkUpdate(BaseBO.INVALID_CASE_ID), "");
		
		// check MaxVicePackageUnit
		cg.setID(BaseCache.MaxVicePackageUnit);
		cg.setValue("博昕");
		Assert.assertEquals(cg.checkUpdate(BaseBO.INVALID_CASE_ID), ConfigGeneral.FIELD_ERROR_value2);
		cg.setID(BaseCache.MaxVicePackageUnit);
		cg.setValue("0");
		Assert.assertEquals(cg.checkUpdate(BaseBO.INVALID_CASE_ID), ConfigGeneral.FIELD_ERROR_value2);
		cg.setID(BaseCache.MaxVicePackageUnit);
		cg.setValue("-99");
		Assert.assertEquals(cg.checkUpdate(BaseBO.INVALID_CASE_ID), ConfigGeneral.FIELD_ERROR_value2);
		//
		cg.setID(BaseCache.MaxVicePackageUnit);
		cg.setValue("1");
		Assert.assertEquals(cg.checkUpdate(BaseBO.INVALID_CASE_ID), "");
		
		// check MaxProviderNOPerCommodity
		cg.setID(BaseCache.MaxProviderNOPerCommodity);
		cg.setValue("博昕");
		Assert.assertEquals(cg.checkUpdate(BaseBO.INVALID_CASE_ID), ConfigGeneral.FIELD_ERROR_value2);
		cg.setID(BaseCache.MaxProviderNOPerCommodity);
		cg.setValue("0");
		Assert.assertEquals(cg.checkUpdate(BaseBO.INVALID_CASE_ID), ConfigGeneral.FIELD_ERROR_value2);
		cg.setID(BaseCache.MaxProviderNOPerCommodity);
		cg.setValue("-99");
		Assert.assertEquals(cg.checkUpdate(BaseBO.INVALID_CASE_ID), ConfigGeneral.FIELD_ERROR_value2);
		//
		cg.setID(BaseCache.MaxProviderNOPerCommodity);
		cg.setValue("1");
		Assert.assertEquals(cg.checkUpdate(BaseBO.INVALID_CASE_ID), "");
		
		// check MaxPurchasingOrderCommodityNO
		cg.setID(BaseCache.MaxPurchasingOrderCommodityNO);
		cg.setValue("博昕");
		Assert.assertEquals(cg.checkUpdate(BaseBO.INVALID_CASE_ID), ConfigGeneral.FIELD_ERROR_value2);
		cg.setID(BaseCache.MaxPurchasingOrderCommodityNO);
		cg.setValue("0");
		Assert.assertEquals(cg.checkUpdate(BaseBO.INVALID_CASE_ID), ConfigGeneral.FIELD_ERROR_value2);
		cg.setID(BaseCache.MaxPurchasingOrderCommodityNO);
		cg.setValue("-99");
		Assert.assertEquals(cg.checkUpdate(BaseBO.INVALID_CASE_ID), ConfigGeneral.FIELD_ERROR_value2);
		//
		cg.setID(BaseCache.MaxPurchasingOrderCommodityNO);
		cg.setValue("1");
		Assert.assertEquals(cg.checkUpdate(BaseBO.INVALID_CASE_ID), "");
		
		// check CommodityLogoVolumeMax
		cg.setID(BaseCache.CommodityLogoVolumeMax);
		cg.setValue("博昕");
		Assert.assertEquals(cg.checkUpdate(BaseBO.INVALID_CASE_ID), ConfigGeneral.FIELD_ERROR_value2);
		cg.setID(BaseCache.CommodityLogoVolumeMax);
		cg.setValue("0");
		Assert.assertEquals(cg.checkUpdate(BaseBO.INVALID_CASE_ID), ConfigGeneral.FIELD_ERROR_value2);
		cg.setID(BaseCache.CommodityLogoVolumeMax);
		cg.setValue("-99");
		Assert.assertEquals(cg.checkUpdate(BaseBO.INVALID_CASE_ID), ConfigGeneral.FIELD_ERROR_value2);
		//
		cg.setID(BaseCache.CommodityLogoVolumeMax);
		cg.setValue("1");
		Assert.assertEquals(cg.checkUpdate(BaseBO.INVALID_CASE_ID), "");
		
		// check MaxWarehousingCommodityNO
		cg.setID(BaseCache.MaxWarehousingCommodityNO);
		cg.setValue("博昕");
		Assert.assertEquals(cg.checkUpdate(BaseBO.INVALID_CASE_ID), ConfigGeneral.FIELD_ERROR_value2);
		cg.setID(BaseCache.MaxWarehousingCommodityNO);
		cg.setValue("0");
		Assert.assertEquals(cg.checkUpdate(BaseBO.INVALID_CASE_ID), ConfigGeneral.FIELD_ERROR_value2);
		cg.setID(BaseCache.MaxWarehousingCommodityNO);
		cg.setValue("-99");
		Assert.assertEquals(cg.checkUpdate(BaseBO.INVALID_CASE_ID), ConfigGeneral.FIELD_ERROR_value2);
		//
		cg.setID(BaseCache.MaxWarehousingCommodityNO);
		cg.setValue("1");
		Assert.assertEquals(cg.checkUpdate(BaseBO.INVALID_CASE_ID), "");
	}

	@Test
	public void checkRetrieveN() {

	}

	@Test
	public void checkRetrieve1() {
		Shared.printTestMethodStartInfo();
		//
		ConfigGeneral cg = new ConfigGeneral();
		cg.setID(-99);
		assertEquals(cg.checkRetrieve1(BaseBO.INVALID_CASE_ID), FieldFormat.FIELD_ERROR_ID);
		cg.setID(0);
		assertEquals(cg.checkRetrieve1(BaseBO.INVALID_CASE_ID), FieldFormat.FIELD_ERROR_ID);
		//
		cg.setID(1);
		assertEquals(cg.checkRetrieve1(BaseBO.INVALID_CASE_ID), "");
	}
}
