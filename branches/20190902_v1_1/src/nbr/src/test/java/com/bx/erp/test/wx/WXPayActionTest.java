package com.bx.erp.test.wx;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MvcResult;
import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import com.bx.erp.action.BaseAction;
import com.bx.erp.model.RetailTrade;
import com.bx.erp.model.BaseModel.EnumBoolean;
import com.bx.erp.model.ErrorInfo.EnumErrorCode;
import com.bx.erp.model.wx.BaseWxModel;
import com.bx.erp.model.wx.WXPayInfo;
import com.bx.erp.test.BaseActionTest;
import com.bx.erp.test.Shared;
import com.github.wxpay.sdk.WXPayUtil;
import com.jayway.jsonpath.JsonPath;

import net.sf.json.JSONObject;

@WebAppConfiguration
public class WXPayActionTest extends BaseActionTest {

	@BeforeClass
	public void setup() {
		super.setUp();
	}

	@AfterClass
	public void tearDown() {
		super.tearDown();
	}

	/**
	 * 1、由于现在还无法模拟用户领取会员卡的操作，所以在测试积分变动时，使用的是预先创建好的会员信息
	 * 2、因为是预先创建的数据，所以数据库的积分可能和WX那边的积分总量不一致，这个错误不需要去修
	 * 3、现在如果本地数据库插入出错，只会log.error一个信息，暂时不做其他处理
	 */
	@Test
	public void testMicroPayEx() throws Exception {
		WXPayInfo wpi = new WXPayInfo();
		wpi.setAuth_code("134617607342397775");
		wpi.setTotal_fee("501.00");
		wpi.setAddBonus(30);
		// 下面5个数据，都是预先创建好的数据，如果后面测试出现问题，可能是会员卡的一些状态问题导致的。
		wpi.setWxVipCardID("p1uoyw82lTouV3LgrdUXldtvi5c8");
		wpi.setWxVipCardCode("415430877106");
		wpi.setVipID(6);
		wpi.setWxVipID(1);
		wpi.setWxVipCardDetailID(1);
		wpi.setBonusIsChanged(EnumBoolean.EB_Yes.getIndex());
		MvcResult mr = mvc.perform(//
				post("/wxpay/microPayEx.bx")//
						.param(WXPayInfo.field.getFIELD_NAME_auth_code(), wpi.getAuth_code()) //
						.param(WXPayInfo.field.getFIELD_NAME_total_fee(), wpi.getTotal_fee()) //
						.param(WXPayInfo.field.getFIELD_NAME_addBonus(), String.valueOf(wpi.getAddBonus())) //
						.param(WXPayInfo.field.getFIELD_NAME_wxVipCardID(), wpi.getWxVipCardID()) //
						.param(WXPayInfo.field.getFIELD_NAME_wxVipCardCode(), wpi.getWxVipCardCode()) //
						.param(WXPayInfo.field.getFIELD_NAME_vipID(), String.valueOf(wpi.getVipID())) //
						.param(WXPayInfo.field.getFIELD_NAME_wxVipID(), String.valueOf(wpi.getWxVipID())) //
						.param(WXPayInfo.field.getFIELD_NAME_wxVipCardDetailID(), String.valueOf(wpi.getWxVipCardDetailID())) //
						.param(WXPayInfo.field.getFIELD_NAME_bonusIsChanged(), String.valueOf(wpi.getBonusIsChanged())) //
						.contentType(MediaType.APPLICATION_JSON)//
						.session((MockHttpSession) Shared.getStaffLoginSession(mvc, Shared.PhoneOfBoss)) //
		)//
				.andExpect(status().isOk())//
				.andDo(print())//
				.andReturn();
		//
		Shared.checkJSONErrorCode(mr);

		JSONObject mircoPayJson = JSONObject.fromObject(mr.getResponse().getContentAsString());
		if (mircoPayJson.get(BaseWxModel.WXPAY_ERR_CODE).equals("AUTH_CODE_INVALID")) {
			Shared.checkJSONErrorCode(mr, EnumErrorCode.EC_OtherError);
			Assert.assertTrue(false, mircoPayJson.get(BaseWxModel.WXPAY_ERR_CODE_DES).toString() + "(本测试不需要修)");
		}
	}

