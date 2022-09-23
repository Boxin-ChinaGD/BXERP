package com.bx.erp.test;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.testng.Assert.assertTrue;

import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.security.interfaces.RSAPublicKey;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;

import org.springframework.context.ApplicationContext;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.testng.Assert;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.bx.erp.action.BaseAction;
import com.bx.erp.action.bo.BaseBO;
import com.bx.erp.action.bo.PosBO;
import com.bx.erp.cache.CacheManager;
import com.bx.erp.dao.PosMapper;
import com.bx.erp.model.Barcodes;
import com.bx.erp.model.BaseModel;
import com.bx.erp.model.BxStaff;
import com.bx.erp.model.CommodityType;
import com.bx.erp.model.CacheType.EnumCacheType;
import com.bx.erp.model.ErrorInfo.EnumErrorCode;
import com.bx.erp.model.wx.BaseWxModel;
import com.bx.erp.model.Pos;
import com.bx.erp.model.Pos.EnumStatusPos;
import com.bx.erp.model.Role.EnumTypeRole;
import com.bx.erp.model.commodity.Commodity;
import com.bx.erp.model.purchasing.Provider;
import com.bx.erp.model.Staff;
import com.bx.erp.model.Vip;
import com.bx.erp.model.BaseModel.EnumBoolean;
import com.bx.erp.util.DataSourceContextHolder;
import com.bx.erp.util.OkHttpUtil;
import com.bx.erp.util.RSAUtils;
import com.jayway.jsonpath.JsonPath;
import net.sf.json.JSONObject;
import okhttp3.OkHttpClient;

public class Shared {
	@Resource
	private OkHttpClient okHttpClient;

	public final static int POS_2_ID = 2;

	/** 用来测试的数据库名称 */
	public final static String DBName_Test = "nbr";
	public final static String BXDBName_Test = "nbr_bx";
	public final static String StaticDBName_Test = "staticdb";
	/** 用来测试的公司编号 */
	public final static String DB_SN_Test = "668866"; // 对应nbr db的SN
	public final static String DB_BXSN_Test = "668867"; // 对应nbr_bx db的SN
	public final static String DB_SNNotExist_Test = "668800"; // 不存在的DB的SN
	public final static int PAGE_SIZE_MAX = 100000000;

	public static final String PhoneOfBoss = "15854320895";
	public static final int BossID = EnumTypeRole.ETR_Boss.getIndex();
	public static final String PhoneOfAssistManager = "13144496272"; // 店长
	public static final String PhoneOfCashier = "15016509167";
//	public static final String PhoneOfManager = "18915460954";
	public static final String PhoneOfResignedStaff = "13196721886"; // 离职员工
	public static final String PhoneOfPreSale = "13888888888";
	public static final int PreSaleID = 1;
	public static final String PhoneOfVipID1 = "13545678110";
	public static final int DEFAULT_Company_ID = 1;
	public static final int DEFAULT_Shop_ID = 2;
	/**
	 * 本OP帐号是专门为了测试登录爆破的，其它任何测试不能使用这个帐号登录
	 */
	public static final String BxStaff_Phone = "13163949281";
	/**
	 * 本OP帐号是专门为了测试登录爆破的，其它任何测试不能使用这个帐号登录
	 */
	public static final String PhoneOfAnotherCashier = "13166668888";

	public static final int DEFAULT_VIP_ID = 1;
	public static final String DEFAULT_VIP_Name = "giggs";
	public static final String DEFAULT_VIP_Mobile = "13545678110";
	public static final String DEFAULT_VIP_openID = "1586421134797"; // vipSource的ID3
	public static final String DEFAULT_VIP_unionID = "3151245621545"; // vipSource的ID3

	public static final int Dev_MerchantID = 446784110; // DEV公众号下审核通过的子商户ID(无公众号无认证)
	public static final int Dev_MerchantID_Delete = 446788349;// DEV公众号已被移除的子商户ID
	public static final int Dev_MerchantID_REJECTED = 446787292;// DEV公众号下审核未通过的子商户ID

	public final static int BigStaffID = 100000000;
	public final static int BIG_ID = 100000000;

	public static final String COOKIE = "cooke";
	public static String SessionID = null;

	public static final int SOURCE_ID_OnlyFoundInSQLite = 100000000; // SQLite中有但是mysql中不存在的sourceID

	/** 博昕运营的手机 */
	public static final String PhoneOfOP = "13185246281";

	/** TOMMy微信的openID，用于公众号测试 */
	public static String openid = "o1uoyw064VMYoLeapdAwA3AqCfY8";

