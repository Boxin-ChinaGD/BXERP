package com.bx.erp.sit.sit1.sg.staff;

import org.testng.annotations.Test;

import com.bx.erp.action.BaseAction;
import com.bx.erp.action.BaseAction.EnumSession;
import com.bx.erp.model.BaseModel;
import com.bx.erp.model.Company;
import com.bx.erp.model.ReturnCommoditySheet;
import com.bx.erp.model.Staff;
import com.bx.erp.model.ErrorInfo.EnumErrorCode;
import com.bx.erp.model.RetailTrade;
import com.bx.erp.model.Role.EnumTypeRole;
import com.bx.erp.model.Staff.EnumStatusStaff;
import com.bx.erp.model.StaffRole;
import com.bx.erp.model.commodity.CommodityHistory;
import com.bx.erp.model.purchasing.PurchasingOrder;
import com.bx.erp.model.trade.Promotion;
import com.bx.erp.test.BaseActionTest;
import com.bx.erp.test.BaseCompanyTest;
import com.bx.erp.test.BaseStaffTest;
import com.bx.erp.test.Shared;
import com.bx.erp.test.checkPoint.CompanyCP;
import com.jayway.jsonpath.JsonPath;

import net.sf.json.JSONObject;

import org.testng.annotations.BeforeClass;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

import javax.servlet.http.HttpSession;

import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MvcResult;
import org.testng.Assert;
import org.testng.annotations.AfterClass;

@WebAppConfiguration
public class StaffListTest extends BaseActionTest {

	protected AtomicInteger order;
	protected String bossPhone = "";
	protected String companySN = "";
	protected String dbName = "";

	private Map<String, BaseModel> staffListMap;
	private static final String Role_PreSale = "1";
	private static final String Role_Boss = "4";

	/** 检查staff手机号唯一字段是否已存在 */
	public static final int CASE_CHECK_UNIQUE_STAFF_PHONE = 1;

	@Test
	public void createNewShopAndOpenStaffList() throws Exception {
		Shared.printTestMethodStartInfo("SIT1_nbr_SG_StaffList_", order, "新店开张，打开员工管理页面");

		// 创建公司
		Company company = BaseCompanyTest.DataInput.getCompany();
		bossPhone = company.getBossPhone();
		dbName = company.getDbName();
		BaseCompanyTest.getUploadFileSession(BaseCompanyTest.uploadBusinessLicensePictureURL, mvc, sessionOP, BaseCompanyTest.DEFAULT_imagesPath, BaseCompanyTest.DEFAULT_imagesType);
		//
		MvcResult mr1 = mvc.perform(BaseCompanyTest.getBuilder("/company/createEx.bx", MediaType.APPLICATION_JSON, company, sessionOP) //
				.session((MockHttpSession) sessionOP)//
		).andExpect(status().isOk()).andDo(print()).andReturn(); //
		Shared.checkJSONErrorCode(mr1);
		CompanyCP.verifyCreate(mr1, company, companyBO, staffBO, shopBO, Shared.BXDBName_Test);
		CompanyCP.verifyUploadBusinessLicensePicture(mr1, company);
		// 验证敏感信息是否为空
		BaseCompanyTest.checkSensitiveInformation(mr1);
		// 验证老板首次登录后会修改密码，判断是否正确修改后删除该售前账号。
		companySN = ((Company) Shared.parse1Object(mr1, company, BaseAction.KEY_Object)).getSN();
		BaseCompanyTest.ValidateIfPreSaleAccountIsDeleted(dbName, mvc, mapBO, companySN, company.getBossPhone(), company.getBossPassword());
		// 登录回原来的session
		sessionBoss = Shared.getStaffLoginSession(mvc, bossPhone, BaseCompanyTest.bossPassword_New, companySN);
	}

	@Test(dependsOnMethods = "createNewShopAndOpenStaffList")
	public void createStaffAndRetrieveByFieldName() throws Exception {
		Shared.printTestMethodStartInfo("SIT1_nbr_SG_StaffList_", order, "新增员工3后，输入员工的姓名或者手机号搜索该员工");

		// 创建员工3
		Staff s3 = BaseStaffTest.DataInput.getStaff();
		MvcResult ret = BaseStaffTest.getToken(sessionBoss, mvc, s3.getPhone(), Staff.FOR_CreateNewStaff);
		//
		String encrypt = Shared.encrypt(ret, Shared.PASSWORD_DEFAULT);
		s3.setRoleID(EnumTypeRole.ETR_Cashier.getIndex());// create.bx时，int1代表角色
		s3.setPwdEncrypted(encrypt);

		Staff staff3 = BaseStaffTest.createStaffViaAction(s3, sessionBoss, mvc, mapBO, dbName);
		staffListMap.put("staff3", staff3);

		queryStaffByNameAndphone(staff3);
	}

	@Test(dependsOnMethods = "createStaffAndRetrieveByFieldName")
	public void updateStaffAndRetrieveByFieldName() throws UnsupportedEncodingException, Exception {
		Shared.printTestMethodStartInfo("SIT1_nbr_SG_StaffList_", order, "修改员工3信息后，输入员工的姓名或者手机号搜索该员工");

		// 修改员工3
		Staff staff3 = (Staff) staffListMap.get("staff3");
		staff3.setRoleID(EnumTypeRole.ETR_Cashier.getIndex());
		staff3.setName("大明虎城");
		Staff staff3Update = BaseStaffTest.updateStaffViaAction(staff3, sessionBoss, mvc, mapBO, dbName);
		staffListMap.replace("staff3", staff3Update);

		queryStaffByNameAndphone(staff3Update);
	}

	@Test(dependsOnMethods = "updateStaffAndRetrieveByFieldName")
	public void deleteStaffAndRetrieveByFieldName() throws UnsupportedEncodingException, CloneNotSupportedException, Exception {
		Shared.printTestMethodStartInfo("SIT1_nbr_SG_StaffList_", order, "删除员工3后，输入该员工的姓名或者手机号进行搜搜");

		// 删除员工3
		Staff staff3 = (Staff) staffListMap.get("staff3");
		staff3.setReturnSalt(0);
		BaseStaffTest.deleteStaffViaAction(staff3, sessionBoss, mvc, mapBO, dbName);
		// 输入员工的姓名搜索该员工
		Staff staffQuery = new Staff();
		staffQuery.setQueryKeyword(staff3.getName());
		//
		List<BaseModel> staffList = BaseStaffTest.retrieveNViaAction(staffQuery, sessionBoss, mvc);
		Assert.assertTrue(staffList.size() == 0, "不应该查询出对象");
		// 输入员工的手机号搜索该员工
		staffQuery.setQueryKeyword(staff3.getPhone());
		staffList = BaseStaffTest.retrieveNViaAction(staffQuery, sessionBoss, mvc);
		Assert.assertTrue(staffList.size() == 0, "不应该查询出对象");
	}

