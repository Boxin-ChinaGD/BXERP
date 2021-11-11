package com.bx.erp.model;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import com.bx.erp.action.BaseAction;
import com.bx.erp.action.bo.BaseBO;
import com.bx.erp.model.BaseModel.EnumBoolean;
import com.bx.erp.model.Vip;
import com.bx.erp.model.Vip.EnumSexVip;
import com.bx.erp.test.Shared;
import com.bx.erp.test.VipMapperTest;
import com.bx.erp.util.FieldFormat;

public class VipTest {

	@BeforeClass
	public void setUp() {
		Shared.printTestClassStartInfo();
	}

	@AfterClass
	public void tearDown() {
		Shared.printTestClassEndInfo();
	}

	@Test
	public void testCheckCreate() throws Exception {
		Shared.printTestMethodStartInfo();

		Vip vip = new Vip();
		vip.setCardID(1);
		vip.setMobile("12345678910");
		vip.setName("会");
		vip.setConsumeTimes(0);
		vip.setConsumeAmount(0);
		vip.setCategory(VipCategory.DEFAULT_VipCategory_ID);
		vip.setBonus(Vip.BONUS_Imported);
		String err = vip.checkCreate(BaseBO.INVALID_CASE_ID);
		Assert.assertEquals(err, "");
		Shared.caseLog("测试手机号");
		vip.setMobile("12345678910xx");
		err = vip.checkCreate(BaseBO.INVALID_CASE_ID);
		Assert.assertEquals(err, Vip.FIELD_ERROR_mobile);
		vip.setMobile("12345678910");
		Shared.caseLog("测试IDInPOS");
		Shared.caseLog("测试POS_SN");
		vip.setLocalPosSN("SN1234567890123456789012345678901234567890");
		err = vip.checkCreate(BaseBO.INVALID_CASE_ID);
		Assert.assertEquals(err, Pos.FIELD_ERROR_PosSN);
		vip.setLocalPosSN("SN");
		Shared.caseLog("测试身份证");
		vip.setiCID("464646199803235");
		err = vip.checkCreate(BaseBO.INVALID_CASE_ID);
		Assert.assertEquals(err, Vip.FIELD_ERROR_ICID);
		vip.setiCID("445222199823232536");
		err = vip.checkCreate(BaseBO.INVALID_CASE_ID);
		Assert.assertEquals(err, Vip.FIELD_ERROR_icid);
		vip.setiCID("464646199803235648");
		Shared.caseLog("测试名称");
		vip.setName("");
		err = vip.checkCreate(BaseBO.INVALID_CASE_ID);
		Assert.assertEquals(err, Vip.FIELD_ERROR_name);
		vip.setName(null);
		err = vip.checkCreate(BaseBO.INVALID_CASE_ID);
		Assert.assertEquals(err, Vip.FIELD_ERROR_name);
		vip.setName(String.format("%0" + (Vip.MAX_LENGTH_WxNickName + 1) + "d", System.currentTimeMillis()));
		err = vip.checkCreate(BaseBO.INVALID_CASE_ID);
		Assert.assertEquals(err, Vip.FIELD_ERROR_name);
		//
		vip.setName("1");
		err = vip.checkCreate(BaseBO.INVALID_CASE_ID);
		Assert.assertEquals(err, "");
		//
		vip.setName("会员123465");
		err = vip.checkCreate(BaseBO.INVALID_CASE_ID);
		Assert.assertEquals(err, "");
		vip.setName("abc123465");
		err = vip.checkCreate(BaseBO.INVALID_CASE_ID);
		Assert.assertEquals(err, "");
		vip.setName("会员");
		Shared.caseLog("测试邮箱");
		vip.setEmail("123xxxcom");
		err = vip.checkCreate(BaseBO.INVALID_CASE_ID);
		Assert.assertEquals(err, Vip.FIELD_ERROR_Email);
		vip.setEmail("123@xxx.com");
		Shared.caseLog("测试总消费次数");
		vip.setConsumeTimes(-1);
		err = vip.checkCreate(BaseBO.INVALID_CASE_ID);
		Assert.assertEquals(err, Vip.FIELD_ERROR_consumeTimes);
		vip.setConsumeTimes(0);
		Shared.caseLog("测试总消费金额");
		vip.setConsumeAmount(-1);
		err = vip.checkCreate(BaseBO.INVALID_CASE_ID);
		Assert.assertEquals(err, Vip.FIELD_ERROR_consumeAmount);
		vip.setConsumeAmount(0);
		Shared.caseLog("测试区域");
		vip.setDistrict("");
		err = vip.checkCreate(BaseBO.INVALID_CASE_ID);
		Assert.assertEquals(err, "");
		vip.setDistrict("1234567890123456789012345678901234567890");
		err = vip.checkCreate(BaseBO.INVALID_CASE_ID);
		Assert.assertEquals(err, Vip.FIELD_ERROR_district);
		vip.setDistrict("广东");
		Shared.caseLog("测试会员分类");
		vip.setCategory(BaseAction.INVALID_ID);
		err = vip.checkCreate(BaseBO.INVALID_CASE_ID);
		Assert.assertEquals(err, Vip.FEILD_ERROR_category);
		vip.setCategory(VipCategory.DEFAULT_VipCategory_ID);
		Shared.caseLog("测试生日");
		SimpleDateFormat sdf = new SimpleDateFormat(BaseAction.DATETIME_FORMAT_Default_Chinese);
		String date = "2020年12月30日  23:59:59";
		vip.setBirthday(sdf.parse(date));
		err = vip.checkCreate(BaseBO.INVALID_CASE_ID);
		Assert.assertEquals(err, "");
		vip.setBirthday(new Date());
		Shared.caseLog("测试cardID");
		vip.setCardID(-1);
		err = vip.checkCreate(BaseBO.INVALID_CASE_ID);
		Assert.assertEquals(err, Vip.FIELD_ERROR_cardID);
		vip.setCardID(23);
		err = vip.checkCreate(BaseBO.INVALID_CASE_ID);
		Assert.assertEquals(err, "");
		
		// sex
		vip.setSex(EnumSexVip.ESV_Female.getIndex() - 1);
		err = vip.checkCreate(BaseBO.INVALID_CASE_ID);
		Assert.assertEquals(err, Vip.FIELD_ERROR_sex);
		vip.setSex(EnumSexVip.ESV_Unknown.getIndex() + 1);
		err = vip.checkCreate(BaseBO.INVALID_CASE_ID);
		Assert.assertEquals(err, Vip.FIELD_ERROR_sex);
		vip.setSex(EnumSexVip.ESV_Female.getIndex());
		err = vip.checkCreate(BaseBO.INVALID_CASE_ID);
		Assert.assertEquals(err, "");
		vip.setSex(EnumSexVip.ESV_Male.getIndex());
		err = vip.checkCreate(BaseBO.INVALID_CASE_ID);
		Assert.assertEquals(err, "");
		vip.setSex(EnumSexVip.ESV_Unknown.getIndex());
		err = vip.checkCreate(BaseBO.INVALID_CASE_ID);
		Assert.assertEquals(err, "");
		
		// bonus
		vip.setCardCode("1234567890123456");
		vip.setBonus(1);
		err = vip.checkCreate(BaseBO.INVALID_CASE_ID);
		Assert.assertEquals(err, Vip.FIELD_ERROR_BonusImported);
		//
		vip.setBonus(-999);
		err = vip.checkCreate(BaseBO.CASE_Vip_ImportFromOldSystem);
		Assert.assertEquals(err, Vip.FIELD_ERROR_bonusUpdated);
		//
		vip.setBonus(0);
		err = vip.checkCreate(BaseBO.CASE_Vip_ImportFromOldSystem);
		Assert.assertEquals(err, "");
		vip.setCardCode(null);
		err = vip.checkCreate(BaseBO.CASE_Vip_ImportFromOldSystem);
		Assert.assertEquals(err, "");
		vip.setCardCode("");
		err = vip.checkCreate(BaseBO.CASE_Vip_ImportFromOldSystem);
		Assert.assertEquals(err, "");
		vip.setCardCode("1a1");
		err = vip.checkCreate(BaseBO.CASE_Vip_ImportFromOldSystem);
		Assert.assertEquals(err, "");
		vip.setCardCode("123456789012345?");
		err = vip.checkCreate(BaseBO.CASE_Vip_ImportFromOldSystem);
		Assert.assertEquals(err, "");
		vip.setCardCode("123456789012345a");
		err = vip.checkCreate(BaseBO.CASE_Vip_ImportFromOldSystem);
		Assert.assertEquals(err, "");
		vip.setCardCode("123456789012a");
		err = vip.checkCreate(BaseBO.CASE_Vip_ImportFromOldSystem);
		Assert.assertEquals(err, "");
		vip.setCardCode("123456789012");
		err = vip.checkCreate(BaseBO.CASE_Vip_ImportFromOldSystem);
		Assert.assertEquals(err, "");
		// cardID
		vip.setCardID(-1);
		err = vip.checkCreate(BaseBO.CASE_Vip_ImportFromOldSystem);
		Assert.assertEquals(err, Vip.FIELD_ERROR_cardID);
		vip.setCardID(0);
		err = vip.checkCreate(BaseBO.CASE_Vip_ImportFromOldSystem);
		Assert.assertEquals(err, Vip.FIELD_ERROR_cardID);
		vip.setCardID(1);
		err = vip.checkCreate(BaseBO.CASE_Vip_ImportFromOldSystem);
		Assert.assertEquals(err, "");
		vip.setCardID(100);
		err = vip.checkCreate(BaseBO.CASE_Vip_ImportFromOldSystem);
		Assert.assertEquals(err, "");
	}

