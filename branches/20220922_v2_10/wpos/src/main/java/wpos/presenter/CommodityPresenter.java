package wpos.presenter;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Component;
import wpos.dao.CommodityShopInfoMapper;
import wpos.helper.Constants;
import wpos.model.promotion.Promotion;
import wpos.utils.EventBus;
import wpos.bo.BaseSQLiteBO;
import wpos.bo.NtpHttpBO;
import wpos.dao.CommodityMapper;
import wpos.event.BaseEvent;
import wpos.event.UI.BaseSQLiteEvent;
import wpos.model.*;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.annotation.Resource;
import javax.persistence.Query;
import javax.persistence.Table;

@Component("commodityPresenter")
public class CommodityPresenter extends BasePresenter {
    private Log log = LogFactory.getLog(this.getClass());

    @Resource
    protected CommodityMapper commodityMapper;

    @Resource
    protected CommodityShopInfoMapper commodityShopInfoMapper;

//    protected static ReentrantReadWriteLock lock = new ReentrantReadWriteLock();

//    public CommodityPresenter(final CommodityMapper commodityMapper) {
//        super(commodityMapper);
//    }

    public static final String QUERY_Commodity_TABLE = "SELECT F_ID,F_Status,F_Name,F_ShortName,F_Specification,F_PackageUnitID,F_PurchasingUnit," +
            "F_BrandID,F_CategoryID,F_MnemonicCode,F_PricingType,F_LatestPricePurchase,F_PriceRetail,F_PriceVIP,F_PriceWholesale," +
            "F_CanChangePrice,F_RuleOfPoint,F_Picture,F_ShelfLife,F_ReturnDays,F_CreateDate,F_PurchaseFlag,F_RefCommodityID," +
            "F_RefCommodityMultiple,F_Tag,F_NO,F_Type,F_NOStart,F_PurchasingPriceStart,F_StartValueRemark,F_CreateDatetime," +
            "F_UpdateDatetime,F_PropertyValue1,F_PropertyValue2,F_PropertyValue3,F_PropertyValue4,F_CurrentWarehousingID," +
            "F_SyncType,F_SyncDatetime" +
            "  FROM T_Commodity ";

    public static String Query_CommodityShopInfo_TABLE = "select F_ID,F_CommodityID,F_ShopID,F_LatestPricePurchase,F_PriceRetail,F_NO,F_NOStart,F_PurchasingPriceStart,F_CurrentWarehousingID from T_CommodityShopInfo ";

    @Override
    public void createTableSync() {
        commodityMapper.createTable();
    }

    @Override
    protected String getTableName() {
        if (Commodity.class.isAnnotationPresent(Table.class)) {
            Table annotation = Commodity.class.getAnnotation(Table.class);
            return annotation.name();
        } else {
            log.error("无法获取表名，未给" + Commodity.class.getSimpleName() + "添加@Table。");
        }
        return "";
    }

    @Override
    protected List<BaseModel> createNSync(int iUseCaseID, final List<?> list) {
        log.info("正在进行CommodityPresenter的createNSync，list=" + (list == null ? null : list.toString()));

        switch (iUseCaseID) {
            default:
                globalWriteLock.writeLock().lock();
                List<BaseModel> baseModels = new ArrayList<BaseModel>();
                try {
                    for (int i = 0; i < list.size(); i++) {
                        ((Commodity) list.get(i)).setSyncDatetime(new Date(System.currentTimeMillis() + NtpHttpBO.TimeDifference));
                        ((Commodity) list.get(i)).setSyncType(BasePresenter.SYNC_Type_C);
                        BaseModel b = createSync(iUseCaseID, (BaseModel) list.get(i));
                        if (b != null) {
                            baseModels.add(b);
                        }
                    }
                    if (baseModels.size() == list.size()) {
                        lastErrorCode = ErrorInfo.EnumErrorCode.EC_NoError;
                    } else {
                        lastErrorCode = ErrorInfo.EnumErrorCode.EC_PartSuccess;
                    }
                } catch (Exception e) {
                    log.info("执行createNSync失败，错误信息=" + e.getMessage());
                    e.printStackTrace();
                    lastErrorCode = ErrorInfo.EnumErrorCode.EC_OtherError;
                }
                globalWriteLock.writeLock().unlock();
                return baseModels;
        }
    }

