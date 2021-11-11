package wpos.presenter;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Component;
import wpos.utils.EventBus;
import wpos.bo.BaseSQLiteBO;
import wpos.bo.NtpHttpBO;
import wpos.dao.RetailTradeCommodityMapper;
import wpos.event.UI.BaseSQLiteEvent;
import wpos.helper.Constants;
import wpos.model.*;

import javax.annotation.Resource;
import javax.persistence.Query;
import javax.persistence.Table;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Component("retailTradeCommodityPresenter")
public class RetailTradeCommodityPresenter extends BasePresenter {
    private Log log = LogFactory.getLog(this.getClass());

//    protected static ReentrantReadWriteLock lock = new ReentrantReadWriteLock();

    @Resource
    private RetailTradeCommodityMapper retailTradeCommodityMapper;

    public static final String QUERY_RetailTradeCommodity_TABLE = "SELECT F_ID," +
            "F_TradeID," +
            "F_CommodityID," +
            "F_CommodityName," +
            "F_BarcodeID," +
            "F_NO," +
            "F_PriceOriginal," +
            "F_NOCanReturn," +
            "F_PriceReturn," +
            "F_PriceSpecialOffer," +
            "F_PriceVIPOriginal," +
            "F_SyncType," +
            "F_SyncDatetime," +
            "F_PromotionID " +
            "FROM T_RetailTradeCommodity ";

//    public RetailTradeCommodityPresenter(final RetailTradeCommodityMapper retailTradeCommodityMapper) {
//        super(retailTradeCommodityMapper);
//    }

    @Override
    public void createTableSync() {
        retailTradeCommodityMapper.createTable();
    }

    @Override
    protected String getTableName() {
        if (RetailTradeCommodity.class.isAnnotationPresent(Table.class)) {
            Table annotation = RetailTradeCommodity.class.getAnnotation(Table.class);
            return annotation.name();
        } else {
            log.error("无法获取表名，未给" + RetailTradeCommodity.class.getSimpleName() + "添加@Table。");
        }
        return "";
    }

    @Override
    protected String getQueryTable() {
        return QUERY_RetailTradeCommodity_TABLE;
    }

    @Override
    protected BaseModel deleteNSync(int iUseCaseID, final BaseModel bm) {
        globalWriteLock.writeLock().lock();
        retailTradeCommodityMapper.deleteAll();
        globalWriteLock.writeLock().unlock();
        return null;
    }

    @Override
    protected List<BaseModel> createNSync(int iUseCaseID, final List<?> list) {
        log.info("正在进行RetailTradeCommodityPresenter的createNSync，list=" + (list == null ? null : list.toString()));

        switch (iUseCaseID) {
            default:
                globalWriteLock.writeLock().lock();
                List<BaseModel> rtclist = new ArrayList<BaseModel>();
                try {
                    for (int i = 0; i < list.size(); i++) {
                        ((RetailTradeCommodity) list.get(i)).setSyncDatetime(Constants.getDefaultSyncDatetime2());
                        ((RetailTradeCommodity) list.get(i)).setSyncType(BasePresenter.SYNC_Type_C);

                        RetailTradeCommodity retailTradeCommodity = (RetailTradeCommodity) createSync(BaseSQLiteBO.INVALID_CASE_ID, (RetailTradeCommodity) list.get(i));
                        rtclist.add(retailTradeCommodity);
                    }

                } catch (Exception e) {
                    log.error(e.getMessage());
                    e.printStackTrace();
                    lastErrorCode = ErrorInfo.EnumErrorCode.EC_OtherError;
                }
                globalWriteLock.writeLock().unlock();
                return (List<BaseModel>) rtclist;
        }
    }

    @Override
    protected BaseModel createSync(int iUseCaseID, BaseModel bm) {
        log.info("正在进行RetailTradeCommodityPresenter的createSync，bm=" + bm);

        globalWriteLock.writeLock().lock();

        RetailTradeCommodity rtcCreated = null;
        try {
            RetailTradeCommodity retailTradeCommodity = (RetailTradeCommodity) bm;
            retailTradeCommodity.setSyncType(BasePresenter.SYNC_Type_C);
            if (retailTradeCommodity.getSyncDatetime() == null) {
                retailTradeCommodity.setSyncDatetime(Constants.getDefaultSyncDatetime2());
            }
            retailTradeCommodityMapper.create(retailTradeCommodity);
            lastErrorCode = ErrorInfo.EnumErrorCode.EC_NoError;

            if (retailTradeCommodity.getID() != null) {
                retailTradeCommodity.setSql("WHERE F_ID = %s");
                retailTradeCommodity.setConditions(new String[]{String.valueOf(bm.getID())});
            } else {
                retailTradeCommodity.setSql("WHERE F_ID = (select MAX(F_ID) from T_RetailTradeCommodity)");
            }
            String a = QUERY_RetailTradeCommodity_TABLE + retailTradeCommodity.getSql();
            String sql = String.format(bm.getSql(), bm.getConditions());
            Query dataQuery = entityManager.createNativeQuery(QUERY_RetailTradeCommodity_TABLE + sql, RetailTradeCommodity.class);
            List<?> rtcList = dataQuery.getResultList();

            globalWriteLock.writeLock().unlock();

            if (rtcList != null && rtcList.size() > 0) {
                lastErrorCode = ErrorInfo.EnumErrorCode.EC_NoError;
                return (BaseModel) rtcList.get(0);
            }
        } catch (Exception e) {
            log.error(e.getMessage());
            e.printStackTrace();

            globalWriteLock.writeLock().unlock();
        }

        lastErrorCode = ErrorInfo.EnumErrorCode.EC_OtherError;
        return null;
    }

