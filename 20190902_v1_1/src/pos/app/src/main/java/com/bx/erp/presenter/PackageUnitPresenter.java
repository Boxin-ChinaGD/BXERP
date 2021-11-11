package com.bx.erp.presenter;

import com.bx.erp.event.BaseEvent;
import com.bx.erp.event.UI.BaseSQLiteEvent;
import com.bx.erp.model.BaseModel;
import com.bx.erp.model.DaoSession;
import com.bx.erp.model.ErrorInfo;
import com.bx.erp.model.PackageUnit;
import com.silencedut.taskscheduler.TaskScheduler;

import org.apache.log4j.Logger;
import org.greenrobot.eventbus.EventBus;

import java.util.List;

import javax.inject.Inject;

import static com.bx.erp.bo.BaseSQLiteBO.CASE_PackageUnit_RetrieveNByConditions;

public class PackageUnitPresenter extends BasePresenter {
    private Logger log = Logger.getLogger(this.getClass());

    @Inject
    public PackageUnitPresenter(DaoSession dao) {
        super(dao);
    }

    @Override
    protected String getTableName() {
        return dao.getPackageUnitDao().getTablename();
    }

    @Override
    protected List<BaseModel> createNSync(int iUseCaseID, final List<?> list) {
        log.info("正在进行PackageUnitPresenter的createNSync，list=" + (list == null ? null : list.toString()));

        switch (iUseCaseID) {
            default:
                try {
                    for (int i = 0; i < list.size(); i++) {
                        PackageUnit packageUnit = (PackageUnit) list.get(i);
                        long id = dao.getPackageUnitDao().insert(packageUnit);
                        ((PackageUnit) list.get(i)).setID(id);
                        lastErrorCode = ErrorInfo.EnumErrorCode.EC_NoError;
                    }
                } catch (Exception e) {
                    log.info("执行createNSync失败，错误信息为" + e.getMessage());
                    e.printStackTrace();
                    lastErrorCode = ErrorInfo.EnumErrorCode.EC_OtherError;
                }
                return (List<BaseModel>) list;
        }
    }

    @Override
    protected BaseModel deleteNSync(int iUseCaseID, BaseModel bm) {
        log.info("正在进行PackageUnitPresenter的deleteNSync，bm=" + bm);

        try {
            dao.getPackageUnitDao().deleteAll();
            lastErrorCode = ErrorInfo.EnumErrorCode.EC_NoError;
        } catch (Exception e) {
            log.info("执行deleteNSync失败，错误信息为" + e.getMessage());
            e.printStackTrace();
            lastErrorCode = ErrorInfo.EnumErrorCode.EC_OtherError;
        }
        return null;
    }

    @Override
    protected BaseModel retrieve1Sync(int iUSeCaseID, BaseModel bm) {
        log.info("正在进行PackageUnitPresenter的retrieve1Sync，bm=" + bm);

        try {
            dao.clear();
            lastErrorCode = ErrorInfo.EnumErrorCode.EC_NoError;

            return dao.getPackageUnitDao().load(bm.getID());
        } catch (Exception e) {
            log.info("执行retrieve1Sync失败，错误信息为" + e.getMessage());
            e.printStackTrace();
            lastErrorCode = ErrorInfo.EnumErrorCode.EC_OtherError;

            return null;
        }
    }

    @Override
    protected List<?> retrieveNSync(int iUSeCaseID, BaseModel bm) {
        log.info("正在进行PackageUnitPresenter的retrieveNSync，bm=" + bm);

        switch (iUSeCaseID) {
            case CASE_PackageUnit_RetrieveNByConditions:
                try {
                    lastErrorCode = ErrorInfo.EnumErrorCode.EC_NoError;

                    return dao.getPackageUnitDao().queryRaw(bm.getSql(), bm.getConditions());
                } catch (Exception e) {
                    log.info("执行retrieveNSync的CASE_PackageUnit_RetrieveNByConditions失败，错误信息为" + e.getMessage());
                    e.printStackTrace();
                    lastErrorCode = ErrorInfo.EnumErrorCode.EC_OtherError;

                    return null;
                }
            default:
                try {
                    lastErrorCode = ErrorInfo.EnumErrorCode.EC_NoError;

                    return dao.getPackageUnitDao().loadAll();
                } catch (Exception e) {
                    log.info("执行retrieveNSync失败，错误信息为" + e.getMessage());
                    e.printStackTrace();
                    lastErrorCode = ErrorInfo.EnumErrorCode.EC_OtherError;

                    return null;
                }
        }
    }

    @Override
    protected boolean refreshByServerDataAsyncC(final int iUseCaseID, final List<?> bmNewList, final BaseSQLiteEvent event) {
        log.info("正在进行PackageUnitPresenter的refreshByServerDataAsyncC，bmNewList=" + (bmNewList == null ? null : bmNewList.toString()));

//        event.setStatus(BaseEvent.EnumEventStatus.EES_SQLite_ToApplyServerData);
        switch (iUseCaseID) {
            default:
                TaskScheduler.execute(new Runnable() {
                    @Override
                    public void run() {
                        log.info("已经得到服务器返回的所有PackageUnit数据，准备进行同步...");

//                        event.setEventTypeSQLite(BaseSQLiteEvent.EnumSQLiteEventType.ESET_PackageUnit_RefreshByServerDataAsync_Done);

                        //删除本地所有的数据
                        deleteNSync(iUseCaseID, null);
                        if (getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError) {
                            //同步所有服务器返回的数据
                            createNSync(iUseCaseID, bmNewList);
                            if (getLastErrorCode() != ErrorInfo.EnumErrorCode.EC_NoError) {
                                event.setLastErrorCode(getLastErrorCode());
                            } else {
                                event.setLastErrorCode(ErrorInfo.EnumErrorCode.EC_NoError);
                            }
                        } else {
                            event.setLastErrorCode(ErrorInfo.EnumErrorCode.EC_OtherError);
                        }
                        //
                        EventBus.getDefault().post(event);
                    }
                });
                break;
        }
        return true;
    }
}
