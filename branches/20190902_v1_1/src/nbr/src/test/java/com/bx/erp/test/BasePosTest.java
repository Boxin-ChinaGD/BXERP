package com.bx.erp.test;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.testng.Assert.assertTrue;

import java.math.BigInteger;
import java.security.interfaces.RSAPublicKey;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;

import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.filter.CharacterEncodingFilter;
import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;

import com.bx.erp.action.BaseAction;
import com.bx.erp.action.BaseAction.EnumSession;
import com.bx.erp.action.bo.BaseBO;
import com.bx.erp.action.bo.PosBO;
import com.bx.erp.action.bo.PosSyncCacheBO;
import com.bx.erp.action.bo.PosSyncCacheDispatcherBO;
import com.bx.erp.cache.CacheManager;
import com.bx.erp.dao.PosMapper;
import com.bx.erp.model.BaseModel;
import com.bx.erp.model.ErrorInfo;
import com.bx.erp.model.Pos;
import com.bx.erp.model.BaseModel.EnumBoolean;
import com.bx.erp.model.CacheType.EnumCacheType;
import com.bx.erp.model.ErrorInfo.EnumErrorCode;
import com.bx.erp.model.Pos.EnumStatusPos;
import com.bx.erp.model.SyncCacheType.EnumSyncCacheType;
import com.bx.erp.test.checkPoint.PosCP;
import com.bx.erp.util.DataSourceContextHolder;
import com.bx.erp.util.MD5Util;
import com.bx.erp.util.RSAUtils;
import com.jayway.jsonpath.JsonPath;

import net.sf.json.JSONObject;

public class BasePosTest extends BaseMapperTest {
	@Resource
	private WebApplicationContext wac;
	private MockMvc mvc;
	protected static HttpSession session;


	public static class DataInput {
		private static Pos posInput = null;
		
		public static final Pos getPOS() throws Exception {
			posInput = new Pos();
			posInput.setSalt(MD5Util.MD5(Shared.PASSWORD_DEFAULT + BaseAction.SHADOW)); // ??????????????????????????????session??????????????????????????????????????????000000
			Thread.sleep(1);
			posInput.setPos_SN("SN" + String.valueOf(System.currentTimeMillis()).substring(6));
			Thread.sleep(1);
			posInput.setShopID(1);
			posInput.setStatus(EnumStatusPos.ESP_Active.getIndex());
			posInput.setPasswordInPOS(UUID.randomUUID().toString().substring(1, 17));
			return (Pos) posInput.clone();
		}
	}
	
	@BeforeClass
	public void setup() {
		Shared.printTestClassStartInfo();

		CharacterEncodingFilter encodingFilter = new CharacterEncodingFilter();
		encodingFilter.setEncoding("utf-8");
		encodingFilter.setForceEncoding(true);

		mvc = MockMvcBuilders.webAppContextSetup(wac).addFilter(encodingFilter, "/*").build();
		try {
			session = Shared.getStaffLoginSession(mvc, Shared.PhoneOfBoss, Shared.PASSWORD_DEFAULT, Shared.DB_SN_Test);
		} catch (Exception e) {
			e.printStackTrace();
		}

		posMapper = (PosMapper) applicationContext.getBean("posMapper");

		Doctor_checkStatus();
		Doctor_checkCreate();
	}

	@AfterClass
	public void tearDown() {
		Shared.printTestClassEndInfo();

		Doctor_checkStatus();
		Doctor_checkCreate();
	}

	private void Doctor_checkStatus() {
		Shared.printTestClassEndInfo();

		Map<String, Object> params = new HashMap<String, Object>();
		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		posMapper.checkStatus(params);
		if (EnumErrorCode.values()[Integer.parseInt(params.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())] != EnumErrorCode.EC_NoError) {
			System.out.println("?????????POS?????????????????????????????????errorCode =" + EnumErrorCode.values()[Integer.parseInt(params.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())]);
		}
		if (!params.get(BaseAction.SP_OUT_PARAM_sErrorMsg).toString().equals("")) {
			System.out.println("?????????POS???????????????????????????????????????????????????????????????errorMsg=" + params.get(BaseAction.SP_OUT_PARAM_sErrorMsg).toString());
		}
	}

