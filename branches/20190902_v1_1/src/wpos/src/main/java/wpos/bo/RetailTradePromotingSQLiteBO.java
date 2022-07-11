package wpos.bo;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import wpos.common.GlobalController;
import wpos.event.BaseHttpEvent;
import wpos.event.UI.BaseSQLiteEvent;
import wpos.model.BaseModel;
import wpos.model.RetailTradePromoting;
import wpos.presenter.BasePresenter;
import wpos.presenter.RetailTradePromotingPresenter;

import javax.annotation.Resource;
import java.util.List;

@Component("retailTradePromotingSQLiteBO")
@Scope("prototype")
public class RetailTradePromotingSQLiteBO extends BaseSQLiteBO {
    private Log log = LogFactory.getLog(this.getClass());

    public RetailTradePromotingSQLiteBO(BaseSQLiteEvent sEvent, BaseHttpEvent hEvent) {
        sqLiteEvent = sEvent;
        httpEvent = hEvent;
    }

    public RetailTradePromotingSQLiteBO() {
    }
    
    @Resource
    private RetailTradePromotingPresenter retailTradePromotingPresenter;

    //此方法在测试代码中使用，不可用于功能代码
    public void setRetailTradePromotingPresenter(RetailTradePromotingPresenter retailTradePromotingPresenter) {
        this.retailTradePromotingPresenter = retailTradePromotingPresenter;
    }

    @Override
    public boolean createAsync(int iUseCaseID, BaseModel bm) {
        log.info("正在执行RetailTradePromotingSQLiteBO的createAsync，bm=" + (bm == null ? null : bm.toString()));

        switch (sqLiteEvent.getEventTypeSQLite()) {
            case ESET_RetailTradePromoting_CreateMasterSlaveAsync_Done:
                if (retailTradePromotingPresenter.createMasterSlaveObjectAsync(iUseCaseID, bm, sqLiteEvent)) {
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
                RetailTradePromoting retailTradePromoting = (RetailTradePromoting) retailTradePromotingPresenter.createObjectSync(iUseCaseID, bm);
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
        return retailTradePromotingPresenter.createReplacerNObjectASync(BaseSQLiteBO.INVALID_CASE_ID, sqLiteEvent.getListMasterTable(), bmList, sqLiteEvent);
    }

    @Override
    protected boolean applyServerDataAsync(BaseModel bmNew) {
        sqLiteEvent.setEventTypeSQLite(BaseSQLiteEvent.EnumSQLiteEventType.ESET_RetailTradePromoting_CreateReplacerAsync_Done);
        return retailTradePromotingPresenter.createReplacerObjectAsync(BaseSQLiteBO.INVALID_CASE_ID, sqLiteEvent.getBaseModel1(), bmNew, sqLiteEvent);
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

    @Override
    public void createTableSync() {
        retailTradePromotingPresenter.createTableSync();
    }

//    public boolean retrieveNByIDsAsync(int iUseCaseID, List<?> bmList) {
//        return retailTradePromotingPresenter.retrieveNByIDs(iUseCaseID, bmList, sqLiteEvent);
//    }

    public boolean retrieveNAsync(int iUseCaseID, BaseModel bm) {
        return retailTradePromotingPresenter.retrieveNObjectAsync(iUseCaseID, bm, sqLiteEvent);
    }

    public boolean updateNAsync(int iUseCaseID, List<?> bmList) {
        return retailTradePromotingPresenter.updateNObjectAsync(BaseSQLiteBO.INVALID_CASE_ID, bmList, sqLiteEvent);
    }
}
