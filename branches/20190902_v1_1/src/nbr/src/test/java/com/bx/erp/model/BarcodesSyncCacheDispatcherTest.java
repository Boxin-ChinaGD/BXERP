package com.bx.erp.model;

import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import com.bx.erp.action.bo.BaseBO;
import com.bx.erp.test.Shared;

public class BarcodesSyncCacheDispatcherTest {

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
		
		BarcodesSyncCacheDispatcher barcodesSyncCacheDispatcher = new BarcodesSyncCacheDispatcher();
		String error = "";
		error = barcodesSyncCacheDispatcher.checkCreate(BaseBO.INVALID_CASE_ID);
		Assert.assertEquals(error, "");
	}
	
	@Test
	public void testCheckRetrieve1() {
		Shared.printTestMethodStartInfo();
		
		BarcodesSyncCacheDispatcher barcodesSyncCacheDispatcher = new BarcodesSyncCacheDispatcher();
		String error = "";
		error = barcodesSyncCacheDispatcher.checkRetrieve1(BaseBO.INVALID_CASE_ID);
		Assert.assertEquals(error, "");
	}
	
	@Test
	public void testCheckRetrieveN() {
		Shared.printTestMethodStartInfo();
		
		BarcodesSyncCacheDispatcher barcodesSyncCacheDispatcher = new BarcodesSyncCacheDispatcher();
		String error = "";
		error = barcodesSyncCacheDispatcher.checkRetrieveN(BaseBO.INVALID_CASE_ID);
		Assert.assertEquals(error, "");
	}
	
	@Test
	public void testCheckUpdate() {
		Shared.printTestMethodStartInfo();
		
		BarcodesSyncCacheDispatcher barcodesSyncCacheDispatcher = new BarcodesSyncCacheDispatcher();
		String error = "";
		error = barcodesSyncCacheDispatcher.checkUpdate(BaseBO.INVALID_CASE_ID);
		Assert.assertEquals(error, "");
	}
	
	@Test
	public void testCheckDelete() {
		Shared.printTestMethodStartInfo();
		
		BarcodesSyncCacheDispatcher barcodesSyncCacheDispatcher = new BarcodesSyncCacheDispatcher();
		String error = "";
		error = barcodesSyncCacheDispatcher.checkDelete(BaseBO.INVALID_CASE_ID);
		Assert.assertEquals(error, "");
	}
}
