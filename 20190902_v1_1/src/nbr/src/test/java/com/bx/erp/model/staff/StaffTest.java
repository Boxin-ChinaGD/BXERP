package com.bx.erp.model.staff;

import java.text.ParseException;

import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import com.bx.erp.action.BaseAction;
import com.bx.erp.action.bo.BaseBO;
import com.bx.erp.model.BaseModel.EnumBoolean;
import com.bx.erp.model.Role.EnumTypeRole;
import com.bx.erp.model.Staff;
import com.bx.erp.test.BaseTestNGSpringContextTest;
import com.bx.erp.test.Shared;
import com.bx.erp.util.FieldFormat;

public class StaffTest extends BaseTestNGSpringContextTest {

	@BeforeClass
	public void setUp() {
		Shared.printTestClassStartInfo();
	}

	@AfterClass
	public void tearDown() {
		Shared.printTestClassEndInfo();
	}

	@Test
	public void checkCreate() throws ParseException {
		Shared.printTestMethodStartInfo();

		// SimpleDateFormat sdf4 = new
		// SimpleDateFormat(BaseAction.DATETIME_FORMAT_Default4);
		// SimpleDateFormat sdf = new
		// SimpleDateFormat(BaseAction.DATETIME_FORMAT_Default_Chinese);
		Staff s = new Staff();
		s.setPhone("12345678910");
		s.setName("收银员");
		s.setICID("44152219940925821X");
		s.setWeChat("weixin");
		// s.setPasswordExpireDate(sdf4.parse("2019/12/30 23:59:59"));
		s.setSalt("12345678901234567890123456789012");
		s.setIsFirstTimeLogin(1);
		s.setShopID(1);
		s.setDepartmentID(1);
		s.setRoleID(1);
		s.setStatus(0);
		s.setReturnSalt(Staff.RETURN_SALT);
		s.setNewPassword("123456");
		String err = s.checkCreate(BaseBO.INVALID_CASE_ID);
		Assert.assertEquals(err, "");
		//
		Shared.caseLog("测试phone");
		s.setPhone("12345678910aa");
		err = s.checkCreate(BaseBO.INVALID_CASE_ID);
		Assert.assertEquals(err, Staff.FIELD_ERROR_phone);
		s.setPhone("");
		err = s.checkCreate(BaseBO.INVALID_CASE_ID);
		Assert.assertEquals(err, Staff.FIELD_ERROR_phone);
		s.setPhone("12345678910");
		//
		Shared.caseLog("测试name");
		s.setName("ABCD");
		err = s.checkCreate(BaseBO.INVALID_CASE_ID);
		Assert.assertEquals(err, "");
		s.setName("123465");
		err = s.checkCreate(BaseBO.INVALID_CASE_ID);
		Assert.assertEquals(err, Staff.FIELD_ERROR_name);
		s.setName("@#$!@#$");
		err = s.checkCreate(BaseBO.INVALID_CASE_ID);
		Assert.assertEquals(err, Staff.FIELD_ERROR_name);
		s.setName("收银员");
		//
		Shared.caseLog("测试ICID");
		s.setICID("44152219940925821Xaa");
		err = s.checkCreate(BaseBO.INVALID_CASE_ID);
		Assert.assertEquals(err, Staff.FIELD_ERROR_ICID);
		s.setICID("44152219940925821X");
		//
		Shared.caseLog("测试WeChat");
		s.setWeChat("@%@%$$%");
		err = s.checkCreate(BaseBO.INVALID_CASE_ID);
		Assert.assertEquals(err, Staff.FIELD_ERROR_weChat);
		s.setWeChat("abcde1234567890123456");
		err = s.checkCreate(BaseBO.INVALID_CASE_ID);
		Assert.assertEquals(err, Staff.FIELD_ERROR_weChat);
		s.setWeChat("weixin");
		//
		// Shared.caseLog("测试密码有效期");
		// s.setPasswordExpireDate(sdf.parse("2019年12月30日 23:59:59"));
		// err = s.checkCreate(BaseBO.INVALID_CASE_ID);
		// Assert.assertEquals(err, "");
		// s.setPasswordExpireDate(null);
		// err = s.checkCreate(BaseBO.INVALID_CASE_ID);
		// Assert.assertEquals(err, "");
		//
		Shared.caseLog("测试盐值");
		s.setSalt("1234567890123456789012345678901234567890");
		err = s.checkCreate(BaseBO.INVALID_CASE_ID);
		Assert.assertEquals(err, Staff.FIELD_ERROR_salt);
		s.setSalt("12345678901234567890123456789012");
		//
		Shared.caseLog("测试是否首次登陆");
		s.setIsFirstTimeLogin(2);
		err = s.checkCreate(BaseBO.INVALID_CASE_ID);
		Assert.assertEquals(err, Staff.FIELD_ERROR_isFirstTimeLogin);
		s.setIsFirstTimeLogin(1);
		//
		Shared.caseLog("测试门店ID");
		s.setShopID(BaseAction.INVALID_ID);
		err = s.checkCreate(BaseBO.INVALID_CASE_ID);
		Assert.assertEquals(err, Staff.FIELD_ERROR_shopID);
		s.setShopID(0);
		err = s.checkCreate(BaseBO.INVALID_CASE_ID);
		Assert.assertEquals(err, Staff.FIELD_ERROR_shopID);
		s.setShopID(1);
		//
		Shared.caseLog("测试部门ID");
		s.setDepartmentID(BaseAction.INVALID_ID);
		err = s.checkCreate(BaseBO.INVALID_CASE_ID);
		Assert.assertEquals(err, Staff.FIELD_ERROR_departmentID);
		s.setDepartmentID(0);
		err = s.checkCreate(BaseBO.INVALID_CASE_ID);
		Assert.assertEquals(err, Staff.FIELD_ERROR_departmentID);
		s.setDepartmentID(1);
		//
		Shared.caseLog("测试角色ID");
//		s.setRoleID(EnumTypeRole.ETR_Manager.getIndex());
//		err = s.checkCreate(BaseBO.INVALID_CASE_ID);
//		Assert.assertEquals(err, Staff.FIELD_ERROR_roleID);
//		s.setRoleID(EnumTypeRole.ETR_Assistant.getIndex());
//		err = s.checkCreate(BaseBO.INVALID_CASE_ID);
//		Assert.assertEquals(err, Staff.FIELD_ERROR_roleID);
//		s.setRoleID(EnumTypeRole.ETR_CommercialManager.getIndex());
//		err = s.checkCreate(BaseBO.INVALID_CASE_ID);
//		Assert.assertEquals(err, Staff.FIELD_ERROR_roleID);
		s.setRoleID(EnumTypeRole.ETR_Cashier.getIndex());
		err = s.checkCreate(BaseBO.INVALID_CASE_ID);
		Assert.assertEquals(err, "");
		s.setRoleID(EnumTypeRole.ETR_Boss.getIndex());
		err = s.checkCreate(BaseBO.INVALID_CASE_ID);
		Assert.assertEquals(err, "");
		s.setRoleID(EnumTypeRole.ETR_PreSale.getIndex());
		err = s.checkCreate(BaseBO.INVALID_CASE_ID);
		Assert.assertEquals(err, "");
		//
		Shared.caseLog("测试状态码");
		s.setStatus(BaseAction.INVALID_STATUS);
		err = s.checkCreate(BaseBO.INVALID_CASE_ID);
		Assert.assertEquals(err, Staff.FIELD_ERROR_status);
		s.setStatus(0);
		//
		Shared.caseLog("测试是否返回盐值");
		s.setReturnSalt(222);
		err = s.checkCreate(BaseBO.INVALID_CASE_ID);
		Assert.assertEquals(err, Staff.FIELD_ERROR_returnSalt);
		s.setReturnSalt(Staff.RETURN_SALT);
	}

