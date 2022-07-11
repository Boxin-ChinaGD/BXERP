package wpos.bo;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import wpos.common.GlobalController;
import wpos.event.BaseHttpEvent;
import wpos.event.UI.BaseSQLiteEvent;
import wpos.model.BaseModel;
import wpos.model.ErrorInfo;
import wpos.model.RetailTrade;
import wpos.presenter.BasePresenter;
import wpos.presenter.RetailTradePresenter;

import javax.annotation.Resource;
import java.util.List;

@Component("retailTradeSQLiteBO")
@Scope("prototype")
public class RetailTradeSQLiteBO extends BaseSQLiteBO {
    private Log log = LogFactory.getLog(this.getClass());

    public RetailTradeSQLiteBO(BaseSQLiteEvent sEvent, BaseHttpEvent hEvent) {
        sqLiteEvent = sEvent;
        httpEvent = hEvent;
    }

    public RetailTradeSQLiteBO() {
    }
    
    @Resource
    private RetailTradePresenter retailTradePresenter;

    //此方法在测试代码中使用，不可用于功能代码
    public void setRetailTradePresenter(RetailTradePresenter retailTradePresenter) {
        this.retailTradePresenter = retailTradePresenter;
    }

    @Override
    public boolean retrieveNASync(int iUseCaseID, BaseModel bm) {//...编写单元测试
        log.info("正在执行RetailTradeSQLiteBO的retrieveNASync，bm=" + bm);

        switch (sqLiteEvent.getEventTypeSQLite()) {
            case ESET_RetailTrade_RetrieveNAsync:
                //...如果没查到东西，也要标记完成
                if (retailTradePresenter.retrieveNObjectAsync(iUseCaseID, bm, sqLiteEvent)) {
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
    public void createTableSync() {
        retailTradePresenter.createTableSync();
    }

    @Override
    public BaseModel createSync(int iUseCaseID, final BaseModel bm) {
        log.info("正在执行RetailTradeSQLiteBO的createSync，bm=" + bm);

        if (bm != null) {
            // 如果临时单为退货单，则将其saleAmountAlipay和saleAmountWeChat设置为源单的相应值，以便临时单的checkCreate()能检查这2个字段，同时微信退款不会出现错误：微信退款的金额不能比零售时微信支付的钱多
            RetailTrade retailTrade = (RetailTrade) bm;
            if (retailTrade.getSourceID() > 0) {
                RetailTrade retailTrade1 = new RetailTrade();
                retailTrade1.setID(Integer.valueOf(retailTrade.getSourceID()));
                RetailTrade retailTrade2 = (RetailTrade) retailTradePresenter.retrieve1ObjectSync(BaseSQLiteBO.INVALID_CASE_ID, retailTrade1);
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
                    RetailTrade rt = (RetailTrade) retailTradePresenter.createObjectSync(iUseCaseID, bm);
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
                retailTrade1.setID(Integer.valueOf(retailTrade.getSourceID()));
                RetailTrade retailTrade2 = (RetailTrade) retailTradePresenter.retrieve1ObjectSync(BaseSQLiteBO.INVALID_CASE_ID, retailTrade1);
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
                    if (retailTradePresenter.createMasterSlaveObjectAsync(iUseCaseID, bm, sqLiteEvent)) {
                        return true;
                    } else {
                        log.info("创建零售单主从表失败");
                    }
                    break;
                case ESET_RetailTrade_CreateAsync:
                    if (retailTradePresenter.createObjectAsync(iUseCaseID, bm, sqLiteEvent)) {
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
        return retailTradePresenter.createReplacerNObjectASync(BaseSQLiteBO.INVALID_CASE_ID, sqLiteEvent.getListMasterTable(), bmList, sqLiteEvent);
    }

    @Override
    protected boolean applyServerDataAsync(BaseModel bmNew) {
        sqLiteEvent.setEventTypeSQLite(BaseSQLiteEvent.EnumSQLiteEventType.ESET_RetailTrade_CreateReplacerAsync_Done);
        return retailTradePresenter.createReplacerObjectAsync(BaseSQLiteBO.INVALID_CASE_ID, sqLiteEvent.getTmpMasterTableObj(), bmNew, sqLiteEvent);
    }

    @Override
    protected boolean applyServerListDataAsync(List<BaseModel> bmNewList) {
        log.info("正在执行RetailTradeSQLiteBO的applyServerListDataAsync，bmNewList=" + (bmNewList == null ? null : bmNewList.toString()));

        if (bmNewList == null) {
            log.info("需要同步的数据为null");
        }
        return retailTradePresenter.refreshByServerDataObjectsAsync(BaseSQLiteBO.INVALID_CASE_ID, bmNewList, sqLiteEvent);
    }

    @Override
    protected boolean applyServerListDataAsyncC(List<BaseModel> bmList) {
        log.info("正在执行RetailTradeSQLiteBO的applyServerListDataAsyncC，bmList=" + (bmList == null ? null : bmList.toString()));

        if (bmList == null) {
            log.info("需要同步的数据为null");
            return true;
        }
        sqLiteEvent.setEventTypeSQLite(BaseSQLiteEvent.EnumSQLiteEventType.ESET_RetailTrade_RefreshByServerDataAsync_Done);
        return retailTradePresenter.refreshByServerDataObjectsAsyncC(BaseSQLiteBO.INVALID_CASE_ID, bmList, sqLiteEvent);
    }

    @Override
    public boolean updateAsync(int iUseCaseID, BaseModel bm) {
        log.info("正在执行RetailTradeSQLiteBO的updateAsync，bm=" + (bm == null ? null : bm.toString()));

        switch (sqLiteEvent.getEventTypeSQLite()) {
            case ESET_RetailTrade_UpdateAsync:
                if (retailTradePresenter.updateObjectAsync(iUseCaseID, bm, sqLiteEvent)) {
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
        return retailTradePresenter.refreshByServerDataObjectsAsync(BaseSQLiteBO.INVALID_CASE_ID, bmList, sqLiteEvent);
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
                List<?> list = retailTradePresenter.retrieveNObjectSync(iUseCaseID, bm);
                sqLiteEvent.setLastErrorCode(retailTradePresenter.getLastErrorCode());
                sqLiteEvent.setLastErrorMessage(retailTradePresenter.getLastErrorMessage());
                return list;
            default:
                log.info("未定义的事件！");
                throw new RuntimeException("未定义的事件！");
        }
    }

}
