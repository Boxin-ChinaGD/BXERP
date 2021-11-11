package com.bx.erp.test;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.Map;

import javax.servlet.http.HttpSession;

import org.junit.AfterClass;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import com.bx.erp.action.BaseAction;
import com.bx.erp.action.bo.BaseBO;
import com.bx.erp.model.ErrorInfo.EnumErrorCode;
import com.bx.erp.util.DataSourceContextHolder;
import com.bx.erp.model.BaseModel;
import com.bx.erp.model.BonusRule;

public class BaseBonusRuleTest extends BaseMapperTest {
	@BeforeClass
	public void setup() {
	}

	@AfterClass
	public void tearDown() {
	}
	
	public static final int DEFAULT_BonusRule_ID = 1; 

	public static class DataInput {
		private static BonusRule bonusRuleInput = null;

		public static final BonusRule getBonusRule(int vipID) throws CloneNotSupportedException {
			bonusRuleInput = new BonusRule();
			bonusRuleInput.setVipCardID(vipID);
			bonusRuleInput.setAmountUnit(1000);
			bonusRuleInput.setIncreaseBonus(1);
			bonusRuleInput.setMaxIncreaseBonus(100);
			bonusRuleInput.setInitIncreaseBonus(0);

			return (BonusRule) bonusRuleInput.clone();
		}

	}

	public static BonusRule createViaMapper(BonusRule bonusRule) {
		Map<String, Object> params = bonusRule.getCreateParam(BaseBO.INVALID_CASE_ID, bonusRule);
		//
		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		BonusRule bonusRuleCreate = (BonusRule) bonusRuleMapper.create(params);
		Assert.assertTrue(bonusRuleCreate != null && EnumErrorCode.values()[Integer.parseInt(params.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())] == EnumErrorCode.EC_NoError, "创建对象失败。param=" + params + "\n\r bonusRule=" + bonusRule);
		//
		bonusRule.setIgnoreIDInComparision(true);
		if (bonusRule.compareTo(bonusRuleCreate) != 0) {
			Assert.assertTrue(false, "创建的对象的字段与DB读出的不相等");
		}
		String error1 = bonusRuleCreate.checkCreate(BaseBO.INVALID_CASE_ID);
		Assert.assertEquals(error1, "");
		//
		return bonusRuleCreate;
	}
	
	public static BonusRule updateViaMapper(BonusRule bonusRule) {
		Map<String, Object> params = bonusRule.getUpdateParam(BaseBO.INVALID_CASE_ID, bonusRule);
		//
		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		BonusRule bonusRuleUpdate = (BonusRule) bonusRuleMapper.update(params);
		Assert.assertTrue(bonusRuleUpdate != null && EnumErrorCode.values()[Integer.parseInt(params.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())] == EnumErrorCode.EC_NoError, "修改对象失败。param=" + params + "\n\r bonusRule=" + bonusRule);
		//
		bonusRule.setIgnoreIDInComparision(true);
		if (bonusRule.compareTo(bonusRuleUpdate) != 0) {
			Assert.assertTrue(false, "创建的对象的字段与DB读出的不相等");
		}
		String error1 = bonusRuleUpdate.checkCreate(BaseBO.INVALID_CASE_ID);
		Assert.assertEquals(error1, "");
		//
		return bonusRuleUpdate;
	}

	public static void deleteViaMapper(BonusRule bonusRule, String dbName) {
		Map<String, Object> paramsDelete = bonusRule.getDeleteParam(BaseBO.INVALID_CASE_ID, bonusRule);
		//
		DataSourceContextHolder.setDbName(dbName);
		bonusRuleMapper.delete(paramsDelete);
		//
		Assert.assertTrue(EnumErrorCode.values()[Integer.parseInt(paramsDelete.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())] == EnumErrorCode.EC_NoError, //
				paramsDelete.get(BaseAction.SP_OUT_PARAM_sErrorMsg).toString());
		// 验证是否查询成功
		BaseModel bonusRuleRetrieve1 = retrieve1ViaMapper(bonusRule, dbName);
		Assert.assertTrue(bonusRuleRetrieve1 == null);
	}

	public static BaseModel retrieve1ViaMapper(BonusRule bonusRule, String dbName) {
		Map<String, Object> params = bonusRule.getRetrieve1Param(BaseBO.INVALID_CASE_ID, bonusRule);
		//
		DataSourceContextHolder.setDbName(dbName);
		BaseModel bonusRuleRetrieve1 = bonusRuleMapper.retrieve1(params);
		//
		Assert.assertTrue(EnumErrorCode.values()[Integer.parseInt(params.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())] == EnumErrorCode.EC_NoError, //
				params.get(BaseAction.SP_OUT_PARAM_sErrorMsg).toString());

		if (bonusRuleRetrieve1 != null) {
			String err = bonusRuleRetrieve1.checkCreate(BaseBO.INVALID_CASE_ID);
			Assert.assertTrue("".equals(err), err);
		}
		//
		return bonusRuleRetrieve1;
	}
	
	public static void updateViaAction(MockMvc mvc, HttpSession session, BonusRule bonusRule, EnumErrorCode errorCode) throws Exception {
		MvcResult mr = mvc.perform(//
				post("/bonusRule/updateEx.bx")//
						.contentType(MediaType.APPLICATION_JSON) //
						.session((MockHttpSession) session) //
						.param(BonusRule.field.getFIELD_NAME_ID(), String.valueOf(bonusRule.getID())) //
						.param(BonusRule.field.getFIELD_NAME_amountUnit(), String.valueOf(bonusRule.getAmountUnit())) //
						.param(BonusRule.field.getFIELD_NAME_increaseBonus(), String.valueOf(bonusRule.getIncreaseBonus())) //
						.param(BonusRule.field.getFIELD_NAME_maxIncreaseBonus(), String.valueOf(bonusRule.getMaxIncreaseBonus())) //
						.param(BonusRule.field.getFIELD_NAME_initIncreaseBonus(), String.valueOf(bonusRule.getInitIncreaseBonus())) //
		).andExpect(status().isOk()).andDo(print()).andReturn();
		// 结果验证：检查错误码
		Shared.checkJSONErrorCode(mr, errorCode);

	}
}