	/** 用户创建、R1、RN商品后，没有对图片进行任何操作。这是默认值 */
	public static final int LAST_OPERATION_TO_PICTURE_None = 0;

	/** 用户最后一个操作是：上传图片 */
	public static final int LAST_OPERATION_TO_PICTURE_Upload = 1;

	/** 用户最后一个操作是：清空图片 */
	public static final int LAST_OPERATION_TO_PICTURE_Clear = 2;

	//
	public static String WrongFormatForInputFieldMsgForTest = "输入数据的格式不正确";

	public static String CompareToErrorMsg = "创建的对象的字段与DB读出的不相等";

	// WX测试特定的运行时间区间，其它时间不运行。START_TIME_WxRelatedTest=开始时间、END_TIME_WxRelatedTest结束时间
	public static String START_TIME_WxRelatedTest = "00:00:00";
	public static String END_TIME_WxRelatedTest = "23:59:59";
	// 在Wx3rdPartyCardActionTest.java里每个测试方法运行后，令线程sleep(5000);
	public static int SLEEP_5_SECOND = 5000;

	/** 重新加载POS到缓存中 */
	public static boolean refreshPosCache(ApplicationContext applicationContext) {
		CacheManager.getCache(DBName_Test, EnumCacheType.ECT_POS).deleteAll();
		PosMapper mapper = (PosMapper) applicationContext.getBean("posMapper");
		Pos pos = new Pos();
		pos.setStatus(0);
		Map<String, Object> params = pos.getRetrieveNParam(BaseBO.INVALID_CASE_ID, pos);

		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		List<BaseModel> retrieveNPos = mapper.retrieveN(params);
		if (retrieveNPos == null || EnumErrorCode.values()[Integer.parseInt(params.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())] != EnumErrorCode.EC_NoError) {
			return false;
		}
		CacheManager.getCache(DBName_Test, EnumCacheType.ECT_POS).writeN(retrieveNPos);

		return true;
	}

	public static BaseModel parse1ObjectByFastjson(MvcResult mr, BaseModel bm, String key) {
		BaseModel baseModel = null;
		try {
			String string = mr.getResponse().getContentAsString();
			com.alibaba.fastjson.JSONObject json = com.alibaba.fastjson.JSONObject.parseObject(string);
			baseModel = bm.parse1(com.alibaba.fastjson.JSONObject.parseObject(json.getString(key)));
		} catch (Exception e) {
			e.printStackTrace();
			Assert.assertTrue(false, "解析model失败！");
		}
		return baseModel;
	}

	// 若要使用fast json， call Shared.parse1ObjectByFastjson()
	public static BaseModel parse1Object(MvcResult mr, BaseModel bm, String key) {
		BaseModel baseModel = null;
		try {
			String json = mr.getResponse().getContentAsString();
			JSONObject o = JSONObject.fromObject(json);
			String object = o.getString(key);
			baseModel = bm.parse1(object);
			Assert.assertTrue(baseModel != null, "model解析为空");
		} catch (Exception e) {
			e.printStackTrace();
			Assert.assertTrue(false, "解析model失败！");
		}
		return baseModel;
	}

	public static List<?> parseNObject(MvcResult mr, BaseModel bm, String key) {
		List<?> baseModel = null;
		try {
			String json = mr.getResponse().getContentAsString();
			JSONObject o = JSONObject.fromObject(json);
			String object = o.getString(key);
			baseModel = bm.parseN(object);
			Assert.assertTrue(baseModel != null, "model解析为空");
		} catch (Exception e) {
			e.printStackTrace();
			Assert.assertTrue(false, "解析model失败！");
		}
		return baseModel;
	}

	/** 在POS机登录前，重置相应的POS机，以防POS被删除功能出现异常 同时，为了防止其它测试增加了没有密码的POS机，将此POS机密码重置为默认密码 */
	public static void resetPOS(MockMvc mvc, int iPosID) throws Exception {
		Pos pos = new Pos();
		pos.setID(iPosID);
		MvcResult mr = mvc.perform(//
				get("/posSync/resetEx.bx?" + Pos.field.getFIELD_NAME_ID() + "=" + pos.getID() + "&" + Pos.field.getFIELD_NAME_returnObject() + "=" + 1)//
						.session((MockHttpSession) Shared.getPosLoginSession(mvc, iPosID))//
						.contentType(MediaType.APPLICATION_JSON)//
		)//
				.andExpect(status().isOk()).andDo(print()).andReturn();

		Shared.checkJSONErrorCode(mr);
	}

