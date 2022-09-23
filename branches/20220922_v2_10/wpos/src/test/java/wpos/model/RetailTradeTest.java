package wpos.model;

import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import wpos.BaseModelTest;
import wpos.bo.BaseHttpBO;
import wpos.bo.BaseSQLiteBO;
import wpos.model.BaseModel;
import wpos.model.RetailTrade;
import wpos.model.RetailTradeCommodity;
import wpos.utils.FieldFormat;
import wpos.utils.GeneralUtil;
import wpos.utils.Shared;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;

public class RetailTradeTest extends BaseModelTest {
    @BeforeClass
    public void setup() {
        Shared.printTestClassStartInfo();
    }

    @AfterClass
    public void tearDown() {
        Shared.printTestClassEndInfo();
    }

    public static class DataInput {
        private static RetailTrade retailTradeInput = null;
        private static RetailTradeCommodity retailTradeCommodityInput = null;

        protected static final RetailTrade getRetailTrade() {
            Random ran = new Random();
            int pos_id = ran.nextInt(5) + 1;
            retailTradeInput = new RetailTrade();
            retailTradeInput.setVipID(1);
            retailTradeInput.setSn(retailTradeInput.generateRetailTradeSN(pos_id));
            retailTradeInput.setLocalSN(ran.nextInt());
            retailTradeInput.setPos_ID(pos_id);
            retailTradeInput.setLogo("url:" + String.valueOf(System.currentTimeMillis()).substring(6));
            retailTradeInput.setSaleDatetime(new Date());
            retailTradeInput.setStatus(0); //...
            retailTradeInput.setStaffID(ran.nextInt(5) + 1);
            retailTradeInput.setPaymentType(ran.nextInt(5) + 1);
            retailTradeInput.setPaymentAccount("A" + String.valueOf(System.currentTimeMillis()).substring(6));
            retailTradeInput.setRemark("remark:" + String.valueOf(System.currentTimeMillis()).substring(6));
            retailTradeInput.setSourceID(BaseSQLiteBO.INVALID_INT_ID);
            retailTradeInput.setSmallSheetID(ran.nextInt(7) + 1);
            retailTradeInput.setAmount(50);
            retailTradeInput.setAliPayOrderSN("zfb" + String.valueOf(System.currentTimeMillis()).substring(6));
            retailTradeInput.setWxOrderSN("wx" + String.valueOf(System.currentTimeMillis()).substring(6));
            retailTradeInput.setWxRefundDesc("xx" + String.valueOf(System.currentTimeMillis()).substring(6));
            retailTradeInput.setWxTradeNO("no" + String.valueOf(System.currentTimeMillis()).substring(6));
            retailTradeInput.setSyncDatetime(new Date());
            retailTradeInput.setID(1);

            RetailTradeCommodity retailTradeCommodity = new RetailTradeCommodity();
            retailTradeCommodity.setCommodityID(1);
            retailTradeCommodity.setNO(ran.nextInt());
            retailTradeCommodity.setPriceOriginal(ran.nextDouble());
            retailTradeCommodity.setDiscount(0.9);
            retailTradeCommodity.setBarcodeID(1);
            retailTradeCommodity.setID(1);
            retailTradeCommodity.setPriceVIPOriginal(0.0);
            //
            RetailTradeCommodity retailTradeCommodity2 = new RetailTradeCommodity();
            retailTradeCommodity2.setCommodityID(2);
            retailTradeCommodity2.setNO(ran.nextInt());
            retailTradeCommodity2.setPriceOriginal(ran.nextDouble());
            retailTradeCommodity2.setDiscount(0.9);
            retailTradeCommodity2.setBarcodeID(1);
            retailTradeCommodity2.setID(1);
            retailTradeCommodity2.setPriceVIPOriginal(0.0);
            //
            List<RetailTradeCommodity> listRetailTradeCommodity = new ArrayList<RetailTradeCommodity>();
            listRetailTradeCommodity.add(retailTradeCommodity);
            listRetailTradeCommodity.add(retailTradeCommodity2);
            //
            retailTradeInput.setListSlave1(listRetailTradeCommodity);

            return (RetailTrade) retailTradeInput.clone();
        }

        protected static final RetailTrade getRetailTrade2() {
            Random ran = new Random();
            int pos_id = ran.nextInt(5) + 1;
            retailTradeInput = new RetailTrade();
            retailTradeInput.setVipID(1);
            retailTradeInput.setSn(retailTradeInput.generateRetailTradeSN(pos_id));
            retailTradeInput.setLocalSN(ran.nextInt());
            retailTradeInput.setPos_ID(pos_id);
            retailTradeInput.setLogo("url:" + String.valueOf(System.currentTimeMillis()).substring(6));
            retailTradeInput.setSaleDatetime(new Date());
            retailTradeInput.setStatus(0); //...
            retailTradeInput.setStaffID(ran.nextInt(5) + 1);
            retailTradeInput.setPaymentType(ran.nextInt(5) + 1);
            retailTradeInput.setPaymentAccount("A" + String.valueOf(System.currentTimeMillis()).substring(6));
            retailTradeInput.setRemark("remark:" + String.valueOf(System.currentTimeMillis()).substring(6));
            retailTradeInput.setSourceID(BaseSQLiteBO.INVALID_INT_ID);
            retailTradeInput.setSmallSheetID(ran.nextInt(7) + 1);
            retailTradeInput.setAmount(50);
            retailTradeInput.setAliPayOrderSN("zfb" + String.valueOf(System.currentTimeMillis()).substring(6));
            retailTradeInput.setWxOrderSN("wx" + String.valueOf(System.currentTimeMillis()).substring(6));
            retailTradeInput.setWxRefundDesc("xx" + String.valueOf(System.currentTimeMillis()).substring(6));
            retailTradeInput.setWxTradeNO("no" + String.valueOf(System.currentTimeMillis()).substring(6));
            retailTradeInput.setSyncDatetime(new Date());
            retailTradeInput.setID(1);

            return (RetailTrade) retailTradeInput.clone();
        }