	@Test
	public void checkUpdate_CASE1() throws ParseException {
		Shared.printTestMethodStartInfo();

		Staff s = new Staff();
		s.setPhone("12345678910");
		s.setIsFirstTimeLogin(1);
		s.setReturnSalt(1);
		s.setUnionid("123");
		s.setOpenid("123");
		String err = s.checkUpdate(BaseBO.CASE_Staff_Update_OpenidAndUnionid);
		Assert.assertEquals(err, "");
		//
		Shared.caseLog("测试phone");
		s.setPhone("123456789102");
		err = s.checkUpdate(BaseBO.CASE_Staff_Update_OpenidAndUnionid);
		Assert.assertEquals(err, Staff.FIELD_ERROR_phone);
		s.setPhone("12345678910");
		//
		Shared.caseLog("测试是否返回盐值");
		s.setReturnSalt(2);
		err = s.checkUpdate(BaseBO.CASE_Staff_Update_OpenidAndUnionid);
		Assert.assertEquals(err, Staff.FIELD_ERROR_returnSalt);
		s.setReturnSalt(1);
		//
		Shared.caseLog("测试Unionid");
		s.setUnionid("123456790123456790123456790123456790123456790123456790123456790123456790123456790123456790" + "123456790123456790123456790123456790123456790123456790123456790123456790123456790123456790");
		err = s.checkUpdate(BaseBO.CASE_Staff_Update_OpenidAndUnionid);
		Assert.assertEquals(err, Staff.FIELD_ERROR_unionid);
		s.setUnionid("123");
		//
		Shared.caseLog("测试Openid");
		s.setOpenid("123456790123456790123456790123456790123456790123456790123456790123456790123456790123456790" + "123456790123456790123456790123456790123456790123456790123456790123456790123456790123456790");
		err = s.checkUpdate(BaseBO.CASE_Staff_Update_OpenidAndUnionid);
		Assert.assertEquals(err, Staff.FIELD_ERROR_openid);
		s.setOpenid("123");
	}

