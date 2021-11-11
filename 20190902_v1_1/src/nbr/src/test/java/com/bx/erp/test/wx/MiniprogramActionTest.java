package com.bx.erp.test.wx;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.testng.Assert.assertTrue;

import java.io.File;
import java.util.List;

import javax.servlet.http.HttpSession;

import org.apache.maven.shared.utils.StringUtils;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.test.web.servlet.MvcResult;
import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import com.bx.erp.action.BaseAction;
import com.bx.erp.action.bo.BaseBO;
import com.bx.erp.model.Vip;
import com.bx.erp.model.VipCard;
import com.bx.erp.model.VipCardCode;
import com.bx.erp.model.BaseModel;
import com.bx.erp.model.Company;
import com.bx.erp.model.MpQrCode;
import com.bx.erp.model.Shop;
import com.bx.erp.model.ErrorInfo.EnumErrorCode;
import com.bx.erp.test.BaseActionTest;
import com.bx.erp.test.BaseCompanyTest;
import com.bx.erp.test.BaseVipCardCodeTest;
import com.bx.erp.test.BaseVipCardTest;
import com.bx.erp.test.BaseVipTest;
import com.bx.erp.test.Shared;

import net.sf.json.JSONObject;

public class MiniprogramActionTest extends BaseActionTest {

	// TODO 是否考虑换一个路径，否则会导致上传商品图片失败（CommoditySyncAction:1595）
	private final String QrCodeUrl = "D:/nbr/pic/mp/nbr/mp/nbr.jpg";

	@BeforeClass
	public void setup() {
		super.setUp();
	}

	@AfterClass
	public void tearDown() {
		super.tearDown();
	}

	private void setDecryptAttribute(Vip vip) throws InterruptedException {
		vip.setDecryptSessionCode(Shared.generateStringByTime(5));
		vip.setEncryptedPhone(Shared.generateStringByTime(5)); //
		vip.setEncryptedUnionID(Shared.generateStringByTime(5)); //
		vip.setIvPhone(Shared.generateStringByTime(5));
		vip.setIvUnionID(Shared.generateStringByTime(5));
	}

	@Test
	public void testLoginCase1() throws Exception {
		Shared.printTestMethodStartInfo();

		Shared.caseLog("会员登录");
		// 创建会员，
		Vip createVip = BaseVipTest.createViaMapper(BaseBO.INVALID_CASE_ID, BaseVipTest.DataInput.getVip(), EnumErrorCode.EC_NoError, Shared.DBName_Test);

		// 创建一种会员卡
		VipCard vipCard = BaseVipCardTest.DataInput.getVipCard();
		VipCard vipCardCreate = BaseVipCardTest.createViaMapper(vipCard);

		// 会员领取会员卡
		VipCardCode vipCardCode = BaseVipCardCodeTest.DataInput.getVipCardCode(createVip.getID(), vipCardCreate.getID());
		VipCardCode vipCardCodeCreate = BaseVipCardCodeTest.createViaMapper(vipCardCode, Shared.DBName_Test, BaseBO.INVALID_CASE_ID, EnumErrorCode.EC_NoError);

		// 会员进行登录
		setDecryptAttribute(createVip);
		createVip.setCompanySN(Shared.DB_SN_Test);
		String jsonString = com.alibaba.fastjson.JSONObject.toJSONString(createVip);
		//
		MvcResult result = (MvcResult) mvc.perform( //
				post("/miniprogram/loginEx.bx") //
						.content(jsonString) //
						.contentType(MediaType.APPLICATION_JSON) //
		)//
				.andExpect(status().isOk()) //
				.andDo(print()).andReturn(); //
		// 删除数据
		BaseVipCardCodeTest.deleteViaMapper(vipCardCodeCreate);
		BaseVipCardTest.deleteViaMapper(vipCardCreate);
		//
		Shared.checkJSONErrorCode(result, EnumErrorCode.EC_NoError);
		// 检查返回的数据是否正确 //... 后面可以编写相应的CP
		BaseModel vip = Shared.parse1Object(result, createVip, BaseAction.KEY_Object);
		Assert.assertTrue(createVip.compareTo(vip) == 0, Shared.CompareToErrorMsg);
		//
		Company company = (Company) Shared.parse1Object(result, new Company(), BaseAction.KEY_Object2);
		Assert.assertTrue(StringUtils.isEmpty(company.getBossPhone()), "Company信息没隐藏");
		//
		List<?> shopList = Shared.parseNObject(result, new Shop(), BaseAction.KEY_ObjectList2);
		Assert.assertTrue(shopList.size() > 0, "公司的门店信息没有返回");
		//
		String json = result.getResponse().getContentAsString();
		JSONObject o = JSONObject.fromObject(json);
		String mobile = o.getString(Vip.field.getFIELD_NAME_mobile());
		String openID = o.getString(Vip.field.getFIELD_NAME_openid());
		String unionID = o.getString(Vip.field.getFIELD_NAME_unionid());
		Assert.assertTrue(!StringUtils.isEmpty(mobile) && createVip.getMobile().equals(mobile) && !StringUtils.isEmpty(openID) && !StringUtils.isEmpty(unionID), "会员正常登录返回的数据不正确");
	}

