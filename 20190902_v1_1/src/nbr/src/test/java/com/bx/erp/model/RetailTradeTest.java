package com.bx.erp.model;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;

import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import com.bx.erp.action.BaseAction;
import com.bx.erp.action.bo.BaseBO;
import com.bx.erp.model.RetailTrade.EnumStatusRetailTrade;
import com.bx.erp.model.trade.RetailTradePromoting;
import com.bx.erp.model.trade.RetailTradePromotingFlow;
import com.bx.erp.test.BaseRetailTradeTest;
import com.bx.erp.test.Shared;
import com.bx.erp.util.FieldFormat;
import com.bx.erp.util.GeneralUtil;

public class RetailTradeTest extends BaseModelTest {

	@Override
	protected BaseModel getMasterTableObject() throws CloneNotSupportedException, InterruptedException {
		RetailTrade retailTrade = DataInput.getRetailTrade();
		return retailTrade;
	}

	@Override
	protected BaseModel updateMasterTableObject(BaseModel master) throws CloneNotSupportedException, InterruptedException {
		master = DataInput.getRetailTrade();
		return master;
	}

	@Override
	protected List<BaseModel> getSlaveTableObject() throws CloneNotSupportedException, InterruptedException {
		List<BaseModel> bmList = new ArrayList<BaseModel>();
		//
		RetailTradeCommodity rtc1 = BaseRetailTradeTest.DataInput.getRetailTradeCommodity();
		RetailTradeCommodity rtc2 = BaseRetailTradeTest.DataInput.getRetailTradeCommodity();
		rtc2.setCommodityID(2); // 这里需要使两个从表对象的商品ID不同
		//
		bmList.add(rtc1);
		bmList.add(rtc2);
		return bmList;
	}

	@Override
	protected List<BaseModel> updateSlaveTableObject(List<BaseModel> slave) {
		slave.remove(0);
		return slave;
	}

	@BeforeClass
	public void setup() {
		Shared.printTestClassStartInfo();
	}

	@AfterClass
	public void tearDown() {
		Shared.printTestClassEndInfo();
	}

	// ... HYJ,为什么要在Revision 2233中删除掉以下测试代码？

	// @Test
	// public void f() {
	// Shared.printTestMethodStartInfo();
	//
	// }
	//
	// private RetailTrade retailTrade = new RetailTrade();
	//
	// @Test
	// public void parse1() {
	// RetailTradeMapper mapper = (RetailTradeMapper)
	// applicationContext.getBean("retailTradeMapper");
	//
	// Map<String, Object> params = retailTrade.getRetrieveNParam(new
	// RetailTrade());
	// List<BaseModel> bmList = (List<BaseModel>) mapper.retrieveN(params);
	// for (BaseModel bm : bmList) {
	// String json = retailTrade.toJson(bm);
	// BaseModel rt = retailTrade.parse1(json);
	// System.err.println(rt);
	// assertTrue(rt != null);
	// }
	// }
	//
	// @Test
	// public void parseN() {
	// RetailTradeMapper mapper = (RetailTradeMapper)
	// applicationContext.getBean("retailTradeMapper");
	//
	// Map<String, Object> params = retailTrade.getRetrieveNParam(new
	// RetailTrade());
	// List<BaseModel> bmList = (List<BaseModel>) mapper.retrieveN(params);
	//
	// String json = retailTrade.toJson(bmList);
	// List<BaseModel> rtList = retailTrade.parseN(json);
	// System.err.println(rtList);
	// assertTrue(rtList.size() > 0);
	//
	// }

	public static class DataInput {
		private static RetailTrade retailTradeInput = null;

		protected static final RetailTrade getRetailTrade() {
			Random ran = new Random();
			retailTradeInput = new RetailTrade();
			retailTradeInput.setVipID(1);
			retailTradeInput.setSn("");
			retailTradeInput.setLocalSN(ran.nextInt());
			retailTradeInput.setPos_ID(ran.nextInt(5) + 1);
			retailTradeInput.setLogo("url:" + String.valueOf(System.currentTimeMillis()).substring(6));
			retailTradeInput.setSaleDatetime(new Date());
			retailTradeInput.setStatus(EnumStatusRetailTrade.ESRT_Normal.getIndex());
			retailTradeInput.setStaffID(ran.nextInt(5) + 1);
			retailTradeInput.setPaymentType(ran.nextInt(5) + 1);
			retailTradeInput.setPaymentAccount("A" + String.valueOf(System.currentTimeMillis()).substring(6));
			retailTradeInput.setRemark("remark:" + String.valueOf(System.currentTimeMillis()).substring(6));
			retailTradeInput.setSourceID(BaseAction.INVALID_ID);
			retailTradeInput.setSmallSheetID(ran.nextInt(7) + 1);
			retailTradeInput.setAmount(50);
			retailTradeInput.setAmountCash(50d);
			retailTradeInput.setAliPayOrderSN("zfb" + String.valueOf(System.currentTimeMillis()).substring(6));
			retailTradeInput.setWxOrderSN("wx" + String.valueOf(System.currentTimeMillis()).substring(6));
			retailTradeInput.setWxRefundDesc("xx" + String.valueOf(System.currentTimeMillis()).substring(6));
			retailTradeInput.setWxTradeNO("no" + String.valueOf(System.currentTimeMillis()).substring(6));
			retailTradeInput.setSyncDatetime(new Date());
			retailTradeInput.setDatetimeStart(new Date());
			retailTradeInput.setDatetimeEnd(new Date());

			RetailTradeCommodity retailTradeCommodity = new RetailTradeCommodity();
			retailTradeCommodity.setCommodityID(1);
			retailTradeCommodity.setNO(ran.nextInt());
			retailTradeCommodity.setPriceOriginal(ran.nextDouble());
			retailTradeCommodity.setBarcodeID(1);
			//
			// RetailTradeCommodity retailTradeCommodity2 = new RetailTradeCommodity();
			// retailTradeCommodity2.setCommodityID(2);
			// retailTradeCommodity2.setNO(ran.nextInt());
			// retailTradeCommodity2.setPriceOriginal(ran.nextDouble());
			// retailTradeCommodity2.setDiscount(0.9);
			// //retailTradeCommodity2.setIsGift(0);
			// retailTradeCommodity2.setBarcodeID(1);
			// //
			// List<RetailTradeCommodity> listRetailTradeCommodity = new
			// ArrayList<RetailTradeCommodity>();
			// listRetailTradeCommodity.add(retailTradeCommodity);
			// listRetailTradeCommodity.add(retailTradeCommodity2);
			// //
			// retailTradeInput.setListSlave1(listRetailTradeCommodity);

			return (RetailTrade) retailTradeInput.clone();
		}
	}