	@Test
	public void testCheckDelete() {
		Shared.printTestMethodStartInfo();

		Vip vip = new Vip();
		vip.setID(1);
		String error = vip.checkDelete(BaseBO.INVALID_CASE_ID);
		Assert.assertEquals(error, "");
		Shared.caseLog("测试ID");
		vip.setID(BaseAction.INVALID_ID);
		error = vip.checkDelete(BaseBO.INVALID_CASE_ID);
		Assert.assertEquals(error, FieldFormat.FIELD_ERROR_ID);
		vip.setID(1);
	}

	@Test
	public void testCheckUpdate() throws Exception {
		Shared.printTestMethodStartInfo();

		Vip vip = new Vip();
		vip.setID(1);
		vip.setDistrict("广东");
		vip.setCategory(VipCategory.DEFAULT_VipCategory_ID);
		vip.setBonus(0);
		String error = vip.checkUpdate(BaseBO.INVALID_CASE_ID);
		Assert.assertEquals(error, "");
		Shared.caseLog("测试ID");
		vip.setID(BaseAction.INVALID_ID);
		error = vip.checkUpdate(BaseBO.INVALID_CASE_ID);
		Assert.assertEquals(error, FieldFormat.FIELD_ERROR_ID);
		vip.setID(1);
		Shared.caseLog("测试区域");
		vip.setDistrict("");
		error = vip.checkUpdate(BaseBO.INVALID_CASE_ID);
		Assert.assertEquals(error, "");
		vip.setDistrict("1234567890123456789012345678901234567890");
		error = vip.checkUpdate(BaseBO.INVALID_CASE_ID);
		Assert.assertEquals(error, Vip.FIELD_ERROR_district);
		vip.setDistrict("广东");
		Shared.caseLog("测试会员分类");
		vip.setCategory(BaseAction.INVALID_ID);
		error = vip.checkUpdate(BaseBO.INVALID_CASE_ID);
		Assert.assertEquals(error, Vip.FEILD_ERROR_category);
		vip.setCategory(VipCategory.DEFAULT_VipCategory_ID);
		//
		Shared.caseLog("测试会员积分");
		vip.setBonus(-1);
		vip.setIsIncreaseBonus(EnumBoolean.EB_Yes.getIndex());
		error = vip.checkUpdate(BaseBO.CASE_Vip_UpdateBonus);
		Assert.assertEquals(error, Vip.FIELD_ERROR_bonusUpdated);
		vip.setBonus(10001);
		error = vip.checkUpdate(BaseBO.CASE_Vip_UpdateBonus);
		Assert.assertEquals(error, Vip.FIELD_ERROR_bonusUpdated);
		
		Shared.caseLog("测试修改生日");
		SimpleDateFormat sdf = new SimpleDateFormat(BaseAction.DATETIME_FORMAT_Default_Chinese);
		String date = "2020年12月30日  23:59:59";
		vip.setBirthday(sdf.parse(date));
		error = vip.checkUpdate(BaseBO.INVALID_CASE_ID);
		Assert.assertEquals(error, "");
		vip.setBirthday(null);
		error = vip.checkUpdate(BaseBO.INVALID_CASE_ID);
		Assert.assertEquals(error, "");
		// 使用积分
		vip.setBonus(-50);
		vip.setIsIncreaseBonus(EnumBoolean.EB_NO.getIndex());
		error = vip.checkUpdate(BaseBO.CASE_Vip_UpdateBonus);
		Assert.assertEquals(error, "");
	}