	@Test
	public void testLoginCase2() throws Exception {
		Shared.printTestMethodStartInfo();

		Shared.caseLog("非会员登录（不带公司SN）");
		Vip vip = BaseVipTest.DataInput.getVip();
		setDecryptAttribute(vip);
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
		//
		BaseModel returnedVip = Shared.parse1Object(result, vip, BaseAction.KEY_Object);
		Assert.assertTrue(vip.compareTo(returnedVip) == 0, Shared.CompareToErrorMsg);
		//
		Company company = (Company) Shared.parse1Object(result, new Company(), BaseAction.KEY_Object2);
		Assert.assertTrue(StringUtils.isEmpty(company.getBossPhone()), "Company信息没隐藏");
		//
		List<?> shopList = Shared.parseNObject(result, new Shop(), BaseAction.KEY_ObjectList2);
		Assert.assertTrue(shopList.size() > 0, "公司的门店信息没有返回");
		//
		String json = result.getResponse().getContentAsString();
		JSONObject o = JSONObject.fromObject(json);
		String mobile = o.getString(Vip.field.getFIELD_NAME_mobile());
		String openID = o.getString(Vip.field.getFIELD_NAME_openid());
		String unionID = o.getString(Vip.field.getFIELD_NAME_unionid());
		Assert.assertTrue(!StringUtils.isEmpty(mobile) && vip.getMobile().equals(mobile) && !StringUtils.isEmpty(openID) && !StringUtils.isEmpty(unionID), "会员正常登录返回的数据不正确");
	}

	@Test
	public void testLoginCase3() throws Exception {
		Shared.printTestMethodStartInfo();

		Shared.caseLog("EncryptedUnionID,IvUnionID,EncryptedPhone等信息不存在");
		Vip vip = BaseVipTest.DataInput.getVip();
		String jsonString = com.alibaba.fastjson.JSONObject.toJSONString(vip);
		//
		MvcResult result = (MvcResult) mvc.perform( //
				post("/miniprogram/loginEx.bx") //
						.content(jsonString) //
						.contentType(MediaType.APPLICATION_JSON) //
		)//
				.andExpect(status().isOk()) //
				.andDo(print()).andReturn(); //
		// 只要传了mobile，并且mockServer = 1，不用传EncryptedUnionID,IvUnionID,EncryptedPhone也可以
		Shared.checkJSONErrorCode(result, EnumErrorCode.EC_NoError);
	}

	@Test
	public void testLoginCase4() throws Exception {
		Shared.printTestMethodStartInfo();

		Shared.caseLog("未传递手机号码");
		Vip vip = BaseVipTest.DataInput.getVip();
		setDecryptAttribute(vip);
		vip.setMobile("");
		String jsonString = com.alibaba.fastjson.JSONObject.toJSONString(vip);
		//
		MvcResult result = (MvcResult) mvc.perform( //
				post("/miniprogram/loginEx.bx") //
						.content(jsonString) //
						.contentType(MediaType.APPLICATION_JSON) //
		)//
				.andExpect(status().isOk()) //
				.andDo(print()).andReturn(); //
		Shared.checkJSONErrorCode(result, EnumErrorCode.EC_WrongFormatForInputField);
	}

	@Test
	public void testLoginCase5() throws Exception {
		Shared.printTestMethodStartInfo();

		Shared.caseLog("新会员(无会员卡)进行登录");
		// 创建会员，
		Vip createVip = BaseVipTest.createViaMapper(BaseBO.INVALID_CASE_ID, BaseVipTest.DataInput.getVip(), EnumErrorCode.EC_NoError, Shared.DBName_Test);
		setDecryptAttribute(createVip);
		createVip.setCompanySN(Shared.DB_SN_Test);
		String jsonString = com.alibaba.fastjson.JSONObject.toJSONString(createVip);
		//
		MvcResult result = (MvcResult) mvc.perform( //
				post("/miniprogram/loginEx.bx") //
						.content(jsonString) //
						.contentType(MediaType.APPLICATION_JSON) //
		)//
				.andExpect(status().isOk()) //
				.andDo(print()).andReturn(); //
		Shared.checkJSONErrorCode(result, EnumErrorCode.EC_NoSuchData);
	}

