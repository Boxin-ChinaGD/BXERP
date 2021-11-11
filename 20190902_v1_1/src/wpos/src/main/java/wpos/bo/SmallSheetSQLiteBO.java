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
import wpos.presenter.BasePresenter;
import wpos.presenter.SmallSheetFramePresenter;

import javax.annotation.Resource;
import java.util.List;

@Component("smallSheetFrameSQLiteBO") //...
@Scope("prototype")
public class SmallSheetSQLiteBO extends BaseSQLiteBO {
    private Log log = LogFactory.getLog(this.getClass());

    public SmallSheetSQLiteBO(BaseSQLiteEvent sEvent, BaseHttpEvent hEvent) {
        sqLiteEvent = sEvent;
        httpEvent = hEvent;
    }

    public SmallSheetSQLiteBO() {
    }
    
    @Resource
    private SmallSheetFramePresenter smallSheetFramePresenter;

    // 只在测试中使用，功能代码不使用
    public void setSmallSheetFramePresenter(SmallSheetFramePresenter smallSheetFramePresenter) {
        this.smallSheetFramePresenter = smallSheetFramePresenter;
    }

    @Override
    public boolean createAsync(int iUseCaseID, final BaseModel bm) {
        log.info("正在执行SmallSheetSQLiteBO的createAsync，bm=" +(bm == null ? null : bm.toString()));
        if (bm != null) {
            String checkMsg = bm.checkCreate(BaseSQLiteBO.INVALID_CASE_ID);
            if (!"".equals(checkMsg)) {
                log.info("checkCreate失败，错误信息：" + checkMsg);
                sqLiteEvent.setLastErrorCode(ErrorInfo.EnumErrorCode.EC_WrongFormatForInputField);
                sqLiteEvent.setLastErrorMessage(checkMsg);
                return false;
            }
            switch (sqLiteEvent.getEventTypeSQLite()) {
                case ESET_SmallSheet_CreateMasterSlaveAsync_Done:
                    if (smallSheetFramePresenter.createMasterSlaveObjectAsync(iUseCaseID, bm, sqLiteEvent)) {
                        return true;
                    } else {
                        log.info("创建小票格式主从表失败");
                    }
                    break;
                default:
                    throw new RuntimeException("未定义的事件！");
            }
        }
        return false;
    }

    @Override
    protected boolean applyServerDataListAsync(List<BaseModel> bmList) {
        return false;
    }

    @Override
    protected boolean applyServerDataAsync(BaseModel bmNew) {
        sqLiteEvent.setEventTypeSQLite(BaseSQLiteEvent.EnumSQLiteEventType.ESET_SmallSheet_CreateReplacerAsync_Done);
        return smallSheetFramePresenter.createReplacerObjectAsync(BaseSQLiteBO.INVALID_CASE_ID, sqLiteEvent.getTmpMasterTableObj(), bmNew, sqLiteEvent);
    }

    @Override
    protected boolean applyServerDataUpdateAsync(BaseModel bmNew) {
        sqLiteEvent.setEventTypeSQLite(BaseSQLiteEvent.EnumSQLiteEventType.ESET_SmallSheet_UpdateMasterSlaveByServerDataAsync_Done);
        return smallSheetFramePresenter.updateMasterSlaveObjectAsync(BaseSQLiteBO.CASE_SmallSheet_UpdateByServerData, bmNew, sqLiteEvent);
    }

    @Override
    protected boolean applyServerDataUpdateAsyncN(List<?> bmList) {
        return false;
    }

    @Override
    public boolean updateAsync(int iUseCaseID, final BaseModel bm) {
        log.info("正在执行SmallSheetSQLiteBO的updateAsync，bm=" +(bm == null ? null : bm.toString()));

        httpEvent.setEventProcessed(false);
        if (bm != null) {
            String checkMsg = bm.checkUpdate(BaseSQLiteBO.INVALID_CASE_ID);
            if (!"".equals(checkMsg)) {
                log.info("checkUpdate失败，错误信息：" + checkMsg);
                sqLiteEvent.setLastErrorCode(ErrorInfo.EnumErrorCode.EC_WrongFormatForInputField);
                sqLiteEvent.setLastErrorMessage(checkMsg);
                return false;
            }

            switch (sqLiteEvent.getEventTypeSQLite()) {
                case ESET_SmallSheet_UpdateMasterSlaveAsync:
//                sqLiteEvent.setEventTypeSQLite(BaseSQLiteEvent.EnumSQLiteEventType.ESET_SmallSheet_UpdateMasterSlaveAsync_Done);
                    if (smallSheetFramePresenter.updateMasterSlaveObjectAsync(iUseCaseID, bm, sqLiteEvent)) {
                        return true;
                    } else {
                        log.info("修改小票格式主从表失败");
                    }
                    break;
                case ESET_SmallSheet_BeforeDeleteMasterSlaveAsync_Done://用于在删除小票格式之前设置SyncDatetime和SyncType
//                sqLiteEvent.setEventTypeSQLite(BaseSQLiteEvent.EnumSQLiteEventType.ESET_SmallSheet_BeforeDeleteMasterSlaveAsync_Done);
                    if (smallSheetFramePresenter.updateMasterSlaveObjectAsync(iUseCaseID, bm, sqLiteEvent)) {
                        return true;
                    } else {
                        log.info("删除小票格式主从表前设置数据失败");
                    }
                    break;
                default:
                    throw new RuntimeException("未定义的事件！");
            }
        }
        return false;
    }

