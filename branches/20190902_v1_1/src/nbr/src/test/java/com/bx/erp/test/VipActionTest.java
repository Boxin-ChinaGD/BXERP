package com.bx.erp.test;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.testng.Assert.assertTrue;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Random;

import javax.servlet.http.HttpSession;

import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MvcResult;
import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import com.bx.erp.action.BaseAction;
import com.bx.erp.action.bo.BaseBO;
import com.bx.erp.model.RetailTrade;
import com.bx.erp.model.Vip;
import com.bx.erp.model.VipCard;
import com.bx.erp.model.VipCardCode;
import com.bx.erp.model.VipCategory;
import com.bx.erp.model.VipSource;
import com.bx.erp.model.BaseModel.EnumBoolean;
import com.bx.erp.model.BaseModel;
import com.bx.erp.model.BonusRule;
import com.bx.erp.model.ErrorInfo.EnumErrorCode;
import com.bx.erp.model.RetailTrade.EnumStatusRetailTrade;
import com.bx.erp.model.Vip.EnumSexVip;
import com.bx.erp.test.checkPoint.VipCP;
import com.jayway.jsonpath.JsonPath;

import net.sf.json.JSONObject;

@WebAppConfiguration
public class VipActionTest extends BaseActionTest {
	public static final String MOBILE = "17352645590";
	public static final String EROOR_MOBILE = "13545678110";
	public static final String CARD_CODE = "415430877106";

	@BeforeClass
	public void setup() {
		super.setUp();
	}

	@AfterClass
	public void tearDown() {
		super.tearDown();
	}

	public RetailTrade getRetailTrade() throws CloneNotSupportedException, InterruptedException {
		Random ran = new Random();
		RetailTrade retailTradeInput = new RetailTrade();
		retailTradeInput = new RetailTrade();
		retailTradeInput.setVipID(1);
		retailTradeInput.setSn("");
		retailTradeInput.setLocalSN(ran.nextInt(1000));
		Thread.sleep(1);
		retailTradeInput.setPos_ID(ran.nextInt(5) + 1);
		retailTradeInput.setLogo("url:" + String.valueOf(System.currentTimeMillis()).substring(6));
		Thread.sleep(1);
		retailTradeInput.setSaleDatetime(new Date());
		retailTradeInput.setStatus(EnumStatusRetailTrade.ESRT_Normal.getIndex());
		retailTradeInput.setStaffID(ran.nextInt(5) + 1);
		retailTradeInput.setPaymentType(1);
		retailTradeInput.setPaymentAccount("A" + String.valueOf(System.currentTimeMillis()).substring(6));
		retailTradeInput.setAmount(50);
		retailTradeInput.setAmountCash(50d);
		Thread.sleep(1);
		retailTradeInput.setRemark("remark:" + String.valueOf(System.currentTimeMillis()).substring(6));
		Thread.sleep(1);
		retailTradeInput.setSourceID(BaseAction.INVALID_ID);
		retailTradeInput.setSmallSheetID(ran.nextInt(7) + 1);
		retailTradeInput.setSyncDatetime(new Date());

		return (RetailTrade) retailTradeInput.clone();
	}

	@Test
	public void testCreateEx1() throws Exception {
		Shared.printTestMethodStartInfo();

		SimpleDateFormat sdf = new SimpleDateFormat(BaseAction.DATETIME_FORMAT_Default3);
		Shared.caseLog("正常创建");
		Vip v = BaseVipTest.DataInput.getVip();
		v.setReturnObject(EnumBoolean.EB_Yes.getIndex());
		v.setConsumeTimes(0);
		v.setConsumeAmount(0);
		//
		MvcResult mr = mvc.perform(//
				post("/vip/createEx.bx")//
						.contentType(MediaType.APPLICATION_JSON) //
						.session((MockHttpSession) sessionBoss) //
						.param(Vip.field.getFIELD_NAME_cardID(), String.valueOf(v.getCardID())) //
						.param(Vip.field.getFIELD_NAME_remark(), v.getRemark()) //
						.param(Vip.field.getFIELD_NAME_logo(), v.getLogo()) //
						.param(Vip.field.getFIELD_NAME_localPosSN(), v.getLocalPosSN()) //
						.param(Vip.field.getFIELD_NAME_iCID(), v.getiCID()) //
						.param(Vip.field.getFIELD_NAME_mobile(), v.getMobile()) //
						.param(Vip.field.getFIELD_NAME_name(), v.getName()) //
						.param(Vip.field.getFIELD_NAME_email(), v.getEmail()) //
						.param(Vip.field.getFIELD_NAME_consumeTimes(), String.valueOf(v.getConsumeTimes())) //
						.param(Vip.field.getFIELD_NAME_consumeAmount(), String.valueOf(v.getConsumeAmount())) //
						.param(Vip.field.getFIELD_NAME_district(), v.getDistrict()) //
						.param(Vip.field.getFIELD_NAME_category(), String.valueOf(v.getCategory())) //
						.param(Vip.field.getFIELD_NAME_birthday(), sdf.format(v.getBirthday())) //
						.param(Vip.field.getFIELD_NAME_bonus(), String.valueOf(v.getBonus())) //
						.param(Vip.field.getFIELD_NAME_lastConsumeDatetime(), sdf.format(v.getLastConsumeDatetime())) //
						.param(Vip.field.getFIELD_NAME_returnObject(), String.valueOf(v.getReturnObject()))//
		).andExpect(status().isOk()).andDo(print()).andReturn();
		// 结果验证：检查错误码
		Shared.checkJSONErrorCode(mr);
		// 验证缓存中的结果
		VipCP.verifyCreate(mr, v, Shared.DBName_Test);
	}

	@Test
	public void testCreateEx2() throws Exception {
		Shared.printTestMethodStartInfo();

		Shared.caseLog("case2:前端除了手机号和姓名必填，有的字段不会传值过来或传NULL过来");
		SimpleDateFormat sdf = new SimpleDateFormat(BaseAction.DATETIME_FORMAT_Default3);
		Vip v = BaseVipTest.DataInput.getVip();
		v.setLocalPosSN("");
		v.setName("Tom");
		v.setEmail("");
		v.setConsumeTimes(0);
		v.setConsumeAmount(0.000000d);
		v.setDistrict("广东");
		v.setBonus(0);
		v.setReturnObject(EnumBoolean.EB_Yes.getIndex());

		MvcResult mr = mvc.perform(//
				post("/vip/createEx.bx")//
						.contentType(MediaType.APPLICATION_JSON) //
						.session((MockHttpSession) sessionBoss) //
						.param(Vip.field.getFIELD_NAME_cardID(), String.valueOf(v.getCardID())) //
						.param(Vip.field.getFIELD_NAME_remark(), v.getRemark()) //
						.param(Vip.field.getFIELD_NAME_logo(), v.getLogo()) //
						.param(Vip.field.getFIELD_NAME_localPosSN(), v.getLocalPosSN()) //
						.param(Vip.field.getFIELD_NAME_iCID(), v.getiCID()) //
						.param(Vip.field.getFIELD_NAME_mobile(), v.getMobile()) //
						.param(Vip.field.getFIELD_NAME_name(), v.getName()) //
						.param(Vip.field.getFIELD_NAME_email(), v.getEmail()) //
						.param(Vip.field.getFIELD_NAME_consumeTimes(), String.valueOf(v.getConsumeTimes())) //
						.param(Vip.field.getFIELD_NAME_consumeAmount(), String.valueOf(v.getConsumeAmount())) //
						.param(Vip.field.getFIELD_NAME_district(), v.getDistrict()) //
						.param(Vip.field.getFIELD_NAME_category(), String.valueOf(v.getCategory())) //
						.param(Vip.field.getFIELD_NAME_birthday(), sdf.format(v.getBirthday())) //
						.param(Vip.field.getFIELD_NAME_bonus(), String.valueOf(v.getBonus())) //
						.param(Vip.field.getFIELD_NAME_lastConsumeDatetime(), sdf.format(v.getLastConsumeDatetime())) //
						.param(Vip.field.getFIELD_NAME_returnObject(), String.valueOf(v.getReturnObject()))//
		).andExpect(status().isOk()).andDo(print()).andReturn();
		// 结果验证：检查错误码
		Shared.checkJSONErrorCode(mr);
		// 验证缓存中的结果
		VipCP.verifyCreate(mr, v, Shared.DBName_Test);
	}