	// @Test //需要提供真实的授权码和优惠券
	public void testMicroPayExCase2() throws Exception {
		Shared.caseLog("服务器请求微信支付，携带优惠券码,则支付成功后进行核销优惠券");

		WXPayInfo wpi = new WXPayInfo();
		wpi.setAuth_code("134579009772861698");
		wpi.setTotal_fee("1.00");
		wpi.setCouponCode("765401940458");

		MvcResult mr = mvc.perform(//
				post("/wxpay/microPayEx.bx")//
						.param(WXPayInfo.field.getFIELD_NAME_auth_code(), wpi.getAuth_code()) //
						.param(WXPayInfo.field.getFIELD_NAME_total_fee(), wpi.getTotal_fee()) //
						.param(WXPayInfo.field.getFIELD_NAME_couponCode(), wpi.getCouponCode()) //
						.contentType(MediaType.APPLICATION_JSON)//
						.session((MockHttpSession) Shared.getStaffLoginSession(mvc, Shared.PhoneOfBoss)) //
		)//
				.andExpect(status().isOk())//
				.andDo(print())//
				.andReturn();
		//
		Shared.checkJSONErrorCode(mr);
	}

	// @Test //需要提供真实的授权码和优惠券
	public void testMicroPayExCase3() throws Exception {
		Shared.caseLog("服务器请求微信支付，没有携带优惠券码,支付成功后响应POS端");

		WXPayInfo wpi = new WXPayInfo();
		wpi.setAuth_code("134579009772861698");
		wpi.setTotal_fee("1.00");

		MvcResult mr = mvc.perform(//
				post("/wxpay/microPayEx.bx")//
						.param(WXPayInfo.field.getFIELD_NAME_auth_code(), wpi.getAuth_code()) //
						.param(WXPayInfo.field.getFIELD_NAME_total_fee(), wpi.getTotal_fee()) //
						.contentType(MediaType.APPLICATION_JSON)//
						.session((MockHttpSession) Shared.getStaffLoginSession(mvc, Shared.PhoneOfBoss)) //
		)//
				.andExpect(status().isOk())//
				.andDo(print())//
				.andReturn();
		//
		Shared.checkJSONErrorCode(mr);
	}

	// @Test //需要提供真实的授权码和优惠券
	public void testMicroPayExCase4() throws Exception {
		Shared.caseLog("服务器请求微信支付，携带了优惠券码，支付失败,优惠券并未核销");

		WXPayInfo wpi = new WXPayInfo();
		wpi.setAuth_code("134579009772861x69118");
		wpi.setTotal_fee("1.00");
		wpi.setCouponCode("587485350006");

		MvcResult mr = mvc.perform(//
				post("/wxpay/microPayEx.bx")//
						.param(WXPayInfo.field.getFIELD_NAME_auth_code(), wpi.getAuth_code()) //
						.param(WXPayInfo.field.getFIELD_NAME_total_fee(), wpi.getTotal_fee()) //
						.param(WXPayInfo.field.getFIELD_NAME_couponCode(), wpi.getCouponCode()) //
						.contentType(MediaType.APPLICATION_JSON)//
						.session((MockHttpSession) Shared.getStaffLoginSession(mvc, Shared.PhoneOfBoss)) //
		)//
				.andExpect(status().isOk())//
				.andDo(print())//
				.andReturn();
		//
		Shared.checkJSONErrorCode(mr, EnumErrorCode.EC_OtherError);
	}

