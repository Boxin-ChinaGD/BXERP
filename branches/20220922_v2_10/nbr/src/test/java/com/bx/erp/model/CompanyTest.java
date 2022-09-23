package com.bx.erp.model;

import static org.testng.Assert.assertEquals;

import java.text.ParseException;
import java.util.HashMap;
import java.util.Map;

import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import com.bx.erp.action.bo.BaseBO;
import com.bx.erp.model.Company;
import com.bx.erp.model.Vip.EnumSexVip;
import com.bx.erp.test.BaseCompanyTest;
import com.bx.erp.test.Shared;
import com.bx.erp.util.FieldFormat;

public class CompanyTest {

	@BeforeClass
	public void setUp() {
		Shared.printTestClassStartInfo();
	}

	@AfterClass
	public void tearDown() {
		Shared.printTestClassEndInfo();
	}

	@Test
	public void testCheckCreate() {
		Shared.printTestMethodStartInfo();

		Company company = new Company();
		company.setDbUserName("DB_Name");
		company.setDbUserPassword("DB_Password");
		company.setBrandName("娃哈哈");

		String error = "";
		company.setStatus(1);
		error = company.checkCreate(BaseBO.INVALID_CASE_ID);
		assertEquals(error, Company.FIELD_ERROR_status);
		company.setStatus(2);
		error = company.checkCreate(BaseBO.INVALID_CASE_ID);
		assertEquals(error, Company.FIELD_ERROR_status);
		company.setStatus(-99);
		error = company.checkCreate(BaseBO.INVALID_CASE_ID);
		assertEquals(error, Company.FIELD_ERROR_status);
		company.setStatus(0);

		company.setBusinessLicenseSN("123456789");
		error = company.checkCreate(BaseBO.INVALID_CASE_ID);
		assertEquals(error, Company.FIELD_ERROR_businessLicenseSN);
		company.setBusinessLicenseSN("1234567891234567");
		error = company.checkCreate(BaseBO.INVALID_CASE_ID);
		assertEquals(error, Company.FIELD_ERROR_businessLicenseSN);
		company.setBusinessLicenseSN("123456789123456789123");
		error = company.checkCreate(BaseBO.INVALID_CASE_ID);
		assertEquals(error, Company.FIELD_ERROR_businessLicenseSN);
		company.setBusinessLicenseSN("123aa6789aa3456");
		error = company.checkCreate(BaseBO.INVALID_CASE_ID);
		assertEquals(error, Company.FIELD_ERROR_businessLicenseSN);
		company.setBusinessLicenseSN("123&&6789*13456");
		error = company.checkCreate(BaseBO.INVALID_CASE_ID);
		assertEquals(error, Company.FIELD_ERROR_businessLicenseSN);
		company.setBusinessLicenseSN("123测试6789测试3456");
		error = company.checkCreate(BaseBO.INVALID_CASE_ID);
		assertEquals(error, Company.FIELD_ERROR_businessLicenseSN);
		company.setBusinessLicenseSN("");
		error = company.checkCreate(BaseBO.INVALID_CASE_ID);
		assertEquals(error, Company.FIELD_ERROR_businessLicenseSN);
		company.setBusinessLicenseSN("123456789123456");
		// company.setBusinessLicenseSN("123456789123456123");
		// company.setBusinessLicenseSN("ASDFGHJKLKJHGFD");
		// company.setBusinessLicenseSN("ASDFGHJKLKJHGFDASD");
		// company.setBusinessLicenseSN("ASDFGHJKL123GFDASD");
		// company.setBusinessLicenseSN("ASDFGL123GFDASD");

		String string = "12345678901234567890";
		company.setBusinessLicensePicture(string + string + string + string + string + string + string);
		error = company.checkCreate(BaseBO.INVALID_CASE_ID);
		assertEquals(error, Company.FIELD_ERROR_businessLicensePicture);
		company.setBusinessLicensePicture("");
		
		company.setLogo(string + string + string + string + string + string + string);
		error = company.checkCreate(BaseBO.INVALID_CASE_ID);
		assertEquals(error, Company.FIELD_ERROR_logo);
		company.setLogo("");

		company.setBossPhone("123456789101");
		error = company.checkCreate(BaseBO.INVALID_CASE_ID);
		assertEquals(error, Company.FIELD_ERROR_bossPhone);
		company.setBossPhone("123456");
		error = company.checkCreate(BaseBO.INVALID_CASE_ID);
		assertEquals(error, Company.FIELD_ERROR_bossPhone);
		company.setBossPhone("");
		error = company.checkCreate(BaseBO.INVALID_CASE_ID);
		assertEquals(error, Company.FIELD_ERROR_bossPhone);
		company.setBossPhone("13764229543");

		company.setBossPassword("12cde");
		error = company.checkCreate(BaseBO.INVALID_CASE_ID);
		assertEquals(error, FieldFormat.FIELD_ERROR_Password);
		company.setBossPassword("1234567890abcdabcd");
		error = company.checkCreate(BaseBO.INVALID_CASE_ID);
		assertEquals(error, FieldFormat.FIELD_ERROR_Password);
		company.setBossPassword("abcd中文1234");
		error = company.checkCreate(BaseBO.INVALID_CASE_ID);
		assertEquals(error, FieldFormat.FIELD_ERROR_Password);
		company.setBossPassword("!@# 密码 $%");
		error = company.checkCreate(BaseBO.INVALID_CASE_ID);
		assertEquals(error, FieldFormat.FIELD_ERROR_Password);
		company.setBossPassword("");
		error = company.checkCreate(BaseBO.INVALID_CASE_ID);
		assertEquals(error, FieldFormat.FIELD_ERROR_Password);
		company.setBossPassword(null);
		error = company.checkCreate(BaseBO.INVALID_CASE_ID);
		assertEquals(error, FieldFormat.FIELD_ERROR_Password);
		company.setBossPassword("112123");

		company.setBossWechat("a12345678901234567890");
		error = company.checkCreate(BaseBO.INVALID_CASE_ID);
		assertEquals(error, FieldFormat.FIELD_ERROR_wechat);
		company.setBossWechat("a1156+156");
		error = company.checkCreate(BaseBO.INVALID_CASE_ID);
		assertEquals(error, FieldFormat.FIELD_ERROR_wechat);
		company.setBossWechat("a123_456");

		company.setDbName("还好");
		error = company.checkCreate(BaseBO.INVALID_CASE_ID);
		assertEquals(error, FieldFormat.FIELD_ERROR_dbName);
		company.setDbName("1156156");
		error = company.checkCreate(BaseBO.INVALID_CASE_ID);
		assertEquals(error, FieldFormat.FIELD_ERROR_dbName);
		company.setDbName("a12345678901234567890");
		error = company.checkCreate(BaseBO.INVALID_CASE_ID);
		assertEquals(error, FieldFormat.FIELD_ERROR_dbName);
		company.setDbName("a1156+156");
		error = company.checkCreate(BaseBO.INVALID_CASE_ID);
		assertEquals(error, FieldFormat.FIELD_ERROR_dbName);
		company.setDbName("a1156-156");
		error = company.checkCreate(BaseBO.INVALID_CASE_ID);
		assertEquals(error, FieldFormat.FIELD_ERROR_dbName);
		company.setDbName("a123_456");

		// company.setKey("还好");
		// error = company.checkCreate(BaseBO.INVALID_CASE_ID);
		// assertEquals(error, Company.FIELD_ERROR_key);
		// company.setKey("1156156");
		// error = company.checkCreate(BaseBO.INVALID_CASE_ID);
		// assertEquals(error, Company.FIELD_ERROR_key);
		// string = "1234567890";
		// company.setKey(string + string + string + "1");
		// error = company.checkCreate(BaseBO.INVALID_CASE_ID);
		// assertEquals(error, Company.FIELD_ERROR_key);
		// company.setKey(string + string + string + string);
		// error = company.checkCreate(BaseBO.INVALID_CASE_ID);
		// assertEquals(error, Company.FIELD_ERROR_key);
		// company.setKey(string + string + string + "12");

		company.setName("一二三四五六七八九十一二三");
		error = company.checkCreate(BaseBO.INVALID_CASE_ID);
		assertEquals(error, Company.FIELD_ERROR_name);
		company.setName("1234567890123");
		error = company.checkCreate(BaseBO.INVALID_CASE_ID);
		assertEquals(error, Company.FIELD_ERROR_name);
		company.setName("");
		error = company.checkCreate(BaseBO.INVALID_CASE_ID);
		assertEquals(error, Company.FIELD_ERROR_name);
		company.setName("bx一号公司12");
		error = company.checkCreate(BaseBO.INVALID_CASE_ID);
		assertEquals(error, Company.FIELD_ERROR_name);
		company.setName("123456789123");
		error = company.checkCreate(BaseBO.INVALID_CASE_ID);
		assertEquals(error, Company.FIELD_ERROR_name);
		company.setName("1234567891aA");
		error = company.checkCreate(BaseBO.INVALID_CASE_ID);
		assertEquals(error, Company.FIELD_ERROR_name);
		company.setName("ofhlkj德生科技里发火");
		error = company.checkCreate(BaseBO.INVALID_CASE_ID);
		assertEquals(error, Company.FIELD_ERROR_name);
		company.setName("bx一号公司");


		company.setBossName("ABCDEF");
		error = company.checkCreate(BaseBO.INVALID_CASE_ID);
		assertEquals(error, "");
		company.setBossName("一二三四五六七八九十一二");
		error = company.checkCreate(BaseBO.INVALID_CASE_ID);
		assertEquals(error, "");
		company.setBossName("");
		error = company.checkCreate(BaseBO.INVALID_CASE_ID);
		assertEquals(error, FieldFormat.FIELD_ERROR_HumanName);
		company.setBossName("一二三四五六七八九十一二三");
		error = company.checkCreate(BaseBO.INVALID_CASE_ID);
		assertEquals(error, FieldFormat.FIELD_ERROR_HumanName);
		company.setBossName("123老板名称");
		error = company.checkCreate(BaseBO.INVALID_CASE_ID);
		assertEquals(error, FieldFormat.FIELD_ERROR_HumanName);
		company.setBossName("老板名称123");
		error = company.checkCreate(BaseBO.INVALID_CASE_ID);
		assertEquals(error, FieldFormat.FIELD_ERROR_HumanName);
		company.setBossName("老板123名称");
		error = company.checkCreate(BaseBO.INVALID_CASE_ID);
		assertEquals(error, FieldFormat.FIELD_ERROR_HumanName);
		company.setBossName("123ABCD");
		error = company.checkCreate(BaseBO.INVALID_CASE_ID);
		assertEquals(error, FieldFormat.FIELD_ERROR_HumanName);
		company.setBossName("ABCD123");
		error = company.checkCreate(BaseBO.INVALID_CASE_ID);
		assertEquals(error, FieldFormat.FIELD_ERROR_HumanName);
		company.setBossName("AB123CD");
		error = company.checkCreate(BaseBO.INVALID_CASE_ID);
		assertEquals(error, FieldFormat.FIELD_ERROR_HumanName);
		company.setBossName("123");
		error = company.checkCreate(BaseBO.INVALID_CASE_ID);
		assertEquals(error, FieldFormat.FIELD_ERROR_HumanName);
		company.setBossName("老板名称");
		error = company.checkCreate(BaseBO.INVALID_CASE_ID);
		assertEquals(error, "");

		company.setDbUserName("");
		error = company.checkCreate(BaseBO.INVALID_CASE_ID);
		assertEquals(error, Company.FIELD_ERROR_DBUserName);
		company.setDbUserName("12121");
		error = company.checkCreate(BaseBO.INVALID_CASE_ID);
		assertEquals(error, Company.FIELD_ERROR_DBUserName);
		company.setDbUserName("a123 13");
		error = company.checkCreate(BaseBO.INVALID_CASE_ID);
		assertEquals(error, Company.FIELD_ERROR_DBUserName);
		company.setDbUserName("123_13");
		error = company.checkCreate(BaseBO.INVALID_CASE_ID);
		assertEquals(error, Company.FIELD_ERROR_DBUserName);
		company.setDbUserName("_________");
		error = company.checkCreate(BaseBO.INVALID_CASE_ID);
		assertEquals(error, Company.FIELD_ERROR_DBUserName);
		company.setDbUserName("a12345678901234567890");
		error = company.checkCreate(BaseBO.INVALID_CASE_ID);
		assertEquals(error, Company.FIELD_ERROR_DBUserName);
		company.setDbUserName("a12313");

		company.setDbUserPassword("12cde");
		error = company.checkCreate(BaseBO.INVALID_CASE_ID);
		assertEquals(error, FieldFormat.FIELD_ERROR_Password);
		company.setDbUserPassword("1234567890abcdabcd");
		error = company.checkCreate(BaseBO.INVALID_CASE_ID);
		assertEquals(error, FieldFormat.FIELD_ERROR_Password);
		company.setDbUserPassword("abcd中文1234");
		error = company.checkCreate(BaseBO.INVALID_CASE_ID);
		assertEquals(error, FieldFormat.FIELD_ERROR_Password);
		company.setDbUserPassword("!@# 密码 $%");
		error = company.checkCreate(BaseBO.INVALID_CASE_ID);
		assertEquals(error, FieldFormat.FIELD_ERROR_Password);
		company.setDbUserPassword("");
		error = company.checkCreate(BaseBO.INVALID_CASE_ID);
		assertEquals(error, FieldFormat.FIELD_ERROR_Password);
		company.setDbUserPassword(null);
		error = company.checkCreate(BaseBO.INVALID_CASE_ID);
		assertEquals(error, FieldFormat.FIELD_ERROR_Password);
		company.setDbUserPassword("112123");

		company.setSubmchid("abcdeabcde");
		error = company.checkCreate(BaseBO.INVALID_CASE_ID);
		assertEquals(error, Company.FIELD_ERROR_submchid);
		company.setSubmchid("12345678999");
		error = company.checkCreate(BaseBO.INVALID_CASE_ID);
		assertEquals(error, Company.FIELD_ERROR_submchid);
		company.setSubmchid("231246512");
		error = company.checkCreate(BaseBO.INVALID_CASE_ID);
		assertEquals(error, Company.FIELD_ERROR_submchid);
		company.setSubmchid("");
		error = company.checkCreate(BaseBO.INVALID_CASE_ID);
		assertEquals(error, "");
		company.setSubmchid("1234512345");
		error = company.checkCreate(BaseBO.INVALID_CASE_ID);
		assertEquals(error, "");
		company.setSubmchid(null);
		error = company.checkCreate(BaseBO.INVALID_CASE_ID);
		assertEquals(error, "");
		// case brandName
		company.setBrandName(null);
		error = company.checkCreate(BaseBO.INVALID_CASE_ID);
		assertEquals(error, Company.FIELD_ERROR_brandName);
		company.setBrandName("");
		error = company.checkCreate(BaseBO.INVALID_CASE_ID);
		assertEquals(error, Company.FIELD_ERROR_brandName);
		company.setBrandName("123456789012345678901");
		error = company.checkCreate(BaseBO.INVALID_CASE_ID);
		assertEquals(error, Company.FIELD_ERROR_brandName);
		company.setBrandName("娃哈哈");
		error = company.checkCreate(BaseBO.INVALID_CASE_ID);
		assertEquals(error, "");
		company.setBrandName("可口可乐");
		error = company.checkCreate(BaseBO.INVALID_CASE_ID);
		assertEquals(error, "");
		company.setBrandName("12345678901234567890");
		error = company.checkCreate(BaseBO.INVALID_CASE_ID);
		assertEquals(error, "");
	}