	@Test
	public void checkUpdate_CASE2() throws ParseException {
		Shared.printTestMethodStartInfo();

		Staff s = new Staff();
		s.setPhone("12345678910");
		s.setIsFirstTimeLogin(1);
		s.setReturnSalt(Staff.RETURN_SALT);
		String err = s.checkUpdate(BaseBO.CASE_ResetOtherPassword);
		Assert.assertEquals(err, "");
		//
		Shared.caseLog("测试phone");
		s.setPhone("123456789102");
		err = s.checkUpdate(BaseBO.CASE_ResetOtherPassword);
		Assert.assertEquals(err, Staff.FIELD_ERROR_phone);
		s.setPhone("12345678910");
		//
		Shared.caseLog("测试是否第一次登陆");
		s.setIsFirstTimeLogin(0);
		err = s.checkUpdate(BaseBO.CASE_ResetOtherPassword);
		Assert.assertEquals(err, Staff.FIELD_ERROR_isFirstTimeLogin);
		s.setIsFirstTimeLogin(1);
		//
		Shared.caseLog("测试是否返回盐值");
		s.setReturnSalt(2);
		err = s.checkUpdate(BaseBO.CASE_ResetOtherPassword);
		Assert.assertEquals(err, Staff.FIELD_ERROR_returnSalt);
		s.setReturnSalt(Staff.NOT_RETURN_SALT);
	}

	@Test
	public void checkUpdate_CASE3() throws ParseException {
		Shared.printTestMethodStartInfo();

		Staff s = new Staff();
		s.setPhone("12345678910");
		s.setOldMD5("123456");
		s.setNewMD5("654321");
		s.setIsFirstTimeLogin(0);
		s.setReturnSalt(1);
		s.setNewPassword("654321");
		s.setOldPassword("123456");
		s.setIsResetOtherPassword(0);
		String err = s.checkUpdate(BaseBO.CASE_ResetMyPassword);
		Assert.assertEquals(err, "");
		//
		Shared.caseLog("测试phone");
		s.setPhone("123456789102");
		err = s.checkUpdate(BaseBO.CASE_ResetMyPassword);
		Assert.assertEquals(err, Staff.FIELD_ERROR_phone);
		s.setPhone("12345678910");
		//
		Shared.caseLog("测试oldMD5");
		s.setOldMD5(s.getNewMD5());
		err = s.checkUpdate(BaseBO.CASE_ResetMyPassword);
		Assert.assertEquals(err, Staff.FIELD_ERROR_samePassword);
		s.setOldMD5("123456");
		//
		Shared.caseLog("测试newMD5");
		s.setNewMD5(s.getOldMD5());
		err = s.checkUpdate(BaseBO.CASE_ResetMyPassword);
		Assert.assertEquals(err, Staff.FIELD_ERROR_samePassword);
		s.setNewMD5("654321");
		//
		Shared.caseLog("测试是否第一次登陆");
		s.setIsFirstTimeLogin(1);
		err = s.checkUpdate(BaseBO.CASE_ResetMyPassword);
		Assert.assertEquals(err, Staff.FIELD_ERROR_isFirstTimeLogin);
		s.setIsFirstTimeLogin(0);
		//
		Shared.caseLog("测试是否返回盐值");
		s.setReturnSalt(2);
		err = s.checkUpdate(BaseBO.CASE_ResetMyPassword);
		Assert.assertEquals(err, Staff.FIELD_ERROR_returnSalt);
		s.setReturnSalt(1);
	}

