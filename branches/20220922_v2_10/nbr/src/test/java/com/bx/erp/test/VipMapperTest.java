package com.bx.erp.test;

import static org.testng.Assert.assertTrue;

import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import com.bx.erp.action.BaseAction;
import com.bx.erp.action.bo.BaseBO;
import com.bx.erp.cache.CacheManager;
import com.bx.erp.model.BaseModel;
import com.bx.erp.model.BonusConsumeHistory;
import com.bx.erp.model.RetailTrade;
import com.bx.erp.model.Vip;
import com.bx.erp.model.VipCard;
import com.bx.erp.model.VipCardCode;
import com.bx.erp.model.VipCategory;
import com.bx.erp.model.BaseModel.EnumBoolean;
import com.bx.erp.model.CacheType.EnumCacheType;
import com.bx.erp.model.ErrorInfo;
import com.bx.erp.model.BonusRule;
import com.bx.erp.util.DataSourceContextHolder;
import com.bx.erp.util.DatetimeUtil;
import com.bx.erp.model.ErrorInfo.EnumErrorCode;
import com.bx.erp.model.Vip.EnumSexVip;

public class VipMapperTest extends BaseMapperTest {

	public static final String MOBILE = "17352645590";
	public static final String EROOR_MOBILE = "13545678110";
	public static final String CARD_CODE = "415430877106";
	public static int amountUnit; // 消费金额。以分为单位, 从DB中获取
	public static int increaseBonus; // 对应增加的积分, 从DB中获取
	public static int maxIncreaseBonus; // 单次最高增加积分, 从DB中获取
	private static VipCard vipCard;

	/** 检查会员手机号唯一字段是否已存在 */
	public static final int CASE_CHECK_UNIQUE_VIP_PHONE = 1;
	/** 检查会员身份证号唯一字段是否已存在 */
	public static final int CASE_CHECK_UNIQUE_VIP_ICID = 2;

	/** 检查会员微信号唯一字段是否已存在 */
	public static final int CASE_CHECK_UNIQUE_VIP_WECHAT = 3;

	/** 检查会员QQ号唯一字段是否已存在 */
	public static final int CASE_CHECK_UNIQUE_VIP_QQ = 4;

	/** 检查会员邮箱唯一字段是否已存在 */
	public static final int CASE_CHECK_UNIQUE_VIP_EMAIL = 5;

	/** 检查会员登录账号唯一字段是否已存在 */
	public static final int CASE_CHECK_UNIQUE_VIP_ACCOUNT = 6;

	/** 查询小于起始值的零售单ID */
	public static final int LESS_THEN_START_RETAIL_TREADE_ID_INSQLITE = 1;

	@BeforeClass
	public void setup() {
		// 获取会员卡信息
		ErrorInfo ecOut = new ErrorInfo();
		vipCard = (VipCard) CacheManager.getCache(Shared.DBName_Test, EnumCacheType.ECT_VipCard).read1(BaseAction.DEFAULT_VipCardID, BaseBO.SYSTEM, ecOut, Shared.DBName_Test);

		super.setUp();
		BonusRule bonusRuleR1;
		try {
			bonusRuleR1 = BaseBonusRuleTest.DataInput.getBonusRule(1);
			bonusRuleR1.setID(1);
			BonusRule bonusRuleR1DB = (BonusRule) BaseBonusRuleTest.retrieve1ViaMapper(bonusRuleR1, Shared.DBName_Test);
			amountUnit = bonusRuleR1DB.getAmountUnit();
			increaseBonus = bonusRuleR1DB.getIncreaseBonus();
			maxIncreaseBonus = bonusRuleR1DB.getMaxIncreaseBonus();
		} catch (CloneNotSupportedException e) {
			e.printStackTrace();
		}
	}

	@AfterClass
	public void tearDown() {
		super.tearDown();
	}

	@Test
	public void createTest1() throws CloneNotSupportedException, InterruptedException {
		Shared.printTestMethodStartInfo();

		// 正常添加
		Shared.caseLog("CASE1:正常添加iIDInPOS为-1");
		Vip vip = BaseVipTest.DataInput.getVip();
		vip.setConsumeTimes(0);
		vip.setConsumeAmount(0);
		Vip vipCreate = BaseVipTest.createViaMapper(BaseBO.INVALID_CASE_ID, vip, EnumErrorCode.EC_NoError, Shared.DBName_Test);

		// 检查初始积分
		BonusRule bonusRule = new BonusRule();
		bonusRule.setID(BaseBonusRuleTest.DEFAULT_BonusRule_ID);
		BonusRule bonusRuleInDB = (BonusRule) BaseBonusRuleTest.retrieve1ViaMapper(bonusRule, Shared.DBName_Test);
		Assert.assertTrue(vipCreate.getBonus() == bonusRuleInDB.getInitIncreaseBonus(), "新创建的会员,初始积分和积分规则表中的初始设置积分不一致");

		// 删除测试对象
		BaseVipTest.deleteViaMapper(vipCreate);
	}

	@Test
	public void createTest2() throws CloneNotSupportedException, InterruptedException {
		Shared.printTestMethodStartInfo();

		String message = "已存在相同身份证号的会员，创建失败";
		Shared.caseLog(message);
		Vip vip1 = BaseVipTest.DataInput.getVip();
		Vip vip1Create = BaseVipTest.createViaMapper(BaseBO.INVALID_CASE_ID, vip1, EnumErrorCode.EC_NoError, Shared.DBName_Test);
		Vip vip2 = BaseVipTest.DataInput.getVip();
		vip2.setiCID(vip1Create.getiCID());
		Map<String, Object> paramsForCreate2 = vip2.getCreateParam(BaseBO.INVALID_CASE_ID, vip2);
		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		vipMapper.create(paramsForCreate2); // ...
		System.out.println(paramsForCreate2.get(BaseAction.SP_OUT_PARAM_sErrorMsg).toString());
		Assert.assertTrue(EnumErrorCode.values()[Integer.parseInt(paramsForCreate2.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())] == EnumErrorCode.EC_BusinessLogicNotDefined,
				paramsForCreate2.get(BaseAction.SP_OUT_PARAM_sErrorMsg).toString());
		Assert.assertTrue(message.equals(paramsForCreate2.get(BaseAction.SP_OUT_PARAM_sErrorMsg).toString()), "错误码不正确：" + paramsForCreate2.get(BaseAction.SP_OUT_PARAM_sErrorMsg).toString());
		// 删除创建出来的测试对象
		BaseVipTest.deleteViaMapper(vip1Create);
	}

	@Test
	public void createTest5() throws CloneNotSupportedException, InterruptedException {
		Shared.printTestMethodStartInfo();

		String message = "已存在相同邮箱的会员，创建失败";
		Shared.caseLog(message);
		Vip vip1 = BaseVipTest.DataInput.getVip();
		Vip vip1Create = BaseVipTest.createViaMapper(BaseBO.INVALID_CASE_ID, vip1, EnumErrorCode.EC_NoError, Shared.DBName_Test);
		Vip vip2 = BaseVipTest.DataInput.getVip();
		vip2.setEmail(vip1Create.getEmail());
		Map<String, Object> paramsForCreate2 = vip2.getCreateParam(BaseBO.INVALID_CASE_ID, vip2);
		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		vipMapper.create(paramsForCreate2); // ...
		System.out.println(paramsForCreate2.get(BaseAction.SP_OUT_PARAM_sErrorMsg).toString());
		Assert.assertTrue(EnumErrorCode.values()[Integer.parseInt(paramsForCreate2.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())] == EnumErrorCode.EC_BusinessLogicNotDefined,
				paramsForCreate2.get(BaseAction.SP_OUT_PARAM_sErrorMsg).toString());
		Assert.assertTrue(message.equals(paramsForCreate2.get(BaseAction.SP_OUT_PARAM_sErrorMsg).toString()), "错误码不正确：" + paramsForCreate2.get(BaseAction.SP_OUT_PARAM_sErrorMsg).toString());
		// 删除创建出来的测试对象
		BaseVipTest.deleteViaMapper(vip1Create);
	}

	// account
	// @Test
	// public void createTest6() throws CloneNotSupportedException,
	// InterruptedException {
	// Shared.printTestMethodStartInfo();
	//
	// String message = "已存在相同登录账号的会员，创建失败";
	// Shared.caseLog(message);
	// Vip vip1 = BaseVipTest.DataInput.getVip();
	// Vip vip1Create = createVip(BaseBO.INVALID_CASE_ID, vip1,
	// EnumErrorCode.EC_NoError);
	// Vip vip2 = BaseVipTest.DataInput.getVip();
	// vip2.setAccount(vip1Create.getAccount());
	// Map<String, Object> paramsForCreate2 =
	// vip2.getCreateParam(BaseBO.INVALID_CASE_ID, vip2);
	// DataSourceContextHolder.setDbName(Shared.DBName_Test);
	// vipMapper.create(paramsForCreate2); // ...
	// System.out.println(paramsForCreate2.get(BaseAction.SP_OUT_PARAM_sErrorMsg).toString());
	// Assert.assertTrue(EnumErrorCode.values()[Integer.parseInt(paramsForCreate2.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())]
	// == EnumErrorCode.EC_BusinessLogicNotDefined,
	// paramsForCreate2.get(BaseAction.SP_OUT_PARAM_sErrorMsg).toString());
	// Assert.assertTrue(message.equals(paramsForCreate2.get(BaseAction.SP_OUT_PARAM_sErrorMsg).toString()),
	// "错误码不正确：" +
	// paramsForCreate2.get(BaseAction.SP_OUT_PARAM_sErrorMsg).toString());
	// // 删除创建出来的测试对象
	// deleteVIP(vip1Create);
	// }