	private void Doctor_checkCreate() {
		Pos pos = new Pos();
		pos.setShopID(BaseAction.INVALID_ID);
		pos.setStatus(BaseAction.INVALID_STATUS);
		pos.setReturnSalt(1);
		pos.setPageIndex(1);
		pos.setPageSize(BaseAction.PAGE_SIZE_Infinite);

		String error = pos.checkRetrieveN(BaseBO.INVALID_CASE_ID);
		if (!error.equals("")) {
			System.out.println("???????????????RN????????????????????????????????????????????????");
			System.out.println("pos=" + pos.toString());
		}
		//
		Map<String, Object> params = pos.getRetrieveNParam(BaseBO.INVALID_CASE_ID, pos);
		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		List<BaseModel> list = posMapper.retrieveN(params);
		if (EnumErrorCode.values()[Integer.parseInt(params.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())] != EnumErrorCode.EC_NoError) {
			System.out.println("?????????????????????errorCode =" + EnumErrorCode.values()[Integer.parseInt(params.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())]);
		}
		if (list.size() == 0 || list == null) {
			System.out.println("RN?????????POS???????????????");
		}
		for (BaseModel bm : list) {
			Pos p = (Pos) bm;
			error = p.checkCreate(BaseBO.INVALID_CASE_ID);
			if (!error.equals("")) {
				System.out.println(p.getID() + "???POS???????????????????????????");
				System.out.println("p=" + p.toString());
			}
		}
	}

	public static Pos createPosViaMapper(Pos pos, String dbName) throws Exception {
		//
		String err = pos.checkCreate(BaseBO.INVALID_CASE_ID);
		assertTrue(err.equals(""), err);
		//
		Map<String, Object> params = pos.getCreateParam(BaseBO.INVALID_CASE_ID, pos);
		//
		DataSourceContextHolder.setDbName(dbName);
		Pos posCreate = (Pos) posMapper.create(params);
		//
		assertTrue(posCreate != null && EnumErrorCode.values()[Integer.parseInt(params.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())] == EnumErrorCode.EC_NoError, "??????????????????");
		pos.setIgnoreIDInComparision(true);
		if (pos.compareTo(posCreate) != 0) {
			assertTrue(false, "???????????????????????????DB??????????????????");
		}
		posCreate.setSalt(pos.getSalt()); //???????????????checkCreate??????
		String err1 = posCreate.checkCreate(BaseBO.INVALID_CASE_ID);
		assertTrue(err1.equals(""), err1);
		posCreate.setSalt(null);
		//
		CacheManager.getCache(dbName, EnumCacheType.ECT_POS).write1(posCreate, dbName, Shared.BossID);

		return posCreate;
	}

	public static Pos retrieve1PosViaMapper(Pos pos, String dbName) throws Exception {
		//
		pos.setReturnSalt(EnumBoolean.EB_Yes.getIndex());
		Map<String, Object> params = pos.getRetrieve1Param(BaseBO.INVALID_CASE_ID, pos);
		//
		DataSourceContextHolder.setDbName(dbName);
		Pos posR1 = (Pos) posMapper.retrieve1(params);
		//
		assertTrue(posR1 != null && EnumErrorCode.values()[Integer.parseInt(params.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())] == EnumErrorCode.EC_NoError, params.get(BaseAction.SP_OUT_PARAM_sErrorMsg).toString());
		pos.setIgnoreIDInComparision(true);  //r1??????????????????ID????????????????????????????????????pos????????????ID???????????????comareTo()????????????
		if (pos.compareTo(posR1) != 0) {
			assertTrue(false, "???????????????????????????DB??????????????????");
		}
		String err = posR1.checkCreate(BaseBO.INVALID_CASE_ID);
		assertTrue(err.equals(""), err);

		return posR1;
	}

