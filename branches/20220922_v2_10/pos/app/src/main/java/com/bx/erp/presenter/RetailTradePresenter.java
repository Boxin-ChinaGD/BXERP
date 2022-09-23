
package com.bx.erp.presenter;

import android.util.Log;

import com.bx.erp.bo.BaseSQLiteBO;
import com.bx.erp.bo.NtpHttpBO;
import com.bx.erp.common.GlobalController;
import com.bx.erp.di.PerActivity;
import com.bx.erp.event.BaseEvent;
import com.bx.erp.event.UI.BaseSQLiteEvent;
import com.bx.erp.helper.Constants;
import com.bx.erp.model.BaseModel;
import com.bx.erp.model.Commodity;
import com.bx.erp.model.DaoSession;
import com.bx.erp.model.ErrorInfo;
import com.bx.erp.model.RetailTrade;
import com.bx.erp.model.RetailTradeCommodity;
import com.bx.erp.model.RetailTradeCoupon;
import com.bx.erp.model.RetailTradePromoting;
import com.bx.erp.model.RetailTradePromotingFlow;
import com.bx.erp.utils.DatetimeUtil;
import com.bx.erp.utils.StringUtils;
import com.silencedut.taskscheduler.TaskScheduler;

import org.apache.log4j.Logger;
import org.greenrobot.eventbus.EventBus;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.inject.Inject;

@PerActivity
public class RetailTradePresenter extends BasePresenter {
    private static Logger log = Logger.getLogger(RetailTradePresenter.class);

    /**
     * 仅测试代码能调用。=true，代表当前工作在测试环境中。=false，代表是App在运行
     */
    public static boolean bInTestMode = false;

    @Inject
    public RetailTradePresenter(final DaoSession dao) {
        super(dao);
    }

    @Override
    protected String getTableName() {
        return dao.getRetailTradeDao().getTablename();
    }

