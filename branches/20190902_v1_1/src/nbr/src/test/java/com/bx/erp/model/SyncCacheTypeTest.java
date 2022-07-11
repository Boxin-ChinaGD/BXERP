package com.bx.erp.model;

import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import com.bx.erp.action.bo.BaseBO;
import com.bx.erp.test.Shared;

public class SyncCacheTypeTest {

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
		
		SyncCacheType syncCacheType = new SyncCacheType();
		String error = "";
		error = syncCacheType.checkCreate(BaseBO.INVALID_CASE_ID);
		Assert.assertEquals(error, "");
	}

	@Test
	public void testCheckRetrieve1() {
		Shared.printTestMethodStartInfo();
		
		SyncCacheType syncCacheType = new SyncCacheType();
		String error = "";
		error = syncCacheType.checkRetrieve1(BaseBO.INVALID_CASE_ID);
		Assert.assertEquals(error, "");
	}

	@Test
	public void testCheckRetrieveN() {
		Shared.printTestMethodStartInfo();
		
		SyncCacheType syncCacheType = new SyncCacheType();
		String error = "";
		error = syncCacheType.checkRetrieveN(BaseBO.INVALID_CASE_ID);
		Assert.assertEquals(error, "");
	}

	@Test
	public void testCheckUpdate() {
		Shared.printTestMethodStartInfo();
		
		SyncCacheType syncCacheType = new SyncCacheType();
		String error = "";
		error = syncCacheType.checkUpdate(BaseBO.INVALID_CASE_ID);
		Assert.assertEquals(error, "");
	}

	@Test
	public void testCheckDelete() {
		Shared.printTestMethodStartInfo();
		
		SyncCacheType syncCacheType = new SyncCacheType();
		String error = "";
		error = syncCacheType.checkDelete(BaseBO.INVALID_CASE_ID);
		Assert.assertEquals(error, "");
	}
}
