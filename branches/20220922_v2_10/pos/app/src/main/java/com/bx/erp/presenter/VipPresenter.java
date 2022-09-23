package com.bx.erp.presenter;

import android.util.Log;

import com.bx.erp.bo.BaseSQLiteBO;
import com.bx.erp.di.PerActivity;
import com.bx.erp.event.BaseEvent;
import com.bx.erp.event.UI.BaseSQLiteEvent;
import com.bx.erp.helper.Constants;
import com.bx.erp.model.BaseModel;
import com.bx.erp.model.DaoSession;
import com.bx.erp.model.ErrorInfo;
import com.bx.erp.model.Vip;
import com.silencedut.taskscheduler.TaskScheduler;

import org.apache.log4j.Logger;
import org.greenrobot.eventbus.EventBus;

import java.util.List;

@PerActivity
public class VipPresenter extends BasePresenter {
    private Logger log = Logger.getLogger(this.getClass());

    public VipPresenter(DaoSession dao) {
        super(dao);
    }

    @Override
    protected String getTableName() {
        return dao.getVipDao().getTablename();
    }

    @Override
    protected List<BaseModel> createNSync(int iUseCaseID, final List<?> list) {
        log.info("正在进行VipPresenter的createNSync，list=" + (list == null ? null : list.toString()));

        switch (iUseCaseID) {
            default:
                try {
                    for (int i = 0; i < list.size(); i++) {
                        long id = dao.getVipDao().insert((Vip) list.get(i));
                        ((Vip) list.get(i)).setID(id);
                        lastErrorCode = ErrorInfo.EnumErrorCode.EC_NoError;
                    }
                } catch (Exception e) {
                    log.info("执行createNSync时出错，错误信息：" + e.getMessage());
                    e.printStackTrace();
                    lastErrorCode = ErrorInfo.EnumErrorCode.EC_OtherError;
                }
                return (List<BaseModel>) list;
        }
    }

    @Override
    protected BaseModel createSync(int iUseCaseID, BaseModel bm) {
        log.info("正在进行VipPresenter的createSync，bm=" + (bm == null ? null : bm.toString()));

        try {
            long id = dao.getVipDao().insert((Vip) bm);
            if (bm.getSyncDatetime() == null) {
                bm.setSyncDatetime(Constants.getDefaultSyncDatetime2());
            }
            bm.setID(id);
            lastErrorCode = ErrorInfo.EnumErrorCode.EC_NoError;
        } catch (Exception e) {
            log.info("执行createSync时出错，错误信息：" + e.getMessage());
            e.printStackTrace();
            lastErrorCode = ErrorInfo.EnumErrorCode.EC_OtherError;
        }

        return bm;
    }

    @Override
    protected boolean updateSync(int iUseCaseID, BaseModel bm) {
        log.info("正在进行VipPresenter的updateSync，bm=" + (bm == null ? null : bm.toString()));

        try {
            dao.getVipDao().update((Vip) bm);
            lastErrorCode = ErrorInfo.EnumErrorCode.EC_NoError;

            return true;
        } catch (Exception e) {
            log.info("执行updateSync时出错，错误信息：" + e.getMessage());
            e.printStackTrace();
            lastErrorCode = ErrorInfo.EnumErrorCode.EC_OtherError;
            return false;
        }
    }

    @Override
    protected BaseModel retrieve1Sync(int iUseCaseID, BaseModel bm) {
        log.info("正在进行VipPresenter的retrieve1Sync，bm=" + (bm == null ? null : bm.toString()));

        try {
            lastErrorCode = ErrorInfo.EnumErrorCode.EC_NoError;

            return dao.getVipDao().load(bm.getID());
        } catch (Exception e) {
            log.info("执行retrieve1Sync时出错，错误信息：" + e.getMessage());
            e.printStackTrace();
            lastErrorCode = ErrorInfo.EnumErrorCode.EC_OtherError;

            return null;
        }
    }

    @Override
    protected List<?> retrieveNSync(int iUseCaseID, BaseModel bm) {
        log.info("正在进行VipPresenter的retrieveNSync，bm=" + (bm == null ? null : bm.toString()));

        switch (iUseCaseID) {
            case BaseSQLiteBO.CASE_Vip_RetrieveNByConditions:
                try {
                    lastErrorCode = ErrorInfo.EnumErrorCode.EC_NoError;

                    return dao.getVipDao().queryRaw(bm.getSql(), bm.getConditions());
                } catch (Exception e) {
                    log.info("执行retrieveNSync时出错，错误信息：" + e.getMessage());
                    e.printStackTrace();
                    lastErrorCode = ErrorInfo.EnumErrorCode.EC_OtherError;

                    return null;
                }
            default:
                try {
                    lastErrorCode = ErrorInfo.EnumErrorCode.EC_NoError;

                    return dao.getVipDao().loadAll();
                } catch (Exception e) {
                    log.info("执行retrieveNSync时出错，错误信息：" + e.getMessage());
                    e.printStackTrace();
                    lastErrorCode = ErrorInfo.EnumErrorCode.EC_OtherError;

                    return null;
                }
        }
    }

