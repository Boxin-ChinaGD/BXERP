package wpos.presenter;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Component;
import wpos.utils.EventBus;
import wpos.bo.BaseSQLiteBO;
import wpos.bo.NtpHttpBO;
import wpos.dao.PromotionScopeMapper;
import wpos.event.UI.BaseSQLiteEvent;
import wpos.helper.Constants;
import wpos.model.BaseModel;
import wpos.model.ErrorInfo;
import wpos.model.promotion.PromotionScope;

import javax.annotation.Resource;
import javax.persistence.Query;
import javax.persistence.Table;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Component("promotionScopePresenter")
public class PromotionScopePresenter extends BasePresenter {
    private Log log = LogFactory.getLog(this.getClass());

    @Resource
    private PromotionScopeMapper promotionScopeMapper;

//    protected static ReentrantReadWriteLock lock = new ReentrantReadWriteLock();

//    public PromotionScopePresenter(PromotionScopeMapper promotionScopeMapper) {
//        super(promotionScopeMapper);
//    }

    public static final String QUERY_PromotionScope_TABLE = "SELECT F_ID,F_PromotionID,F_CommodityID,F_CommodityName,F_SyncType,F_SyncDatetime FROM T_PromotionScope ";

    @Override
    protected String getTableName() {
        if (PromotionScope.class.isAnnotationPresent(Table.class)) {
            Table annotation = PromotionScope.class.getAnnotation(Table.class);
            return annotation.name();
        } else {
            log.error("无法获取表名，未给" + PromotionScope.class.getSimpleName() + "添加@Table。");
        }
        return "";
    }

    @Override
    protected String getQueryTable() {
        return QUERY_PromotionScope_TABLE;
    }

    @Override
    public void createTableSync() {
        promotionScopeMapper.createTable();
    }

    @Override
    protected BaseModel deleteNSync(int iUseCaseID, final BaseModel bm) {
        globalWriteLock.writeLock().lock();
        promotionScopeMapper.deleteAll();
        globalWriteLock.writeLock().unlock();
        return null;
    }