	// @Test //需要提供真实的授权码和优惠券
	public void testMicroPayExCase5() throws Exception {
		Shared.caseLog("服务器请求微信支付，携带了优惠券码，支付成功后进行核销优惠券，优惠券码不存在，核销失败");

		WXPayInfo wpi = new WXPayInfo();
		wpi.setAuth_code("134579009772861698");
		wpi.setTotal_fee("1.00");
		wpi.setCouponCode("587485350fff006");

		MvcResult mr = mvc.perform(//
				post("/wxpay/microPayEx.bx")//
						.param(WXPayInfo.field.getFIELD_NAME_auth_code(), wpi.getAuth_code()) //
						.param(WXPayInfo.field.getFIELD_NAME_total_fee(), wpi.getTotal_fee()) //
						.param(WXPayInfo.field.getFIELD_NAME_couponCode(), wpi.getCouponCode()) //
						.contentType(MediaType.APPLICATION_JSON)//
						.session((MockHttpSession) Shared.getStaffLoginSession(mvc, Shared.PhoneOfBoss)) //
		)//
				.andExpect(status().isOk())//
				.andDo(print())//
				.andReturn();
		//
		Shared.checkJSONErrorCode(mr);
		String json = mr.getResponse().getContentAsString();
		JSONObject o = JSONObject.fromObject(json);
		String msg = JsonPath.read(o, "$." + BaseAction.KEY_HTMLTable_Parameter_msg);
		Assert.assertTrue("支付成功，核销优惠券失败！".equals(msg), "返回结果不一样");
	}

	// @Test //需要提供真实的授权码和优惠券
	public void testMicroPayExCase6() throws Exception {
		Shared.caseLog("服务器请求微信支付，携带了优惠券码,支付成功后进行核销优惠券,优惠券码的状态为不可核销的。核销失败。");

		WXPayInfo wpi = new WXPayInfo();
		wpi.setAuth_code("134579009772861698");
		wpi.setTotal_fee("1.00");
		wpi.setCouponCode("587485350006");

		MvcResult mr = mvc.perform(//
				post("/wxpay/microPayEx.bx")//
						.param(WXPayInfo.field.getFIELD_NAME_auth_code(), wpi.getAuth_code()) //
						.param(WXPayInfo.field.getFIELD_NAME_total_fee(), wpi.getTotal_fee()) //
						.param(WXPayInfo.field.getFIELD_NAME_couponCode(), wpi.getCouponCode()) //
						.contentType(MediaType.APPLICATION_JSON)//
						.session((MockHttpSession) Shared.getStaffLoginSession(mvc, Shared.PhoneOfBoss)) //
		)//
				.andExpect(status().isOk())//
				.andDo(print())//
				.andReturn();
		//
		Shared.checkJSONErrorCode(mr);
		String json = mr.getResponse().getContentAsString();
		JSONObject o = JSONObject.fromObject(json);
		String msg = JsonPath.read(o, "$." + BaseAction.KEY_HTMLTable_Parameter_msg);
		Assert.assertTrue("支付成功，核销优惠券失败！".equals(msg), "返回结果不一样");
	}

	@Test
	public void testMicroPayExCase8() throws Exception {
		Shared.caseLog("服务器请求微信支付0元");

		WXPayInfo wpi = new WXPayInfo();
		wpi.setAuth_code("134579009772861698");
		wpi.setTotal_fee("0.00");

		MvcResult mr = mvc.perform(//
				post("/wxpay/microPayEx.bx")//
						.param(WXPayInfo.field.getFIELD_NAME_auth_code(), wpi.getAuth_code()) //
						.param(WXPayInfo.field.getFIELD_NAME_total_fee(), wpi.getTotal_fee()) //
						.contentType(MediaType.APPLICATION_JSON)//
						.session((MockHttpSession) Shared.getStaffLoginSession(mvc, Shared.PhoneOfBoss)) //
		)//
				.andExpect(status().isOk())//
				.andDo(print())//
				.andReturn();
		//
		String responseData = mr.getResponse().getContentAsString();
		Assert.assertTrue("".equals(responseData), "返回结果和预期的不一样");
	}