	// VipAction使用了默认的分类1
	// @Test
	// public void testCreateEx3() throws Exception {
	// Shared.printTestMethodStartInfo();
	//
	// String message = "不能使用不存在的VipCategory创建";
	// Shared.caseLog("Case3:" + message);
	// SimpleDateFormat sdf = new
	// SimpleDateFormat(BaseAction.DATETIME_FORMAT_Default3);
	// Vip v4 = BaseVipTest.DataInput.getVip();
	// v4.setCategory(Shared.BIG_ID);
	// v4.setReturnObject(EnumBoolean.EB_Yes.getIndex());
	//
	// MvcResult mr3 = mvc.perform(//
	// post("/vip/createEx.bx")//
	// .contentType(MediaType.APPLICATION_JSON).session((MockHttpSession) session)
	// //
	// .param(Vip.field.getFIELD_NAME_cardID(), String.valueOf(v4.getCardID())) //
	// .param(Vip.field.getFIELD_NAME_localPosSN(), v4.getLocalPosSN()) //
	// .param(Vip.field.getFIELD_NAME_iCID(), v4.getiCID()) //
	// .param(Vip.field.getFIELD_NAME_mobile(), v4.getMobile()) //
	// .param(Vip.field.getFIELD_NAME_name(), v4.getName()) //
	// .param(Vip.field.getFIELD_NAME_email(), v4.getEmail()) //
	// .param(Vip.field.getFIELD_NAME_consumeTimes(), "0") //
	// .param(Vip.field.getFIELD_NAME_consumeAmount(), "0") //
	// .param(Vip.field.getFIELD_NAME_district(), v4.getDistrict()) //
	// .param(Vip.field.getFIELD_NAME_category(), String.valueOf(v4.getCategory()))
	// //
	// .param(Vip.field.getFIELD_NAME_status(), String.valueOf(v4.getStatus())) //
	// .param(Vip.field.getFIELD_NAME_birthday(), sdf.format(v4.getBirthday())) //
	// .param(Vip.field.getFIELD_NAME_bonus(), "0") //
	// .param(Vip.field.getFIELD_NAME_lastConsumeDatetime(),
	// sdf.format(v4.getLastConsumeDatetime())) //
	// .param(Vip.field.getFIELD_NAME_returnObject(),
	// String.valueOf(v4.getReturnObject()))//
	// ).andExpect(status().isOk()).andDo(print()).andReturn();
	// //
	// Shared.checkJSONErrorCode(mr3, EnumErrorCode.EC_BusinessLogicNotDefined);
	// Shared.checkJSONMsg(mr3, message, "错误信息不正确！");
	// }

	@Test
	public void testCreateEx4() throws Exception {
		Shared.printTestMethodStartInfo();
		SimpleDateFormat sdf = new SimpleDateFormat(BaseAction.DATETIME_FORMAT_Default3);
		Shared.caseLog("case4: 使用售前账号去进行创建VIP");
		Vip v5 = BaseVipTest.DataInput.getVip();
		MvcResult mr5 = mvc.perform(//
				post("/vip/createEx.bx")//
						.contentType(MediaType.APPLICATION_JSON).session((MockHttpSession) Shared.getStaffLoginSession(mvc, BaseAction.ACCOUNT_Phone_PreSale)) //
						.param(Vip.field.getFIELD_NAME_cardID(), String.valueOf(v5.getCardID())) //
						.param(Vip.field.getFIELD_NAME_localPosSN(), v5.getLocalPosSN()) //
						.param(Vip.field.getFIELD_NAME_iCID(), v5.getiCID()) //
						.param(Vip.field.getFIELD_NAME_mobile(), v5.getMobile()) //
						.param(Vip.field.getFIELD_NAME_name(), v5.getName()) //
						.param(Vip.field.getFIELD_NAME_email(), v5.getEmail()) //
						.param(Vip.field.getFIELD_NAME_consumeTimes(), String.valueOf(v5.getConsumeTimes())) //
						.param(Vip.field.getFIELD_NAME_consumeAmount(), String.valueOf(v5.getConsumeAmount())) //
						.param(Vip.field.getFIELD_NAME_district(), v5.getDistrict()) //
						.param(Vip.field.getFIELD_NAME_category(), String.valueOf(v5.getCategory())) //
						.param(Vip.field.getFIELD_NAME_birthday(), sdf.format(v5.getBirthday())) //
						.param(Vip.field.getFIELD_NAME_bonus(), String.valueOf(v5.getBonus())) //
						.param(Vip.field.getFIELD_NAME_lastConsumeDatetime(), sdf.format(v5.getLastConsumeDatetime())) //
						.param(Vip.field.getFIELD_NAME_returnObject(), String.valueOf(v5.getReturnObject()))//
		).andExpect(status().isOk()).andDo(print()).andReturn();
		Shared.checkJSONErrorCode(mr5, EnumErrorCode.EC_NoPermission);
	}

	@Test
	public void testCreateEx5() throws Exception {
		Shared.printTestMethodStartInfo();

		String message = "已存在相同身份证号的会员，创建失败";
		Shared.caseLog("Case5:" + message);
		// 先创建一个测试对象
		Vip vip1 = BaseVipTest.DataInput.getVip();
		Vip vip1Create = BaseVipTest.createViaMapper(BaseBO.INVALID_CASE_ID, vip1, EnumErrorCode.EC_NoError, Shared.DBName_Test);
		SimpleDateFormat sdf = new SimpleDateFormat(BaseAction.DATETIME_FORMAT_Default3);
		Vip v4 = BaseVipTest.DataInput.getVip();
		v4.setiCID(vip1Create.getiCID());
		v4.setReturnObject(EnumBoolean.EB_Yes.getIndex());
		//
		MvcResult mr3 = mvc.perform(//
				post("/vip/createEx.bx")//
						.contentType(MediaType.APPLICATION_JSON).session((MockHttpSession) sessionBoss) //
						.param(Vip.field.getFIELD_NAME_cardID(), String.valueOf(v4.getCardID())) //
						.param(Vip.field.getFIELD_NAME_localPosSN(), v4.getLocalPosSN()) //
						.param(Vip.field.getFIELD_NAME_iCID(), v4.getiCID()) //
						.param(Vip.field.getFIELD_NAME_mobile(), v4.getMobile()) //
						.param(Vip.field.getFIELD_NAME_name(), v4.getName()) //
						.param(Vip.field.getFIELD_NAME_email(), v4.getEmail()) //
						.param(Vip.field.getFIELD_NAME_consumeTimes(), "0") //
						.param(Vip.field.getFIELD_NAME_consumeAmount(), "0") //
						.param(Vip.field.getFIELD_NAME_district(), v4.getDistrict()) //
						.param(Vip.field.getFIELD_NAME_category(), String.valueOf(v4.getCategory())) //
						.param(Vip.field.getFIELD_NAME_birthday(), sdf.format(v4.getBirthday())) //
						.param(Vip.field.getFIELD_NAME_bonus(), "0") //
						.param(Vip.field.getFIELD_NAME_lastConsumeDatetime(), sdf.format(v4.getLastConsumeDatetime())) //
						.param(Vip.field.getFIELD_NAME_returnObject(), String.valueOf(v4.getReturnObject()))//
		).andExpect(status().isOk()).andDo(print()).andReturn();
		//
		Shared.checkJSONErrorCode(mr3, EnumErrorCode.EC_BusinessLogicNotDefined);
		Shared.checkJSONMsg(mr3, message, "错误信息不正确！");
		// 删除创建出来的测试对象
		BaseVipTest.deleteViaMapper(vip1Create);
	}

	@Test
	public void testCreateEx6() throws Exception {
		Shared.printTestMethodStartInfo();

		String message = "已存在相同电话号码的会员，创建失败";
		Shared.caseLog("Case6:" + message);
		// 先创建一个测试对象
		Vip vip1 = BaseVipTest.DataInput.getVip();
		Vip vip1Create = BaseVipTest.createViaMapper(BaseBO.INVALID_CASE_ID, vip1, EnumErrorCode.EC_NoError, Shared.DBName_Test);
		SimpleDateFormat sdf = new SimpleDateFormat(BaseAction.DATETIME_FORMAT_Default3);
		Vip v4 = BaseVipTest.DataInput.getVip();
		v4.setMobile(vip1Create.getMobile());
		v4.setReturnObject(EnumBoolean.EB_Yes.getIndex());

		MvcResult mr3 = mvc.perform(//
				post("/vip/createEx.bx")//
						.contentType(MediaType.APPLICATION_JSON).session((MockHttpSession) sessionBoss) //
						.param(Vip.field.getFIELD_NAME_cardID(), String.valueOf(v4.getCardID())) //
						.param(Vip.field.getFIELD_NAME_localPosSN(), v4.getLocalPosSN()) //
						.param(Vip.field.getFIELD_NAME_iCID(), v4.getiCID()) //
						.param(Vip.field.getFIELD_NAME_mobile(), v4.getMobile()) //
						.param(Vip.field.getFIELD_NAME_name(), v4.getName()) //
						.param(Vip.field.getFIELD_NAME_email(), v4.getEmail()) //
						.param(Vip.field.getFIELD_NAME_consumeTimes(), "0") //
						.param(Vip.field.getFIELD_NAME_consumeAmount(), "0") //
						.param(Vip.field.getFIELD_NAME_district(), v4.getDistrict()) //
						.param(Vip.field.getFIELD_NAME_category(), String.valueOf(v4.getCategory())) //
						.param(Vip.field.getFIELD_NAME_birthday(), sdf.format(v4.getBirthday())) //
						.param(Vip.field.getFIELD_NAME_bonus(), "0") //
						.param(Vip.field.getFIELD_NAME_lastConsumeDatetime(), sdf.format(v4.getLastConsumeDatetime())) //
						.param(Vip.field.getFIELD_NAME_returnObject(), String.valueOf(v4.getReturnObject()))//
		).andExpect(status().isOk()).andDo(print()).andReturn();
		//
		Shared.checkJSONErrorCode(mr3, EnumErrorCode.EC_BusinessLogicNotDefined);
		Shared.checkJSONMsg(mr3, message, "错误信息不正确！");
		// 删除创建的测试对象
		BaseVipTest.deleteViaMapper(vip1Create);
	}

