package com.bx.erp.test;


import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import com.bx.erp.model.BaseModel;
import com.bx.erp.model.BonusRule;

public class BonusRuleMapperTest extends BaseMapperTest {
	
	/*sp判断bForceDelete=1才会删除对象，否则不给删除 */
	public static final int forceDelete = 1;
	
	@BeforeClass
	public void setup() {
		super.setUp();
	}

	@AfterClass
	public void tearDown() {
		super.tearDown();
	}

	@Test
	public void createTest1() throws Exception {
		Shared.printTestMethodStartInfo();
		//
		Shared.caseLog("Case1:正常添加");
		//
		BonusRule bonusRule = BaseBonusRuleTest.DataInput.getBonusRule(1); // ...
		BonusRule bonusRuleCreate = BaseBonusRuleTest.createViaMapper(bonusRule);
		// 
		bonusRuleCreate.setbForceDelete(forceDelete);
		BaseBonusRuleTest.deleteViaMapper(bonusRuleCreate, Shared.DBName_Test);
	}

	// TODO 现在不提供action创建积分规则的,这个只是测试用,测试可以去掉
//	@Test
//	public void createTest2() throws Exception {
//		Shared.printTestMethodStartInfo();
//		//
//		Shared.caseLog("Case2:重复创建");
//		//
//		BonusRule bonusRule = BaseBonusRuleTest.DataInput.getBonusRule(1); // ...
//		BonusRule bonusRuleCreate = BaseBonusRuleTest.createViaMapper(bonusRule);
//		// 重复创建
//		Map<String, Object> params = bonusRule.getCreateParam(BaseBO.INVALID_CASE_ID, bonusRule);
//		//
//		DataSourceContextHolder.setDbName(Shared.DBName_Test);
//		BonusRule bonusRuleCreate2 = (BonusRule) bonusRuleMapper.create(params);
//		Assert.assertTrue(bonusRuleCreate2 == null && EnumErrorCode.values()[Integer.parseInt(params.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())] == EnumErrorCode.EC_BusinessLogicNotDefined,
//				"创建对象失败。param=" + params + "\n\r bonusRule=" + bonusRule);
//		//
//		BaseBonusRuleTest.deleteViaMapper(bonusRuleCreate);
//	}
	
	@Test
	public void updateTest1() throws Exception {
		Shared.printTestMethodStartInfo();
		//
		Shared.caseLog("Case1:正常添加");
		//
		BonusRule bonusRule = BaseBonusRuleTest.DataInput.getBonusRule(1);
		BonusRule bonusRuleCreate = BaseBonusRuleTest.createViaMapper(bonusRule);
		// 
		BonusRule bonusRuleToUpdate = (BonusRule) bonusRuleCreate.clone();
		bonusRuleToUpdate.setAmountUnit(200);
		bonusRuleToUpdate.setIncreaseBonus(200);
		bonusRuleToUpdate.setMaxIncreaseBonus(200);
		bonusRuleToUpdate.setInitIncreaseBonus(200);
		BaseBonusRuleTest.updateViaMapper(bonusRuleToUpdate);
		//
		bonusRuleCreate.setbForceDelete(forceDelete);
		BaseBonusRuleTest.deleteViaMapper(bonusRuleCreate, Shared.DBName_Test);
	}
	
	@Test
	public void retrieve1Test1() throws Exception {
		Shared.printTestMethodStartInfo();
		//
		Shared.caseLog("Case1:正常查询");
		//
		BonusRule bonusRule = BaseBonusRuleTest.DataInput.getBonusRule(1); // ...
		BonusRule bonusRuleCreate = BaseBonusRuleTest.createViaMapper(bonusRule);
		// 
		BaseModel bonusRuleRetrieve1 = BaseBonusRuleTest.retrieve1ViaMapper(bonusRuleCreate, Shared.DBName_Test);
		Assert.assertTrue(bonusRuleRetrieve1 != null);
		//
		bonusRuleCreate.setbForceDelete(forceDelete);
		BaseBonusRuleTest.deleteViaMapper(bonusRuleCreate, Shared.DBName_Test);
	}
	
	@Test
	public void deleteTest1() throws Exception {
		Shared.printTestMethodStartInfo();
		//
		Shared.caseLog("Case1:正常删除");
		//
		BonusRule bonusRule = BaseBonusRuleTest.DataInput.getBonusRule(1); // ...
		BonusRule bonusRuleCreate = BaseBonusRuleTest.createViaMapper(bonusRule);
		//
		bonusRuleCreate.setbForceDelete(forceDelete);
		BaseBonusRuleTest.deleteViaMapper(bonusRuleCreate, Shared.DBName_Test);
	}
	
//	@Test
//	public void deleteTest2() throws Exception {
//		Shared.printTestMethodStartInfo();
//		//
//		Shared.caseLog("Case2:有会员拥有积分，不能删除积分规则");
//		//
//		BonusRule bonusRule = BaseBonusRuleTest.DataInput.getBonusRule(1); // ...
//		BonusRule bonusRuleCreate = BaseBonusRuleTest.createViaMapper(bonusRule);
//		//创建会员
//		Vip vip = BaseVipTest.DataInput.getVip();
//		
//		BaseBonusRuleTest.deleteViaMapper(bonusRuleCreate);
//	}
}