	@Test
	public void testCheckCreate() {
		Shared.printTestMethodStartInfo();
		RetailTrade retailTrade = new RetailTrade();
		retailTrade.setVipID(1);
		retailTrade.setSn("LS2019052915010800011234");
		retailTrade.setPos_ID(1);
		retailTrade.setLocalSN(1);
		retailTrade.setLogo("123463");
		retailTrade.setStaffID(1);
		retailTrade.setPaymentType(1);
		retailTrade.setPaymentAccount("123456");
		retailTrade.setStatus(EnumStatusRetailTrade.ESRT_Normal.getIndex());
		retailTrade.setRemark("");
		retailTrade.setSourceID(-1);
		retailTrade.setSmallSheetID(1);
		retailTrade.setAmount(0.000000d);
		retailTrade.setAmountCash(0.000000d);
		retailTrade.setAmountAlipay(0.000000d);
		retailTrade.setAmountWeChat(0.000000d);
		retailTrade.setAmount1(0.000000d);
		retailTrade.setAmount2(0.000000d);
		retailTrade.setAmount3(0.000000d);
		retailTrade.setAmount4(0.000000d);
		retailTrade.setAmount5(0.000000d);
		retailTrade.setShopID(2);

		String error = "";
		// 实体类已把此check注释，故注释测试
		// retailTrade.setVipID(-99);
		// error = retailTrade.checkCreate(BaseBO.INVALID_CASE_ID);
		// Assert.assertEquals(error, RetailTrade.FIELD_ERROR_VipID);
		// retailTrade.setVipID(0);
		// error = retailTrade.checkCreate(BaseBO.INVALID_CASE_ID);
		// Assert.assertEquals(error, RetailTrade.FIELD_ERROR_VipID);
		// retailTrade.setVipID(1);
		//
		// 设置F_SN的长度小于24，结果不通过。
		retailTrade.setSn("LS1234567");
		error = retailTrade.checkCreate(BaseBO.INVALID_CASE_ID);
		Assert.assertEquals(error, RetailTrade.FIELD_ERROR_sn);
		// 设置F_SN的长度等于25，结果不通过。
		retailTrade.setSn("LS12345678912345678912345");
		error = retailTrade.checkCreate(BaseBO.INVALID_CASE_ID);
		Assert.assertEquals(error, RetailTrade.FIELD_ERROR_sn);
		// 设置F_SN的长度超过26，结果不通过。
		retailTrade.setSn("LS1234567891234567891234567891");
		error = retailTrade.checkCreate(BaseBO.INVALID_CASE_ID);
		Assert.assertEquals(error, RetailTrade.FIELD_ERROR_sn);
		// 设置F_SN不是LS开头，结果不通过。
		retailTrade.setSn("1234567890123456789012");
		error = retailTrade.checkCreate(BaseBO.INVALID_CASE_ID);
		Assert.assertEquals(error, RetailTrade.FIELD_ERROR_sn);
		// 设置F_SN包含中文，结果不通过。
		retailTrade.setSn("LS123456星巴克1234567891234");
		error = retailTrade.checkCreate(BaseBO.INVALID_CASE_ID);
		Assert.assertEquals(error, RetailTrade.FIELD_ERROR_sn);
		// 设置F_SN包含其他英文字符，结果不通过。
		retailTrade.setSn("LS123456abc1234567891234");
		error = retailTrade.checkCreate(BaseBO.INVALID_CASE_ID);
		Assert.assertEquals(error, RetailTrade.FIELD_ERROR_sn);
		// 设置F_SN包含空格，结果不通过
		retailTrade.setSn("LS123456   1234567891234");
		error = retailTrade.checkCreate(BaseBO.INVALID_CASE_ID);
		Assert.assertEquals(error, RetailTrade.FIELD_ERROR_sn);
		// 设置F_SN为null，结果不通过
		retailTrade.setSn(null);
		error = retailTrade.checkCreate(BaseBO.INVALID_CASE_ID);
		Assert.assertEquals(error, RetailTrade.FIELD_ERROR_sn);
		// 设置F_SN为空串，结果不通过
		retailTrade.setSn("");
		error = retailTrade.checkCreate(BaseBO.INVALID_CASE_ID);
		Assert.assertEquals(error, RetailTrade.FIELD_ERROR_sn);
		// 设置F_SN包含特殊字符，结果不通过
		retailTrade.setSn("LS123456*&^1234567891234");
		error = retailTrade.checkCreate(BaseBO.INVALID_CASE_ID);
		Assert.assertEquals(error, RetailTrade.FIELD_ERROR_sn);
		// 设置F_SN的日期比当前时间晚，结果不通过。
		SimpleDateFormat sdf = new SimpleDateFormat(BaseAction.DATE_FORMAT_RetailTradeSN);
		retailTrade.setSn("LS" + sdf.format(new Date(new Date().getTime() + new Date().getTime())) + "00011234");
		error = retailTrade.checkCreate(BaseBO.INVALID_CASE_ID);
		Assert.assertEquals(error, RetailTrade.FIELD_ERROR_sn);
		// 设置F_SN为26位的退货单，结尾为_1，且格式正确，结果通过。
		retailTrade.setSn("LS2019010101010100011234_1");
		error = retailTrade.checkCreate(BaseBO.INVALID_CASE_ID);
		Assert.assertEquals(error, "");
		// 设置F_SN为24位普通零售单，且格式正确，结果通过。
		retailTrade.setSn("LS2019010101010100011234");
		error = retailTrade.checkCreate(BaseBO.INVALID_CASE_ID);
		Assert.assertEquals(error, "");
		//
		retailTrade.setLocalSN(-99);
		error = retailTrade.checkCreate(BaseBO.INVALID_CASE_ID);
		Assert.assertEquals(error, RetailTrade.FIELD_ERROR_LOCALSN);
		retailTrade.setLocalSN(0);
		error = retailTrade.checkCreate(BaseBO.INVALID_CASE_ID);
		Assert.assertEquals(error, RetailTrade.FIELD_ERROR_LOCALSN);
		retailTrade.setLocalSN(1);
		//
		retailTrade.setPos_ID(-99);
		error = retailTrade.checkCreate(BaseBO.INVALID_CASE_ID);
		Assert.assertEquals(error, RetailTrade.FIELD_ERROR_POSID);
		retailTrade.setPos_ID(0);
		error = retailTrade.checkCreate(BaseBO.INVALID_CASE_ID);
		Assert.assertEquals(error, RetailTrade.FIELD_ERROR_POSID);
		retailTrade.setPos_ID(1);
		// 实体类已把此check注释，故注释测试
		// String str = "1234567890123456789012345678901234567890";
		// retailTrade.setLogo(str + str + str + str);
		// error = retailTrade.checkCreate(BaseBO.INVALID_CASE_ID);
		// Assert.assertEquals(error, RetailTrade.FIELD_ERROR_logo);
		// retailTrade.setLogo(null);
		// error = retailTrade.checkCreate(BaseBO.INVALID_CASE_ID);
		// Assert.assertEquals(error, RetailTrade.FIELD_ERROR_logo);
		// retailTrade.setLogo("123463");
		//
		retailTrade.setStaffID(-99);
		error = retailTrade.checkCreate(BaseBO.INVALID_CASE_ID);
		Assert.assertEquals(error, RetailTrade.FIELD_ERROR_StaffID);
		retailTrade.setStaffID(0);
		error = retailTrade.checkCreate(BaseBO.INVALID_CASE_ID);
		Assert.assertEquals(error, RetailTrade.FIELD_ERROR_StaffID);
		retailTrade.setStaffID(1);
		//
		retailTrade.setPaymentType(-99);
		error = retailTrade.checkCreate(BaseBO.INVALID_CASE_ID);
		Assert.assertEquals(error, RetailTrade.FIELD_ERROR_paymentType);
		retailTrade.setPaymentType(0);
		error = retailTrade.checkCreate(BaseBO.INVALID_CASE_ID);
		Assert.assertEquals(error, RetailTrade.FIELD_ERROR_paymentType);
		retailTrade.setPaymentType(8);
		error = retailTrade.checkCreate(BaseBO.INVALID_CASE_ID);
		Assert.assertEquals(error, RetailTrade.FIELD_ERROR_paymentType);
		retailTrade.setPaymentType(1);
		// 实体类已把此check注释，故注释测试
		// retailTrade.setPaymentAccount("12345678901234567890123");
		// error = retailTrade.checkCreate(BaseBO.INVALID_CASE_ID);
		// Assert.assertEquals(error, RetailTrade.FIELD_ERROR_paymentAccount);
		// retailTrade.setPaymentAccount("");
		// error = retailTrade.checkCreate(BaseBO.INVALID_CASE_ID);
		// Assert.assertEquals(error, RetailTrade.FIELD_ERROR_paymentAccount);
		// retailTrade.setPaymentAccount("123456");
		//
		retailTrade.setRemark("12345678901234567890123");
		error = retailTrade.checkCreate(BaseBO.INVALID_CASE_ID);
		Assert.assertEquals(error, RetailTrade.FIELD_ERROR_remark);
		retailTrade.setRemark("");
		//
		retailTrade.setSourceID(-99);
		error = retailTrade.checkCreate(BaseBO.INVALID_CASE_ID);
		Assert.assertEquals(error, RetailTrade.FIELD_ERROR_sourceID);
		retailTrade.setSourceID(0);
		error = retailTrade.checkCreate(BaseBO.INVALID_CASE_ID);
		Assert.assertEquals(error, RetailTrade.FIELD_ERROR_sourceID);
		retailTrade.setSourceID(1);
		//
		retailTrade.setAmount(-0.01d);
		error = retailTrade.checkCreate(BaseBO.INVALID_CASE_ID);
		Assert.assertEquals(error, String.format(RetailTrade.FIELD_ERROR_Amount, RetailTrade.field.getFIELD_NAME_amount()));
		retailTrade.setAmount(0.000000d);
		//
		retailTrade.setAmountCash(-0.01d);
		error = retailTrade.checkCreate(BaseBO.INVALID_CASE_ID);
		Assert.assertEquals(error, String.format(RetailTrade.FIELD_ERROR_Amount, RetailTrade.field.getFIELD_NAME_amountCash()));
		retailTrade.setAmountCash(0.000000d);
		//
		retailTrade.setAmountAlipay(-0.01d);
		error = retailTrade.checkCreate(BaseBO.INVALID_CASE_ID);
		Assert.assertEquals(error, String.format(RetailTrade.FIELD_ERROR_Amount, RetailTrade.field.getFIELD_NAME_amountAlipay()));
		retailTrade.setAmountAlipay(0.000000d);
		//
		retailTrade.setAmountWeChat(-0.01d);
		error = retailTrade.checkCreate(BaseBO.INVALID_CASE_ID);
		Assert.assertEquals(error, String.format(RetailTrade.FIELD_ERROR_Amount, RetailTrade.field.getFIELD_NAME_amountWeChat()));
		retailTrade.setAmountWeChat(0.000000d);
		//
		retailTrade.setAmount1(-0.01d);
		error = retailTrade.checkCreate(BaseBO.INVALID_CASE_ID);
		Assert.assertEquals(error, String.format(RetailTrade.FIELD_ERROR_Amount, RetailTrade.field.getFIELD_NAME_amount1()));
		retailTrade.setAmount1(0.000000d);
		//
		retailTrade.setAmount2(-0.01d);
		error = retailTrade.checkCreate(BaseBO.INVALID_CASE_ID);
		Assert.assertEquals(error, String.format(RetailTrade.FIELD_ERROR_Amount, RetailTrade.field.getFIELD_NAME_amount2()));
		retailTrade.setAmount2(0.000000d);
		//
		retailTrade.setAmount3(-0.01d);
		error = retailTrade.checkCreate(BaseBO.INVALID_CASE_ID);
		Assert.assertEquals(error, String.format(RetailTrade.FIELD_ERROR_Amount, RetailTrade.field.getFIELD_NAME_amount3()));
		retailTrade.setAmount3(0.000000d);
		//
		retailTrade.setAmount4(-0.01d);
		error = retailTrade.checkCreate(BaseBO.INVALID_CASE_ID);
		Assert.assertEquals(error, String.format(RetailTrade.FIELD_ERROR_Amount, RetailTrade.field.getFIELD_NAME_amount4()));
		retailTrade.setAmount4(0.000000d);
		//
		retailTrade.setAmount5(-0.01d);
		error = retailTrade.checkCreate(BaseBO.INVALID_CASE_ID);
		Assert.assertEquals(error, String.format(RetailTrade.FIELD_ERROR_Amount, RetailTrade.field.getFIELD_NAME_amount5()));
		retailTrade.setAmount5(0.000000d);
		//
		retailTrade.setSmallSheetID(-1);
		error = retailTrade.checkCreate(BaseBO.INVALID_CASE_ID);
		Assert.assertEquals(error, RetailTrade.FIELD_ERROR_smallSheetID);
		retailTrade.setSmallSheetID(0);
		error = retailTrade.checkCreate(BaseBO.INVALID_CASE_ID);
		Assert.assertEquals(error, RetailTrade.FIELD_ERROR_smallSheetID);
		retailTrade.setSmallSheetID(1);
		//
		error = retailTrade.checkCreate(BaseBO.INVALID_CASE_ID);
		Assert.assertEquals(error, "");

		// 检查：每种支付方式对应的钱加起来=应收款
		retailTrade.setAmount(10d);
		// case1
		retailTrade.setPaymentType(1);
		retailTrade.setAmountCash(9d);
		error = retailTrade.checkCreate(BaseBO.INVALID_CASE_ID);
		Assert.assertEquals(error, RetailTrade.FIELD_ERROR_amountTotal);
		retailTrade.setAmountCash(10d);
		error = retailTrade.checkCreate(BaseBO.INVALID_CASE_ID);
		Assert.assertEquals(error, "");
		// case2
		retailTrade.setPaymentType(3);
		retailTrade.setAmountCash(9d);
		retailTrade.setAmountAlipay(6d);
		error = retailTrade.checkCreate(BaseBO.INVALID_CASE_ID);
		Assert.assertEquals(error, RetailTrade.FIELD_ERROR_amountTotal);
		retailTrade.setAmountCash(4d);
		retailTrade.setAmountAlipay(6d);
		error = retailTrade.checkCreate(BaseBO.INVALID_CASE_ID);
		Assert.assertEquals(error, "");
		// case3
		retailTrade.setPaymentType(7);
		retailTrade.setAmountCash(1d);
		retailTrade.setAmountAlipay(6d);
		retailTrade.setAmountWeChat(2d);
		error = retailTrade.checkCreate(BaseBO.INVALID_CASE_ID);
		Assert.assertEquals(error, RetailTrade.FIELD_ERROR_amountTotal);
		retailTrade.setAmountCash(1d);
		retailTrade.setAmountAlipay(6d);
		retailTrade.setAmountWeChat(3d);
		error = retailTrade.checkCreate(BaseBO.INVALID_CASE_ID);
		Assert.assertEquals(error, "");

		// 检查：如果只有现金支付，则可以为0.000000d，如果混合支付带有现金支付，那么现金支付不可为0.000000d,支付方式为2，对应的钱应该>0.000000d
		retailTrade.setAmount(10d);
		retailTrade.setAmountCash(RetailTrade.Min_Amount);
		retailTrade.setAmountAlipay(RetailTrade.Min_Amount);
		retailTrade.setAmountWeChat(RetailTrade.Min_Amount);
		// case1
		retailTrade.setPaymentType(3);
		retailTrade.setAmountCash(RetailTrade.Min_Amount);
		retailTrade.setAmountAlipay(10d);
		error = retailTrade.checkCreate(BaseBO.INVALID_CASE_ID);
		Assert.assertEquals(error, String.format(RetailTrade.FIELD_ERROR_payment, RetailTrade.field.getFIELD_NAME_amountCash(), RetailTrade.field.getFIELD_NAME_amountCash()));
		// case2
		retailTrade.setPaymentType(7);
		retailTrade.setAmountCash(7d);
		retailTrade.setAmountAlipay(RetailTrade.Min_Amount);
		retailTrade.setAmountWeChat(3d);
		error = retailTrade.checkCreate(BaseBO.INVALID_CASE_ID);
		Assert.assertEquals(error, String.format(RetailTrade.FIELD_ERROR_payment, RetailTrade.field.getFIELD_NAME_amountAlipay(), RetailTrade.field.getFIELD_NAME_amountAlipay()));
		// case3
		retailTrade.setPaymentType(7);
		retailTrade.setAmountCash(7d);
		retailTrade.setAmountAlipay(3d);
		retailTrade.setAmountWeChat(RetailTrade.Min_Amount);
		error = retailTrade.checkCreate(BaseBO.INVALID_CASE_ID);
		Assert.assertEquals(error, String.format(RetailTrade.FIELD_ERROR_payment, RetailTrade.field.getFIELD_NAME_amountWeChat(), RetailTrade.field.getFIELD_NAME_amountWeChat()));
		// case4
		retailTrade.setPaymentType(1);
		retailTrade.setAmount(RetailTrade.Min_Amount);
		retailTrade.setAmountCash(RetailTrade.Min_Amount);
		retailTrade.setAmountAlipay(RetailTrade.Min_Amount);
		retailTrade.setAmountWeChat(RetailTrade.Min_Amount);
		error = retailTrade.checkCreate(BaseBO.INVALID_CASE_ID);
		Assert.assertEquals(error, "");

		// 如果没有这种支付方式，那么这种支付金额要为0；
		retailTrade.setPaymentType(1);
		retailTrade.setAmount(1d);
		retailTrade.setAmountCash(0.9d);
		// case1
		retailTrade.setAmountAlipay(0.1d);
		error = retailTrade.checkCreate(BaseBO.INVALID_CASE_ID);
		Assert.assertEquals(error, String.format(RetailTrade.FIELD_ERROR_notPayment, RetailTrade.field.getFIELD_NAME_amountAlipay(), RetailTrade.field.getFIELD_NAME_amountAlipay()));
		retailTrade.setAmountCash(1d);
		retailTrade.setAmountAlipay(0d);
		error = retailTrade.checkCreate(BaseBO.INVALID_CASE_ID);
		Assert.assertEquals(error, "");
		// case2
		retailTrade.setAmountCash(0.9d);
		retailTrade.setAmountWeChat(0.1d);
		error = retailTrade.checkCreate(BaseBO.INVALID_CASE_ID);
		Assert.assertEquals(error, String.format(RetailTrade.FIELD_ERROR_notPayment, RetailTrade.field.getFIELD_NAME_amountWeChat(), RetailTrade.field.getFIELD_NAME_amountWeChat()));
		retailTrade.setAmountCash(1d);
		retailTrade.setAmountWeChat(0d);
		error = retailTrade.checkCreate(BaseBO.INVALID_CASE_ID);
		Assert.assertEquals(error, "");
		// case3
		retailTrade.setAmountCash(0.9d);
		retailTrade.setAmount1(0.1d);
		error = retailTrade.checkCreate(BaseBO.INVALID_CASE_ID);
		Assert.assertEquals(error, String.format(RetailTrade.FIELD_ERROR_notPayment, RetailTrade.field.getFIELD_NAME_amount1(), RetailTrade.field.getFIELD_NAME_amount1()));
		retailTrade.setAmountCash(1d);
		retailTrade.setAmount1(0d);
		error = retailTrade.checkCreate(BaseBO.INVALID_CASE_ID);
		Assert.assertEquals(error, "");
		// case4
		retailTrade.setAmountCash(0.9d);
		retailTrade.setAmount2(0.1d);
		error = retailTrade.checkCreate(BaseBO.INVALID_CASE_ID);
		Assert.assertEquals(error, String.format(RetailTrade.FIELD_ERROR_notPayment, RetailTrade.field.getFIELD_NAME_amount2(), RetailTrade.field.getFIELD_NAME_amount2()));
		retailTrade.setAmountCash(1d);
		retailTrade.setAmount2(0d);
		error = retailTrade.checkCreate(BaseBO.INVALID_CASE_ID);
		Assert.assertEquals(error, "");
		// case5
		retailTrade.setAmountCash(0.9d);
		retailTrade.setAmount3(0.1d);
		error = retailTrade.checkCreate(BaseBO.INVALID_CASE_ID);
		Assert.assertEquals(error, String.format(RetailTrade.FIELD_ERROR_notPayment, RetailTrade.field.getFIELD_NAME_amount3(), RetailTrade.field.getFIELD_NAME_amount3()));
		retailTrade.setAmountCash(1d);
		retailTrade.setAmount3(0d);
		error = retailTrade.checkCreate(BaseBO.INVALID_CASE_ID);
		Assert.assertEquals(error, "");
		// case6
		retailTrade.setAmountCash(0.9d);
		retailTrade.setAmount4(0.1d);
		error = retailTrade.checkCreate(BaseBO.INVALID_CASE_ID);
		Assert.assertEquals(error, String.format(RetailTrade.FIELD_ERROR_notPayment, RetailTrade.field.getFIELD_NAME_amount4(), RetailTrade.field.getFIELD_NAME_amount4()));
		retailTrade.setAmountCash(1d);
		retailTrade.setAmount4(0d);
		error = retailTrade.checkCreate(BaseBO.INVALID_CASE_ID);
		Assert.assertEquals(error, "");
		// case7
		retailTrade.setAmountCash(0.9d);
		retailTrade.setAmount5(0.1d);
		error = retailTrade.checkCreate(BaseBO.INVALID_CASE_ID);
		Assert.assertEquals(error, String.format(RetailTrade.FIELD_ERROR_notPayment, RetailTrade.field.getFIELD_NAME_amount5(), RetailTrade.field.getFIELD_NAME_amount5()));
		retailTrade.setAmountCash(1d);
		retailTrade.setAmount5(0d);
		error = retailTrade.checkCreate(BaseBO.INVALID_CASE_ID);
		Assert.assertEquals(error, "");
		// case8
		retailTrade.setPaymentType(2);
		retailTrade.setAmount(1d);
		retailTrade.setAmountCash(0.9d);
		retailTrade.setAmountAlipay(0.1d);
		error = retailTrade.checkCreate(BaseBO.INVALID_CASE_ID);
		Assert.assertEquals(error, String.format(RetailTrade.FIELD_ERROR_notPayment, RetailTrade.field.getFIELD_NAME_amountCash(), RetailTrade.field.getFIELD_NAME_amountCash()));
		retailTrade.setAmountCash(0d);
		retailTrade.setAmountAlipay(1d);
		error = retailTrade.checkCreate(BaseBO.INVALID_CASE_ID);
		Assert.assertEquals(error, "");

		// 零售单的状态测试
		retailTrade.setStatus(0);
		error = retailTrade.checkCreate(BaseBO.INVALID_CASE_ID);
		Assert.assertEquals(error, RetailTrade.FIELD_ERROR_status);
		//
		retailTrade.setStatus(3);
		error = retailTrade.checkCreate(BaseBO.INVALID_CASE_ID);
		Assert.assertEquals(error, RetailTrade.FIELD_ERROR_status);
		//
		retailTrade.setStatus(EnumStatusRetailTrade.ESRT_IncludeDeletedCommodity.getIndex());
		error = retailTrade.checkCreate(BaseBO.INVALID_CASE_ID);
		Assert.assertEquals(error, "");

		// consumerOpenID
		retailTrade.setConsumerOpenID("01234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567891");
		error = retailTrade.checkCreate(BaseBO.INVALID_CASE_ID);
		Assert.assertEquals(error, RetailTrade.FIELD_ERROR_consumerOpenID);
		//
		retailTrade.setConsumerOpenID(null);
		error = retailTrade.checkCreate(BaseBO.INVALID_CASE_ID);
		Assert.assertEquals(error, "");
		//
		retailTrade.setConsumerOpenID("asdf1231dsfa545455dfsa");
		error = retailTrade.checkCreate(BaseBO.INVALID_CASE_ID);
		Assert.assertEquals(error, "");
		// shopID
		retailTrade.setShopID(-1);
		error = retailTrade.checkCreate(BaseBO.INVALID_CASE_ID);
		Assert.assertEquals(error, RetailTrade.FIELD_ERROR_shopID);
		retailTrade.setShopID(0);
		error = retailTrade.checkCreate(BaseBO.INVALID_CASE_ID);
		Assert.assertEquals(error, RetailTrade.FIELD_ERROR_shopID);
		retailTrade.setShopID(2);
		error = retailTrade.checkCreate(BaseBO.INVALID_CASE_ID);
		Assert.assertEquals(error, "");
	}