	@Test(dependsOnMethods = "deleteStaffAndRetrieveByFieldName")
	public void createStaffAndUpdateThenRetrieveByFieldName() throws Exception {
		Shared.printTestMethodStartInfo("SIT1_nbr_SG_StaffList_", order, "新增员工4后，修改员工4的相关信息，再输入姓名或者手机号搜索");

		// 创建员工4
		Staff s4 = BaseStaffTest.DataInput.getStaff();
		MvcResult ret = BaseStaffTest.getToken(sessionBoss, mvc, s4.getPhone(), Staff.FOR_CreateNewStaff);
		//
		String encrypt = Shared.encrypt(ret, Shared.PASSWORD_DEFAULT);
		s4.setRoleID(EnumTypeRole.ETR_Cashier.getIndex());// create.bx时，int1代表角色
		s4.setPwdEncrypted(encrypt);

		Staff staff4 = BaseStaffTest.createStaffViaAction(s4, sessionBoss, mvc, mapBO, dbName);
		// 修改员工4
		staff4.setRoleID(EnumTypeRole.ETR_Cashier.getIndex());
		staff4.setName("泰坦巨猿宝宝");
		Staff staff4Update = BaseStaffTest.updateStaffViaAction(staff4, sessionBoss, mvc, mapBO, dbName);
		staffListMap.put("staff4", staff4Update);

		queryStaffByNameAndphone(staff4Update);
	}

	protected void queryStaffByNameAndphone(Staff staff) throws Exception {
		// 输入员工的名称搜索该员工
		Staff staffQuery = new Staff();
		staffQuery.setQueryKeyword(staff.getName());
		//
		List<BaseModel> staffList = BaseStaffTest.retrieveNViaAction(staffQuery, sessionBoss, mvc);
		//
		boolean isExist = false;
		for (int i = 0; i < staffList.size(); i++) {
			if (((Staff) staffList.get(i)).getName().equals(staff.getName())) {
				isExist = true;
				break;
			}
		}
		Assert.assertTrue(isExist, "查询不到对象");
		// 输入员工的手机号搜索该员工
		staffQuery.setQueryKeyword(staff.getPhone());
		//
		staffList = BaseStaffTest.retrieveNViaAction(staffQuery, sessionBoss, mvc);
		//
		isExist = false;
		for (int i = 0; i < staffList.size(); i++) {
			if (((Staff) staffList.get(i)).getPhone().equals(staff.getPhone())) {
				isExist = true;
				break;
			}
		}
		Assert.assertTrue(isExist, "查询不到对象");
	}

	@Test(dependsOnMethods = "createStaffAndUpdateThenRetrieveByFieldName")
	public void createStaffAndDeleteThenRetrieveByFieldName() throws Exception {
		Shared.printTestMethodStartInfo("SIT1_nbr_SG_StaffList_", order, "新增员工5后，删除员工5，再输入姓名或者手机号搜索");

		// 新增员工5
		Staff s5 = BaseStaffTest.DataInput.getStaff();
		MvcResult ret = BaseStaffTest.getToken(sessionBoss, mvc, s5.getPhone(), Staff.FOR_CreateNewStaff);
		//
		String encrypt = Shared.encrypt(ret, Shared.PASSWORD_DEFAULT);
		s5.setRoleID(EnumTypeRole.ETR_Cashier.getIndex());// create.bx时，int1代表角色
		s5.setPwdEncrypted(encrypt);
		//
		Staff staff5 = BaseStaffTest.createStaffViaAction(s5, sessionBoss, mvc, mapBO, dbName);
		// 删除员工5
		staff5.setReturnSalt(0);
		BaseStaffTest.deleteStaffViaAction(staff5, sessionBoss, mvc, mapBO, dbName);
		// 输入员工的姓名搜索该员工
		Staff staffQuery = new Staff();
		staffQuery.setQueryKeyword(staff5.getName());
		//
		List<BaseModel> staffList = BaseStaffTest.retrieveNViaAction(staffQuery, sessionBoss, mvc);
		Assert.assertTrue(staffList.size() == 0, "不应该查询出对象");
		// 输入员工的手机号搜索该员工
		staffQuery.setQueryKeyword(staff5.getPhone());
		//
		staffList = BaseStaffTest.retrieveNViaAction(staffQuery, sessionBoss, mvc);
		Assert.assertTrue(staffList.size() == 0, "不应该查询出对象");
	}

	@Test(dependsOnMethods = "createStaffAndUpdateThenRetrieveByFieldName")
	public void createStaffAndUpdateThenDeleteAndRetrieveByFieldName() throws Exception {
		Shared.printTestMethodStartInfo("SIT1_nbr_SG_StaffList_", order, "新增员工6后，修改员工6的相关信息，删除员工6，再输入姓名或者手机号搜索");

		// 新增员工6
		Staff s = BaseStaffTest.DataInput.getStaff();
		MvcResult ret = BaseStaffTest.getToken(sessionBoss, mvc, s.getPhone(), Staff.FOR_CreateNewStaff);
		//
		String encrypt = Shared.encrypt(ret, Shared.PASSWORD_DEFAULT);
		s.setRoleID(EnumTypeRole.ETR_Cashier.getIndex());// create.bx时，int1代表角色
		s.setPwdEncrypted(encrypt);
		//
		Staff staff = BaseStaffTest.createStaffViaAction(s, sessionBoss, mvc, mapBO, dbName);
		// 修改员工6
		staff.setRoleID(EnumTypeRole.ETR_Cashier.getIndex());
		staff.setName("吊睛白虎");
		Staff staff6Update = BaseStaffTest.updateStaffViaAction(staff, sessionBoss, mvc, mapBO, dbName);
		// 删除员工6
		staff6Update.setReturnSalt(0);
		BaseStaffTest.deleteStaffViaAction(staff6Update, sessionBoss, mvc, mapBO, dbName);
		// 输入员工的名称搜索该员工
		Staff staffQuery = new Staff();
		staffQuery.setQueryKeyword(staff.getName());
		//
		List<BaseModel> staffList = BaseStaffTest.retrieveNViaAction(staffQuery, sessionBoss, mvc);
		Assert.assertTrue(staffList == null || staffList.size() == 0, "删除掉的员工还能查询出来！");
	}