        protected static final RetailTradeCommodity getRetailTradeCommodity() {
            Random ran = new Random();
            retailTradeCommodityInput = new RetailTradeCommodity();
            retailTradeCommodityInput.setCommodityID(1);
            retailTradeCommodityInput.setNO(ran.nextInt());
            retailTradeCommodityInput.setPriceOriginal(ran.nextDouble());
            retailTradeCommodityInput.setDiscount(0.9);
            retailTradeCommodityInput.setBarcodeID(1);
            retailTradeCommodityInput.setPriceVIPOriginal(0.0);

            return (RetailTradeCommodity) retailTradeCommodityInput.clone();
        }
    }

    @Test
    public void testCheckCreate() throws Exception {
        Shared.printTestMethodStartInfo();

        RetailTrade retailTrade = new RetailTrade();
        retailTrade.setVipID(1);
        retailTrade.setSn(retailTrade.generateRetailTradeSN(1));
        retailTrade.setPos_ID(1);
        retailTrade.setLocalSN(1);
        retailTrade.setLogo("123463");
        retailTrade.setStaffID(1);
        retailTrade.setPaymentType(1);
        retailTrade.setPaymentAccount("123456");
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
        retailTrade.setDatetimeStart(new Date());
        retailTrade.setDatetimeEnd(new Date());
        retailTrade.setSyncDatetime(new Date());
        retailTrade.setSaleDatetime(new Date());

        String error = "";
        // 设置F_SN为null，结果不通过。
        retailTrade.setSn(null);
        error = retailTrade.checkCreate(BaseSQLiteBO.INVALID_CASE_ID);
        Assert.assertEquals(error, RetailTrade.FIELD_ERROR_sn);
        //
        // 设置F_SN空串，结果不通过。
        retailTrade.setSn("");
        error = retailTrade.checkCreate(BaseSQLiteBO.INVALID_CASE_ID);
        Assert.assertEquals(error, RetailTrade.FIELD_ERROR_sn);
        //
        // 设置F_SN的长度超过24，结果不通过。
        retailTrade.setSn("LS201905291501080888880000001");
        error = retailTrade.checkCreate(BaseSQLiteBO.INVALID_CASE_ID);
        Assert.assertEquals(error, RetailTrade.FIELD_ERROR_sn);
        //
        // 设置F_SN不是LS开头，结果不通过。
        retailTrade.setSn("HH2019052915010800000001");
        error = retailTrade.checkCreate(BaseSQLiteBO.INVALID_CASE_ID);
        Assert.assertEquals(error, RetailTrade.FIELD_ERROR_sn);
        //
        // 设置F_SN包含中文，结果不通过。
        retailTrade.setSn("LS2019052915烤鸡翅800000001");
        error = retailTrade.checkCreate(BaseSQLiteBO.INVALID_CASE_ID);
        Assert.assertEquals(error, RetailTrade.FIELD_ERROR_sn);
        //
        // 设置F_SN包含其他英文字符，结果不通过。
        retailTrade.setSn("LS2019052915sss800000001");
        error = retailTrade.checkCreate(BaseSQLiteBO.INVALID_CASE_ID);
        Assert.assertEquals(error, RetailTrade.FIELD_ERROR_sn);
        //
        // 设置F_SN的长度小于10，结果不通过。
        retailTrade.setSn("LS201905");
        error = retailTrade.checkCreate(BaseSQLiteBO.INVALID_CASE_ID);
        Assert.assertEquals(error, RetailTrade.FIELD_ERROR_sn);
        //
        // 设置F_SN的长度等于24且格式正确，结果通过。
        retailTrade.setSn("LS2019052915010800010001");
        error = retailTrade.checkCreate(BaseSQLiteBO.INVALID_CASE_ID);
        Assert.assertEquals(error, "");
        retailTrade.setSn("LS2019052915010800010001");
        //
        retailTrade.setLocalSN(-99);
        error = retailTrade.checkCreate(BaseSQLiteBO.INVALID_CASE_ID);
        Assert.assertEquals(error, RetailTrade.FIELD_ERROR_LOCALSN);
        retailTrade.setLocalSN(0);
        error = retailTrade.checkCreate(BaseSQLiteBO.INVALID_CASE_ID);
        Assert.assertEquals(error, RetailTrade.FIELD_ERROR_LOCALSN);
        retailTrade.setLocalSN(1);
        //
        retailTrade.setPos_ID(-99);
        error = retailTrade.checkCreate(BaseSQLiteBO.INVALID_CASE_ID);
        Assert.assertEquals(error, RetailTrade.FIELD_ERROR_POSID);
        retailTrade.setPos_ID(0);
        error = retailTrade.checkCreate(BaseSQLiteBO.INVALID_CASE_ID);
        Assert.assertEquals(error, RetailTrade.FIELD_ERROR_POSID);
        retailTrade.setPos_ID(1);
        //
        retailTrade.setStaffID(-99);
        error = retailTrade.checkCreate(BaseSQLiteBO.INVALID_CASE_ID);
        Assert.assertEquals(error, RetailTrade.FIELD_ERROR_StaffID);
        retailTrade.setStaffID(0);
        error = retailTrade.checkCreate(BaseSQLiteBO.INVALID_CASE_ID);
        Assert.assertEquals(error, RetailTrade.FIELD_ERROR_StaffID);
        retailTrade.setStaffID(1);
        //
        retailTrade.setPaymentType(-99);
        error = retailTrade.checkCreate(BaseSQLiteBO.INVALID_CASE_ID);
        Assert.assertEquals(error, RetailTrade.FIELD_ERROR_paymentType);
        retailTrade.setPaymentType(0);
        error = retailTrade.checkCreate(BaseSQLiteBO.INVALID_CASE_ID);
        Assert.assertEquals(error, RetailTrade.FIELD_ERROR_paymentType);
        retailTrade.setPaymentType(8);
        error = retailTrade.checkCreate(BaseSQLiteBO.INVALID_CASE_ID);
        Assert.assertEquals(error, RetailTrade.FIELD_ERROR_paymentType);
        retailTrade.setPaymentType(1);
        //
        retailTrade.setRemark("12345678901234567890123");
        error = retailTrade.checkCreate(BaseSQLiteBO.INVALID_CASE_ID);
        Assert.assertEquals(error, RetailTrade.FIELD_ERROR_remark);
        retailTrade.setRemark("");
        //
        retailTrade.setSourceID(-99);
        error = retailTrade.checkCreate(BaseSQLiteBO.INVALID_CASE_ID);
        Assert.assertEquals(error, RetailTrade.FIELD_ERROR_sourceID);
        retailTrade.setSourceID(-1);
        //
        retailTrade.setAmount(-0.01d);
        error = retailTrade.checkCreate(BaseSQLiteBO.INVALID_CASE_ID);
        Assert.assertEquals(error, String.format(RetailTrade.FIELD_ERROR_Amount, retailTrade.field.getFIELD_NAME_amount()));
        retailTrade.setAmount(0.000000d);
        //
        retailTrade.setAmountCash(-0.01d);
        error = retailTrade.checkCreate(BaseSQLiteBO.INVALID_CASE_ID);
        Assert.assertEquals(error, String.format(RetailTrade.FIELD_ERROR_Amount, retailTrade.field.getFIELD_NAME_amountCash()));
        retailTrade.setAmountCash(0.000000d);
        //
        retailTrade.setAmountAlipay(-0.01d);
        error = retailTrade.checkCreate(BaseSQLiteBO.INVALID_CASE_ID);
        Assert.assertEquals(error, String.format(RetailTrade.FIELD_ERROR_Amount, retailTrade.field.getFIELD_NAME_amountAlipay()));
        retailTrade.setAmountAlipay(0.000000d);
        //
        retailTrade.setAmountWeChat(-0.01d);
        error = retailTrade.checkCreate(BaseSQLiteBO.INVALID_CASE_ID);
        Assert.assertEquals(error, String.format(RetailTrade.FIELD_ERROR_Amount, retailTrade.field.getFIELD_NAME_amountWeChat()));
        retailTrade.setAmountWeChat(0.000000d);
        //
        retailTrade.setAmount1(-0.01d);
        error = retailTrade.checkCreate(BaseSQLiteBO.INVALID_CASE_ID);
        Assert.assertEquals(error, String.format(RetailTrade.FIELD_ERROR_Amount, retailTrade.field.getFIELD_NAME_amount1()));
        retailTrade.setAmount1(0.000000d);
        //
        retailTrade.setAmount2(-0.01d);
        error = retailTrade.checkCreate(BaseSQLiteBO.INVALID_CASE_ID);
        Assert.assertEquals(error, String.format(RetailTrade.FIELD_ERROR_Amount, retailTrade.field.getFIELD_NAME_amount2()));
        retailTrade.setAmount2(0.000000d);
        //
        retailTrade.setAmount3(-0.01d);
        error = retailTrade.checkCreate(BaseSQLiteBO.INVALID_CASE_ID);
        Assert.assertEquals(error, String.format(RetailTrade.FIELD_ERROR_Amount, retailTrade.field.getFIELD_NAME_amount3()));
        retailTrade.setAmount3(0.000000d);
        //
        retailTrade.setAmount4(-0.01d);
        error = retailTrade.checkCreate(BaseSQLiteBO.INVALID_CASE_ID);
        Assert.assertEquals(error, String.format(RetailTrade.FIELD_ERROR_Amount, retailTrade.field.getFIELD_NAME_amount4()));
        retailTrade.setAmount4(0.000000d);
        //
        retailTrade.setAmount5(-0.01d);
        error = retailTrade.checkCreate(BaseSQLiteBO.INVALID_CASE_ID);
        Assert.assertEquals(error, String.format(RetailTrade.FIELD_ERROR_Amount, retailTrade.field.getFIELD_NAME_amount5()));
        retailTrade.setAmount5(0.000000d);
        //
        retailTrade.setSmallSheetID(-1);
        error = retailTrade.checkCreate(BaseSQLiteBO.INVALID_CASE_ID);
        Assert.assertEquals(error, RetailTrade.FIELD_ERROR_smallSheetID);
        retailTrade.setSmallSheetID(0);
        error = retailTrade.checkCreate(BaseSQLiteBO.INVALID_CASE_ID);
        Assert.assertEquals(error, RetailTrade.FIELD_ERROR_smallSheetID);
        retailTrade.setSmallSheetID(1);

        // 检查：每种支付方式对应的钱加起来=应收款
        retailTrade.setAmount(10d);
        // case1
        retailTrade.setPaymentType(1);
        retailTrade.setAmountCash(9d);
        error = retailTrade.checkCreate(BaseSQLiteBO.INVALID_CASE_ID);
        Assert.assertEquals(error, RetailTrade.FIELD_ERROR_amountTotal);
        retailTrade.setAmountCash(10d);
        error = retailTrade.checkCreate(BaseSQLiteBO.INVALID_CASE_ID);
        Assert.assertEquals(error, "");
        // case2
        retailTrade.setPaymentType(3);
        retailTrade.setAmountCash(9d);
        retailTrade.setAmountAlipay(6d);
        error = retailTrade.checkCreate(BaseSQLiteBO.INVALID_CASE_ID);
        Assert.assertEquals(error, RetailTrade.FIELD_ERROR_amountTotal);
        retailTrade.setAmountCash(4d);
        retailTrade.setAmountAlipay(6d);
        error = retailTrade.checkCreate(BaseSQLiteBO.INVALID_CASE_ID);
        Assert.assertEquals(error, "");
        // case3
        retailTrade.setPaymentType(7);
        retailTrade.setAmountCash(1d);
        retailTrade.setAmountAlipay(6d);
        retailTrade.setAmountWeChat(2d);
        error = retailTrade.checkCreate(BaseSQLiteBO.INVALID_CASE_ID);
        Assert.assertEquals(error, RetailTrade.FIELD_ERROR_amountTotal);
        retailTrade.setAmountCash(1d);
        retailTrade.setAmountAlipay(6d);
        retailTrade.setAmountWeChat(3d);
        error = retailTrade.checkCreate(BaseSQLiteBO.INVALID_CASE_ID);
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
        error = retailTrade.checkCreate(BaseSQLiteBO.INVALID_CASE_ID);
        Assert.assertEquals(error, String.format(RetailTrade.FIELD_ERROR_payment, retailTrade.field.getFIELD_NAME_amountCash(), retailTrade.field.getFIELD_NAME_amountCash()));
        // case2
        retailTrade.setPaymentType(7);
        retailTrade.setAmountCash(7d);
        retailTrade.setAmountAlipay(RetailTrade.Min_Amount);
        retailTrade.setAmountWeChat(3d);
        error = retailTrade.checkCreate(BaseSQLiteBO.INVALID_CASE_ID);
        Assert.assertEquals(error, String.format(RetailTrade.FIELD_ERROR_payment, retailTrade.field.getFIELD_NAME_amountAlipay(), retailTrade.field.getFIELD_NAME_amountAlipay()));
        // case3
        retailTrade.setPaymentType(7);
        retailTrade.setAmountCash(7d);
        retailTrade.setAmountAlipay(3d);
        retailTrade.setAmountWeChat(RetailTrade.Min_Amount);
        error = retailTrade.checkCreate(BaseSQLiteBO.INVALID_CASE_ID);
        Assert.assertEquals(error, String.format(RetailTrade.FIELD_ERROR_payment, retailTrade.field.getFIELD_NAME_amountWeChat(), retailTrade.field.getFIELD_NAME_amountWeChat()));
        // case4
        retailTrade.setPaymentType(1);
        retailTrade.setAmount(RetailTrade.Min_Amount);
        retailTrade.setAmountCash(RetailTrade.Min_Amount);
        retailTrade.setAmountAlipay(RetailTrade.Min_Amount);
        retailTrade.setAmountWeChat(RetailTrade.Min_Amount);
        error = retailTrade.checkCreate(BaseSQLiteBO.INVALID_CASE_ID);
        Assert.assertEquals(error, "");

        // 如果没有这种支付方式，那么这种支付金额要为0；
        retailTrade.setPaymentType(1);
        retailTrade.setAmount(1d);
        retailTrade.setAmountCash(0.9d);
        // case1
        retailTrade.setAmountAlipay(0.1d);
        error = retailTrade.checkCreate(BaseSQLiteBO.INVALID_CASE_ID);
        Assert.assertEquals(error, String.format(RetailTrade.FIELD_ERROR_notPayment, retailTrade.field.getFIELD_NAME_amountAlipay(), retailTrade.field.getFIELD_NAME_amountAlipay()));
        retailTrade.setAmountCash(1d);
        retailTrade.setAmountAlipay(0d);
        error = retailTrade.checkCreate(BaseSQLiteBO.INVALID_CASE_ID);
        Assert.assertEquals(error, "");
        // case2
        retailTrade.setAmountCash(0.9d);
        retailTrade.setAmountWeChat(0.1d);
        error = retailTrade.checkCreate(BaseSQLiteBO.INVALID_CASE_ID);
        Assert.assertEquals(error, String.format(RetailTrade.FIELD_ERROR_notPayment, retailTrade.field.getFIELD_NAME_amountWeChat(), retailTrade.field.getFIELD_NAME_amountWeChat()));
        retailTrade.setAmountCash(1d);
        retailTrade.setAmountWeChat(0d);
        error = retailTrade.checkCreate(BaseSQLiteBO.INVALID_CASE_ID);
        Assert.assertEquals(error, "");
        // case3
        retailTrade.setAmountCash(0.9d);
        retailTrade.setAmount1(0.1d);
        error = retailTrade.checkCreate(BaseSQLiteBO.INVALID_CASE_ID);
        Assert.assertEquals(error, String.format(RetailTrade.FIELD_ERROR_notPayment, retailTrade.field.getFIELD_NAME_amount1(), retailTrade.field.getFIELD_NAME_amount1()));
        retailTrade.setAmountCash(1d);
        retailTrade.setAmount1(0d);
        error = retailTrade.checkCreate(BaseSQLiteBO.INVALID_CASE_ID);
        Assert.assertEquals(error, "");
        // case4
        retailTrade.setAmountCash(0.9d);
        retailTrade.setAmount2(0.1d);
        error = retailTrade.checkCreate(BaseSQLiteBO.INVALID_CASE_ID);
        Assert.assertEquals(error, String.format(RetailTrade.FIELD_ERROR_notPayment, retailTrade.field.getFIELD_NAME_amount2(), retailTrade.field.getFIELD_NAME_amount2()));
        retailTrade.setAmountCash(1d);
        retailTrade.setAmount2(0d);
        error = retailTrade.checkCreate(BaseSQLiteBO.INVALID_CASE_ID);
        Assert.assertEquals(error, "");
        // case5
        retailTrade.setAmountCash(0.9d);
        retailTrade.setAmount3(0.1d);
        error = retailTrade.checkCreate(BaseSQLiteBO.INVALID_CASE_ID);
        Assert.assertEquals(error, String.format(RetailTrade.FIELD_ERROR_notPayment, retailTrade.field.getFIELD_NAME_amount3(), retailTrade.field.getFIELD_NAME_amount3()));
        retailTrade.setAmountCash(1d);
        retailTrade.setAmount3(0d);
        error = retailTrade.checkCreate(BaseSQLiteBO.INVALID_CASE_ID);
        Assert.assertEquals(error, "");
        // case6
        retailTrade.setAmountCash(0.9d);
        retailTrade.setAmount4(0.1d);
        error = retailTrade.checkCreate(BaseSQLiteBO.INVALID_CASE_ID);
        Assert.assertEquals(error, String.format(RetailTrade.FIELD_ERROR_notPayment, retailTrade.field.getFIELD_NAME_amount4(), retailTrade.field.getFIELD_NAME_amount4()));
        retailTrade.setAmountCash(1d);
        retailTrade.setAmount4(0d);
        error = retailTrade.checkCreate(BaseSQLiteBO.INVALID_CASE_ID);
        Assert.assertEquals(error, "");
        // case7
        retailTrade.setAmountCash(0.9d);
        retailTrade.setAmount5(0.1d);
        error = retailTrade.checkCreate(BaseSQLiteBO.INVALID_CASE_ID);
        Assert.assertEquals(error, String.format(RetailTrade.FIELD_ERROR_notPayment, retailTrade.field.getFIELD_NAME_amount5(), retailTrade.field.getFIELD_NAME_amount5()));
        retailTrade.setAmountCash(1d);
        retailTrade.setAmount5(0d);
        error = retailTrade.checkCreate(BaseSQLiteBO.INVALID_CASE_ID);
        Assert.assertEquals(error, "");
        // case8
        retailTrade.setPaymentType(2);
        retailTrade.setAmount(1d);
        retailTrade.setAmountCash(0.9d);
        retailTrade.setAmountAlipay(0.1d);
        error = retailTrade.checkCreate(BaseSQLiteBO.INVALID_CASE_ID);
        Assert.assertEquals(error, String.format(RetailTrade.FIELD_ERROR_notPayment, retailTrade.field.getFIELD_NAME_amountCash(), retailTrade.field.getFIELD_NAME_amountCash()));
        retailTrade.setAmountCash(0d);
        retailTrade.setAmountAlipay(1d);
        error = retailTrade.checkCreate(BaseSQLiteBO.INVALID_CASE_ID);
        Assert.assertEquals(error, "");

        // 检查：退款时，支付宝退的钱不能多过购买时支付宝支付的钱，WX退的钱不能多过购买时WX支付的钱
        retailTrade.setSourceID(1);
        retailTrade.setPaymentType(3);
        retailTrade.setAmount(60);
        // case1:支付宝退款

        retailTrade.setSaleAmountAlipay(50d);
        retailTrade.setAmountCash(5d);
        retailTrade.setAmountAlipay(55d);
        error = retailTrade.checkCreate(BaseSQLiteBO.INVALID_CASE_ID);
        Assert.assertEquals(error, RetailTrade.FIELD_ERROR_saleAfterReturnForAmountAlipay);
        retailTrade.setAmountCash(10d);
        retailTrade.setAmountAlipay(50d);
        error = retailTrade.checkCreate(BaseSQLiteBO.INVALID_CASE_ID);
        Assert.assertEquals(error, "");
        // case2:微信退款
        retailTrade.setAmountAlipay(0d);
        retailTrade.setPaymentType(5);
        retailTrade.setSaleAmountWeChat(50d);
        retailTrade.setAmountWeChat(55d);
        retailTrade.setAmountCash(5d);
        error = retailTrade.checkCreate(BaseSQLiteBO.INVALID_CASE_ID);
        Assert.assertEquals(error, RetailTrade.FIELD_ERROR_saleAfterReturnForAmountWeChat);
        retailTrade.setAmountWeChat(50d);
        retailTrade.setAmountCash(10d);
        error = retailTrade.checkCreate(BaseSQLiteBO.INVALID_CASE_ID);
        Assert.assertEquals(error, "");

        //
        error = retailTrade.checkCreate(BaseSQLiteBO.INVALID_CASE_ID);
        Assert.assertEquals(error, "");
        // consumerOpenID
        retailTrade.setConsumerOpenID("01234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567891");
        error = retailTrade.checkCreate(BaseSQLiteBO.INVALID_CASE_ID);
        Assert.assertEquals(error, RetailTrade.FIELD_ERROR_consumerOpenID);
        //
        retailTrade.setConsumerOpenID(null);
        error = retailTrade.checkCreate(BaseSQLiteBO.INVALID_CASE_ID);
        Assert.assertEquals(error, "");
        //
        retailTrade.setConsumerOpenID("asdf1231dsfa545455dfsa");
        error = retailTrade.checkCreate(BaseSQLiteBO.INVALID_CASE_ID);
        Assert.assertEquals(error, "");

        // 从表中包含重复类型的商品
        List<RetailTradeCommodity> retailTradeCommodities = new ArrayList<RetailTradeCommodity>();
        for (int i = 0; i < 2; i++) {
            RetailTradeCommodity retailTradeCommodity = DataInput.getRetailTradeCommodity();
            retailTradeCommodity.setCommodityID(1);
            retailTradeCommodity.setNO(1);
            retailTradeCommodity.setTradeID(1L);
            retailTradeCommodities.add(retailTradeCommodity);
        }
        retailTrade.setListSlave1(retailTradeCommodities);
        error = retailTrade.checkCreate(BaseSQLiteBO.INVALID_CASE_ID);
        Assert.assertEquals(error, RetailTrade.FIELD_ERROR_listSlave1);
        //
        for (int i = 0; i < retailTrade.getListSlave1().size(); i++) {
            RetailTradeCommodity retailTradeCommodity = (RetailTradeCommodity) retailTrade.getListSlave1().get(i);
            retailTradeCommodity.setCommodityID(i + 1);
        }
        error = retailTrade.checkCreate(BaseSQLiteBO.INVALID_CASE_ID);
        Assert.assertEquals(error, "");
    }

