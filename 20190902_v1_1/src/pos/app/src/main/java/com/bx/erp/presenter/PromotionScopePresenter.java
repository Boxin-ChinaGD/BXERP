package com.bx.erp.presenter;

import com.bx.erp.bo.BaseSQLiteBO;
import com.bx.erp.bo.NtpHttpBO;
import com.bx.erp.event.UI.BaseSQLiteEvent;
import com.bx.erp.helper.Constants;
import com.bx.erp.model.BaseModel;
import com.bx.erp.model.DaoSession;
import com.bx.erp.model.ErrorInfo;
import com.bx.erp.model.PromotionScope;
import com.silencedut.taskscheduler.TaskScheduler;

import org.apache.log4j.Logger;
import org.greenrobot.eventbus.EventBus;

import java.util.Date;
import java.util.List;

public class PromotionScopePresenter extends BasePresenter {
    private Logger log = Logger.getLogger(this.getClass());

    public PromotionScopePresenter(DaoSession dao) {
        super(dao);
    }

    @Override
    protected String getTableName() {
        return dao.getPromotionScopeDao().getTablename();
    }

    @Override
    protected BaseModel deleteNSync(int iUseCaseID, final BaseModel bm) {
        dao.getPromotionScopeDao().deleteAll();
        return null;
    }

    @Override
    protected List<BaseModel> createNSync(int iUseCaseID, final List<?> list) {
        log.info("正在进行PromotionScopePresenter的createNSync，list=" + (list == null ? null : list.toString()));

        switch (iUseCaseID) {
            default:
                try {
                    for (int i = 0; i < list.size(); i++) {
                        ((PromotionScope) list.get(i)).setSyncDatetime(Constants.getDefaultSyncDatetime2());
                        ((PromotionScope) list.get(i)).setSyncType(BasePresenter.SYNC_Type_C);
                        long id = dao.getPromotionScopeDao().insert((PromotionScope) list.get(i));
                        ((PromotionScope) list.get(i)).setID(id);
                        log.info("PromotionScope创建成功，ID=" + id);

                        lastErrorCode = ErrorInfo.EnumErrorCode.EC_NoError;
                    }
                } catch (Exception e) {
                    log.info("执行createNSync失败，错误信息为" +e.getMessage());
                    e.printStackTrace();
                    lastErrorCode = ErrorInfo.EnumErrorCode.EC_OtherError;
                }
                return (List<BaseModel>) list;
        }
    }

    @Override
    protected BaseModel createSync(int iUseCaseID, BaseModel bm) {
        log.info("正在进行PromotionScopePresenter的createSync，bm=" + bm);

        try {
            PromotionScope PromotionScope = (PromotionScope) bm;
            PromotionScope.setSyncType(BasePresenter.SYNC_Type_C);
            if (PromotionScope.getSyncDatetime() == null) {
                PromotionScope.setSyncDatetime(Constants.getDefaultSyncDatetime2());
            }
            long id = dao.getPromotionScopeDao().insert(PromotionScope);
            bm.setID(id);
            lastErrorCode = ErrorInfo.EnumErrorCode.EC_NoError;
        } catch (Exception e) {
            log.info(e.getMessage());
            e.printStackTrace();
            lastErrorCode = ErrorInfo.EnumErrorCode.EC_OtherError;
        }

        return bm;
    }

    @Override
    protected BaseModel retrieve1Sync(int iUseCaseID, BaseModel bm) {
        log.info("正在进行PromotionScopePresenter的retrieve1Sync，bm=" + bm);

        try {
            lastErrorCode = ErrorInfo.EnumErrorCode.EC_NoError;

            return dao.getPromotionScopeDao().load(bm.getID());
        } catch (Exception e) {
            log.info(e.getMessage());
            e.printStackTrace();
            lastErrorCode = ErrorInfo.EnumErrorCode.EC_OtherError;

            return null;
        }
    }

    @Override
    protected List<?> retrieveNSync(int iUseCaseID, BaseModel bm) {
        log.info("正在进行PromotionScopePresenter的retrieveNSync，bm=" + bm);

        switch (iUseCaseID) {
            case BaseSQLiteBO.CASE_PromotionScope_RetrieveNByConditions:
                try {
                    lastErrorCode = ErrorInfo.EnumErrorCode.EC_NoError;

                    return dao.getPromotionScopeDao().queryRaw(bm.getSql(), bm.getConditions());
                } catch (Exception e) {
                    log.info(e.getMessage());
                    e.printStackTrace();
                    lastErrorCode = ErrorInfo.EnumErrorCode.EC_OtherError;

                    return null;
                }
            default:
                try {
                    lastErrorCode = ErrorInfo.EnumErrorCode.EC_NoError;

                    return dao.getPromotionScopeDao().loadAll();
                } catch (Exception e) {
                    log.info(e.getMessage());
                    e.printStackTrace();
                    lastErrorCode = ErrorInfo.EnumErrorCode.EC_OtherError;

                    return null;
                }
        }
    }

    @Override
    protected BaseModel deleteSync(int iUseCaseID, BaseModel bm) {
        log.info("正在进行PromotionScopePresenter的deleteSync，bm=" + bm);

        try {
            dao.getPromotionScopeDao().deleteByKey(bm.getID());
            lastErrorCode = ErrorInfo.EnumErrorCode.EC_NoError;
        } catch (Exception e) {
            log.info(e.getMessage());
            e.printStackTrace();
            lastErrorCode = ErrorInfo.EnumErrorCode.EC_OtherError;
        }

        return null;
    }

