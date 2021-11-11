package com.bx.erp.test.wx;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import javax.servlet.http.HttpSession;

import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MvcResult;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import com.bx.erp.model.ErrorInfo.EnumErrorCode;
import com.bx.erp.model.Company;
import com.bx.erp.model.Staff;
import com.bx.erp.test.BaseActionTest;
import com.bx.erp.test.BaseCompanyTest;
import com.bx.erp.test.Shared;

@WebAppConfiguration
public class WxActionTest extends BaseActionTest {
	@BeforeClass
	public void setup() {
		super.setUp();
	}

	@AfterClass
	public void tearDown() {
		super.tearDown();
	}

	@Test
	public void testUserBindCase1() throws Exception {
		Shared.printTestMethodStartInfo();
		Shared.caseLog("正常绑定");

		MvcResult ret = getToken(Shared.PhoneOfBoss);

		Staff staff = new Staff();
		staff.setCompanySN(Shared.DB_SN_Test);
		staff.setOpenid("o1uoyw" + String.valueOf(System.currentTimeMillis()).substring(0, 10));
		staff.setPhone(Shared.PhoneOfBoss);
		staff.setPwdEncrypted(Shared.encrypt(ret, Shared.PASSWORD_DEFAULT));

		userBind(ret.getRequest().getSession(), staff, EnumErrorCode.EC_NoError);
	}

	@Test
	public void testUserBindCase2() throws Exception {
		Shared.printTestMethodStartInfo();
		Shared.caseLog("绑定公司时不输入密码");

		Staff staff = new Staff();
		staff.setCompanySN(Shared.DB_SN_Test);
		staff.setOpenid("o1uoyw" + String.valueOf(System.currentTimeMillis()).substring(0, 10));
		staff.setPhone(Shared.PhoneOfBoss);

		userBind(Shared.getStaffLoginSession(mvc, Shared.PhoneOfBoss), staff, EnumErrorCode.EC_BusinessLogicNotDefined);
	}

	@Test
	public void testUserBindCase3() throws Exception {
		Shared.printTestMethodStartInfo();
		Shared.caseLog("绑定公司时不输入公司编号");

		Staff staff = new Staff();
		staff.setOpenid("o1uoyw" + String.valueOf(System.currentTimeMillis()).substring(0, 10));
		staff.setPhone(Shared.PhoneOfBoss);
		staff.setPwdEncrypted(Shared.PASSWORD_DEFAULT);

		userBind(Shared.getStaffLoginSession(mvc, Shared.PhoneOfBoss), staff, EnumErrorCode.EC_BusinessLogicNotDefined);
	}

	@Test
	public void testUserBindCase4() throws Exception {
		Shared.printTestMethodStartInfo();
		Shared.caseLog("绑定公司时不输入电话号码");

		Staff staff = new Staff();
		staff.setOpenid("o1uoyw" + String.valueOf(System.currentTimeMillis()).substring(0, 10));
		staff.setPwdEncrypted(Shared.PASSWORD_DEFAULT);
		staff.setCompanySN(Shared.DB_SN_Test);

		userBind(Shared.getStaffLoginSession(mvc, Shared.PhoneOfBoss), staff, EnumErrorCode.EC_BusinessLogicNotDefined);
	}

	@Test
	public void testUserBindCase5() throws Exception {
		Shared.printTestMethodStartInfo();
		Shared.caseLog("绑定公司时openID不存在");

		Staff staff = new Staff();
		staff.setPwdEncrypted(Shared.PASSWORD_DEFAULT);
		staff.setPhone(Shared.PhoneOfBoss);
		staff.setCompanySN(Shared.DB_SN_Test);

		userBind(Shared.getStaffLoginSession(mvc, Shared.PhoneOfBoss), staff, EnumErrorCode.EC_BusinessLogicNotDefined);
	}

	@Test
	public void testUserBindCase6() throws Exception {
		Shared.printTestMethodStartInfo();
		Shared.caseLog("绑定公司时输入的密码为错误的密码");

		MvcResult ret = getToken(Shared.PhoneOfBoss);

		Staff staff = new Staff();
		staff.setPwdEncrypted(Shared.encrypt(ret, "1111111"));
		staff.setPhone(Shared.PhoneOfBoss);
		staff.setCompanySN(Shared.DB_SN_Test);
		staff.setOpenid("o1uoyw" + String.valueOf(System.currentTimeMillis()).substring(0, 10));

		userBind(ret.getRequest().getSession(), staff, EnumErrorCode.EC_NoSuchData);
	}

