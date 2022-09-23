package com.test.bx.app.bo;


import com.base.BaseAndroidTestCase;
import com.bx.erp.bo.BaseSQLiteBO;
import com.bx.erp.bo.RetailTradeAggregationSQLiteBO;
import com.bx.erp.common.GlobalController;
import com.bx.erp.event.UI.BaseSQLiteEvent;
import com.bx.erp.event.UI.RetailTradeAggregationSQLiteEvent;
import com.bx.erp.model.ErrorInfo;
import com.bx.erp.model.RetailTradeAggregation;
import com.bx.erp.utils.DatetimeUtil;
import com.bx.erp.utils.Shared;
import com.bx.erp.view.activity.BaseActivity;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import java.text.ParseException;
import java.util.Date;

public class RetailtradeAggregationSqliteBoTest extends BaseAndroidTestCase {

    private static RetailTradeAggregationSQLiteEvent retailTradeAggregationSQLiteEvent;

    private static RetailTradeAggregationSQLiteBO retailTradeAggregationSQLiteBO;

    private static final int Event_ID_RetailTradeAggregationSqliteBoTest = 10000;

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onRetailTradeAggregationEvent(RetailTradeAggregationSQLiteEvent event) {
        if (event.getId() == Event_ID_RetailTradeAggregationSqliteBoTest) {
            event.onEvent();
        } else {
            StackTraceElement ste = new Exception().getStackTrace()[1];
            System.out.println(ste.getClassName() + "." + ste.getMethodName() + "未处理的Event，ID=" + event.getId());
        }
    }

    public static class DataInput {
        private static RetailTradeAggregation retailTradeAggregation = null;

        protected static final RetailTradeAggregation getRetailTradeAggregation() throws CloneNotSupportedException {
            retailTradeAggregation = new RetailTradeAggregation();
            retailTradeAggregation.setStaffID(1);
            retailTradeAggregation.setPosID(5);
            retailTradeAggregation.setTradeNO(10);
            retailTradeAggregation.setAmount(100);
            retailTradeAggregation.setReserveAmount(20);
            retailTradeAggregation.setCashAmount(10);
            retailTradeAggregation.setWechatAmount(20);
            retailTradeAggregation.setAlipayAmount(30);
            retailTradeAggregation.setWorkTimeStart(new Date());
            retailTradeAggregation.setWorkTimeEnd(DatetimeUtil.addSecond(new Date(), 5));
            retailTradeAggregation.setUploadDateTime(new Date());

            return (RetailTradeAggregation) retailTradeAggregation.clone();
        }
    }

    @BeforeClass
    public static void beforeClass() {
        Shared.printTestClassStartInfo();
    }

    @AfterClass
    public static void afterClass() {
        Shared.printTestClassEndInfo();
    }

    @Override
    public void setUp() throws Exception {
        super.setUp();

        EventBus.getDefault().register(this);

        if (retailTradeAggregationSQLiteEvent == null) {
            retailTradeAggregationSQLiteEvent = new RetailTradeAggregationSQLiteEvent();
            retailTradeAggregationSQLiteEvent.setId(Event_ID_RetailTradeAggregationSqliteBoTest);
        }
        if (retailTradeAggregationSQLiteBO == null) {
            retailTradeAggregationSQLiteBO = new RetailTradeAggregationSQLiteBO(GlobalController.getInstance().getContext(), retailTradeAggregationSQLiteEvent, null);
        }
        BaseActivity.retailTradeAggregation = new RetailTradeAggregation();
    }

    @Override
    public void tearDown() throws Exception {
        super.tearDown();

        EventBus.getDefault().unregister(this);
    }