	@Test
	public void testLoginCase6() throws Exception {
		Shared.printTestMethodStartInfo();

		Shared.caseLog("普通用户登录却携带了不存在的公司编号");
		Vip vip = BaseVipTest.DataInput.getVip();
		setDecryptAttribute(vip);
		vip.setCompanySN(Shared.generateStringByTime(5));
		String jsonString = com.alibaba.fastjson.JSONObject.toJSONString(vip);
		//
		MvcResult result = (MvcResult) mvc.perform( //
				post("/miniprogram/loginEx.bx") //
						.content(jsonString) //
						.contentType(MediaType.APPLICATION_JSON) //
		)//
				.andExpect(status().isOk()) //
				.andDo(print()).andReturn(); //
		Shared.checkJSONErrorCode(result, EnumErrorCode.EC_OtherError);
	}

	@Test
	public void testLoginCase7() throws Exception {
		Shared.printTestMethodStartInfo();

		Shared.caseLog("此会员在多家商店进行了注册");

		// 创建会员A
		Vip createVip = BaseVipTest.createViaMapper(BaseBO.INVALID_CASE_ID, BaseVipTest.DataInput.getVip(), EnumErrorCode.EC_NoError, Shared.DBName_Test);
		// 创建一种会员卡
		VipCard vipCard = BaseVipCardTest.DataInput.getVipCard();
		VipCard vipCardCreate = BaseVipCardTest.createViaMapper(vipCard);
		// 会员领取会员卡
		VipCardCode vipCardCode = BaseVipCardCodeTest.DataInput.getVipCardCode(createVip.getID(), vipCardCreate.getID());
		VipCardCode vipCardCodeCreate = BaseVipCardCodeTest.createViaMapper(vipCardCode, Shared.DBName_Test, BaseBO.INVALID_CASE_ID, EnumErrorCode.EC_NoError);

		// 创建公司A
		Company company = BaseCompanyTest.DataInput.getCompany();
		Company createCompany = BaseCompanyTest.createCompanyViaAction(company, mvc, mapBO, sessionOP);
		// 公司A中创建会员A
		Vip vipCreate2 = BaseVipTest.createViaMapper(BaseBO.INVALID_CASE_ID, createVip, EnumErrorCode.EC_NoError, createCompany.getDbName());

		// 公司A中创建会员A的会员卡
		VipCardCode vipCardCode2 = BaseVipCardCodeTest.DataInput.getVipCardCode(vipCreate2.getID(), 1);
		//
		BaseVipCardCodeTest.createViaMapper(vipCardCode2, createCompany.getDbName(), BaseBO.INVALID_CASE_ID, EnumErrorCode.EC_NoError);
		// 会员A登录
		setDecryptAttribute(createVip);
		createVip.setCompanySN(Shared.DB_SN_Test);
		String jsonString = com.alibaba.fastjson.JSONObject.toJSONString(createVip);
		//
		MvcResult result = (MvcResult) mvc.perform( //
				post("/miniprogram/loginEx.bx") //
						.content(jsonString) //
						.contentType(MediaType.APPLICATION_JSON) //
		)//
				.andExpect(status().isOk()) //
				.andDo(print()).andReturn(); //
		//
		BaseVipCardCodeTest.deleteViaMapper(vipCardCodeCreate);
		BaseVipCardTest.deleteViaMapper(vipCardCreate);
		//
		Shared.checkJSONErrorCode(result);
		Assert.assertTrue(Shared.parseNObject(result, company, BaseAction.KEY_ObjectList).size() == 2, "此号码在俩家公司注册会员，返回的公司集合数目不正确");
		//
		List<?> shopList = Shared.parseNObject(result, new Shop(), BaseAction.KEY_ObjectList2);
		Assert.assertTrue(shopList.size() > 0, "公司的门店信息没有返回");
	}

