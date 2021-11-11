package com.bx.erp.model;

import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import com.bx.erp.action.BaseAction;
import com.bx.erp.test.Shared;

public class CouponScopeTest {

	@BeforeClass
	public void setUp() {
		Shared.printTestClassStartInfo();
	}

	@AfterClass
	public void tearDown() {
		Shared.printTestClassEndInfo();
	}
	
	@Test
	public void testCheckPageSize() {
		Shared.printTestMethodStartInfo();
		
		//
		CouponScope couponScope = new CouponScope();
		couponScope.setPageSize(BaseAction.PAGE_SIZE);
		Assert.assertTrue(couponScope.checkPageSize(couponScope));
		//
		couponScope.setPageSize(BaseAction.PAGE_SIZE_MAX);
		Assert.assertTrue(couponScope.checkPageSize(couponScope));
		//
		couponScope.setPageSize(BaseAction.PAGE_SIZE_Infinite);
		Assert.assertTrue(couponScope.checkPageSize(couponScope));
		// 
		couponScope.setPageSize(-1);
		Assert.assertTrue(couponScope.checkPageSize(couponScope));
	}
}