    @Override
    protected BaseModel createSync(int iUseCaseID, BaseModel bm) {
        log.info("正在进行CommodityPresenter的createSync，bm=" + (bm == null ? null : bm.toString()));

        globalWriteLock.writeLock().lock();

        BaseModel commodity = null;
        try {
            bm.setSyncDatetime(new Date(System.currentTimeMillis() + NtpHttpBO.TimeDifference));
            bm.setSyncType(BasePresenter.SYNC_Type_C);
            commodityMapper.create((Commodity) bm);

            String sql;
            if (bm.getID() != null) {
                sql = "WHERE F_ID = " + bm.getID();
            } else {
                sql = "WHERE F_ID = (SELECT MAX(F_ID) FROM T_Commodity)";
            }
            Query query = entityManager.createNativeQuery(QUERY_Commodity_TABLE + sql, Commodity.class);
            List<Commodity> commodities = query.getResultList();
            if (commodities != null && commodities.size() > 0) {
                lastErrorCode = ErrorInfo.EnumErrorCode.EC_NoError;
                commodity = commodities.get(0);
                List<CommodityShopInfo> commodityShopInfoList = (List<CommodityShopInfo>) bm.getListSlave2();
                for(CommodityShopInfo commodityShopInfo : commodityShopInfoList) {
                    commodityShopInfoMapper.create(commodityShopInfo);
                }
            } else {
                lastErrorCode = ErrorInfo.EnumErrorCode.EC_OtherError;
            }
        } catch (Exception e) {
            log.error("执行createSync失败，错误信息=" + e.getMessage());
            e.printStackTrace();
            lastErrorCode = ErrorInfo.EnumErrorCode.EC_OtherError;
        }
        globalWriteLock.writeLock().unlock();

        return commodity;
    }

    @Override
    protected boolean updateSync(int iUseCaseID, BaseModel bm) {
        log.info("正在进行CommodityPresenter的updateSync，bm=" + (bm == null ? null : bm.toString()));
        globalWriteLock.writeLock().lock();
        try {
            bm.setSyncDatetime(new Date(System.currentTimeMillis() + NtpHttpBO.TimeDifference));
            bm.setSyncType(BasePresenter.SYNC_Type_U);
            commodityMapper.save((Commodity) bm);
            List<CommodityShopInfo> commodityShopInfoList = (List<CommodityShopInfo>) bm.getListSlave2();
            for(CommodityShopInfo commodityShopInfo : commodityShopInfoList) {
                commodityShopInfoMapper.save(commodityShopInfo);
            }
            lastErrorCode = ErrorInfo.EnumErrorCode.EC_NoError;
            globalWriteLock.writeLock().unlock();
            return true;
        } catch (Exception e) {
            log.error("执行updateSync失败，错误信息=" + e.getMessage());

            e.printStackTrace();
            lastErrorCode = ErrorInfo.EnumErrorCode.EC_OtherError;
            globalWriteLock.writeLock().unlock();
            return false;
        }
    }

    @Override
    public BaseModel retrieve1Sync(int iUseCaseID, BaseModel bm) {
        log.info("正在进行CommodityPresenter的retrieve1Sync，bm=" + (bm == null ? null : bm.toString()));

        try {
            lastErrorCode = ErrorInfo.EnumErrorCode.EC_NoError;
            BaseModel baseModel = commodityMapper.findOne(bm.getID());
            if(baseModel != null) {
                String[] condition = {bm.getID() + "", Constants.shopID + ""};
                String sql = String.format("where F_CommodityID = '%s' and F_ShopID = '%s'", condition);
                Query query = entityManager.createNativeQuery(Query_CommodityShopInfo_TABLE + sql, CommodityShopInfo.class);
                baseModel.setListSlave2(query.getResultList());
            }
            return baseModel;
        } catch (Exception e) {
            log.error("执行CommodityPresenter的retrieve1Sync，错误信息为" + e.getMessage());
            e.printStackTrace();
            lastErrorCode = ErrorInfo.EnumErrorCode.EC_OtherError;

            return null;
        }
    }