	// LocalPosID被删除了
	// @Test
	// public void createTest7() throws CloneNotSupportedException,
	// InterruptedException {
	// Shared.printTestMethodStartInfo();
	//
	// Shared.caseLog("CASE7:当iIDInPOS不为-1的时候");
	// Vip vip8 = BaseVipTest.DataInput.getVip();
	// vip8.setLocalPosID(1);
	// Map<String, Object> paramsForCreate8 =
	// vip8.getCreateParam(BaseBO.INVALID_CASE_ID, vip8);
	// DataSourceContextHolder.setDbName(Shared.DBName_Test);
	// Vip vipCreate = (Vip) vipMapper.create(paramsForCreate8); // ...
	// Assert.assertEquals(EnumErrorCode.values()[Integer.parseInt(paramsForCreate8.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())],
	// EnumErrorCode.EC_NoError);
	// deleteVIP(vipCreate);
	// }

	@Test
	public void createTest8() throws CloneNotSupportedException, InterruptedException {
		Shared.printTestMethodStartInfo();

		String message = "不能使用不存在的VipCategory创建";
		Shared.caseLog("CASE8：" + message);
		Vip vip9 = BaseVipTest.DataInput.getVip();
		vip9.setCategory(BaseAction.INVALID_ID);
		Map<String, Object> paramsForCreate9 = vip9.getCreateParam(BaseBO.INVALID_CASE_ID, vip9);
		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		Vip vipCreate9 = (Vip) vipMapper.create(paramsForCreate9); // ...
		assertTrue(vipCreate9 == null);
		Assert.assertTrue(EnumErrorCode.values()[Integer.parseInt(paramsForCreate9.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())] == EnumErrorCode.EC_BusinessLogicNotDefined,
				paramsForCreate9.get(BaseAction.SP_OUT_PARAM_sErrorMsg).toString());
		Assert.assertTrue(message.equals(paramsForCreate9.get(BaseAction.SP_OUT_PARAM_sErrorMsg).toString()), paramsForCreate9.get(BaseAction.SP_OUT_PARAM_sErrorMsg).toString());

	}

	@Test
	public void createTest9() throws CloneNotSupportedException, InterruptedException {
		Shared.printTestMethodStartInfo();
		Shared.caseLog("CASE9：正常创建女性会员");
		Vip vipGet = BaseVipTest.DataInput.getVip();
		vipGet.setSex(EnumSexVip.ESV_Female.getIndex());
		Vip vipCreate = BaseVipTest.createViaMapper(BaseBO.INVALID_CASE_ID, vipGet, EnumErrorCode.EC_NoError, Shared.DBName_Test);
		Assert.assertTrue(vipCreate.getSex() == EnumSexVip.ESV_Female.getIndex(), "创建的不是女性会员");
		BaseVipTest.deleteViaMapper(vipCreate);
	}

	@Test
	public void createTest10() throws CloneNotSupportedException, InterruptedException {
		Shared.printTestMethodStartInfo();
		Shared.caseLog("CASE10：正常创建男性会员");
		Vip vipGet = BaseVipTest.DataInput.getVip();
		vipGet.setSex(EnumSexVip.ESV_Male.getIndex());
		Vip vipCreate = BaseVipTest.createViaMapper(BaseBO.INVALID_CASE_ID, vipGet, EnumErrorCode.EC_NoError, Shared.DBName_Test);
		Assert.assertTrue(vipCreate.getSex() == EnumSexVip.ESV_Male.getIndex(), "创建的不是男性会员");
		BaseVipTest.deleteViaMapper(vipCreate);
	}

	@Test
	public void createTest11() throws CloneNotSupportedException, InterruptedException {
		Shared.printTestMethodStartInfo();
		Shared.caseLog("CASE10：正常创建未知性别会员");
		Vip vipGet = BaseVipTest.DataInput.getVip();
		vipGet.setSex(EnumSexVip.ESV_Unknown.getIndex());
		Vip vipCreate = BaseVipTest.createViaMapper(BaseBO.INVALID_CASE_ID, vipGet, EnumErrorCode.EC_NoError, Shared.DBName_Test);
		Assert.assertTrue(vipCreate.getSex() == EnumSexVip.ESV_Unknown.getIndex(), "创建的不是未知性别会员");
		BaseVipTest.deleteViaMapper(vipCreate);
	}

	@Test
	public void createTest12() throws CloneNotSupportedException, InterruptedException {
		Shared.printTestMethodStartInfo();
		Shared.caseLog("CASE10：传入错误的会员性别");
		Vip vipGet = BaseVipTest.DataInput.getVip();
		vipGet.setSex(EnumSexVip.ESV_Female.getIndex() - 1);
		String err = vipGet.checkCreate(BaseAction.INVALID_ID);
		Assert.assertEquals(err, Vip.FIELD_ERROR_sex);
	}

	@Test
	public void createTest13() throws CloneNotSupportedException, InterruptedException {
		Shared.printTestMethodStartInfo();

		// 正常添加
		Shared.caseLog("CASE13:创建会员时，初始积分为积分规则的初始设置积分");
		Vip vip = BaseVipTest.DataInput.getVip();
		vip.setConsumeTimes(0);
		vip.setConsumeAmount(0);
		Vip vipCreate = BaseVipTest.createViaMapper(BaseBO.INVALID_CASE_ID, vip, EnumErrorCode.EC_NoError, Shared.DBName_Test);

		// 检查初始积分
		BonusRule bonusRule = new BonusRule();
		bonusRule.setID(BaseBonusRuleTest.DEFAULT_BonusRule_ID);
		BonusRule bonusRuleInDB = (BonusRule) BaseBonusRuleTest.retrieve1ViaMapper(bonusRule, Shared.DBName_Test);
		Assert.assertTrue(vipCreate.getBonus() == bonusRuleInDB.getInitIncreaseBonus(), "新创建的会员,初始积分和积分规则表中的初始设置积分不一致");

		// 删除测试对象
		BaseVipTest.deleteViaMapper(vipCreate);
	}

	@Test
	public void createTest14() throws Exception {
		Shared.printTestMethodStartInfo();

		Shared.caseLog("从旧商家导入数据创建会员时。初始积分为导入的数据");
		Vip vip = BaseVipTest.DataInput.getVip();
		Vip vipCreate = BaseVipTest.createViaMapper(BaseBO.CASE_Vip_ImportFromOldSystem, vip, EnumErrorCode.EC_NoError, Shared.DBName_Test);

		// 删除测试对象
		BaseVipTest.deleteViaMapper(vipCreate);
	}

	@Test
	public void retrieve1Test() throws CloneNotSupportedException, InterruptedException {
		Shared.printTestMethodStartInfo();

		System.out.println("\n------------------------ retrieve1 Vip Test ------------------------");

		// 正常添加
		Vip vip = BaseVipTest.DataInput.getVip();
		Vip vipCreate = BaseVipTest.createViaMapper(BaseBO.INVALID_CASE_ID, vip, EnumErrorCode.EC_NoError, Shared.DBName_Test); // ...

		System.out.println("\n------------------------ CASE1:用存在的ID查询------------------------");
		Map<String, Object> paramsForRetrieve1 = vip.getRetrieve1Param(BaseBO.INVALID_CASE_ID, vipCreate);
		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		Vip vipRetrieve1 = (Vip) vipMapper.retrieve1(paramsForRetrieve1);
		if (vipRetrieve1 == null) {
			assertTrue(false);
		}
		Assert.assertEquals(EnumErrorCode.values()[Integer.parseInt(paramsForRetrieve1.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())], EnumErrorCode.EC_NoError);

		System.out.println("\n------------------------ CASE2:用不存在的ID查询------------------------");
		vipCreate.setID(-22);
		Map<String, Object> paramsForRetrieve2 = vip.getRetrieve1Param(BaseBO.INVALID_CASE_ID, vipCreate);
		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		Vip vipRetrieve2 = (Vip) vipMapper.retrieve1(paramsForRetrieve2);
		assertTrue(vipRetrieve2 == null && EnumErrorCode.values()[Integer.parseInt(paramsForRetrieve2.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())] == EnumErrorCode.EC_NoError, "查询失败");

	}