	@Test
	public void testCheckRetrieve1() {
		Shared.printTestMethodStartInfo();

		Vip vip = new Vip();
		vip.setID(1);
		String error = vip.checkRetrieve1(BaseBO.INVALID_CASE_ID);
		Assert.assertEquals(error, "");
		Shared.caseLog("测试ID");
		vip.setID(BaseAction.INVALID_ID);
		error = vip.checkRetrieve1(BaseBO.INVALID_CASE_ID);
		Assert.assertEquals(error, FieldFormat.FIELD_ERROR_ID);
		vip.setID(1);
	}

	@Test
	public void testCheckRetrieveN_CASE1() {
		Shared.printTestMethodStartInfo();

		Vip vip = new Vip();
		vip.setID(1);
		vip.setFieldToCheckUnique(VipMapperTest.CASE_CHECK_UNIQUE_VIP_PHONE);// 为1是检查手机号、为2是检查身份证、为3是检查微信号、为4是检查QQ号、为5是检查邮箱、为6是检查账号
		vip.setQueryKeyword("12345678910");// 包含手机号、身份证、微信号、QQ号、邮箱、账号
		String error = vip.checkRetrieveN(BaseBO.CASE_CheckUniqueField);
		Assert.assertEquals(error, "");
		Shared.caseLog("测试ID");
		vip.setID(0);// ID为0时，是不根据ID查询
		error = vip.checkRetrieveN(BaseBO.CASE_CheckUniqueField);
		Assert.assertEquals(error, "");
		vip.setID(BaseAction.INVALID_ID);
		error = vip.checkRetrieveN(BaseBO.CASE_CheckUniqueField);
		Assert.assertEquals(error, FieldFormat.FIELD_ERROR_ID);
		vip.setID(1);
		Shared.caseLog("测试setFieldToCheckUnique");
		vip.setFieldToCheckUnique(7);
		error = vip.checkRetrieveN(BaseBO.CASE_CheckUniqueField);
		Assert.assertEquals(error, Vip.FIELD_ERROR_fieldToCheckUnique);
		Shared.caseLog("测试手机号不合法");
		vip.setFieldToCheckUnique(1);
		vip.setQueryKeyword("123456790123456798");
		error = vip.checkRetrieveN(BaseBO.CASE_CheckUniqueField);
		Assert.assertEquals(error, Vip.FIELD_ERROR_CheckMobile);
		Shared.caseLog("测试身份证不合法");
		vip.setFieldToCheckUnique(2);
		vip.setQueryKeyword("12345678901234567890");
		error = vip.checkRetrieveN(BaseBO.CASE_CheckUniqueField);
		Assert.assertEquals(error, Vip.FIELD_ERROR_ICID);
		Shared.caseLog("测试邮箱不合法");
		vip.setFieldToCheckUnique(5);
		vip.setQueryKeyword("1234567890123456789012345678901234567890");
		error = vip.checkRetrieveN(BaseBO.CASE_CheckUniqueField);
		Assert.assertEquals(error, Vip.FIELD_ERROR_Email);
	}

