package com.bx.erp.model;

import org.testng.annotations.Test;

import com.bx.erp.action.BaseAction;
import com.bx.erp.action.bo.BaseBO;
import com.bx.erp.test.Shared;

import junit.framework.Assert;

public class CouponCodeTest extends BaseModelTest {
	@Test
	public void checkRetrieveN_CASE1() {
		Shared.printTestMethodStartInfo();

		Shared.caseLog("RetrieveNByVipID");

		CouponCode couponCode = new CouponCode();
		couponCode.setVipID(BaseAction.INVALID_ID);
		String err = couponCode.checkRetrieveN(BaseBO.CASE_CouponCode_retrieveNByVipID);
		Assert.assertTrue(err, CouponCode.FIELD_ERROR_VipID.equals(err));
		couponCode.setVipID(Shared.BIG_ID);

		couponCode.setSubStatus(BaseAction.INVALID_ID);
		err = couponCode.checkRetrieveN(BaseBO.CASE_CouponCode_retrieveNByVipID);
		Assert.assertTrue(err, CouponCode.FIELD_ERROR_SubStatus.equals(err));
		//
		couponCode.setSubStatus(Shared.BIG_ID);
		err = couponCode.checkRetrieveN(BaseBO.CASE_CouponCode_retrieveNByVipID);
		Assert.assertTrue(err, CouponCode.FIELD_ERROR_SubStatus.equals(err));
		//
		couponCode.setSubStatus(CouponCode.EnumSubStatusCouponCode.ESSC_Consumed.getIndex());
		err = couponCode.checkRetrieveN(BaseBO.CASE_CouponCode_retrieveNByVipID);
		Assert.assertTrue(err, "".equals(err));
	}
	
	@Test
	public void checkRetrieveN_CASE2() {
		Shared.printTestMethodStartInfo();
		
		Shared.caseLog("RetrieveNTotalByVipID");
		CouponCode couponCode = new CouponCode();
		couponCode.setVipID(BaseAction.INVALID_ID);
		String err = couponCode.checkRetrieveN(BaseBO.CASE_CouponCode_retrieveNTotalByVipID);
		Assert.assertTrue(err, CouponCode.FIELD_ERROR_VipID.equals(err));
		
		couponCode.setVipID(Shared.BIG_ID);
		err = couponCode.checkRetrieveN(BaseBO.CASE_CouponCode_retrieveNTotalByVipID);
		Assert.assertTrue(err, "".equals(err));

	}
}
