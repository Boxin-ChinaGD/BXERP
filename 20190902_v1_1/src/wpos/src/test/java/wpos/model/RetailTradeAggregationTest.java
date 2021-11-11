package wpos.model;

import org.testng.Assert;
import org.testng.annotations.Test;
import wpos.allController.BaseController;
import wpos.base.BaseTestCase;
import wpos.bo.BaseSQLiteBO;
import wpos.model.RetailTradeAggregation;
import wpos.utils.DatetimeUtil;
import wpos.utils.Shared;

import java.util.Date;

public class RetailTradeAggregationTest extends BaseTestCase {
    @Test
    public void test_a_CheckCreate() throws Exception {

        Shared.printTestMethodStartInfo();

        RetailTradeAggregation retailTradeAggregation = new RetailTradeAggregation();
        BaseController.retailTradeAggregation = new RetailTradeAggregation();
        Date date = new Date();
        retailTradeAggregation.setWorkTimeStart(date);
        BaseController.retailTradeAggregation.setWorkTimeStart(date);

        retailTradeAggregation.setWorkTimeEnd(DatetimeUtil.addSecond(retailTradeAggregation.getWorkTimeStart(), 1));
        BaseController.retailTradeAggregation.setWorkTimeEnd(DatetimeUtil.addSecond(BaseController.retailTradeAggregation.getWorkTimeStart(), 1));
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
        BaseController.retailTradeAggregation = null;

    }

    @Test
    public void test_b_CheckUpdate() {
        Shared.printTestMethodStartInfo();

        RetailTradeAggregation retailTradeAggregation = new RetailTradeAggregation();
        BaseController.retailTradeAggregation = new RetailTradeAggregation();
        Date date = new Date();
        retailTradeAggregation.setWorkTimeStart(date);
        BaseController.retailTradeAggregation.setWorkTimeStart(date);

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
        BaseController.retailTradeAggregation = null;
    }
}