	@Test
	public void retrieveNTest() throws CloneNotSupportedException, InterruptedException {
		Shared.printTestMethodStartInfo();

		System.out.println("\n------------------------ retrieveN Vip Test ------------------------");
		// 正常添加
		Vip vip = BaseVipTest.DataInput.getVip();
		Vip vipCreate = BaseVipTest.createViaMapper(BaseBO.INVALID_CASE_ID, vip, EnumErrorCode.EC_NoError, Shared.DBName_Test);
		System.out.println("\n------------------------ CASE1:用Status查询------------------------");
		Vip vip2 = new Vip();
		vip2.setPageIndex(1);
		vip2.setPageSize(10);
		Map<String, Object> paramsForRetrieveN = vip.getRetrieveNParam(BaseBO.INVALID_CASE_ID, vip2);
		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		List<BaseModel> list1 = vipMapper.retrieveN(paramsForRetrieveN);
		assertTrue(list1.size() > 0 && EnumErrorCode.values()[Integer.parseInt(paramsForRetrieveN.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())] == EnumErrorCode.EC_NoError, "查询失败！");

		// localPosID字段被删除胃
		// System.out.println("\n------------------------
		// CASE2:用Status和IDInPOS查询------------------------");
		// vip2.setLocalPosID(1);
		// Map<String, Object> paramsForRetrieveN2 =
		// vip.getRetrieveNParam(BaseBO.INVALID_CASE_ID, vip2);
		// DataSourceContextHolder.setDbName(Shared.DBName_Test);
		// vipMapper.retrieveN(paramsForRetrieveN2);
		// Assert.assertEquals(EnumErrorCode.values()[Integer.parseInt(paramsForRetrieveN2.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())],
		// EnumErrorCode.EC_NoError);

		System.out.println("\n------------------------ CASE3:用Status和District查询------------------------");
		vip2.setDistrict("广州");
		Map<String, Object> paramsForRetrieveN3 = vip.getRetrieveNParam(BaseBO.INVALID_CASE_ID, vip2);
		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		List<BaseModel> list3 = vipMapper.retrieveN(paramsForRetrieveN3);
		assertTrue(list3.size() > 0 && EnumErrorCode.values()[Integer.parseInt(paramsForRetrieveN3.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())] == EnumErrorCode.EC_NoError, "查询失败！");

		System.out.println("\n------------------------CASE4:用Status和Category查询------------------------");
		vip2.setDistrict(null);
		vip2.setCategory(VipCategory.DEFAULT_VipCategory_ID);
		Map<String, Object> paramsForRetrieveN4 = vip.getRetrieveNParam(BaseBO.INVALID_CASE_ID, vip2);
		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		List<BaseModel> list4 = vipMapper.retrieveN(paramsForRetrieveN4);
		assertTrue(list4.size() > 0 && EnumErrorCode.values()[Integer.parseInt(paramsForRetrieveN4.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())] == EnumErrorCode.EC_NoError, "查询失败！");
		//
		BaseVipTest.deleteViaMapper(vipCreate);
	}

	@Test
	public void retrieveNTestCase5() throws Exception {
		Shared.printTestMethodStartInfo();

		Shared.caseLog("case5:传递手机号码，查找此手机号码的会员");
		Vip vip = BaseVipTest.DataInput.getVip();
		Vip createVIP = BaseVipTest.createViaMapper(BaseBO.INVALID_CASE_ID, vip, EnumErrorCode.EC_NoError, Shared.DBName_Test);

		Vip queryVip = new Vip();
		queryVip.setMobile(createVIP.getMobile());
		List<BaseModel> vipList = BaseVipTest.retrieveNViaMapper(queryVip, BaseBO.INVALID_CASE_ID);
		Assert.assertTrue(vipList != null && vipList.size() == 1, "根据手机号查找会员失败");

		BaseVipTest.deleteViaMapper(createVIP);
	}

	@Test
	public void retrieveNTestCase6() throws Exception {
		Shared.printTestMethodStartInfo();

		Shared.caseLog("case6:传递不存在的手机号码，查找数据为空");
		Vip queryVip = new Vip();
		queryVip.setMobile(Shared.getValidStaffPhone());
		List<BaseModel> vipList = BaseVipTest.retrieveNViaMapper(queryVip, BaseBO.INVALID_CASE_ID);
		Assert.assertTrue(vipList != null && vipList.size() == 0, "根据不存在的手机号查找会员成功");
	}

	@Test
	public void retrieveNTestCase9() throws Exception {
		Shared.printTestMethodStartInfo();

		Shared.caseLog("case9:传递过长的SN,查找数据为空");

		Vip queryVip = new Vip();
		queryVip.setMobile(Shared.generateStringByTime(9) + "1");
		List<BaseModel> vipList = BaseVipTest.retrieveNViaMapper(queryVip, BaseBO.INVALID_CASE_ID);
		Assert.assertTrue(vipList != null && vipList.size() == 0, "传递过长的SN查找会员成功");
	}

	@Test
	public void updateTest1() throws CloneNotSupportedException, InterruptedException {
		Shared.printTestMethodStartInfo();

		Shared.caseLog("case1:正常修改");
		// 创建会员
		Vip vip = BaseVipTest.DataInput.getVip();
		Vip vipCreate = BaseVipTest.createViaMapper(BaseBO.INVALID_CASE_ID, vip, EnumErrorCode.EC_NoError, Shared.DBName_Test);
		// 修改会员
		Vip vip2 = BaseVipTest.DataInput.getVip();
		vip2.setID(vipCreate.getID());
		Vip vip3 = updateVIP(vip2, EnumErrorCode.EC_NoError);
		BaseVipTest.deleteViaMapper(vip3);
	}

	@Test
	public void updateTest2() throws CloneNotSupportedException, InterruptedException {
		Shared.printTestMethodStartInfo();

		Shared.caseLog("CASE2:输入一个不存在的Id进行修改");
		Vip vip = new Vip();
		vip.setID(Shared.BIG_ID);
		vip.setDistrict("广州");
		vip.setCategory(VipCategory.DEFAULT_VipCategory_ID);
		String error = vip.checkUpdate(BaseBO.INVALID_CASE_ID);
		Assert.assertEquals(error, "");
		//
		Map<String, Object> paramsForUpdate3 = vip.getUpdateParam(BaseBO.INVALID_CASE_ID, vip);
		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		vipMapper.update(paramsForUpdate3);
		Assert.assertEquals(EnumErrorCode.values()[Integer.parseInt(paramsForUpdate3.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())], EnumErrorCode.EC_NoError);
	}

	@Test
	public void updateTest3() throws CloneNotSupportedException, InterruptedException {
		
		Shared.printTestMethodStartInfo();

		Shared.caseLog("CAS3:修改会员生日");
		// 创建会员
		Vip vip = BaseVipTest.DataInput.getVip();
		Vip vipCreate = BaseVipTest.createViaMapper(BaseBO.INVALID_CASE_ID, vip, EnumErrorCode.EC_NoError, Shared.DBName_Test);
		// 修改会员
		Vip tmpUpdateVip = new Vip();
		tmpUpdateVip.setID(vipCreate.getID());
		tmpUpdateVip.setCategory(vipCreate.getCategory());
		Vip updateVip = updateVIP(tmpUpdateVip, EnumErrorCode.EC_NoError);
		//
		BaseVipTest.deleteViaMapper(updateVip);
	}
	
	@Test
	public void updateTest4() throws CloneNotSupportedException, InterruptedException {
		Shared.printTestMethodStartInfo();

		Shared.caseLog("CASE4:修改会员备注");
		// 创建会员
		Vip vipGet = BaseVipTest.DataInput.getVip();
		Vip vipGet2 = (Vip) vipGet.clone();
		Vip vipCreate = BaseVipTest.createViaMapper(BaseBO.INVALID_CASE_ID, vipGet, EnumErrorCode.EC_NoError, Shared.DBName_Test);
		// 修改会员
		vipGet2.setID(vipCreate.getID());
		vipGet2.setCategory(vipCreate.getCategory());
		vipGet2.setRemark(Shared.generateStringByTime(8));
		Vip vipUpdate = BaseVipTest.updateViaMapper(BaseBO.INVALID_CASE_ID, vipGet2);
		Vip vipR1 = BaseVipTest.retrieve1ViaMapper(vipCreate, Shared.DBName_Test);
		Assert.assertTrue(vipUpdate.getRemark().equals(vipR1.getRemark()), "修改会员备注失败");
		//
		BaseVipTest.deleteViaMapper(vipUpdate);
	}

	@Test
	public void updateBonusTest1() throws CloneNotSupportedException, InterruptedException {
		Shared.printTestMethodStartInfo();

		Shared.caseLog("case1:正常增加积分");
		// 创建会员
		Vip vip = BaseVipTest.DataInput.getVip();
		Vip vipCreate = BaseVipTest.createViaMapper(BaseBO.INVALID_CASE_ID, vip, EnumErrorCode.EC_NoError, Shared.DBName_Test);
		Vip vipToUpdate = (Vip) vipCreate.clone();
		vipToUpdate.setAmount(5000);
		vipToUpdate.setIsIncreaseBonus(EnumBoolean.EB_Yes.getIndex());
		// 修改会员积分
		Vip vipUpdate = BaseVipTest.updateBonusViaMapper(vipToUpdate, Shared.DBName_Test, EnumErrorCode.EC_NoError);
		//
		vipToUpdate.setBonus(vipCreate.getBonus() + vipToUpdate.getAmount() / amountUnit * increaseBonus);
		if (vipUpdate.compareTo(vipToUpdate) == -1) {
			Assert.assertTrue(false, "修改会员积分失败，积分和期望结果不一致");
		}
		BaseVipTest.deleteViaMapper(vipCreate);
	}