    @Test
    public void testCheckRetrieveN() throws ParseException {
        RetailTrade retailTrade = DataInput.getRetailTrade();
        retailTrade.setQueryKeyword(retailTrade.getSn());
        // 检查pageIndex和pageSize
        retailTrade.setPageIndex("-1");
        retailTrade.setPageSize("10");
        Assert.assertEquals(FieldFormat.FIELD_ERROR_Paging, retailTrade.checkRetrieveN(BaseSQLiteBO.INVALID_CASE_ID));
        // 检查pageIndex和pageSize
        retailTrade.setPageIndex("1");
        retailTrade.setPageSize("0");
        Assert.assertEquals(FieldFormat.FIELD_ERROR_Paging, retailTrade.checkRetrieveN(BaseSQLiteBO.INVALID_CASE_ID));
        //
        // 检查pageIndex和pageSize
        retailTrade.setPageIndex("1");
        retailTrade.setPageSize("10");
        Assert.assertEquals("", retailTrade.checkRetrieveN(BaseSQLiteBO.INVALID_CASE_ID));
        //
        //检查日期
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy年MM月dd日 HH:mm:ss");
        String date = "1970年00月00日 00:00:00";
        retailTrade.setDatetimeStart(sdf.parse(date));
        retailTrade.setDatetimeEnd(new Date());
        Assert.assertEquals("", retailTrade.checkRetrieveN(BaseSQLiteBO.INVALID_CASE_ID));
        //
        // 设置F_SN空串，结果通过。
        retailTrade.setQueryKeyword("");
        Assert.assertEquals("", retailTrade.checkRetrieveN(BaseSQLiteBO.INVALID_CASE_ID));
        //
        // 设置F_SN的长度小于10位，结果不通过。
        retailTrade.setQueryKeyword("LS201905");
        Assert.assertEquals(RetailTrade.FIELD_ERROR_sn_ForQuery, retailTrade.checkRetrieveN(BaseSQLiteBO.INVALID_CASE_ID));
        //
        // 设置F_SN的长度超过24，结果不通过。
        retailTrade.setQueryKeyword("LS201905291501080888880000001");
        Assert.assertEquals(RetailTrade.FIELD_ERROR_sn_ForQuery, retailTrade.checkRetrieveN(BaseSQLiteBO.INVALID_CASE_ID));
        //
        // 设置F_SN包含中文，结果不通过。
        retailTrade.setQueryKeyword("LS201905291501鸡腿00010001");
        Assert.assertEquals(RetailTrade.FIELD_ERROR_sn_ForQuery, retailTrade.checkRetrieveN(BaseSQLiteBO.INVALID_CASE_ID));
        //
        // 设置F_SN包含空格，结果不通过。
        retailTrade.setQueryKeyword("LS20190529150108 0010001");
        Assert.assertEquals(RetailTrade.FIELD_ERROR_sn_ForQuery, retailTrade.checkRetrieveN(BaseSQLiteBO.INVALID_CASE_ID));
        //
        // 设置F_SN为null，结果不通过。
        retailTrade.setQueryKeyword(null);
        Assert.assertEquals(RetailTrade.FIELD_ERROR_sn_ForQuery, retailTrade.checkRetrieveN(BaseSQLiteBO.INVALID_CASE_ID));
        //
        // 设置F_SN包含不是LS的其它英文字符，结果不通过。
        retailTrade.setQueryKeyword("LS201905291501abc0010001");
        Assert.assertEquals(RetailTrade.FIELD_ERROR_sn_ForQuery, retailTrade.checkRetrieveN(BaseSQLiteBO.INVALID_CASE_ID));
        //
        // 设置F_SN包含特殊字符，结果不通过。
        retailTrade.setQueryKeyword("LS201905291501&*&0010001");
        Assert.assertEquals(RetailTrade.FIELD_ERROR_sn_ForQuery, retailTrade.checkRetrieveN(BaseSQLiteBO.INVALID_CASE_ID));
        //
        // 设置F_SN是ls开头，结果通过。
        retailTrade.setQueryKeyword("ls20190529");
        Assert.assertEquals("", retailTrade.checkRetrieveN(BaseSQLiteBO.INVALID_CASE_ID));
        //
        // 设置F_SN是_1结尾，结果通过。
        retailTrade.setQueryKeyword("ls2019052915011230010001_1");
        Assert.assertEquals("", retailTrade.checkRetrieveN(BaseSQLiteBO.INVALID_CASE_ID));
        //
        // 设置F_SN的长度10-24位且格式正确，结果通过。
        retailTrade.setQueryKeyword("LS2019052915010800010001");
        Assert.assertEquals("", retailTrade.checkRetrieveN(BaseSQLiteBO.INVALID_CASE_ID));
        //
        //检查VipID
        retailTrade.setVipID(-1);
        Assert.assertEquals(FieldFormat.FIELD_ERROR_ID, retailTrade.checkRetrieveN(BaseSQLiteBO.INVALID_CASE_ID));
        //
        //检查VipID
        retailTrade.setVipID(1);
        Assert.assertEquals("", retailTrade.checkRetrieveN(BaseSQLiteBO.INVALID_CASE_ID));
        //
        //Case：CASE_RetailTrade_RetrieveNOldRetailTradeAsyncC
        //
        retailTrade.setQueryKeyword("");
        Assert.assertEquals("", retailTrade.checkRetrieveN(BaseHttpBO.CASE_RetailTrade_RetrieveNOldRetailTradeAsyncC));
        //
        // 设置F_SN的长度小于10位，结果不通过。
        retailTrade.setQueryKeyword("LS201905");
        Assert.assertEquals(RetailTrade.FIELD_ERROR_sn_ForQuery, retailTrade.checkRetrieveN(BaseHttpBO.CASE_RetailTrade_RetrieveNOldRetailTradeAsyncC));
        //
        // 设置F_SN的长度超过24，结果不通过。
        retailTrade.setQueryKeyword("LS201905291501080888880000001");
        Assert.assertEquals(RetailTrade.FIELD_ERROR_sn_ForQuery, retailTrade.checkRetrieveN(BaseHttpBO.CASE_RetailTrade_RetrieveNOldRetailTradeAsyncC));
        //
        // 设置F_SN包含中文，结果不通过。
        retailTrade.setQueryKeyword("LS201905291501鸡腿00010001");
        Assert.assertEquals(RetailTrade.FIELD_ERROR_sn_ForQuery, retailTrade.checkRetrieveN(BaseHttpBO.CASE_RetailTrade_RetrieveNOldRetailTradeAsyncC));
        //
        // 设置F_SN包含空格，结果不通过。
        retailTrade.setQueryKeyword("LS20190529150108 0010001");
        Assert.assertEquals(RetailTrade.FIELD_ERROR_sn_ForQuery, retailTrade.checkRetrieveN(BaseHttpBO.CASE_RetailTrade_RetrieveNOldRetailTradeAsyncC));
        //
        // 设置F_SN为null，结果不通过。
        retailTrade.setQueryKeyword(null);
        Assert.assertEquals(RetailTrade.FIELD_ERROR_sn_ForQuery, retailTrade.checkRetrieveN(BaseHttpBO.CASE_RetailTrade_RetrieveNOldRetailTradeAsyncC));
        //
        // 设置F_SN包含不是LS的其它英文字符，结果不通过。
        retailTrade.setQueryKeyword("201905291501abc0010001");
        Assert.assertEquals(RetailTrade.FIELD_ERROR_sn_ForQuery, retailTrade.checkRetrieveN(BaseHttpBO.CASE_RetailTrade_RetrieveNOldRetailTradeAsyncC));
        //
        // 设置F_SN包含特殊字符，结果不通过。
        retailTrade.setQueryKeyword("LS201905291501&*&0010001");
        Assert.assertEquals(RetailTrade.FIELD_ERROR_sn_ForQuery, retailTrade.checkRetrieveN(BaseHttpBO.CASE_RetailTrade_RetrieveNOldRetailTradeAsyncC));
        //
        // 设置F_SN是ls开头，结果通过。
        retailTrade.setQueryKeyword("ls2019052915011230010001");
        Assert.assertEquals("", retailTrade.checkRetrieveN(BaseHttpBO.CASE_RetailTrade_RetrieveNOldRetailTradeAsyncC));
        //
        // 设置F_SN是_1结尾，结果通过。
        retailTrade.setQueryKeyword("ls2019052915011230010001_1");
        Assert.assertEquals("", retailTrade.checkRetrieveN(BaseHttpBO.CASE_RetailTrade_RetrieveNOldRetailTradeAsyncC));
        //
        // 设置F_SN的长度10-24位且格式正确，结果通过。
        retailTrade.setQueryKeyword("LS2019052915010800010001");
        Assert.assertEquals("", retailTrade.checkRetrieveN(BaseHttpBO.CASE_RetailTrade_RetrieveNOldRetailTradeAsyncC));
    }