	/** string1的值为公司编号 */
	// ...以后需要重构
	public static MockHttpSession getStaffLoginSession(MockMvc mvc, String iPhone) throws Exception {
		// Staff s = new Staff();
		MvcResult result = (MvcResult) mvc.perform(//
				post("/staff/getTokenEx.bx")//
						.param(Staff.field.getFIELD_NAME_phone(), iPhone)//
						.contentType(MediaType.APPLICATION_JSON)//
		)//
				.andExpect(status().isOk())//
				.andReturn();

		Shared.checkJSONErrorCode(result);
		String encrypt = Shared.encrypt(result, Shared.PASSWORD_DEFAULT);

		MvcResult result1 = (MvcResult) mvc.perform(//
				post("/staff/loginEx.bx")//
						.param(Staff.field.getFIELD_NAME_phone(), iPhone)//
						.param(Staff.field.getFIELD_NAME_salt(), "")//
						.param(Staff.field.getFIELD_NAME_pwdEncrypted(), encrypt)//
						.param(Staff.field.getFIELD_NAME_companySN(), DB_SN_Test)//
						.session((MockHttpSession) result.getRequest().getSession())//
						.contentType(MediaType.APPLICATION_JSON))//
				.andExpect(status().isOk())//
				.andReturn();//
		Shared.checkJSONErrorCode(result1);

		return (MockHttpSession) result1.getRequest().getSession();
	}

	// ...以后需要重构
	public static MockHttpSession getStaffLoginSession(MockMvc mvc, String iPhone, String password, String companySN) throws Exception {
		// Staff s = new Staff();
		MvcResult result = (MvcResult) mvc.perform(//
				post("/staff/getTokenEx.bx")//
						.param(Staff.field.getFIELD_NAME_phone(), iPhone)//
						.contentType(MediaType.APPLICATION_JSON)//
		)//
				.andExpect(status().isOk())//
				.andReturn();

		Shared.checkJSONErrorCode(result);
		String encrypt = Shared.encrypt(result, password);

		MvcResult result1 = (MvcResult) mvc.perform(//
				post("/staff/loginEx.bx")//
						.param(Staff.field.getFIELD_NAME_phone(), iPhone)//
						.param(Staff.field.getFIELD_NAME_salt(), "")//
						.param(Staff.field.getFIELD_NAME_pwdEncrypted(), encrypt)//
						.param(Staff.field.getFIELD_NAME_companySN(), companySN)//
						.session((MockHttpSession) result.getRequest().getSession())//
						.contentType(MediaType.APPLICATION_JSON))//
				.andExpect(status().isOk())//
				.andReturn();//
		Shared.checkJSONErrorCode(result1);

		return (MockHttpSession) result1.getRequest().getSession();
	}

	public static MockHttpSession getBXStaffLoginSession(MockMvc mvc, String iPhone) throws Exception {
		MvcResult result = (MvcResult) mvc.perform(post("/bxStaff/getTokenEx.bx") //
				.param(BxStaff.field.getFIELD_NAME_mobile(), iPhone) //
				.contentType(MediaType.APPLICATION_JSON)) //
				.andExpect(status().isOk()).andReturn();
		Shared.checkJSONErrorCode(result);
		String encrypt = Shared.encrypt(result, Shared.PASSWORD_DEFAULT);

		MvcResult result1 = (MvcResult) mvc.perform( //
				post("/bxStaff/loginEx.bx") //
						.param(BxStaff.field.getFIELD_NAME_mobile(), iPhone) //
						.param(BxStaff.field.getFIELD_NAME_salt(), "000000")//
						.param(BxStaff.field.getFIELD_NAME_pwdEncrypted(), encrypt) //
						.param(BxStaff.field.getFIELD_NAME_companySN(), DB_BXSN_Test)//
						.session((MockHttpSession) result.getRequest().getSession()) //
						.contentType(MediaType.APPLICATION_JSON) //
		)//
				.andExpect(status().isOk()) //
				.andReturn();//
		return (MockHttpSession) result1.getRequest().getSession();
	}

