package com.bx.erp.test;

import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import com.bx.erp.model.BonusRule;
import com.bx.erp.model.ErrorInfo.EnumErrorCode;

public class BonusRuleActionTest extends BaseActionTest {
	@BeforeClass
	public void setup() {
		super.setUp();
	}

	@AfterClass
	public void tearDown() {
		super.tearDown();
	}

	@Test
	public void testUpdateEx1() throws Exception {
		Shared.printTestMethodStartInfo();

		Shared.caseLog("case1:修改积分规则");
		// 创建积分规则
		BonusRule bonusRule = BaseBonusRuleTest.DataInput.getBonusRule(1);
		BonusRule bonusRuleCreate = BaseBonusRuleTest.createViaMapper(bonusRule);
		// 修改会员卡
		BonusRule bonusRuleToUpdate = (BonusRule) bonusRuleCreate.clone();
		bonusRuleToUpdate.setAmountUnit(200);
		bonusRuleToUpdate.setIncreaseBonus(200);
		bonusRuleToUpdate.setMaxIncreaseBonus(200);
		bonusRuleToUpdate.setInitIncreaseBonus(200);
		//
		//
		BaseBonusRuleTest.updateViaAction(mvc, sessionBoss, bonusRuleToUpdate, EnumErrorCode.EC_NoError);
		//
		bonusRuleCreate.setbForceDelete(BonusRuleMapperTest.forceDelete);
		BaseBonusRuleTest.deleteViaMapper(bonusRuleCreate, Shared.DBName_Test);
	}
	
	@Test
	public void testUpdateEx2() throws Exception {
		Shared.printTestMethodStartInfo();

		Shared.caseLog("case2:获取积分金额小于等于0");
		// 创建积分规则
		BonusRule bonusRule = BaseBonusRuleTest.DataInput.getBonusRule(1);
		BonusRule bonusRuleCreate = BaseBonusRuleTest.createViaMapper(bonusRule);
		// 修改会员卡
		BonusRule bonusRuleToUpdate = (BonusRule) bonusRuleCreate.clone();
		bonusRuleToUpdate.setAmountUnit(-1);
		bonusRuleToUpdate.setIncreaseBonus(200);
		bonusRuleToUpdate.setMaxIncreaseBonus(200);
		bonusRuleToUpdate.setInitIncreaseBonus(200);
		//
		//
		BaseBonusRuleTest.updateViaAction(mvc, sessionBoss, bonusRuleToUpdate, EnumErrorCode.EC_WrongFormatForInputField);
		//
		bonusRuleCreate.setbForceDelete(BonusRuleMapperTest.forceDelete);
		BaseBonusRuleTest.deleteViaMapper(bonusRuleCreate, Shared.DBName_Test);
	}
	
	@Test
	public void testUpdateEx3() throws Exception {
		Shared.printTestMethodStartInfo();

		Shared.caseLog("case3:金额对应获取积分小于等于0");
		// 创建积分规则
		BonusRule bonusRule = BaseBonusRuleTest.DataInput.getBonusRule(1);
		BonusRule bonusRuleCreate = BaseBonusRuleTest.createViaMapper(bonusRule);
		// 修改会员卡
		BonusRule bonusRuleToUpdate = (BonusRule) bonusRuleCreate.clone();
		bonusRuleToUpdate.setAmountUnit(200);
		bonusRuleToUpdate.setIncreaseBonus(-1);
		bonusRuleToUpdate.setMaxIncreaseBonus(200);
		bonusRuleToUpdate.setInitIncreaseBonus(200);
		//
		//
		BaseBonusRuleTest.updateViaAction(mvc, sessionBoss, bonusRuleToUpdate, EnumErrorCode.EC_WrongFormatForInputField);
		//
		bonusRuleCreate.setbForceDelete(BonusRuleMapperTest.forceDelete);
		BaseBonusRuleTest.deleteViaMapper(bonusRuleCreate, Shared.DBName_Test);
	}
	
	@Test
	public void testUpdateEx4() throws Exception {
		Shared.printTestMethodStartInfo();

		Shared.caseLog("case4:单次最大获取积分小于IncreaseBonus");
		// 创建积分规则
		BonusRule bonusRule = BaseBonusRuleTest.DataInput.getBonusRule(1);
		BonusRule bonusRuleCreate = BaseBonusRuleTest.createViaMapper(bonusRule);
		// 修改会员卡
		BonusRule bonusRuleToUpdate = (BonusRule) bonusRuleCreate.clone();
		bonusRuleToUpdate.setAmountUnit(200);
		bonusRuleToUpdate.setIncreaseBonus(200);
		bonusRuleToUpdate.setMaxIncreaseBonus(-1);
		bonusRuleToUpdate.setInitIncreaseBonus(200);
		//
		//
		BaseBonusRuleTest.updateViaAction(mvc, sessionBoss, bonusRuleToUpdate, EnumErrorCode.EC_WrongFormatForInputField);
		//
		bonusRuleCreate.setbForceDelete(BonusRuleMapperTest.forceDelete);
		BaseBonusRuleTest.deleteViaMapper(bonusRuleCreate, Shared.DBName_Test);
	}
	
	@Test
	public void testUpdateEx5() throws Exception {
		Shared.printTestMethodStartInfo();

		Shared.caseLog("case5:初始积分小于0");
		// 创建积分规则
		BonusRule bonusRule = BaseBonusRuleTest.DataInput.getBonusRule(1);
		BonusRule bonusRuleCreate = BaseBonusRuleTest.createViaMapper(bonusRule);
		// 修改会员卡
		BonusRule bonusRuleToUpdate = (BonusRule) bonusRuleCreate.clone();
		bonusRuleToUpdate.setAmountUnit(200);
		bonusRuleToUpdate.setIncreaseBonus(200);
		bonusRuleToUpdate.setMaxIncreaseBonus(200);
		bonusRuleToUpdate.setInitIncreaseBonus(-1);
		//
		//
		BaseBonusRuleTest.updateViaAction(mvc, sessionBoss, bonusRuleToUpdate, EnumErrorCode.EC_WrongFormatForInputField);
		//
		bonusRuleCreate.setbForceDelete(BonusRuleMapperTest.forceDelete);
		BaseBonusRuleTest.deleteViaMapper(bonusRuleCreate, Shared.DBName_Test);
	}
}
