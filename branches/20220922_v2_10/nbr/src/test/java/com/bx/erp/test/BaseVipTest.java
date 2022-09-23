package com.bx.erp.test;

import static org.testng.Assert.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Random;

import javax.servlet.http.HttpSession;

import org.testng.Assert;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.testng.annotations.BeforeClass;

import com.bx.erp.action.BaseAction;
import com.bx.erp.action.bo.BaseBO;
import com.bx.erp.model.RetailTrade;
import com.bx.erp.model.Vip;
import com.bx.erp.model.VipCard;
import com.bx.erp.model.BaseModel;
import com.bx.erp.model.ErrorInfo;
import com.bx.erp.model.ErrorInfo.EnumErrorCode;
import com.bx.erp.model.RetailTrade.EnumStatusRetailTrade;
import com.bx.erp.test.checkPoint.VipCP;
import com.bx.erp.util.DataSourceContextHolder;

import net.sf.json.JSONObject;

public class BaseVipTest extends BaseMapperTest {

	@BeforeClass
	public void setup() {
		Shared.printTestClassStartInfo();

	}

	public static Vip createViaMapper(int iUseCaseID, Vip vip, ErrorInfo.EnumErrorCode enumErrorCode, String dbName) {
		if(!vip.setDefaultValueToCreate(iUseCaseID)) {
			Assert.assertTrue(false, "setDefaultValueToCreate() err");
		}
		
		String err = vip.checkCreate(iUseCaseID);
		Assert.assertEquals(err, "");
		Map<String, Object> params = vip.getCreateParam(iUseCaseID, vip);
		//
		DataSourceContextHolder.setDbName(dbName);
		Vip vipCreate = (Vip) vipMapper.create(params);
		//
		Assert.assertTrue(EnumErrorCode.values()[Integer.parseInt(params.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())] == enumErrorCode, params.get(BaseAction.SP_OUT_PARAM_sErrorMsg).toString());
		//
		if (vipCreate != null) { // 条件合法，创建成功
			int bonus = vipCreate.getBonus();
			vipCreate.setBonus(vip.getBonus());
			err = vipCreate.checkCreate(iUseCaseID);
			Assert.assertEquals(err, "");
			//
			vip.setIgnoreIDInComparision(true);
			if (vip.compareTo(vipCreate) != 0) {
				Assert.assertTrue(false, "创建的对象的字段与DB读出的不相等");
			}
			vipCreate.setBonus(bonus);
			//
			System.out.println("测试成功，创建Vip成功： " + vipCreate);
		} else { // 条件不合法，创建失败
			System.out.println("测试成功，创建失败:" + params.get(BaseAction.SP_OUT_PARAM_sErrorMsg));
		}
		//
		return vipCreate;
	}

	public static Vip updateViaMapper(int iCaseID, Vip vip) {
		String error = vip.checkUpdate(iCaseID);
		Assert.assertEquals(error, "");

		Map<String, Object> params = vip.getUpdateParam(iCaseID, vip);
		//
		Vip vipUpdate = null;
		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		switch (iCaseID) {
		case BaseBO.CASE_Vip_UpdateBonus:
			vipUpdate = (Vip) vipMapper.updateBonus(params);
			break;
		case BaseBO.CASE_Vip_ResetBonus:
			vipUpdate = (Vip) vipMapper.resetBonus(params);
			break;
		default:
			vipUpdate = (Vip) vipMapper.update(params);
			break;
		}
		//
		assertTrue(vipUpdate != null && EnumErrorCode.values()[Integer.parseInt(params.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())] == EnumErrorCode.EC_NoError, "更新对象失败。param=" + params + "\n\r vipUpdate=" + vipUpdate);
		if (vip.compareTo(vipUpdate) != 0) {
			assertTrue(false, "更新的对象的字段与DB读出的不相等");
		}
		error = vipUpdate.checkUpdate(iCaseID);
		Assert.assertEquals(error, "");

		return vipUpdate;
	}

