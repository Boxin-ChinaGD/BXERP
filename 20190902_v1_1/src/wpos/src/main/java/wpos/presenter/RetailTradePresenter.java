
package wpos.presenter;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Component;
import wpos.utils.EventBus;
import wpos.bo.BaseSQLiteBO;
import wpos.bo.NtpHttpBO;
import wpos.dao.RetailTradeCommodityMapper;
import wpos.dao.RetailTradeMapper;
import wpos.dao.RetailTradePromotingFlowMapper;
import wpos.dao.RetailTradePromotingMapper;
import wpos.event.BaseEvent;
import wpos.event.UI.BaseSQLiteEvent;
import wpos.helper.Constants;
import wpos.model.*;
import wpos.utils.DatetimeUtil;
import wpos.utils.StringUtils;

import javax.annotation.Resource;
import javax.persistence.Query;
import javax.persistence.Table;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Component("retailTradePresenter")
public class RetailTradePresenter extends BasePresenter {
    private static Log log = LogFactory.getLog(RetailTradePresenter.class);

    public static final String QUERY_RetailTrade_TABLE = "SELECT F_ID," +
            "F_VipID," +
            "F_SN," +
            "F_LocalSN," +
            "F_POS_ID," +
            "F_Logo," +
            "F_SaleDatetime," +
            "F_StaffID," +
            "F_PaymentType," +
            "F_PaymentAccount," +
            "F_Status," +
            "F_Remark," +
            "F_SourceID," +
            "F_SyncDatetime, " +
            "F_Amount, " +
            "F_AmountCash, " +
            "F_AmountAlipay, " +
            "F_AmountWeChat, " +
            "F_Amount1, " +
            "F_Amount2, " +
            "F_Amount3, " +
            "F_Amount4, " +
            "F_Amount5 , " +
            "F_SmallSheetID, " +
            "F_AliPayOrderSN, " +
            "F_WxOrderSN, " +
            "F_WxTradeNO, " +
            "F_WxRefundNO, " +
            "F_WxRefundDesc, " +
            "F_WxRefundSubMchID, " +
            "F_CouponAmount, " +
            "F_ConsumerOpenID, " +
            "F_SyncType, " +
            "F_SlaveCreated, " +
            "F_ShopID " +
            "FROM T_RetailTrade ";

//    protected static ReentrantReadWriteLock lock = new ReentrantReadWriteLock();

    /**
     * 仅测试代码能调用。=true，代表当前工作在测试环境中。=false，代表是App在运行
     */
    public static boolean bInTestMode = false;

    @Resource
    private RetailTradeMapper retailTradeMapper;

    @Resource
    private RetailTradeCommodityMapper retailTradeCommodityMapper;

    @Resource
    private RetailTradePromotingMapper retailTradePromotingMapper;

    @Resource
    private RetailTradePromotingFlowMapper retailTradePromotingFlowMapper;

    @Resource
    private RetailTradeCommodityPresenter retailTradeCommodityPresenter;

    @Resource
    private RetailTradePromotingPresenter retailTradePromotingPresenter;

//    public RetailTradePresenter(final RetailTradeMapper retailTradeMapper) {
//        super(retailTradeMapper);
//    }

    @Override
    protected String getTableName() {
        if (RetailTrade.class.isAnnotationPresent(Table.class)) {
            Table annotation = RetailTrade.class.getAnnotation(Table.class);
            return annotation.name();
        } else {
            log.error("无法获取表名，未给" + RetailTrade.class.getSimpleName() + "添加@Table。");
        }
        return "";
    }

    @Override
    protected String getQueryTable() {
        return QUERY_RetailTrade_TABLE;
    }

    @Override
    public void createTableSync() {
        retailTradeMapper.createTable();
    }

    @Override
    protected List<BaseModel> createNSync(int iUseCaseID, final List<?> list) {
        log.info("正在进行RetailTradePresenter的createNSync，list=" + (list == null ? null : list.toString()));

        switch (iUseCaseID) {
            default:
                globalWriteLock.writeLock().lock();
                try {
                    List<BaseModel> retailTradeCreateList = new ArrayList<BaseModel>();
                    for (int i = 0; i < list.size(); i++) {
                        ((RetailTrade) list.get(i)).setSyncDatetime(Constants.getDefaultSyncDatetime2());
                        ((RetailTrade) list.get(i)).setSyncType(BasePresenter.SYNC_Type_C);

                        RetailTrade retailTrade = (RetailTrade) createSync(BaseSQLiteBO.INVALID_CASE_ID, (RetailTrade) list.get(i));
                        log.info("RetailTrade创建成功，ID=" + retailTrade.getID());
                        retailTradeCreateList.add(retailTrade);
                    }
                    lastErrorCode = ErrorInfo.EnumErrorCode.EC_NoError;
                    globalWriteLock.writeLock().unlock();
                    return retailTradeCreateList;
                } catch (Exception e) {
                    log.info("执行createNSync失败，错误信息为" + e.getMessage());
                    e.printStackTrace();
                    lastErrorCode = ErrorInfo.EnumErrorCode.EC_OtherError;
                }
                globalWriteLock.writeLock().unlock();
                return null;
        }
    }

    @Override
    protected BaseModel createSync(int iUseCaseID, BaseModel bm) {
        log.info("正在进行RetailTradePresenter的createSync，bm=" + (bm == null ? null : bm.toString()));
        globalWriteLock.writeLock().lock();
        try {
            RetailTrade retailTrade = (RetailTrade) bm;
            if (bm.getSyncDatetime() == null) {
                bm.setSyncDatetime(Constants.getDefaultSyncDatetime2());
            }
            bm.setSyncType(BasePresenter.SYNC_Type_C);
            retailTradeMapper.create((RetailTrade) bm);
            String sql = "";
            if (retailTrade.getID() != null) {
                retailTrade.setSql("WHERE F_ID = %s");
                retailTrade.setConditions(new String[]{String.valueOf(bm.getID())});
                sql = String.format(retailTrade.getSql(), retailTrade.getConditions());
            } else {
                sql = "WHERE F_ID = (select MAX(F_ID) from T_RetailTrade)";
            }

            Query dataQuery = entityManager.createNativeQuery(QUERY_RetailTrade_TABLE + sql, RetailTrade.class);
            List<?> rtList = dataQuery.getResultList();

            RetailTrade retailTradeCreate = new RetailTrade();
            if (rtList != null && rtList.size() > 0) {
                retailTradeCreate = (RetailTrade) rtList.get(0);
                retailTradeCreate.fillNonDBFieldValue(iUseCaseID, bm);
            }
            // 创建零售单从表数据
            List<RetailTradeCommodity> rtcList = (List<RetailTradeCommodity>) bm.getListSlave1();
            List<RetailTradeCommodity> rtcListInSQLite = new ArrayList<RetailTradeCommodity>();
            RetailTradeCommodity retailTradeCommodity = new RetailTradeCommodity();
            if (rtcList != null) {
                for (int i = 0; i < rtcList.size(); i++) {
                    rtcList.get(i).setTradeID((long) retailTradeCreate.getID());
                    retailTradeCommodity = (RetailTradeCommodity) retailTradeCommodityPresenter.createSync(BaseSQLiteBO.INVALID_CASE_ID, rtcList.get(i));
                    rtcListInSQLite.add(retailTradeCommodity);
                }
                retailTradeCreate.setListSlave1(rtcListInSQLite);
            }
            log.info("向本地SQLite插入主从表数据成功！TradeID=" + retailTradeCreate.getID() + "  "
                    + (retailTradeCreate.getListSlave1() != null ? ((List<RetailTradeCommodity>) retailTradeCreate.getListSlave1()).get(0).getID() + "~" //打印第1个ID
                    + ((RetailTradeCommodity) retailTradeCreate.getListSlave1().get(retailTradeCreate.getListSlave1().size() - 1)).getID() : "")//打印最后1个ID
            );
            //
            if (!bInTestMode) {
                retailTradeCreate.setSlaveCreated(BaseModel.EnumBoolean.EB_Yes.getIndex());
                retailTradeMapper.save(retailTradeCreate);
            }
            lastErrorCode = ErrorInfo.EnumErrorCode.EC_NoError;
            globalWriteLock.writeLock().unlock();

            return retailTradeCreate;
        } catch (Exception e) {
            log.error("执行createSync失败，错误信息为" + e.getMessage());
            e.printStackTrace();
            lastErrorCode = ErrorInfo.EnumErrorCode.EC_OtherError;
        }
        globalWriteLock.writeLock().unlock();
        return null;
    }

