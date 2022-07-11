package com.bx.erp.test.staff;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.math.BigInteger;
import java.security.interfaces.RSAPublicKey;

import javax.servlet.http.HttpSession;

import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.util.StringUtils;
import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import com.bx.erp.action.BaseAction;
import com.bx.erp.action.BaseAction.EnumSession;
import com.bx.erp.cache.BaseCache;
import com.bx.erp.cache.CacheManager;
import com.bx.erp.model.BxStaff;
import com.bx.erp.model.Company;
import com.bx.erp.model.ErrorInfo;
import com.bx.erp.model.CacheType.EnumCacheType;
import com.bx.erp.model.ErrorInfo.EnumErrorCode;
import com.bx.erp.model.config.BxConfigGeneral;
import com.bx.erp.test.BaseActionTest;
import com.bx.erp.test.Shared;
import com.bx.erp.util.FieldFormat;
import com.bx.erp.util.RSAUtils;
import com.jayway.jsonpath.JsonPath;

import net.sf.json.JSONObject;

@WebAppConfiguration
public class BxStaffActionTest extends BaseActionTest {
	private final String PHONE = "13185246281";

	@BeforeClass
	public void setup() {
		super.setUp();
	}

	@AfterClass
	public void tearDown() {
		super.tearDown();
	}

	@Test
	public void testRetrieve1Ex() throws Exception {
		Shared.printTestMethodStartInfo();

		System.out.println("-------------------case1: 根据ID查询-------------------");
		MvcResult mr = mvc.perform(//
				get("/bxStaff/retrieve1Ex.bx?" + BxStaff.field.getFIELD_NAME_ID() + "=1&" + BxStaff.field.getFIELD_NAME_mobile() + "=")//
						.contentType(MediaType.APPLICATION_JSON)//
						.session((MockHttpSession) Shared.getBXStaffLoginSession(mvc, PHONE))//
		)//
				.andExpect(status().isOk())//
				.andDo(print())//
				.andReturn();

		Shared.checkJSONErrorCode(mr);

		System.out.println("-------------------case2: 根据不存在ID查询-------------------");
		MvcResult mr2 = mvc.perform(//
				get("/bxStaff/retrieve1Ex.bx?" + BxStaff.field.getFIELD_NAME_ID() + "=-1&" + BxStaff.field.getFIELD_NAME_mobile() + "=")//
						.contentType(MediaType.APPLICATION_JSON)//
						.session((MockHttpSession) Shared.getBXStaffLoginSession(mvc, PHONE))//
		)//
				.andExpect(status().isOk())//
				.andDo(print())//
				.andReturn();

		Shared.checkJSONErrorCode(mr2, EnumErrorCode.EC_WrongFormatForInputField);
		Shared.checkJSONMsg(mr2, FieldFormat.FIELD_ERROR_ID, "错误信息与预期不相符");

		// bxStaff暂时没有检查权限
		// System.out.println("-------------------case3: 没有权限进行查询-------------------");
		// MvcResult mr3 = mvc.perform(//
		// get("/bxStaff/retrieve1Ex.bx?" + BxStaff.field.getFIELD_NAME_ID() + "=1&" +
		// BxStaff.field.getFIELD_NAME_mobile() + "=")//
		// .contentType(MediaType.APPLICATION_JSON)//
		// .session((MockHttpSession) Shared.getStaffLoginSession(mvc,
		// NoPermission_PHONE))//
		// )//
		// .andExpect(status().isOk())//
		// .andDo(print())//
		// .andReturn();
		//
		// Shared.checkJSONErrorCode(mr3, EnumErrorCode.EC_NoPermission);

		System.out.println("-------------------case4: 用手机号码进行查询-------------------");
		MvcResult mr4 = mvc.perform(//
				get("/bxStaff/retrieve1Ex.bx?" + BxStaff.field.getFIELD_NAME_ID() + "=0&" + BxStaff.field.getFIELD_NAME_mobile() + "=13462346281")//
						.contentType(MediaType.APPLICATION_JSON)//
						.session((MockHttpSession) Shared.getBXStaffLoginSession(mvc, PHONE))//
		)//
				.andExpect(status().isOk())//
				.andDo(print())//
				.andReturn();

		Shared.checkJSONErrorCode(mr4);

		System.out.println("-------------------case5: 用不存在的手机号码进行查询-------------------");
		MvcResult mr5 = mvc.perform(//
				get("/bxStaff/retrieve1Ex.bx?" + BxStaff.field.getFIELD_NAME_ID() + "=0&" + BxStaff.field.getFIELD_NAME_mobile() + "=-99999")//
						.contentType(MediaType.APPLICATION_JSON)//
						.session((MockHttpSession) Shared.getBXStaffLoginSession(mvc, PHONE))//
		)//
				.andExpect(status().isOk())//
				.andDo(print())//
				.andReturn();

		Shared.checkJSONErrorCode(mr5, EnumErrorCode.EC_WrongFormatForInputField);
		Shared.checkJSONMsg(mr5, BxStaff.FIELD_ERROR_Mobile, "错误信息与预期不相符");
	}

