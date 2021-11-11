package com.bx.erp.sit.sit1.sg.login;

import org.testng.annotations.Test;

import com.bx.erp.action.BaseAction;
import com.bx.erp.model.Staff;
import com.bx.erp.model.BaseModel;
import com.bx.erp.model.ErrorInfo.EnumErrorCode;
import com.bx.erp.model.Role.EnumTypeRole;
import com.bx.erp.test.BaseActionTest;
import com.bx.erp.test.BaseStaffTest;
import com.bx.erp.test.Shared;
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
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

import javax.servlet.http.HttpSession;

import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.testng.annotations.AfterClass;

@WebAppConfiguration
public class NBRLoginPageTest extends BaseActionTest {

	/** 初始密码测试数据(根据IO文档) */
	private static String staff16Password = "123456";
	private static String staff17Password = "HowAreYou";
	private static String staff18Password = "IAmFine";
	private static String staff19Password = "ThankYou";
	private static String staff20Password = "NiceToMeetYou";
	private static String staff21Password = "WelcomeToChina";
	private static String staff22Password = "goodgoodStudy";

	protected static final String Key_OldPasword = "sPasswordEncryptedOld";
	protected static final String Key_NewPasword = "sPasswordEncryptedNew";

	protected AtomicInteger order;
	protected Map<String, BaseModel> NBRLoginPageMap;

	@BeforeClass
	public void beforeClass() throws Exception {
		super.setUp();

		order = new AtomicInteger();
		NBRLoginPageMap = new HashMap<String, BaseModel>();

		sessionBoss = Shared.getStaffLoginSession(mvc, Shared.PhoneOfBoss, Shared.PASSWORD_DEFAULT, Shared.DB_SN_Test);
	}

	public static class DataInput {
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

	}

	@Test
	public void createCashierThenUpdateOtherStaffInfo() throws Exception {
		Shared.printTestMethodStartInfo("SIT1_nbr_SG_AdminLogin_", order, "新建一个角色为收银员的员工16，登录后，到员工页面修改其他员工的信息");

		// 新建一个角色为收银员的员工16
		Staff s16 = BaseStaffTest.DataInput.getStaff();
		MvcResult ret = BaseStaffTest.getToken(sessionBoss, mvc, s16.getPhone(), Staff.FOR_CreateNewStaff);
		//
		String encrypt = Shared.encrypt(ret, Shared.PASSWORD_DEFAULT);
		s16.setRoleID(EnumTypeRole.ETR_Cashier.getIndex());// create.bx时，int1代表角色
		s16.setPwdEncrypted(encrypt);
		//
		Staff staff16 = BaseStaffTest.createStaffViaAction(s16, sessionBoss, mvc, mapBO, Shared.DBName_Test);
		NBRLoginPageMap.put("staff16", staff16);
		//
		// 用这个收银员账号登录
		sessionBoss = (MockHttpSession) staffFirstTimeLogin(staff16, Shared.PASSWORD_DEFAULT, staff16Password, Shared.DB_SN_Test);
		// 修改其他员工的信息
		Staff staff2Update = BaseStaffTest.DataInput.getStaff();
		staff2Update.setRoleID(EnumTypeRole.ETR_Cashier.getIndex());// updateEx.bx时，int2代表角色
		staff2Update.setID(2);

		MvcResult mr2 = mvc.perform(//
				DataInput.getBuilder("/staff/updateEx.bx", MediaType.APPLICATION_JSON, staff2Update)//
						.session((MockHttpSession) sessionBoss)//
		)//
				.andExpect(status().isOk())//
				.andDo(print())//
				.andReturn();//
		// 结果验证:检查错误码
		Shared.checkJSONErrorCode(mr2, EnumErrorCode.EC_NoPermission);
		//
		// 测试完成后把session给改回来
		sessionBoss = Shared.getStaffLoginSession(mvc, Shared.PhoneOfBoss, Shared.PASSWORD_DEFAULT, Shared.DB_SN_Test);
	}

