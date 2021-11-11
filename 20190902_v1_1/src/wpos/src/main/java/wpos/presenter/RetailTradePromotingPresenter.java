package wpos.presenter;

//import android.util.Log;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Component;
import wpos.utils.EventBus;
import wpos.bo.BaseSQLiteBO;
import wpos.bo.NtpHttpBO;
import wpos.dao.RetailTradePromotingFlowMapper;
import wpos.dao.RetailTradePromotingMapper;
import wpos.event.BaseEvent;
import wpos.event.UI.BaseSQLiteEvent;
import wpos.helper.Constants;
import wpos.model.*;

import javax.annotation.Resource;
import javax.persistence.Query;
import javax.persistence.Table;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static wpos.bo.BaseSQLiteBO.CASE_RetailTradePromoting_RetrieveNByConditions;
import static wpos.bo.BaseSQLiteBO.CASE_RetailTradePromoting_RetrieveNByTradeID;
import static wpos.bo.BaseSQLiteBO.CASE_RetailTradePtomoting_RetrieveNToUpload;

@Component("retailTradePromotingPresenter")
public class RetailTradePromotingPresenter extends BasePresenter {
    private Log log = LogFactory.getLog(this.getClass());

//    protected static ReentrantReadWriteLock lock = new ReentrantReadWriteLock();

    public static final String QUERY_RetailTradePromoting_TABLE = "SELECT F_ID," +
            "F_TradeID," +
            "F_CreateDatetime," +
            "F_SyncDatetime," +
            "F_SyncType " +
            "FROM T_RetailTradePromoting ";

    @Resource
    private RetailTradePromotingMapper retailTradePromotingMapper;

    @Resource
    private RetailTradePromotingFlowMapper retailTradePromotingFlowMapper;

    @Resource
    private RetailTradePromotingFlowPresenter retailTradePromotingFlowPresenter;

//    public RetailTradePromotingPresenter(final RetailTradePromotingMapper retailTradePromotingMapper) {
//        super(retailTradePromotingMapper);
//    }

    @Override
    protected String getTableName() {
        if (RetailTradePromoting.class.isAnnotationPresent(Table.class)) {
            Table annotation = RetailTradePromoting.class.getAnnotation(Table.class);
            return annotation.name();
        } else {
            log.error("无法获取表名，未给" + RetailTradePromoting.class.getSimpleName() + "添加@Table。");
        }
        return "";
    }

    @Override
    protected String getQueryTable() {
        return QUERY_RetailTradePromoting_TABLE;
    }

    @Override
    public void createTableSync() {
        retailTradePromotingMapper.createTable();
    }