	public static MockHttpSession getVipLoginSession(MockMvc mvc, Vip vip, boolean firstTimeLogin) throws Exception {
		vip.setDecryptSessionCode(Shared.generateStringByTime(5));
		vip.setCompanySN(DB_SN_Test);
		//
		if (firstTimeLogin) {
			vip.setEncryptedPhone(Shared.generateStringByTime(5)); //
			vip.setEncryptedUnionID(Shared.generateStringByTime(5)); //
			vip.setIvPhone(Shared.generateStringByTime(5));
			vip.setIvUnionID(Shared.generateStringByTime(5));
			//
			// vip.setMobile(""); // 要根据手机号码查询所属公司，不能为""
		} else {
			vip.setEncryptedPhone(""); //
			vip.setEncryptedUnionID(""); //
			vip.setIvPhone("");
			vip.setIvUnionID("");
		}
		//
		String jsonString = com.alibaba.fastjson.JSONObject.toJSONString(vip);
		//
		MvcResult result = (MvcResult) mvc.perform( //
				post("/miniprogram/loginEx.bx") //
						.content(jsonString) //
						.contentType(MediaType.APPLICATION_JSON) //
		)//
				.andExpect(status().isOk()) //
				.andDo(print()).andReturn(); //
		Shared.checkJSONErrorCode(result, EnumErrorCode.EC_NoError);
		return (MockHttpSession) result.getRequest().getSession();
	}

	public static MockHttpSession getPosLoginSession(MockMvc mvc, int iPosID) throws Exception {
		Pos p = new Pos();
		p.setID(iPosID);
		// ..拿到公钥
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

		final String pwd = Shared.PASSWORD_DEFAULT;
		// ..加密密码
		String pwdEncrypted = RSAUtils.encryptByPublicKey(pwd, publicKey);
		p.setPwdEncrypted(pwdEncrypted);

		MvcResult mr = mvc.perform(post("/pos/loginEx.bx") //
				.param(Pos.field.getFIELD_NAME_ID(), String.valueOf(iPosID)) //
				.param(Pos.field.getFIELD_NAME_pwdEncrypted(), pwdEncrypted) //
				.param(Pos.field.getFIELD_NAME_companySN(), DB_SN_Test)//
				.session((MockHttpSession) ret.getRequest().getSession()) //
				.contentType(MediaType.APPLICATION_JSON)) //
				.andExpect(status().isOk()).andDo(print()).andReturn();

		Shared.checkJSONErrorCode(mr);
		System.out.println("pos登录时sessionId:" + mr.getRequest().getSession().getId());

		System.out.println("POS" + iPosID + "登录成功");

		final String Staff_Phone = Shared.PhoneOfBoss; // 店员3 店长
		System.out.println("staff正在登录...");
		Staff staff = new Staff();
		// ..拿到公钥
		MvcResult ret2 = mvc.perform(post("/staff/getTokenEx.bx").contentType(MediaType.APPLICATION_JSON).session((MockHttpSession) mr.getRequest().getSession()).param(Staff.field.getFIELD_NAME_phone(), Staff_Phone))
				.andExpect(status().isOk()).andDo(print()).andReturn();

		String json2 = ret2.getResponse().getContentAsString();
		JSONObject o2 = JSONObject.fromObject(json2);
		String modulus2 = JsonPath.read(o2, "$.rsa.modulus");
		String exponent2 = JsonPath.read(o2, "$.rsa.exponent");
		modulus2 = new BigInteger(modulus2, 16).toString();
		exponent2 = new BigInteger(exponent2, 16).toString();

		System.out.println("staff获取token时sessionId:" + ret2.getRequest().getSession().getId());
		RSAPublicKey publicKey2 = RSAUtils.getPublicKey(modulus2, exponent2);

		final String pwd2 = Shared.PASSWORD_DEFAULT;
		// ..加密密码
		String pwdEncrypted2 = RSAUtils.encryptByPublicKey(pwd2, publicKey2);
		staff.setPwdEncrypted(pwdEncrypted2);

		MvcResult mr2 = mvc.perform(post("/staff/loginEx.bx")//
				.param(Staff.field.getFIELD_NAME_phone(), Staff_Phone)//
				.param(Staff.field.getFIELD_NAME_companySN(), Shared.DB_SN_Test)//
				.param(Staff.field.getFIELD_NAME_pwdEncrypted(), pwdEncrypted2)//
				.session((MockHttpSession) ret2.getRequest().getSession())//
				.contentType(MediaType.APPLICATION_JSON)//
		)//
				.andExpect(status().isOk())//
				.andDo(print())//
				.andReturn();

		Shared.checkJSONErrorCode(mr2, EnumErrorCode.EC_NoError);
		System.out.println("staff登录成功。sessionId=" + mr2.getRequest().getSession().getId());

		return (MockHttpSession) mr2.getRequest().getSession();
	}