	public static class DataInput {
		private static Vip vipInput = new Vip();
		private static RetailTrade retailTradeInput = null;

		public static final RetailTrade getRetailTrade() throws CloneNotSupportedException, InterruptedException {
			Random ran = new Random();
			retailTradeInput = new RetailTrade();
			retailTradeInput.setSn("");
			retailTradeInput.setLocalSN(ran.nextInt(1000));
			Thread.sleep(1);
			retailTradeInput.setPos_ID(ran.nextInt(5) + 1);
			retailTradeInput.setLogo("url:" + String.valueOf(System.currentTimeMillis()).substring(6));
			Thread.sleep(1);
			retailTradeInput.setSaleDatetime(new Date());
			retailTradeInput.setStatus(EnumStatusRetailTrade.ESRT_Normal.getIndex());
			retailTradeInput.setStaffID(ran.nextInt(5) + 1);
			retailTradeInput.setPaymentType(1);
			retailTradeInput.setPaymentAccount("A" + String.valueOf(System.currentTimeMillis()).substring(6));
			retailTradeInput.setAmount(50);
			retailTradeInput.setAmountCash(50d);
			Thread.sleep(1);
			retailTradeInput.setRemark("remark:" + String.valueOf(System.currentTimeMillis()).substring(6));
			Thread.sleep(1);
			retailTradeInput.setSourceID(BaseAction.INVALID_ID);
			retailTradeInput.setSmallSheetID(ran.nextInt(7) + 1);
			retailTradeInput.setSyncDatetime(new Date());

			return (RetailTrade) retailTradeInput.clone();
		}

		public static final Vip getVip() throws CloneNotSupportedException, InterruptedException {
			vipInput.setMobile(Shared.getValidStaffPhone());
			vipInput.setiCID(Shared.getValidICID());
			Thread.sleep(10);
			vipInput.setCardID(1);
			Thread.sleep(10);
			vipInput.setName("Tom" + Shared.generateCompanyName(6));
			Thread.sleep(10);
			vipInput.setEmail(String.valueOf(System.currentTimeMillis()).substring(6) + "@bx.vip");
			vipInput.setDistrict("广州");
			vipInput.setCategory(1);
			vipInput.setBirthday(new Date());
			vipInput.setLastConsumeDatetime(new Date());
			vipInput.setSn("");
			vipInput.setSex(0);
			vipInput.setLogo("123456123456");
			vipInput.setRemark("1111");
			vipInput.setConsumeTimes(0);
			vipInput.setConsumeAmount(0);
			vipInput.setBonus(0);
			vipInput.setCreateDatetime(new Date());
			return (Vip) vipInput.clone();
		}

