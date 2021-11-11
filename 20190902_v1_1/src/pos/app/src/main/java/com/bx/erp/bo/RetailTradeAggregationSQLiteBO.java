package com.bx.erp.bo;

import android.content.Context;

import com.bx.erp.common.GlobalController;
import com.bx.erp.event.BaseEvent;
import com.bx.erp.event.BaseHttpEvent;
import com.bx.erp.event.UI.BaseSQLiteEvent;
import com.bx.erp.model.BaseModel;
import com.bx.erp.model.ErrorInfo;
import com.bx.erp.model.RetailTradeAggregation;
import com.bx.erp.presenter.RetailTradeAggregationPresenter;
import com.bx.erp.utils.DatetimeUtil;
import com.bx.erp.utils.GeneralUtil;
import com.bx.erp.view.activity.BaseActivity;

import org.apache.log4j.Logger;

import java.util.List;

public class RetailTradeAggregationSQLiteBO extends BaseSQLiteBO {
    private Logger log = Logger.getLogger(this.getClass());
    //checkcraete
    public static final String FIELD_ERROR_TradeNo_CreateInSQLite = "往sqlite插入的收银汇总的零售单数不能为非零";
    public static final String FIELD_ERROR_Amount_CreateInSQLite = "往sqlite插入的收银汇总的营业额不能为非零";
    public static final String FIELD_ERROR_CashAmount_CreateInSQLite = "往sqlite插入的收银汇总的现金支付金额不能为非零";
    public static final String FIELD_ERROR_WeChatAmount_CreateInSQLite = "往sqlite插入的收银汇总的微信支付金额不能为非零";//TODO ...如以后有更多的支付方式，请在此添加类似的限制
    //checkupdate
    public static final String FIELD_ERROR_workTimeStart_Update = "零售单汇总的数据更新时，上班时间不可以被更新";
    public static final String FIELD_ERROR_StaffID_Update = "零售单汇总的数据更新时，StaffID不可以被更新";
    public static final String FIELD_ERROR_PosID_Update = "零售单汇总的数据更新时，PosID不可以被更新";
    public static final String FIELD_ERROR_ReserveAmount_Update = "零售单汇总的数据更新时，准备金金额不可以被更新";
    public static final String FIELD_ERROR_TradeNO_Update = "零售单汇总的数据更新时，销售单数不可以小于未更新的零售单汇总的销售单数";
    public static final String FIELD_ERROR_workTimeEnd_Update = "零售单汇总的数据更新时，下班时间必须大于未更新时的下班时间";

    public RetailTradeAggregationSQLiteBO(Context ctx, BaseSQLiteEvent sEvent, BaseHttpEvent hEvent) {
        context = ctx;
        sqLiteEvent = sEvent;
        httpEvent = hEvent;
    }

    @Override
    public BaseModel createSync(int iUseCaseID, BaseModel bm) {
        if (checkCreate((RetailTradeAggregation) bm, getSqLiteEvent())) {
            return GlobalController.getInstance().getRetailTradeAggregationPresenter().createObjectSync(iUseCaseID, bm);
        } else {
            log.error("向sqlite插入的收银汇总失败：" + getSqLiteEvent().printErrorInfo());
            return null;
        }
    }

    @Override
    public boolean createAsync(int iUseCaseID, BaseModel bm) {
        log.info("正在执行RetailTradeAggregationSQLiteBO的createAsync,bm=" + (bm == null ? null : bm.toString()));

        switch (sqLiteEvent.getEventTypeSQLite()) {
            case ESET_RetailTradeAggregation_CreateAsync:
                if (GlobalController.getInstance().getRetailTradeAggregationPresenter().createObjectAsync(iUseCaseID, bm, sqLiteEvent)) {
                    return true;
                } else {
                    log.info("插入交班汇总表数据失败!");
                }
                break;
            default:
                log.info("未定义的事件!");
                throw new RuntimeException("未定义的事件!");
        }
        return false;
    }

    @Override
    public boolean onResultCreate(BaseModel bm) {
        sqLiteEvent.setEventTypeSQLite(BaseSQLiteEvent.EnumSQLiteEventType.ESET_RetailTradeAggregation_CreateAsync_HttpDone);
        return ((RetailTradeAggregationPresenter) GlobalController.getInstance().getRetailTradeAggregationPresenter()).deleteOldObjectAsync(BaseSQLiteBO.INVALID_CASE_ID, sqLiteEvent.getBaseModel1(), bm, sqLiteEvent);
    }

    @Override
    protected boolean applyServerDataListAsync(List<BaseModel> bmList) {
        return false;
    }

    @Override
    protected boolean applyServerDataAsync(BaseModel bmNew) {
        return false;
    }