	@Test
	public void updateBonusTest2() throws CloneNotSupportedException, InterruptedException {
		Shared.printTestMethodStartInfo();

		Shared.caseLog("case2:增加的积分超过单次获取积分上限");
		// 创建会员
		Vip vip = BaseVipTest.DataInput.getVip();
		Vip vipCreate = BaseVipTest.createViaMapper(BaseBO.INVALID_CASE_ID, vip, EnumErrorCode.EC_NoError, Shared.DBName_Test);
		Vip vipToUpdate = (Vip) vipCreate.clone();
		vipToUpdate.setAmount(5000000);
		vipToUpdate.setIsIncreaseBonus(EnumBoolean.EB_Yes.getIndex());
		// 修改会员积分
		Vip vipUpdate = BaseVipTest.updateBonusViaMapper(vipToUpdate, Shared.DBName_Test, EnumErrorCode.EC_NoError);
		//
		vipToUpdate.setBonus(vipCreate.getBonus() + maxIncreaseBonus);
		if (vipUpdate.compareTo(vipToUpdate) == -1) {
			Assert.assertTrue(false, "修改会员积分失败，积分和期望结果不一致");
		}

		BaseVipTest.deleteViaMapper(vipCreate);
	}

	@Test
	public void updateBonusTest3() throws CloneNotSupportedException, InterruptedException {
		Shared.printTestMethodStartInfo();

		Shared.caseLog("case3:vipID不存在");
		// 创建会员
		Vip vip = BaseVipTest.DataInput.getVip();
		// 修改会员积分
		Vip vipUpdate = BaseVipTest.updateBonusViaMapper(vip, Shared.DBName_Test, EnumErrorCode.EC_NoSuchData);
		Assert.assertTrue(vipUpdate == null);
	}

	@Test
	public void updateBonusTest4() throws CloneNotSupportedException, InterruptedException {
		Shared.printTestMethodStartInfo();

		Shared.caseLog("case4:使用积分兑换优惠券");
		// 创建会员
		Vip vip = BaseVipTest.DataInput.getVip();
		Vip vipCreate = BaseVipTest.createViaMapper(BaseBO.INVALID_CASE_ID, vip, EnumErrorCode.EC_NoError, Shared.DBName_Test);
		Vip vipToUpdate = (Vip) vipCreate.clone();
		vipToUpdate.setAmount(5000);
		vipToUpdate.setIsIncreaseBonus(EnumBoolean.EB_Yes.getIndex());
		// 增加会员积分
		Vip vipUpdate = BaseVipTest.updateBonusViaMapper(vipToUpdate, Shared.DBName_Test, EnumErrorCode.EC_NoError);
		//
		vipToUpdate.setBonus(vipCreate.getBonus() + vipToUpdate.getAmount() / amountUnit * increaseBonus);
		if (vipUpdate.compareTo(vipToUpdate) == -1) {
			Assert.assertTrue(false, "修改会员积分失败，积分和期望结果不一致");
		}

		// 使用积分
		vipToUpdate = (Vip) vipUpdate.clone();
		vipToUpdate.setAmount(0);
		vipToUpdate.setBonus(-50);
		vipToUpdate.setIsIncreaseBonus(EnumBoolean.EB_NO.getIndex());
		//
		Vip vipUpdate2 = BaseVipTest.updateBonusViaMapper(vipToUpdate, Shared.DBName_Test, EnumErrorCode.EC_NoError);
		vipToUpdate.setBonus(vipUpdate.getBonus() + vipToUpdate.getBonus());
		if (vipUpdate2.compareTo(vipToUpdate) == -1) {
			Assert.assertTrue(false, "修改会员积分失败，积分和期望结果不一致");
		}

		BaseVipTest.deleteViaMapper(vipCreate);
	}

	@Test
	public void updateBonusTest5() throws CloneNotSupportedException, InterruptedException {
		Shared.printTestMethodStartInfo();

		Shared.caseLog("case5:使用的积分超过会员原本的积分");
		// 创建会员
		Vip vip = BaseVipTest.DataInput.getVip();
		Vip vipCreate = BaseVipTest.createViaMapper(BaseBO.INVALID_CASE_ID, vip, EnumErrorCode.EC_NoError, Shared.DBName_Test);
		Vip vipToUpdate = (Vip) vipCreate.clone();
		vipToUpdate.setAmount(0);
		vipToUpdate.setBonus(-50);
		vipToUpdate.setIsIncreaseBonus(EnumBoolean.EB_NO.getIndex());
		BaseVipTest.updateBonusViaMapper(vipToUpdate, Shared.DBName_Test, EnumErrorCode.EC_BusinessLogicNotDefined);
		// 修改会员积分
		BaseVipTest.deleteViaMapper(vipCreate);
	}

	@Test
	public void updateBonusTest6() throws CloneNotSupportedException, InterruptedException {
		Shared.printTestMethodStartInfo();

		Shared.caseLog("case6:老板进行会员积分修改，积分数量正常");
		// 创建会员
		Vip vip = BaseVipTest.DataInput.getVip();
		Vip vipCreate = BaseVipTest.createViaMapper(BaseBO.INVALID_CASE_ID, vip, EnumErrorCode.EC_NoError, Shared.DBName_Test);
		Vip vipToUpdate = (Vip) vipCreate.clone();
		vipToUpdate.setStaffID(STAFF_ID3);
		vipToUpdate.setAmount(0);
		vipToUpdate.setBonus(500);
		vipToUpdate.setManuallyAdded(EnumBoolean.EB_Yes.getIndex());
		vipToUpdate.setRemarkForBonusHistory("退货积分变动500");
		// 修改会员积分
		Vip vipUpdate = BaseVipTest.updateBonusViaMapper(vipToUpdate, Shared.DBName_Test, EnumErrorCode.EC_NoError);
		//
		if (vipUpdate.compareTo(vipToUpdate) == -1) {
			Assert.assertTrue(false, "修改会员积分失败，积分和期望结果不一致");
		}
		BaseVipTest.deleteViaMapper(vipCreate);
	}

	@Test
	public void updateBonusTest7() throws CloneNotSupportedException, InterruptedException {
		Shared.printTestMethodStartInfo();

		Shared.caseLog("case7:老板进行会员积分修改，积分数量是负数");
		// 创建会员
		Vip vip = BaseVipTest.DataInput.getVip();
		Vip vipCreate = BaseVipTest.createViaMapper(BaseBO.INVALID_CASE_ID, vip, EnumErrorCode.EC_NoError, Shared.DBName_Test);
		Vip vipToUpdate = (Vip) vipCreate.clone();
		vipToUpdate.setStaffID(STAFF_ID3);
		vipToUpdate.setAmount(0);
		vipToUpdate.setBonus(-500);
		vipToUpdate.setManuallyAdded(EnumBoolean.EB_Yes.getIndex());
		vipToUpdate.setRemarkForBonusHistory("退货积分变动-500");
		// 修改会员积分
		BaseVipTest.updateBonusViaMapper(vipToUpdate, Shared.DBName_Test, EnumErrorCode.EC_BusinessLogicNotDefined);
		BaseVipTest.deleteViaMapper(vipCreate);
	}

	@Test
	public void updateBonusTest8() throws CloneNotSupportedException, InterruptedException {
		Shared.printTestMethodStartInfo();

		Shared.caseLog("case8:消费增加积分，如果金额为0，不做积分操作");
		// 创建会员
		Vip vip = BaseVipTest.DataInput.getVip();
		Vip vipCreate = BaseVipTest.createViaMapper(BaseBO.INVALID_CASE_ID, vip, EnumErrorCode.EC_NoError, Shared.DBName_Test);
		Vip vipToUpdate = (Vip) vipCreate.clone();
		vipToUpdate.setAmount(0);
		vipToUpdate.setIsIncreaseBonus(EnumBoolean.EB_Yes.getIndex());
		// 修改会员积分
		Vip vipUpdate = BaseVipTest.updateBonusViaMapper(vipToUpdate, Shared.DBName_Test, EnumErrorCode.EC_NoError);
		//
		if (vipUpdate.compareTo(vipToUpdate) == -1) {
			Assert.assertTrue(false, "修改会员积分失败，积分和期望结果不一致");
		}
		//
		// 验证积分历史
		BonusConsumeHistory bonusConsumeHistoryToRN = new BonusConsumeHistory();
		bonusConsumeHistoryToRN.setVipID(vipCreate.getID());
		List<BaseModel> bonusConsumeHistoryRetrieveN = BaseBonusConsumeHistoryTest.retrieveNViaMapper(bonusConsumeHistoryToRN, Shared.DBName_Test, EnumErrorCode.EC_NoError);
		Assert.assertTrue(bonusConsumeHistoryRetrieveN != null //
				&& bonusConsumeHistoryRetrieveN.size() == 0, "修改积分失败");
		//
		BaseVipTest.deleteViaMapper(vipCreate);
	}
	