    @Override
    protected List<?> retrieveNSync(int iUseCaseID, BaseModel bm) {
        log.info("正在进行CommodityPresenter的retrieveNSync，bm=" + (bm == null ? null : bm.toString()));

        switch (iUseCaseID) {
            case BaseSQLiteBO.CASE_Commodity_RetrieveNByConditions:
                try {
                    lastErrorCode = ErrorInfo.EnumErrorCode.EC_NoError;
                    String sql = String.format(bm.getSql(), bm.getConditions());
                    Query query = entityManager.createNativeQuery(QUERY_Commodity_TABLE + sql, Commodity.class);
                    return query.getResultList();
                } catch (Exception e) {
                    log.error("执行retrieveNSync的CASE_Commodity_RetrieveNByConditions分支时失败，错误信息为" + e.getMessage());
                    e.printStackTrace();
                    lastErrorCode = ErrorInfo.EnumErrorCode.EC_OtherError;

                    return null;
                }
            default:
                try {
                    lastErrorCode = ErrorInfo.EnumErrorCode.EC_NoError;

                    return commodityMapper.findAll();
                } catch (Exception e) {
                    log.error("执行retrieveNSync时失败，错误信息为" + e.getMessage());
                    e.printStackTrace();
                    lastErrorCode = ErrorInfo.EnumErrorCode.EC_OtherError;

                    return null;
                }
        }
    }

    @Override
    protected BaseModel deleteSync(int iUseCaseID, BaseModel bm) {
        log.info("正在进行CommodityPresenter的deleteSync，bm=" + (bm == null ? null : bm.toString()));
        globalWriteLock.writeLock().lock();
        try {
            commodityMapper.delete(bm.getID());
            commodityShopInfoMapper.deleteByCommodityID(bm.getID());
            lastErrorCode = ErrorInfo.EnumErrorCode.EC_NoError;
        } catch (Exception e) {
            log.error("执行deleteSync时失败，错误信息为" + e.getMessage());
            e.printStackTrace();
            lastErrorCode = ErrorInfo.EnumErrorCode.EC_OtherError;
        }
        globalWriteLock.writeLock().unlock();
        return null;
    }

