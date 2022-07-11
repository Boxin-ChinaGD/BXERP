package com.bx.erp.model;

import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import com.bx.erp.action.bo.BaseBO;
import com.bx.erp.test.Shared;
import com.bx.erp.util.FieldFormat;

public class BxStaffTest {

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

		BxStaff bxStaff = new BxStaff();
		String error = "";
		error = bxStaff.checkCreate(BaseBO.INVALID_CASE_ID);
		Assert.assertEquals(error, "");
	}

	@Test
	public void testCheckRetrieve1() {
		Shared.printTestMethodStartInfo();

		BxStaff bxStaff = new BxStaff();
		String error = "";

		bxStaff.setID(-99);
		error = bxStaff.checkRetrieve1(BaseBO.INVALID_CASE_ID);
		Assert.assertEquals(error, FieldFormat.FIELD_ERROR_ID);
		bxStaff.setID(0);
		//
		bxStaff.setMobile("446554");
		error = bxStaff.checkRetrieve1(BaseBO.INVALID_CASE_ID);
		Assert.assertEquals(error, BxStaff.FIELD_ERROR_Mobile);
		bxStaff.setMobile("13611231212");
		//
		error = bxStaff.checkRetrieve1(BaseBO.INVALID_CASE_ID);
		Assert.assertEquals(error, "");
	}

	@Test
	public void testCheckRetrieveN() {
		Shared.printTestMethodStartInfo();

		BxStaff bxStaff = new BxStaff();
		String error = "";

		bxStaff.setPageIndex(-88);
		error = bxStaff.checkRetrieveN(BaseBO.INVALID_CASE_ID);
		Assert.assertEquals(error, FieldFormat.FIELD_ERROR_Paging);
		bxStaff.setPageIndex(0);
		error = bxStaff.checkRetrieveN(BaseBO.INVALID_CASE_ID);
		Assert.assertEquals(error, FieldFormat.FIELD_ERROR_Paging);
		bxStaff.setPageIndex(1);
		//
		bxStaff.setPageSize(-88);
		error = bxStaff.checkRetrieveN(BaseBO.INVALID_CASE_ID);
		Assert.assertEquals(error, FieldFormat.FIELD_ERROR_Paging);
		bxStaff.setPageSize(0);
		Assert.assertEquals(error, FieldFormat.FIELD_ERROR_Paging);
		bxStaff.setPageSize(1);
		//
		error = bxStaff.checkRetrieveN(BaseBO.INVALID_CASE_ID);
		Assert.assertEquals(error, "");
	}

	@Test
	public void testCheckUpdate() {
		Shared.printTestMethodStartInfo();

		BxStaff bxStaff = new BxStaff();
		String error = "";
		error = bxStaff.checkUpdate(BaseBO.INVALID_CASE_ID);
		Assert.assertEquals(error, "");
	}

	@Test
	public void testCheckDelete() {
		Shared.printTestMethodStartInfo();

		BxStaff bxStaff = new BxStaff();
		String error = "";
		error = bxStaff.checkDelete(BaseBO.INVALID_CASE_ID);
		Assert.assertEquals(error, "");
	}
}