    @Override
    protected BaseModel deleteSync(int iUseCaseID, BaseModel bm) {
        log.info("正在进行VipPresenter的deleteSync，bm=" + (bm == null ? null : bm.toString()));

        try {
            dao.getVipDao().deleteByKey(bm.getID());
            lastErrorCode = ErrorInfo.EnumErrorCode.EC_NoError;
        } catch (Exception e) {
            log.info("执行deleteSync时出错，错误信息：" + e.getMessage());
            e.printStackTrace();
            lastErrorCode = ErrorInfo.EnumErrorCode.EC_OtherError;
        }

        return null;
    }

    @Override
    protected boolean createAsync(int iUseCaseID, final BaseModel bm, final BaseSQLiteEvent event) {
        log.info("正在进行VipPresenter的createAsync，bm=" + (bm == null ? null : bm.toString()));

        event.setStatus(BaseEvent.EnumEventStatus.EES_SQLite_ToDo);
        switch (iUseCaseID) {
            default:
                TaskScheduler.execute(new Runnable() {
                    @Override
                    public void run() {
//                        event.setEventTypeSQLite(BaseSQLiteEvent.EnumSQLiteEventType.ESET_Vip_CreateAsync);
                        //
                        try {
                            Vip vip = (Vip) bm;
                            vip.setSyncDatetime(Constants.getDefaultSyncDatetime2());
                            vip.setSyncType(BasePresenter.SYNC_Type_C);
                            long id = dao.getVipDao().insert((Vip) bm);
                            ((Vip) bm).setID(id);
                            event.setLastErrorCode(ErrorInfo.EnumErrorCode.EC_NoError);
                        } catch (Exception e) {
                            Log.e("输出：", "创建Vip时出错，错误信息：" + e.getMessage());
                            event.setLastErrorCode(ErrorInfo.EnumErrorCode.EC_OtherError);
                        }
                        //
                        event.setBaseModel1(bm);
                        //
                        EventBus.getDefault().post(event);
                    }
                });
                break;
        }

        return true;
    }

    @Override
    protected boolean updateAsync(int iUseCaseID, final BaseModel bm, final BaseSQLiteEvent event) {
        log.info("正在进行VipPresenter的updateAsync，bm=" + (bm == null ? null : bm.toString()));

        event.setStatus(BaseEvent.EnumEventStatus.EES_SQLite_ToDo);
        switch (iUseCaseID) {
            default:
                TaskScheduler.execute(new Runnable() {
                    @Override
                    public void run() {
//                        event.setEventTypeSQLite(BaseSQLiteEvent.EnumSQLiteEventType.ESET_Vip_UpdateAsync);
                        //
                        try {
                            Vip vip = (Vip) bm;
                            vip.setSyncDatetime(Constants.getDefaultSyncDatetime2());
                            vip.setSyncType(BasePresenter.SYNC_Type_U);
                            dao.getVipDao().update((Vip) bm);
                            event.setLastErrorCode(ErrorInfo.EnumErrorCode.EC_NoError);
                        } catch (Exception e) {
                            Log.e("输出：", "更新Vip时出错，错误信息：" + e.getMessage());
                            event.setLastErrorCode(ErrorInfo.EnumErrorCode.EC_OtherError);
                        }
                        //
                        event.setBaseModel1(bm);
                        //
                        EventBus.getDefault().post(event);
                    }
                });
                break;
        }

        return true;
    }