    @Override
    protected List<BaseModel> createNSync(int iUseCaseID, final List<?> list) {
        log.info("正在进行PromotionScopePresenter的createNSync，list=" + (list == null ? null : list.toString()));

        switch (iUseCaseID) {
            default:
                globalWriteLock.writeLock().lock();
                List<BaseModel> baseModels = new ArrayList<BaseModel>();
                try {
                    for (int i = 0; i < list.size(); i++) {
                        ((PromotionScope) list.get(i)).setSyncDatetime(Constants.getDefaultSyncDatetime2());
                        ((PromotionScope) list.get(i)).setSyncType(BasePresenter.SYNC_Type_C);
                        BaseModel baseModel = createSync(iUseCaseID, ((PromotionScope) list.get(i)));
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
        log.info("正在进行PromotionScopePresenter的createSync，bm=" + bm);

        globalWriteLock.writeLock().lock();
        BaseModel baseModel = null;
        try {
            PromotionScope PromotionScope = (PromotionScope) bm;
            PromotionScope.setSyncType(BasePresenter.SYNC_Type_C);
            if (PromotionScope.getSyncDatetime() == null) {
                PromotionScope.setSyncDatetime(Constants.getDefaultSyncDatetime2());
            }
            promotionScopeMapper.create(PromotionScope);
            String sql;
            if (bm.getID() != null) {
                sql = "WHERE F_ID = " + bm.getID();
            } else {
                sql = "WHERE F_ID = (SELECT MAX(F_ID) FROM T_PromotionScope)";
            }
            Query query = entityManager.createNativeQuery(QUERY_PromotionScope_TABLE + sql, PromotionScope.class);
            List<PromotionScope> promotionScopes = query.getResultList();
            if (promotionScopes != null && promotionScopes.size() > 0) {
                lastErrorCode = ErrorInfo.EnumErrorCode.EC_NoError;
                baseModel = promotionScopes.get(0);
            } else {
                lastErrorCode = ErrorInfo.EnumErrorCode.EC_OtherError;
            }
        } catch (Exception e) {
            log.error(e.getMessage());
            e.printStackTrace();
            lastErrorCode = ErrorInfo.EnumErrorCode.EC_OtherError;
        }

        globalWriteLock.writeLock().unlock();
        return baseModel;
    }

    @Override
    protected BaseModel retrieve1Sync(int iUseCaseID, BaseModel bm) {
        log.info("正在进行PromotionScopePresenter的retrieve1Sync，bm=" + bm);

        try {
            lastErrorCode = ErrorInfo.EnumErrorCode.EC_NoError;

            return promotionScopeMapper.findOne(bm.getID());
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
                    String sql = String.format(bm.getSql(), bm.getConditions());
                    Query query = entityManager.createNativeQuery(QUERY_PromotionScope_TABLE + sql, PromotionScope.class);
                    return query.getResultList();
                } catch (Exception e) {
                    log.info(e.getMessage());
                    e.printStackTrace();
                    lastErrorCode = ErrorInfo.EnumErrorCode.EC_OtherError;

                    return null;
                }
            default:
                try {
                    lastErrorCode = ErrorInfo.EnumErrorCode.EC_NoError;

                    return promotionScopeMapper.findAll();
                } catch (Exception e) {
                    log.error(e.getMessage());
                    e.printStackTrace();
                    lastErrorCode = ErrorInfo.EnumErrorCode.EC_OtherError;

                    return null;
                }
        }
    }

    @Override
    protected BaseModel deleteSync(int iUseCaseID, BaseModel bm) {
        log.info("正在进行PromotionScopePresenter的deleteSync，bm=" + bm);
        globalWriteLock.writeLock().lock();
        try {
            promotionScopeMapper.delete(bm.getID());
            lastErrorCode = ErrorInfo.EnumErrorCode.EC_NoError;
        } catch (Exception e) {
            log.error(e.getMessage());
            e.printStackTrace();
            lastErrorCode = ErrorInfo.EnumErrorCode.EC_OtherError;
        }
        globalWriteLock.writeLock().unlock();
        return null;
    }

    @Override
    protected boolean createAsync(int iUseCaseID, final BaseModel bm, final BaseSQLiteEvent event) {
        log.info("正在进行PromotionScopePresenter的createAsync，bm=" + bm);

        switch (iUseCaseID) {
            default:
                (new Thread() {
                    @Override
                    public void run() {
//                        event.setEventTypeSQLite(BaseSQLiteEvent.EnumSQLiteEventType.ESET_PromotionScope_CreateAsync);
                        //
                        globalWriteLock.writeLock().lock();
                        BaseModel baseModel = null;
                        try {
                            bm.setSyncType(BasePresenter.SYNC_Type_C);
                            bm.setSyncDatetime(Constants.getDefaultSyncDatetime2());
                            baseModel = createSync(iUseCaseID, bm);
                            event.setLastErrorCode(ErrorInfo.EnumErrorCode.EC_NoError);
                        } catch (Exception e) {
                            log.info("创建PromotionScope时出错，错误信息：" + e.getMessage());
                            event.setLastErrorCode(ErrorInfo.EnumErrorCode.EC_OtherError);
                        }
                        //
                        event.setBaseModel1(baseModel);
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
    protected boolean createNAsync(int iUseCaseID, final List<?> list, final BaseSQLiteEvent event) {
        log.info("正在进行PromotionScopePresenter的createNAsync，list=" + (list == null ? null : list.toString()));

        switch (iUseCaseID) {
            default:
                (new Thread() {
                    @Override
                    public void run() {
//                        event.setEventTypeSQLite(BaseSQLiteEvent.EnumSQLiteEventType.ESET_PromotionScope_CreateNAsync);
                        //
                        globalWriteLock.writeLock().lock();
                        List<BaseModel> baseModels = new ArrayList<BaseModel>();
                        try {
                            for (int i = 0; i < list.size(); i++) {
                                ((PromotionScope) list.get(i)).setSyncDatetime(new Date(System.currentTimeMillis() + NtpHttpBO.TimeDifference));
                                ((PromotionScope) list.get(i)).setSyncType(BasePresenter.SYNC_Type_C);
                                BaseModel baseModel = createSync(iUseCaseID, ((PromotionScope) list.get(i)));
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
                            log.error("创建Commodity时出错，错误信息：" + e.getMessage());
                            event.setLastErrorCode(ErrorInfo.EnumErrorCode.EC_OtherError);
                            log.error("创建Commodity时出错2，错误信息：" + event.getLastErrorCode());
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
    protected boolean deleteAsync(int iUseCaseID, final BaseModel bm, final BaseSQLiteEvent event) {
        log.info("正在进行PromotionScopePresenter的deleteAsync，bm=" + bm);

        switch (iUseCaseID) {
            default:
                (new Thread() {
                    @Override
                    public void run() {
//                        event.setEventTypeSQLite(BaseSQLiteEvent.EnumSQLiteEventType.ESET_PromotionScope_DeleteAsync);
                        //
                        globalWriteLock.writeLock().lock();
                        try {
                            promotionScopeMapper.delete(bm.getID());
                            event.setLastErrorCode(ErrorInfo.EnumErrorCode.EC_NoError);
                        } catch (Exception e) {
                            log.info("删除PromotionScope时出错，错误信息：" + e.getMessage());
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
    protected boolean retrieve1Async(int iUseCaseID, final BaseModel bm, final BaseSQLiteEvent event) {
        log.info("正在进行PromotionScopePresenter的retrieve1Async，bm=" + bm);

        switch (iUseCaseID) {
            default:
                (new Thread() {
                    @Override
                    public void run() {
                        PromotionScope result = null;
//                        event.setEventTypeSQLite(BaseSQLiteEvent.EnumSQLiteEventType.ESET_PromotionScope_Retrieve1Async);
                        //
                        try {
                            result = (PromotionScope) promotionScopeMapper.findOne(bm.getID());//...
                            event.setLastErrorCode(ErrorInfo.EnumErrorCode.EC_NoError);
                        } catch (Exception e) {
                            log.error("Retrieve1 PromotionScope时出错，错误信息：" + e.getMessage());
                            event.setLastErrorCode(ErrorInfo.EnumErrorCode.EC_OtherError);
                        }
                        //
                        event.setBaseModel1(result);
                        //
                        EventBus.getDefault().post(event);
                    }
                }).start();
                break;
        }

        return true;
    }

    @Override
    protected boolean retrieveNAsync(int iUseCaseID, final BaseModel bm, final BaseSQLiteEvent event) {
        log.info("正在进行PromotionScopePresenter的retrieveNAsync，bm=" + bm);

        switch (iUseCaseID) {
            case BaseSQLiteBO.CASE_PromotionScope_RetrieveNByConditions:
                (new Thread() {
                    @Override
                    public void run() {
                        List<PromotionScope> result = null;
//                                                event.setID(new Date(System.currentTimeMillis() + NtpHttpBO.TimeDifference).getTime());
//                        event.setEventTypeSQLite(BaseSQLiteEvent.EnumSQLiteEventType.ESET_PromotionScope_RetrieveNAsync);

                        try {
                            String sql = String.format(bm.getSql(), bm.getConditions());
                            Query query = entityManager.createNativeQuery(QUERY_PromotionScope_TABLE + sql, PromotionScope.class);
                            result = query.getResultList();
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
                }).start();
            default:
                (new Thread() {
                    @Override
                    public void run() {
                        List<PromotionScope> result = null;
                        //                        event.setID(new Date(System.currentTimeMillis() + NtpHttpBO.TimeDifference).getTime());
//                        event.setEventTypeSQLite(BaseSQLiteEvent.EnumSQLiteEventType.ESET_PromotionScope_RetrieveNAsync);
                        //
                        try {
                            Thread.sleep(1); //令event.setID()的ID惟一
                            result = promotionScopeMapper.findAll();//...
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
                }).start();
                break;
        }

        return true;
    }

    public void drop() {
        try {
//            promotionScopeMapper.dropTable(promotionScopeMapper.getDatabase(), true);
            lastErrorCode = ErrorInfo.EnumErrorCode.EC_NoError;
        } catch (Exception e) {
            log.info("删除SQLite中的表PromotionScope 时出错，错误信息：" + e.getMessage());
            lastErrorCode = ErrorInfo.EnumErrorCode.EC_OtherError;
        }
    }
}
