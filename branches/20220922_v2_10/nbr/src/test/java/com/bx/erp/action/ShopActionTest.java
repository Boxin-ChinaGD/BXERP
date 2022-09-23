package com.bx.erp.action;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.text.ParseException;
import java.util.List;

import javax.servlet.http.HttpSession;

import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import com.bx.erp.model.Pos.EnumStatusPos;
import com.bx.erp.model.ErrorInfo.EnumErrorCode;
import com.bx.erp.model.Pos;
import com.bx.erp.model.Shop;
import com.bx.erp.model.Staff;
import com.bx.erp.test.BaseActionTest;
import com.bx.erp.test.BaseCompanyTest;
import com.bx.erp.test.Shared;
import com.bx.erp.test.staff.BaseShopTest;
import com.bx.erp.util.MD5Util;
import com.jayway.jsonpath.JsonPath;

import net.sf.json.JSONObject;

@WebAppConfiguration
public class ShopActionTest extends BaseActionTest {

	public static class DataInput {
		private static Pos posInput;

		protected static final MockHttpServletRequestBuilder getBuilder(String url, MediaType contentType, Pos p) {
			MockHttpServletRequestBuilder builder = post(url).contentType(contentType)//
					.param(Pos.field.getFIELD_NAME_ID(), String.valueOf(p.getID())) //
					.param(Pos.field.getFIELD_NAME_pwdEncrypted(), p.getPwdEncrypted())//
					.param(Pos.field.getFIELD_NAME_pos_SN(), p.getPos_SN())//
					.param(Pos.field.getFIELD_NAME_passwordInPOS(), Shared.PASSWORD_DEFAULT).param(Pos.field.getFIELD_NAME_shopID(), String.valueOf(p.getShopID())); //
			return builder;
		}

		protected static final Pos getPos() throws ParseException {
			posInput = new Pos();
			posInput.setPos_SN("SN" + System.currentTimeMillis() % 1000000);//
			posInput.setPwdEncrypted("000000");
			posInput.setSalt(MD5Util.MD5(Shared.PASSWORD_DEFAULT + BaseAction.SHADOW));// 其它测试在获取服务器session时，需要登录，需要用默认密码000000。//如果不设置这个，DB里salt值为空串，导致后面的测试出问题，比如POS无法正常登录
			posInput.setStatus(EnumStatusPos.ESP_Active.getIndex());

			return posInput;
		}
	}

