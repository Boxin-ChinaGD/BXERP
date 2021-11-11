package com.bx.erp.test;

import static org.testng.Assert.assertNull;
import static org.testng.Assert.assertTrue;

import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;

import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.filter.CharacterEncodingFilter;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import com.bx.erp.action.BaseAction;
import com.bx.erp.action.BaseAction.EnumSession;
import com.bx.erp.action.bo.BaseBO;
import com.bx.erp.action.bo.PosBO;
import com.bx.erp.cache.CacheManager;
import com.bx.erp.dao.commodity.CommoditySyncCacheDispatcherMapper;
import com.bx.erp.dao.commodity.CommoditySyncCacheMapper;
import com.bx.erp.model.BaseModel;
import com.bx.erp.model.ErrorInfo.EnumErrorCode;
import com.bx.erp.model.Pos;
import com.bx.erp.model.Staff;
import com.bx.erp.model.BaseModel.EnumBoolean;
import com.bx.erp.model.Pos.EnumStatusPos;
import com.bx.erp.model.commodity.CommoditySyncCache;
import com.bx.erp.model.commodity.CommoditySyncCacheDispatcher;
import com.bx.erp.model.CacheType.EnumCacheType;
import com.bx.erp.util.DataSourceContextHolder;
import com.bx.erp.util.MD5Util;

@WebAppConfiguration
public class PosMapperTest extends BaseMapperTest {
	private static final int INVALID_ID = -999;

	@Resource
	private PosBO posBO;
	@Resource
	private WebApplicationContext wac;
	private MockMvc mvc;
	private HttpSession session;

	private Staff staff;

	public static class DataInput {
		protected static final Pos getPOS() throws Exception {
			Pos p = new Pos();
			p.setSalt(MD5Util.MD5(Shared.PASSWORD_DEFAULT + BaseAction.SHADOW)); // 其它测试在获取服务器session时，需要登录，需要用默认密码000000。//如果不设置这个，DB里salt值为空串，导致后面的测试出问题，比如POS无法正常登录
			Thread.sleep(1);
			p.setPos_SN("SN" + String.valueOf(System.currentTimeMillis()).substring(6));
			Thread.sleep(1);
			p.setShopID(1);
			p.setStatus(EnumStatusPos.ESP_Active.getIndex());
			p.setPasswordInPOS(UUID.randomUUID().toString().substring(1, 17));

			return (Pos) p.clone();
		}
	}

