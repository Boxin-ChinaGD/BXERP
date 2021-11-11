 package com.bx.erp.model;

import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import com.bx.erp.action.bo.BaseBO;
import com.bx.erp.test.Shared;

public class VipCardCodeTest {
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
		VipCardCode vipCardCode = new VipCardCode();
		vipCardCode.setSN("");
		String err = vipCardCode.checkCreate(BaseBO.INVALID_CASE_ID);
		Assert.assertTrue("".equals(err), err);
		//
		vipCardCode.setSN(null);
		err = vipCardCode.checkCreate(BaseBO.INVALID_CASE_ID);
		Assert.assertTrue("".equals(err), err);
		//
		vipCardCode.setSN("123456789");
		err = vipCardCode.checkCreate(BaseBO.INVALID_CASE_ID);
		Assert.assertTrue(VipCardCode.FIELD_ERROR_SN.equals(err), err);
		//
		vipCardCode.setSN("123456789");
		err = vipCardCode.checkCreate(BaseBO.CASE_VipCardCode_ImportFromOldSystem);
		Assert.assertTrue(VipCardCode.FIELD_ERROR_SNLength.equals(err), err);
		//
		vipCardCode.setSN("1234567891234567");
		err = vipCardCode.checkCreate(BaseBO.CASE_VipCardCode_ImportFromOldSystem);
		Assert.assertTrue("".equals(err), err);
		//
		vipCardCode.setSN(null);
		err = vipCardCode.checkCreate(BaseBO.CASE_VipCardCode_ImportFromOldSystem);
		Assert.assertTrue(VipCardCode.FIELD_ERROR_SNLength.equals(err), err);
		//
		vipCardCode.setSN("");
		err = vipCardCode.checkCreate(BaseBO.CASE_VipCardCode_ImportFromOldSystem);
		Assert.assertTrue(VipCardCode.FIELD_ERROR_SNLength.equals(err), err);
	}
}