    @Override
    protected BaseModel deleteNSync(int iUseCaseID, final BaseModel bm) {
        globalWriteLock.writeLock().lock();
        log.info("正在进行RetailTradePresenter的deleteNSync，bm=" + (bm == null ? null : bm.toString()));
        try {
            retailTradeCommodityMapper.deleteAll();
            retailTradeMapper.deleteAll();
            lastErrorCode = ErrorInfo.EnumErrorCode.EC_NoError;
        } catch (Exception e) {
            e.printStackTrace();
            lastErrorCode = ErrorInfo.EnumErrorCode.EC_OtherError;
        }
        globalWriteLock.writeLock().unlock();
        return null;
    }

    @Override
    protected boolean updateSync(int iUseCaseID, BaseModel bm) {
        return true;
    }

    @Override
    protected BaseModel retrieve1Sync(int iUseCaseID, BaseModel bm) {
        log.info("正在进行RetailTradePresenter的retrieve1Sync，bm=" + (bm == null ? null : bm.toString()));

        try {
            BaseModel retailTrade = retailTradeMapper.findOne(bm.getID());

            if (retailTrade != null) {
                RetailTradeCommodity retailTradeCommodity = new RetailTradeCommodity();
                retailTradeCommodity.setSql("where F_TradeID = %s");
                retailTradeCommodity.setConditions(new String[]{String.valueOf(retailTrade.getID())});
                //
                String sql = String.format(retailTradeCommodity.getSql(), retailTradeCommodity.getConditions());
                Query dataQuery = entityManager.createNativeQuery(RetailTradeCommodityPresenter.QUERY_RetailTradeCommodity_TABLE + sql, RetailTradeCommodity.class);
                retailTrade.setListSlave1(dataQuery.getResultList());
            }

            lastErrorCode = ErrorInfo.EnumErrorCode.EC_NoError;
            return retailTrade;
        } catch (Exception e) {
            log.error("执行retrieve1Sync失败，错误信息为" + e.getMessage());
            e.printStackTrace();
            lastErrorCode = ErrorInfo.EnumErrorCode.EC_OtherError;

            return null;
        }
    }

//    @RequiresApi(api = Build.VERSION_CODES.N)
//    public List<RetailTrade> retrieveNAsyncC(BaseHttpBO.CASE_RetailTrade_RetrieveNOldRetailTradeAsyncC, BaseModel bm) {
//        log.info("本地服务器当中的数据有：" + retailTradeMapper.loadAll());
//        List<RetailTrade> list = new ArrayList<>();
//        RetailTrade retailTrade = (RetailTrade) bm;
//        try {
//            String sql = "select (case " + retailTrade.getSn() +" when '' then 1 = 1 else F_SN = '' end ) and (case '' when '' then 1 = 1 else " +
//                    "F_POS_SN = '' end ) and (case 0 when 0 then 1 = 1 else F_POS_ID = 0 end ) and F_SaleDatetime >= "
//                    + "0 and F_SaleDatetime <= 1544524080185 from retailTrade";
//
////            String sql = "select (case" + retailTrade.getSn() + " when '' then 1 = 1 else F_SN =" + retailTrade.getSn() + " end ) and (case " + retailTrade.getPos_SN() + " when '' then 1 = 1 else " +
////                    "F_POS_SN = " + retailTrade.getPos_SN() + " end ) and (case " + retailTrade.getPos_ID() + " when 0 then 1 = 1 else F_POS_ID =" + retailTrade.getPos_ID() + " end ) and F_SaleDatetime >= "
////                    + dateToStamp(retailTrade.getDatetimeStart()) + " and F_SaleDatetime <= " + dateToStamp(retailTrade.getDatetimeEnd()) + " from retailTrade";
//
//            Cursor cursor = dao.getDatabase().rawQuery(sql, null);
//            cursor.moveToNext();
//            if (cursor.moveToFirst() == false) {
//                log.info("pagingRetrieveNAsync找不到数据!");
//            } else {
//                RetailTrade rt = new RetailTrade();
//                rt.setID(cursor.getLong(0));
//                rt.setSaleDatetime(Constants.getSimpleDateFormat().parse(cursor.getColumnName(5)));
//                rt.setAmount(cursor.getDouble(13));
//                list.add(rt);
//                while (cursor.moveToNext()) {
//                    rt = new RetailTrade();
//                    rt.setID(cursor.getLong(0));
//                    rt.setSaleDatetime(Constants.getSimpleDateFormat().parse(cursor.getString(5)));
//                    rt.setAmount(cursor.getDouble(13));
//                    list.add(rt);
//                }
//            }
//        } catch (ParseException e) {
//            e.printStackTrace();
//        }
//        return list;
//    }

    //    @RequiresApi(api = Build.VERSION_CODES.N)
    public static String dateToStamp(Date date) throws ParseException {
        String res;
        long ts = date.getTime();
        res = String.valueOf(ts);
        return res;
    }

