package com.bx.erp.test.staff;

import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import com.bx.erp.model.StaffBelonging;
import com.bx.erp.test.BaseMapperTest;
import com.bx.erp.test.BaseStaffTest;
import com.bx.erp.test.Shared;
import com.bx.erp.util.DataSourceContextHolder;

public class StaffBelongingMapperTest extends BaseMapperTest {
	
	@BeforeClass
	public void setup() {
		super.setUp();
	}

	@AfterClass
	public void tearDown() {
		super.tearDown();
	}
	
	@Test
	public void retrieveNTest() throws Exception {
		Shared.printTestMethodStartInfo();
		System.out.println("\n------------------------ RetrieveN StaffBelonging Test ------------------------");
		StaffBelonging staffBelonging = new StaffBelonging();
		//
		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		BaseStaffTest.retrieveNStaffBelonging(staffBelonging);
	}
}