    @Override
    protected boolean createNAsync(int iUseCaseID, final List<?> list, final BaseSQLiteEvent event) {
        log.info("正在进行CommodityPresenter的createNAsync，list=" + (list == null ? null : list.toString()));

        switch (iUseCaseID) {
            default:
                new Thread() {
                    @Override
                    public void run() {
//                        event.setEventTypeSQLite(BaseSQLiteEvent.EnumSQLiteEventType.ESET_Commodity_CreateNAsync);
                        //
                        globalWriteLock.writeLock().lock();
                        List<BaseModel> baseModels = new ArrayList<BaseModel>();
                        try {
                            for (int i = 0; i < list.size(); i++) {
                                ((Commodity) list.get(i)).setSyncDatetime(new Date(System.currentTimeMillis() + NtpHttpBO.TimeDifference));
                                ((Commodity) list.get(i)).setSyncType(BasePresenter.SYNC_Type_C);
                                BaseModel baseModel = createSync(iUseCaseID, ((Commodity) list.get(i)));
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
                            log.error("创建Commodity时出错，错误信息：" + e.getMessage());
                            event.setLastErrorCode(ErrorInfo.EnumErrorCode.EC_OtherError);
                        }
                        //
                        event.setListMasterTable(baseModels);
                        globalWriteLock.writeLock().unlock();
                        //
                        EventBus.getDefault().post(event);
                    }
                }.start();
                break;
        }

        return true;
    }

    @Override
    protected boolean retrieveNAsync(int iUseCaseID, final BaseModel bm, final BaseSQLiteEvent event) {
        log.info("正在进行CommodityPresenter的retrieveNAsync，bm=" + (bm == null ? null : bm.toString()));

        switch (iUseCaseID) {
            case BaseSQLiteBO.CASE_Commodity_RetrieveNByConditions:
                new Thread() {
                    @Override
                    public void run() {
                        event.setEventProcessed(false);
                        List<Commodity> result = null;
                        //
                        try {
                            String sql = String.format(bm.getSql(), bm.getConditions());
                            Query query = entityManager.createNativeQuery(QUERY_Commodity_TABLE + sql, Commodity.class);
                            result = query.getResultList();
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
                }.start();
                break;
            default:
                new Thread() {
                    @Override
                    public void run() {
                        List<Commodity> result = null;
//                        event.setEventTypeSQLite(BaseSQLiteEvent.EnumSQLiteEventType.ESET_Commodity_RetrieveNAsync);
                        //
                        try {
                            result = commodityMapper.findAll();//...
                            event.setLastErrorCode(ErrorInfo.EnumErrorCode.EC_NoError);
                        } catch (Exception e) {
                            log.error("查询SQLite的表Commodity的所有记录时出错，错误信息：" + e.getMessage());
                            event.setLastErrorCode(ErrorInfo.EnumErrorCode.EC_OtherError);
                        }
                        //
                        event.setListMasterTable(result);
                        //
                        EventBus.getDefault().post(event);
                    }
                }.start();
                break;
        }

        return true;
    }

    @Override
    protected boolean createAsync(int iUseCaseID, final BaseModel bm, final BaseSQLiteEvent event) {
        log.info("正在进行CommodityPresenter的createAsync，bm=" + (bm == null ? null : bm.toString()));

        switch (iUseCaseID) {
            default:
                (new Thread() {
                    @Override
                    public void run() {
//                        event.setEventTypeSQLite(BaseSQLiteEvent.EnumSQLiteEventType.ESET_Commodity_CreateAsync);
                        //
                        globalWriteLock.writeLock().lock();
                        BaseModel baseModel = null;
                        try {
                            bm.setSyncDatetime(new Date(System.currentTimeMillis() + NtpHttpBO.TimeDifference));
                            bm.setSyncType(BasePresenter.SYNC_Type_C);
                            baseModel = createSync(iUseCaseID, bm);
                            event.setLastErrorCode(lastErrorCode);
                        } catch (Exception e) {
                            log.error("创建Commodity时出错，错误信息：" + e.getMessage());
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
    protected boolean retrieve1Async(int iUseCaseID, final BaseModel bm, final BaseSQLiteEvent event) {
        log.info("正在进行CommodityPresenter的retrieve1Async，bm=" + (bm == null ? null : bm.toString()));

        switch (iUseCaseID) {
            default:
                (new Thread() {
                    @Override
                    public void run() {
                        Commodity result = null;
//                        event.setEventTypeSQLite(BaseSQLiteEvent.EnumSQLiteEventType.ESET_Commodity_Retrieve1Async);
                        //
                        try {
                            result = (Commodity) commodityMapper.findOne(bm.getID());//...
                            event.setLastErrorCode(ErrorInfo.EnumErrorCode.EC_NoError);
                        } catch (Exception e) {
                            log.error("Retrieve1 Commodity时出错，错误信息：" + e.getMessage());
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
    protected boolean updateAsync(int iUseCaseID, final BaseModel bm, final BaseSQLiteEvent event) {
        log.info("正在进行CommodityPresenter的updateAsync，bm=" + (bm == null ? null : bm.toString()));

        switch (iUseCaseID) {
            default:
                (new Thread() {
                    @Override
                    public void run() {
//                        event.setEventTypeSQLite(BaseSQLiteEvent.EnumSQLiteEventType.ESET_Commodity_UpdateAsync);
                        //
                        globalWriteLock.writeLock().lock();
                        try {
                            bm.setSyncDatetime(new Date(System.currentTimeMillis() + NtpHttpBO.TimeDifference));
                            bm.setSyncType(BasePresenter.SYNC_Type_U);
                            commodityMapper.save((Commodity) bm);
                            event.setLastErrorCode(ErrorInfo.EnumErrorCode.EC_NoError);
                        } catch (Exception e) {
                            log.error("更新Commodity时出错，错误信息：" + e.getMessage());
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
    protected boolean refreshByServerDataAsync(final int iUseCaseID, final List<?> bmNewList, final BaseSQLiteEvent event) {
        log.info("正在进行CommodityPresenter的refreshByServerDataAsync，bmNewList=" + (bmNewList == null ? null : bmNewList.toString()));

        event.setStatus(BaseEvent.EnumEventStatus.EES_SQLite_ToApplyServerData);
        switch (iUseCaseID) {
            default:
                (new Thread() {
                    @Override
                    public void run() {
                        log.info("已经得到服务器返回的需要同步到SQLite的Commodity的所有数据, 准备进行同步...");
                        globalWriteLock.writeLock().lock();
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
                                        createSync(iUseCaseID, (Commodity) bmList.get(i));

                                        log.info("服务器返回的C型数据成功同步到本地SQLite中!");
                                    } else if (BasePresenter.SYNC_Type_U.equals(commodityType)) {//假如返回的string1==U,则修改SQLite中对应的数据
                                        updateSync(iUseCaseID, bmList.get(i));

                                        log.info("服务器返回的U型数据成功同步到本地SQLite中!");
                                    } else if (BasePresenter.SYNC_Type_D.equals(commodityType)) {//假如返回的string1==D,则删除SQLite中对应的数据
                                        deleteSync(iUseCaseID, bmList.get(i));

                                        log.info("服务器返回的D型数据成功同步到本地SQLite中!");
                                    }
                                    event.setLastErrorCode(lastErrorCode);
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
    protected boolean refreshByServerDataAsyncC(final int iUseCaseID, final List<?> bmNewList, final BaseSQLiteEvent event) {
        log.info("正在进行CommodityPresenter的refreshByServerDataAsyncC，bmNewList=" + (bmNewList == null ? null : bmNewList.toString()));

        event.setStatus(BaseEvent.EnumEventStatus.EES_SQLite_ToApplyServerData);
        switch (iUseCaseID) {
            default:
                (new Thread() {
                    @Override
                    public void run() {
                        log.info("已经得到服务器返回的commodity数据，准备进行同步！");
                        globalWriteLock.writeLock().lock();
                        event.setLastErrorCode(ErrorInfo.EnumErrorCode.EC_NoError);
//                        event.setEventTypeSQLite(BaseSQLiteEvent.EnumSQLiteEventType.ESET_Commodity_RefreshByServerDataAsyncC_Done);
                        try {
                            List<Commodity> commodityList = (List<Commodity>) bmNewList;
                            //根据返回的数据，查找本地SQLite中是否存在该条数据，如果存在就删除再插入，否则就直接插入
                            if (commodityList != null) {
                                if (event.getPageIndex() != null && event.getPageIndex().equals(Commodity.PAGEINDEX_START)) {
//                                    String sql = "delete from " + getTableName() + " where F_ID > %s ";//
//                                    String[] conditions = new String[]{String.valueOf(commodityList.get(0).getID())};
//                                    entityManager.createNativeQuery(String.format(sql, conditions), Commodity.class);
                                    commodityMapper.deleteBiggerIDs(commodityList.get(0).getID());
                                } else if (event.getPageIndex() != null && event.getPageIndex().equals(Commodity.PAGEINDEX_END)) {
//                                    String sql = "delete from " + getTableName() + " where F_ID < %s ";
//                                    String[] conditions = new String[]{String.valueOf(commodityList.get(commodityList.size() - 1).getID())};
//                                    entityManager.createNativeQuery(String.format(sql, conditions), Commodity.class);
                                    commodityMapper.deleteSmallerIDs(commodityList.get(commodityList.size() - 1).getID());
                                }

                                event.setPageIndex(""); //初始化分页标记
                                String sql = "delete from " + getTableName() + " where F_ID >= %s and F_ID <= %s";
//                                String sql = "select * from " + getTableName() + " where F_ID >= %s and F_ID <= %s";
                                String[] conditions = new String[]{String.valueOf(commodityList.get(commodityList.size() - 1).getID()), String.valueOf(commodityList.get(0).getID())};
                                String a = String.format(sql, conditions);
                                //先删除本地数据库原有的数据
//                                Query query = entityManager.createNativeQuery(String.format(sql, conditions), Commodity.class);
//                                query.executeUpdate();
                                // TODO 目前先使用这种方法删除数据
                                commodityMapper.deleteByRangeID(commodityList.get(commodityList.size() - 1).getID(), commodityList.get(0).getID());
                                for(Commodity commodity : commodityList) {
                                    commodityShopInfoMapper.deleteByCommodityID(commodity.getID());
                                }

                                createNSync(iUseCaseID, bmNewList);
                                event.setLastErrorCode(ErrorInfo.EnumErrorCode.EC_NoError);
                            }
                            event.setListMasterTable(bmNewList);
                        } catch (Exception e) {
                            e.printStackTrace();
                            event.setLastErrorCode(ErrorInfo.EnumErrorCode.EC_OtherError);
                        }
                        event.setStatus(BaseEvent.EnumEventStatus.EES_SQLite_DoneApplyServerData);
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
    protected boolean updateNAsync(int iUseCaseID, final List<?> bmList, final BaseSQLiteEvent event) {
        log.info("正在进行CommodityPresenter的updateNAsync，bmList=" + (bmList == null ? null : bmList.toString()));

        event.setStatus(BaseEvent.EnumEventStatus.EES_SQLite_ToApplyServerData);
        switch (iUseCaseID) {
            default:
                (new Thread() {
                    @Override
                    public void run() {
                        globalWriteLock.writeLock().lock();
                        log.info("获取到服务器返回的条形码的商品,准备进行更新库存...");

                        event.setLastErrorCode(ErrorInfo.EnumErrorCode.EC_NoError);
//                        event.setEventTypeSQLite(BaseSQLiteEvent.EnumSQLiteEventType.ESET_Commodity_UpdateNAsync);
                        List<Commodity> commodityList = new ArrayList<Commodity>();
                        if (bmList != null) {
                            //获取到返回的商品的ID和库存，找到SQLite中对应的数据，更新库存
                            try {
                                List<Commodity> list = (List<Commodity>) bmList;
                                for (int i = 0; i < list.size(); i++) {
                                    Commodity commodity = (Commodity) commodityMapper.findOne(list.get(i).getID());
                                    if (commodity != null) {
                                        commodity.setNO((list.get(i)).getNO());
                                        commodity.setBarcode(list.get(i).getBarcode());
                                        commodityMapper.save(commodity);
                                        commodityList.add(commodity);
                                    } else {
                                        log.info("无法更新该库存，本地中没有该商品：" + list.get(i));
                                        event.setLastErrorMessage("本地没有数据。你可以尝试同步服务器的数据后再搜索");//...TODO
                                    }
                                }
                            } catch (Exception e) {
                                log.error("执行updateNAsync失败，错误信息为" + e.getMessage());
                                e.printStackTrace();
                                event.setLastErrorCode(ErrorInfo.EnumErrorCode.EC_OtherError);
                            }
                        }
                        event.setListMasterTable(commodityList);
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
    protected String getQueryTable() {
        return QUERY_Commodity_TABLE;
    }
}