	@BeforeClass
	public void setup() {
		super.setUp();

		CharacterEncodingFilter encodingFilter = new CharacterEncodingFilter();
		encodingFilter.setEncoding("utf-8");
		encodingFilter.setForceEncoding(true);

		mvc = MockMvcBuilders.webAppContextSetup(wac).addFilter(encodingFilter, "/*").build();
		try {
			session = Shared.getStaffLoginSession(mvc, Shared.PhoneOfBoss, Shared.PASSWORD_DEFAULT, Shared.DB_SN_Test);
			staff = (Staff) session.getAttribute(EnumSession.SESSION_Staff.getName());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@AfterClass
	public void tearDown() {
		super.tearDown();

		CacheManager.getCache(Shared.DBName_Test, EnumCacheType.ECT_POS).deleteAll();
	}

	@Test
	public void createTest() throws Exception {
		Shared.printTestMethodStartInfo();

		System.out.println("\n------------------------ Create Pos Test ------------------------");

		Pos pos = DataInput.getPOS();
		Map<String, Object> params = pos.getCreateParam(BaseBO.INVALID_CASE_ID, pos);
		System.out.println("\n-------------------------case1:正常添加-----------------------------");

		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		Pos posCreate = (Pos) posMapper.create(params);
		assertTrue(posCreate != null && EnumErrorCode.values()[Integer.parseInt(params.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())] == EnumErrorCode.EC_NoError, "创建对象失败");
		//
		pos.setIgnoreIDInComparision(true);
		if (pos.compareTo(posCreate) != 0) {
			assertTrue(false, "创建的对象的字段与DB读出的不相等");
		}
		CacheManager.getCache(Shared.DBName_Test, EnumCacheType.ECT_POS).write1(posCreate, Shared.DBName_Test, staff.getID());
		System.out.println(CacheManager.getCache(Shared.DBName_Test, EnumCacheType.ECT_POS).readN(false, false));

		System.out.println("\n------------------case2:重复添加相同SN的POS机，会返回EC_Duplicated------------------");
		pos.setPos_SN(posCreate.getPos_SN());
		Map<String, Object> params2 = pos.getCreateParam(BaseBO.INVALID_CASE_ID, pos);

		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		posMapper.create(params2);// ...
		assertTrue(EnumErrorCode.values()[Integer.parseInt(params2.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())] == EnumErrorCode.EC_Duplicated, "创建对象失败");
		System.out.println("创建POSTest 成功！！");
		deletePOS(posCreate);

		System.out.println("----------------------------case3： 传入不存在的门店ID创建POS机-------------------");
		Pos pos1 = DataInput.getPOS();
		pos1.setShopID(INVALID_ID);
		Map<String, Object> params1 = pos1.getCreateParam(BaseBO.INVALID_CASE_ID, pos1);

		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		Pos posCreate1 = (Pos) posMapper.create(params1);
		assertTrue(posCreate1 == null && EnumErrorCode.values()[Integer.parseInt(params1.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())] == EnumErrorCode.EC_BusinessLogicNotDefined, "创建对象失败");

		Shared.caseLog("case4:重复添加相同SN的已被删除的POS机，正常创建");
		// 创建并删除一个pos机
		Pos pos4 = DataInput.getPOS();
		Map<String, Object> params4 = pos4.getCreateParam(BaseBO.INVALID_CASE_ID, pos4);
		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		Pos posCreate4 = (Pos) posMapper.create(params4);
		assertTrue(posCreate4 != null && EnumErrorCode.values()[Integer.parseInt(params4.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())] == EnumErrorCode.EC_NoError, "创建对象失败");
		pos4.setIgnoreIDInComparision(true);
		if (pos4.compareTo(posCreate4) != 0) {
			assertTrue(false, "创建的对象的字段与DB读出的不相等");
		}
		CacheManager.getCache(Shared.DBName_Test, EnumCacheType.ECT_POS).write1(posCreate4, Shared.DBName_Test, staff.getID());
		System.out.println(CacheManager.getCache(Shared.DBName_Test, EnumCacheType.ECT_POS).readN(false, false));
		//
		Map<String, Object> deleteParam1 = posCreate4.getDeleteParam(BaseBO.INVALID_CASE_ID, posCreate4);
		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		posMapper.delete(deleteParam1);
		assertTrue(EnumErrorCode.values()[Integer.parseInt(deleteParam1.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())] == EnumErrorCode.EC_NoError, "删除对象失败");
		CacheManager.getCache(Shared.DBName_Test, EnumCacheType.ECT_POS).delete1(posCreate4);
		System.out.println(CacheManager.getCache(Shared.DBName_Test, EnumCacheType.ECT_POS).readN(false, false));
		//
		// 使用已删除的pos机的信息进行创建
		Map<String, Object> params5 = pos4.getCreateParam(BaseBO.INVALID_CASE_ID, pos4);
		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		Pos posCreate5 = (Pos) posMapper.create(params5);
		assertTrue(posCreate5 != null && EnumErrorCode.values()[Integer.parseInt(params5.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())] == EnumErrorCode.EC_NoError, "创建对象失败");
		pos4.setIgnoreIDInComparision(true);
		if (pos4.compareTo(posCreate5) != 0) {
			assertTrue(false, "创建的对象的字段与DB读出的不相等");
		}
		CacheManager.getCache(Shared.DBName_Test, EnumCacheType.ECT_POS).write1(posCreate5, Shared.DBName_Test, staff.getID());
		System.out.println(CacheManager.getCache(Shared.DBName_Test, EnumCacheType.ECT_POS).readN(false, false));
		//
		deletePOS(posCreate5);
	}

	@Test
	public void updateTest() throws Exception {
		Shared.printTestMethodStartInfo();

		System.out.println("\n------------------------ update Pos Test ------------------------");

		Pos pos = DataInput.getPOS();
		Map<String, Object> params = pos.getCreateParam(BaseBO.INVALID_CASE_ID, pos);
		//
		System.out.println("\n------------------------ 正常添加,用于测试 ------------------------");

		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		Pos posCreate = (Pos) posMapper.create(params);
		assertTrue(posCreate != null && EnumErrorCode.values()[Integer.parseInt(params.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())] == EnumErrorCode.EC_NoError, "创建对象失败");
		//
		pos.setIgnoreIDInComparision(true);
		if (pos.compareTo(posCreate) != 0) {
			assertTrue(false, "创建的对象的字段与DB读出的不相等");
		}
		CacheManager.getCache(Shared.DBName_Test, EnumCacheType.ECT_POS).write1(posCreate, Shared.DBName_Test, staff.getID());
		System.out.println(CacheManager.getCache(Shared.DBName_Test, EnumCacheType.ECT_POS).readN(false, false));
		//
		Pos posU = DataInput.getPOS();
		posU.setID(posCreate.getID());
		posU.setPos_SN(posCreate.getPos_SN());
		posU.setShopID(1);
		Map<String, Object> paramsUpdate = posU.getUpdateParam(BaseBO.INVALID_CASE_ID, posU);

		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		Pos updatePos = (Pos) posMapper.update(paramsUpdate);
		assertTrue(updatePos != null && EnumErrorCode.values()[Integer.parseInt(paramsUpdate.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())] == EnumErrorCode.EC_NoError, "修改对象失败");
		//
		posU.setIgnoreIDInComparision(true);
		if (posU.compareTo(updatePos) != 0) {
			assertTrue(false, "修改的对象的字段与DB读出的不相等");
		}
		//
		System.out.println("修改POSTest 成功！！");
		//
		CacheManager.getCache(Shared.DBName_Test, EnumCacheType.ECT_POS).write1(updatePos, Shared.DBName_Test, staff.getID());
		System.out.println(CacheManager.getCache(Shared.DBName_Test, EnumCacheType.ECT_POS).readN(false, false));

		deletePOS(posCreate);

		System.out.println("\n------------------------ 用不存在的门店ID进行修改 ------------------------");
		Pos posU1 = DataInput.getPOS();
		posU1.setID(posCreate.getID());
		posU1.setPos_SN(posCreate.getPos_SN());
		posU1.setShopID(INVALID_ID);
		Map<String, Object> paramsUpdate1 = posU.getUpdateParam(BaseBO.INVALID_CASE_ID, posU1);

		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		Pos updatePos1 = (Pos) posMapper.update(paramsUpdate1);
		assertTrue(updatePos1 == null && EnumErrorCode.values()[Integer.parseInt(paramsUpdate1.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())] == EnumErrorCode.EC_BusinessLogicNotDefined);
	}

	@Test
	public void recycleAppTest1() throws Exception {
		Shared.caseLog("Case1:重新注册一个已经登录过app，未删除的POS机");

		Pos pos = DataInput.getPOS();
		pos.setReturnSalt(EnumBoolean.EB_Yes.getIndex());
		Pos posCreate = BasePosTest.createPosViaMapper(pos, Shared.DBName_Test);
		//
		posCreate.setReturnSalt(EnumBoolean.EB_Yes.getIndex());
		posCreate.setResetPasswordInPos(EnumBoolean.EB_Yes.getIndex()); // R1会令F_PasswordInPos=NULL
		Pos posR1 = BasePosTest.retrieve1PosViaMapper(posCreate, Shared.DBName_Test);
		//
		posR1.setPasswordInPOS(UUID.randomUUID().toString().substring(1, 17));
		posR1.setSalt(MD5Util.MD5(Shared.PASSWORD_DEFAULT + BaseAction.SHADOW));
		posR1.setReturnSalt(EnumBoolean.EB_Yes.getIndex());
		Pos posRecycleApp = BasePosTest.recycleAppPosViaMapper(posR1, Shared.DBName_Test);
		//
		deletePOS(posRecycleApp);
	}

	@Test
	public void recycleAppTest2() throws Exception {
		Shared.caseLog("Case2:重新注册一个未登录过app，未删除的POS机");

		Pos pos = DataInput.getPOS();
		pos.setReturnSalt(EnumBoolean.EB_Yes.getIndex());
		Pos posCreate = BasePosTest.createPosViaMapper(pos, Shared.DBName_Test);
		//
		posCreate.setPasswordInPOS(UUID.randomUUID().toString().substring(1, 17));
		posCreate.setSalt(MD5Util.MD5(Shared.PASSWORD_DEFAULT + BaseAction.SHADOW));
		posCreate.setReturnSalt(EnumBoolean.EB_Yes.getIndex());
		Pos posRecycleApp = BasePosTest.recycleAppPosViaMapper(posCreate, Shared.DBName_Test);
		//
		deletePOS(posRecycleApp);
	}

	@Test
	public void recycleAppTest3() throws Exception {
		Shared.caseLog("Case3:重新注册已经登录过app，已删除的POS机");

		Pos pos = DataInput.getPOS();
		pos.setReturnSalt(EnumBoolean.EB_Yes.getIndex());
		Pos posCreate = BasePosTest.createPosViaMapper(pos, Shared.DBName_Test);
		//
		posCreate.setReturnSalt(EnumBoolean.EB_Yes.getIndex());
		posCreate.setResetPasswordInPos(EnumBoolean.EB_Yes.getIndex());
		Pos posR1 = BasePosTest.retrieve1PosViaMapper(posCreate, Shared.DBName_Test);
		//
		deletePOS(posR1);
		//
		posR1.setPasswordInPOS(UUID.randomUUID().toString().substring(1, 17));
		posR1.setSalt(MD5Util.MD5(Shared.PASSWORD_DEFAULT + BaseAction.SHADOW));
		posR1.setReturnSalt(EnumBoolean.EB_Yes.getIndex());
		Map<String, Object> params = posR1.getUpdateParam(BaseBO.CASE_POS_RecycleApp, posR1);
		//
		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		Pos posRecycleApp = (Pos) posMapper.recycleApp(params);
		assertTrue(posRecycleApp == null && EnumErrorCode.values()[Integer.parseInt(params.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())] == EnumErrorCode.EC_BusinessLogicNotDefined,
				params.get(BaseAction.SP_OUT_PARAM_sErrorMsg).toString());
	}

	@Test
	public void recycleAppTest4() throws Exception {
		Shared.caseLog("Case4:重新注册一个不存在POS机");

		Pos pos = DataInput.getPOS();
		pos.setID(Shared.BIG_ID);
		pos.setReturnSalt(EnumBoolean.EB_Yes.getIndex());
		Map<String, Object> params = pos.getUpdateParam(BaseBO.CASE_POS_RecycleApp, pos);
		//
		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		Pos posRecycleApp = (Pos) posMapper.recycleApp(params);
		assertTrue(posRecycleApp == null && EnumErrorCode.values()[Integer.parseInt(params.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())] == EnumErrorCode.EC_BusinessLogicNotDefined,
				params.get(BaseAction.SP_OUT_PARAM_sErrorMsg).toString());
	}

	private void deletePOS(Pos posCreate) {
		Map<String, Object> deleteParam = posCreate.getDeleteParam(BaseBO.INVALID_CASE_ID, posCreate);

		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		posMapper.delete(deleteParam);
		assertTrue(posCreate != null && EnumErrorCode.values()[Integer.parseInt(deleteParam.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())] == EnumErrorCode.EC_NoError, "删除对象失败");
		CacheManager.getCache(Shared.DBName_Test, EnumCacheType.ECT_POS).delete1(posCreate);
	}

	@Test
	public void retrieve1Test() throws Exception {
		Shared.printTestMethodStartInfo();

		System.out.println("\n------------------------ retrieve1 Pos Test ------------------------");

		Pos pos = DataInput.getPOS();
		Map<String, Object> params = pos.getCreateParam(BaseBO.INVALID_CASE_ID, pos);
		// 正常添加
		System.out.println("\n------------------------ 正常添加,用于测试  ------------------------");
		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		Pos posCreate = (Pos) posMapper.create(params);
		assertTrue(posCreate != null && EnumErrorCode.values()[Integer.parseInt(params.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())] == EnumErrorCode.EC_NoError, "创建对象失败");
		CacheManager.getCache(Shared.DBName_Test, EnumCacheType.ECT_POS).write1(posCreate, Shared.DBName_Test, staff.getID());
		System.out.println(CacheManager.getCache(Shared.DBName_Test, EnumCacheType.ECT_POS).readN(false, false));

		pos.setIgnoreIDInComparision(true);
		if (pos.compareTo(posCreate) != 0) {
			assertTrue(false, "创建的对象的字段与DB读出的不相等");
		}
		CacheManager.getCache(Shared.DBName_Test, EnumCacheType.ECT_POS).write1(posCreate, Shared.DBName_Test, staff.getID());
		System.out.println(CacheManager.getCache(Shared.DBName_Test, EnumCacheType.ECT_POS).readN(false, false));

		System.out.println("\n------------------------ case1:正常查询------------------------");
		Map<String, Object> params2 = pos.getRetrieve1Param(BaseBO.INVALID_CASE_ID, posCreate);

		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		Pos posR1 = (Pos) posMapper.retrieve1(params2);

		assertTrue(posR1 != null && EnumErrorCode.values()[Integer.parseInt(params2.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())] == EnumErrorCode.EC_NoError, "查找对象失败");

		pos.setIgnoreIDInComparision(true);
		if (pos.compareTo(posR1) != 0) {
			assertTrue(false, "查找的对象的字段与DB读出的不相等");
		}
		System.out.println("查找单个POSTest 成功！！");

		deletePOS(posCreate);
	}

	@Test
	public void retrieve1BySNTest() throws Exception {
		Shared.printTestMethodStartInfo();

		System.out.println("\n------------------------ Case 1 通过公司的SN查找POS ------------------------");

		Pos pos = DataInput.getPOS();
		Map<String, Object> params = pos.getCreateParam(BaseBO.INVALID_CASE_ID, pos);
		//
		System.out.println("\n------------------------ 正常添加,用于测试 ------------------------");
		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		Pos posCreate = (Pos) posMapper.create(params);
		assertTrue(posCreate != null && EnumErrorCode.values()[Integer.parseInt(params.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())] == EnumErrorCode.EC_NoError, "创建对象失败");

		pos.setIgnoreIDInComparision(true);
		if (pos.compareTo(posCreate) != 0) {
			assertTrue(false, "创建的对象的字段与DB读出的不相等");
		}
		CacheManager.getCache(Shared.DBName_Test, EnumCacheType.ECT_POS).write1(posCreate, Shared.DBName_Test, staff.getID());
		System.out.println(CacheManager.getCache(Shared.DBName_Test, EnumCacheType.ECT_POS).readN(false, false));

		Map<String, Object> params2 = pos.getRetrieve1Param(BaseBO.INVALID_CASE_ID, posCreate);

		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		Pos posR1 = (Pos) posMapper.retrieve1(params2);

		assertTrue(posR1 != null && EnumErrorCode.values()[Integer.parseInt(params2.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())] == EnumErrorCode.EC_NoError, "查找对象失败");

		pos.setIgnoreIDInComparision(true);
		if (pos.compareTo(posR1) != 0) {
			assertTrue(false, "查找的对象的字段与DB读出的不相等");
		}
		System.out.println("已验证该pos创建成功！");

		// 通过公司的SN查询
		posCreate.setCompanySN(Shared.DB_SN_Test);
		Map<String, Object> retrieve1ByFieldsParam = pos.getRetrieve1Param(BaseBO.CASE_Pos_Retrieve1BySN, posCreate);

		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		BaseModel posR1BySN = posMapper.retrieve1BySN(retrieve1ByFieldsParam);

		assertTrue(posR1BySN != null && EnumErrorCode.values()[Integer.parseInt(retrieve1ByFieldsParam.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())] == EnumErrorCode.EC_NoError, "查找对象失败");

		if (posCreate.compareTo(posR1BySN) != 0) {
			assertTrue(false, "查找的对象的字段与DB读出的不相等");
		}
		deletePOS(posCreate);

		System.out.println("\n------------------------ Case 2 通过不存在的公司的SN查找POS ------------------------");
		Pos pos2 = new Pos();
		pos2.setCompanySN(Shared.DB_SN_Test);
		pos2.setPos_SN("不存在的POS_SN");
		Map<String, Object> retrieve1Param2 = pos2.getRetrieve1Param(BaseBO.CASE_Pos_Retrieve1BySN, pos2);

		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		BaseModel pos2BySN = posMapper.retrieve1BySN(retrieve1Param2);

		assertTrue(pos2BySN == null && EnumErrorCode.values()[Integer.parseInt(retrieve1ByFieldsParam.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())] == EnumErrorCode.EC_NoError, "查找对象失败");

		System.out.println("\n------------------------ Case 3 通过已删除的公司的SN查找POS ------------------------");
		Pos pos3 = DataInput.getPOS();
		pos3.setCompanySN(Shared.DB_SN_Test);
		Map<String, Object> params3 = pos3.getCreateParam(BaseBO.INVALID_CASE_ID, pos3);
		// 正常添加
		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		Pos posCreate3 = (Pos) posMapper.create(params3);
		assertTrue(posCreate3 != null && EnumErrorCode.values()[Integer.parseInt(params3.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())] == EnumErrorCode.EC_NoError, "创建对象失败");

		pos3.setIgnoreIDInComparision(true);
		if (pos3.compareTo(posCreate3) != 0) {
			assertTrue(false, "创建的对象的字段与DB读出的不相等");
		}
		CacheManager.getCache(Shared.DBName_Test, EnumCacheType.ECT_POS).write1(posCreate3, Shared.DBName_Test, staff.getID());
		System.out.println(CacheManager.getCache(Shared.DBName_Test, EnumCacheType.ECT_POS).readN(false, false));
		// 查找
		Map<String, Object> params3R1 = pos3.getRetrieve1Param(BaseBO.INVALID_CASE_ID, posCreate3);

		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		Pos posR3 = (Pos) posMapper.retrieve1(params3R1);

		assertTrue(posR3 != null && EnumErrorCode.values()[Integer.parseInt(params3R1.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())] == EnumErrorCode.EC_NoError, "查找对象失败");

		posCreate3.setIgnoreIDInComparision(true);
		if (posCreate3.compareTo(posR3) != 0) {
			assertTrue(false, "查找的对象的字段与DB读出的不相等");
		}
		System.out.println("已验证该pos创建成功！");
		// 删除
		Map<String, Object> deleteParam3 = posR3.getDeleteParam(BaseBO.INVALID_CASE_ID, posR3);

		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		posMapper.delete(deleteParam3);

		CacheManager.getCache(Shared.DBName_Test, EnumCacheType.ECT_POS).delete1(posCreate3);
		System.out.println(CacheManager.getCache(Shared.DBName_Test, EnumCacheType.ECT_POS).readN(false, false));

		assertTrue(EnumErrorCode.values()[Integer.parseInt(deleteParam3.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())] == EnumErrorCode.EC_NoError, "删除失败");
	}

	@Test
	public void retrieveNTest() throws Exception {
		Shared.printTestMethodStartInfo();

		System.out.println("\n------------------------ retrieveN Pos Test ------------------------");

		Pos pos = DataInput.getPOS();
		Map<String, Object> params = pos.getCreateParam(BaseBO.INVALID_CASE_ID, pos);
		//
		System.out.println("\n------------------------ 正常添加，用于测试 ------------------------");
		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		Pos posCreate = (Pos) posMapper.create(params);
		assertTrue(posCreate != null && EnumErrorCode.values()[Integer.parseInt(params.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())] == EnumErrorCode.EC_NoError, "创建对象失败");

		pos.setIgnoreIDInComparision(true);
		if (pos.compareTo(posCreate) != 0) {
			assertTrue(false, "创建的对象的字段与DB读出的不相等");
		}
		CacheManager.getCache(Shared.DBName_Test, EnumCacheType.ECT_POS).write1(posCreate, Shared.DBName_Test, staff.getID());
		System.out.println(CacheManager.getCache(Shared.DBName_Test, EnumCacheType.ECT_POS).readN(false, false));

		//
		System.out.println("\n------------------------ case1:根据SN查找 ------------------------");
		Pos posSN = new Pos();
		posSN.setPos_SN(posCreate.getPos_SN());

		Map<String, Object> paramsSN = posSN.getRetrieveNParam(BaseBO.INVALID_CASE_ID, posSN);

		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		List<BaseModel> retrieveNSN = posMapper.retrieveN(paramsSN);
		assertTrue(retrieveNSN != null && EnumErrorCode.values()[Integer.parseInt(paramsSN.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())] == EnumErrorCode.EC_NoError, "查找对象失败");
		//
		System.out.println("\n------------------------ case2:根据shopID查找------------------------");
		Pos posShop = new Pos();
		posShop.setShopID(posCreate.getShopID());

		Map<String, Object> paramsShop = posShop.getRetrieveNParam(BaseBO.INVALID_CASE_ID, posShop);

		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		List<BaseModel> retrieveNShop = posMapper.retrieveN(paramsShop);
		assertTrue(retrieveNShop != null && EnumErrorCode.values()[Integer.parseInt(paramsShop.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())] == EnumErrorCode.EC_NoError, "查找对象失败");

		//
		System.out.println("\n------------------------ case3:根据status查找------------------------");
		Pos posStatus = new Pos();
		posStatus.setStatus(posCreate.getStatus());

		Map<String, Object> paramsStatus = posStatus.getRetrieveNParam(BaseBO.INVALID_CASE_ID, posStatus);

		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		List<BaseModel> retrieveNStatus = posMapper.retrieveN(paramsStatus);
		assertTrue(retrieveNStatus != null && EnumErrorCode.values()[Integer.parseInt(paramsStatus.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())] == EnumErrorCode.EC_NoError, "查找对象失败");

		System.out.println("查找多个POSTest 成功！！");

		deletePOS(posCreate);

	}

	@Test
	public void deleteTest() throws Exception {
		Shared.printTestMethodStartInfo();

		System.out.println("\n------------------------ 删除 Pos Test ------------------------");

		Pos pos = DataInput.getPOS();
		Map<String, Object> params = pos.getCreateParam(BaseBO.INVALID_CASE_ID, pos);
		// 正常添加
		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		Pos posCreate = (Pos) posMapper.create(params);
		assertTrue(posCreate != null && EnumErrorCode.values()[Integer.parseInt(params.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())] == EnumErrorCode.EC_NoError, "创建对象失败");
		//
		pos.setIgnoreIDInComparision(true);
		if (pos.compareTo(posCreate) != 0) {
			assertTrue(false, "创建的对象的字段与DB读出的不相等");
		}
		CacheManager.getCache(Shared.DBName_Test, EnumCacheType.ECT_POS).write1(posCreate, Shared.DBName_Test, staff.getID());
		System.out.println(CacheManager.getCache(Shared.DBName_Test, EnumCacheType.ECT_POS).readN(false, false));

		System.out.println("\n------------------------ Case 1 没有任何关联这台POS机 ------------------------");
		Map<String, Object> deleteParam = posCreate.getDeleteParam(BaseBO.INVALID_CASE_ID, posCreate);

		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		posMapper.delete(deleteParam);
		assertTrue(EnumErrorCode.values()[Integer.parseInt(deleteParam.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())] == EnumErrorCode.EC_NoError, "删除对象失败");
		CacheManager.getCache(Shared.DBName_Test, EnumCacheType.ECT_POS).delete1(posCreate);
		System.out.println(CacheManager.getCache(Shared.DBName_Test, EnumCacheType.ECT_POS).readN(false, false));

		System.out.println("\n------------------------ Case 2 有其他关联这台POS机 ------------------------"); // ...有待定义其它关联
		Pos pos2 = DataInput.getPOS();
		Map<String, Object> params2 = pos2.getCreateParam(BaseBO.INVALID_CASE_ID, pos2);
		//
		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		Pos posCreate2 = (Pos) posMapper.create(params2);
		CacheManager.getCache(Shared.DBName_Test, EnumCacheType.ECT_POS).write1(posCreate2, Shared.DBName_Test, staff.getID());
		System.out.println(CacheManager.getCache(Shared.DBName_Test, EnumCacheType.ECT_POS).readN(false, false));

		assertTrue(posCreate2 != null && EnumErrorCode.values()[Integer.parseInt(params2.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())] == EnumErrorCode.EC_NoError, "创建对象失败");
		//
		pos.setIgnoreIDInComparision(true);
		if (pos.compareTo(posCreate) != 0) {
			assertTrue(false, "创建的对象的字段与DB读出的不相等");
		}
		CommoditySyncCacheMapper sscMapper = (CommoditySyncCacheMapper) applicationContext.getBean("commoditySyncCacheMapper");

		CommoditySyncCache csc = new CommoditySyncCache();
		csc.setSyncData_ID(10);
		csc.setSyncSequence(1);// 本字段在这里无关紧要
		csc.setSyncType("C");
		csc.setPosID(posCreate2.getID());
		//
		Map<String, Object> createParam = csc.getCreateParam(BaseBO.INVALID_CASE_ID, csc);
		//
		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		List<List<BaseModel>> sscCreateList = sscMapper.createEx(createParam);

		CommoditySyncCache sscCreate = (CommoditySyncCache) sscCreateList.get(0).get(0);
		assertTrue(sscCreateList != null && EnumErrorCode.values()[Integer.parseInt(createParam.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())] == EnumErrorCode.EC_NoError, "创建对象失败");
		//
		csc.setIgnoreIDInComparision(true);
		if (csc.compareTo(sscCreateList.get(0).get(0)) != 0) {
			assertTrue(false, "创建的对象的字段与DB读出的不相等");
		}
		CacheManager.getCache(Shared.DBName_Test, EnumCacheType.ECT_POS).delete1(posCreate2);
		System.out.println(CacheManager.getCache(Shared.DBName_Test, EnumCacheType.ECT_POS).readN(false, false));

		Map<String, Object> paramDelete = posCreate2.getDeleteParam(BaseBO.INVALID_CASE_ID, posCreate2);

		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		posMapper.delete(paramDelete);
		assertTrue(EnumErrorCode.values()[Integer.parseInt(paramDelete.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())] == EnumErrorCode.EC_BusinessLogicNotDefined, "删除对象失败");

		CommoditySyncCacheDispatcherMapper sscdMapper = (CommoditySyncCacheDispatcherMapper) applicationContext.getBean("commoditySyncCacheDispatcherMapper");
		CommoditySyncCacheDispatcher cscd = new CommoditySyncCacheDispatcher();

		// 模拟所有pos机已经同步该同步块，然后删除该同步块后，删除该创建的POS机
		List<Pos> listPos = (List<Pos>) Shared.getPosesFromDB(posBO, Shared.DBName_Test);
		for (Pos p : listPos) {
			if (p.getID() != posCreate2.getID()) {
				cscd.setSyncCacheID(sscCreate.getID());
				cscd.setPos_ID(p.getID());
				createParam = cscd.getCreateParam(BaseBO.INVALID_CASE_ID, cscd);

				DataSourceContextHolder.setDbName(Shared.DBName_Test);
				cscd = (CommoditySyncCacheDispatcher) sscdMapper.create(createParam);
				System.out.println(createParam);
			}
		}
		paramDelete = sscCreate.getDeleteParam(BaseBO.INVALID_CASE_ID, sscCreate);
		//
		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		sscMapper.delete(paramDelete);

		deletePOS(posCreate2);
		System.out.println("删除 POSTest 测试成功");
	}

	@Test
	public void resetTest() throws Exception {
		Shared.printTestMethodStartInfo();

		System.out.println("\n------------------------ 创建一个被删除的Pos ------------------------");

		Pos pos = DataInput.getPOS();
		Map<String, Object> params = pos.getCreateParam(BaseBO.INVALID_CASE_ID, pos);
		// 正常添加
		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		Pos posCreate = (Pos) posMapper.create(params);
		assertTrue(posCreate != null && EnumErrorCode.values()[Integer.parseInt(params.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())] == EnumErrorCode.EC_NoError, "创建对象失败");
		//
		pos.setIgnoreIDInComparision(true);
		if (pos.compareTo(posCreate) != 0) {
			assertTrue(false, "创建的对象的字段与DB读出的不相等");
		}
		CacheManager.getCache(Shared.DBName_Test, EnumCacheType.ECT_POS).write1(posCreate, Shared.DBName_Test, staff.getID());
		System.out.println(CacheManager.getCache(Shared.DBName_Test, EnumCacheType.ECT_POS).readN(false, false));
		//
		Map<String, Object> deleteParam = posCreate.getDeleteParam(BaseBO.INVALID_CASE_ID, posCreate);

		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		posMapper.delete(deleteParam);
		assertTrue(EnumErrorCode.values()[Integer.parseInt(deleteParam.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())] == EnumErrorCode.EC_NoError, "删除对象失败");

		CacheManager.getCache(Shared.DBName_Test, EnumCacheType.ECT_POS).delete1(posCreate);
		System.out.println(CacheManager.getCache(Shared.DBName_Test, EnumCacheType.ECT_POS).readN(false, false));

		//
		Map<String, Object> params2 = pos.getRetrieve1Param(BaseBO.INVALID_CASE_ID, posCreate);

		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		Pos posR1 = (Pos) posMapper.retrieve1(params2);
		assertTrue(EnumErrorCode.values()[Integer.parseInt(params2.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())] == EnumErrorCode.EC_NoError);
		assertTrue(posR1.getStatus() == Pos.EnumStatusPos.ESP_Inactive.getIndex());

		System.out.println("\n------------------------Case1:重置一个已经删除POS机  ------------------------");
		Map<String, Object> resetrParam = posCreate.getUpdateParam(BaseBO.CASE_POS_Reset, posR1);

		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		Pos pos1 = (Pos) posMapper.reset(resetrParam);

		CacheManager.getCache(Shared.DBName_Test, EnumCacheType.ECT_POS).write1(pos1, Shared.DBName_Test, staff.getID());
		System.out.println(CacheManager.getCache(Shared.DBName_Test, EnumCacheType.ECT_POS).readN(false, false));
		//
		assertTrue(EnumErrorCode.values()[Integer.parseInt(resetrParam.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())] == EnumErrorCode.EC_NoError);
		assertTrue(pos1.getStatus() == Pos.EnumStatusPos.ESP_Active.getIndex());

		System.out.println("\n------------------------Case2:重置一个正常状态POS机  ------------------------");
		Map<String, Object> resetrParam2 = posCreate.getUpdateParam(BaseBO.CASE_POS_Reset, posR1);

		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		Pos pos2 = (Pos) posMapper.reset(resetrParam2);

		CacheManager.getCache(Shared.DBName_Test, EnumCacheType.ECT_POS).write1(pos2, Shared.DBName_Test, staff.getID());
		System.out.println(CacheManager.getCache(Shared.DBName_Test, EnumCacheType.ECT_POS).readN(false, false));
		//
		assertTrue(EnumErrorCode.values()[Integer.parseInt(resetrParam2.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())] == EnumErrorCode.EC_NoError);
		assertTrue(pos2.getStatus() == Pos.EnumStatusPos.ESP_Active.getIndex());

		System.out.println("\n------------------------Case3:重置一个不存在POS机  ------------------------");
		Pos pos3 = new Pos();
		pos3.setID(INVALID_ID);
		Map<String, Object> resetrParam3 = posCreate.getUpdateParam(BaseBO.CASE_POS_Reset, pos3);

		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		Pos pos4 = (Pos) posMapper.reset(resetrParam3);
		assertNull(pos4);
		assertTrue(EnumErrorCode.values()[Integer.parseInt(resetrParam3.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())] == EnumErrorCode.EC_BusinessLogicNotDefined);

		Map<String, Object> deleteParam2 = posR1.getDeleteParam(BaseBO.INVALID_CASE_ID, posR1);

		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		posMapper.delete(deleteParam2);
		CacheManager.getCache(Shared.DBName_Test, EnumCacheType.ECT_POS).delete1(posR1);

	}
}