    @Override
    protected boolean retrieveNAsync(int iUseCaseID, final BaseModel bm, final BaseSQLiteEvent event) {
        log.info("正在进行VipPresenter的retrieveNAsync，bm=" + (bm == null ? null : bm.toString()));

        event.setStatus(BaseEvent.EnumEventStatus.EES_SQLite_ToDo);
        switch (iUseCaseID) {
            case BaseSQLiteBO.CASE_Vip_RetrieveNByConditions:
                TaskScheduler.execute(new Runnable() {
                    @Override
                    public void run() {
                        List<Vip> result = null;
                        try {
                            result = dao.getVipDao().queryRaw(bm.getSql(), bm.getConditions());
                            event.setLastErrorCode(ErrorInfo.EnumErrorCode.EC_NoError);
                        } catch (Exception e) {
                            Log.e("输出：", "RetrieveNAsync Vip出错，错误信息：" + e.getMessage());
                            event.setLastErrorCode(ErrorInfo.EnumErrorCode.EC_OtherError);
                        }
                        //
                        event.setListMasterTable(result);
                        //
                        EventBus.getDefault().post(event);
                    }
                });
                break;
            default:
                TaskScheduler.execute(new Runnable() {
                    @Override
                    public void run() {
                        List<Vip> result = null;
                        //                        event.setID(new Date().getTime());
//                        event.setEventTypeSQLite(BaseSQLiteEvent.EnumSQLiteEventType.ESET_Vip_RetrieveNAsync);
                        //
                        try {
                            Thread.sleep(1); //令event.setID()的ID惟一
                            result = dao.getVipDao().loadAll();//...
                            event.setLastErrorCode(ErrorInfo.EnumErrorCode.EC_NoError);
                        } catch (Exception e) {
                            Log.e("输出：", "查询SQLite的表Vip的所有记录时出错，错误信息：" + e.getMessage());
                            event.setLastErrorCode(ErrorInfo.EnumErrorCode.EC_OtherError);
                        }
                        //
                        event.setListMasterTable(result);
                        //
                        EventBus.getDefault().post(event);
                    }
                });
                break;
        }

        return true;
    }

    @Override
    protected boolean deleteAsync(int iUseCaseID, final BaseModel bm, final BaseSQLiteEvent event) {
        log.info("正在进行VipPresenter的deleteAsync，bm=" + (bm == null ? null : bm.toString()));

        switch (iUseCaseID) {
            default:
                TaskScheduler.execute(new Runnable() {
                    @Override
                    public void run() {
//                        event.setEventTypeSQLite(BaseSQLiteEvent.EnumSQLiteEventType.ESET_Vip_DeleteAsync);
                        //
                        if (bm != null) {
                            try {
                                dao.getVipDao().delete((Vip) bm);
                                event.setLastErrorCode(ErrorInfo.EnumErrorCode.EC_NoError);
                                log.info("删除本地Vip数据成功，ID = " + bm.getID());
                                event.setBaseModel1(bm);
                            } catch (Exception e) {
                                Log.e("输出：", "删除Vip时出错，错误信息：" + e.getMessage());
                                event.setLastErrorCode(ErrorInfo.EnumErrorCode.EC_OtherError);
                                log.info("删除本地Vip数据失败，原因：" + e.getMessage());
                            }
                        }
                        //
                        event.setStatus(BaseEvent.EnumEventStatus.EES_SQLite_DoneApplyServerData);
                        event.setBaseModel1(bm);
                        //
                        EventBus.getDefault().post(event);
                    }
                });
                break;
        }

        return true;
    }

    @Override
    protected boolean retrieve1Async(int iUseCaseID, final BaseModel bm, final BaseSQLiteEvent event) {
        log.info("正在进行VipPresenter的retrieve1Async，bm=" + (bm == null ? null : bm.toString()));

        switch (iUseCaseID) {
            default:
                TaskScheduler.execute(new Runnable() {
                    @Override
                    public void run() {
                        Vip result = null;
//                        event.setEventTypeSQLite(BaseSQLiteEvent.EnumSQLiteEventType.ESET_Vip_Retrieve1Async);
                        //
                        try {
                            result = dao.getVipDao().load(((Vip) bm).getID());//...
                            event.setLastErrorCode(ErrorInfo.EnumErrorCode.EC_NoError);
                        } catch (Exception e) {
                            Log.e("输出：", "Retrieve1 Vip时出错，错误信息：" + e.getMessage());
                            event.setLastErrorCode(ErrorInfo.EnumErrorCode.EC_OtherError);
                        }
                        //
                        event.setBaseModel1(result);
                        //
                        EventBus.getDefault().post(event);
                    }
                });
                break;
        }

        return true;
    }