    @Override
    protected boolean createReplacerAsync(final int iUseCaseID, final BaseModel bmOld, final BaseModel bmNew, final BaseSQLiteEvent event) {
        log.info("正在进行RetailTradePromotingPresenter的createReplacerAsync，bmOld=" + bmOld + ",bmNew=" + bmNew);

        event.setStatus(BaseEvent.EnumEventStatus.EES_SQLite_ToApplyServerData);

        switch (iUseCaseID) {
            default:
                new Thread() {
                    @Override
                    public void run() {
                        globalWriteLock.writeLock().lock();
                        log.info("已经得到服务器返回的促销详细数据，准备删除本地旧数据，插入服务器返回的新数据...");

                        try {
                            // 删除本地存在的数据
                            if (bmOld != null) {
                                RetailTradePromoting oldRtp = (RetailTradePromoting) bmOld;
                                List<RetailTradePromotingFlow> listRetailTradePromotingFlow = (List<RetailTradePromotingFlow>) oldRtp.getListSlave1();
                                if (listRetailTradePromotingFlow != null && listRetailTradePromotingFlow.size() > 0) {
                                    for (RetailTradePromotingFlow retailTradePromotingFlow : listRetailTradePromotingFlow) {
                                        retailTradePromotingFlowMapper.delete(retailTradePromotingFlow.getID());
                                    }
                                }
                                retailTradePromotingMapper.delete(oldRtp.getID());
                            }
                            RetailTradePromoting rtpNew = (RetailTradePromoting) bmNew;
                            // 查询次零售单促销计算过程是否存在在本地数据库中
                            rtpNew.setSql("Where F_TradeID = %s ");
                            rtpNew.setConditions(new String[]{String.valueOf(rtpNew.getTradeID())});
                            String sql = String.format(rtpNew.getSql(), rtpNew.getConditions());
                            Query dataQuery = entityManager.createNativeQuery(RetailTradeCommodityPresenter.QUERY_RetailTradeCommodity_TABLE + sql, RetailTradeCommodity.class);
                            List<RetailTradePromoting> list1 = dataQuery.getResultList();
                            if (list1 != null && list1.size() > 0) {
                                for (RetailTradePromoting retailTradePromoting : list1) {
                                    retailTradePromoting.setSql("Where F_RetailTradePromotingID = %s ");
                                    retailTradePromoting.setConditions(new String[]{String.valueOf(retailTradePromoting.getID())});
                                    sql = String.format(retailTradePromoting.getSql(), retailTradePromoting.getConditions());
                                    dataQuery = entityManager.createNativeQuery(RetailTradeCommodityPresenter.QUERY_RetailTradeCommodity_TABLE + sql, RetailTradeCommodity.class);
                                    List<RetailTradePromotingFlow> retailTradePromotingFlows = dataQuery.getResultList();
                                    if (retailTradePromotingFlows != null && retailTradePromotingFlows.size() > 0) {
                                        for (RetailTradePromotingFlow retailTradePromotingFlow : retailTradePromotingFlows) {
                                            // 删除从表
                                            retailTradePromotingFlowMapper.delete(retailTradePromotingFlow.getID());
                                        }
                                    }
                                    // 删除主表
                                    retailTradePromotingMapper.delete(retailTradePromoting.getID());
                                }
                            }
                            BaseModel bmNewCreate = createSync(BaseSQLiteBO.INVALID_CASE_ID, (RetailTradePromoting) bmNew);
                            //
                            event.setLastErrorCode(ErrorInfo.EnumErrorCode.EC_NoError);
                            event.setBaseModel1(bmNewCreate);
                            //
                            log.info("插入服务器返回的主从表成功！ID = " + bmNewCreate.getID());
                        } catch (Exception e) {
                            log.info("createReplacerAsync()异常：" + e.getMessage());
                            e.printStackTrace();
                            event.setLastErrorCode(ErrorInfo.EnumErrorCode.EC_OtherError);
                        }
//                        event.setEventTypeSQLite(BaseSQLiteEvent.EnumSQLiteEventType.ESET_RetailTradePromoting_CreateReplacerAsync_Done);
                        //
                        EventBus.getDefault().post(event);
                        globalWriteLock.writeLock().unlock();
                    }
                }.start();
                break;
        }
        return true;
    }

    @Override
    protected boolean createReplacerNAsync(final int iUseCaseID, final List<?> bmOldList, final List<?> bmNewList, final BaseSQLiteEvent event) {
        log.info("正在进行RetailTradePromotingPresenter的createReplacerNAsync，bmOldList=" + (bmOldList == null ? null : bmOldList.toString())//
                + ",bmNewList=" + (bmNewList == null ? null : bmNewList.toString()));

        event.setStatus(BaseEvent.EnumEventStatus.EES_SQLite_ToApplyServerData);
        switch (iUseCaseID) {
            default:
                new Thread() {
                    @Override
                    public void run() {
                        globalWriteLock.writeLock().lock();
                        log.info("已经得到服务器返回的一批RetailTradePromoting，准备删除本地旧数据，插入新数据！");
                        try {
                            //删除旧数据
                            for (int i = 0; i < bmOldList.size(); i++) {
                                RetailTradePromoting retailTradePromotingOld = (RetailTradePromoting) bmOldList.get(i);
                                //删除从表
                                for (int j = 0; j < retailTradePromotingOld.getListSlave1().size(); j++) {
                                    retailTradePromotingFlowMapper.delete(((RetailTradePromotingFlow) retailTradePromotingOld.getListSlave1().get(j)).getID());
                                }
                                //删除主表
                                retailTradePromotingMapper.delete(retailTradePromotingOld.getID());
                                log.info("删除主表ID：" + retailTradePromotingOld.getID() + "   删除从表ID：" + ((RetailTradePromotingFlow) retailTradePromotingOld.getListSlave1().get(0)).getID() + "~" +
                                        ((RetailTradePromotingFlow) retailTradePromotingOld.getListSlave1().get(retailTradePromotingOld.getListSlave1().size() - 1)).getID());
                            }

                            //插入新数据
                            for (int i = 0; i < bmNewList.size(); i++) {
                                RetailTradePromoting retailTradePromotingNew = (RetailTradePromoting) bmNewList.get(i);
                                //插入主表
                                BaseModel retailTradePromotingNewCreate = createSync(BaseSQLiteBO.INVALID_CASE_ID, retailTradePromotingNew);
                                log.info("插入主表ID：" + retailTradePromotingNewCreate.getID() + "   插入从表ID：" + ((RetailTradePromotingFlow) retailTradePromotingNewCreate.getListSlave1().get(0)).getID() + "~" +
                                        ((RetailTradePromotingFlow) retailTradePromotingNewCreate.getListSlave1().get(retailTradePromotingNewCreate.getListSlave1().size() - 1)).getID());
                            }

                            event.setLastErrorCode(ErrorInfo.EnumErrorCode.EC_NoError);
                            event.setListMasterTable(bmNewList);
                        } catch (Exception e) {
                            e.printStackTrace();
                            log.error("RetailTradePromoting createreplacerNAsync()异常：" + e.getMessage());
                            event.setLastErrorCode(ErrorInfo.EnumErrorCode.EC_OtherError);
                        }
                        EventBus.getDefault().post(event);
                        globalWriteLock.writeLock().unlock();
                    }
                }.start();
                break;
        }
        return true;
    }

