package wpos.bo;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import wpos.common.GlobalController;
import wpos.event.BaseHttpEvent;
import wpos.event.UI.BaseSQLiteEvent;
import wpos.model.BaseModel;
import wpos.presenter.BasePresenter;
import wpos.presenter.VipPresenter;

import javax.annotation.Resource;
import java.util.List;

import static wpos.event.UI.BaseSQLiteEvent.EnumSQLiteEventType.ESET_Vip_UpdateByServerDataNAsync_Done;

@Component("vipSQLiteBO")
@Scope("prototype")
public class VipSQLiteBO extends BaseSQLiteBO {
    private Log log = LogFactory.getLog(this.getClass());

    public VipSQLiteBO(BaseSQLiteEvent sEvent, BaseHttpEvent hEvent) {
        sqLiteEvent = sEvent;
        httpEvent = hEvent;
    }

    public VipSQLiteBO() {
    }
    
    @Resource
    private VipPresenter vipPresenter;

    // 只在测试中使用，功能代码不使用
    public void setVipPresenter(VipPresenter vipPresenter) {
        this.vipPresenter = vipPresenter;
    }

    @Override
    public boolean createAsync(int iUseCaseID, BaseModel bm) {
        log.info("正在执行VipSQLiteBO的createAsync，bm=" +(bm == null ? null : bm.toString()));

        switch (sqLiteEvent.getEventTypeSQLite()) {
            case ESET_Vip_CreateAsync:
//                sqLiteEvent.setEventTypeSQLite(BaseSQLiteEvent.EnumSQLiteEventType.ESET_Vip_CreateAsync);
                if (vipPresenter.createObjectAsync(BaseSQLiteBO.INVALID_CASE_ID, bm, sqLiteEvent)) {
                    return true;
                } else {
                    log.info("创建Vip失败！");
                }
                break;
            default:
                throw new RuntimeException("未定义的事件！");
        }
        return false;
    }

    @Override
    protected boolean applyServerDataListAsync(List<BaseModel> bmList) {
        return false;
    }

    @Override
    protected boolean applyServerDataAsync(BaseModel bmNew) {
        sqLiteEvent.setEventTypeSQLite(BaseSQLiteEvent.EnumSQLiteEventType.ESET_Vip_CreateReplacerAsync_Done);
        return vipPresenter.createReplacerObjectAsync(BaseSQLiteBO.INVALID_CASE_ID, sqLiteEvent.getBaseModel1(), bmNew, sqLiteEvent);
    }

    @Override
    public boolean updateAsync(int iUseCaseID, BaseModel bm) {
        log.info("正在执行VipSQLiteBO的updateAsync，bm=" +(bm == null ? null : bm.toString()));

        switch (sqLiteEvent.getEventTypeSQLite()) {
            case ESET_Vip_UpdateAsync:
                if (vipPresenter.updateObjectAsync(BaseSQLiteBO.INVALID_CASE_ID, bm, sqLiteEvent)) {
                    return true;
                } else {
                    log.info("修改Vip失败！");
                }
                break;
            case ESET_Vip_BeforeDeleteAsync_Done:
//                sqLiteEvent.setEventTypeSQLite(BaseSQLiteEvent.EnumSQLiteEventType.ESET_Vip_BeforeDeleteAsync_Done);
                if (vipPresenter.updateMasterSlaveObjectAsync(iUseCaseID, bm, sqLiteEvent)) {
                    return true;
                } else {
                    log.info("删除Vip前设置数据失败！");
                }
                break;
            default:
                throw new RuntimeException("未定义的事件！");
        }
        return false;
    }

    @Override
    protected boolean applyServerDataUpdateAsync(BaseModel bmNew) {
//        sqLiteEvent.setEventTypeSQLite(ESET_Vip_UpdateByServerDataAsync_Done);
        return vipPresenter.updateMasterSlaveObjectAsync(BaseSQLiteBO.INVALID_CASE_ID, bmNew, sqLiteEvent);
    }

    @Override
    protected boolean applyServerDataUpdateAsyncN(List<?> bmList) {
        sqLiteEvent.setEventTypeSQLite(ESET_Vip_UpdateByServerDataNAsync_Done);
        return vipPresenter.updateNObjectAsync(BaseSQLiteBO.INVALID_CASE_ID, bmList, sqLiteEvent);
    }

    @Override
    protected boolean applyServerDataDeleteAsync(BaseModel bmDelete) {
        return vipPresenter.deleteObjectAsync(BaseSQLiteBO.INVALID_CASE_ID, bmDelete, sqLiteEvent);
    }

    @Override
    protected boolean applyServerListDataAsync(List<BaseModel> bmList) {
        sqLiteEvent.setEventTypeSQLite(BaseSQLiteEvent.EnumSQLiteEventType.ESET_Vip_RefreshByServerDataAsync_Done);
        return vipPresenter.refreshByServerDataObjectsAsync(BaseSQLiteBO.INVALID_CASE_ID, bmList, sqLiteEvent);
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
        vipPresenter.createTableSync();
    }
}