    @Override
    protected boolean createNAsync(int iUseCaseID, final List<?> list, final BaseSQLiteEvent event) {
        log.info("正在进行VipPresenter的createNAsync，list=" + (list == null ? null : list.toString()));

        switch (iUseCaseID) {
            default:
                TaskScheduler.execute(new Runnable() {
                    @Override
                    public void run() {
//                        event.setEventTypeSQLite(BaseSQLiteEvent.EnumSQLiteEventType.ESET_Vip_CreateNAsync);
                        //
                        try {
                            for (int i = 0; i < list.size(); i++) {
                                long id = dao.getVipDao().insert((Vip) list.get(i));//...插入一部分失败
                                ((Vip) list.get(i)).setID(id);
                            }
                            event.setLastErrorCode(ErrorInfo.EnumErrorCode.EC_NoError);
                        } catch (Exception e) {
                            log.info("创建Commodity时出错，错误信息：" + e.getMessage());
                            event.setLastErrorCode(ErrorInfo.EnumErrorCode.EC_OtherError);
                            log.info("创建Commodity时出错2，错误信息：" + event.getLastErrorCode());
                        }
                        //
                        event.setListMasterTable(list);
                        //
                        EventBus.getDefault().post(event);
                    }
                });
                break;
        }

        return true;
    }

    @Override
    protected boolean createReplacerAsync(final int iUseCaseID, final BaseModel bmOld, final BaseModel bmNew, final BaseSQLiteEvent event) {
        log.info("正在进行VipPresenter的createReplacerAsync，bmNew=" + (bmNew == null ? null : bmNew.toString())//
                + ",bmOld=" + (bmOld == null ? null : bmOld.toString()));

        event.setStatus(BaseEvent.EnumEventStatus.EES_SQLite_ToApplyServerData);

        switch (iUseCaseID) {
            default:
                TaskScheduler.execute(new Runnable() {
                    @Override
                    public void run() {
                        log.info("已经得到服务器返回的Vip数据，准备删除本地的旧数据，插入服务器返回的数据！");

                        try {
                            deleteSync(iUseCaseID, bmOld);
                            createSync(iUseCaseID, bmNew);
                            //
                            event.setLastErrorCode(ErrorInfo.EnumErrorCode.EC_NoError);
                            event.setBaseModel1(bmNew);

                            log.info("插入服务器返回的数据成功！ID=" + bmNew.getID());
                        } catch (Exception e) {
                            log.info("createReplacerAsync()异常：" + e.getMessage());
                            e.printStackTrace();
                            event.setLastErrorCode(ErrorInfo.EnumErrorCode.EC_OtherError);
                        }
                        event.setStatus(BaseEvent.EnumEventStatus.EES_SQLite_DoneApplyServerData);
                        event.setEventTypeSQLite(BaseSQLiteEvent.EnumSQLiteEventType.ESET_Vip_CreateReplacerAsync_Done);
                        //event.setData("CREATE_DONE");
                        //
                        EventBus.getDefault().post(event);
                    }
                });
                break;
        }
        return true;
    }

    @Override
    protected boolean updateMasterSlaveAsync(final int iUseCaseID, final BaseModel bmMaster, final BaseSQLiteEvent event) {
        log.info("正在进行VipPresenter的updateMasterSlaveAsync，bmMaster=" + (bmMaster == null ? null : bmMaster.toString()));

        event.setStatus(BaseEvent.EnumEventStatus.EES_SQLite_ToApplyServerData);
        switch (event.getEventTypeSQLite()) {
            case ESET_Vip_UpdateByServerDataAsync_Done:
                log.info("修改本地Vip数据...");
                event.setEventTypeSQLite(BaseSQLiteEvent.EnumSQLiteEventType.ESET_Vip_UpdateByServerDataAsync_Done);
                TaskScheduler.execute(new Runnable() {
                    @Override
                    public void run() {
                        event.setSyncType(BasePresenter.SYNC_Type_U);
                        try {
                            updateSync(iUseCaseID, bmMaster);
                            event.setLastErrorCode(ErrorInfo.EnumErrorCode.EC_NoError);
                            event.setBaseModel1(bmMaster);
                            log.info("修改Vip成功，VIP：" + bmMaster.toString());
                            //event.setData("UPDATE_DONE");
                        } catch (Exception e) {
                            e.printStackTrace();
                            event.setLastErrorCode(ErrorInfo.EnumErrorCode.EC_OtherError);
                            log.info("向本地SQLite修改Vip数据失败！原因： " + e.getMessage());
                        }
                        event.setStatus(BaseEvent.EnumEventStatus.EES_SQLite_DoneApplyServerData);
                        //
                        EventBus.getDefault().post(event);
                    }
                });
                break;
            case ESET_Vip_BeforeDeleteAsync_Done:
                //在删除Vip之前设置SyncType和SyncDatetime
                try {
                    bmMaster.setSyncDatetime(Constants.getDefaultSyncDatetime2());
                    bmMaster.setSyncType("D");
                    event.setLastErrorCode(ErrorInfo.EnumErrorCode.EC_NoError);
                    event.setBaseModel1(bmMaster);
                } catch (Exception e) {
                    e.printStackTrace();
                    log.info("删除Vip之前设置SyncType和SyncDatetime出错，错误信息：" + e.getMessage());
                    event.setLastErrorCode(ErrorInfo.EnumErrorCode.EC_OtherError);
                }
                event.setStatus(BaseEvent.EnumEventStatus.EES_SQLite_Done);
                //
                EventBus.getDefault().post(event);
                break;
            default:
                log.info("未定义的事件！");
                throw new RuntimeException("未定义的事件！");
        }

        return true;
    }

