package com.bx.erp.presenter;

import android.util.Log;

import com.bx.erp.bo.BaseSQLiteBO;
import com.bx.erp.bo.NtpHttpBO;
import com.bx.erp.common.GlobalController;
import com.bx.erp.event.BaseEvent;
import com.bx.erp.event.UI.BaseSQLiteEvent;
import com.bx.erp.helper.Constants;
import com.bx.erp.model.BaseModel;
import com.bx.erp.model.DaoSession;
import com.bx.erp.model.ErrorInfo;
import com.bx.erp.model.RetailTradePromoting;
import com.bx.erp.model.RetailTradePromotingFlow;
import com.silencedut.taskscheduler.TaskScheduler;

import org.apache.log4j.Logger;
import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.inject.Inject;

import static com.bx.erp.bo.BaseSQLiteBO.CASE_RetailTradePromoting_RetrieveNByConditions;
import static com.bx.erp.bo.BaseSQLiteBO.CASE_RetailTradePromoting_RetrieveNByTradeID;
import static com.bx.erp.bo.BaseSQLiteBO.CASE_RetailTradePtomoting_RetrieveNToUpload;

public class RetailTradePromotingPresenter extends BasePresenter {
    private Logger log = Logger.getLogger(this.getClass());

    @Inject
    public RetailTradePromotingPresenter(final DaoSession dao) {
        super(dao);
    }

    @Override
    protected String getTableName() {
        return dao.getRetailTradePromotingDao().getTablename();
    }