	@Test
	public void testCheckRetrieveN_CASE2() {
		Shared.printTestMethodStartInfo();

		Vip vip = new Vip();
		vip.setMobile("12345678910");
		String error = vip.checkRetrieveN(BaseBO.CASE_Vip_RetrieveNByMobileOrVipCardSN);
		Assert.assertEquals(error, "");
		Shared.caseLog("测试手机号");
		vip.setMobile("12345");
		error = vip.checkRetrieveN(BaseBO.CASE_Vip_RetrieveNByMobileOrVipCardSN);
		Assert.assertEquals(error, Vip.FIELD_ERROR_RNByMobileOrVipCardSN_mobile);
		vip.setMobile("123456");
	}

	@Test
	public void testCheckRetrieveN_CASE3() {
		Shared.printTestMethodStartInfo();

		Vip vip = new Vip();
		vip.setCategory(VipCategory.DEFAULT_VipCategory_ID);
		vip.setDistrict("广东");
		vip.setMobile("12345678910");
		vip.setiCID("464646199804235568");
		vip.setEmail("123@xxx.com");
		vip.setPageIndex(1);
		vip.setPageSize(10);
		String error = vip.checkRetrieveN(BaseBO.INVALID_CASE_ID);
		Assert.assertEquals(error, "");
		// Shared.caseLog("测试IDInPOS");
		// vip.setIDInPOS(BaseAction.INVALID_ID);
		// error = vip.checkRetrieveN(BaseBO.INVALID_CASE_ID);
		// Assert.assertEquals(error, "");
		// vip.setIDInPOS(-2);
		// error = vip.checkRetrieveN(BaseBO.INVALID_CASE_ID);
		// Assert.assertEquals(error, Vip.FIELD_ERROR_IDInPOS);
		// vip.setIDInPOS(1);
		Shared.caseLog("测试Category");
		vip.setCategory(BaseAction.INVALID_ID);
		error = vip.checkRetrieveN(BaseBO.INVALID_CASE_ID);
		Assert.assertEquals(error, "");
		vip.setCategory(-2);
		error = vip.checkRetrieveN(BaseBO.INVALID_CASE_ID);
		Assert.assertEquals(error, Vip.FEILD_ERROR_category);
		vip.setCategory(VipCategory.DEFAULT_VipCategory_ID);
		Shared.caseLog("测试District");
		vip.setDistrict("广东12345678901234567890123456789012345678901234567890");
		error = vip.checkRetrieveN(BaseBO.INVALID_CASE_ID);
		Assert.assertEquals(error, Vip.FIELD_ERROR_district);
		vip.setDistrict("广东");
		Shared.caseLog("测试Mobile");
		vip.setMobile("12345678910sss");
		error = vip.checkRetrieveN(BaseBO.INVALID_CASE_ID);
		Assert.assertEquals(error, Vip.FIELD_ERROR_mobile);
		vip.setMobile("12345678910");
		Shared.caseLog("测试iCID");
		vip.setiCID("4646461998042355sss68");
		error = vip.checkRetrieveN(BaseBO.INVALID_CASE_ID);
		Assert.assertEquals(error, Vip.FIELD_ERROR_ICID);
		vip.setiCID("464646199804235568");
		Shared.caseLog("测试Email");
		vip.setEmail("123@xssfafasfasdfa13d1as3dsa21dsa31d3sadssfasfaxx.com");
		error = vip.checkRetrieveN(BaseBO.INVALID_CASE_ID);
		Assert.assertEquals(error, Vip.FIELD_ERROR_Email);
		vip.setEmail("123@xxx.com");
		Shared.caseLog("测试PageIndex");
		vip.setPageIndex(0);
		error = vip.checkRetrieveN(BaseBO.INVALID_CASE_ID);
		Assert.assertEquals(error, FieldFormat.FIELD_ERROR_Paging);
		vip.setPageIndex(1);
		Shared.caseLog("测试PageSize");
		vip.setPageSize(0);
		error = vip.checkRetrieveN(BaseBO.INVALID_CASE_ID);
		Assert.assertEquals(error, FieldFormat.FIELD_ERROR_Paging);
		vip.setPageSize(10);
	}