	@Test
	public void testCreateEx7() throws Exception {
		Shared.printTestMethodStartInfo();

		String message = "已存在相同邮箱的会员，创建失败";
		Shared.caseLog("Case7:" + message);
		// 先创建一个测试对象
		Vip vip1 = BaseVipTest.DataInput.getVip();
		Vip vip1Create = BaseVipTest.createViaMapper(BaseBO.INVALID_CASE_ID, vip1, EnumErrorCode.EC_NoError, Shared.DBName_Test);
		SimpleDateFormat sdf = new SimpleDateFormat(BaseAction.DATETIME_FORMAT_Default3);
		Vip v4 = BaseVipTest.DataInput.getVip();
		v4.setEmail(vip1Create.getEmail());
		v4.setReturnObject(EnumBoolean.EB_Yes.getIndex());

		MvcResult mr3 = mvc.perform(//
				post("/vip/createEx.bx")//
						.contentType(MediaType.APPLICATION_JSON).session((MockHttpSession) sessionBoss) //
						.param(Vip.field.getFIELD_NAME_cardID(), String.valueOf(v4.getCardID())) //
						.param(Vip.field.getFIELD_NAME_localPosSN(), v4.getLocalPosSN()) //
						.param(Vip.field.getFIELD_NAME_iCID(), v4.getiCID()) //
						.param(Vip.field.getFIELD_NAME_mobile(), v4.getMobile()) //
						.param(Vip.field.getFIELD_NAME_name(), v4.getName()) //
						.param(Vip.field.getFIELD_NAME_email(), v4.getEmail()) //
						.param(Vip.field.getFIELD_NAME_consumeTimes(), "0") //
						.param(Vip.field.getFIELD_NAME_consumeAmount(), "0") //
						.param(Vip.field.getFIELD_NAME_district(), v4.getDistrict()) //
						.param(Vip.field.getFIELD_NAME_category(), String.valueOf(v4.getCategory())) //
						.param(Vip.field.getFIELD_NAME_birthday(), sdf.format(v4.getBirthday())) //
						.param(Vip.field.getFIELD_NAME_bonus(), "0") //
						.param(Vip.field.getFIELD_NAME_lastConsumeDatetime(), sdf.format(v4.getLastConsumeDatetime())) //
						.param(Vip.field.getFIELD_NAME_returnObject(), String.valueOf(v4.getReturnObject()))//
		).andExpect(status().isOk()).andDo(print()).andReturn();
		//
		Shared.checkJSONErrorCode(mr3, EnumErrorCode.EC_BusinessLogicNotDefined);
		Shared.checkJSONMsg(mr3, message, "错误信息不正确！");
		// 删除创建的测试对象
		BaseVipTest.deleteViaMapper(vip1Create);
	}

	@Test
	public void testCreateEx8() throws Exception {
		Shared.printTestMethodStartInfo();

		SimpleDateFormat sdf = new SimpleDateFormat(BaseAction.DATETIME_FORMAT_Default3);
		Shared.caseLog("iCID不合法");
		Vip v = BaseVipTest.DataInput.getVip();
		v.setiCID("445222199823232536");
		v.setReturnObject(EnumBoolean.EB_Yes.getIndex());
		//
		MvcResult mr = mvc.perform(//
				post("/vip/createEx.bx")//
						.contentType(MediaType.APPLICATION_JSON) //
						.session((MockHttpSession) sessionBoss) //
						.param(Vip.field.getFIELD_NAME_cardID(), String.valueOf(v.getCardID())) //
						.param(Vip.field.getFIELD_NAME_localPosSN(), v.getLocalPosSN()) //
						.param(Vip.field.getFIELD_NAME_iCID(), v.getiCID()) //
						.param(Vip.field.getFIELD_NAME_mobile(), v.getMobile()) //
						.param(Vip.field.getFIELD_NAME_name(), v.getName()) //
						.param(Vip.field.getFIELD_NAME_email(), v.getEmail()) //
						.param(Vip.field.getFIELD_NAME_consumeTimes(), "0") //
						.param(Vip.field.getFIELD_NAME_consumeAmount(), "0") //
						.param(Vip.field.getFIELD_NAME_district(), v.getDistrict()) //
						.param(Vip.field.getFIELD_NAME_category(), String.valueOf(v.getCategory())) //
						.param(Vip.field.getFIELD_NAME_birthday(), sdf.format(v.getBirthday())) //
						.param(Vip.field.getFIELD_NAME_bonus(), "0") //
						.param(Vip.field.getFIELD_NAME_lastConsumeDatetime(), sdf.format(v.getLastConsumeDatetime())) //
						.param(Vip.field.getFIELD_NAME_returnObject(), String.valueOf(v.getReturnObject()))//
		).andExpect(status().isOk()).andDo(print()).andReturn();
		// 结果验证：检查错误码
		Shared.checkJSONErrorCode(mr, EnumErrorCode.EC_WrongFormatForInputField);
		Shared.checkJSONMsg(mr, Vip.FIELD_ERROR_icid, "错误信息不正确！");

		Shared.caseLog("iCID长度不符合要求");
		v.setiCID("12312");
		MvcResult mr3 = mvc.perform(//
				post("/vip/createEx.bx")//
						.contentType(MediaType.APPLICATION_JSON) //
						.session((MockHttpSession) sessionBoss) //
						.param(Vip.field.getFIELD_NAME_cardID(), String.valueOf(v.getCardID())) //
						.param(Vip.field.getFIELD_NAME_localPosSN(), v.getLocalPosSN()) //
						.param(Vip.field.getFIELD_NAME_iCID(), v.getiCID()) //
						.param(Vip.field.getFIELD_NAME_mobile(), v.getMobile()) //
						.param(Vip.field.getFIELD_NAME_name(), v.getName()) //
						.param(Vip.field.getFIELD_NAME_email(), v.getEmail()) //
						.param(Vip.field.getFIELD_NAME_consumeTimes(), "0") //
						.param(Vip.field.getFIELD_NAME_consumeAmount(), "0") //
						.param(Vip.field.getFIELD_NAME_district(), v.getDistrict()) //
						.param(Vip.field.getFIELD_NAME_category(), String.valueOf(v.getCategory())) //
						.param(Vip.field.getFIELD_NAME_birthday(), sdf.format(v.getBirthday())) //
						.param(Vip.field.getFIELD_NAME_bonus(), "0") //
						.param(Vip.field.getFIELD_NAME_lastConsumeDatetime(), sdf.format(v.getLastConsumeDatetime())) //
						.param(Vip.field.getFIELD_NAME_returnObject(), String.valueOf(v.getReturnObject()))//
		).andExpect(status().isOk()).andDo(print()).andReturn();
		// 结果验证：检查错误码
		Shared.checkJSONErrorCode(mr3, EnumErrorCode.EC_WrongFormatForInputField);
		Shared.checkJSONMsg(mr3, Vip.FIELD_ERROR_ICID, "错误信息不正确！");
	}

	// 会员名字只检查长度2-32
	// @Test
	// public void testCreateEx9() throws Exception {
	// Shared.printTestMethodStartInfo();
	//
	// SimpleDateFormat sdf = new
	// SimpleDateFormat(BaseAction.DATETIME_FORMAT_Default3);
	// Shared.caseLog("CASE9:创建会员时，会员名称带有数字，创建失败");
	// Vip v = BaseVipTest.DataInput.getVip();
	// v.setName("会员1号");
	// v.setReturnObject(EnumBoolean.EB_Yes.getIndex());
	// //
	// MvcResult mr = mvc.perform(//
	// post("/vip/createEx.bx")//
	// .contentType(MediaType.APPLICATION_JSON) //
	// .session((MockHttpSession) session) //
	// .param(Vip.field.getFIELD_NAME_cardID(), String.valueOf(v.getCardID())) //
	// .param(Vip.field.getFIELD_NAME_localPosSN(), v.getLocalPosSN()) //
	// .param(Vip.field.getFIELD_NAME_iCID(), v.getiCID()) //
	// .param(Vip.field.getFIELD_NAME_mobile(), v.getMobile()) //
	// .param(Vip.field.getFIELD_NAME_name(), v.getName()) //
	// .param(Vip.field.getFIELD_NAME_email(), v.getEmail()) //
	// .param(Vip.field.getFIELD_NAME_consumeTimes(), "0") //
	// .param(Vip.field.getFIELD_NAME_consumeAmount(), "0") //
	// .param(Vip.field.getFIELD_NAME_district(), v.getDistrict()) //
	// .param(Vip.field.getFIELD_NAME_category(), String.valueOf(v.getCategory()))
	// //
	// .param(Vip.field.getFIELD_NAME_status(), String.valueOf(v.getStatus())) //
	// .param(Vip.field.getFIELD_NAME_birthday(), sdf.format(v.getBirthday())) //
	// .param(Vip.field.getFIELD_NAME_bonus(), "0") //
	// .param(Vip.field.getFIELD_NAME_lastConsumeDatetime(),
	// sdf.format(v.getLastConsumeDatetime())) //
	// .param(Vip.field.getFIELD_NAME_returnObject(),
	// String.valueOf(v.getReturnObject()))//
	// ).andExpect(status().isOk()).andDo(print()).andReturn();
	// // 结果验证：检查错误码
	// Shared.checkJSONErrorCode(mr, EnumErrorCode.EC_WrongFormatForInputField);
	// Shared.checkJSONMsg(mr, Vip.FIELD_ERROR_name, "错误信息不正确！");
	// }