	@Test
	public void testLoginCase8() throws Exception {
		Shared.printTestMethodStartInfo();

		Shared.caseLog("普通用户登录却携带了存在的公司编号");
		Vip vip = BaseVipTest.DataInput.getVip();
		setDecryptAttribute(vip);
		vip.setCompanySN(Shared.DB_SN_Test);
		String jsonString = com.alibaba.fastjson.JSONObject.toJSONString(vip);
		//
		MvcResult result = (MvcResult) mvc.perform( //
				post("/miniprogram/loginEx.bx") //
						.content(jsonString) //
						.contentType(MediaType.APPLICATION_JSON) //
		)//
				.andExpect(status().isOk()) //
				.andDo(print()).andReturn(); //
		Shared.checkJSONErrorCode(result);
	}

	@Test
	public void testSelectMyCompanyExCase1() throws Exception {
		Shared.printTestMethodStartInfo();

		Shared.caseLog("会话中不存在手机号码");
		//
		MvcResult result = (MvcResult) mvc.perform( //
				post("/miniprogram/selectMyCompanyEx.bx") //
						.contentType(MediaType.APPLICATION_JSON) //
		)//
				.andExpect(status().isOk()) //
				.andDo(print()).andReturn(); //

		Assert.assertTrue("".equals(result.getResponse().getContentAsString()), "会话无号码未进行处理");
	}

	@Test
	public void testSelectMyCompanyExCase2() throws Exception {
		Shared.printTestMethodStartInfo();

		Shared.caseLog("传递不存在的公司ID");
		Vip createVip = BaseVipTest.createViaMapper(BaseBO.INVALID_CASE_ID, BaseVipTest.DataInput.getVip(), EnumErrorCode.EC_NoError, Shared.DBName_Test);

		// 创建一种会员卡
		VipCard vipCard = BaseVipCardTest.DataInput.getVipCard();
		VipCard vipCardCreate = BaseVipCardTest.createViaMapper(vipCard);

		// 会员领取会员卡
		VipCardCode vipCardCode = BaseVipCardCodeTest.DataInput.getVipCardCode(createVip.getID(), vipCardCreate.getID());
		VipCardCode vipCardCodeCreate = BaseVipCardCodeTest.createViaMapper(vipCardCode, Shared.DBName_Test, BaseBO.INVALID_CASE_ID, EnumErrorCode.EC_NoError);
		//
		MvcResult result = (MvcResult) mvc.perform( //
				post("/miniprogram/selectMyCompanyEx.bx") //
						.param(Company.field.getFIELD_NAME_ID(), String.valueOf(Shared.BIG_ID)) //
						.contentType(MediaType.APPLICATION_JSON) //
						.session((MockHttpSession) Shared.getVipLoginSession(mvc, createVip, true)) //
		)//
				.andExpect(status().isOk()) //
				.andDo(print()).andReturn(); //
		// 删除数据
		BaseVipCardCodeTest.deleteViaMapper(vipCardCodeCreate);
		BaseVipCardTest.deleteViaMapper(vipCardCreate);
		//
		Assert.assertTrue("".equals(result.getResponse().getContentAsString()), "传递不存在的公司ID，能够查找到公司");
	}

	@Test
	public void testSelectMyCompanyExCase3() throws Exception {
		Shared.printTestMethodStartInfo();

		Shared.caseLog("会话中的会员，并不存在于新创建的公司中");
		Vip createVip = BaseVipTest.createViaMapper(BaseBO.INVALID_CASE_ID, BaseVipTest.DataInput.getVip(), EnumErrorCode.EC_NoError, Shared.DBName_Test);
		// 创建一种会员卡
		VipCard vipCard = BaseVipCardTest.DataInput.getVipCard();
		VipCard vipCardCreate = BaseVipCardTest.createViaMapper(vipCard);
		// 会员A领取会员卡
		VipCardCode vipCardCode = BaseVipCardCodeTest.DataInput.getVipCardCode(createVip.getID(), vipCardCreate.getID());
		VipCardCode vipCardCodeCreate = BaseVipCardCodeTest.createViaMapper(vipCardCode, Shared.DBName_Test, BaseBO.INVALID_CASE_ID, EnumErrorCode.EC_NoError);
		// vipA登录
		HttpSession sessionVip = Shared.getVipLoginSession(mvc, createVip, true);
		// 创建公司
		Company company = BaseCompanyTest.DataInput.getCompany();
		Company createCompany = BaseCompanyTest.createCompanyViaAction(company, mvc, mapBO, sessionOP);
		//
		MvcResult result = (MvcResult) mvc.perform( //
				post("/miniprogram/selectMyCompanyEx.bx") //
						.param(Company.field.getFIELD_NAME_ID(), String.valueOf(createCompany.getID())) //
						.contentType(MediaType.APPLICATION_JSON) //
						.session((MockHttpSession) sessionVip) //
		)//
				.andExpect(status().isOk()) //
				.andDo(print()).andReturn(); //

		BaseVipCardCodeTest.deleteViaMapper(vipCardCodeCreate);
		BaseVipCardTest.deleteViaMapper(vipCardCreate);

		Shared.checkJSONErrorCode(result, EnumErrorCode.EC_NoSuchData);
		//
	}

