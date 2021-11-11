package wpos.presenter;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Component;
import wpos.utils.EventBus;
import wpos.bo.BaseSQLiteBO;
import wpos.dao.VipMapper;
import wpos.event.BaseEvent;
import wpos.event.UI.BaseSQLiteEvent;
import wpos.helper.Constants;
import wpos.model.*;

import javax.annotation.Resource;
import javax.persistence.Query;
import javax.persistence.Table;
import java.util.List;

@Component("vipPresenter")
public class VipPresenter extends BasePresenter {
    private Log log = LogFactory.getLog(this.getClass());

    public static final String QUERY_VIP_TABLE = "SELECT F_ID," +
            "F_ICID," +
            "F_Mobile," +
            "F_Name," +
            "F_Email," +
            "F_ConsumeTimes," +
            "F_ConsumeAmount," +
            "F_District," +
            "F_Category," +
            "F_Birthday," +
            "F_Bonus," +
            "F_LastConsumeDatetime," +
            "F_LocalPosSN," +
            "F_SyncDatetime," +
            "F_SyncType," +
            "F_CardID " +
            "FROM T_Vip ";


    @Resource
    private VipMapper vipMapper;

//    public VipPresenter(VipMapper vipMapper) {
//        super(vipMapper);
//    }

    @Override
    public void createTableSync() {
        vipMapper.createTable();
    }

    @Override
    protected String getTableName() {
        if (Vip.class.isAnnotationPresent(Table.class)) {
            Table annotation = Vip.class.getAnnotation(Table.class);
            return annotation.name();
        } else {
            log.error("无法获取表名，未给" + Vip.class.getSimpleName() + "添加@Table。");
        }
        return "";
    }

    @Override
    protected String getQueryTable() {
        return QUERY_VIP_TABLE;
    }

    @Override
    protected List<BaseModel> createNSync(int iUseCaseID, final List<?> list) {
        log.info("正在进行VipPresenter的createNSync，list=" + (list == null ? null : list.toString()));

        switch (iUseCaseID) {
            default:
                globalWriteLock.writeLock().lock();
                try {
                    vipMapper.save((List<Vip>) list);
                    lastErrorCode = ErrorInfo.EnumErrorCode.EC_NoError;
                } catch (Exception e) {
                    log.error("执行createNSync时出错，错误信息：" + e.getMessage());
                    e.printStackTrace();
                    lastErrorCode = ErrorInfo.EnumErrorCode.EC_OtherError;
                }
                globalWriteLock.writeLock().unlock();
                return (List<BaseModel>) list;
        }
    }

    @Override
    protected BaseModel createSync(int iUseCaseID, BaseModel bm) {
        log.info("正在进行VipPresenter的createSync，bm=" + (bm == null ? null : bm.toString()));
        globalWriteLock.writeLock().lock();
        try {
            if (bm.getSyncDatetime() == null) {
                bm.setSyncDatetime(Constants.getDefaultSyncDatetime2());
            }
            vipMapper.save((Vip) bm);
            lastErrorCode = ErrorInfo.EnumErrorCode.EC_NoError;
//            if (vipMapper.save(bm) == SQLITE_EXECUTE_NUMBER) {
//                lastErrorCode = ErrorInfo.EnumErrorCode.EC_NoError;
//            }
//            else {
//                lastErrorCode = ErrorInfo.EnumErrorCode.EC_OtherError;
//            }
        } catch (Exception e) {
            log.error("执行createSync时出错，错误信息：" + e.getMessage());
            e.printStackTrace();
            lastErrorCode = ErrorInfo.EnumErrorCode.EC_OtherError;
        }
        globalWriteLock.writeLock().unlock();
        return bm;
    }

    @Override
    protected boolean updateSync(int iUseCaseID, BaseModel bm) {
        log.info("正在进行VipPresenter的updateSync，bm=" + (bm == null ? null : bm.toString()));
        globalWriteLock.writeLock().lock();
        try {
            vipMapper.save((Vip) bm);
            lastErrorCode = ErrorInfo.EnumErrorCode.EC_NoError;
            globalWriteLock.writeLock().unlock();
            return true;
        } catch (Exception e) {
            log.error("执行updateSync时出错，错误信息：" + e.getMessage());
            e.printStackTrace();
            lastErrorCode = ErrorInfo.EnumErrorCode.EC_OtherError;
            globalWriteLock.writeLock().unlock();
            return false;
        }
    }