    @Test
    public void test_createAsync() throws CloneNotSupportedException {
        Shared.printTestMethodStartInfo();

        BaseActivity.retailTradeAggregation = RetailtradeAggregationSqliteBoTest.DataInput.getRetailTradeAggregation();
        BaseActivity.retailTradeAggregation.setTradeNO(18);
        retailTradeAggregationSQLiteBO.getSqLiteEvent().setEventTypeSQLite(BaseSQLiteEvent.EnumSQLiteEventType.ESET_RetailTradeAggregation_CreateAsync);
        //往sqlite插入一条零售单数为非0的收银汇总,期待结果：字段验证不通过，插入失败
        if (retailTradeAggregationSQLiteBO.createSync(BaseSQLiteBO.INVALID_CASE_ID, BaseActivity.retailTradeAggregation) == null
                && retailTradeAggregationSQLiteBO.getSqLiteEvent().getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_BusinessLogicNotDefined) {
            Assert.assertTrue("收银汇总插入sqlite时，TradeNo字段验证失败", retailTradeAggregationSQLiteBO.getSqLiteEvent().getLastErrorMessage().equals(retailTradeAggregationSQLiteBO.FIELD_ERROR_TradeNo_CreateInSQLite));
        }
        //往sqlite插入一条零售单数为0，营业额非0的收银汇总,期待结果：字段验证不通过，插入失败
        BaseActivity.retailTradeAggregation.setTradeNO(0);
        BaseActivity.retailTradeAggregation.setAmount(0.2938d);
        if (retailTradeAggregationSQLiteBO.createSync(BaseSQLiteBO.INVALID_CASE_ID, BaseActivity.retailTradeAggregation) == null
                && retailTradeAggregationSQLiteBO.getSqLiteEvent().getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_BusinessLogicNotDefined) {
            Assert.assertTrue("收银汇总插入sqlite时，Amount字段验证失败", retailTradeAggregationSQLiteBO.getSqLiteEvent().getLastErrorMessage().equals(retailTradeAggregationSQLiteBO.FIELD_ERROR_Amount_CreateInSQLite));
        }
        //往sqlite插入一条零售单数为0，营业额为0，现金支付金额非0的收银汇总,期待结果：字段验证不通过，插入失败
        BaseActivity.retailTradeAggregation.setAmount(0.000000d);
        BaseActivity.retailTradeAggregation.setCashAmount(0.183293d);
        if (retailTradeAggregationSQLiteBO.createSync(BaseSQLiteBO.INVALID_CASE_ID, BaseActivity.retailTradeAggregation) == null
                && retailTradeAggregationSQLiteBO.getSqLiteEvent().getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_BusinessLogicNotDefined) {
            Assert.assertTrue("收银汇总插入sqlite时，CashAmount字段验证失败", retailTradeAggregationSQLiteBO.getSqLiteEvent().getLastErrorMessage().equals(retailTradeAggregationSQLiteBO.FIELD_ERROR_CashAmount_CreateInSQLite));
        }
        //往sqlite插入一条零售单数为0，营业额为0，现金支付金额为0,微信支付非0的收银汇总,期待结果：字段验证不通过，插入失败
        BaseActivity.retailTradeAggregation.setCashAmount(0.000000d);
        BaseActivity.retailTradeAggregation.setWechatAmount(0.9999d);
        if (retailTradeAggregationSQLiteBO.createSync(BaseSQLiteBO.INVALID_CASE_ID, BaseActivity.retailTradeAggregation) == null
                && retailTradeAggregationSQLiteBO.getSqLiteEvent().getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_BusinessLogicNotDefined) {
            Assert.assertTrue("收银汇总插入sqlite时，WeChatAmount字段验证失败", retailTradeAggregationSQLiteBO.getSqLiteEvent().getLastErrorMessage().equals(retailTradeAggregationSQLiteBO.FIELD_ERROR_WeChatAmount_CreateInSQLite));
        }
        //
        BaseActivity.retailTradeAggregation.setWechatAmount(0.00000d);
        RetailTradeAggregation rta = (RetailTradeAggregation) retailTradeAggregationSQLiteBO.createSync(BaseSQLiteBO.INVALID_CASE_ID, BaseActivity.retailTradeAggregation);
        Assert.assertTrue("收银汇总插入SQLite失败：" + retailTradeAggregationSQLiteBO.getSqLiteEvent().printErrorInfo(), rta != null);
        //清空测试数据
        retailTradeAggregationSQLiteBO.deleteAsync(BaseSQLiteBO.INVALID_CASE_ID, rta);
        BaseActivity.retailTradeAggregation = null;
    }

