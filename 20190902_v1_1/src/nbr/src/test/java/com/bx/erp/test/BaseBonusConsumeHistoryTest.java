package com.bx.erp.test;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Random;

import org.junit.AfterClass;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;

import com.bx.erp.action.BaseAction;
import com.bx.erp.action.bo.BaseBO;
import com.bx.erp.model.BaseModel;
import com.bx.erp.model.BonusConsumeHistory;
import com.bx.erp.model.ErrorInfo.EnumErrorCode;
import com.bx.erp.util.DataSourceContextHolder;

public class BaseBonusConsumeHistoryTest extends BaseMapperTest {
	@BeforeClass
	public void setup() {
	}

	@AfterClass
	public void tearDown() {
	}

	public static class DataInput {
		private static BonusConsumeHistory bonusConsumeDetailsInput = null;

		public static final BonusConsumeHistory getBonusConsumeHistory(int vipID) throws CloneNotSupportedException {
			bonusConsumeDetailsInput = new BonusConsumeHistory();
			bonusConsumeDetailsInput.setVipID(vipID);
			bonusConsumeDetailsInput.setStaffID(STAFF_ID3);
			bonusConsumeDetailsInput.setBonus(new Random().nextInt(100) + 1);
			bonusConsumeDetailsInput.setAddedBonus(new Random().nextInt(100) + 1);
			bonusConsumeDetailsInput.setRemark("xxxxxxxxxxxxxxxxxxxxx");
			bonusConsumeDetailsInput.setCreateDatetime(new Date());

			return (BonusConsumeHistory) bonusConsumeDetailsInput.clone();
		}

	}

	public static BonusConsumeHistory createViaMapper(BonusConsumeHistory bonusConsumeHistory) {
		Map<String, Object> params = bonusConsumeHistory.getCreateParam(BaseBO.INVALID_CASE_ID, bonusConsumeHistory);
		//
		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		BonusConsumeHistory bonusConsumeHistoryCreate = (BonusConsumeHistory) bonusConsumeHistoryMapper.create(params);
		Assert.assertTrue(bonusConsumeHistoryCreate != null && EnumErrorCode.values()[Integer.parseInt(params.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())] == EnumErrorCode.EC_NoError,
				"创建对象失败。param=" + params + "\n\r bonusConsumeHistory=" + bonusConsumeHistory);
		//
		bonusConsumeHistory.setIgnoreIDInComparision(true);
		bonusConsumeHistory.setCreateDatetime((Date) bonusConsumeHistoryCreate.getCreateDatetime().clone()); // 令比较能通过
		if (bonusConsumeHistory.compareTo(bonusConsumeHistoryCreate) != 0) {
			Assert.assertTrue(false, "创建的对象的字段与DB读出的不相等");
		}
		String error1 = bonusConsumeHistoryCreate.checkCreate(BaseBO.INVALID_CASE_ID);
		Assert.assertEquals(error1, "");
		//
		return bonusConsumeHistoryCreate;
	}

	public static void deleteViaMapper(BonusConsumeHistory bonusConsumeHistory) {
		Map<String, Object> paramsDelete = bonusConsumeHistory.getDeleteParam(BaseBO.INVALID_CASE_ID, bonusConsumeHistory);
		//
		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		bonusConsumeHistoryMapper.delete(paramsDelete);
		//
		Assert.assertTrue(EnumErrorCode.values()[Integer.parseInt(paramsDelete.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())] == EnumErrorCode.EC_NoError, //
				paramsDelete.get(BaseAction.SP_OUT_PARAM_sErrorMsg).toString());
		// 验证是否查询成功
		BaseModel bonusRuleRetrieve1 = retrieve1ViaMapper(bonusConsumeHistory);
		Assert.assertTrue(bonusRuleRetrieve1 == null);
	}

	public static BaseModel retrieve1ViaMapper(BonusConsumeHistory bonusConsumeHistory) {
		Map<String, Object> params = bonusConsumeHistory.getRetrieve1Param(BaseBO.INVALID_CASE_ID, bonusConsumeHistory);
		//
		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		BaseModel bonusConsumeHistoryRetrieve1 = bonusConsumeHistoryMapper.retrieve1(params);
		//
		Assert.assertTrue(EnumErrorCode.values()[Integer.parseInt(params.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())] == EnumErrorCode.EC_NoError, //
				params.get(BaseAction.SP_OUT_PARAM_sErrorMsg).toString());

		if (bonusConsumeHistoryRetrieve1 != null) {
			String err = bonusConsumeHistoryRetrieve1.checkCreate(BaseBO.INVALID_CASE_ID);
			Assert.assertTrue("".equals(err), err);
		}
		//
		return bonusConsumeHistoryRetrieve1;
	}

	public static List<?> retrieveNViaAction(BonusConsumeHistory bonusConsumeHistoryToRN, MockMvc mvc, MockHttpSession session) throws Exception {
		//
		MvcResult mr = mvc.perform(//
				post("/bonusConsumeHistory/retrieveNEx.bx")//
						.param(BonusConsumeHistory.field.getFIELD_NAME_vipID(), String.valueOf(bonusConsumeHistoryToRN.getVipID())) //
						.param(BonusConsumeHistory.field.getFIELD_NAME_vipMobile(), bonusConsumeHistoryToRN.getVipMobile()) //
						.param(BonusConsumeHistory.field.getFIELD_NAME_vipName(), bonusConsumeHistoryToRN.getVipName()) //
						.contentType(MediaType.APPLICATION_JSON) //
						.session((MockHttpSession) session) //
		).andExpect(status().isOk()).andDo(print()).andReturn();
		// 结果验证：检查错误码
		Shared.checkJSONErrorCode(mr);

		List<?> bonusConsumeHistoryList = Shared.parseNObject(mr, bonusConsumeHistoryToRN, BaseAction.KEY_ObjectList);
		return bonusConsumeHistoryList;
	}
	
	public static List<BaseModel> retrieveNViaMapper(BonusConsumeHistory bonusConsumeHistoryToRN, String dbName, EnumErrorCode eec) {
		String error = bonusConsumeHistoryToRN.checkRetrieveN(BaseBO.INVALID_CASE_ID);
		Assert.assertEquals(error, "");
		Map<String, Object> params = bonusConsumeHistoryToRN.getRetrieveNParam(BaseBO.INVALID_CASE_ID, bonusConsumeHistoryToRN);
		//
		DataSourceContextHolder.setDbName(dbName);
		List<BaseModel> bonusConsumeHistoryRetrieveN = bonusConsumeHistoryMapper.retrieveN(params);
		//
		if(eec != EnumErrorCode.EC_NoError) {
			Assert.assertTrue(EnumErrorCode.values()[Integer.parseInt(params.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())] == eec, //
					params.get(BaseAction.SP_OUT_PARAM_sErrorMsg).toString());
			return null;
		}
		Assert.assertTrue(EnumErrorCode.values()[Integer.parseInt(params.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())] == EnumErrorCode.EC_NoError, //
				params.get(BaseAction.SP_OUT_PARAM_sErrorMsg).toString());
		return bonusConsumeHistoryRetrieveN;
	}
}