	@Test
	public void testCheckRetrieve1() {
		Shared.printTestMethodStartInfo();

		RetailTrade retailTrade = new RetailTrade();
		String error = "";
		//
		retailTrade.setID(-99);
		error = retailTrade.checkRetrieve1(BaseBO.INVALID_CASE_ID);
		Assert.assertEquals(error, FieldFormat.FIELD_ERROR_ID);
		retailTrade.setID(0);
		error = retailTrade.checkRetrieve1(BaseBO.INVALID_CASE_ID);
		Assert.assertEquals(error, FieldFormat.FIELD_ERROR_ID);
		retailTrade.setID(1);
		//
		error = retailTrade.checkRetrieve1(BaseBO.INVALID_CASE_ID);
		Assert.assertEquals(error, "");
	}

	@Test
	public void testCheckRetrieveN() {
		Shared.printTestMethodStartInfo();

		RetailTrade retailTrade = new RetailTrade();
		String error = "";

		retailTrade.setVipID(-99);
		error = retailTrade.checkRetrieveN(BaseBO.INVALID_CASE_ID);
		Assert.assertEquals(error, RetailTrade.FIELD_ERROR_VipID);
		retailTrade.setVipID(0);
		//
		// App发出的请求
		// 空串
		retailTrade.setQueryKeyword("");
		error = retailTrade.checkRetrieveN(BaseBO.CASE_RetailTrade_RetrieveNFromApp);
		Assert.assertEquals(error, "");
		// null
		retailTrade.setQueryKeyword(null);
		error = retailTrade.checkRetrieveN(BaseBO.CASE_RetailTrade_RetrieveNFromApp);
		Assert.assertEquals(error, RetailTrade.FIELD_ERROR_sn_ForQuery);
		// 超过26位
		retailTrade.setQueryKeyword("1234567812345678123456781234567812345678");
		error = retailTrade.checkRetrieveN(BaseBO.CASE_RetailTrade_RetrieveNFromApp);
		Assert.assertEquals(error, RetailTrade.FIELD_ERROR_sn_ForQuery);
		// 有空格
		retailTrade.setQueryKeyword(" 12345678123456781 ");
		error = retailTrade.checkRetrieveN(BaseBO.CASE_RetailTrade_RetrieveNFromApp);
		Assert.assertEquals(error, RetailTrade.FIELD_ERROR_sn_ForQuery);
		// 包含LS的英文字符但不在开头
		retailTrade.setQueryKeyword("2019071LS00010000200");
		error = retailTrade.checkRetrieveN(BaseBO.CASE_RetailTrade_RetrieveNFromApp);
		Assert.assertEquals(error, RetailTrade.FIELD_ERROR_sn_ForQuery);
		// 长度小于10，结果不通过
		retailTrade.setQueryKeyword("201907");
		error = retailTrade.checkRetrieveN(BaseBO.CASE_RetailTrade_RetrieveNFromApp);
		Assert.assertEquals(error, RetailTrade.FIELD_ERROR_sn_ForQuery);
		// 10-26位且不以ls开头
		retailTrade.setQueryKeyword("20190713000010000200");
		error = retailTrade.checkRetrieveN(BaseBO.CASE_RetailTrade_RetrieveNFromApp);
		Assert.assertEquals(error, "");
		// 10-26位且以ls开头
		retailTrade.setQueryKeyword("ls201907130000100002");
		error = retailTrade.checkRetrieveN(BaseBO.CASE_RetailTrade_RetrieveNFromApp);
		Assert.assertEquals(error, "");
		// 包含不是LS的英文字符，结果不通过
		retailTrade.setQueryKeyword("LS20190713000abc000200003");
		error = retailTrade.checkRetrieveN(BaseBO.CASE_RetailTrade_RetrieveNFromApp);
		Assert.assertEquals(error, RetailTrade.FIELD_ERROR_sn_ForQuery);
		// 中文
		retailTrade.setQueryKeyword("LS20190713000烤面筋000200003");
		error = retailTrade.checkRetrieveN(BaseBO.CASE_RetailTrade_RetrieveNFromApp);
		Assert.assertEquals(error, RetailTrade.FIELD_ERROR_sn_ForQuery);
		// 特殊字符
		retailTrade.setQueryKeyword("！@#￥（&（%");
		error = retailTrade.checkRetrieveN(BaseBO.CASE_RetailTrade_RetrieveNFromApp);
		Assert.assertEquals(error, RetailTrade.FIELD_ERROR_sn_ForQuery);
		// 26位且以LS开头,不以_1结尾
		retailTrade.setQueryKeyword("LS201907130000100002_2");
		error = retailTrade.checkRetrieveN(BaseBO.CASE_RetailTrade_RetrieveNFromApp);
		Assert.assertEquals(error, RetailTrade.FIELD_ERROR_sn_ForQuery);
		// 26位且以LS开头,以_1结尾
		retailTrade.setQueryKeyword("LS201907130000100002_1");
		error = retailTrade.checkRetrieveN(BaseBO.CASE_RetailTrade_RetrieveNFromApp);
		Assert.assertEquals(error, "");
		// web端请求
		// 设置queryKeyword的长度超过32，结果不通过。
		retailTrade.setQueryKeyword("123456781234567812345678123454564564567812345678");
		error = retailTrade.checkRetrieveN(BaseBO.CASE_RetailTrade_RetrieveNBySNFromWeb);
		Assert.assertEquals(error, RetailTrade.FIELD_ERROR_queryKeyword);
		// 设置queryKeyword是0-32位，且首尾有空格，结果不通过
		retailTrade.setQueryKeyword(" 12345678123456781 ");
		error = retailTrade.checkRetrieveN(BaseBO.CASE_RetailTrade_RetrieveNBySNFromWeb);
		Assert.assertEquals(error, RetailTrade.FIELD_ERROR_queryKeyword);
		// 设置queryKeyword为null，结果不通过
		retailTrade.setQueryKeyword(null);
		error = retailTrade.checkRetrieveN(BaseBO.CASE_RetailTrade_RetrieveNBySNFromWeb);
		Assert.assertEquals(error, RetailTrade.FIELD_ERROR_queryKeyword);
		// 设置queryKeyword是0-32位，且包含特殊字符，结果不通过
		retailTrade.setQueryKeyword("！@#￥（&（%");
		error = retailTrade.checkRetrieveN(BaseBO.CASE_RetailTrade_RetrieveNBySNFromWeb);
		Assert.assertEquals(error, RetailTrade.FIELD_ERROR_queryKeyword);
		// 设置queryKeyword为空串，结果通过
		retailTrade.setQueryKeyword("");
		error = retailTrade.checkRetrieveN(BaseBO.CASE_RetailTrade_RetrieveNBySNFromWeb);
		Assert.assertEquals(error, "");
		// 设置queryKeyword是0-32位，且包含中文，结果通过。
		retailTrade.setQueryKeyword("烤面筋");
		error = retailTrade.checkRetrieveN(BaseBO.CASE_RetailTrade_RetrieveNBySNFromWeb);
		Assert.assertEquals(error, "");
		// 设置queryKeyword是0-32位，且包含英文字符，结果通过。
		retailTrade.setQueryKeyword("helloisme");
		error = retailTrade.checkRetrieveN(BaseBO.CASE_RetailTrade_RetrieveNBySNFromWeb);
		Assert.assertEquals(error, "");
		//
		retailTrade.setLocalSN(-99);
		error = retailTrade.checkRetrieveN(BaseBO.INVALID_CASE_ID);
		Assert.assertEquals(error, RetailTrade.FIELD_ERROR_LOCALSN);
		retailTrade.setLocalSN(0);
		//
		retailTrade.setPos_ID(-99);
		error = retailTrade.checkRetrieveN(BaseBO.INVALID_CASE_ID);
		Assert.assertEquals(error, RetailTrade.FIELD_ERROR_POSID);
		retailTrade.setPos_ID(0);
		//
		retailTrade.setPaymentType(-99);
		error = retailTrade.checkRetrieveN(BaseBO.INVALID_CASE_ID);
		Assert.assertEquals(error, RetailTrade.FIELD_ERROR_paymentType);
		retailTrade.setPaymentType(0);
		//
		retailTrade.setStaffID(-99);
		error = retailTrade.checkRetrieveN(BaseBO.INVALID_CASE_ID);
		Assert.assertEquals(error, RetailTrade.FIELD_ERROR_StaffID);
		retailTrade.setStaffID(0);
		//
		error = retailTrade.checkRetrieveN(BaseBO.INVALID_CASE_ID);
		Assert.assertEquals(error, "");
	}

