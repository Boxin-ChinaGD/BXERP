package com.bx.erp.model;

import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import com.bx.erp.action.bo.BaseBO;
import com.bx.erp.test.Shared;

public class NtpTest {

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
		
		Ntp ntp = new Ntp();
		String error = "";
		error = ntp.checkCreate(BaseBO.INVALID_CASE_ID);
		Assert.assertEquals(error, "");
	}

	@Test
	public void testCheckRetrieve1() {
		Shared.printTestMethodStartInfo();
		
		Ntp ntp = new Ntp();
		String error = "";
		error = ntp.checkRetrieve1(BaseBO.INVALID_CASE_ID);
		Assert.assertEquals(error, "");
	}

	@Test
	public void testCheckRetrieveN() {
		Shared.printTestMethodStartInfo();
		
		Ntp ntp = new Ntp();
		String error = "";
		error = ntp.checkRetrieveN(BaseBO.INVALID_CASE_ID);
		Assert.assertEquals(error, "");
	}

	@Test
	public void testCheckUpdate() {
		Shared.printTestMethodStartInfo();
		
		Ntp ntp = new Ntp();
		String error = "";
		error = ntp.checkUpdate(BaseBO.INVALID_CASE_ID);
		Assert.assertEquals(error, "");
	}

	@Test
	public void testCheckDelete() {
		Shared.printTestMethodStartInfo();
		
		Ntp ntp = new Ntp();
		String error = "";
		error = ntp.checkDelete(BaseBO.INVALID_CASE_ID);
		Assert.assertEquals(error, "");
	}
}
