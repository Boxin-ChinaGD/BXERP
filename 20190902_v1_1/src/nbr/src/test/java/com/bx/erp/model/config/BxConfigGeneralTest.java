package com.bx.erp.model.config;

import static org.testng.Assert.assertEquals;

import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import com.bx.erp.action.bo.BaseBO;
import com.bx.erp.test.Shared;

public class BxConfigGeneralTest {
	@BeforeClass
	public void setup() {
		Shared.printTestClassStartInfo();
	}

	@AfterClass
	public void tearDown() {
		Shared.printTestClassEndInfo();
	}

	// @Test
	// public void checkUpdate() {
	// BxConfigGeneral cg = new BxConfigGeneral();
	//
	// //正整数格式型配置行测试
	// cg.setID(BaseCache.CompanyBusinessLicensePictureVolumeMax);
	// cg.setValue("博昕");
	// Assert.assertEquals(cg.checkUpdate(BaseBO.INVALID_CASE_ID),
	// BxConfigGeneral.FIELD_ERROR_value4);
	// cg.setID(BaseCache.CompanyBusinessLicensePictureVolumeMax);
	// cg.setValue("-2");
	// Assert.assertEquals(cg.checkUpdate(BaseBO.INVALID_CASE_ID),
	// BxConfigGeneral.FIELD_ERROR_value4);
	// //
	// cg.setID(BaseCache.CompanyBusinessLicensePictureVolumeMax);
	// cg.setValue("2");
	// Assert.assertEquals(cg.checkUpdate(BaseBO.INVALID_CASE_ID), "");
	//
	// //整数格式型配置行测试
	// cg.setID(BaseCache.CommodityNOStart);
	// cg.setValue("博昕");
	// Assert.assertEquals(cg.checkUpdate(BaseBO.INVALID_CASE_ID),
	// BxConfigGeneral.FIELD_ERROR_value2);
	// cg.setID(BaseCache.CommodityNOStart);
	// cg.setValue("-2");
	// Assert.assertEquals(cg.checkUpdate(BaseBO.INVALID_CASE_ID), "");
	// //
	// cg.setID(BaseCache.CommodityNOStart);
	// cg.setValue("2");
	// Assert.assertEquals(cg.checkUpdate(BaseBO.INVALID_CASE_ID), "");
	//
	// //路径配置行测试
	// cg.setID(BaseCache.CompanyBusinessLicensePictureDir);
	// cg.setValue("");
	// Assert.assertEquals(cg.checkUpdate(BaseBO.INVALID_CASE_ID),
	// BxConfigGeneral.FIELD_ERROR_value3);
	// //
	// cg.setID(BaseCache.CompanyBusinessLicensePictureDir);
	// cg.setValue(null);
	// Assert.assertEquals(cg.checkUpdate(BaseBO.INVALID_CASE_ID),
	// BxConfigGeneral.FIELD_ERROR_value3);
	// //
	// cg.setID(BaseCache.CompanyBusinessLicensePictureDir);
	// cg.setValue("D:/nbr/pic/common_db/business_license_picture/");
	// Assert.assertEquals(cg.checkUpdate(BaseBO.INVALID_CASE_ID), "");
	//
	// //...还有部分字段未测试
	// }

	@Test
	public void checkRetrieveN() {

	}

	@Test
	public void checkRetrieve1() {
		Shared.printTestMethodStartInfo();
		//
		BxConfigGeneral bcg = new BxConfigGeneral();
		bcg.setID(-99);
		assertEquals(bcg.checkRetrieve1(BaseBO.INVALID_CASE_ID), BxConfigGeneral.FIELD_ERROR_BxConfigGeneralID);
		bcg.setID(0);
		assertEquals(bcg.checkRetrieve1(BaseBO.INVALID_CASE_ID), BxConfigGeneral.FIELD_ERROR_BxConfigGeneralID);
		bcg.setID(BxConfigGeneral.MAX_BxConfigGeneralID + 1);
		assertEquals(bcg.checkRetrieve1(BaseBO.INVALID_CASE_ID), BxConfigGeneral.FIELD_ERROR_BxConfigGeneralID);
		//
		bcg.setID(1);
		assertEquals(bcg.checkRetrieve1(BaseBO.INVALID_CASE_ID), "");
	}
}
