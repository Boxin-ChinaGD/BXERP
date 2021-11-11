package wpos.presenter;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Component;
import wpos.dao.PromotionShopScopeMapper;
import wpos.model.Commodity;
import wpos.model.promotion.PromotionShopScope;
import wpos.utils.EventBus;
import wpos.bo.BaseSQLiteBO;
import wpos.bo.NtpHttpBO;
import wpos.dao.PromotionMapper;
import wpos.dao.PromotionScopeMapper;
import wpos.event.BaseEvent;
import wpos.event.UI.BaseSQLiteEvent;
import wpos.model.BaseModel;
import wpos.model.ErrorInfo;
import wpos.model.promotion.Promotion;
import wpos.model.promotion.PromotionScope;

import javax.annotation.Resource;
import javax.persistence.Query;
import javax.persistence.Table;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static wpos.bo.BaseSQLiteBO.CASE_Promotion_RetrieveNByConditions;

@Component("promotionPresenter")
public class PromotionPresenter extends BasePresenter {
    private Log log = LogFactory.getLog(this.getClass());

    @Resource
    private PromotionMapper promotionMapper;

    @Resource
    private PromotionScopeMapper promotionScopeMapper;

    @Resource
    private PromotionShopScopeMapper promotionShopScopeMapper;

//    protected static ReentrantReadWriteLock lock = new ReentrantReadWriteLock();

    public final String QUERY_Promotion_TABLE = "SELECT F_ID,F_SN,F_Name,F_Status,F_Type,F_DatetimeStart,F_DatetimeEnd,F_ExcecutionThreshold,F_ExcecutionAmount," +
            "F_ExcecutionDiscount,F_Scope,F_Staff,F_CreateDatetime,F_SyncType,F_SyncDatetime FROM T_Promotion ";


//    public PromotionPresenter(final PromotionMapper promotionMapper) {
//        super(promotionMapper);
//    }

    @Override
    public void createTableSync() {
        promotionMapper.createTable();
    }

    @Override
    protected List<BaseModel> createNSync(int iUseCaseID, final List<?> list) {
        log.info("正在进行PromotionPresenter的createOrReplaceAsync，list=" + (list == null ? null : list.toString()));

        switch (iUseCaseID) {
            default:
                globalWriteLock.writeLock().lock();
                List<BaseModel> baseModels = new ArrayList<BaseModel>();
                try {
                    for (Object object : list) {
                        BaseModel baseModel = createSync(iUseCaseID, (BaseModel) object);
                        if (baseModel != null) {
                            baseModels.add(baseModel);
                        }
                    }
                    if (baseModels.size() == list.size()) {
                        lastErrorCode = ErrorInfo.EnumErrorCode.EC_NoError;
                    } else {
                        lastErrorCode = ErrorInfo.EnumErrorCode.EC_PartSuccess;
                    }
                } catch (Exception e) {
                    log.error("执行createNSync失败，错误信息为" + e.getMessage());
                    e.printStackTrace();
                    lastErrorCode = ErrorInfo.EnumErrorCode.EC_OtherError;
                }
                globalWriteLock.writeLock().unlock();
                return baseModels;
        }
    }

    @Override
    protected BaseModel createSync(int iUseCaseID, BaseModel bm) {
        log.info("正在进行PromotionPresenter的createSync，bm=" + bm);

        globalWriteLock.writeLock().lock();
        BaseModel baseModel = null;
        try {
            bm.setSyncType(BasePresenter.SYNC_Type_C);
            bm.setSyncDatetime(new Date(System.currentTimeMillis() + NtpHttpBO.TimeDifference));
            promotionMapper.create((Promotion) bm);

            String sql;
            if (bm.getID() != null) {
                sql = "WHERE F_ID = " + bm.getID();
            } else {
                sql = "WHERE F_ID = (SELECT MAX(F_ID) FROM T_Promotion)";
            }
            Query query = entityManager.createNativeQuery(QUERY_Promotion_TABLE + sql, Promotion.class);
            List<Promotion> promotions = query.getResultList();
            if (promotions != null && promotions.size() > 0) {
                lastErrorCode = ErrorInfo.EnumErrorCode.EC_NoError;
                baseModel = promotions.get(0);
                bm.setID(baseModel.getID());
            } else {
                lastErrorCode = ErrorInfo.EnumErrorCode.EC_OtherError;
            }
        } catch (Exception e) {
            log.error("执行createSync失败，错误信息为" + e.getMessage());
            e.printStackTrace();
            lastErrorCode = ErrorInfo.EnumErrorCode.EC_OtherError;
        }
        globalWriteLock.writeLock().unlock();

        return baseModel;
    }

