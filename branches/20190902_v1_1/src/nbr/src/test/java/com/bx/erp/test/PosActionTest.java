package com.bx.erp.test;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.testng.Assert.assertTrue;

import java.math.BigInteger;
import java.security.interfaces.RSAPublicKey;
import java.util.Map;

import javax.servlet.http.HttpSession;

import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MvcResult;
import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import com.bx.erp.action.BaseAction;
import com.bx.erp.action.CompanyAction;
import com.bx.erp.action.bo.BaseBO;
import com.bx.erp.model.Pos;
import com.bx.erp.model.BaseModel.EnumBoolean;
import com.bx.erp.model.ErrorInfo.EnumErrorCode;
import com.bx.erp.model.Pos.EnumStatusPos;
import com.bx.erp.util.DataSourceContextHolder;
import com.bx.erp.util.RSAUtils;
import com.jayway.jsonpath.JsonPath;

import net.sf.json.JSONObject;

@WebAppConfiguration
public class PosActionTest extends BaseActionTest {
	public static final int RETURN_OBJECT = 1;

	@BeforeClass
	public void setup() {
		super.setUp();
	}

	@AfterClass
	public void tearDown() {
		super.tearDown();
	}

	// @Test
	// public void testUpdate() throws Exception {
	// Shared.printTestMethodStartInfo();
	//
	// Pos p = new Pos();
	//
	// MvcResult mr =
	// mvc.perform(post("/pos/updateEx.bx").param(p.getFIELD_NAME_ID(),
	// "1").param(p.getFIELD_NAME_pos_SN(),
	// "SN123456").param(p.getFIELD_NAME_shopID(), "1")
	// .contentType(MediaType.APPLICATION_JSON)).andExpect(status().isOk()).andDo(print()).andReturn();
	//
	// Shared.checkJSONErrorCode(mr);
	// }

	// @Test
	// public void testCreate() throws Exception {
	// Shared.printTestMethodStartInfo();
	//
	// Pos p = new Pos();
	//
	// // ????????????
	// MvcResult mr1 =
	// mvc.perform(post("/pos/createEx.bx").param(p.getFIELD_NAME_salt(),
	// "B1AFC07474C37C5AEC4199ED28E09705").param(p.getFIELD_NAME_pos_SN(), "SN" +
	// String.valueOf(System.currentTimeMillis()).substring(6)).param(p.getFIELD_NAME_shopID(),
	// "1").param(p.getFIELD_NAME_status(), "0")
	// .contentType(MediaType.APPLICATION_JSON)).andExpect(status().isOk()).andDo(print()).andReturn();
	//
	// Shared.checkJSONErrorCode(mr1);
	//
	// // ????????????
	// MvcResult mr2 =
	// mvc.perform(post("/pos/createEx.bx").param(p.getFIELD_NAME_salt(),
	// "B1AFC07474C37C5AEC4199ED28E09705").param(p.getFIELD_NAME_pos_SN(),
	// "SN23346348").param(p.getFIELD_NAME_shopID(),
	// "1").param(p.getFIELD_NAME_status(), "0")
	// .contentType(MediaType.APPLICATION_JSON)).andExpect(status().isOk()).andDo(print()).andReturn();
	//
	// Shared.checkJSONErrorCode(mr2,EnumErrorCode.EC_Duplicated);
	// }

	@Test
	public void testRetrieve1() throws Exception {
		Shared.printTestMethodStartInfo();

		HttpSession session = Shared.getStaffLoginSession(mvc, Shared.PhoneOfBoss);
		MvcResult mr = mvc.perform(//
				get("/pos/retrieve1Ex.bx?" + Pos.field.getFIELD_NAME_ID() + "=1")//
						.session((MockHttpSession) session)//
						.contentType(MediaType.APPLICATION_JSON)//
		)//
				.andExpect(status().isOk()).andDo(print()).andReturn();
		Shared.checkJSONErrorCode(mr);

		System.out.println("------------------------case2:????????????????????????-----------------------------");
//		HttpSession session2 = Shared.getStaffLoginSession(mvc, Shared.PhoneOfManager);
//		MvcResult mr2 = mvc.perform(//
//				get("/pos/retrieve1Ex.bx?" + Pos.field.getFIELD_NAME_ID() + "=1")//
//						.session((MockHttpSession) session2)//
//						.contentType(MediaType.APPLICATION_JSON)//
//		)//
//				.andExpect(status().isOk()).andDo(print()).andReturn();
//		Shared.checkJSONErrorCode(mr2, EnumErrorCode.EC_NoPermission);
	}