	@Test
	public void checkUpdate_CASE4() throws ParseException {
		Shared.printTestMethodStartInfo();

		// SimpleDateFormat sdf4 = new
		// SimpleDateFormat(BaseAction.DATETIME_FORMAT_Default4);
		// SimpleDateFormat sdf = new
		// SimpleDateFormat(BaseAction.DATETIME_FORMAT_Default_Chinese);
		Staff s = new Staff();
		s.setID(1);
		s.setPhone("12345678910");
		s.setName("收银员");
		s.setICID("44152219940925821X");
		s.setWeChat("weixin");
		// s.setPasswordExpireDate(sdf4.parse("2019/12/30 23:59:59"));
		s.setShopID(1);
		s.setIsLoginFromPos(Staff.RETURN_SALT);
		s.setDepartmentID(1);
		s.setStatus(0);
		s.setRoleID(1);
		String err = s.checkUpdate(BaseBO.INVALID_CASE_ID);
		Assert.assertEquals(err, "");
		//
		Shared.caseLog("测试ID");
		s.setID(BaseAction.INVALID_ID);
		err = s.checkUpdate(BaseBO.INVALID_CASE_ID);
		Assert.assertEquals(err, FieldFormat.FIELD_ERROR_ID);
		s.setID(1);
		//
		Shared.caseLog("测试phone");
		s.setPhone("12345678910aa");
		err = s.checkUpdate(BaseBO.INVALID_CASE_ID);
		Assert.assertEquals(err, Staff.FIELD_ERROR_phone);
		s.setPhone("");
		err = s.checkUpdate(BaseBO.INVALID_CASE_ID);
		Assert.assertEquals(err, Staff.FIELD_ERROR_phone);
		s.setPhone("12345678910");
		//
		Shared.caseLog("测试name");
		s.setName("ABCD");
		err = s.checkUpdate(BaseBO.INVALID_CASE_ID);
		Assert.assertEquals(err, "");
		s.setName("123456789");
		err = s.checkUpdate(BaseBO.INVALID_CASE_ID);
		Assert.assertEquals(err, Staff.FIELD_ERROR_name);
		s.setName("@#$!@#$");
		err = s.checkUpdate(BaseBO.INVALID_CASE_ID);
		Assert.assertEquals(err, Staff.FIELD_ERROR_name);
		s.setName("收银员");
		//
		Shared.caseLog("测试ICID");
		s.setICID("44152219940925821Xaa");
		err = s.checkUpdate(BaseBO.INVALID_CASE_ID);
		Assert.assertEquals(err, Staff.FIELD_ERROR_ICID);
		s.setICID("44152219940925821X");
		//
		Shared.caseLog("测试WeChat");
		s.setWeChat("@%@%$$%");
		err = s.checkUpdate(BaseBO.INVALID_CASE_ID);
		Assert.assertEquals(err, Staff.FIELD_ERROR_weChat);
		s.setWeChat("abcde1234567890123456");
		err = s.checkUpdate(BaseBO.INVALID_CASE_ID);
		Assert.assertEquals(err, Staff.FIELD_ERROR_weChat);
		s.setWeChat("weixin");
		//
		Shared.caseLog("测试密码有效期");
		// s.setPasswordExpireDate(sdf.parse("2019年12月30日 23:59:59"));
		// err = s.checkUpdate(BaseBO.INVALID_CASE_ID);
		// Assert.assertEquals(err, "");
		// s.setPasswordExpireDate(null);
		// err = s.checkUpdate(BaseBO.INVALID_CASE_ID);
		// Assert.assertEquals(err, "");
		//
		Shared.caseLog("测试门店ID");
		s.setShopID(BaseAction.INVALID_ID);
		err = s.checkUpdate(BaseBO.INVALID_CASE_ID);
		Assert.assertEquals(err, Staff.FIELD_ERROR_shopID);
		s.setShopID(0);
		err = s.checkUpdate(BaseBO.INVALID_CASE_ID);
		Assert.assertEquals(err, Staff.FIELD_ERROR_shopID);
		s.setShopID(1);
		//
		Shared.caseLog("测试部门ID");
		s.setDepartmentID(BaseAction.INVALID_ID);
		err = s.checkUpdate(BaseBO.INVALID_CASE_ID);
		Assert.assertEquals(err, Staff.FIELD_ERROR_departmentID);
		s.setDepartmentID(0);
		err = s.checkUpdate(BaseBO.INVALID_CASE_ID);
		Assert.assertEquals(err, Staff.FIELD_ERROR_departmentID);
		s.setDepartmentID(1);
		//
		Shared.caseLog("测试角色ID");
//		s.setRoleID(EnumTypeRole.ETR_Manager.getIndex());
//		err = s.checkUpdate(BaseBO.INVALID_CASE_ID);
//		Assert.assertEquals(err, Staff.FIELD_ERROR_roleID);
//		s.setRoleID(EnumTypeRole.ETR_Assistant.getIndex());
//		err = s.checkUpdate(BaseBO.INVALID_CASE_ID);
//		Assert.assertEquals(err, Staff.FIELD_ERROR_roleID);
//		s.setRoleID(EnumTypeRole.ETR_CommercialManager.getIndex());
//		err = s.checkUpdate(BaseBO.INVALID_CASE_ID);
//		Assert.assertEquals(err, Staff.FIELD_ERROR_roleID);
//		s.setRoleID(EnumTypeRole.ETR_Cashier.getIndex());
		err = s.checkUpdate(BaseBO.INVALID_CASE_ID);
		Assert.assertEquals(err, "");
		s.setRoleID(EnumTypeRole.ETR_Boss.getIndex());
		err = s.checkUpdate(BaseBO.INVALID_CASE_ID);
		Assert.assertEquals(err, "");
		s.setRoleID(EnumTypeRole.ETR_PreSale.getIndex());
		err = s.checkUpdate(BaseBO.INVALID_CASE_ID);
		Assert.assertEquals(err, "");
		//
		Shared.caseLog("测试状态码");
		s.setStatus(BaseAction.INVALID_STATUS);
		err = s.checkUpdate(BaseBO.INVALID_CASE_ID);
		Assert.assertEquals(err, Staff.FIELD_ERROR_status);
		s.setStatus(0);
		//
		Shared.caseLog("测试是否返回盐值");
		s.setIsLoginFromPos(2);
		err = s.checkUpdate(BaseBO.INVALID_CASE_ID);
		Assert.assertEquals(err, Staff.FIELD_ERROR_returnSalt);
		s.setIsFirstTimeLogin(Staff.RETURN_SALT);
	}

