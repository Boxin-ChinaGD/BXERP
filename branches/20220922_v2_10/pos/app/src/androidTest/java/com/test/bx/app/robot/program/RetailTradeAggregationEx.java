package com.test.bx.app.robot.program;


import com.bx.erp.bo.BaseHttpBO;
import com.bx.erp.bo.BaseSQLiteBO;
import com.bx.erp.bo.RetailTradeAggregationHttpBO;
import com.bx.erp.bo.RetailTradeAggregationSQLiteBO;
import com.bx.erp.common.GlobalController;
import com.bx.erp.event.BaseEvent;
import com.bx.erp.event.RetailTradeAggregationHttpEvent;
import com.bx.erp.event.UI.BaseSQLiteEvent;
import com.bx.erp.event.UI.RetailTradeAggregationSQLiteEvent;
import com.bx.erp.helper.Constants;
import com.bx.erp.model.*;
import com.bx.erp.model.RetailTrade;
import com.bx.erp.model.RetailTradeAggregation;
import com.bx.erp.presenter.BasePresenter;
import com.bx.erp.utils.DatetimeUtil;
import com.bx.erp.utils.Shared;
import com.bx.erp.view.activity.BaseActivity;
import com.test.bx.app.robot.RobotEx;
import com.test.bx.app.robot.ShopRobotTest;
import com.test.bx.app.robot.ShopRobotTestEx;

import java.text.ParseException;
import java.util.Calendar;
import java.util.Date;

public class RetailTradeAggregationEx extends ProgramEx {

    private static RetailTradeAggregationSQLiteBO retailTradeAggregationSQLiteBO = null;
    private static RetailTradeAggregationHttpBO retailTradeAggregationHttpBO = null;

    private static RetailTradeAggregationSQLiteEvent retailTradeAggregationSQLiteEvent = null;
    private static RetailTradeAggregationHttpEvent retailTradeAggregationHttpEvent = null;

    public RetailTradeAggregationEx(RetailTradeAggregationSQLiteBO retailTradeAggregationSQLiteBO, RetailTradeAggregationHttpBO retailTradeAggregationHttpBO, RetailTradeAggregationSQLiteEvent retailTradeAggregationSQLiteEvent, RetailTradeAggregationHttpEvent retailTradeAggregationHttpEvent) {
        Calendar calendarStart = Calendar.getInstance();
        calendarStart.setTime(new Date());
        calendarStart.add(Calendar.DATE, ShopRobotTestEx.dayNumber);
        this.retailTradeAggregationSQLiteBO = retailTradeAggregationSQLiteBO;
        this.retailTradeAggregationHttpBO = retailTradeAggregationHttpBO;
        this.retailTradeAggregationSQLiteEvent = retailTradeAggregationSQLiteEvent;
        this.retailTradeAggregationHttpEvent = retailTradeAggregationHttpEvent;
    }


    @Override
    public boolean run(StringBuilder errorInfo) throws InterruptedException, ParseException {
        //将当前时间设为零售汇总的下班时间,上传更新的收银汇总到服务器
        ShopRobotTest.caseLog("交班，上传的收银汇总=" + BaseActivity.retailTradeAggregation);
        com.bx.erp.model.RetailTradeAggregation retailTradeAggregation = new com.bx.erp.model.RetailTradeAggregation();
        int tradeNo = RetailTradeEx.retailTradesCreatedInServer.size();
        double amount = 0.000000d;
        double reserveAmount = 1000.00d; //准备金
        double cashAmount = 0.000000d;
        double wechatAmount = 0.000000d;
        double alipayAmount = 0.000000d;
        for (int i = 0; i < RetailTradeEx.retailTradesCreatedInServer.size(); i++) {
            RetailTrade retailTrade = RetailTradeEx.retailTradesCreatedInServer.get(i);
            amount += retailTrade.getAmount();
            cashAmount += retailTrade.getAmountCash();
            wechatAmount += retailTrade.getAmountWeChat();
            alipayAmount += retailTrade.getAmountAlipay();
        }
        retailTradeAggregation.setID(BaseActivity.retailTradeAggregation.getID());
        retailTradeAggregation.setStaffID(BaseActivity.retailTradeAggregation.getStaffID());
        retailTradeAggregation.setPosID(Constants.posID);
        retailTradeAggregation.setTradeNO(tradeNo);
        retailTradeAggregation.setAmount(amount);
        retailTradeAggregation.setReserveAmount(reserveAmount);
        retailTradeAggregation.setCashAmount(cashAmount);
        retailTradeAggregation.setWechatAmount(wechatAmount);
        retailTradeAggregation.setAlipayAmount(alipayAmount);
        retailTradeAggregation.setWorkTimeStart(RobotEx.workTimeStart); //...
        Calendar calendarEnd = Calendar.getInstance();
        calendarEnd.setTime(new Date());
        calendarEnd.add(Calendar.DATE, ShopRobotTestEx.dayNumber);
        retailTradeAggregation.setWorkTimeEnd(calendarEnd.getTime());
        retailTradeAggregation.setSyncType(BasePresenter.SYNC_Type_C);
        if (!uploadRetailTradeAggregation(retailTradeAggregation)) {
            return false;
        }
        /**
         * 重新初始化用户的会话信息，防止污染下一个上班的收银员的会话信息
         */
        initialization();
        return true;
    }

