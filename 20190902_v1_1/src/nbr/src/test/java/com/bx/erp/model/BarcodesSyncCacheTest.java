package com.bx.erp.model;

import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import com.bx.erp.action.bo.BaseBO;
import com.bx.erp.test.Shared;

public class BarcodesSyncCacheTest {

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
		Shared.printTestMethodStartInfo();
		
		BarcodesSyncCache barcodesSyncCache = new BarcodesSyncCache();
		String error = "";
		error = barcodesSyncCache.checkCreate(BaseBO.INVALID_CASE_ID);
		Assert.assertEquals(error, "");
	}
	
	@Test
	public void testCheckRetrieve1() {
		Shared.printTestMethodStartInfo();
		
		BarcodesSyncCache barcodesSyncCache = new BarcodesSyncCache();
		String error = "";
		error = barcodesSyncCache.checkRetrieve1(BaseBO.INVALID_CASE_ID);
		Assert.assertEquals(error, "");
	}
	
	@Test
	public void testCheckRetrieveN() {
		Shared.printTestMethodStartInfo();
		
		BarcodesSyncCache barcodesSyncCache = new BarcodesSyncCache();
		String error = "";
		error = barcodesSyncCache.checkRetrieveN(BaseBO.INVALID_CASE_ID);
		Assert.assertEquals(error, "");
	}
	
	@Test
	public void testCheckUpdate() {
		Shared.printTestMethodStartInfo();
		
		BarcodesSyncCache barcodesSyncCache = new BarcodesSyncCache();
		String error = "";
		error = barcodesSyncCache.checkUpdate(BaseBO.INVALID_CASE_ID);
		Assert.assertEquals(error, "");
	}
	
	@Test
	public void testCheckDelete() {
		Shared.printTestMethodStartInfo();
		
		BarcodesSyncCache barcodesSyncCache = new BarcodesSyncCache();
		String error = "";
		error = barcodesSyncCache.checkDelete(BaseBO.INVALID_CASE_ID);
		Assert.assertEquals(error, "");
	}
	
	@Test
	public void testCheckDelete_CASE_X_DeleteAllBarcodesSyncCache() {
		Shared.printTestMethodStartInfo();
		
		BarcodesSyncCache barcodesSyncCache = new BarcodesSyncCache();
		String error = "";
		error = barcodesSyncCache.checkDelete(BaseBO.CASE_X_DeleteAllBarcodesSyncCache);
		Assert.assertEquals(error, "");
	}
}
