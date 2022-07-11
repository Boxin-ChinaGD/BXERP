package com.bx.erp.presenter;

import com.bx.erp.bo.BaseSQLiteBO;
import com.bx.erp.bo.NtpHttpBO;
import com.bx.erp.di.PerActivity;
import com.bx.erp.event.BaseEvent;
import com.bx.erp.event.UI.BaseSQLiteEvent;
import com.bx.erp.helper.Constants;
import com.bx.erp.model.BaseModel;
import com.bx.erp.model.Commodity;
import com.bx.erp.model.CommodityShopInfo;
import com.bx.erp.model.DaoSession;
import com.bx.erp.model.ErrorInfo;
import com.silencedut.taskscheduler.TaskScheduler;

import org.apache.log4j.Logger;
import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.inject.Inject;

@PerActivity
public class CommodityPresenter extends BasePresenter {
    private Logger log = Logger.getLogger(this.getClass());

    @Inject
    public CommodityPresenter(final DaoSession dao) {
        super(dao);
    }

    @Override
    protected String getTableName() {
        return dao.getCommodityDao().getTablename();
    }

    @Override
    protected List<BaseModel> createNSync(int iUseCaseID, final List<?> list) {
        log.info("正在进行CommodityPresenter的createNSync，list=" + (list == null ? null : list.toString()));

        switch (iUseCaseID) {
            default:
                try {
                    for (int i = 0; i < list.size(); i++) {
                        ((Commodity) list.get(i)).setSyncDatetime(new Date(System.currentTimeMillis() + NtpHttpBO.TimeDifference));
                        ((Commodity) list.get(i)).setSyncType(BasePresenter.SYNC_Type_C);
                        long id = dao.getCommodityDao().insert((Commodity) list.get(i));
                        ((Commodity) list.get(i)).setID(id);
                    }
                    lastErrorCode = ErrorInfo.EnumErrorCode.EC_NoError;
                } catch (Exception e) {
                    log.info("执行createNSync失败，错误信息=" + e.getMessage());
                    e.printStackTrace();
                    lastErrorCode = ErrorInfo.EnumErrorCode.EC_OtherError;
                }
                return (List<BaseModel>) list;
        }
    }

    @Override
    protected BaseModel createSync(int iUseCaseID, BaseModel bm) {
        log.info("正在进行CommodityPresenter的createSync，bm=" +(bm == null ? null : bm.toString()));

        try {
            bm.setSyncDatetime(new Date(System.currentTimeMillis() + NtpHttpBO.TimeDifference));
            bm.setSyncType(BasePresenter.SYNC_Type_C);
            long id = dao.getCommodityDao().insert((Commodity) bm);
            bm.setID(id);
            List<CommodityShopInfo> commodityShopInfoList = (List<CommodityShopInfo>) bm.getListSlave2();
            for(CommodityShopInfo commodityShopInfo : commodityShopInfoList) {
                if(commodityShopInfo.getID() != null && commodityShopInfo.getID().intValue() > 0 && dao.getCommodityShopInfoDao().load(commodityShopInfo.getID()) != null) {
                    dao.getCommodityShopInfoDao().deleteByKey(commodityShopInfo.getID());
                }
                long commShopInfoId = dao.getCommodityShopInfoDao().insert(commodityShopInfo);
                commodityShopInfo.setID(commShopInfoId);
            }
            lastErrorCode = ErrorInfo.EnumErrorCode.EC_NoError;
        } catch (Exception e) {
            log.info("执行createSync失败，错误信息=" + e.getMessage());
            e.printStackTrace();
            lastErrorCode = ErrorInfo.EnumErrorCode.EC_OtherError;
        }

        return bm;
    }

    @Override
    protected boolean updateSync(int iUseCaseID, BaseModel bm) {
        log.info("正在进行CommodityPresenter的updateSync，bm=" +(bm == null ? null : bm.toString()));

        try {
            bm.setSyncDatetime(new Date(System.currentTimeMillis() + NtpHttpBO.TimeDifference));
            bm.setSyncType(BasePresenter.SYNC_Type_U);
            dao.getCommodityDao().update((Commodity) bm);
            List<CommodityShopInfo> commodityShopInfoList = (List<CommodityShopInfo>) bm.getListSlave2();
//            if(commodityShopInfoList == null) {
//                log.error("商品门店信息为null，bm = " + bm);
//            } else {
//                for(CommodityShopInfo commodityShopInfo : commodityShopInfoList) {
//                    dao.getCommodityShopInfoDao().update(commodityShopInfo);
//                }
//            }
            for(CommodityShopInfo commodityShopInfo : commodityShopInfoList) {
                dao.getCommodityShopInfoDao().update(commodityShopInfo);
            }
            lastErrorCode = ErrorInfo.EnumErrorCode.EC_NoError;

            return true;
        } catch (Exception e) {
            log.info("执行updateSync失败，错误信息=" + e.getMessage());

            e.printStackTrace();
            lastErrorCode = ErrorInfo.EnumErrorCode.EC_OtherError;
            return false;
        }
    }