	public static HttpSession getPosLoginSession(MockMvc mvc, int iPosID, String staff_Phone, String staff_Password, String companySN, String passwordInPOS) throws Exception {
		Pos p = new Pos();
		p.setID(iPosID);
		// ..拿到公钥
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
		// ..加密密码
		String pwdEncrypted = RSAUtils.encryptByPublicKey(pwd, publicKey);
		p.setPwdEncrypted(pwdEncrypted);

		MvcResult mr = mvc.perform(post("/pos/loginEx.bx") //
				.param(Pos.field.getFIELD_NAME_ID(), String.valueOf(iPosID)) //
				.param(Pos.field.getFIELD_NAME_pwdEncrypted(), pwdEncrypted) //
				.param(Pos.field.getFIELD_NAME_companySN(), companySN)//
				.session((MockHttpSession) ret.getRequest().getSession()) //
				.contentType(MediaType.APPLICATION_JSON)) //
				.andExpect(status().isOk()).andDo(print()).andReturn();

		Shared.checkJSONErrorCode(mr);
		System.out.println("pos登录时sessionId:" + mr.getRequest().getSession().getId());

		System.out.println("POS" + iPosID + "登录成功");

		System.out.println("staff正在登录...");
		Staff staff = new Staff();
		// ..拿到公钥
		MvcResult ret2 = mvc.perform(post("/staff/getTokenEx.bx").contentType(MediaType.APPLICATION_JSON).session((MockHttpSession) mr.getRequest().getSession()).param(Staff.field.getFIELD_NAME_phone(), staff_Phone))
				.andExpect(status().isOk()).andDo(print()).andReturn();

		String json2 = ret2.getResponse().getContentAsString();
		JSONObject o2 = JSONObject.fromObject(json2);
		String modulus2 = JsonPath.read(o2, "$.rsa.modulus");
		String exponent2 = JsonPath.read(o2, "$.rsa.exponent");
		modulus2 = new BigInteger(modulus2, 16).toString();
		exponent2 = new BigInteger(exponent2, 16).toString();

		System.out.println("staff获取token时sessionId:" + ret2.getRequest().getSession().getId());
		RSAPublicKey publicKey2 = RSAUtils.getPublicKey(modulus2, exponent2);

		final String pwd2 = staff_Password;
		// ..加密密码
		String pwdEncrypted2 = RSAUtils.encryptByPublicKey(pwd2, publicKey2);
		staff.setPwdEncrypted(pwdEncrypted2);

		MvcResult mr2 = mvc.perform(post("/staff/loginEx.bx")//
				.param(Staff.field.getFIELD_NAME_phone(), staff_Phone)//
				.param(Staff.field.getFIELD_NAME_companySN(), companySN)//
				.param(Staff.field.getFIELD_NAME_pwdEncrypted(), pwdEncrypted2)//
				.session((MockHttpSession) ret2.getRequest().getSession())//
				.contentType(MediaType.APPLICATION_JSON)//
		)//
				.andExpect(status().isOk())//
				.andDo(print())//
				.andReturn();

		Shared.checkJSONErrorCode(mr2, EnumErrorCode.EC_NoError);
		System.out.println("staff登录成功。sessionId=" + mr2.getRequest().getSession().getId());

		return mr2.getRequest().getSession();
	}

	public static JSONObject checkJSONErrorCode(MvcResult mr) throws UnsupportedEncodingException {
		String json = mr.getResponse().getContentAsString();
		JSONObject o = JSONObject.fromObject(json);
		String err = JsonPath.read(o, "$." + BaseAction.JSON_ERROR_KEY);
		String msg = "没有错误信息";
		try {
			msg = JsonPath.read(o, "$." + BaseAction.KEY_HTMLTable_Parameter_msg);
		} catch (Exception e) {
		}

		if (err.compareTo(EnumErrorCode.EC_NoError.toString()) != 0) {
			Assert.fail("错误码不正确！" + err + "(" + msg + ")");
		}

		return o;
	}

	public static void checkJSONErrorCode(JSONObject json) {
		System.out.println("json：" + json);

		String err = JsonPath.read(json, "$." + BaseAction.JSON_ERROR_KEY);
		String msg = "没有错误信息";

		try {
			msg = JsonPath.read(json, "$." + BaseAction.KEY_HTMLTable_Parameter_msg);
		} catch (Exception e) {
		}

		if (err.compareTo(EnumErrorCode.EC_NoError.toString()) != 0) {
			Assert.fail("错误码不正确！" + err + "(" + msg + ")");
		}
	}

	public static void checkWxJSONErrorMsg(JSONObject json, int errcode) {
		System.out.println("json：" + json);

		int errorcode = json.getJSONObject(BaseAction.KEY_HTMLTable_Parameter_msg).getInt(BaseWxModel.WX_ERRCODE);

		if (errorcode != errcode) {
			Assert.assertTrue(false, "微信返回错误码(" + errorcode + ")与期望错误码(" + errcode + ")不一致！");
		}
	}

