package com.bx.erp.bo;

import android.content.Context;

import com.bx.erp.common.GlobalController;
import com.bx.erp.event.BaseHttpEvent;
import com.bx.erp.event.UI.BaseSQLiteEvent;
import com.bx.erp.model.BaseModel;
import com.bx.erp.model.RetailTradePromoting;

import org.apache.log4j.Logger;

import java.util.List;

public class RetailTradePromotingSQLiteBO extends BaseSQLiteBO {
    private Logger log = Logger.getLogger(this.getClass());

    public RetailTradePromotingSQLiteBO(Context ctx, BaseSQLiteEvent sEvent, BaseHttpEvent hEvent) {
        context = ctx;
        sqLiteEvent = sEvent;
        httpEvent = hEvent;
    }

    @Override
    public boolean createAsync(int iUseCaseID, BaseModel bm) {
        log.info("正在执行RetailTradePromotingSQLiteBO的createAsync，bm=" + (bm == null ? null : bm.toString()));

        switch (sqLiteEvent.getEventTypeSQLite()) {
            case ESET_RetailTradePromoting_CreateMasterSlaveAsync_Done:
                if (GlobalController.getInstance().getRetailTradePromotingPresenter().createMasterSlaveObjectAsync(iUseCaseID, bm, sqLiteEvent)) {
                    return true;
                } else {
                    return false;
                }
            default:
                break;
        }
        return false;
    }

    @Override
    public BaseModel createSync(int iUseCaseID, BaseModel bm) {
        log.info("正在执行RetailTradePromotingSQLiteBO的createSync，bm=" + (bm == null ? null : bm.toString()));

        switch (iUseCaseID) {
            case CASE_RetailTradePromoting_CreateMasterSlaveSQLite:
                RetailTradePromoting retailTradePromoting = (RetailTradePromoting) GlobalController.getInstance().getRetailTradePromotingPresenter().createObjectSync(iUseCaseID, bm);
                if (retailTradePromoting != null) {
                    return retailTradePromoting;
                } else {
                    log.info("创建临时计算过程主从表失败");
                }
                break;
            default:
                log.info("未定义的事件！");
                throw new RuntimeException("未定义的事件！");
        }
        return null;
    }

    @Override
    protected boolean applyServerDataListAsync(List<BaseModel> bmList) {
        sqLiteEvent.setEventTypeSQLite(BaseSQLiteEvent.EnumSQLiteEventType.ESET_RetailTradePromoting_CreateReplacerNAsync_Done);
        return GlobalController.getInstance().getRetailTradePromotingPresenter().createReplacerNObjectASync(BaseSQLiteBO.INVALID_CASE_ID, sqLiteEvent.getListMasterTable(), bmList, sqLiteEvent);
    }

    @Override
    protected boolean applyServerDataAsync(BaseModel bmNew) {
        sqLiteEvent.setEventTypeSQLite(BaseSQLiteEvent.EnumSQLiteEventType.ESET_RetailTradePromoting_CreateReplacerAsync_Done);
        return GlobalController.getInstance().getRetailTradePromotingPresenter().createReplacerObjectAsync(BaseSQLiteBO.INVALID_CASE_ID, sqLiteEvent.getBaseModel1(), bmNew, sqLiteEvent);
    }

    @Override
    public boolean updateAsync(int iUseCaseID, BaseModel bm) {
        return false;
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
        return null;
    }

//    public boolean retrieveNByIDsAsync(int iUseCaseID, List<?> bmList) {
//        return GlobalController.getInstance().getRetailTradePromotingPresenter().retrieveNByIDs(iUseCaseID, bmList, sqLiteEvent);
//    }

    public boolean retrieveNAsync(int iUseCaseID, BaseModel bm) {
        return GlobalController.getInstance().getRetailTradePromotingPresenter().retrieveNObjectAsync(iUseCaseID, bm, sqLiteEvent);
    }

    public boolean updateNAsync(int iUseCaseID, List<?> bmList) {
        return GlobalController.getInstance().getRetailTradePromotingPresenter().updateNObjectAsync(BaseSQLiteBO.INVALID_CASE_ID, bmList, sqLiteEvent);
    }
}