	public static Pos recycleAppPosViaMapper(Pos pos, String dbName) throws Exception {
		//
		Map<String, Object> params = pos.getUpdateParam(BaseBO.CASE_POS_RecycleApp, pos);
		//
		DataSourceContextHolder.setDbName(dbName);
		Pos posRecycleApp = (Pos) posMapper.recycleApp(params);
		//
		assertTrue(posRecycleApp != null && EnumErrorCode.values()[Integer.parseInt(params.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())] == EnumErrorCode.EC_NoError, params.get(BaseAction.SP_OUT_PARAM_sErrorMsg).toString());
		pos.setIgnoreIDInComparision(true);
		if (pos.compareTo(posRecycleApp) != 0) {
			assertTrue(false, "???????????????????????????DB??????????????????");
		}
		String err = pos.checkCreate(BaseBO.INVALID_CASE_ID);
		assertTrue(err.equals(""), err);

		return posRecycleApp;
	}

	public static Pos createPosViaSyncAction(Pos pos, MockMvc mvc, Map<String, BaseBO> BOsMap, String dbName) throws Exception {
		HttpSession session = Shared.getBXStaffLoginSession(mvc, Shared.PhoneOfOP);
		MvcResult mr = mvc.perform(//
				getPosBuilder("/posSync/createEx.bx", MediaType.APPLICATION_JSON, pos) //
						.contentType(MediaType.APPLICATION_JSON) //
						.session((MockHttpSession) session) //
		)//
				.andExpect(status().isOk())//
				.andDo(print())//
				.andReturn();
		// ??????????????????????????????
		Shared.checkJSONErrorCode(mr);
		String json = mr.getResponse().getContentAsString();
		JSONObject o = JSONObject.fromObject(json);
		Pos pos1 = new Pos();
		pos1 = (Pos) pos1.parse1(o.getString(BaseAction.KEY_Object));
		// ????????????????????????
		Pos poss = (Pos) session.getAttribute(EnumSession.SESSION_POS.getName());
		//
		PosBO posBO = (PosBO) BOsMap.get(PosBO.class.getSimpleName());
		PosSyncCacheBO posSyncCacheBO = (PosSyncCacheBO) BOsMap.get(PosSyncCacheBO.class.getSimpleName());
		PosSyncCacheDispatcherBO posSyncCacheDispatcherBO = (PosSyncCacheDispatcherBO) BOsMap.get(PosSyncCacheDispatcherBO.class.getSimpleName());
		verifySyncCreateResult(mr, pos, posBO, posSyncCacheBO, posSyncCacheDispatcherBO, EnumCacheType.ECT_POS, EnumSyncCacheType.ESCT_PosSyncInfo, poss.getID(), dbName);// posID > 0??????pos??????????????????
		return pos1;
	}

	public static void deletePosViaSyncAction(Pos pos, MockMvc mvc, Map<String, BaseBO> BOsMap, String dbName) throws Exception {
		HttpSession session = Shared.getBXStaffLoginSession(mvc, Shared.PhoneOfOP);
		MvcResult mr = mvc.perform(get("/posSync/deleteEx.bx?" + Pos.field.getFIELD_NAME_ID() + "=" + pos.getID() + "&" + Pos.field.getFIELD_NAME_companySN() + "=" + pos.getCompanySN()) //
				.contentType(MediaType.APPLICATION_JSON) //
				.session((MockHttpSession) session) //
		).andExpect(status().isOk()).andDo(print()).andReturn();
		//
		Shared.checkJSONErrorCode(mr);
		// ?????????????????????????????????????????????????????????
		ErrorInfo ecOut = new ErrorInfo();
		Pos bm = (Pos) CacheManager.getCache(dbName, EnumCacheType.ECT_POS).read1(pos.getID(), BaseBO.SYSTEM, ecOut, dbName);
		Assert.assertTrue(bm.getStatus() == 1, "???????????????????????????????????????");
		// ???????????????????????????????????????
		Pos pos2 = (Pos) session.getAttribute(EnumSession.SESSION_POS.getName());
		//
		PosBO posBO = (PosBO) BOsMap.get(PosBO.class.getSimpleName());
		PosSyncCacheBO posSyncCacheBO = (PosSyncCacheBO) BOsMap.get(PosSyncCacheBO.class.getSimpleName());
		PosSyncCacheDispatcherBO posSyncCacheDispatcherBO = (PosSyncCacheDispatcherBO) BOsMap.get(PosSyncCacheDispatcherBO.class.getSimpleName());
		verifySyncDeleteSuccessResult(pos, posBO, posSyncCacheBO, posSyncCacheDispatcherBO, EnumSyncCacheType.ESCT_PosSyncInfo, pos2.getID(), dbName);
	}

