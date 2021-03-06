package com.test.bx.app;


import com.bx.erp.bo.BaseHttpBO;
import com.bx.erp.bo.BaseSQLiteBO;
import com.bx.erp.model.BaseModel;
import com.bx.erp.model.RetailTrade;
import com.bx.erp.model.RetailTradeCommodity;
import com.bx.erp.model.RetailTradeCoupon;
import com.bx.erp.utils.FieldFormat;
import com.bx.erp.utils.GeneralUtil;
import com.bx.erp.utils.Shared;

import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

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
            retailTradeInput.setID(1l);

            RetailTradeCommodity retailTradeCommodity = new RetailTradeCommodity();
            retailTradeCommodity.setCommodityID(1);
            retailTradeCommodity.setNO(ran.nextInt());
            retailTradeCommodity.setPriceOriginal(ran.nextDouble());
            retailTradeCommodity.setDiscount(0.9);
            retailTradeCommodity.setBarcodeID(1);
            retailTradeCommodity.setID(1l);
            retailTradeCommodity.setPriceVIPOriginal(0.0);
            //
            RetailTradeCommodity retailTradeCommodity2 = new RetailTradeCommodity();
            retailTradeCommodity2.setCommodityID(2);
            retailTradeCommodity2.setNO(ran.nextInt());
            retailTradeCommodity2.setPriceOriginal(ran.nextDouble());
            retailTradeCommodity2.setDiscount(0.9);
            retailTradeCommodity2.setBarcodeID(1);
            retailTradeCommodity2.setID(1l);
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
            retailTradeInput.setID(1l);

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
        // ??????F_SN???null?????????????????????
        retailTrade.setSn(null);
        error = retailTrade.checkCreate(BaseSQLiteBO.INVALID_CASE_ID);
        Assert.assertEquals(error, RetailTrade.FIELD_ERROR_sn);
        //
        // ??????F_SN???????????????????????????
        retailTrade.setSn("");
        error = retailTrade.checkCreate(BaseSQLiteBO.INVALID_CASE_ID);
        Assert.assertEquals(error, RetailTrade.FIELD_ERROR_sn);
        //
        // ??????F_SN???????????????24?????????????????????
        retailTrade.setSn("LS201905291501080888880000001");
        error = retailTrade.checkCreate(BaseSQLiteBO.INVALID_CASE_ID);
        Assert.assertEquals(error, RetailTrade.FIELD_ERROR_sn);
        //
        // ??????F_SN??????LS???????????????????????????
        retailTrade.setSn("HH2019052915010800000001");
        error = retailTrade.checkCreate(BaseSQLiteBO.INVALID_CASE_ID);
        Assert.assertEquals(error, RetailTrade.FIELD_ERROR_sn);
        //
        // ??????F_SN?????????????????????????????????
        retailTrade.setSn("LS2019052915?????????800000001");
        error = retailTrade.checkCreate(BaseSQLiteBO.INVALID_CASE_ID);
        Assert.assertEquals(error, RetailTrade.FIELD_ERROR_sn);
        //
        // ??????F_SN?????????????????????????????????????????????
        retailTrade.setSn("LS2019052915sss800000001");
        error = retailTrade.checkCreate(BaseSQLiteBO.INVALID_CASE_ID);
        Assert.assertEquals(error, RetailTrade.FIELD_ERROR_sn);
        //
        // ??????F_SN???????????????10?????????????????????
        retailTrade.setSn("LS201905");
        error = retailTrade.checkCreate(BaseSQLiteBO.INVALID_CASE_ID);
        Assert.assertEquals(error, RetailTrade.FIELD_ERROR_sn);
        //
        // ??????F_SN???????????????24?????????????????????????????????
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

        // ????????????????????????????????????????????????=?????????
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

        // ????????????????????????????????????????????????0.000000d?????????????????????????????????????????????????????????????????????0.000000d,???????????????2?????????????????????>0.000000d
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

        // ???????????????????????????????????????????????????????????????0???
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

        // ????????????????????????????????????????????????????????????????????????????????????WX??????????????????????????????WX????????????
        retailTrade.setSourceID(1);
        retailTrade.setPaymentType(3);
        retailTrade.setAmount(60);
        // case1:???????????????

        retailTrade.setSaleAmountAlipay(50d);
        retailTrade.setAmountCash(5d);
        retailTrade.setAmountAlipay(55d);
        error = retailTrade.checkCreate(BaseSQLiteBO.INVALID_CASE_ID);
        Assert.assertEquals(error, RetailTrade.FIELD_ERROR_saleAfterReturnForAmountAlipay);
        retailTrade.setAmountCash(10d);
        retailTrade.setAmountAlipay(50d);
        error = retailTrade.checkCreate(BaseSQLiteBO.INVALID_CASE_ID);
        Assert.assertEquals(error, "");
        // case2:????????????
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

        // ????????????????????????????????????
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
        // ??????pageIndex???pageSize
        retailTrade.setPageIndex("-1");
        retailTrade.setPageSize("10");
        Assert.assertEquals(FieldFormat.FIELD_ERROR_Paging, retailTrade.checkRetrieveN(BaseSQLiteBO.INVALID_CASE_ID));
        // ??????pageIndex???pageSize
        retailTrade.setPageIndex("1");
        retailTrade.setPageSize("0");
        Assert.assertEquals(FieldFormat.FIELD_ERROR_Paging, retailTrade.checkRetrieveN(BaseSQLiteBO.INVALID_CASE_ID));
        //
        // ??????pageIndex???pageSize
        retailTrade.setPageIndex("1");
        retailTrade.setPageSize("10");
        Assert.assertEquals("", retailTrade.checkRetrieveN(BaseSQLiteBO.INVALID_CASE_ID));
        //
        //????????????
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy???MM???dd??? HH:mm:ss");
        String date = "1970???00???00??? 00:00:00";
        retailTrade.setDatetimeStart(sdf.parse(date));
        retailTrade.setDatetimeEnd(new Date());
        Assert.assertEquals("", retailTrade.checkRetrieveN(BaseSQLiteBO.INVALID_CASE_ID));
        //
        // ??????F_SN????????????????????????
        retailTrade.setQueryKeyword("");
        Assert.assertEquals("", retailTrade.checkRetrieveN(BaseSQLiteBO.INVALID_CASE_ID));
        //
        // ??????F_SN???????????????10????????????????????????
        retailTrade.setQueryKeyword("LS201905");
        Assert.assertEquals(RetailTrade.FIELD_ERROR_sn_ForQuery, retailTrade.checkRetrieveN(BaseSQLiteBO.INVALID_CASE_ID));
        //
        // ??????F_SN???????????????24?????????????????????
        retailTrade.setQueryKeyword("LS201905291501080888880000001");
        Assert.assertEquals(RetailTrade.FIELD_ERROR_sn_ForQuery, retailTrade.checkRetrieveN(BaseSQLiteBO.INVALID_CASE_ID));
        //
        // ??????F_SN?????????????????????????????????
        retailTrade.setQueryKeyword("LS201905291501??????00010001");
        Assert.assertEquals(RetailTrade.FIELD_ERROR_sn_ForQuery, retailTrade.checkRetrieveN(BaseSQLiteBO.INVALID_CASE_ID));
        //
        // ??????F_SN?????????????????????????????????
        retailTrade.setQueryKeyword("LS20190529150108 0010001");
        Assert.assertEquals(RetailTrade.FIELD_ERROR_sn_ForQuery, retailTrade.checkRetrieveN(BaseSQLiteBO.INVALID_CASE_ID));
        //
        // ??????F_SN???null?????????????????????
        retailTrade.setQueryKeyword(null);
        Assert.assertEquals(RetailTrade.FIELD_ERROR_sn_ForQuery, retailTrade.checkRetrieveN(BaseSQLiteBO.INVALID_CASE_ID));
        //
        // ??????F_SN????????????LS??????????????????????????????????????????
        retailTrade.setQueryKeyword("LS201905291501abc0010001");
        Assert.assertEquals(RetailTrade.FIELD_ERROR_sn_ForQuery, retailTrade.checkRetrieveN(BaseSQLiteBO.INVALID_CASE_ID));
        //
        // ??????F_SN???????????????????????????????????????
        retailTrade.setQueryKeyword("LS201905291501&*&0010001");
        Assert.assertEquals(RetailTrade.FIELD_ERROR_sn_ForQuery, retailTrade.checkRetrieveN(BaseSQLiteBO.INVALID_CASE_ID));
        //
        // ??????F_SN???ls????????????????????????
        retailTrade.setQueryKeyword("ls20190529");
        Assert.assertEquals("", retailTrade.checkRetrieveN(BaseSQLiteBO.INVALID_CASE_ID));
        //
        // ??????F_SN???_1????????????????????????
        retailTrade.setQueryKeyword("ls2019052915011230010001_1");
        Assert.assertEquals("", retailTrade.checkRetrieveN(BaseSQLiteBO.INVALID_CASE_ID));
        //
        // ??????F_SN?????????10-24????????????????????????????????????
        retailTrade.setQueryKeyword("LS2019052915010800010001");
        Assert.assertEquals("", retailTrade.checkRetrieveN(BaseSQLiteBO.INVALID_CASE_ID));
        //
        //??????VipID
        retailTrade.setVipID(-1);
        Assert.assertEquals(FieldFormat.FIELD_ERROR_ID, retailTrade.checkRetrieveN(BaseSQLiteBO.INVALID_CASE_ID));
        //
        //??????VipID
        retailTrade.setVipID(1);
        Assert.assertEquals("", retailTrade.checkRetrieveN(BaseSQLiteBO.INVALID_CASE_ID));
        //
        //Case???CASE_RetailTrade_RetrieveNOldRetailTradeAsyncC
        //
        retailTrade.setQueryKeyword("");
        Assert.assertEquals("", retailTrade.checkRetrieveN(BaseHttpBO.CASE_RetailTrade_RetrieveNOldRetailTradeAsyncC));
        //
        // ??????F_SN???????????????10????????????????????????
        retailTrade.setQueryKeyword("LS201905");
        Assert.assertEquals(RetailTrade.FIELD_ERROR_sn_ForQuery, retailTrade.checkRetrieveN(BaseHttpBO.CASE_RetailTrade_RetrieveNOldRetailTradeAsyncC));
        //
        // ??????F_SN???????????????24?????????????????????
        retailTrade.setQueryKeyword("LS201905291501080888880000001");
        Assert.assertEquals(RetailTrade.FIELD_ERROR_sn_ForQuery, retailTrade.checkRetrieveN(BaseHttpBO.CASE_RetailTrade_RetrieveNOldRetailTradeAsyncC));
        //
        // ??????F_SN?????????????????????????????????
        retailTrade.setQueryKeyword("LS201905291501??????00010001");
        Assert.assertEquals(RetailTrade.FIELD_ERROR_sn_ForQuery, retailTrade.checkRetrieveN(BaseHttpBO.CASE_RetailTrade_RetrieveNOldRetailTradeAsyncC));
        //
        // ??????F_SN?????????????????????????????????
        retailTrade.setQueryKeyword("LS20190529150108 0010001");
        Assert.assertEquals(RetailTrade.FIELD_ERROR_sn_ForQuery, retailTrade.checkRetrieveN(BaseHttpBO.CASE_RetailTrade_RetrieveNOldRetailTradeAsyncC));
        //
        // ??????F_SN???null?????????????????????
        retailTrade.setQueryKeyword(null);
        Assert.assertEquals(RetailTrade.FIELD_ERROR_sn_ForQuery, retailTrade.checkRetrieveN(BaseHttpBO.CASE_RetailTrade_RetrieveNOldRetailTradeAsyncC));
        //
        // ??????F_SN????????????LS??????????????????????????????????????????
        retailTrade.setQueryKeyword("201905291501abc0010001");
        Assert.assertEquals(RetailTrade.FIELD_ERROR_sn_ForQuery, retailTrade.checkRetrieveN(BaseHttpBO.CASE_RetailTrade_RetrieveNOldRetailTradeAsyncC));
        //
        // ??????F_SN???????????????????????????????????????
        retailTrade.setQueryKeyword("LS201905291501&*&0010001");
        Assert.assertEquals(RetailTrade.FIELD_ERROR_sn_ForQuery, retailTrade.checkRetrieveN(BaseHttpBO.CASE_RetailTrade_RetrieveNOldRetailTradeAsyncC));
        //
        // ??????F_SN???ls????????????????????????
        retailTrade.setQueryKeyword("ls2019052915011230010001");
        Assert.assertEquals("", retailTrade.checkRetrieveN(BaseHttpBO.CASE_RetailTrade_RetrieveNOldRetailTradeAsyncC));
        //
        // ??????F_SN???_1????????????????????????
        retailTrade.setQueryKeyword("ls2019052915011230010001_1");
        Assert.assertEquals("", retailTrade.checkRetrieveN(BaseHttpBO.CASE_RetailTrade_RetrieveNOldRetailTradeAsyncC));
        //
        // ??????F_SN?????????10-24????????????????????????????????????
        retailTrade.setQueryKeyword("LS2019052915010800010001");
        Assert.assertEquals("", retailTrade.checkRetrieveN(BaseHttpBO.CASE_RetailTrade_RetrieveNOldRetailTradeAsyncC));
    }

    @Override
    protected BaseModel getMasterTableObject() throws CloneNotSupportedException, InterruptedException {
        RetailTrade retailTrade = DataInput.getRetailTrade2();
        return retailTrade;
    }

    @Override
    protected BaseModel updateMasterTableObject(BaseModel master) throws CloneNotSupportedException, InterruptedException {
        master = DataInput.getRetailTrade2();
        return master;
    }

    @Override
    protected List<BaseModel> getSlaveTableObject() throws CloneNotSupportedException {
        List<BaseModel> bmList = new ArrayList<BaseModel>();
        //
        RetailTradeCommodity rtc1 = DataInput.getRetailTradeCommodity();
        RetailTradeCommodity rtc2 = DataInput.getRetailTradeCommodity();
        rtc2.setCommodityID(2); // ??????????????????????????????????????????ID??????
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
        // case1; ?????????1?????????
        RetailTrade retailTrade = new RetailTrade();
        String sn = retailTrade.generateRetailTradeSN(1);
        Assert.assertTrue("sn???????????????", FieldFormat.checkRetailTradeSN(false, sn));
        // case 2: ????????????
        sn = retailTrade.generateRetailTradeSN(10000);
        Assert.assertFalse("sn????????????, ???????????????", FieldFormat.checkRetailTradeSN(false, sn));
        //case3: ??????????????????????????????
        sn = retailTrade.generateRetailTradeSN(9999);
        Assert.assertTrue("sn???????????????", FieldFormat.checkRetailTradeSN(false, sn));
    }

    @Test
    public void test() {
        System.out.println("=====" + GeneralUtil.round(635.125, 2));
    }
}