	@Test(dependsOnMethods = "createCashierThenUpdateOtherStaffInfo")
	public void createBossThenUpdateOtherStaffInfo() throws Exception {
		Shared.printTestMethodStartInfo("SIT1_nbr_SG_AdminLogin_", order, "新建一个角色为店长的员工17，登录后，到员工页面修改员工16的信息");

		// 新建一个角色为店长的员工17
		Staff s17 = BaseStaffTest.DataInput.getStaff();
		MvcResult ret = BaseStaffTest.getToken(sessionBoss, mvc, s17.getPhone(), Staff.FOR_CreateNewStaff);
		//
		String encrypt17 = Shared.encrypt(ret, Shared.PASSWORD_DEFAULT);
		s17.setPwdEncrypted(encrypt17);
		//
		Staff staff17 = BaseStaffTest.createStaffViaAction(s17, sessionBoss, mvc, mapBO, Shared.DBName_Test);
		NBRLoginPageMap.put("staff17", staff17);
		//
		// 登录员工17
		sessionBoss = (MockHttpSession) staffFirstTimeLogin(staff17, Shared.PASSWORD_DEFAULT, staff17Password, Shared.DB_SN_Test);
		// 修改员工16的信息
		Staff staff16 = (Staff) NBRLoginPageMap.get("staff16");
		staff16.setName("店员" + Shared.generateCompanyName(6));
		Staff staff16Update = BaseStaffTest.updateStaffViaAction(staff16, sessionBoss, mvc, mapBO, Shared.DBName_Test);
		NBRLoginPageMap.replace("staff16", staff16Update);
		//
		// 测试完成后把session给改回来
		sessionBoss = Shared.getStaffLoginSession(mvc, Shared.PhoneOfBoss, Shared.PASSWORD_DEFAULT, Shared.DB_SN_Test);
	}

	@Test(dependsOnMethods = "createBossThenUpdateOtherStaffInfo")
	public void createStaffThenFirstLogin() throws Exception {
		Shared.printTestMethodStartInfo("SIT1_nbr_SG_AdminLogin_", order, "新建员工18后登录，该员工是第一次登录时，会被强制修改密码");

		// 新建一个角色为店长的员工18
		Staff s18 = BaseStaffTest.DataInput.getStaff();
		MvcResult ret = BaseStaffTest.getToken(sessionBoss, mvc, s18.getPhone(), Staff.FOR_CreateNewStaff);
		//
		String encrypt = Shared.encrypt(ret, Shared.PASSWORD_DEFAULT);
		s18.setPwdEncrypted(encrypt);
		//
		Staff staff18 = BaseStaffTest.createStaffViaAction(s18, sessionBoss, mvc, mapBO, Shared.DBName_Test);
		NBRLoginPageMap.put("staff18", staff18);
		//
		// 模拟第一次登录（重定向到修改密码页面）
		staffFirstTimeLogin(staff18, Shared.PASSWORD_DEFAULT, staff18Password, Shared.DB_SN_Test);
	}