	public static void checkJSONErrorCode(JSONObject json, EnumErrorCode eecExpected) {
		System.out.println("json：" + json);

		String err = JsonPath.read(json, "$." + BaseAction.JSON_ERROR_KEY);
		String msg = "没有错误信息";

		try {
			msg = JsonPath.read(json, "$." + BaseAction.KEY_HTMLTable_Parameter_msg);
		} catch (Exception e) {
		}

		if (err.compareTo(eecExpected.toString()) != 0) {
			Assert.fail("错误码和期望的错误码不一致！" + err + "(" + msg + ")");
		}
	}

	public static JSONObject checkJSONErrorCode(MvcResult mr, EnumErrorCode eecExpected) throws UnsupportedEncodingException {
		String json = mr.getResponse().getContentAsString();
		JSONObject o = JSONObject.fromObject(json);
		String err = JsonPath.read(o, "$." + BaseAction.JSON_ERROR_KEY);
		String msg = "没有错误信息";
		try {
			msg = JsonPath.read(o, "$." + BaseAction.KEY_HTMLTable_Parameter_msg);
		} catch (Exception e) {
		}
		if (err.compareTo(eecExpected.toString()) != 0) {
			Assert.fail("错误码和期望的错误码不一致！" + err + "(" + msg + ")");
		}

		return o;
	}

	public static JSONObject checkJSONMsg(MvcResult mr, final String msgIn, final String err) throws UnsupportedEncodingException {
		String json = mr.getResponse().getContentAsString();
		JSONObject o = JSONObject.fromObject(json);
		String msg = JsonPath.read(o, "$." + BaseAction.KEY_HTMLTable_Parameter_msg);
		if (msg == null || !msg.equals(msgIn)) {
			Assert.fail(err);
		}

		return o;
	}

	public static String encrypt(MvcResult mr, String pwd) throws Exception {
		String json = mr.getResponse().getContentAsString();
		JSONObject o = JSONObject.fromObject(json);
		String modulus = JsonPath.read(o, "$.rsa.modulus");
		String exponent = JsonPath.read(o, "$.rsa.exponent");
		modulus = new BigInteger(modulus, 16).toString();
		exponent = new BigInteger(exponent, 16).toString();

		RSAPublicKey publicKey = RSAUtils.getPublicKey(modulus, exponent);

		String sPasswordEncryptedNew = RSAUtils.encryptByPublicKey(pwd, publicKey);

		return sPasswordEncryptedNew;
	}

	public static final String PASSWORD_DEFAULT = "000000";

	public static void printTestClassStartInfo() {
		StackTraceElement ste = new Exception().getStackTrace()[1];
		System.out.println(new SimpleDateFormat(BaseAction.DATETIME_FORMAT_Default2).format(new Date()) + "\t\t开始运行测试类：" + ste.getClassName() + "...");
	}

	public static void printTestClassEndInfo() {
		StackTraceElement ste = new Exception().getStackTrace()[1];
		System.out.println(new SimpleDateFormat(BaseAction.DATETIME_FORMAT_Default2).format(new Date()) + "\t\t结束运行测试类：" + ste.getClassName() + "...");
	}

	public static void printTestMethodStartInfo() {
		StackTraceElement ste = new Exception().getStackTrace()[1];
		System.out.println(new SimpleDateFormat(BaseAction.DATETIME_FORMAT_Default2).format(new Date()) + "\t\t开始运行测试：" + ste.getClassName() + "." + ste.getMethodName() + "()...");
	}

	public static void printTestMethodStartInfo(String testCaseID, AtomicInteger order, String testCaseName) {
		StackTraceElement ste = new Exception().getStackTrace()[1];
		System.out.println(new SimpleDateFormat(BaseAction.DATETIME_FORMAT_Default2).format(new Date()) + "\t\t开始运行测试：" + ste.getClassName() + "." + ste.getMethodName() + "()..." + "\r\n用例ID：" + testCaseID + order.incrementAndGet()
				+ "\r\n用例名称：" + testCaseName + "\r\n");
	}