	@Test(dependsOnMethods = "createStaffAndUpdateThenDeleteAndRetrieveByFieldName")
	public void updateStaffAndDeleteThenRetrieveByFieldName() throws UnsupportedEncodingException, Exception {
		Shared.printTestMethodStartInfo("SIT1_nbr_SG_StaffList_", order, "修改员工4信息，删除员工4，再输入姓名手机号搜搜");

		// 修改员工4
		Staff staff4 = (Staff) staffListMap.get("staff4");
		staff4.setRoleID(EnumTypeRole.ETR_Cashier.getIndex());
		staff4.setName("泰坦巨猿大宝宝");
		Staff staff4Update = BaseStaffTest.updateStaffViaAction(staff4, sessionBoss, mvc, mapBO, dbName);
		// 删除员工4
		staff4Update.setReturnSalt(0);
		BaseStaffTest.deleteStaffViaAction(staff4Update, sessionBoss, mvc, mapBO, dbName);
		//
		Staff staffQuery = new Staff();
		staffQuery.setQueryKeyword(staff4Update.getName());
		//
		List<BaseModel> staffList = BaseStaffTest.retrieveNViaAction(staffQuery, sessionBoss, mvc);
		Assert.assertTrue(staffList.size() == 0, "不应该查询出对象");
	}

	@Test(dependsOnMethods = "updateStaffAndDeleteThenRetrieveByFieldName")
	public void createStaffAndReload() throws Exception {
		Shared.printTestMethodStartInfo("SIT1_nbr_SG_StaffList_", order, "新增员工7后，关闭页面再打开");

		// 新增员工7
		Staff s = BaseStaffTest.DataInput.getStaff();
		MvcResult ret = BaseStaffTest.getToken(sessionBoss, mvc, s.getPhone(), Staff.FOR_CreateNewStaff);
		//
		String encrypt = Shared.encrypt(ret, Shared.PASSWORD_DEFAULT);
		s.setRoleID(EnumTypeRole.ETR_Cashier.getIndex());// create.bx时，int1代表角色
		s.setPwdEncrypted(encrypt);
		//
		Staff staff = BaseStaffTest.createStaffViaAction(s, sessionBoss, mvc, mapBO, dbName);
		staffListMap.put("staff7", staff);

		// ...关闭页面再打开
	}

	@Test(dependsOnMethods = "createStaffAndReload")
	public void updateStaffAndReload() throws UnsupportedEncodingException, Exception {
		Shared.printTestMethodStartInfo("SIT1_nbr_SG_StaffList_", order, "修改员工7信息，关闭页面再打开");

		// 修改员工7
		Staff staff7 = (Staff) staffListMap.get("staff7");
		staff7.setRoleID(EnumTypeRole.ETR_Cashier.getIndex());
		staff7.setName("九尾灵猫");
		Staff staff7Update = BaseStaffTest.updateStaffViaAction(staff7, sessionBoss, mvc, mapBO, dbName);
		staffListMap.replace("staff7", staff7Update);

		// ...重新打开页面
	}

	@Test(dependsOnMethods = "updateStaffAndReload")
	public void deleteStaffAndReload() throws UnsupportedEncodingException, CloneNotSupportedException, Exception {
		Shared.printTestMethodStartInfo("SIT1_nbr_SG_StaffList_", order, "删除员工7后，关闭页面再打开");

		// 删除员工7
		Staff staff7 = (Staff) staffListMap.get("staff7");
		staff7.setReturnSalt(0);
		BaseStaffTest.deleteStaffViaAction(staff7, sessionBoss, mvc, mapBO, dbName);

		// ...重新打开页面
	}

	@Test(dependsOnMethods = "deleteStaffAndReload")
	public void createStaffAndUpdateThenReload() throws Exception {
		Shared.printTestMethodStartInfo("SIT1_nbr_SG_StaffList_", order, "新增员工8后，修改员工8信息，再关闭页面再打开");

		// 新增员工8
		Staff s = BaseStaffTest.DataInput.getStaff();
		MvcResult ret = BaseStaffTest.getToken(sessionBoss, mvc, s.getPhone(), Staff.FOR_CreateNewStaff);
		//
		String encrypt = Shared.encrypt(ret, Shared.PASSWORD_DEFAULT);
		s.setRoleID(EnumTypeRole.ETR_Cashier.getIndex());// create.bx时，int1代表角色
		s.setPwdEncrypted(encrypt);
		//
		Staff staff = BaseStaffTest.createStaffViaAction(s, sessionBoss, mvc, mapBO, dbName);
		//
		// 修改员工8信息
		staff.setRoleID(EnumTypeRole.ETR_Cashier.getIndex());
		staff.setName("千手观音");
		Staff staff8Update = BaseStaffTest.updateStaffViaAction(staff, sessionBoss, mvc, mapBO, dbName);
		staffListMap.put("staff8", staff8Update);
		//
		// 关闭页面再打开
	}

	@Test(dependsOnMethods = "createStaffAndUpdateThenReload")
	public void createStaffAndDeleteThenReload() throws Exception {
		Shared.printTestMethodStartInfo("SIT1_nbr_SG_StaffList_", order, "新增员工9后，删除员工9，再关闭页面再打开");

		// 新增员工9
		Staff s = BaseStaffTest.DataInput.getStaff();
		MvcResult ret = BaseStaffTest.getToken(sessionBoss, mvc, s.getPhone(), Staff.FOR_CreateNewStaff);
		//
		String encrypt = Shared.encrypt(ret, Shared.PASSWORD_DEFAULT);
		s.setRoleID(EnumTypeRole.ETR_Cashier.getIndex());// create.bx时，int1代表角色
		s.setPwdEncrypted(encrypt);
		//
		Staff staff = BaseStaffTest.createStaffViaAction(s, sessionBoss, mvc, mapBO, dbName);
		//
		// 删除员工9
		staff.setReturnSalt(0);
		BaseStaffTest.deleteStaffViaAction(staff, sessionBoss, mvc, mapBO, dbName);
		//
		// ...关闭页面再打开
	}