    @Override
    protected boolean refreshByServerDataAsync(final int iUseCaseID, final List<?> bmNewList, final BaseSQLiteEvent event) {
        log.info("正在进行VipPresenter的refreshByServerDataAsync，bmNewList=" + (bmNewList == null ? null : bmNewList.toString()));

        event.setStatus(BaseEvent.EnumEventStatus.EES_SQLite_ToApplyServerData);
        switch (iUseCaseID) {
            default:
                TaskScheduler.execute(new Runnable() {
                    @Override
                    public void run() {
                        log.info("已经得到服务器返回的需要同步到本地SQLite的所有数据，准备进行同步...");
                        List<BaseModel> bmList = (List<BaseModel>) bmNewList;
//                        event.setEventTypeSQLite(BaseSQLiteEvent.EnumSQLiteEventType.ESET_Vip_RefreshByServerDataAsync_Done);
                        try {
                            if (bmList != null) {
                                for (int i = 0; i < bmList.size(); i++) {
                                    String syncType = bmList.get(i).getSyncType();

                                    if (BasePresenter.SYNC_Type_C.equals(syncType)) {
                                        Vip vip = (Vip) retrieve1Sync(BaseSQLiteBO.INVALID_CASE_ID, bmList.get(i));
                                        if (vip != null) {
                                            deleteSync(iUseCaseID, vip);
                                        }
                                        //
                                        createSync(iUseCaseID, bmList.get(i));

                                        log.info("服务器返回的C型Vip数据成功同步到本地SQLite中!");
                                    } else if (BasePresenter.SYNC_Type_U.equals(syncType)) {
                                        updateSync(iUseCaseID, bmList.get(i));

                                        log.info("服务器返回的U型Vip数据成功同步到本地SQLite中!");
                                    } else if (BasePresenter.SYNC_Type_D.equals(syncType)) {
                                        deleteSync(BaseSQLiteBO.INVALID_CASE_ID, bmList.get(i));

                                        log.info("服务器返回的D型Vip数据成功同步到本地SQLite中!");
                                    }
                                    event.setLastErrorCode(ErrorInfo.EnumErrorCode.EC_NoError);
                                    event.setListMasterTable(bmList);
                                }
                            } else {
                                event.setLastErrorCode(ErrorInfo.EnumErrorCode.EC_NoError);
                                event.setListMasterTable(bmList);
                            }
                        } catch (Exception e) {
                            log.info(e.getMessage());
                            e.printStackTrace();
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

    @Override
    protected boolean updateNAsync(final int iUseCaseID, final List<?> bmList, final BaseSQLiteEvent event) {
        log.info("正在进行VipPresenter的updateNAsync，bmList=" + (bmList == null ? null : bmList.toString()));

        event.setStatus(BaseEvent.EnumEventStatus.EES_SQLite_ToApplyServerData);
        switch (event.getEventTypeSQLite()) {
            case ESET_Vip_UpdateByServerDataNAsync_Done:
                log.info("RetrieveNC服务器返回的Vip，进行更新本地SQLite");
                TaskScheduler.execute(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            if (bmList.size() > 0) {
                                for (int i = 0; i < bmList.size(); i++) {
                                    updateSync(iUseCaseID, (BaseModel) bmList.get(i));
                                }
                                log.info("RetrieveNC服务器返回的Vip，进行更新本地SQLite成功！" + "VipID : " + ((Vip) bmList.get(0)).getID()
                                        + "~" + ((Vip) bmList.get(bmList.size() - 1)).getID());
                            }
                            event.setListMasterTable(bmList);
                            event.setLastErrorCode(ErrorInfo.EnumErrorCode.EC_NoError);
                        } catch (Exception e) {
                            e.printStackTrace();
                            event.setLastErrorCode(ErrorInfo.EnumErrorCode.EC_OtherError);
                            log.info("RetrieveNC服务器返回的Vip，进行更新本地SQLite失败！原因：" + e.getMessage());
                        }
                        EventBus.getDefault().post(event);
                    }
                });
                break;
        }
        return true;
    }
}