	@Test
	public void updateBonusTest9() throws CloneNotSupportedException, InterruptedException {
		Shared.printTestMethodStartInfo();

		Shared.caseLog("case9:要增加的积分超过单次可获取的积分上限MaxIncreaseBonus，只增加MaxIncreaseBonus（不是老板操作）");
		Vip vip = BaseVipTest.DataInput.getVip();
		Vip vipCreate = BaseVipTest.createViaMapper(BaseBO.INVALID_CASE_ID, vip, EnumErrorCode.EC_NoError, Shared.DBName_Test);
		Vip vipToUpdate = (Vip) vipCreate.clone();
		vipToUpdate.setAmount((maxIncreaseBonus + 1) * 100);
		vipToUpdate.setIsIncreaseBonus(EnumBoolean.EB_Yes.getIndex());
		// 修改会员积分
		Vip vipUpdate = BaseVipTest.updateBonusViaMapper(vipToUpdate, Shared.DBName_Test, EnumErrorCode.EC_NoError);
		//
		// 验证积分历史
		BonusConsumeHistory bonusConsumeHistoryToRN = new BonusConsumeHistory();
		bonusConsumeHistoryToRN.setVipID(vipCreate.getID());
		List<BaseModel> bonusConsumeHistoryRetrieveN = BaseBonusConsumeHistoryTest.retrieveNViaMapper(bonusConsumeHistoryToRN, Shared.DBName_Test, EnumErrorCode.EC_NoError);
		Assert.assertTrue(bonusConsumeHistoryRetrieveN != null //
				&& bonusConsumeHistoryRetrieveN.size() == 1, "修改积分失败");
		//
		Assert.assertTrue(vipUpdate.getBonus() == maxIncreaseBonus, "修改会员积分失败，积分和期望结果不一致");
		BaseVipTest.deleteViaMapper(vipCreate);
	}
	
	@Test
	public void updateBonusTest10() throws CloneNotSupportedException, InterruptedException {
		Shared.printTestMethodStartInfo();

		Shared.caseLog("case10:要增加的积分IncreaseBonus超过单次可获取的积分上限MaxIncreaseBonus，少于10000，增加IncreaseBonus（老板操作）");
		Vip vip = BaseVipTest.DataInput.getVip();
		Vip vipCreate = BaseVipTest.createViaMapper(BaseBO.INVALID_CASE_ID, vip, EnumErrorCode.EC_NoError, Shared.DBName_Test);
		Vip vipToUpdate = (Vip) vipCreate.clone();
		vipToUpdate.setBonus(maxIncreaseBonus + 1);
		vipToUpdate.setIsIncreaseBonus(EnumBoolean.EB_Yes.getIndex());
		vipToUpdate.setManuallyAdded(EnumBoolean.EB_Yes.getIndex());
		// 修改会员积分
		Vip vipUpdate = BaseVipTest.updateBonusViaMapper(vipToUpdate, Shared.DBName_Test, EnumErrorCode.EC_NoError);
		//
		// 验证积分历史
		BonusConsumeHistory bonusConsumeHistoryToRN = new BonusConsumeHistory();
		bonusConsumeHistoryToRN.setVipID(vipCreate.getID());
		List<BaseModel> bonusConsumeHistoryRetrieveN = BaseBonusConsumeHistoryTest.retrieveNViaMapper(bonusConsumeHistoryToRN, Shared.DBName_Test, EnumErrorCode.EC_NoError);
		Assert.assertTrue(bonusConsumeHistoryRetrieveN != null //
				&& bonusConsumeHistoryRetrieveN.size() == 1, "修改积分失败");
		//
		Assert.assertTrue(vipUpdate.getBonus() == maxIncreaseBonus + 1, "修改会员积分失败，积分和期望结果不一致");
		BaseVipTest.deleteViaMapper(vipCreate);
	}
	
	@Test
	public void updateBonusTest11() throws CloneNotSupportedException, InterruptedException {
		Shared.printTestMethodStartInfo();

		Shared.caseLog("case11:要增加的积分IncreaseBonus超过单次可获取的积分上限MaxIncreaseBonus，大于10000，修改失败（老板操作）");
		Vip vip = BaseVipTest.DataInput.getVip();
		Vip vipCreate = BaseVipTest.createViaMapper(BaseBO.INVALID_CASE_ID, vip, EnumErrorCode.EC_NoError, Shared.DBName_Test);
		Vip vipToUpdate = (Vip) vipCreate.clone();
		vipToUpdate.setBonus(Vip.MAX_Bonus + 1);
		vipToUpdate.setIsIncreaseBonus(EnumBoolean.EB_Yes.getIndex());
		vipToUpdate.setManuallyAdded(EnumBoolean.EB_Yes.getIndex());
		String error = vipToUpdate.checkUpdate(BaseBO.CASE_Vip_UpdateBonus);
		Assert.assertEquals(error, Vip.FIELD_ERROR_bonusUpdated);
		BaseVipTest.deleteViaMapper(vipCreate);
	}

	public void addBonus(Vip vip) throws CloneNotSupportedException, InterruptedException { // 给会员增加1000积分
		Shared.printTestMethodStartInfo();
		vip.setAmountOnAddBonus(100000);
		vip.setBonusOnMinusAmount(0);
		vip.setIsIncreaseBonus(EnumBoolean.EB_Yes.getIndex());
		// 修改会员积分
		Map<String, Object> paramsForUpdate = vip.getUpdateParam(BaseBO.CASE_Vip_UpdateBonus, vip);
		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		Vip vipUpdate = (Vip) vipMapper.updateBonus(paramsForUpdate);
		if (vipUpdate.compareTo(vip) == 0) {
			Assert.assertTrue(false, "修改会员积分失败，修改后的会员信息跟修改前的会员信息一样,期望的是不一样");
		}

		Assert.assertTrue(vipUpdate != null && vipUpdate.getBonus() == 1000 && EnumErrorCode.values()[Integer.parseInt(paramsForUpdate.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())] == EnumErrorCode.EC_NoError, "返回的邮箱字段与预期的不一致");
	}

	@Test
	public void resetBonusTest1() throws CloneNotSupportedException, InterruptedException {
		Shared.printTestMethodStartInfo();

		Shared.caseLog("case1:根据固定天数进行积分清零，会员创建时间到今天未达到清零天数，不清零");
		// 创建会员
		Vip vip = BaseVipTest.DataInput.getVip();
		Vip vipCreate = BaseVipTest.createViaMapper(BaseBO.INVALID_CASE_ID, vip, EnumErrorCode.EC_NoError, Shared.DBName_Test);
		// 增加会员积分
		Vip vipToUpdate = (Vip) vipCreate.clone();
		vipToUpdate.setAmount(5000);
		vipToUpdate.setIsIncreaseBonus(EnumBoolean.EB_Yes.getIndex());
		Map<String, Object> paramsUpdateBonus = vipToUpdate.getUpdateParam(BaseBO.CASE_Vip_UpdateBonus, vipToUpdate);
		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		Vip vipUpdateInDB = (Vip) vipMapper.updateBonus(paramsUpdateBonus);
		//
		vipToUpdate.setBonus(vipCreate.getBonus() + vipToUpdate.getAmount() / amountUnit * increaseBonus);
		if (vipUpdateInDB.compareTo(vipToUpdate) == -1) {
			Assert.assertTrue(false, "修改会员积分失败，积分和期望结果不一致");
		}
		Assert.assertTrue(vipUpdateInDB != null && EnumErrorCode.values()[Integer.parseInt(paramsUpdateBonus.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())] == EnumErrorCode.EC_NoError, "修改积分失败");
		// 重置会员积分
		Map<String, Object> paramsResetBonus = vip.getUpdateParam(BaseBO.CASE_Vip_ResetBonus, vip);
		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		vipMapper.resetBonus(paramsResetBonus);
		//
		Assert.assertTrue(EnumErrorCode.values()[Integer.parseInt(paramsResetBonus.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())] == EnumErrorCode.EC_NoError, "测试结果与预期的不一致");
		// 查询会员信息进行结果验证
		Vip vipR1 = BaseVipTest.retrieve1ViaMapper(vipCreate, Shared.DBName_Test);
		Assert.assertTrue(vipR1.getBonus() == vipUpdateInDB.getBonus(), "会员积分不应该清零清零");
		// 删除创建的会员
		BaseVipTest.deleteViaMapper(vipCreate);
	}

