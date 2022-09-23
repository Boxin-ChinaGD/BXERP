package com.bx.erp.model.config;

import static org.testng.Assert.assertEquals;

import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import com.bx.erp.action.bo.BaseBO;
import com.bx.erp.test.Shared;
import com.bx.erp.util.FieldFormat;

public class ConfigCacheSizeTest {
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
		Shared.printTestMethodStartInfo();
		//
		ConfigCacheSize ccs = new ConfigCacheSize();
		ccs.setID(-99);
		assertEquals(ccs.checkUpdate(BaseBO.INVALID_CASE_ID), FieldFormat.FIELD_ERROR_ID);
		ccs.setID(0);
		assertEquals(ccs.checkUpdate(BaseBO.INVALID_CASE_ID), FieldFormat.FIELD_ERROR_ID);
		ccs.setID(1);
		//
		ccs.setValue(null);
		assertEquals(ccs.checkUpdate(BaseBO.INVALID_CASE_ID), ConfigCacheSize.FIELD_ERROR_value);
		ccs.setValue("");
		assertEquals(ccs.checkUpdate(BaseBO.INVALID_CASE_ID), ConfigCacheSize.FIELD_ERROR_value);
		ccs.setValue("ffffffffffffffffffffffffffffffffffffffffffffff");
		assertEquals(ccs.checkUpdate(BaseBO.INVALID_CASE_ID), ConfigCacheSize.FIELD_ERROR_value);
		ccs.setValue("1433223");
		//
		assertEquals(ccs.checkUpdate(BaseBO.INVALID_CASE_ID), "");
	}

	@Test
	public void checkRetrieveN() {

	}

	@Test
	public void checkRetrieve1() {
		Shared.printTestMethodStartInfo();
		//
		ConfigCacheSize ccs = new ConfigCacheSize();
		ccs.setID(-99);
		assertEquals(ccs.checkRetrieve1(BaseBO.INVALID_CASE_ID), FieldFormat.FIELD_ERROR_ID);
		ccs.setID(0);
		assertEquals(ccs.checkRetrieve1(BaseBO.INVALID_CASE_ID), FieldFormat.FIELD_ERROR_ID);
		//
		ccs.setID(1);
		assertEquals(ccs.checkRetrieve1(BaseBO.INVALID_CASE_ID), "");
	}
}
