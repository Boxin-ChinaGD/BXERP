package com.bx.erp.presenter;

import android.util.Log;

import com.bx.erp.bo.BaseSQLiteBO;
import com.bx.erp.bo.NtpHttpBO;
import com.bx.erp.event.BaseEvent;
import com.bx.erp.event.UI.BaseSQLiteEvent;
import com.bx.erp.model.BaseModel;
import com.bx.erp.model.CommodityCategory;
import com.bx.erp.model.DaoSession;
import com.bx.erp.model.ErrorInfo;
import com.silencedut.taskscheduler.TaskScheduler;

import org.apache.log4j.Logger;
import org.greenrobot.eventbus.EventBus;

import java.util.Date;
import java.util.List;

import javax.inject.Inject;

import static com.bx.erp.bo.BaseSQLiteBO.CASE_Category_RetrieveNByConditions;

public class CommodityCategoryPresenter extends BasePresenter {
    private Logger log = Logger.getLogger(this.getClass());

    @Inject
    public CommodityCategoryPresenter(final DaoSession dao) {
        super(dao);
    }

    @Override
    protected List<BaseModel> createNSync(int iUseCaseID, final List<?> list) {
        log.info("正在进行CommodityCategoryPresenter的createNSync，list=" + (list == null ? null : list.toString()));

        switch (iUseCaseID) {
            default:
                try {
                    for (int i = 0; i < list.size(); i++) {
                        CommodityCategory c = (CommodityCategory) list.get(i);
                        long id = dao.getCommodityCategoryDao().insert(c);
                        ((CommodityCategory) list.get(i)).setID(id);
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
    protected BaseModel createSync(int iUseCaseID, BaseModel bm) {
        log.info("正在进行CommodityCategoryPresenter的createSync，bm=" +(bm == null ? null : bm.toString()));

        try {
            bm.setSyncType(BasePresenter.SYNC_Type_C);
            bm.setSyncDatetime(new Date(System.currentTimeMillis() + NtpHttpBO.TimeDifference));
            long id = dao.getCommodityCategoryDao().insert((CommodityCategory) bm);
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
        log.info("正在进行CommodityCategoryPresenter的updateSync，bm=" +(bm == null ? null : bm.toString()));

        try {
            bm.setSyncDatetime(new Date(System.currentTimeMillis() + NtpHttpBO.TimeDifference));
            bm.setSyncType(BasePresenter.SYNC_Type_U);
            dao.getCommodityCategoryDao().update((CommodityCategory) bm);
            lastErrorCode = ErrorInfo.EnumErrorCode.EC_NoError;

            return true;
        } catch (Exception e) {
            log.info("执行updateSync失败，错误信息为" + e.getMessage());
            e.printStackTrace();
            lastErrorCode = ErrorInfo.EnumErrorCode.EC_NoError;

            return false;
        }
    }

    @Override
    protected BaseModel retrieve1Sync(int iUseCaseID, BaseModel bm) {
        log.info("正在进行CommodityCategoryPresenter的retrieve1Sync，bm=" +(bm == null ? null : bm.toString()));

        try {
            lastErrorCode = ErrorInfo.EnumErrorCode.EC_NoError;

            return dao.getCommodityCategoryDao().load(bm.getID());
        } catch (Exception e) {
            log.info("执行retrieve1Sync失败，错误信息为" + e.getMessage());
            e.printStackTrace();
            lastErrorCode = ErrorInfo.EnumErrorCode.EC_OtherError;

            return null;
        }
    }

    @Override
    protected List<?> retrieveNSync(int iUseCaseID, BaseModel bm) {
        log.info("正在进行CommodityCategoryPresenter的retrieveNSync，bm=" +(bm == null ? null : bm.toString()));

        switch (iUseCaseID) {
            case CASE_Category_RetrieveNByConditions:
                try {
                    lastErrorCode = ErrorInfo.EnumErrorCode.EC_NoError;

                    return dao.getCommodityCategoryDao().queryRaw(bm.getSql(), bm.getConditions());
                } catch (Exception e) {
                    log.info("执行retrieveNSync的CASE_Category_RetrieveNByConditions分支时出现异常，错误信息为" + e.getMessage());
                    e.printStackTrace();
                    lastErrorCode = ErrorInfo.EnumErrorCode.EC_OtherError;

                    return null;
                }
            default:
                try {
                    lastErrorCode = ErrorInfo.EnumErrorCode.EC_NoError;

                    return dao.getCommodityCategoryDao().loadAll();
                } catch (Exception e) {
                    log.info("执行retrieveNSync失败，错误信息为" + e.getMessage());
                    e.printStackTrace();
                    lastErrorCode = ErrorInfo.EnumErrorCode.EC_NoError;

                    return null;
                }
        }
    }

    @Override
    protected BaseModel deleteSync(int iUseCaseID, BaseModel bm) {
        log.info("正在进行CommodityCategoryPresenter的deleteSync，bm=" +(bm == null ? null : bm.toString()));

        try {
            dao.getCommodityCategoryDao().deleteByKey(bm.getID());
            lastErrorCode = ErrorInfo.EnumErrorCode.EC_NoError;
        } catch (Exception e) {
            log.info("执行deleteSync时失败，错误信息为" + e.getMessage());
            e.printStackTrace();
            lastErrorCode = ErrorInfo.EnumErrorCode.EC_OtherError;
        }

        return null;
    }

    @Override
    protected BaseModel deleteNSync(int iUserCaseID, BaseModel bm) {
        log.info("正在进行CommodityCategoryPresenter的deleteNSync，bm=" +(bm == null ? null : bm.toString()));

        try {
            dao.getCommodityCategoryDao().deleteAll();
            lastErrorCode = ErrorInfo.EnumErrorCode.EC_NoError;
        } catch (Exception e) {
            log.info("执行deleteNSync时失败，错误信息为" + e.getMessage());
            e.printStackTrace();
            lastErrorCode = ErrorInfo.EnumErrorCode.EC_OtherError;
        }

        return null;
    }

    @Override
    protected boolean refreshByServerDataAsync(final int iUseCaseID, final List<?> bmNewList, final BaseSQLiteEvent event) {
        log.info("正在进行CommodityCategoryPresenter的refreshByServerDataAsync，bmNewList=" + (bmNewList == null ? null : bmNewList.toString()));

        event.setStatus(BaseEvent.EnumEventStatus.EES_SQLite_ToApplyServerData);
        switch (iUseCaseID) {
            default:
                TaskScheduler.execute(new Runnable() {
                    @Override
                    public void run() {
                        log.info("已经得到服务器返回的需要同步的Category数据, 准备进行同步...");
                        List<BaseModel> bmList = (List<BaseModel>) bmNewList;
                        try {
                            if (bmList != null) {
                                for (int i = 0; i < bmList.size(); i++) {
                                    String CategoryType = bmList.get(i).getSyncType();

                                    if (BasePresenter.SYNC_Type_C.equals(CategoryType)) {
                                        CommodityCategory commodityCategory = (CommodityCategory) retrieve1Sync(iUseCaseID, bmList.get(i));
                                        if (commodityCategory != null) {
                                            deleteSync(BaseSQLiteBO.INVALID_CASE_ID, commodityCategory);
                                        }
                                        //
                                        createSync(iUseCaseID, bmList.get(i));

                                        log.info("服务器返回的C型Category数据成功同步到SQLite中!");
                                    } else if (BasePresenter.SYNC_Type_U.equals(CategoryType)) {
                                        updateSync(iUseCaseID, bmList.get(i));

                                        log.info("服务器返回的U型Category数据成功同步到SQLite中!");
                                    } else if (BasePresenter.SYNC_Type_D.equals(CategoryType)) {
                                        CommodityCategory commodityCategory = (CommodityCategory) retrieve1Sync(iUseCaseID, bmList.get(i));
                                        if (commodityCategory != null) {
                                            deleteSync(BaseSQLiteBO.INVALID_CASE_ID, commodityCategory);
                                        }

                                        log.info("服务器返回的D型Category数据成功同步到SQLite中!");
                                    }
                                    event.setLastErrorCode(ErrorInfo.EnumErrorCode.EC_NoError);
                                    event.setListMasterTable(bmList);
                                }
                            } else {
                                event.setLastErrorCode(ErrorInfo.EnumErrorCode.EC_NoError);
                                event.setListMasterTable(bmList);
                            }
                        } catch (Exception e) {
                            log.info("同步Category失败，错误信息为" + e.getMessage());
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
    protected boolean refreshByServerDataAsyncC(final int iUseCaseID, final List<?> bmNewList, final BaseSQLiteEvent event) {
        log.info("正在进行CommodityCategoryPresenter的refreshByServerDataAsyncC，bmNewList=" + (bmNewList == null ? null : bmNewList.toString()));

        event.setStatus(BaseEvent.EnumEventStatus.EES_SQLite_ToApplyServerData);
        switch (iUseCaseID) {
            default:
                TaskScheduler.execute(new Runnable() {
                    @Override
                    public void run() {
                        log.info("已经得到服务器返回的需要同步的Category数据, 准备进行同步...");

//                        event.setEventTypeSQLite(BaseSQLiteEvent.EnumSQLiteEventType.ESET_CommodityCategory_RefreshByServerDataAsyncC_Done);

                        //删除本地所有的数据
                        deleteNSync(iUseCaseID, null);
                        if (getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError) {
                            //同步所有服务器返回的数据
                            createNSync(iUseCaseID, bmNewList);
                            if (getLastErrorCode() != ErrorInfo.EnumErrorCode.EC_NoError) {
                                event.setLastErrorCode(ErrorInfo.EnumErrorCode.EC_OtherError);
                            } else {
                                event.setLastErrorCode(ErrorInfo.EnumErrorCode.EC_NoError);
                            }

                        } else {
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
        log.info("正在进行CommodityCategoryPresenter的createNAsync，list=" + (list == null ? null : list.toString()));

        switch (iUseCaseID) {
            default:
                TaskScheduler.execute(new Runnable() {
                    @Override
                    public void run() {
//                        event.setEventTypeSQLite(BaseSQLiteEvent.EnumSQLiteEventType.ESET_CommodityCategory_CreateNAsync);
                        //
                        try {
                            for (int i = 0; i < list.size(); i++) {
                                CommodityCategory c = (CommodityCategory) list.get(i);
                                c.setSyncDatetime(new Date(System.currentTimeMillis() + NtpHttpBO.TimeDifference));
                                c.setSyncType(BasePresenter.SYNC_Type_C);
                                long id = dao.getCommodityCategoryDao().insert(c);//...插入一部分失败
                                ((CommodityCategory) list.get(i)).setID(id);
                            }
                            event.setLastErrorCode(ErrorInfo.EnumErrorCode.EC_NoError);
                        } catch (Exception e) {
                            log.info("创建Category时出错，错误信息：" + e.getMessage());
                            event.setLastErrorCode(ErrorInfo.EnumErrorCode.EC_OtherError);
                            log.info("创建Category时出错2，错误信息：" + event.getLastErrorCode());
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
        log.info("正在进行CommodityCategoryPresenter的createAsync，bm=" +(bm == null ? null : bm.toString()));

        switch (iUseCaseID) {
            default:
                TaskScheduler.execute(new Runnable() {
                    @Override
                    public void run() {
//                        event.setEventTypeSQLite(BaseSQLiteEvent.EnumSQLiteEventType.ESET_CommodityCategory_CreateAsync);
                        //
                        try {
                            bm.setSyncType(BasePresenter.SYNC_Type_C);
                            bm.setSyncDatetime(new Date(System.currentTimeMillis() + NtpHttpBO.TimeDifference));
                            long id = dao.getCommodityCategoryDao().insert((CommodityCategory) bm);
                            ((CommodityCategory) bm).setID(id);
                            event.setLastErrorCode(ErrorInfo.EnumErrorCode.EC_NoError);
                        } catch (Exception e) {
                            Log.e("输出：", "创建Category时出错，错误信息：" + e.getMessage());
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
        log.info("正在进行CommodityCategoryPresenter的deleteAsync，bm=" +(bm == null ? null : bm.toString()));

        switch (iUseCaseID) {
            default:
                TaskScheduler.execute(new Runnable() {
                    @Override
                    public void run() {
                        event.setEventTypeSQLite(BaseSQLiteEvent.EnumSQLiteEventType.ESET_CommodityCategory_DeleteAsync);
                        //
                        try {
                            dao.getCommodityCategoryDao().delete((CommodityCategory) bm);
                            event.setLastErrorCode(ErrorInfo.EnumErrorCode.EC_NoError);
                        } catch (Exception e) {
                            Log.e("输出：", "删除Category时出错，错误信息：" + e.getMessage());
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
        log.info("正在进行CommodityCategoryPresenter的updateAsync，bm=" +(bm == null ? null : bm.toString()));

        switch (iUseCaseID) {
            default:
                TaskScheduler.execute(new Runnable() {
                    @Override
                    public void run() {
                        event.setEventTypeSQLite(BaseSQLiteEvent.EnumSQLiteEventType.ESET_CommodityCategory_UpdateAsync);
                        //
                        try {
                            bm.setSyncType(BasePresenter.SYNC_Type_U);
                            bm.setSyncDatetime(new Date(System.currentTimeMillis() + NtpHttpBO.TimeDifference));
                            dao.getCommodityCategoryDao().update((CommodityCategory) bm);
                            event.setLastErrorCode(ErrorInfo.EnumErrorCode.EC_NoError);
                        } catch (Exception e) {
                            log.info("更新Category时出错，错误信息：" + e.getMessage());
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
        log.info("正在进行CommodityCategoryPresenter的retrieveNAsync，bm=" +(bm == null ? null : bm.toString()));

        switch (iUseCaseID) {
            default:
                TaskScheduler.execute(new Runnable() {
                    @Override
                    public void run() {
                        List<CommodityCategory> result = null;
                        event.setEventTypeSQLite(BaseSQLiteEvent.EnumSQLiteEventType.ESET_CommodityCategory_RetrieveNAsync);
                        try {
                            Thread.sleep(1); //令event.setID()的ID惟一
                            result = dao.getCommodityCategoryDao().loadAll();//...
                            event.setLastErrorCode(ErrorInfo.EnumErrorCode.EC_NoError);
                        } catch (Exception e) {
                            log.info("查询SQLite的表Category的所有记录时出错，错误信息：" + e.getMessage());
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
}
