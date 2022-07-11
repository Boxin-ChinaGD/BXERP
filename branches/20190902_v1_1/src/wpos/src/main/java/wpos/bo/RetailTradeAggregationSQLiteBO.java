package wpos.bo;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import wpos.allController.BaseController;
import wpos.common.GlobalController;
import wpos.event.BaseEvent;
import wpos.event.BaseHttpEvent;
import wpos.event.UI.BaseSQLiteEvent;
import wpos.model.BaseModel;
import wpos.model.ErrorInfo;
import wpos.model.RetailTradeAggregation;
import wpos.presenter.BasePresenter;
import wpos.presenter.RetailTradeAggregationPresenter;
import wpos.utils.DatetimeUtil;
import wpos.utils.GeneralUtil;

import javax.annotation.Resource;
import java.util.List;

@Component("retailTradeAggregationSQLiteBO")
@Scope("prototype")
public class RetailTradeAggregationSQLiteBO extends BaseSQLiteBO {
    private Log log = LogFactory.getLog(this.getClass());
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

    public RetailTradeAggregationSQLiteBO(BaseSQLiteEvent sEvent, BaseHttpEvent hEvent) {
        sqLiteEvent = sEvent;
        httpEvent = hEvent;
    }

    public RetailTradeAggregationSQLiteBO() {
    }
    
    @Resource
    private RetailTradeAggregationPresenter retailTradeAggregationPresenter;

    //此方法在测试代码中使用，不可用于功能代码
    public void setRetailTradeAggregationPresenter(RetailTradeAggregationPresenter retailTradeAggregationPresenter) {
        this.retailTradeAggregationPresenter = retailTradeAggregationPresenter;
    }

    @Override
    public BaseModel createSync(int iUseCaseID, BaseModel bm) {
        if (checkCreate((RetailTradeAggregation) bm, getSqLiteEvent())) {
            return retailTradeAggregationPresenter.createObjectSync(iUseCaseID, bm);
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
                if (retailTradeAggregationPresenter.createObjectAsync(iUseCaseID, bm, sqLiteEvent)) {
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
        return ((RetailTradeAggregationPresenter) retailTradeAggregationPresenter).deleteOldObjectAsync(BaseSQLiteBO.INVALID_CASE_ID, sqLiteEvent.getBaseModel1(), bm, sqLiteEvent);
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
        if (!checkUpdate(BaseController.retailTradeAggregation, (RetailTradeAggregation) bm, getSqLiteEvent())) { //TODO 将来Giggs会再做重构
            getSqLiteEvent().setStatus(BaseEvent.EnumEventStatus.EES_SQLite_Done);
            return false;
        }

        return retailTradeAggregationPresenter.updateObjectAsync(iUseCaseID, bm, getSqLiteEvent());
    }

    @Override
    public boolean updateSync(int iUseCaseID, BaseModel bm) {
        if (!checkUpdate(BaseController.retailTradeAggregation, (RetailTradeAggregation) bm, getSqLiteEvent())) { //TODO 将来Giggs会再做重构
            getSqLiteEvent().setStatus(BaseEvent.EnumEventStatus.EES_SQLite_Done);
            return false;
        }

        return retailTradeAggregationPresenter.updateObjectSync(iUseCaseID, bm);
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
        List<?> list = retailTradeAggregationPresenter.retrieveNObjectSync(iUseCaseID, bm);
        return list;
    }

    @Override
    public void createTableSync() {
        retailTradeAggregationPresenter.createTableSync();
    }
}