	public static Pos recycleAppPosViaAction(Pos pos, MockMvc mvc, Map<String, BaseBO> BOsMap, String dbName) throws Exception {
		HttpSession session = Shared.getBXStaffLoginSession(mvc, Shared.PhoneOfOP);
		MvcResult mr = mvc.perform(//
				post("/pos/recycleApp.bx") //
						.param(Pos.field.getFIELD_NAME_ID(), String.valueOf(pos.getID())) //
						.param(Pos.field.getFIELD_NAME_returnSalt(), String.valueOf(EnumBoolean.EB_Yes.getIndex())) //
						.param(Pos.field.getFIELD_NAME_companySN(), pos.getCompanySN()) //
						.contentType(MediaType.APPLICATION_JSON) //
						.session((MockHttpSession) session) //
		)//
				.andExpect(status().isOk()).andDo(print()).andReturn();
		Shared.checkJSONErrorCode(mr);
		// ?????????
		PosCP.verifyRecycleApp(mr, pos, dbName);
		return pos;
	}

	public static void getPosLoginSession(MockMvc mvc, int iPosID, String companySN, String passwordInPOS, int resetPasswordInPos /*???1?????????Pos????????????NULL*/) throws Exception {
		Pos p = new Pos();
		p.setID(iPosID);
		// ..????????????
		MvcResult ret = mvc.perform(post("/pos/getTokenEx.bx") //
				.contentType(MediaType.APPLICATION_JSON) //
				.param(Pos.field.getFIELD_NAME_ID(), String.valueOf(iPosID))) //
				.andExpect(status().isOk()).andDo(print()).andReturn();

		String json = ret.getResponse().getContentAsString();
		JSONObject o = JSONObject.fromObject(json);
		String modulus = JsonPath.read(o, "$.rsa.modulus");
		String exponent = JsonPath.read(o, "$.rsa.exponent");
		modulus = new BigInteger(modulus, 16).toString();
		exponent = new BigInteger(exponent, 16).toString();

		RSAPublicKey publicKey = RSAUtils.getPublicKey(modulus, exponent);

		final String pwd = passwordInPOS;
		// ..????????????
		String pwdEncrypted = RSAUtils.encryptByPublicKey(pwd, publicKey);
		p.setPwdEncrypted(pwdEncrypted);

		MvcResult mr = mvc.perform(post("/pos/loginEx.bx") //
				.param(Pos.field.getFIELD_NAME_ID(), String.valueOf(iPosID)) //
				.param(Pos.field.getFIELD_NAME_pwdEncrypted(), pwdEncrypted) //
				.param(Pos.field.getFIELD_NAME_companySN(), companySN)//
				.param(Pos.field.getFIELD_NAME_resetPasswordInPos(), String.valueOf(resetPasswordInPos))//
				.session((MockHttpSession) ret.getRequest().getSession()) //
				.contentType(MediaType.APPLICATION_JSON)) //
				.andExpect(status().isOk()).andDo(print()).andReturn();

		Shared.checkJSONErrorCode(mr);
		System.out.println("pos?????????sessionId:" + mr.getRequest().getSession().getId());

		System.out.println("POS" + iPosID + "????????????");
	}
	
	@SuppressWarnings("unchecked")
	public static List<Pos> retrieveNViaAction(HttpSession session, MockMvc mvc, Pos pos) throws Exception {
		
		MvcResult mr3 = mvc.perform( //
				get("/pos/retrieveNEx.bx?" + Pos.field.getFIELD_NAME_status() + "=" + pos.getStatus() + "&shopID=" + pos.getShopID()) //
						.contentType(MediaType.APPLICATION_JSON) //
						.session((MockHttpSession) session)//
		) //
				.andExpect(status().isOk()) //
				.andDo(print()) //
				.andReturn();
		Shared.checkJSONErrorCode(mr3);
		return (List<Pos>) Shared.parseNObject(mr3, new Pos(), BaseAction.KEY_ObjectList);
	}
}