    @Override
    protected boolean applyServerListDataAsync(List<BaseModel> bmNewList) {
        log.info("正在执行SmallSheetSQLiteBO的applyServerListDataAsync，bmNewList=" + (bmNewList == null ? null : bmNewList.toString()));

        if (bmNewList == null) {
            log.info("需要同步的SmallSheet数据为null");
        }
        sqLiteEvent.setEventTypeSQLite(BaseSQLiteEvent.EnumSQLiteEventType.ESET_SmallSheet_RefreshByServerDataAsync_Done);
        return smallSheetFramePresenter.refreshByServerDataObjectsAsync(BaseSQLiteBO.INVALID_CASE_ID, bmNewList, sqLiteEvent);
    }

    @Override
    protected boolean applyServerListDataAsyncC(List<BaseModel> bmList) {
        log.info("正在执行SmallSheetSQLiteBO的applyServerListDataAsyncC，bmList=" + (bmList == null ? null : bmList.toString()));

        if (bmList == null) {
            log.info("需要同步的SmallSheet数据为null");
        }
        sqLiteEvent.setEventTypeSQLite(BaseSQLiteEvent.EnumSQLiteEventType.ESET_SmallSheet_RefreshByServerDataAsyncC_Done);
        return smallSheetFramePresenter.refreshByServerDataObjectsAsyncC(BaseSQLiteBO.INVALID_CASE_ID, bmList, sqLiteEvent);
    }

    @Override
    public boolean deleteAsync(int iUseCaseID, BaseModel bm) {
        log.info("正在执行SmallSheetSQLiteBO的deleteAsync，bm=" +(bm == null ? null : bm.toString()));

        switch (sqLiteEvent.getEventTypeSQLite()) {
            case ESET_SmallSheet_BeforeDeleteMasterSlaveAsync_Done://用于在删除小票格式之前设置SyncDatetime和SyncType
//                sqLiteEvent.setEventTypeSQLite(BaseSQLiteEvent.EnumSQLiteEventType.ESET_SmallSheet_BeforeDeleteMasterSlaveAsync_Done);
                if (smallSheetFramePresenter.updateMasterSlaveObjectAsync(iUseCaseID, bm, sqLiteEvent)) {
                    return true;
                } else {
                    log.info("删除小票格式主从表前设置数据失败");
                }
                break;
            case ESET_SmallSheet_DeleteMasterSlaveAsync_Done:
//                sqLiteEvent.setEventTypeSQLite(BaseSQLiteEvent.EnumSQLiteEventType.ESET_SmallSheet_DeleteMasterSlaveAsync_Done);
                if (smallSheetFramePresenter.deleteMasterSlaveObjectAsync(iUseCaseID, bm, sqLiteEvent)) {
                    return true;
                } else {
                    log.info("删除小票格式主从表失败");
                }
                break;
            default:
                throw new RuntimeException("未定义的事件!");
        }
        return false;
    }

    @Override
    protected boolean applyServerDataDeleteAsync(BaseModel bm) {
        log.info("正在执行SmallSheetSQLiteBO的applyServerDataDeleteAsync，bm=" +(bm == null ? null : bm.toString()));

        sqLiteEvent.setEventTypeSQLite(BaseSQLiteEvent.EnumSQLiteEventType.ESET_SmallSheet_DeleteMasterSlaveAsync_Done);
        if (smallSheetFramePresenter.deleteMasterSlaveObjectAsync(BaseSQLiteBO.INVALID_CASE_ID, bm, sqLiteEvent)) {
            return true;
        } else {
            log.info("删除本地小票格式失败");
            return false;
        }
    }

    @Override
    public List<?> retrieveNSync(int iUseCaseID, BaseModel bm) {
        return null;
    }

    @Override
    public void createTableSync() {
        smallSheetFramePresenter.createTableSync();
    }

    public boolean createAsyncNoUpload(int iUseCaseID, BaseModel bm) {
        return smallSheetFramePresenter.createObjectAsync(iUseCaseID, bm, sqLiteEvent);
    }

    public boolean updateAsyncNoUpload(int iUseCaseID, BaseModel bm) {
        return smallSheetFramePresenter.updateObjectAsync(iUseCaseID, bm, sqLiteEvent);
    }

    public boolean deleteAsyncNoUpload(int iUseCaseID, BaseModel bm) {
        return smallSheetFramePresenter.deleteObjectAsync(iUseCaseID, bm, sqLiteEvent);
    }
}
