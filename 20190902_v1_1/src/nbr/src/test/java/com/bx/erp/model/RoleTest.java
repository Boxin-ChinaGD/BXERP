package com.bx.erp.model;

import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import com.bx.erp.action.BaseAction;
import com.bx.erp.action.bo.BaseBO;
import com.bx.erp.model.Role;
import com.bx.erp.test.BaseTestNGSpringContextTest;
import com.bx.erp.test.Shared;
import com.bx.erp.util.FieldFormat;

public class RoleTest extends BaseTestNGSpringContextTest{
	
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
		
		Shared.caseLog("testCheckCreate");
		Role role1 = new Role();
		role1.setName("这是角色");
		String error1 = role1.checkCreate(BaseBO.INVALID_CASE_ID);
		Assert.assertEquals(error1, "");
		//测试name
		role1.setName("");
		error1 = role1.checkCreate(BaseBO.INVALID_CASE_ID);
		Assert.assertEquals(error1, Role.FIELD_ERROR_name);
		role1.setName("12345678901234567890aa");
		error1 = role1.checkCreate(BaseBO.INVALID_CASE_ID);
		Assert.assertEquals(error1, Role.FIELD_ERROR_name);
		
	}
	
	
	@Test
	public void testCheckUpdate() {
		Shared.printTestMethodStartInfo();
		
		Shared.caseLog("testCheckUpdate");
		Role role1 = new Role();
		role1.setID(1);
		role1.setName("这是角色");
		String error1 = role1.checkUpdate(BaseBO.INVALID_CASE_ID);
		Assert.assertEquals(error1, "");
		//测试ID
		role1.setID(BaseAction.INVALID_ID);
		error1 = role1.checkUpdate(BaseBO.INVALID_CASE_ID);
		Assert.assertEquals(error1, FieldFormat.FIELD_ERROR_ID);
		role1.setID(2);
		//测试name
		role1.setName("");
		error1 = role1.checkUpdate(BaseBO.INVALID_CASE_ID);
		Assert.assertEquals(error1, Role.FIELD_ERROR_name);
		role1.setName("12345678901234567890aa");
		error1 = role1.checkUpdate(BaseBO.INVALID_CASE_ID);
		Assert.assertEquals(error1, Role.FIELD_ERROR_name);
	}
	
	@Test
	public void testCheckRetrieveN_CASE1() {
		Shared.printTestMethodStartInfo();
		
		Shared.caseLog("testCheckRetrieveN");
		Role role1 = new Role();
		role1.setID(1);
		role1.setPageIndex(1);
		role1.setPageSize(10);
		String error1 = role1.checkRetrieveN(BaseBO.CASE_Role_RetrieveAlsoStaff);
		Assert.assertEquals(error1, "");
		//测试ID
		role1.setID(BaseAction.INVALID_ID);
		error1 = role1.checkRetrieveN(BaseBO.CASE_Role_RetrieveAlsoStaff);
		Assert.assertEquals(error1, FieldFormat.FIELD_ERROR_ID);
		//测试PageIndex
		role1.setPageIndex(0);
		error1 = role1.checkRetrieveN(BaseBO.CASE_Role_RetrieveAlsoStaff);
		Assert.assertEquals(error1, FieldFormat.FIELD_ERROR_Paging);
		role1.setPageIndex(2);
		//测试PageSize
		role1.setPageSize(0);
		error1 = role1.checkRetrieveN(BaseBO.CASE_Role_RetrieveAlsoStaff);
		Assert.assertEquals(error1, FieldFormat.FIELD_ERROR_Paging);
		role1.setPageSize(5);
	}
	
	@Test
	public void testCheckRetrieveN_CASE2() {
		Shared.printTestMethodStartInfo();
		
		Shared.caseLog("testCheckRetrieveN");
		Role role1 = new Role();
		role1.setName("这是角色");
		role1.setPageIndex(1);
		role1.setPageSize(10);
		String error1 = role1.checkRetrieveN(BaseBO.INVALID_CASE_ID);
		Assert.assertEquals(error1, "");
		//测试name
		role1.setName("");
		error1 = role1.checkRetrieveN(BaseBO.INVALID_CASE_ID);
		Assert.assertEquals(error1, "");
		role1.setName("12345678901234567890aa");
		error1 = role1.checkRetrieveN(BaseBO.INVALID_CASE_ID);
		Assert.assertEquals(error1, Role.FIELD_ERROR_name);
		//测试PageIndex
		role1.setPageIndex(0);
		error1 = role1.checkRetrieveN(BaseBO.INVALID_CASE_ID);
		Assert.assertEquals(error1, FieldFormat.FIELD_ERROR_Paging);
		role1.setPageIndex(2);
		//测试PageSize
		role1.setPageSize(0);
		error1 = role1.checkRetrieveN(BaseBO.INVALID_CASE_ID);
		Assert.assertEquals(error1, FieldFormat.FIELD_ERROR_Paging);
		role1.setPageSize(5);
	}
	
	@Test
	public void testCheckDelete() {
		Shared.printTestMethodStartInfo();
		
		Shared.caseLog("testCheckDelete");
		Role role1 = new Role();
		role1.setID(1);
		role1.setbForceDelete(1);
		String error1 = role1.checkDelete(BaseBO.INVALID_CASE_ID);
		Assert.assertEquals(error1, "");
		//测试ID
		role1.setID(BaseAction.INVALID_ID);
		error1 = role1.checkDelete(BaseBO.INVALID_CASE_ID);
		Assert.assertEquals(error1, FieldFormat.FIELD_ERROR_ID);
		role1.setID(2);
		//测试int1
		role1.setbForceDelete(0);
		error1 = role1.checkDelete(BaseBO.INVALID_CASE_ID);
		Assert.assertEquals(error1, "");
		role1.setbForceDelete(-1);
		error1 = role1.checkDelete(BaseBO.INVALID_CASE_ID);
		Assert.assertEquals(error1, Role.FEILD_ERROR_bForceDelete);
	}
	
	@Test
	public void testCheckRetrieve1() {
		Shared.printTestMethodStartInfo();
		
		Role role = new Role();
		String error = role.checkRetrieve1(BaseBO.INVALID_CASE_ID);
		Assert.assertEquals(error, "");
	}
}
