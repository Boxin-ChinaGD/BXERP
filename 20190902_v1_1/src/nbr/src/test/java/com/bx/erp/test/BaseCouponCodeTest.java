package com.bx.erp.test;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.List;
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
import com.bx.erp.model.BaseModel;
import com.bx.erp.model.CouponCode;
import com.bx.erp.model.CouponCode.EnumCouponCodeStatus;
import com.bx.erp.model.ErrorInfo.EnumErrorCode;
import com.bx.erp.util.DataSourceContextHolder;

import net.sf.json.JSONObject;

public class BaseCouponCodeTest extends BaseMapperTest {
	@BeforeClass
	public void setup() {
	}

	@AfterClass
	public void tearDown() {
	}

	public static class DataInput {
		private static CouponCode couponCodeInput = null;

		public static final CouponCode getCouponCode(int vipID, int couponID) throws CloneNotSupportedException {
			couponCodeInput = new CouponCode();
			couponCodeInput.setVipID(vipID);
			couponCodeInput.setCouponID(couponID);
			couponCodeInput.setStatus(EnumCouponCodeStatus.ECCS_Normal.getIndex());

			return (CouponCode) couponCodeInput.clone();
		}

	}

	public static CouponCode createViaMapper(CouponCode couponCode) {
		Map<String, Object> params = couponCode.getCreateParam(BaseBO.INVALID_CASE_ID, couponCode);
		//
		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		CouponCode couponCodeCreate = (CouponCode) couponCodeMapper.create(params);
		Assert.assertTrue(couponCodeCreate != null && EnumErrorCode.values()[Integer.parseInt(params.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())] == EnumErrorCode.EC_NoError,
				"创建对象失败。param=" + params + "\n\r couponCode=" + couponCode);
		//
		couponCode.setIgnoreIDInComparision(true);
		if (couponCode.compareTo(couponCodeCreate) != 0) {
			Assert.assertTrue(false, "创建的对象的字段与DB读出的不相等");
		}
		String error1 = couponCodeCreate.checkCreate(BaseBO.INVALID_CASE_ID);
		Assert.assertEquals(error1, "");
		//
		return couponCodeCreate;
	}

	public static void deleteViaMapper(CouponCode couponCode) {
		Map<String, Object> paramsDelete = couponCode.getDeleteParam(BaseBO.INVALID_CASE_ID, couponCode);
		//
		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		couponCodeMapper.delete(paramsDelete);
		//
		Assert.assertTrue(EnumErrorCode.values()[Integer.parseInt(paramsDelete.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())] == EnumErrorCode.EC_NoError, //
				paramsDelete.get(BaseAction.SP_OUT_PARAM_sErrorMsg).toString());
		// 验证是否查询成功
		BaseModel bonusRuleRetrieve1 = retrieve1ViaMapper(couponCode);
		Assert.assertTrue(bonusRuleRetrieve1 == null);
	}

	public static BaseModel retrieve1ViaMapper(CouponCode couponCode) {
		Map<String, Object> params = couponCode.getRetrieve1Param(BaseBO.INVALID_CASE_ID, couponCode);
		//
		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		BaseModel couponCodeRetrieve1 = couponCodeMapper.retrieve1(params);
		//
		Assert.assertTrue(EnumErrorCode.values()[Integer.parseInt(params.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())] == EnumErrorCode.EC_NoError, //
				params.get(BaseAction.SP_OUT_PARAM_sErrorMsg).toString());

		if (couponCodeRetrieve1 != null) {
			String err = couponCodeRetrieve1.checkCreate(BaseBO.INVALID_CASE_ID);
			Assert.assertTrue("".equals(err), err);
		}
		//
		return couponCodeRetrieve1;
	}

	// 核销卡券
	public static BaseModel updateViaMapper(CouponCode couponCode) {
		Map<String, Object> params = couponCode.getUpdateParam(BaseBO.CASE_CouponCode_Consume, couponCode);
		//
		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		CouponCode couponCodeConsumed = (CouponCode) couponCodeMapper.consume(params);
		//
		Assert.assertTrue(couponCodeConsumed != null && couponCodeConsumed.getStatus() == EnumCouponCodeStatus.ECCS_Consumed.getIndex() //
				&& EnumErrorCode.values()[Integer.parseInt(params.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())] == EnumErrorCode.EC_NoError, //
				params.get(BaseAction.SP_OUT_PARAM_sErrorMsg).toString());

		String err = couponCodeConsumed.checkCreate(BaseBO.INVALID_CASE_ID);
		Assert.assertTrue("".equals(err), err);

		return couponCodeConsumed;
	}