    @Override
    protected boolean updateSync(int iUseCaseID, BaseModel bm) {
        log.info("正在进行PromotionPresenter的updateSync，bm=" + bm);
        globalWriteLock.writeLock().lock();
        try {
            bm.setSyncDatetime(new Date(System.currentTimeMillis() + NtpHttpBO.TimeDifference));
            bm.setSyncType(BasePresenter.SYNC_Type_U);
            promotionMapper.save((Promotion) bm);
            lastErrorCode = ErrorInfo.EnumErrorCode.EC_NoError;
            globalWriteLock.writeLock().unlock();
            return true;
        } catch (Exception e) {
            log.error("执行updateSync失败，错误信息为" + e.getMessage());
            e.printStackTrace();
            lastErrorCode = ErrorInfo.EnumErrorCode.EC_OtherError;
            globalWriteLock.writeLock().unlock();
            return false;
        }
    }

    @Override
    protected BaseModel retrieve1Sync(int iUseCaseID, BaseModel bm) {
        log.info("正在进行PromotionPresenter的retrieve1Sync，bm=" + bm);

        try {
            lastErrorCode = ErrorInfo.EnumErrorCode.EC_NoError;

            return promotionMapper.findOne(bm.getID());
        } catch (Exception e) {
            log.error("执行retrieve1Sync失败，错误信息为" + e.getMessage());
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
                    String sql = String.format(bm.getSql(), bm.getConditions());
                    Query query = entityManager.createNativeQuery(QUERY_Promotion_TABLE + sql, Promotion.class);
                    return query.getResultList();
                } catch (Exception e) {
                    log.info("执行retrieveNSync的CASE_Promotion_RetrieveNByConditions失败，错误信息为" + e.getMessage());
                    e.printStackTrace();
                    lastErrorCode = ErrorInfo.EnumErrorCode.EC_OtherError;

                    return null;
                }
            default:
                try {
                    lastErrorCode = ErrorInfo.EnumErrorCode.EC_NoError;

                    List<Promotion> promotionList = promotionMapper.findAll();
                    for (int i = 0; i < promotionList.size(); i++) {
                        PromotionScope promotionScope = new PromotionScope();
                        promotionScope.setSql("where F_PromotionID = '%s'");
                        promotionScope.setConditions(new String[]{String.valueOf(promotionList.get(i).getID())});
                        String sql = String.format(promotionScope.getSql(), promotionScope.getConditions());
                        Query query = entityManager.createNativeQuery(PromotionScopePresenter.QUERY_PromotionScope_TABLE + sql, PromotionScope.class);

                        List<PromotionScope> promotionScopeList = query.getResultList();
                        promotionList.get(i).setListSlave1(promotionScopeList);
                    }

                    return promotionList;
                } catch (Exception e) {
                    log.error("执行retrieveNSync失败，错误信息为" + e.getMessage());
                    e.printStackTrace();
                    lastErrorCode = ErrorInfo.EnumErrorCode.EC_NoError;

                    return null;
                }
        }
    }

    @Override
    protected BaseModel deleteSync(int iUseCaseID, BaseModel bm) {
        log.info("正在进行PromotionPresenter的deleteSync，bm=" + bm);
        globalWriteLock.writeLock().lock();
        try {
            promotionMapper.delete(bm.getID());
            lastErrorCode = ErrorInfo.EnumErrorCode.EC_NoError;
        } catch (Exception e) {
            log.error("执行deleteSync失败，错误信息为" + e.getMessage());
            e.printStackTrace();
            lastErrorCode = ErrorInfo.EnumErrorCode.EC_OtherError;
        }
        globalWriteLock.writeLock().unlock();
        return null;
    }

    @Override
    protected BaseModel deleteNSync(int iUserCaseID, BaseModel bm) {
        log.info("正在进行PromotionPresenter的deleteNSync，bm=" + bm);
        globalWriteLock.writeLock().lock();
        try {
            promotionMapper.deleteAll();
            lastErrorCode = ErrorInfo.EnumErrorCode.EC_NoError;
        } catch (Exception e) {
            log.error("执行deleteNSync失败，错误信息为" + e.getMessage());
            e.printStackTrace();
            lastErrorCode = ErrorInfo.EnumErrorCode.EC_OtherError;
        }
        globalWriteLock.writeLock().unlock();
        return null;
    }

    @Override
    protected boolean refreshByServerDataAsync(final int iUseCaseID, final List<?> bmNewList, final BaseSQLiteEvent event) {
        log.info("正在进行PromotionPresenter的refreshByServerDataAsync，bmNewList=" + (bmNewList == null ? null : bmNewList.toString()));

        event.setStatus(BaseEvent.EnumEventStatus.EES_SQLite_ToApplyServerData);
        switch (iUseCaseID) {
            default:
                (new Thread() {
                    @Override
                    public void run() {
                        log.info("已经得到服务器返回的需要同步的Promotion数据, 准备进行同步...");
                        globalWriteLock.writeLock().lock();
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
//                                            promotionScopeList = dao.getPromotionScopeDao().queryRaw("where F_PromotionID = ?", new String[]{String.valueOf(oldPromotion.getID())});
                                            String sql = "where F_PromotionID = " + oldPromotion.getID();
                                            Query query = entityManager.createNativeQuery(PromotionScopePresenter.QUERY_PromotionScope_TABLE + sql, PromotionScope.class);
                                            List<PromotionScope> promotionScopeList = query.getResultList();
                                            if (promotionScopeList.size() > 0) {
                                                for (int j = 0; j < promotionScopeList.size(); j++) {
                                                    promotionScopeMapper.delete(promotionScopeList.get(j).getID());
                                                }
                                            }
                                            Query queryPromotionShopScope = entityManager.createNativeQuery(PromotionShopScopePresenter.QUERY_PromotionShopScope_TABLE + sql, PromotionShopScope.class);
                                            List<PromotionShopScope> promotionShopScopeList = queryPromotionShopScope.getResultList();
                                            if (promotionShopScopeList.size() > 0) {
                                                for (int j = 0; j < promotionShopScopeList.size(); j++) {
                                                    promotionShopScopeMapper.delete(promotionShopScopeList.get(j).getID());
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
                                                promotionScopeMapper.create(promotionScopeList.get(j));
                                            }
                                        }
                                        List<PromotionShopScope> promotionShopScopeList = (List<PromotionShopScope>) newPromotion.getListSlave2();
                                        if (promotionShopScopeList.size() > 0) {
                                            for (int j = 0; j < promotionShopScopeList.size(); j++) {
                                                promotionShopScopeMapper.create(promotionShopScopeList.get(j));
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
                                            promotionScopeMapper.save(promotionScopeList.get(j));
                                        }
                                        List<PromotionShopScope> promotionShopScopeList = new ArrayList<>();
                                        promotionShopScopeList = (List<PromotionShopScope>) promotion.getListSlave2();
                                        for (int j = 0; j < promotionShopScopeList.size(); j++) {
                                            promotionShopScopeMapper.save(promotionShopScopeList.get(j));
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
                                                    promotionScopeMapper.delete(promotionScopeList.get(j).getID());
                                                }
                                            }
                                            if (promotionShopScopeList != null && promotionShopScopeList.size() > 0) {
                                                for (int j = 0; j < promotionShopScopeList.size(); j++) {
                                                    promotionShopScopeMapper.delete(promotionShopScopeList.get(j).getID());
                                                }
                                            }
                                            if (promotionShopScopeList != null && promotionShopScopeList.size() > 0) {
                                                for (int j = 0; j < promotionShopScopeList.size(); j++) {
                                                    promotionShopScopeMapper.delete(promotionShopScopeList.get(j).getID());
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
                        globalWriteLock.writeLock().unlock();
                        EventBus.getDefault().post(event);
                    }
                }).start();
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
                (new Thread() {
                    @Override
                    public void run() {
                        log.info("已经得到服务器返回的需要同步的PromotionInfo数据, 准备进行同步...");

//                        event.setEventTypeSQLite(BaseSQLiteEvent.EnumSQLiteEventType.ESET_Promotion_RefreshByServerDataAsync_Done);
                        globalWriteLock.writeLock().lock();
                        {
                            try {
                                //删除本地所有的数据
                                promotionScopeMapper.deleteAll();
                                promotionShopScopeMapper.deleteAll();
                                deleteNSync(iUseCaseID, null);
                                if (getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError) {
                                    //同步所有服务器返回的数据
//                            createNSync(iUseCaseID, bmNewList);
                                    if (bmNewList != null) {
                                        for (int i = 0; i < bmNewList.size(); i++) {
                                            promotionMapper.create((Promotion) bmNewList.get(i));
                                            for (int j = 0; j < ((Promotion) bmNewList.get(i)).getListSlave1().size(); j++) {
                                                promotionScopeMapper.create((PromotionScope) ((Promotion) bmNewList.get(i)).getListSlave1().get(j));
                                            }
                                            for (int j = 0; j < ((Promotion) bmNewList.get(i)).getListSlave2().size(); j++) {
                                                promotionShopScopeMapper.create((PromotionShopScope) ((Promotion) bmNewList.get(i)).getListSlave2().get(j));
                                            }
                                        }
                                    } else {
                                        log.info("服务器没有promotion返回");
                                    }

//                            log.info("所有的从表信息有：" + GlobalController.getInstance().getPromotionScopePresenter().retrieveNSync(BaseSQLiteBO.INVALID_CASE_ID, null));
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
                        globalWriteLock.writeLock().unlock();
                        //
                        EventBus.getDefault().post(event);
                    }
                }).start();
                break;
        }
        return true;
    }

    @Override
    protected boolean createNAsync(int iUseCaseID, final List<?> list, final BaseSQLiteEvent event) {
        log.info("正在进行PromotionPresenter的createNAsync，list=" + (list == null ? null : list.toString()));

        switch (iUseCaseID) {
            default:
                (new Thread() {
                    @Override
                    public void run() {
//                        event.setEventTypeSQLite(BaseSQLiteEvent.EnumSQLiteEventType.ESET_Promotion_CreateNAsync);
                        //
                        globalWriteLock.writeLock().lock();
                        List<BaseModel> baseModels = new ArrayList<BaseModel>();
                        try {
                            for (int i = 0; i < list.size(); i++) {
                                Promotion p = (Promotion) list.get(i);
                                p.setSyncDatetime(new Date(System.currentTimeMillis() + NtpHttpBO.TimeDifference));
                                p.setSyncType(BasePresenter.SYNC_Type_C);
                                BaseModel baseModel = createSync(iUseCaseID, p);
                                if (baseModel != null) {
                                    baseModels.add(baseModel);
                                }
                            }
                            if (baseModels.size() == list.size()) {
                                event.setLastErrorCode(ErrorInfo.EnumErrorCode.EC_NoError);
                            } else {
                                event.setLastErrorCode(ErrorInfo.EnumErrorCode.EC_PartSuccess);
                            }
                        } catch (Exception e) {
                            log.error("创建Promotion时出错，错误信息：" + e.getMessage());
                            event.setLastErrorCode(ErrorInfo.EnumErrorCode.EC_OtherError);
                            log.error("创建Promotion时出错2，错误信息：" + event.getLastErrorCode());
                        }
                        //
                        event.setListMasterTable(baseModels);
                        globalWriteLock.writeLock().unlock();
                        //
                        EventBus.getDefault().post(event);
                    }
                }).start();
                break;
        }

        return true;
    }

    @Override
    protected boolean createAsync(int iUseCaseID, final BaseModel bm, final BaseSQLiteEvent event) {
        log.info("正在进行PromotionPresenter的createAsync，bm=" + bm);

        switch (iUseCaseID) {
            default:
                (new Thread() {
                    @Override
                    public void run() {
//                        event.setEventTypeSQLite(BaseSQLiteEvent.EnumSQLiteEventType.ESET_Promotion_CreateAsync);
                        //
                        globalWriteLock.writeLock().lock();
                        BaseModel baseModel = null;
                        try {
                            bm.setSyncType(BasePresenter.SYNC_Type_C);
                            bm.setSyncDatetime(new Date(System.currentTimeMillis() + NtpHttpBO.TimeDifference));
                            baseModel = createSync(iUseCaseID, bm);
                            event.setLastErrorCode(lastErrorCode);
                        } catch (Exception e) {
                            log.error("创建Promotion时出错，错误信息：" + e.getMessage());
                            event.setLastErrorCode(ErrorInfo.EnumErrorCode.EC_OtherError);
                        }
                        //
                        event.setBaseModel1(baseModel);
                        globalWriteLock.writeLock().unlock();
                        //
                        EventBus.getDefault().post(event);
                    }
                }).start();
                break;
        }

        return true;
    }

    @Override
    protected boolean deleteAsync(int iUseCaseID, final BaseModel bm, final BaseSQLiteEvent event) {
        log.info("正在进行PromotionPresenter的deleteAsync，bm=" + bm);

        switch (iUseCaseID) {
            default:
                (new Thread() {
                    @Override
                    public void run() {
//                        event.setEventTypeSQLite(BaseSQLiteEvent.EnumSQLiteEventType.ESET_Promotion_DeleteAsync);
                        //
                        globalWriteLock.writeLock().lock();
                        try {
                            promotionMapper.delete(bm.getID());
                            event.setLastErrorCode(ErrorInfo.EnumErrorCode.EC_NoError);
                        } catch (Exception e) {
                            log.error("删除Promotion时出错，错误信息：" + e.getMessage());
                            event.setLastErrorCode(ErrorInfo.EnumErrorCode.EC_OtherError);
                        }
                        //
                        event.setBaseModel1(bm);
                        //
                        EventBus.getDefault().post(event);
                        globalWriteLock.writeLock().unlock();
                    }
                }).start();
                break;
        }

        return true;
    }

    @Override
    protected boolean updateAsync(int iUseCaseID, final BaseModel bm, final BaseSQLiteEvent event) {
        log.info("正在进行PromotionPresenter的updateAsync，bm=" + bm);

        switch (iUseCaseID) {
            default:
                (new Thread() {
                    @Override
                    public void run() {
//                        event.setEventTypeSQLite(BaseSQLiteEvent.EnumSQLiteEventType.ESET_Promotion_UpdateAsync);
                        //
                        globalWriteLock.writeLock().lock();
                        try {
                            bm.setSyncType(BasePresenter.SYNC_Type_U);
                            bm.setSyncDatetime(new Date(System.currentTimeMillis() + NtpHttpBO.TimeDifference));
                            promotionMapper.save((Promotion) bm);
                            event.setLastErrorCode(ErrorInfo.EnumErrorCode.EC_NoError);
                        } catch (Exception e) {
                            log.info("更新Promotion时出错，错误信息：" + e.getMessage());
                            event.setLastErrorCode(ErrorInfo.EnumErrorCode.EC_OtherError);
                        }
                        //
                        event.setBaseModel1(bm);
                        //
                        EventBus.getDefault().post(event);
                        globalWriteLock.writeLock().unlock();
                    }
                }).start();
                break;
        }

        return true;
    }

    @Override
    protected boolean retrieveNAsync(int iUseCaseID, final BaseModel bm, final BaseSQLiteEvent event) {
        log.info("正在进行PromotionPresenter的retrieveNAsync，bm=" + bm);

        switch (iUseCaseID) {
            default:
                (new Thread() {
                    @Override
                    public void run() {
                        List<Promotion> result = null;
//                        event.setEventTypeSQLite(BaseSQLiteEvent.EnumSQLiteEventType.ESET_Promotion_RetrieveNAsync);
                        try {
                            Thread.sleep(1); //令event.setID()的ID惟一
                            result = promotionMapper.findAll();//...
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
                }).start();
                break;
        }

        return true;
    }

    @Override
    protected String getTableName() {
        if (Promotion.class.isAnnotationPresent(Table.class)) {
            Table annotation = Promotion.class.getAnnotation(Table.class);
            return annotation.name();
        } else {
            log.error("无法获取表名，未给" + Promotion.class.getSimpleName() + "添加@Table。");
        }
        return "";
    }

    @Override
    protected String getQueryTable() {
        return QUERY_Promotion_TABLE;
    }
}