	@Test
	public void checkRetrieve1_CASE1() {
		Shared.printTestMethodStartInfo();

		Staff s = new Staff();
		s.setID(1);
		s.setPhone("12345678910");
		s.setInvolvedResigned(Staff.INVOLVE_RESIGNED);
		s.setReturnSalt(Staff.RETURN_SALT);
		String err = s.checkRetrieve1(BaseBO.CASE_Login);
		Assert.assertEquals(err, "");
		//
		Shared.caseLog("测试ID和手机号都不合法");
		s.setID(BaseAction.INVALID_ID);
		s.setPhone("");
		err = s.checkRetrieve1(BaseBO.CASE_Login);
		Assert.assertEquals(err, Staff.FIELD_ERROR_IDorPhone);
		//
		Shared.caseLog("测试手机号合法，ID不合法");
		s.setID(BaseAction.INVALID_ID);
		s.setPhone("12345678910");
		err = s.checkRetrieve1(BaseBO.CASE_Login);
		Assert.assertEquals(err, "");
		//
		Shared.caseLog("测试手机号不合法，ID合法");
		s.setPhone("12345678910xx");
		s.setID(1);
		err = s.checkRetrieve1(BaseBO.CASE_Login);
		Assert.assertEquals(err, "");
		//
		Shared.caseLog("测试是否查询离职员工");
		s.setInvolvedResigned(Shared.BIG_ID);
		err = s.checkRetrieve1(BaseBO.CASE_Login);
		Assert.assertEquals(err, Staff.FIELD_ERROR_involvedResigned);
		s.setInvolvedResigned(Staff.INVOLVE_RESIGNED);
		//
		Shared.caseLog("测试是否返回盐值");
		s.setReturnSalt(Shared.BIG_ID);
		err = s.checkRetrieve1(BaseBO.CASE_Login);
		Assert.assertEquals(err, Staff.FIELD_ERROR_returnSalt);
		s.setReturnSalt(Staff.RETURN_SALT);
	}