    @Override
    protected boolean createReplacerAsync(final int iUseCaseID, final BaseModel bmOld, final BaseModel bmNew, final BaseSQLiteEvent event) {
        log.info("正在进行RetailTradePromotingPresenter的createReplacerAsync，bmOld=" + bmOld + ",bmNew=" + bmNew);

        event.setStatus(BaseEvent.EnumEventStatus.EES_SQLite_ToApplyServerData);

        switch (iUseCaseID) {
            default:
                TaskScheduler.execute(new Runnable() {
                    @Override
                    public void run() {
                        log.info("已经得到服务器返回的促销详细数据，准备删除本地旧数据，插入服务器返回的新数据...");

                        try {
                            // 删除本地存在的数据
                            if (bmOld != null) {
                                RetailTradePromoting oldRtp = (RetailTradePromoting) bmOld;
                                List<RetailTradePromotingFlow> listRetailTradePromotingFlow = (List<RetailTradePromotingFlow>) oldRtp.getListSlave1();
                                if (listRetailTradePromotingFlow != null && listRetailTradePromotingFlow.size() > 0) {
                                    for (RetailTradePromotingFlow retailTradePromotingFlow : listRetailTradePromotingFlow) {
                                        dao.getRetailTradePromotingFlowDao().delete(retailTradePromotingFlow);
                                    }
                                }
                                dao.getRetailTradePromotingDao().delete(oldRtp);
                            }
                            RetailTradePromoting rtpNew = (RetailTradePromoting) bmNew;
                            // 查询次零售单促销计算过程是否存在在本地数据库中
                            List<RetailTradePromoting> list1 = dao.getRetailTradePromotingDao().queryRaw("Where F_TradeID = ? ", new String[]{String.valueOf(rtpNew.getTradeID())});
                            if (list1 != null && list1.size() > 0) {
                                for (RetailTradePromoting retailTradePromoting : list1) {
                                    List<RetailTradePromotingFlow> retailTradePromotingFlows = dao.getRetailTradePromotingFlowDao().queryRaw("Where F_RetailTradePromotingID = ? ", new String[]{String.valueOf(retailTradePromoting.getID())});
                                    if (retailTradePromotingFlows != null && retailTradePromotingFlows.size() > 0) {
                                        for (RetailTradePromotingFlow retailTradePromotingFlow : retailTradePromotingFlows) {
                                            // 删除从表
                                            dao.getRetailTradePromotingFlowDao().delete(retailTradePromotingFlow);
                                        }
                                    }
                                    // 删除主表
                                    dao.getRetailTradePromotingDao().delete(retailTradePromoting);
                                }
                            }
                            dao.getRetailTradePromotingDao().insert((RetailTradePromoting) bmNew);
                            List<RetailTradePromotingFlow> listRetailTradePromotingFlow = (List<RetailTradePromotingFlow>) bmNew.getListSlave1();
                            if (listRetailTradePromotingFlow != null && listRetailTradePromotingFlow.size() > 0) {
                                for (RetailTradePromotingFlow retailTradePromotingFlow : listRetailTradePromotingFlow) {
                                    dao.getRetailTradePromotingFlowDao().insert(retailTradePromotingFlow);
                                }
                            }
                            //
                            event.setLastErrorCode(ErrorInfo.EnumErrorCode.EC_NoError);
                            event.setBaseModel1(bmNew);
                            //
                            log.info("插入服务器返回的主从表成功！ID = " + bmNew.getID());
                        } catch (Exception e) {
                            log.info("createReplacerAsync()异常：" + e.getMessage());
                            e.printStackTrace();
                            event.setLastErrorCode(ErrorInfo.EnumErrorCode.EC_OtherError);
                        }
//                        event.setEventTypeSQLite(BaseSQLiteEvent.EnumSQLiteEventType.ESET_RetailTradePromoting_CreateReplacerAsync_Done);
                        //
                        EventBus.getDefault().post(event);
                    }
                });
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
                TaskScheduler.execute(new Runnable() {
                    @Override
                    public void run() {
                        log.info("已经得到服务器返回的一批RetailTradePromoting，准备删除本地旧数据，插入新数据！");
                        try {
                            //删除旧数据
                            for (int i = 0; i < bmOldList.size(); i++) {
                                RetailTradePromoting retailTradePromotingOld = (RetailTradePromoting) bmOldList.get(i);
                                //删除从表
                                for (int j = 0; j < retailTradePromotingOld.getListSlave1().size(); j++) {
                                    dao.getRetailTradePromotingFlowDao().delete((RetailTradePromotingFlow) retailTradePromotingOld.getListSlave1().get(j));
                                }
                                //删除主表
                                dao.getRetailTradePromotingDao().delete(retailTradePromotingOld);
                                log.info("删除主表ID：" + retailTradePromotingOld.getID() + "   删除从表ID：" + ((RetailTradePromotingFlow) retailTradePromotingOld.getListSlave1().get(0)).getID() + "~" +
                                        ((RetailTradePromotingFlow) retailTradePromotingOld.getListSlave1().get(retailTradePromotingOld.getListSlave1().size() - 1)).getID());
                            }

                            //插入新数据
                            for (int i = 0; i < bmNewList.size(); i++) {
                                RetailTradePromoting retailTradePromotingNew = (RetailTradePromoting) bmNewList.get(i);
                                //插入主表
                                dao.getRetailTradePromotingDao().insert(retailTradePromotingNew);

                                //插入从表
                                for (int j = 0; j < retailTradePromotingNew.getListSlave1().size(); j++) {
                                    dao.getRetailTradePromotingFlowDao().insert((RetailTradePromotingFlow) retailTradePromotingNew.getListSlave1().get(j));
                                }

                                log.info("插入主表ID：" + retailTradePromotingNew.getID() + "   插入从表ID：" + ((RetailTradePromotingFlow) retailTradePromotingNew.getListSlave1().get(0)).getID() + "~" +
                                        ((RetailTradePromotingFlow) retailTradePromotingNew.getListSlave1().get(retailTradePromotingNew.getListSlave1().size() - 1)).getID());
                            }

                            event.setLastErrorCode(ErrorInfo.EnumErrorCode.EC_NoError);
                            event.setListMasterTable(bmNewList);
                        } catch (Exception e) {
                            e.printStackTrace();
                            log.info("RetailTradePromoting createreplacerNAsync()异常：" + e.getMessage());
                            event.setLastErrorCode(ErrorInfo.EnumErrorCode.EC_OtherError);
                        }
                        EventBus.getDefault().post(event);
                    }
                });
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
                TaskScheduler.execute(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            dao.clear();
                            lastErrorCode = ErrorInfo.EnumErrorCode.EC_NoError;
                            List<RetailTradePromoting> retailTradePromotings = dao.getRetailTradePromotingDao().queryRaw("Where F_TradeID = ?", new String[]{String.valueOf(rtp.getTradeID())});
                            if (retailTradePromotings != null && retailTradePromotings.size() == 1) {
                                List<RetailTradePromotingFlow> retailTradePromotingFlows = dao.getRetailTradePromotingFlowDao().queryRaw("Where F_RetailTradePromotingID = ?", new String[]{String.valueOf(retailTradePromotings.get(0).getID())});
                                retailTradePromotings.get(0).setListSlave1(retailTradePromotingFlows);
                                event.setBaseModel1(retailTradePromotings.get(0));
                            }
                            event.setLastErrorCode(ErrorInfo.EnumErrorCode.EC_NoError);
                        } catch (Exception e) {
                            e.printStackTrace();
                            log.info("根据TradeID查询RetailTradePromoting异常，异常信息为：" + e.getMessage());
                            event.setLastErrorCode(ErrorInfo.EnumErrorCode.EC_OtherError);
                        }
                        EventBus.getDefault().post(event);
                    }
                });
                break;
            default:
                TaskScheduler.execute(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            dao.clear();
                            lastErrorCode = ErrorInfo.EnumErrorCode.EC_NoError;
                            RetailTradePromoting retailTradePromoting = dao.getRetailTradePromotingDao().load(bm.getID());
                            if (retailTradePromoting != null) {//查出从表
                                List<RetailTradePromotingFlow> retailTradePromotingFlows = dao.getRetailTradePromotingFlowDao().queryRaw("Where F_RetailTradePromotingID = ?", new String[]{String.valueOf(retailTradePromoting.getID())});
                                retailTradePromoting.setListSlave1(retailTradePromotingFlows);
                            }
                            event.setBaseModel1(retailTradePromoting);
                            event.setLastErrorCode(ErrorInfo.EnumErrorCode.EC_NoError);
                        } catch (Exception e) {
                            log.info("执行retrieve1Async时出错，错误信息：" + e.getMessage());
                            e.printStackTrace();
                            event.setLastErrorCode(ErrorInfo.EnumErrorCode.EC_OtherError);
                        }
                        EventBus.getDefault().post(event);
                    }
                });
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
                    dao.clear();
                    lastErrorCode = ErrorInfo.EnumErrorCode.EC_NoError;
                    List<RetailTradePromoting> retailTradePromotings = dao.getRetailTradePromotingDao().queryRaw("Where F_TradeID = ?", new String[]{String.valueOf(rtp.getTradeID())});
                    if (retailTradePromotings != null && retailTradePromotings.size() == 1) {
                        List<RetailTradePromotingFlow> retailTradePromotingFlows = dao.getRetailTradePromotingFlowDao().queryRaw("Where F_RetailTradePromotingID = ?", new String[]{String.valueOf(retailTradePromotings.get(0).getID())});
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
                    dao.clear();
                    RetailTradePromoting retailTradePromoting = dao.getRetailTradePromotingDao().load(rtp.getID());
                    if (retailTradePromoting != null) {//查出从表
                        List<RetailTradePromotingFlow> retailTradePromotingFlows = dao.getRetailTradePromotingFlowDao().queryRaw("Where F_RetailTradePromotingID = ?", new String[]{String.valueOf(retailTradePromoting.getID())});
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
                    List<?> list = dao.getRetailTradePromotingDao().queryRaw(retailTradePromoting.getSql(), retailTradePromoting.getConditions());

                    List<BaseModel> newList = null;
                    if (list != null) {
                        newList = new ArrayList<>();
                        for (Object o : list) {
                            RetailTradePromoting rtp = (RetailTradePromoting) o;
                            BaseModel baseModel = retrieve1Sync(1, rtp);
                            newList.add(baseModel);
                        }
                    }
                    return newList;
                } catch (Exception e) {
                    log.info("执行retrieveNSync时出错，错误信息：" + e.getMessage());
                    e.printStackTrace();
                    lastErrorCode = ErrorInfo.EnumErrorCode.EC_OtherError;

                    return null;
                }
            default:
                try {
                    lastErrorCode = ErrorInfo.EnumErrorCode.EC_NoError;

                    return dao.getRetailTradePromotingDao().loadAll();
                } catch (Exception e) {
                    e.printStackTrace();
                    lastErrorCode = ErrorInfo.EnumErrorCode.EC_NoError;
                    log.info("错误为" + lastErrorCode);
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

                TaskScheduler.execute(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            createSync(iUseCaseID, bmMaster);
                            //
                            event.setLastErrorCode(ErrorInfo.EnumErrorCode.EC_NoError);

                            log.info("向本地SQLite插入主从表数据成功！RetailTradePromotingFlow=" + bmMaster.getID() + "  "
                                    + (bmMaster.getListSlave1().size() > 0 ? ((RetailTradePromotingFlow) bmMaster.getListSlave1().get(0)).getID() + "~" //打印第1个ID
                                    + ((RetailTradePromotingFlow) bmMaster.getListSlave1().get(((RetailTradePromoting) bmMaster).getListSlave1().size() - 1)).getID() : "")//打印最后1个ID
                            );
                            event.setBaseModel1(bmMaster);
                            event.setTmpMasterTableObj(bmMaster);
                        } catch (Exception e) {
                            e.printStackTrace();
                            event.setLastErrorCode(ErrorInfo.EnumErrorCode.EC_OtherError);

                            log.info("向本地SQLite插入主从表数据失败！原因：" + e.getMessage());
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
                    }
                });
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

        try {
            if (bm.getSyncDatetime() == null) {
                bm.setSyncDatetime(Constants.getDefaultSyncDatetime2());
            }
            if (((RetailTradePromoting) bm).getCreateDatetime() == null) {
                ((RetailTradePromoting) bm).setCreateDatetime(new Date(System.currentTimeMillis() + NtpHttpBO.TimeDifference));
            }
            bm.setSyncType(BasePresenter.SYNC_Type_C);
            long id = dao.getRetailTradePromotingDao().insert((RetailTradePromoting) bm);
            //
            List<RetailTradePromotingFlow> rtpfList = (List<RetailTradePromotingFlow>) bm.getListSlave1();
            if (rtpfList != null) {
                for (int i = 0; i < rtpfList.size(); i++) {
                    rtpfList.get(i).setRetailTradePromotingID((int) id);
                    GlobalController.getInstance().getRetailTradePromotingFlowPresenter().createSync(iUseCaseID, rtpfList.get(i));
                }
            }
            bm.setID(id);
            lastErrorCode = ErrorInfo.EnumErrorCode.EC_NoError;
        } catch (Exception e) {
            log.info("执行createSync时出错，错误信息：" + e.getMessage());
            e.printStackTrace();
            lastErrorCode = ErrorInfo.EnumErrorCode.EC_OtherError;
        }

        return bm;
    }