	@Test(dependsOnMethods = "createStaffThenFirstLogin")
	public void createStaffThenUpdatePasswordAndLogin() throws Exception {
		Shared.printTestMethodStartInfo("SIT1_nbr_SG_AdminLogin_", order, "新建一个员工19，修改密码后，再登录nbr系统");

		// 新建一个角色为收银员的员工19
		Staff s19 = BaseStaffTest.DataInput.getStaff();
		MvcResult ret = BaseStaffTest.getToken(sessionBoss, mvc, s19.getPhone(), Staff.FOR_CreateNewStaff);
		//
		String encrypt = Shared.encrypt(ret, Shared.PASSWORD_DEFAULT);
		s19.setRoleID(EnumTypeRole.ETR_Cashier.getIndex());// create.bx时，int1代表角色
		s19.setPwdEncrypted(encrypt);
		//
		Staff staff19 = BaseStaffTest.createStaffViaAction(s19, sessionBoss, mvc, mapBO, Shared.DBName_Test);
		NBRLoginPageMap.put("staff19", staff19);
		//
		// 用这个收银员账号登录
		sessionBoss = (MockHttpSession) staffFirstTimeLogin(staff19, Shared.PASSWORD_DEFAULT, staff19Password, Shared.DB_SN_Test);
		// 修改自己密码
		final String pwdNew = "AndYou";
		Staff staff19Update = BaseStaffTest.resetMyPasswordExViaAction(staff19, mvc, mapBO, Shared.DBName_Test, staff19Password, pwdNew, staff19.getRoleID());
		staff19Password = pwdNew;// 新密码替换旧密码，方便其他测试方法用到。
		NBRLoginPageMap.replace("staff19", staff19Update);
		//
		// 登陆nbr
		Shared.getStaffLoginSession(mvc, staff19Update.getPhone(), staff19Password, Shared.DB_SN_Test);
		//
		// 把session改回来
		sessionBoss = Shared.getStaffLoginSession(mvc, Shared.PhoneOfBoss, Shared.PASSWORD_DEFAULT, Shared.DB_SN_Test);
	}

	@Test(dependsOnMethods = "createStaffThenUpdatePasswordAndLogin")
	public void updateStaffPasswordThenLogin() throws Exception {
		Shared.printTestMethodStartInfo("SIT1_nbr_SG_AdminLogin_", order, "店长修改员工19的密码后登录员工19");

		// 店长重置员工19的密码
		Staff staff19 = (Staff) NBRLoginPageMap.get("staff19");
		Staff staff19Update = BaseStaffTest.resetOtherPasswordExViaAction(staff19, sessionBoss, mvc, mapBO, Shared.DBName_Test, Shared.PASSWORD_DEFAULT);
		NBRLoginPageMap.replace("staff19", staff19Update);
		//
		// 登录员工19（重置密码后第一次登录会重定向到修改密码页面）
		staffFirstTimeLogin(staff19Update, Shared.PASSWORD_DEFAULT, staff19Password, Shared.DB_SN_Test);
	}

	@Test(dependsOnMethods = "updateStaffPasswordThenLogin")
	public void updateStaffRoleThenLogin() throws Exception {
		Shared.printTestMethodStartInfo("SIT1_nbr_SG_AdminLogin_", order, "店长修改员工19角色为店长后登录员工19");

		// 修改员工19的角色
		Staff staff19 = (Staff) NBRLoginPageMap.get("staff19");
		staff19.setRoleID(EnumTypeRole.ETR_Boss.getIndex());
		Staff staff19Update = BaseStaffTest.updateStaffViaAction(staff19, sessionBoss, mvc, mapBO, Shared.DBName_Test);
		NBRLoginPageMap.replace("staff19", staff19Update);
		//
		// 登录员工19
		Shared.getStaffLoginSession(mvc, staff19Update.getPhone(), staff19Password, Shared.DB_SN_Test);// ...
	}

	@Test(dependsOnMethods = "updateStaffRoleThenLogin")
	public void updateStaffRoleThenLogin2() throws Exception {
		Shared.printTestMethodStartInfo("SIT1_nbr_SG_AdminLogin_", order, "店长修改员工18角色为收银员后登录员工18");

		// 修改员工18的角色
		Staff staff18 = (Staff) NBRLoginPageMap.get("staff18");
		staff18.setRoleID(EnumTypeRole.ETR_Cashier.getIndex());
		Staff staff18Update = BaseStaffTest.updateStaffViaAction(staff18, sessionBoss, mvc, mapBO, Shared.DBName_Test);
		NBRLoginPageMap.replace("staff18", staff18Update);
		//
		// 登录员工18
		Shared.getStaffLoginSession(mvc, staff18Update.getPhone(), staff18Password, Shared.DB_SN_Test);// ...
	}