	@Test
	public void testCheckRetrieve1() {
		Shared.printTestMethodStartInfo();

		Company company = new Company();
		String error = "";
		error = company.checkRetrieve1(BaseBO.INVALID_CASE_ID);
		Assert.assertEquals(error, "");
	}

	@Test
	public void testCheckRetrieveN() throws ParseException, CloneNotSupportedException, InterruptedException {
		Shared.printTestMethodStartInfo();

		Company company = BaseCompanyTest.DataInput.getCompany();
		String error = "";
		//
		company.setFieldToCheckUnique(Company.CASE_CheckName);
		company.setUniqueField(company.getName());
		error = company.checkRetrieveN(BaseBO.CASE_CheckUniqueField);
		Assert.assertEquals(error, "");
		//
		company.setFieldToCheckUnique(Company.CASE_CheckBusinessLicenseSN);
		company.setUniqueField(company.getBusinessLicenseSN());
		error = company.checkRetrieveN(BaseBO.CASE_CheckUniqueField);
		Assert.assertEquals(error, "");
		//
		company.setFieldToCheckUnique(Company.CASE_CheckDbName);
		company.setUniqueField(company.getDbName());
		error = company.checkRetrieveN(BaseBO.CASE_CheckUniqueField);
		Assert.assertEquals(error, "");
		//
		company.setFieldToCheckUnique(Company.CASE_CheckSubmchid);
		company.setUniqueField(company.getSubmchid());
		error = company.checkRetrieveN(BaseBO.CASE_CheckUniqueField);
		Assert.assertEquals(error, "");
		//
		company.setQueryKeyword(Shared.DEFAULT_VIP_Mobile);
		error = company.checkRetrieveN(BaseBO.CASE_Company_retrieveNByVipMobile);
		Assert.assertEquals(error, "");
	}
	