    @Override
    protected boolean createAsync(int iUseCaseID, final BaseModel bm, final BaseSQLiteEvent event) {
        log.info("正在进行PromotionScopePresenter的createAsync，bm=" + bm);

        switch (iUseCaseID) {
            default:
                TaskScheduler.execute(new Runnable() {
                    @Override
                    public void run() {
//                        event.setEventTypeSQLite(BaseSQLiteEvent.EnumSQLiteEventType.ESET_PromotionScope_CreateAsync);
                        //
                        try {
                            bm.setSyncType(BasePresenter.SYNC_Type_C);
                            bm.setSyncDatetime(Constants.getDefaultSyncDatetime2());
                            long id = dao.getPromotionScopeDao().insert((PromotionScope) bm);
                            bm.setID(id);
                            event.setLastErrorCode(ErrorInfo.EnumErrorCode.EC_NoError);
                        } catch (Exception e) {
                            log.info("创建PromotionScope时出错，错误信息：" + e.getMessage());
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
    protected boolean createNAsync(int iUseCaseID, final List<?> list, final BaseSQLiteEvent event) {
        log.info("正在进行PromotionScopePresenter的createNAsync，list=" + (list == null ? null : list.toString()));

        switch (iUseCaseID) {
            default:
                TaskScheduler.execute(new Runnable() {
                    @Override
                    public void run() {
//                        event.setEventTypeSQLite(BaseSQLiteEvent.EnumSQLiteEventType.ESET_PromotionScope_CreateNAsync);
                        //
                        try {
                            for (int i = 0; i < list.size(); i++) {

                                ((PromotionScope) list.get(i)).setSyncDatetime(new Date(System.currentTimeMillis() + NtpHttpBO.TimeDifference));
                                ((PromotionScope) list.get(i)).setSyncType(BasePresenter.SYNC_Type_C);
                                long id = dao.getPromotionScopeDao().insert((PromotionScope) list.get(i));//...插入一部分失败
                                ((PromotionScope) list.get(i)).setID(id);
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
    protected boolean deleteAsync(int iUseCaseID, final BaseModel bm, final BaseSQLiteEvent event) {
        log.info("正在进行PromotionScopePresenter的deleteAsync，bm=" + bm);

        switch (iUseCaseID) {
            default:
                TaskScheduler.execute(new Runnable() {
                    @Override
                    public void run() {
//                        event.setEventTypeSQLite(BaseSQLiteEvent.EnumSQLiteEventType.ESET_PromotionScope_DeleteAsync);
                        //
                        try {
                            dao.getPromotionScopeDao().delete((PromotionScope) bm);
                            event.setLastErrorCode(ErrorInfo.EnumErrorCode.EC_NoError);
                        } catch (Exception e) {
                            log.info("删除PromotionScope时出错，错误信息：" + e.getMessage());
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
        log.info("正在进行PromotionScopePresenter的retrieve1Async，bm=" + bm);

        switch (iUseCaseID) {
            default:
                TaskScheduler.execute(new Runnable() {
                    @Override
                    public void run() {
                        PromotionScope result = null;
//                        event.setEventTypeSQLite(BaseSQLiteEvent.EnumSQLiteEventType.ESET_PromotionScope_Retrieve1Async);
                        //
                        try {
                            result = dao.getPromotionScopeDao().load(((PromotionScope) bm).getID());//...
                            event.setLastErrorCode(ErrorInfo.EnumErrorCode.EC_NoError);
                        } catch (Exception e) {
                            log.info("Retrieve1 PromotionScope时出错，错误信息：" + e.getMessage());
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
    protected boolean retrieveNAsync(int iUseCaseID, final BaseModel bm, final BaseSQLiteEvent event) {
        log.info("正在进行PromotionScopePresenter的retrieveNAsync，bm=" + bm);

        switch (iUseCaseID) {
            case BaseSQLiteBO.CASE_PromotionScope_RetrieveNByConditions:
                TaskScheduler.execute(new Runnable() {
                    @Override
                    public void run() {
                        List<PromotionScope> result = null;
                        //                        event.setID(new Date(System.currentTimeMillis() + NtpHttpBO.TimeDifference).getTime());
//                        event.setEventTypeSQLite(BaseSQLiteEvent.EnumSQLiteEventType.ESET_PromotionScope_RetrieveNAsync);
                        //
                        try {
                            //                            Thread.sleep(1); //令event.setID()的ID惟一
                            result = dao.getPromotionScopeDao().queryRaw(bm.getSql(), bm.getConditions());
                            event.setLastErrorCode(ErrorInfo.EnumErrorCode.EC_NoError);
                        } catch (Exception e) {
                            log.info("RetrieveNAsync RetailTrade出错，错误信息：" + e.getMessage());
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
                        List<PromotionScope> result = null;
                        //                        event.setID(new Date(System.currentTimeMillis() + NtpHttpBO.TimeDifference).getTime());
//                        event.setEventTypeSQLite(BaseSQLiteEvent.EnumSQLiteEventType.ESET_PromotionScope_RetrieveNAsync);
                        //
                        try {
                            Thread.sleep(1); //令event.setID()的ID惟一
                            result = dao.getPromotionScopeDao().loadAll();//...
                            event.setLastErrorCode(ErrorInfo.EnumErrorCode.EC_NoError);
                        } catch (Exception e) {
                            log.info("查询SQLite的表RetailTrade的所有记录时出错，错误信息：" + e.getMessage());
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

    public void drop() {
        try {
            dao.getPromotionScopeDao().dropTable(dao.getPromotionScopeDao().getDatabase(), true);
            lastErrorCode = ErrorInfo.EnumErrorCode.EC_NoError;
        } catch (Exception e) {
            log.info("删除SQLite中的表PromotionScope 时出错，错误信息：" + e.getMessage());
            lastErrorCode = ErrorInfo.EnumErrorCode.EC_OtherError;
        }
    }
}