	@Test
	public void testLogout() throws Exception {
		final String BxStaff_Phone = "13462346281";

		BxStaff bxStaff = new BxStaff();
		// ..拿到公钥
		MvcResult ret = mvc.perform(post("/bxStaff/getTokenEx.bx").contentType(MediaType.APPLICATION_JSON).param(BxStaff.field.getFIELD_NAME_mobile(), BxStaff_Phone)).andExpect(status().isOk()).andDo(print()).andReturn();

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
		bxStaff.setPwdEncrypted(pwdEncrypted);

		MvcResult mr = mvc
				.perform(post("/bxStaff/loginEx.bx").param(BxStaff.field.getFIELD_NAME_mobile(), BxStaff_Phone).param(BxStaff.field.getFIELD_NAME_pwdEncrypted(), pwdEncrypted)
						.param(BxStaff.field.getFIELD_NAME_companySN(), Shared.DB_BXSN_Test).session((MockHttpSession) ret.getRequest().getSession()).contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk()).andDo(print()).andReturn();

		Shared.checkJSONErrorCode(mr, EnumErrorCode.EC_NoError);

		MvcResult mr2 = mvc.perform(get("/bxStaff/logoutEx.bx").session((MockHttpSession) mr.getRequest().getSession()).contentType(MediaType.APPLICATION_JSON)).andExpect(status().isOk()).andDo(print()).andReturn();
		Shared.checkJSONErrorCode(mr2);
	}

	@Test
	public void testLogin() throws Exception {
		Shared.printTestMethodStartInfo();

		// ------------------- 第一次测试：密码正确且密码格式正确 -------------------
		// System.out.println("------------------- 第一次测试：密码正确且密码格式正确
		// -------------------");
		final String BxStaff_Phone = "13462346281";

		BxStaff bs = new BxStaff();
		// ..拿到公钥
		MvcResult ret = mvc.perform(post("/bxStaff/getTokenEx.bx").contentType(MediaType.APPLICATION_JSON).param(BxStaff.field.getFIELD_NAME_mobile(), BxStaff_Phone)).andExpect(status().isOk()).andDo(print()).andReturn();

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
		bs.setPwdEncrypted(pwdEncrypted);

		MvcResult mr = mvc.perform(post//
		("/bxStaff/loginEx.bx")//
				.param(BxStaff.field.getFIELD_NAME_mobile(), BxStaff_Phone)//
				.param(BxStaff.field.getFIELD_NAME_pwdEncrypted(), pwdEncrypted)//
				.session((MockHttpSession) ret.getRequest().getSession())//
				.contentType(MediaType.APPLICATION_JSON)//
		)//
				.andExpect(status().isOk()).andDo(print()).andReturn();

		Shared.checkJSONErrorCode(mr, EnumErrorCode.EC_NoError);
	}

	@Test
	public void testLogin2() throws Exception {
		Shared.printTestMethodStartInfo();

		Shared.caseLog("case2：用户登录超过当日限制次数");
		//
		ErrorInfo ec = new ErrorInfo();
		BxConfigGeneral bxConfigGeneral = (BxConfigGeneral) CacheManager.getCache(BaseAction.DBName_Public, EnumCacheType.ECT_BXConfigGeneral).read1(BaseCache.MaxLoginCountIn1Day, STAFF_ID4, ec, BaseAction.DBName_Public);
		String oldMaxLoginCountIn1Day = bxConfigGeneral.getValue();
		//
		int newMaxLoginCountIn1Day = 5;
		BxConfigGeneral newBxConfigGeneral = (BxConfigGeneral) bxConfigGeneral.clone();
		newBxConfigGeneral.setValue(String.valueOf(newMaxLoginCountIn1Day));
		CacheManager.getCache(BaseAction.DBName_Public, EnumCacheType.ECT_BXConfigGeneral).write1(newBxConfigGeneral, BaseAction.DBName_Public, STAFF_ID4);

		for (int i = 0; i < newMaxLoginCountIn1Day - 1; i++) {
			login(Shared.BxStaff_Phone);
		}

		BxStaff bs = new BxStaff();
		// ..拿到公钥
		MvcResult ret = mvc.perform(post("/bxStaff/getTokenEx.bx").contentType(MediaType.APPLICATION_JSON).param(BxStaff.field.getFIELD_NAME_mobile(), Shared.BxStaff_Phone)).andExpect(status().isOk()).andDo(print()).andReturn();

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
		bs.setPwdEncrypted(pwdEncrypted);

		MvcResult mr = mvc.perform(post//
		("/bxStaff/loginEx.bx")//
				.param(BxStaff.field.getFIELD_NAME_mobile(), Shared.BxStaff_Phone)//
				.param(BxStaff.field.getFIELD_NAME_pwdEncrypted(), pwdEncrypted)//
				.session((MockHttpSession) ret.getRequest().getSession())//
				.contentType(MediaType.APPLICATION_JSON)//
		)//
				.andExpect(status().isOk()).andDo(print()).andReturn();

		newBxConfigGeneral.setValue(oldMaxLoginCountIn1Day);
		CacheManager.getCache(BaseAction.DBName_Public, EnumCacheType.ECT_BXConfigGeneral).write1(newBxConfigGeneral, BaseAction.DBName_Public, STAFF_ID4);

		Assert.assertTrue(StringUtils.isEmpty(mr.getResponse().getContentAsString()), "没有正常限制用户登录次数");
	}
	
