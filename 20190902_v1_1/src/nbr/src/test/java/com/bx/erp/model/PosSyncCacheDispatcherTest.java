package com.bx.erp.model;

import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import com.bx.erp.action.bo.BaseBO;
import com.bx.erp.test.Shared;

public class PosSyncCacheDispatcherTest {

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
		
		PosSyncCacheDispatcher posSyncCacheDispatcher = new PosSyncCacheDispatcher();
		String error = "";
		error = posSyncCacheDispatcher.checkCreate(BaseBO.INVALID_CASE_ID);
		Assert.assertEquals(error, "");
	}

	@Test
	public void testCheckRetrieve1() {
		Shared.printTestMethodStartInfo();
		
		PosSyncCacheDispatcher posSyncCacheDispatcher = new PosSyncCacheDispatcher();
		String error = "";
		error = posSyncCacheDispatcher.checkRetrieve1(BaseBO.INVALID_CASE_ID);
		Assert.assertEquals(error, "");
	}

	@Test
	public void testCheckRetrieveN() {
		Shared.printTestMethodStartInfo();
		
		PosSyncCacheDispatcher posSyncCacheDispatcher = new PosSyncCacheDispatcher();
		String error = "";
		error = posSyncCacheDispatcher.checkRetrieveN(BaseBO.INVALID_CASE_ID);
		Assert.assertEquals(error, "");
	}

	@Test
	public void testCheckUpdate() {
		Shared.printTestMethodStartInfo();
		
		PosSyncCacheDispatcher posSyncCacheDispatcher = new PosSyncCacheDispatcher();
		String error = "";
		error = posSyncCacheDispatcher.checkUpdate(BaseBO.INVALID_CASE_ID);
		Assert.assertEquals(error, "");
	}

	@Test
	public void testCheckDelete() {
		Shared.printTestMethodStartInfo();
		
		PosSyncCacheDispatcher posSyncCacheDispatcher = new PosSyncCacheDispatcher();
		String error = "";
		error = posSyncCacheDispatcher.checkDelete(BaseBO.INVALID_CASE_ID);
		Assert.assertEquals(error, "");
	}
}