    @Override
    protected List<?> retrieveNSync(int iUseCaseID, BaseModel bm) {
        log.info("正在进行RetailTradePresenter的retrieveNSync，bm=" + (bm == null ? null : bm.toString()));
        List<RetailTrade> retailTradeList;
        RetailTrade retailTrade = new RetailTrade();
        switch (iUseCaseID) {
            case BaseSQLiteBO.CASE_RetailTrade_RetrieveNToUpload:
                retailTrade.setSql("where F_SyncDatetime = %s and F_SlaveCreated = %s");
                retailTrade.setConditions(new String[]{"0", String.valueOf(BaseModel.EnumBoolean.EB_Yes.getIndex())});
                String sql = String.format(retailTrade.getSql(), retailTrade.getConditions());
                Query dataQuery = entityManager.createNativeQuery(QUERY_RetailTrade_TABLE + sql, RetailTrade.class);
                retailTradeList = dataQuery.getResultList();
                try {
                    if (retailTradeList != null) {
                        for (int i = 0; i < retailTradeList.size(); i++) {
                            retailTrade.setSql("where F_TradeID = %s");
                            retailTrade.setConditions(new String[]{String.valueOf(retailTradeList.get(i).getID())});
                            //
                            sql = String.format(retailTrade.getSql(), retailTrade.getConditions());
                            dataQuery = entityManager.createNativeQuery(RetailTradeCommodityPresenter.QUERY_RetailTradeCommodity_TABLE + sql, RetailTradeCommodity.class);
                            List<RetailTradeCommodity> retailTradeCommodityList = dataQuery.getResultList();
                            //
                            retailTradeList.get(i).setListSlave1(retailTradeCommodityList);
                            retailTradeList.get(i).setDatetimeStart(Constants.getDefaultSyncDatetime2());
                            retailTradeList.get(i).setDatetimeEnd(Constants.getDefaultSyncDatetime2());
                            retailTradeList.get(i).setSyncDatetime(Constants.getDefaultSyncDatetime2());
                            retailTradeList.get(i).setReturnObject(1);
                        }
                    }
                    lastErrorCode = ErrorInfo.EnumErrorCode.EC_NoError;
                    return retailTradeList;
                } catch (ParseException e) {
                    log.info("RetailTradePresenter.retrieveNSync()发生异常：" + e.getMessage());
                    e.printStackTrace();
                    lastErrorCode = ErrorInfo.EnumErrorCode.EC_OtherError;
                }
                return null;
            case BaseSQLiteBO.CASE_RetailTrade_RetrieveNByConditions:
                try {
                    sql = String.format(bm.getSql(), bm.getConditions());
                    dataQuery = entityManager.createNativeQuery(QUERY_RetailTrade_TABLE + sql, RetailTrade.class);
                    retailTradeList = dataQuery.getResultList();
                    if (retailTradeList != null) {
                        for (int i = 0; i < retailTradeList.size(); i++) {
                            retailTrade.setSql("where F_TradeID = %s");
                            retailTrade.setConditions(new String[]{String.valueOf(retailTradeList.get(i).getID())});
                            sql = String.format(retailTrade.getSql(), retailTrade.getConditions());
                            dataQuery = entityManager.createNativeQuery(RetailTradeCommodityPresenter.QUERY_RetailTradeCommodity_TABLE + sql, RetailTradeCommodity.class);
                            List<RetailTradeCommodity> retailTradeCommodityList = dataQuery.getResultList();
                            //
                            retailTradeList.get(i).setListSlave1(retailTradeCommodityList);
                            retailTradeList.get(i).setDatetimeStart(Constants.getDefaultSyncDatetime2());
                            retailTradeList.get(i).setDatetimeEnd(Constants.getDefaultSyncDatetime2());
                            retailTradeList.get(i).setSyncDatetime(Constants.getDefaultSyncDatetime2());
                            retailTradeList.get(i).setReturnObject(1);
                        }
                    }
                    lastErrorCode = ErrorInfo.EnumErrorCode.EC_NoError;
                    return retailTradeList;
                } catch (Exception e) {
                    log.error("RetailTradePresenter.retrieveNSync()发生异常：" + e.getMessage());
                    e.printStackTrace();
                    lastErrorCode = ErrorInfo.EnumErrorCode.EC_OtherError;
                    return null;
                }
            case BaseSQLiteBO.CASE_RetailTrade_RetrieveNForReturned:
                // 根据SourceID查询本地SQLite的零售单
                try {
                    RetailTrade returnRetailTrade = (RetailTrade) bm;
                    RetailTrade retailTrade2 = new RetailTrade();
                    retailTrade2.setSql("where F_SourceID = %s");
                    retailTrade2.setConditions(new String[]{String.valueOf(returnRetailTrade.getSourceID())});
                    sql = String.format(retailTrade2.getSql(), retailTrade2.getConditions());
                    dataQuery = entityManager.createNativeQuery(QUERY_RetailTrade_TABLE + sql, RetailTrade.class);
                    retailTradeList = dataQuery.getResultList();
                    if (retailTradeList != null) {
                        for (int i = 0; i < retailTradeList.size(); i++) {
                            RetailTradeCommodity retailTradeCommodity = new RetailTradeCommodity();
                            retailTradeCommodity.setSql("where F_TradeID = %s");
                            retailTradeCommodity.setConditions(new String[]{String.valueOf(retailTradeList.get(i).getID())});
//                            sql = String.format(retailTrade2.getSql(), retailTrade2.getConditions());
                            String sqlRtc = String.format(retailTradeCommodity.getSql(), retailTradeCommodity.getConditions());
                            Query dataQueryRtc = entityManager.createNativeQuery(RetailTradeCommodityPresenter.QUERY_RetailTradeCommodity_TABLE + sqlRtc, RetailTradeCommodity.class);
//                            retailTradeList = dataQuery.getResultList();
                            List<RetailTradeCommodity> retailTradeCommodityList = dataQueryRtc.getResultList();
                            //
                            retailTradeList.get(i).setListSlave1(retailTradeCommodityList);
                            retailTradeList.get(i).setDatetimeStart(Constants.getDefaultSyncDatetime2());
                            retailTradeList.get(i).setDatetimeEnd(Constants.getDefaultSyncDatetime2());
                            retailTradeList.get(i).setSyncDatetime(Constants.getDefaultSyncDatetime2());
                            retailTradeList.get(i).setReturnObject(1);
                        }
                    }
                    lastErrorCode = ErrorInfo.EnumErrorCode.EC_NoError;
                    return retailTradeList;
                } catch (Exception e) {
                    log.info("RetailTradePresenter.retrieveNSync()发生异常：" + e.getMessage());
                    e.printStackTrace();
                    lastErrorCode = ErrorInfo.EnumErrorCode.EC_OtherError;
                    return null;
                }

            default:
                try {
                    retailTradeList = retailTradeMapper.findAll();
                    if (retailTradeList != null) {
                        for (int i = 0; i < retailTradeList.size(); i++) {
                            retailTrade.setSql("where F_TradeID = %s");
                            retailTrade.setConditions(new String[]{String.valueOf(retailTradeList.get(i).getID())});
                            sql = String.format(retailTrade.getSql(), retailTrade.getConditions());
                            dataQuery = entityManager.createNativeQuery(RetailTradeCommodityPresenter.QUERY_RetailTradeCommodity_TABLE + sql, RetailTradeCommodity.class);
                            List<RetailTradeCommodity> retailTradeCommodityList = dataQuery.getResultList();
                            //
                            retailTradeList.get(i).setListSlave1(retailTradeCommodityList);
                            ((RetailTrade) retailTradeList.get(i)).setDatetimeStart(Constants.getDefaultSyncDatetime2());
                            ((RetailTrade) retailTradeList.get(i)).setDatetimeEnd(Constants.getDefaultSyncDatetime2());
                            retailTradeList.get(i).setSyncDatetime(Constants.getDefaultSyncDatetime2());
                            retailTradeList.get(i).setReturnObject(1);
                        }
                    }
                    lastErrorCode = ErrorInfo.EnumErrorCode.EC_NoError;
                    return retailTradeList;
                } catch (Exception e) {
                    e.printStackTrace();
                    log.error(this.getClass().getName() + ".retrieveNSync()发生异常：" + e.getMessage());
                    lastErrorCode = ErrorInfo.EnumErrorCode.EC_OtherError;
                    return null;
                }
        }
    }

    @Override
    protected BaseModel deleteSync(int iUseCaseID, BaseModel bm) {
        log.info("正在进行RetailTradePresenter的deleteSync，bm=" + (bm == null ? null : bm.toString()));
        globalWriteLock.writeLock().lock();
        try {
            retailTradeMapper.delete(bm.getID());
            //
            bm.setSql("where F_TradeID = %s");
            bm.setConditions(new String[]{String.valueOf(bm.getID())});
            String sql = String.format(bm.getSql(), bm.getConditions());
            Query dataQuery = entityManager.createNativeQuery(RetailTradeCommodityPresenter.QUERY_RetailTradeCommodity_TABLE + sql, RetailTradeCommodity.class);
            List<RetailTradeCommodity> retailTradeCommodityList = dataQuery.getResultList();
            for (int i = 0; i < retailTradeCommodityList.size(); i++) {
                retailTradeCommodityMapper.delete(retailTradeCommodityList.get(i).getID());
            }
            //
            lastErrorCode = ErrorInfo.EnumErrorCode.EC_NoError;
        } catch (Exception e) {
            log.error("RetailTradePresenter.deleteSync发生异常：" + e.getMessage());
            e.printStackTrace();
            lastErrorCode = ErrorInfo.EnumErrorCode.EC_OtherError;
        }
        globalWriteLock.writeLock().unlock();
        return null;
    }