    @Override
    protected List<BaseModel> createNSync(int iUseCaseID, final List<?> list) {
        log.info("正在进行RetailTradePresenter的createNSync，list=" + (list == null ? null : list.toString()));

        switch (iUseCaseID) {
            default:
                try {
                    for (int i = 0; i < list.size(); i++) {
                        ((RetailTrade) list.get(i)).setSyncDatetime(Constants.getDefaultSyncDatetime2());
                        ((RetailTrade) list.get(i)).setSyncType(BasePresenter.SYNC_Type_C);
                        long id = dao.getRetailTradeDao().insert((RetailTrade) list.get(i));
                        ((RetailTrade) list.get(i)).setID(id);
                        log.info("RetailTrade创建成功，ID=" + id);

                        if (((RetailTrade) list.get(i)).getListSlave1() != null) {
                            List<RetailTradeCommodity> rtcList = (List<RetailTradeCommodity>) ((RetailTrade) list.get(i)).getListSlave1();
                            for (int j = 0; j < rtcList.size(); j++) {
                                rtcList.get(j).setTradeID(id);
                                dao.getRetailTradeCommodityDao().insert(rtcList.get(j));
                            }
                        }

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
        log.info("正在进行RetailTradePresenter的createSync，bm=" + (bm == null ? null : bm.toString()));

        try {
            if (bm.getSyncDatetime() == null) {
                bm.setSyncDatetime(Constants.getDefaultSyncDatetime2());
            }
            bm.setSyncType(BasePresenter.SYNC_Type_C);
            long id = dao.getRetailTradeDao().insert((RetailTrade) bm);
            List<RetailTradeCommodity> rtcList = (List<RetailTradeCommodity>) bm.getListSlave1();
            List<RetailTradeCommodity> rtcListInSQLite = new ArrayList<RetailTradeCommodity>();
            if (rtcList != null) {
                for (int i = 0; i < rtcList.size(); i++) {
                    rtcList.get(i).setTradeID(id);
                    RetailTradeCommodity retailTradeCommodity = (RetailTradeCommodity) GlobalController.getInstance().getRetailTradeCommodityPresenter().createSync(iUseCaseID, rtcList.get(i));
                    if (lastErrorCode != ErrorInfo.EnumErrorCode.EC_NoError) {
                        return null;
                    }
                    rtcListInSQLite.add(retailTradeCommodity);

                    //更新本地的商品库存。如果是查单返回的零售单，不会跑到这里，所以不会错误地更新库存
                    Commodity commodityToR1 = new Commodity();
                    commodityToR1.setID((long) retailTradeCommodity.getCommodityID());
                    Commodity commodity = (Commodity) GlobalController.getInstance().getCommodityPresenter().retrieve1Sync(iUseCaseID, commodityToR1);
                    commodity.setNO(commodity.getNO() - retailTradeCommodity.getNO());
                    //
                    if (!GlobalController.getInstance().getCommodityPresenter().updateSync(iUseCaseID, commodity)) {
                        log.error("商品" + commodity.getName() + "[" + commodity.getID() + "]库存从" + (commodity.getNO() + retailTradeCommodity.getNO()) + "更新为" + commodity.getNO() + "失败");
                    }
                }
                bm.setListSlave1(rtcListInSQLite);
            }
            bm.setID(id);
            if (!bInTestMode) {
                ((RetailTrade) bm).setSlaveCreated(BaseModel.EnumBoolean.EB_Yes.getIndex());
                dao.getRetailTradeDao().update((RetailTrade) bm);
            }
            lastErrorCode = ErrorInfo.EnumErrorCode.EC_NoError;
        } catch (Exception e) {
            log.info("执行createSync失败，错误信息为" + e.getMessage());
            e.printStackTrace();
            lastErrorCode = ErrorInfo.EnumErrorCode.EC_OtherError;
        }

        return bm;
    }

    @Override
    protected BaseModel deleteNSync(int iUseCaseID, final BaseModel bm) {
        log.info("正在进行RetailTradePresenter的deleteNSync，bm=" + (bm == null ? null : bm.toString()));
        try {
            dao.getRetailTradeCommodityDao().deleteAll();
            dao.getRetailTradeDao().deleteAll();
            lastErrorCode = ErrorInfo.EnumErrorCode.EC_NoError;
        } catch (Exception e) {
            e.printStackTrace();
            lastErrorCode = ErrorInfo.EnumErrorCode.EC_OtherError;
        }
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
            dao.clear();
            BaseModel retailTrade = dao.getRetailTradeDao().load(bm.getID());

            if (retailTrade != null) {
                retailTrade.setSql("where F_TradeID = ?");
                retailTrade.setConditions(new String[]{String.valueOf(retailTrade.getID())});
                List<RetailTradeCommodity> baseModels = dao.getRetailTradeCommodityDao().queryRaw(retailTrade.getSql(), retailTrade.getConditions());
                retailTrade.setListSlave1(baseModels);
            }

            lastErrorCode = ErrorInfo.EnumErrorCode.EC_NoError;
            return retailTrade;
        } catch (Exception e) {
            log.info("执行retrieve1Sync失败，错误信息为" + e.getMessage());
            e.printStackTrace();
            lastErrorCode = ErrorInfo.EnumErrorCode.EC_OtherError;

            return null;
        }
    }

//    @RequiresApi(api = Build.VERSION_CODES.N)
//    public List<RetailTrade> retrieveNAsyncC(BaseHttpBO.CASE_RetailTrade_RetrieveNOldRetailTradeAsyncC, BaseModel bm) {
//        log.info("本地服务器当中的数据有：" + dao.getRetailTradeDao().loadAll());
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
                retailTrade.setSql("where F_SyncDatetime = ? and F_SlaveCreated = ?");
                retailTrade.setConditions(new String[]{"0",String.valueOf(BaseModel.EnumBoolean.EB_Yes.getIndex())});
                retailTradeList = dao.getRetailTradeDao().queryRaw(retailTrade.getSql(), retailTrade.getConditions());
                try {
                    if (retailTradeList != null) {
                        for (int i = 0; i < retailTradeList.size(); i++) {
                            retailTrade.setSql("where F_TradeID = ?");
                            retailTrade.setConditions(new String[]{String.valueOf(retailTradeList.get(i).getID())});
                            List<RetailTradeCommodity> retailTradeCommodityList = dao.getRetailTradeCommodityDao().queryRaw(retailTrade.getSql(), retailTrade.getConditions());
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
                    retailTradeList = dao.getRetailTradeDao().queryRaw(bm.getSql(), bm.getConditions());
                    if (retailTradeList != null) {
                        for (int i = 0; i < retailTradeList.size(); i++) {
                            retailTrade.setSql("where F_TradeID = ?");
                            retailTrade.setConditions(new String[]{String.valueOf(retailTradeList.get(i).getID())});
                            List<RetailTradeCommodity> retailTradeCommodityList = dao.getRetailTradeCommodityDao().queryRaw(retailTrade.getSql(), retailTrade.getConditions());
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
            case BaseSQLiteBO.CASE_RetailTrade_RetrieveNForReturned:
                // 根据SourceID查询本地SQLite的零售单
                try {
                    RetailTrade returnRetailTrade = (RetailTrade) bm;
                    RetailTrade retailTrade2 = new RetailTrade();
                    retailTrade2.setSql("where F_SourceID = ?");
                    retailTrade2.setConditions(new String[]{String.valueOf(returnRetailTrade.getSourceID())});
                    retailTradeList = dao.getRetailTradeDao().queryRaw(retailTrade2.getSql(), retailTrade2.getConditions());
                    if (retailTradeList != null) {
                        for (int i = 0; i < retailTradeList.size(); i++) {
                            retailTrade2.setSql("where F_TradeID = ?");
                            retailTrade2.setConditions(new String[]{String.valueOf(retailTradeList.get(i).getID())});
                            List<RetailTradeCommodity> retailTradeCommodityList = dao.getRetailTradeCommodityDao().queryRaw(retailTrade2.getSql(), retailTrade2.getConditions());
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
                    retailTradeList = dao.getRetailTradeDao().loadAll();
                    if (retailTradeList != null) {
                        for (int i = 0; i < retailTradeList.size(); i++) {
                            retailTrade.setSql("where F_TradeID = ?");
                            retailTrade.setConditions(new String[]{String.valueOf(retailTradeList.get(i).getID())});
                            List<RetailTradeCommodity> retailTradeCommodityList = dao.getRetailTradeCommodityDao().queryRaw(retailTrade.getSql(), retailTrade.getConditions());
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
                    e.printStackTrace();
                    log.info(this.getClass().getName() + ".retrieveNSync()发生异常：" + e.getMessage());
                    lastErrorCode = ErrorInfo.EnumErrorCode.EC_OtherError;
                    return null;
                }
        }
    }

    @Override
    protected BaseModel deleteSync(int iUseCaseID, BaseModel bm) {
        log.info("正在进行RetailTradePresenter的deleteSync，bm=" + (bm == null ? null : bm.toString()));

        try {
            dao.getRetailTradeDao().deleteByKey(bm.getID());
            //
            bm.setSql("where F_TradeID = ?");
            bm.setConditions(new String[]{String.valueOf(bm.getID())});
            List<RetailTradeCommodity> retailTradeCommodityList = dao.getRetailTradeCommodityDao().queryRaw(bm.getSql(), bm.getConditions());
            for (int i = 0; i < retailTradeCommodityList.size(); i++) {
                dao.getRetailTradeCommodityDao().deleteByKey(retailTradeCommodityList.get(i).getID());
            }
            //
            lastErrorCode = ErrorInfo.EnumErrorCode.EC_NoError;
        } catch (Exception e) {
            log.info("RetailTradePresenter.deleteSync发生异常：" + e.getMessage());
            e.printStackTrace();
            lastErrorCode = ErrorInfo.EnumErrorCode.EC_OtherError;
        }

        return null;
    }

    @Override
    protected boolean createMasterSlaveAsync(final int iUseCaseID, final BaseModel bmMaster, final BaseSQLiteEvent event) {
        log.info("正在进行RetailTradePresenter的createMasterSlaveAsync，bmMaster=" + (bmMaster == null ? null : bmMaster));

        event.setStatus(BaseEvent.EnumEventStatus.EES_SQLite_ToDo);
        switch (event.getEventTypeSQLite()) {
            case ESET_RetailTrade_CreateMasterSlaveAsync_Done:
                log.info("准备向本地SQLite插入RetailTrade主从表数据....");

                TaskScheduler.execute(new Runnable() {
                    @Override
                    public void run() {
//                        event.setEventTypeSQLite(BaseSQLiteEvent.EnumSQLiteEventType.ESET_RetailTrade_CreateMasterSlaveAsync_Done);
                        try {
                            RetailTrade bm = (RetailTrade) bmMaster;
                            bm.setSyncType(BasePresenter.SYNC_Type_C);
                            bm.setSyncDatetime(Constants.getDefaultSyncDatetime2());
                            bm.setDatetimeStart(new Date(System.currentTimeMillis() + NtpHttpBO.TimeDifference));
                            bm.setDatetimeEnd(new Date(System.currentTimeMillis() + NtpHttpBO.TimeDifference));
                            dao.getRetailTradeDao().insert(bm);
                            if (bm.getListSlave1() != null) {
                                for (int i = 0; i < (bm).getListSlave1().size(); i++) {
                                    RetailTradeCommodity rtc = (RetailTradeCommodity) (bm).getListSlave1().get(i);
                                    rtc.setTradeID(bm.getID());
                                    rtc.setSyncType(BasePresenter.SYNC_Type_C);
                                    rtc.setSyncDatetime(Constants.getDefaultSyncDatetime2());
                                    dao.getRetailTradeCommodityDao().insert(rtc);
                                }
                            }
                            //
                            log.info("向本地SQLite插入主从表数据成功！TradeID=" + bm.getID() + "  "
                                    + ((bm).getListSlave1() != null ? ((List<RetailTradeCommodity>) bm.getListSlave1()).get(0).getID() + "~" //打印第1个ID
                                    + ((RetailTradeCommodity) bm.getListSlave1().get(bm.getListSlave1().size() - 1)).getID() : "")//打印最后1个ID
                            );
                            //
                            if (!bInTestMode) {
                                ((RetailTrade) bm).setSlaveCreated(BaseModel.EnumBoolean.EB_Yes.getIndex());
                                dao.getRetailTradeDao().update((RetailTrade) bm);
                            }
                            event.setLastErrorCode(ErrorInfo.EnumErrorCode.EC_NoError);
                            event.setBaseModel1(bm);
                            event.setTmpMasterTableObj(bm);
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
                        EventBus.getDefault().post(event);
                    }
                });
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
                TaskScheduler.execute(new Runnable() {
                    @Override
                    public void run() {
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
                                                //删除新数据
                                                deleteRetailTrade(retailTradeOld);
                                                //插入新数据
                                                insertRetailTrade(retailTradeNew);
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
                        EventBus.getDefault().post(event);
                    }
                });
        }
        return true;
    }

    /**
     * TODO 将来对此函数编写单元测试
     */
    protected void insertRetailTrade(RetailTrade retailTradeNew) throws ParseException {
        log.info("当前准备插入的数据为：" + retailTradeNew);
        if (retailTradeNew.getSyncDatetime() == null) { //... 为null的情况有nbr响应POS时没设置,或者是解析同步时间失败
            log.error("已同步过的零售单=" + retailTradeNew + "因为SyncDatetime为空，所以会下次继续上传");
            retailTradeNew.setSyncDatetime(Constants.getDefaultSyncDatetime2());
        }
        retailTradeNew.setSyncType(BasePresenter.SYNC_Type_C);
        dao.getRetailTradeDao().insert(retailTradeNew);

        //插入零售单商品的新数据
        if (retailTradeNew.getListSlave1() != null) {
            for (int k = 0; k < retailTradeNew.getListSlave1().size(); k++) {
                RetailTradeCommodity rtc = (RetailTradeCommodity) retailTradeNew.getListSlave1().get(k);
                rtc.setSyncType(BasePresenter.SYNC_Type_C);
                if (rtc.getSyncDatetime() == null) { //...
                    log.error("已同步过的零售单商品=" + rtc + "因为SyncDatetime为空，所以会下次继续上传");
                    rtc.setSyncDatetime(Constants.getDefaultSyncDatetime2());
                }
                rtc.setTradeID(retailTradeNew.getID()); //必须设置TradeID，否则无法关联主表
                dao.getRetailTradeCommodityDao().insert(rtc);
            }
        } else {
            //... 为空的情况可能是从表部分创建失败。HttpEvent就不会进行解析
        }
        log.info("插入本地的零售单新数据成功！TradeID=" + retailTradeNew.getID() + "  "
                + (retailTradeNew.getListSlave1() != null ? ((RetailTradeCommodity) retailTradeNew.getListSlave1().get(0)).getID() + "~" //打印第1个ID
                + ((RetailTradeCommodity) retailTradeNew.getListSlave1().get((retailTradeNew).getListSlave1().size() - 1)).getID() : "")//打印最后1个ID
        );

        //插入零售单优惠券使用表的新数据
        if (retailTradeNew.getListSlave3() != null) {
            for (int k = 0; k < retailTradeNew.getListSlave3().size(); k++) {
                RetailTradeCoupon retailTradeCoupon = (RetailTradeCoupon) retailTradeNew.getListSlave3().get(k);
                retailTradeCoupon.setRetailTradeID(retailTradeNew.getID().intValue());
                dao.getRetailTradeCouponDao().insert(retailTradeCoupon);

                log.info("插入本地的零售单优惠券使用表新数据成功！PromotionID=" + retailTradeCoupon.getID());
            }
        }

        //插入零售单促销的新数据
        if (retailTradeNew.getListSlave2() != null) {
            for (int l = 0; l < retailTradeNew.getListSlave2().size(); l++) {
                RetailTradePromoting retailTradePromoting = (RetailTradePromoting) retailTradeNew.getListSlave2().get(l);
                retailTradePromoting.setSyncType(BasePresenter.SYNC_Type_C);
                dao.getRetailTradePromotingDao().insert(retailTradePromoting);
                if (retailTradePromoting.getListSlave1() != null) {
                    for (int n = 0; n < retailTradePromoting.getListSlave1().size(); n++) {
                        RetailTradePromotingFlow retailTradePromotingFlow = (RetailTradePromotingFlow) retailTradePromoting.getListSlave1().get(n);
                        retailTradePromotingFlow.setSyncType(BasePresenter.SYNC_Type_C);
                        dao.getRetailTradePromotingFlowDao().insert(retailTradePromotingFlow);
                    }
                }
                //
                log.info("插入本地的零售单促销新数据成功！PromotionID=" + retailTradePromoting.getID() + "  "
                        + (retailTradePromoting.getListSlave1() != null ? ((RetailTradePromotingFlow) retailTradePromoting.getListSlave1().get(0)).getID() + "~" //打印第1个ID
                        + ((RetailTradePromotingFlow) retailTradePromoting.getListSlave1().get((retailTradePromoting).getListSlave1().size() - 1)).getID() : "")//打印最后1个ID
                );
            }
        }
    }

    /**
     * TODO 将来对此函数编写单元测试
     */
    protected void deleteRetailTrade(RetailTrade retailTradeOld) {
        log.info("当前准备删除的临时数据为：" + retailTradeOld);
        // 删除零售单商品旧数据
        for (int k = 0; k < retailTradeOld.getListSlave1().size(); k++) {
            dao.getRetailTradeCommodityDao().delete((RetailTradeCommodity) (retailTradeOld.getListSlave1()).get(k));
        }
        // 删除零售单优惠券使用表旧数据,如果有
        if (retailTradeOld.getListSlave3() != null) {
            for (int k = 0; k < retailTradeOld.getListSlave3().size(); k++) {
                dao.getRetailTradeCouponDao().delete((RetailTradeCoupon) retailTradeOld.getListSlave3().get(k));
                log.info("当前准备删除的临时零售单优惠券使用表数据为：" + retailTradeOld);
            }
        } else {
            log.info("本单并没有零售单优惠券使用表");
        }

        // 删除零售单促销表旧数据,如果有
        if (retailTradeOld.getListSlave2() != null) {
            for (int l = 0; l < retailTradeOld.getListSlave2().size(); l++) {
                RetailTradePromoting retailTradePromoting = (RetailTradePromoting) retailTradeOld.getListSlave2().get(0);
                log.info("当前准备删除的临时零售单促销数据为：" + retailTradePromoting);
                for (int n = 0; n < retailTradePromoting.getListSlave1().size(); n++) {
                    dao.getRetailTradePromotingFlowDao().delete((RetailTradePromotingFlow) retailTradePromoting.getListSlave1().get(n));
                }
                dao.getRetailTradePromotingDao().delete(retailTradePromoting);
                log.info("删除本地的零售单促销表旧数据成功！PromotingID=" + retailTradePromoting.getID() + "  "
                        + (retailTradePromoting.getListSlave1().size() > 0 ? ((RetailTradePromotingFlow) retailTradePromoting.getListSlave1().get(0)).getID() + "~" //打印第1个ID
                        + ((RetailTradePromotingFlow) retailTradePromoting.getListSlave1().get((retailTradePromoting).getListSlave1().size() - 1)).getID() : "")//打印最后1个ID
                );
            }
        } else {
            log.info("此零售单无零售单促销表！");
        }

        dao.getRetailTradeDao().delete(retailTradeOld);
        log.info("删除本地的零售单旧数据成功！TradeID=" + retailTradeOld.getID() + "  "
                + (retailTradeOld.getListSlave1().size() > 0 ? ((RetailTradeCommodity) retailTradeOld.getListSlave1().get(0)).getID() + "~" //打印第1个ID
                + ((RetailTradeCommodity) retailTradeOld.getListSlave1().get((retailTradeOld).getListSlave1().size() - 1)).getID() : "")//打印最后1个ID
        );
    }

    @Override
    protected boolean createReplacerAsync(final int iUseCaseID, final BaseModel bmOld, final BaseModel bmNew, final BaseSQLiteEvent event) {
        log.info("正在进行RetailTradePresenter的createReplacerAsync，bmOld=" + (bmOld == null ? null : bmOld.toString())//
                + "，bmNew=" + (bmNew == null ? null : bmNew.toString()));

        event.setStatus(BaseEvent.EnumEventStatus.EES_SQLite_ToApplyServerData);

        switch (iUseCaseID) {
            default:
                TaskScheduler.execute(new Runnable() {
                    @Override
                    public void run() {
                        log.info("已经得到服务器返回的主表和从表数据，准备删除本地的旧数据，并插入服务器的数据...");//...

                        try {
                            deleteRetailTrade((RetailTrade) bmOld);
                            //
                            RetailTrade retailTradeNew = (RetailTrade) bmNew;
                            insertRetailTrade(retailTradeNew);
                            //如果是退货类型的零售单，则需要更新下源单的可退货数量
                            if (retailTradeNew.getSourceID() > 0) {
                                if (!updateNOCanReturn(retailTradeNew)) {
                                    event.setLastErrorCode(ErrorInfo.EnumErrorCode.EC_OtherError);
                                    log.info("上传退货单成功后，SQLite更新源单商品的可退货数量失败");
                                    EventBus.getDefault().post(event);
                                }
                            }
                            //
                            event.setLastErrorCode(ErrorInfo.EnumErrorCode.EC_NoError);
                            event.setBaseModel1(retailTradeNew);
                        } catch (Exception e) {
                            log.info("createReplacerAsync()异常：" + e.getMessage());
                            e.printStackTrace();
                            event.setLastErrorCode(ErrorInfo.EnumErrorCode.EC_OtherError);
                        }
//                        event.setStatus(BaseEvent.EnumEventStatus.EES_SQLite_DoneApplyServerData);
//                        event.setEventTypeSQLite(BaseSQLiteEvent.EnumSQLiteEventType.ESET_RetailTrade_CreateReplacerAsync_Done);
                        //
                        EventBus.getDefault().post(event);
                    }
                });
                break;
        }

        return true;
    }

    @Override
    protected boolean refreshByServerDataAsync(final int iUseCaseID, final List<?> bmNewList, final BaseSQLiteEvent event) {
        event.setStatus(BaseEvent.EnumEventStatus.EES_SQLite_ToDo);
        switch (iUseCaseID) {
            default:
                TaskScheduler.execute(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            log.info("查询后服务器返回的零售单，准备进行插入数据库！");
//                            event.setEventTypeSQLite(BaseSQLiteEvent.EnumSQLiteEventType.ESET_RetailTrade_updateNAsync);
                            //如果查询零售单返回数据，则把本地对应ID的零售单删除，然后再插入服务器返回的零售单
                            if (bmNewList != null) {
                                if (bmNewList.size() > 0) {
                                    List<RetailTrade> retailTradeList = (List<RetailTrade>) bmNewList;
                                    //删除
                                    for (int i = 0; i < retailTradeList.size(); i++) {
                                        RetailTrade retailTrade = dao.getRetailTradeDao().load(retailTradeList.get(i).getID());
                                        RetailTrade rt = retailTradeList.get(i);
                                        if (retailTrade != null) {
                                            if (rt.getListSlave1() != null && rt.getListSlave1().size() > 0) {
                                                for (int j = 0; j < rt.getListSlave1().size(); j++) {
                                                    RetailTradeCommodity rtc = (RetailTradeCommodity) rt.getListSlave1().get(j);
                                                    dao.getRetailTradeCommodityDao().delete(rtc);
                                                }
                                            }
                                            dao.getRetailTradeDao().delete(retailTrade);
                                        }
                                    }

                                    List<RetailTradeCommodity> retailTradeCommodityList = dao.getRetailTradeCommodityDao().loadAll();

                                    //插入
                                    for (int i = 0; i < retailTradeList.size(); i++) {
                                        RetailTrade rt = retailTradeList.get(i);
                                        dao.getRetailTradeDao().insertOrReplace(rt);
                                        if (rt.getListSlave1() != null) {
                                            for (int j = 0; j < rt.getListSlave1().size(); j++) {
                                                RetailTradeCommodity rtc = (RetailTradeCommodity) rt.getListSlave1().get(j);
                                                dao.getRetailTradeCommodityDao().insertOrReplace(rtc);
                                            }
                                        }
                                    }
                                }
                            }
                            event.setLastErrorCode(ErrorInfo.EnumErrorCode.EC_NoError);
                        } catch (Exception e) {
                            log.info("执行updateNAsync时出错，错误信息：" + e.getMessage());
                            e.printStackTrace();
                            event.setLastErrorCode(ErrorInfo.EnumErrorCode.EC_OtherError);
                        }
                        event.setListMasterTable(bmNewList);
//                        event.setString1("RN");
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
        log.info("正在进行RetailTradePresenter的createAsync，bm=" + (bm == null ? null : bm.toString()));

        switch (iUseCaseID) {
            default:
                TaskScheduler.execute(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            event.setLastErrorCode(ErrorInfo.EnumErrorCode.EC_NoError);

                            RetailTrade retailTrade = (RetailTrade) bm;
                            retailTrade.setSyncDatetime(Constants.getDefaultSyncDatetime2());
                            retailTrade.setSyncType(BasePresenter.SYNC_Type_C);
                            retailTrade.setDatetimeStart(new Date(System.currentTimeMillis() + NtpHttpBO.TimeDifference));
                            retailTrade.setDatetimeEnd(new Date(System.currentTimeMillis() + NtpHttpBO.TimeDifference));
                            retailTrade.setSaleDatetime(new Date(System.currentTimeMillis() + NtpHttpBO.TimeDifference));

                            long id = dao.getRetailTradeDao().insert(retailTrade);
                            retailTrade.setID(id);

                            List<RetailTradeCommodity> retailTradeCommodities = (List<RetailTradeCommodity>) retailTrade.getListSlave1();
                            for (int i = 0; i < retailTradeCommodities.size(); i++) {
                                RetailTradeCommodity retailTradeCommodity = retailTradeCommodities.get(i);

                                retailTradeCommodity.setSyncDatetime(Constants.getDefaultSyncDatetime2());
                                retailTradeCommodity.setSyncType(BasePresenter.SYNC_Type_C);
                                retailTradeCommodity.setTradeID(id);

                                dao.getRetailTradeCommodityDao().insert(retailTradeCommodity);
                                //服务器不需要知道零售单商品表的ID
                            }
                            // 创建退货单没有上传，需要设置SlaveCreated = 1
                            if (!bInTestMode) {
                                ((RetailTrade) bm).setSlaveCreated(BaseModel.EnumBoolean.EB_Yes.getIndex());
                                dao.getRetailTradeDao().update((RetailTrade) bm);
                            }
                        } catch (Exception e) {
//                            Log.e("输出：", "创建RetailTrade时出错，错误信息：" + e.getMessage());
                            log.info("创建RetailTrade时出错，错误信息：" + e.getMessage());
                            e.printStackTrace();
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

//    @Override
//    protected boolean createOrReplaceNAsync(int iUseCaseID, final List<?> list, final BaseSQLiteEvent event) {
//        switch (iUseCaseID) {
//            default:
//                TaskScheduler.execute(new Runnable() {
//                    @Override
//                    public void run() {
////                        event.setEventTypeSQLite(BaseSQLiteEvent.EnumSQLiteEventType.ESET_RetailTrade_CreateNAsync);
//                        //
//                        try {
//                            for (int i = 0; i < list.size(); i++) {
//                                ((RetailTrade) list.get(i)).setSyncDatetime(new Date(System.currentTimeMillis() + NtpHttpBO.TimeDifference));
//                                long id = dao.getRetailTradeDao().insertOrReplace((RetailTrade) list.get(i));
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
                TaskScheduler.execute(new Runnable() {
                    @Override
                    public void run() {
//                        event.setEventTypeSQLite(BaseSQLiteEvent.EnumSQLiteEventType.ESET_RetailTrade_CreateNAsync);
                        //
                        try {
                            for (int i = 0; i < list.size(); i++) {
                                ((RetailTrade) list.get(i)).setSyncDatetime(new Date(System.currentTimeMillis() + NtpHttpBO.TimeDifference));
                                ((RetailTrade) list.get(i)).setSyncType(BasePresenter.SYNC_Type_C);

                                long id = dao.getRetailTradeDao().insert((RetailTrade) list.get(i));//...插入一部分失败
                                ((RetailTrade) list.get(i)).setID(id);
                            }
                            event.setLastErrorCode(ErrorInfo.EnumErrorCode.EC_NoError);
                        } catch (Exception e) {
                            log.info("创建RetailTrade时出错，错误信息：" + e.getMessage());
                            event.setLastErrorCode(ErrorInfo.EnumErrorCode.EC_OtherError);
                            log.info("创建RetailTrade时出错2，错误信息：" + event.getLastErrorCode());
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
    protected boolean updateAsync(int iUseCaseID, final BaseModel bm, final BaseSQLiteEvent event) {
        log.info("正在进行RetailTradePresenter的updateAsync，bm=" + (bm == null ? null : bm.toString()));

        switch (iUseCaseID) {
            default:
                TaskScheduler.execute(new Runnable() {
                    @Override
                    public void run() {
                        //
                        try {
                            bm.setSyncType(BasePresenter.SYNC_Type_C); // ...ZJH需要C型数据上传
                            dao.getRetailTradeDao().update((RetailTrade) bm);
                            event.setLastErrorCode(ErrorInfo.EnumErrorCode.EC_NoError);
//                          event.setStatus(BaseEvent.EnumEventStatus.EES_SQLite_Done);
                        } catch (Exception e) {
                            Log.e("输出：", "创建RetailTrade时出错，错误信息：" + e.getMessage());
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
        log.info("正在进行RetailTradePresenter的retrieveNAsync，bm=" + (bm == null ? null : bm.toString()));

        switch (iUseCaseID) {
            case BaseSQLiteBO.CASE_RetailTrade_RetrieveNToUpload:
                //...
                TaskScheduler.execute(new Runnable() {
                    @Override
                    public void run() {
                        List<RetailTrade> result = null;
                        //
                        try {
                            BaseModel bm = new BaseModel();
                            bm.setSql("where F_SyncDatetime = ? and F_SlaveCreated = ?");
                            bm.setConditions(new String[]{"0",String.valueOf(BaseModel.EnumBoolean.EB_Yes.getIndex())});
                            result = dao.getRetailTradeDao().queryRaw(bm.getSql(), bm.getConditions());
                            if (result != null) {
                                for (int j = 0; j < result.size(); j++) {
                                    if (result.get(j).getSourceID() > 0 && result.get(j).getAmountWeChat() > 0 && (StringUtils.isEmpty(result.get(j).getWxRefundNO()) && StringUtils.isEmpty(result.get(j).getWxRefundSubMchID()))) {
                                        result.remove(j);
                                    }
                                }
                                log.info("查询到本地SQLite有" + result.size() + "个临时零售单！");
                                for (int i = 0; i < result.size(); i++) {
                                    //查询零售单商品
                                    bm.setSql("where F_TradeID = ?");
                                    bm.setConditions(new String[]{String.valueOf(result.get(i).getID())});
                                    List<RetailTradeCommodity> retailTradeCommodityList = dao.getRetailTradeCommodityDao().queryRaw(bm.getSql(), bm.getConditions());
                                    result.get(i).setListSlave1(retailTradeCommodityList);
                                    //查询零售单促销和从表
                                    List<RetailTradePromoting> retailTradePromotingList = dao.getRetailTradePromotingDao().queryRaw(bm.getSql(), bm.getConditions());
                                    if (retailTradePromotingList != null || retailTradePromotingList.size() == 0) {
                                        for (RetailTradePromoting retailTradePromoting : retailTradePromotingList) {
                                            retailTradePromoting.setSql("where F_RetailTradePromotingID = ?");
                                            retailTradePromoting.setConditions(new String[]{String.valueOf(retailTradePromoting.getID())});
                                            List<RetailTradePromotingFlow> retailTradePromotingFlowList = dao.getRetailTradePromotingFlowDao().queryRaw(retailTradePromoting.getSql(), retailTradePromoting.getConditions());
                                            if (retailTradePromotingFlowList != null && retailTradePromotingFlowList.size() > 0) {
                                                retailTradePromoting.setListSlave1(retailTradePromotingFlowList);
                                            } else {
                                                log.error("没有找到零售单localSN=" + result.get(i).getLocalSN() + "的零售单促销过程表");
                                                //...是否要return
                                            }
                                        }
                                    }
                                    result.get(i).setListSlave2(retailTradePromotingList);

                                    //查询零售单优惠券使用表
                                    bm.setSql("where F_RetailTradeID = ?");
                                    bm.setConditions(new String[]{String.valueOf(result.get(i).getID())});
                                    List<RetailTradeCoupon> retailTradeCouponList = dao.getRetailTradeCouponDao().queryRaw(bm.getSql(), bm.getConditions());
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
                            Log.e("输出：", "RetrieveNAsync RetailTrade出错，错误信息：" + e.getMessage());
                            event.setLastErrorCode(ErrorInfo.EnumErrorCode.EC_OtherError);
                        }
                        //
                        event.setListMasterTable(result);
                        //
                        EventBus.getDefault().post(event);
                    }
                });
                break;
            case BaseSQLiteBO.CASE_RetailTrade_RetrieveNByConditions:
                TaskScheduler.execute(new Runnable() {
                    @Override
                    public void run() {
                        List<RetailTrade> result = null;
                        try {
                            result = dao.getRetailTradeDao().queryRaw(bm.getSql(), bm.getConditions());
                            if (result != null) {
                                for (int j = 0; j < result.size(); j++) {
                                    if (result.get(j).getSourceID() > 0 && result.get(j).getAmountWeChat() > 0 && (StringUtils.isEmpty(result.get(j).getWxRefundNO()) && StringUtils.isEmpty(result.get(j).getWxRefundSubMchID()))) {
                                        result.remove(j);
                                    }
                                }
                                for (int i = 0; i < result.size(); i++) {
                                    bm.setSql("where F_TradeID = ?");
                                    bm.setConditions(new String[]{String.valueOf(result.get(i).getID())});
                                    List<RetailTradeCommodity> retailTradeCommodityList = dao.getRetailTradeCommodityDao().queryRaw(bm.getSql(), bm.getConditions());
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
                            Log.e("输出：", "RetrieveNAsync RetailTrade出错，错误信息：" + e.getMessage());
                            event.setLastErrorCode(ErrorInfo.EnumErrorCode.EC_OtherError);
                        }
                        //
                        event.setListMasterTable(result);
                        //
                        EventBus.getDefault().post(event);
                    }
                });
                break;
            default:
                TaskScheduler.execute(new Runnable() {
                    @Override
                    public void run() {
                        List<RetailTrade> result = null;
                        //
                        try {
                            Thread.sleep(1); //令event.setID()的ID惟一
                            result = dao.getRetailTradeDao().loadAll();//...
                            RetailTrade bm = new RetailTrade();
                            if (result != null) {
                                for (int j = 0; j < result.size(); j++) {
                                    if (result.get(j).getSourceID() > 0 && result.get(j).getAmountWeChat() > 0 && (StringUtils.isEmpty(result.get(j).getWxRefundNO()) && StringUtils.isEmpty(result.get(j).getWxRefundSubMchID()))) {
                                        result.remove(j);
                                    }
                                }
                                log.info("查询到本地SQLite有临时零售单！");
                                for (int i = 0; i < result.size(); i++) {
                                    bm.setSql("where F_TradeID = ?");
                                    bm.setConditions(new String[]{String.valueOf(result.get(i).getID())});
                                    List<RetailTradeCommodity> retailTradeCommodityList = dao.getRetailTradeCommodityDao().queryRaw(bm.getSql(), bm.getConditions());
                                    result.get(i).setListSlave1(retailTradeCommodityList);
                                    result.get(i).setDatetimeStart(Constants.getDefaultSyncDatetime2());
                                    result.get(i).setDatetimeEnd(Constants.getDefaultSyncDatetime2());
                                    result.get(i).setSyncDatetime(Constants.getDefaultSyncDatetime2());
                                    result.get(i).setReturnObject(1);
                                }
                            }
                            event.setLastErrorCode(ErrorInfo.EnumErrorCode.EC_NoError);
                        } catch (Exception e) {
                            Log.e("输出：", "查询SQLite的表RetailTrade的所有记录时出错，错误信息：" + e.getMessage());
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
    protected boolean deleteAsync(int iUseCaseID, final BaseModel bm, final BaseSQLiteEvent event) {
        log.info("正在进行RetailTradePresenter的deleteAsync，bm=" + (bm == null ? null : bm.toString()));

        switch (iUseCaseID) {
            default:
                TaskScheduler.execute(new Runnable() {
                    @Override
                    public void run() {
//                        event.setEventTypeSQLite(BaseSQLiteEvent.EnumSQLiteEventType.ESET_RetailTrade_DeleteAsync);
                        //
                        try {
                            dao.getRetailTradeDao().delete((RetailTrade) bm);
                            event.setLastErrorCode(ErrorInfo.EnumErrorCode.EC_NoError);
                        } catch (Exception e) {
                            Log.e("输出：", "删除RetailTrade时出错，错误信息：" + e.getMessage());
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
        log.info("正在进行RetailTradePresenter的retrieve1Async，bm=" + (bm == null ? null : bm.toString()));

        switch (iUseCaseID) {
            default:
                TaskScheduler.execute(new Runnable() {
                    @Override
                    public void run() {
                        RetailTrade result = null;
//                        event.setEventTypeSQLite(BaseSQLiteEvent.EnumSQLiteEventType.ESET_RetailTrade_Retrieve1Async);
                        //
                        try {
                            result = dao.getRetailTradeDao().load(((RetailTrade) bm).getID());//...
                            if (result != null) {
                                bm.setSql("where F_TradeID = ?");
                                bm.setConditions(new String[]{String.valueOf(result.getID())});
                                result.setListSlave1(dao.getRetailTradeCommodityDao().queryRaw(bm.getSql(), bm.getConditions()));
                            }

                            event.setLastErrorCode(ErrorInfo.EnumErrorCode.EC_NoError);
                        } catch (Exception e) {
                            Log.e("输出：", "Retrieve1 RetailTrade时出错，错误信息：" + e.getMessage());
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
    protected boolean refreshByServerDataAsyncC(final int iUseCaseID, final List<?> bmNewList, final BaseSQLiteEvent event) {
        log.info("正在进行RetailTradePresenter的refreshByServerDataAsyncC，bmNewList=" + (bmNewList == null ? null : bmNewList.toString()));

        event.setStatus(BaseEvent.EnumEventStatus.EES_SQLite_ToApplyServerData);
        switch (iUseCaseID) {
            default:
                TaskScheduler.execute(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            log.info("已经得到服务器返回的需要同步的RetailTrade数据, 准备进行同步...");

//                        event.setEventTypeSQLite(BaseSQLiteEvent.EnumSQLiteEventType.ESET_RetailTrade_RefreshByServerDataAsync_Done);

                            //删除本地所有的数据
                            dao.getRetailTradeCommodityDao().deleteAll();
                            dao.getRetailTradeDao().deleteAll();
                            //同步所有服务器返回的数据
                            if (bmNewList != null) {
                                for (int i = 0; i < bmNewList.size(); i++) {
                                    dao.getRetailTradeDao().insert((RetailTrade) bmNewList.get(i));
                                    for (int j = 0; j < ((RetailTrade) bmNewList.get(i)).getListSlave1().size(); j++) {
                                        dao.getRetailTradeCommodityDao().insert(((List<RetailTradeCommodity>) ((RetailTrade) bmNewList.get(i)).getListSlave1()).get(j));
                                    }
                                }
                            } else {
                                log.info("服务器没有retailTrade返回");
                            }
                            event.setLastErrorCode(ErrorInfo.EnumErrorCode.EC_NoError);
                        } catch (Exception e) {
                            event.setLastErrorCode(ErrorInfo.EnumErrorCode.EC_OtherError);
                            log.info("RetailTrade refreshByServerDataAsyncC出错，错误信息为：" + e.getMessage());
                        }
                        //
                        EventBus.getDefault().post(event);
                    }
                });
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
                TaskScheduler.execute(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            log.info("查询后服务器返回的零售单，准备进行插入数据库！");
//                            event.setEventTypeSQLite(BaseSQLiteEvent.EnumSQLiteEventType.ESET_RetailTrade_updateNAsync);
                            //如果查询零售单返回数据，则把本地对应ID的零售单删除，然后再插入服务器返回的零售单
                            if (bmList != null) {
                                if (bmList.size() > 0) {
                                    List<RetailTrade> retailTradeList = (List<RetailTrade>) bmList;
                                    //删除
                                    for (int i = 0; i < retailTradeList.size(); i++) {
                                        RetailTrade retailTrade = dao.getRetailTradeDao().load(retailTradeList.get(i).getID());
                                        RetailTrade rt = retailTradeList.get(i);
                                        if (retailTrade != null) {
                                            if (rt.getListSlave1() != null && rt.getListSlave1().size() > 0) {
                                                for (int j = 0; j < rt.getListSlave1().size(); j++) {
                                                    RetailTradeCommodity rtc = (RetailTradeCommodity) rt.getListSlave1().get(j);
                                                    dao.getRetailTradeCommodityDao().delete(rtc);
                                                    if (dao.getRetailTradeCommodityDao().load(rtc.getID()) != null) {
                                                        new RuntimeException("删除失败");
                                                    }
                                                }
                                            }
                                            dao.getRetailTradeDao().delete(retailTrade);
                                        }
                                    }
                                    //插入
                                    for (int i = 0; i < retailTradeList.size(); i++) {
                                        RetailTrade rt = retailTradeList.get(i);
                                        dao.getRetailTradeDao().insert(rt);
                                        if (rt.getListSlave1() != null) {
                                            for (int j = 0; j < rt.getListSlave1().size(); j++) {
                                                RetailTradeCommodity rtc = (RetailTradeCommodity) rt.getListSlave1().get(j);
                                                dao.getRetailTradeCommodityDao().insert(rtc);
                                            }
                                        }
                                    }
                                }
                            }
                            event.setLastErrorCode(ErrorInfo.EnumErrorCode.EC_NoError);
                        } catch (Exception e) {
                            log.info("执行updateNAsync时出错，错误信息：" + e.getMessage());
                            e.printStackTrace();
                            event.setLastErrorCode(ErrorInfo.EnumErrorCode.EC_OtherError);
                        }
                        event.setListMasterTable(bmList);
//                        event.setString1("RN");
                        //
                        EventBus.getDefault().post(event);
                    }
                });
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
            dao.getRetailTradeDao().dropTable(dao.getRetailTradeDao().getDatabase(), true);
            lastErrorCode = ErrorInfo.EnumErrorCode.EC_NoError;
        } catch (Exception e) {
            Log.e("输出：", "删除SQLite中的表RetailTrade时出错，错误信息：" + e.getMessage());
            lastErrorCode = ErrorInfo.EnumErrorCode.EC_OtherError;
        }
    }

    protected boolean updateNOCanReturn(RetailTrade retailTrade) {
        RetailTrade rt = new RetailTrade();
        rt.setID((long) retailTrade.getSourceID());
        rt = (RetailTrade) retrieve1Sync(BaseSQLiteBO.INVALID_CASE_ID, rt);
        if (lastErrorCode != ErrorInfo.EnumErrorCode.EC_NoError) {
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
                    GlobalController.getInstance().getRetailTradeCommodityPresenter().updateObjectSync(BaseSQLiteBO.INVALID_CASE_ID, rtc);
                    if (lastErrorCode != ErrorInfo.EnumErrorCode.EC_NoError) {
                        return false;
                    }
                    break;
                }
            }
        }

        return true;
    }
}