	@Test(dependsOnMethods = "createStaffAndDeleteThenReload")
	public void updateStaffAndDeleteThenReload() throws UnsupportedEncodingException, Exception {
		Shared.printTestMethodStartInfo("SIT1_nbr_SG_StaffList_", order, "修改员工8信息后，再删除员工8，关闭页面再打开");

		// 修改员工8
		Staff staff8 = (Staff) staffListMap.get("staff8");
		staff8.setRoleID(EnumTypeRole.ETR_Cashier.getIndex());
		staff8.setName("千手修罗龙");
		Staff staff8Update = BaseStaffTest.updateStaffViaAction(staff8, sessionBoss, mvc, mapBO, dbName);
		// 删除员工8
		staff8Update.setReturnSalt(0);
		BaseStaffTest.deleteStaffViaAction(staff8Update, sessionBoss, mvc, mapBO, dbName);

		// ...重新打开页面
	}

	@Test(dependsOnMethods = "updateStaffAndDeleteThenReload")
	public void createStaffAndUpdateThenDeleteAndReload() throws Exception {
		Shared.printTestMethodStartInfo("SIT1_nbr_SG_StaffList_", order, "新增员工10后，再修改员工10信息，再删除该员工10，关闭页面再打开");

		// 新增员工10
		Staff s = BaseStaffTest.DataInput.getStaff();
		MvcResult ret = BaseStaffTest.getToken(sessionBoss, mvc, s.getPhone(), Staff.FOR_CreateNewStaff);
		//
		String encrypt = Shared.encrypt(ret, Shared.PASSWORD_DEFAULT);
		s.setRoleID(EnumTypeRole.ETR_Cashier.getIndex());// create.bx时，int1代表角色
		s.setPwdEncrypted(encrypt);
		//
		Staff staff = BaseStaffTest.createStaffViaAction(s, sessionBoss, mvc, mapBO, dbName);
		// 修改员工10
		staff.setRoleID(EnumTypeRole.ETR_Cashier.getIndex());
		staff.setName("小蜘蛛");
		Staff staff10Update = BaseStaffTest.updateStaffViaAction(staff, sessionBoss, mvc, mapBO, dbName);
		// 删除该员工10
		staff10Update.setReturnSalt(0);
		BaseStaffTest.deleteStaffViaAction(staff10Update, sessionBoss, mvc, mapBO, dbName);

		// ...关闭页面再打开
	}

	@Test(dependsOnMethods = "createStaffAndUpdateThenDeleteAndReload")
	public void createStaffAndDeleteThenUpdateAndDelete() throws Exception {
		Shared.printTestMethodStartInfo("SIT1_nbr_SG_StaffList_", order, "新增员工11后，删除员工11，再修改员工11为在职，最后在删除员工11");

		// 新增员工11
		Staff s = BaseStaffTest.DataInput.getStaff();
		MvcResult ret = BaseStaffTest.getToken(sessionBoss, mvc, s.getPhone(), Staff.FOR_CreateNewStaff);
		//
		String encrypt = Shared.encrypt(ret, Shared.PASSWORD_DEFAULT);
		s.setRoleID(EnumTypeRole.ETR_Cashier.getIndex());// create.bx时，int1代表角色
		s.setPwdEncrypted(encrypt);
		//
		Staff staff = BaseStaffTest.createStaffViaAction(s, sessionBoss, mvc, mapBO, dbName);
		staffListMap.put("staff11", staff);
		// 删除员工11
		staff.setReturnSalt(0);
		BaseStaffTest.deleteStaffViaAction(staff, sessionBoss, mvc, mapBO, dbName);
		// 修改员工11为在职
		staff.setRoleID(EnumTypeRole.ETR_Cashier.getIndex());
		staff.setStatus(EnumStatusStaff.ESS_Incumbent.getIndex());
		Staff staff11Update = BaseStaffTest.updateStaffViaAction(staff, sessionBoss, mvc, mapBO, dbName);
		// 删除员工11
		staff11Update.setReturnSalt(0);
		BaseStaffTest.deleteStaffViaAction(staff11Update, sessionBoss, mvc, mapBO, dbName);
	}

	@Test(dependsOnMethods = "createStaffAndDeleteThenUpdateAndDelete")
	public void createPhoneAStaffAndUpdatePhoneThenCreatePhoneAStaffAgain() throws Exception {
		Shared.printTestMethodStartInfo("SIT1_nbr_SG_StaffList_", order, "新增一个员工12，其手机号为A，再把该员工的手机号改为B，然后在新增一个手机号为A的员工13");

		// 新增一个员工12
		Staff s = BaseStaffTest.DataInput.getStaff();
		MvcResult ret = BaseStaffTest.getToken(sessionBoss, mvc, s.getPhone(), Staff.FOR_CreateNewStaff);
		//
		String encrypt = Shared.encrypt(ret, Shared.PASSWORD_DEFAULT);
		s.setRoleID(EnumTypeRole.ETR_Cashier.getIndex());// create.bx时，int1代表角色
		s.setPwdEncrypted(encrypt);
		//
		Staff staff = BaseStaffTest.createStaffViaAction(s, sessionBoss, mvc, mapBO, dbName);
		// 修改员工12手机号
		staff.setRoleID(EnumTypeRole.ETR_Cashier.getIndex());
		staff.setPhone("18814126911");
		Staff staff12Update = BaseStaffTest.updateStaffViaAction(staff, sessionBoss, mvc, mapBO, dbName);
		staffListMap.put("staff12", staff12Update);
		// 新增员工13（手机号为员工12原手机号）
		Staff s13 = BaseStaffTest.DataInput.getStaff();
		MvcResult ret13 = BaseStaffTest.getToken(sessionBoss, mvc, s13.getPhone(), Staff.FOR_CreateNewStaff);
		//
		String encrypt13 = Shared.encrypt(ret13, Shared.PASSWORD_DEFAULT);
		s13.setRoleID(EnumTypeRole.ETR_Cashier.getIndex());// create.bx时，int1代表角色
		s13.setPwdEncrypted(encrypt13);
		//
		Staff staff13 = BaseStaffTest.createStaffViaAction(s13, sessionBoss, mvc, mapBO, dbName);
		staffListMap.put("staff13", staff13);
	}

	@Test(dependsOnMethods = "createPhoneAStaffAndUpdatePhoneThenCreatePhoneAStaffAgain")
	public void cancelCreateStaff() {
		Shared.printTestMethodStartInfo("SIT1_nbr_SG_StaffList_", order, "新增员工时，点击取消按钮");

		// ...新增员工时，点击取消按钮
	}