	@Test
	public void testCheckRetrieveNEx() {
		Shared.printTestMethodStartInfo();

		Vip vip = new Vip();
		String error = "";
		error = vip.checkRetrieveNEx(BaseBO.INVALID_CASE_ID);
		Assert.assertEquals(error, "");

		// ...需要增加case
	}

	@Test
	public void testCheckRetrieveNByMobileOrVipCardSNEx() throws InterruptedException {
		Shared.printTestMethodStartInfo();

		Vip vip = new Vip();
		String error = "";

		Shared.caseLog("手机号码和会员卡卡号都为空");
		error = vip.doCheckRetrieveN(BaseBO.CASE_Vip_RetrieveNByMobileOrVipCardSN);
		Assert.assertEquals(error, Vip.FIELD_ERROR_RNByMobileOrVipCardSN);

		Shared.caseLog("手机号码小于6位");
		vip.setMobile("12345");
		error = vip.doCheckRetrieveN(BaseBO.CASE_Vip_RetrieveNByMobileOrVipCardSN);
		Assert.assertEquals(error, Vip.FIELD_ERROR_RNByMobileOrVipCardSN_mobile);

		Shared.caseLog("手机号码大于11位");
		vip.setMobile("123456789101");
		error = vip.doCheckRetrieveN(BaseBO.CASE_Vip_RetrieveNByMobileOrVipCardSN);
		Assert.assertEquals(error, Vip.FIELD_ERROR_RNByMobileOrVipCardSN_mobile);
		vip.setMobile(Shared.generateStringByTime(9));

		Shared.caseLog("会员卡卡号大于16位");
		vip.setVipCardSN(Shared.generateStringByTime(9) + Shared.generateStringByTime(9));
		error = vip.doCheckRetrieveN(BaseBO.CASE_Vip_RetrieveNByMobileOrVipCardSN);
		Assert.assertEquals(error, Vip.FIELD_ERROR_RNByMobileOrVipCardSN_VipCardSN);
		vip.setVipCardSN(Shared.generateStringByTime(9));

		error = vip.doCheckRetrieveN(BaseBO.CASE_Vip_RetrieveNByMobileOrVipCardSN);
		Assert.assertEquals(error, "");
	}
}