	// 没有会员卡，会登录失败，也就无法请求/miniprogram/selectMyCompanyEx.bx接口
	// @Test
	// public void testSelectMyCompanyExCase4() throws Exception {
	// Shared.printTestMethodStartInfo();
	//
	// Shared.caseLog("会员在此公司中并没有会员卡");
	// Vip createVip = BaseVipTest.createViaMapper(BaseBO.INVALID_CASE_ID,
	// BaseVipTest.DataInput.getVip(), EnumErrorCode.EC_NoError,
	// Shared.DBName_Test);
	// // 创建一种会员卡
	// VipCard vipCard = BaseVipCardTest.DataInput.getVipCard();
	// VipCard vipCardCreate = BaseVipCardTest.createViaMapper(vipCard);
	// // 会员A领取会员卡
	// VipCardCode vipCardCode =
	// BaseVipCardCodeTest.DataInput.getVipCardCode(createVip.getID(),
	// vipCardCreate.getID());
	// VipCardCode vipCardCodeCreate =
	// BaseVipCardCodeTest.createViaMapper(vipCardCode, Shared.DBName_Test);
	//
	// Company company = BaseCompanyTest.DataInput.getCompany();
	// Company createCompany = BaseCompanyTest.createCompanyViaAction(company, mvc,
	// mapBO, sessionOP);
	// // 公司A中创建会员A
	// BaseVipTest.createViaMapper(BaseBO.INVALID_CASE_ID, createVip,
	// EnumErrorCode.EC_NoError, createCompany.getDbName());
	// //
	// MvcResult result = (MvcResult) mvc.perform( //
	// post("/miniprogram/selectMyCompanyEx.bx") //
	// .param(Company.field.getFIELD_NAME_ID(),
	// String.valueOf(createCompany.getID())) //
	// .contentType(MediaType.APPLICATION_JSON) //
	// .session((MockHttpSession) Shared.getVipLoginSession(mvc, createVip, true))
	// //
	// )//
	// .andExpect(status().isOk()) //
	// .andDo(print()).andReturn(); //
	// Assert.assertTrue("".equals(result.getResponse().getContentAsString()),
	// "此会员有会员卡（DB中是没有的）");
	// //
	// BaseVipCardCodeTest.deleteViaMapper(vipCardCodeCreate);
	// BaseVipCardTest.deleteViaMapper(vipCardCreate);
	// }

