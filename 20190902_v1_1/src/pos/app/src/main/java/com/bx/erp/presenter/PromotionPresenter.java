package com.bx.erp.presenter;

import android.util.Log;

import com.bx.erp.bo.BaseSQLiteBO;
import com.bx.erp.bo.NtpHttpBO;
import com.bx.erp.common.GlobalController;
import com.bx.erp.event.BaseEvent;
import com.bx.erp.event.UI.BaseSQLiteEvent;
import com.bx.erp.model.BaseModel;
import com.bx.erp.model.DaoSession;
import com.bx.erp.model.ErrorInfo;
import com.bx.erp.model.Promotion;
import com.bx.erp.model.PromotionScope;
import com.bx.erp.model.PromotionShopScope;
import com.silencedut.taskscheduler.TaskScheduler;

import org.apache.log4j.Logger;
import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.locks.ReentrantLock;

import javax.inject.Inject;

import static com.bx.erp.bo.BaseSQLiteBO.CASE_Promotion_RetrieveNByConditions;

public class PromotionPresenter extends BasePresenter {
    private Logger log = Logger.getLogger(this.getClass());
    private ReentrantLock lock = new ReentrantLock();

    @Inject
    public PromotionPresenter(final DaoSession dao) {
        super(dao);
    }

    @Override
    protected List<BaseModel> createNSync(int iUseCaseID, final List<?> list) {
        log.info("正在进行PromotionPresenter的createOrReplaceAsync，list=" + (list == null ? null : list.toString()));

        switch (iUseCaseID) {
            default:
                try {
                    for (int i = 0; i < list.size(); i++) {
                        Promotion c = (Promotion) list.get(i);
                        long id = dao.getPromotionDao().insert(c);
                        ((Promotion) list.get(i)).setID(id);
                    }
                    lastErrorCode = ErrorInfo.EnumErrorCode.EC_NoError;
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
        log.info("正在进行PromotionPresenter的createSync，bm=" + bm);

        try {
            bm.setSyncType(BasePresenter.SYNC_Type_C);
            bm.setSyncDatetime(new Date(System.currentTimeMillis() + NtpHttpBO.TimeDifference));
            long id = dao.getPromotionDao().insert((Promotion) bm);
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
        log.info("正在进行PromotionPresenter的updateSync，bm=" + bm);

        try {
            bm.setSyncDatetime(new Date(System.currentTimeMillis() + NtpHttpBO.TimeDifference));
            bm.setSyncType(BasePresenter.SYNC_Type_U);
            dao.getPromotionDao().update((Promotion) bm);
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
        log.info("正在进行PromotionPresenter的retrieve1Sync，bm=" + bm);

        try {
            dao.clear();
            lastErrorCode = ErrorInfo.EnumErrorCode.EC_NoError;

            return dao.getPromotionDao().load(bm.getID());
        } catch (Exception e) {
            log.info("执行retrieve1Sync失败，错误信息为" + e.getMessage());
            e.printStackTrace();
            lastErrorCode = ErrorInfo.EnumErrorCode.EC_OtherError;

            return null;
        }
    }

    @Override
    protected List<?> retrieveNSync(int iUseCaseID, BaseModel bm) {
        log.info("正在进行PromotionPresenter的retrieveNSync，bm=" + bm);

        switch (iUseCaseID) {
            case CASE_Promotion_RetrieveNByConditions:
                try {
                    lastErrorCode = ErrorInfo.EnumErrorCode.EC_NoError;

                    return dao.getPromotionDao().queryRaw(bm.getSql(), bm.getConditions());
                } catch (Exception e) {
                    log.info("执行retrieveNSync的CASE_Promotion_RetrieveNByConditions失败，错误信息为" + e.getMessage());
                    e.printStackTrace();
                    lastErrorCode = ErrorInfo.EnumErrorCode.EC_OtherError;

                    return null;
                }
            default:
                try {
                    dao.clear();
                    lastErrorCode = ErrorInfo.EnumErrorCode.EC_NoError;

                    List<Promotion> promotionList = dao.getPromotionDao().loadAll();
                    for (int i = 0; i < promotionList.size(); i++) {
                        PromotionScope promotionScope = new PromotionScope();
                        promotionScope.setSql("where F_PromotionID = ?");
                        promotionScope.setConditions(new String[]{String.valueOf(promotionList.get(i).getID())});
                        List<PromotionScope> promotionScopeList = (List<PromotionScope>) GlobalController.getInstance().getPromotionScopePresenter().retrieveNSync(BaseSQLiteBO.CASE_PromotionScope_RetrieveNByConditions, promotionScope);
                        promotionList.get(i).setListSlave1(promotionScopeList);
                    }

                    return promotionList;
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
        log.info("正在进行PromotionPresenter的deleteSync，bm=" + bm);

        try {
            dao.getPromotionDao().deleteByKey(bm.getID());
            lastErrorCode = ErrorInfo.EnumErrorCode.EC_NoError;
        } catch (Exception e) {
            log.info("执行deleteSync失败，错误信息为" + e.getMessage());
            e.printStackTrace();
            lastErrorCode = ErrorInfo.EnumErrorCode.EC_OtherError;
        }

        return null;
    }

    @Override
    protected BaseModel deleteNSync(int iUserCaseID, BaseModel bm) {
        log.info("正在进行PromotionPresenter的deleteNSync，bm=" + bm);

        try {
            dao.getPromotionDao().deleteAll();
            lastErrorCode = ErrorInfo.EnumErrorCode.EC_NoError;
        } catch (Exception e) {
            log.info("执行deleteNSync失败，错误信息为" + e.getMessage());
            e.printStackTrace();
            lastErrorCode = ErrorInfo.EnumErrorCode.EC_OtherError;
        }

        return null;
    }

    @Override
    protected boolean refreshByServerDataAsync(final int iUseCaseID, final List<?> bmNewList, final BaseSQLiteEvent event) {
        log.info("正在进行PromotionPresenter的refreshByServerDataAsync，bmNewList=" + (bmNewList == null ? null : bmNewList.toString()));

        event.setStatus(BaseEvent.EnumEventStatus.EES_SQLite_ToApplyServerData);
        switch (iUseCaseID) {
            default:
                TaskScheduler.execute(new Runnable() {
                    @Override
                    public void run() {
                        log.info("已经得到服务器返回的需要同步的Promotion数据, 准备进行同步...");
                        List<BaseModel> bmList = (List<BaseModel>) bmNewList;
//                        event.setEventTypeSQLite(BaseSQLiteEvent.EnumSQLiteEventType.ESET_Promotion_RefreshByServerDataAsync_Done);
                        try {
                            if (bmList != null) {
                                for (int i = 0; i < bmList.size(); i++) {
                                    String PromotionType = bmList.get(i).getSyncType();

                                    //根据ID找到对应的主从表，对主从表进行相应的CUD操作
                                    if (BasePresenter.SYNC_Type_C.equals(PromotionType)) {
                                        //根据ID找到主表
                                        Promotion oldPromotion = (Promotion) retrieve1Sync(iUseCaseID, bmList.get(i));
                                        //根据PromotionID找到从表并删除从表
                                        if (oldPromotion != null) {
                                            List<PromotionScope> promotionScopeList = new ArrayList<PromotionScope>();
                                            promotionScopeList = dao.getPromotionScopeDao().queryRaw("where F_PromotionID = ?", new String[]{String.valueOf(oldPromotion.getID())});
                                            if (promotionScopeList.size() > 0) {
                                                for (int j = 0; j < promotionScopeList.size(); j++) {
                                                    dao.getPromotionScopeDao().delete(promotionScopeList.get(j));
                                                }
                                            }
                                            // 门店
                                            List<PromotionShopScope> promotionShopScopeList = new ArrayList<>();
                                            promotionShopScopeList = dao.getPromotionShopScopeDao().queryRaw("where F_PromotionID = ?", new String[]{String.valueOf(oldPromotion.getID())});
                                            if (promotionShopScopeList.size() > 0) {
                                                for (int j = 0; j < promotionShopScopeList.size(); j++) {
                                                    dao.getPromotionShopScopeDao().delete(promotionShopScopeList.get(j));
                                                }
                                            }
                                            //删除主表
                                            deleteSync(BaseSQLiteBO.INVALID_CASE_ID, oldPromotion);
                                        }
                                        //
                                        Promotion newPromotion = (Promotion) bmList.get(i);
                                        //插入主表
                                        createSync(iUseCaseID, newPromotion);
                                        //插入从表
                                        List<PromotionScope> promotionScopeList = new ArrayList<PromotionScope>();
                                        promotionScopeList = (List<PromotionScope>) newPromotion.getListSlave1();
                                        if (promotionScopeList.size() > 0) {
                                            for (int j = 0; j < promotionScopeList.size(); j++) {
                                                dao.getPromotionScopeDao().insert(promotionScopeList.get(j));
                                            }
                                        }
                                        //插入从表
                                        List<PromotionShopScope> promotionShopScopeList = new ArrayList<>();
                                        promotionShopScopeList = (List<PromotionShopScope>) newPromotion.getListSlave2();
                                        if (promotionShopScopeList.size() > 0) {
                                            for (int j = 0; j < promotionShopScopeList.size(); j++) {
                                                dao.getPromotionShopScopeDao().insert(promotionShopScopeList.get(j));
                                            }
                                        }

                                        log.info("服务器返回的C型Promotion数据成功同步到SQLite中!");
                                    } else if (BasePresenter.SYNC_Type_U.equals(PromotionType)) {
                                        Promotion promotion = (Promotion) bmList.get(i);
                                        //修改主表
                                        updateSync(iUseCaseID, promotion);
                                        //修改从表
                                        List<PromotionScope> promotionScopeList = new ArrayList<PromotionScope>();
                                        promotionScopeList = (List<PromotionScope>) promotion.getListSlave1();
                                        for (int j = 0; j < promotionScopeList.size(); j++) {
                                            dao.getPromotionScopeDao().update(promotionScopeList.get(j));
                                        }
                                        List<PromotionShopScope> promotionShopScopeList = new ArrayList<>();
                                        promotionShopScopeList = (List<PromotionShopScope>) promotion.getListSlave2();
                                        for (int j = 0; j < promotionShopScopeList.size(); j++) {
                                            dao.getPromotionShopScopeDao().update(promotionShopScopeList.get(j));
                                        }
                                        log.info("服务器返回的U型Promotion数据成功同步到SQLite中!");
                                    } else if (BasePresenter.SYNC_Type_D.equals(PromotionType)) {
                                        Promotion promotion = (Promotion) retrieve1Sync(iUseCaseID, bmList.get(i));
                                        List<PromotionScope> promotionScopeList = new ArrayList<PromotionScope>();
                                        List<PromotionShopScope> promotionShopScopeList = new ArrayList<>();
                                        if (promotion != null) {
                                            promotionScopeList = (List<PromotionScope>) promotion.getListSlave1();
                                            promotionShopScopeList = (List<PromotionShopScope>) promotion.getListSlave2();
                                            //删除从表
                                            if (promotionScopeList != null && promotionScopeList.size() > 0) {
                                                for (int j = 0; j < promotionScopeList.size(); j++) {
                                                    dao.getPromotionScopeDao().delete(promotionScopeList.get(j));
                                                }
                                            }
                                            if (promotionShopScopeList != null && promotionShopScopeList.size() > 0) {
                                                for (int j = 0; j < promotionShopScopeList.size(); j++) {
                                                    dao.getPromotionShopScopeDao().delete(promotionShopScopeList.get(j));
                                                }
                                            }
                                            if (promotion != null) {
                                                deleteSync(BaseSQLiteBO.INVALID_CASE_ID, promotion);
                                            }
                                            log.info("服务器返回的D型Promotion数据成功同步到SQLite中!");
                                        } else {
                                            log.info("本地找不到需要删除的Promotion");
                                        }
                                    }
                                    event.setLastErrorCode(ErrorInfo.EnumErrorCode.EC_NoError);
                                    event.setListMasterTable(bmList);
                                }
                            } else {
                                event.setLastErrorCode(ErrorInfo.EnumErrorCode.EC_NoError);
                                event.setListMasterTable(bmList);
                            }
                        } catch (Exception e) {
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
        log.info("正在进行PromotionPresenter的refreshByServerDataAsyncC，bmNewList=" + (bmNewList == null ? null : bmNewList.toString()));

        event.setStatus(BaseEvent.EnumEventStatus.EES_SQLite_ToApplyServerData);
        switch (iUseCaseID) {
            default:
                TaskScheduler.execute(new Runnable() {
                    @Override
                    public void run() {
                        lock.lock();
                        {
                            try {
                                log.info("已经得到服务器返回的需要同步的PromotionInfo数据, 准备进行同步...");

//                        event.setEventTypeSQLite(BaseSQLiteEvent.EnumSQLiteEventType.ESET_Promotion_RefreshByServerDataAsync_Done);
                                log.debug("++++++++++++++++++++++++++ 开始清空 promotion 和 promotionscope 表, 线程名称" + Thread.currentThread().getName());
                                //删除本地所有的数据
                                GlobalController.getInstance().getPromotionScopePresenter().deleteNSync(iUseCaseID, null);
                                GlobalController.getInstance().getPromotionShopScopePresenter().deleteNSync(iUseCaseID, null);
                                deleteNSync(iUseCaseID, null);
                                if (getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError) {
                                    //同步所有服务器返回的数据
//                            createNSync(iUseCaseID, bmNewList);
                                    if (bmNewList != null) {
                                        for (int i = 0; i < bmNewList.size(); i++) {
                                            Promotion promotion = (Promotion) bmNewList.get(i);
                                            dao.getPromotionDao().insert(promotion);
                                            log.debug("促销=" + promotion);
                                            for (int j = 0; j < ((Promotion) bmNewList.get(i)).getListSlave1().size(); j++) {
                                                PromotionScope promotionScope = (PromotionScope) ((Promotion) bmNewList.get(i)).getListSlave1().get(j);
                                                log.debug("促销范围：" + dao.getPromotionScopeDao().insert(promotionScope));
                                            }
                                            for (int j = 0; j < ((Promotion) bmNewList.get(i)).getListSlave2().size(); j++) {
                                                PromotionShopScope promotionShopScope = (PromotionShopScope) ((Promotion) bmNewList.get(i)).getListSlave2().get(j);
                                                log.debug("促销范围：" + dao.getPromotionShopScopeDao().insert(promotionShopScope));
                                            }
                                        }
                                    } else {
                                        log.info("服务器没有promotion返回");
                                    }
                                    log.debug("所有的从表信息有：" + GlobalController.getInstance().getPromotionScopePresenter().retrieveNSync(BaseSQLiteBO.INVALID_CASE_ID, null));
                                    if (getLastErrorCode() != ErrorInfo.EnumErrorCode.EC_NoError) {
                                        event.setLastErrorCode(ErrorInfo.EnumErrorCode.EC_OtherError);
                                    }
                                } else {
                                    event.setLastErrorCode(ErrorInfo.EnumErrorCode.EC_OtherError);
                                }

                                event.setLastErrorCode(ErrorInfo.EnumErrorCode.EC_NoError);
                                event.setStatus(BaseEvent.EnumEventStatus.EES_SQLite_DoneApplyServerData);
                            } catch (Exception e) {
                                log.debug("异常：" + e.getMessage());
                            }
                        }
                        lock.unlock();

                        EventBus.getDefault().post(event);
                    }
                });
                break;
        }
        return true;
    }

    @Override
    protected boolean createNAsync(int iUseCaseID, final List<?> list, final BaseSQLiteEvent event) {
        log.info("正在进行PromotionPresenter的createNAsync，list=" + (list == null ? null : list.toString()));

        switch (iUseCaseID) {
            default:
                TaskScheduler.execute(new Runnable() {
                    @Override
                    public void run() {
//                        event.setEventTypeSQLite(BaseSQLiteEvent.EnumSQLiteEventType.ESET_Promotion_CreateNAsync);
                        //
                        try {
                            for (int i = 0; i < list.size(); i++) {
                                Promotion p = (Promotion) list.get(i);
                                p.setSyncDatetime(new Date(System.currentTimeMillis() + NtpHttpBO.TimeDifference));
                                p.setSyncType(BasePresenter.SYNC_Type_C);
                                long id = dao.getPromotionDao().insert(p);//...插入一部分失败
                                ((Promotion) list.get(i)).setID(id);
                            }
                            event.setLastErrorCode(ErrorInfo.EnumErrorCode.EC_NoError);
                        } catch (Exception e) {
                            log.info("创建Promotion时出错，错误信息：" + e.getMessage());
                            event.setLastErrorCode(ErrorInfo.EnumErrorCode.EC_OtherError);
                            log.info("创建Promotion时出错2，错误信息：" + event.getLastErrorCode());
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
        log.info("正在进行PromotionPresenter的createAsync，bm=" + bm);

        switch (iUseCaseID) {
            default:
                TaskScheduler.execute(new Runnable() {
                    @Override
                    public void run() {
//                        event.setEventTypeSQLite(BaseSQLiteEvent.EnumSQLiteEventType.ESET_Promotion_CreateAsync);
                        //
                        try {
                            bm.setSyncType(BasePresenter.SYNC_Type_C);
                            bm.setSyncDatetime(new Date(System.currentTimeMillis() + NtpHttpBO.TimeDifference));
                            long id = dao.getPromotionDao().insert((Promotion) bm);
                            ((Promotion) bm).setID(id);
                            event.setLastErrorCode(ErrorInfo.EnumErrorCode.EC_NoError);
                        } catch (Exception e) {
                            Log.e("输出：", "创建Promotion时出错，错误信息：" + e.getMessage());
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
        log.info("正在进行PromotionPresenter的deleteAsync，bm=" + bm);

        switch (iUseCaseID) {
            default:
                TaskScheduler.execute(new Runnable() {
                    @Override
                    public void run() {
//                        event.setEventTypeSQLite(BaseSQLiteEvent.EnumSQLiteEventType.ESET_Promotion_DeleteAsync);
                        //
                        try {
                            dao.getPromotionDao().delete((Promotion) bm);
                            event.setLastErrorCode(ErrorInfo.EnumErrorCode.EC_NoError);
                        } catch (Exception e) {
                            Log.e("输出：", "删除Promotion时出错，错误信息：" + e.getMessage());
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
        log.info("正在进行PromotionPresenter的updateAsync，bm=" + bm);

        switch (iUseCaseID) {
            default:
                TaskScheduler.execute(new Runnable() {
                    @Override
                    public void run() {
//                        event.setEventTypeSQLite(BaseSQLiteEvent.EnumSQLiteEventType.ESET_Promotion_UpdateAsync);
                        //
                        try {
                            bm.setSyncType(BasePresenter.SYNC_Type_U);
                            bm.setSyncDatetime(new Date(System.currentTimeMillis() + NtpHttpBO.TimeDifference));
                            dao.getPromotionDao().update((Promotion) bm);
                            event.setLastErrorCode(ErrorInfo.EnumErrorCode.EC_NoError);
                        } catch (Exception e) {
                            log.info("更新Promotion时出错，错误信息：" + e.getMessage());
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
        log.info("正在进行PromotionPresenter的retrieveNAsync，bm=" + bm);

        switch (iUseCaseID) {
            default:
                TaskScheduler.execute(new Runnable() {
                    @Override
                    public void run() {
                        List<Promotion> result = null;
//                        event.setEventTypeSQLite(BaseSQLiteEvent.EnumSQLiteEventType.ESET_Promotion_RetrieveNAsync);
                        try {
                            Thread.sleep(1); //令event.setID()的ID惟一
                            result = dao.getPromotionDao().loadAll();//...
                            event.setLastErrorCode(ErrorInfo.EnumErrorCode.EC_NoError);
                        } catch (Exception e) {
                            log.info("查询SQLite的表Promotion的所有记录时出错，错误信息：" + e.getMessage());
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
        return dao.getPromotionDao().getTablename();
    }
}
