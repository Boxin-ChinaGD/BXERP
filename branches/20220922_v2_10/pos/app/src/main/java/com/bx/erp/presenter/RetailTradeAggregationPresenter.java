
package com.bx.erp.presenter;

import com.bx.erp.bo.BaseSQLiteBO;
import com.bx.erp.bo.NtpHttpBO;
import com.bx.erp.event.BaseEvent;
import com.bx.erp.event.UI.BaseSQLiteEvent;
import com.bx.erp.helper.Constants;
import com.bx.erp.model.BaseModel;
import com.bx.erp.model.DaoSession;
import com.bx.erp.model.ErrorInfo;
import com.bx.erp.model.RetailTradeAggregation;
import com.silencedut.taskscheduler.TaskScheduler;

import org.apache.log4j.Logger;
import org.greenrobot.eventbus.EventBus;

import java.util.Date;
import java.util.List;

public class RetailTradeAggregationPresenter extends BasePresenter {
    private Logger log = Logger.getLogger(this.getClass());

    private final static String OutdatedDay = "-7";

    public RetailTradeAggregationPresenter(final DaoSession dao) {
        super(dao);
    }

    @Override
    protected String getTableName() {
        return dao.getRetailTradeAggregationDao().getTablename();
    }

    @Override
    protected BaseModel createSync(int iUseCaseID, BaseModel bm) {
        log.info("正在进行RetailTradeAggregationPresenter的createSync，bm=" + bm);

        try {
            bm.setSyncDatetime(Constants.getDefaultSyncDatetime2());
            bm.setSyncType(BasePresenter.SYNC_Type_C);
            long id = dao.getRetailTradeAggregationDao().insert((RetailTradeAggregation) bm);
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
    protected BaseModel retrieve1Sync(int iUseCaseID, BaseModel bm) {
        log.info("正在进行RetailTradeAggregationPresenter的retrieve1Sync，bm=" + bm);

        try {
            bm = dao.getRetailTradeAggregationDao().load(bm.getID());
            lastErrorCode = ErrorInfo.EnumErrorCode.EC_NoError;
        } catch (Exception e) {
            log.info("执行retrieve1Sync失败，错误信息为" + e.getMessage());
            e.printStackTrace();
            lastErrorCode = ErrorInfo.EnumErrorCode.EC_OtherError;
        }

        return bm;
    }

    @Override
    protected List<?> retrieveNSync(int iUseCaseID, BaseModel bm) {
        log.info("正在进行RetailTradeAggregationPresenter的retrieveNSync，bm=" + bm);

        switch (iUseCaseID) {
            case BaseSQLiteBO.CASE_RetailTradeAggregation_RetrieveNByConditions:
                try {
                    lastErrorCode = ErrorInfo.EnumErrorCode.EC_NoError;
                    return dao.getRetailTradeAggregationDao().queryRaw(bm.getSql(), bm.getConditions());
                } catch (Exception e) {
                    log.info("执行retrieveNSync的CASE_RetailTradeAggregation_RetrieveNByConditions失败，错误信息为" + e.getMessage());
                    e.printStackTrace();
                    lastErrorCode = ErrorInfo.EnumErrorCode.EC_OtherError;

                    return null;
                }
            default:
                try {
                    lastErrorCode = ErrorInfo.EnumErrorCode.EC_NoError;

                    return dao.getRetailTradeAggregationDao().loadAll();
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
        log.info("正在进行RetailTradeAggregationPresenter的deleteSync，bm=" + bm);

        switch (iUseCaseID) {
            case BaseSQLiteBO.CASE_RetailTradeAggregation_DeleteOutdatedSync:
                try {
                    lastErrorCode = ErrorInfo.EnumErrorCode.EC_NoError;
//                    bm.setSql(" where date(F_WorkTimeEnd) <= ?");
//                    bm.setConditions(new String[]{" date('now', '-7 day')"});  // ...暂时先写死一个常量，将来重构成在配置表里面获取
                    bm.setSql("WHERE F_WorkTimeEnd <= ?");
                    bm.setConditions(new String[]{String.valueOf((new Date(System.currentTimeMillis() + NtpHttpBO.TimeDifference).getTime() - 7 * 24 * 3600 * 1000))});  // 暂时先写死一个常量，将来重构成在配置表里面获取
                    List<RetailTradeAggregation> list = (List<RetailTradeAggregation>) retrieveNObjectSync(BaseSQLiteBO.INVALID_CASE_ID, null);
                    List<RetailTradeAggregation> rtcList = dao.getRetailTradeAggregationDao().queryRaw(bm.getSql(), bm.getConditions());
                    for (RetailTradeAggregation rtc : rtcList) {
                        dao.getRetailTradeAggregationDao().delete(rtc);
                    }
                } catch (Exception e) {
                    log.info("删除RetailTradeAggregation" + OutdatedDay + "天以后的数据时出错！");
                    e.printStackTrace();
                }
                break;
            default:
                try {
                    dao.getRetailTradeAggregationDao().deleteByKey(bm.getID());
                    lastErrorCode = ErrorInfo.EnumErrorCode.EC_NoError;
                } catch (Exception e) {
                    e.printStackTrace();
                    lastErrorCode = ErrorInfo.EnumErrorCode.EC_OtherError;
                }
                break;
        }

        return null;
    }

    @Override
    protected boolean createAsync(int iUseCaseID, final BaseModel bm, final BaseSQLiteEvent event) {
        log.info("正在进行RetailTradeAggregationPresenter的createAsync，bm=" + bm);

        event.setStatus(BaseEvent.EnumEventStatus.EES_SQLite_ToDo);
        switch (iUseCaseID) {
            default:
                TaskScheduler.execute(new Runnable() {
                    @Override
                    public void run() {
//                        event.setEventTypeSQLite(BaseSQLiteEvent.EnumSQLiteEventType.ESET_RetailTradeAggregation_CreateAsync);
                        //
                        try {
                            RetailTradeAggregation retailTradeAggregation = (RetailTradeAggregation) bm;
                            retailTradeAggregation.setSyncDatetime(Constants.getDefaultSyncDatetime2());
                            retailTradeAggregation.setSyncType(BasePresenter.SYNC_Type_C);
                            long id = dao.getRetailTradeAggregationDao().insert(retailTradeAggregation);
                            bm.setID(id);
                            event.setLastErrorCode(ErrorInfo.EnumErrorCode.EC_NoError);
                        } catch (Exception e) {
                            log.info("创建RetailTradeAggregation时出错, 错误信息: " + e.getMessage());
                            event.setLastErrorCode(ErrorInfo.EnumErrorCode.EC_OtherError);
                        }
                        //
                        event.setStatus(BaseEvent.EnumEventStatus.EES_SQLite_Done);
                        event.setBaseModel1(bm);
                        //
                        EventBus.getDefault().post(event);
                    }
                });
        }

        return true;
    }

    @Override
    protected boolean retrieveNAsync(int iUseCaseID, final BaseModel bm, final BaseSQLiteEvent event) {
        log.info("正在进行RetailTradeAggregationPresenter的retrieveNAsync，bm=" + bm);

        event.setStatus(BaseEvent.EnumEventStatus.EES_SQLite_ToDo);
        switch (iUseCaseID) {
            default:
                TaskScheduler.execute(new Runnable() {
                    @Override
                    public void run() {
                        List<RetailTradeAggregation> result = null;
                        //
//                        event.setEventTypeSQLite(BaseSQLiteEvent.EnumSQLiteEventType.ESET_RetailTradeAggregation_DeleteAsync);
                        //
                        try {
                            result = dao.getRetailTradeAggregationDao().loadAll();
                            event.setLastErrorCode(ErrorInfo.EnumErrorCode.EC_NoError);
                        } catch (Exception e) {
                            log.info("查询SQLite的表RetailTradeAggregation的所有记录时出错，错误信息：" + e.getMessage());
                            event.setLastErrorCode(ErrorInfo.EnumErrorCode.EC_OtherError);
                        }
                        //
                        event.setStatus(BaseEvent.EnumEventStatus.EES_SQLite_Done);
                        event.setListMasterTable(result);
                        //
                        EventBus.getDefault().post(event);
                    }
                });
        }

        return true;
    }

    @Override
    protected boolean deleteAsync(int iUseCaseID, final BaseModel bm, final BaseSQLiteEvent event) {
        log.info("正在进行RetailTradeAggregationPresenter的deleteAsync，bm=" + bm);

        event.setStatus(BaseEvent.EnumEventStatus.EES_SQLite_ToDo);
        switch (iUseCaseID) {
            default:
                TaskScheduler.execute(new Runnable() {
                    @Override
                    public void run() {
//                        event.setEventTypeSQLite(BaseSQLiteEvent.EnumSQLiteEventType.ESET_RetailTradeAggregation_DeleteAsync);
                        //
                        try {
                            dao.getRetailTradeAggregationDao().delete((RetailTradeAggregation) bm);
                            event.setLastErrorCode(ErrorInfo.EnumErrorCode.EC_NoError);
                        } catch (Exception e) {
                            log.info("删除RetailTradeAggregation时出错，错误信息：" + e.getMessage());
                            event.setLastErrorCode(ErrorInfo.EnumErrorCode.EC_OtherError);
                        }
                        //
                        event.setStatus(BaseEvent.EnumEventStatus.EES_SQLite_Done);
                        event.setBaseModel1(bm);
                        //
                        EventBus.getDefault().post(event);
                    }
                });
                break;
        }

        return true;
    }

    public BaseModel deleteOutdatedSync(int iUseCaseID, final BaseModel bm, final String day) {
        log.info("正在进行RetailTradeAggregationPresenter的deleteOutdatedSync，bm=" + bm + ",day=" + day);

        try {
            lastErrorCode = ErrorInfo.EnumErrorCode.EC_NoError;
            bm.setSql("where Date(F_WorkTimeStart) <= DATE(DATE_SUB(NOW(),INTERVAL ? day))");
            bm.setConditions(new String[]{OutdatedDay});

            List<RetailTradeAggregation> rtcList = dao.getRetailTradeAggregationDao().queryRaw(bm.getSql(), bm.getConditions());
            for (RetailTradeAggregation rtc : rtcList) {
                dao.getRetailTradeAggregationDao().delete(rtc);
            }
        } catch (Exception e) {
            log.info("删除RetailTradeAggregation" + day + "天以后的数据时出错！");
            e.printStackTrace();
        }
        return null;
    }

    public boolean deleteOutdatedASync(int iUseCaseID, final BaseModel bm, final BaseSQLiteEvent event, final String day) {
        log.info("正在进行RetailTradeAggregationPresenter的ddeleteOutdatedASync，bm=" + bm + ",day=" + day);

        event.setStatus(BaseEvent.EnumEventStatus.EES_SQLite_ToDo);
        switch (iUseCaseID) {
            default:
                TaskScheduler.execute(new Runnable() {
                    @Override
                    public void run() {
//                        event.setEventTypeSQLite(BaseSQLiteEvent.EnumSQLiteEventType.ESET_RetailTradeAggregation_DeleteAsync);
                        //
                        try {
                            bm.setSql("where Date(F_WorkTimeStart) <= DATE(DATE_SUB(NOW(),INTERVAL ? day))");
                            bm.setConditions(new String[]{day});

                            List<RetailTradeAggregation> rtcList = dao.getRetailTradeAggregationDao().queryRaw(bm.getSql(), bm.getConditions());
                            for (RetailTradeAggregation rtc : rtcList) {
                                dao.getRetailTradeAggregationDao().delete(rtc);
                            }
                            event.setLastErrorCode(ErrorInfo.EnumErrorCode.EC_NoError);
                        } catch (Exception e) {
                            log.info("删除RetailTradeAggregation" + day + "天以后的数据时出错，错误信息：" + e.getMessage());
                            event.setLastErrorCode(ErrorInfo.EnumErrorCode.EC_OtherError);
                        }
                        //
                        event.setStatus(BaseEvent.EnumEventStatus.EES_SQLite_Done);
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
    protected boolean createReplacerAsync(final int iUseCaseID, final BaseModel bmOld, final BaseModel bmNew, final BaseSQLiteEvent event) {
        throw new RuntimeException("尚未实现的接口!");
    }

    public boolean deleteOldObjectAsync(final int iUseCaseID, final BaseModel bmOld, final BaseModel bmNew, final BaseSQLiteEvent event) {
        log.info("正在进行RetailTradeAggregationPresenter的deleteOldAsync，bmOld=" + bmOld + ",bmNew=" + bmNew);

        event.setStatus(BaseEvent.EnumEventStatus.EES_SQLite_ToDo);

        switch (iUseCaseID) {
            default:
                TaskScheduler.execute(new Runnable() {
                    @Override
                    public void run() {
                        log.info("已经得到服务器返回的收银汇总数据，准备删除本地旧数据(但并不会插入服务器返回的新数据)");

                        try {
                            dao.getRetailTradeAggregationDao().delete((RetailTradeAggregation) bmOld);
                            //
                            event.setLastErrorCode(ErrorInfo.EnumErrorCode.EC_NoError);
                            event.setBaseModel2(bmNew);
                        } catch (Exception e) {
                            log.info("deleteOldObjectAsync()异常：" + e.getMessage());
                            e.printStackTrace();
                            event.setLastErrorCode(ErrorInfo.EnumErrorCode.EC_OtherError);
                        }
                        event.setStatus(BaseEvent.EnumEventStatus.EES_SQLite_Done);
                        //
                        EventBus.getDefault().post(event);
                    }
                });
                break;
        }
        return true;
    }

    @Override
    protected boolean updateSync(int iUseCaseID, final BaseModel bm) {
        try {
            RetailTradeAggregation retailTradeAggregation = (RetailTradeAggregation) bm;
            retailTradeAggregation.setSyncDatetime(Constants.getDefaultSyncDatetime2()); //SQLite中值为0。如果非正常下班的话，下次上班登录成功后，SyncDataActivity将扫描并上传这个收银汇总
            retailTradeAggregation.setSyncType(BasePresenter.SYNC_Type_C);
            dao.getRetailTradeAggregationDao().update(retailTradeAggregation);
            lastErrorCode = ErrorInfo.EnumErrorCode.EC_NoError;
        } catch (Exception e) {
            log.error("更新RetailTradeAggregation时出错，错误信息：" + e.getMessage());
            lastErrorCode = ErrorInfo.EnumErrorCode.EC_OtherError;
            return false;
        }

        return true;
    }

    @Override
    protected boolean updateAsync(int iUseCaseID, final BaseModel bm, final BaseSQLiteEvent event) {
        log.info("正在进行RetailTradeAggregationPresenter的updateAsync，bm=" + bm);

        switch (iUseCaseID) {
            default:
                TaskScheduler.execute(new Runnable() {
                    @Override
                    public void run() {
                        //
                        try {
                            RetailTradeAggregation retailTradeAggregation = (RetailTradeAggregation) bm;
                            retailTradeAggregation.setSyncDatetime(Constants.getDefaultSyncDatetime2()); //SQLite中值为0。如果非正常下班的话，下次上班登录成功后，SyncDataActivity将扫描并上传这个收银汇总
                            retailTradeAggregation.setSyncType(BasePresenter.SYNC_Type_C);
                            dao.getRetailTradeAggregationDao().update(retailTradeAggregation);
                            event.setBaseModel1(retailTradeAggregation);
                            event.setLastErrorCode(ErrorInfo.EnumErrorCode.EC_NoError);
                        } catch (Exception e) {
                            log.error("更新RetailTradeAggregation时出错，错误信息：" + e.getMessage());
                            event.setLastErrorCode(ErrorInfo.EnumErrorCode.EC_OtherError);
                            event.setBaseModel1(null);
                        }
                        event.setStatus(BaseEvent.EnumEventStatus.EES_SQLite_Done);
                        //
                        EventBus.getDefault().post(event);
                    }
                });
                break;
        }

        return true;
    }
}
