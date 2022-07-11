package com.bx.erp.presenter;

import android.util.Log;

import com.bx.erp.bo.BaseSQLiteBO;
import com.bx.erp.bo.NtpHttpBO;
import com.bx.erp.di.PerActivity;
import com.bx.erp.event.BaseEvent;
import com.bx.erp.event.UI.BaseSQLiteEvent;
import com.bx.erp.model.Barcodes;
import com.bx.erp.model.BaseModel;
import com.bx.erp.model.DaoSession;
import com.bx.erp.model.ErrorInfo;
import com.silencedut.taskscheduler.TaskScheduler;

import org.apache.log4j.Logger;
import org.greenrobot.eventbus.EventBus;

import java.util.Date;
import java.util.List;

import static com.bx.erp.bo.BaseSQLiteBO.CASE_Barcodes_DeleteNByConditions;
import static com.bx.erp.bo.BaseSQLiteBO.CASE_Barcodes_RetrieveNByConditions;

@PerActivity
public class BarcodesPresenter extends BasePresenter {
    private Logger log = Logger.getLogger(this.getClass());

    public BarcodesPresenter(DaoSession dao) {
        super(dao);
    }

    @Override
    protected List<BaseModel> createNSync(int iUseCaseID, final List<?> list) {
        log.info("正在进行BarcodesPresenter的createNSync，list=" + (list == null ? null : list.toString()));

        switch (iUseCaseID) {
            default:
                try {
                    for (int i = 0; i < list.size(); i++) {
                        Barcodes c = (Barcodes) list.get(i);
                        c.setSyncType(BasePresenter.SYNC_Type_C);
                        long id = dao.getBarcodesDao().insert(c);
                        ((Barcodes) list.get(i)).setID(id);
                        lastErrorCode = ErrorInfo.EnumErrorCode.EC_NoError;
                    }
                } catch (Exception e) {
                    log.info("createNSync出现异常，错误信息为" + e.getMessage());
                    e.printStackTrace();
                    lastErrorCode = ErrorInfo.EnumErrorCode.EC_OtherError;
                }
                return (List<BaseModel>) list;
        }
    }

    @Override
    protected BaseModel createSync(int iUseCaseID, BaseModel bm) {
        log.info("正在进行BarcodesPresenter的createSync，bm=" + (bm == null ? null : bm.toString()));

        try {
            bm.setSyncType(BasePresenter.SYNC_Type_C);
            long id = dao.getBarcodesDao().insert((Barcodes) bm);
            bm.setID(id);
            lastErrorCode = ErrorInfo.EnumErrorCode.EC_NoError;
        } catch (Exception e) {
            log.info("createSync出现异常，错误信息为" + e.getMessage());
            e.printStackTrace();
            lastErrorCode = ErrorInfo.EnumErrorCode.EC_OtherError;
        }

        return bm;
    }

    @Override
    protected BaseModel retrieve1Sync(int iUseCaseID, BaseModel bm) {
        log.info("正在进行BarcodesPresenter的retrieve1Sync，bm=" + (bm == null ? null : bm.toString()));

        try {
            dao.clear();
            lastErrorCode = ErrorInfo.EnumErrorCode.EC_NoError;

            return dao.getBarcodesDao().load(bm.getID());
        } catch (Exception e) {
            log.info("retrieve1Sync出现异常，错误信息为" + e.getMessage());
            e.printStackTrace();
            lastErrorCode = ErrorInfo.EnumErrorCode.EC_OtherError;

            return null;
        }
    }

    @Override
    protected List<?> retrieveNSync(int iUseCaseID, BaseModel bm) {
        log.info("正在进行BarcodesPresenter的retrieveNSync，bm=" + (bm == null ? null : bm.toString()));

        switch (iUseCaseID) {
            case CASE_Barcodes_RetrieveNByConditions:
                try {
                    lastErrorCode = ErrorInfo.EnumErrorCode.EC_NoError;
                    List<?> list = dao.getBarcodesDao().queryRaw(bm.getSql(), bm.getConditions());
                    return list;
                } catch (Exception e) {
                    log.info("执行retrieveNSync的CASE_Barcodes_RetrieveNByConditions分支时出现异常，错误信息为" + e.getMessage());
                    e.printStackTrace();
                    lastErrorCode = ErrorInfo.EnumErrorCode.EC_OtherError;

                    return null;
                }
            default:
                try {
                    lastErrorCode = ErrorInfo.EnumErrorCode.EC_NoError;

                    return dao.getBarcodesDao().loadAll();
                } catch (Exception e) {
                    log.info("执行retrieveNSync时出现异常，错误信息为" + e.getMessage());
                    e.printStackTrace();
                    lastErrorCode = ErrorInfo.EnumErrorCode.EC_NoError;
                    log.info("错误为" + lastErrorCode);
                    return null;
                }
        }
    }