    @Override
    protected BaseModel retrieve1Sync(int iUseCaseID, BaseModel bm) {
        log.info("正在进行RetailTradeCommodityPresenter的retrieve1Sync，bm=" + bm);

        try {
            lastErrorCode = ErrorInfo.EnumErrorCode.EC_NoError;

            return retailTradeCommodityMapper.findOne(bm.getID());
//            return null;
        } catch (Exception e) {
            log.error(e.getMessage());
            e.printStackTrace();
            lastErrorCode = ErrorInfo.EnumErrorCode.EC_OtherError;

            return null;
        }
    }

    @Override
    protected List<?> retrieveNSync(int iUseCaseID, BaseModel bm) {//本地RN不传pageIndex \ pageSize ，故不添加checkRetrieveN()
        log.info("正在进行RetailTradeCommodityPresenter的retrieveNSync，bm=" + bm);

        switch (iUseCaseID) {
            case BaseSQLiteBO.CASE_RetailTradeCommodity_RetrieveNByConditions:
                try {
                    lastErrorCode = ErrorInfo.EnumErrorCode.EC_NoError;

                    String sql = String.format(bm.getSql(), bm.getConditions());
                    Query dataQuery = entityManager.createNativeQuery(QUERY_RetailTradeCommodity_TABLE + sql, RetailTradeCommodity.class);
                    return dataQuery.getResultList();
                } catch (Exception e) {
                    log.info(e.getMessage());
                    e.printStackTrace();
                    lastErrorCode = ErrorInfo.EnumErrorCode.EC_OtherError;

                    return null;
                }
            default:
                try {
                    lastErrorCode = ErrorInfo.EnumErrorCode.EC_NoError;

                    return retailTradeCommodityMapper.findAll();
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
        log.info("正在进行RetailTradeCommodityPresenter的deleteSync，bm=" + bm);
        globalWriteLock.writeLock().lock();
        try {
            retailTradeCommodityMapper.delete(bm.getID());
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
    protected boolean createAsync(int iUseCaseID, final BaseModel bm, final BaseSQLiteEvent event) {// 在创建主表是已做从表的字段验证
        log.info("正在进行RetailTradeCommodityPresenter的createAsync，bm=" + bm);
        switch (iUseCaseID) {
            default:
                (new Thread() {
                    @Override
                    public void run() {
//                        event.setEventTypeSQLite(BaseSQLiteEvent.EnumSQLiteEventType.ESET_RetailTradeCommodity_CreateAsync);
                        //
                        globalWriteLock.writeLock().lock();
                        RetailTradeCommodity rtcCreated = null;
                        try {
                            bm.setSyncType(BasePresenter.SYNC_Type_C);
                            bm.setSyncDatetime(Constants.getDefaultSyncDatetime2());
                            rtcCreated = (RetailTradeCommodity) createSync(BaseSQLiteBO.INVALID_CASE_ID, bm);
                        } catch (Exception e) {
                            log.info("创建RetailTradeCommodity时出错，错误信息：" + e.getMessage());
                            event.setLastErrorCode(ErrorInfo.EnumErrorCode.EC_OtherError);
                        }
                        //
                        event.setBaseModel1(rtcCreated);
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
    protected boolean createNAsync(int iUseCaseID, final List<?> list, final BaseSQLiteEvent event) {
        log.info("正在进行RetailTradeCommodityPresenter的createNAsync，list=" + (list == null ? null : list.toString()));

        switch (iUseCaseID) {
            default:
                (new Thread() {
                    @Override
                    public void run() {
//                        event.setEventTypeSQLite(BaseSQLiteEvent.EnumSQLiteEventType.ESET_RetailTradeCommodity_CreateNAsync);
                        //
                        globalWriteLock.writeLock().lock();
                        List<?> rtcCreatedList = null;
                        try {
                            for (int i = 0; i < list.size(); i++) {
                                ((RetailTradeCommodity) list.get(i)).setSyncDatetime(new Date(System.currentTimeMillis() + NtpHttpBO.TimeDifference));
                                ((RetailTradeCommodity) list.get(i)).setSyncType(BasePresenter.SYNC_Type_C);
                            }
                            rtcCreatedList = createNSync(BaseSQLiteBO.INVALID_CASE_ID, list);
                        } catch (Exception e) {
                            log.info("创建Commodity时出错，错误信息：" + e.getMessage());
                            event.setLastErrorCode(ErrorInfo.EnumErrorCode.EC_OtherError);
                            log.info("创建Commodity时出错2，错误信息：" + event.getLastErrorCode());
                        }
                        //
                        event.setListMasterTable(rtcCreatedList);
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
    protected boolean deleteAsync(int iUseCaseID, final BaseModel bm, final BaseSQLiteEvent event) {
        log.info("正在进行RetailTradeCommodityPresenter的deleteAsync，bm=" + bm);

        if (bm != null) {
            String checkMsg = bm.checkDelete(iUseCaseID);
            if (!"".equals(checkMsg)) {
                return false;
            }
        }

        switch (iUseCaseID) {
            default:
                (new Thread() {
                    @Override
                    public void run() {
//                        event.setEventTypeSQLite(BaseSQLiteEvent.EnumSQLiteEventType.ESET_RetailTradeCommodity_DeleteAsync);
                        //
                        globalWriteLock.writeLock().lock();
                        try {
                            retailTradeCommodityMapper.delete(bm.getID());
                            event.setLastErrorCode(ErrorInfo.EnumErrorCode.EC_NoError);
                        } catch (Exception e) {
                            log.info("删除RetailTradeCommodity时出错，错误信息：" + e.getMessage());
                            event.setLastErrorCode(ErrorInfo.EnumErrorCode.EC_OtherError);
                        }
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
        log.info("正在进行RetailTradeCommodityPresenter的retrieve1Async，bm=" + bm);

        if (bm != null) {
            String checkMsg = bm.checkRetrieve1(iUseCaseID);
            if (!"".equals(checkMsg)) {
                return false;
            }
        }

        switch (iUseCaseID) {
            default:
                (new Thread() {
                    @Override
                    public void run() {
                        RetailTradeCommodity result = null;
//                        event.setEventTypeSQLite(BaseSQLiteEvent.EnumSQLiteEventType.ESET_RetailTradeCommodity_Retrieve1Async);
                        //
                        try {
                            result = retailTradeCommodityMapper.findOne(bm.getID());//...
                            event.setLastErrorCode(ErrorInfo.EnumErrorCode.EC_NoError);
                        } catch (Exception e) {
                            log.error("Retrieve1 RetailTradeCommodity时出错，错误信息：" + e.getMessage());
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
        log.info("正在进行RetailTradeCommodityPresenter的retrieveNAsync，bm=" + bm);

        switch (iUseCaseID) {
            case BaseSQLiteBO.CASE_RetailTradeCommodity_RetrieveNByConditions:
                (new Thread() {
                    @Override
                    public void run() {
                        List<RetailTradeCommodity> result = null;
                        //                        event.setID(new Date(System.currentTimeMillis() + NtpHttpBO.TimeDifference).getTime());
//                        event.setEventTypeSQLite(BaseSQLiteEvent.EnumSQLiteEventType.ESET_RetailTradeCommodity_RetrieveNAsync);
                        //
                        try {
                            String sql = String.format(bm.getSql(), bm.getConditions());
                            Query dataQuery = entityManager.createNativeQuery(QUERY_RetailTradeCommodity_TABLE + sql, RetailTradeCommodity.class);
                            result = dataQuery.getResultList();
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
                        List<RetailTradeCommodity> result = null;
                        //                        event.setID(new Date(System.currentTimeMillis() + NtpHttpBO.TimeDifference).getTime());
//                        event.setEventTypeSQLite(BaseSQLiteEvent.EnumSQLiteEventType.ESET_RetailTradeCommodity_RetrieveNAsync);
                        //
                        try {
                            Thread.sleep(1); //令event.setID()的ID惟一
                            result = retailTradeCommodityMapper.findAll();
                            event.setLastErrorCode(ErrorInfo.EnumErrorCode.EC_NoError);
                        } catch (Exception e) {
                            log.error("查询SQLite的表RetailTrade的所有记录时出错，错误信息：" + e.getMessage());
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
//            retailTradeCommodityMapper.dropTable(retailTradeCommodityMapper.getDatabase(), true);
            lastErrorCode = ErrorInfo.EnumErrorCode.EC_NoError;
        } catch (Exception e) {
            log.error("删除SQLite中的表RetailTradeCommodity 时出错，错误信息：" + e.getMessage());
            lastErrorCode = ErrorInfo.EnumErrorCode.EC_OtherError;
        }
    }

    @Override
    protected boolean updateSync(int iUseCaseID, final BaseModel bm) {
        globalWriteLock.writeLock().lock();
        try {
//            retailTradeCommodityMapper.update((RetailTradeCommodity) bm);
            retailTradeCommodityMapper.save((RetailTradeCommodity) bm);
            lastErrorCode = ErrorInfo.EnumErrorCode.EC_NoError;
        } catch (Exception e) {
            log.error("修改RetailTradeCommodity时出错，错误信息：" + e.getMessage());
            lastErrorCode = ErrorInfo.EnumErrorCode.EC_OtherError;
        }
        globalWriteLock.writeLock().unlock();
        return true;
    }
}