	@Test
	public void testCreateEx10() throws Exception {
		Shared.printTestMethodStartInfo();
		Shared.caseLog("case10: 正常创建, 同时也创建了vipSource, 一张会员卡VipCardCode");
		Vip vipGet = BaseVipTest.DataInput.getVip();
		vipGet.setReturnObject(EnumBoolean.EB_Yes.getIndex());
		vipGet.setConsumeTimes(0);
		vipGet.setConsumeAmount(0);
		vipGet.setBonus(0);
		BaseVipTest.createViaAction(vipGet, sessionBoss, mvc, EnumErrorCode.EC_NoError);
		// VipSource vipSource = new VipSource();
		// vipSource.setPageIndex(BaseAction.PAGE_StartIndex);
		// vipSource.setPageSize(BaseAction.PAGE_SIZE_Infinite);
		// List<BaseModel> vipSourcesList =
		// BaseVipSourceTest.retrieveNViaMapper(vipSource, Shared.DBName_Test);
		// boolean hasCreateVipSource = false;
		// for(BaseModel bm : vipSourcesList) {
		// VipSource vipSourceBm = (VipSource) bm;
		// if(vipSourceBm.getVipID() == vipCreate.getID()) {
		// hasCreateVipSource = true;
		// BaseVipSourceTest.deleteViaMapper(vipSourceBm);
		// break;
		// }
		// }
		// Assert.assertTrue(hasCreateVipSource, "创建VIP的时候，没有创建VipSource");
		// //
		// VipCardCode vipCardCode = new VipCardCode();
		// vipCardCode.setPageIndex(BaseAction.PAGE_StartIndex);
		// vipCardCode.setPageSize(BaseAction.PAGE_SIZE_Infinite);
		// List<BaseModel> vipCardCodeList =
		// BaseVipCardCodeTest.retrieveNViaMapper(vipCardCode, Shared.DBName_Test);
		// boolean hasCreateVipCardCode = false;
		// for(BaseModel bm : vipCardCodeList) {
		// VipCardCode vipCardCodeBm = (VipCardCode) bm;
		// if(vipCardCodeBm.getVipID() == vipCreate.getID()) {
		// hasCreateVipCardCode = true;
		// BaseVipCardCodeTest.deleteViaMapper(vipCardCodeBm);
		// break;
		// }
		// }
		// Assert.assertTrue(hasCreateVipCardCode, "创建VIP的时候，没有创建VipCardCode");
		// 删除测试数据，创建会员会创建vipsource、vipcardcode，所以删除不了测试数据
		// BaseVipTest.deleteViaMapper(vipCreate);
	}

	@Test
	public void testCreateEx11() throws Exception {
		Shared.printTestMethodStartInfo();
		Shared.caseLog("正常创建女性会员");
		Vip vipGet = BaseVipTest.DataInput.getVip();
		vipGet.setSex(EnumSexVip.ESV_Female.getIndex());
		Vip vipCreate = BaseVipTest.createViaAction(vipGet, sessionBoss, mvc, EnumErrorCode.EC_NoError);
		Assert.assertTrue(vipCreate.getSex() == EnumSexVip.ESV_Female.getIndex(), "没有正确创建女性会员");
		// 删除测试数据，创建会员会创建vipsource、vipcardcode，所以删除不了测试数据
		// BaseVipTest.deleteViaMapper(vipCreate);
	}

	@Test
	public void testCreateEx12() throws Exception {
		Shared.printTestMethodStartInfo();
		Shared.caseLog("正常创建男性会员");
		Vip vipGet = BaseVipTest.DataInput.getVip();
		vipGet.setSex(EnumSexVip.ESV_Male.getIndex());
		Vip vipCreate = BaseVipTest.createViaAction(vipGet, sessionBoss, mvc, EnumErrorCode.EC_NoError);
		Assert.assertTrue(vipCreate.getSex() == EnumSexVip.ESV_Male.getIndex(), "没有正确创建男性会员");
		// 删除测试数据，创建会员会创建vipsource、vipcardcode，所以删除不了测试数据
		// BaseVipTest.deleteViaMapper(vipCreate);
	}

	@Test
	public void testCreateEx13() throws Exception {
		Shared.printTestMethodStartInfo();
		Shared.caseLog("正常创建未知性别会员");
		Vip vipGet = BaseVipTest.DataInput.getVip();
		vipGet.setSex(EnumSexVip.ESV_Unknown.getIndex());
		Vip vipCreate = BaseVipTest.createViaAction(vipGet, sessionBoss, mvc, EnumErrorCode.EC_NoError);
		Assert.assertTrue(vipCreate.getSex() == EnumSexVip.ESV_Unknown.getIndex(), "没有正确创建未知性别会员");
		// 删除测试数据，创建会员会创建vipsource、vipcardcode，所以删除不了测试数据
		// BaseVipTest.deleteViaMapper(vipCreate);
	}

	@Test
	public void testCreateEx14() throws Exception {
		Shared.printTestMethodStartInfo();
		Shared.caseLog("传入错误的会员性别");
		Vip vipGet = BaseVipTest.DataInput.getVip();
		vipGet.setSex(EnumSexVip.ESV_Unknown.getIndex() + 1);
		Vip vipCreate = BaseVipTest.createViaAction(vipGet, sessionBoss, mvc, EnumErrorCode.EC_WrongFormatForInputField);
		Assert.assertTrue(vipCreate == null, "传入错误的会员性别，期望是创建失败");
		// 删除测试数据，创建会员会创建vipsource、vipcardcode，所以删除不了测试数据
		// BaseVipTest.deleteViaMapper(vipCreate);
	}

	@Test
	public void testCreateEx15() throws Exception {
		Shared.printTestMethodStartInfo();
		Shared.caseLog("传入错误的会员性别");
		Vip vipGet = BaseVipTest.DataInput.getVip();
		vipGet.setSex(EnumSexVip.ESV_Female.getIndex() - 1);
		Vip vipCreate = BaseVipTest.createViaAction(vipGet, sessionBoss, mvc, EnumErrorCode.EC_WrongFormatForInputField);
		Assert.assertTrue(vipCreate == null, "传入错误的会员性别，期望是创建失败");
		// 删除测试数据，创建会员会创建vipsource、vipcardcode，所以删除不了测试数据
		// BaseVipTest.deleteViaMapper(vipCreate);
	}

	@Test
	public void testCreateEx16() throws Exception {
		Shared.printTestMethodStartInfo();
		Shared.caseLog("case16: 创建会员时，初始积分为积分规则的初始设置积分");
		Vip vipGet = BaseVipTest.DataInput.getVip();
		Vip vipCreate = BaseVipTest.createViaAction(vipGet, sessionBoss, mvc, EnumErrorCode.EC_NoError);
		Assert.assertTrue(vipCreate != null, "创建VIP失败");
		// 检查初始积分
		BonusRule bonusRule = new BonusRule();
		bonusRule.setID(BaseBonusRuleTest.DEFAULT_BonusRule_ID);
		BonusRule bonusRuleInDB = (BonusRule) BaseBonusRuleTest.retrieve1ViaMapper(bonusRule, Shared.DBName_Test);
		Assert.assertTrue(vipCreate.getBonus() == bonusRuleInDB.getInitIncreaseBonus(), "新创建的会员,初始积分和积分规则表中的初始设置积分不一致");
	}

	@Test
	public void testCreateEx17() throws Exception {
		Shared.printTestMethodStartInfo();
		Shared.caseLog("导入商家数据进行创建会员,初始积分为商家数据上的积分");

		Vip vipGet = BaseVipTest.DataInput.getVip();
		vipGet.setBonus(100);
		vipGet.setIsImported(EnumBoolean.EB_Yes.getIndex());
		vipGet.setCardCode(Shared.DB_SN_Test + Shared.generateStringByTime(9) + "1");
		Vip vipInDB = BaseVipTest.createViaAction(vipGet, sessionBoss, mvc, EnumErrorCode.EC_NoError);
		//
		Assert.assertTrue(vipInDB != null && vipInDB.getBonus() == vipGet.getBonus(), "会员的初始积分并不是导入数据的积分");

		// 删除测试数据
		VipCardCode vipCardCode = new VipCardCode();
		vipCardCode.setVipID(vipInDB.getID());
		List<BaseModel> vipCardCodes = BaseVipCardCodeTest.retrieveNViaMapper(vipCardCode, Shared.DBName_Test);
		for (BaseModel baseModel : vipCardCodes) {
			VipCardCode vcc = (VipCardCode) baseModel;
			BaseVipCardCodeTest.deleteViaMapper(vcc);
		}
		//
		VipSource vipSource = new VipSource();
		List<BaseModel> vipSources = BaseVipSourceTest.retrieveNViaMapper(vipSource, Shared.DBName_Test);
		for (BaseModel baseModel : vipSources) {
			VipSource vs = (VipSource) baseModel;
			if (vs.getVipID() == vipInDB.getID()) {
				BaseVipSourceTest.deleteViaMapper(vs);
			}
		}
		//
		BaseVipTest.deleteViaMapper(vipInDB);
	}

	
	@Test
	public void testUpdateEx1() throws Exception {
		Shared.printTestMethodStartInfo();

		Shared.caseLog("case1: 正常修改");
		Vip vip = BaseVipTest.DataInput.getVip();
		Vip vipCreate = BaseVipTest.createViaMapper(BaseBO.INVALID_CASE_ID, vip, EnumErrorCode.EC_NoError, Shared.DBName_Test);
		//
		Vip tmpUpdateVip = (Vip) vipCreate.clone();
		tmpUpdateVip.setDistrict("广州");
		tmpUpdateVip.setCategory(VipCategory.DEFAULT_VipCategory_ID);
		BaseVipTest.updateViaAction(tmpUpdateVip, sessionBoss, mvc);

		BaseVipTest.deleteViaMapper(vipCreate);
	}