	@Test
	public void testCheckUpdate() {
		Shared.printTestMethodStartInfo();

		RetailTrade retailTrade = new RetailTrade();
		String error = "";
		error = retailTrade.checkUpdate(BaseBO.INVALID_CASE_ID);
		Assert.assertEquals(error, "");
	}

	@Test
	public void testCheckDelete() {
		Shared.printTestMethodStartInfo();

		RetailTrade retailTrade = new RetailTrade();
		String error = "";
		error = retailTrade.checkDelete(BaseBO.INVALID_CASE_ID);
		Assert.assertEquals(error, "");
	}

	@Test
	public void testCompareTo1() throws CloneNotSupportedException, InterruptedException {
		testCompareTo_Case1();
	}

	@Test
	public void testCompareTo2() throws CloneNotSupportedException, InterruptedException {
		testCompareTo_Case2();
	}

	@Test
	public void testCompareTo3() throws CloneNotSupportedException, InterruptedException {
		testCompareTo_Case3();
	}

	@Test
	public void testCompareTo4() throws CloneNotSupportedException, InterruptedException {
		testCompareTo_Case4();
	}

	@Test
	public void testCompareTo5() throws CloneNotSupportedException, InterruptedException {
		testCompareTo_Case5();
	}

	@Test
	public void testCompareTo6() throws CloneNotSupportedException, InterruptedException {
		testCompareTo_Case6();
	}

