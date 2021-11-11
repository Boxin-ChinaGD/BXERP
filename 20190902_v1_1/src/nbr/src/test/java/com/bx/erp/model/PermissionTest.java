package com.bx.erp.model;

import static org.testng.Assert.assertEquals;

import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import com.bx.erp.action.bo.BaseBO;
import com.bx.erp.test.Shared;
import com.bx.erp.util.FieldFormat;

public class PermissionTest {

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

		Permission permission = new Permission();
		String error = "";

		String str = "123456789012345678901234567890";
		permission.setSP(str + str + str);
		error = permission.checkCreate(BaseBO.INVALID_CASE_ID);
		Assert.assertEquals(error, Permission.FIELD_ERROR_SP);
		permission.setSP("12346");
		//
		permission.setName(str + str + str);
		error = permission.checkCreate(BaseBO.INVALID_CASE_ID);
		Assert.assertEquals(error, Permission.FIELD_ERROR_name);
		permission.setName("12346");
		//
		permission.setDomain(str + str + str);
		error = permission.checkCreate(BaseBO.INVALID_CASE_ID);
		Assert.assertEquals(error, Permission.FIELD_ERROR_domain);
		permission.setDomain("12346");
		//
		permission.setRemark(str + str + str);
		error = permission.checkCreate(BaseBO.INVALID_CASE_ID);
		Assert.assertEquals(error, Permission.FIELD_ERROR_remark);
		permission.setRemark("12346");
		//
		error = permission.checkCreate(BaseBO.INVALID_CASE_ID);
		Assert.assertEquals(error, "");
	}

	@Test
	public void testCheckRetrieve1() {
		Shared.printTestMethodStartInfo();

		Permission permission = new Permission();
		String error = "";
		error = permission.checkRetrieve1(BaseBO.INVALID_CASE_ID);
		Assert.assertEquals(error, "");
	}

	@Test
	public void testCheckRetrieveN() {
		Shared.printTestMethodStartInfo();

		Permission p = new Permission();
		String err = "";
		// Case RetrieveAlsoRoleStaff
		p.setID(-99);
		err = p.checkRetrieveN(BaseBO.CASE_Permission_RetrieveAlsoRoleStaff);
		assertEquals(err, FieldFormat.FIELD_ERROR_ID);
		p.setID(0);
		err = p.checkRetrieveN(BaseBO.CASE_Permission_RetrieveAlsoRoleStaff);
		assertEquals(err, FieldFormat.FIELD_ERROR_ID);
		//
		p.setID(1);
		err = p.checkRetrieveN(BaseBO.CASE_Permission_RetrieveAlsoRoleStaff);
		Assert.assertEquals(err, "");

		// case default
		p.setName("12516165165156165165165615165165615165165");
		err = p.checkRetrieveN(BaseBO.INVALID_CASE_ID);
		Assert.assertEquals(err, Permission.FIELD_ERROR_name);
		p.setName("哈哈哈");
		//
		p.setDomain("12516165165156165165165615165165615165165");
		err = p.checkRetrieveN(BaseBO.INVALID_CASE_ID);
		Assert.assertEquals(err, Permission.FIELD_ERROR_domain);
		p.setDomain("哈哈哈");
		//
		p.setPageIndex(-666);
		err = p.checkRetrieveN(BaseBO.INVALID_CASE_ID);
		Assert.assertEquals(err, FieldFormat.FIELD_ERROR_Paging);
		p.setPageIndex(0);
		err = p.checkRetrieveN(BaseBO.INVALID_CASE_ID);
		Assert.assertEquals(err, FieldFormat.FIELD_ERROR_Paging);
		p.setPageIndex(1);
		//
		p.setPageSize(-666);
		err = p.checkRetrieveN(BaseBO.INVALID_CASE_ID);
		Assert.assertEquals(err, FieldFormat.FIELD_ERROR_Paging);
		p.setPageSize(0);
		err = p.checkRetrieveN(BaseBO.INVALID_CASE_ID);
		Assert.assertEquals(err, FieldFormat.FIELD_ERROR_Paging);
		p.setPageSize(1);
		//
		err = p.checkRetrieveN(BaseBO.INVALID_CASE_ID);
		Assert.assertEquals(err, "");
	}

	@Test
	public void testCheckUpdate() {
		Shared.printTestMethodStartInfo();

		Permission permission = new Permission();
		String error = "";
		error = permission.checkUpdate(BaseBO.INVALID_CASE_ID);
		Assert.assertEquals(error, "");
	}

	@Test
	public void testCheckDelete() {
		Shared.printTestMethodStartInfo();

		Permission p = new Permission();
		String err = "";
		//
		p.setID(-99);
		err = p.checkDelete(BaseBO.INVALID_CASE_ID);
		assertEquals(err, FieldFormat.FIELD_ERROR_ID);
		p.setID(0);
		err = p.checkDelete(BaseBO.INVALID_CASE_ID);
		assertEquals(err, FieldFormat.FIELD_ERROR_ID);
		//
		p.setID(1);
		err = p.checkDelete(BaseBO.INVALID_CASE_ID);
		Assert.assertEquals(err, "");
	}
}