	@Test
	public void testCheckRetrieveNEx() throws ParseException, CloneNotSupportedException, InterruptedException {
		Shared.printTestMethodStartInfo();

		Company company = BaseCompanyTest.DataInput.getCompany();
		String error = "";
		//
		company.setMobile(Shared.DEFAULT_VIP_Mobile);
		company.setOpenID(Shared.DEFAULT_VIP_openID);
		company.setUnionID(Shared.DEFAULT_VIP_unionID);
		company.setVipName("谢霆锋");
		error = company.checkRetrieveNEx(BaseBO.CASE_Company_matchVip);
		Assert.assertEquals(error, "");
		//
		company.setMobile("");
		error = company.checkRetrieveNEx(BaseBO.CASE_Company_matchVip);
		Assert.assertEquals(error, Vip.FIELD_ERROR_CheckMobile);
		company.setMobile(Shared.DEFAULT_VIP_Mobile);

		company.setOpenID("");
		error = company.checkRetrieveNEx(BaseBO.CASE_Company_matchVip);
		Assert.assertEquals(error, FieldFormat.FIELD_ERROR_openID);
		//
		company.setOpenID("123456789012345678901234567890123456789012345678901");  // 长51
		error = company.checkRetrieveNEx(BaseBO.CASE_Company_matchVip);
		Assert.assertEquals(error, FieldFormat.FIELD_ERROR_openID);
		//
		company.setOpenID("12345678901234567890123456789012345678901234567890A");  // 长51
		error = company.checkRetrieveNEx(BaseBO.CASE_Company_matchVip);
		Assert.assertEquals(error, FieldFormat.FIELD_ERROR_openID);
		//
		company.setOpenID("1234567890123456789012345 ");  // 长51
		error = company.checkRetrieveNEx(BaseBO.CASE_Company_matchVip);
		Assert.assertEquals(error, FieldFormat.FIELD_ERROR_openID);
		//
		company.setOpenID("1234567890123王道789012345");  // 长51
		error = company.checkRetrieveNEx(BaseBO.CASE_Company_matchVip);
		Assert.assertEquals(error, FieldFormat.FIELD_ERROR_openID);
		//
		company.setOpenID("abcABC_890123456789012345678901___678901234567890");  // 长50
		error = company.checkRetrieveNEx(BaseBO.CASE_Company_matchVip);
		Assert.assertEquals(error, "");
		//
		company.setOpenID("abcABC_8901234567890123456789012345678901234567890");  // 长50
		error = company.checkRetrieveNEx(BaseBO.CASE_Company_matchVip);
		Assert.assertEquals(error, "");
		
		company.setUnionID("");
		error = company.checkRetrieveNEx(BaseBO.CASE_Company_matchVip);
		Assert.assertEquals(error, "");
		//
		company.setUnionID("12345678901234567890123456789012345678901234567890A"); // 长51
		error = company.checkRetrieveNEx(BaseBO.CASE_Company_matchVip);
		Assert.assertEquals(error, FieldFormat.FIELD_ERROR_UnionID);
		//
		company.setUnionID("!$@#$#@!$"); 
		error = company.checkRetrieveNEx(BaseBO.CASE_Company_matchVip);
		Assert.assertEquals(error, FieldFormat.FIELD_ERROR_UnionID);
		//
		company.setUnionID(null);
		company.setUnionID(Shared.DEFAULT_VIP_unionID); 
		error = company.checkRetrieveNEx(BaseBO.CASE_Company_matchVip);
		Assert.assertEquals(error, "");
		//
		company.setUnionID(Shared.DEFAULT_VIP_unionID + "_"); 
		error = company.checkRetrieveNEx(BaseBO.CASE_Company_matchVip);
		Assert.assertEquals(error, "");
		//
		company.setUnionID(Shared.DEFAULT_VIP_unionID); 
		error = company.checkRetrieveNEx(BaseBO.CASE_Company_matchVip);
		Assert.assertEquals(error, "");
		
		company.setSex(EnumSexVip.ESV_Female.getIndex() - 1);
		error = company.checkRetrieveNEx(BaseBO.CASE_Company_matchVip);
		Assert.assertEquals(error, Vip.FIELD_ERROR_sex);
		company.setSex(EnumSexVip.ESV_Unknown.getIndex() + 1);
		error = company.checkRetrieveNEx(BaseBO.CASE_Company_matchVip);
		Assert.assertEquals(error, Vip.FIELD_ERROR_sex);
		company.setSex(EnumSexVip.ESV_Female.getIndex());
		error = company.checkRetrieveNEx(BaseBO.CASE_Company_matchVip);
		Assert.assertEquals(error, "");
		company.setSex(EnumSexVip.ESV_Male.getIndex());
		error = company.checkRetrieveNEx(BaseBO.CASE_Company_matchVip);
		Assert.assertEquals(error, "");
		company.setSex(EnumSexVip.ESV_Unknown.getIndex());
		error = company.checkRetrieveNEx(BaseBO.CASE_Company_matchVip);
		Assert.assertEquals(error, "");
		
		company.setVipName("太上老君");
		error = company.checkRetrieveNEx(BaseBO.CASE_Company_matchVip);
		Assert.assertEquals(error, "");
		company.setVipName("罗志祥");
		error = company.checkRetrieveNEx(BaseBO.CASE_Company_matchVip);
		Assert.assertEquals(error, "");
		company.setVipName(String.format("%0" + (Vip.MAX_LENGTH_WxNickName + 1) + "d", System.currentTimeMillis()));
		error = company.checkRetrieveNEx(BaseBO.CASE_Company_matchVip);
		Assert.assertEquals(error, Vip.FIELD_ERROR_name);
		company.setVipName("1");
		error = company.checkRetrieveNEx(BaseBO.CASE_Company_matchVip);
		Assert.assertEquals(error, "");
		company.setVipName("会员123465");
		error = company.checkRetrieveNEx(BaseBO.CASE_Company_matchVip);
		Assert.assertEquals(error, "");
		company.setVipName("abc123465");
		error = company.checkRetrieveNEx(BaseBO.CASE_Company_matchVip);
		Assert.assertEquals(error, "");
		
		company.setOpenID("openidjjfosjfsjf-_sfew");
		company.setUnionID(""); // 为空，所以vip name可以为任何值
		company.setVipName(null);
		error = company.checkRetrieveNEx(BaseBO.CASE_Company_matchVip);
		Assert.assertEquals(error, "");

		company.setOpenID("openidjjfosjfsjf-_sfew");
		company.setUnionID(""); // 为空，所以vip name可以为任何值
		company.setVipName("");
		error = company.checkRetrieveNEx(BaseBO.CASE_Company_matchVip);
		Assert.assertEquals(error, "");
	}

