package com.test.bx.app;

import android.test.AndroidTestCase;

import com.bx.erp.bo.BaseSQLiteBO;
import com.bx.erp.model.RetailTradeAggregation;
import com.bx.erp.utils.DatetimeUtil;
import com.bx.erp.utils.Shared;
import com.bx.erp.view.activity.BaseActivity;

import org.junit.Assert;
import org.junit.Test;

import java.util.Date;

public class RetailTradeAggregationTest extends AndroidTestCase {
    @Test
    public void test_a_CheckCreate() throws Exception {

        Shared.printTestMethodStartInfo();

        RetailTradeAggregation retailTradeAggregation = new RetailTradeAggregation();
        BaseActivity.retailTradeAggregation = new RetailTradeAggregation();
        Date date = new Date();
        retailTradeAggregation.setWorkTimeStart(date);
        BaseActivity.retailTradeAggregation.setWorkTimeStart(date);

        retailTradeAggregation.setWorkTimeEnd(DatetimeUtil.addSecond(retailTradeAggregation.getWorkTimeStart(), 1));
        BaseActivity.retailTradeAggregation.setWorkTimeEnd(DatetimeUtil.addSecond(BaseActivity.retailTradeAggregation.getWorkTimeStart(), 1));
        String error = "";
        //
        retailTradeAggregation.setStaffID(0);
        error = retailTradeAggregation.checkCreate(BaseSQLiteBO.INVALID_CASE_ID);
        Assert.assertEquals(error, RetailTradeAggregation.FIELD_ERROR_StaffID);
        retailTradeAggregation.setStaffID(-98);
        error = retailTradeAggregation.checkCreate(BaseSQLiteBO.INVALID_CASE_ID);
        Assert.assertEquals(error, RetailTradeAggregation.FIELD_ERROR_StaffID);
        retailTradeAggregation.setStaffID(1);
        //
        retailTradeAggregation.setPosID(0);
        error = retailTradeAggregation.checkCreate(BaseSQLiteBO.INVALID_CASE_ID);
        Assert.assertEquals(error, RetailTradeAggregation.FIELD_ERROR_PosID);
        retailTradeAggregation.setPosID(-98);
        error = retailTradeAggregation.checkCreate(BaseSQLiteBO.INVALID_CASE_ID);
        Assert.assertEquals(error, RetailTradeAggregation.FIELD_ERROR_PosID);
        retailTradeAggregation.setPosID(1);
        //
        retailTradeAggregation.setTradeNO(-1);
        error = retailTradeAggregation.checkCreate(BaseSQLiteBO.INVALID_CASE_ID);
        Assert.assertEquals(error, RetailTradeAggregation.FIELD_ERROR_TradeNO);
        retailTradeAggregation.setTradeNO(0);
        error = retailTradeAggregation.checkCreate(BaseSQLiteBO.INVALID_CASE_ID);
        Assert.assertEquals(error, "");
        //
        retailTradeAggregation.setReserveAmount(-10d);
        error = retailTradeAggregation.checkCreate(BaseSQLiteBO.INVALID_CASE_ID);
        Assert.assertEquals(error, RetailTradeAggregation.FIELD_ERROR_ReserveAmount);
        //
        retailTradeAggregation.setReserveAmount(0);
        error = retailTradeAggregation.checkCreate(BaseSQLiteBO.INVALID_CASE_ID);
        Assert.assertEquals(error, "");
        //
        retailTradeAggregation.setReserveAmount(10001d);
        error = retailTradeAggregation.checkCreate(BaseSQLiteBO.INVALID_CASE_ID);
        Assert.assertEquals(error, RetailTradeAggregation.FIELD_ERROR_ReserveAmount);
        //
        retailTradeAggregation.setReserveAmount(9999);
        error = retailTradeAggregation.checkCreate(BaseSQLiteBO.INVALID_CASE_ID);
        Assert.assertEquals(error, "");
        //
        retailTradeAggregation.setWorkTimeStart(new Date());
        retailTradeAggregation.setWorkTimeEnd(DatetimeUtil.addSecond(retailTradeAggregation.getWorkTimeStart(), -1));
        error = retailTradeAggregation.checkCreate(BaseSQLiteBO.INVALID_CASE_ID);
        Assert.assertEquals(error, RetailTradeAggregation.FIELD_ERROR_workTimeStartAndEnd);
        retailTradeAggregation.setWorkTimeStart(new Date());
        retailTradeAggregation.setWorkTimeEnd(retailTradeAggregation.getWorkTimeStart());
        error = retailTradeAggregation.checkCreate(BaseSQLiteBO.INVALID_CASE_ID);
        Assert.assertEquals(error, RetailTradeAggregation.FIELD_ERROR_workTimeStartAndEnd);
        retailTradeAggregation.setWorkTimeStart(new Date());
        retailTradeAggregation.setWorkTimeEnd(DatetimeUtil.addSecond(retailTradeAggregation.getWorkTimeStart(), 1));
        error = retailTradeAggregation.checkCreate(BaseSQLiteBO.INVALID_CASE_ID);
        Assert.assertEquals(error, "");

        // 复原，不影响其他测试
        BaseActivity.retailTradeAggregation = null;

    }