    protected boolean checkCreate(final RetailTradeAggregation retailTradeAggregation, final BaseSQLiteEvent event) {
        event.setLastErrorCode(ErrorInfo.EnumErrorCode.EC_BusinessLogicNotDefined);
        if (retailTradeAggregation.getTradeNO() != 0) {
            event.setLastErrorMessage(FIELD_ERROR_TradeNo_CreateInSQLite);
            return false;
        }
        if (retailTradeAggregation.getAmount() != 0) {
            event.setLastErrorMessage(FIELD_ERROR_Amount_CreateInSQLite);
            return false;
        }
        if (retailTradeAggregation.getCashAmount() != 0) {
            event.setLastErrorMessage(FIELD_ERROR_CashAmount_CreateInSQLite);
            return false;
        }
        if (retailTradeAggregation.getWechatAmount() != 0) {
            event.setLastErrorMessage(FIELD_ERROR_WeChatAmount_CreateInSQLite);
            return false;
        }

        event.setLastErrorCode(ErrorInfo.EnumErrorCode.EC_NoError);
        event.setLastErrorMessage("");
        return true;
    }

    /**
     * 更新收银汇总时，某些字段是禁止更新的
     */
    protected boolean checkUpdate(final RetailTradeAggregation oldObj, final RetailTradeAggregation newObj, final BaseSQLiteEvent event) {
        event.setLastErrorCode(ErrorInfo.EnumErrorCode.EC_BusinessLogicNotDefined);
        if (!String.valueOf(newObj.getStaffID()).equals(String.valueOf(oldObj.getStaffID()))) {
            event.setLastErrorMessage(FIELD_ERROR_StaffID_Update);
            return false;
        }
        if (!String.valueOf(newObj.getPosID()).equals(String.valueOf(oldObj.getPosID()))) {
            event.setLastErrorMessage(FIELD_ERROR_PosID_Update);
            return false;
        }
        if (Math.abs(GeneralUtil.sub(newObj.getReserveAmount(), Double.valueOf(oldObj.getReserveAmount()))) > BaseModel.TOLERANCE) {
            event.setLastErrorMessage(FIELD_ERROR_ReserveAmount_Update);
            return false;
        }
        if (newObj.getTradeNO() < oldObj.getTradeNO()) {
            event.setLastErrorMessage(FIELD_ERROR_TradeNO_Update);
            return false;
        }
        if (!DatetimeUtil.compareDate(oldObj.getWorkTimeStart(), newObj.getWorkTimeStart())) {
            event.setLastErrorMessage(FIELD_ERROR_workTimeStart_Update);
            event.setLastErrorCode(ErrorInfo.EnumErrorCode.EC_BusinessLogicNotDefined);
            return false;
        }
        if (newObj.getWorkTimeEnd().before(oldObj.getWorkTimeEnd())) {
            event.setLastErrorMessage(FIELD_ERROR_workTimeEnd_Update);
            return false;
        }

        event.setLastErrorCode(ErrorInfo.EnumErrorCode.EC_NoError);
        event.setLastErrorMessage("");
        return true;
    }

    @Override
    public boolean updateAsync(int iUseCaseID, BaseModel bm) {
        if (!checkUpdate(BaseActivity.retailTradeAggregation, (RetailTradeAggregation) bm, getSqLiteEvent())) { //TODO 将来Giggs会再做重构
            getSqLiteEvent().setStatus(BaseEvent.EnumEventStatus.EES_SQLite_Done);
            return false;
        }

        return GlobalController.getInstance().getRetailTradeAggregationPresenter().updateObjectAsync(iUseCaseID, bm, getSqLiteEvent());
    }

    @Override
    public boolean updateSync(int iUseCaseID, BaseModel bm) {
        if (!checkUpdate(BaseActivity.retailTradeAggregation, (RetailTradeAggregation) bm, getSqLiteEvent())) { //TODO 将来Giggs会再做重构
            getSqLiteEvent().setStatus(BaseEvent.EnumEventStatus.EES_SQLite_Done);
            return false;
        }

        return GlobalController.getInstance().getRetailTradeAggregationPresenter().updateObjectSync(iUseCaseID, bm);
    }

    @Override
    protected boolean applyServerDataUpdateAsync(BaseModel bmNew) {
        return false;
    }

    @Override
    protected boolean applyServerDataUpdateAsyncN(List<?> bmList) {
        return false;
    }

    @Override
    protected boolean applyServerDataDeleteAsync(BaseModel bmDelete) {
        return false;
    }

    @Override
    protected boolean applyServerListDataAsync(List<BaseModel> bmList) {
        return false;
    }

    @Override
    protected boolean applyServerListDataAsyncC(List<BaseModel> bmList) {
        return false;
    }

    @Override
    public boolean deleteAsync(int iUseCaseID, BaseModel bm) {
        return false;
    }

    @Override
    public List<?> retrieveNSync(int iUseCaseID, BaseModel bm) {
        List<?> list = GlobalController.getInstance().getRetailTradeAggregationPresenter().retrieveNObjectSync(iUseCaseID, bm);
        sqLiteEvent.setLastErrorCode(GlobalController.getInstance().getRetailTradePresenter().getLastErrorCode());
        sqLiteEvent.setLastErrorMessage(GlobalController.getInstance().getRetailTradePresenter().getLastErrorMessage());
        return list;
    }
}
