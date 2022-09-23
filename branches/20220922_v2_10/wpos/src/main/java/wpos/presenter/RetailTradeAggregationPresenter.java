
package wpos.presenter;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Component;
import wpos.utils.EventBus;
import wpos.bo.BaseSQLiteBO;
import wpos.bo.NtpHttpBO;
import wpos.dao.RetailTradeAggregationMapper;
import wpos.event.BaseEvent;
import wpos.event.UI.BaseSQLiteEvent;
import wpos.helper.Constants;
import wpos.model.*;

import javax.annotation.Resource;
import javax.persistence.Query;
import javax.persistence.Table;
import java.util.Date;
import java.util.List;

@Component("retailTradeAggregationPresenter")
public class RetailTradeAggregationPresenter extends BasePresenter {
    private Log log = LogFactory.getLog(this.getClass());

//    protected static ReentrantReadWriteLock lock = new ReentrantReadWriteLock();

    private final static String OutdatedDay = "-7";

    public static final String QUERY_RetailTradeAggregation_TABLE = "SELECT F_ID," +
            "F_StaffID," +
            "F_PosID," +
            "F_WorkTimeStart," +
            "F_WorkTimeEnd," +
            "F_TradeNO," +
            "F_Amount," +
            "F_ReserveAmount," +
            "F_CashAmount," +
            "F_WechatAmount," +
            "F_AlipayAmount," +
            "F_Amount1," +
            "F_Amount2," +
            "F_Amount3," +
            "F_Amount4," +
            "F_Amount5, " +
            "F_UploadDateTime, " +
            "F_SyncDatetime, " +
            "F_SyncType " +
            "FROM T_RetailTradeAggregation ";

    @Resource
    private RetailTradeAggregationMapper retailTradeAggregationMapper;

//    public RetailTradeAggregationPresenter(final RetailTradeAggregationMapper retailTradeAggregationMapper) {
//        super(retailTradeAggregationMapper);
//    }

    @Override
    public void createTableSync() {
        retailTradeAggregationMapper.createTable();
    }

    @Override
    protected String getTableName() {
        if (RetailTradeAggregation.class.isAnnotationPresent(Table.class)) {
            Table annotation = RetailTradeAggregation.class.getAnnotation(Table.class);
            return annotation.name();
        } else {
            log.error("无法获取表名，未给" + RetailTradeAggregation.class.getSimpleName() + "添加@Table。");
        }
        return "";
    }

    @Override
    protected String getQueryTable() {
        return QUERY_RetailTradeAggregation_TABLE;
    }

    @Override
    protected BaseModel createSync(int iUseCaseID, BaseModel bm) {
        log.info("正在进行RetailTradeAggregationPresenter的createSync，bm=" + bm);

        globalWriteLock.writeLock().lock();

        try {
            bm.setSyncDatetime(Constants.getDefaultSyncDatetime2());
            bm.setSyncType(BasePresenter.SYNC_Type_C);

            retailTradeAggregationMapper.create((RetailTradeAggregation) bm);
            if (bm.getID() != null) {
                bm.setSql("WHERE F_ID = %s");
                bm.setConditions(new String[]{String.valueOf(bm.getID())});
            } else {
                bm.setSql("WHERE F_ID = (select MAX(F_ID) from T_RetailTradeAggregation)");
            }
            String sql = String.format(bm.getSql(), bm.getConditions());
            Query dataQuery = entityManager.createNativeQuery(QUERY_RetailTradeAggregation_TABLE + sql, RetailTradeAggregation.class);
            List<RetailTradeAggregation> rtcList = dataQuery.getResultList();
            globalWriteLock.writeLock().unlock();

            if (rtcList != null && rtcList.size() > 0) {
                lastErrorCode = ErrorInfo.EnumErrorCode.EC_NoError;
                bm.setID(rtcList.get(0).getID());
                return (BaseModel) rtcList.get(0);
            }
            lastErrorCode = ErrorInfo.EnumErrorCode.EC_OtherError;
        } catch (Exception e) {
            log.error("执行createSync失败，错误信息为" + e.getMessage());
            e.printStackTrace();
            lastErrorCode = ErrorInfo.EnumErrorCode.EC_OtherError;

            globalWriteLock.writeLock().unlock();

        }
        return null;
    }