	public static List<BaseModel> retrieveNViaMapper(CouponCode couponCode, int iCaseID) {
		Map<String, Object> params = couponCode.getRetrieveNParam(iCaseID, couponCode);
		//
		List<BaseModel> couponCodeRetrieveN = null;
		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		switch (iCaseID) {
		case BaseBO.CASE_CouponCode_retrieveNByVipID:
			couponCodeRetrieveN = couponCodeMapper.retrieveNByVipID(params);
			break;
		default:
			couponCodeRetrieveN = couponCodeMapper.retrieveN(params);
			break;
		}
		Assert.assertTrue(EnumErrorCode.values()[Integer.parseInt(params.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())] == EnumErrorCode.EC_NoError, //
				params.get(BaseAction.SP_OUT_PARAM_sErrorMsg).toString());

		return couponCodeRetrieveN;
	}

	public static int retrieveNTotalByVipIDViaMapper(CouponCode couponCode, String dbName) {
		DataSourceContextHolder.setDbName(dbName);
		Map<String, Object> params = couponCode.getRetrieveNParam(BaseBO.CASE_CouponCode_retrieveNTotalByVipID, couponCode);
		couponCodeMapper.retrieveNTotalByVipID(params);
		//
		Assert.assertTrue(EnumErrorCode.values()[Integer.parseInt(params.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())] == EnumErrorCode.EC_NoError, //
				params.get(BaseAction.SP_OUT_PARAM_sErrorMsg).toString());
		return Integer.parseInt(params.get(BaseAction.SP_OUT_PARAM_iTotalRecord).toString());
	}

	public static int retrieveNTotalByVipIDViaAction(CouponCode couponCode, MockMvc mvc, HttpSession session) throws Exception {
		MvcResult mr = mvc.perform(//
				post("/couponCode/retrieveNTotalByVipIDEx.bx") //
						.param(CouponCode.field.getFIELD_NAME_vipID(), String.valueOf(couponCode.getVipID())) //
						.contentType(MediaType.APPLICATION_JSON) //
						.session((MockHttpSession) session) //
		)//
				.andExpect(status().isOk())//
				.andDo(print())//
				.andReturn();
		Shared.checkJSONErrorCode(mr);
		String responseData = mr.getResponse().getContentAsString();
		JSONObject json = JSONObject.fromObject(responseData);
		return json.getInt(BaseAction.KEY_HTMLTable_Parameter_TotalRecord);
	}

	public static List<BaseModel> retrieveNViaAction(CouponCode couponCode, MockMvc mvc, HttpSession session, int iCaseID) throws Exception {
		MvcResult mr = null;
		switch (iCaseID) {
		case BaseBO.CASE_CouponCode_retrieveNByVipID:
			mr = mvc.perform(//
					post("/couponCode/retrieveNByVipIDEx.bx") //
							.param(CouponCode.field.getFIELD_NAME_vipID(), String.valueOf(couponCode.getVipID())) //
							.param(CouponCode.field.getFIELD_NAME_subStatus(), String.valueOf(couponCode.getSubStatus())) //
							.contentType(MediaType.APPLICATION_JSON) //
							.session((MockHttpSession) session) //
			)//
					.andExpect(status().isOk())//
					.andDo(print())//
					.andReturn();
			break;
		default:
			mr = mvc.perform(//
					post("/couponCode/retrieveNEx.bx") //
							.param(CouponCode.field.getFIELD_NAME_vipID(), String.valueOf(couponCode.getVipID())) //
							.param(CouponCode.field.getFIELD_NAME_couponID(), String.valueOf(couponCode.getCouponID())) //
							.param(CouponCode.field.getFIELD_NAME_status(), String.valueOf(couponCode.getStatus())) //
							.contentType(MediaType.APPLICATION_JSON) //
							.session((MockHttpSession) session) //
			)//
					.andExpect(status().isOk())//
					.andDo(print())//
					.andReturn();
		}
		// 结果验证：检查错误码
		Shared.checkJSONErrorCode(mr);

		return couponCode.parseN(JSONObject.fromObject(mr.getResponse().getContentAsString()).getString(BaseAction.KEY_ObjectList));
	}

	public static CouponCode createViaAction(CouponCode couponCode, MockMvc mvc, HttpSession session, EnumErrorCode error) throws Exception {
		MvcResult mr = mvc.perform(//
				post("/couponCode/createEx.bx") //
						.param(CouponCode.field.getFIELD_NAME_vipID(), String.valueOf(couponCode.getVipID())) //
						.param(CouponCode.field.getFIELD_NAME_couponID(), String.valueOf(couponCode.getCouponID())) //
						.param(CouponCode.field.getFIELD_NAME_status(), String.valueOf(couponCode.getStatus())) //
						.contentType(MediaType.APPLICATION_JSON) //
						.session((MockHttpSession) session) //
		)//
				.andExpect(status().isOk())//
				.andDo(print())//
				.andReturn();
		// 结果验证：检查错误码
		Shared.checkJSONErrorCode(mr, error);

		if (error == EnumErrorCode.EC_NoError) {
			String json = mr.getResponse().getContentAsString();
			JSONObject o = JSONObject.fromObject(json);
			return (CouponCode) couponCode.parse1(o.getString(BaseAction.KEY_Object));
		}
		return null;
	}
}