	@Test
	public void testRetrieve1BySN() throws Exception {
		Shared.printTestMethodStartInfo();

		HttpSession session = Shared.getPosLoginSession(mvc, 1);

		System.out.println("---------------------------------------------- case1: ??????SN??????POS ---------------------------------------");
		Pos pos = PosMapperTest.DataInput.getPOS();
		Map<String, Object> params = pos.getCreateParam(BaseBO.INVALID_CASE_ID, pos);
		// ????????????
		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		Pos posCreate = (Pos) posMapper.create(params);
		assertTrue(posCreate != null && EnumErrorCode.values()[Integer.parseInt(params.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())] == EnumErrorCode.EC_NoError, "??????????????????");

		pos.setIgnoreIDInComparision(true);
		if (pos.compareTo(posCreate) != 0) {
			assertTrue(false, "???????????????????????????DB??????????????????");
		}

		Map<String, Object> params2 = pos.getRetrieve1Param(BaseBO.INVALID_CASE_ID, posCreate);
		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		Pos posR1 = (Pos) posMapper.retrieve1(params2);

		assertTrue(posR1 != null && EnumErrorCode.values()[Integer.parseInt(params2.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())] == EnumErrorCode.EC_NoError, "??????????????????");

		pos.setIgnoreIDInComparision(true);
		if (pos.compareTo(posR1) != 0) {
			assertTrue(false, "???????????????????????????DB??????????????????");
		}
		System.out.println("????????????pos???????????????");

		MvcResult mr = mvc.perform(//
				get("/pos/retrieve1BySNEx.bx?" + Pos.field.getFIELD_NAME_pos_SN() + "=" + posR1.getPos_SN() + "&" + Pos.field.getFIELD_NAME_companySN() + "=" + Shared.DB_SN_Test)//
						.session((MockHttpSession) session)//
						.contentType(MediaType.APPLICATION_JSON)//
		)//
				.andExpect(status().isOk()).andDo(print()).andReturn();
		Shared.checkJSONErrorCode(mr);
		//
		String json = mr.getResponse().getContentAsString();
		JSONObject o = JSONObject.fromObject(json);
		int id = JsonPath.read(o, "$.pos.ID");
		if (id == 0) {
			assertTrue(false, "??????????????????POS???POS_ID?????????");
		}

		Map<String, Object> deleteParam2 = pos.getDeleteParam(BaseBO.INVALID_CASE_ID, posR1);
		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		posMapper.delete(deleteParam2);
		assertTrue(EnumErrorCode.values()[Integer.parseInt(deleteParam2.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())] == EnumErrorCode.EC_NoError, "??????????????????");

		System.out.println("---------------------------------------------- case2: ???????????????SN???POS????????????SN??????POS ---------------------------------------");
		String POS_SN = "??????SN";
		MvcResult mr2 = mvc.perform(//
				get("/pos/retrieve1BySNEx.bx?" + Pos.field.getFIELD_NAME_pos_SN() + "=" + POS_SN + "&" + Pos.field.getFIELD_NAME_companySN() + "=" + Shared.DB_SN_Test)//
						.session((MockHttpSession) session)//
						.contentType(MediaType.APPLICATION_JSON)//
		)//
				.andExpect(status().isOk()).andDo(print()).andReturn();
		Shared.checkJSONErrorCode(mr2, EnumErrorCode.EC_NoSuchData);
		//

		System.out.println("---------------------------------------------- case3: ???????????????POS ---------------------------------------");
		Pos pos3 = PosMapperTest.DataInput.getPOS();
		Map<String, Object> params3 = pos3.getCreateParam(BaseBO.INVALID_CASE_ID, pos3);
		// ????????????
		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		Pos posCreate3 = (Pos) posMapper.create(params3);
		assertTrue(posCreate3 != null && EnumErrorCode.values()[Integer.parseInt(params3.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())] == EnumErrorCode.EC_NoError, "??????????????????");

		pos3.setIgnoreIDInComparision(true);
		if (pos3.compareTo(posCreate3) != 0) {
			assertTrue(false, "???????????????????????????DB??????????????????");
		}

		Map<String, Object> params3R1 = pos.getRetrieve1Param(BaseBO.INVALID_CASE_ID, posCreate3);
		DataSourceContextHolder.setDbName(Shared.DBName_Test);

		Pos pos3R1 = (Pos) posMapper.retrieve1(params3R1);

		assertTrue(pos3R1 != null && EnumErrorCode.values()[Integer.parseInt(params3R1.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())] == EnumErrorCode.EC_NoError, "??????????????????");

		pos3.setIgnoreIDInComparision(true);
		if (pos3.compareTo(pos3R1) != 0) {
			assertTrue(false, "???????????????????????????DB??????????????????");
		}
		System.out.println("????????????pos???????????????");

		MvcResult mr3 = mvc.perform(//
				get("/pos/retrieve1BySNEx.bx?" + Pos.field.getFIELD_NAME_pos_SN() + "=" + pos3R1.getPos_SN() + "&" + Pos.field.getFIELD_NAME_companySN() + "=" + Shared.DB_SN_Test)//
						.session((MockHttpSession) session).contentType(MediaType.APPLICATION_JSON)//
		)//
				.andExpect(status().isOk()).andDo(print()).andReturn();
		Shared.checkJSONErrorCode(mr3);
		//
		String json3 = mr3.getResponse().getContentAsString();
		JSONObject o3 = JSONObject.fromObject(json3);
		int id3 = JsonPath.read(o3, "$.pos.ID");
		if (id3 == 0) {
			assertTrue(false, "??????????????????POS???POS_ID?????????");
		}

		Map<String, Object> deleteParam3 = pos.getDeleteParam(BaseBO.INVALID_CASE_ID, pos3R1);
		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		posMapper.delete(deleteParam3);
		assertTrue(EnumErrorCode.values()[Integer.parseInt(deleteParam3.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())] == EnumErrorCode.EC_NoError, "??????????????????");

		System.out.println("---------------------------------------------- case4: ?????????????????????POS ---------------------------------------");
		Pos pos4 = PosMapperTest.DataInput.getPOS();
		Map<String, Object> params4 = pos4.getCreateParam(BaseBO.INVALID_CASE_ID, pos4);
		// ????????????
		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		Pos posCreate4 = (Pos) posMapper.create(params4);
		assertTrue(posCreate4 != null && EnumErrorCode.values()[Integer.parseInt(params4.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())] == EnumErrorCode.EC_NoError, "??????????????????");

		pos4.setIgnoreIDInComparision(true);
		if (pos4.compareTo(posCreate4) != 0) {
			assertTrue(false, "???????????????????????????DB??????????????????");
		}
		// ??????
		Map<String, Object> params4R1 = pos4.getRetrieve1Param(BaseBO.INVALID_CASE_ID, posCreate4);

		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		Pos posR4 = (Pos) posMapper.retrieve1(params4R1);

		assertTrue(posR4 != null && EnumErrorCode.values()[Integer.parseInt(params4R1.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())] == EnumErrorCode.EC_NoError, "??????????????????");

		posCreate4.setIgnoreIDInComparision(true);
		if (posCreate4.compareTo(posR4) != 0) {
			assertTrue(false, "???????????????????????????DB??????????????????");
		}
		System.out.println("????????????pos???????????????");
		// ??????
		Map<String, Object> deleteParam = posR4.getDeleteParam(BaseBO.INVALID_CASE_ID, posR4);

		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		posMapper.delete(deleteParam);

		assertTrue(EnumErrorCode.values()[Integer.parseInt(deleteParam.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())] == EnumErrorCode.EC_NoError, "????????????");

		MvcResult mr4 = mvc.perform(//
				get("/pos/retrieve1BySNEx.bx?" + Pos.field.getFIELD_NAME_pos_SN() + "=" + posR4.getPos_SN() + "&" + Pos.field.getFIELD_NAME_companySN() + "=" + Shared.DB_SN_Test)//
						.session((MockHttpSession) session)//
						.contentType(MediaType.APPLICATION_JSON)//
		)//
				.andExpect(status().isOk()).andDo(print()).andReturn();
		Shared.checkJSONErrorCode(mr4, EnumErrorCode.EC_NoSuchData);

		System.out.println("---------------------------------------------- case5: ????????????????????????SN????????? ---------------------------------------");
		MvcResult mr5 = mvc.perform(//
				get("/pos/retrieve1BySNEx.bx?" + Pos.field.getFIELD_NAME_pos_SN() + "=" + pos3R1.getPos_SN() + "&" + Pos.field.getFIELD_NAME_companySN() + "=1203")//
						.session((MockHttpSession) session).contentType(MediaType.APPLICATION_JSON)//
		)//
				.andExpect(status().isOk()).andDo(print()).andReturn();
		Shared.checkJSONErrorCode(mr5, EnumErrorCode.EC_NoSuchData);

		System.out.println("---------------------------------------------- case6: ????????????DB?????????SN????????? ---------------------------------------");
		String publicDBSN = "668867";
		MvcResult mr6 = mvc.perform(//
				get("/pos/retrieve1BySNEx.bx?" + Pos.field.getFIELD_NAME_pos_SN() + "=" + publicDBSN + "&" + Pos.field.getFIELD_NAME_companySN() + "=" + Shared.DB_SN_Test)//
						.session((MockHttpSession) session).contentType(MediaType.APPLICATION_JSON)//
		)//
				.andExpect(status().isOk()).andDo(print()).andReturn();
		Shared.checkJSONErrorCode(mr6, EnumErrorCode.EC_NoSuchData);
	}

