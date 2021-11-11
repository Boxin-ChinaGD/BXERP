//package com.bx.erp.model.wx.card;
//
//import org.testng.annotations.AfterClass;
//import org.testng.annotations.BeforeClass;
//import org.testng.annotations.Test;
//
//import com.bx.erp.action.bo.BaseBO;
//import com.bx.erp.model.BaseModelTest;
//import com.bx.erp.model.wx.BaseWxModel.EnumCardAndCouponStatus;
//import com.bx.erp.model.wx.BaseWxModel.EnumWxCardAndCouponType;
//import com.bx.erp.test.Shared;
//
//import junit.framework.Assert;
//
//public class WxCardTest extends BaseModelTest{
//	
//	
//	@BeforeClass
//	public void setUp() {
//		Shared.printTestClassStartInfo();
//	}
//
//	@AfterClass
//	public void tearDown() {
//		Shared.printTestClassEndInfo();
//	}
//	
//	@Test
//	public void testCheckCreate1() {
//		Shared.printTestMethodStartInfo();
//		Shared.caseLog("case1: 微信会员卡类型的检查");
//		
//		WxCard wxCard = new WxCard();
//		wxCard.setCard_type(EnumWxCardAndCouponType.EWCACT_MEMBER_CARD.getName());
//		wxCard.setStatus(EnumCardAndCouponStatus.EWCACS_CARD_STATUS_VERIFY_OK.getName());
//		String error = "";
//		error = wxCard.checkCreate(BaseBO.INVALID_CASE_ID);
//		Assert.assertEquals(error, "");
//		//
//		wxCard.setCard_type("xxx");
//		error = wxCard.checkCreate(BaseBO.INVALID_CASE_ID);
//		Assert.assertEquals(WxCard.FIELD_ERROR_cardType, error);
//		// 
//	}
//	
//	@Test
//	public void testCheckCreate2() {
//		Shared.printTestMethodStartInfo();
//		Shared.caseLog("case2: 微信会员卡状态的检查");
//		
//		WxCard wxCard = new WxCard();
//		wxCard.setCard_type(EnumWxCardAndCouponType.EWCACT_MEMBER_CARD.getName());
//		wxCard.setStatus(EnumCardAndCouponStatus.EWCACS_CARD_STATUS_VERIFY_OK.getName());
//		String error = "";
//		error = wxCard.checkCreate(BaseBO.INVALID_CASE_ID);
//		Assert.assertEquals(error, "");
//		//
//		wxCard.setStatus("xxx");
//		error = wxCard.checkCreate(BaseBO.INVALID_CASE_ID);
//		Assert.assertEquals(WxCard.FIELD_ERROR_status, error);
//		// 
//	}
//	
//	
//}
