package com.bx.erp.model;

import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import com.bx.erp.action.BaseAction;
import com.bx.erp.action.bo.BaseBO;
import com.bx.erp.model.BaseModel.EnumBoolean;
import com.bx.erp.model.Role.EnumTypeRole;
import com.bx.erp.model.Staff.EnumStatusStaff;
import com.bx.erp.test.Shared;
import com.bx.erp.util.FieldFormat;

public class StaffRoleTest {

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
		
		StaffRole staffRole = new StaffRole();
		String error = "";
		error = staffRole.checkCreate(BaseBO.INVALID_CASE_ID);
		Assert.assertEquals(error, "");
	}

	@Test
	public void testCheckRetrieve1() {
		Shared.printTestMethodStartInfo();
		
		StaffRole staffRole = new StaffRole();
		staffRole.setID(1);
		staffRole.setStaffID(1);
		String error = staffRole.checkRetrieve1(BaseBO.INVALID_CASE_ID);
		Assert.assertEquals(error, "");
		//测试ID不合法，staffID为0
		staffRole.setID(BaseAction.INVALID_ID);
		staffRole.setStaffID(0);
		error = staffRole.checkRetrieve1(BaseBO.INVALID_CASE_ID);
		Assert.assertEquals(error, FieldFormat.FIELD_ERROR_ID);
		//测试ID为0，staffID不合法
		staffRole.setID(0);
		staffRole.setStaffID(BaseAction.INVALID_ID);
		error = staffRole.checkRetrieve1(BaseBO.INVALID_CASE_ID);
		Assert.assertEquals(error, StaffRole.FIELD_ERROR_staffID);
		//测试ID和staffID都为0
		staffRole.setID(0);
		staffRole.setStaffID(0);
		error = staffRole.checkRetrieve1(BaseBO.INVALID_CASE_ID);
		Assert.assertEquals(error, StaffRole.FIELD_ERROR_IDAndStaffID);
		staffRole.setID(1);
		staffRole.setStaffID(1);
	}

	@Test
	public void testCheckRetrieveN() {
		Shared.printTestMethodStartInfo();
		
		StaffRole staffRole = new StaffRole();
		staffRole.setRoleID(EnumTypeRole.ETR_Cashier.getIndex());
		staffRole.setStatus(EnumStatusStaff.ESS_Incumbent.getIndex());
		staffRole.setOperator(EnumBoolean.EB_NO.getIndex());
		staffRole.setPageIndex(1);
		staffRole.setPageSize(10);
		String error = staffRole.checkRetrieveN(BaseBO.INVALID_CASE_ID);
		Assert.assertEquals(error, "");
		//测试角色ID
		staffRole.setRoleID(BaseAction.INVALID_ID);
		error = staffRole.checkRetrieveN(BaseBO.INVALID_CASE_ID);
		Assert.assertEquals(error, "");
		staffRole.setRoleID(-2);
		error = staffRole.checkRetrieveN(BaseBO.INVALID_CASE_ID);
		Assert.assertEquals(error, StaffRole.FIELD_ERROR_roleID);
		staffRole.setRoleID(EnumTypeRole.ETR_Cashier.getIndex());
		//测试角色状态
		staffRole.setStatus(BaseAction.INVALID_STATUS);
		error = staffRole.checkRetrieveN(BaseBO.INVALID_CASE_ID);
		Assert.assertEquals(error, "");
		staffRole.setStatus(999999);
		error = staffRole.checkRetrieveN(BaseBO.INVALID_CASE_ID);
		Assert.assertEquals(error, StaffRole.FIELD_ERROR_status);
		staffRole.setStatus(-9999999);
		error = staffRole.checkRetrieveN(BaseBO.INVALID_CASE_ID);
		Assert.assertEquals(error, StaffRole.FIELD_ERROR_status);
		staffRole.setStatus(EnumStatusStaff.ESS_Incumbent.getIndex());
		//
		Shared.caseLog("测试操作员是否为0或1");
		staffRole.setOperator(-1);
		error = staffRole.checkRetrieveN(BaseBO.INVALID_CASE_ID);
		Assert.assertEquals(error, Staff.FIELD_ERROR_operator);
		staffRole.setOperator(2);
		error = staffRole.checkRetrieveN(BaseBO.INVALID_CASE_ID);
		Assert.assertEquals(error, Staff.FIELD_ERROR_operator);
		staffRole.setOperator(EnumBoolean.EB_Yes.getIndex());
		error = staffRole.checkRetrieveN(BaseBO.INVALID_CASE_ID);
		Assert.assertEquals(error, "");
		//测试PageIndex
		staffRole.setPageIndex(0);
		error = staffRole.checkRetrieveN(BaseBO.INVALID_CASE_ID);
		Assert.assertEquals(error, FieldFormat.FIELD_ERROR_Paging);
		staffRole.setPageIndex(1);
		//测试PageSize
		staffRole.setPageSize(0);
		error = staffRole.checkRetrieveN(BaseBO.INVALID_CASE_ID);
		Assert.assertEquals(error, FieldFormat.FIELD_ERROR_Paging);
		staffRole.setPageSize(10);
	}

	@Test
	public void testCheckUpdate() {
		Shared.printTestMethodStartInfo();
		
		StaffRole staffRole = new StaffRole();
		String error = "";
		error = staffRole.checkUpdate(BaseBO.INVALID_CASE_ID);
		Assert.assertEquals(error, "");
	}

	@Test
	public void testCheckDelete() {
		Shared.printTestMethodStartInfo();
		
		StaffRole staffRole = new StaffRole();
		String error = "";
		error = staffRole.checkDelete(BaseBO.INVALID_CASE_ID);
		Assert.assertEquals(error, "");
	}
}