    @Override
    protected boolean retrieve1Async(int iUseCaseID, final BaseModel bm, final BaseSQLiteEvent event) {
        log.info("正在进行CommodityPresenter的retrieve1Async，bm=" + (bm == null ? null : bm.toString()));

        final RetailTradePromoting rtp = (RetailTradePromoting) bm;
        switch (iUseCaseID) {
            case CASE_RetailTradePromoting_RetrieveNByTradeID:
                new Thread() {
                    @Override
                    public void run() {
                        try {
                            lastErrorCode = ErrorInfo.EnumErrorCode.EC_NoError;
                            BaseModel retailTradePromoting = retrieve1Sync(CASE_RetailTradePromoting_RetrieveNByTradeID, rtp);
                            event.setBaseModel1(retailTradePromoting);
                            event.setLastErrorCode(ErrorInfo.EnumErrorCode.EC_NoError);
                        } catch (Exception e) {
                            e.printStackTrace();
                            log.error("根据TradeID查询RetailTradePromoting异常，异常信息为：" + e.getMessage());
                            event.setLastErrorCode(ErrorInfo.EnumErrorCode.EC_OtherError);
                        }
                        EventBus.getDefault().post(event);
                    }
                }.start();
                break;
            default:
                new Thread() {
                    @Override
                    public void run() {
                        try {
                            lastErrorCode = ErrorInfo.EnumErrorCode.EC_NoError;
                            BaseModel retailTradePromoting = retrieve1Sync(BaseSQLiteBO.INVALID_CASE_ID, rtp);
                            event.setBaseModel1(retailTradePromoting);
                            event.setLastErrorCode(ErrorInfo.EnumErrorCode.EC_NoError);
                        } catch (Exception e) {
                            log.info("执行retrieve1Async时出错，错误信息：" + e.getMessage());
                            e.printStackTrace();
                            event.setLastErrorCode(ErrorInfo.EnumErrorCode.EC_OtherError);
                        }
                        EventBus.getDefault().post(event);
                    }
                }.start();
                break;
        }
        return true;
    }