	@Test
	public void testCheckUpdate() {
		Shared.printTestMethodStartInfo();

		Company company = new Company();
		company.setBrandName("娃哈哈");
		String error = "";
		company.setID(0);
		error = company.checkUpdate(BaseBO.INVALID_CASE_ID);
		assertEquals(error, FieldFormat.FIELD_ERROR_ID);
		company.setID(-99);
		error = company.checkUpdate(BaseBO.INVALID_CASE_ID);
		assertEquals(error, FieldFormat.FIELD_ERROR_ID);
		company.setID(1);

		company.setBusinessLicenseSN("1234567890123456");
		error = company.checkUpdate(BaseBO.INVALID_CASE_ID);
		assertEquals(error, Company.FIELD_ERROR_businessLicenseSN);
		company.setBusinessLicenseSN("");
		error = company.checkUpdate(BaseBO.INVALID_CASE_ID);
		assertEquals(error, Company.FIELD_ERROR_businessLicenseSN);
		company.setBusinessLicenseSN("123456789012345");

		String string = "12345678901234567890";
		company.setBusinessLicensePicture(string + string + string + string + string + string + string);
		error = company.checkUpdate(BaseBO.INVALID_CASE_ID);
		assertEquals(error, Company.FIELD_ERROR_businessLicensePicture);
		company.setBusinessLicensePicture("");

		company.setBossPhone("123456789101");
		error = company.checkUpdate(BaseBO.INVALID_CASE_ID);
		assertEquals(error, Company.FIELD_ERROR_bossPhone);
		company.setBossPhone("123456");
		error = company.checkUpdate(BaseBO.INVALID_CASE_ID);
		assertEquals(error, Company.FIELD_ERROR_bossPhone);
		company.setBossPhone("");
		error = company.checkUpdate(BaseBO.INVALID_CASE_ID);
		assertEquals(error, Company.FIELD_ERROR_bossPhone);
		company.setBossPhone("13764229543");

		company.setBossWechat("a12345678901234567890");
		error = company.checkUpdate(BaseBO.INVALID_CASE_ID);
		assertEquals(error, FieldFormat.FIELD_ERROR_wechat);
		company.setBossWechat("a1156+156");
		error = company.checkUpdate(BaseBO.INVALID_CASE_ID);
		assertEquals(error, FieldFormat.FIELD_ERROR_wechat);
		company.setBossWechat("a123_456");

		company.setName("一二三四五六七八九十一二三");
		error = company.checkUpdate(BaseBO.INVALID_CASE_ID);
		assertEquals(error, Company.FIELD_ERROR_name);
		company.setName("1234567890123");
		error = company.checkUpdate(BaseBO.INVALID_CASE_ID);
		assertEquals(error, Company.FIELD_ERROR_name);
		company.setName("");
		error = company.checkUpdate(BaseBO.INVALID_CASE_ID);
		assertEquals(error, Company.FIELD_ERROR_name);
		company.setName("ty0123456789");
		error = company.checkUpdate(BaseBO.INVALID_CASE_ID);
		assertEquals(error, Company.FIELD_ERROR_name);
		company.setName("012345678999");
		error = company.checkUpdate(BaseBO.INVALID_CASE_ID);
		assertEquals(error, Company.FIELD_ERROR_name);
		company.setName("胡vb123");
		error = company.checkUpdate(BaseBO.INVALID_CASE_ID);
		assertEquals(error, Company.FIELD_ERROR_name);
		company.setName("abdyyyyyyyyqu");
		error = company.checkUpdate(BaseBO.INVALID_CASE_ID);
		assertEquals(error, Company.FIELD_ERROR_name);
		company.setName("胡wqtyuwyqwwe在");
		error = company.checkUpdate(BaseBO.INVALID_CASE_ID);
		assertEquals(error, Company.FIELD_ERROR_name);
		company.setName("bx一号公司");

		company.setBossName("ABCDEF");
		error = company.checkUpdate(BaseBO.INVALID_CASE_ID);
		assertEquals(error, "");
		company.setBossName("一二三四五六七八九十一二");
		error = company.checkUpdate(BaseBO.INVALID_CASE_ID);
		assertEquals(error, "");
		company.setBossName("");
		error = company.checkUpdate(BaseBO.INVALID_CASE_ID);
		assertEquals(error, FieldFormat.FIELD_ERROR_HumanName);
		company.setBossName("一二三四五六七八九十一二三");
		error = company.checkUpdate(BaseBO.INVALID_CASE_ID);
		assertEquals(error, FieldFormat.FIELD_ERROR_HumanName);
		company.setBossName("123老板名称");
		error = company.checkUpdate(BaseBO.INVALID_CASE_ID);
		assertEquals(error, FieldFormat.FIELD_ERROR_HumanName);
		company.setBossName("老板名称123");
		error = company.checkUpdate(BaseBO.INVALID_CASE_ID);
		assertEquals(error, FieldFormat.FIELD_ERROR_HumanName);
		company.setBossName("老板123名称");
		error = company.checkUpdate(BaseBO.INVALID_CASE_ID);
		assertEquals(error, FieldFormat.FIELD_ERROR_HumanName);
		company.setBossName("123ABCD");
		error = company.checkUpdate(BaseBO.INVALID_CASE_ID);
		assertEquals(error, FieldFormat.FIELD_ERROR_HumanName);
		company.setBossName("ABCD123");
		error = company.checkUpdate(BaseBO.INVALID_CASE_ID);
		assertEquals(error, FieldFormat.FIELD_ERROR_HumanName);
		company.setBossName("AB123CD");
		error = company.checkUpdate(BaseBO.INVALID_CASE_ID);
		assertEquals(error, FieldFormat.FIELD_ERROR_HumanName);
		company.setBossName("123");
		error = company.checkUpdate(BaseBO.INVALID_CASE_ID);
		assertEquals(error, FieldFormat.FIELD_ERROR_HumanName);
		company.setBossName("老板名称");
		error = company.checkUpdate(BaseBO.INVALID_CASE_ID);
		assertEquals(error, "");
		// case brandName
		company.setBrandName(null);
		error = company.checkUpdate(BaseBO.INVALID_CASE_ID);
		assertEquals(error, Company.FIELD_ERROR_brandName);
		company.setBrandName("");
		error = company.checkUpdate(BaseBO.INVALID_CASE_ID);
		assertEquals(error, Company.FIELD_ERROR_brandName);
		company.setBrandName("123456789012345678901");
		error = company.checkUpdate(BaseBO.INVALID_CASE_ID);
		assertEquals(error, Company.FIELD_ERROR_brandName);
		company.setBrandName("娃哈哈");
		error = company.checkUpdate(BaseBO.INVALID_CASE_ID);
		assertEquals(error, "");
		company.setBrandName("可口可乐");
		error = company.checkUpdate(BaseBO.INVALID_CASE_ID);
		assertEquals(error, "");
		company.setBrandName("12345678901234567890");
		error = company.checkUpdate(BaseBO.INVALID_CASE_ID);
		assertEquals(error, "");

	}