	@Test
	public void resetBonusTest2() throws CloneNotSupportedException, InterruptedException {
		Shared.printTestMethodStartInfo();

		Shared.caseLog("case2:根据固定天数进行积分清零，会员创建时间到今天等于清零天数的N倍，积分清零");
		// 创建会员
		Vip vip = BaseVipTest.DataInput.getVip();
		vip.setCreateDatetime(DatetimeUtil.getDays(new Date(), vipCard.getClearBonusDay() * -2)); // -2可以是-1、-3之类的倍数
		Vip vipCreate = BaseVipTest.createViaMapper(BaseBO.INVALID_CASE_ID, vip, EnumErrorCode.EC_NoError, Shared.DBName_Test);
		// 增加会员积分
		Vip vipToUpdate = (Vip) vipCreate.clone();
		vipToUpdate.setAmount(5000);
		vipToUpdate.setIsIncreaseBonus(EnumBoolean.EB_Yes.getIndex());
		Map<String, Object> paramsUpdateBonus = vipToUpdate.getUpdateParam(BaseBO.CASE_Vip_UpdateBonus, vipToUpdate);
		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		Vip vipUpdateInDB = (Vip) vipMapper.updateBonus(paramsUpdateBonus);
		//
		vipToUpdate.setBonus(vipCreate.getBonus() + vipToUpdate.getAmount() / amountUnit * increaseBonus);
		if (vipUpdateInDB.compareTo(vipToUpdate) == -1) {
			Assert.assertTrue(false, "修改会员积分失败，积分和期望结果不一致");
		}
		Assert.assertTrue(vipUpdateInDB != null && EnumErrorCode.values()[Integer.parseInt(paramsUpdateBonus.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())] == EnumErrorCode.EC_NoError, "修改积分失败");
		// 重置会员积分
		Map<String, Object> paramsResetBonus = vip.getUpdateParam(BaseBO.CASE_Vip_ResetBonus, vip);
		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		vipMapper.resetBonus(paramsResetBonus);
		//
		Assert.assertTrue(EnumErrorCode.values()[Integer.parseInt(paramsResetBonus.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())] == EnumErrorCode.EC_NoError, "测试结果与预期的不一致");
		// 查询会员信息进行结果验证
		Vip vipR1 = BaseVipTest.retrieve1ViaMapper(vipCreate, Shared.DBName_Test);
		Assert.assertTrue(vipR1.getBonus() == 0, "会员积分没有正常清零");
		// 删除创建的会员
		BaseVipTest.deleteViaMapper(vipCreate);
	}

	@Test
	public void resetBonusTest3() throws CloneNotSupportedException, InterruptedException {
		Shared.printTestMethodStartInfo();

		Shared.caseLog("case3:根据固定天数进行积分清零，会员创建时间到今天超过清零天数但不能被整除，不清零");
		// 创建会员
		Vip vip = BaseVipTest.DataInput.getVip();
		vip.setCreateDatetime(DatetimeUtil.getDays(new Date(), vipCard.getClearBonusDay() * (-1) - 1));
		Vip vipCreate = BaseVipTest.createViaMapper(BaseBO.INVALID_CASE_ID, vip, EnumErrorCode.EC_NoError, Shared.DBName_Test);
		// 增加会员积分
		Vip vipToUpdate = (Vip) vipCreate.clone();
		vipToUpdate.setAmount(5000);
		vipToUpdate.setIsIncreaseBonus(EnumBoolean.EB_Yes.getIndex());
		Map<String, Object> paramsUpdateBonus = vipToUpdate.getUpdateParam(BaseBO.CASE_Vip_UpdateBonus, vipToUpdate);
		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		Vip vipUpdateInDB = (Vip) vipMapper.updateBonus(paramsUpdateBonus);
		//
		vipToUpdate.setBonus(vipCreate.getBonus() + vipToUpdate.getAmount() / amountUnit * increaseBonus);
		if (vipUpdateInDB.compareTo(vipToUpdate) == -1) {
			Assert.assertTrue(false, "修改会员积分失败，积分和期望结果不一致");
		}
		Assert.assertTrue(vipUpdateInDB != null && EnumErrorCode.values()[Integer.parseInt(paramsUpdateBonus.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())] == EnumErrorCode.EC_NoError, "修改积分失败");
		// 重置会员积分
		Map<String, Object> paramsResetBonus = vip.getUpdateParam(BaseBO.CASE_Vip_ResetBonus, vip);
		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		vipMapper.resetBonus(paramsResetBonus);
		//
		Assert.assertTrue(EnumErrorCode.values()[Integer.parseInt(paramsResetBonus.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())] == EnumErrorCode.EC_NoError, "测试结果与预期的不一致");
		// 查询会员信息进行结果验证
		Vip vipR1 = BaseVipTest.retrieve1ViaMapper(vipCreate, Shared.DBName_Test);
		Assert.assertTrue(vipR1.getBonus() == vipUpdateInDB.getBonus(), "会员积分不应该清零清零");
		// 删除创建的会员
		BaseVipTest.deleteViaMapper(vipCreate);
	}

	@Test
	public void resetBonusTest4() throws CloneNotSupportedException, InterruptedException {
		Shared.printTestMethodStartInfo();
		Shared.caseLog("case4:根据固定日期进行积分清零，会员当年创建，达到清零日期，积分清零");
		// 修改会员卡积分清零规则
		VipCard vipCardToUpdate = (VipCard) vipCard.clone();
		vipCardToUpdate.setClearBonusDatetime(new Date());
		vipCardToUpdate.setClearBonusDay(0);
		BaseVipCardTest.updateViaMapper(vipCardToUpdate);
		// 创建会员
		Vip vip = BaseVipTest.DataInput.getVip();
		Vip vipCreate = BaseVipTest.createViaMapper(BaseBO.INVALID_CASE_ID, vip, EnumErrorCode.EC_NoError, Shared.DBName_Test);
		// 增加会员积分
		Vip vipToUpdate = (Vip) vipCreate.clone();
		vipToUpdate.setAmount(5000);
		vipToUpdate.setIsIncreaseBonus(EnumBoolean.EB_Yes.getIndex());
		Map<String, Object> paramsUpdateBonus = vipToUpdate.getUpdateParam(BaseBO.CASE_Vip_UpdateBonus, vipToUpdate);
		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		Vip vipUpdate = (Vip) vipMapper.updateBonus(paramsUpdateBonus);
		//
		vipToUpdate.setBonus(vipCreate.getBonus() + vipToUpdate.getAmount() / amountUnit * increaseBonus);
		if (vipUpdate.compareTo(vipToUpdate) == -1) {
			Assert.assertTrue(false, "修改会员积分失败，积分和期望结果不一致");
		}
		Assert.assertTrue(vipUpdate != null && EnumErrorCode.values()[Integer.parseInt(paramsUpdateBonus.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())] == EnumErrorCode.EC_NoError, "修改积分失败");
		// 重置会员积分
		Map<String, Object> paramsResetBonus = vip.getUpdateParam(BaseBO.CASE_Vip_ResetBonus, vip);
		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		vipMapper.resetBonus(paramsResetBonus);
		//
		Assert.assertTrue(EnumErrorCode.values()[Integer.parseInt(paramsResetBonus.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())] == EnumErrorCode.EC_NoError, "测试结果与预期的不一致");
		// 查询会员信息进行结果验证
		Vip vipR1 = BaseVipTest.retrieve1ViaMapper(vipCreate, Shared.DBName_Test);
		Assert.assertTrue(vipR1.getBonus() == 0, "会员积分没有正常清零");
		// 删除创建的会员
		BaseVipTest.deleteViaMapper(vipCreate);
		// 还原会员卡积分清零规则
		BaseVipCardTest.updateViaMapper(vipCard);
	}

	@Test
	public void resetBonusTest5() throws CloneNotSupportedException, InterruptedException {
		Shared.printTestMethodStartInfo();
		Shared.caseLog("case5:根据固定日期进行积分清零，会员上一年创建，达到清零日期，积分清零");
		// 修改会员卡积分清零规则
		VipCard vipCardToUpdate = (VipCard) vipCard.clone();
		vipCardToUpdate.setClearBonusDatetime(new Date());
		vipCardToUpdate.setClearBonusDay(0);
		BaseVipCardTest.updateViaMapper(vipCardToUpdate);
		// 创建会员
		Vip vip = BaseVipTest.DataInput.getVip();
		Calendar cal = Calendar.getInstance();
		cal.setTime(new Date());
		cal.add(Calendar.YEAR, -1);
		vip.setCreateDatetime(cal.getTime());
		Vip vipCreate = BaseVipTest.createViaMapper(BaseBO.INVALID_CASE_ID, vip, EnumErrorCode.EC_NoError, Shared.DBName_Test);
		// 增加会员积分
		Vip vipToUpdate = (Vip) vipCreate.clone();
		vipToUpdate.setAmount(5000);
		vipToUpdate.setIsIncreaseBonus(EnumBoolean.EB_Yes.getIndex());
		Map<String, Object> paramsUpdateBonus = vipToUpdate.getUpdateParam(BaseBO.CASE_Vip_UpdateBonus, vipToUpdate);
		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		Vip vipUpdate = (Vip) vipMapper.updateBonus(paramsUpdateBonus);
		//
		vipToUpdate.setBonus(vipCreate.getBonus() + vipToUpdate.getAmount() / amountUnit * increaseBonus);
		if (vipUpdate.compareTo(vipToUpdate) == -1) {
			Assert.assertTrue(false, "修改会员积分失败，积分和期望结果不一致");
		}
		Assert.assertTrue(vipUpdate != null && EnumErrorCode.values()[Integer.parseInt(paramsUpdateBonus.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())] == EnumErrorCode.EC_NoError, "修改积分失败");
		// 重置会员积分
		Map<String, Object> paramsResetBonus = vip.getUpdateParam(BaseBO.CASE_Vip_ResetBonus, vip);
		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		vipMapper.resetBonus(paramsResetBonus);
		//
		Assert.assertTrue(EnumErrorCode.values()[Integer.parseInt(paramsResetBonus.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())] == EnumErrorCode.EC_NoError, "测试结果与预期的不一致");
		// 查询会员信息进行结果验证
		Vip vipR1 = BaseVipTest.retrieve1ViaMapper(vipCreate, Shared.DBName_Test);
		Assert.assertTrue(vipR1.getBonus() == 0, "会员积分没有正常清零");
		// 删除创建的会员
		BaseVipTest.deleteViaMapper(vipCreate);
		// 还原会员卡积分清零规则
		BaseVipCardTest.updateViaMapper(vipCard);
	}