		public static final MockHttpServletRequestBuilder getBuilder(String url, MediaType contentType, Vip v) {
			SimpleDateFormat sdf = new SimpleDateFormat(BaseAction.DATETIME_FORMAT_Default3);
			MockHttpServletRequestBuilder builder = post(url).contentType(contentType)//
					.param(Vip.field.getFIELD_NAME_ID(), String.valueOf(v.getID())) //
					.param(Vip.field.getFIELD_NAME_bonus(), String.valueOf(v.getBonus()))//
					.param(Vip.field.getFIELD_NAME_remarkForBonusHistory(), v.getRemarkForBonusHistory())//
					.param(Vip.field.getFIELD_NAME_manuallyAdded(), String.valueOf(v.getManuallyAdded())) //
					.param(Vip.field.getFIELD_NAME_cardID(), String.valueOf(v.getCardID())) //
					.param(Vip.field.getFIELD_NAME_remark(), v.getRemark()) //
					.param(Vip.field.getFIELD_NAME_logo(), v.getLogo()) //
					.param(Vip.field.getFIELD_NAME_localPosSN(), v.getLocalPosSN()) //
					.param(Vip.field.getFIELD_NAME_iCID(), v.getiCID()) //
					.param(Vip.field.getFIELD_NAME_mobile(), v.getMobile()) //
					.param(Vip.field.getFIELD_NAME_name(), v.getName()) //
					.param(Vip.field.getFIELD_NAME_email(), v.getEmail()) //
					.param(Vip.field.getFIELD_NAME_consumeTimes(), String.valueOf(v.getConsumeTimes())) //
					.param(Vip.field.getFIELD_NAME_consumeAmount(), String.valueOf(v.getConsumeAmount())) //
					.param(Vip.field.getFIELD_NAME_district(), v.getDistrict()) //
					.param(Vip.field.getFIELD_NAME_category(), String.valueOf(v.getCategory())) //
					.param(Vip.field.getFIELD_NAME_birthday(), sdf.format(v.getBirthday())) //
					.param(Vip.field.getFIELD_NAME_bonus(), String.valueOf(v.getBonus())) //
					.param(Vip.field.getFIELD_NAME_lastConsumeDatetime(), sdf.format(v.getLastConsumeDatetime())) //
					.param(Vip.field.getFIELD_NAME_sex(), String.valueOf(v.getSex())) //
					.param(Vip.field.getFIELD_NAME_returnObject(), String.valueOf(v.getReturnObject()));//
			return builder;
		}
	}

	public static List<BaseModel> retrieveNViaMapper(Vip vip, int iCaseID) {
		String error = vip.checkRetrieveN(iCaseID);
		Assert.assertEquals(error, "");

		Map<String, Object> params = vip.getRetrieveNParam(iCaseID, vip);
		//
		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		List<BaseModel> vipRetrieveN = (List<BaseModel>) vipMapper.retrieveN(params);
		//
		assertTrue(EnumErrorCode.values()[Integer.parseInt(params.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())] == EnumErrorCode.EC_NoError, "查询对象失败。param=" + params);
		return vipRetrieveN;
	}

	public static List<BaseModel> retrieveNViaAction(Vip vip, HttpSession session, MockMvc mvc) throws Exception {
		MvcResult mr = mvc.perform(//
				post("/vip/retrieveNEx.bx")//
						.param(Vip.field.getFIELD_NAME_mobile(), vip.getMobile()) //
						.session((MockHttpSession) session)//
						.contentType(MediaType.APPLICATION_JSON)//
		)//
				.andExpect(status().isOk())//
				.andDo(print())//
				.andReturn();//

		Shared.checkJSONErrorCode(mr);

		String json = mr.getResponse().getContentAsString();
		JSONObject o = JSONObject.fromObject(json);
		return vip.parseN(o.getString(BaseAction.KEY_ObjectList));
	}

	public static List<BaseModel> retrieveNByMobileOrVipCardSNViaAction(Vip vip, HttpSession session, MockMvc mvc) throws Exception {
		MvcResult mr = mvc.perform(//
				post("/vip/retrieveNByMobileOrVipCardSNEx.bx")//
						.param(Vip.field.getFIELD_NAME_mobile(), vip.getMobile()) //
						.param(Vip.field.getFIELD_NAME_vipCardSN(), vip.getVipCardSN()) //
						.session((MockHttpSession) session)//
						.contentType(MediaType.APPLICATION_JSON)//
		)//
				.andExpect(status().isOk())//
				.andDo(print())//
				.andReturn();//

		Shared.checkJSONErrorCode(mr);

		String json = mr.getResponse().getContentAsString();
		JSONObject o = JSONObject.fromObject(json);
		return vip.parseN(o.getString(BaseAction.KEY_ObjectList));
	}