	@Test
	public void testUpdateEx2() throws Exception {
		Shared.printTestMethodStartInfo();

		Shared.caseLog("case2: 正常修改会员的生日");
		Vip vip = BaseVipTest.DataInput.getVip();
		Vip vipCreate = BaseVipTest.createViaMapper(BaseBO.INVALID_CASE_ID, vip, EnumErrorCode.EC_NoError, Shared.DBName_Test);
		//
		Vip tmpUpdateVip = (Vip) vipCreate.clone();
		tmpUpdateVip.setBirthday(new Date());
		BaseVipTest.updateViaAction(tmpUpdateVip, sessionBoss, mvc);

		BaseVipTest.deleteViaMapper(vipCreate);
	}

	@Test
	public void testUpdateEx4() throws Exception {
		Shared.printTestMethodStartInfo();

		Shared.caseLog("case4:会员A修改会员B的信息");

		// 创建会员A
		Vip createVip = BaseVipTest.createViaMapper(BaseBO.INVALID_CASE_ID, BaseVipTest.DataInput.getVip(), EnumErrorCode.EC_NoError, Shared.DBName_Test);
		// 创建一种会员卡
		VipCard vipCard = BaseVipCardTest.DataInput.getVipCard();
		VipCard vipCardCreate = BaseVipCardTest.createViaMapper(vipCard);
		// 会员A领取会员卡
		VipCardCode vipCardCode = BaseVipCardCodeTest.DataInput.getVipCardCode(createVip.getID(), vipCardCreate.getID());
		VipCardCode vipCardCodeCreate = BaseVipCardCodeTest.createViaMapper(vipCardCode, Shared.DBName_Test, BaseBO.INVALID_CASE_ID, EnumErrorCode.EC_NoError);
		// vipA登录
		HttpSession sessionVip = Shared.getVipLoginSession(mvc, createVip, true);
		// 修改会员B的信息
		Vip vipB = BaseVipTest.DataInput.getVip();
		vipB.setID(new Random().nextInt(6) + 2);
		MvcResult mr = mvc.perform(//
				post("/vip/updateEx.bx")//
						.contentType(MediaType.APPLICATION_JSON)//
						.session((MockHttpSession) sessionVip) //
						.param(Vip.field.getFIELD_NAME_ID(), String.valueOf(vipB.getID())) //
						.param(Vip.field.getFIELD_NAME_district(), vipB.getDistrict())//
						.param(Vip.field.getFIELD_NAME_category(), String.valueOf(vipB.getCategory())) //
						.param(Vip.field.getFIELD_NAME_name(), vipB.getName())//
						.param(Vip.field.getFIELD_NAME_birthday(), String.valueOf(vipB.getBirthday()))//
		)//
				.andExpect(status().isOk())//
				.andDo(print())//
				.andReturn();
		Assert.assertTrue("".equals(mr.getResponse().getContentAsString()), "会员A修改会员B信息未能正确处理");
		//
		BaseVipCardCodeTest.deleteViaMapper(vipCardCodeCreate);
		BaseVipCardTest.deleteViaMapper(vipCardCreate);
	}

	@Test
	public void testUpdateBonusEx1() throws Exception {
		Shared.printTestMethodStartInfo();
		Shared.caseLog("case1:老板设置积分");
		// 创建会员
		Vip vip = BaseVipTest.DataInput.getVip();
		Vip vipCreate = BaseVipTest.createViaMapper(BaseBO.INVALID_CASE_ID, vip, EnumErrorCode.EC_NoError, Shared.DBName_Test);
		//
		Vip vipToUpdate = (Vip) vipCreate.clone();
		vipToUpdate.setBonus(500);
		vipToUpdate.setManuallyAdded(EnumBoolean.EB_Yes.getIndex());
		vipToUpdate.setRemarkForBonusHistory("因为XXXX增加XXXX积分");
		MvcResult mr = mvc.perform(//
				post("/vip/updateBonusEx.bx")//
						.contentType(MediaType.APPLICATION_JSON)//
						.session((MockHttpSession) sessionBoss) //
						.param(Vip.field.getFIELD_NAME_ID(), String.valueOf(vipToUpdate.getID())) //
						.param(Vip.field.getFIELD_NAME_bonus(), String.valueOf(vipToUpdate.getBonus()))//
						.param(Vip.field.getFIELD_NAME_remarkForBonusHistory(), vipToUpdate.getRemarkForBonusHistory())//
						.param(Vip.field.getFIELD_NAME_manuallyAdded(), String.valueOf(vipToUpdate.getManuallyAdded())) //
		)//
				.andExpect(status().isOk())//
				.andDo(print())//
				.andReturn();
		// 结果验证:检查错误码
		Shared.checkJSONErrorCode(mr);
		// 结果验证：检查缓存结果
		VipCP.verifyUpdate(mr, vipToUpdate, Shared.DBName_Test);
	}

	@Test
	public void testUpdateBonusEx2() throws Exception {
		Shared.printTestMethodStartInfo();
		Shared.caseLog("case2:没有权限");
		// 创建会员
		Vip vip = BaseVipTest.DataInput.getVip();
		Vip vipCreate = BaseVipTest.createViaMapper(BaseBO.INVALID_CASE_ID, vip, EnumErrorCode.EC_NoError, Shared.DBName_Test);
		//
		Vip vipToUpdate = (Vip) vipCreate.clone();
		vipToUpdate.setBonus(500);
		vipToUpdate.setManuallyAdded(EnumBoolean.EB_Yes.getIndex());
		vipToUpdate.setRemarkForBonusHistory("因为XXXX增加XXXX积分");
		MvcResult mr = mvc.perform(//
				post("/vip/updateBonusEx.bx")//
						.contentType(MediaType.APPLICATION_JSON)//
						.session((MockHttpSession) sessionCashier) //
						.param(Vip.field.getFIELD_NAME_ID(), String.valueOf(vipToUpdate.getID())) //
						.param(Vip.field.getFIELD_NAME_bonus(), String.valueOf(vipToUpdate.getBonus()))//
						.param(Vip.field.getFIELD_NAME_remarkForBonusHistory(), vipToUpdate.getRemarkForBonusHistory())//
						.param(Vip.field.getFIELD_NAME_manuallyAdded(), String.valueOf(vipToUpdate.getManuallyAdded())) //
		)//
				.andExpect(status().isOk())//
				.andDo(print())//
				.andReturn();
		// 结果验证:检查错误码
		Shared.checkJSONErrorCode(mr, EnumErrorCode.EC_NoPermission);
	}

	@Test
	public void testUpdateBonusEx3() throws Exception {
		Shared.printTestMethodStartInfo();
		Shared.caseLog("case3:不是从页面进行的积分修改");
		// 创建会员
		Vip vip = BaseVipTest.DataInput.getVip();
		Vip vipCreate = BaseVipTest.createViaMapper(BaseBO.INVALID_CASE_ID, vip, EnumErrorCode.EC_NoError, Shared.DBName_Test);
		//
		Vip vipToUpdate = (Vip) vipCreate.clone();
		vipToUpdate.setBonus(500);
		vipToUpdate.setManuallyAdded(EnumBoolean.EB_NO.getIndex());
		vipToUpdate.setRemarkForBonusHistory("因为XXXX增加XXXX积分");
		MvcResult mr = mvc.perform(//
				post("/vip/updateBonusEx.bx")//
						.contentType(MediaType.APPLICATION_JSON)//
						.session((MockHttpSession) sessionCashier) //
						.param(Vip.field.getFIELD_NAME_ID(), String.valueOf(vipToUpdate.getID())) //
						.param(Vip.field.getFIELD_NAME_bonus(), String.valueOf(vipToUpdate.getBonus()))//
						.param(Vip.field.getFIELD_NAME_remarkForBonusHistory(), vipToUpdate.getRemarkForBonusHistory())//
						.param(Vip.field.getFIELD_NAME_manuallyAdded(), String.valueOf(vipToUpdate.getManuallyAdded())) //
		)//
				.andExpect(status().isOk())//
				.andDo(print())//
				.andReturn();
		String json = mr.getResponse().getContentAsString();
		Assert.assertTrue(json.equals(""), "返回值不正确");
	}

