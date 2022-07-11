package com.test.bx.app.robot.program;

import com.bx.erp.bo.BaseSQLiteBO;
import com.bx.erp.bo.RetailTradeAggregationSQLiteBO;
import com.bx.erp.bo.RetailTradeHttpBO;
import com.bx.erp.common.GlobalController;
import com.bx.erp.event.BaseEvent;
import com.bx.erp.event.UI.BaseSQLiteEvent;
import com.bx.erp.event.UI.RetailTradeSQLiteEvent;
import com.bx.erp.helper.Constants;
import com.bx.erp.model.BaseModel;
import com.bx.erp.model.RetailTrade;
import com.bx.erp.presenter.BasePresenter;
import com.bx.erp.utils.Shared;
import com.bx.erp.view.activity.BaseActivity;
import com.test.bx.app.robot.RobotConfig;
import com.test.bx.app.robot.ShopRobotTest;


import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class RetailTradeAggregation extends Program {
    private RetailTradeAggregationSQLiteBO retailTradeAggregationSQLiteBO;
    private RetailTradeSQLiteEvent retailTradeSQLiteEvent;

//    @Subscribe(threadMode = ThreadMode.MAIN)
//    public void onRetailTradeHttpEvent(RetailTradeHttpEvent event) {
//        event.onEvent();
//    }
//
//    @Subscribe(threadMode = ThreadMode.MAIN)
//    public void onRetailTradeSQLiteEvent(RetailTradeSQLiteEvent event){
//        event.onEvent();
//        if (remnantRetailTradeList.size() > 0) {
//            remnantRetailTradeList.remove(0);
//            //
//            if (remnantRetailTradeList.size() > 0) {
//                try {
//                    //继续上传零售单
//                    uploadRetailTrade(remnantRetailTradeList.get(0));
//                } catch (InterruptedException e) {
//                    e.printStackTrace();
//                }
//            } else {
//                isDone = true;
//            }
//        }
//    }

    public RetailTradeAggregation(Date startDatetime, Date endDatetime, final RobotConfig rc, RetailTradeAggregationSQLiteBO retailTradeAggregationSQLiteBO, RetailTradeSQLiteEvent retailTradeSQLiteEvent, boolean bRunInRandomMode) {
        super(startDatetime, endDatetime, rc,bRunInRandomMode);
        this.retailTradeAggregationSQLiteBO = retailTradeAggregationSQLiteBO;
        this.retailTradeSQLiteEvent = retailTradeSQLiteEvent;
        remnantRetailTradeList = new ArrayList<BaseModel>();
        listRT = new ArrayList<com.bx.erp.model.RetailTrade>();
    }

    public void setListRT(List<RetailTrade> listRT) {
        this.listRT = listRT;
    }

    /**
     * 今日生成的零售单
     */
    protected List<com.bx.erp.model.RetailTrade> listRT;

    protected List<BaseModel> remnantRetailTradeList;

    protected Date workTimeStart;

    public void setWorkTimeStart(Date workTimeStart) {
        this.workTimeStart = workTimeStart;
    }

    @Override
    protected void generateReport() {

    }

    @Override
    public boolean run(Date currentDatetime, StringBuilder sbError, Program[] programs, boolean bRunInRandomMode) {
        //创建今日的收银汇总单
        if(!bRunInRandomMode) { // TODO 可能要修改成队列的形式
            if (listRT.size() > 0) {
                com.bx.erp.model.RetailTradeAggregation retailTradeAggregation = new com.bx.erp.model.RetailTradeAggregation();
                int tradeNo = listRT.size();
                double amount = 0.000000d;
                double reserveAmount = 1000.00d; //准备金
                double cashAmount = 0.000000d;
                double wechatAmount = 0.000000d;
                double alipayAmount = 0.000000d;

                for (int i = 0; i < listRT.size(); i++) {
                    RetailTrade retailTrade = listRT.get(i);
                    amount += retailTrade.getAmount();
                    cashAmount += retailTrade.getAmountCash();
                    wechatAmount += retailTrade.getAmountWeChat();
                    alipayAmount += retailTrade.getAmountAlipay();
                }

                retailTradeAggregation.setStaffID(BaseActivity.retailTradeAggregation.getStaffID());
                retailTradeAggregation.setPosID(Constants.posID);
                retailTradeAggregation.setTradeNO(tradeNo);
                retailTradeAggregation.setAmount(amount);
                retailTradeAggregation.setReserveAmount(reserveAmount);
                retailTradeAggregation.setCashAmount(cashAmount);
                retailTradeAggregation.setWechatAmount(wechatAmount);
                retailTradeAggregation.setAlipayAmount(alipayAmount);
                retailTradeAggregation.setWorkTimeStart(workTimeStart); //...
                retailTradeAggregation.setWorkTimeEnd(currentDatetime);
                retailTradeAggregation.setSyncType(BasePresenter.SYNC_Type_C);

                try {
                    createRetailTradeAggregation(retailTradeAggregation);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
        else {
            if (listRT.size() > 0) {
                com.bx.erp.model.RetailTradeAggregation retailTradeAggregation = new com.bx.erp.model.RetailTradeAggregation();
                int tradeNo = listRT.size();
                double amount = 0.000000d;
                double reserveAmount = 1000.00d; //准备金
                double cashAmount = 0.000000d;
                double wechatAmount = 0.000000d;
                double alipayAmount = 0.000000d;

                for (int i = 0; i < listRT.size(); i++) {
                    RetailTrade retailTrade = listRT.get(i);
                    amount += retailTrade.getAmount();
                    cashAmount += retailTrade.getAmountCash();
                    wechatAmount += retailTrade.getAmountWeChat();
                    alipayAmount += retailTrade.getAmountAlipay();
                }

                retailTradeAggregation.setStaffID(BaseActivity.retailTradeAggregation.getStaffID());
                retailTradeAggregation.setPosID(Constants.posID);
                retailTradeAggregation.setTradeNO(tradeNo);
                retailTradeAggregation.setAmount(amount);
                retailTradeAggregation.setReserveAmount(reserveAmount);
                retailTradeAggregation.setCashAmount(cashAmount);
                retailTradeAggregation.setWechatAmount(wechatAmount);
                retailTradeAggregation.setAlipayAmount(alipayAmount);
                retailTradeAggregation.setWorkTimeStart(workTimeStart); //...
                retailTradeAggregation.setWorkTimeEnd(currentDatetime);
                retailTradeAggregation.setSyncType(BasePresenter.SYNC_Type_C);

                try {
                    createRetailTradeAggregation(retailTradeAggregation);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
        return true;
    }

    @Override
    protected boolean canRunNow(Date d) {
        return false;
    }

    @Override
    public void resetForNextDay() {

    }

    protected void createRetailTradeAggregation(com.bx.erp.model.RetailTradeAggregation retailTradeAggregation) throws InterruptedException {
        retailTradeAggregationSQLiteBO.getSqLiteEvent().setEventTypeSQLite(BaseSQLiteEvent.EnumSQLiteEventType.ESET_RetailTradeAggregation_CreateAsync);
//        retailTradeAggregationSQLiteBO.getSqLiteEvent().setString2(Shared.UPLOAD);
        if (!retailTradeAggregationSQLiteBO.createAsync(BaseSQLiteBO.INVALID_CASE_ID, retailTradeAggregation)) {
            throw new RuntimeException("收银汇总本地创建失败!");
        }

        lTimeout = 60;
        while (retailTradeAggregationSQLiteBO.getSqLiteEvent().getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_DoneApplyServerData &&//
                retailTradeAggregationSQLiteBO.getSqLiteEvent().getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_NoAction && lTimeout-- > 0) {
            Thread.sleep(1000);
        }
        if (retailTradeAggregationSQLiteBO.getSqLiteEvent().getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_DoneApplyServerData &&//
                retailTradeAggregationSQLiteBO.getSqLiteEvent().getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_NoAction) {
            throw new RuntimeException("收银汇总上传失败！！原因：超时");
        }
    }
}