	@Test
	public void testRetrieveN1() throws Exception {
		Shared.printTestMethodStartInfo();

		// ????????????ID???
		MvcResult mr1 = mvc.perform(//
				get("/pos/retrieveNEx.bx?" + Pos.field.getFIELD_NAME_shopID() + "=1&status=-1")//
						.contentType(MediaType.APPLICATION_JSON)//
						.session((MockHttpSession) sessionBoss)//
		)//
				.andExpect(status().isOk())//
				.andDo(print())//
				.andReturn();
		Shared.checkJSONErrorCode(mr1);
	}

	@Test
	public void testRetrieveN2() throws Exception {
		// ??????POS_SN???
		MvcResult mr2 = mvc.perform( //
				get("/pos/retrieveNEx.bx?" + Pos.field.getFIELD_NAME_pos_SN() + "=SN&shopID=-1&status=-1") //
						.contentType(MediaType.APPLICATION_JSON) //
						.session((MockHttpSession) sessionBoss)//
		) //
				.andExpect(status().isOk()) //
				.andDo(print()) //
				.andReturn();
		Shared.checkJSONErrorCode(mr2);
	}

	@Test
	public void testRetrieveN3() throws Exception {
		// ??????status???
		MvcResult mr3 = mvc.perform( //
				get("/pos/retrieveNEx.bx?" + Pos.field.FIELD_NAME_status + "=" + EnumStatusPos.ESP_Active.getIndex() + "&shopID=-1") //
						.contentType(MediaType.APPLICATION_JSON) //
						.session((MockHttpSession) sessionBoss)//
		) //
				.andExpect(status().isOk()) //
				.andDo(print()) //
				.andReturn();
		Shared.checkJSONErrorCode(mr3);
	}