	/** @param posBO
	 * @return 列表中的Pos按ID升序排列，但可能不是连续的，因为其它测试可能对DB中的POS有过CRUD操作。返回值也不会是null，因为Mybatis不会返回null的list */
	@SuppressWarnings("unchecked")
	public static List<Pos> getPosesFromDB(PosBO posBO, String dbName) {
		// 由于测试需要，将PAGE_SIZE_MAX设置成很大的数
		BaseAction.PAGE_SIZE_MAX = BaseAction.PAGE_SIZE_Infinite;
		Pos p = new Pos();
		p.setPageIndex(1);
		p.setPageSize(BaseAction.PAGE_SIZE_Infinite);
		p.setStatus(EnumStatusPos.ESP_Active.getIndex());
		p.setShopID(BaseAction.INVALID_ID);
		DataSourceContextHolder.setDbName(dbName);
		List<Pos> listPos = (List<Pos>) posBO.retrieveNObject(BaseBO.SYSTEM, BaseBO.INVALID_CASE_ID, p);
		// 还原PAGE_SIZE_MAX
		BaseAction.PAGE_SIZE_MAX = 50;
		//
		if (posBO.getLastErrorCode() != EnumErrorCode.EC_NoError) {
			assertTrue(false, "获取POS DB数据错误！");
		}
		Pos tmp = new Pos();
		tmp.setIsASC(EnumBoolean.EB_Yes.getIndex());
		Collections.sort(listPos, tmp); // 确保升序排列，才能符合测试需要：前5台POS的ID分别是1到5

		return listPos;
	}

	/** 生成一个由字母组成的字符串，长度为length */
	public static String generateCompanyName(int length) {
		String str = "";
		for (int i = 0; i < length; i++) {
			str = str + (char) (Math.abs(new Random().nextInt()) % 26 + 'a');
		}
		return str;
	}

	public static String generateVipName(int length) {
		String str = "";
		for (int i = 0; i < length; i++) {
			str = str + (char) (Math.abs(new Random().nextInt()) % 26 + 'a');
		}
		return str;

	}

	/** 用时间戳生成一个随机数，长度为length length大于0小于10 */
	public static String generateStringByTime(int length) throws InterruptedException {
		assert length > 0 && length < 10;
		String value = String.valueOf(System.currentTimeMillis());
		String num = value.substring(value.length() - length, value.length());
		Thread.sleep(1);
		return num;
	}

	/** 根据当前时间、pos_id和随机数生成SN号 */
	public static String generateRetailTradeSN(int pos_id) {
		SimpleDateFormat sdf = new SimpleDateFormat(BaseAction.DATE_FORMAT_RetailTradeSN);
		String dayStr = sdf.format(new Date());
		StringBuffer sb = null;
		try {
			Thread.sleep(1000);
			sb = new StringBuffer();
			sb.append("LS").append(dayStr);
			for (int i = 1; i <= 4 - String.valueOf(pos_id).length(); i++) {
				sb.append("0");
			}
			sb.append(String.valueOf(pos_id)).append(String.valueOf((int) (Math.floor((Math.random() * 9 + 1) * 1000))));
			return sb.toString();
		} catch (Exception e) {
			System.out.println("生成零售单SN失败：" + e.getMessage());
			return null;
		}
	}

	/** 生成随机DB名称。它必须是以字母或下划线开头，和JAVA的变量命名一样 */
	public static String GenerateDBName() {
		String dbName = "nbr_test_" + UUID.randomUUID().toString().substring(1, 7);
		System.out.println("生成的DB name为：" + dbName);
		return dbName;
	}

	/** 生成一个合法的手机号码，以13开头，共11位 */
	public static String getValidStaffPhone() {
		String str = "%09d";
		String phone = String.format(str, System.currentTimeMillis() % 1000000000);
		return "13" + phone;
	}

	/** 生成一个合法的身份证，以320开头，共11位 */
	public static String getValidICID() throws InterruptedException {
		String str = "%04d";
		String ICID = String.format(str, System.currentTimeMillis() % 10000);
		Thread.sleep(1);
		String headstr = "%06d";
		String head = String.format(headstr, System.currentTimeMillis() % 1000000);
		return head + "19970701" + ICID;
	}

	/** 生成一个近似随机的供应商名字，共32位。格式：prefix+当前时间（前面补0）。
	 * Provider.Max_LengthName要足够大，否则会出错 */
	public static String getLongestProviderName(String prefix) {
		String str = "%0" + (Provider.Max_LengthName - prefix.length()) + "d";
		String providerName = String.format(str, System.currentTimeMillis());
		return prefix + providerName;
	}

	public static String getFakedSalt() {
		UUID uuid = UUID.randomUUID();
		String key = uuid.toString().replace("-", "");
		return key.toUpperCase();
	}

