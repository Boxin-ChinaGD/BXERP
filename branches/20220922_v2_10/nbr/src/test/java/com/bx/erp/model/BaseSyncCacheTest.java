package com.bx.erp.model;

import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import com.bx.erp.action.bo.BaseBO;
import com.bx.erp.test.Shared;

public class BaseSyncCacheTest {
	
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
		
		BaseSyncCache baseSyncCache = new BaseSyncCache();
		baseSyncCache.setSyncType("C");
		baseSyncCache.setSyncData_ID(1);
		baseSyncCache.setSyncSequence(1);
		String error = baseSyncCache.checkCreate(BaseBO.INVALID_CASE_ID);
		Assert.assertEquals(error, "");
		
		baseSyncCache.setSyncType("A");
		error = baseSyncCache.checkCreate(BaseBO.INVALID_CASE_ID);
		Assert.assertEquals(error, BaseSyncCache.FIELD_ERROR_syncType);
		
		baseSyncCache.setSyncType("U");
		baseSyncCache.setSyncData_ID(-1);
		error = baseSyncCache.checkCreate(BaseBO.INVALID_CASE_ID);
		Assert.assertEquals(error, BaseSyncCache.FIELD_ERROR_syncData_ID);
		
		baseSyncCache.setSyncData_ID(2);
		baseSyncCache.setSyncSequence(-1);
		error = baseSyncCache.checkCreate(BaseBO.INVALID_CASE_ID);
		Assert.assertEquals(error, BaseSyncCache.FIELD_ERROR_syncSequence);
	}
	
	@Test
	public void testCheckRetrieve1() {
		Shared.printTestMethodStartInfo();
		
		BaseSyncCache baseSyncCache = new BaseSyncCache();
		String error = baseSyncCache.checkRetrieve1(BaseBO.INVALID_CASE_ID);
		Assert.assertEquals(error, "");
	}
	
	@Test
	public void testCheckRetrieveN() {
		Shared.printTestMethodStartInfo();
		
		BaseSyncCache baseSyncCache = new BaseSyncCache();
		String error = baseSyncCache.checkRetrieveN(BaseBO.INVALID_CASE_ID);
		Assert.assertEquals(error, "");
	}
	
	@Test
	public void testCheckUpdate() {
		Shared.printTestMethodStartInfo();
		
		BaseSyncCache baseSyncCache = new BaseSyncCache();
		String error = baseSyncCache.checkUpdate(BaseBO.INVALID_CASE_ID);
		Assert.assertEquals(error, "");
	}
	
	@Test
	public void testCheckDelete() {
		Shared.printTestMethodStartInfo();
		
		BaseSyncCache baseSyncCache = new BaseSyncCache();
		baseSyncCache.setSyncData_ID(0);
		String error = baseSyncCache.checkDelete(BaseBO.INVALID_CASE_ID);
		Assert.assertEquals(error, BaseSyncCache.FIELD_ERROR_syncData_ID);
		
		baseSyncCache.setSyncData_ID(-1);
		error = baseSyncCache.checkDelete(BaseBO.INVALID_CASE_ID);
		Assert.assertEquals(error, BaseSyncCache.FIELD_ERROR_syncData_ID);
		
		baseSyncCache.setSyncData_ID(1);
		error = baseSyncCache.checkDelete(BaseBO.INVALID_CASE_ID);
		Assert.assertEquals(error, "");
	}
}
