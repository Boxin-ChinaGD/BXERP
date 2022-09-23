package com.bx.erp.test;

import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import com.bx.erp.action.bo.BaseBO;
import com.bx.erp.model.BonusRule;

public class BonusRuleTest {
	@BeforeClass
	public void setUp() {
		Shared.printTestClassStartInfo();
	}

	@AfterClass
	public void tearDown() {
		Shared.printTestClassEndInfo();
	}
	
	@Test
	public void testCheckUpdate() throws Exception {
		Shared.printTestMethodStartInfo();

		BonusRule bonusRule = BaseBonusRuleTest.DataInput.getBonusRule(1);
		bonusRule.setID(1);
		String error = bonusRule.checkUpdate(BaseBO.INVALID_CASE_ID);
		Assert.assertEquals(error, "");
		Shared.caseLog("测试获取积分金额，必须大于0");
		bonusRule.setAmountUnit(0);
		error = bonusRule.checkUpdate(BaseBO.INVALID_CASE_ID);
		Assert.assertEquals(error, BonusRule.FIELD_ERROR_amountUnit);
		bonusRule.setAmountUnit(500);
		Shared.caseLog("测试金额对应获取积分，必须大于0");
		bonusRule.setIncreaseBonus(0);
		error = bonusRule.checkUpdate(BaseBO.INVALID_CASE_ID);
		Assert.assertEquals(error, BonusRule.FIELD_ERROR_increaseBonus);
		bonusRule.setIncreaseBonus(500);
		Shared.caseLog("测试单次最大获取积分，必须大于或等于金额对应获取积分");
		bonusRule.setMaxIncreaseBonus(300);
		error = bonusRule.checkUpdate(BaseBO.INVALID_CASE_ID);
		Assert.assertEquals(error, BonusRule.FIELD_ERROR_maxIncreaseBonus);
		bonusRule.setMaxIncreaseBonus(1000);
		Shared.caseLog("测试初始积分，必须大于0或等于0");
		bonusRule.setInitIncreaseBonus(-1);
		error = bonusRule.checkUpdate(BaseBO.INVALID_CASE_ID);
		Assert.assertEquals(error, BonusRule.FIELD_ERROR_initIncreaseBonus);
		bonusRule.setInitIncreaseBonus(0);
	}
}