	@Test
	public void testRetrieveN4() throws Exception {
		// ????????????????????????
//		MvcResult mr4 = mvc.perform(//
//				get("/pos/retrieveNEx.bx?" + Pos.field.getFIELD_NAME_shopID() + "=1&status=-1")//
//						.contentType(MediaType.APPLICATION_JSON)//
//						.session((MockHttpSession) Shared.getStaffLoginSession(mvc, Shared.PhoneOfManager))//
//		)//
//				.andExpect(status().isOk())//
//				.andDo(print())//
//				.andReturn();
//		Shared.checkJSONErrorCode(mr4, EnumErrorCode.EC_NoPermission);
	}

	@Test
	public void testRetrieveN5() throws Exception {
		// ??????????????????
		MvcResult mr5 = mvc.perform(//
				get("/pos/retrieveNEx.bx?" + Pos.field.getFIELD_NAME_shopID() + "=1&status=2")//
						.contentType(MediaType.APPLICATION_JSON)//
						.session((MockHttpSession) sessionBoss)//
		)//
				.andExpect(status().isOk())//
				.andDo(print())//
				.andReturn();
		Shared.checkJSONErrorCode(mr5, EnumErrorCode.EC_OtherError);// PosAction???switch??????case???EC_WrongFormatForInputField?????????
	}

	// @Test
	// public void testDelete() throws Exception {
	// Shared.printTestMethodStartInfo();
	//
	// MvcResult mr =
	// mvc.perform(get("/pos/deleteEx.bx?ID=3").contentType(MediaType.APPLICATION_JSON)).andExpect(status().isOk()).andDo(print()).andReturn();
	//
	// Shared.checkJSONErrorCode(mr);
	// }