	@Test
	public void checkRetrieve1_CASE2() {
		Shared.printTestMethodStartInfo();

		Staff s = new Staff();
		s.setID(1);
		s.setPhone("12345678910");
		s.setInvolvedResigned(Staff.INVOLVE_RESIGNED);
		s.setReturnSalt(Staff.RETURN_SALT);
		String err = s.checkRetrieve1(BaseBO.INVALID_CASE_ID);
		Assert.assertEquals(err, "");
		//
		Shared.caseLog("测试ID和手机号都不合法");
		s.setID(BaseAction.INVALID_ID);
		s.setPhone("");
		err = s.checkRetrieve1(BaseBO.INVALID_CASE_ID);
		Assert.assertEquals(err, Staff.FIELD_ERROR_IDorPhone);
		//
		Shared.caseLog("测试手机号合法，ID不合法");
		s.setID(BaseAction.INVALID_ID);
		s.setPhone("12345678910");
		err = s.checkRetrieve1(BaseBO.INVALID_CASE_ID);
		Assert.assertEquals(err, "");
		//
		Shared.caseLog("测试手机号不合法，ID合法");
		s.setPhone("12345678910xx");
		s.setID(1);
		err = s.checkRetrieve1(BaseBO.INVALID_CASE_ID);
		Assert.assertEquals(err, "");
		//
		Shared.caseLog("测试是否查询离职员工");
		s.setInvolvedResigned(Shared.BIG_ID);
		err = s.checkRetrieve1(BaseBO.INVALID_CASE_ID);
		Assert.assertEquals(err, Staff.FIELD_ERROR_involvedResigned);
		s.setInvolvedResigned(Staff.INVOLVE_RESIGNED);
		//
		Shared.caseLog("测试是否返回盐值");
		s.setReturnSalt(Shared.BIG_ID);
		err = s.checkRetrieve1(BaseBO.INVALID_CASE_ID);
		Assert.assertEquals(err, Staff.FIELD_ERROR_returnSalt);
		s.setReturnSalt(Staff.RETURN_SALT);
	}

	@Test
	public void checkDelete() {
		Shared.printTestMethodStartInfo();

		Staff s = new Staff();
		s.setID(1);
		String err = s.checkDelete(BaseBO.INVALID_CASE_ID);
		Assert.assertEquals(err, "");
		//
		Shared.caseLog("测试ID");
		s.setID(BaseAction.INVALID_ID);
		err = s.checkDelete(BaseBO.INVALID_CASE_ID);
		Assert.assertEquals(err, FieldFormat.FIELD_ERROR_ID);
		s.setID(1);
	}