	/** 随机选取几个可能收银的商品，包括组合商品、单品、多包装商品，不包括已经删除的商品
	 *
	 * @param maxNO
	 *            最大的商品数目
	 * @return
	 * @throws Exception
	 */
	public static List<Commodity> getCommodityList(int maxNO, StringBuilder sbError) throws Exception {
		assert maxNO > 0;
		Random r = new Random();
		int commodityNO = r.nextInt(maxNO + 1);

		Map<String, Commodity> map = new HashMap<>(); // key=F_ID, value=Commodity
		Commodity commodity = new Commodity();
		for (int i = 0; i < commodityNO; i++) {
			// 读取商品。R1，传递商品ID。
			commodity.setID(r.nextInt(100) + 1);
			//
			Map<String, String> params = new HashMap<>();
			params.put(Commodity.field.getFIELD_NAME_ID(), String.valueOf(commodity.getID()));
			String response = OkHttpUtil.get("/commodity/retrieve1Ex.bx", params);
			//
			JSONObject object = JSONObject.fromObject(response);
			String err = JsonPath.read(object, "$." + BaseAction.JSON_ERROR_KEY);
			if (err.compareTo(EnumErrorCode.EC_NoError.toString()) != 0) {
				i -= 1;
				continue;
			}
			//
			Commodity commodity1 = new Commodity();
			commodity1 = (Commodity) commodity1.parse1(object.getString(BaseAction.KEY_Object));
			if (commodity1 != null && (commodity1.getType() == CommodityType.EnumCommodityType.ECT_Normal.getIndex())) {
				Map<String, String> params1 = new HashMap<>();
				params1.put(Barcodes.field.getFIELD_NAME_commodityID(), String.valueOf(commodity1.getID()));
				String responseBarcodes = OkHttpUtil.get("/barcodes/retrieveNEx.bx", params1);
				//
				JSONObject objectBarcodes = JSONObject.fromObject(responseBarcodes);
				Barcodes barcodes1 = new Barcodes();
				List<BaseModel> listBarcodes = barcodes1.parseN(objectBarcodes.getString("barcodesList"));
				if (listBarcodes == null || listBarcodes.size() <= 0) {
					sbError.append("未能查找到barcodeID");
					return null;
				}
				commodity1.setBarcodeID(listBarcodes.get(0).getID());
				//
				map.put(String.valueOf(commodity1.getID()), commodity1);
			} else {
				i -= 1;
				continue;
			}
		}
		// ... 如果只获取一个商品，然后商品ID不存在的情况下该如何处理

		return new ArrayList<>(map.values());
	}

	public static String httpEncrypt(String responseToken, String pwd) throws Exception {
		JSONObject o = JSONObject.fromObject(responseToken);
		String modulus = JsonPath.read(o, "$.rsa.modulus");
		String exponent = JsonPath.read(o, "$.rsa.exponent");
		modulus = new BigInteger(modulus, 16).toString();
		exponent = new BigInteger(exponent, 16).toString();

		RSAPublicKey publicKey = RSAUtils.getPublicKey(modulus, exponent);

		String sPasswordEncryptedNew = RSAUtils.encryptByPublicKey(pwd, publicKey);

		return sPasswordEncryptedNew;
	}

	public static void getHttpStaffLoginSession() throws Exception {
		String urlGetTokenEx = "/staff/getTokenEx.bx";
		Map<String, String> paramsToken = new HashMap<>();
		paramsToken.put(Staff.field.getFIELD_NAME_phone(), PhoneOfBoss);
		String responseToken = OkHttpUtil.post(urlGetTokenEx, paramsToken);
		//
		String encrypt = httpEncrypt(responseToken, PASSWORD_DEFAULT);
		//
		Map<String, String> paramsLogin = new HashMap<>();
		paramsLogin.put(Staff.field.getFIELD_NAME_phone(), PhoneOfBoss);
		paramsLogin.put(Staff.field.getFIELD_NAME_salt(), "");
		paramsLogin.put(Staff.field.getFIELD_NAME_pwdEncrypted(), encrypt);
		paramsLogin.put(Staff.field.getFIELD_NAME_companySN(), DB_SN_Test);
		String urlLoginEx = "/staff/loginEx.bx";
		OkHttpUtil.post(urlLoginEx, paramsLogin);
	}

	public static void caseLog(String s) {
		System.out.println("\n---------------------------------------" + s + "-----------------------------------------------");
	}

	public static String toJSONString(Object object) {
		com.alibaba.fastjson.JSONObject.DEFFAULT_DATE_FORMAT = BaseAction.DATETIME_FORMAT_Default1;
		return JSON.toJSONString(object, SerializerFeature.WriteDateUseDateFormat).toString();
	}
}