    @Test
    public void test_b_CheckUpdate() {
        Shared.printTestMethodStartInfo();

        RetailTradeAggregation retailTradeAggregation = new RetailTradeAggregation();
        BaseActivity.retailTradeAggregation = new RetailTradeAggregation();
        Date date = new Date();
        retailTradeAggregation.setWorkTimeStart(date);
        BaseActivity.retailTradeAggregation.setWorkTimeStart(date);

        retailTradeAggregation.setWorkTimeEnd(DatetimeUtil.addSecond(retailTradeAggregation.getWorkTimeStart(), 1));
        String error = "";
        //
        retailTradeAggregation.setStaffID(0);
        error = retailTradeAggregation.checkUpdate(BaseSQLiteBO.INVALID_CASE_ID);
        Assert.assertEquals(error, RetailTradeAggregation.FIELD_ERROR_StaffID);
        retailTradeAggregation.setStaffID(-98);
        error = retailTradeAggregation.checkUpdate(BaseSQLiteBO.INVALID_CASE_ID);
        Assert.assertEquals(error, RetailTradeAggregation.FIELD_ERROR_StaffID);
        retailTradeAggregation.setStaffID(1);
        //
        retailTradeAggregation.setPosID(0);
        error = retailTradeAggregation.checkUpdate(BaseSQLiteBO.INVALID_CASE_ID);
        Assert.assertEquals(error, RetailTradeAggregation.FIELD_ERROR_PosID);
        retailTradeAggregation.setPosID(-98);
        error = retailTradeAggregation.checkUpdate(BaseSQLiteBO.INVALID_CASE_ID);
        Assert.assertEquals(error, RetailTradeAggregation.FIELD_ERROR_PosID);
        retailTradeAggregation.setPosID(1);
        //
        retailTradeAggregation.setTradeNO(-1);
        error = retailTradeAggregation.checkUpdate(BaseSQLiteBO.INVALID_CASE_ID);
        Assert.assertEquals(error, RetailTradeAggregation.FIELD_ERROR_TradeNO);
        retailTradeAggregation.setTradeNO(0);
        error = retailTradeAggregation.checkUpdate(BaseSQLiteBO.INVALID_CASE_ID);
        Assert.assertEquals(error, "");
        //
        retailTradeAggregation.setReserveAmount(-10d);
        error = retailTradeAggregation.checkUpdate(BaseSQLiteBO.INVALID_CASE_ID);
        Assert.assertEquals(error, RetailTradeAggregation.FIELD_ERROR_ReserveAmount);
        //
        retailTradeAggregation.setReserveAmount(0);
        error = retailTradeAggregation.checkUpdate(BaseSQLiteBO.INVALID_CASE_ID);
        Assert.assertEquals(error, "");
        //
        retailTradeAggregation.setReserveAmount(10001d);
        error = retailTradeAggregation.checkUpdate(BaseSQLiteBO.INVALID_CASE_ID);
        Assert.assertEquals(error, RetailTradeAggregation.FIELD_ERROR_ReserveAmount);
        //
        retailTradeAggregation.setReserveAmount(9999);
        error = retailTradeAggregation.checkUpdate(BaseSQLiteBO.INVALID_CASE_ID);
        Assert.assertEquals(error, "");
        //
        retailTradeAggregation.setWorkTimeStart(null);
        error = retailTradeAggregation.checkUpdate(BaseSQLiteBO.INVALID_CASE_ID);
        Assert.assertEquals(error, RetailTradeAggregation.FIELD_ERROR_workTimeStart);


        retailTradeAggregation.setWorkTimeStart(new Date());
        retailTradeAggregation.setWorkTimeEnd(DatetimeUtil.addSecond(retailTradeAggregation.getWorkTimeStart(), -1));
        error = retailTradeAggregation.checkUpdate(BaseSQLiteBO.INVALID_CASE_ID);
        Assert.assertEquals(error, RetailTradeAggregation.FIELD_ERROR_workTimeStartAndEnd);
        retailTradeAggregation.setWorkTimeStart(new Date());
        retailTradeAggregation.setWorkTimeEnd(retailTradeAggregation.getWorkTimeStart());
        error = retailTradeAggregation.checkUpdate(BaseSQLiteBO.INVALID_CASE_ID);
        Assert.assertEquals(error, RetailTradeAggregation.FIELD_ERROR_workTimeStartAndEnd);
        retailTradeAggregation.setWorkTimeStart(new Date());
        retailTradeAggregation.setWorkTimeEnd(DatetimeUtil.addSecond(retailTradeAggregation.getWorkTimeStart(), 1));
        error = retailTradeAggregation.checkUpdate(BaseSQLiteBO.INVALID_CASE_ID);
        Assert.assertEquals(error, "");

        // 复原，不影响其他测试
        BaseActivity.retailTradeAggregation = null;
    }
}