	@Test
	public void deleteTest1() throws CloneNotSupportedException, InterruptedException {
		Shared.printTestMethodStartInfo();

		Shared.caseLog("CASE1:删除没有积分没有消费过的会员");
		// 创建Vip
		Vip vip = BaseVipTest.DataInput.getVip();
		Vip vipCreate = BaseVipTest.createViaMapper(BaseBO.INVALID_CASE_ID, vip, EnumErrorCode.EC_NoError, Shared.DBName_Test);
		// 删除Vip
		BaseVipTest.deleteViaMapper(vipCreate);
	}

	@Test
	public void testRetrieveNByMobileOrVipCardSNCase1() throws Exception {
		Shared.printTestMethodStartInfo();

		Shared.caseLog("CASE1：输入5位数以上的手机号码精确搜索vip");

		Vip vip = BaseVipTest.DataInput.getVip();
		Vip createVip = BaseVipTest.createViaMapper(BaseBO.INVALID_CASE_ID, vip, EnumErrorCode.EC_NoError, Shared.DBName_Test);

		Vip queryVip = new Vip();
		queryVip.setMobile(createVip.getMobile());
		Map<String, Object> params = vip.getRetrieveNParam(BaseBO.CASE_Vip_RetrieveNByMobileOrVipCardSN, queryVip);
		//
		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		List<BaseModel> vipRetrieveN = (List<BaseModel>) vipMapper.retrieveNByMobileOrVipCardSN(params);
		//
		Assert.assertTrue(vipRetrieveN != null && vipRetrieveN.size() == 1, "使用手机号码查找会员失败");
		for (BaseModel baseModel : vipRetrieveN) {
			if (createVip.compareTo(baseModel) != 0) {
				Assert.assertTrue(false, Shared.CompareToErrorMsg);
			}
		}
	}

	@Test
	public void testRetrieveNByMobileOrVipCardSNCase2() throws Exception {
		Shared.printTestMethodStartInfo();

		Shared.caseLog("case2:输入5位数以下的手机号码精确搜索vip");

		Vip queryVip = new Vip();
		queryVip.setMobile(Shared.generateStringByTime(4));
		Map<String, Object> params = queryVip.getRetrieveNParam(BaseBO.CASE_Vip_RetrieveNByMobileOrVipCardSN, queryVip);
		//
		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		List<BaseModel> vipRetrieveN = (List<BaseModel>) vipMapper.retrieveNByMobileOrVipCardSN(params);
		//
		Assert.assertTrue(vipRetrieveN != null && vipRetrieveN.size() == 0, "使用5位以下的手机号码查找会员失败");
	}

	@Test
	public void testRetrieveNByMobileOrVipCardSNCase3() throws Exception {
		Shared.printTestMethodStartInfo();

		Shared.caseLog("case3:不输入值返回所有会员信息");

		Vip queryVip = new Vip();
		Map<String, Object> params = queryVip.getRetrieveNParam(BaseBO.CASE_Vip_RetrieveNByMobileOrVipCardSN, queryVip);
		//
		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		List<BaseModel> vipRetrieveN = (List<BaseModel>) vipMapper.retrieveNByMobileOrVipCardSN(params);
		//
		Assert.assertTrue(vipRetrieveN != null && vipRetrieveN.size() > 0, "使用手机号码查找会员失败");
	}

	@Test
	public void testRetrieveNByMobileOrVipCardSNCase4() throws Exception {
		Shared.printTestMethodStartInfo();

		Shared.caseLog("case4:传递会员卡号号码，查找此卡号号码的会员");
		// 创建会员卡
		VipCard vipCard = BaseVipCardTest.DataInput.getVipCard();
		VipCard vipCardCreate = BaseVipCardTest.createViaMapper(vipCard);

		// 创建会员
		Vip createVip = BaseVipTest.createViaMapper(BaseBO.INVALID_CASE_ID, BaseVipTest.DataInput.getVip(), EnumErrorCode.EC_NoError, Shared.DBName_Test);

		// 领取一张会员卡
		VipCardCode vipCardCode = BaseVipCardCodeTest.DataInput.getVipCardCode(createVip.getID(), vipCardCreate.getID());
		VipCardCode createVipCardCode = BaseVipCardCodeTest.createViaMapper(vipCardCode, Shared.DBName_Test, BaseBO.INVALID_CASE_ID, EnumErrorCode.EC_NoError);

		Vip queryVip = new Vip();
		queryVip.setVipCardSN(createVipCardCode.getSN());
		Map<String, Object> params = queryVip.getRetrieveNParam(BaseBO.CASE_Vip_RetrieveNByMobileOrVipCardSN, queryVip);
		//
		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		List<BaseModel> vipRetrieveN = (List<BaseModel>) vipMapper.retrieveNByMobileOrVipCardSN(params);
		//
		Assert.assertTrue(vipRetrieveN != null && vipRetrieveN.size() == 1, "使用手机号码查找会员失败");
		Assert.assertTrue(createVip.compareTo(vipRetrieveN.get(0)) == 0, Shared.CompareToErrorMsg);

		// 删除数据
		BaseVipCardCodeTest.deleteViaMapper(createVipCardCode);
		BaseVipCardTest.deleteViaMapper(vipCardCreate);
		BaseVipTest.deleteViaMapper(createVip);
	}

	@Test
	public void testRetrieveNByMobileOrVipCardSNCase5() throws Exception {
		Shared.printTestMethodStartInfo();

		Shared.caseLog("case5:传递不存在的会员卡卡号号码，查找数据为空");

		Vip queryVip = new Vip();
		queryVip.setVipCardSN(Shared.generateStringByTime(9));
		Map<String, Object> params = queryVip.getRetrieveNParam(BaseBO.CASE_Vip_RetrieveNByMobileOrVipCardSN, queryVip);
		//
		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		List<BaseModel> vipRetrieveN = (List<BaseModel>) vipMapper.retrieveNByMobileOrVipCardSN(params);
		//
		Assert.assertTrue(vipRetrieveN != null && vipRetrieveN.size() == 0, "使用不存在的会员卡卡号查找会员失败");
	}