    public void test_updateAsync() throws ParseException, CloneNotSupportedException {
        Shared.printTestMethodStartInfo();

        BaseActivity.retailTradeAggregation = RetailtradeAggregationSqliteBoTest.DataInput.getRetailTradeAggregation();
        RetailTradeAggregation rta = new RetailTradeAggregation();
        rta.setID(BaseActivity.retailTradeAggregation.getID());
        //以下测试调用的是异步接口，调用接口时，返回的是checkUpdate()的结果，不是写SQLite的结果。判断checkUpdate()的结果已经达到了测试的目的，所以不需要等待写SQLite的结果
        //
        //检验staffID
        rta.setStaffID(BaseActivity.retailTradeAggregation.getStaffID() + 1);
        if (retailTradeAggregationSQLiteBO.updateAsync(BaseSQLiteBO.INVALID_CASE_ID, rta)) {
            Assert.assertTrue("测试失败：" + retailTradeAggregationSQLiteBO.getSqLiteEvent().printErrorInfo(), false);
        } else {
            if (retailTradeAggregationSQLiteBO.getSqLiteEvent().getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_BusinessLogicNotDefined
                    && retailTradeAggregationSQLiteBO.getSqLiteEvent().getLastErrorMessage().equals(retailTradeAggregationSQLiteBO.FIELD_ERROR_StaffID_Update)) {
            } else {
                Assert.assertTrue("在sqlite更新收银汇总时，staffID字段验证失败", false);
            }
        }
        rta.setStaffID(BaseActivity.retailTradeAggregation.getStaffID());
        //检验PosID
        rta.setPosID(BaseActivity.retailTradeAggregation.getPosID() + 1);
        if (retailTradeAggregationSQLiteBO.updateAsync(BaseSQLiteBO.INVALID_CASE_ID, rta)) {
            Assert.assertTrue("测试失败：" + retailTradeAggregationSQLiteBO.getSqLiteEvent().printErrorInfo(), false);
        } else {
            if (retailTradeAggregationSQLiteBO.getSqLiteEvent().getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_BusinessLogicNotDefined
                    && retailTradeAggregationSQLiteBO.getSqLiteEvent().getLastErrorMessage().equals(retailTradeAggregationSQLiteBO.FIELD_ERROR_PosID_Update)) {
            } else {
                Assert.assertTrue("在sqlite更新收银汇总时，PosID字段验证失败", false);
            }
        }
        rta.setPosID(BaseActivity.retailTradeAggregation.getPosID());
        //检验ReserveAmount
        rta.setReserveAmount(BaseActivity.retailTradeAggregation.getReserveAmount() + 1);
        if (retailTradeAggregationSQLiteBO.updateAsync(BaseSQLiteBO.INVALID_CASE_ID, rta)) {
            Assert.assertTrue("测试失败：" + retailTradeAggregationSQLiteBO.getSqLiteEvent().printErrorInfo(), false);
        } else {
            if (retailTradeAggregationSQLiteBO.getSqLiteEvent().getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_BusinessLogicNotDefined
                    && retailTradeAggregationSQLiteBO.getSqLiteEvent().getLastErrorMessage().equals(retailTradeAggregationSQLiteBO.FIELD_ERROR_ReserveAmount_Update)) {
            } else {
                Assert.assertTrue("在sqlite更新收银汇总时，ReserveAmount字段验证失败", false);
            }
        }
        rta.setReserveAmount(BaseActivity.retailTradeAggregation.getReserveAmount());
        //检验TradeNO
        rta.setTradeNO(BaseActivity.retailTradeAggregation.getTradeNO() - 1);
        if (retailTradeAggregationSQLiteBO.updateAsync(BaseSQLiteBO.INVALID_CASE_ID, rta)) {
            Assert.assertTrue("测试失败：" + retailTradeAggregationSQLiteBO.getSqLiteEvent().printErrorInfo(), false);
        } else {
            if (retailTradeAggregationSQLiteBO.getSqLiteEvent().getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_BusinessLogicNotDefined
                    && retailTradeAggregationSQLiteBO.getSqLiteEvent().getLastErrorMessage().equals(retailTradeAggregationSQLiteBO.FIELD_ERROR_TradeNO_Update)) {
            } else {
                Assert.assertTrue("在sqlite更新收银汇总时，TradeNO字段验证失败", false);
            }
        }
        rta.setTradeNO(BaseActivity.retailTradeAggregation.getTradeNO() + 1);
        //检验workTimeStart
        rta.setWorkTimeStart(DatetimeUtil.addMinutes(BaseActivity.retailTradeAggregation.getWorkTimeStart(), 5));
        if (retailTradeAggregationSQLiteBO.updateAsync(BaseSQLiteBO.INVALID_CASE_ID, rta)) {
            Assert.assertTrue("测试失败：" + retailTradeAggregationSQLiteBO.getSqLiteEvent().printErrorInfo(), false);
        } else {
            if (retailTradeAggregationSQLiteBO.getSqLiteEvent().getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_BusinessLogicNotDefined
                    && retailTradeAggregationSQLiteBO.getSqLiteEvent().getLastErrorMessage().equals(retailTradeAggregationSQLiteBO.FIELD_ERROR_workTimeStart_Update)) {
            } else {
                Assert.assertTrue("在sqlite更新收银汇总时，workTimeStart字段验证失败", false);
            }
        }
        rta.setWorkTimeStart(BaseActivity.retailTradeAggregation.getWorkTimeStart());
        //检验workTimeEnd
        rta.setWorkTimeEnd(DatetimeUtil.addMinutes(BaseActivity.retailTradeAggregation.getWorkTimeEnd(), -5));
        if (retailTradeAggregationSQLiteBO.updateAsync(BaseSQLiteBO.INVALID_CASE_ID, rta)) {
            Assert.assertTrue("测试失败：" + retailTradeAggregationSQLiteBO.getSqLiteEvent().printErrorInfo(), false);
        } else {
            if (retailTradeAggregationSQLiteBO.getSqLiteEvent().getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_BusinessLogicNotDefined
                    && retailTradeAggregationSQLiteBO.getSqLiteEvent().getLastErrorMessage().equals(retailTradeAggregationSQLiteBO.FIELD_ERROR_workTimeEnd_Update)) {
            } else {
                Assert.assertTrue("在sqlite更新收银汇总时，workTimeEnd字段验证失败", false);
            }
        }
        //条件符合时，是否可以成功更新收银汇总
        rta.setWorkTimeEnd((Date) BaseActivity.retailTradeAggregation.getWorkTimeEnd().clone());
        if (retailTradeAggregationSQLiteBO.updateAsync(BaseSQLiteBO.INVALID_CASE_ID, rta)
                && retailTradeAggregationSQLiteBO.getSqLiteEvent().getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError) {
            Assert.assertTrue("在sqlite更新收银汇总失败：" + retailTradeAggregationSQLiteBO.getSqLiteEvent().printErrorInfo(), retailTradeAggregationSQLiteBO.getSqLiteEvent().getLastErrorMessage().equals(""));
        }

        //清空测试数据
        retailTradeAggregationSQLiteBO.deleteAsync(BaseSQLiteBO.INVALID_CASE_ID, rta);
        BaseActivity.retailTradeAggregation = null;
    }
}