	@Test
	public void testRetrieve1Ex() throws Exception {
		Shared.printTestMethodStartInfo();
		System.out.println("\n------------------------ CASE1:用存在的ID查询------------------------");
		MvcResult mr = mvc.perform(//
				get("/vip/retrieve1Ex.bx?ID=1").session(//
						(MockHttpSession) Shared.getStaffLoginSession(mvc, Shared.PhoneOfBoss)//
				).contentType(MediaType.APPLICATION_JSON)//
		)//
				.andExpect(status().isOk())//
				.andDo(print())//
				.andReturn();
		Shared.checkJSONErrorCode(mr);

		JSONObject o1 = JSONObject.fromObject(mr.getResponse().getContentAsString());
		Integer i1 = JsonPath.read(o1, "$.vip.ID");
		assertTrue(i1 == 1);

		System.out.println("\n------------------------ CASE2:用不存在的ID查询------------------------");
		MvcResult mr2 = mvc.perform(//
				get("/vip/retrieve1Ex.bx?ID=" + Shared.BIG_ID).session(//
						(MockHttpSession) Shared.getStaffLoginSession(mvc, Shared.PhoneOfBoss)//
				).contentType(MediaType.APPLICATION_JSON)//
		)//
				.andExpect(status().isOk())//
				.andDo(print())//
				.andReturn();
		Shared.checkJSONErrorCode(mr2);

		String json = mr2.getResponse().getContentAsString();
		JSONObject o2 = JSONObject.fromObject(json);
		String s2 = o2.getString("vip");
		assertTrue("null".equals(s2));

		/*
		 * 如果VipAction的testRetrieve1Ex进行了模型判断非空才能打开这条 System.out.
		 * println("\n------------------------ CASE2:用不存在的ID查询------------------------"
		 * ); MvcResult mr2 = mvc.perform(//
		 * get("/vip/retrieve1Ex.bx?ID=-22").session(// (MockHttpSession)
		 * Shared.getStaffLoginSession(mvc, Shared.PhoneOfBoss)//
		 * ).contentType(MediaType.APPLICATION_JSON)// )// .andExpect(status().isOk())//
		 * .andDo(print())// .andReturn(); Shared.checkJSONErrorCode(mr2);
		 */

		System.out.println("\n------------------------ CASE3：没有权限进行操作-----------------------");
//		MvcResult mr3 = mvc.perform(//
//				get("/vip/retrieve1Ex.bx?ID=1")//
//						.session((MockHttpSession) Shared.getStaffLoginSession(mvc, Shared.PhoneOfManager))//
//						.contentType(MediaType.APPLICATION_JSON)//
//		)//
//				.andExpect(status().isOk())//
//				.andDo(print())//
//				.andReturn();
//		Shared.checkJSONErrorCode(mr3, EnumErrorCode.EC_NoPermission);
	}

	@Test
	public void testRetrieveNEx1() throws Exception {
		Shared.printTestMethodStartInfo();

		Shared.caseLog(" CASE1:用District查询");
		MvcResult mr3 = mvc.perform(//
				post("/vip/retrieveNEx.bx").session((MockHttpSession) Shared.getStaffLoginSession(mvc, Shared.PhoneOfBoss))//
						.param(Vip.field.getFIELD_NAME_district(), "广州")//
						.param(Vip.field.getFIELD_NAME_category(), String.valueOf(BaseAction.INVALID_ID))//
						.contentType(MediaType.APPLICATION_JSON)//
		)//
				.andExpect(status().isOk())//
				.andDo(print())//
				.andReturn();
		Shared.checkJSONErrorCode(mr3);

		JSONObject o3 = JSONObject.fromObject(mr3.getResponse().getContentAsString());
		List<String> list5 = JsonPath.read(o3, "$." + BaseAction.KEY_ObjectList + "[*].district");
		String s = "广州";
		int i = 0;
		for (String string : list5) {
			if (s.equals(string)) {
				i++;
			}
		}
		assertTrue(i > 0);
	}

	@Test
	public void testRetrieveNEx2() throws Exception {
		Shared.printTestMethodStartInfo();

		Shared.caseLog("CASE2:用Category查询");
		MvcResult mr4 = mvc.perform(//
				post("/vip/retrieveNEx.bx").session((MockHttpSession) Shared.getStaffLoginSession(mvc, Shared.PhoneOfBoss))//
						.param(Vip.field.getFIELD_NAME_category(), String.valueOf(VipCategory.DEFAULT_VipCategory_ID))//
						.contentType(MediaType.APPLICATION_JSON)//
		)//
				.andExpect(status().isOk())//
				.andDo(print())//
				.andReturn();
		Shared.checkJSONErrorCode(mr4);

		JSONObject o4 = JSONObject.fromObject(mr4.getResponse().getContentAsString());
		//
		List<Integer> list7 = JsonPath.read(o4, "$." + BaseAction.KEY_ObjectList + "[*].category");
		for (int i5 = 0; i5 < list7.size(); i5++) {
			assertTrue(list7.get(i5) == VipCategory.DEFAULT_VipCategory_ID);
		}
	}

	@Test
	public void testRetrieveNEx3() throws Exception {
		Shared.printTestMethodStartInfo();

		Shared.caseLog("CASE3:没有权限进行查询");
//		MvcResult mr5 = mvc.perform(//
//				post("/vip/retrieveNEx.bx")//
//						.param(Vip.field.getFIELD_NAME_district(), "")//
//						.param(Vip.field.getFIELD_NAME_category(), String.valueOf(BaseAction.INVALID_ID))//
//						.session((MockHttpSession) Shared.getStaffLoginSession(mvc, Shared.PhoneOfManager))//
//						.contentType(MediaType.APPLICATION_JSON)//
//		)//
//				.andExpect(status().isOk())//
//				.andDo(print())//
//				.andReturn();
//		Shared.checkJSONErrorCode(mr5, EnumErrorCode.EC_NoPermission);
		// 售前账号已被删除
		// Shared.caseLog("case6: 使用售前账号去进行查询");
		// MvcResult mr6 = mvc.perform(//
		// post("/vip/retrieveNEx.bx")//
		// .param(Vip.field.getFIELD_NAME_IDInPOS(),
		// String.valueOf(BaseAction.INVALID_ID))//
		// .param(Vip.field.getFIELD_NAME_district(), "")//
		// .param(Vip.field.getFIELD_NAME_category(),
		// String.valueOf(BaseAction.INVALID_ID))//
		// .param(Vip.field.getFIELD_NAME_queryKeyword(), "")//
		// .param(Vip.field.getFIELD_NAME_status(),
		// String.valueOf(EnumStatusVip.ESV_Normal.getIndex()))//
		// .session((MockHttpSession) Shared.getStaffLoginSession(mvc,
		// BaseAction.ACCOUNT_Phone_PreSale))//
		// .contentType(MediaType.APPLICATION_JSON)//
		// )//
		// .andExpect(status().isOk())//
		// .andDo(print())//
		// .andReturn();
		// Shared.checkJSONErrorCode(mr6);
	}

	// @Test
	// public void testDeleteList() throws Exception {
	// Shared.printTestMethodStartInfo();
	//
	// Vip vip = getVip();
	// Map<String, Object> paramsForCreate =
	// vip.getCreateParam(BaseBO.INVALID_CASE_ID, vip);
	// VipMapper mapper = (VipMapper) applicationContext.getBean("vipMapper");
	// Vip vipCreate = (Vip) mapper.create(paramsForCreate);
	// vip.setIgnoreIDInComparision(true);
	// if(vip.compareTo(vipCreate) != 0) {
	// Assert.assertTrue(false, "创建的对象的字段与DB读出的不相等");
	// }
	//
	// MvcResult mr = mvc.perform(get("/vip/deleteListEx.bx?vipListID=" +
	// vipCreate.getID()).session((MockHttpSession)
	// getStaffLoginSession(mvc,Shared.PhoneOfBoss)).contentType(MediaType.APPLICATION_JSON)).andExpect(status().isOk()).andDo(print()).andReturn();
	// Shared.checkJSONErrorCode(mr);
	//
	// //case2:不可删除有记录的vip
	// MvcResult mr2 = null;
	// try {
	// Vip vip2 = getVip();
	// Map<String, Object> paramsForCreate2 =
	// vip2.getCreateParam(BaseBO.INVALID_CASE_ID, vip2);
	// Vip vipCreate2 = (Vip) mapper.create(paramsForCreate2);
	// vip2.setIgnoreIDInComparision(true);
	// if(vip2.compareTo(vipCreate2) != 0) {
	// Assert.assertTrue(false, "创建的对象的字段与DB读出的不相等");
	// }
	//
	// mr2 = mvc.perform(get("/vip/deleteListEx.bx?vipListID=" + vipCreate2.getID()
	// + ",1").session((MockHttpSession)
	// getStaffLoginSession(mvc,Shared.PhoneOfBoss)).contentType(MediaType.APPLICATION_JSON)).andExpect(status().isOk()).andDo(print()).andReturn();
	// } catch (Exception e) {
	// assertNull(mr2);
	// }
	// }

	@Test
	public void testRetrieveNExCase6() throws Exception {
		Shared.printTestMethodStartInfo();

		Shared.caseLog("case6:传递手机号码，查找此手机号码的会员");
		Vip vip = BaseVipTest.DataInput.getVip();
		Vip createVIP = BaseVipTest.createViaMapper(BaseBO.INVALID_CASE_ID, vip, EnumErrorCode.EC_NoError, Shared.DBName_Test);

		Vip queryVip = new Vip();
		queryVip.setMobile(createVIP.getMobile());
		List<BaseModel> vipList = BaseVipTest.retrieveNViaAction(queryVip, sessionBoss, mvc);
		//
		Assert.assertTrue(vipList != null && vipList.size() == 1, "根据手机号码查找会员失败");
		for (BaseModel baseModel : vipList) {
			Assert.assertTrue(createVIP.compareTo(baseModel) == 0, Shared.CompareToErrorMsg);
		}

		BaseVipTest.deleteViaMapper(createVIP);
	}