	@Test
	public void retrieveNVipConsumeHistoryTest() throws CloneNotSupportedException, InterruptedException {
		Shared.printTestMethodStartInfo();

		System.out.println("\n------------------------ RetrieveNVipConsumeHistoryTest Vip Test ------------------------");

		// 正常添加
		System.out.println("\n------------------------ 创建一个VIP ------------------------");
		Vip vip = BaseVipTest.DataInput.getVip();
		Vip vipCreate = BaseVipTest.createViaMapper(BaseBO.INVALID_CASE_ID, vip, EnumErrorCode.EC_NoError, Shared.DBName_Test); // ...

		System.out.println("\n------------------------ 创建一个零售单A ------------------------");

		RetailTrade rt = BaseRetailTradeTest.DataInput.getRetailTrade();
		rt.setVipID(vipCreate.getID());
		RetailTrade localRetailTrade = BaseRetailTradeTest.createRetailTrade(rt);

		System.out.println("\n------------------------ 创建一个零售单B ------------------------");

		RetailTrade rt2 = BaseRetailTradeTest.DataInput.getRetailTrade();
		rt2.setVipID(vipCreate.getID());
		RetailTrade localRetailTrade2 = BaseRetailTradeTest.createRetailTrade(rt2);

		System.out.println("\n------------------------ Case1:根据VipID查询零售单信息，默认起始值为大于0的零售单ID------------------------");
		Map<String, Object> paramsForRetrieveN = vip.getRetrieveNParamEx(BaseBO.CASE_Vip_RetrieveNVipConsumeHistory, vipCreate);
		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		List<List<BaseModel>> vipReteieveN = (List<List<BaseModel>>) vipMapper.retrieveNVipConsumeHistory(paramsForRetrieveN); //

		Assert.assertTrue(vipReteieveN != null && EnumErrorCode.values()[Integer.parseInt(paramsForRetrieveN.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())] == EnumErrorCode.EC_NoError, "创建Vip失败");

		System.out.println("\n------------------------ Case2:起始值为大于等于零售单A ID的零售单,返回零售单B------------------------");
		vipCreate.setStartRetailTreadeIDInSQLite(localRetailTrade.getID());
		Map<String, Object> paramsForRetrieveN3 = vip.getRetrieveNParamEx(BaseBO.CASE_Vip_RetrieveNVipConsumeHistory, vipCreate);
		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		List<List<BaseModel>> vipReteieveN3 = (List<List<BaseModel>>) vipMapper.retrieveNVipConsumeHistory(paramsForRetrieveN3); //
		if (localRetailTrade2.compareTo(vipReteieveN3.get(0).get(0)) != 0) {
			Assert.assertTrue(false, "创建的对象的字段与DB读出的不相等");
		}

		Assert.assertTrue(vipReteieveN3 != null && EnumErrorCode.values()[Integer.parseInt(paramsForRetrieveN3.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())] == EnumErrorCode.EC_NoError, "创建Vip失败");

		System.out.println("\n------------------------ Case3:起始值为小于零售单B ID的零售单,返回零售单A------------------------");
		vipCreate.setStartRetailTreadeIDInSQLite(localRetailTrade2.getID());
		vipCreate.setbQuerySmallerThanStartID(LESS_THEN_START_RETAIL_TREADE_ID_INSQLITE);// 查询小于起始值的零售单ID
		Map<String, Object> paramsForRetrieveN4 = vip.getRetrieveNParamEx(BaseBO.CASE_Vip_RetrieveNVipConsumeHistory, vipCreate);
		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		List<List<BaseModel>> vipReteieveN4 = (List<List<BaseModel>>) vipMapper.retrieveNVipConsumeHistory(paramsForRetrieveN4); //

		if (localRetailTrade.compareTo(vipReteieveN4.get(0).get(0)) != 0) {
			Assert.assertTrue(false, "创建的对象的字段与DB读出的不相等");
		}

		Assert.assertTrue(vipReteieveN4 != null && EnumErrorCode.values()[Integer.parseInt(paramsForRetrieveN4.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())] == EnumErrorCode.EC_NoError, "创建Vip失败");

		System.out.println("\n------------------------ Case4:VipID不存在------------------------");
		Vip v = new Vip();
		v.setID(-2);
		Map<String, Object> paramsForRetrieveN2 = vip.getRetrieveNParamEx(BaseBO.CASE_Vip_RetrieveNVipConsumeHistory, v);
		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		List<List<BaseModel>> vipReteieveN2 = (List<List<BaseModel>>) vipMapper.retrieveNVipConsumeHistory(paramsForRetrieveN2); //

		Assert.assertTrue(vipReteieveN2.size() == 0 && EnumErrorCode.values()[Integer.parseInt(paramsForRetrieveN2.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())] == EnumErrorCode.EC_NoSuchData, "创建Vip失败");

	}

	@Test
	public void checkUniqueFieldTest() throws Exception {
		System.out.println("------------------------Vip checkUniqueField Test ----------------------");

		System.out.println("----------------------------Case1:查询一个不存在的会员的手机号码-------------------------------------------");
		Vip vip1 = new Vip();
		vip1.setFieldToCheckUnique(CASE_CHECK_UNIQUE_VIP_PHONE);
		vip1.setQueryKeyword("33144496272");
		Map<String, Object> params1 = vip1.getRetrieveNParam(BaseBO.CASE_CheckUniqueField, vip1);
		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		vipMapper.checkUniqueField(params1);
		//
		assertTrue(EnumErrorCode.values()[Integer.parseInt(params1.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())] == EnumErrorCode.EC_NoError);

		System.out.println("----------------------------Case2:查询一个已存在的会员的手机号码-------------------------------------------");
		Vip vip2 = new Vip();
		vip2.setFieldToCheckUnique(CASE_CHECK_UNIQUE_VIP_PHONE);
		vip2.setQueryKeyword("13545678110");
		Map<String, Object> params2 = vip2.getRetrieveNParam(BaseBO.CASE_CheckUniqueField, vip2);
		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		vipMapper.checkUniqueField(params2);
		//
		assertTrue(EnumErrorCode.values()[Integer.parseInt(params2.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())] == EnumErrorCode.EC_Duplicated);

		System.out.println("----------------------------Case3:查询一个不存在的会员的身份证号-------------------------------------------");
		Vip vip3 = new Vip();
		vip3.setFieldToCheckUnique(CASE_CHECK_UNIQUE_VIP_ICID);
		vip3.setQueryKeyword("540883198412111666");
		Map<String, Object> params3 = vip3.getRetrieveNParam(BaseBO.CASE_CheckUniqueField, vip3);
		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		vipMapper.checkUniqueField(params3);
		//
		assertTrue(EnumErrorCode.values()[Integer.parseInt(params3.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())] == EnumErrorCode.EC_NoError);

		System.out.println("----------------------------Case4:查询一个已存在的会员的身份证号-------------------------------------------");
		Vip vip4 = new Vip();
		vip4.setFieldToCheckUnique(CASE_CHECK_UNIQUE_VIP_ICID);
		vip4.setQueryKeyword("320803199707016031");

		Map<String, Object> params4 = vip4.getRetrieveNParam(BaseBO.CASE_CheckUniqueField, vip4);
		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		vipMapper.checkUniqueField(params4);
		//
		assertTrue(EnumErrorCode.values()[Integer.parseInt(params4.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())] == EnumErrorCode.EC_Duplicated);

		System.out.println("----------------------------Case9:查询一个不存在的会员的邮箱-------------------------------------------");
		Vip vip9 = new Vip();
		vip9.setFieldToCheckUnique(CASE_CHECK_UNIQUE_VIP_EMAIL);
		vip9.setQueryKeyword("623456@bx.vip");
		Map<String, Object> params9 = vip9.getRetrieveNParam(BaseBO.CASE_CheckUniqueField, vip9);
		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		vipMapper.checkUniqueField(params9);
		//
		assertTrue(EnumErrorCode.values()[Integer.parseInt(params9.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())] == EnumErrorCode.EC_NoError);

		System.out.println("----------------------------Case10:查询一个已存在的会员的邮箱-------------------------------------------");
		Vip vip10 = new Vip();
		vip10.setFieldToCheckUnique(CASE_CHECK_UNIQUE_VIP_EMAIL);
		vip10.setQueryKeyword("123456@bx.vip");
		Map<String, Object> params10 = vip10.getRetrieveNParam(BaseBO.CASE_CheckUniqueField, vip10);
		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		vipMapper.checkUniqueField(params10);
		//
		assertTrue(EnumErrorCode.values()[Integer.parseInt(params10.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())] == EnumErrorCode.EC_Duplicated);

		System.out.println("----------------------------Case11:查询一个不存在的会员的登录账号-------------------------------------------");
		Vip vip11 = new Vip();
		vip11.setFieldToCheckUnique(CASE_CHECK_UNIQUE_VIP_ACCOUNT);
		vip11.setQueryKeyword("623456");
		Map<String, Object> params11 = vip11.getRetrieveNParam(BaseBO.CASE_CheckUniqueField, vip11);
		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		vipMapper.checkUniqueField(params11);
		//
		assertTrue(EnumErrorCode.values()[Integer.parseInt(params9.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())] == EnumErrorCode.EC_NoError);

		System.out.println("----------------------------Case12:查询一个已存在的会员的邮箱-------------------------------------------");
		Vip vip12 = new Vip();
		vip12.setFieldToCheckUnique(CASE_CHECK_UNIQUE_VIP_ACCOUNT);
		vip12.setQueryKeyword("123456");
		Map<String, Object> params12 = vip12.getRetrieveNParam(BaseBO.CASE_CheckUniqueField, vip12);
		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		vipMapper.checkUniqueField(params12);
		//
		assertTrue(EnumErrorCode.values()[Integer.parseInt(params12.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())] == EnumErrorCode.EC_Duplicated);
	}

	public Vip updateVIP(Vip vip, EnumErrorCode errorCode) {
		String error = vip.checkUpdate(BaseBO.INVALID_CASE_ID);
		Assert.assertEquals(error, "");
		//
		Map<String, Object> paramsForUpdate = vip.getUpdateParam(BaseBO.INVALID_CASE_ID, vip);
		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		Vip vipUpdate = (Vip) vipMapper.update(paramsForUpdate);
		Assert.assertEquals(EnumErrorCode.values()[Integer.parseInt(paramsForUpdate.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())], errorCode);
		//
		if (vipUpdate == null) {
			return vipUpdate;
		}
		if (vipUpdate.compareTo(vip) == 0) {
			Assert.assertTrue(false, "修改会员失败，修改后的会员信息跟修改前的会员信息一样,期望的是不一样");
		}
		//
		return vipUpdate;
	}
}