	@Test(dependsOnMethods = "updateStaffRoleThenLogin2")
	public void updateStaffPhoneAndResetStaffPasswordThenLogin() throws Exception {
		Shared.printTestMethodStartInfo("SIT1_nbr_SG_AdminLogin_", order, "修改员工19的手机号码，再修改该员工的密码，然后登录");

		// 修改员工19的电话
		Staff staff19 = (Staff) NBRLoginPageMap.get("staff19");
		staff19.setRoleID(EnumTypeRole.ETR_Boss.getIndex());
		staff19.setPhone(Shared.getValidStaffPhone());
		Staff staff19Update = BaseStaffTest.updateStaffViaAction(staff19, sessionBoss, mvc, mapBO, Shared.DBName_Test);
		//
		// 修改员工19的密码
		staff19Update = BaseStaffTest.resetOtherPasswordExViaAction(staff19, sessionBoss, mvc, mapBO, Shared.DBName_Test, Shared.PASSWORD_DEFAULT);
		NBRLoginPageMap.replace("staff19", staff19Update);
		//
		// 登录员工19（重置密码后第一次登录会重定向到修改密码页面）
		staffFirstTimeLogin(staff19Update, Shared.PASSWORD_DEFAULT, staff19Password, Shared.DB_SN_Test);
	}

	@Test(dependsOnMethods = "updateStaffRoleThenLogin2")
	public void deleteStaffThenLogin() throws Exception {
		Shared.printTestMethodStartInfo("SIT1_nbr_SG_AdminLogin_", order, "删除一个员工19后登录");

		// 删除员工19（结果验证在方法里面）
		Staff staff19 = (Staff) NBRLoginPageMap.get("staff19");
		staff19.setReturnSalt(0);
		BaseStaffTest.deleteStaffViaAction(staff19, sessionBoss, mvc, mapBO, Shared.DBName_Test);
		//
		// 登录员工19
		MvcResult ret1 = mvc.perform(//
				post("/staff/getTokenEx.bx")//
						.contentType(MediaType.APPLICATION_JSON)//
						.param(Staff.field.getFIELD_NAME_phone(), staff19.getPhone())//
		)//
				.andExpect(status().isOk())//
				.andDo(print())//
				.andReturn();//

		String pwdEncrypted19 = Shared.encrypt(ret1, staff19Password);
		//
		MvcResult mr = mvc.perform(//
				post("/staff/loginEx.bx")//
						.param(Staff.field.getFIELD_NAME_companySN(), Shared.DB_SN_Test)// ...串联之后要改
						.param(Staff.field.getFIELD_NAME_phone(), staff19.getPhone())//
						.param(Staff.field.getFIELD_NAME_pwdEncrypted(), pwdEncrypted19)//
						.param(Staff.field.getFIELD_NAME_isLoginFromPos(), String.valueOf(staff19.getIsFirstTimeLogin()))//
						.session((MockHttpSession) ret1.getRequest().getSession())//
						.contentType(MediaType.APPLICATION_JSON)//
		)//
				.andExpect(status().isOk())//
				.andDo(print()).andReturn();//

		Shared.checkJSONErrorCode(mr, EnumErrorCode.EC_NoSuchData);
	}

