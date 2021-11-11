package com.bx.erp.presenter;

import com.bx.erp.bo.BaseSQLiteBO;
import com.bx.erp.bo.NtpHttpBO;
import com.bx.erp.event.UI.BaseSQLiteEvent;
import com.bx.erp.helper.Constants;
import com.bx.erp.model.BaseModel;
import com.bx.erp.model.CommodityShopInfo;
import com.bx.erp.model.DaoSession;
import com.bx.erp.model.ErrorInfo;
import com.silencedut.taskscheduler.TaskScheduler;

import org.apache.log4j.Logger;
import org.greenrobot.eventbus.EventBus;

import java.util.Date;
import java.util.List;

public class CommodityShopInfoPresenter extends BasePresenter {
    private Logger log = Logger.getLogger(this.getClass());

    public CommodityShopInfoPresenter(DaoSession dao) {
        super(dao);
    }

    @Override
    protected String getTableName() {
        return dao.getCommodityShopInfoDao().getTablename();
    }

    @Override
    protected BaseModel deleteNSync(int iUseCaseID, final BaseModel bm) {
        dao.getCommodityShopInfoDao().deleteAll();
        return null;
    }

    @Override
    protected List<BaseModel> createNSync(int iUseCaseID, final List<?> list) {
        log.info("正在进行CommodityShopInfoPresenter的createNSync，list=" + (list == null ? null : list.toString()));

        switch (iUseCaseID) {
            default:
                try {
                    for (int i = 0; i < list.size(); i++) {
                        ((CommodityShopInfo) list.get(i)).setSyncDatetime(Constants.getDefaultSyncDatetime2());
                        ((CommodityShopInfo) list.get(i)).setSyncType(BasePresenter.SYNC_Type_C);
                        long id = dao.getCommodityShopInfoDao().insert((CommodityShopInfo) list.get(i));
                        ((CommodityShopInfo) list.get(i)).setID(id);
                        log.info("CommodityShopInfo创建成功，ID=" + id);

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
        log.info("正在进行CommodityShopInfoPresenter的createSync，bm=" + bm);

        try {
            CommodityShopInfo commodityShopInfo = (CommodityShopInfo) bm;
            commodityShopInfo.setSyncType(BasePresenter.SYNC_Type_C);
            if (commodityShopInfo.getSyncDatetime() == null) {
                commodityShopInfo.setSyncDatetime(Constants.getDefaultSyncDatetime2());
            }
            long id = dao.getCommodityShopInfoDao().insert(commodityShopInfo);
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
        log.info("正在进行CommodityShopInfoPresenter的retrieve1Sync，bm=" + bm);

        try {
            lastErrorCode = ErrorInfo.EnumErrorCode.EC_NoError;

            return dao.getCommodityShopInfoDao().load(bm.getID());
        } catch (Exception e) {
            log.info(e.getMessage());
            e.printStackTrace();
            lastErrorCode = ErrorInfo.EnumErrorCode.EC_OtherError;

            return null;
        }
    }

    @Override
    protected List<?> retrieveNSync(int iUseCaseID, BaseModel bm) {
        log.info("正在进行CommodityShopInfoPresenter的retrieveNSync，bm=" + bm);

        switch (iUseCaseID) {
            case BaseSQLiteBO.CASE_CommodityShopInfo_RetrieveNByConditions:
                try {
                    lastErrorCode = ErrorInfo.EnumErrorCode.EC_NoError;

                    return dao.getCommodityShopInfoDao().queryRaw(bm.getSql(), bm.getConditions());
                } catch (Exception e) {
                    log.info(e.getMessage());
                    e.printStackTrace();
                    lastErrorCode = ErrorInfo.EnumErrorCode.EC_OtherError;

                    return null;
                }
            default:
                try {
                    lastErrorCode = ErrorInfo.EnumErrorCode.EC_NoError;

                    return dao.getCommodityShopInfoDao().loadAll();
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
        log.info("正在进行CommodityShopInfoPresenter的deleteSync，bm=" + bm);

        try {
            dao.getCommodityShopInfoDao().deleteByKey(bm.getID());
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
        log.info("正在进行CommodityShopInfoPresenter的createAsync，bm=" + bm);

        switch (iUseCaseID) {
            default:
                TaskScheduler.execute(new Runnable() {
                    @Override
                    public void run() {
                        //
                        try {
                            bm.setSyncType(BasePresenter.SYNC_Type_C);
                            bm.setSyncDatetime(Constants.getDefaultSyncDatetime2());
                            long id = dao.getCommodityShopInfoDao().insert((CommodityShopInfo) bm);
                            bm.setID(id);
                            event.setLastErrorCode(ErrorInfo.EnumErrorCode.EC_NoError);
                        } catch (Exception e) {
                            log.info("创建CommodityShopInfo时出错，错误信息：" + e.getMessage());
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
        log.info("正在进行CommodityShopInfoPresenter的createNAsync，list=" + (list == null ? null : list.toString()));

        switch (iUseCaseID) {
            default:
                TaskScheduler.execute(new Runnable() {
                    @Override
                    public void run() {
                        //
                        try {
                            for (int i = 0; i < list.size(); i++) {

                                ((CommodityShopInfo) list.get(i)).setSyncDatetime(new Date(System.currentTimeMillis() + NtpHttpBO.TimeDifference));
                                ((CommodityShopInfo) list.get(i)).setSyncType(BasePresenter.SYNC_Type_C);
                                long id = dao.getCommodityShopInfoDao().insert((CommodityShopInfo) list.get(i));//...插入一部分失败
                                ((CommodityShopInfo) list.get(i)).setID(id);
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
        log.info("正在进行CommodityShopInfoPresenter的deleteAsync，bm=" + bm);

        switch (iUseCaseID) {
            default:
                TaskScheduler.execute(new Runnable() {
                    @Override
                    public void run() {
                        //
                        try {
                            dao.getCommodityShopInfoDao().delete((CommodityShopInfo) bm);
                            event.setLastErrorCode(ErrorInfo.EnumErrorCode.EC_NoError);
                        } catch (Exception e) {
                            log.info("删除CommodityShopInfo时出错，错误信息：" + e.getMessage());
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
        log.info("正在进行CommodityShopInfoPresenter的retrieve1Async，bm=" + bm);

        switch (iUseCaseID) {
            default:
                TaskScheduler.execute(new Runnable() {
                    @Override
                    public void run() {
                        CommodityShopInfo result = null;
                        //
                        try {
                            result = dao.getCommodityShopInfoDao().load(((CommodityShopInfo) bm).getID());//...
                            event.setLastErrorCode(ErrorInfo.EnumErrorCode.EC_NoError);
                        } catch (Exception e) {
                            log.info("Retrieve1 CommodityShopInfo时出错，错误信息：" + e.getMessage());
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
        log.info("正在进行CommodityShopInfoPresenter的retrieveNAsync，bm=" + bm);

        switch (iUseCaseID) {
            case BaseSQLiteBO.CASE_CommodityShopInfo_RetrieveNByConditions:
                TaskScheduler.execute(new Runnable() {
                    @Override
                    public void run() {
                        List<CommodityShopInfo> result = null;
                        //                        event.setID(new Date(System.currentTimeMillis() + NtpHttpBO.TimeDifference).getTime());
                        //
                        try {
                            //                            Thread.sleep(1); //令event.setID()的ID惟一
                            result = dao.getCommodityShopInfoDao().queryRaw(bm.getSql(), bm.getConditions());
                            event.setLastErrorCode(ErrorInfo.EnumErrorCode.EC_NoError);
                        } catch (Exception e) {
                            log.info("RetrieveNAsync CommodityShopInfo出错，错误信息：" + e.getMessage());
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
                        List<CommodityShopInfo> result = null;
                        //                        event.setID(new Date(System.currentTimeMillis() + NtpHttpBO.TimeDifference).getTime());
                        //
                        try {
                            Thread.sleep(1); //令event.setID()的ID惟一
                            result = dao.getCommodityShopInfoDao().loadAll();//...
                            event.setLastErrorCode(ErrorInfo.EnumErrorCode.EC_NoError);
                        } catch (Exception e) {
                            log.info("查询SQLite的表CommodityShopInfo的所有记录时出错，错误信息：" + e.getMessage());
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
            dao.getCommodityShopInfoDao().dropTable(dao.getCommodityShopInfoDao().getDatabase(), true);
            lastErrorCode = ErrorInfo.EnumErrorCode.EC_NoError;
        } catch (Exception e) {
            log.info("删除SQLite中的表CommodityShopInfo 时出错，错误信息：" + e.getMessage());
            lastErrorCode = ErrorInfo.EnumErrorCode.EC_OtherError;
        }
    }
}
