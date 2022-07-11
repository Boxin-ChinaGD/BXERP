package com.bx.erp.model;

import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import com.bx.erp.action.bo.BaseBO;
import com.bx.erp.model.Department;
import com.bx.erp.test.Shared;
import com.bx.erp.util.FieldFormat;


public class DepartmentTest {

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

		Department department = new Department();
		String error = "";
		error = department.checkCreate(BaseBO.INVALID_CASE_ID);
		Assert.assertEquals(error, "");
	}

	@Test
	public void testCheckRetrieve1() {
		Shared.printTestMethodStartInfo();

		Department department = new Department();
		String error = "";
		error = department.checkRetrieve1(BaseBO.INVALID_CASE_ID);
		Assert.assertEquals(error, "");
	}

	@Test
	public void testCheckRetrieveN() {
		Shared.printTestMethodStartInfo();

		Department department = new Department();
		String error = "";

		department.setPageIndex(-88);
		error = department.checkRetrieveN(BaseBO.INVALID_CASE_ID);
		Assert.assertEquals(error, FieldFormat.FIELD_ERROR_Paging);
		department.setPageIndex(0);
		error = department.checkRetrieveN(BaseBO.INVALID_CASE_ID);
		Assert.assertEquals(error, FieldFormat.FIELD_ERROR_Paging);
		department.setPageIndex(1);
		//
		department.setPageSize(-88);
		error = department.checkRetrieveN(BaseBO.INVALID_CASE_ID);
		Assert.assertEquals(error, FieldFormat.FIELD_ERROR_Paging);
		department.setPageSize(0);
		Assert.assertEquals(error, FieldFormat.FIELD_ERROR_Paging);
		department.setPageSize(1);
		//
		error = department.checkRetrieveN(BaseBO.INVALID_CASE_ID);
		Assert.assertEquals(error, "");
	}

	@Test
	public void testCheckUpdate() {
		Shared.printTestMethodStartInfo();

		Department department = new Department();
		String error = "";
		error = department.checkUpdate(BaseBO.INVALID_CASE_ID);
		Assert.assertEquals(error, "");
	}

	@Test
	public void testCheckDelete() {
		Shared.printTestMethodStartInfo();

		Department department = new Department();
		String error = "";
		error = department.checkDelete(BaseBO.INVALID_CASE_ID);
		Assert.assertEquals(error, "");
	}
}