    private void initialization() {
        RetailTradeEx.retailTradesCreatedInServer.clear();
        BaseActivity.retailTradeAggregation = null;
    }

    private boolean uploadRetailTradeAggregation(com.bx.erp.model.RetailTradeAggregation retailTradeAggregation) {
        ShopRobotTest.caseLog("createRetailTradeAggregationToSQL++++retailTradeAggregation+" + retailTradeAggregation);
        /*上传当前的零售汇总到服务器 TODO 下面几行代码互相有紧密的关系，但是看起来像没有关系 */
        retailTradeAggregationSQLiteBO.getSqLiteEvent().setEventTypeSQLite(BaseSQLiteEvent.EnumSQLiteEventType.ESET_RetailTradeAggregation_CreateAsync);
        retailTradeAggregationSQLiteEvent.setBaseModel1(retailTradeAggregation); //为了在服务器返回对象后，还能知道原先的对象是什么，然后把它从SQLite中删除。目前SQLite不保存收银汇总。BaseModel1保存旧数据，BaseModel2保存新数据
        if (!retailTradeAggregationHttpBO.createAsync(BaseHttpBO.INVALID_CASE_ID, retailTradeAggregation)) {
            ShopRobotTest.caseLog("上传当前的零售汇总到服务器失败, 错误信息：" + retailTradeAggregationHttpBO.getHttpEvent().printErrorInfo());
            return false;
        }
        long lTimeOut = 60;
        while (retailTradeAggregationSQLiteBO.getSqLiteEvent().getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_NoAction && //
                retailTradeAggregationSQLiteBO.getSqLiteEvent().getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_Done && lTimeOut-- > 0) { //
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        if (retailTradeAggregationSQLiteBO.getSqLiteEvent().getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_NoAction && //
                retailTradeAggregationSQLiteBO.getSqLiteEvent().getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_Done) { //
            ShopRobotTest.caseLog("上传收银汇总单失败！原因：超时！");
            return false;
            //...
        }
        if (retailTradeAggregationSQLiteBO.getSqLiteEvent().getLastErrorCode() != ErrorInfo.EnumErrorCode.EC_NoError) {
            ShopRobotTest.caseLog("上传收银汇总单失败！错误码为" + retailTradeAggregationSQLiteBO.getSqLiteEvent().getLastErrorCode());
            return false;
            //...
        }
        return true;
    }

    /**
     * 用户设置ReserveAmount后，创建收银汇总到本地sqlite中
     */
    private void createRetailTradeAggregation() {
        BaseActivity.retailTradeAggregation.setPosID(Constants.posID);
        //准备金被点确定之前，day end 收银汇总 dialog已经弹出来。当dialog消失时，如果不设置work time start为第2天的时间，会导致work time start是前一天的时间。
        //这不符合我们的业务逻辑且在上传时，因为day end dialog结束时已经创建了一个retailTradeAggregation在SQLite中，现在用户点击确定后，又创建了一个retailTradeAggregation。这种情况无法避免但是暂时可以接受。
        //我们规定收银汇总必须是当天的，不可以跨天。
        BaseActivity.retailTradeAggregation.setWorkTimeStart(DatetimeUtil.addSecond(new Date(), 1));
        BaseActivity.retailTradeAggregation.setWorkTimeEnd(DatetimeUtil.addSecond(BaseActivity.retailTradeAggregation.getWorkTimeStart(), 1));
        //
        com.bx.erp.model.RetailTradeAggregation retailTradeAggregation = new RetailTradeAggregation();
        retailTradeAggregation.setStaffID(BaseActivity.retailTradeAggregation.getStaffID());
        retailTradeAggregation.setPosID(BaseActivity.retailTradeAggregation.getPosID());
        retailTradeAggregation.setWorkTimeStart(BaseActivity.retailTradeAggregation.getWorkTimeStart());
        retailTradeAggregation.setReserveAmount(BaseActivity.retailTradeAggregation.getReserveAmount());
        retailTradeAggregation.setWorkTimeEnd(BaseActivity.retailTradeAggregation.getWorkTimeEnd());
        //
        retailTradeAggregationSQLiteBO.getSqLiteEvent().setEventTypeSQLite(BaseSQLiteEvent.EnumSQLiteEventType.ESET_RetailTradeAggregation_CreateAsync);
        if (!retailTradeAggregationSQLiteBO.createAsync(BaseSQLiteBO.INVALID_CASE_ID, retailTradeAggregation)) {
            ShopRobotTest.caseLog("创建零售单汇总失败");
        }
    }
}