	@Test
	public void testRetrieveNExCase7() throws Exception {
		Shared.printTestMethodStartInfo();

		Shared.caseLog("case7:传递不存在的手机号码，查找数据为空");

		Vip queryVip = new Vip();
		queryVip.setMobile(Shared.getValidStaffPhone());
		List<BaseModel> vipList = BaseVipTest.retrieveNViaAction(queryVip, sessionBoss, mvc);
		Assert.assertTrue(vipList != null && vipList.size() == 0, "不存在的手机号码也能查找到会员");
	}

	@Test
	public void testRetrieveNByMobileOrVipCardSNExCase1() throws Exception {
		Shared.printTestMethodStartInfo();
		Shared.caseLog(" case1:输入5位数以上的手机号码精确搜索vip ");

		Vip createVip = BaseVipTest.createViaMapper(BaseBO.INVALID_CASE_ID, BaseVipTest.DataInput.getVip(), EnumErrorCode.EC_NoError, Shared.DBName_Test);

		Vip queryVip = new Vip();
		queryVip.setMobile(createVip.getMobile());

		List<BaseModel> vipList = BaseVipTest.retrieveNByMobileOrVipCardSNViaAction(queryVip, sessionBoss, mvc);
		Assert.assertTrue(vipList != null && vipList.size() == 1, "使用手机号码查找会员失败");
		Assert.assertTrue(createVip.compareTo(vipList.get(0)) == 0, Shared.CompareToErrorMsg);
	}

	@Test
	public void testRetrieveNByMobileOrVipCardSNExCase2() throws Exception {
		Shared.printTestMethodStartInfo();
		Shared.caseLog("case2:输入5位数以下的手机号码精确搜索vip，没有数据");

		MvcResult mr2 = mvc.perform(//
				post("/vip/retrieveNByMobileOrVipCardSNEx.bx").session((MockHttpSession) Shared.getStaffLoginSession(mvc, Shared.PhoneOfBoss))//
						.param(Vip.field.getFIELD_NAME_mobile(), "1234") //
						.contentType(MediaType.APPLICATION_JSON)//
		)//
				.andExpect(status().isOk())//
				.andDo(print())//
				.andReturn();

		Shared.checkJSONErrorCode(mr2, EnumErrorCode.EC_WrongFormatForInputField);
	}

	@Test
	public void testRetrieveNByMobileOrVipCardSNExCase4() throws Exception {
		Shared.printTestMethodStartInfo();
		Shared.caseLog(" case4:没有权限进行操作");
//		MvcResult mr4 = mvc.perform(//
//				post("/vip/retrieveNByMobileOrVipCardSNEx.bx")//
//						.session((MockHttpSession) Shared.getStaffLoginSession(mvc, Shared.PhoneOfManager))//
//						.param(Vip.field.getFIELD_NAME_mobile(), "13545678110") //
//						.contentType(MediaType.APPLICATION_JSON)//
//		)//
//				.andExpect(status().isOk())//
//				.andDo(print())//
//				.andReturn();
//		Shared.checkJSONErrorCode(mr4, EnumErrorCode.EC_NoPermission);
	}

	@Test
	public void testRetrieveNByMobileOrVipCardSNExCase5() throws Exception {
		Shared.printTestMethodStartInfo();
		Shared.caseLog(" case5:传递会员卡号号码，查找此卡号号码的会员");

		// 创建会员卡 TODO 直接拿NBR公司的会员卡即可
		VipCard vipCard = BaseVipCardTest.DataInput.getVipCard();
		VipCard vipCardCreate = BaseVipCardTest.createViaMapper(vipCard);

		// 创建会员
		Vip createVip = BaseVipTest.createViaMapper(BaseBO.INVALID_CASE_ID, BaseVipTest.DataInput.getVip(), EnumErrorCode.EC_NoError, Shared.DBName_Test);

		// 领取一张会员卡
		VipCardCode vipCardCode = BaseVipCardCodeTest.DataInput.getVipCardCode(createVip.getID(), vipCardCreate.getID());
		VipCardCode createVipCardCode = BaseVipCardCodeTest.createViaMapper(vipCardCode, Shared.DBName_Test, BaseBO.INVALID_CASE_ID, EnumErrorCode.EC_NoError);

		Vip queryVip = new Vip();
		queryVip.setVipCardSN(createVipCardCode.getSN());
		//
		List<BaseModel> vipList = BaseVipTest.retrieveNByMobileOrVipCardSNViaAction(queryVip, sessionBoss, mvc);
		Assert.assertTrue(vipList != null && vipList.size() == 1, "根据会员卡卡号查找会员失败");
		Assert.assertTrue(createVip.compareTo(vipList.get(0)) == 0, Shared.CompareToErrorMsg);

		// 删除数据
		BaseVipCardCodeTest.deleteViaMapper(createVipCardCode);
		BaseVipCardTest.deleteViaMapper(vipCardCreate);
		BaseVipTest.deleteViaMapper(createVip);
	}

	@Test
	public void testRetrieveNByMobileOrVipCardSNExCase6() throws Exception {
		Shared.printTestMethodStartInfo();
		Shared.caseLog(" case6:传递不存在的会员卡号号码，查找为空");

		Vip queryVip = new Vip();
		queryVip.setVipCardSN("1234" + Shared.generateStringByTime(9));

		List<BaseModel> vipList = BaseVipTest.retrieveNByMobileOrVipCardSNViaAction(queryVip, sessionBoss, mvc);
		Assert.assertTrue(vipList != null && vipList.size() == 0, "根据不存在的会员卡卡号查找会员成功");
	}

	@Test
	public void testRetrieveNVipConsumeHistoryEx() throws Exception {
		Shared.printTestMethodStartInfo();

		System.out.println("\n------------------------ 创建一个VIP ------------------------");
		Vip vip = BaseVipTest.DataInput.getVip();
		Vip vipCreate = BaseVipTest.createViaMapper(BaseBO.INVALID_CASE_ID, vip, EnumErrorCode.EC_NoError, Shared.DBName_Test);

		System.out.println("\n------------------------ 创建一个零售单A ------------------------");

		RetailTrade rt = getRetailTrade();
		rt.setVipID(vipCreate.getID());
		RetailTrade localRetailTrade = BaseRetailTradeTest.createRetailTrade(rt);

		System.out.println("\n------------------------ 创建一个零售单B ------------------------");

		RetailTrade rt2 = getRetailTrade();
		rt2.setVipID(vipCreate.getID());
		RetailTrade localRetailTrade2 = BaseRetailTradeTest.createRetailTrade(rt2);

		System.out.println("\n------------------------ CASE1:根据VipID查询零售单------------------------");
		MvcResult mr = mvc.perform(//
				get("/vip/retrieveNVipConsumeHistoryEx.bx?ID=" + vipCreate.getID()) //
						.session((MockHttpSession) Shared.getStaffLoginSession(mvc, Shared.PhoneOfBoss)) //
						.contentType(MediaType.APPLICATION_JSON)//
		)//
				.andExpect(status().isOk())//
				.andDo(print())//
				.andReturn();
		Shared.checkJSONErrorCode(mr);

		System.out.println("\n------------------------ CASE2:起始值为大于零售单A ID的零售单,返回零售单B------------------------");
		MvcResult mr2 = mvc.perform(//
				get("/vip/retrieveNVipConsumeHistoryEx.bx?ID=" + vipCreate.getID() + "&startRetailTreadeIDInSQLite=" + localRetailTrade.getID()) //
						.session((MockHttpSession) Shared.getStaffLoginSession(mvc, Shared.PhoneOfBoss)) //
						.contentType(MediaType.APPLICATION_JSON)//
		)//
				.andExpect(status().isOk())//
				.andDo(print())//
				.andReturn();
		Shared.checkJSONErrorCode(mr2);

		JSONObject o2 = JSONObject.fromObject(mr2.getResponse().getContentAsString());
		Integer i2 = JsonPath.read(o2, "$.retailTradeList[0].ID");
		assertTrue(i2 == localRetailTrade2.getID());

		System.out.println("\n------------------------ CASE3:起始值为小于零售单B ID的零售单,返回零售单A------------------------");
		MvcResult mr3 = mvc.perform(//
				get("/vip/retrieveNVipConsumeHistoryEx.bx?ID=" + vipCreate.getID() + "&startRetailTreadeIDInSQLite=" + localRetailTrade2.getID() + "&bQuerySmallerThanStartID=1") //
						.session((MockHttpSession) Shared.getStaffLoginSession(mvc, Shared.PhoneOfBoss)) //
						.contentType(MediaType.APPLICATION_JSON)//
		)//
				.andExpect(status().isOk())//
				.andDo(print())//
				.andReturn();
		Shared.checkJSONErrorCode(mr3);

		JSONObject o3 = JSONObject.fromObject(mr3.getResponse().getContentAsString());
		Integer i3 = JsonPath.read(o3, "$.retailTradeList[0].ID");
		assertTrue(i3 == localRetailTrade.getID());

		System.out.println("\n------------------------ CASE4:VipID不存在------------------------");
		MvcResult mr4 = mvc.perform(//
				get("/vip/retrieveNVipConsumeHistoryEx.bx?ID=-1").session(//
						(MockHttpSession) Shared.getStaffLoginSession(mvc, Shared.PhoneOfBoss)//
				).contentType(MediaType.APPLICATION_JSON)//
		)//
				.andExpect(status().isOk())//
				.andDo(print())//
				.andReturn();

		Shared.checkJSONErrorCode(mr4, EnumErrorCode.EC_NoSuchData);

		System.out.println("\n------------------------ CASE5:没有权限操作------------------------");
//		MvcResult mr5 = mvc.perform(//
//				get("/vip/retrieveNVipConsumeHistoryEx.bx?ID=1").session(//
//						(MockHttpSession) Shared.getStaffLoginSession(mvc, Shared.PhoneOfManager)//
//				).contentType(MediaType.APPLICATION_JSON)//
//		)//
//				.andExpect(status().isOk())//
//				.andDo(print())//
//				.andReturn();
//
//		Shared.checkJSONErrorCode(mr5, EnumErrorCode.EC_NoPermission);

	}

