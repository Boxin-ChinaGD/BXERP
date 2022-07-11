package com.bx.erp.model;

import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import com.bx.erp.action.bo.BaseBO;
import com.bx.erp.test.Shared;
import com.bx.erp.util.FieldFormat;

public class StaffPermissionTest {

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
		
		StaffPermission staffPermission = new StaffPermission();
		String error = "";
		error = staffPermission.checkCreate(BaseBO.INVALID_CASE_ID);
		Assert.assertEquals(error, "");
	}

	@Test
	public void testCheckRetrieve1() {
		Shared.printTestMethodStartInfo();
		
		StaffPermission staffPermission = new StaffPermission();
		String error = "";
		error = staffPermission.checkRetrieve1(BaseBO.INVALID_CASE_ID);
		Assert.assertEquals(error, "");
	}

	@Test
	public void testCheckRetrieveN() {
		Shared.printTestMethodStartInfo();
		
		StaffPermission staffPermission = new StaffPermission();
		staffPermission.setPageIndex(1);
		staffPermission.setPageSize(10);
		String error = staffPermission.checkRetrieveN(BaseBO.INVALID_CASE_ID);
		Assert.assertEquals(error, "");
		//测试PageIndex
		staffPermission.setPageIndex(0);
		error = staffPermission.checkRetrieveN(BaseBO.INVALID_CASE_ID);
		Assert.assertEquals(error, FieldFormat.FIELD_ERROR_Paging);
		staffPermission.setPageIndex(1);
		//测试PageSize
		staffPermission.setPageSize(0);
		error = staffPermission.checkRetrieveN(BaseBO.INVALID_CASE_ID);
		Assert.assertEquals(error, FieldFormat.FIELD_ERROR_Paging);
		staffPermission.setPageSize(10);
	}

	@Test
	public void testCheckUpdate() {
		Shared.printTestMethodStartInfo();
		
		StaffPermission staffPermission = new StaffPermission();
		String error = "";
		error = staffPermission.checkUpdate(BaseBO.INVALID_CASE_ID);
		Assert.assertEquals(error, "");
	}

	@Test
	public void testCheckDelete() {
		Shared.printTestMethodStartInfo();
		
		StaffPermission staffPermission = new StaffPermission();
		String error = "";
		error = staffPermission.checkDelete(BaseBO.INVALID_CASE_ID);
		Assert.assertEquals(error, "");
	}
}