	@Test(dependsOnMethods = "deleteStaffThenLogin")
	public void createStaffThenUpdateAndLogin() throws Exception {
		Shared.printTestMethodStartInfo("SIT1_nbr_SG_AdminLogin_", order, "新增员工20，分别修改该员工手机号和密码后登录");

		// 新增员工20
		Staff s20 = BaseStaffTest.DataInput.getStaff();
		MvcResult ret = BaseStaffTest.getToken(sessionBoss, mvc, s20.getPhone(), Staff.FOR_CreateNewStaff);
		//
		String encrypt = Shared.encrypt(ret, Shared.PASSWORD_DEFAULT);
		s20.setRoleID(EnumTypeRole.ETR_Cashier.getIndex());// create.bx时，int1代表角色
		s20.setPwdEncrypted(encrypt);
		//
		Staff staff20 = BaseStaffTest.createStaffViaAction(s20, sessionBoss, mvc, mapBO, Shared.DBName_Test);
		NBRLoginPageMap.put("staff20", staff20);
		//
		// 修改员工20的手机号
		staff20.setRoleID(EnumTypeRole.ETR_Cashier.getIndex());
		staff20.setPhone(Shared.getValidStaffPhone());
		Staff staff20Update = BaseStaffTest.updateStaffViaAction(staff20, sessionBoss, mvc, mapBO, Shared.DBName_Test);
		// 登录员工20(第一次登录重置密码)
		staffFirstTimeLogin(staff20Update, Shared.PASSWORD_DEFAULT, staff20Password, Shared.DB_SN_Test);
		//
		// 重新登录店长账号
		sessionBoss = Shared.getStaffLoginSession(mvc, Shared.PhoneOfBoss, Shared.PASSWORD_DEFAULT, Shared.DB_SN_Test);
		// 修改员工20密码
		BaseStaffTest.resetOtherPasswordExViaAction(staff20Update, sessionBoss, mvc, mapBO, Shared.DBName_Test, Shared.PASSWORD_DEFAULT);
		NBRLoginPageMap.replace("staff20", staff20Update);
		// 登录员工20(店长重置员工密码后相当于第一次登录)
		staffFirstTimeLogin(staff20Update, Shared.PASSWORD_DEFAULT, staff20Password, Shared.DB_SN_Test);// ...
		//
		// 把session改回来
		sessionBoss = Shared.getStaffLoginSession(mvc, Shared.PhoneOfBoss, Shared.PASSWORD_DEFAULT, Shared.DB_SN_Test);
	}

	@Test(dependsOnMethods = "createStaffThenUpdateAndLogin")
	public void createStaffThenDeleteAndLogin() throws Exception {
		Shared.printTestMethodStartInfo("SIT1_nbr_SG_AdminLogin_", order, "新增员工21，删除该员工后登录");

		// 新增员工21
		Staff s21 = BaseStaffTest.DataInput.getStaff();
		MvcResult ret = BaseStaffTest.getToken(sessionBoss, mvc, s21.getPhone(), Staff.FOR_CreateNewStaff);
		//
		String encrypt = Shared.encrypt(ret, staff21Password);
		s21.setRoleID(EnumTypeRole.ETR_Cashier.getIndex());// create.bx时，int1代表角色
		s21.setPwdEncrypted(encrypt);
		Staff staff21 = BaseStaffTest.createStaffViaAction(s21, sessionBoss, mvc, mapBO, Shared.DBName_Test);
		//
		// 删除员工21
		staff21.setReturnSalt(0);
		BaseStaffTest.deleteStaffViaAction(staff21, sessionBoss, mvc, mapBO, Shared.DBName_Test);
		// 登录员工21（失败）
		MvcResult ret1 = mvc.perform(//
				post("/staff/getTokenEx.bx")//
						.contentType(MediaType.APPLICATION_JSON)//
						.param(Staff.field.getFIELD_NAME_phone(), staff21.getPhone())//
		)//
				.andExpect(status().isOk())//
				.andDo(print())//
				.andReturn();//

		String pwdEncrypted21 = Shared.encrypt(ret1, staff21Password);
		//
		MvcResult mr = mvc.perform(//
				post("/staff/loginEx.bx")//
						.param(Staff.field.getFIELD_NAME_companySN(), Shared.DB_SN_Test)// ...串联之后要改
						.param(Staff.field.getFIELD_NAME_phone(), staff21.getPhone())//
						.param(Staff.field.getFIELD_NAME_pwdEncrypted(), pwdEncrypted21)//
						.param(Staff.field.getFIELD_NAME_isLoginFromPos(), String.valueOf(staff21.getIsFirstTimeLogin()))//
						.session((MockHttpSession) ret1.getRequest().getSession())//
						.contentType(MediaType.APPLICATION_JSON)//
		)//
				.andExpect(status().isOk())//
				.andDo(print()).andReturn();//

		Shared.checkJSONErrorCode(mr, EnumErrorCode.EC_NoSuchData);
	}

