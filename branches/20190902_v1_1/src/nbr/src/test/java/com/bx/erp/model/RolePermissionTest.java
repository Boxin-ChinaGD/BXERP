package com.bx.erp.model;

import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import com.bx.erp.action.BaseAction;
import com.bx.erp.action.bo.BaseBO;
import com.bx.erp.test.Shared;
import com.bx.erp.util.FieldFormat;

public class RolePermissionTest {

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
		
		RolePermission rolePermission = new RolePermission();
		String error = "";
		error = rolePermission.checkCreate(BaseBO.INVALID_CASE_ID);
		Assert.assertEquals(error, "");
	}

	@Test
	public void testCheckRetrieve1() {
		Shared.printTestMethodStartInfo();
		
		RolePermission rolePermission = new RolePermission();
		String error = "";
		error = rolePermission.checkRetrieve1(BaseBO.INVALID_CASE_ID);
		Assert.assertEquals(error, "");
	}

	@Test
	public void testCheckRetrieveN() {
		Shared.printTestMethodStartInfo();
		
		RolePermission rolePermission = new RolePermission();
		String error = "";
		error = rolePermission.checkRetrieveN(BaseBO.INVALID_CASE_ID);
		Assert.assertEquals(error, "");
	}

	@Test
	public void testCheckUpdate() {
		Shared.printTestMethodStartInfo();
		
		RolePermission rolePermission = new RolePermission();
		String error = "";
		error = rolePermission.checkUpdate(BaseBO.INVALID_CASE_ID);
		Assert.assertEquals(error, "");
	}

	@Test
	public void testCheckDelete() {
		Shared.printTestMethodStartInfo();
		
		RolePermission rolePermission = new RolePermission();
		rolePermission.setID(1);
		String error = rolePermission.checkDelete(BaseBO.INVALID_CASE_ID);
		Assert.assertEquals(error, "");
		//测试ID
		rolePermission.setID(BaseAction.INVALID_ID);
		error = rolePermission.checkDelete(BaseBO.INVALID_CASE_ID);
		Assert.assertEquals(error, FieldFormat.FIELD_ERROR_ID);
	}
}