	public static Vip retrieve1ViaMapper(Vip vip, String dbName) {
		String error = vip.checkRetrieve1(BaseBO.INVALID_CASE_ID);
		Assert.assertEquals(error, "");

		Map<String, Object> params = vip.getRetrieve1Param(BaseBO.INVALID_CASE_ID, vip);
		//
		DataSourceContextHolder.setDbName(dbName);
		Vip vipRetrieve1 = (Vip) vipMapper.retrieve1(params);
		//
		assertTrue(EnumErrorCode.values()[Integer.parseInt(params.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())] == EnumErrorCode.EC_NoError, "查询对象失败。param=" + params);
		return vipRetrieve1;
	}

	public static void deleteAndVerificationVip(Vip vip) {
		String error = vip.checkDelete(BaseBO.INVALID_CASE_ID);
		Assert.assertEquals(error, "");

		Map<String, Object> params = vip.getDeleteParam(BaseBO.INVALID_CASE_ID, vip);
		//
		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		vipMapper.delete(params);
		//
		assertTrue(EnumErrorCode.values()[Integer.parseInt(params.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())] == EnumErrorCode.EC_NoError, "删除对象失败。param=" + params);
		// 验证是否删除成功(修改F_Status为2)
		Vip vipDelete = retrieve1ViaMapper(vip, Shared.DBName_Test);
		Assert.assertTrue(vipDelete != null, params.get(BaseAction.SP_OUT_PARAM_sErrorMsg).toString());
	}

	public static VipCard createVipCardViaMapper(VipCard vipCard) {
		String error = vipCard.checkCreate(BaseBO.INVALID_CASE_ID);
		Assert.assertEquals(error, "");

		Map<String, Object> params = vipCard.getCreateParam(BaseBO.INVALID_CASE_ID, vipCard);
		//
		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		VipCard vipCardCreate = (VipCard) vipCardMapper.create(params);
		//
		assertTrue(vipCardCreate != null && EnumErrorCode.values()[Integer.parseInt(params.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())] == EnumErrorCode.EC_NoError, //
				"创建对象失败。param=" + params + "\n\r vipCard=" + vipCardCreate);
		vipCard.setIgnoreIDInComparision(true);
		if (vipCard.compareTo(vipCardCreate) != 0) {
			assertTrue(false, Shared.CompareToErrorMsg);
		}
		//
		error = vipCardCreate.checkCreate(BaseBO.INVALID_CASE_ID);
		Assert.assertEquals(error, "");

		return vipCardCreate;
	}

	public static VipCard updateVipCardViaMapper(VipCard vipCard) {
		String error = vipCard.checkUpdate(BaseBO.INVALID_CASE_ID);
		Assert.assertEquals(error, "");

		Map<String, Object> params = vipCard.getUpdateParam(BaseBO.INVALID_CASE_ID, vipCard);
		//
		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		VipCard wxVipCardUpdate = (VipCard) vipCardMapper.update(params);
		//
		assertTrue(wxVipCardUpdate != null && EnumErrorCode.values()[Integer.parseInt(params.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())] == EnumErrorCode.EC_NoError, "更新对象失败。param=" + params + "\n\r vipCard=" + wxVipCardUpdate);
		if (vipCard.compareTo(wxVipCardUpdate) != 0) {
			assertTrue(false, "更新的对象的字段与DB读出的不相等");
		}
		//
		error = wxVipCardUpdate.checkCreate(BaseBO.INVALID_CASE_ID);
		Assert.assertEquals(error, "");

		return wxVipCardUpdate;
	}

	public static VipCard retrieve1VipCardViaMapper(VipCard vipCard) {
		String error = vipCard.checkRetrieve1(BaseBO.INVALID_CASE_ID);
		Assert.assertEquals(error, "");

		Map<String, Object> params = vipCard.getRetrieve1Param(BaseBO.INVALID_CASE_ID, vipCard);
		//
		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		VipCard vipCardRetrieve1 = (VipCard) vipCardMapper.retrieve1(params);
		//
		assertTrue(EnumErrorCode.values()[Integer.parseInt(params.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())] == EnumErrorCode.EC_NoError, "查询对象失败。param=" + params);
		return vipCardRetrieve1;
	}