	@Test(dependsOnMethods = "cancelCreateStaff")
	public void createStaffThenUpdateAndCancel() throws Exception {
		Shared.printTestMethodStartInfo("SIT1_nbr_SG_StaffList_", order, "新增员工14后，修改员工14信息时，点击取消按钮");

		// 新增员工14
		Staff s = BaseStaffTest.DataInput.getStaff();
		MvcResult ret = BaseStaffTest.getToken(sessionBoss, mvc, s.getPhone(), Staff.FOR_CreateNewStaff);
		//
		String encrypt = Shared.encrypt(ret, Shared.PASSWORD_DEFAULT);
		s.setRoleID(EnumTypeRole.ETR_Cashier.getIndex());// create.bx时，int1代表角色
		s.setPwdEncrypted(encrypt);
		//
		Staff staff = BaseStaffTest.createStaffViaAction(s, sessionBoss, mvc, mapBO, dbName);
		staffListMap.put("staff14", staff);

		// ...修改员工14信息时，点击取消按钮
	}

	@Test(dependsOnMethods = "createStaffThenUpdateAndCancel")
	public void createStaffThenUpdatePassword() throws Exception {
		Shared.printTestMethodStartInfo("SIT1_nbr_SG_StaffList_", order, "新建一个员工15，修改密码");

		// 新建一个员工15
		Staff s = BaseStaffTest.DataInput.getStaff();
		MvcResult ret = BaseStaffTest.getToken(sessionBoss, mvc, s.getPhone(), Staff.FOR_CreateNewStaff);
		//
		String encrypt = Shared.encrypt(ret, Shared.PASSWORD_DEFAULT);
		s.setRoleID(EnumTypeRole.ETR_Cashier.getIndex());// create.bx时，int1代表角色
		s.setPwdEncrypted(encrypt);
		//
		Staff staff = BaseStaffTest.createStaffViaAction(s, sessionBoss, mvc, mapBO, dbName);
		// 修改员工密码
		String staff15Password = "111111";
		Staff staff15Update = BaseStaffTest.resetOtherPasswordExViaAction(staff, sessionBoss, mvc, mapBO, dbName, staff15Password);
		staffListMap.put("staff15", staff15Update);
	}

	@Test(dependsOnMethods = "createStaffThenUpdatePassword")
	public void cancelUpdateStaff() {
		Shared.printTestMethodStartInfo("SIT1_nbr_SG_StaffList_", order, "修改员工15时，点击取消按钮");

		// ...修改员工15时，点击取消按钮
	}

	@Test(dependsOnMethods = "cancelUpdateStaff")
	public void createStaffThenRetrieveByRoleID() throws Exception {
		Shared.printTestMethodStartInfo("SIT1_nbr_SG_StaffList_", order, "新增员工16后，根据员工的角色查询");

		// 新增员工16
		Staff s = BaseStaffTest.DataInput.getStaff();
		MvcResult ret = BaseStaffTest.getToken(sessionBoss, mvc, s.getPhone(), Staff.FOR_CreateNewStaff);
		//
		String encrypt = Shared.encrypt(ret, Shared.PASSWORD_DEFAULT);
		s.setRoleID(EnumTypeRole.ETR_Cashier.getIndex());// create.bx时，int1代表角色
		s.setPwdEncrypted(encrypt);
		//
		Staff staff = BaseStaffTest.createStaffViaAction(s, sessionBoss, mvc, mapBO, dbName);
		staffListMap.put("staff16", staff);
		// 根据角色ID进行查找
		Staff staffQuery = new Staff();
		staffQuery.setRoleID(Integer.valueOf(Role_PreSale));
		//
		List<BaseModel> staffList = BaseStaffTest.retrieveNViaAction(staffQuery, sessionBoss, mvc);
		//
		boolean isExist = false;
		for (int i = 0; i < staffList.size(); i++) {
			if (Integer.valueOf(Role_PreSale) == ((Staff) staffList.get(i)).getRoleID()) {
				isExist = true;
				break;
			}
		}
		Assert.assertTrue(isExist, "查询不到对象");
	}

	@Test(dependsOnMethods = "createStaffThenRetrieveByRoleID")
	public void retrieveLeftStaff() throws Exception {
		Shared.printTestMethodStartInfo("SIT1_nbr_SG_StaffList_", order, "当员工的角色为离职员工(员工11)时，在进行模糊搜索");

		// 模糊搜索员工11
		Staff staff11 = (Staff) staffListMap.get("staff11");
		//
		Staff staffQuery = new Staff();
		staffQuery.setQueryKeyword(staff11.getName());
		//
		List<BaseModel> staffList = BaseStaffTest.retrieveNViaAction(staffQuery, sessionBoss, mvc);
		Assert.assertTrue(staffList.size() == 0, "不应该查询出对象");
	}

	@Test(dependsOnMethods = "retrieveLeftStaff")
	public void updateStaffThenRetrieveByRoleID() throws UnsupportedEncodingException, Exception {
		Shared.printTestMethodStartInfo("SIT1_nbr_SG_StaffList_", order, "修改员工16信息后，根据员工角色查询");
		// 修改员工16
		Staff staff16 = (Staff) staffListMap.get("staff16");
		staff16.setRoleID(EnumTypeRole.ETR_Cashier.getIndex());
		staff16.setName("曼陀罗龙");
		Staff staff16Update = BaseStaffTest.updateStaffViaAction(staff16, sessionBoss, mvc, mapBO, dbName);
		staffListMap.replace("staff16", staff16Update);
		//
		Staff staffQuery = new Staff();
		staffQuery.setRoleID(Integer.valueOf(Role_PreSale));
		//
		List<BaseModel> staffList = BaseStaffTest.retrieveNViaAction(staffQuery, sessionBoss, mvc);
		//
		boolean isExist = false;
		for (int i = 0; i < staffList.size(); i++) {
			if (((Staff) staffList.get(i)).getName().equals(staff16Update.getName())) {
				isExist = true;
				break;
			}
		}
		Assert.assertTrue(isExist, "查询不到对象");
		queryStaffByNameAndphone(staff16Update);
	}