    @Override
    protected BaseModel deleteSync(int iUseCaseID, BaseModel bm) {
        log.info("正在进行BarcodesPresenter的deleteSync，bm=" + (bm == null ? null : bm.toString()));

        try {
            dao.getBarcodesDao().deleteByKey(bm.getID());
            lastErrorCode = ErrorInfo.EnumErrorCode.EC_NoError;
        } catch (Exception e) {
            log.info("执行deleteSync时出现异常，错误信息为" + e.getMessage());
            e.printStackTrace();
            lastErrorCode = ErrorInfo.EnumErrorCode.EC_OtherError;
        }

        return null;
    }

    @Override
    protected BaseModel deleteNSync(int iUserCaseID, BaseModel bm) {
        log.info("正在进行BarcodesPresenter的deleteNSync，bm=" + (bm == null ? null : bm.toString()));

        switch (iUserCaseID) {
            case CASE_Barcodes_DeleteNByConditions:
                try {
                    dao.getDatabase().execSQL(bm.getSql(), bm.getConditions());
                    lastErrorCode = ErrorInfo.EnumErrorCode.EC_NoError;
                } catch (Exception e) {
                    log.info("执行deleteNSync时出现异常，错误信息为" + e.getMessage());
                    e.printStackTrace();
                    lastErrorCode = ErrorInfo.EnumErrorCode.EC_OtherError;
                }
                break;
            default:
                try {
                    dao.getBarcodesDao().deleteAll();
                    lastErrorCode = ErrorInfo.EnumErrorCode.EC_NoError;
                } catch (Exception e) {
                    log.info("执行deleteNSync时出现异常，错误信息为" + e.getMessage());
                    e.printStackTrace();
                    lastErrorCode = ErrorInfo.EnumErrorCode.EC_OtherError;
                }
                break;
        }
        return null;
    }