	@Test
	public void testCheckUpdateSubmchid() {
		Shared.printTestMethodStartInfo();

		Company company = new Company();

		String error = "";
		company.setID(0);
		error = company.checkUpdate(BaseBO.CASE_Company_UpdateSubmchid);
		assertEquals(error, FieldFormat.FIELD_ERROR_ID);
		company.setID(-99);
		error = company.checkUpdate(BaseBO.CASE_Company_UpdateSubmchid);
		assertEquals(error, FieldFormat.FIELD_ERROR_ID);
		company.setID(1);
		//
		company.setSubmchid("中文中文中文中文中文");
		error = company.checkUpdate(BaseBO.CASE_Company_UpdateSubmchid);
		assertEquals(error, Company.FIELD_ERROR_submchid);
		company.setSubmchid("1212 12121");
		error = company.checkUpdate(BaseBO.CASE_Company_UpdateSubmchid);
		assertEquals(error, Company.FIELD_ERROR_submchid);
		company.setSubmchid("1212&12121");
		error = company.checkUpdate(BaseBO.CASE_Company_UpdateSubmchid);
		assertEquals(error, Company.FIELD_ERROR_submchid);
		company.setSubmchid("abcdeabcde");
		error = company.checkUpdate(BaseBO.CASE_Company_UpdateSubmchid);
		assertEquals(error, Company.FIELD_ERROR_submchid);
		company.setSubmchid("12345678999");
		error = company.checkUpdate(BaseBO.CASE_Company_UpdateSubmchid);
		assertEquals(error, Company.FIELD_ERROR_submchid);
		company.setSubmchid("231246512");
		error = company.checkUpdate(BaseBO.CASE_Company_UpdateSubmchid);
		assertEquals(error, Company.FIELD_ERROR_submchid);
		company.setSubmchid("");
		error = company.checkUpdate(BaseBO.CASE_Company_UpdateSubmchid);
		assertEquals(error, "");
		company.setSubmchid("1234512345");
		error = company.checkUpdate(BaseBO.CASE_Company_UpdateSubmchid);
		assertEquals(error, "");
		company.setSubmchid(null);
		error = company.checkUpdate(BaseBO.CASE_Company_UpdateSubmchid);
		assertEquals(error, "");
	}

