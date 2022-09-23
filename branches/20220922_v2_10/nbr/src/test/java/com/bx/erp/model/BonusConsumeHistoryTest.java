package com.bx.erp.model;

import java.util.HashMap;
import java.util.Map;

import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import com.bx.erp.action.BaseAction;
import com.bx.erp.action.bo.BaseBO;
import com.bx.erp.test.Shared;

public class BonusConsumeHistoryTest {
	
	@BeforeClass
	public void setUp() {
		Shared.printTestClassStartInfo();
	}

	@AfterClass
	public void tearDown() {
		Shared.printTestClassEndInfo();
	}
	
	@Test
	public void testCheckRetrieveN() throws CloneNotSupportedException {
		Shared.printTestMethodStartInfo();
		// 
		BonusConsumeHistory bonusConsumeHistory = new BonusConsumeHistory();
		bonusConsumeHistory.setVipID(BaseAction.INVALID_ID);
		bonusConsumeHistory.setVipMobile("");
		bonusConsumeHistory.setVipName("");
		String errorMsg = bonusConsumeHistory.checkRetrieveN(BaseAction.INVALID_ID);
		Assert.assertEquals(errorMsg, "");
		
		// vipID
		bonusConsumeHistory.setVipID(-2);
		errorMsg = bonusConsumeHistory.checkRetrieveN(BaseAction.INVALID_ID);
		Assert.assertEquals(errorMsg, BonusConsumeHistory.FIELD_ERROR_vipID);
		bonusConsumeHistory.setVipID(BaseAction.INVALID_ID);
		
		// vipMobile
		bonusConsumeHistory.setVipMobile("138");
		errorMsg = bonusConsumeHistory.checkRetrieveN(BaseAction.INVALID_ID);
		Assert.assertEquals(errorMsg, BonusConsumeHistory.FIELD_ERROR_vipMobile);
		bonusConsumeHistory.setVipID(BaseAction.INVALID_ID);
		// 
		bonusConsumeHistory.setVipMobile("138888888888");
		errorMsg = bonusConsumeHistory.checkRetrieveN(BaseAction.INVALID_ID);
		Assert.assertEquals(errorMsg, BonusConsumeHistory.FIELD_ERROR_vipMobile);
		bonusConsumeHistory.setVipID(BaseAction.INVALID_ID);
		// 
		bonusConsumeHistory.setVipMobile("13as在");
		errorMsg = bonusConsumeHistory.checkRetrieveN(BaseAction.INVALID_ID);
		Assert.assertEquals(errorMsg, BonusConsumeHistory.FIELD_ERROR_vipMobile);
		bonusConsumeHistory.setVipID(BaseAction.INVALID_ID);
		// 
		bonusConsumeHistory.setVipMobile("");
		errorMsg = bonusConsumeHistory.checkRetrieveN(BaseAction.INVALID_ID);
		Assert.assertEquals(errorMsg, "");
		bonusConsumeHistory.setVipID(BaseAction.INVALID_ID);
		//
		bonusConsumeHistory.setVipMobile(null);
		errorMsg = bonusConsumeHistory.checkRetrieveN(BaseAction.INVALID_ID);
		Assert.assertEquals(errorMsg, "");
		bonusConsumeHistory.setVipID(BaseAction.INVALID_ID);
		
		// vipName
		bonusConsumeHistory.setVipName("一");;
		errorMsg = bonusConsumeHistory.checkRetrieveN(BaseAction.INVALID_ID);
		Assert.assertEquals(errorMsg, BonusConsumeHistory.FIELD_ERROR_vipName);
		bonusConsumeHistory.setVipID(BaseAction.INVALID_ID);
		//
		bonusConsumeHistory.setVipName(String.format("%0" + (Vip.MAX_LENGTH_WxNickName + 1) + "d", System.currentTimeMillis()));
		errorMsg = bonusConsumeHistory.checkRetrieveN(BaseAction.INVALID_ID);
		Assert.assertEquals(errorMsg, BonusConsumeHistory.FIELD_ERROR_vipName);
		bonusConsumeHistory.setVipID(BaseAction.INVALID_ID);
		//
		bonusConsumeHistory.setVipName("");
		errorMsg = bonusConsumeHistory.checkRetrieveN(BaseAction.INVALID_ID);
		Assert.assertEquals(errorMsg, "");
		bonusConsumeHistory.setVipID(BaseAction.INVALID_ID);
		//
		bonusConsumeHistory.setVipName(null);
		errorMsg = bonusConsumeHistory.checkRetrieveN(BaseAction.INVALID_ID);
		Assert.assertEquals(errorMsg, "");
		bonusConsumeHistory.setVipID(BaseAction.INVALID_ID);
	}
	
	@Test
	public void testGetRetrieveNParam() {
		Shared.printTestMethodStartInfo();
		// if vipID大于0，mobile、name就为空串
		// else if mobile不为空串、vipID就为-1，name就为空串
		// else if name不为空串、vipID就为-1，mobile就为空串
		BonusConsumeHistory bonusConsumeHistory = new BonusConsumeHistory();
		int vipID = 1;
		bonusConsumeHistory.setVipID(vipID);
		bonusConsumeHistory.setVipMobile("13888888888");
		bonusConsumeHistory.setVipName("fdsa的");
		Map<String, Object> retrieveNParam = new HashMap<String, Object>();
		retrieveNParam = bonusConsumeHistory.getRetrieveNParam(BaseBO.INVALID_CASE_ID, bonusConsumeHistory);
		Assert.assertEquals(vipID, Integer.parseInt(retrieveNParam.get(BonusConsumeHistory.field.getFIELD_NAME_vipID()).toString()));
		Assert.assertEquals("", retrieveNParam.get(BonusConsumeHistory.field.getFIELD_NAME_vipMobile()).toString());
		Assert.assertEquals("", retrieveNParam.get(BonusConsumeHistory.field.getFIELD_NAME_vipName()).toString());
		// mobile
		bonusConsumeHistory.setVipID(BaseAction.INVALID_ID);
		String mobile = "13888888888";
		bonusConsumeHistory.setVipMobile(mobile);
		bonusConsumeHistory.setVipName("fdsa的");
		retrieveNParam = bonusConsumeHistory.getRetrieveNParam(BaseBO.INVALID_CASE_ID, bonusConsumeHistory);
		Assert.assertEquals(BaseAction.INVALID_ID, Integer.parseInt(retrieveNParam.get(BonusConsumeHistory.field.getFIELD_NAME_vipID()).toString()));
		Assert.assertEquals(mobile, retrieveNParam.get(BonusConsumeHistory.field.getFIELD_NAME_vipMobile()).toString());
		Assert.assertEquals("", retrieveNParam.get(BonusConsumeHistory.field.getFIELD_NAME_vipName()).toString());
		// name
		bonusConsumeHistory.setVipID(BaseAction.INVALID_ID);
		bonusConsumeHistory.setVipMobile("");
		String name = "fdsa的";
		bonusConsumeHistory.setVipName(name);
		retrieveNParam = bonusConsumeHistory.getRetrieveNParam(BaseBO.INVALID_CASE_ID, bonusConsumeHistory);
		Assert.assertEquals(BaseAction.INVALID_ID, Integer.parseInt(retrieveNParam.get(BonusConsumeHistory.field.getFIELD_NAME_vipID()).toString()));
		Assert.assertEquals("", retrieveNParam.get(BonusConsumeHistory.field.getFIELD_NAME_vipMobile()).toString());
		Assert.assertEquals(name, retrieveNParam.get(BonusConsumeHistory.field.getFIELD_NAME_vipName()).toString());
	}
}