    @Override
    protected boolean createMasterSlaveAsync(final int iUseCaseID, final BaseModel bmMaster, final BaseSQLiteEvent event) {
        log.info("正在进行RetailTradePresenter的createMasterSlaveAsync，bmMaster=" + (bmMaster == null ? null : bmMaster));

        event.setStatus(BaseEvent.EnumEventStatus.EES_SQLite_ToDo);
        switch (event.getEventTypeSQLite()) {
            case ESET_RetailTrade_CreateMasterSlaveAsync_Done:
                log.info("准备向本地SQLite插入RetailTrade主从表数据....");

                (new Thread() {
                    @Override
                    public void run() {
//                        event.setEventTypeSQLite(BaseSQLiteEvent.EnumSQLiteEventType.ESET_RetailTrade_CreateMasterSlaveAsync_Done);
                        globalWriteLock.writeLock().lock();
                        try {
                            RetailTrade bm = (RetailTrade) bmMaster;
                            bm.setSyncType(BasePresenter.SYNC_Type_C);
                            bm.setSyncDatetime(Constants.getDefaultSyncDatetime2());
                            bm.setDatetimeStart(new Date(System.currentTimeMillis() + NtpHttpBO.TimeDifference));
                            bm.setDatetimeEnd(new Date(System.currentTimeMillis() + NtpHttpBO.TimeDifference));
                            RetailTrade retailTrade = (RetailTrade) createSync(BaseSQLiteBO.INVALID_CASE_ID, bm);
                            //
                            event.setLastErrorCode(lastErrorCode);
                            event.setBaseModel1(retailTrade);
                            event.setTmpMasterTableObj(retailTrade);
                        } catch (Exception e) {
                            e.printStackTrace();
                            event.setLastErrorCode(ErrorInfo.EnumErrorCode.EC_OtherError);

                            log.info("向本地SQLite插入主从表数据失败！原因：" + e.getMessage());
                        }
                        //
                        switch (iUseCaseID) {
                            case BaseSQLiteBO.CASE_RetailTrade_CreateMasterSlaveSQLite:
                                //...一般情况下，不在Presenter和event中设置EventTypeSQLite。但是这个是因为只能在Presenter改变EventTypeSQLite，所以只能这么做
                                event.setEventTypeSQLite(BaseSQLiteEvent.EnumSQLiteEventType.ESET_RetailTrade_RefreshMasterSlaveAsyncSQLite_Done);
                                break;
                            default:
                                break;
                        }
                        globalWriteLock.writeLock().unlock();
                        EventBus.getDefault().post(event);
                    }
                }).start();
                break;
            default:
                throw new RuntimeException("未定义的事件！");
        }
        return true;
    }

    @Override
    protected boolean createReplacerNAsync(final int iUseCaseID, final List<?> bmOldList, final List<?> bmNewList, final BaseSQLiteEvent event) {
        log.info("正在进行RetailTradePresenter的createReplacerNAsync，bmOldList=" + (bmOldList == null ? null : bmOldList.toString()) //
                + "，bmNewList=" + (bmNewList == null ? null : bmNewList.toString()));

        event.setStatus(BaseEvent.EnumEventStatus.EES_SQLite_ToApplyServerData);
        switch (iUseCaseID) {
            default:
                (new Thread() {
                    @Override
                    public void run() {
                        globalWriteLock.writeLock().lock();
                        log.info("已经得到服务器返回的主表和从表数据，准备删除本地的旧数据，并插入服务器的数据...");//...
                        log.info("旧的零售单为：" + bmOldList);
                        log.info("服务器返回的零售单为：" + bmNewList);
                        try {
                            if (bmNewList != null) {
                                for (int i = 0; i < bmNewList.size(); i++) {
                                    RetailTrade retailTradeNew = (RetailTrade) bmNewList.get(i);
                                    if (bmOldList != null) {
                                        for (int j = 0; j < bmOldList.size(); j++) {
                                            RetailTrade retailTradeOld = (RetailTrade) bmOldList.get(j);
                                            if (retailTradeNew.getLocalSN() == retailTradeOld.getLocalSN() && retailTradeNew.getPos_ID() == retailTradeOld.getPos_ID() && DatetimeUtil.compareDate(retailTradeNew.getSaleDatetime(), retailTradeOld.getSaleDatetime())) {   //POS_SN和POS_ID,SaleDatetime组成的唯一键用于识别是否同一张单
                                                log.info("当前准备删除的临时数据为：" + retailTradeOld);
                                                // 删除零售单商品旧数据
                                                for (int k = 0; k < retailTradeOld.getListSlave1().size(); k++) {
                                                    if(retailTradeCommodityMapper.findOne(((RetailTradeCommodity) (retailTradeOld.getListSlave1()).get(k)).getID()) != null) {
                                                        retailTradeCommodityMapper.delete(((RetailTradeCommodity) (retailTradeOld.getListSlave1()).get(k)).getID());
                                                    }
                                                }
                                                // 删除零售单促销表旧数据,如果有
                                                if (retailTradeOld.getListSlave2() != null) {
                                                    for (int l = 0; l < retailTradeOld.getListSlave2().size(); l++) {
                                                        RetailTradePromoting retailTradePromoting = (RetailTradePromoting) retailTradeOld.getListSlave2().get(0);
                                                        log.info("当前准备删除的临时零售单促销数据为：" + retailTradePromoting);
                                                        for (int n = 0; n < retailTradePromoting.getListSlave1().size(); n++) {
                                                            if(retailTradePromotingFlowMapper.findOne(((RetailTradePromotingFlow) retailTradePromoting.getListSlave1().get(n)).getID()) != null) {
                                                                retailTradePromotingFlowMapper.delete(((RetailTradePromotingFlow) retailTradePromoting.getListSlave1().get(n)).getID());
                                                            }
                                                        }
                                                        if(retailTradePromotingMapper.findOne(retailTradePromoting.getID()) != null) {
                                                            retailTradePromotingMapper.delete(retailTradePromoting.getID());
                                                        }
                                                        log.info("删除本地的零售单促销表旧数据成功！PromotingID=" + retailTradePromoting.getID() + "  "
                                                                + (retailTradePromoting.getListSlave1().size() > 0 ? ((RetailTradePromotingFlow) retailTradePromoting.getListSlave1().get(0)).getID() + "~" //打印第1个ID
                                                                + ((RetailTradePromotingFlow) retailTradePromoting.getListSlave1().get((retailTradePromoting).getListSlave1().size() - 1)).getID() : "")//打印最后1个ID
                                                        );
                                                    }
                                                } else {
                                                    log.info("此零售单无零售单促销表！");
                                                }
                                                if(retailTradeMapper.findOne(retailTradeOld.getID()) != null) {
                                                    retailTradeMapper.delete(retailTradeOld.getID());
                                                }
                                                log.info("删除本地的零售单旧数据成功！TradeID=" + retailTradeOld.getID() + "  "
                                                        + (retailTradeOld.getListSlave1().size() > 0 ? ((RetailTradeCommodity) retailTradeOld.getListSlave1().get(0)).getID() + "~" //打印第1个ID
                                                        + ((RetailTradeCommodity) retailTradeOld.getListSlave1().get((retailTradeOld).getListSlave1().size() - 1)).getID() : "")//打印最后1个ID
                                                );
                                                //插入新零售单主从表数据
                                                log.info("当前准备插入的数据为：" + retailTradeNew);
                                                if (retailTradeNew.getSyncDatetime() == null) { //... 为null的情况有nbr响应POS时没设置,或者是解析同步时间失败
                                                    log.error("已同步过的零售单=" + retailTradeNew + "因为SyncDatetime为空，所以会下次继续上传");
                                                    retailTradeNew.setSyncDatetime(Constants.getDefaultSyncDatetime2());
                                                }
                                                retailTradeNew.setSyncType(BasePresenter.SYNC_Type_C);
                                                createSync(BaseSQLiteBO.INVALID_CASE_ID, retailTradeNew);
                                                //插入零售单促销的新数据
                                                if (retailTradeNew.getListSlave2() != null) {
                                                    for (int l = 0; l < retailTradeNew.getListSlave2().size(); l++) {
                                                        RetailTradePromoting retailTradePromoting = (RetailTradePromoting) retailTradeNew.getListSlave2().get(l);
                                                        retailTradePromoting.setSyncType(BasePresenter.SYNC_Type_C);
                                                        retailTradePromotingPresenter.createSync(BaseSQLiteBO.INVALID_CASE_ID, retailTradePromoting);
                                                    }
                                                }
                                                bmOldList.remove(j);
                                                break;
                                            }
                                        }
                                    }
                                }
                            }
                            event.setLastErrorCode(ErrorInfo.EnumErrorCode.EC_NoError);
                            event.setListMasterTable(bmNewList);
                        } catch (Exception e) {
                            log.info("createReplacerNAsync()异常：" + e.getMessage());
                            e.printStackTrace();
                            event.setLastErrorCode(ErrorInfo.EnumErrorCode.EC_OtherError);
                        }
                        //
                        globalWriteLock.writeLock().unlock();
                        EventBus.getDefault().post(event);
                    }
                }).start();
        }
        return true;
    }