	@Test(dependsOnMethods = "createStaffThenDeleteAndLogin")
	public void createStaffAndUpdateThenDeleteAndLogin() throws Exception {
		Shared.printTestMethodStartInfo("SIT1_nbr_SG_AdminLogin_", order, "新增员工22，修改该员工，再删除该员工后登录");

		// 新增员工22
		Staff s22 = BaseStaffTest.DataInput.getStaff();
		MvcResult ret = BaseStaffTest.getToken(sessionBoss, mvc, s22.getPhone(), Staff.FOR_CreateNewStaff);
		//
		String encrypt = Shared.encrypt(ret, staff22Password);
		s22.setRoleID(EnumTypeRole.ETR_Cashier.getIndex());// create.bx时，int1代表角色
		s22.setPwdEncrypted(encrypt);
		Staff staff22 = BaseStaffTest.createStaffViaAction(s22, sessionBoss, mvc, mapBO, Shared.DBName_Test);
		// 修改员工22手机号
		staff22.setRoleID(EnumTypeRole.ETR_Cashier.getIndex());
		staff22.setPhone(Shared.getValidStaffPhone());
		Staff staff22Update = BaseStaffTest.updateStaffViaAction(staff22, sessionBoss, mvc, mapBO, Shared.DBName_Test);
		// 删除员工22
		staff22Update.setReturnSalt(0);
		BaseStaffTest.deleteStaffViaAction(staff22Update, sessionBoss, mvc, mapBO, Shared.DBName_Test);
		// 登录员工22（失败）
		MvcResult ret1 = mvc.perform(//
				post("/staff/getTokenEx.bx")//
						.contentType(MediaType.APPLICATION_JSON)//
						.param(Staff.field.getFIELD_NAME_phone(), staff22Update.getPhone())//
		)//
				.andExpect(status().isOk())//
				.andDo(print())//
				.andReturn();//

		String pwdEncrypted22 = Shared.encrypt(ret1, staff22Password);
		//
		MvcResult mr = mvc.perform(//
				post("/staff/loginEx.bx")//
						.param(Staff.field.getFIELD_NAME_companySN(), Shared.DB_SN_Test)// ...串联之后要改
						.param(Staff.field.getFIELD_NAME_phone(), staff22Update.getPhone())//
						.param(Staff.field.getFIELD_NAME_pwdEncrypted(), pwdEncrypted22)//
						.param(Staff.field.getFIELD_NAME_isLoginFromPos(), String.valueOf(staff22Update.getIsFirstTimeLogin()))//
						.session((MockHttpSession) ret1.getRequest().getSession())//
						.contentType(MediaType.APPLICATION_JSON)//
		)//
				.andExpect(status().isOk())//
				.andDo(print()).andReturn();//

		Shared.checkJSONErrorCode(mr, EnumErrorCode.EC_NoSuchData);
		// 重新创建员工22
		staff22 = BaseStaffTest.createStaffViaAction(s22, sessionBoss, mvc, mapBO, Shared.DBName_Test);
		// 修改员工22密码
		String staff22Pwd = "goodgoodStudy";
		staff22Update = BaseStaffTest.resetOtherPasswordExViaAction(staff22, sessionBoss, mvc, mapBO, Shared.DBName_Test, staff22Pwd);
		// 删除员工22
		staff22Update.setReturnSalt(0);
		BaseStaffTest.deleteStaffViaAction(staff22Update, sessionBoss, mvc, mapBO, Shared.DBName_Test);
		// 登录员工22（失败）
		MvcResult ret3 = mvc.perform(//
				post("/staff/getTokenEx.bx")//
						.contentType(MediaType.APPLICATION_JSON)//
						.param(Staff.field.getFIELD_NAME_phone(), staff22Update.getPhone())//
		)//
				.andExpect(status().isOk())//
				.andDo(print())//
				.andReturn();//

		pwdEncrypted22 = Shared.encrypt(ret3, staff22Pwd);
		//
		MvcResult mr2 = mvc.perform(//
				post("/staff/loginEx.bx")//
						.param(Staff.field.getFIELD_NAME_companySN(), Shared.DB_SN_Test)// ...串联之后要改
						.param(Staff.field.getFIELD_NAME_phone(), staff22Update.getPhone())//
						.param(Staff.field.getFIELD_NAME_pwdEncrypted(), pwdEncrypted22)//
						.param(Staff.field.getFIELD_NAME_isLoginFromPos(), String.valueOf(staff22Update.getIsFirstTimeLogin()))//
						.session((MockHttpSession) ret3.getRequest().getSession())//
						.contentType(MediaType.APPLICATION_JSON)//
		)//
				.andExpect(status().isOk())//
				.andDo(print()).andReturn();//

		Shared.checkJSONErrorCode(mr2, EnumErrorCode.EC_NoSuchData);
		// 把session改回来
		sessionBoss = Shared.getStaffLoginSession(mvc, Shared.PhoneOfBoss, Shared.PASSWORD_DEFAULT, Shared.DB_SN_Test);
	}
				
