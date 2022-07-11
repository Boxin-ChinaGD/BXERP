package com.bx.erp.action.bo;

import static org.testng.Assert.assertTrue;

import org.springframework.test.context.web.WebAppConfiguration;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import com.bx.erp.test.BaseActionTest;
import com.bx.erp.test.Shared;
import com.bx.erp.util.DataSourceContextHolder;

@WebAppConfiguration
public class BaseBOTest extends BaseActionTest {

	@BeforeClass
	public void setup() {
		super.setUp();
	}

	@AfterClass
	public void tearDown() {
		super.tearDown();
	}

	@Test
	public void checkStaffPermissionTest() {
		int staffID1 = 0;
		int staffID2 = 2;

		String SP_VIP_Create = "SP_VIP_Create";

		// CASE1: 没有该员工ID，返回false
		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		assertTrue(!vipBO.checkStaffPermission(staffID1, SP_VIP_Create));

		// CASE2: 员工1为副店长.有该权限，返回true
		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		assertTrue(vipBO.checkStaffPermission(staffID2, SP_VIP_Create));
	}

}