    @Override
    protected boolean createReplacerAsync(final int iUseCaseID, final BaseModel bmOld, final BaseModel bmNew, final BaseSQLiteEvent event) {
        log.info("正在进行RetailTradePresenter的createReplacerAsync，bmOld=" + (bmOld == null ? null : bmOld.toString())//
                + "，bmNew=" + (bmNew == null ? null : bmNew.toString()));

        event.setStatus(BaseEvent.EnumEventStatus.EES_SQLite_ToApplyServerData);

        switch (iUseCaseID) {
            default:
                (new Thread() {
                    @Override
                    public void run() {
                        globalWriteLock.writeLock().lock();
                        log.info("已经得到服务器返回的主表和从表数据，准备删除本地的旧数据，并插入服务器的数据...");//...

                        try {
                            for (int i = 0; i < ((RetailTrade) bmOld).getListSlave1().size(); i++) {
                                if(retailTradeCommodityMapper.findOne(((RetailTradeCommodity) bmOld.getListSlave1().get(i)).getID()) != null) {
                                    retailTradeCommodityMapper.delete(((RetailTradeCommodity) bmOld.getListSlave1().get(i)).getID());
                                }
                            }
                            if(retailTradeMapper.findOne(bmOld.getID()) != null) {
                                retailTradeMapper.delete(bmOld.getID());
                            }
                            log.info("删除本地的旧数据成功！TradeID=" + bmOld.getID() + "  "
                                    + (((RetailTrade) bmOld).getListSlave1().size() > 0 ? ((RetailTradeCommodity) bmOld.getListSlave1().get(0)).getID() + "~" //打印第1个ID
                                    + ((RetailTradeCommodity) bmOld.getListSlave1().get(((RetailTrade) bmOld).getListSlave1().size() - 1)).getID() : "")//打印最后1个ID
                            );
                            //
                            RetailTrade retailTradeNew = (RetailTrade) bmNew;
                            if (retailTradeNew.getSyncDatetime() == null) {
                                retailTradeNew.setSyncDatetime(Constants.getDefaultSyncDatetime2());
                            }
                            retailTradeNew.setSyncType(BasePresenter.SYNC_Type_C);
                            //注释原因：以往int2存放的是临时零售单ID，如今临时ID是会放在POS_SN中
//                            retailTradeNew.setInt2(NULL);//在开始的时候使用了int2临时保存RetailTradePromoting的临时ID，在上面已经将服务器返回的RetailTrade setBaseModel1，所以此处可以将int2设为NULL。
                            RetailTrade retailTradeNewCreate = (RetailTrade) createSync(BaseSQLiteBO.INVALID_CASE_ID, retailTradeNew);
                            //
                            //如果是退货类型的零售单，则需要更新下源单的可退货数量
                            if (retailTradeNewCreate.getSourceID() > 0) {
                                if (!updateNOCanReturn(retailTradeNewCreate)) {
                                    event.setLastErrorCode(ErrorInfo.EnumErrorCode.EC_OtherError);
                                    log.error("上传退货单成功后，SQLite更新源单商品的可退货数量失败");
                                    EventBus.getDefault().post(event);
                                }
                            }
                            //
                            event.setLastErrorCode(ErrorInfo.EnumErrorCode.EC_NoError);
                            event.setBaseModel1(retailTradeNewCreate);
                        } catch (Exception e) {
                            log.error("createReplacerAsync()异常：" + e.getMessage());
                            e.printStackTrace();
                            event.setLastErrorCode(ErrorInfo.EnumErrorCode.EC_OtherError);
                        }
//                        event.setStatus(BaseEvent.EnumEventStatus.EES_SQLite_DoneApplyServerData);
//                        event.setEventTypeSQLite(BaseSQLiteEvent.EnumSQLiteEventType.ESET_RetailTrade_CreateReplacerAsync_Done);
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
    protected boolean refreshByServerDataAsync(final int iUseCaseID, final List<?> bmNewList, final BaseSQLiteEvent event) {
        event.setStatus(BaseEvent.EnumEventStatus.EES_SQLite_ToDo);
        switch (iUseCaseID) {
            default:
                (new Thread() {
                    @Override
                    public void run() {
                        try {
                            log.info("查询后服务器返回的零售单，准备进行插入数据库！");
//                            event.setEventTypeSQLite(BaseSQLiteEvent.EnumSQLiteEventType.ESET_RetailTrade_updateNAsync);
                            //如果查询零售单返回数据，则把本地对应ID的零售单删除，然后再插入服务器返回的零售单
                            globalWriteLock.writeLock().lock();
                            if (bmNewList != null) {
                                if (bmNewList.size() > 0) {
                                    List<RetailTrade> retailTradeList = (List<RetailTrade>) bmNewList;
                                    //删除
                                    for (int i = 0; i < retailTradeList.size(); i++) {
                                        RetailTrade retailTrade = (RetailTrade) retailTradeMapper.findOne(retailTradeList.get(i).getID());
                                        RetailTrade rt = retailTradeList.get(i);
                                        if (retailTrade != null) {
                                            if (rt.getListSlave1() != null && rt.getListSlave1().size() > 0) {
                                                for (int j = 0; j < rt.getListSlave1().size(); j++) {
                                                    RetailTradeCommodity rtc = (RetailTradeCommodity) rt.getListSlave1().get(j);
                                                    RetailTradeCommodity retailTradeCommodity = retailTradeCommodityMapper.findOne(rtc.getID());
                                                    if (retailTradeCommodity != null) {
                                                        retailTradeCommodityMapper.delete(rtc.getID());
                                                    }
                                                }
                                            }
                                            retailTradeMapper.delete(retailTrade.getID());
                                        }
                                    }

//                                    List<RetailTradeCommodity> retailTradeCommodityList = retailTradeCommodityMapper.findAll();

                                    //插入
                                    for (int i = 0; i < retailTradeList.size(); i++) {
                                        RetailTrade rt = retailTradeList.get(i);
                                        createSync(BaseSQLiteBO.INVALID_CASE_ID, rt);
                                    }
                                }
                            }
                            event.setLastErrorCode(ErrorInfo.EnumErrorCode.EC_NoError);
                        } catch (Exception e) {
                            log.error("执行updateNAsync时出错，错误信息：" + e.getMessage());
                            e.printStackTrace();
                            event.setLastErrorCode(ErrorInfo.EnumErrorCode.EC_OtherError);
                        }
                        event.setListMasterTable(bmNewList);
                        globalWriteLock.writeLock().unlock();
//                        event.setString1("RN");
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
        log.info("正在进行RetailTradePresenter的createAsync，bm=" + (bm == null ? null : bm.toString()));

        switch (iUseCaseID) {
            default:
                (new Thread() {
                    @Override
                    public void run() {
                        globalWriteLock.writeLock().lock();
                        RetailTrade retailTrade = (RetailTrade) bm;
                        try {
                            event.setLastErrorCode(ErrorInfo.EnumErrorCode.EC_NoError);

                            retailTrade.setSyncDatetime(Constants.getDefaultSyncDatetime2());
                            retailTrade.setSyncType(BasePresenter.SYNC_Type_C);
                            retailTrade.setDatetimeStart(new Date(System.currentTimeMillis() + NtpHttpBO.TimeDifference));
                            retailTrade.setDatetimeEnd(new Date(System.currentTimeMillis() + NtpHttpBO.TimeDifference));
                            retailTrade.setSaleDatetime(new Date(System.currentTimeMillis() + NtpHttpBO.TimeDifference));


                            List<RetailTradeCommodity> retailTradeCommodities = (List<RetailTradeCommodity>) retailTrade.getListSlave1();
                            for (int i = 0; i < retailTradeCommodities.size(); i++) {
                                retailTradeCommodities.get(i).setSyncDatetime(Constants.getDefaultSyncDatetime2());
                                retailTradeCommodities.get(i).setSyncType(BasePresenter.SYNC_Type_C);
//                                retailTradeCommodities.get(i).setTradeID((long) retailTrade.getID());
                            }

                            retailTrade = (RetailTrade) createSync(BaseSQLiteBO.INVALID_CASE_ID, retailTrade);
                            event.setLastErrorCode(lastErrorCode);
                        } catch (Exception e) {
//                            Log.e("输出：", "创建RetailTrade时出错，错误信息：" + e.getMessage());
                            log.info("创建RetailTrade时出错，错误信息：" + e.getMessage());
                            e.printStackTrace();
                            event.setLastErrorCode(ErrorInfo.EnumErrorCode.EC_OtherError);
                        }
                        //
                        event.setBaseModel1(retailTrade);
                        // 释放锁要在postEvent前，postEvent可能要继续访问数据库
                        globalWriteLock.writeLock().unlock();
                        EventBus.getDefault().post(event);
                    }
                }).start();
                break;
        }

        return true;
    }

//    @Override
//    protected boolean createOrReplaceNAsync(int iUseCaseID, final List<?> list, final BaseSQLiteEvent event) {
//        switch (iUseCaseID) {
//            default:
//                (new Thread() {
//                    @Override
//                    public void run() {
////                        event.setEventTypeSQLite(BaseSQLiteEvent.EnumSQLiteEventType.ESET_RetailTrade_CreateNAsync);
//                        //
//                        try {
//                            for (int i = 0; i < list.size(); i++) {
//                                ((RetailTrade) list.get(i)).setSyncDatetime(new Date(System.currentTimeMillis() + NtpHttpBO.TimeDifference));
//                                long id = retailTradeMapper.createOrReplace((RetailTrade) list.get(i));
//                                ((RetailTrade) list.get(i)).setID(id);
//                            }
//                            event.setLastErrorCode(ErrorInfo.EnumErrorCode.EC_NoError);
//                        } catch (Exception e) {
//                            log.info("创建RetailTrade时出错，错误信息：" + e.getMessage());
//                            event.setLastErrorCode(ErrorInfo.EnumErrorCode.EC_OtherError);
//                        }
//                        //
//                        event.setListMasterTable(list);
//                        //
//                        EventBus.getDefault().post(event);
//                    }
//                });
//                break;
//        }
//
//        return true;
//    }

    @Override
    protected boolean createNAsync(int iUseCaseID, final List<?> list, final BaseSQLiteEvent event) {
        log.info("正在进行RetailTradePresenter的createNAsync，list=" + (list == null ? null : list.toString()));

        switch (iUseCaseID) {
            default:
                (new Thread() {
                    @Override
                    public void run() {
//                        event.setEventTypeSQLite(BaseSQLiteEvent.EnumSQLiteEventType.ESET_RetailTrade_CreateNAsync);
                        //
                        globalWriteLock.writeLock().lock();
                        try {
                            for (int i = 0; i < list.size(); i++) {
                                ((RetailTrade) list.get(i)).setSyncDatetime(new Date(System.currentTimeMillis() + NtpHttpBO.TimeDifference));
                                ((RetailTrade) list.get(i)).setSyncType(BasePresenter.SYNC_Type_C);

                                RetailTrade retailTrade = (RetailTrade) createSync(BaseSQLiteBO.INVALID_CASE_ID, (BaseModel) list.get(i));
                                ((RetailTrade) list.get(i)).setID(retailTrade.getID());
                            }
                            event.setLastErrorCode(ErrorInfo.EnumErrorCode.EC_NoError);
                        } catch (Exception e) {
                            log.error("创建RetailTrade时出错，错误信息：" + e.getMessage());
                            event.setLastErrorCode(ErrorInfo.EnumErrorCode.EC_OtherError);
                            log.error("创建RetailTrade时出错2，错误信息：" + event.getLastErrorCode());
                        }
                        //
                        event.setListMasterTable(list);
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
        log.info("正在进行RetailTradePresenter的updateAsync，bm=" + (bm == null ? null : bm.toString()));

        switch (iUseCaseID) {
            default:
                (new Thread() {
                    @Override
                    public void run() {
                        //
                        globalWriteLock.writeLock().lock();
                        try {
                            bm.setSyncType(BasePresenter.SYNC_Type_C); // ...ZJH需要C型数据上传
                            retailTradeMapper.save((RetailTrade) bm);
                            event.setLastErrorCode(ErrorInfo.EnumErrorCode.EC_NoError);
//                          event.setStatus(BaseEvent.EnumEventStatus.EES_SQLite_Done);
                        } catch (Exception e) {
                            log.error("创建RetailTrade时出错，错误信息：" + e.getMessage());
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
        log.info("正在进行RetailTradePresenter的retrieveNAsync，bm=" + (bm == null ? null : bm.toString()));

        switch (iUseCaseID) {
            case BaseSQLiteBO.CASE_RetailTrade_RetrieveNToUpload:
                //...
                (new Thread() {
                    @Override
                    public void run() {
                        // 重置基础资料时，有时会出现“database is locked”异常（复现率低），sqlite不能同时读写，所以同步上传零售单要先获取锁
                        globalWriteLock.writeLock().lock();
                        System.out.println("RetailTradePresenter retrieveNAsync正在加锁...." + globalWriteLock.writeLock().getHoldCount());
                        List<RetailTrade> result = null;
                        //
                        try {
                            BaseModel bm = new BaseModel();
                            bm.setSql("where F_SyncDatetime = %s and F_SlaveCreated = %s");
                            bm.setConditions(new String[]{"0", String.valueOf(BaseModel.EnumBoolean.EB_Yes.getIndex())});
                            String sql = String.format(bm.getSql(), bm.getConditions());
                            Query dataQuery = entityManager.createNativeQuery(QUERY_RetailTrade_TABLE + sql, RetailTrade.class);
                            result = dataQuery.getResultList();
                            if (result != null && result.size() > 0) {
                                for (int j = 0; j < result.size(); j++) {
                                    if (result.get(j).getSourceID() > 0 && result.get(j).getAmountWeChat() > 0 && (StringUtils.isEmpty(result.get(j).getWxRefundNO()) && StringUtils.isEmpty(result.get(j).getWxRefundSubMchID()))) {
                                        result.remove(j);
                                    }
                                }
                                log.info("查询到本地SQLite有临时零售单！");
                                for (int i = 0; i < result.size(); i++) {
                                    //查询零售单商品
                                    bm.setSql("where F_TradeID = %s");
                                    bm.setConditions(new String[]{String.valueOf(result.get(i).getID())});
                                    sql = String.format(bm.getSql(), bm.getConditions());
                                    dataQuery = entityManager.createNativeQuery(RetailTradeCommodityPresenter.QUERY_RetailTradeCommodity_TABLE + sql, RetailTradeCommodity.class);
                                    List<RetailTradeCommodity> retailTradeCommodityList = dataQuery.getResultList();
                                    result.get(i).setListSlave1(retailTradeCommodityList);
                                    //查询零售单促销和从表
                                    dataQuery = entityManager.createNativeQuery(RetailTradePromotingPresenter.QUERY_RetailTradePromoting_TABLE + sql, RetailTradePromoting.class);
                                    List<RetailTradePromoting> retailTradePromotingList = dataQuery.getResultList();
                                    if (retailTradePromotingList != null || retailTradePromotingList.size() == 0) {
                                        for (RetailTradePromoting retailTradePromoting : retailTradePromotingList) {
                                            retailTradePromoting.setSql("where F_RetailTradePromotingID = %s");
                                            retailTradePromoting.setConditions(new String[]{String.valueOf(retailTradePromoting.getID())});
                                            sql = String.format(retailTradePromoting.getSql(), retailTradePromoting.getConditions());
                                            dataQuery = entityManager.createNativeQuery(RetailTradePromotingFlowPresenter.QUERY_RetailTradePromotingFlow_TABLE + sql, RetailTradePromotingFlow.class);
                                            List<RetailTradePromotingFlow> retailTradePromotingFlowList = dataQuery.getResultList();
                                            if (retailTradePromotingFlowList != null && retailTradePromotingFlowList.size() > 0) {
                                                retailTradePromoting.setListSlave1(retailTradePromotingFlowList);
                                            } else {
                                                log.error("没有找到零售单localSN=" + result.get(i).getLocalSN() + "的零售单促销过程表");
                                                //...是否要return
                                            }
                                        }
                                    }
                                    result.get(i).setListSlave2(retailTradePromotingList);
                                    //
                                    //查询零售单优惠券使用表
                                    RetailTradeCoupon retailTradeCoupon = new RetailTradeCoupon();
                                    retailTradeCoupon.setSql("where F_RetailTradeID = %s");
                                    retailTradeCoupon.setConditions(new String[]{String.valueOf(result.get(i).getID())});
                                    String retailTradeCouponSql = String.format(retailTradeCoupon.getSql(), retailTradeCoupon.getConditions());
                                    Query retailTradeCouponDataQuery = entityManager.createNativeQuery(RetailTradeCouponPresenter.QUERY_RetailTradeCoupon_TABLE + retailTradeCouponSql, RetailTradeCoupon.class);
                                    List<RetailTradeCoupon> retailTradeCouponList = retailTradeCouponDataQuery.getResultList();
                                    //
                                    result.get(i).setListSlave3(retailTradeCouponList);

                                    result.get(i).setDatetimeStart(Constants.getDefaultSyncDatetime2());
                                    result.get(i).setDatetimeEnd(Constants.getDefaultSyncDatetime2());
                                    result.get(i).setSyncDatetime(Constants.getDefaultSyncDatetime2());
                                    result.get(i).setReturnObject(1);
                                }
                            }
                            event.setLastErrorCode(ErrorInfo.EnumErrorCode.EC_NoError);
                        } catch (Exception e) {
                            log.error("RetrieveNAsync RetailTrade出错，错误信息：" + e.getMessage());
                            event.setLastErrorCode(ErrorInfo.EnumErrorCode.EC_OtherError);
                        }
                        //
                        event.setListMasterTable(result);
                        //
                        globalWriteLock.writeLock().unlock();
                        System.out.println("RetailTradePresenter retrieveNAsync已经解锁" + globalWriteLock.writeLock().getHoldCount());
                        EventBus.getDefault().post(event);
                    }
                }).start();
                break;
            case BaseSQLiteBO.CASE_RetailTrade_RetrieveNByConditions:
                (new Thread() {
                    @Override
                    public void run() {
                        List<RetailTrade> result = null;
                        try {
                            String sql = String.format(bm.getSql(), bm.getConditions());
                            Query dataQuery = entityManager.createNativeQuery(QUERY_RetailTrade_TABLE + sql, RetailTrade.class);
                            result = dataQuery.getResultList();
                            if (result != null) {
                                for (int j = 0; j < result.size(); j++) {
                                    if (result.get(j).getSourceID() > 0 && result.get(j).getAmountWeChat() > 0 && (StringUtils.isEmpty(result.get(j).getWxRefundNO()) && StringUtils.isEmpty(result.get(j).getWxRefundSubMchID()))) {
                                        result.remove(j);
                                    }
                                }
                                for (int i = 0; i < result.size(); i++) {
                                    bm.setSql("where F_TradeID = %s");
                                    bm.setConditions(new String[]{String.valueOf(result.get(i).getID())});
                                    sql = String.format(bm.getSql(), bm.getConditions());
                                    dataQuery = entityManager.createNativeQuery(RetailTradeCommodityPresenter.QUERY_RetailTradeCommodity_TABLE + sql, RetailTradeCommodity.class);
                                    List<RetailTradeCommodity> retailTradeCommodityList = dataQuery.getResultList();
                                    result.get(i).setListSlave1(retailTradeCommodityList);
                                    result.get(i).setDatetimeStart(Constants.getDefaultSyncDatetime2());
                                    result.get(i).setDatetimeEnd(Constants.getDefaultSyncDatetime2());
                                    result.get(i).setSyncDatetime(Constants.getDefaultSyncDatetime2());
                                    result.get(i).setReturnObject(1);
                                }
                            }
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
                }).start();
                break;
            default:
                (new Thread() {
                    @Override
                    public void run() {
                        List<RetailTrade> result = null;
                        //
                        try {
                            Thread.sleep(1); //令event.setID()的ID惟一
                            result = retailTradeMapper.findAll();//...
                            RetailTrade bm = new RetailTrade();
                            if (result != null) {
                                for (int j = 0; j < result.size(); j++) {
                                    if (((RetailTrade) result.get(j)).getSourceID() > 0 && ((RetailTrade) result.get(j)).getAmountWeChat() > 0 && (StringUtils.isEmpty(((RetailTrade) result.get(j)).getWxRefundNO()) && StringUtils.isEmpty(((RetailTrade) result.get(j)).getWxRefundSubMchID()))) {
                                        result.remove(j);
                                    }
                                }
                                log.info("查询到本地SQLite有零售单！");
                                for (int i = 0; i < result.size(); i++) {
                                    bm.setSql("where F_TradeID = %s");
                                    bm.setConditions(new String[]{String.valueOf(result.get(i).getID())});
                                    String sql = String.format(bm.getSql(), bm.getConditions());
                                    Query dataQuery = entityManager.createNativeQuery(QUERY_RetailTrade_TABLE + sql, RetailTrade.class);
                                    List<RetailTradeCommodity> retailTradeCommodityList = dataQuery.getResultList();
                                    result.get(i).setListSlave1(retailTradeCommodityList);
                                    ((RetailTrade) result.get(i)).setDatetimeStart(Constants.getDefaultSyncDatetime2());
                                    ((RetailTrade) result.get(i)).setDatetimeEnd(Constants.getDefaultSyncDatetime2());
                                    result.get(i).setSyncDatetime(Constants.getDefaultSyncDatetime2());
                                    result.get(i).setReturnObject(1);
                                }
                            }
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

    @Override
    protected boolean deleteAsync(int iUseCaseID, final BaseModel bm, final BaseSQLiteEvent event) {
        log.info("正在进行RetailTradePresenter的deleteAsync，bm=" + (bm == null ? null : bm.toString()));

        switch (iUseCaseID) {
            default:
                (new Thread() {
                    @Override
                    public void run() {
//                        event.setEventTypeSQLite(BaseSQLiteEvent.EnumSQLiteEventType.ESET_RetailTrade_DeleteAsync);
                        //
                        globalWriteLock.writeLock().lock();
                        try {
                            retailTradeMapper.delete(bm.getID());
                            event.setLastErrorCode(ErrorInfo.EnumErrorCode.EC_NoError);
                        } catch (Exception e) {
                            log.error("删除RetailTrade时出错，错误信息：" + e.getMessage());
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
        log.info("正在进行RetailTradePresenter的retrieve1Async，bm=" + (bm == null ? null : bm.toString()));

        switch (iUseCaseID) {
            default:
                (new Thread() {
                    @Override
                    public void run() {
                        RetailTrade result = null;
//                        event.setEventTypeSQLite(BaseSQLiteEvent.EnumSQLiteEventType.ESET_RetailTrade_Retrieve1Async);
                        //
                        try {
                            result = (RetailTrade) retailTradeMapper.findOne(bm.getID());//...
                            if (result != null) {
                                bm.setSql("where F_TradeID = %s");
                                bm.setConditions(new String[]{String.valueOf(result.getID())});
                                String sql = String.format(bm.getSql(), bm.getConditions());
                                Query dataQuery = entityManager.createNativeQuery(RetailTradeCommodityPresenter.QUERY_RetailTradeCommodity_TABLE + sql, RetailTradeCommodity.class);
                                List<RetailTradeCommodity> rtcList = dataQuery.getResultList();
                                result.setListSlave1(rtcList);
                            }

                            event.setLastErrorCode(ErrorInfo.EnumErrorCode.EC_NoError);
                        } catch (Exception e) {
                            log.error("Retrieve1 RetailTrade时出错，错误信息：" + e.getMessage());
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
    protected boolean refreshByServerDataAsyncC(final int iUseCaseID, final List<?> bmNewList, final BaseSQLiteEvent event) {
        log.info("正在进行RetailTradePresenter的refreshByServerDataAsyncC，bmNewList=" + (bmNewList == null ? null : bmNewList.toString()));

        event.setStatus(BaseEvent.EnumEventStatus.EES_SQLite_ToApplyServerData);
        switch (iUseCaseID) {
            default:
                (new Thread() {
                    @Override
                    public void run() {
                        globalWriteLock.writeLock().lock();
                        try {
                            log.info("已经得到服务器返回的需要同步的RetailTrade数据, 准备进行同步...");

//                        event.setEventTypeSQLite(BaseSQLiteEvent.EnumSQLiteEventType.ESET_RetailTrade_RefreshByServerDataAsync_Done);

                            //删除本地所有的数据
                            retailTradeCommodityMapper.deleteAll();
                            retailTradeMapper.deleteAll();
                            //同步所有服务器返回的数据
                            if (bmNewList != null) {
                                for (int i = 0; i < bmNewList.size(); i++) {
                                    createSync(BaseSQLiteBO.INVALID_CASE_ID, (BaseModel) bmNewList.get(i));
                                }
                            } else {
                                log.info("服务器没有retailTrade返回");
                            }
                            event.setLastErrorCode(ErrorInfo.EnumErrorCode.EC_NoError);
                        } catch (Exception e) {
                            event.setLastErrorCode(ErrorInfo.EnumErrorCode.EC_OtherError);
                            log.error("RetailTrade refreshByServerDataAsyncC出错，错误信息为：" + e.getMessage());
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
    protected boolean updateNAsync(final int iUseCaseID, final List<?> bmList, final BaseSQLiteEvent event) {
        log.info("正在进行RetailTradePresenter的updateNAsync，bmList=" + (bmList == null ? null : bmList.toString()));

        event.setStatus(BaseEvent.EnumEventStatus.EES_SQLite_ToDo);
        switch (iUseCaseID) {
            default:
                (new Thread() {
                    @Override
                    public void run() {
                        try {
                            globalWriteLock.writeLock().lock();
                            log.info("查询后服务器返回的零售单，准备进行插入数据库！");
//                            event.setEventTypeSQLite(BaseSQLiteEvent.EnumSQLiteEventType.ESET_RetailTrade_updateNAsync);
                            //如果查询零售单返回数据，则把本地对应ID的零售单删除，然后再插入服务器返回的零售单
                            if (bmList != null) {
                                if (bmList.size() > 0) {
                                    List<RetailTrade> retailTradeList = (List<RetailTrade>) bmList;
                                    //删除
                                    for (int i = 0; i < retailTradeList.size(); i++) {
                                        RetailTrade retailTrade = (RetailTrade) retailTradeMapper.findOne(retailTradeList.get(i).getID());
                                        RetailTrade rt = retailTradeList.get(i);
                                        if (retailTrade != null) {
                                            if (rt.getListSlave1() != null && rt.getListSlave1().size() > 0) {
                                                for (int j = 0; j < rt.getListSlave1().size(); j++) {
                                                    RetailTradeCommodity rtc = (RetailTradeCommodity) rt.getListSlave1().get(j);
                                                    retailTradeCommodityMapper.delete(rtc.getID());
                                                    if (retailTradeCommodityMapper.findOne(rtc.getID()) != null) {
                                                        new RuntimeException("删除失败");
                                                    }
                                                }
                                            }
                                            retailTradeMapper.delete(retailTrade.getID());
                                        }
                                    }
                                    //插入
                                    for (int i = 0; i < retailTradeList.size(); i++) {
                                        RetailTrade rt = retailTradeList.get(i);
                                        createSync(BaseSQLiteBO.INVALID_CASE_ID, rt);
                                    }
                                }
                            }
                            event.setLastErrorCode(ErrorInfo.EnumErrorCode.EC_NoError);
                        } catch (Exception e) {
                            log.error("执行updateNAsync时出错，错误信息：" + e.getMessage());
                            e.printStackTrace();
                            event.setLastErrorCode(ErrorInfo.EnumErrorCode.EC_OtherError);
                        }
                        event.setListMasterTable(bmList);
//                        event.setString1("RN");
                        //
                        EventBus.getDefault().post(event);
                        globalWriteLock.writeLock().unlock();
                    }
                }).start();
                break;
        }
        return true;
    }

    //上传时设置错误码
    public void setPresenterErrorCode() {
        lastErrorCode = ErrorInfo.EnumErrorCode.EC_NoError;
    }

    public void drop() {
        try {
            retailTradeMapper.dropTable();
            lastErrorCode = ErrorInfo.EnumErrorCode.EC_NoError;
        } catch (Exception e) {
            log.error("删除SQLite中的表RetailTrade时出错，错误信息：" + e.getMessage());
            lastErrorCode = ErrorInfo.EnumErrorCode.EC_OtherError;
        }
    }

    protected boolean updateNOCanReturn(RetailTrade retailTrade) {
        globalWriteLock.writeLock().lock();
        RetailTrade rt = new RetailTrade();
        rt.setID(retailTrade.getSourceID());
        rt = (RetailTrade) retrieve1Sync(BaseSQLiteBO.INVALID_CASE_ID, rt);
        if (lastErrorCode != ErrorInfo.EnumErrorCode.EC_NoError) {
            globalWriteLock.writeLock().unlock();
            return false;
        }
        //
        List<RetailTradeCommodity> sourceRTC = (List<RetailTradeCommodity>) rt.getListSlave1();
        List<RetailTradeCommodity> retailTradeCommodityList = (List<RetailTradeCommodity>) retailTrade.getListSlave1();
        for (int i = 0; i < sourceRTC.size(); i++) {
            RetailTradeCommodity rtc = sourceRTC.get(i);
            for (int j = 0; j < retailTradeCommodityList.size(); j++) {
                if (retailTradeCommodityList.get(j).getCommodityID() == rtc.getCommodityID()) {
                    rtc.setNOCanReturn(rtc.getNOCanReturn() - retailTradeCommodityList.get(j).getNO());
                    retailTradeCommodityMapper.save(rtc);
                    if (lastErrorCode != ErrorInfo.EnumErrorCode.EC_NoError) {
                        globalWriteLock.writeLock().unlock();
                        return false;
                    }
                    break;
                }
            }
        }
        globalWriteLock.writeLock().unlock();
        return true;
    }
}
