package com.bx.erp.model;

import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import com.bx.erp.action.bo.BaseBO;
import com.bx.erp.test.Shared;

public class BaseSyncCacheDispatcherTest {

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
		
		BaseSyncCacheDispatcher baseSyncCacheDispatcher = new BaseSyncCacheDispatcher();
		baseSyncCacheDispatcher.setPos_ID(1);
		baseSyncCacheDispatcher.setSyncCacheID(1);
		String error = baseSyncCacheDispatcher.checkCreate(BaseBO.INVALID_CASE_ID);
		Assert.assertEquals(error, "");
		
		baseSyncCacheDispatcher.setPos_ID(-1);
		error = baseSyncCacheDispatcher.checkCreate(BaseBO.INVALID_CASE_ID);
		Assert.assertEquals(error, BaseSyncCacheDispatcher.FIELD_NAME_pos_ID);
		
		baseSyncCacheDispatcher.setPos_ID(2);
		baseSyncCacheDispatcher.setSyncCacheID(-1);
		error = baseSyncCacheDispatcher.checkCreate(BaseBO.INVALID_CASE_ID);
		Assert.assertEquals(error, BaseSyncCacheDispatcher.FIELD_NAME_syncCacheID);
	}

	@Test
	public void testCheckRetrieve1() {
		Shared.printTestMethodStartInfo();
		
		BaseSyncCacheDispatcher baseSyncCacheDispatcher = new BaseSyncCacheDispatcher();
		String error = "";
		error = baseSyncCacheDispatcher.checkRetrieve1(BaseBO.INVALID_CASE_ID);
		Assert.assertEquals(error, "");
	}

	@Test
	public void testCheckRetrieveN() {
		Shared.printTestMethodStartInfo();
		
		BaseSyncCacheDispatcher baseSyncCacheDispatcher = new BaseSyncCacheDispatcher();
		String error = "";
		error = baseSyncCacheDispatcher.checkRetrieveN(BaseBO.INVALID_CASE_ID);
		Assert.assertEquals(error, "");
	}

	@Test
	public void testCheckUpdate() {
		Shared.printTestMethodStartInfo();
		
		BaseSyncCacheDispatcher baseSyncCacheDispatcher = new BaseSyncCacheDispatcher();
		String error = "";
		error = baseSyncCacheDispatcher.checkUpdate(BaseBO.INVALID_CASE_ID);
		Assert.assertEquals(error, "");
	}

	@Test
	public void testCheckDelete() {
		Shared.printTestMethodStartInfo();
		
		BaseSyncCacheDispatcher baseSyncCacheDispatcher = new BaseSyncCacheDispatcher();
		String error = "";
		error = baseSyncCacheDispatcher.checkDelete(BaseBO.INVALID_CASE_ID);
		Assert.assertEquals(error, "");
	}
}
