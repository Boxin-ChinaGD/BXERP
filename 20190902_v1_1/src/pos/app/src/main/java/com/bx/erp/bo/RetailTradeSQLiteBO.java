package com.bx.erp.bo;

import android.content.Context;

import com.bx.erp.common.GlobalController;
import com.bx.erp.event.BaseHttpEvent;
import com.bx.erp.event.UI.BaseSQLiteEvent;
import com.bx.erp.model.BaseModel;
import com.bx.erp.model.ErrorInfo;
import com.bx.erp.model.RetailTrade;

import org.apache.log4j.Logger;

import java.util.List;

public class RetailTradeSQLiteBO extends BaseSQLiteBO {
    private Logger log = Logger.getLogger(this.getClass());

    public RetailTradeSQLiteBO(Context ctx, BaseSQLiteEvent sEvent, BaseHttpEvent hEvent) {
        context = ctx;
        sqLiteEvent = sEvent;
        httpEvent = hEvent;
    }

    @Override
    public boolean retrieveNASync(int iUseCaseID, BaseModel bm) {//...编写单元测试
        log.info("正在执行RetailTradeSQLiteBO的retrieveNASync，bm=" + bm);

        switch (sqLiteEvent.getEventTypeSQLite()) {
            case ESET_RetailTrade_RetrieveNAsync:
                //...如果没查到东西，也要标记完成
                if (GlobalController.getInstance().getRetailTradePresenter().retrieveNObjectAsync(iUseCaseID, bm, sqLiteEvent)) {
                    return true;
                } else {
                    log.info("查询本地临时的零售单失败！");
                }
                break;
            default:
                break;
        }
        return false;
    }

    @Override
    public BaseModel createSync(int iUseCaseID, final BaseModel bm) {
        log.info("正在执行RetailTradeSQLiteBO的createSync，bm=" + bm);

        if (bm != null) {
            // 如果临时单为退货单，则将其saleAmountAlipay和saleAmountWeChat设置为源单的相应值，以便临时单的checkCreate()能检查这2个字段，同时微信退款不会出现错误：微信退款的金额不能比零售时微信支付的钱多
            RetailTrade retailTrade = (RetailTrade) bm;
            if (retailTrade.getSourceID() > 0) {
                RetailTrade retailTrade1 = new RetailTrade();
                retailTrade1.setID(Long.valueOf(String.valueOf(retailTrade.getSourceID())));
                RetailTrade retailTrade2 = (RetailTrade) GlobalController.getInstance().getRetailTradePresenter().retrieve1ObjectSync(BaseSQLiteBO.INVALID_CASE_ID, retailTrade1);
                retailTrade.setSaleAmountAlipay(retailTrade2.getAmountAlipay());
                retailTrade.setSaleAmountWeChat(retailTrade2.getAmountWeChat());
            }
            String checkMsg = bm.checkCreate(BaseSQLiteBO.INVALID_CASE_ID);
            if (!"".equals(checkMsg)) {
                System.out.println(checkMsg);
                sqLiteEvent.setLastErrorCode(ErrorInfo.EnumErrorCode.EC_WrongFormatForInputField);
                sqLiteEvent.setLastErrorMessage(checkMsg);
				return null;
            }

            switch (iUseCaseID) {
                case CASE_RetailTrade_CreateMasterSlaveSQLite:
                    RetailTrade rt = (RetailTrade) GlobalController.getInstance().getRetailTradePresenter().createObjectSync(iUseCaseID, bm);
                    if (rt != null) {
                        return rt;
                    } else {
                        log.info("创建临时零售单主从表失败");
                    }
                    break;
                default:
                    log.info("未定义的事件！");
                    throw new RuntimeException("未定义的事件！");
            }
        }
        return null;
    }

    @Override
    public boolean createAsync(int iUseCaseID, final BaseModel bm) {
        log.info("正在执行RetailTradeSQLiteBO的createAsync，bm=" + bm);

        if (bm != null) {
            // 如果临时单为退货单，则将其saleAmountAlipay和saleAmountWeChat设置为源单的相应值，以便临时单的checkCreate()能检查这2个字段，同时微信退款不会出现错误：微信退款的金额不能比零售时微信支付的钱多
            RetailTrade retailTrade = (RetailTrade) bm;
            if (retailTrade.getSourceID() > 0) {
                RetailTrade retailTrade1 = new RetailTrade();
                retailTrade1.setID(Long.valueOf(String.valueOf(retailTrade.getSourceID())));
                RetailTrade retailTrade2 = (RetailTrade) GlobalController.getInstance().getRetailTradePresenter().retrieve1ObjectSync(BaseSQLiteBO.INVALID_CASE_ID, retailTrade1);
                retailTrade.setSaleAmountAlipay(retailTrade2.getAmountAlipay());
                retailTrade.setSaleAmountWeChat(retailTrade2.getAmountWeChat());
            }
            String checkMsg = bm.checkCreate(BaseSQLiteBO.INVALID_CASE_ID);
            if (!"".equals(checkMsg)) {
                System.out.println(checkMsg);
                sqLiteEvent.setLastErrorCode(ErrorInfo.EnumErrorCode.EC_WrongFormatForInputField);
                sqLiteEvent.setLastErrorMessage(checkMsg);
                return false;
            }

            switch (sqLiteEvent.getEventTypeSQLite()) {
                case ESET_RetailTrade_CreateMasterSlaveAsync_Done:
                    if (GlobalController.getInstance().getRetailTradePresenter().createMasterSlaveObjectAsync(iUseCaseID, bm, sqLiteEvent)) {
                        return true;
                    } else {
                        log.info("创建零售单主从表失败");
                    }
                    break;
                case ESET_RetailTrade_CreateAsync:
                    if (GlobalController.getInstance().getRetailTradePresenter().createObjectAsync(iUseCaseID, bm, sqLiteEvent)) {
                        return true;
                    } else {
                        log.info("创建临时零售单主从表失败");
                    }
                    break;
                default:
                    log.info("未定义的事件！");
                    throw new RuntimeException("未定义的事件！");
            }
        }
        return false;
    }

