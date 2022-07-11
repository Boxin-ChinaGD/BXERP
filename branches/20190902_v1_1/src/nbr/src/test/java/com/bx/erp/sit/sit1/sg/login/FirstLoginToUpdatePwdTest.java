package com.bx.erp.sit.sit1.sg.login;

import org.testng.annotations.Test;

import com.bx.erp.action.BaseAction;
import com.bx.erp.model.Company;
import com.bx.erp.model.Role;
import com.bx.erp.model.Staff;
import com.bx.erp.model.BaseModel.EnumBoolean;
import com.bx.erp.model.ErrorInfo.EnumErrorCode;
import com.bx.erp.model.Role.EnumTypeRole;
import com.bx.erp.model.Staff.EnumStatusStaff;
import com.bx.erp.test.BaseActionTest;
import com.bx.erp.test.BaseCompanyTest;
import com.bx.erp.test.Shared;
import com.bx.erp.test.checkPoint.CompanyCP;
import com.bx.erp.test.checkPoint.StaffCP;
import com.bx.erp.util.RSAUtils;
import com.jayway.jsonpath.JsonPath;

import net.sf.json.JSONObject;

import org.testng.annotations.BeforeClass;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.security.interfaces.RSAPublicKey;
import java.text.SimpleDateFormat;
import java.util.concurrent.atomic.AtomicInteger;

import javax.servlet.http.HttpSession;

import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.testng.annotations.AfterClass;

@WebAppConfiguration
public class FirstLoginToUpdatePwdTest extends BaseActionTest {

	protected AtomicInteger order;
	protected final String bossPhone = "13914126924";
	protected static String bossPassword = "123456";
	protected String dbName;
	protected static String companySN;

	@BeforeClass
	public void beforeClass() throws Exception {
		super.setUp();
		order = new AtomicInteger();

	}

	public static class DataInput {
		private static Staff staffInput;

		protected static final MockHttpServletRequestBuilder getBuilder(String url, MediaType contentType, Staff s) {
			MockHttpServletRequestBuilder builder = post(url).contentType(contentType)//
					.param(Staff.field.getFIELD_NAME_ID(), "" + s.getID()) //
					.param(Staff.field.getFIELD_NAME_phone(), s.getPhone())//
					.param(Staff.field.getFIELD_NAME_name(), s.getName())//
					.param(Staff.field.getFIELD_NAME_ICID(), s.getICID())//
					.param(Staff.field.getFIELD_NAME_weChat(), s.getWeChat())//
					.param(Staff.field.getFIELD_NAME_pwdEncrypted(), s.getPwdEncrypted())//
					.param(Staff.field.getFIELD_NAME_passwordExpireDate(), s.getPasswordExpireDate() + "")//
					.param(Staff.field.getFIELD_NAME_isFirstTimeLogin(), s.getIsFirstTimeLogin() + "")//
					.param(Staff.field.getFIELD_NAME_shopID(), s.getShopID() + "")//
					.param(Staff.field.getFIELD_NAME_departmentID(), s.getDepartmentID() + "")//
					.param(Staff.field.getFIELD_NAME_roleID(), s.getRoleID() + "")//
					.param(Staff.field.getFIELD_NAME_returnSalt(), s.getReturnSalt() + "")//
					.param(Staff.field.getFIELD_NAME_status(), s.getStatus() + "");//
			return builder;
		}

		protected static final Staff getStaff() throws Exception {
			staffInput = new Staff();
			staffInput.setPhone(Shared.getValidStaffPhone());
			Thread.sleep(100);
			staffInput.setName("店员" + Shared.generateCompanyName(6));//
			Thread.sleep(100);
			Thread.sleep(100);
			staffInput.setICID(Shared.getValidICID()); //
			Thread.sleep(100);
			staffInput.setWeChat("rr1" + System.currentTimeMillis() % 1000000);//
			SimpleDateFormat sdf = new SimpleDateFormat(BaseAction.DATETIME_FORMAT_Default4);//
			staffInput.setPasswordExpireDate(sdf.parse("2018/02/15 12:30:45"));//
			staffInput.setIsFirstTimeLogin(EnumBoolean.EB_Yes.getIndex()); //
			staffInput.setShopID(1);//
			staffInput.setDepartmentID(1);//
			// staffInput.setInt1(1);
			// staffInput.setInt2(1);
			staffInput.setStatus(EnumStatusStaff.ESS_Incumbent.getIndex());//

			return (Staff) staffInput.clone();
		}
	}