	@Test
	public void testMicroPayExCase7() throws Exception {
		Shared.caseLog("支付金额为0元，且携带了优惠券码。返回消息：'顾客使用优惠券后微信支付金额为0元,无法进行微信支付'");

		WXPayInfo wpi = new WXPayInfo();
		wpi.setAuth_code("134579009772861698");
		wpi.setTotal_fee("0.00");
		wpi.setCouponCode("587485350006");

		MvcResult mr = mvc.perform(//
				post("/wxpay/microPayEx.bx")//
						.param(WXPayInfo.field.getFIELD_NAME_auth_code(), wpi.getAuth_code()) //
						.param(WXPayInfo.field.getFIELD_NAME_total_fee(), wpi.getTotal_fee()) //
						.param(WXPayInfo.field.getFIELD_NAME_couponCode(), wpi.getCouponCode()) //
						.contentType(MediaType.APPLICATION_JSON)//
						.session((MockHttpSession) Shared.getStaffLoginSession(mvc, Shared.PhoneOfBoss)) //
		)//
				.andExpect(status().isOk())//
				.andDo(print())//
				.andReturn();
		//
		Shared.checkJSONErrorCode(mr, EnumErrorCode.EC_BusinessLogicNotDefined);
	}

	// @Test
	public void testRefundEx() throws Exception {
		// RetailTrade rt = new RetailTrade();
		String out_refund_no = String.valueOf(System.currentTimeMillis() % 1000000) + WXPayUtil.generateNonceStr().substring(12);
		MvcResult mr = mvc.perform(//
				post("/wxpay/refundEx.bx")//
						.param(RetailTrade.field.getFIELD_NAME_wxOrderSN(), "4289627489620190227164951266940") //
						.param(RetailTrade.field.getFIELD_NAME_wxTradeNO(), out_refund_no) //
						.param(RetailTrade.field.getFIELD_NAME_amount(), "1") //
						.contentType(MediaType.APPLICATION_JSON)//
						.session((MockHttpSession) Shared.getStaffLoginSession(mvc, Shared.PhoneOfBoss)) //
		)//
				.andExpect(status().isOk())//
				.andDo(print())//
				.andReturn();
		JSONObject mircoPayJson = JSONObject.fromObject(mr.getResponse().getContentAsString());
		if (mircoPayJson.get(BaseWxModel.WXPAY_ERR_CODE).equals("PARAM_ERROR")) {
			Shared.checkJSONErrorCode(mr, EnumErrorCode.EC_OtherError);
			Assert.assertTrue(false, mircoPayJson.get(BaseWxModel.WXPAY_ERR_CODE_DES).toString() + "(本测试不需要修)");
		}
		Shared.checkJSONErrorCode(mr);
	}

	@Test
	public void testReverseEx() throws Exception {
		// RetailTrade rt = new RetailTrade();
		// String out_reverse_no = String.valueOf(System.currentTimeMillis() % 1000000)
		// + WXPayUtil.generateNonceStr().substring(12);

		// WXPayInfo wpi = new WXPayInfo();
		// wpi.setAuth_code("134617607342397775");
		// wpi.setTotal_fee("301");
		// MvcResult mr1 = mvc.perform(//
		// post("/wxpay/microPayEx.bx")//
		// .param(wpi.getFIELD_NAME_auth_code(),wpi.getAuth_code())
		// .param(wpi.getFIELD_NAME_total_fee(), wpi.getTotal_fee())
		// .contentType(MediaType.APPLICATION_JSON)//
		// )//
		// .andExpect(status().isOk())//
		// .andDo(print())//
		// .andReturn();
		// Shared.checkJSONErrorCode(mr1);

		MvcResult mr2 = mvc.perform(//
				post("/wxpay/reverseEx.bx")//
						// .param(rt.getFIELD_NAME_wxRefundSubMchID(), "1523999791") //
						// .param(rt.getFIELD_NAME_wxOrderSN(), "4289627489620190227164951266940") //
						// .param(rt.getFIELD_NAME_wxTradeNO(), out_reverse_no) //
						.contentType(MediaType.APPLICATION_JSON)//
						.session((MockHttpSession) Shared.getStaffLoginSession(mvc, Shared.PhoneOfBoss)) //
		)//
				.andExpect(status().isOk())//
				.andDo(print())//
				.andReturn();
		Shared.checkJSONErrorCode(mr2);
	}
}