	@Test
	public void testCheckDelete() {
		Shared.printTestMethodStartInfo();

		Company company = new Company();
		String error = "";
		error = company.checkDelete(BaseBO.INVALID_CASE_ID);
		Assert.assertEquals(error, "");
	}

	@Test
	public void testGetCreateParam() {
		Shared.printTestMethodStartInfo();

		Company company = new Company();
		company.setBusinessLicenseSN("1");
		company.setBusinessLicensePicture("2");
		company.setBossPhone("3");
		company.setBossWechat("4");
		company.setDbName("5");
		company.setKey("6");
		company.setName("7");
		company.setBossName("8");
		company.setSubmchid("9");
		Map<String, Object> createParam = new HashMap<>();
		createParam = company.getCreateParam(BaseBO.INVALID_CASE_ID, company);
		Assert.assertEquals(company.getBusinessLicenseSN() == createParam.get("businessLicenseSN"), true);
		Assert.assertEquals(company.getBusinessLicensePicture() == createParam.get("businessLicensePicture"), true);
		Assert.assertEquals(company.getBossPhone() == createParam.get("bossPhone"), true);
		Assert.assertEquals(company.getBossWechat() == createParam.get("bossWechat"), true);
		Assert.assertEquals(company.getDbName() == createParam.get("dbName"), true);
		Assert.assertEquals(company.getKey() == createParam.get("key"), true);
		Assert.assertEquals(company.getName() == createParam.get("name"), true);
		Assert.assertEquals(company.getBossName() == createParam.get("bossName"), true);
		Assert.assertEquals(company.getSubmchid() == createParam.get("submchid"), true);
	}

