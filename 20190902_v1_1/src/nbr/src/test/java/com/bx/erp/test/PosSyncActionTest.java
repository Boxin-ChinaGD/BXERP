package com.bx.erp.test;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.testng.Assert.assertTrue;

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

import com.bx.erp.model.ErrorInfo.EnumErrorCode;
import com.bx.erp.action.BaseAction;
import com.bx.erp.action.BaseAction.EnumSession;
import com.bx.erp.action.bo.BaseBO;
import com.bx.erp.cache.CacheManager;
import com.bx.erp.model.Pos;
import com.bx.erp.model.CacheType.EnumCacheType;
import com.bx.erp.model.Company;
import com.bx.erp.model.ErrorInfo;
import com.bx.erp.model.SyncCacheType.EnumSyncCacheType;
import com.bx.erp.util.DataSourceContextHolder;
import com.bx.erp.util.FieldFormat;
import com.jayway.jsonpath.JsonPath;

import net.sf.json.JSONObject;

@WebAppConfiguration
public class PosSyncActionTest extends BaseActionTest {
	private static final int INVALID_ID = 999999999;

	EnumCacheType ect = EnumCacheType.ECT_POS;
	EnumSyncCacheType esct = EnumSyncCacheType.ESCT_PosSyncInfo;

	@BeforeClass
	public void setup() {
		super.setUp();

		try {
			Shared.resetPOS(mvc, 1);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@AfterClass
	public void tearDown() {
		super.tearDown();
	}

	// 用于删除创建的POS，不然其他测试在delte *SyncCache时会出现问题。
	protected void deletePos(int id) throws Exception {

		Pos pos = new Pos();
		pos.setID(id);
		Map<String, Object> deleteParam = pos.getDeleteParam(BaseBO.INVALID_CASE_ID, pos);
		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		posMapper.delete(deleteParam);
		assertTrue(EnumErrorCode.values()[Integer.parseInt(deleteParam.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())] == EnumErrorCode.EC_NoError, "删除对象失败，" + deleteParam.get(BaseAction.SP_OUT_PARAM_sErrorMsg));

		CacheManager.getCache(Shared.DBName_Test, EnumCacheType.ECT_POS).delete1(pos);
	}

	@Test
	public void testCreateEx() throws Exception {
		Shared.printTestMethodStartInfo();

		Shared.caseLog("Case1:正常添加POS");
		Pos pos = DataInput.getPOS();
		BasePosTest.createPosViaSyncAction(pos, mvc, mapBO, Shared.DBName_Test);
	}

	@Test
	public void testCreateEx2() throws Exception {
		Shared.printTestMethodStartInfo();
		Shared.caseLog("Case2:重复添加POS");

		Pos pos = DataInput.getPOS();
		BasePosTest.createPosViaSyncAction(pos, mvc, mapBO, Shared.DBName_Test);
		// 重复添加
		MvcResult mr2 = mvc.perform(//
				getPosBuilder("/posSync/createEx.bx", MediaType.APPLICATION_JSON, pos) //
						.contentType(MediaType.APPLICATION_JSON) //
						.session((MockHttpSession) sessionOP) //
		)//
				.andExpect(status().isOk())//
				.andDo(print())//
				.andReturn();
		Shared.checkJSONErrorCode(mr2, EnumErrorCode.EC_Duplicated);
	}

	@Test
	public void testCreateEx3() throws Exception {
		Shared.printTestMethodStartInfo();
		Shared.caseLog("Case3:无posID传递");
		// 无posID传递
		Pos pos2 = DataInput.getPOS();

		MvcResult mr3 = mvc.perform(//
				getPosBuilder("/posSync/createEx.bx", MediaType.APPLICATION_JSON, pos2) //
						.session((MockHttpSession) sessionOP) //
						.contentType(MediaType.APPLICATION_JSON) //
		)//
				.andExpect(status().isOk())//
				.andDo(print())//
				.andReturn();
		Shared.checkJSONErrorCode(mr3);
		// 删除本次测试创建成功的pos
		int id = Shared.parse1Object(mr3, pos2, BaseAction.KEY_Object).getID();
		deletePos(id);
	}

	@Test
	public void testCreateEx4() throws Exception {
		Shared.printTestMethodStartInfo();
		Shared.caseLog("Case4: 用不存在门店ID创建POS");
		Pos pos3 = DataInput.getPOS();
		pos3.setShopID(INVALID_ID);
		MvcResult mr4 = mvc.perform(//
				getPosBuilder("/posSync/createEx.bx", MediaType.APPLICATION_JSON, pos3) //
						.contentType(MediaType.APPLICATION_JSON) //
						.session((MockHttpSession) sessionOP) //
		)//
				.andExpect(status().isOk())//
				.andDo(print())//
				.andReturn();
		Shared.checkJSONErrorCode(mr4, EnumErrorCode.EC_BusinessLogicNotDefined);
	}

	// 该测试意义不明确，也没有正确起到测试用例的结果，后面想起来再重新编写
	// @Test
	// public void testCreateEx5() throws Exception {
	// Shared.printTestMethodStartInfo();
	// Shared.caseLog("case5:用于检查死循环：添加一个可用的pos机，一个不可用的pos机");
	// Pos posTest = DataInput.getPOS();
	// posTest.setStatus(0);
	// MvcResult retTest = mvc.perform(//
	// post("/pos/getTokenEx.bx")//
	// .contentType(MediaType.APPLICATION_JSON)//
	// .param(Pos.field.getFIELD_NAME_passwordInPOS(), Shared.PASSWORD_DEFAULT)//
	// )//
	// .andExpect(status().isOk())//
	// .andDo(print())//
	// .andReturn();
	// // 结果验证：检查错误码
	// Shared.checkJSONErrorCode(retTest);
	//
	// Pos posTest2 = DataInput.getPOS();
	// posTest2.setStatus(1);
	// MvcResult retTest2 = mvc.perform(//
	// post("/pos/getTokenEx.bx")//
	// .contentType(MediaType.APPLICATION_JSON)//
	// .session((MockHttpSession) session) //
	// .param(Pos.field.getFIELD_NAME_passwordInPOS(), Shared.PASSWORD_DEFAULT)//
	// )//
	// .andExpect(status().isOk())//
	// .andDo(print())//
	// .andReturn();
	// // 结果验证：检查错误码
	// Shared.checkJSONErrorCode(retTest2);
	// }

	@Test
	public void testCreateEx6() throws Exception {
		Shared.printTestMethodStartInfo();
		Shared.caseLog("Case6:使用已经删除的POS_SN进行创建");
		// 创建并删除一个pos机
		Pos pos4 = DataInput.getPOS();
		pos4.setStatus(Pos.EnumStatusPos.ESP_Active.getIndex());
		Map<String, Object> params4 = pos4.getCreateParam(BaseBO.INVALID_CASE_ID, pos4);
		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		Pos posCreate4 = (Pos) posMapper.create(params4);
		assertTrue(posCreate4 != null && EnumErrorCode.values()[Integer.parseInt(params4.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())] == EnumErrorCode.EC_NoError, "创建对象成功");
		pos4.setIgnoreIDInComparision(true);
		if (pos4.compareTo(posCreate4) != 0) {
			assertTrue(false, "创建的对象的字段与DB读出的不相等");
		}
		//
		deletePos(posCreate4.getID());

		MvcResult mr1 = mvc.perform(//
				getPosBuilder("/posSync/createEx.bx", MediaType.APPLICATION_JSON, pos4) //
						.contentType(MediaType.APPLICATION_JSON) //
						.session((MockHttpSession) sessionOP) //
		)//
				.andExpect(status().isOk())//
				.andDo(print())//
				.andReturn();
		// 结果验证：检查错误码
		Shared.checkJSONErrorCode(mr1);
		//
		// 删除本次测试创建成功的pos
		String json1 = mr1.getResponse().getContentAsString();
		JSONObject o1 = JSONObject.fromObject(json1);
		int id1 = JsonPath.read(o1, "$.object.ID");
		deletePos(id1);
	}

	@Test
	public void testCreateEx7() throws Exception {
		Shared.printTestMethodStartInfo();
		Shared.caseLog("Case7:使用已经删除的POS_SN进行创建");

		Pos pos5 = DataInput.getPOS();
		pos5.setPos_SN("");
		MvcResult mr5 = mvc.perform(//
				getPosBuilder("/posSync/createEx.bx", MediaType.APPLICATION_JSON, pos5) //
						.contentType(MediaType.APPLICATION_JSON) //
						.session((MockHttpSession) sessionOP) //
		)//
				.andExpect(status().isOk())//
				.andDo(print())//
				.andReturn();
		// 结果验证：检查错误码
		Shared.checkJSONErrorCode(mr5, EnumErrorCode.EC_WrongFormatForInputField);
	}

	@Test
	public void testCreateEx8() throws Exception {
		Shared.printTestMethodStartInfo();
		Shared.caseLog("Case8:一台pos机不能被两个公司使用");
		//
		Pos pos = DataInput.getPOS();
		MvcResult mr = mvc.perform(//
				getPosBuilder("/posSync/createEx.bx", MediaType.APPLICATION_JSON, pos) //
						.contentType(MediaType.APPLICATION_JSON) //
						.session((MockHttpSession) sessionOP) //
		)//
				.andExpect(status().isOk())//
				.andDo(print())//
				.andReturn();
		// 结果验证：检查错误码
		Shared.checkJSONErrorCode(mr);
		// 验证缓存中的结果
		String json1 = mr.getResponse().getContentAsString();
		JSONObject o1 = JSONObject.fromObject(json1);
		int createPosID = JsonPath.read(o1, "$.object.ID");
		//
		Pos poss = (Pos) mr.getRequest().getSession().getAttribute(EnumSession.SESSION_POS.getName());
		int posID = poss.getID();
		verifySyncCreateResult(mr, pos, posBO, posSyncCacheBO, posSyncCacheDispatcherBO, ect, esct, posID, Shared.DBName_Test);// posID > 0代表pos机发送的请求
		// 创建公司
		Company company = BaseCompanyTest.DataInput.getCompany();
		Company createComapny = BaseCompanyTest.createCompanyViaAction(company, mvc, mapBO, sessionOP);
		pos.setCompanySN(createComapny.getSN());
		// 使用相同的POS在不同的公司创建
		MvcResult mr2 = mvc.perform(//
				getPosBuilder("/posSync/createEx.bx", MediaType.APPLICATION_JSON, pos) //
						.contentType(MediaType.APPLICATION_JSON) //
						.session((MockHttpSession) sessionOP) //
		)//
				.andExpect(status().isOk())//
				.andDo(print())//
				.andReturn();
		// 结果验证：检查错误码
		Shared.checkJSONErrorCode(mr2, EnumErrorCode.EC_Duplicated);
		// 删除创建的POS机
		deletePos(createPosID);
	}

	@Test
	public void testCreateEx9() throws Exception {
		Shared.printTestMethodStartInfo();
		Shared.caseLog("Case9:非OP账号进行BXStaff登录进行创建pos");
		//
		Pos pos = DataInput.getPOS();
		HttpSession session1 = Shared.getBXStaffLoginSession(mvc, Shared.PhoneOfBoss);
		MvcResult mr = mvc.perform(//
				getPosBuilder("/posSync/createEx.bx", MediaType.APPLICATION_JSON, pos) //
						.contentType(MediaType.APPLICATION_JSON) //
						.session((MockHttpSession) session1) //
		)//
				.andExpect(status().isOk())//
				.andDo(print())//
				.andReturn();
		//
		String json = mr.getResponse().getContentAsString();
		Assert.assertTrue("".equals(json), "测试预期结果为空");
	}

	@Test
	public void testCreateEx10() throws Exception {
		Shared.printTestMethodStartInfo();
		Shared.caseLog("Case10:非OP账号进行Staff登录进行创建pos");
		//
		Pos pos = DataInput.getPOS();
		HttpSession session1 = Shared.getStaffLoginSession(mvc, Shared.PhoneOfBoss);
		MvcResult mr = mvc.perform(//
				getPosBuilder("/posSync/createEx.bx", MediaType.APPLICATION_JSON, pos) //
						.contentType(MediaType.APPLICATION_JSON) //
						.session((MockHttpSession) session1) //
		)//
				.andExpect(status().isOk())//
				.andDo(print())//
				.andReturn();
		//
		String json = mr.getResponse().getContentAsString();
		Assert.assertTrue("".equals(json), "测试预期结果为空");
	}

	// @Test
	// public void testUpdateEx() throws Exception {
	// Shared.printTestMethodStartInfo();
	// Shared.caseLog("Case1:正常更新");
	// session = Shared.getBXStaffLoginSession(mvc, Shared.PhoneOfOP);
	// // 正常添加
	// DataSourceContextHolder.setDbName(Shared.DBName_Test);
	// Pos posCreate = createPosWithMapper();
	//
	// EnumCacheType ect = EnumCacheType.ECT_POS;
	// EnumSyncCacheType esct = EnumSyncCacheType.ESCT_PosSyncInfo;
	//
	// Pos pos2 = DataInput.getPOS();
	// pos2.setID(posCreate.getID());
	//
	// MvcResult mr = mvc.perform(getPosBuilder("/posSync/updateEx.bx",
	// MediaType.APPLICATION_JSON, pos2) //
	// .contentType(MediaType.APPLICATION_JSON) //
	// .session((MockHttpSession) session) //
	// ).andExpect(status().isOk()).andDo(print()).andReturn();
	// // 结果验证:检查错误码
	// Shared.checkJSONErrorCode(mr);
	// // 结果验证：检查缓存结果
	// Pos p = (Pos) session.getAttribute(EnumSession.SESSION_POS.getName());
	// int posID = p.getID();
	// verifySyncUpdateResult(mr, pos2, posBO, posSyncCacheBO,
	// posSyncCacheDispatcherBO, ect, esct, posID, Shared.DBName_Test);
	// deletePos(posCreate.getID());
	// }

	// @Test
	// public void testUpdateEx2() throws Exception {
	// Shared.printTestMethodStartInfo();
	// Shared.caseLog("Case2:无POSID更新");
	// // 正常添加
	// Pos posCreate = createPosWithMapper();
	// Pos pos2 = DataInput.getPOS();
	// pos2.setID(posCreate.getID());
	// // 无posid
	// MvcResult mr2 = mvc.perform(getPosBuilder("/posSync/updateEx.bx",
	// MediaType.APPLICATION_JSON, pos2) //
	// .session((MockHttpSession) Shared.getStaffLoginSession(mvc,
	// Shared.PhoneOfBoss)) //
	// .contentType(MediaType.APPLICATION_JSON) //
	// ).andExpect(status().isOk()).andDo(print()).andReturn();
	// Shared.checkJSONErrorCode(mr2, EnumErrorCode.EC_NoError);
	// deletePos(posCreate.getID());
	// }

	// @Test
	// public void testUpdateEx3() throws Exception {
	// Shared.printTestMethodStartInfo();
	// Shared.caseLog("Case3:用不存在的shopID更新");
	//
	// Pos posCreate = createPosWithMapper();
	// // 用不存在的shopID进行更新
	// Pos pos3 = DataInput.getPOS();
	// pos3.setID(posCreate.getID());
	// pos3.setShopID(INVALID_ID);
	//
	// MvcResult mr3 = mvc.perform(getPosBuilder("/posSync/updateEx.bx",
	// MediaType.APPLICATION_JSON, pos3) //
	// .contentType(MediaType.APPLICATION_JSON) //
	// .session((MockHttpSession) session) //
	// ).andExpect(status().isOk()).andDo(print()).andReturn();
	//
	// Shared.checkJSONErrorCode(mr3, EnumErrorCode.EC_BusinessLogicNotDefined);
	//
	// deletePos(posCreate.getID());
	// }

	@Test
	public void testFeedbackEx() throws Exception {
		Shared.printTestMethodStartInfo();

		MvcResult mr = mvc.perform(get("/posSync/feedbackEx.bx?sID=1,2&errorCode=EC_NoError") //
				.contentType(MediaType.APPLICATION_JSON) //
				.session((MockHttpSession) Shared.getPosLoginSession(mvc, 2))) //
				.andExpect(status().isOk()).andDo(print()).andReturn();

		Shared.checkJSONErrorCode(mr);
	}

	@Test
	public void testRetrieveNEx() throws Exception {
		Shared.printTestMethodStartInfo();

		Shared.resetPOS(mvc, 2);
		//
		MvcResult mr = mvc.perform( //
				get("/posSync/retrieveNEx.bx") //
						.contentType(MediaType.APPLICATION_JSON) //
						.session((MockHttpSession) Shared.getPosLoginSession(mvc, 2))) //
				.andExpect(status().isOk())//
				.andDo(print())//
				.andReturn();

		Shared.checkJSONErrorCode(mr);
	}

	@Test
	public void testDeleteEx() throws Exception {
		Shared.printTestMethodStartInfo();
		Shared.caseLog("case1:正常删除");

		// 正常添加
		Pos posCreate = createPosWithMapper();

		MvcResult mr = mvc.perform(get("/posSync/deleteEx.bx?" + Pos.field.getFIELD_NAME_ID() + "=" + posCreate.getID() + "&" + Pos.field.getFIELD_NAME_companySN() + "=" + Shared.DB_SN_Test) //
				.contentType(MediaType.APPLICATION_JSON) //
				.session((MockHttpSession) sessionOP) //
		).andExpect(status().isOk()).andDo(print()).andReturn();
		//
		Shared.checkJSONErrorCode(mr);
		// 结果验证：检查普通缓存中不存在这条数据
		ErrorInfo ecOut = new ErrorInfo();
		Pos bm = (Pos) CacheManager.getCache(Shared.DBName_Test, ect).read1(posCreate.getID(), BaseBO.SYSTEM, ecOut, Shared.DBName_Test);
		Assert.assertTrue(bm.getStatus() == 1, "普通缓存存在本次创建的对象");
		// 结果验证：验证缓存中的结果
		Pos pos2 = (Pos) sessionBoss.getAttribute(EnumSession.SESSION_POS.getName());
		int posID = pos2.getID();
		verifySyncDeleteSuccessResult(posCreate, posBO, posSyncCacheBO, posSyncCacheDispatcherBO, esct, posID, Shared.DBName_Test);
	}

	@Test
	public void testDeleteEx2() throws Exception {
		Shared.printTestMethodStartInfo();

		Shared.caseLog("case2:没有POSID");
		Pos posCreate4 = createPosWithMapper();
		//
		MvcResult mr2 = mvc.perform(//
				get("/posSync/deleteEx.bx?" + Pos.field.getFIELD_NAME_ID() + "=" + posCreate4.getID() + "&" + Pos.field.getFIELD_NAME_companySN() + "=" + Shared.DB_SN_Test) //
						.session((MockHttpSession) sessionOP) //
						.contentType(MediaType.APPLICATION_JSON) //
		).andExpect(status().isOk()).andDo(print()).andReturn();
		Shared.checkJSONErrorCode(mr2, EnumErrorCode.EC_NoError);
	}

	@Test
	public void testDeleteEx3() throws Exception {
		Shared.printTestMethodStartInfo();

		Shared.caseLog("case3:未传递公司的SN");
		Pos posCreate5 = createPosWithMapper();
		//
		MvcResult mr3 = mvc.perform(//
				get("/posSync/deleteEx.bx?" + Pos.field.getFIELD_NAME_ID() + "=" + posCreate5.getID()) //
						.session((MockHttpSession) sessionOP) //
						.contentType(MediaType.APPLICATION_JSON) //
		).andExpect(status().isOk()).andDo(print()).andReturn();
		//
		Assert.assertEquals("", mr3.getResponse().getContentAsString());

		deletePos(posCreate5.getID());
	}

	@Test
	public void testDeleteEx4() throws Exception {
		Shared.printTestMethodStartInfo();

		Shared.caseLog("case4:非OP账号进行BXStaff登录进行删除pos");
		HttpSession session1 = Shared.getBXStaffLoginSession(mvc, Shared.PhoneOfBoss);
		//
		MvcResult mr = mvc.perform(//
				get("/posSync/deleteEx.bx?" + Pos.field.getFIELD_NAME_ID() + "=1") //
						.session((MockHttpSession) session1) //
						.contentType(MediaType.APPLICATION_JSON) //
		).andExpect(status().isOk()).andDo(print()).andReturn();
		//
		String json = mr.getResponse().getContentAsString();
		Assert.assertTrue("".equals(json), "测试预期结果为空");
	}

	@Test
	public void testDeleteEx5() throws Exception {
		Shared.printTestMethodStartInfo();

		Shared.caseLog("case5:非OP账号进行Staff登录进行删除pos");
		HttpSession session1 = Shared.getStaffLoginSession(mvc, Shared.PhoneOfBoss);
		//
		MvcResult mr = mvc.perform(//
				get("/posSync/deleteEx.bx?" + Pos.field.getFIELD_NAME_ID() + "=1") //
						.session((MockHttpSession) session1) //
						.contentType(MediaType.APPLICATION_JSON) //
		).andExpect(status().isOk()).andDo(print()).andReturn();
		//
		String json = mr.getResponse().getContentAsString();
		Assert.assertTrue("".equals(json), "测试预期结果为空");
	}

	@Test
	public void testResetEx() throws Exception {
		Shared.printTestMethodStartInfo();
		Shared.caseLog("Case1:重置一个已经删除POS机");

		sessionBoss = Shared.getPosLoginSession(mvc, 1);
		// 创建一个状态为1的POS机
		Pos posCreate = createPosWithMapper();
		//
		deletePos(posCreate.getID());
		//
		MvcResult mr = mvc.perform(//
				get("/posSync/resetEx.bx?" + Pos.field.getFIELD_NAME_ID() + "=" + posCreate.getID() + "&" + Pos.field.getFIELD_NAME_returnObject() + "=" + 1)//
						.session((MockHttpSession) sessionBoss)//
						.contentType(MediaType.APPLICATION_JSON)//
		)//
				.andExpect(status().isOk()).andDo(print()).andReturn();

		Shared.checkJSONErrorCode(mr);
		//
		// JSONObject o = JSONObject.fromObject(mr.getResponse().getContentAsString());
		// //
		// JSONObject object = JsonPath.read(o, "$." + BaseAction.KEY_Object);
		// Pos posObject = (Pos) JSONObject.toBean(object, Pos.class);
		// assertTrue(posObject.getStatus() == Pos.EnumStatusPos.ESP_Active.getIndex());

		JSONObject o = JSONObject.fromObject(mr.getResponse().getContentAsString());
		Pos jsonTPos = (Pos) new Pos().parse1(o.getString(BaseAction.KEY_Object));
		assertTrue(jsonTPos.getStatus() == Pos.EnumStatusPos.ESP_Active.getIndex());

		deletePos(posCreate.getID());
	}

	@Test
	public void testResetEx2() throws Exception {
		Shared.printTestMethodStartInfo();
		Shared.caseLog("Case2:重置一个正常状态的POS机");
		Pos pos1 = DataInput.getPOS();
		Map<String, Object> params1 = pos1.getCreateParam(BaseBO.INVALID_CASE_ID, pos1);
		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		Pos posCreate1 = (Pos) posMapper.create(params1);
		pos1.setSalt(pos1.getSalt() == null ? "" : pos1.getSalt());
		pos1.setIgnoreIDInComparision(true);
		if (pos1.compareTo(posCreate1) != 0) {
			assertTrue(false, "创建的对象的字段与DB读出的不相等");
		}
		assertTrue(posCreate1 != null && EnumErrorCode.values()[Integer.parseInt(params1.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())] == EnumErrorCode.EC_NoError, "创建对象成功");

		MvcResult mr1 = mvc.perform(//
				get("/posSync/resetEx.bx?" + Pos.field.getFIELD_NAME_ID() + "=" + posCreate1.getID() + "&" + Pos.field.getFIELD_NAME_returnObject() + "=" + 1)//
						.session((MockHttpSession) sessionBoss)//
						.contentType(MediaType.APPLICATION_JSON)//
		)//
				.andExpect(status().isOk()).andDo(print()).andReturn();

		Shared.checkJSONErrorCode(mr1);
		//
		// JSONObject o1 =
		// JSONObject.fromObject(mr1.getResponse().getContentAsString()); //
		// JSONObject object1 = JsonPath.read(o1, "$." + BaseAction.KEY_Object);
		// Pos posObject1 = (Pos) JSONObject.toBean(object1, Pos.class);

		JSONObject o2 = JSONObject.fromObject(mr1.getResponse().getContentAsString()); //
		Pos jsonTPos2 = (Pos) new Pos().parse1(o2.getString(BaseAction.KEY_Object));

		assertTrue(jsonTPos2.getStatus() == Pos.EnumStatusPos.ESP_Active.getIndex());

		deletePos(posCreate1.getID());
	}

	@Test
	public void testResetEx3() throws Exception {
		Shared.printTestMethodStartInfo();
		Shared.caseLog("Case3:重置一个不存在的POS机");
		MvcResult mr2 = mvc.perform(//
				get("/posSync/resetEx.bx?" + Pos.field.getFIELD_NAME_ID() + "=" + -999 + "&" + Pos.field.getFIELD_NAME_returnObject() + "=" + 1)//
						.session((MockHttpSession) sessionBoss)//
						.contentType(MediaType.APPLICATION_JSON)//
		)//
				.andExpect(status().isOk()).andDo(print()).andReturn();

		Shared.checkJSONErrorCode(mr2, EnumErrorCode.EC_WrongFormatForInputField);
		Shared.checkJSONMsg(mr2, FieldFormat.FIELD_ERROR_ID, "错误信息与预期中的不相符");
	}

	@Test
	public void testResetEx4() throws Exception {
		Shared.printTestMethodStartInfo();
		Shared.caseLog("Case4:没有权限进行重置,由于是BX_Staff登录，所以默认是系统权限");
		MvcResult mr3 = mvc.perform(//
				get("/posSync/resetEx.bx?" + Pos.field.getFIELD_NAME_ID() + "=1"//
						+ "&" + Pos.field.getFIELD_NAME_returnObject() + "=" + 1)//
								.session((MockHttpSession) Shared.getStaffLoginSession(mvc, Shared.PhoneOfCashier))//
								.contentType(MediaType.APPLICATION_JSON)//
		)//
				.andExpect(status().isOk()).andDo(print()).andReturn();

		Shared.checkJSONErrorCode(mr3, EnumErrorCode.EC_NoError);
	}

	private Pos createPosWithMapper() throws Exception {

		Pos pos = DataInput.getPOS();
		Map<String, Object> params = pos.getCreateParam(BaseBO.INVALID_CASE_ID, pos);
		// 正常添加
		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		Pos posCreate = (Pos) posMapper.create(params);
		assertTrue(posCreate != null && EnumErrorCode.values()[Integer.parseInt(params.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())] == EnumErrorCode.EC_NoError, "创建对象成功");

		pos.setIgnoreIDInComparision(true);
		if (pos.compareTo(posCreate) != 0 && !pos.getPos_SN().equals(posCreate.getPos_SN())) {
			assertTrue(false, "创建的对象的字段与DB读出的不相等");
		}
		return posCreate;
	}
}