	@Test
	public void createCompanyAndLogin() throws Exception {
		Shared.printTestMethodStartInfo("SIT1_nbr_SG_FirstLoginToUpdatePwd_", order, "创建一个新的公司后，使用老板账号进行登录");

		Company company = BaseCompanyTest.DataInput.getCompany();
		company.setBrandName("曼联球服");
		BaseCompanyTest.getUploadFileSession(BaseCompanyTest.uploadBusinessLicensePictureURL, mvc, sessionOP, BaseCompanyTest.DEFAULT_imagesPath, BaseCompanyTest.DEFAULT_imagesType);
		//
		MvcResult mr1 = mvc.perform(BaseCompanyTest.getBuilder("/company/createEx.bx", MediaType.APPLICATION_JSON, company, sessionOP) //
				.session((MockHttpSession) sessionOP)//
		).andExpect(status().isOk()).andDo(print()).andReturn(); //
		Shared.checkJSONErrorCode(mr1);
		CompanyCP.verifyCreate(mr1, company, companyBO, staffBO, shopBO, company.getDbName());
		CompanyCP.verifyUploadBusinessLicensePicture(mr1, company);
		// 验证敏感信息是否为空
		BaseCompanyTest.checkSensitiveInformation(mr1);
		String json = mr1.getResponse().getContentAsString();
		JSONObject o = JSONObject.fromObject(json);
		companySN = JsonPath.read(o, "$.object.SN");
		dbName = JsonPath.read(o, "$.object.dbName");
		// 验证老板首次登录后会修改密码，判断是否正确修改后删除该售前账号。
		BaseCompanyTest.ValidateIfPreSaleAccountIsDeleted(dbName, mvc, mapBO, companySN, company.getBossPhone(), company.getBossPassword());
		bossPassword = BaseCompanyTest.bossPassword_New;// 新密码替换旧密码
		//
		// 使用售前账号登录（失败）
		MvcResult result = mvc.perform(//
				post("/staff/getTokenEx.bx")//
						.param(Staff.field.getFIELD_NAME_phone(), BaseAction.ACCOUNT_Phone_PreSale)//
						.contentType(MediaType.APPLICATION_JSON)//
		)//
				.andExpect(status().isOk())//
				.andReturn();

		Shared.checkJSONErrorCode(result);
		String encrypt = Shared.encrypt(result, Shared.PASSWORD_DEFAULT);

		MvcResult result1 = mvc.perform(//
				post("/staff/loginEx.bx")//
						.param(Staff.field.getFIELD_NAME_phone(), BaseAction.ACCOUNT_Phone_PreSale)//
						.param(Staff.field.getFIELD_NAME_pwdEncrypted(), encrypt)//
						.param(Staff.field.getFIELD_NAME_companySN(), companySN)//
						.session((MockHttpSession) result.getRequest().getSession())//
						.contentType(MediaType.APPLICATION_JSON))//
				.andExpect(status().isOk())//
				.andReturn();//
		Shared.checkJSONErrorCode(result1, EnumErrorCode.EC_NoSuchData);
		// 使用创建出来的BOSS账号登录SESSION
		try {
			sessionBoss = Shared.getStaffLoginSession(mvc, company.getBossPhone(), bossPassword, companySN);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Test(dependsOnMethods = "createCompanyAndLogin")
	public void createStaffAndLogin() throws Exception {
		Shared.printTestMethodStartInfo("SIT1_nbr_SG_FirstLoginToUpdatePwd_", order, "新增一个员工后登录");

		// 新建一个角色为收银员的员工23
		String staff23Phone = "18814126920";
		String staff23Password = "daydayUp";
		MvcResult ret = getToken(sessionBoss, staff23Phone, Staff.FOR_CreateNewStaff);
		//
		String encrypt = Shared.encrypt(ret, Shared.PASSWORD_DEFAULT);
		Staff s23 = DataInput.getStaff();
		s23.setName("弗兰德");
		s23.setRoleID(EnumTypeRole.ETR_Cashier.getIndex());// create.bx时，int1代表角色
		s23.setPhone(staff23Phone);
		s23.setPwdEncrypted(encrypt);
		Staff staff23 = createStaff(s23);
		//
		// 登录员工23
		staffFirstTimeLogin(staff23, Shared.PASSWORD_DEFAULT, staff23Password, companySN);
	}

	@Test(dependsOnMethods = "createStaffAndLogin")
	public void useLocalComputerCURDFirstLoginPageThenUseOtherComputerLoginAndRetrieve() throws Exception {
		Shared.printTestMethodStartInfo("SIT1_nbr_SG_FirstLoginToUpdatePwd", order, "在本机进行CRUD操作后，在新机登录nbr系统进行查询，查看原机操作的内容是否存在");

	}

	@Test(dependsOnMethods = "useLocalComputerCURDFirstLoginPageThenUseOtherComputerLoginAndRetrieve")
	public void useTwoOrMoreComputerLoginAtTheSameTimeAndCURDFirstLoginPage() throws Exception {
		Shared.printTestMethodStartInfo("SIT1_nbr_SG_FirstLoginToUpdatePwd", order, "在两台机器或者两台以上机器同时登录nbr进行CRUD操作（有可能操作了同一个商品），测试功能是否都通过");

	}

	protected HttpSession staffFirstTimeLogin(Staff staff, String password, String password_New, String companySN) throws Exception, UnsupportedEncodingException {

		// 登录操作拿公钥
		MvcResult ret1 = mvc.perform(//
				post("/staff/getTokenEx.bx")//
						.contentType(MediaType.APPLICATION_JSON)//
						.param(Staff.field.getFIELD_NAME_phone(), staff.getPhone())//
		)//
				.andExpect(status().isOk())//
				.andDo(print())//
				.andReturn();//
		Shared.checkJSONErrorCode(ret1);
		// 密码加密
		String encrypt = Shared.encrypt(ret1, password);
		// 登录
		MvcResult mr = mvc.perform(//
				post("/staff/loginEx.bx")//
						.param(Staff.field.getFIELD_NAME_companySN(), companySN)//
						.param(Staff.field.getFIELD_NAME_phone(), staff.getPhone())//
						.param(Staff.field.getFIELD_NAME_pwdEncrypted(), encrypt)//
						.param(Staff.field.getFIELD_NAME_isLoginFromPos(), String.valueOf(staff.getIsFirstTimeLogin()))//
						.session((MockHttpSession) ret1.getRequest().getSession())//
						.contentType(MediaType.APPLICATION_JSON)//
		)//
				.andExpect(status().isOk())//
				.andDo(print()).andReturn();//
		// 判断是否第一次登录
		// 第一次登录会重定向到密码修改页面
		if (staff.getRoleID() == Role.EnumTypeRole.ETR_Cashier.getIndex()) {// 收银员会报重定向的错误码
			Shared.checkJSONErrorCode(mr, EnumErrorCode.EC_Redirect);
		} else if (staff.getRoleID() == Role.EnumTypeRole.ETR_Boss.getIndex()) {// 店长不会报重定向的错误码
			Shared.checkJSONErrorCode(mr);
		}
		//
		// 强制修改密码
		// HttpSession session = Shared.getStaffLoginSession(mvc, staff.getPhone(),
		// password, companySN);
		MvcResult ret = mvc.perform(post("/staff/getTokenEx.bx") //
				.contentType(MediaType.APPLICATION_JSON) //
				.param(Staff.field.getFIELD_NAME_phone(), staff.getPhone())) //
				.andExpect(status().isOk()).andDo(print()).andReturn();
		// 解析出模和指数
		String json = ret.getResponse().getContentAsString();
		JSONObject o = JSONObject.fromObject(json);
		String modulus = JsonPath.read(o, "$.rsa.modulus");
		String exponent = JsonPath.read(o, "$.rsa.exponent");
		modulus = new BigInteger(modulus, 16).toString();
		exponent = new BigInteger(exponent, 16).toString();
		// 拿到公钥
		RSAPublicKey publicKey = RSAUtils.getPublicKey(modulus, exponent);
		//
		final String pwdOld = password;
		final String pwdNew = password_New;
		// 加密密码
		String sPasswordEncryptedOld = RSAUtils.encryptByPublicKey(pwdOld, publicKey);
		String sPasswordEncryptedNew = RSAUtils.encryptByPublicKey(pwdNew, publicKey);
		// 修改密码
		MvcResult m = mvc.perform(post("/staff/resetMyPasswordEx.bx") //
				.session((MockHttpSession) ret1.getRequest().getSession())//
				.param(Staff.field.getFIELD_NAME_phone(), staff.getPhone()) //
				.param("sPasswordEncryptedOld", sPasswordEncryptedOld) //
				.param("sPasswordEncryptedNew", sPasswordEncryptedNew) //
				.contentType(MediaType.APPLICATION_JSON)).andExpect(status().isOk()).andDo(print()).andReturn();
		//
		Shared.checkJSONErrorCode(m);
		// 解析修改密码后的staff对象
		JSONObject jsonObject = JSONObject.fromObject(m.getResponse().getContentAsString());
		staff = (Staff) staff.parse1(jsonObject.getString(BaseAction.KEY_Object));
		// 登出
		MvcResult mr2 = mvc.perform(get("/staff/logoutEx.bx")//
				.session((MockHttpSession) ret1.getRequest().getSession())//
				.contentType(MediaType.APPLICATION_JSON))//
				.andExpect(status().isOk()).andDo(print()).andReturn();
		Shared.checkJSONErrorCode(mr2);
		// 再登陆一次
		MvcResult ret2 = mvc.perform(//
				post("/staff/getTokenEx.bx")//
						.contentType(MediaType.APPLICATION_JSON)//
						.param(Staff.field.getFIELD_NAME_phone(), staff.getPhone())//
		)//
				.andExpect(status().isOk())//
				.andDo(print())//
				.andReturn();//
		Shared.checkJSONErrorCode(ret2);
		//
		encrypt = Shared.encrypt(ret2, password_New);
		MvcResult mr3 = mvc.perform(//
				post("/staff/loginEx.bx")//
						.param(Staff.field.getFIELD_NAME_companySN(), companySN)//
						.param(Staff.field.getFIELD_NAME_phone(), staff.getPhone())//
						.param(Staff.field.getFIELD_NAME_pwdEncrypted(), encrypt)//
						.param(Staff.field.getFIELD_NAME_isLoginFromPos(), String.valueOf(staff.getIsFirstTimeLogin()))//
						.session((MockHttpSession) sessionBoss)//
						.contentType(MediaType.APPLICATION_JSON)//
		)//
				.andExpect(status().isOk())//
				.andDo(print()).andReturn();//
		Shared.checkJSONErrorCode(mr3);
		return mr3.getRequest().getSession();

	}

	protected Staff createStaff(Staff s) throws Exception, UnsupportedEncodingException, CloneNotSupportedException {
		MvcResult mr = mvc.perform(//
				DataInput.getBuilder("/staff/createEx.bx", MediaType.APPLICATION_JSON, s)//
						.session((MockHttpSession) sessionBoss)//
		)//
				.andExpect(status().isOk())//
				.andDo(print())//
				.andReturn();//
		// 结果验证：检查错误码
		Shared.checkJSONErrorCode(mr);
		StaffCP.verifyCreate(mr, s, staffRoleBO, dbName);
		//
		JSONObject object = JSONObject.fromObject(mr.getResponse().getContentAsString());
		Staff staff = new Staff();
		staff = (Staff) staff.parse1(object.getString(BaseAction.KEY_Object));
		return staff;
	}

	protected MvcResult getToken(HttpSession session, String phone, int forModifyPassword) throws Exception {
		MvcResult ret = mvc.perform(post("/staff/getTokenEx.bx") //
				.contentType(MediaType.APPLICATION_JSON) //
				.session((MockHttpSession) session) //
				.param(Staff.field.getFIELD_NAME_phone(), phone)//
				.param(Staff.field.getFIELD_NAME_forModifyPassword(), String.valueOf(forModifyPassword))//
		) //
				.andExpect(status().isOk()).andDo(print()).andReturn();
		Shared.checkJSONErrorCode(ret);

		return ret;
	}

	@AfterClass
	public void afterClass() {
		super.tearDown();
	}

}