    @Override
    protected boolean refreshByServerDataAsync(final int iUseCaseID, final List<?> bmNewList, final BaseSQLiteEvent event) {
        log.info("正在进行BarcodesPresenter的refreshByServerDataAsync，bmNewList=" + (bmNewList == null ? null : bmNewList.toString()));

        event.setStatus(BaseEvent.EnumEventStatus.EES_SQLite_ToApplyServerData);
        switch (iUseCaseID) {
            default:
                TaskScheduler.execute(new Runnable() {
                    @Override
                    public void run() {
                        log.info("已经得到服务器返回的需要同步的Barcodes数据, 准备进行同步...");
                        List<BaseModel> bmList = (List<BaseModel>) bmNewList;
//                        event.setEventTypeSQLite(BaseSQLiteEvent.EnumSQLiteEventType.ESET_Barcodes_RefreshByServerDataAsync_Done);
                        try {
                            if (bmList != null) {
                                for (int i = 0; i < bmList.size(); i++) {
                                    String BarcodesType = bmList.get(i).getSyncType();

                                    if (BasePresenter.SYNC_Type_C.equals(BarcodesType)) {
                                        Barcodes barcodes = (Barcodes) retrieve1Sync(iUseCaseID, bmList.get(i));
                                        if (barcodes != null) {
                                            dao.getBarcodesDao().deleteByKey(barcodes.getID());
                                        }
                                        //
                                        dao.getBarcodesDao().insert((Barcodes) bmList.get(i));

                                        log.info("服务器返回的C型Barcodes数据成功同步到SQLite中!");
                                    } else if (BasePresenter.SYNC_Type_D.equals(BarcodesType)) {
                                        Barcodes barcodes = (Barcodes) retrieve1Sync(iUseCaseID, bmList.get(i));
                                        if (barcodes != null) {
                                            dao.getBarcodesDao().deleteByKey(barcodes.getID());
                                        }

                                        log.info("服务器返回的D型Barcodes数据成功同步到SQLite中!");
                                    }
                                    event.setLastErrorCode(ErrorInfo.EnumErrorCode.EC_NoError);
                                    event.setListMasterTable(bmList);
                                }
                            } else {
                                event.setLastErrorCode(ErrorInfo.EnumErrorCode.EC_NoError);
                                event.setListMasterTable(bmList);
                            }
                        } catch (Exception e) {
                            log.info("执行refreshByServerDataAsync时出现异常，错误信息为" + e.getMessage());
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
    protected boolean refreshByServerDataAsyncC(final int iUseCaseID, final List<?> bmNewList, final BaseSQLiteEvent event) {
        log.info("正在进行BarcodesPresenter的refreshByServerDataAsyncC，bmNewList=" + (bmNewList == null ? null : bmNewList.toString()));

        event.setStatus(BaseEvent.EnumEventStatus.EES_SQLite_ToApplyServerData);
        switch (iUseCaseID) {
            default:
                TaskScheduler.execute(new Runnable() {
                    @Override
                    public void run() {
                        log.info("已经得到服务器返回的需要同步的Barcodes数据, 准备进行同步...");
                        event.setLastErrorCode(ErrorInfo.EnumErrorCode.EC_NoError);
//                        event.setEventTypeSQLite(BaseSQLiteEvent.EnumSQLiteEventType.ESET_Barcodes_RefreshByServerDataAsyncC_Done);

                        try {
                            List<Barcodes> barcodesList = (List<Barcodes>) bmNewList;
                            // TODO 可能存在没删除掉的情况
                            //根据返回的数据，查找本地SQLite是否存该条数据，如果存在就删除再插入，否则就直接插入
                            if (barcodesList != null && barcodesList.size() > 0) {
                                Barcodes deleteConditionsBarcode = new Barcodes();
                                if (event.getPageIndex() != null && event.getPageIndex().equals(Barcodes.PAGEINDEX_START)) {
                                    String sql = "delete from " + getTableName() + " where F_ID > ? ";//
                                    String[] conditions = new String[]{String.valueOf(barcodesList.get(0).getID())};
                                    deleteConditionsBarcode.setSql(sql);
                                    deleteConditionsBarcode.setConditions(conditions);
                                    deleteConditionsBarcode.setSubUseCaseID(BaseModel.EnumSubUseCaseID.ESUC_Long);
                                    deleteNObjectSync(BaseSQLiteBO.CASE_Barcodes_DeleteNByConditions, deleteConditionsBarcode);
                                    if (lastErrorCode != ErrorInfo.EnumErrorCode.EC_NoError) {
                                        event.setLastErrorCode(lastErrorCode);
                                        event.setStatus(BaseEvent.EnumEventStatus.EES_SQLite_DoneApplyServerData);
                                        EventBus.getDefault().post(event);
                                        return;
                                    }
                                } else if (event.getPageIndex() != null && event.getPageIndex().equals(Barcodes.PAGEINDEX_END)) {
                                    String sql = "delete from " + getTableName() + " where F_ID < ? ";//
                                    String[] conditions = new String[]{String.valueOf(barcodesList.get(barcodesList.size() - 1).getID())};
                                    deleteConditionsBarcode.setSql(sql);
                                    deleteConditionsBarcode.setConditions(conditions);
                                    deleteConditionsBarcode.setSubUseCaseID(BaseModel.EnumSubUseCaseID.ESUC_Long);
                                    deleteNObjectSync(BaseSQLiteBO.CASE_Barcodes_DeleteNByConditions, deleteConditionsBarcode);
                                    if (lastErrorCode != ErrorInfo.EnumErrorCode.EC_NoError) {
                                        event.setLastErrorCode(lastErrorCode);
                                        event.setStatus(BaseEvent.EnumEventStatus.EES_SQLite_DoneApplyServerData);
                                        EventBus.getDefault().post(event);
                                        return;
                                    }
                                }
                                //
                                event.setPageIndex("");
                                String sql = "delete from " + getTableName() + " where F_ID >= ? AND F_ID <= ?";
                                String[] conditions = new String[]{String.valueOf(barcodesList.get(barcodesList.size() - 1).getID()), String.valueOf(barcodesList.get(0).getID())};
                                //先删除本地数据库原有的数据
                                deleteConditionsBarcode.setSql(sql);
                                deleteConditionsBarcode.setConditions(conditions);
                                deleteConditionsBarcode.setSubUseCaseID(BaseModel.EnumSubUseCaseID.ESUC_Long);
                                deleteNObjectSync(BaseSQLiteBO.CASE_Barcodes_DeleteNByConditions, deleteConditionsBarcode);
                                if (lastErrorCode != ErrorInfo.EnumErrorCode.EC_NoError) {
                                    event.setLastErrorCode(lastErrorCode);
                                    event.setStatus(BaseEvent.EnumEventStatus.EES_SQLite_DoneApplyServerData);
                                    EventBus.getDefault().post(event);
                                    return;
                                }
                                for (int i = 0; i < barcodesList.size(); i++) {
                                    dao.getBarcodesDao().insert(barcodesList.get(i));
                                }
                            }
                            event.setListMasterTable(bmNewList);
                        } catch (Exception e) {
                            log.info("执行refreshByServerDataAsyncC时出现异常，错误信息为" + e.getMessage());

                            e.printStackTrace();
                            event.setLastErrorCode(ErrorInfo.EnumErrorCode.EC_OtherError);
                        }
                        event.setStatus(BaseEvent.EnumEventStatus.EES_SQLite_DoneApplyServerData);
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
        log.info("正在进行BarcodesPresenter的refreshByServerDataAsyncC，list=" + (list == null ? null : list.toString()));

        switch (iUseCaseID) {
            default:
                TaskScheduler.execute(new Runnable() {
                    @Override
                    public void run() {
//                        event.setEventTypeSQLite(BaseSQLiteEvent.EnumSQLiteEventType.ESET_Barcodes_CreateNAsync);
                        //
                        try {
                            for (int i = 0; i < list.size(); i++) {
                                Barcodes c = (Barcodes) list.get(i);
                                c.setSyncDatetime(new Date(System.currentTimeMillis() + NtpHttpBO.TimeDifference));
                                c.setSyncType(BasePresenter.SYNC_Type_C);
                                long id = dao.getBarcodesDao().insert(c);//...插入一部分失败
                                ((Barcodes) list.get(i)).setID(id);
                            }
                            event.setLastErrorCode(ErrorInfo.EnumErrorCode.EC_NoError);
                        } catch (Exception e) {
                            log.info("创建Barcodes时出错，错误信息：" + e.getMessage());
                            event.setLastErrorCode(ErrorInfo.EnumErrorCode.EC_OtherError);
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
    protected boolean createAsync(int iUseCaseID, final BaseModel bm, final BaseSQLiteEvent event) {
        log.info("正在进行BarcodesPresenter的createAsync，bm=" + (bm == null ? null : bm.toString()));

        switch (iUseCaseID) {
            default:
                TaskScheduler.execute(new Runnable() {
                    @Override
                    public void run() {
//                        event.setEventTypeSQLite(BaseSQLiteEvent.EnumSQLiteEventType.ESET_Barcodes_CreateAsync);
                        //
                        try {
                            bm.setSyncType(BasePresenter.SYNC_Type_C);
                            bm.setSyncDatetime(new Date(System.currentTimeMillis() + NtpHttpBO.TimeDifference));
                            long id = dao.getBarcodesDao().insert((Barcodes) bm);
                            ((Barcodes) bm).setID(id);
                            event.setLastErrorCode(ErrorInfo.EnumErrorCode.EC_NoError);
                        } catch (Exception e) {
                            Log.e("输出：", "创建Barcodes时出错，错误信息：" + e.getMessage());
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
    protected boolean deleteAsync(int iUseCaseID, final BaseModel bm, final BaseSQLiteEvent event) {
        log.info("正在进行BarcodesPresenter的deleteAsync，bm=" + (bm == null ? null : bm.toString()));

        switch (iUseCaseID) {
            default:
                TaskScheduler.execute(new Runnable() {
                    @Override
                    public void run() {
                        event.setEventTypeSQLite(BaseSQLiteEvent.EnumSQLiteEventType.ESET_Barcodes_DeleteAsync);
                        //
                        try {
                            dao.getBarcodesDao().deleteByKey(bm.getID());
                            event.setLastErrorCode(ErrorInfo.EnumErrorCode.EC_NoError);
                        } catch (Exception e) {
                            Log.e("输出：", "删除Barcodes时出错，错误信息：" + e.getMessage());
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
        log.info("正在进行BarcodesPresenter的retrieveNAsync，bm=" + (bm == null ? null : bm.toString()));

        switch (iUseCaseID) {
            default:
                TaskScheduler.execute(new Runnable() {
                    @Override
                    public void run() {
                        List<Barcodes> result = null;
                        event.setEventTypeSQLite(BaseSQLiteEvent.EnumSQLiteEventType.ESET_Barcodes_RetrieveNAsync);
                        try {
                            Thread.sleep(1); //令event.setID()的ID惟一
                            result = dao.getBarcodesDao().loadAll();//...
                            event.setLastErrorCode(ErrorInfo.EnumErrorCode.EC_NoError);
                        } catch (Exception e) {
                            log.info("查询SQLite的表Barcodes的所有记录时出错，错误信息：" + e.getMessage());
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
    protected String getTableName() {
        return dao.getBarcodesDao().getTablename();
    }
}