	@Test
	public void testLoginEx1() throws Exception {
		Shared.printTestMethodStartInfo();

		Shared.caseLog("-----------------------  ????????????  -------------------------");
		final String POS_ID = "2";

		Pos p = new Pos();
		// ..????????????
		MvcResult ret = mvc.perform(//
				post("/pos/getTokenEx.bx")//
						.contentType(MediaType.APPLICATION_JSON)//
						.param(Pos.field.getFIELD_NAME_ID(), POS_ID)//
		)//
				.andExpect(status().isOk())//
				.andDo(print())//
				.andReturn();

		String pwdEncrypted = Shared.encrypt(ret, Shared.PASSWORD_DEFAULT);
		p.setPwdEncrypted(pwdEncrypted);

		MvcResult mr = mvc.perform(//
				post("/pos/loginEx.bx").param(Pos.field.getFIELD_NAME_ID(), POS_ID)//
						.param(Pos.field.getFIELD_NAME_pwdEncrypted(), pwdEncrypted)//
						.param(Pos.field.getFIELD_NAME_companySN(), Shared.DB_SN_Test)//
						.session((MockHttpSession) ret.getRequest().getSession()).contentType(MediaType.APPLICATION_JSON))//
				.andExpect(status().isOk())//
				.andDo(print())//
				.andReturn();

		Shared.checkJSONErrorCode(mr);
	}

	@Test
	public void testLoginEx2() throws Exception {
		Shared.printTestMethodStartInfo();
		Shared.caseLog("----------------------------------- case2 : ??????????????????POS????????????----------------------");

		Pos p = new Pos();
		final String POS_ID2 = "6";

		MvcResult ret2 = mvc.perform(post("/pos/getTokenEx.bx").contentType(MediaType.APPLICATION_JSON).param(Pos.field.getFIELD_NAME_ID(), POS_ID2)).andExpect(status().isOk()).andDo(print()).andReturn();

		String json2 = ret2.getResponse().getContentAsString();
		JSONObject o2 = JSONObject.fromObject(json2);
		String modulus2 = JsonPath.read(o2, "$.rsa.modulus");
		String exponent2 = JsonPath.read(o2, "$.rsa.exponent");
		modulus2 = new BigInteger(modulus2, 16).toString();
		exponent2 = new BigInteger(exponent2, 16).toString();

		RSAPublicKey publicKey2 = RSAUtils.getPublicKey(modulus2, exponent2);

		final String pwd2 = Shared.PASSWORD_DEFAULT;
		// ..????????????
		String pwdEncrypted2 = RSAUtils.encryptByPublicKey(pwd2, publicKey2);
		p.setPwdEncrypted(pwdEncrypted2);

		MvcResult mr2 = mvc.perform(//
				post("/pos/loginEx.bx")//
						.param(Pos.field.getFIELD_NAME_ID(), POS_ID2)//
						.param(Pos.field.getFIELD_NAME_pwdEncrypted(), pwdEncrypted2)//
						.param(Pos.field.getFIELD_NAME_companySN(), Shared.DB_SN_Test)//
						.session((MockHttpSession) ret2.getRequest().getSession())//
						.contentType(MediaType.APPLICATION_JSON))//
				.andExpect(status().isOk()).andDo(print()).andReturn();

		Shared.checkJSONErrorCode(mr2, EnumErrorCode.EC_Hack);
	}

	@Test
	public void testLoginEx3() throws Exception {
		Shared.printTestMethodStartInfo();
		Shared.caseLog("----------------------------------- case3 : ??????????????????????????? ----------------------");

		Pos p = new Pos();
		final String POS_ID = "2";
		final String ErrorPassword = "666666";
		// ..????????????
		MvcResult ret = mvc.perform(//
				post("/pos/getTokenEx.bx")//
						.contentType(MediaType.APPLICATION_JSON)//
						.param(Pos.field.getFIELD_NAME_ID(), POS_ID)//
		)//
				.andExpect(status().isOk())//
				.andDo(print())//
				.andReturn();

		String pwdEncrypted = Shared.encrypt(ret, ErrorPassword);
		p.setPwdEncrypted(pwdEncrypted);

		MvcResult mr = mvc.perform(//
				post("/pos/loginEx.bx").param(Pos.field.getFIELD_NAME_ID(), POS_ID)//
						.param(Pos.field.getFIELD_NAME_pwdEncrypted(), pwdEncrypted)//
						.param(Pos.field.getFIELD_NAME_companySN(), Shared.DB_SN_Test)//
						.session((MockHttpSession) ret.getRequest().getSession()).contentType(MediaType.APPLICATION_JSON))//
				.andExpect(status().isOk())//
				.andDo(print())//
				.andReturn();

		Shared.checkJSONErrorCode(mr, EnumErrorCode.EC_NoSuchData);
	}