	@Test(dependsOnMethods = "updateStaffThenRetrieveByRoleID")
	public void deleteStaffThenRetrieveByRoleID() throws UnsupportedEncodingException, CloneNotSupportedException, Exception {
		Shared.printTestMethodStartInfo("SIT1_nbr_SG_StaffList_", order, "删除员工16后，根据员工的角色查询");

		// 删除员工16
		Staff staff16 = (Staff) staffListMap.get("staff16");
		staff16.setReturnSalt(0);
		BaseStaffTest.deleteStaffViaAction(staff16, sessionBoss, mvc, mapBO, dbName);
		//
		// 根据员工角色查询
		// 根据角色查询
		MvcResult mr = mvc.perform(//
				get("/staffRole/retrieveNEx.bx?" + StaffRole.field.getFIELD_NAME_roleID() + "=" + Role_PreSale)//
						.contentType(MediaType.APPLICATION_JSON)//
						.session((MockHttpSession) sessionBoss)//
		)//
				.andExpect(status().isOk())//
				.andDo(print())//
				.andReturn();
		Shared.checkJSONErrorCode(mr);
		JSONObject jsonObject = JSONObject.fromObject(mr.getResponse().getContentAsString());
		List<String> nameList16 = JsonPath.read(jsonObject, "$.objectList[*].name");
		for (int i = 0; i < nameList16.size(); i++) {
			if (nameList16.get(i).equals(staff16.getName())) {
				Assert.assertTrue(false, "不应该查询到对象");
			}
		}
	}

	@Test(dependsOnMethods = "deleteStaffThenRetrieveByRoleID")
	public void createPhoneAStaffAndDeleteThenCreatePhoneAStaffAgain() throws Exception {
		Shared.printTestMethodStartInfo("SIT1_nbr_SG_StaffList_", order, "新增员工17手机号为A，让员工17处于离职状态，再新增员工18，其手机号为A");

		// 新增员工17
		Staff s = BaseStaffTest.DataInput.getStaff();
		MvcResult ret = BaseStaffTest.getToken(sessionBoss, mvc, s.getPhone(), Staff.FOR_CreateNewStaff);
		//
		String encrypt = Shared.encrypt(ret, Shared.PASSWORD_DEFAULT);
		s.setRoleID(EnumTypeRole.ETR_Cashier.getIndex());// create.bx时，int1代表角色
		s.setPwdEncrypted(encrypt);
		//
		Staff staff = BaseStaffTest.createStaffViaAction(s, sessionBoss, mvc, mapBO, dbName);
		//
		// 让员工17处于离职状态
		staff.setReturnSalt(0);
		BaseStaffTest.deleteStaffViaAction(staff, sessionBoss, mvc, mapBO, dbName);
		//
		// 新增员工18（手机号与员工17相同）
		Staff s18 = BaseStaffTest.DataInput.getStaff();
		s18.setPhone(staff.getPhone());
		MvcResult ret18 = BaseStaffTest.getToken(sessionBoss, mvc, s18.getPhone(), Staff.FOR_CreateNewStaff);
		//
		String encrypt18 = Shared.encrypt(ret18, Shared.PASSWORD_DEFAULT);
		s18.setRoleID(EnumTypeRole.ETR_Cashier.getIndex());// create.bx时，int1代表角色
		s18.setPwdEncrypted(encrypt18);
		//
		Staff staff18 = BaseStaffTest.createStaffViaAction(s18, sessionBoss, mvc, mapBO, dbName);
		staffListMap.put("staff18", staff18);
	}

	@Test(dependsOnMethods = "createPhoneAStaffAndDeleteThenCreatePhoneAStaffAgain")
	public void createStaffAndUpdateTheLeftStaff() throws Exception {
		Shared.printTestMethodStartInfo("SIT1_nbr_SG_StaffList_", order, "新增一个员工19，手机号码和某一个离职员工16一样，然后再将离职员工16转为在职");

		// 新增一个员工19(手机号等于离职员工16的手机号)
		Staff staff16 = (Staff) staffListMap.get("staff16");
		Staff s = BaseStaffTest.DataInput.getStaff();
		s.setPhone(staff16.getPhone());
		MvcResult ret = BaseStaffTest.getToken(sessionBoss, mvc, s.getPhone(), Staff.FOR_CreateNewStaff);
		//
		String encrypt = Shared.encrypt(ret, Shared.PASSWORD_DEFAULT);
		s.setRoleID(EnumTypeRole.ETR_Cashier.getIndex());// create.bx时，int1代表角色
		s.setPwdEncrypted(encrypt);
		//
		Staff staff = BaseStaffTest.createStaffViaAction(s, sessionBoss, mvc, mapBO, dbName);
		staffListMap.put("staff19", staff);
		//
		// 将离职员工16转为在职
		staff16.setRoleID(EnumTypeRole.ETR_Cashier.getIndex());
		staff16.setStatus(EnumStatusStaff.ESS_Incumbent.getIndex());
		MvcResult mr = mvc.perform(//
				BaseStaffTest.DataInput.getBuilder("/staff/updateEx.bx", MediaType.APPLICATION_JSON, staff16)//
						.session((MockHttpSession) sessionBoss)//
		)//
				.andExpect(status().isOk())//
				.andDo(print())//
				.andReturn();//
		// 结果验证:检查错误码
		Shared.checkJSONErrorCode(mr, EnumErrorCode.EC_BusinessLogicNotDefined);
	}

	@Test(dependsOnMethods = "createStaffAndUpdateTheLeftStaff")
	public void deleteAllStaffAfterPagination() throws Exception {
		Shared.printTestMethodStartInfo("SIT1_nbr_SG_StaffList_", order, "进行员工分页后，删除分页后所有的员工");

		// 拿到登录session的员工
		Staff staffLogin = getStaffFromSession(sessionBoss);
		// 查询所有员工
		MvcResult mr = mvc.perform(//
				post("/staff/retrieveNEx.bx")//
						.param(Staff.field.getFIELD_NAME_status(), String.valueOf(EnumStatusStaff.ESS_Incumbent.getIndex()))//
						.contentType(MediaType.APPLICATION_JSON)//
						.session((MockHttpSession) sessionBoss)//
		)//
				.andExpect(status().isOk())//
				.andDo(print())//
				.andReturn();
		Shared.checkJSONErrorCode(mr);
		JSONObject jsonObject = JSONObject.fromObject(mr.getResponse().getContentAsString());
		List<?> staffList = JsonPath.read(jsonObject, "$.objectList[*]");
		Staff staff = new Staff();
		// 删除所有员工
		for (int i = 0; i < staffList.size(); i++) {
			staff = (Staff) staff.parse1(staffList.get(i).toString());
			if (staff.getID() != staffLogin.getID() && checkStaffDependency(staff)) {
				staff.setReturnSalt(0);
				BaseStaffTest.deleteStaffViaAction(staff, sessionBoss, mvc, mapBO, dbName);
			}
		}

	}