	@Test
	public void testCompareTo7() throws CloneNotSupportedException, InterruptedException {
		testCompareTo_Case7();
	}

	@Test
	public void testCompareTo8() throws CloneNotSupportedException, InterruptedException {
		testCompareTo_Case8();
	}

	@Test
	public void testCompareTo9() throws CloneNotSupportedException, InterruptedException {
		testCompareTo_Case9();
	}

	@Test
	public void testCompareTo10() throws CloneNotSupportedException, InterruptedException {
		testCompareTo_Case10();
	}

	@Test
	public void testCompareTo11() throws CloneNotSupportedException, InterruptedException {
		Shared.caseLog("case11: 俩对象数据一样，一个状态为2");
		RetailTrade master = (RetailTrade) getMasterTableObject();
		List<BaseModel> slave = getSlaveTableObject();
		master.setListSlave1(slave);
		//
		RetailTrade master1 = (RetailTrade) master.clone();
		master1.setStatus(EnumStatusRetailTrade.ESRT_IncludeDeletedCommodity.getIndex());
		RetailTrade master2 = (RetailTrade) master.clone();
		Assert.assertTrue(master1.compareTo(master2) == -1, "比较失败");
	}

	@Test
	public void testCompareTo12() throws CloneNotSupportedException, InterruptedException {
		Shared.caseLog("case12: 俩对象数据一样，状态都为2");
		RetailTrade master = (RetailTrade) getMasterTableObject();
		master.setStatus(EnumStatusRetailTrade.ESRT_IncludeDeletedCommodity.getIndex());
		List<BaseModel> slave = getSlaveTableObject();
		master.setListSlave1(slave);
		//
		RetailTrade master1 = (RetailTrade) master.clone();
		RetailTrade master2 = (RetailTrade) master.clone();
		Assert.assertTrue(master1.compareTo(master2) == 0, "比较失败");
	}