	public static void deleteAndVerificationWxVipCardViaMapper(VipCard vipCard) {
		String error = vipCard.checkDelete(BaseBO.INVALID_CASE_ID);
		Assert.assertEquals(error, "");

		Map<String, Object> params = vipCard.getDeleteParam(BaseBO.INVALID_CASE_ID, vipCard);
		//
		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		vipCardMapper.delete(params);
		//
		assertTrue(EnumErrorCode.values()[Integer.parseInt(params.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())] == EnumErrorCode.EC_NoError, "删除对象失败。param=" + params);
		// 验证数据库有没有删除成功
		VipCard vipCardRetrieve1 = retrieve1VipCardViaMapper(vipCard);
		assertTrue(vipCardRetrieve1 == null);
	}

	public static void deleteViaMapper(Vip vipCreate) {
		Map<String, Object> paramsForDelete = vipCreate.getDeleteParam(BaseBO.INVALID_CASE_ID, vipCreate);
		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		vipMapper.delete(paramsForDelete);
		//
		Assert.assertTrue(EnumErrorCode.values()[Integer.parseInt(paramsForDelete.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())] == EnumErrorCode.EC_NoError, paramsForDelete.get(BaseAction.SP_OUT_PARAM_sErrorMsg).toString());
		// 查询已删除的会员
		Map<String, Object> paramsForRetrieve1 = vipCreate.getRetrieve1Param(BaseBO.INVALID_CASE_ID, vipCreate);
		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		Vip retrieve1Vip = (Vip) vipMapper.retrieve1(paramsForRetrieve1);
		//
		Assert.assertTrue(retrieve1Vip == null, paramsForRetrieve1.get(BaseAction.SP_OUT_PARAM_sErrorMsg).toString());
	}

	public static Vip createViaAction(Vip v, HttpSession session, MockMvc mvc, EnumErrorCode eec) throws Exception {
		//
		SimpleDateFormat sdf = new SimpleDateFormat(BaseAction.DATETIME_FORMAT_Default3);
		MvcResult mr = mvc.perform(//
				post("/vip/createEx.bx")//
						.contentType(MediaType.APPLICATION_JSON) //
						.session((MockHttpSession) session) //
						.param(Vip.field.getFIELD_NAME_cardID(), String.valueOf(v.getCardID())) //
						.param(Vip.field.getFIELD_NAME_remark(), v.getRemark()) //
						.param(Vip.field.getFIELD_NAME_logo(), v.getLogo()) //
						.param(Vip.field.getFIELD_NAME_localPosSN(), v.getLocalPosSN()) //
						.param(Vip.field.getFIELD_NAME_iCID(), v.getiCID()) //
						.param(Vip.field.getFIELD_NAME_mobile(), v.getMobile()) //
						.param(Vip.field.getFIELD_NAME_name(), v.getName()) //
						.param(Vip.field.getFIELD_NAME_email(), v.getEmail()) //
						.param(Vip.field.getFIELD_NAME_consumeTimes(), String.valueOf(v.getConsumeTimes())) //
						.param(Vip.field.getFIELD_NAME_consumeAmount(), String.valueOf(v.getConsumeAmount())) //
						.param(Vip.field.getFIELD_NAME_district(), v.getDistrict()) //
						.param(Vip.field.getFIELD_NAME_category(), String.valueOf(v.getCategory())) //
						.param(Vip.field.getFIELD_NAME_birthday(), sdf.format(v.getBirthday())) //
						.param(Vip.field.getFIELD_NAME_bonus(), String.valueOf(v.getBonus())) //
						.param(Vip.field.getFIELD_NAME_lastConsumeDatetime(), sdf.format(v.getLastConsumeDatetime())) //
						.param(Vip.field.getFIELD_NAME_sex(), String.valueOf(v.getSex())) //
						.param(Vip.field.getFIELD_NAME_returnObject(), String.valueOf(v.getReturnObject()))//
						.param(Vip.field.getFIELD_NAME_isImported(), String.valueOf(v.getIsImported())) //
						.param(Vip.field.getFIELD_NAME_cardCode(), v.getCardCode()) //
		).andExpect(status().isOk()).andDo(print()).andReturn();
		// 结果验证：检查错误码
		Shared.checkJSONErrorCode(mr, eec);
		if (eec == EnumErrorCode.EC_NoError) {
			// 验证缓存中的结果
			VipCP.verifyCreate(mr, v, Shared.DBName_Test);
			JSONObject object = JSONObject.fromObject(mr.getResponse().getContentAsString());
			Vip vipCreate = new Vip();
			vipCreate = (Vip) vipCreate.parse1(object.getString(BaseAction.KEY_Object));
			return vipCreate;
		}
		return null;
	}