    @Override
    protected BaseModel retrieve1Sync(int iUseCaseID, BaseModel bm) {
        log.info("正在进行RetailTradePromotingPresenter的retrieve1Sync，bm=" + (bm == null ? null : bm.toString()));

        RetailTradePromoting rtp = (RetailTradePromoting) bm;
        switch (iUseCaseID) {
            case CASE_RetailTradePromoting_RetrieveNByTradeID:
                try {
                    lastErrorCode = ErrorInfo.EnumErrorCode.EC_NoError;
                    rtp.setSql("Where F_TradeID = %s");
                    rtp.setConditions(new String[]{String.valueOf(rtp.getTradeID())});
                    String sql = String.format(bm.getSql(), bm.getConditions());
                    Query dataQuery = entityManager.createNativeQuery(QUERY_RetailTradePromoting_TABLE + sql, RetailTradePromoting.class);
                    List<RetailTradePromoting> retailTradePromotings = dataQuery.getResultList();

                    if (retailTradePromotings != null && retailTradePromotings.size() == 1) {
                        rtp.setSql("Where F_RetailTradePromotingID = %s");
                        rtp.setConditions(new String[]{String.valueOf(retailTradePromotings.get(0).getID())});
                        sql = String.format(bm.getSql(), bm.getConditions());
                        dataQuery = entityManager.createNativeQuery(RetailTradePromotingFlowPresenter.QUERY_RetailTradePromotingFlow_TABLE + sql, RetailTradePromotingFlow.class);
                        List<RetailTradePromotingFlow> retailTradePromotingFlows = dataQuery.getResultList();
                        retailTradePromotings.get(0).setListSlave1(retailTradePromotingFlows);
                        return retailTradePromotings.get(0);
                    }
                    return null;
                } catch (Exception e) {
                    log.info("执行retrieve1Sync时出错，错误信息：" + e.getMessage());
                    e.printStackTrace();
                    lastErrorCode = ErrorInfo.EnumErrorCode.EC_OtherError;

                    return null;
                }
            default:
                try {
                    RetailTradePromoting retailTradePromoting = (RetailTradePromoting) retailTradePromotingMapper.findOne(rtp.getID());
                    if (retailTradePromoting != null) {//查出从表
                        rtp.setSql("Where F_RetailTradePromotingID = %s");
                        rtp.setConditions(new String[]{String.valueOf(retailTradePromoting.getID())});
                        String sql = String.format(bm.getSql(), bm.getConditions());
                        Query dataQuery = entityManager.createNativeQuery(RetailTradePromotingFlowPresenter.QUERY_RetailTradePromotingFlow_TABLE + sql, RetailTradePromotingFlow.class);
                        List<RetailTradePromotingFlow> retailTradePromotingFlows = dataQuery.getResultList();
                        retailTradePromoting.setListSlave1(retailTradePromotingFlows);
                    }
                    lastErrorCode = ErrorInfo.EnumErrorCode.EC_NoError;
                    return retailTradePromoting;
                } catch (Exception e) {
                    log.info("执行retrieve1Sync时出错，错误信息：" + e.getMessage());
                    e.printStackTrace();
                    lastErrorCode = ErrorInfo.EnumErrorCode.EC_OtherError;
                    return null;
                }
        }
    }

    @Override
    protected List<?> retrieveNSync(int iUseCaseID, BaseModel bm) {
        log.info("正在进行RetailTradePromotingPresenter的retrieveNSync，bm=" + (bm == null ? null : bm.toString()));

        switch (iUseCaseID) {
            case CASE_RetailTradePromoting_RetrieveNByConditions:
                try {
                    RetailTradePromoting retailTradePromoting = (RetailTradePromoting) bm;
                    lastErrorCode = ErrorInfo.EnumErrorCode.EC_NoError;
                    String sql = String.format(retailTradePromoting.getSql(), retailTradePromoting.getConditions());
                    Query dataQuery = entityManager.createNativeQuery(QUERY_RetailTradePromoting_TABLE + sql, RetailTradePromoting.class);
                    List<?> list = dataQuery.getResultList();
                    List<BaseModel> newList = null;
                    if (list != null) {
                        newList = new ArrayList<BaseModel>();
                        for (Object o : list) {
                            RetailTradePromoting rtp = (RetailTradePromoting) o;
                            BaseModel baseModel = retrieve1Sync(BaseSQLiteBO.INVALID_CASE_ID, rtp);
                            newList.add(baseModel);
                        }
                    }
                    return newList;
                } catch (Exception e) {
                    log.error("执行retrieveNSync时出错，错误信息：" + e.getMessage());
                    e.printStackTrace();
                    lastErrorCode = ErrorInfo.EnumErrorCode.EC_OtherError;

                    return null;
                }
            default:
                try {
                    lastErrorCode = ErrorInfo.EnumErrorCode.EC_NoError;

                    return retailTradePromotingMapper.findAll();
                } catch (Exception e) {
                    e.printStackTrace();
                    lastErrorCode = ErrorInfo.EnumErrorCode.EC_NoError;
                    log.error("错误为" + lastErrorCode);
                    return null;
                }
        }
    }