	@Test
	public void checkRetrieveN_CASE1() {
		Shared.printTestMethodStartInfo();

		Staff staff = new Staff();
		staff.setID(1);
		staff.setFieldToCheckUnique(1);
		staff.setUniqueField("12345678910");// 包含手机号、身份证、微信号
		String err = staff.checkRetrieveN(BaseBO.CASE_CheckUniqueField);
		Assert.assertEquals(err, "");
		//
		Shared.caseLog("测试ID");
		staff.setID(BaseAction.INVALID_ID);
		err = staff.checkRetrieveN(BaseBO.CASE_CheckUniqueField);
		Assert.assertEquals(err, FieldFormat.FIELD_ERROR_ID);
		staff.setID(1);
		//
		Shared.caseLog("测试int1");
		staff.setFieldToCheckUnique(1);
		err = staff.checkRetrieveN(BaseBO.CASE_CheckUniqueField);
		Assert.assertEquals(err, "");
		staff.setFieldToCheckUnique(2);
		staff.setUniqueField("44152219940925821X");
		err = staff.checkRetrieveN(BaseBO.CASE_CheckUniqueField);
		Assert.assertEquals(err, "");
		staff.setFieldToCheckUnique(3);
		staff.setUniqueField("1234678910");
		err = staff.checkRetrieveN(BaseBO.CASE_CheckUniqueField);
		Assert.assertEquals(err, "");
		staff.setFieldToCheckUnique(4);
		err = staff.checkRetrieveN(BaseBO.CASE_CheckUniqueField);
		Assert.assertEquals(err, Staff.FIELD_ERROR_fieldToCheckUnique);
		//
		Shared.caseLog("测试手机号");
		staff.setFieldToCheckUnique(Staff.CASE_CHECKPHONE);
		staff.setUniqueField("1234678910xx");
		err = staff.checkRetrieveN(BaseBO.CASE_CheckUniqueField);
		Assert.assertEquals(err, Staff.FIELD_ERROR_phone);
		//
		Shared.caseLog("测试身份证");
		staff.setFieldToCheckUnique(Staff.CASE_CHECKICID);
		staff.setUniqueField("44152219940925821Xxx");
		err = staff.checkRetrieveN(BaseBO.CASE_CheckUniqueField);
		Assert.assertEquals(err, Staff.FIELD_ERROR_ICID);
		staff.setUniqueField("");
		err = staff.checkRetrieveN(BaseBO.CASE_CheckUniqueField);
		Assert.assertEquals(err, "");
		staff.setUniqueField(null);
		err = staff.checkRetrieveN(BaseBO.CASE_CheckUniqueField);
		Assert.assertEquals(err, "");
		//
		Shared.caseLog("测试微信号");
		staff.setFieldToCheckUnique(Staff.CASE_CHECKWECHAT);
		staff.setUniqueField("12345678901234567890xxx");
		err = staff.checkRetrieveN(BaseBO.CASE_CheckUniqueField);
		Assert.assertEquals(err, Staff.FIELD_ERROR_weChat);
		staff.setUniqueField("");
		err = staff.checkRetrieveN(BaseBO.CASE_CheckUniqueField);
		Assert.assertEquals(err, "");
		staff.setUniqueField(null);
		err = staff.checkRetrieveN(BaseBO.CASE_CheckUniqueField);
		Assert.assertEquals(err, "");
		staff.setUniqueField("1234678910");
	}

	@Test
	public void checkRetrieveN_CASE2() {
		Shared.printTestMethodStartInfo();

		Staff staff = new Staff();
		staff.setQueryKeyword("12345678910");// 包含name和phone
		staff.setInvolvedResigned(1);
		staff.setOperator(EnumBoolean.EB_NO.getIndex());
		staff.setPageIndex(1);
		staff.setPageSize(10);
		String err = staff.checkRetrieveN(BaseBO.INVALID_CASE_ID);
		Assert.assertEquals(err, "");
		//
		Shared.caseLog("测试QueryKeyword");
		staff.setQueryKeyword("12345678910xxxxx");
		err = staff.checkRetrieveN(BaseBO.INVALID_CASE_ID);
		Assert.assertEquals(err, Staff.FIELD_ERROR_queryKeyword);
		staff.setQueryKeyword("12345678910");
		//
		Shared.caseLog("测试是否查询离职员工");
		staff.setInvolvedResigned(9);
		err = staff.checkRetrieveN(BaseBO.INVALID_CASE_ID);
		Assert.assertEquals(err, Staff.FIELD_ERROR_RN_involvedResigned);
		staff.setInvolvedResigned(1);
		//
		Shared.caseLog("测试操作员是否为0或1");
		staff.setOperator(-1);
		err = staff.checkRetrieveN(BaseBO.INVALID_CASE_ID);
		Assert.assertEquals(err, Staff.FIELD_ERROR_operator);
		staff.setOperator(2);
		err = staff.checkRetrieveN(BaseBO.INVALID_CASE_ID);
		Assert.assertEquals(err, Staff.FIELD_ERROR_operator);
		staff.setOperator(EnumBoolean.EB_Yes.getIndex());
		err = staff.checkRetrieveN(BaseBO.INVALID_CASE_ID);
		Assert.assertEquals(err, "");
		//
		Shared.caseLog("测试PageIndex");
		staff.setPageIndex(0);
		err = staff.checkRetrieveN(BaseBO.INVALID_CASE_ID);
		Assert.assertEquals(err, FieldFormat.FIELD_ERROR_Paging);
		staff.setPageIndex(1);
		//
		Shared.caseLog("测试PageSize");
		staff.setPageSize(0);
		err = staff.checkRetrieveN(BaseBO.INVALID_CASE_ID);
		Assert.assertEquals(err, FieldFormat.FIELD_ERROR_Paging);
		staff.setPageSize(10);
	}
}