    @Override
    public BaseModel retrieve1Sync(int iUseCaseID, BaseModel bm) {
        log.info("正在进行CommodityPresenter的retrieve1Sync，bm=" +(bm == null ? null : bm.toString()));

        try {
            dao.clear();
            lastErrorCode = ErrorInfo.EnumErrorCode.EC_NoError;
            BaseModel baseModel = dao.getCommodityDao().load(bm.getID());
            if(baseModel != null) {
                String[] condition = {String.valueOf(baseModel.getID()), String.valueOf(Constants.shopID)};
                String sql = "where F_CommodityID = ? and F_ShopID = ?";
                List<CommodityShopInfo> commodityShopInfos = dao.getCommodityShopInfoDao().queryRaw(sql, condition);
                baseModel.setListSlave2(commodityShopInfos);
            }
            return baseModel;
        } catch (Exception e) {
            log.info("执行CommodityPresenter的retrieve1Sync，错误信息为" + e.getMessage());
            e.printStackTrace();
            lastErrorCode = ErrorInfo.EnumErrorCode.EC_OtherError;

            return null;
        }
    }

    @Override
    protected List<?> retrieveNSync(int iUseCaseID, BaseModel bm) {
        log.info("正在进行CommodityPresenter的retrieveNSync，bm=" +(bm == null ? null : bm.toString()));

        switch (iUseCaseID) {
            case BaseSQLiteBO.CASE_Commodity_RetrieveNByConditions:
                try {
                    lastErrorCode = ErrorInfo.EnumErrorCode.EC_NoError;

                    return dao.getCommodityDao().queryRaw(bm.getSql(), bm.getConditions());
                } catch (Exception e) {
                    log.info("执行retrieveNSync的CASE_Commodity_RetrieveNByConditions分支时失败，错误信息为" + e.getMessage());
                    e.printStackTrace();
                    lastErrorCode = ErrorInfo.EnumErrorCode.EC_OtherError;

                    return null;
                }
            default:
                try {
                    lastErrorCode = ErrorInfo.EnumErrorCode.EC_NoError;

                    return dao.getCommodityDao().loadAll();
                } catch (Exception e) {
                    log.info("执行retrieveNSync时失败，错误信息为" + e.getMessage());
                    e.printStackTrace();
                    lastErrorCode = ErrorInfo.EnumErrorCode.EC_OtherError;

                    return null;
                }
        }
    }

    @Override
    protected BaseModel deleteSync(int iUseCaseID, BaseModel bm) {
        log.info("正在进行CommodityPresenter的deleteSync，bm=" +(bm == null ? null : bm.toString()));

        try {
            dao.getCommodityDao().deleteByKey(bm.getID());
            String sql = "delete from " + dao.getCommodityShopInfoDao().getTablename() + " where F_CommodityID = ? ";//
            String[] conditions = new String[]{String.valueOf(bm.getID())};
            dao.getDatabase().execSQL(sql, conditions);
            lastErrorCode = ErrorInfo.EnumErrorCode.EC_NoError;
        } catch (Exception e) {
            log.info("执行deleteSync时失败，错误信息为" + e.getMessage());
            e.printStackTrace();
            lastErrorCode = ErrorInfo.EnumErrorCode.EC_OtherError;
        }

        return null;
    }