    @Override
    protected boolean createMasterSlaveAsync(final int iUseCaseID, final BaseModel bmMaster, final BaseSQLiteEvent event) {
        log.info("正在进行RetailTradePromotingPresenter的createMasterSlaveAsync，bmMaster=" + bmMaster);

        event.setStatus(BaseEvent.EnumEventStatus.EES_SQLite_ToDo);
        switch (event.getEventTypeSQLite()) {
            case ESET_RetailTradePromoting_CreateMasterSlaveAsync_Done:
                log.info("准备向本地SQLite插入RetailTradePromoting主从表数据....");

                new Thread() {
                    @Override
                    public void run() {
                        globalWriteLock.writeLock().lock();
                        try {
                            RetailTradePromoting retailTradePromotingCreate = (RetailTradePromoting) createSync(iUseCaseID, bmMaster);
                            //
                            event.setLastErrorCode(ErrorInfo.EnumErrorCode.EC_NoError);

                            log.info("向本地SQLite插入主从表数据成功！RetailTradePromotingFlow=" + retailTradePromotingCreate.getID() + "  "
                                    + (retailTradePromotingCreate.getListSlave1().size() > 0 ? ((RetailTradePromotingFlow) retailTradePromotingCreate.getListSlave1().get(0)).getID() + "~" //打印第1个ID
                                    + ((RetailTradePromotingFlow) retailTradePromotingCreate.getListSlave1().get(((RetailTradePromoting) retailTradePromotingCreate).getListSlave1().size() - 1)).getID() : "")//打印最后1个ID
                            );
                            event.setBaseModel1(retailTradePromotingCreate);
                            event.setTmpMasterTableObj(retailTradePromotingCreate);
                        } catch (Exception e) {
                            e.printStackTrace();
                            event.setLastErrorCode(ErrorInfo.EnumErrorCode.EC_OtherError);

                            log.error("向本地SQLite插入主从表数据失败！原因：" + e.getMessage());
                        }
                        //
                        switch (iUseCaseID) {
                            case BaseSQLiteBO.CASE_RetailTradePromoting_CreateMasterSlaveSQLite:
                                event.setEventTypeSQLite(BaseSQLiteEvent.EnumSQLiteEventType.ESET_RetailTradePromoting_RefreshMasterSlaveAsyncSQLite_Done);
                                break;
                            default:
                                break;
                        }
                        EventBus.getDefault().post(event);
                        globalWriteLock.writeLock().unlock();
                    }
                }.start();
                break;
            default:
                log.info(" 未定义的事件！");
                throw new RuntimeException("未定义的事件！");
        }
        return true;
    }