	@Test
	public void testRetrieveNToCheckUniqueFieldEx() throws Exception {
		Shared.printTestMethodStartInfo();

		sessionBoss = Shared.getStaffLoginSession(mvc, Shared.PhoneOfBoss);

		// Vip vip = new Vip();

		System.out.println("----------------------------Case1:查询一个不存在的会员的手机号码------------------------------------------");
		MvcResult mr1 = mvc.perform(//
				post("/vip/retrieveNToCheckUniqueFieldEx.bx")//
						.contentType(MediaType.APPLICATION_JSON)//
						.session((MockHttpSession) sessionBoss)//
						.param(Vip.field.getFIELD_NAME_fieldToCheckUnique(), "1")//
						.param(Vip.field.getFIELD_NAME_queryKeyword(), "19144496272")//
		)//
				.andExpect(status().isOk())//
				.andDo(print())//
				.andReturn();
		Shared.checkJSONErrorCode(mr1);

		System.out.println("----------------------------Case2:查询一个已存在的会员的手机号码------------------------------------------");
		MvcResult mr2 = mvc.perform(//
				post("/vip/retrieveNToCheckUniqueFieldEx.bx")//
						.contentType(MediaType.APPLICATION_JSON)//
						.session((MockHttpSession) sessionBoss)//
						.param(Vip.field.getFIELD_NAME_fieldToCheckUnique(), "1")//
						.param(Vip.field.getFIELD_NAME_queryKeyword(), "13545678110")//
		)//
				.andExpect(status().isOk())//
				.andDo(print())//
				.andReturn();
		Shared.checkJSONErrorCode(mr2, EnumErrorCode.EC_Duplicated);

		System.out.println("----------------------------Case3:查询一个不存在的会员的身份证号------------------------------------------");
		MvcResult mr3 = mvc.perform(//
				post("/vip/retrieveNToCheckUniqueFieldEx.bx")//
						.contentType(MediaType.APPLICATION_JSON)//
						.session((MockHttpSession) sessionBoss)//
						.param(Vip.field.getFIELD_NAME_fieldToCheckUnique(), "2")//
						.param(Vip.field.getFIELD_NAME_queryKeyword(), "540883198412111666")//
		)//
				.andExpect(status().isOk())//
				.andDo(print())//
				.andReturn();
		Shared.checkJSONErrorCode(mr3);

		System.out.println("----------------------------Case4:查询一个已存在的会员的身份证号------------------------------------------");
		MvcResult mr4 = mvc.perform(//
				post("/vip/retrieveNToCheckUniqueFieldEx.bx")//
						.contentType(MediaType.APPLICATION_JSON)//
						.session((MockHttpSession) sessionBoss)//
						.param(Vip.field.getFIELD_NAME_fieldToCheckUnique(), "2")//
						.param(Vip.field.getFIELD_NAME_queryKeyword(), "320803199707016031")//
		)//
				.andExpect(status().isOk())//
				.andDo(print())//
				.andReturn();
		Shared.checkJSONErrorCode(mr4, EnumErrorCode.EC_Duplicated);

		System.out.println("----------------------------Case9:查询一个不存在的会员的邮箱------------------------------------------");
		MvcResult mr9 = mvc.perform(//
				post("/vip/retrieveNToCheckUniqueFieldEx.bx")//
						.contentType(MediaType.APPLICATION_JSON)//
						.session((MockHttpSession) sessionBoss)//
						.param(Vip.field.getFIELD_NAME_fieldToCheckUnique(), "5")//
						.param(Vip.field.getFIELD_NAME_queryKeyword(), "623456@bx.vip")//
		)//
				.andExpect(status().isOk())//
				.andDo(print())//
				.andReturn();
		Shared.checkJSONErrorCode(mr9);

		System.out.println("----------------------------Case10:查询一个已存在的会员的邮箱------------------------------------------");
		MvcResult mr10 = mvc.perform(//
				post("/vip/retrieveNToCheckUniqueFieldEx.bx")//
						.contentType(MediaType.APPLICATION_JSON)//
						.session((MockHttpSession) sessionBoss)//
						.param(Vip.field.getFIELD_NAME_fieldToCheckUnique(), "5")//
						.param(Vip.field.getFIELD_NAME_queryKeyword(), "123456@bx.vip")//
		)//
				.andExpect(status().isOk())//
				.andDo(print())//
				.andReturn();
		Shared.checkJSONErrorCode(mr10, EnumErrorCode.EC_Duplicated);

		// account字段已经被删除
		// System.out.println("----------------------------Case11:查询一个不存在的会员的登录账号------------------------------------------");
		// MvcResult mr11 = mvc.perform(//
		// post("/vip/retrieveNToCheckUniqueFieldEx.bx")//
		// .contentType(MediaType.APPLICATION_JSON)//
		// .session((MockHttpSession) session)//
		// .param(Vip.field.getFIELD_NAME_fieldToCheckUnique(), "6")//
		// .param(Vip.field.getFIELD_NAME_queryKeyword(), "623456")//
		// )//
		// .andExpect(status().isOk())//
		// .andDo(print())//
		// .andReturn();
		// Shared.checkJSONErrorCode(mr11);
		//
		// System.out.println("----------------------------Case12:查询一个已存在的会员的登录账号------------------------------------------");
		// MvcResult mr12 = mvc.perform(//
		// post("/vip/retrieveNToCheckUniqueFieldEx.bx")//
		// .contentType(MediaType.APPLICATION_JSON)//
		// .session((MockHttpSession) session)//
		// .param(Vip.field.getFIELD_NAME_fieldToCheckUnique(), "6")//
		// .param(Vip.field.getFIELD_NAME_queryKeyword(), "123456")//
		// )//
		// .andExpect(status().isOk())//
		// .andDo(print())//
		// .andReturn();
		// Shared.checkJSONErrorCode(mr12, EnumErrorCode.EC_Duplicated);

		System.out.println("----------------------------Case13:传入正确的int1，但传入的queryKeyword为空(当黑客行为，返回格式不符)------------------------------------------");
		String int13 = "2";
		String queryKeyword13 = null;
		MvcResult mr13 = mvc.perform(//
				post("/vip/retrieveNToCheckUniqueFieldEx.bx")//
						.contentType(MediaType.APPLICATION_JSON)//
						.session((MockHttpSession) sessionBoss)//
						.param(Vip.field.getFIELD_NAME_fieldToCheckUnique(), int13)//
						.param(Vip.field.getFIELD_NAME_queryKeyword(), queryKeyword13)//
		)//
				.andExpect(status().isOk())//
				.andDo(print())//
				.andReturn();
		Shared.checkJSONErrorCode(mr13, EnumErrorCode.EC_WrongFormatForInputField);

		System.out.println("----------------------------Case14:传入未定义检查那个字段的int1，(当黑客行为，返回格式不符)------------------------------------------");
		String int14 = "9999";
		String queryKeyword14 = "623456";
		MvcResult mr14 = mvc.perform(//
				post("/vip/retrieveNToCheckUniqueFieldEx.bx")//
						.contentType(MediaType.APPLICATION_JSON)//
						.session((MockHttpSession) sessionBoss)//
						.param(Vip.field.getFIELD_NAME_fieldToCheckUnique(), int14)//
						.param(Vip.field.getFIELD_NAME_queryKeyword(), queryKeyword14)//
		)//
				.andExpect(status().isOk())//
				.andDo(print())//
				.andReturn();
		Shared.checkJSONErrorCode(mr14, EnumErrorCode.EC_WrongFormatForInputField);

		// System.out.println("----------------------------Case13:没有权限进行查询---------------------------------------");
		// MvcResult mr13 = mvc.perform(//
		// post("/staff/retrieveNToCheckUniqueFieldEx.bx")//
		// .contentType(MediaType.APPLICATION_JSON)//
		// .session((MockHttpSession) Shared.getStaffLoginSession(mvc,
		// Shared.PhoneOfManager))//
		// .param(vip.getFIELD_NAME_fieldToCheckUnique(), "6")//
		// .param(vip.getFIELD_NAME_string1(), "623456")//
		// )//
		// .andExpect(status().isOk())//
		// .andDo(print())//
		// .andReturn();
		// Shared.checkJSONErrorCode(mr13, EnumErrorCode.EC_NoPermission);
	}
}