	@BeforeClass
	public void setup() {
		super.setUp();

		try {
			sessionBoss = Shared.getBXStaffLoginSession(mvc, Shared.PhoneOfOP);
			BaseCompanyTest.keepOldNbrAndNbrBx();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@AfterClass
	public void tearDown() {
		super.tearDown();
	}

	// @Test
	// public void testIndex() throws Exception {
	// Shared.printTestMethodStartInfo();
	//
	// mvc.perform(//
	// get("/shop.bx")//
	// .session((MockHttpSession) Shared.getBXStaffLoginSession(mvc, PhoneOfOP))//
	// )//
	// .andExpect(status().isOk());
	// }

	// private void checkShopStatus(int iID) {
	// ShopMapper mapper = (ShopMapper) applicationContext.getBean("shopMapper");
	// Shop s = new Shop();
	// s.setID(iID);
	// Map<String, Object> params = s.getRetrieve1Param(BaseBO.INVALID_CASE_ID, s);
	// List<List<BaseModel>> listBM = mapper.retrieve1Ex(params);
	// Shop shop = (Shop) listBM.get(0).get(0);
	//
	// assertTrue(shop != null && shop.getID() == iID && shop.getStatus() ==
	// Shop.EnumStatusShop.ESS_ArrearageLocking.getIndex());
	// Assert.assertEquals(EnumErrorCode.values()[Integer.parseInt(params.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())],
	// EnumErrorCode.EC_NoError);
	// }
	//
	// // 检查所有相关pos和staff的状态
	// private void checkStatusOfPosAndStaff(Pos posCreate, Staff staff) throws
	// Exception {
	// // 读取相关pos机的状态应该1
	// PosMapper mapper = (PosMapper) applicationContext.getBean("posMapper");
	// Pos pos = new Pos();
	// pos.setID(posCreate.getID());
	// Map<String, Object> params2 = pos.getRetrieve1Param(BaseBO.INVALID_CASE_ID,
	// pos);
	// DataSourceContextHolder.setDbName(Shared.DBName_Test);
	// Pos posR1 = (Pos) mapper.retrieve1(params2);
	// assertTrue(posR1 != null &&
	// EnumErrorCode.values()[Integer.parseInt(params2.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())]
	// == EnumErrorCode.EC_NoError, "查找对象成功");
	// assertTrue(posR1.getStatus() == Pos.EnumStatusPos.ESP_Inactive.getIndex());
	//
	// // 读取相应在职的staff，应该查找不到该staff
	// StaffMapper staffMapper = (StaffMapper)
	// applicationContext.getBean("staffMapper");
	// Staff s = new Staff();
	// s.setID(staff.getID());
	// s.setInt1(Staff.EnumStatusStaff.ESS_Incumbent.getIndex());
	// Map<String, Object> retrieve1Param =
	// s.getRetrieve1Param(BaseBO.INVALID_CASE_ID, s);
	// Staff staffRetrieve1 = (Staff) staffMapper.retrieve1(retrieve1Param);
	// Assert.assertNull(staffRetrieve1);
	// }

	// private Shop createShop() throws CloneNotSupportedException,
	// InterruptedException {
	// ShopMapper mapper = (ShopMapper) applicationContext.getBean("shopMapper");
	//
	// Shop s = new Shop();
	// s.setName("博昕" + String.valueOf(System.currentTimeMillis()).substring(6));
	// s.setCompanyID(1);
	// s.setAddress("岗顶");
	// s.setStatus(1);
	// s.setBxStaffID(1);
	// s.setLongitude((float) 123.123);
	// s.setLatitude((float) 123.12);
	// s.setKey("123456");
	// s.setRemark("备注");
	// Thread.sleep(1);
	//
	// Map<String, Object> paramsForCreate =
	// s.getCreateParam(BaseBO.INVALID_CASE_ID, s);
	// DataSourceContextHolder.setDbName(Shared.DBName_Test);
	// Shop shopCreate = (Shop) mapper.create(paramsForCreate); // ...
	// Assert.assertNotNull(shopCreate);
	// Assert.assertEquals(EnumErrorCode.values()[Integer.parseInt(paramsForCreate.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())],
	// EnumErrorCode.EC_NoError);
	//
	// s.setIgnoreIDInComparision(true);
	// if (s.compareTo(shopCreate) != 0) {
	// Assert.assertTrue(false, "创建的对象的字段与DB读出的不相等");
	// }
	//
	// return shopCreate;
	// }
	//
	// private Pos createPos(Shop shopCreate) throws Exception {
	// Pos pos = DataInput.getPos();
	// pos.setShopID(shopCreate.getID());//
	// MvcResult ret = mvc.perform(//
	// post("/pos/getTokenEx.bx")//
	// .contentType(MediaType.APPLICATION_JSON)//
	// .param(pos.getFIELD_NAME_passwordInPOS(), Shared.PASSWORD_DEFAULT)//
	// )//
	// .andExpect(status().isOk())//
	// .andDo(print())//
	// .andReturn();
	// Shared.checkJSONErrorCode(ret);
	// String encrypt = Shared.encrypt(ret, Shared.PASSWORD_DEFAULT);
	//
	// Shared.resetPOS(mvc, 1);
	// //
	// pos.setPwdEncrypted(encrypt);
	// MvcResult mr = mvc.perform(//
	// DataInput.getBuilder("/posSync/createEx.bx", MediaType.APPLICATION_JSON, pos)
	// //
	// .param(pos.getFIELD_NAME_int1(), "1")//
	// .session((MockHttpSession) Shared.getPosLoginSession(mvc, 1)) //
	// )//
	// .andExpect(status().isOk())//
	// .andDo(print())//
	// .andReturn();
	//
	// Shared.checkJSONErrorCode(mr);
	//
	// JSONObject o = JSONObject.fromObject(mr.getResponse().getContentAsString());
	// //
	// JSONObject jsonObject = JsonPath.read(o, "$.object");
	// Pos p = (Pos) JSONObject.toBean(jsonObject, Pos.class);
	//
	// return p;
	// }
	//
	// private Staff createStaff(Shop shopCreate) throws Exception {
	// String Staff_Phone = "152007" + (int) (Math.random() * 9000 + 1000) + 1;
	//
	// Staff staff1 = new Staff();
	// System.out.println("-------------------case1:正常添加------------------------");
	// MvcResult ret = mvc.perform(//
	// post("/staff/getTokenEx.bx")//
	// .contentType(MediaType.APPLICATION_JSON)//
	// .param(staff1.getFIELD_NAME_phone(), Staff_Phone)//
	// )//
	// .andExpect(status().isOk())//
	// .andDo(print())//
	// .andReturn();
	// Shared.checkJSONErrorCode(ret);
	// String encrypt = Shared.encrypt(ret, Shared.PASSWORD_DEFAULT);
	//
	// Staff staff = DataInput.getStaff();
	// staff.setPhone(Staff_Phone);
	// staff.setPwdEncrypted(encrypt);
	// MvcResult mr = mvc.perform(//
	// DataInput.getBuilder("/staffSync/createEx.bx", MediaType.APPLICATION_JSON,
	// staff)//
	// .session((MockHttpSession) Shared.getStaffLoginSession(mvc,
	// Shared.PhoneOfBoss))//
	// .param(staff1.getFIELD_NAME_shopID(), String.valueOf(shopCreate.getID()))//
	// .param(staff1.getFIELD_NAME_int1(), "1")//
	// )//
	// .andExpect(status().isOk())//
	// .andDo(print())//
	// .andReturn();//
	// Shared.checkJSONErrorCode(mr);
	//
	// JSONObject o = JSONObject.fromObject(mr.getResponse().getContentAsString());
	// //
	// JSONObject jsonObject = JsonPath.read(o, "$.object");
	// Staff s = (Staff) JSONObject.toBean(jsonObject, Staff.class);
	//
	// return s;
	// }

	// @Test
	// public void testDeleteExTest() throws Exception {
	// Shared.printTestMethodStartInfo();
	//
	// session = Shared.getStaffLoginSession(mvc, Shared.PhoneOfBoss);
	//
	// System.out.println("---------------------------case1:删除存在门店。创建一个门店，一个staff和一个pos机。可以直接删除，错误码为0------------------------------");
	// // 创建一个门店，调用syncAction一个staff和一个pos
	// Shop shopCreate = createShop();
	// Pos posCreate = createPos(shopCreate);
	// Staff staffCreate = createStaff(shopCreate);
	//
	// Shop s = new Shop();
	// MvcResult mr = mvc.perform(//
	// post("/shop/deleteEx.bx") //
	// .param(s.getFIELD_NAME_ID(), String.valueOf(shopCreate.getID()))//
	// .contentType(MediaType.APPLICATION_JSON)//
	// .session((MockHttpSession) session)//
	// ) //
	// .andExpect(status().isOk())//
	// .andDo(print())//
	// .andReturn();
	//
	// Shared.checkJSONErrorCode(mr);
	//
	// // 检查普通缓存是否已经删除成功
	// checkCacheForStaffAndPos(posCreate, staffCreate);
	//
	// // 检查同步块缓存是否按照预期进行处理，删除之前同步块，生成D型同步块
	// checkPosAndStaffSyncCache(posCreate, staffCreate);
	//
	// // 检查门店和所有与该门店相关pos和staff的状态并检查登录情况
	// checkStatusOfPosAndShopAndStaff(shopCreate, posCreate, staffCreate);
	//
	// System.out.println("---------------------------case2:删除存在门店。创建一个门店，两个staff和两个pos机。可以直接删除，错误码为0---------------------------------");
	// // 创建一个门店，两个staff和两个pos机,将相应的pos和staff加入普通缓存
	// Shop shopCreate1 = createShop();
	// Pos posCreate1 = createPos(shopCreate1);
	// Pos posCreate2 = createPos(shopCreate1);
	// Staff staffCreate1 = createStaff(shopCreate1);
	// Staff staffCreate2 = createStaff(shopCreate1);
	//
	// MvcResult mr2 = mvc.perform(//
	// post("/shop/deleteEx.bx") //
	// .param(s.getFIELD_NAME_ID(), String.valueOf(shopCreate1.getID()))//
	// .contentType(MediaType.APPLICATION_JSON)//
	// .session((MockHttpSession) session)//
	// ) //
	// .andExpect(status().isOk())//
	// .andDo(print())//
	// .andReturn();
	//
	// Shared.checkJSONErrorCode(mr2);
	//
	// checkCacheForStaffAndPos(posCreate1, staffCreate1);
	// checkCacheForStaffAndPos(posCreate2, staffCreate2);
	// //
	// checkPosAndStaffSyncCache(posCreate1, staffCreate1);
	// checkPosAndStaffSyncCache(posCreate2, staffCreate2);
	// //
	// checkStatusOfPosAndShopAndStaff(shopCreate1, posCreate1, staffCreate1);
	// checkLoginForStaffAndPos(posCreate1, staffCreate1);
	// //
	// checkStatusOfPosAndStaff(posCreate2, staffCreate2);
	// checkLoginForStaffAndPos(posCreate2, staffCreate2);
	//
	// System.out.println("---------------------------case3:删除不存在门店。可以直接删除，错误码为0---------------------------------");
	// MvcResult mr3 = mvc.perform(//
	// post("/shop/deleteEx.bx") //
	// .param(s.getFIELD_NAME_ID(), String.valueOf(-1))//
	// .contentType(MediaType.APPLICATION_JSON)//
	// .session((MockHttpSession) session)//
	// ) //
	// .andExpect(status().isOk())//
	// .andDo(print())//
	// .andReturn();
	//
	// Shared.checkJSONErrorCode(mr3);
	//
	// System.out.println("---------------------------case4:没有权限进行删除---------------------------------");
	// MvcResult mr4 = mvc.perform(//
	// post("/shop/deleteEx.bx") //
	// .param(s.getFIELD_NAME_ID(), String.valueOf(1))//
	// .contentType(MediaType.APPLICATION_JSON)//
	// .session((MockHttpSession) Shared.getBXStaffLoginSession(mvc, Staff2Phone))//
	// ) //
	// .andExpect(status().isOk())//
	// .andDo(print())//
	// .andReturn();
	//
	// Shared.checkJSONErrorCode(mr4, EnumErrorCode.EC_NoPermission);
	// }

	// private void checkLoginForStaffAndPos(Pos posCreate1, Staff staffCreate1)
	// throws Exception, UnsupportedEncodingException {
	// checkPosLogin(String.valueOf(posCreate1.getID()));
	// checkStaffLogin(staffCreate1.getPhone());
	// }
	//
	// // 检查普通缓存是否已经删除成功
	// private void checkCacheForStaffAndPos(Pos posCreate, Staff staffCreate) {
	// ErrorInfo ecOut = new ErrorInfo();
	// Pos pos = (Pos) CacheManager.getCache(Shared.DBName_Test,
	// EnumCacheType.ECT_POS).read1(posCreate.getID(), BaseBO.SYSTEM, ecOut,
	// Shared.DBName_Test);
	// assertTrue(ecOut.getErrorCode().toString() ==
	// EnumErrorCode.EC_NoError.toString());
	// Staff staff = (Staff) CacheManager.getCache(Shared.DBName_Test,
	// EnumCacheType.ECT_Staff).read1(staffCreate.getID(), staffCreate.getID(),
	// ecOut, Shared.DBName_Test);
	// assertTrue(ecOut.getErrorCode().toString() ==
	// EnumErrorCode.EC_NoError.toString());
	//
	// assertTrue((pos == null || pos.getStatus() == 1) && staff == null);
	// }
	//
	// // 检查同步块缓存是否按照预期进行处理，删除之前同步块，生成D型同步块
	// private void checkPosAndStaffSyncCache(Pos posCreate1, Staff staffCreate1) {
	// checkPosSyncCache(posCreate1);
	// checkStaffSyncCache(staffCreate1);
	// }
	//
	// private void checkPosSyncCache(Pos pos) {
	// PosSyncCacheMapper mapper = (PosSyncCacheMapper)
	// applicationContext.getBean("posSyncCacheMapper");
	// PosSyncCache sc = new PosSyncCache();
	// Map<String, Object> paramsForRetrieveN =
	// sc.getRetrieveNParam(BaseBO.INVALID_CASE_ID, sc);
	// DataSourceContextHolder.setDbName(Shared.DBName_Test);
	// List<BaseModel> posSyncCachelist = (List<BaseModel>)
	// mapper.retrieveN(paramsForRetrieveN); // ...
	// Assert.assertEquals(EnumErrorCode.values()[Integer.parseInt(paramsForRetrieveN.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())],
	// EnumErrorCode.EC_NoError);
	//
	// Boolean bDTypeExist = false;
	// for (BaseModel bm : posSyncCachelist) {
	// PosSyncCache posSyncCache = (PosSyncCache) bm;
	// if (posSyncCache.getSyncData_ID() == pos.getID() &&
	// posSyncCache.getSyncType().equals(SyncCache.SYNC_Type_D)) {
	// bDTypeExist = true;
	// } else if (posSyncCache.getSyncData_ID() == pos.getID() &&
	// posSyncCache.getSyncType().equals(SyncCache.SYNC_Type_U)//
	// || posSyncCache.getSyncData_ID() == pos.getID() &&
	// posSyncCache.getSyncType().equals(SyncCache.SYNC_Type_C)) {
	// bDTypeExist = false;
	// break;
	// }
	// }
	// assertTrue(bDTypeExist);
	// }
	//
	// private void checkStaffSyncCache(Staff staff) {
	// StaffSyncCacheMapper mapper = (StaffSyncCacheMapper)
	// applicationContext.getBean("staffSyncCacheMapper");
	// StaffSyncCache sc = new StaffSyncCache();
	// Map<String, Object> paramsForRetrieveN =
	// sc.getRetrieveNParam(BaseBO.INVALID_CASE_ID, sc);
	// List<BaseModel> staffSyncCachelist = (List<BaseModel>)
	// mapper.retrieveN(paramsForRetrieveN); // ...
	// Assert.assertEquals(EnumErrorCode.values()[Integer.parseInt(paramsForRetrieveN.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())],
	// EnumErrorCode.EC_NoError);
	//
	// Boolean bDTypeExist = false;
	// for (BaseModel bm : staffSyncCachelist) {
	// StaffSyncCache staffSyncCache = (StaffSyncCache) bm;
	// if (staffSyncCache.getSyncData_ID() == staff.getID() &&
	// staffSyncCache.getSyncType().equals("D")) {
	// bDTypeExist = true;
	// } else if (staffSyncCache.getSyncData_ID() == staff.getID() &&
	// staffSyncCache.getSyncType().equals(SyncCache.SYNC_Type_U)
	// || staffSyncCache.getSyncData_ID() == staff.getID() &&
	// staffSyncCache.getSyncType().equals(SyncCache.SYNC_Type_C)) {
	// bDTypeExist = false;
	// break;
	// }
	// }
	// assertTrue(bDTypeExist);
	// }
	//
	// // 检查门店和所有与该门店相关pos和staff的状态并检查登录情况
	// private void checkStatusOfPosAndShopAndStaff(Shop shopCreate, Pos posCreate,
	// Staff staff) throws Exception {
	// checkShopStatus(shopCreate.getID());
	//
	// checkStatusOfPosAndStaff(posCreate, staff);
	// }
	//
	// private void checkPosLogin(String posID) throws Exception,
	// UnsupportedEncodingException {
	//
	// Pos p = new Pos();
	// // ..拿到公钥
	// MvcResult ret =
	// mvc.perform(post("/pos/getTokenEx.bx").contentType(MediaType.APPLICATION_JSON).param(p.getFIELD_NAME_ID(),
	// posID)).andExpect(status().isOk()).andDo(print()).andReturn();
	//
	// String pwdEncrypted = Shared.encrypt(ret, Shared.PASSWORD_DEFAULT);
	// p.setPwdEncrypted(pwdEncrypted);
	//
	// MvcResult mr = mvc
	// .perform(post("/pos/loginEx.bx").param(p.getFIELD_NAME_ID(),
	// posID).param(p.getFIELD_NAME_pwdEncrypted(),
	// pwdEncrypted).session((MockHttpSession)
	// ret.getRequest().getSession()).contentType(MediaType.APPLICATION_JSON))
	// .andExpect(status().isOk()).andDo(print()).andReturn();
	//
	// Assert.assertNotNull(mr);
	// //注释原因：黑客行为返回的mr为空。
	// //Shared.checkJSONErrorCode(mr, EnumErrorCode.EC_Hack);
	// }
	//
	// private void checkStaffLogin(String phone) throws Exception,
	// UnsupportedEncodingException {
	// // 离职员工登录，返回EC_Hack
	// Staff s = new Staff();
	// // ..拿到公钥
	// MvcResult ret =
	// mvc.perform(post("/staff/getTokenEx.bx").contentType(MediaType.APPLICATION_JSON).param(s.getFIELD_NAME_phone(),
	// phone)).andExpect(status().isOk()).andDo(print()).andReturn();
	//
	// String json = ret.getResponse().getContentAsString();
	// JSONObject o = JSONObject.fromObject(json);
	// String modulus = JsonPath.read(o, "$.rsa.modulus");
	// String exponent = JsonPath.read(o, "$.rsa.exponent");
	// modulus = new BigInteger(modulus, 16).toString();
	// exponent = new BigInteger(exponent, 16).toString();
	//
	// RSAPublicKey publicKey = RSAUtils.getPublicKey(modulus, exponent);
	//
	// final String pwd = "123456";
	// // ..加密密码
	// String pwdEncrypted = RSAUtils.encryptByPublicKey(pwd, publicKey);
	// s.setPwdEncrypted(pwdEncrypted);
	//
	// MvcResult mr = mvc.perform(//
	// post("/staff/loginEx.bx")//
	// .param(s.getFIELD_NAME_string1(), Shared.DB_SN_Test)//
	// .param(s.getFIELD_NAME_int1(), "1")//
	// .param(s.getFIELD_NAME_phone(), phone)//
	// .param(s.getFIELD_NAME_pwdEncrypted(), pwdEncrypted)//
	// .session((MockHttpSession) ret.getRequest().getSession())//
	// .contentType(MediaType.APPLICATION_JSON)//
	// )//
	// .andExpect(status().isOk())//
	// .andDo(print())//
	// .andReturn();
	//
	// Shared.checkJSONErrorCode(mr, EnumErrorCode.EC_Hack);
	// }

	// @Test 暂时未使用到该接口
	public void testRetrieveNEx() throws Exception {
		Shared.printTestMethodStartInfo();
		// Shop s=new Shop();

		System.out.println("\n------------------------ case1:正常查询多个个门店 ------------------------");
		MvcResult mr = mvc.perform(//
				get("/shop/retrieveNEx.bx")//
						.contentType(MediaType.APPLICATION_JSON)//
						.session((MockHttpSession) Shared.getBXStaffLoginSession(mvc, Shared.PhoneOfOP))//
		) //
				.andExpect(status().isOk())//
				.andDo(print())//
				.andReturn();

		Shared.checkJSONErrorCode(mr);

		System.out.println("\n------------------------ case2:输入iDistrictID作为条件查询------------------------");
		MvcResult mr1 = mvc.perform(//
				get("/shop/retrieveNEx.bx")//
						.param(Shop.field.getFIELD_NAME_districtID(), "1")//
						.contentType(MediaType.APPLICATION_JSON)//
						.session((MockHttpSession) Shared.getBXStaffLoginSession(mvc, Shared.PhoneOfBoss))//
		) //
				.andExpect(status().isOk())//
				.andDo(print())//
				.andReturn();

		Shared.checkJSONErrorCode(mr1);
		JSONObject o = JSONObject.fromObject(mr1.getResponse().getContentAsString());
		List<Integer> list = JsonPath.read(o, "$.objectList[*].districtID");
		for (int i : list) {
			Assert.assertTrue(i == 1);
		}
	}

	// @Test 暂时未使用到该接口
	public void testRetrieveNEx3() throws Exception {
		Shared.printTestMethodStartInfo();

		Shared.caseLog("Case3:非OP账号进行BXStaff登录进行查询");
		MvcResult mr = mvc.perform(//
				get("/shop/retrieveNEx.bx")//
						.contentType(MediaType.APPLICATION_JSON)//
						.session((MockHttpSession) Shared.getBXStaffLoginSession(mvc, Shared.PhoneOfBoss))//
		) //
				.andExpect(status().isOk())//
				.andDo(print())//
				.andReturn();
		//
		String json = mr.getResponse().getContentAsString();
		Assert.assertTrue("".equals(json), "测试预期结果为空");
	}

	// @Test 暂时未使用到该接口
	public void testRetrieveNEx4() throws Exception {
		Shared.printTestMethodStartInfo();

		Shared.caseLog("Case4:非OP账号进行Staff登录进行查询");
		MvcResult mr = mvc.perform(//
				get("/shop/retrieveNEx.bx")//
						.contentType(MediaType.APPLICATION_JSON)//
						.session((MockHttpSession) Shared.getStaffLoginSession(mvc, Shared.PhoneOfBoss))//
		) //
				.andExpect(status().isOk())//
				.andDo(print())//
				.andReturn();
		//
		String json = mr.getResponse().getContentAsString();
		Assert.assertTrue("".equals(json), "测试预期结果为空");
	}

	@Test
	public void testRetrieve1Ex() throws Exception {
		Shared.printTestMethodStartInfo();

		Shared.caseLog("case1:正常查询一个门店");
		Shop shopCreate = BaseShopTest.createViaMapper(BaseShopTest.DataInput.getShop());
		BaseShopTest.retrieve1ViaAction(shopCreate, mvc, sessionBoss);
	}

	@Test
	public void testRetrieve2Ex() throws Exception {
		Shared.printTestMethodStartInfo();

		Shared.caseLog("case2:用不存在的ID查询门店 ");
		MvcResult mr2 = mvc.perform(//
				get("/shop/retrieve1Ex.bx?" + Staff.field.getFIELD_NAME_ID() + "=" + BaseAction.INVALID_ID + "&" + Shop.field.getFIELD_NAME_dbName() + "=" + Shared.DBName_Test)//
						.contentType(MediaType.APPLICATION_JSON)//
						.session((MockHttpSession) sessionBoss)//
		) //
				.andExpect(status().isOk())//
				.andDo(print())//
				.andReturn();

		Shared.checkJSONErrorCode(mr2, EnumErrorCode.EC_OtherError);
	}

	@Test
	public void testRetrieve3Ex() throws Exception {
		Shared.printTestMethodStartInfo();

		Shared.caseLog("case3:用不存在的DB查询门店");
		MvcResult mr3 = mvc.perform(//
				get("/shop/retrieve1Ex.bx?" + Staff.field.getFIELD_NAME_ID() + "=1&" + Shop.field.getFIELD_NAME_dbName() + "=" + "1111")//
						.contentType(MediaType.APPLICATION_JSON)//
						.session((MockHttpSession) sessionBoss)//
		) //
				.andExpect(status().isOk())//
				.andDo(print())//
				.andReturn();

		Assert.assertTrue("".equals(mr3.getResponse().getContentAsString()));
		// System.out.println("\n------------------------ case3:没有权限进行查询
		// ------------------------");
		// MvcResult mr3 = mvc.perform(//
		// get("/shop/retrieve1Ex.bx?" + s.getFIELD_NAME_ID() + "=1")//
		// .contentType(MediaType.APPLICATION_JSON)//
		// .session((MockHttpSession) Shared.getBXStaffLoginSession(mvc, Staff2Phone))//
		// ) //
		// .andExpect(status().isOk())//
		// .andDo(print())//
		// .andReturn();
		//
		// Shared.checkJSONErrorCode(mr3, EnumErrorCode.EC_NoPermission);
	}

	@Test
	public void testRetrieve1Ex4() throws Exception {
		Shared.printTestMethodStartInfo();

		Shared.caseLog("Case4:非OP账号进行BXStaff登录进行查询");
		HttpSession session1 = Shared.getBXStaffLoginSession(mvc, Shared.PhoneOfBoss);
		MvcResult mr = mvc.perform(//
				get("/shop/retrieve1Ex.bx?" + Shop.field.getFIELD_NAME_ID() + "=1&" + Shop.field.getFIELD_NAME_dbName() + "=" + Shared.DBName_Test)//
						.contentType(MediaType.APPLICATION_JSON)//
						.session((MockHttpSession) session1)//
		) //
				.andExpect(status().isOk())//
				.andDo(print())//
				.andReturn();
		//
		String json = mr.getResponse().getContentAsString();
		Assert.assertTrue("".equals(json), "测试预期结果为空");
	}

	@Test
	public void testRetrieve1Ex5() throws Exception {
		Shared.printTestMethodStartInfo();

		Shared.caseLog("Case4:非OP账号进行Staff登录进行查询");
		HttpSession session1 = Shared.getStaffLoginSession(mvc, Shared.PhoneOfBoss);
		MvcResult mr = mvc.perform(//
				get("/shop/retrieve1Ex.bx?" + Shop.field.getFIELD_NAME_ID() + "=1&" + Shop.field.getFIELD_NAME_dbName() + "=" + Shared.DBName_Test)//
						.contentType(MediaType.APPLICATION_JSON)//
						.session((MockHttpSession) session1)//
		) //
				.andExpect(status().isOk())//
				.andDo(print())//
				.andReturn();
		//
		String json = mr.getResponse().getContentAsString();
		Assert.assertTrue("".equals(json), "测试预期结果为空");
	}
	// @Test
	// public void testCreateEx() throws Exception {
	// Shared.printTestMethodStartInfo();
	//
	// session = Shared.getBXStaffLoginSession(mvc, PhoneOfOP);
	// //session = Shared.getStaffLoginSession(mvc, Shared.PhoneOfBoss);
	// Shop s = new Shop();
	//
	// System.out.println("---------------------case1：正常创建-----------------------------");
	// MvcResult mr = mvc.perform(//
	// post("/shop/createEx.bx")//
	// .session((MockHttpSession) session)//
	// .param(s.getFIELD_NAME_name(), "博昕" + System.currentTimeMillis() % 1000000)//
	// .param(s.getFIELD_NAME_companyID(), "1")//
	// .param(s.getFIELD_NAME_address(), "岗顶" + System.currentTimeMillis() %
	// 1000000)//
	// .param(s.getFIELD_NAME_status(), "1")//
	// .param(s.getFIELD_NAME_longitude(), "123.123")//
	// .param(s.getFIELD_NAME_latitude(), "123.12")//
	// .param(s.getFIELD_NAME_key(), "123456" + System.currentTimeMillis() %
	// 1000000)//
	// .param(s.getFIELD_NAME_bxStaffID(), "1")//
	// .param(s.getFIELD_NAME_remark(), "")//
	// .contentType(MediaType.APPLICATION_JSON)//
	// )//
	// .andExpect(status().isOk())//
	// .andDo(print())//
	// .andReturn();
	//
	// Shared.checkJSONErrorCode(mr);
	//
	// System.out.println("---------------------------case2:添加重复公司名称的重复名称门店--------------------------------------");
	// MvcResult mr1 = mvc.perform(//
	// post("/shop/createEx.bx")//
	// .session((MockHttpSession) session)//
	// .param(s.getFIELD_NAME_name(), "博昕小卖部")//
	// .param(s.getFIELD_NAME_companyID(), "1")//
	// .param(s.getFIELD_NAME_address(), "岗顶" + System.currentTimeMillis() %
	// 1000000)//
	// .param(s.getFIELD_NAME_status(), "1")//
	// .param(s.getFIELD_NAME_longitude(), "123.123")//
	// .param(s.getFIELD_NAME_latitude(), "123.12")//
	// .param(s.getFIELD_NAME_key(), "123456" + System.currentTimeMillis() %
	// 1000000)//
	// .param(s.getFIELD_NAME_bxStaffID(), "1")//
	// .param(s.getFIELD_NAME_remark(), "")//
	// .contentType(MediaType.APPLICATION_JSON)//
	// )//
	// .andExpect(status().isOk())//
	// .andDo(print())//
	// .andReturn();
	//
	// Shared.checkJSONErrorCode(mr1, EnumErrorCode.EC_Duplicated);
	//
	// System.out.println("---------------------------case3：添加不存在的公司的门店
	// --------------------------------------");
	// MvcResult mr2 = mvc.perform(//
	// post("/shop/createEx.bx")//
	// .session((MockHttpSession) session)//
	// .param(s.getFIELD_NAME_name(), "博昕小卖部")//
	// .param(s.getFIELD_NAME_companyID(), "-1")//
	// .param(s.getFIELD_NAME_address(), "岗顶" + System.currentTimeMillis() %
	// 1000000)//
	// .param(s.getFIELD_NAME_status(), "1")//
	// .param(s.getFIELD_NAME_longitude(), "123.123")//
	// .param(s.getFIELD_NAME_latitude(), "123.12")//
	// .param(s.getFIELD_NAME_key(), "123456" + System.currentTimeMillis() %
	// 1000000)//
	// .param(s.getFIELD_NAME_bxStaffID(), "1")//
	// .param(s.getFIELD_NAME_remark(), "")//
	// .contentType(MediaType.APPLICATION_JSON)//
	// )//
	// .andExpect(status().isOk())//
	// .andDo(print())//
	// .andReturn();
	//
	// Shared.checkJSONErrorCode(mr2, EnumErrorCode.EC_BusinessLogicNotDefined);
	//
	// System.out.println("---------------------------case4：添加不存在的业务经理的门店
	// --------------------------------------");
	// MvcResult mr3 = mvc.perform(//
	// post("/shop/createEx.bx")//
	// .session((MockHttpSession) session)//
	// .param(s.getFIELD_NAME_name(), "博昕小卖部")//
	// .param(s.getFIELD_NAME_companyID(), "1")//
	// .param(s.getFIELD_NAME_address(), "岗顶" + System.currentTimeMillis() %
	// 1000000)//
	// .param(s.getFIELD_NAME_status(), "1")//
	// .param(s.getFIELD_NAME_longitude(), "123.123")//
	// .param(s.getFIELD_NAME_latitude(), "123.12")//
	// .param(s.getFIELD_NAME_key(), "123456" + System.currentTimeMillis() %
	// 1000000)//
	// .param(s.getFIELD_NAME_bxStaffID(), "-9999")//
	// .param(s.getFIELD_NAME_remark(), "")//
	// .contentType(MediaType.APPLICATION_JSON)//
	// )//
	// .andExpect(status().isOk())//
	// .andDo(print())//
	// .andReturn();
	//
	// Shared.checkJSONErrorCode(mr3, EnumErrorCode.EC_BusinessLogicNotDefined);
	//
	// System.out.println("---------------------------case5：没有权限进行创建--------------------------------------");
	// MvcResult mr4 = mvc.perform(//
	// post("/shop/createEx.bx")//
	// .session((MockHttpSession) Shared.getBXStaffLoginSession(mvc, Staff2Phone))//
	// .param(s.getFIELD_NAME_name(), "博昕" + System.currentTimeMillis() % 1000000)//
	// .param(s.getFIELD_NAME_companyID(), "1")//
	// .param(s.getFIELD_NAME_address(), "岗顶" + System.currentTimeMillis() %
	// 1000000)//
	// .param(s.getFIELD_NAME_status(), "1")//
	// .param(s.getFIELD_NAME_longitude(), "123.123")//
	// .param(s.getFIELD_NAME_latitude(), "123.12")//
	// .param(s.getFIELD_NAME_key(), "123456" + System.currentTimeMillis() %
	// 1000000)//
	// .param(s.getFIELD_NAME_bxStaffID(), "1")//
	// .param(s.getFIELD_NAME_remark(), "")//
	// .contentType(MediaType.APPLICATION_JSON)//
	// )//
	// .andExpect(status().isOk())//
	// .andDo(print())//
	// .andReturn();
	//
	// Shared.checkJSONErrorCode(mr4, EnumErrorCode.EC_NoPermission);
	// }

	// @Test
	// public void testUpdateEx() throws Exception {
	// Shared.printTestMethodStartInfo();
	//
	// session = Shared.getStaffLoginSession(mvc, Shared.PhoneOfBoss);
	// Shop s = new Shop();
	//
	// System.out.println("---------------------case1：正常更改-----------------------------");
	// Shop shopCreate = createShop();
	// MvcResult mr = mvc.perform(//
	// post("/shop/updateEx.bx")//
	// .session((MockHttpSession) session)//
	// .param(s.getFIELD_NAME_name(), "博昕" + System.currentTimeMillis() % 1000000)//
	// .param(s.getFIELD_NAME_companyID(), "1")//
	// .param(s.getFIELD_NAME_address(), "岗顶" + System.currentTimeMillis() %
	// 1000000)//
	// .param(s.getFIELD_NAME_ID(), String.valueOf(shopCreate.getID()))//
	// .param(s.getFIELD_NAME_longitude(), "123.123")//
	// .param(s.getFIELD_NAME_latitude(), "123.12")//
	// .param(s.getFIELD_NAME_key(), "123456" + System.currentTimeMillis() %
	// 1000000)//
	// .param(s.getFIELD_NAME_remark(), "")//
	// .contentType(MediaType.APPLICATION_JSON)//
	// )//
	// .andExpect(status().isOk())//
	// .andDo(print())//
	// .andReturn();
	//
	// Shared.checkJSONErrorCode(mr);
	//
	// System.out.println("---------------------------case2:修改成重复的门店名称--------------------------------------");
	// MvcResult mr1 = mvc.perform(//
	// post("/shop/updateEx.bx")//
	// .session((MockHttpSession) session)//
	// .param(s.getFIELD_NAME_name(), "博昕小卖部")//
	// .param(s.getFIELD_NAME_companyID(), "1")//
	// .param(s.getFIELD_NAME_address(), "岗顶" + System.currentTimeMillis() %
	// 1000000)//
	// .param(s.getFIELD_NAME_ID(), String.valueOf(shopCreate.getID()))//
	// .param(s.getFIELD_NAME_longitude(), "123.123")//
	// .param(s.getFIELD_NAME_latitude(), "123.12")//
	// .param(s.getFIELD_NAME_key(), "123456" + System.currentTimeMillis() %
	// 1000000)//
	// .param(s.getFIELD_NAME_remark(), "")//
	// .contentType(MediaType.APPLICATION_JSON)//
	// )//
	// .andExpect(status().isOk())//
	// .andDo(print())//
	// .andReturn();
	//
	// Shared.checkJSONErrorCode(mr1, EnumErrorCode.EC_Duplicated);
	//
	// System.out.println("---------------------------case3：没有权限进行修改-----------------------------");
	// MvcResult mr3 = mvc.perform(//
	// post("/shop/updateEx.bx")//
	// .session((MockHttpSession) Shared.getBXStaffLoginSession(mvc, Staff2Phone))//
	// .param(s.getFIELD_NAME_name(), "博昕" + System.currentTimeMillis() % 1000000)//
	// .param(s.getFIELD_NAME_companyID(), "1")//
	// .param(s.getFIELD_NAME_address(), "岗顶" + System.currentTimeMillis() %
	// 1000000)//
	// .param(s.getFIELD_NAME_ID(), String.valueOf(shopCreate.getID()))//
	// .param(s.getFIELD_NAME_longitude(), "123.123")//
	// .param(s.getFIELD_NAME_latitude(), "123.12")//
	// .param(s.getFIELD_NAME_key(), "123456" + System.currentTimeMillis() %
	// 1000000)//
	// .param(s.getFIELD_NAME_remark(), "")//
	// .contentType(MediaType.APPLICATION_JSON)//
	// )//
	// .andExpect(status().isOk())//
	// .andDo(print())//
	// .andReturn();
	//
	// Shared.checkJSONErrorCode(mr3, EnumErrorCode.EC_NoPermission);
	// }

	// @Test
	// public void testRetrieveNToCheckUniqueFieldEx1() throws Exception {
	// Shared.printTestMethodStartInfo();
	//
	// System.out.println("----------------------------Case1:传入一个不存在的门店名称，进行创建------------------------------------------");
	// MvcResult mr1 = mvc.perform(//
	// post("/shop/retrieveNToCheckUniqueFieldEx.bx")//
//						.contentType(MediaType.APPLICATION_JSON)//
//						.session((MockHttpSession) session)//
//						.param(Shop.field.getFIELD_NAME_companyID(), String.valueOf(BaseCompanyTest.nbrCloned.getID())) //
//						.param(Shop.field.getFIELD_NAME_fieldToCheckUnique(), String.valueOf(Shop.CASE_CheckName)) //
//						.param(Shop.field.getFIELD_NAME_uniqueField(), "部卖小昕博")//
//		)//
//				.andExpect(status().isOk())//
	// .andDo(print())//
	// .andReturn();
	// Shared.checkJSONErrorCode(mr1);

	// System.out.println("----------------------------Case10:没有权限进行查询-----------------------------------------");
	// MvcResult mr10 = mvc.perform(//
	// post("/shop/retrieveNToCheckUniqueFieldEx.bx")//
	// .contentType(MediaType.APPLICATION_JSON)//
	// .session((MockHttpSession) Shared.getBXStaffLoginSession(mvc,
	// Staff2Phone))//\
	// .param(shop.getFIELD_NAME_int1(), "1")
	// .param(shop.getFIELD_NAME_uniqueField(), "博昕小卖部")//
	// )//
	// .andExpect(status().isOk())//
	// .andDo(print())//
	// .andReturn();
	// Shared.checkJSONErrorCode(mr10, EnumErrorCode.EC_NoPermission);
	// }

	// @Test
	// public void testRetrieveNToCheckUniqueFieldEx2() throws Exception {
	//
//		System.out.println("----------------------------Case2:传入一个已存在的名称，进行创建------------------------------------------");
//		MvcResult mr2 = mvc.perform(//
//				post("/shop/retrieveNToCheckUniqueFieldEx.bx")//
//						.contentType(MediaType.APPLICATION_JSON)//
//						.session((MockHttpSession) session)//
//						.param(Shop.field.getFIELD_NAME_companyID(), String.valueOf(BaseCompanyTest.nbrCloned.getID())) //
//						.param(Shop.field.getFIELD_NAME_fieldToCheckUnique(), String.valueOf(Shop.CASE_CheckName))//
//						.param(Shop.field.getFIELD_NAME_uniqueField(), "博昕小卖部")//
//		)//
//				.andExpect(status().isOk())//
	// .andDo(print())//
	// .andReturn();
	// Shared.checkJSONErrorCode(mr2, EnumErrorCode.EC_Duplicated);
	// }

	// @Test
	// public void testRetrieveNToCheckUniqueFieldEx3() throws Exception {
	//
	// System.out.println("----------------------------Case3:传入错误的int1(小于1)------------------------------------------");
//		MvcResult mr3 = mvc.perform(//
//				post("/shop/retrieveNToCheckUniqueFieldEx.bx")//
//						.contentType(MediaType.APPLICATION_JSON)//
//						.session((MockHttpSession) session)//
//						.param(Shop.field.getFIELD_NAME_companyID(), String.valueOf(BaseCompanyTest.nbrCloned.getID())) //
//						.param(Shop.field.getFIELD_NAME_fieldToCheckUnique(), "0")//
//						.param(Shop.field.getFIELD_NAME_uniqueField(), "博昕小卖部")//
//		)//
//				.andExpect(status().isOk())//
	// .andDo(print())//
	// .andReturn();
	// Shared.checkJSONErrorCode(mr3, EnumErrorCode.EC_WrongFormatForInputField);
	// }

	// @Test
	// public void testRetrieveNToCheckUniqueFieldEx4() throws Exception {
	//
	// System.out.println("----------------------------Case4:传入错误的int1(大于Integer.MAX_VALUE)------------------------------------------");
//		MvcResult mr4 = mvc.perform(//
//				post("/shop/retrieveNToCheckUniqueFieldEx.bx")//
//						.contentType(MediaType.APPLICATION_JSON)//
//						.session((MockHttpSession) session)//
//						.param(Shop.field.getFIELD_NAME_companyID(), String.valueOf(BaseCompanyTest.nbrCloned.getID())) //
//						.param(Shop.field.getFIELD_NAME_fieldToCheckUnique(), String.valueOf(Integer.MAX_VALUE + 1))//
//						.param(Shop.field.getFIELD_NAME_uniqueField(), "博昕小卖部")//
	// )//
	// .andExpect(status().isOk())//
	// .andDo(print())//
	// .andReturn();
	// Shared.checkJSONErrorCode(mr4, EnumErrorCode.EC_WrongFormatForInputField);
	// }

	// @Test
	// public void testRetrieveNToCheckUniqueFieldEx5() throws Exception {
	//
	// System.out.println("----------------------------Case5:传入非法的uniqueField(有非中英文数字的字符)------------------------------------------");
//		MvcResult mr5 = mvc.perform(//
//				post("/shop/retrieveNToCheckUniqueFieldEx.bx")//
//						.contentType(MediaType.APPLICATION_JSON)//
//						.session((MockHttpSession) session)//
//						.param(Shop.field.getFIELD_NAME_companyID(), String.valueOf(BaseCompanyTest.nbrCloned.getID())) //
//						.param(Shop.field.getFIELD_NAME_fieldToCheckUnique(), String.valueOf(Shop.CASE_CheckName))//
	// .param(Shop.field.getFIELD_NAME_uniqueField(), "博_昕小卖部")//
	// )//
	// .andExpect(status().isOk())//
	// .andDo(print())//
	// .andReturn();
	// Shared.checkJSONErrorCode(mr5);
	// }
	//
	// @Test
	// public void testRetrieveNToCheckUniqueFieldEx6() throws Exception {
	//
	// System.out.println("----------------------------Case6:传入非法的uniqueField(首位有空格)------------------------------------------");
	// MvcResult mr6 = mvc.perform(//
	// post("/shop/retrieveNToCheckUniqueFieldEx.bx")//
//						.contentType(MediaType.APPLICATION_JSON)//
//						.session((MockHttpSession) session)//
//						.param(Shop.field.getFIELD_NAME_companyID(), String.valueOf(BaseCompanyTest.nbrCloned.getID())) //
//						.param(Shop.field.getFIELD_NAME_fieldToCheckUnique(), String.valueOf(Shop.CASE_CheckName)).param(Shop.field.getFIELD_NAME_uniqueField(), " 博昕小卖部")//
	// )//
	// .andExpect(status().isOk())//
	// .andDo(print())//
	// .andReturn();
	// Shared.checkJSONErrorCode(mr6, EnumErrorCode.EC_WrongFormatForInputField);
	// }
	//
	// @Test
	// public void testRetrieveNToCheckUniqueFieldEx7() throws Exception {
	//
	// System.out.println("----------------------------Case7:传入非法的uniqueField(尾部有空格)------------------------------------------");
	// MvcResult mr7 = mvc.perform(//
	// post("/shop/retrieveNToCheckUniqueFieldEx.bx")//
//						.contentType(MediaType.APPLICATION_JSON)//
//						.session((MockHttpSession) session)//
//						.param(Shop.field.getFIELD_NAME_companyID(), String.valueOf(BaseCompanyTest.nbrCloned.getID())) //
//						.param(Shop.field.getFIELD_NAME_fieldToCheckUnique(), String.valueOf(Shop.CASE_CheckName))//
//						.param(Shop.field.getFIELD_NAME_uniqueField(), "博昕小卖部 ")//
//		)//
	// .andExpect(status().isOk())//
	// .andDo(print())//
	// .andReturn();
	// Shared.checkJSONErrorCode(mr7, EnumErrorCode.EC_WrongFormatForInputField);
	// }
	//
	// @Test
	// public void testRetrieveNToCheckUniqueFieldEx8() throws Exception {
//
//		System.out.println("----------------------------Case8:传入的uniqueField(中间有空格)------------------------------------------");
//		MvcResult mr8 = mvc.perform(//
//				post("/shop/retrieveNToCheckUniqueFieldEx.bx")//
//						.contentType(MediaType.APPLICATION_JSON)//
//						.session((MockHttpSession) session)//
//						.param(Shop.field.getFIELD_NAME_companyID(), String.valueOf(BaseCompanyTest.nbrCloned.getID())) //
//						.param(Shop.field.getFIELD_NAME_fieldToCheckUnique(), String.valueOf(Shop.CASE_CheckName)).param(Shop.field.getFIELD_NAME_uniqueField(), "博昕 小卖部")//
//		)//
	// .andExpect(status().isOk())//
	// .andDo(print())//
	// .andReturn();
	// Shared.checkJSONErrorCode(mr8);
	// }
	//
	// @Test
	// public void testRetrieveNToCheckUniqueFieldEx9() throws Exception {
	//
	// System.out.println("----------------------------Case9:传入非法的uniqueField(长度大于20)------------------------------------------");
	// MvcResult mr9 = mvc.perform(//
//				post("/shop/retrieveNToCheckUniqueFieldEx.bx")//
//						.contentType(MediaType.APPLICATION_JSON)//
//						.session((MockHttpSession) session)//
//						.param(Shop.field.getFIELD_NAME_companyID(), String.valueOf(BaseCompanyTest.nbrCloned.getID())) //
//						.param(Shop.field.getFIELD_NAME_fieldToCheckUnique(), String.valueOf(Shop.CASE_CheckName))//
//						.param(Shop.field.getFIELD_NAME_uniqueField(), "123456789012345678901")//
//		)//
//				.andExpect(status().isOk())//
//				.andDo(print())//
	// .andReturn();
	// Shared.checkJSONErrorCode(mr9, EnumErrorCode.EC_WrongFormatForInputField);
	// }
	//
	// @Test
	// public void testRetrieveNToCheckUniqueFieldEx10() throws Exception {
	// Shared.printTestMethodStartInfo();
	//
//		Shared.caseLog("Case10:非OP账号进行Staff登录进行检查字段是否唯一");
//
//		HttpSession session1 = Shared.getBXStaffLoginSession(mvc, Shared.PhoneOfBoss);
//		MvcResult mr = mvc.perform(//
//				post("/shop/retrieveNToCheckUniqueFieldEx.bx")//
//						.contentType(MediaType.APPLICATION_JSON)//
//						.session((MockHttpSession) session1)//
//						.param(Shop.field.getFIELD_NAME_companyID(), String.valueOf(BaseCompanyTest.nbrCloned.getID())) //
//						.param(Shop.field.getFIELD_NAME_fieldToCheckUnique(), String.valueOf(Shop.CASE_CheckName)) //
//						.param(Shop.field.getFIELD_NAME_uniqueField(), "部卖小昕博")//
//		)//
//				.andExpect(status().isOk())//
//				.andDo(print())//
//				.andReturn();
	// //
	// String json = mr.getResponse().getContentAsString();
	// Assert.assertTrue("".equals(json), "测试预期结果为空");
	// }
	//
	// @Test
	// public void testRetrieveNToCheckUniqueFieldEx11() throws Exception {
	// Shared.printTestMethodStartInfo();
	//
	// Shared.caseLog("Case11:非OP账号进行Staff登录进行检查字段是否唯一");
//		HttpSession session1 = Shared.getStaffLoginSession(mvc, Shared.PhoneOfBoss);
//		MvcResult mr = mvc.perform(//
//				post("/shop/retrieveNToCheckUniqueFieldEx.bx")//
//						.contentType(MediaType.APPLICATION_JSON)//
//						.session((MockHttpSession) session1)//
//						.param(Shop.field.getFIELD_NAME_companyID(), String.valueOf(BaseCompanyTest.nbrCloned.getID())) //
//						.param(Shop.field.getFIELD_NAME_fieldToCheckUnique(), String.valueOf(Shop.CASE_CheckName)) //
	// .param(Shop.field.getFIELD_NAME_uniqueField(), "部卖小昕博")//
	// )//
	// .andExpect(status().isOk())//
	// .andDo(print())//
	// .andReturn();
	// //
	// String json = mr.getResponse().getContentAsString();
	// Assert.assertTrue("".equals(json), "测试预期结果为空");
	// }
	
	@Test
	public void testRetrieveNEx1() throws Exception {
		sessionBoss = Shared.getStaffLoginSession(mvc, Shared.PhoneOfBoss);
		
		System.out.println("---------------- Case1：正常查询 -------------");
		//
		MvcResult mr = mvc.perform( //
				get("/shop/retrieveNEx.bx") //
				.session((MockHttpSession) Shared.getStaffLoginSession(mvc, Shared.PhoneOfBoss)) //
				.contentType(MediaType.APPLICATION_JSON)) //
				.andExpect(status().isOk()) //
				.andDo(print()).andReturn(); //
		Shared.checkJSONErrorCode(mr);
	}
	
	@Test
	public void testRetrieveNByFieldsEx1() throws Exception {
		sessionBoss = Shared.getStaffLoginSession(mvc, Shared.PhoneOfBoss);
		
		System.out.println("---------------- Case1:根据门店名称去模糊搜索 -------------");
		//
		Shop shop = new Shop();
		shop.setQueryKeyword("默认");
		//
		MvcResult mr = mvc.perform( //
				post("/shop/retrieveNByFieldsEx.bx") //
				.session((MockHttpSession) Shared.getStaffLoginSession(mvc, Shared.PhoneOfBoss)) //
				.param(Shop.field.getFIELD_NAME_queryKeyword(), shop.getQueryKeyword()) //
				.contentType(MediaType.APPLICATION_JSON)) //
				.andExpect(status().isOk()) //
				.andDo(print()).andReturn(); //
		Shared.checkJSONErrorCode(mr);

		JSONObject o = JSONObject.fromObject(mr.getResponse().getContentAsString()); //
		List<?> list = JsonPath.read(o, "$.objectList[*].name");
		//
		for (int i = 0; i < list.size(); i++) {
			String s1 = (String) list.get(i);
			Assert.assertTrue(s1.contains(shop.getQueryKeyword()));
		}
	}
	
	@Test
	public void testRetrieveNByFieldsEx2() throws Exception {
		sessionBoss = Shared.getStaffLoginSession(mvc, Shared.PhoneOfBoss);
		
		System.out.println("---------------- Case2:根据地域ID去模糊搜索 -------------");
		//
		Shop shop = new Shop();
		shop.setDistrictID(1);
		shop.setQueryKeyword("");
		//
		MvcResult mr = mvc.perform( //
				post("/shop/retrieveNByFieldsEx.bx") //
				.session((MockHttpSession) Shared.getStaffLoginSession(mvc, Shared.PhoneOfBoss)) //
				.param(Shop.field.getFIELD_NAME_queryKeyword(), shop.getQueryKeyword()) //
				.param(Shop.field.getFIELD_NAME_districtID(), String.valueOf(shop.getDistrictID())) //
				.contentType(MediaType.APPLICATION_JSON)) //
				.andExpect(status().isOk()) //
				.andDo(print()).andReturn(); //
		Shared.checkJSONErrorCode(mr);

		JSONObject o = JSONObject.fromObject(mr.getResponse().getContentAsString()); //
		List<?> list = JsonPath.read(o, "$.objectList[*].districtID");
		//
		for (int i = 0; i < list.size(); i++) {
			Integer i1 = (Integer) list.get(i);
			Assert.assertTrue(i1 == shop.getDistrictID());
		}
	}
	
	@Test
	public void testRetrieveNByFieldsEx3() throws Exception {
		sessionBoss = Shared.getStaffLoginSession(mvc, Shared.PhoneOfBoss);
		
		System.out.println("---------------- Case3:根据地域ID和门店名称去模糊搜索 -------------");
		//
		Shop shop = new Shop();
		shop.setDistrictID(1);
		shop.setQueryKeyword("默认");
		//
		MvcResult mr = mvc.perform( //
				post("/shop/retrieveNByFieldsEx.bx") //
				.session((MockHttpSession) Shared.getStaffLoginSession(mvc, Shared.PhoneOfBoss)) //
				.param(Shop.field.getFIELD_NAME_queryKeyword(), shop.getQueryKeyword()) //
				.param(Shop.field.getFIELD_NAME_districtID(), String.valueOf(shop.getDistrictID())) //
				.contentType(MediaType.APPLICATION_JSON)) //
				.andExpect(status().isOk()) //
				.andDo(print()).andReturn(); //
		Shared.checkJSONErrorCode(mr);

		JSONObject o = JSONObject.fromObject(mr.getResponse().getContentAsString()); //
		List<?> list = JsonPath.read(o, "$.objectList[*].name");
		List<?> list2 = JsonPath.read(o, "$.objectList[*].districtID");
		//
		for (int i = 0; i < list.size(); i++) {
			String s1 = (String) list.get(i);
			Integer i1 = (Integer) list2.get(i);
			Assert.assertTrue(s1.contains(shop.getQueryKeyword()) && i1 == shop.getDistrictID());
		}
	}
}