    @Override
    protected BaseModel createSync(int iUseCaseID, BaseModel bm) {
        log.info("正在进行RetailTradePromotingPresenter的createSync，bm=" + (bm == null ? null : bm.toString()));

        globalWriteLock.writeLock().lock();

        try {
            if (bm.getSyncDatetime() == null) {
                bm.setSyncDatetime(Constants.getDefaultSyncDatetime2());
            }
            if (((RetailTradePromoting) bm).getCreateDatetime() == null) {
                ((RetailTradePromoting) bm).setCreateDatetime(new Date(System.currentTimeMillis() + NtpHttpBO.TimeDifference));
            }
            bm.setSyncType(BasePresenter.SYNC_Type_C);
            retailTradePromotingMapper.create((RetailTradePromoting) bm);

            if (bm.getID() != null) {
                bm.setSql("WHERE F_ID = %s");
                bm.setConditions(new String[]{String.valueOf(bm.getID())});
            } else {
                bm.setSql("WHERE F_ID = (select MAX(F_ID) from T_RetailTradePromoting)");
            }
            String sql = String.format(bm.getSql(), bm.getConditions());
            Query dataQuery = entityManager.createNativeQuery(QUERY_RetailTradePromoting_TABLE + sql, RetailTradePromoting.class);
            List<?> rtcList = dataQuery.getResultList();

            RetailTradePromoting retailTradePromoting = new RetailTradePromoting();
            if (rtcList != null && rtcList.size() > 0) {
                retailTradePromoting = (RetailTradePromoting) rtcList.get(0);
            }
            //
            List<BaseModel> rtpfList = (List<BaseModel>) bm.getListSlave1();
            List<BaseModel> rtpCreateList = new ArrayList<BaseModel>();
            if (rtpfList != null) {
                for (int i = 0; i < rtpfList.size(); i++) {
                    ((RetailTradePromotingFlow) rtpfList.get(i)).setRetailTradePromotingID(retailTradePromoting.getID());
                    RetailTradePromotingFlow retailTradePromotingFlow = (RetailTradePromotingFlow) retailTradePromotingFlowPresenter.createSync(BaseSQLiteBO.INVALID_CASE_ID, (RetailTradePromotingFlow) rtpfList.get(i));
                    rtpCreateList.add(retailTradePromotingFlow);
                }
            }
            log.info("插入本地的零售单促销新数据成功！PromotionID=" + retailTradePromoting.getID() + "  "
                    + (rtpCreateList != null ? (rtpCreateList.get(0)).getID() + "~" //打印第1个ID
                    + (rtpCreateList.get(rtpCreateList.size() - 1)).getID() : "")//打印最后1个ID
            );
            lastErrorCode = ErrorInfo.EnumErrorCode.EC_NoError;
            retailTradePromoting.setListSlave1(rtpCreateList);
            globalWriteLock.writeLock().unlock();
            return retailTradePromoting;
        } catch (Exception e) {
            log.error("执行createSync时出错，错误信息：" + e.getMessage());
            e.printStackTrace();
            lastErrorCode = ErrorInfo.EnumErrorCode.EC_OtherError;
        }
        globalWriteLock.writeLock().unlock();
        return null;
    }

//    public boolean retrieveNByIDs(final int iUseCaseID, final List<?> bmList, final BaseSQLiteEvent event) {
//        event.setStatus(BaseEvent.EnumEventStatus.EES_SQLite_ToDo);
//        switch (iUseCaseID) {
//            default:
//                new Thread() {
//                    @Override
//                    public void run() {
//                        try {
//                            List<RetailTradePromoting> retailTradePromotingList = new ArrayList<RetailTradePromoting>();
//                            if (bmList.size() > 0) {
//                                for (int i = 0; i < bmList.size(); i++) {
//                                    RetailTradePromoting rtp = new RetailTradePromoting();
//                                    rtp.setID((long) ((RetailTrade)bmList.get(i)).getInt2());
//                                    RetailTradePromoting retailTradePromoting = (RetailTradePromoting) retrieve1Sync(Constants.INVALID_CASE_ID, rtp);
//                                    retailTradePromotingList.add(retailTradePromoting);
//                                }
//                                if (retailTradePromotingList.size() > 0) {
//                                    event.setListMasterTable(retailTradePromotingList);
//                                } else {
//                                    event.setListMasterTable(null);
//                                }
//                            }
//                            event.setLastErrorCode(ErrorInfo.EnumErrorCode.EC_NoError);
//                        } catch (Exception e) {
//                            e.printStackTrace();
//                            event.setLastErrorCode(ErrorInfo.EnumErrorCode.EC_OtherError);
//                        }
//                        EventBus.getDefault().post(event);
//                    }
//                });
//                break;
//        }
//        return true;
//    }