	public static Vip updateViaAction(Vip vip, HttpSession session, MockMvc mvc) throws Exception {
		MvcResult mr = mvc.perform(//
				post("/vip/updateEx.bx")//
						.contentType(MediaType.APPLICATION_JSON)//
						.session((MockHttpSession) session) //
						.param(Vip.field.getFIELD_NAME_ID(), String.valueOf(vip.getID())) //
						.param(Vip.field.getFIELD_NAME_district(), vip.getDistrict())//
						.param(Vip.field.getFIELD_NAME_category(), String.valueOf(vip.getCategory())) //
						.param(Vip.field.getFIELD_NAME_name(), vip.getName())//
						.param(Vip.field.getFIELD_NAME_birthday(), String.valueOf(vip.getBirthday()))//
		)//
				.andExpect(status().isOk())//
				.andDo(print())//
				.andReturn();
		// 结果验证:检查错误码
		Shared.checkJSONErrorCode(mr);
		// 结果验证：检查缓存结果
		VipCP.verifyUpdate(mr, vip, Shared.DBName_Test);
		return (Vip) Shared.parse1Object(mr, vip, BaseAction.KEY_Object);
	}

	public static Vip updateBonusViaAction(Vip vip, HttpSession session, MockMvc mvc) throws Exception {
		MvcResult mr = mvc.perform(DataInput.getBuilder("/vip/updateBonusEx.bx", MediaType.APPLICATION_JSON, vip) //
				.session((MockHttpSession) session)//
		) //
				.andExpect(status().isOk())//
				.andDo(print())//
				.andReturn();
		// 结果验证:检查错误码
		Shared.checkJSONErrorCode(mr);
		// 结果验证：检查缓存结果
		VipCP.verifyUpdate(mr, vip, Shared.DBName_Test);
		return (Vip) Shared.parse1Object(mr, vip, BaseAction.KEY_Object);
	}
	
	public static Vip updateBonusViaMapper(Vip vipToUpdate, String dbName, EnumErrorCode eec) {
		String errorMsg= vipToUpdate.checkUpdate(BaseBO.CASE_Vip_UpdateBonus);
		Assert.assertEquals(errorMsg, "");
		Map<String, Object> paramsForUpdate = vipToUpdate.getUpdateParam(BaseBO.CASE_Vip_UpdateBonus, vipToUpdate);
		DataSourceContextHolder.setDbName(dbName);
		Vip vipUpdate = (Vip) vipMapper.updateBonus(paramsForUpdate);
		//
		if(eec != EnumErrorCode.EC_NoError) {
			Assert.assertTrue(EnumErrorCode.values()[Integer.parseInt(paramsForUpdate.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())] == eec, "修改积分失败");
			return null;
		}
		Assert.assertTrue(vipUpdate != null && EnumErrorCode.values()[Integer.parseInt(paramsForUpdate.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())] == EnumErrorCode.EC_NoError, "修改积分失败");
		return vipUpdate;
	}
}