	@Test(dependsOnMethods = "createStaffAndUpdateThenDeleteAndLogin")
	public void updateStaffThenDeleteAndLogin() throws Exception {
		Shared.printTestMethodStartInfo("SIT1_nbr_SG_AdminLogin_", order, "修改员工20信息（手机号和密码）后删除该员工，再登录该员工");

		// 修改员工20手机号
		Staff staff20 = (Staff) NBRLoginPageMap.get("staff20");
		staff20.setRoleID(EnumTypeRole.ETR_Cashier.getIndex());
		staff20.setPhone(Shared.getValidStaffPhone());
		Staff staff20Update = BaseStaffTest.updateStaffViaAction(staff20, sessionBoss, mvc, mapBO, Shared.DBName_Test);
		// 修改员工20密码
		String staff20Pwd = "NiceToMeetYou";
		staff20Update = BaseStaffTest.resetOtherPasswordExViaAction(staff20Update, sessionBoss, mvc, mapBO, Shared.DBName_Test, staff20Pwd);
		// 删除员工20
		staff20Update.setReturnSalt(0);
		BaseStaffTest.deleteStaffViaAction(staff20Update, sessionBoss, mvc, mapBO, Shared.DBName_Test);
		// 登录员工20
		MvcResult ret2 = mvc.perform(//
				post("/staff/getTokenEx.bx")//
						.contentType(MediaType.APPLICATION_JSON)//
						.param(Staff.field.getFIELD_NAME_phone(), staff20Update.getPhone())//
		)//
				.andExpect(status().isOk())//
				.andDo(print())//
				.andReturn();//

		String pwdEncrypted20 = Shared.encrypt(ret2, staff20Pwd);
		//
		MvcResult mr = mvc.perform(//
				post("/staff/loginEx.bx")//
						.param(Staff.field.getFIELD_NAME_companySN(), Shared.DB_SN_Test)// ...串联之后要改
						.param(Staff.field.getFIELD_NAME_phone(), staff20Update.getPhone())//
						.param(Staff.field.getFIELD_NAME_pwdEncrypted(), pwdEncrypted20)//
						.param(Staff.field.getFIELD_NAME_isLoginFromPos(), String.valueOf(staff20Update.getIsFirstTimeLogin()))//
						.session((MockHttpSession) ret2.getRequest().getSession())//
						.contentType(MediaType.APPLICATION_JSON)//
		)//
				.andExpect(status().isOk())//
				.andDo(print()).andReturn();//

		Shared.checkJSONErrorCode(mr, EnumErrorCode.EC_NoSuchData);
		//
		// 把session改回来
		sessionBoss = Shared.getStaffLoginSession(mvc, Shared.PhoneOfBoss, Shared.PASSWORD_DEFAULT, Shared.DB_SN_Test);
	}