	@Test
	public void testLoginEx4() throws Exception {
		Shared.printTestMethodStartInfo();
		Shared.caseLog("----------------------------------- case4 : ???????????????????????????????????? ----------------------");

		Pos p = new Pos();
		final String ErrorPassword = "666666";
		// ..????????????
		MvcResult ret = mvc.perform(//
				post("/pos/getTokenEx.bx")//
						.contentType(MediaType.APPLICATION_JSON)//
		// .param(Pos.field.getFIELD_NAME_ID(), POS_ID)//
		)//
				.andExpect(status().isOk())//
				.andDo(print())//
				.andReturn();

		String pwdEncrypted = Shared.encrypt(ret, ErrorPassword);
		p.setPwdEncrypted(pwdEncrypted);

		MvcResult mr = mvc.perform(//
				post("/pos/loginEx.bx")//
						// .param(Pos.field.getFIELD_NAME_ID(), POS_ID)//
						.param(Pos.field.getFIELD_NAME_pwdEncrypted(), pwdEncrypted)//
						.param(Pos.field.getFIELD_NAME_companySN(), Shared.DB_SN_Test)//
						.session((MockHttpSession) ret.getRequest().getSession()).contentType(MediaType.APPLICATION_JSON))//
				.andExpect(status().isOk())//
				.andDo(print())//
				.andReturn();

		String json = mr.getResponse().getContentAsString();
		JSONObject o = JSONObject.fromObject(json);
		Assert.assertTrue(o.get(BaseAction.KEY_Object) == null);
		Shared.checkJSONErrorCode(mr, EnumErrorCode.EC_NoSuchData);
	}

	@Test
	public void testRepeatLogin() throws Exception {
		final int POS_ID = 2;

		Shared.getPosLoginSession(mvc, POS_ID);
	}

	@Test
	public void testRecycleApp1() throws Exception {
		Shared.printTestMethodStartInfo();

		Shared.caseLog("Case1:?????????????????????????????????app???????????????POS???");
		Pos pos = DataInput.getPOS();
		pos.setReturnSalt(EnumBoolean.EB_Yes.getIndex());
		Pos posCreate = BasePosTest.createPosViaSyncAction(pos, mvc, mapBO, Shared.DBName_Test);
		posCreate.setCompanySN(pos.getCompanySN()); // ????????????????????????????????????session??????
		//
		posCreate.setReturnSalt(EnumBoolean.EB_Yes.getIndex());
		posCreate.setResetPasswordInPos(EnumBoolean.EB_Yes.getIndex());
		BasePosTest.getPosLoginSession(mvc, posCreate.getID(), Shared.DB_SN_Test, posCreate.getPasswordInPOS(), posCreate.getResetPasswordInPos());
		//
		posCreate.setReturnSalt(EnumBoolean.EB_Yes.getIndex());
		BasePosTest.recycleAppPosViaAction(posCreate, mvc, mapBO, Shared.DBName_Test);
		//
		BasePosTest.deletePosViaSyncAction(posCreate, mvc, mapBO, Shared.DBName_Test);
	}

	@Test
	public void testRecycleApp2() throws Exception {
		Shared.printTestMethodStartInfo();

		Shared.caseLog("Case2:??????????????????????????????app???????????????POS???");
		Pos pos = DataInput.getPOS();
		pos.setReturnSalt(EnumBoolean.EB_Yes.getIndex());
		Pos posCreate = BasePosTest.createPosViaSyncAction(pos, mvc, mapBO, Shared.DBName_Test);
		posCreate.setCompanySN(pos.getCompanySN());
		//
		posCreate.setReturnSalt(EnumBoolean.EB_Yes.getIndex());
		BasePosTest.recycleAppPosViaAction(posCreate, mvc, mapBO, Shared.DBName_Test);
		//
		BasePosTest.deletePosViaSyncAction(posCreate, mvc, mapBO, Shared.DBName_Test);
	}

