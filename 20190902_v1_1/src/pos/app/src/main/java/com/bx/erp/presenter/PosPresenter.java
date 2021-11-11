package com.bx.erp.presenter;

import android.util.Log;

import com.bx.erp.bo.BaseSQLiteBO;
import com.bx.erp.bo.NtpHttpBO;
import com.bx.erp.di.PerActivity;
import com.bx.erp.event.BaseEvent;
import com.bx.erp.event.UI.BaseSQLiteEvent;
import com.bx.erp.helper.Constants;
import com.bx.erp.model.BaseModel;
import com.bx.erp.model.DaoSession;
import com.bx.erp.model.ErrorInfo;
import com.bx.erp.model.Pos;
import com.silencedut.taskscheduler.TaskScheduler;

import org.apache.log4j.Logger;
import org.greenrobot.eventbus.EventBus;

import java.util.Date;
import java.util.List;

import javax.inject.Inject;


@PerActivity
public class PosPresenter extends BasePresenter {
    private Logger log = Logger.getLogger(this.getClass());

    @Inject
    public PosPresenter(DaoSession dao) {
        super(dao);
    }

    @Override
    protected List<BaseModel> createNSync(int iUseCaseID, final List<?> list) {
        log.info("正在进行PosPresenter的createNSync，list=" + (list == null ? null : list.toString()));

        switch (iUseCaseID) {
            default:
                try {
                    for (int i = 0; i < list.size(); i++) {
                        ((Pos) list.get(i)).setSyncType(BasePresenter.SYNC_Type_C);
                        ((Pos) list.get(i)).setSyncDatetime(new Date(System.currentTimeMillis() + NtpHttpBO.TimeDifference));
                        long id = dao.getPosDao().insert((Pos) list.get(i));
                        ((Pos) list.get(i)).setID(id);
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
    public BaseModel createSync(int iUseCaseID, BaseModel bm) {
        log.info("正在进行PosPresenter的createSync，bm=" + bm);

        try {
            bm.setSyncDatetime(new Date(System.currentTimeMillis() + NtpHttpBO.TimeDifference));
            bm.setSyncType(BasePresenter.SYNC_Type_C);
            long id = dao.getPosDao().insert((Pos) bm);
            bm.setID(id);
            lastErrorCode = ErrorInfo.EnumErrorCode.EC_NoError;
        } catch (Exception e) {
            log.info("执行createSync失败，错误信息为" + e.getMessage());
            e.printStackTrace();
            lastErrorCode = ErrorInfo.EnumErrorCode.EC_OtherError;
        }

        return bm;
    }

    @Override
    protected boolean updateSync(int iUseCaseID, BaseModel bm) {
        log.info("正在进行PosPresenter的updateSync，bm=" + bm);

        try {
            bm.setSyncDatetime(new Date(System.currentTimeMillis() + NtpHttpBO.TimeDifference));
            bm.setSyncType(BasePresenter.SYNC_Type_U);
            dao.getPosDao().update((Pos) bm);
            lastErrorCode = ErrorInfo.EnumErrorCode.EC_NoError;

            return true;
        } catch (Exception e) {
            log.info("执行updateSync失败，错误信息为" + e.getMessage());
            e.printStackTrace();
            lastErrorCode = ErrorInfo.EnumErrorCode.EC_OtherError;
            return false;
        }
    }

    @Override
    protected BaseModel retrieve1Sync(int iUseCaseID, BaseModel bm) {
        log.info("正在进行PosPresenter的retrieve1Sync，bm=" + bm);

        try {
            lastErrorCode = ErrorInfo.EnumErrorCode.EC_NoError;

            return dao.getPosDao().load(bm.getID());
        } catch (Exception e) {
            log.info("执行retrieve1Sync失败，错误信息为" + e.getMessage());
            e.printStackTrace();
            lastErrorCode = ErrorInfo.EnumErrorCode.EC_OtherError;

            return null;
        }
    }

    @Override
    protected List<?> retrieveNSync(int iUseCaseID, BaseModel bm) {
        log.info("正在进行PosPresenter的retrieveNSync，bm=" + bm);

        switch (iUseCaseID) {
            case BaseSQLiteBO.CASE_Pos_RetrieveNByConditions:
                try {
                    lastErrorCode = ErrorInfo.EnumErrorCode.EC_NoError;

                    return dao.getPosDao().queryRaw(bm.getSql(), bm.getConditions());
                } catch (Exception e) {
                    log.info("执行retrieveNSync的CASE_Pos_RetrieveNByConditions失败，错误信息为" + e.getMessage());
                    e.printStackTrace();
                    lastErrorCode = ErrorInfo.EnumErrorCode.EC_OtherError;

                    return null;
                }
            default:
                try {
                    lastErrorCode = ErrorInfo.EnumErrorCode.EC_NoError;

                    return dao.getPosDao().loadAll();
                } catch (Exception e) {
                    log.info("执行retrieveNSync失败，错误信息为" + e.getMessage());
                    e.printStackTrace();
                    lastErrorCode = ErrorInfo.EnumErrorCode.EC_OtherError;

                    return null;
                }
        }
    }

    @Override
    protected BaseModel deleteSync(int iUseCaseID, BaseModel bm) {
        log.info("正在进行PosPresenter的deleteSync，bm=" + bm);

        try {
            dao.getPosDao().deleteByKey(bm.getID());
            lastErrorCode = ErrorInfo.EnumErrorCode.EC_NoError;
        } catch (Exception e) {
            log.info("执行deleteSync失败，错误信息为" + e.getMessage());
            e.printStackTrace();
            lastErrorCode = ErrorInfo.EnumErrorCode.EC_OtherError;
        }

        return null;
    }

    @Override
    protected boolean createAsync(int iUseCaseID, final BaseModel bm, final BaseSQLiteEvent event) {
        log.info("正在进行PosPresenter的createAsync，bm=" + bm);

        switch (iUseCaseID) {
            case BaseSQLiteBO.CASE_POS_Retrieve1ForIdentity:
                TaskScheduler.execute(new Runnable() {
                    @Override
                    public void run() {
//                        event.setEventTypeSQLite(BaseSQLiteEvent.EnumSQLiteEventType.ESET_Pos_CreateAsync);
                        //
                        if (bm != null) {
                            try {
                                long id = dao.getPosDao().insert((Pos) bm);
                                ((Pos) bm).setID(id);
                                event.setLastErrorCode(ErrorInfo.EnumErrorCode.EC_NoError);
                                log.info("创建POSMODEL成功！");
                            } catch (Exception e) {
                                log.error("创建PosModel时出错，错误信息：" + e.getMessage());
                                event.setLastErrorCode(ErrorInfo.EnumErrorCode.EC_OtherError);
                            }
                        }
                        //
                        event.setBaseModel1(bm);
                        //
                        EventBus.getDefault().post(event);
                    }
                });
                break;
            default:
                TaskScheduler.execute(new Runnable() {
                    @Override
                    public void run() {
//                        event.setEventTypeSQLite(BaseSQLiteEvent.EnumSQLiteEventType.ESET_Pos_CreateAsync);
                        //
                        try {

                            ((Pos) bm).setSyncDatetime(new Date(System.currentTimeMillis() + NtpHttpBO.TimeDifference));
                            ((Pos) bm).setSyncType(BasePresenter.SYNC_Type_C);
                            long id = dao.getPosDao().insert((Pos) bm);
                            ((Pos) bm).setID(id);
                            event.setLastErrorCode(ErrorInfo.EnumErrorCode.EC_NoError);
                        } catch (Exception e) {
                            log.error("创建PosModel时出错，错误信息：" + e.getMessage());
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
        log.info("正在进行PosPresenter的updateAsync，bm=" + bm);

        switch (iUseCaseID) {
            default:
                TaskScheduler.execute(new Runnable() {
                    @Override
                    public void run() {
                        //event.setEventTypeSQLite(BaseSQLiteEvent.EnumSQLiteEventType.ESET_Pos_UpdateAsync);
                        //
                        try {
                            bm.setSyncDatetime(new Date(System.currentTimeMillis() + NtpHttpBO.TimeDifference));
                            bm.setSyncType(BasePresenter.SYNC_Type_U);
                            dao.getPosDao().update((Pos) bm);
                            event.setLastErrorCode(ErrorInfo.EnumErrorCode.EC_NoError);
                        } catch (Exception e) {
                            log.error("更新PosModel时出错，错误信息：" + e.getMessage());
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
        log.info("正在进行PosPresenter的retrieveNAsync，bm=" + bm);

        switch (iUseCaseID) {
            case BaseSQLiteBO.CASE_Pos_RetrieveNByConditions:
                TaskScheduler.execute(new Runnable() {
                    @Override
                    public void run() {
                        List<Pos> result = null;
                        //event.setID(new Date(System.currentTimeMillis() + NtpHttpBO.TimeDifference).getTime());
                        //event.setEventTypeSQLite(BaseSQLiteEvent.EnumSQLiteEventType.ESET_Pos_RetrieveNAsync);
                        //
                        try {
                            //Thread.sleep(1); //令event.setID()的ID惟一
                            result = dao.getPosDao().queryRaw(bm.getSql(), bm.getConditions());
                            event.setLastErrorCode(ErrorInfo.EnumErrorCode.EC_NoError);
                        } catch (Exception e) {
                            log.error("RetrieveNAsync PosModel出错，错误信息：" + e.getMessage());
                            event.setLastErrorCode(ErrorInfo.EnumErrorCode.EC_OtherError);
                        }
                        //
                        event.setListMasterTable(result);
                        //
                        EventBus.getDefault().post(event);
                    }
                });
            default:
                TaskScheduler.execute(new Runnable() {
                    @Override
                    public void run() {
                        List<Pos> result = null;
                        //                        event.setID(new Date(System.currentTimeMillis() + NtpHttpBO.TimeDifference).getTime());
//                        event.setEventTypeSQLite(BaseSQLiteEvent.EnumSQLiteEventType.ESET_Pos_RetrieveNAsync);
                        //
                        try {
                            Thread.sleep(1); //令event.setID()的ID惟一
                            result = dao.getPosDao().loadAll();//...
                            event.setLastErrorCode(ErrorInfo.EnumErrorCode.EC_NoError);
                        } catch (Exception e) {
                            log.error("查询SQLite的表PosModel的所有记录时出错，错误信息：" + e.getMessage());
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
        log.info("正在进行PosPresenter的deleteAsync，bm=" + bm);

        switch (iUseCaseID) {
            default:
                TaskScheduler.execute(new Runnable() {
                    @Override
                    public void run() {
//                        event.setEventTypeSQLite(BaseSQLiteEvent.EnumSQLiteEventType.ESET_Pos_DeleteAsync);
                        //
                        try {
                            dao.getPosDao().delete((Pos) bm);
                            event.setLastErrorCode(ErrorInfo.EnumErrorCode.EC_NoError);
                        } catch (Exception e) {
                            log.error("删除PosModel时出错，错误信息：" + e.getMessage());
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
    protected boolean retrieve1Async(int iUseCaseID, final BaseModel bm, final BaseSQLiteEvent event) {
        log.info("正在进行PosPresenter的retrieve1Async，bm=" + bm);

        switch (iUseCaseID) {
            case BaseSQLiteBO.CASE_POS_Retrieve1ForIdentity:
                TaskScheduler.execute(new Runnable() {
                    @Override
                    public void run() {
                        //event.setEventTypeSQLite(BaseSQLiteEvent.EnumSQLiteEventType.ESET_Pos_Retrieve1Async);
                        event.setBaseModel1(null);
                        //通过机器自身的SN码去进行SQLite查找。
                        try {
                            Pos pos = new Pos();
                            String pos_SN = Constants.MyPosSN;
                            pos.setSql("where F_POS_SN = ?");
                            pos.setConditions(new String[]{pos_SN});
                            List<Pos> bmList = dao.getPosDao().queryRaw(pos.getSql(), pos.getConditions());
                            event.setLastErrorCode(ErrorInfo.EnumErrorCode.EC_NoError);

                            if (bmList.size() > 0) {
                                Pos pos2 = bmList.get(0);
                                log.info("SQLite中查找到本机的身份：" + pos2);
                                Constants.posID = pos2.getID().intValue();
                                if (pos2.getPasswordInPOS() != null && !"".equals(pos2.getPasswordInPOS())) {
                                    event.setBaseModel1(pos2);
                                } else {
                                    event.setBaseModel1(null);
                                }
                            }
                        } catch (Exception e) {
                            log.error("Retrieve1 PosModel时出错，错误信息：" + e.getMessage());
                            event.setLastErrorCode(ErrorInfo.EnumErrorCode.EC_OtherError);
                        }
                        EventBus.getDefault().post(event);
                    }
                });
                break;
            default:
                TaskScheduler.execute(new Runnable() {
                    @Override
                    public void run() {
                        Pos result = null;
//                        event.setEventTypeSQLite(BaseSQLiteEvent.EnumSQLiteEventType.ESET_Pos_Retrieve1Async);
                        //
                        try {
                            result = dao.getPosDao().load(((Pos) bm).getID());//...
                            event.setLastErrorCode(ErrorInfo.EnumErrorCode.EC_NoError);
                        } catch (Exception e) {
                            log.error("Retrieve1 PosModel时出错，错误信息：" + e.getMessage());
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
        log.info("正在进行PosPresenter的createNAsync，list=" + (list == null ? null : list.toString()));

        switch (iUseCaseID) {
            default:
                TaskScheduler.execute(new Runnable() {
                    @Override
                    public void run() {
//                        event.setEventTypeSQLite(BaseSQLiteEvent.EnumSQLiteEventType.ESET_Pos_CreateNAsync);
                        //
                        try {
                            for (int i = 0; i < list.size(); i++) {

                                ((Pos) list.get(i)).setSyncDatetime(new Date(System.currentTimeMillis() + NtpHttpBO.TimeDifference));
                                ((Pos) list.get(i)).setSyncType(BasePresenter.SYNC_Type_C);
                                long id = dao.getPosDao().insert((Pos) list.get(i));//...插入一部分失败
                                ((Pos) list.get(i)).setID(id);
                            }
                            event.setLastErrorCode(ErrorInfo.EnumErrorCode.EC_NoError);
                        } catch (Exception e) {
                            log.info("创建POS时出错，错误信息：" + e.getMessage());
                            event.setLastErrorCode(ErrorInfo.EnumErrorCode.EC_OtherError);
                            log.info("创建POS时出错2，错误信息：" + event.getLastErrorCode());
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
    protected boolean refreshByServerDataAsyncC(final int iUseCaseID, final List<?> bmNewList, final BaseSQLiteEvent event) {
        log.info("正在进行PosPresenter的refreshByServerDataAsyncC，bmNewList=" + (bmNewList == null ? null : bmNewList.toString()));

        event.setStatus(BaseEvent.EnumEventStatus.EES_SQLite_ToApplyServerData);
        switch (iUseCaseID) {
            default:
                TaskScheduler.execute(new Runnable() {
                    @Override
                    public void run() {
                        log.info("已经得到服务器返回的需要同步的Pos数据, 准备进行同步...");

                        //删除本地所有的数据
                        deleteNSync(iUseCaseID, null);
                        if (getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError) {
                            //同步所有服务器返回的数据
                            createNSync(iUseCaseID, bmNewList);
                            if (getLastErrorCode() != ErrorInfo.EnumErrorCode.EC_NoError) {
                                event.setLastErrorCode(ErrorInfo.EnumErrorCode.EC_OtherError);
                            }
                        } else {
                            event.setLastErrorCode(ErrorInfo.EnumErrorCode.EC_OtherError);
                        }
                        event.setLastErrorCode(ErrorInfo.EnumErrorCode.EC_NoError);
                        //
                        EventBus.getDefault().post(event);
                    }
                });
                break;
        }
        return true;
    }


    @Override
    protected boolean refreshByServerDataAsync(final int iUseCaseID, final List<?> bmNewList, final BaseSQLiteEvent event) {
        log.info("正在进行PosPresenter的refreshByServerDataAsync，bmNewList=" + (bmNewList == null ? null : bmNewList.toString()));

        event.setStatus(BaseEvent.EnumEventStatus.EES_SQLite_ToApplyServerData);
        switch (iUseCaseID) {
            default:
                TaskScheduler.execute(new Runnable() {
                    @Override
                    public void run() {
                        log.info("已经得到服务器返回的需要同步的POS数据, 准备进行同步...");
                        List<BaseModel> bmList = (List<BaseModel>) bmNewList;
//                        event.setEventTypeSQLite(BaseSQLiteEvent.EnumSQLiteEventType.ESET_Pos_RefreshByServerDataAsync_Done);
                        try {
                            if (bmList != null) {
                                for (int i = 0; i < bmList.size(); i++) {
                                    String PosType = bmList.get(i).getSyncType();

                                    if (BasePresenter.SYNC_Type_C.equals(PosType)) {
                                        Pos pos = (Pos) retrieve1Sync(iUseCaseID, bmList.get(i));
                                        if (pos != null) {
                                            deleteSync(BaseSQLiteBO.INVALID_CASE_ID, pos);
                                        }
                                        //
                                        dao.getPosDao().insert((Pos) bmList.get(i));

                                        log.info("服务器返回的C型POS数据成功同步到SQLite中!");
                                    } else if (BasePresenter.SYNC_Type_U.equals(PosType)) {
                                        updateSync(iUseCaseID, bmList.get(i));

                                        log.info("服务器返回的U型POS数据成功同步到SQLite中!");
                                    } else if (BasePresenter.SYNC_Type_D.equals(PosType)) {
                                        Pos pos = (Pos) retrieve1Sync(iUseCaseID, bmList.get(i));
                                        if (pos != null) {
                                            deleteSync(BaseSQLiteBO.INVALID_CASE_ID, pos);
                                        }

                                        log.info("服务器返回的D型POS数据成功同步到SQLite中!");
                                    }
                                }
                                event.setListMasterTable(bmList);
                                event.setLastErrorCode(ErrorInfo.EnumErrorCode.EC_NoError);
                            } else {
                                event.setLastErrorCode(ErrorInfo.EnumErrorCode.EC_NoError);
                                event.setListMasterTable(bmList);
                            }
                        } catch (Exception e) {
                            log.info("执行refreshByServerDataAsync失败，错误信息为" + e.getMessage());
                            e.printStackTrace();
                            event.setLastErrorCode(ErrorInfo.EnumErrorCode.EC_OtherError);
                            event.setLastErrorMessage(e.getMessage());
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
    protected BaseModel deleteNSync(int iUserCaseID, BaseModel bm) {
        log.info("正在进行PosPresenter的deleteNSync，bm=" + bm);

        try {
            dao.getPosDao().deleteAll();
            lastErrorCode = ErrorInfo.EnumErrorCode.EC_NoError;
        } catch (Exception e) {
            log.info("执行deleteNSync失败，错误信息为" + e.getMessage());
            e.printStackTrace();
            lastErrorCode = ErrorInfo.EnumErrorCode.EC_OtherError;
        }

        return null;
    }

    public void drop() {
        try {
            dao.getPosDao().dropTable(dao.getPosDao().getDatabase(), true);
            lastErrorCode = ErrorInfo.EnumErrorCode.EC_NoError;
        } catch (Exception e) {
            log.error("删除SQLite中的表RetailTrade时出错，错误信息：" + e.getMessage());
            lastErrorCode = ErrorInfo.EnumErrorCode.EC_OtherError;
        }
    }

    @Override
    protected boolean createOrReplaceAsync(int iUseCaseID, final BaseModel baseModel, final BaseSQLiteEvent event) {
        log.info("正在进行PosPresenter的createOrReplaceAsync，baseModel=" + baseModel);

        switch (iUseCaseID) {
            default:
                TaskScheduler.execute(new Runnable() {
                    @Override
                    public void run() {
                        Pos pos = (Pos) baseModel;
                        try {
                            if (pos == null) {
                                //...
                            }
                            //服务器返回的pos,如果本地存在，就删除掉，重新插入服务器返回的
                            pos.setSql("where F_POS_SN = ?");
                            pos.setConditions(new String[]{pos.getPos_SN()});
                            List<Pos> posList = dao.getPosDao().queryRaw(pos.getSql(), pos.getConditions());
                            if (posList.size() == 0) {
                                //插入服务器返回的pos
                                createSync(BaseSQLiteBO.INVALID_CASE_ID, pos);
                            } else {
                                //删除原来的pos，再插入服务器返回的Pos
                                deleteSync(BaseSQLiteBO.INVALID_CASE_ID, posList.get(0));
                                createSync(BaseSQLiteBO.INVALID_CASE_ID, pos);
                            }

                            //服务器返回的Company，如果本地存在，就删除在重新插入，否则直接插入到本地SQLite


                            event.setLastErrorCode(ErrorInfo.EnumErrorCode.EC_NoError);
                        } catch (Exception e) {
                            e.printStackTrace();
                            log.info("插入新的Pos信息出错，错误信息为：" + e.getMessage());
                            event.setLastErrorCode(ErrorInfo.EnumErrorCode.EC_OtherError);
                        }
                        event.setBaseModel1(pos);
                        //
                        EventBus.getDefault().post(event);
                    }
                });
                break;
        }
        return true;
    }

    @Override
    protected String getTableName() {
        return dao.getPosDao().getTablename();
    }

}