	@Test(dependsOnMethods = "deleteAllStaffAfterPagination")
	public void useLocalComputerCURDStaffListThenUseOtherComputerLoginAndRetrieve() {
		Shared.printTestMethodStartInfo("SIT1_nbr_SG_StaffList_", order, "在本机进行CRUD操作后，在新机登录nbr系统进行查询，查看原机操作的内容是否存在");

	}

	@Test(dependsOnMethods = "useLocalComputerCURDStaffListThenUseOtherComputerLoginAndRetrieve")
	public void useTwoOrMoreComputerLoginAtTheSameTimeAndCURDStaffList() {
		Shared.printTestMethodStartInfo("SIT1_nbr_SG_StaffList_", order, "在两台机器或者两台以上机器同时登录nbr进行CRUD操作（有可能操作了同一个商品），测试功能是否都通过");

	}

	@Test(dependsOnMethods = "useTwoOrMoreComputerLoginAtTheSameTimeAndCURDStaffList")
	public void createStaffAndDeleteThenRetrieveByRoleList() throws Exception {
		Shared.printTestMethodStartInfo("SIT1_nbr_SG_StaffList_", order, "新增员工后，删除员工，查看离职员工列表,查看收银员列表,查看店长列表，查看全部在职员工列表");

		// 创建员工20
		Staff s = BaseStaffTest.DataInput.getStaff();
		MvcResult ret = BaseStaffTest.getToken(sessionBoss, mvc, s.getPhone(), Staff.FOR_CreateNewStaff);
		//
		String encrypt = Shared.encrypt(ret, Shared.PASSWORD_DEFAULT);
		s.setRoleID(EnumTypeRole.ETR_Cashier.getIndex());// create.bx时，int1代表角色
		s.setPwdEncrypted(encrypt);
		//
		Staff staff = BaseStaffTest.createStaffViaAction(s, sessionBoss, mvc, mapBO, dbName);
		// 删除员工20
		BaseStaffTest.deleteStaffViaAction(staff, sessionBoss, mvc, mapBO, dbName);
		// 查看离职员工是否有员工20
		MvcResult mr = mvc.perform(//
				post("/staff/retrieveNEx.bx")//
						.param(Staff.field.getFIELD_NAME_status(), String.valueOf(Staff.INVOLVE_RESIGNED))//
						.contentType(MediaType.APPLICATION_JSON)//
						.session((MockHttpSession) sessionBoss)//
		)//
				.andExpect(status().isOk())//
				.andDo(print())//
				.andReturn();
		Shared.checkJSONErrorCode(mr);
		JSONObject jsonObject = JSONObject.fromObject(mr.getResponse().getContentAsString());
		List<String> nameList = JsonPath.read(jsonObject, "$.objectList[*].name");
		Assert.assertTrue(nameList.contains(staff.getName()), "查询不到对象");
		// 查询收银员是否有员工20
		MvcResult mr2 = mvc.perform(//
				get("/staffRole/retrieveNEx.bx?" + StaffRole.field.getFIELD_NAME_roleID() + "=" + Role_PreSale)//
						.contentType(MediaType.APPLICATION_JSON)//
						.session((MockHttpSession) sessionBoss)//
		)//
				.andExpect(status().isOk())//
				.andDo(print())//
				.andReturn();
		Shared.checkJSONErrorCode(mr2);
		JSONObject jsonObject2 = JSONObject.fromObject(mr2.getResponse().getContentAsString());
		nameList = JsonPath.read(jsonObject2, "$.objectList[*].name");
		Assert.assertFalse(nameList.contains(staff.getName()), "居然查询到对象！");
		// 查询店长是否有员工20
		MvcResult mr3 = mvc.perform(//
				get("/staffRole/retrieveNEx.bx?" + StaffRole.field.getFIELD_NAME_roleID() + "=" + Role_Boss)//
						.contentType(MediaType.APPLICATION_JSON)//
						.session((MockHttpSession) sessionBoss)//
		)//
				.andExpect(status().isOk())//
				.andDo(print())//
				.andReturn();
		Shared.checkJSONErrorCode(mr3);
		JSONObject jsonObject3 = JSONObject.fromObject(mr3.getResponse().getContentAsString());
		nameList = JsonPath.read(jsonObject3, "$.objectList[*].name");
		Assert.assertFalse(nameList.contains(staff.getName()), "居然查询到对象！");
		// 查询全部在职员工是否有员工20
		MvcResult mr4 = mvc.perform(//
				post("/staff/retrieveNEx.bx")//
						.param(Staff.field.getFIELD_NAME_status(), String.valueOf(Staff.NOT_INVOLVE_RESIGNED))//
						.contentType(MediaType.APPLICATION_JSON)//
						.session((MockHttpSession) sessionBoss)//
		)//
				.andExpect(status().isOk())//
				.andDo(print())//
				.andReturn();
		Shared.checkJSONErrorCode(mr4);
		JSONObject jsonObject4 = JSONObject.fromObject(mr4.getResponse().getContentAsString());
		nameList = JsonPath.read(jsonObject4, "$.objectList[*].name");
		Assert.assertFalse(nameList.contains(staff.getName()), "居然查询到对象！");
	}