	@Test
	public void testRecycleApp3() throws Exception {
		Shared.printTestMethodStartInfo();

		Shared.caseLog("Case3:???????????????????????????app???????????????POS???");
		Pos pos = DataInput.getPOS();
		pos.setReturnSalt(EnumBoolean.EB_Yes.getIndex());
		Pos posCreate = BasePosTest.createPosViaSyncAction(pos, mvc, mapBO, Shared.DBName_Test);
		posCreate.setCompanySN(pos.getCompanySN());
		//
		posCreate.setReturnSalt(EnumBoolean.EB_Yes.getIndex());
		posCreate.setResetPasswordInPos(EnumBoolean.EB_Yes.getIndex());
		BasePosTest.getPosLoginSession(mvc, posCreate.getID(), Shared.DB_SN_Test, posCreate.getPasswordInPOS(), posCreate.getResetPasswordInPos());
		//
		BasePosTest.deletePosViaSyncAction(posCreate, mvc, mapBO, Shared.DBName_Test);
		//
		posCreate.setReturnSalt(EnumBoolean.EB_Yes.getIndex());
		HttpSession sessionOP = Shared.getBXStaffLoginSession(mvc, Shared.PhoneOfOP);
		MvcResult mr = mvc.perform(//
				post("/pos/recycleApp.bx") //
						.param(Pos.field.getFIELD_NAME_ID(), String.valueOf(posCreate.getID())) //
						.param(Pos.field.getFIELD_NAME_returnSalt(), String.valueOf(posCreate.getReturnSalt())) //
						.param(Pos.field.getFIELD_NAME_companySN(), pos.getCompanySN()) //
						.contentType(MediaType.APPLICATION_JSON) //
						.session((MockHttpSession) sessionOP) //
		)//
				.andExpect(status().isOk()).andDo(print()).andReturn();
		Shared.checkJSONErrorCode(mr, EnumErrorCode.EC_BusinessLogicNotDefined);
	}

	@Test
	public void testRecycleApp4() throws Exception {
		Shared.printTestMethodStartInfo();

		Shared.caseLog("Case4:???????????????????????????POS???");
		Pos pos = DataInput.getPOS();
		pos.setID(Shared.BIG_ID);
		pos.setReturnSalt(EnumBoolean.EB_Yes.getIndex());
		HttpSession sessionOP = Shared.getBXStaffLoginSession(mvc, Shared.PhoneOfOP);
		MvcResult mr = mvc.perform(//
				post("/pos/recycleApp.bx") //
						.param(Pos.field.getFIELD_NAME_ID(), String.valueOf(pos.getID())) //
						.param(Pos.field.getFIELD_NAME_returnSalt(), String.valueOf(pos.getReturnSalt())) //
						.param(Pos.field.getFIELD_NAME_companySN(), pos.getCompanySN()) //
						.contentType(MediaType.APPLICATION_JSON) //
						.session((MockHttpSession) sessionOP) //
		)//
				.andExpect(status().isOk()).andDo(print()).andReturn();
		Shared.checkJSONErrorCode(mr, EnumErrorCode.EC_BusinessLogicNotDefined);
	}

	@Test
	public void testRecycleApp5() throws Exception {
		Shared.caseLog("case5:??????????????????????????????");
		HttpSession sessionPreSale = Shared.getStaffLoginSession(mvc, BaseAction.ACCOUNT_Phone_PreSale);
		Pos pos = DataInput.getPOS();
		pos.setID(1);
		MvcResult mr = mvc.perform(//
				post("/pos/recycleApp.bx") //
						.param(Pos.field.getFIELD_NAME_ID(), String.valueOf(pos.getID())) //
						.param(Pos.field.getFIELD_NAME_returnSalt(), String.valueOf(pos.getReturnSalt())) //
						.param(Pos.field.getFIELD_NAME_companySN(), pos.getCompanySN()) //
						.contentType(MediaType.APPLICATION_JSON) //
						.session((MockHttpSession) sessionPreSale) //
		)//
				.andExpect(status().isOk()).andDo(print()).andReturn();
		String json = mr.getResponse().getContentAsString();
		Assert.assertTrue("".equals(json), "????????????????????????");
	}

	@Test
	public void testRecycleApp6() throws Exception {
		Shared.caseLog("Case6:?????????????????????????????????");
//		HttpSession sessionManager = Shared.getStaffLoginSession(mvc, Shared.PhoneOfManager);
//		Pos pos = DataInput.getPOS();
//		pos.setID(1);
//		MvcResult mr = mvc.perform(//
//				post("/pos/recycleApp.bx") //
//						.param(Pos.field.getFIELD_NAME_ID(), String.valueOf(pos.getID())) //
//						.param(Pos.field.getFIELD_NAME_returnSalt(), String.valueOf(pos.getReturnSalt())) //
//						.param(Pos.field.getFIELD_NAME_companySN(), pos.getCompanySN()) //
//						.contentType(MediaType.APPLICATION_JSON) //
//						.session((MockHttpSession) sessionManager) //
//		)//
//				.andExpect(status().isOk()).andDo(print()).andReturn();
//		String json = mr.getResponse().getContentAsString();
//		Assert.assertTrue("".equals(json), "????????????????????????");
	}