	@Test
	public void testGetRetrieve1Param() {
		Shared.printTestMethodStartInfo();

		Company company = new Company();
		company.setID(123);
		Map<String, Object> retrieve1Param = new HashMap<String, Object>();
		retrieve1Param = company.getRetrieve1Param(BaseBO.INVALID_CASE_ID, company);
		Assert.assertEquals(company.getID() == Integer.parseInt(retrieve1Param.get("ID").toString()), true);
	}

	@Test
	public void testGetRetrieveNParam() {
		Shared.printTestMethodStartInfo();

		Company company = new Company();
		company.setStatus(1);
		company.setPageIndex(2);
		company.setPageSize(3);
		Map<String, Object> retrieveNParam = new HashMap<String, Object>();
		retrieveNParam = company.getRetrieveNParam(BaseBO.INVALID_CASE_ID, company);
		Assert.assertEquals(company.getStatus() == Integer.parseInt(retrieveNParam.get("status").toString()), true);
		Assert.assertEquals(company.getPageIndex() == Integer.parseInt(retrieveNParam.get("iPageIndex").toString()), true);
		Assert.assertEquals(company.getPageSize() == Integer.parseInt(retrieveNParam.get("iPageSize").toString()), true);
	}

	@Test
	public void testGetUpdateParam() {
		Shared.printTestMethodStartInfo();

		Company company = new Company();
		company.setBusinessLicenseSN("1");
		company.setBusinessLicensePicture("2");
		company.setBossPhone("3");
		company.setBossWechat("4");
		company.setDbName("5");
		company.setKey("6");
		company.setName("7");
		company.setBossName("8");
		Map<String, Object> createParam = new HashMap<>();
		createParam = company.getUpdateParam(BaseBO.INVALID_CASE_ID, company);
		Assert.assertEquals(company.getBusinessLicenseSN() == createParam.get("businessLicenseSN"), true);
		Assert.assertEquals(company.getBusinessLicensePicture() == createParam.get("businessLicensePicture"), true);
		Assert.assertEquals(company.getBossPhone() == createParam.get("bossPhone"), true);
		Assert.assertEquals(company.getBossWechat() == createParam.get("bossWechat"), true);
		Assert.assertEquals(company.getDbName() == createParam.get("dbName"), true);
		Assert.assertEquals(company.getKey() == createParam.get("key"), true);
		Assert.assertEquals(company.getName() == createParam.get("name"), true);
		Assert.assertEquals(company.getBossName() == createParam.get("bossName"), true);
	}