	@Test
	public void testCompareTo13() throws CloneNotSupportedException, InterruptedException {
		Shared.caseLog("case13: 俩对象数据一样，状态都不为2");
		RetailTrade master = (RetailTrade) getMasterTableObject();
		List<BaseModel> slave = getSlaveTableObject();
		master.setListSlave1(slave);
		//
		master.setStatus(EnumStatusRetailTrade.ESRT_Normal.getIndex());
		RetailTrade master1 = (RetailTrade) master.clone();
		master.setStatus(EnumStatusRetailTrade.ESRT_Hold.getIndex());
		RetailTrade master2 = (RetailTrade) master.clone();
		Assert.assertTrue(master1.compareTo(master2) == -1, "比较失败");
	}

	@Test
	public void testCalculateReturnPrice() throws Exception {
		Shared.caseLog("未参与任何的优惠。计算退货价");

		RetailTrade retailTrade = BaseRetailTradeTest.DataInput.getRetailTrade();
		// 设置从表
		List<RetailTradeCommodity> retailTradeCommodities = new ArrayList<RetailTradeCommodity>();
		RetailTradeCommodity retailTradeCommodity = BaseRetailTradeTest.DataInput.getRetailTradeCommodity();
		retailTradeCommodity.setCommodityID(1); // 普通商品
		retailTradeCommodity.setPriceOriginal(10d);
		retailTradeCommodity.setPriceReturn(retailTradeCommodity.getPriceOriginal());
		retailTradeCommodities.add(retailTradeCommodity);
		//
		RetailTradeCommodity retailTradeCommodity2 = BaseRetailTradeTest.DataInput.getRetailTradeCommodity();
		retailTradeCommodity2.setCommodityID(59); // 多包装商品
		retailTradeCommodity2.setPriceOriginal(100d);
		retailTradeCommodity2.setPriceReturn(retailTradeCommodity2.getPriceOriginal());
		retailTradeCommodities.add(retailTradeCommodity2);
		//
		RetailTradeCommodity retailTradeCommodity3 = BaseRetailTradeTest.DataInput.getRetailTradeCommodity();
		retailTradeCommodity3.setCommodityID(111); // 组合商品商品
		retailTradeCommodity3.setPriceOriginal(20d);
		retailTradeCommodity3.setPriceReturn(retailTradeCommodity3.getPriceOriginal());
		retailTradeCommodities.add(retailTradeCommodity3);
		retailTrade.setListSlave1(retailTradeCommodities);
		//
		double originalTotalAmount = 0.000000d;
		for (RetailTradeCommodity rtc : retailTradeCommodities) {
			originalTotalAmount = GeneralUtil.sum(originalTotalAmount, GeneralUtil.mul(rtc.getNO(), rtc.getPriceOriginal()));
		}
		retailTrade.setAmount(originalTotalAmount);

		RetailTrade cloneRetailTrade = (RetailTrade) retailTrade.clone();
		cloneRetailTrade.calculateReturnPrice(cloneRetailTrade);
		// 比较退货价
		for (int i = 0; i < cloneRetailTrade.getListSlave1().size(); i++) {
			RetailTradeCommodity rtc = (RetailTradeCommodity) cloneRetailTrade.getListSlave1().get(i);
			Assert.assertTrue(Math.abs(GeneralUtil.sub(rtc.getPriceReturn(), ((RetailTradeCommodity) retailTrade.getListSlave1().get(i)).getPriceReturn())) < BaseModel.TOLERANCE, "未参与任何优惠，更改了退货价");
		}
	}