	@Test
	public void testLogin3() throws Exception {
		Shared.printTestMethodStartInfo();
		Shared.caseLog("case3：OP登录后，需要返回公司Logo的URL");

		final String BxStaff_Phone = "13462346281";

		BxStaff bs = new BxStaff();
		// ..拿到公钥
		MvcResult ret = mvc.perform(post("/bxStaff/getTokenEx.bx").contentType(MediaType.APPLICATION_JSON).param(BxStaff.field.getFIELD_NAME_mobile(), BxStaff_Phone)).andExpect(status().isOk()).andDo(print()).andReturn();

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
		bs.setPwdEncrypted(pwdEncrypted);

		MvcResult mr = mvc.perform(post//
		("/bxStaff/loginEx.bx")//
				.param(BxStaff.field.getFIELD_NAME_mobile(), BxStaff_Phone)//
				.param(BxStaff.field.getFIELD_NAME_pwdEncrypted(), pwdEncrypted)//
				.session((MockHttpSession) ret.getRequest().getSession())//
				.contentType(MediaType.APPLICATION_JSON)//
		)//
				.andExpect(status().isOk()).andDo(print()).andReturn();

		Shared.checkJSONErrorCode(mr, EnumErrorCode.EC_NoError);
		
		HttpSession session = ret.getRequest().getSession();
		Company company = (Company) session.getAttribute(EnumSession.SESSION_Company.getName());
		Assert.assertTrue(company.getLogo() != null, "商家登录后，需要返回公司Logo的URL");
	}

	private void login(String phone) throws Exception {
		BxStaff bs = new BxStaff();
		// ..拿到公钥
		MvcResult ret = mvc.perform(post("/bxStaff/getTokenEx.bx").contentType(MediaType.APPLICATION_JSON).param(BxStaff.field.getFIELD_NAME_mobile(), phone)).andExpect(status().isOk()).andDo(print()).andReturn();

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
		bs.setPwdEncrypted(pwdEncrypted);

		MvcResult mr = mvc.perform(post//
		("/bxStaff/loginEx.bx")//
				.param(BxStaff.field.getFIELD_NAME_mobile(), phone)//
				.param(BxStaff.field.getFIELD_NAME_pwdEncrypted(), pwdEncrypted)//
				.session((MockHttpSession) ret.getRequest().getSession())//
				.contentType(MediaType.APPLICATION_JSON)//
		)//
				.andExpect(status().isOk()).andDo(print()).andReturn();

		Shared.checkJSONErrorCode(mr, EnumErrorCode.EC_NoError);
	}

	@Test
	public void testRetrieveNEx() throws Exception {
		Shared.printTestMethodStartInfo();

		System.out.println("-------------------case1:正常查询-------------------");
		MvcResult mr = mvc.perform(//
				get("/bxStaff/retrieveNEx.bx")//
						.contentType(MediaType.APPLICATION_JSON)//
						.session((MockHttpSession) Shared.getBXStaffLoginSession(mvc, PHONE))//
		)//
				.andExpect(status().isOk())//
				.andDo(print())//
				.andReturn();

		Shared.checkJSONErrorCode(mr);

		// bxStaff暂时没有检查权限
		// System.out.println("-------------------case2: 没有权限进行查询-------------------");
		// MvcResult mr2 = mvc.perform(//
		// get("/bxStaff/retrieveNEx.bx")//
		// .contentType(MediaType.APPLICATION_JSON)//
		// .session((MockHttpSession) Shared.getStaffLoginSession(mvc,
		// NoPermission_PHONE))//
		// )//
		// .andExpect(status().isOk())//
		// .andDo(print())//
		// .andReturn();
		//
		// Shared.checkJSONErrorCode(mr2, EnumErrorCode.EC_NoPermission);

	}
}