	@Test
	public void testRecycleApp7() throws Exception {
		Shared.caseLog("Case7:??????????????????????????????");
		HttpSession sessionBoss = Shared.getStaffLoginSession(mvc, Shared.PhoneOfBoss);
		Pos pos = DataInput.getPOS();
		pos.setID(1);
		MvcResult mr = mvc.perform(//
				post("/pos/recycleApp.bx") //
						.param(Pos.field.getFIELD_NAME_ID(), String.valueOf(pos.getID())) //
						.param(Pos.field.getFIELD_NAME_returnSalt(), String.valueOf(pos.getReturnSalt())) //
						.param(Pos.field.getFIELD_NAME_companySN(), pos.getCompanySN()) //
						.contentType(MediaType.APPLICATION_JSON) //
						.session((MockHttpSession) sessionBoss) //
		)//
				.andExpect(status().isOk()).andDo(print()).andReturn();
		String json = mr.getResponse().getContentAsString();
		Assert.assertTrue("".equals(json), "????????????????????????");
	}

	@Test
	public void testRecycleApp8() throws Exception {
		Shared.caseLog("Case8:?????????????????????????????????");
		HttpSession sessionBoss = Shared.getStaffLoginSession(mvc, Shared.PhoneOfAssistManager);
		Pos pos = DataInput.getPOS();
		pos.setID(1);
		MvcResult mr = mvc.perform(//
				post("/pos/recycleApp.bx") //
						.param(Pos.field.getFIELD_NAME_ID(), String.valueOf(pos.getID())) //
						.param(Pos.field.getFIELD_NAME_returnSalt(), String.valueOf(pos.getReturnSalt())) //
						.param(Pos.field.getFIELD_NAME_companySN(), pos.getCompanySN()) //
						.contentType(MediaType.APPLICATION_JSON) //
						.session((MockHttpSession) sessionBoss) //
		)//
				.andExpect(status().isOk()).andDo(print()).andReturn();
		String json = mr.getResponse().getContentAsString();
		Assert.assertTrue("".equals(json), "????????????????????????");
	}

	@Test
	public void testRecycleApp9() throws Exception {
		Shared.caseLog("Case9:???????????????????????????");
		HttpSession sessionOP = Shared.getBXStaffLoginSession(mvc, Shared.PhoneOfOP);
		Pos pos = DataInput.getPOS();
		pos.setID(1);
		pos.setCompanySN(Shared.DB_SNNotExist_Test);
		MvcResult mr = mvc.perform(//
				post("/pos/recycleApp.bx") //
						.param(Pos.field.getFIELD_NAME_ID(), String.valueOf(pos.getID())) //
						.param(Pos.field.getFIELD_NAME_returnSalt(), String.valueOf(pos.getReturnSalt())) //
						.param(Pos.field.getFIELD_NAME_companySN(), pos.getCompanySN()) //
						.contentType(MediaType.APPLICATION_JSON) //
						.session((MockHttpSession) sessionOP) //
		)//
				.andExpect(status().isOk()).andDo(print()).andReturn();
		Shared.checkJSONErrorCode(mr, EnumErrorCode.EC_BusinessLogicNotDefined);
		Shared.checkJSONMsg(mr, CompanyAction.ERROR_MSG_CompanyNotExist, "CompanySN????????????????????????");
	}

	@Test
	public void testRecycleApp10() throws Exception {
		Shared.caseLog("Case10:???OP????????????BXStaff????????????????????????app");
		HttpSession session1 = Shared.getBXStaffLoginSession(mvc, Shared.PhoneOfBoss);
		Pos pos = DataInput.getPOS();
		pos.setID(1);
		MvcResult mr = mvc.perform(//
				post("/pos/recycleApp.bx") //
						.param(Pos.field.getFIELD_NAME_ID(), String.valueOf(pos.getID())) //
						.param(Pos.field.getFIELD_NAME_returnSalt(), String.valueOf(pos.getReturnSalt())) //
						.param(Pos.field.getFIELD_NAME_companySN(), pos.getCompanySN()) //
						.contentType(MediaType.APPLICATION_JSON) //
						.session((MockHttpSession) session1) //
		)//
				.andExpect(status().isOk()).andDo(print()).andReturn();
		String json = mr.getResponse().getContentAsString();
		Assert.assertTrue("".equals(json), "????????????????????????");
	}
}