	@Test
	public void testCalculateReturnPrice2() throws Exception {
		Shared.caseLog("参与促销或优惠券，使金额减少，计算退货价");

		RetailTrade retailTrade = BaseRetailTradeTest.DataInput.getRetailTrade();
		// 设置从表
		List<RetailTradeCommodity> retailTradeCommodities = new ArrayList<RetailTradeCommodity>();
		RetailTradeCommodity retailTradeCommodity = BaseRetailTradeTest.DataInput.getRetailTradeCommodity();
		retailTradeCommodity.setCommodityID(1); // 普通商品
		retailTradeCommodity.setPriceOriginal(10d);
		retailTradeCommodity.setPriceReturn(retailTradeCommodity.getPriceOriginal());
		retailTradeCommodities.add(retailTradeCommodity);
		//
		RetailTradeCommodity retailTradeCommodity2 = BaseRetailTradeTest.DataInput.getRetailTradeCommodity();
		retailTradeCommodity2.setCommodityID(59); // 多包装商品
		retailTradeCommodity2.setPriceOriginal(100d);
		retailTradeCommodity2.setPriceReturn(retailTradeCommodity2.getPriceOriginal());
		retailTradeCommodities.add(retailTradeCommodity2);
		//
		RetailTradeCommodity retailTradeCommodity3 = BaseRetailTradeTest.DataInput.getRetailTradeCommodity();
		retailTradeCommodity3.setCommodityID(111); // 组合商品商品
		retailTradeCommodity3.setPriceOriginal(20d);
		retailTradeCommodity3.setPriceReturn(retailTradeCommodity3.getPriceOriginal());
		retailTradeCommodities.add(retailTradeCommodity3);
		retailTrade.setListSlave1(retailTradeCommodities);
		//
		List<RetailTradePromoting> retailTradePromotingList = new ArrayList<RetailTradePromoting>();
		RetailTradePromoting retailTradePromoting = new RetailTradePromoting();
		List<RetailTradePromotingFlow> retailTradePromotingFlowList = new ArrayList<RetailTradePromotingFlow>();
		RetailTradePromotingFlow retailTradePromotingFlow = new RetailTradePromotingFlow();
		retailTradePromotingFlow.setPromotionID(1);
		retailTradePromotingFlow.setProcessFlow("测试数据aaa");
		retailTradePromotingFlowList.add(retailTradePromotingFlow);
		retailTradePromoting.setListSlave1(retailTradePromotingFlowList);
		retailTradePromotingList.add(retailTradePromoting);
		retailTrade.setListSlave2(retailTradePromotingList);
		//
		double originalTotalAmount = 0.000000d;
		for (RetailTradeCommodity rtc : retailTradeCommodities) {
			originalTotalAmount = GeneralUtil.sum(originalTotalAmount, GeneralUtil.mul(rtc.getNO(), rtc.getPriceOriginal()));
		}
		retailTrade.setAmount(GeneralUtil.sub(originalTotalAmount, 30)); // 节省了30元
		retailTrade.calculateReturnPrice(retailTrade);
		// 比较退货价
		for (Object object : retailTrade.getListSlave1()) {
			RetailTradeCommodity rtc = (RetailTradeCommodity) object;
			if (rtc.getCommodityID() == 1) {
				Assert.assertTrue(Math.abs(GeneralUtil.sub(rtc.getPriceReturn(), 9.769231d)) < BaseModel.TOLERANCE, "计算有误");
				break;
			}
			if (rtc.getCommodityID() == 59) {
				Assert.assertTrue(Math.abs(GeneralUtil.sub(rtc.getPriceReturn(), 97.692308d)) < BaseModel.TOLERANCE, "计算有误");
				break;
			}
			if (rtc.getCommodityID() == 111) {
				Assert.assertTrue(Math.abs(GeneralUtil.sub(rtc.getPriceReturn(), 19.538462)) < BaseModel.TOLERANCE, "计算有误");
				break;
			}
		}
	}
}