    @Override
    protected BaseModel getMasterTableObject() {
        RetailTrade retailTrade = DataInput.getRetailTrade2();
        return retailTrade;
    }

    @Override
    protected BaseModel updateMasterTableObject(BaseModel master) {
        master = DataInput.getRetailTrade2();
        return master;
    }

    @Override
    protected List<BaseModel> getSlaveTableObject() {
        List<BaseModel> bmList = new ArrayList<BaseModel>();
        //
        RetailTradeCommodity rtc1 = DataInput.getRetailTradeCommodity();
        RetailTradeCommodity rtc2 = DataInput.getRetailTradeCommodity();
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

    @Test
    public void testCompareTo1() throws CloneNotSupportedException, InterruptedException {
        compareTo_Case1();
    }

    @Test
    public void testCompareTo2() throws CloneNotSupportedException, InterruptedException {
        compareTo_Case2();
    }

    @Test
    public void testCompareTo3() throws CloneNotSupportedException, InterruptedException {
        compareTo_Case3();
    }

    @Test
    public void testCompareTo4() throws CloneNotSupportedException, InterruptedException {
        compareTo_Case4();
    }

    @Test
    public void testCompareTo5() throws CloneNotSupportedException, InterruptedException {
        compareTo_Case5();
    }

    @Test
    public void testCompareTo6() throws CloneNotSupportedException, InterruptedException {
        compareTo_Case6();
    }

    @Test
    public void testCompareTo7() throws CloneNotSupportedException, InterruptedException {
        compareTo_Case7();
    }

    @Test
    public void testCompareTo8() throws CloneNotSupportedException, InterruptedException {
        compareTo_Case8();
    }

    @Test
    public void testCompareTo9() throws CloneNotSupportedException, InterruptedException {
        compareTo_Case9();
    }

    @Test
    public void testCompareTo10() throws CloneNotSupportedException, InterruptedException {
        compareTo_Case10();
    }

    @Test
    public void testGenerateRetailTradeSN() {
        // case1; 参数为1位数时
        RetailTrade retailTrade = new RetailTrade();
        String sn = retailTrade.generateRetailTradeSN(1);
        Assert.assertTrue( FieldFormat.checkRetailTradeSN(false, sn),"sn不符合规范");
        // case 2: 参数超长
        sn = retailTrade.generateRetailTradeSN(10000);
        Assert.assertFalse(FieldFormat.checkRetailTradeSN(false, sn),"sn符合规范, 预期是失败");
        //case3: 参数刚刚好达到临界值
        sn = retailTrade.generateRetailTradeSN(9999);
        Assert.assertTrue( FieldFormat.checkRetailTradeSN(false, sn),"sn不符合规范");
    }

    @Test
    public void test() {
        System.out.println("=====" + GeneralUtil.round(635.125, 2));
    }
}