    @Override
    protected BaseModel retrieve1Sync(int iUseCaseID, BaseModel bm) {
        log.info("正在进行RetailTradeAggregationPresenter的retrieve1Sync，bm=" + bm);

        try {
//            bm = retailTradeAggregationMapper.retrieve1(bm.getID());
            bm = retailTradeAggregationMapper.findOne(bm.getID());
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
                    String sql = String.format(bm.getSql(), bm.getConditions());
                    Query dataQuery = entityManager.createNativeQuery(QUERY_RetailTradeAggregation_TABLE + sql, RetailTradeAggregation.class);
                    return dataQuery.getResultList();
                } catch (Exception e) {
                    log.info("执行retrieveNSync的CASE_RetailTradeAggregation_RetrieveNByConditions失败，错误信息为" + e.getMessage());
                    e.printStackTrace();
                    lastErrorCode = ErrorInfo.EnumErrorCode.EC_OtherError;

                    return null;
                }
            default:
                try {
                    lastErrorCode = ErrorInfo.EnumErrorCode.EC_NoError;

                    return retailTradeAggregationMapper.findAll();
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
        globalWriteLock.writeLock().lock();
        switch (iUseCaseID) {
            case BaseSQLiteBO.CASE_RetailTradeAggregation_DeleteOutdatedSync:
                try {
                    lastErrorCode = ErrorInfo.EnumErrorCode.EC_NoError;
                    bm.setSql("WHERE F_WorkTimeEnd <= %s");
                    bm.setConditions(new String[]{String.valueOf((new Date(System.currentTimeMillis() + NtpHttpBO.TimeDifference).getTime() - 7 * 24 * 3600 * 1000))});  // 暂时先写死一个常量，将来重构成在配置表里面获取
                    String sql = String.format(bm.getSql(), bm.getConditions());
                    Query dataQuery = entityManager.createNativeQuery(QUERY_RetailTradeAggregation_TABLE + sql, RetailTradeAggregation.class);
                    List<?> rtcList = dataQuery.getResultList();
                    for (Object o : rtcList) {
                        RetailTradeAggregation rta = (RetailTradeAggregation) o;
                        retailTradeAggregationMapper.delete(rta.getID());
                    }
                } catch (Exception e) {
                    log.info("删除RetailTradeAggregation" + OutdatedDay + "天以后的数据时出错！");
                    e.printStackTrace();
                }
                break;
            default:
                try {
                    retailTradeAggregationMapper.delete(bm.getID());
                    lastErrorCode = ErrorInfo.EnumErrorCode.EC_NoError;
                } catch (Exception e) {
                    e.printStackTrace();
                    lastErrorCode = ErrorInfo.EnumErrorCode.EC_OtherError;
                }
                break;
        }
        globalWriteLock.writeLock().unlock();
        return null;
    }

    @Override
    protected boolean createAsync(int iUseCaseID, final BaseModel bm, final BaseSQLiteEvent event) {
        log.info("正在进行RetailTradeAggregationPresenter的createAsync，bm=" + bm);

        event.setStatus(BaseEvent.EnumEventStatus.EES_SQLite_ToDo);
        switch (iUseCaseID) {
            default:
                (new Thread() {
                    @Override
                    public void run() {
//                        event.setEventTypeSQLite(BaseSQLiteEvent.EnumSQLiteEventType.ESET_RetailTradeAggregation_CreateAsync);
                        //
                        globalWriteLock.writeLock().lock();
                        System.out.println("RetailTradeAggregationPresenter createAsync正在加锁...." + globalWriteLock.writeLock().getHoldCount());
                        RetailTradeAggregation retailTradeAggregationCreated = null;
                        try {
                            RetailTradeAggregation retailTradeAggregation = (RetailTradeAggregation) bm;
                            retailTradeAggregation.setSyncDatetime(Constants.getDefaultSyncDatetime2());
                            retailTradeAggregation.setSyncType(BasePresenter.SYNC_Type_C);
                            retailTradeAggregationCreated = (RetailTradeAggregation) createSync(BaseSQLiteBO.INVALID_CASE_ID, bm);
                            event.setLastErrorCode(ErrorInfo.EnumErrorCode.EC_NoError);
                        } catch (Exception e) {
                            log.info("创建RetailTradeAggregation时出错, 错误信息: " + e.getMessage());
                            event.setLastErrorCode(ErrorInfo.EnumErrorCode.EC_OtherError);
                        }
                        //
                        event.setStatus(BaseEvent.EnumEventStatus.EES_SQLite_Done);
                        event.setBaseModel1(retailTradeAggregationCreated);
                        //
                        globalWriteLock.writeLock().unlock();
                        System.out.println("RetailTradeAggregationPresenter createAsync" + globalWriteLock.writeLock().getHoldCount());
                        EventBus.getDefault().post(event);
                    }
                }).start();
        }

        return true;
    }

    @Override
    protected boolean retrieveNAsync(int iUseCaseID, final BaseModel bm, final BaseSQLiteEvent event) {
        log.info("正在进行RetailTradeAggregationPresenter的retrieveNAsync，bm=" + bm);

        event.setStatus(BaseEvent.EnumEventStatus.EES_SQLite_ToDo);
        switch (iUseCaseID) {
            default:
                (new Thread() {
                    @Override
                    public void run() {
                        List<RetailTradeAggregation> result = null;
                        //
//                        event.setEventTypeSQLite(BaseSQLiteEvent.EnumSQLiteEventType.ESET_RetailTradeAggregation_DeleteAsync);
                        //
                        try {
                            result = retailTradeAggregationMapper.findAll();
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
                }).start();
        }

        return true;
    }

    @Override
    protected boolean deleteAsync(int iUseCaseID, final BaseModel bm, final BaseSQLiteEvent event) {
        log.info("正在进行RetailTradeAggregationPresenter的deleteAsync，bm=" + bm);

        event.setStatus(BaseEvent.EnumEventStatus.EES_SQLite_ToDo);
        switch (iUseCaseID) {
            default:
                (new Thread() {
                    @Override
                    public void run() {
//                        event.setEventTypeSQLite(BaseSQLiteEvent.EnumSQLiteEventType.ESET_RetailTradeAggregation_DeleteAsync);
                        //
                        globalWriteLock.writeLock().lock();
                        try {
                            retailTradeAggregationMapper.delete(bm.getID());
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
                        globalWriteLock.writeLock().unlock();
                    }
                }).start();
                break;
        }

        return true;
    }

    public BaseModel deleteOutdatedSync(int iUseCaseID, final BaseModel bm, final String day) {
        log.info("正在进行RetailTradeAggregationPresenter的deleteOutdatedSync，bm=" + bm + ",day=" + day);
        globalWriteLock.writeLock().lock();
        try {
//            lastErrorCode = ErrorInfo.EnumErrorCode.EC_NoError;
//            bm.setSql("where Date(F_WorkTimeStart) <= DATE(DATE_SUB(NOW(),INTERVAL ? day))");
//            bm.setConditions(new String[]{OutdatedDay});
//
//            List<RetailTradeAggregation> rtcList = retailTradeAggregationMapper.queryRaw(bm.getSql(), bm.getConditions());
//            for (RetailTradeAggregation rtc : rtcList) {
//                retailTradeAggregationMapper.delete(rtc);
//            }
        } catch (Exception e) {
            log.error("删除RetailTradeAggregation" + day + "天以后的数据时出错！");
            e.printStackTrace();
        }
        globalWriteLock.writeLock().unlock();
        return null;
    }

    public boolean deleteOutdatedASync(int iUseCaseID, final BaseModel bm, final BaseSQLiteEvent event, final String day) {
        log.info("正在进行RetailTradeAggregationPresenter的ddeleteOutdatedASync，bm=" + bm + ",day=" + day);

        event.setStatus(BaseEvent.EnumEventStatus.EES_SQLite_ToDo);
        switch (iUseCaseID) {
            default:
                (new Thread() {
                    @Override
                    public void run() {
                        globalWriteLock.writeLock().lock();
//                        event.setEventTypeSQLite(BaseSQLiteEvent.EnumSQLiteEventType.ESET_RetailTradeAggregation_DeleteAsync);
                        //
                        try {
//                            bm.setSql("where Date(F_WorkTimeStart) <= DATE(DATE_SUB(NOW(),INTERVAL ? day))");
//                            bm.setConditions(new String[]{day});
//
//                            List<RetailTradeAggregation> rtcList = retailTradeAggregationMapper.queryRaw(bm.getSql(), bm.getConditions());
//                            for (RetailTradeAggregation rtc : rtcList) {
//                                retailTradeAggregationMapper.delete(rtc);
//                            }
//                            event.setLastErrorCode(ErrorInfo.EnumErrorCode.EC_NoError);
                        } catch (Exception e) {
                            log.error("删除RetailTradeAggregation" + day + "天以后的数据时出错，错误信息：" + e.getMessage());
                            event.setLastErrorCode(ErrorInfo.EnumErrorCode.EC_OtherError);
                        }
                        //
                        event.setStatus(BaseEvent.EnumEventStatus.EES_SQLite_Done);
                        event.setBaseModel1(bm);
                        //
//                        EventBus.getDefault().post(event);
                        globalWriteLock.writeLock().unlock();
                    }
                }).start();
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
                (new Thread() {
                    @Override
                    public void run() {
                        globalWriteLock.writeLock().lock();
                        log.info("已经得到服务器返回的收银汇总数据，准备删除本地旧数据(但并不会插入服务器返回的新数据)");

                        try {
                            retailTradeAggregationMapper.delete(bmOld.getID());
                            //
                            event.setLastErrorCode(ErrorInfo.EnumErrorCode.EC_NoError);
                            event.setBaseModel2(bmNew);
                        } catch (Exception e) {
                            log.error("deleteOldObjectAsync()异常：" + e.getMessage());
                            e.printStackTrace();
                            event.setLastErrorCode(ErrorInfo.EnumErrorCode.EC_OtherError);
                        }
                        event.setStatus(BaseEvent.EnumEventStatus.EES_SQLite_Done);
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
    protected boolean updateSync(int iUseCaseID, final BaseModel bm) {
        globalWriteLock.writeLock().lock();
        try {
            RetailTradeAggregation retailTradeAggregation = (RetailTradeAggregation) bm;
            retailTradeAggregation.setSyncDatetime(Constants.getDefaultSyncDatetime2()); //SQLite中值为0。如果非正常下班的话，下次上班登录成功后，SyncDataStage将扫描并上传这个收银汇总
            retailTradeAggregation.setSyncType(BasePresenter.SYNC_Type_C);
//            retailTradeAggregationMapper.update(retailTradeAggregation);
            retailTradeAggregationMapper.save(retailTradeAggregation);
            lastErrorCode = ErrorInfo.EnumErrorCode.EC_NoError;
        } catch (Exception e) {
            log.error("更新RetailTradeAggregation时出错，错误信息：" + e.getMessage());
            lastErrorCode = ErrorInfo.EnumErrorCode.EC_OtherError;
            globalWriteLock.writeLock().unlock();
            return false;
        }
        globalWriteLock.writeLock().unlock();
        return true;
    }

    @Override
    protected boolean updateAsync(int iUseCaseID, final BaseModel bm, final BaseSQLiteEvent event) {
        log.info("正在进行RetailTradeAggregationPresenter的updateAsync，bm=" + bm);

        switch (iUseCaseID) {
            default:
                (new Thread() {
                    @Override
                    public void run() {
                        //
                        globalWriteLock.writeLock().lock();
                        try {
                            RetailTradeAggregation retailTradeAggregation = (RetailTradeAggregation) bm;
                            retailTradeAggregation.setSyncDatetime(Constants.getDefaultSyncDatetime2()); //SQLite中值为0。如果非正常下班的话，下次上班登录成功后，SyncDataStage将扫描并上传这个收银汇总
                            retailTradeAggregation.setSyncType(BasePresenter.SYNC_Type_C);
                            retailTradeAggregationMapper.save(retailTradeAggregation);
                            event.setBaseModel1(retailTradeAggregation);
                            event.setLastErrorCode(ErrorInfo.EnumErrorCode.EC_NoError);
                        } catch (Exception e) {
                            log.error("更新RetailTradeAggregation时出错，错误信息：" + e.getMessage());
                            event.setLastErrorCode(ErrorInfo.EnumErrorCode.EC_OtherError);
                            event.setBaseModel1(null);
                        }
                        event.setStatus(BaseEvent.EnumEventStatus.EES_SQLite_Done);
                        //
                        globalWriteLock.writeLock().unlock();

                        EventBus.getDefault().post(event);
                    }
                }).start();
                break;
        }

        return true;
    }
}