    @Override
    protected boolean applyServerDataListAsync(List<BaseModel> bmList) {
        sqLiteEvent.setEventTypeSQLite(BaseSQLiteEvent.EnumSQLiteEventType.ESET_RetailTrade_CreateNReplacerAsync_Done);
        return GlobalController.getInstance().getRetailTradePresenter().createReplacerNObjectASync(BaseSQLiteBO.INVALID_CASE_ID, sqLiteEvent.getListMasterTable(), bmList, sqLiteEvent);
    }

    @Override
    protected boolean applyServerDataAsync(BaseModel bmNew) {
        sqLiteEvent.setEventTypeSQLite(BaseSQLiteEvent.EnumSQLiteEventType.ESET_RetailTrade_CreateReplacerAsync_Done);
        return GlobalController.getInstance().getRetailTradePresenter().createReplacerObjectAsync(BaseSQLiteBO.INVALID_CASE_ID, sqLiteEvent.getTmpMasterTableObj(), bmNew, sqLiteEvent);
    }

    @Override
    protected boolean applyServerListDataAsync(List<BaseModel> bmNewList) {
        log.info("正在执行RetailTradeSQLiteBO的applyServerListDataAsync，bmNewList=" + (bmNewList == null ? null : bmNewList.toString()));

        if (bmNewList == null) {
            log.info("需要同步的数据为null");
        }
        return GlobalController.getInstance().getRetailTradePresenter().refreshByServerDataObjectsAsync(BaseSQLiteBO.INVALID_CASE_ID, bmNewList, sqLiteEvent);
    }

    @Override
    protected boolean applyServerListDataAsyncC(List<BaseModel> bmList) {
        log.info("正在执行RetailTradeSQLiteBO的applyServerListDataAsyncC，bmList=" + (bmList == null ? null : bmList.toString()));

        if (bmList == null) {
            log.info("需要同步的数据为null");
            return true;
        }
        sqLiteEvent.setEventTypeSQLite(BaseSQLiteEvent.EnumSQLiteEventType.ESET_RetailTrade_RefreshByServerDataAsync_Done);
        return GlobalController.getInstance().getRetailTradePresenter().refreshByServerDataObjectsAsyncC(BaseSQLiteBO.INVALID_CASE_ID, bmList, sqLiteEvent);
    }

    @Override
    public boolean updateAsync(int iUseCaseID, BaseModel bm) {
        log.info("正在执行RetailTradeSQLiteBO的updateAsync，bm=" + (bm == null ? null : bm.toString()));

        switch (sqLiteEvent.getEventTypeSQLite()) {
            case ESET_RetailTrade_UpdateAsync:
                if (GlobalController.getInstance().getRetailTradePresenter().updateObjectAsync(iUseCaseID, bm, sqLiteEvent)) {
                    log.info("更新临时退货零售单成功");
                    return true;
                } else {
                    log.info("更新临时退货零售单失败");
                }
                break;
            default:
                log.info("未定义的事件！");
                throw new RuntimeException("未定义的事件！");
        }

        return false;
    }

    @Override
    protected boolean applyServerDataUpdateAsync(BaseModel bmNew) {
        return false;
    }

    @Override
    protected boolean applyServerDataUpdateAsyncN(List<?> bmList) {
        log.info("正在执行RetailTradeSQLiteBO的applyServerDataUpdateAsyncN，bmList=" + (bmList == null ? null : bmList.toString()));

        if (bmList == null) {
            log.info("查找到的零售单为null");
            return true;
        }
        sqLiteEvent.setEventTypeSQLite(BaseSQLiteEvent.EnumSQLiteEventType.ESET_RetailTrade_RefreshByServerDataAsync_Done);
        return GlobalController.getInstance().getRetailTradePresenter().refreshByServerDataObjectsAsync(BaseSQLiteBO.INVALID_CASE_ID, bmList, sqLiteEvent);
    }

    @Override
    protected boolean applyServerDataDeleteAsync(BaseModel bmDelete) {
        return false;
    }

    @Override
    public boolean deleteAsync(int iUseCaseID, BaseModel bm) {
        return false;
    }

    @Override
    public List<?> retrieveNSync(int iUseCaseID, BaseModel bm) {
        log.info("正在执行RetailTradeSQLiteBO的retrieveNSync，bm=" + (bm == null ? null : bm.toString()));

        if (bm == null) {
            sqLiteEvent.setLastErrorCode(ErrorInfo.EnumErrorCode.EC_OtherError);
            sqLiteEvent.setLastErrorMessage("输入的查询条件为null");
            log.info("输入的查询条件为null");
            return null;
        }

        switch (sqLiteEvent.getEventTypeSQLite()) {
            case ESET_RetailTrade_RetrieveNSync:
                List<?> list = GlobalController.getInstance().getRetailTradePresenter().retrieveNObjectSync(iUseCaseID, bm);
                sqLiteEvent.setLastErrorCode(GlobalController.getInstance().getRetailTradePresenter().getLastErrorCode());
                sqLiteEvent.setLastErrorMessage(GlobalController.getInstance().getRetailTradePresenter().getLastErrorMessage());
                return list;
            default:
                log.info("未定义的事件！");
                throw new RuntimeException("未定义的事件！");
        }
    }

}