	private boolean checkStaffDependency(Staff staff) throws Exception {

		// 查看仓库
		MvcResult mr1 = mvc.perform(//
				post("/warehouse/retrieveNEx.bx") //
						.contentType(MediaType.APPLICATION_JSON) //
						.session((MockHttpSession) sessionBoss)//
		) //
				.andExpect(status().isOk())//
				.andDo(print())//
				.andReturn();//

		Shared.checkJSONErrorCode(mr1);
		JSONObject jsonObject1 = JSONObject.fromObject(mr1.getResponse().getContentAsString());
		List<Integer> warehouseStaffIDList = JsonPath.read(jsonObject1, "$.warehouseList[*].staffID");
		for (int i = 0; i < warehouseStaffIDList.size(); i++) {
			if (staff.getID() == warehouseStaffIDList.get(i)) {
				return false;
			}
		}
		// 查看采购
		MvcResult mr2 = mvc.perform(//
				post("/purchasingOrder/retrieveNByFieldsEx.bx") //
						.param("sValue", "")//
						.param(PurchasingOrder.field.getFIELD_NAME_staffID(), String.valueOf(staff.getID()))//
						.contentType(MediaType.APPLICATION_JSON)//
						.session((MockHttpSession) sessionBoss) //
		).andExpect(status().isOk())//
				.andDo(print())//
				.andReturn();
		Shared.checkJSONErrorCode(mr2);
		JSONObject jsonObject2 = JSONObject.fromObject(mr2.getResponse().getContentAsString()); //
		List<?> purchasingOrderList = JsonPath.read(jsonObject2, "$.purchasingOrderList[*]");
		if (purchasingOrderList.size() != 0) {
			return false;
		}
		// 查看入库
		MvcResult mr3 = mvc.perform( //
				get("/warehousing/retrieveNEx.bx?") //
						.contentType(MediaType.APPLICATION_JSON) //
						.session((MockHttpSession) sessionBoss)) //
				.andExpect(status().isOk()) //
				.andDo(print()) //
				.andReturn(); //
		Shared.checkJSONErrorCode(mr3);
		JSONObject jsonObject3 = JSONObject.fromObject(mr3.getResponse().getContentAsString());
		List<Integer> warehousingStaffIDList = JsonPath.read(jsonObject3, "$.warehousingList[*].staffID");
		for (int i = 0; i < warehousingStaffIDList.size(); i++) {
			if (staff.getID() == warehousingStaffIDList.get(i)) {
				return false;
			}
		}
		// 查看退货
		MvcResult mr4 = mvc.perform(post("/returnCommoditySheet/retrieveNEx.bx") //
				.param(ReturnCommoditySheet.field.getFIELD_NAME_status(), String.valueOf(BaseAction.INVALID_STATUS)) //
				.param(ReturnCommoditySheet.field.getFIELD_NAME_staffID(), String.valueOf(staff.getID())) //
				.contentType(MediaType.APPLICATION_JSON) //
				.session((MockHttpSession) sessionBoss)//
		).andExpect(status().isOk()).andDo(print()).andReturn();
		Shared.checkJSONErrorCode(mr4);

		JSONObject jsonObject4 = JSONObject.fromObject(mr4.getResponse().getContentAsString());
		List<Integer> returnCommodityStaffIDList = JsonPath.read(jsonObject4, "$.objectList[*].staffID");
		for (int i = 0; i < returnCommodityStaffIDList.size(); i++) {
			if (staff.getID() == returnCommodityStaffIDList.get(i)) {
				return false;
			}
		}
		// 查看盘点
		MvcResult mr5 = mvc.perform(post("/inventorySheet/retrieveNEx.bx") //
				.param(ReturnCommoditySheet.field.getFIELD_NAME_status(), String.valueOf(BaseAction.INVALID_STATUS)) //
				.contentType(MediaType.APPLICATION_JSON) //
				.session((MockHttpSession) sessionBoss)//
		).andExpect(status().isOk()).andDo(print()).andReturn();
		Shared.checkJSONErrorCode(mr5);
		JSONObject jsonObject5 = JSONObject.fromObject(mr5.getResponse().getContentAsString());
		List<Integer> inventorySheetStaffIDList = JsonPath.read(jsonObject5, "$.objectList[*].staffID");
		for (int i = 0; i < inventorySheetStaffIDList.size(); i++) {
			if (staff.getID() == inventorySheetStaffIDList.get(i)) {
				return false;
			}
		}
		// 查看零售
		MvcResult mr6 = mvc.perform(//
				post("/retailTrade/retrieveNEx.bx")//
						.session((MockHttpSession) sessionBoss)//
						.param(RetailTrade.field.getFIELD_NAME_queryKeyword(), "") //
						.contentType(MediaType.APPLICATION_JSON)//
		)//
				.andExpect(status().isOk()).andDo(print()).andReturn();
		//
		Shared.checkJSONErrorCode(mr6);

		JSONObject jsonObject6 = JSONObject.fromObject(mr6.getResponse().getContentAsString());
		List<Integer> retailTradeStaffIDList = JsonPath.read(jsonObject6, "$.objectList[*].staffID");
		for (int i = 0; i < retailTradeStaffIDList.size(); i++) {
			if (staff.getID() == retailTradeStaffIDList.get(i)) {
				return false;
			}
		}
		// 查看促销
		MvcResult mr7 = mvc.perform(//
				post("/promotion/retrieveNEx.bx")//
						.param(Promotion.field.getFIELD_NAME_subStatusOfStatus(), "0")//
						.param(Promotion.field.getFIELD_NAME_status(), "-1")//
						.contentType(MediaType.APPLICATION_JSON)//
						.session((MockHttpSession) sessionBoss)//
		)//
				.andExpect(status().isOk())//
				.andDo(print()).andReturn();
		Shared.checkJSONErrorCode(mr7);
		JSONObject jsonObject7 = JSONObject.fromObject(mr7.getResponse().getContentAsString());
		List<Integer> promotionStaffList = JsonPath.read(jsonObject7, "$.objectList[*].staffID");
		for (int i = 0; i < promotionStaffList.size(); i++) {
			if (staff.getID() == promotionStaffList.get(i)) {
				return false;
			}
		}
		// 查看商品历史
		MvcResult mr8 = mvc.perform(//
				post("/commodityHistory/retrieveNEx.bx")//
						.param(CommodityHistory.field.getFIELD_NAME_staffID(), String.valueOf(staff.getID()))//
						.param(CommodityHistory.field.getFIELD_NAME_commodityID(), String.valueOf(BaseAction.INVALID_ID)) //
						.session((MockHttpSession) Shared.getStaffLoginSession(mvc, Shared.PhoneOfAssistManager))//
						.contentType(MediaType.APPLICATION_JSON)//
		)//
				.andExpect(status().isOk())//
				.andDo(print())//
				.andReturn();
		//
		Shared.checkJSONErrorCode(mr8);
		JSONObject jsonObject8 = JSONObject.fromObject(mr8.getResponse().getContentAsString());
		List<Integer> commodityHistoryStaffIDList = JsonPath.read(jsonObject8, "$.objectList[*].staffID");
		for (int i = 0; i < commodityHistoryStaffIDList.size(); i++) {
			if (staff.getID() == commodityHistoryStaffIDList.get(i)) {
				return false;
			}
		}
		return true;
	}

	protected Staff getStaffFromSession(HttpSession session) {
		Staff staff = (Staff) session.getAttribute(EnumSession.SESSION_Staff.getName());
		assert staff != null;
		logger.info("当前使用的staff=" + staff);

		return staff;
	}

	@BeforeClass
	public void beforeClass() throws Exception {
		super.setUp();

		order = new AtomicInteger();
		staffListMap = new HashMap<String, BaseModel>();

	}

	@AfterClass
	public void afterClass() {
		super.tearDown();
	}

}