    @Override
    protected boolean retrieveNAsync(int iUseCaseID, final BaseModel bm, final BaseSQLiteEvent event) {
        log.info("正在进行RetailTradePromotingPresenter的retrieveNAsync，bm=" + (bm == null ? null : bm.toString()));

        switch (iUseCaseID) {
            case CASE_RetailTradePtomoting_RetrieveNToUpload:
                new Thread() {
                    @Override
                    public void run() {
                        List<RetailTradePromoting> retailTradePromotingList = new ArrayList<RetailTradePromoting>();
                        //
                        try {
                            RetailTradePromoting retailTradePromoting = new RetailTradePromoting();
                            //找主表
                            retailTradePromoting.setSql("where F_SyncDatetime = %s");
                            retailTradePromoting.setConditions(new String[]{"0"});
                            String sql = String.format(retailTradePromoting.getSql(), retailTradePromoting.getConditions());
                            Query dataQuery = entityManager.createNativeQuery(QUERY_RetailTradePromoting_TABLE + sql, RetailTradePromoting.class);
                            retailTradePromotingList = dataQuery.getResultList();
                            if (retailTradePromotingList != null) {
                                //找从表
                                for (int i = 0; i < retailTradePromotingList.size(); i++) {
                                    retailTradePromoting.setSql("where F_RetailTradePromotingID = %s");
                                    retailTradePromoting.setConditions(new String[]{String.valueOf(retailTradePromotingList.get(i).getID())});
                                    sql = String.format(retailTradePromoting.getSql(), retailTradePromoting.getConditions());
                                    dataQuery = entityManager.createNativeQuery(RetailTradePromotingFlowPresenter.QUERY_RetailTradePromotingFlow_TABLE + sql, RetailTradePromotingFlow.class);
                                    List<RetailTradePromotingFlow> retailTradePromotingFlowList = dataQuery.getResultList();
                                    retailTradePromotingList.get(i).setListSlave1(retailTradePromotingFlowList);
                                }
                            }
                            event.setListMasterTable(retailTradePromotingList);
                            event.setLastErrorCode(ErrorInfo.EnumErrorCode.EC_NoError);
                        } catch (Exception e) {
                            e.printStackTrace();
                            log.info("查找临时RetailTradePromoting异常，异常信息为：" + e.getMessage());
                            event.setLastErrorCode(ErrorInfo.EnumErrorCode.EC_OtherError);
                        }
                        EventBus.getDefault().post(event);
                    }
                }.start();
                break;
            case BaseSQLiteBO.CASE_RetailTradePromoting_RetrieveNByConditions:
                new Thread() {
                    @Override
                    public void run() {
                        List<RetailTradePromoting> result = null;
                        try {
                            String sql = String.format(bm.getSql(), bm.getConditions());
                            Query dataQuery = entityManager.createNativeQuery(QUERY_RetailTradePromoting_TABLE + sql, RetailTradePromoting.class);
                            result = dataQuery.getResultList();
                            event.setLastErrorCode(ErrorInfo.EnumErrorCode.EC_NoError);
                        } catch (Exception e) {
                            e.printStackTrace();
                            log.error("RetrieveNAsync RetailTrade出错，错误信息：" + e.getMessage());
                            event.setLastErrorCode(ErrorInfo.EnumErrorCode.EC_OtherError);
                        }
                        //
                        event.setListMasterTable(result);
                        //
                        EventBus.getDefault().post(event);
                    }
                }.start();
                break;
            default:
                break;
        }
        return true;
    }

    @Override
    protected boolean updateNAsync(int iUseCaseID, final List<?> bmList, final BaseSQLiteEvent event) {
        log.info("正在进行RetailTradePromotingPresenter的updateNAsync，bmList=" + (bmList == null ? null : bmList.toString()));

        event.setStatus(BaseEvent.EnumEventStatus.EES_SQLite_ToDo);
        new Thread() {
            @Override
            public void run() {
                try {
                    globalWriteLock.writeLock().lock();
                    for (int i = 0; i < bmList.size(); i++) {
                        retailTradePromotingMapper.save((RetailTradePromoting) bmList.get(i));
                        log.info("update后的RetailTradePromoting：" + ((RetailTradePromoting) bmList.get(i)).toString());
                    }
                    event.setLastErrorCode(ErrorInfo.EnumErrorCode.EC_NoError);
                } catch (Exception e) {
                    e.printStackTrace();
                    log.error("Update RetailTradePromoting 异常，异常信息为：" + e.getMessage());
                    event.setLastErrorCode(ErrorInfo.EnumErrorCode.EC_OtherError);
                }
                EventBus.getDefault().post(event);
                globalWriteLock.writeLock().unlock();
            }
        }.start();
        return true;
    }

    @Override
    protected BaseModel deleteNSync(int iUserCaseID, BaseModel bm) {
        log.info("正在进行RetailTradePromotingPresenter的deleteNSync，bm=" + (bm == null ? null : bm.toString()));
        globalWriteLock.writeLock().lock();
        try {
            retailTradePromotingMapper.deleteAll();
            lastErrorCode = ErrorInfo.EnumErrorCode.EC_NoError;
        } catch (Exception e) {
            e.printStackTrace();
            lastErrorCode = ErrorInfo.EnumErrorCode.EC_OtherError;
        }
        globalWriteLock.writeLock().unlock();
        return null;
    }

    @Override
    protected BaseModel deleteSync(int iUseCaseID, final BaseModel bm) {
        log.info("正在进行RetailTradePromotingPresenter的deleteSync，bm=" + (bm == null ? null : bm.toString()));
        globalWriteLock.writeLock().lock();
        try {
            retailTradePromotingMapper.delete(bm.getID());
            lastErrorCode = ErrorInfo.EnumErrorCode.EC_NoError;
        } catch (Exception e) {
            log.error("执行deleteSync时出错，错误信息：" + e.getMessage());
            e.printStackTrace();
            lastErrorCode = ErrorInfo.EnumErrorCode.EC_OtherError;
        }
        globalWriteLock.writeLock().unlock();
        return null;
    }
}
