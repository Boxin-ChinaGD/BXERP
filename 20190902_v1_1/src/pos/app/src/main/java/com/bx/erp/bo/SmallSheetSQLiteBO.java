package com.bx.erp.bo;

import android.content.Context;

import com.bx.erp.common.GlobalController;
import com.bx.erp.event.BaseHttpEvent;
import com.bx.erp.event.UI.BaseSQLiteEvent;
import com.bx.erp.model.BaseModel;
import com.bx.erp.model.ErrorInfo;

import org.apache.log4j.Logger;

import java.util.List;

public class SmallSheetSQLiteBO extends BaseSQLiteBO {
    private Logger log = Logger.getLogger(this.getClass());

    public SmallSheetSQLiteBO(Context ctx, BaseSQLiteEvent sEvent, BaseHttpEvent hEvent) {
        context = ctx;
        sqLiteEvent = sEvent;
        httpEvent = hEvent;
    }

    @Override
    public boolean createAsync(int iUseCaseID, final BaseModel bm) {
        log.info("正在执行SmallSheetSQLiteBO的createAsync，bm=" + (bm == null ? null : bm.toString()));
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
                    if (GlobalController.getInstance().getSmallSheetFramePresenter().createMasterSlaveObjectAsync(iUseCaseID, bm, sqLiteEvent)) {
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
        return GlobalController.getInstance().getSmallSheetFramePresenter().createReplacerObjectAsync(BaseSQLiteBO.INVALID_CASE_ID, sqLiteEvent.getTmpMasterTableObj(), bmNew, sqLiteEvent);
    }

    @Override
    protected boolean applyServerDataUpdateAsync(BaseModel bmNew) {
        sqLiteEvent.setEventTypeSQLite(BaseSQLiteEvent.EnumSQLiteEventType.ESET_SmallSheet_UpdateMasterSlaveByServerDataAsync_Done);
        return GlobalController.getInstance().getSmallSheetFramePresenter().updateMasterSlaveObjectAsync(BaseSQLiteBO.CASE_SmallSheet_UpdateByServerData, bmNew, sqLiteEvent);
    }

    @Override
    protected boolean applyServerDataUpdateAsyncN(List<?> bmList) {
        return false;
    }

    @Override
    public boolean updateAsync(int iUseCaseID, final BaseModel bm) {
        log.info("正在执行SmallSheetSQLiteBO的updateAsync，bm=" + (bm == null ? null : bm.toString()));

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
//                sqLiteEvent.setEventTypeSQLite(BaseSQLiteEvent.EnumSQLiteEventType.ESET_SmallSheet_UpdateMasterSlaveAsync);
                    if (GlobalController.getInstance().getSmallSheetFramePresenter().updateMasterSlaveObjectAsync(iUseCaseID, bm, sqLiteEvent)) {
                        return true;
                    } else {
                        log.info("修改小票格式主从表失败");
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
        return GlobalController.getInstance().getSmallSheetFramePresenter().refreshByServerDataObjectsAsync(BaseSQLiteBO.INVALID_CASE_ID, bmNewList, sqLiteEvent);
    }

    @Override
    protected boolean applyServerListDataAsyncC(List<BaseModel> bmList) {
        log.info("正在执行SmallSheetSQLiteBO的applyServerListDataAsyncC，bmList=" + (bmList == null ? null : bmList.toString()));

        if (bmList == null) {
            log.info("需要同步的SmallSheet数据为null");
        }
        sqLiteEvent.setEventTypeSQLite(BaseSQLiteEvent.EnumSQLiteEventType.ESET_SmallSheet_RefreshByServerDataAsyncC_Done);
        return GlobalController.getInstance().getSmallSheetFramePresenter().refreshByServerDataObjectsAsyncC(BaseSQLiteBO.INVALID_CASE_ID, bmList, sqLiteEvent);
    }

    @Override
    public boolean deleteAsync(int iUseCaseID, BaseModel bm) {
        log.info("正在执行SmallSheetSQLiteBO的deleteAsync，bm=" + (bm == null ? null : bm.toString()));

        switch (sqLiteEvent.getEventTypeSQLite()) {
            case ESET_SmallSheet_BeforeDeleteMasterSlaveAsync_Done://用于在删除小票格式之前设置SyncDatetime和SyncType
//                sqLiteEvent.setEventTypeSQLite(BaseSQLiteEvent.EnumSQLiteEventType.ESET_SmallSheet_BeforeDeleteMasterSlaveAsync_Done);
                if (GlobalController.getInstance().getSmallSheetFramePresenter().updateMasterSlaveObjectAsync(iUseCaseID, bm, sqLiteEvent)) {
                    return true;
                } else {
                    log.info("删除小票格式主从表前设置数据失败");
                }
                break;
            case ESET_SmallSheet_DeleteMasterSlaveAsync_Done:
//                sqLiteEvent.setEventTypeSQLite(BaseSQLiteEvent.EnumSQLiteEventType.ESET_SmallSheet_DeleteMasterSlaveAsync_Done);
                if (GlobalController.getInstance().getSmallSheetFramePresenter().deleteMasterSlaveObjectAsync(iUseCaseID, bm, sqLiteEvent)) {
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
        log.info("正在执行SmallSheetSQLiteBO的applyServerDataDeleteAsync，bm=" + (bm == null ? null : bm.toString()));

        sqLiteEvent.setEventTypeSQLite(BaseSQLiteEvent.EnumSQLiteEventType.ESET_SmallSheet_DeleteMasterSlaveAsync_Done);
        if (GlobalController.getInstance().getSmallSheetFramePresenter().deleteMasterSlaveObjectAsync(BaseSQLiteBO.INVALID_CASE_ID, bm, sqLiteEvent)) {
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

//    public boolean createAsyncNoUpload(int iUseCaseID, BaseModel bm) {
//        return GlobalController.getInstance().getSmallSheetFramePresenter().createObjectAsync(iUseCaseID, bm, sqLiteEvent);
//    }

    public boolean updateAsyncNoUpload(int iUseCaseID, BaseModel bm) {
        return GlobalController.getInstance().getSmallSheetFramePresenter().updateObjectAsync(iUseCaseID, bm, sqLiteEvent);
    }

    public boolean deleteAsyncNoUpload(int iUseCaseID, BaseModel bm) {
        return GlobalController.getInstance().getSmallSheetFramePresenter().deleteObjectAsync(iUseCaseID, bm, sqLiteEvent);
    }
}