	@Test
	public void testClone() throws CloneNotSupportedException {
		Shared.printTestMethodStartInfo();

		Company company = new Company();
		company.setID(123);
		company.setBusinessLicenseSN("1");
		company.setBusinessLicensePicture("2");
		company.setBossPhone("3");
		company.setBossWechat("4");
		company.setDbName("张三");
		company.setKey("6");
		company.setName("7");
		company.setBossName("8");
		company.setSubmchid("9");
		Company companyClone = (Company) company.clone();
		Assert.assertEquals(company.getID() == companyClone.getID(), true);
		Assert.assertEquals(company.getBusinessLicenseSN() == companyClone.getBusinessLicenseSN(), true);
		Assert.assertEquals(company.getBusinessLicensePicture() == companyClone.getBusinessLicensePicture(), true);
		Assert.assertEquals(company.getBossPhone() == companyClone.getBossPhone(), true);
		Assert.assertEquals(company.getBossWechat() == companyClone.getBossWechat(), true);
		Assert.assertEquals(company.getDbName() == companyClone.getDbName(), true);
		Assert.assertEquals(company.getKey() == companyClone.getKey(), true);
		Assert.assertEquals(company.getName() == companyClone.getName(), true);
		Assert.assertEquals(company.getBossName() == companyClone.getBossName(), true);
		Assert.assertEquals(company.getSubmchid() == companyClone.getSubmchid(), true);
	}

	@Test
	public void testCompareTo() throws CloneNotSupportedException {
		Shared.printTestMethodStartInfo();

		Company company = new Company();
		company.setID(123);
		company.setBusinessLicenseSN("1");
		company.setBusinessLicensePicture("2");
		company.setBossPhone("3");
		company.setBossPassword("879546213");
		company.setBossWechat("4");
		company.setDbName("5");
		company.setKey("6");
		company.setStatus(1);
		company.setName("7");
		company.setBossName("8");
		company.setDbUserName("abc");
		company.setDbUserPassword("132465");
		company.setSubmchid("9");
		company.setBrandName("NIKE");
		Assert.assertEquals(company.compareTo((Company)company.clone()) == 0, true);
	}
}