    @Override
    protected boolean createNAsync(int iUseCaseID, final List<?> list, final BaseSQLiteEvent event) {
        log.info("正在进行CommodityPresenter的createNAsync，list=" + (list == null ? null : list.toString()));

        switch (iUseCaseID) {
            default:
                TaskScheduler.execute(new Runnable() {
                    @Override
                    public void run() {
//                        event.setEventTypeSQLite(BaseSQLiteEvent.EnumSQLiteEventType.ESET_Commodity_CreateNAsync);
                        //
                        try {
                            for (int i = 0; i < list.size(); i++) {
                                ((Commodity) list.get(i)).setSyncDatetime(new Date(System.currentTimeMillis() + NtpHttpBO.TimeDifference));
                                ((Commodity) list.get(i)).setSyncType(BasePresenter.SYNC_Type_C);
                                long id = dao.getCommodityDao().insert((Commodity) list.get(i));//...插入一部分失败
                                ((Commodity) list.get(i)).setID(id);
                            }
                            event.setLastErrorCode(ErrorInfo.EnumErrorCode.EC_NoError);
                        } catch (Exception e) {
                            log.info("创建Commodity时出错，错误信息：" + e.getMessage());
                            event.setLastErrorCode(ErrorInfo.EnumErrorCode.EC_OtherError);
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
    protected boolean retrieveNAsync(int iUseCaseID, final BaseModel bm, final BaseSQLiteEvent event) {
        log.info("正在进行CommodityPresenter的retrieveNAsync，bm=" +(bm == null ? null : bm.toString()));

        switch (iUseCaseID) {
            case BaseSQLiteBO.CASE_Commodity_RetrieveNByConditions:
                TaskScheduler.execute(new Runnable() {
                    @Override
                    public void run() {
                        event.setEventProcessed(false);
                        List<Commodity> result = null;
                        //
                        try {
                            result = dao.getCommodityDao().queryRaw(bm.getSql(), bm.getConditions());
                            event.setLastErrorCode(ErrorInfo.EnumErrorCode.EC_NoError);
                        } catch (Exception e) {
                            log.info("RetrieveNAsync Commodity出错，错误信息：" + e.getMessage());
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
                        List<Commodity> result = null;
//                        event.setEventTypeSQLite(BaseSQLiteEvent.EnumSQLiteEventType.ESET_Commodity_RetrieveNAsync);
                        //
                        try {
                            result = dao.getCommodityDao().loadAll();//...
                            event.setLastErrorCode(ErrorInfo.EnumErrorCode.EC_NoError);
                        } catch (Exception e) {
                            log.info("查询SQLite的表Commodity的所有记录时出错，错误信息：" + e.getMessage());
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
    protected boolean createAsync(int iUseCaseID, final BaseModel bm, final BaseSQLiteEvent event) {
        log.info("正在进行CommodityPresenter的createAsync，bm=" +(bm == null ? null : bm.toString()));

        switch (iUseCaseID) {
            default:
                TaskScheduler.execute(new Runnable() {
                    @Override
                    public void run() {
//                        event.setEventTypeSQLite(BaseSQLiteEvent.EnumSQLiteEventType.ESET_Commodity_CreateAsync);
                        //
                        try {
                            bm.setSyncDatetime(new Date(System.currentTimeMillis() + NtpHttpBO.TimeDifference));
                            bm.setSyncType(BasePresenter.SYNC_Type_C);
                            long id = dao.getCommodityDao().insert((Commodity) bm);
                            ((Commodity) bm).setID(id);
                            event.setLastErrorCode(ErrorInfo.EnumErrorCode.EC_NoError);
                        } catch (Exception e) {
                            log.info("创建Commodity时出错，错误信息：" + e.getMessage());
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
        log.info("正在进行CommodityPresenter的retrieve1Async，bm=" +(bm == null ? null : bm.toString()));

        switch (iUseCaseID) {
            default:
                TaskScheduler.execute(new Runnable() {
                    @Override
                    public void run() {
                        Commodity result = null;
//                        event.setEventTypeSQLite(BaseSQLiteEvent.EnumSQLiteEventType.ESET_Commodity_Retrieve1Async);
                        //
                        try {
                            result = dao.getCommodityDao().load(((Commodity) bm).getID());//...
                            event.setLastErrorCode(ErrorInfo.EnumErrorCode.EC_NoError);
                        } catch (Exception e) {
                            log.info("Retrieve1 Commodity时出错，错误信息：" + e.getMessage());
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
    protected boolean updateAsync(int iUseCaseID, final BaseModel bm, final BaseSQLiteEvent event) {
        log.info("正在进行CommodityPresenter的updateAsync，bm=" +(bm == null ? null : bm.toString()));

        switch (iUseCaseID) {
            default:
                TaskScheduler.execute(new Runnable() {
                    @Override
                    public void run() {
//                        event.setEventTypeSQLite(BaseSQLiteEvent.EnumSQLiteEventType.ESET_Commodity_UpdateAsync);
                        //
                        try {
                            bm.setSyncDatetime(new Date(System.currentTimeMillis() + NtpHttpBO.TimeDifference));
                            bm.setSyncType(BasePresenter.SYNC_Type_U);
                            dao.getCommodityDao().update((Commodity) bm);
                            event.setLastErrorCode(ErrorInfo.EnumErrorCode.EC_NoError);
                        } catch (Exception e) {
                            log.info("更新Commodity时出错，错误信息：" + e.getMessage());
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
    protected boolean refreshByServerDataAsync(final int iUseCaseID, final List<?> bmNewList, final BaseSQLiteEvent event) {
        log.info("正在进行CommodityPresenter的refreshByServerDataAsync，bmNewList=" + (bmNewList == null ? null : bmNewList.toString()));

        event.setStatus(BaseEvent.EnumEventStatus.EES_SQLite_ToApplyServerData);
        switch (iUseCaseID) {
            default:
                TaskScheduler.execute(new Runnable() {
                    @Override
                    public void run() {
                        log.info("已经得到服务器返回的需要同步到SQLite的Commodity的所有数据, 准备进行同步...");
                        List<BaseModel> bmList = (List<BaseModel>) bmNewList;
                        try {
                            if (bmList != null) {
                                for (int i = 0; i < bmList.size(); i++) {
                                    String commodityType = bmList.get(i).getSyncType();

                                    if (BasePresenter.SYNC_Type_C.equals(commodityType)) {//假如返回的数据中String1 == CommodityCategory , 在SQLite对Commodity进行查找,如果不存在就进行create操作,否则先删除再Create
                                        Commodity commodity = (Commodity) retrieve1Sync(BaseSQLiteBO.INVALID_CASE_ID, bmList.get(i));
                                        if (commodity != null) {
                                            deleteSync(BaseSQLiteBO.INVALID_CASE_ID, commodity);
                                        }
                                        //
                                        bmList.get(i).setSyncDatetime(new Date(System.currentTimeMillis() + NtpHttpBO.TimeDifference));
                                        bmList.get(i).setSyncType(BasePresenter.SYNC_Type_C);
                                        dao.getCommodityDao().insert((Commodity) bmList.get(i));
                                        List<CommodityShopInfo> commodityShopInfoList = (List<CommodityShopInfo>) bmList.get(i).getListSlave2();
                                        for(CommodityShopInfo commodityShopInfo : commodityShopInfoList) {
                                            if(commodityShopInfo.getID() != null && commodityShopInfo.getID().intValue() > 0 && dao.getCommodityShopInfoDao().load(commodityShopInfo.getID()) != null) {
                                                dao.getCommodityShopInfoDao().deleteByKey(commodityShopInfo.getID());
                                            }
                                            dao.getCommodityShopInfoDao().insert(commodityShopInfo);
                                        }
                                        log.info("服务器返回的C型数据成功同步到本地SQLite中!");
                                    } else if (BasePresenter.SYNC_Type_U.equals(commodityType)) {//假如返回的string1==U,则修改SQLite中对应的数据
                                        updateSync(iUseCaseID, bmList.get(i));

                                        log.info("服务器返回的U型数据成功同步到本地SQLite中!");
                                    } else if (BasePresenter.SYNC_Type_D.equals(commodityType)) {//假如返回的string1==D,则删除SQLite中对应的数据
                                        deleteSync(iUseCaseID, bmList.get(i));

                                        log.info("服务器返回的D型数据成功同步到本地SQLite中!");
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
//                        event.setStatus(BaseEvent.EnumEventStatus.EES_SQLite_DoneApplyServerData);
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
        log.info("正在进行CommodityPresenter的refreshByServerDataAsyncC，bmNewList=" + (bmNewList == null ? null : bmNewList.toString()));

        event.setStatus(BaseEvent.EnumEventStatus.EES_SQLite_ToApplyServerData);
        switch (iUseCaseID) {
            default:
                TaskScheduler.execute(new Runnable() {
                    @Override
                    public void run() {
                        log.info("已经得到服务器返回的commodity数据，准备进行同步！");

                        event.setLastErrorCode(ErrorInfo.EnumErrorCode.EC_NoError);
//                        event.setEventTypeSQLite(BaseSQLiteEvent.EnumSQLiteEventType.ESET_Commodity_RefreshByServerDataAsyncC_Done);
                        try {
                            List<Commodity> commodityList = (List<Commodity>) bmNewList;
                            //根据返回的数据，查找本地SQLite中是否存在该条数据，如果存在就删除再插入，否则就直接插入
                            if (commodityList != null) {
                                if (event.getPageIndex() != null && event.getPageIndex().equals(Commodity.PAGEINDEX_START)) {
                                    String sql = "delete from " + getTableName() + " where F_ID > ? ";//
                                    String[] conditions = new String[]{String.valueOf(commodityList.get(0).getID())};
                                    dao.getDatabase().execSQL(sql, conditions);
                                } else if (event.getPageIndex() != null && event.getPageIndex().equals(Commodity.PAGEINDEX_END)) {
                                    String sql = "delete from " + getTableName() + " where F_ID < ? ";
                                    String[] conditions = new String[]{String.valueOf(commodityList.get(commodityList.size() - 1).getID())};
                                    dao.getDatabase().execSQL(sql, conditions);
                                }

                                event.setPageIndex(""); //初始化分页标记
                                String sql = "delete from " + getTableName() + " where F_ID >= ? and F_ID <= ?";
                                String[] conditions = new String[]{String.valueOf(commodityList.get(commodityList.size() - 1).getID()), String.valueOf(commodityList.get(0).getID())};

                                //先删除本地数据库原有的数据
                                dao.getDatabase().execSQL(sql, conditions);
                                // 删除从表
                                for(Commodity commodity : commodityList) {
                                    String sqlCommShopInfo = "delete from " + dao.getCommodityShopInfoDao().getTablename() + " where F_CommodityID = ? ";//
                                    String[] conditionsCommShopInfo = new String[]{String.valueOf(commodity.getID())};
                                    dao.getDatabase().execSQL(sqlCommShopInfo, conditionsCommShopInfo);
                                }

                                for (int i = 0; i < commodityList.size(); i++) {
                                    dao.getCommodityDao().insert((Commodity) commodityList.get(i));
                                    List<CommodityShopInfo> commodityShopInfoList = (List<CommodityShopInfo>) commodityList.get(i).getListSlave2();
                                    for(CommodityShopInfo commodityShopInfo : commodityShopInfoList) {
                                        if(commodityShopInfo.getID() != null && commodityShopInfo.getID().intValue() > 0 && dao.getCommodityShopInfoDao().load(commodityShopInfo.getID()) != null) {
                                            dao.getCommodityShopInfoDao().deleteByKey(commodityShopInfo.getID());
                                        }
                                        dao.getCommodityShopInfoDao().insert(commodityShopInfo);
                                    }
                                }
                            }
                            event.setListMasterTable(bmNewList);
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
    protected boolean updateNAsync(int iUseCaseID, final List<?> bmList, final BaseSQLiteEvent event) {
        log.info("正在进行CommodityPresenter的updateNAsync，bmList=" + (bmList == null ? null : bmList.toString()));

        event.setStatus(BaseEvent.EnumEventStatus.EES_SQLite_ToApplyServerData);
        switch (iUseCaseID) {
            default:
                TaskScheduler.execute(new Runnable() {
                    @Override
                    public void run() {
                        log.info("获取到服务器返回的条形码的商品,准备进行更新库存...");

                        event.setLastErrorCode(ErrorInfo.EnumErrorCode.EC_NoError);
//                        event.setEventTypeSQLite(BaseSQLiteEvent.EnumSQLiteEventType.ESET_Commodity_UpdateNAsync);
                        List<Commodity> commodityList = new ArrayList<Commodity>();
                        if (bmList != null) {
                            //获取到返回的商品的ID和库存，找到SQLite中对应的数据，更新库存
                            try {
                                List<Commodity> list = (List<Commodity>) bmList;
                                for (int i = 0; i < list.size(); i++) {
                                    Commodity commodity = dao.getCommodityDao().load(list.get(i).getID());
                                    if (commodity != null){
                                        commodity.setNO((list.get(i)).getNO());
                                        commodity.setBarcode(list.get(i).getBarcode());
                                        dao.getCommodityDao().update(commodity);
                                        commodityList.add(commodity);
                                    } else {
                                        log.info("无法更新该库存，本地中没有该商品：" + list.get(i));
                                        event.setLastErrorMessage("本地没有数据。你可以尝试同步服务器的数据后再搜索");//...TODO
                                    }
                                }
                            } catch (Exception e) {
                                log.info("执行updateNAsync失败，错误信息为" + e.getMessage());
                                e.printStackTrace();
                                event.setLastErrorCode(ErrorInfo.EnumErrorCode.EC_OtherError);
                            }
                        }
                        event.setListMasterTable(commodityList);
                        //
                        EventBus.getDefault().post(event);
                    }
                });
                break;
        }
        return true;
    }
}