	@Test
	public void testSelectMyCompanyExCase5() throws Exception {
		Shared.printTestMethodStartInfo();

		Shared.caseLog("会员选择一家公司（正常流程case）");
		// 创建会员A
		Vip createVip = BaseVipTest.createViaMapper(BaseBO.INVALID_CASE_ID, BaseVipTest.DataInput.getVip(), EnumErrorCode.EC_NoError, Shared.DBName_Test);
		// 创建一种会员卡
		VipCard vipCard = BaseVipCardTest.DataInput.getVipCard();
		VipCard vipCardCreate = BaseVipCardTest.createViaMapper(vipCard);

		// 会员领取会员卡
		VipCardCode vipCardCode = BaseVipCardCodeTest.DataInput.getVipCardCode(createVip.getID(), vipCardCreate.getID());
		VipCardCode vipCardCodeCreate = BaseVipCardCodeTest.createViaMapper(vipCardCode, Shared.DBName_Test, BaseBO.INVALID_CASE_ID, EnumErrorCode.EC_NoError);

		// 创建公司A
		Company company = BaseCompanyTest.DataInput.getCompany();
		Company createCompany = BaseCompanyTest.createCompanyViaAction(company, mvc, mapBO, sessionOP);

		// 公司A中创建会员A
		Vip createVip2 = BaseVipTest.createViaMapper(BaseBO.INVALID_CASE_ID, createVip, EnumErrorCode.EC_NoError, createCompany.getDbName());
		// 创建公司A会员A的会员卡
		VipCardCode vipCardCodeCreate2 = BaseVipCardCodeTest.DataInput.getVipCardCode(createVip2.getID(), 1);
		BaseVipCardCodeTest.createViaMapper(vipCardCodeCreate2, createCompany.getDbName(), BaseBO.INVALID_CASE_ID, EnumErrorCode.EC_NoError);
		// 选择一家公司
		MvcResult mr = (MvcResult) mvc.perform( //
				post("/miniprogram/selectMyCompanyEx.bx") //
						.param(Company.field.getFIELD_NAME_ID(), String.valueOf(Shared.DEFAULT_Company_ID)) //
						.contentType(MediaType.APPLICATION_JSON) //
						.session((MockHttpSession) Shared.getVipLoginSession(mvc, createVip2, true)) //
		)//
				.andExpect(status().isOk()) //
				.andDo(print()).andReturn(); //
		//
		BaseVipCardCodeTest.deleteViaMapper(vipCardCodeCreate);
		BaseVipCardTest.deleteViaMapper(vipCardCreate);
		//
		Shared.checkJSONErrorCode(mr);
		BaseModel returnedCompany = Shared.parse1Object(mr, new Company(), BaseAction.KEY_Object);
		Assert.assertTrue(returnedCompany.getID() == Shared.DEFAULT_Company_ID, "返回的公司ID不一致");
		//
		BaseModel returnedVip = Shared.parse1Object(mr, createVip, BaseAction.KEY_Object2);
		Assert.assertTrue(createVip.compareTo(returnedVip) == 0, Shared.CompareToErrorMsg);
		//
		List<?> shopList = Shared.parseNObject(mr, new Shop(), BaseAction.KEY_ObjectList2);
		Assert.assertTrue(shopList.size() > 0, "公司的门店信息没有返回");
	}

	@Test
	public void testGenerateQRCode1() throws Exception {
		Shared.printTestMethodStartInfo();

		Shared.caseLog("Case1 正常创建小程序码");
		// 先删除路径下原有的文件
		File file = new File(QrCodeUrl);
		file.delete();
		//
		MpQrCode mpQrCode = new MpQrCode();
		mpQrCode.setWidth(400);
		MvcResult mr = mvc.perform(//
				post("/miniprogram/generateQRCode.bx")//
						.contentType(MediaType.APPLICATION_JSON) //
						.param(MpQrCode.field.getFIELD_NAME_width(), String.valueOf(mpQrCode.getWidth())) //
						.session((MockHttpSession) sessionBoss) //
		).andExpect(status().isOk()).andDo(print()).andReturn();
		//
		Shared.checkJSONErrorCode(mr);
		//
		String json = mr.getResponse().getContentAsString();
		JSONObject o = JSONObject.fromObject(json);
		String qrCodeUrl = o.getString("qrCodeUrl");
		assertTrue(qrCodeUrl.length() > 0, "没有返回小程序码地址");
		//
		assertTrue(file.exists(), "小程序码没有正确创建");

	}

	@Test
	public void testGenerateQRCode2() throws Exception {
		Shared.printTestMethodStartInfo();

		Shared.caseLog("Case2 小程序码宽度小于280");
		//
		MpQrCode mpQrCode = new MpQrCode();
		mpQrCode.setWidth(100);
		MvcResult mr = mvc.perform(//
				post("/miniprogram/generateQRCode.bx")//
						.contentType(MediaType.APPLICATION_JSON) //
						.param(MpQrCode.field.getFIELD_NAME_width(), String.valueOf(mpQrCode.getWidth())) //
						.session((MockHttpSession) sessionBoss) //
		).andExpect(status().isOk()).andDo(print()).andReturn();
		//
		Shared.checkJSONErrorCode(mr, EnumErrorCode.EC_OtherError);

	}

	@Test
	public void testGenerateQRCode3() throws Exception {
		Shared.printTestMethodStartInfo();

		Shared.caseLog("Case3 小程序码宽度大于1280");
		//
		MpQrCode mpQrCode = new MpQrCode();
		mpQrCode.setWidth(2000);
		MvcResult mr = mvc.perform(//
				post("/miniprogram/generateQRCode.bx")//
						.contentType(MediaType.APPLICATION_JSON) //
						.param(MpQrCode.field.getFIELD_NAME_width(), String.valueOf(mpQrCode.getWidth())) //
						.session((MockHttpSession) sessionBoss) //
		).andExpect(status().isOk()).andDo(print()).andReturn();
		//
		Shared.checkJSONErrorCode(mr, EnumErrorCode.EC_OtherError);

	}
}