	@Test(dependsOnMethods = "updateStaffThenDeleteAndLogin")
	public void useLocalComputerCURDNBRLoginPageThenUseOtherComputerLoginAndRetrieve() throws Exception {
		Shared.printTestMethodStartInfo("SIT1_nbr_SG_AdminLogin_", order, "在本机进行CRUD操作后，在新机登录nbr系统进行查询，查看原机操作的内容是否存在");

	}

	@Test(dependsOnMethods = "useLocalComputerCURDNBRLoginPageThenUseOtherComputerLoginAndRetrieve")
	public void useTwoOrMoreComputerLoginAtTheSameTimeAndCURDNBRLoginPage() throws Exception {
		Shared.printTestMethodStartInfo("SIT1_nbr_SG_AdminLogin_", order, "在两台机器或者两台以上机器同时登录nbr进行CRUD操作（有可能操作了同一个商品），测试功能是否都通过");

	}

	@Test(dependsOnMethods = "useTwoOrMoreComputerLoginAtTheSameTimeAndCURDNBRLoginPage")
	public void preSalesCanLoginDirectly() throws Exception {
		Shared.printTestMethodStartInfo("SIT1_nbr_SG_AdminLogin_", order, "售前人员无需修改密码直接登录");

		// ...老板账号登录之后就把售前账号删除了
	}

	@Test(dependsOnMethods = "preSalesCanLoginDirectly")
	public void useTwoWebLoginSameStaffAndCURD() throws Exception {
		Shared.printTestMethodStartInfo("SIT1_nbr_SG_AdminLogin_", order, "在浏览器Web1上登录Staff登录成功，后在新开一个浏览器Web2上登录该Stff,在Web1上做CRUD的操作，测试Staff在Web1上是否被拒绝操作");

	}

	@Test(dependsOnMethods = "useTwoWebLoginSameStaffAndCURD")
	public void useWebAndPosLoginSameStaffAndCURD() throws Exception {
		Shared.printTestMethodStartInfo("SIT1_nbr_SG_AdminLogin_", order, "在网页端登录Staff，后在POS端登录相同的Staff，且都登录成功，都可做CRUD操作");

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
						.param(Staff.field.getFIELD_NAME_companySN(), Shared.DB_SN_Test)//
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
		Shared.checkJSONErrorCode(mr, EnumErrorCode.EC_Redirect); // 收银员和店长都会报重定向的错误码
		//
		// 强制修改密码
		// HttpSession session = Shared.getStaffLoginSession(mvc, staff.getPhone(),
		// password, Shared.DB_SN_Test);
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
						.param(Staff.field.getFIELD_NAME_companySN(), Shared.DB_SN_Test)//
						.param(Staff.field.getFIELD_NAME_phone(), staff.getPhone())//
						.param(Staff.field.getFIELD_NAME_pwdEncrypted(), encrypt)//
						.param(Staff.field.getFIELD_NAME_isLoginFromPos(), String.valueOf(staff.getIsFirstTimeLogin()))//
						.session((MockHttpSession) ret2.getRequest().getSession())//
						.contentType(MediaType.APPLICATION_JSON)//
		)//
				.andExpect(status().isOk())//
				.andDo(print()).andReturn();//
		Shared.checkJSONErrorCode(mr3);
		return mr3.getRequest().getSession();

	}

	@AfterClass
	public void afterClass() {
		super.tearDown();
	}

}