    @Override
    protected BaseModel retrieve1Sync(int iUseCaseID, BaseModel bm) {
        log.info("正在进行VipPresenter的retrieve1Sync，bm=" + (bm == null ? null : bm.toString()));

        try {
            lastErrorCode = ErrorInfo.EnumErrorCode.EC_NoError;
            return vipMapper.findOne(bm.getID());
        } catch (Exception e) {
            log.error("执行retrieve1Sync时出错，错误信息：" + e.getMessage());
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

//                    return vipMapper.queryRaw(bm.getSql(), bm.getConditions());
//                    return vipMapper.findByCardID(0);
                    String sql = String.format(bm.getSql(), bm.getConditions());
                    Query dataQuery = entityManager.createNativeQuery(QUERY_VIP_TABLE + sql, Vip.class);
                    return dataQuery.getResultList();
//                    return null;
                } catch (Exception e) {
                    log.error("执行retrieveNSync时出错，错误信息：" + e.getMessage());
                    e.printStackTrace();
                    lastErrorCode = ErrorInfo.EnumErrorCode.EC_OtherError;

                    return null;
                }
            default:
                try {
                    lastErrorCode = ErrorInfo.EnumErrorCode.EC_NoError;

//                    return vipMapper.retrieveN(bm);
                    return vipMapper.findAll();
//                    return vipMapper.findAll((Vip)bm);
                } catch (Exception e) {
                    log.error("执行retrieveNSync时出错，错误信息：" + e.getMessage());
                    e.printStackTrace();
                    lastErrorCode = ErrorInfo.EnumErrorCode.EC_OtherError;

                    return null;
                }
        }
    }

    @Override
    protected BaseModel deleteSync(int iUseCaseID, BaseModel bm) {
        log.info("正在进行VipPresenter的deleteSync，bm=" + (bm == null ? null : bm.toString()));
        globalWriteLock.writeLock().lock();
        try {
            vipMapper.delete(bm.getID());
            lastErrorCode = ErrorInfo.EnumErrorCode.EC_NoError;
        } catch (Exception e) {
            log.error("执行deleteSync时出错，错误信息：" + e.getMessage());
            e.printStackTrace();
            lastErrorCode = ErrorInfo.EnumErrorCode.EC_OtherError;
        }
        globalWriteLock.writeLock().unlock();
        return null;
    }

    @Override
    protected boolean createAsync(int iUseCaseID, final BaseModel bm, final BaseSQLiteEvent event) {
        log.info("正在进行VipPresenter的createAsync，bm=" + (bm == null ? null : bm.toString()));

        event.setStatus(BaseEvent.EnumEventStatus.EES_SQLite_ToDo);
        switch (iUseCaseID) {
            default:
                new Thread() {
                    @Override
                    public void run() {
//                        event.setEventTypeSQLite(BaseSQLiteEvent.EnumSQLiteEventType.ESET_Vip_CreateAsync);
                        //
                        globalWriteLock.writeLock().lock();
                        try {
                            Vip vip = (Vip) bm;
                            vip.setSyncDatetime(Constants.getDefaultSyncDatetime2());
                            vip.setSyncType(BasePresenter.SYNC_Type_C);
                            vipMapper.save((Vip) bm);
//                            vipMapper.create(bm);
                            event.setLastErrorCode(ErrorInfo.EnumErrorCode.EC_NoError);
                        } catch (Exception e) {
                            log.error("创建Vip时出错，错误信息：" + e.getMessage());
                            event.setLastErrorCode(ErrorInfo.EnumErrorCode.EC_OtherError);
                        }
                        //
                        event.setBaseModel1(bm);
                        //
                        EventBus.getDefault().post(event);
                        globalWriteLock.writeLock().unlock();
                    }
                }.start();
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
                new Thread() {
                    @Override
                    public void run() {
//                        event.setEventTypeSQLite(BaseSQLiteEvent.EnumSQLiteEventType.ESET_Vip_UpdateAsync);
                        //
                        globalWriteLock.writeLock().lock();
                        try {
                            Vip vip = (Vip) bm;
                            vip.setSyncDatetime(Constants.getDefaultSyncDatetime2());
                            vip.setSyncType(BasePresenter.SYNC_Type_U);
                            vipMapper.save((Vip) bm);
//                            vipMapper.update((Vip) bm);
                            event.setLastErrorCode(ErrorInfo.EnumErrorCode.EC_NoError);
                        } catch (Exception e) {
                            log.error("更新Vip时出错，错误信息：" + e.getMessage());
                            event.setLastErrorCode(ErrorInfo.EnumErrorCode.EC_OtherError);
                        }
                        //
                        event.setBaseModel1(bm);
                        //
                        EventBus.getDefault().post(event);
                        globalWriteLock.writeLock().unlock();
                    }
                }.start();
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
                new Thread() {
                    @Override
                    public void run() {
                        List<Vip> result = null;
                        try {
//                            result = vipMapper.queryRaw(bm.getSql(), bm.getConditions());
                            String sql = String.format(bm.getSql(), bm.getConditions());
                            Query dataQuery = entityManager.createNativeQuery(QUERY_VIP_TABLE + sql, Vip.class);
                            result = dataQuery.getResultList();
                            event.setLastErrorCode(ErrorInfo.EnumErrorCode.EC_NoError);
                        } catch (Exception e) {
                            log.error("RetrieveNAsync Vip出错，错误信息：" + e.getMessage());
                            event.setLastErrorCode(ErrorInfo.EnumErrorCode.EC_OtherError);
                        }
                        //
                        event.setListMasterTable(result);
                        //
                        EventBus.getDefault().post(event);
                    }
                }.start();
                break;
            default:
                new Thread() {
                    @Override
                    public void run() {
//                        List<BaseModel> result = null;
                        List<Vip> result = null;
                        //                        event.setID(new Date().getTime());
//                        event.setEventTypeSQLite(BaseSQLiteEvent.EnumSQLiteEventType.ESET_Vip_RetrieveNAsync);
                        //
                        try {
                            Thread.sleep(1); //令event.setID()的ID惟一
//                            result = vipMapper.retrieveN(bm);//...
                            result = vipMapper.findAll();//...
                            event.setLastErrorCode(ErrorInfo.EnumErrorCode.EC_NoError);
                        } catch (Exception e) {
                            log.error("查询SQLite的表Vip的所有记录时出错，错误信息：" + e.getMessage());
                            event.setLastErrorCode(ErrorInfo.EnumErrorCode.EC_OtherError);
                        }
                        //
                        event.setListMasterTable(result);
                        //
                        EventBus.getDefault().post(event);
                    }
                }.start();
                break;
        }

        return true;
    }

    @Override
    protected boolean deleteAsync(int iUseCaseID, final BaseModel bm, final BaseSQLiteEvent event) {
        log.info("正在进行VipPresenter的deleteAsync，bm=" + (bm == null ? null : bm.toString()));

        switch (iUseCaseID) {
            default:
                new Thread() {
                    @Override
                    public void run() {
//                        event.setEventTypeSQLite(BaseSQLiteEvent.EnumSQLiteEventType.ESET_Vip_DeleteAsync);
                        //
                        globalWriteLock.writeLock().lock();
                        if (bm != null) {
                            try {
                                vipMapper.delete(bm.getID());
                                event.setLastErrorCode(ErrorInfo.EnumErrorCode.EC_NoError);
                                log.info("删除本地Vip数据成功，ID = " + bm.getID());
                                event.setBaseModel1(bm);
                            } catch (Exception e) {
                                log.error("删除Vip时出错，错误信息：" + e.getMessage());
                                event.setLastErrorCode(ErrorInfo.EnumErrorCode.EC_OtherError);
                                log.error("删除本地Vip数据失败，原因：" + e.getMessage());
                            }
                        }
                        //
                        event.setStatus(BaseEvent.EnumEventStatus.EES_SQLite_DoneApplyServerData);
                        event.setBaseModel1(bm);
                        //
                        EventBus.getDefault().post(event);
                        globalWriteLock.writeLock().unlock();
                    }
                }.start();
                break;
        }

        return true;
    }

    @Override
    protected boolean retrieve1Async(int iUseCaseID, final BaseModel bm, final BaseSQLiteEvent event) {
        log.info("正在进行VipPresenter的retrieve1Async，bm=" + (bm == null ? null : bm.toString()));

        switch (iUseCaseID) {
            default:
                new Thread() {
                    @Override
                    public void run() {
                        Vip result = null;
//                        event.setEventTypeSQLite(BaseSQLiteEvent.EnumSQLiteEventType.ESET_Vip_Retrieve1Async);
                        //
                        try {
//                            result = (Vip) vipMapper.retrieve1(bm.getID());//...
                            result = (Vip) vipMapper.findOne(bm.getID());//...
                            event.setLastErrorCode(ErrorInfo.EnumErrorCode.EC_NoError);
                        } catch (Exception e) {
                            log.error("Retrieve1 Vip时出错，错误信息：" + e.getMessage());
                            event.setLastErrorCode(ErrorInfo.EnumErrorCode.EC_OtherError);
                        }
                        //
                        event.setBaseModel1(result);
                        //
//                        EventBus.getDefault().post(event);
                    }
                }.start();
                break;
        }

        return true;
    }

    @Override
    protected boolean createNAsync(int iUseCaseID, final List<?> list, final BaseSQLiteEvent event) {
        log.info("正在进行VipPresenter的createNAsync，list=" + (list == null ? null : list.toString()));

        switch (iUseCaseID) {
            default:
                new Thread() {
                    @Override
                    public void run() {
                        globalWriteLock.writeLock().lock();
//                        event.setEventTypeSQLite(BaseSQLiteEvent.EnumSQLiteEventType.ESET_Vip_CreateNAsync);
                        //
                        try {
                            for (int i = 0; i < list.size(); i++) {
//                                vipMapper.create((Vip) list.get(i));//...插入一部分失败
                                vipMapper.save((Vip) list.get(i));//...插入一部分失败
                            }
                            event.setLastErrorCode(ErrorInfo.EnumErrorCode.EC_NoError);
                        } catch (Exception e) {
                            log.error("创建Commodity时出错，错误信息：" + e.getMessage());
                            event.setLastErrorCode(ErrorInfo.EnumErrorCode.EC_OtherError);
                            log.error("创建Commodity时出错2，错误信息：" + event.getLastErrorCode());
                        }
                        //
                        event.setListMasterTable(list);
                        //
                        EventBus.getDefault().post(event);
                        globalWriteLock.writeLock().unlock();
                    }
                }.start();
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
                new Thread() {
                    @Override
                    public void run() {
                        globalWriteLock.writeLock().lock();
                        log.info("已经得到服务器返回的Vip数据，准备删除本地的旧数据，插入服务器返回的数据！");

                        try {
                            deleteSync(iUseCaseID, bmOld);
                            createSync(iUseCaseID, bmNew);
                            //
                            event.setLastErrorCode(ErrorInfo.EnumErrorCode.EC_NoError);
                            event.setBaseModel1(bmNew);

                            log.info("插入服务器返回的数据成功！ID=" + bmNew.getID());
                        } catch (Exception e) {
                            log.error("createReplacerAsync()异常：" + e.getMessage());
                            e.printStackTrace();
                            event.setLastErrorCode(ErrorInfo.EnumErrorCode.EC_OtherError);
                        }
                        event.setStatus(BaseEvent.EnumEventStatus.EES_SQLite_DoneApplyServerData);
                        event.setEventTypeSQLite(BaseSQLiteEvent.EnumSQLiteEventType.ESET_Vip_CreateReplacerAsync_Done);
                        //event.setData("CREATE_DONE");
                        //
                        EventBus.getDefault().post(event);
                        globalWriteLock.writeLock().unlock();
                    }
                }.start();
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
                new Thread() {
                    @Override
                    public void run() {
                        globalWriteLock.writeLock().lock();
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
                        globalWriteLock.writeLock().unlock();
                    }
                }.start();
                break;
            case ESET_Vip_BeforeDeleteAsync_Done:
                globalWriteLock.writeLock().lock();
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
                globalWriteLock.writeLock().unlock();
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
                new Thread() {
                    @Override
                    public void run() {
                        globalWriteLock.writeLock().lock();
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
                            log.error(e.getMessage());
                            e.printStackTrace();
                            event.setLastErrorCode(ErrorInfo.EnumErrorCode.EC_OtherError);
                        }
                        //
                        EventBus.getDefault().post(event);
                        globalWriteLock.writeLock().unlock();
                    }
                }.start();
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
                new Thread() {
                    @Override
                    public void run() {
                        globalWriteLock.writeLock().lock();
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
                        globalWriteLock.writeLock().unlock();
                    }
                }.start();
                break;
        }
        return true;
    }
}