//    public boolean retrieveNByIDs(final int iUseCaseID, final List<?> bmList, final BaseSQLiteEvent event) {
//        event.setStatus(BaseEvent.EnumEventStatus.EES_SQLite_ToDo);
//        switch (iUseCaseID) {
//            default:
//                TaskScheduler.execute(new Runnable() {
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
                TaskScheduler.execute(new Runnable() {
                    @Override
                    public void run() {
                        dao.clear();
                        List<RetailTradePromoting> retailTradePromotingList = new ArrayList<RetailTradePromoting>();
                        //
                        try {
                            RetailTradePromoting retailTradePromoting = new RetailTradePromoting();
                            //找主表
                            retailTradePromoting.setSql("where F_SyncDatetime = ?");
                            retailTradePromoting.setConditions(new String[]{"0"});
                            retailTradePromotingList = dao.getRetailTradePromotingDao().queryRaw(retailTradePromoting.getSql(), retailTradePromoting.getConditions());
                            if (retailTradePromotingList != null) {
                                //找从表
                                for (int i = 0; i < retailTradePromotingList.size(); i++) {
                                    retailTradePromoting.setSql("where F_RetailTradePromotingID = ?");
                                    retailTradePromoting.setConditions(new String[]{String.valueOf(retailTradePromotingList.get(i).getID())});
                                    List<RetailTradePromotingFlow> retailTradePromotingFlowList = dao.getRetailTradePromotingFlowDao().queryRaw(retailTradePromoting.getSql(), retailTradePromoting.getConditions());
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
                });
                break;
            case BaseSQLiteBO.CASE_RetailTradePromoting_RetrieveNByConditions:
                TaskScheduler.execute(new Runnable() {
                    @Override
                    public void run() {
                        List<RetailTradePromoting> result = null;
                        try {
                            result = dao.getRetailTradePromotingDao().queryRaw(bm.getSql(), bm.getConditions());
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
                break;
        }
        return true;
    }

    @Override
    protected boolean updateNAsync(int iUseCaseID, final List<?> bmList, final BaseSQLiteEvent event) {
        log.info("正在进行RetailTradePromotingPresenter的updateNAsync，bmList=" + (bmList == null ? null : bmList.toString()));

        event.setStatus(BaseEvent.EnumEventStatus.EES_SQLite_ToDo);
        TaskScheduler.execute(new Runnable() {
            @Override
            public void run() {
                try {
                    for (int i = 0; i < bmList.size(); i++) {
                        dao.getRetailTradePromotingDao().update((RetailTradePromoting) bmList.get(i));
                        log.info("update后的RetailTradePromoting：" + ((RetailTradePromoting) bmList.get(i)).toString());
                    }
                    event.setLastErrorCode(ErrorInfo.EnumErrorCode.EC_NoError);
                } catch (Exception e) {
                    e.printStackTrace();
                    log.info("Update RetailTradePromoting 异常，异常信息为：" + e.getMessage());
                    event.setLastErrorCode(ErrorInfo.EnumErrorCode.EC_OtherError);
                }
                EventBus.getDefault().post(event);
            }
        });
        return true;
    }

    @Override
    protected BaseModel deleteNSync(int iUserCaseID, BaseModel bm) {
        log.info("正在进行RetailTradePromotingPresenter的deleteNSync，bm=" + (bm == null ? null : bm.toString()));

        try {
            dao.getRetailTradePromotingDao().deleteAll();
            lastErrorCode = ErrorInfo.EnumErrorCode.EC_NoError;
        } catch (Exception e) {
            e.printStackTrace();
            lastErrorCode = ErrorInfo.EnumErrorCode.EC_OtherError;
        }

        return null;
    }

    @Override
    protected BaseModel deleteSync(int iUseCaseID, final BaseModel bm) {
        log.info("正在进行RetailTradePromotingPresenter的deleteSync，bm=" + (bm == null ? null : bm.toString()));

        try {
            dao.getRetailTradePromotingDao().delete((RetailTradePromoting) bm);
            lastErrorCode = ErrorInfo.EnumErrorCode.EC_NoError;
        } catch (Exception e) {
            log.info("执行deleteSync时出错，错误信息：" + e.getMessage());
            e.printStackTrace();
            lastErrorCode = ErrorInfo.EnumErrorCode.EC_OtherError;
        }
        return null;
    }
}