	@Test
	public void testUserBindCase7() throws Exception {
		Shared.printTestMethodStartInfo();
		Shared.caseLog("一个微信号绑定多个帐号");

		// 老板帐号绑定openID
		MvcResult ret = getToken(Shared.PhoneOfBoss);

		Staff staffBoss = new Staff();
		staffBoss.setPwdEncrypted(Shared.encrypt(ret, Shared.PASSWORD_DEFAULT));
		staffBoss.setPhone(Shared.PhoneOfBoss);
		staffBoss.setCompanySN(Shared.DB_SN_Test);
		staffBoss.setOpenid("o1uoyw" + String.valueOf(System.currentTimeMillis()).substring(0, 10));

		userBind(ret.getRequest().getSession(), staffBoss, EnumErrorCode.EC_NoError);

		// 收银员帐号绑定相同的OpenID
		ret = getToken(Shared.PhoneOfCashier);

		Staff staffCashier = new Staff();
		staffCashier.setPwdEncrypted(Shared.encrypt(ret, Shared.PASSWORD_DEFAULT));
		staffCashier.setPhone(Shared.PhoneOfCashier);
		staffCashier.setCompanySN(Shared.DB_SN_Test);
		staffCashier.setOpenid(staffBoss.getOpenid());

		userBind(ret.getRequest().getSession(), staffCashier, EnumErrorCode.EC_BusinessLogicNotDefined);
	}

	@Test
	public void testUserBindCase8() throws Exception {
		Shared.printTestMethodStartInfo();
		Shared.caseLog("一个微信号绑定多公司的员工帐号");
		// 绑定公司A的老板帐号
		MvcResult ret = getToken(Shared.PhoneOfBoss);

		Staff staffBoss = new Staff();
		staffBoss.setPwdEncrypted(Shared.encrypt(ret, Shared.PASSWORD_DEFAULT));
		staffBoss.setPhone(Shared.PhoneOfBoss);
		staffBoss.setCompanySN(Shared.DB_SN_Test);
		staffBoss.setOpenid("o1uoyw" + Shared.generateStringByTime(9));

		userBind(ret.getRequest().getSession(), staffBoss, EnumErrorCode.EC_NoError);

		// 绑定公司B的老板帐号
		// 创建公司B
		Company company = BaseCompanyTest.DataInput.getCompany();
		Company createCompany = BaseCompanyTest.createCompanyViaAction(company, mvc, mapBO, sessionOP);

		ret = getToken(createCompany.getBossPhone());

		// 修改公司B的staff的openID为nbr公司老板的openID
		Staff staff = new Staff();
		// 老板首次登录会修改密码
		staff.setPwdEncrypted(Shared.encrypt(ret, BaseCompanyTest.bossPassword_New));
		staff.setPhone(createCompany.getBossPhone());
		staff.setCompanySN(createCompany.getSN());
		staff.setOpenid(staffBoss.getOpenid());

		userBind(ret.getRequest().getSession(), staff, EnumErrorCode.EC_NoError);
	}

	private MvcResult getToken(String phone) throws Exception {
		MvcResult ret = mvc.perform(//
				post("/wx/getTokenEx.bx")//
						.contentType(MediaType.APPLICATION_JSON)//
						.param(Staff.field.getFIELD_NAME_phone(), phone))//
				.andExpect(status().isOk())//
				.andDo(print()).andReturn();
		Shared.checkJSONErrorCode(ret);
		return ret;
	}

	private void userBind(HttpSession session, Staff staff, EnumErrorCode errorCode) throws Exception {
		MvcResult mr = mvc.perform(//
				post("/wx/userBind.bx")//
						.param(Staff.field.getFIELD_NAME_companySN(), staff.getCompanySN()) //
						.param(Staff.field.getFIELD_NAME_openid(), staff.getOpenid()) //
						.param(Staff.field.getFIELD_NAME_pwdEncrypted(), staff.getPwdEncrypted()) //
						.param(Staff.field.getFIELD_NAME_phone(), staff.getPhone())//
						.contentType(MediaType.APPLICATION_JSON)//
						.session((MockHttpSession) session) //
		)//
				.andExpect(status().isOk())//
				.andDo(print())//
				.andReturn();
		Shared.checkJSONErrorCode(mr, errorCode);
	}
}
