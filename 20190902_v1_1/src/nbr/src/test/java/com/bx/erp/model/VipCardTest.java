package com.bx.erp.model;

import java.util.Date;

import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import com.bx.erp.action.BaseAction;
import com.bx.erp.action.bo.BaseBO;
import com.bx.erp.test.BaseVipCardTest;
import com.bx.erp.test.Shared;
import com.bx.erp.util.FieldFormat;

public class VipCardTest {
	@BeforeClass
	public void setUp() {
		Shared.printTestClassStartInfo();
	}

	@AfterClass
	public void tearDown() {
		Shared.printTestClassEndInfo();
	}

	@Test
	public void testCheckRetrieve1() {
		Shared.printTestMethodStartInfo();

		VipCard vipCard = new VipCard();
		vipCard.setID(1);
		String error = vipCard.checkRetrieve1(BaseBO.INVALID_CASE_ID);
		Assert.assertEquals(error, "");
		Shared.caseLog("测试ID");
		vipCard.setID(BaseAction.INVALID_ID);
		error = vipCard.checkRetrieve1(BaseBO.INVALID_CASE_ID);
		Assert.assertEquals(error, FieldFormat.FIELD_ERROR_ID);
		vipCard.setID(1);
	}

	@Test
	public void testCheckUpdate() throws Exception {
		Shared.printTestMethodStartInfo();

		VipCard vipCard = BaseVipCardTest.DataInput.getVipCard();
		vipCard.setID(1);
		String error = vipCard.checkUpdate(BaseBO.INVALID_CASE_ID);
		Assert.assertEquals(error, "");
		Shared.caseLog("测试ID");
		vipCard.setID(BaseAction.INVALID_ID);
		error = vipCard.checkUpdate(BaseBO.INVALID_CASE_ID);
		Assert.assertEquals(error, FieldFormat.FIELD_ERROR_ID);
		vipCard.setID(1);
		//
		Shared.caseLog("会员卡积分清零规则天数和积分清零规则日期都非法");
		vipCard.setClearBonusDay(-500);
		vipCard.setClearBonusDatetime(null);
		error = vipCard.checkUpdate(BaseBO.INVALID_CASE_ID);
		Assert.assertEquals(error, VipCard.FIELD_ERROR_clearBonusDay_clearBonusDatetime);
		//
		Shared.caseLog("会员卡积分清零规则天数合法，积分清零规则日期非法");
		vipCard.setClearBonusDay(500);
		vipCard.setClearBonusDatetime(null);
		error = vipCard.checkUpdate(BaseBO.INVALID_CASE_ID);
		Assert.assertEquals(error, "");
		//
		Shared.caseLog("会员卡积分清零规则天数非法，积分清零规则日期合法");
		vipCard.setClearBonusDay(0);
		vipCard.setClearBonusDatetime(new Date());
		error = vipCard.checkUpdate(BaseBO.INVALID_CASE_ID);
		Assert.assertEquals(error, "");
		//
		Shared.caseLog("会员卡背景颜色格式不正确Case1");
		vipCard.setBackgroundColor("255.255.255");
		error = vipCard.checkUpdate(BaseBO.INVALID_CASE_ID);
		Assert.assertEquals(error, VipCard.FIELD_ERROR_backgroundColor);
		//
		Shared.caseLog("会员卡背景颜色格式不正确Case2");
		vipCard.setBackgroundColor("255,255;255,255");
		error = vipCard.checkUpdate(BaseBO.INVALID_CASE_ID);
		Assert.assertEquals(error, VipCard.FIELD_ERROR_backgroundColor);
		//
		Shared.caseLog("会员卡背景颜色格式不正确Case3");
		vipCard.setBackgroundColor("255,255,255;255,255,256");
		error = vipCard.checkUpdate(BaseBO.INVALID_CASE_ID);
		Assert.assertEquals(error, VipCard.FIELD_ERROR_backgroundColor);
		//
		Shared.caseLog("会员卡背景颜色格式不正确Case4");
		vipCard.setBackgroundColor("255,255,255;255,255,-1");
		error = vipCard.checkUpdate(BaseBO.INVALID_CASE_ID);
		Assert.assertEquals(error, VipCard.FIELD_ERROR_backgroundColor);
	}
}
