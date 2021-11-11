package wpos.presenter;

//import android.database.Cursor;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Component;
import wpos.event.BaseEvent;
import wpos.event.UI.BaseSQLiteEvent;
import wpos.helper.Constants;
import wpos.model.*;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.locks.ReentrantReadWriteLock;

import static wpos.presenter.CommodityPresenter.Query_CommodityShopInfo_TABLE;

/**
 * 本类提供同步接口和异步接口。<br />
 * 提供异步接口是为了保证Android界面的流畅性。<br />
 * 同步接口大部分在测试代码中使用。异步接口大部分在非测试代码中使用。<br />
 */
@Component("basePresenter")
public class BasePresenter {
    private Log log = LogFactory.getLog(this.getClass());

    public static final String SYNC_Type_C = "C";    //创建型同步块
    public static final String SYNC_Type_U = "U";    //更新型同步块
    public static final String SYNC_Type_D = "D";    //删除型同步块

    /**
     * 使用的 SQLite 数据库不支持并发（同时）修改数据库，所以涉及 CUD 数据库的方法需要公用一个锁 gloabalWriteLock
     * */
    public static final ReentrantReadWriteLock globalWriteLock = new ReentrantReadWriteLock();

    /**
     * 本地创建一个对象A后会保存在SQLite中，然后同步到服务器，服务器会返回一个新ID给对象A，对象A的ID需要更新为服务器的ID。如果在服务器返回ID前，有新的对象B被创建，B的ID可能覆盖掉对象A的ID。
     * 为此，需要在创建对象A时，生成一个较大的ID，其值=当前SQLite最大的ID X 2 + SQLITE_ID_GAP
     */
    public static final int SQLITE_ID_GAP = 10000;

    public static final int SQLITE_EXECUTE_NUMBER = 1;

    public static final Integer generateTableID(Integer maxID) {
        return maxID * 2 + SQLITE_ID_GAP;
    }

//    protected DaoSession dao;

//    protected BaseMapper baseMapper;

    public ErrorInfo.EnumErrorCode getLastErrorCode() {
        return lastErrorCode;
    }

    protected ErrorInfo.EnumErrorCode lastErrorCode;

    public String getLastErrorMessage() {
        return lastErrorMessage;
    }

    protected String lastErrorMessage;

    @PersistenceContext
    protected EntityManager entityManager;

//    public BasePresenter(final BaseMapper baseMapper) {
//        this.baseMapper = baseMapper;
//    }

    /**
     * 拿到已经同步或未同步到服务器的最大ID
     *
     * @param bSynchronized            true，拿到已经同步到服务器的最大ID；false，拿到未同步到服务器的最大ID
     * @param sFieldNameOfSyncDatetime 标识是否同步的日期型字段
     * @return
     */
    public Integer getMaxId(boolean bSynchronized, String sFieldNameOfSyncDatetime, Class modelClass) {
        // TODO EntityManager没有游标
        String sTableName = getTableName();
        String queryTable = getQueryTable();
        Integer maxId = 0;
        try {
            String op = (bSynchronized ? " != " : " = ");
            String sql = " where F_ID = (select max(F_ID) from " + sTableName + " where " + sFieldNameOfSyncDatetime + " " + op + "0" + ")";
            Query dataQuery = entityManager.createNativeQuery(queryTable + sql, modelClass);
            List<BaseModel> list = dataQuery.getResultList();
            if (list != null && list.size() > 0) {
                maxId = list.get(0).getID();
            }
            lastErrorCode = ErrorInfo.EnumErrorCode.EC_NoError;
        } catch (Exception e) {
            log.info("读取SQLite的表" + sTableName + "的最大ID时出错，错误信息：" + e.getMessage());
            lastErrorCode = ErrorInfo.EnumErrorCode.EC_OtherError;
        }

        return maxId;
    }

    protected String getTableName() {
        throw new RuntimeException("Not yet implemented!");
    }

    protected String getQueryTable() {
        throw new RuntimeException("Not yet implemented!");
    }

    protected Class getClassName() {
        throw new RuntimeException("Not yet implemented!");
    }

    /**
     * 生成临时对象（替身)ID。待同步成功，服务器返回真身后，删除本地的替身
     * //... 将来加上锁
     *
     * @param sFieldNameOfSyncDatetime
     * @return
     */
    public Integer generateTmpRowID(String sFieldNameOfSyncDatetime, Class modelClass) {
        Integer maxIDSynchronized = getMaxId(true, sFieldNameOfSyncDatetime, modelClass); //真正数据ID
        if (lastErrorCode != ErrorInfo.EnumErrorCode.EC_NoError) {
            return -1;
        }
        Integer maxIDNotSynchronized = getMaxId(false, sFieldNameOfSyncDatetime, modelClass);  //临时数据ID
        if (lastErrorCode != ErrorInfo.EnumErrorCode.EC_NoError) {
            return -2;
        }

        if (maxIDSynchronized == 0 && maxIDNotSynchronized == 0) {
            return BasePresenter.generateTableID(0);
        } else if (maxIDSynchronized == 0 && maxIDNotSynchronized > 0) {
            return maxIDNotSynchronized + 1;
        } else if (maxIDSynchronized > 0 && maxIDNotSynchronized == 0) {
            return BasePresenter.generateTableID(maxIDSynchronized);
        }
        //else if (maxIDSynchronized > 0 && maxIDNotSynchronized > 0) {
        return maxIDNotSynchronized + 1;//...这里虽然正确地得到了MAXID，但可能maxIDSynchronized接近了替身的位置，导致服务器返回真身时，插入替身所在的位置，导致插入失败。这种情况一般不会出现
//        }
    }

    /**
     * 查找F_SyncDatetime为“1970-01-01 08:00:00 000”的数据
     *
     * @return
     */
//    public List<?> getTmpDataID(String tableName) {
//        List<Long> idList = new ArrayList<>();
//        String sql = "select * from " + tableName + " where F_SyncDatetime = '0'";
//        try {
//            Cursor cursor = dao.getDatabase().rawQuery(sql, null);
//            cursor.moveToNext();
//            if (cursor.moveToFirst() == false) {
//                log.info("找不到数据");
//            } else {
//                log.info("时间：" + cursor.getString(cursor.getColumnIndex("F_SyncDatetime")));
//                idList.add(cursor.getLong(0));
//                while (cursor.moveToNext()) {
//                    idList.add(Long.valueOf(cursor.getString(0)));
//                }
//            }
//        } catch (Exception e) {
//            log.info("getTmpData出错，错误信息：" + e.getMessage());
//        }
//        return idList;
//    }

    /**
     * 分页返回本地SQLite的数据
     *
     * @param tableName 数据表名称
     * @param pageIndex 页码
     * @param pageSize  每一页的数据量
     * @return
     */
    public List<?> retrieveNAsyncByPage(String tableName, int pageIndex, int pageSize) {
        List<BaseModel> list = new ArrayList<BaseModel>();
//        String sql = "select * from " + tableName + " Limit " + pageSize + " Offset " + ((pageIndex - 1) * pageSize);
//        String countSQL = "select count(*) from " + tableName;
        try {
            // TODO
//            Cursor countCursor = dao.getDatabase().rawQuery(sql, null);
//            Cursor cursor = dao.getDatabase().rawQuery(sql, null);
//            cursor.moveToNext();
//            if (cursor.moveToFirst() == false) {
//                log.info("pagingRetrieveNAsync找不到数据!");
//            } else {
//                Commodity commodity = new Commodity();
//                commodity.setID(cursor.getLong(0));
//                commodity.setName(cursor.getString(2));
//                commodity.setCategoryID(cursor.getInt(8));
//                commodity.setSpecification(cursor.getString(4));
//                commodity.setNO(cursor.getInt(25));
//                commodity.setPackageUnitID(cursor.getInt(5));
//                list.add(commodity);
//                while (cursor.moveToNext()) {
//                    commodity = new Commodity();
//                    commodity.setID(cursor.getLong(0));
//                    commodity.setName(cursor.getString(2));
//                    commodity.setCategoryID(cursor.getInt(8));
//                    commodity.setSpecification(cursor.getString(4));
//                    commodity.setNO(cursor.getInt(25));
//                    commodity.setPackageUnitID(cursor.getInt(5));
//                    list.add(commodity);
//                }
//            }
            String sql = String.format("Limit '%s', '%s'", new String[]{String.valueOf((pageIndex - 1) * pageSize), String.valueOf(pageSize)});
            Query query = entityManager.createNativeQuery(CommodityPresenter.QUERY_Commodity_TABLE + sql, Commodity.class);
            List<Commodity> commodityList = query.getResultList();
            for(Commodity commodity : commodityList) {
                String[] condition = {String.valueOf(commodity.getID()), String.valueOf(Constants.shopID)};
                String sqlCommShoInfo = String.format("where F_CommodityID = '%s' and F_ShopID = '%s'", condition);
                Query queryCommShoInfo = entityManager.createNativeQuery(Query_CommodityShopInfo_TABLE + sqlCommShoInfo, CommodityShopInfo.class);
                List<CommodityShopInfo> commodityShopInfoList = queryCommShoInfo.getResultList();
                commodity.setListSlave2(commodityShopInfoList);
            }
            return commodityList;
//            return query.getResultList();
        } catch (Exception e) {
            e.printStackTrace();
            log.error("retrieveNAsyncByPage出错，错误信息：" + e.getMessage());
        }
        return list;
    }

    /**
     * 查询本地某个表的总条数
     *
     * @return -1，查询出现异常。>-1，查询正常
     */
    public Integer retrieveCount() {
        String countSQL = "select count(1) from " + getTableName();
        try {
//            Cursor countCursor = dao.getDatabase().rawQuery(countSQL, null);
//            Cursor cursor = dao.getDatabase().rawQuery(countSQL, null);
//            cursor.moveToFirst();
//            Integer count = cursor.getInt(0);
//            cursor.close();
//            return count;
            return 0;
        } catch (Exception e) {
            e.printStackTrace();
            log.error("retrieveCount，错误信息：" + e.getMessage());
            return -1;
        }
    }


    protected List<?> createNSync(int iUseCaseID, final List<?> list) {
        throw new RuntimeException("Not yet implemented!");
    }

    protected BaseModel createSync(int iUseCaseID, final BaseModel bm) {
        throw new RuntimeException("Not yet implemented!");
    }

    protected boolean updateSync(int iUseCaseID, final BaseModel bm) {
        throw new RuntimeException("Not yet implemented!");
    }

    protected BaseModel retrieve1Sync(int iUseCaseID, final BaseModel bm) {
        throw new RuntimeException("Not yet implemented!");
    }

    protected List<?> retrieveNSync(int iUseCaseID, final BaseModel bm) {
        throw new RuntimeException("Not yet implemented!");
    }

    protected BaseModel deleteSync(int iUseCaseID, final BaseModel bm) {
        throw new RuntimeException("Not yet implemented!");
    }

    protected BaseModel deleteNSync(int iUseCaseID, final BaseModel bm) {
        throw new RuntimeException("Not yet implemented!");
    }

    public void createTableSync() {
        throw new RuntimeException("Not yet implemented!");
    }

    public List<?> createNObjectSync(int iUseCaseID, final List<?> list) {
        if (list != null) {
            List<BaseModel> bmList = (List<BaseModel>) list;
            for (BaseModel bm : bmList) {
                String err = bm.checkCreate(iUseCaseID);
                if (err.length() > 0) {
                    Constants.checkModelLog(log, bm, err);
                    lastErrorCode = ErrorInfo.EnumErrorCode.EC_WrongFormatForInputField;
                    lastErrorMessage = err;
                    return null;
                }
            }
        }
        return createNSync(iUseCaseID, list);
    }

    public BaseModel createObjectSync(int iUseCaseID, final BaseModel bm) {
        String err = bm.checkCreate(iUseCaseID);
        if (err.length() > 0) {
            Constants.checkModelLog(log, bm, err);
            lastErrorCode = ErrorInfo.EnumErrorCode.EC_WrongFormatForInputField;
            lastErrorMessage = err;
            return null;
        }
        return createSync(iUseCaseID, bm);
    }

    public boolean updateObjectSync(int iUseCaseID, final BaseModel bm) {
        String err = bm.checkUpdate(iUseCaseID);
        if (err.length() > 0) {
            Constants.checkModelLog(log, bm, err);
            lastErrorCode = ErrorInfo.EnumErrorCode.EC_WrongFormatForInputField;
            lastErrorMessage = err;
            return false;
        }
        return updateSync(iUseCaseID, bm);
    }

    public BaseModel retrieve1ObjectSync(int iUseCaseID, final BaseModel bm) {
        if (bm != null) {
            String err = bm.checkRetrieve1(iUseCaseID);
            if (err.length() > 0) {
                Constants.checkModelLog(log, bm, err);
                lastErrorCode = ErrorInfo.EnumErrorCode.EC_WrongFormatForInputField;
                lastErrorMessage = err;
                return null;
            }
        }
        return retrieve1Sync(iUseCaseID, bm);
    }

    public List<?> retrieveNObjectSync(int iUseCaseID, final BaseModel bm) {
        if (bm != null) {
            String err = bm.checkRetrieveN(iUseCaseID);
            if (err.length() > 0) {
                Constants.checkModelLog(log, bm, err);
                lastErrorCode = ErrorInfo.EnumErrorCode.EC_WrongFormatForInputField;
                lastErrorMessage = err;
                return null;
            }
        }
        return retrieveNSync(iUseCaseID, bm);
    }

    public BaseModel deleteObjectSync(int iUseCaseID, final BaseModel bm) {
        String err = bm.checkDelete(iUseCaseID);
        if (err.length() > 0) {
            Constants.checkModelLog(log, bm, err);
            lastErrorCode = ErrorInfo.EnumErrorCode.EC_WrongFormatForInputField;
            lastErrorMessage = err;
            return null;
        }
        return deleteSync(iUseCaseID, bm);
    }

    public BaseModel deleteNObjectSync(int iUseCaseID, final BaseModel bm) {
        if (bm != null) {
            String err = bm.checkDelete(iUseCaseID);
            if (err.length() > 0) {
                Constants.checkModelLog(log, bm, err);
                lastErrorCode = ErrorInfo.EnumErrorCode.EC_WrongFormatForInputField;
                lastErrorMessage = err;
                return null;
            }
        }
        return deleteNSync(iUseCaseID, bm);
    }

    protected boolean createOrReplaceNAsync(int iUseCaseID, final List<?> list, final BaseSQLiteEvent event) {
        throw new RuntimeException("Not yet implemented!");
    }

    protected boolean createOrReplaceAsync(int iUseCaseID, final BaseModel baseModel, final BaseSQLiteEvent event) {
        throw new RuntimeException("Not yet implemented!");
    }

    protected boolean createNAsync(int iUseCaseID, final List<?> list, final BaseSQLiteEvent event) {
        throw new RuntimeException("Not yet implemented!");
    }

    protected boolean createAsync(int iUseCaseID, final BaseModel bm, final BaseSQLiteEvent event) {
        throw new RuntimeException("Not yet implemented!");
    }

    /**
     * 将已经存在于SQLite中的bmOld删除，将服务器返回的bmNew插入SQLite中
     *
     * @param iUseCaseID
     * @param bmOld      要删除的已经存在于SQLite中的旧的对象。其ID是本地SQLite中的ID
     * @param bmNew      要插入SQLite中的新对象。其ID是服务器DB中的ID
     * @param event
     * @return
     */
    protected boolean createReplacerAsync(final int iUseCaseID, final BaseModel bmOld, final BaseModel bmNew, final BaseSQLiteEvent event) {
        throw new RuntimeException("Not yet implemented!");
    }

    /**
     * 将已经存在于SQLite中的bmOldList删除，将服务器返回的bmNewList插入SQLite中
     *
     * @param iUseCaseID
     * @param bmOldList  要删除的已经存在于SQLite中的旧的对象集合。其中本地SQLite的ID
     * @param bmNewList  要插入SQLite中新的对象集合，其ID是服务器DB中的ID
     * @param event
     * @return
     */
    protected boolean createReplacerNAsync(final int iUseCaseID, final List<?> bmOldList, final List<?> bmNewList, final BaseSQLiteEvent event) {
        throw new RuntimeException("Not yet implemented!");
    }

    /**
     * 根据返回的数据的 string1 的类型进行增删CUD操作.根据返回的数据查找SQLite中对应的数据,如果同一个小票ID,string1==C和string1==U,查找SQLite如果不存在就create, 存在就进行update
     * 同一个小票ID如果string1==CommodityCategory,就对SQLite中对应的数据进行create
     * 同一个小票ID如果string1==U,就对SQLite中对应的数据进行update
     * 同一个小票ID如果string1==D,就对SQLite中对应的数据进行delete
     * 在httpevent进行操作m,去掉重复ID
     * 1.判断是否存在相同ID,若不存在就对SQLite中的数据进行相应操作
     * 2.如果存在相同ID,判断是否存在D类型,如果存在D类型,查找SQLite中是否存在对应数据,如果有就对其进行delete操作,如果没有就不进行任何操作
     * 3.如果不存在D类型,只有C和U型,就进行create操作.
     *
     * @param iUseCaseID
     * @param bmNewList  server返回的对象List
     * @param event
     * @return
     */
    protected boolean refreshByServerDataAsync(final int iUseCaseID, final List<?> bmNewList, final BaseSQLiteEvent event) {
        throw new RuntimeException("Not yet implemented!");
    }

    /**
     * @param iUseCaseID
     * @param bmNewList  server返回的对象List
     * @param event
     * @return
     */
    protected boolean refreshByServerDataAsyncC(final int iUseCaseID, final List<?> bmNewList, final BaseSQLiteEvent event) {
        throw new RuntimeException("Not yet implemented!");
    }

    /**
     * 向SQLite插入主表和从表的数据
     *
     * @param iUseCaseID
     * @param bmMaster   主表数据，里面有从表数据的指针
     * @param event
     * @return
     */
    protected boolean createMasterSlaveAsync(final int iUseCaseID, final BaseModel bmMaster, final BaseSQLiteEvent event) {
        throw new RuntimeException("Not yet implemented!");
    }

    /**
     * 向SQLite修改主表和从表的数据
     *
     * @param iUseCaseID
     * @param bmMaster   主表数据，里面有从表数据的指针
     * @param event
     * @return
     */
    protected boolean updateMasterSlaveAsync(final int iUseCaseID, final BaseModel bmMaster, final BaseSQLiteEvent event) {
        throw new RuntimeException("Not yet implemented!");
    }

    /**
     * 想SQLite删除主从表数据
     *
     * @param iUseCaseID
     * @param bm         主表数据,里面有从表数据的指针
     * @param event
     * @return
     */
    protected boolean deleteMasterSlaveAsync(final int iUseCaseID, final BaseModel bm, final BaseSQLiteEvent event) {
        throw new RuntimeException("Not yet implemented!");
    }

    protected boolean updateAsync(int iUseCaseID, final BaseModel bm, final BaseSQLiteEvent event) {
        throw new RuntimeException("Not yet implemented!");
    }

    protected boolean retrieve1Async(int iUseCaseID, final BaseModel bm, final BaseSQLiteEvent event) {
        throw new RuntimeException("Not yet implemented!");
    }

    protected boolean retrieveNAsync(int iUseCaseID, final BaseModel bm, final BaseSQLiteEvent event) {
        throw new RuntimeException("Not yet implemented!");
    }

    protected boolean deleteAsync(int iUseCaseID, final BaseModel bm, final BaseSQLiteEvent event) {
        throw new RuntimeException("Not yet implemented!");
    }

    protected boolean updateNAsync(int iUseCaseID, final List<?> bmList, final BaseSQLiteEvent event) {
        throw new RuntimeException("Not yet implemented!");
    }

    /**
     * 将网络返回的对象（真身）bmNew插入SQLite中，并在此前将临时对象（替身）bmOld删除
     */
    public boolean createReplacerObjectAsync(final int iUseCaseID, final BaseModel bmOld, final BaseModel bmNew, final BaseSQLiteEvent event) {
        return createReplacerAsync(iUseCaseID, bmOld, bmNew, event);
    }

    /**
     * 将网络返回的对象集合（真身）bmNewList插入SQLite中，并在此前将临时对象集合(替身) bmOldList删除
     */
    public boolean createReplacerNObjectASync(final int iUseCaseID, final List<?> bmOldList, final List<?> bmNewList, final BaseSQLiteEvent event) {
        return createReplacerNAsync(iUseCaseID, bmOldList, bmNewList, event);
    }

    public boolean refreshByServerDataObjectsAsync(final int iUseCaseID, final List<?> bmNewList, final BaseSQLiteEvent event) {
        return refreshByServerDataAsync(iUseCaseID, bmNewList, event);
    }

    public boolean refreshByServerDataObjectsAsyncC(final int iUseCaseID, final List<?> bmNewList, final BaseSQLiteEvent event) {
        return refreshByServerDataAsyncC(iUseCaseID, bmNewList, event);
    }

    /**
     * 向SQLite插入主从表对象。一般情况下，这些主从表对象是临时对象（替身），服务器返回对象（真身）后，将删除替身
     */
    public boolean createMasterSlaveObjectAsync(final int iUseCaseID, final BaseModel bmMaster, final BaseSQLiteEvent event) {
        return createMasterSlaveAsync(iUseCaseID, bmMaster, event);
    }

    public boolean updateMasterSlaveObjectAsync(final int iUseCaseID, final BaseModel bmMaster, final BaseSQLiteEvent event) {
        return updateMasterSlaveAsync(iUseCaseID, bmMaster, event);
    }

    public boolean deleteMasterSlaveObjectAsync(final int iUseCaseID, final BaseModel bm, final BaseSQLiteEvent event) {
        return deleteMasterSlaveAsync(iUseCaseID, bm, event);
    }

    public boolean createOrReplaceNObjectAsync(int iUseCaseID, final List<?> list, final BaseSQLiteEvent event) {
        return createOrReplaceNAsync(iUseCaseID, list, event);
    }

    public boolean createOrReplaceObjectAsync(int iUseCaseID, final BaseModel baseModel, final BaseSQLiteEvent event) {
        return createOrReplaceAsync(iUseCaseID, baseModel, event);
    }

    public boolean createNObjectAsync(int iUseCaseID, final List<?> list, final BaseSQLiteEvent event) {
        if (list != null) {
            List<BaseModel> bmList = (List<BaseModel>) list;
            for (BaseModel bm : bmList) {
                String err = bm.checkCreate(iUseCaseID);
                if (err.length() > 0) {
                    Constants.checkModelLog(log, bm, err);
                    event.setLastErrorCode(ErrorInfo.EnumErrorCode.EC_WrongFormatForInputField);
                    event.setLastErrorMessage(err);
                    event.setStatus(BaseEvent.EnumEventStatus.EES_SQLite_NoAction);
                    event.setEventProcessed(true);
                    return false;
                }
            }
        }

        return createNAsync(iUseCaseID, list, event);
    }

    public boolean createObjectAsync(int iUseCaseID, final BaseModel bm, final BaseSQLiteEvent event) {
        String err = bm.checkCreate(iUseCaseID);
        if (err.length() > 0) {
            Constants.checkModelLog(log, bm, err);
            event.setLastErrorCode(ErrorInfo.EnumErrorCode.EC_WrongFormatForInputField);
            event.setLastErrorMessage(err);
            event.setStatus(BaseEvent.EnumEventStatus.EES_SQLite_NoAction);
            event.setEventProcessed(true);
            return false;
        }
        return createAsync(iUseCaseID, bm, event);
    }

    public boolean updateObjectAsync(int iUseCaseID, final BaseModel bm, final BaseSQLiteEvent event) {
        String err = bm.checkUpdate(iUseCaseID);
        if (err.length() > 0) {
            Constants.checkModelLog(log, bm, err);
            event.setLastErrorCode(ErrorInfo.EnumErrorCode.EC_WrongFormatForInputField);
            event.setLastErrorMessage(err);
            event.setStatus(BaseEvent.EnumEventStatus.EES_SQLite_NoAction);
            event.setEventProcessed(true);
            return false;
        }
        return updateAsync(iUseCaseID, bm, event);
    }

    public boolean retrieve1ObjectAsync(int iUseCaseID, final BaseModel bm, final BaseSQLiteEvent event) {
        if (bm != null) {
            String err = bm.checkRetrieve1(iUseCaseID);
            if (err.length() > 0) {
                Constants.checkModelLog(log, bm, err);
                event.setLastErrorCode(ErrorInfo.EnumErrorCode.EC_WrongFormatForInputField);
                event.setLastErrorMessage(err);
                event.setStatus(BaseEvent.EnumEventStatus.EES_SQLite_NoAction);
                event.setEventProcessed(true);
                return false;
            }
        }
        return retrieve1Async(iUseCaseID, bm, event);
    }

    public boolean retrieveNObjectAsync(int iUseCaseID, final BaseModel bm, final BaseSQLiteEvent event) {
        if (bm != null) {
            String err = bm.checkRetrieveN(iUseCaseID);
            if (err.length() > 0) {
                Constants.checkModelLog(log, bm, err);
                event.setLastErrorCode(ErrorInfo.EnumErrorCode.EC_WrongFormatForInputField);
                event.setLastErrorMessage(err);
                event.setStatus(BaseEvent.EnumEventStatus.EES_SQLite_NoAction);
                event.setEventProcessed(true);
                return false;
            }
        }
        return retrieveNAsync(iUseCaseID, bm, event);
    }

    public boolean deleteObjectAsync(int iUseCaseID, final BaseModel bm, final BaseSQLiteEvent event) {
        String err = bm.checkDelete(iUseCaseID);
        if (err.length() > 0) {
            Constants.checkModelLog(log, bm, err);
            event.setLastErrorCode(ErrorInfo.EnumErrorCode.EC_WrongFormatForInputField);
            event.setLastErrorMessage(err);
            event.setStatus(BaseEvent.EnumEventStatus.EES_SQLite_NoAction);
            event.setEventProcessed(true);
            return false;
        }
        return deleteAsync(iUseCaseID, bm, event);
    }

    public boolean updateNObjectAsync(int iUseCaseID, final List<?> bmList, final BaseSQLiteEvent event) {
        if (bmList != null) {
            List<BaseModel> list = (List<BaseModel>) bmList;
            for (BaseModel bm : list) {
                String err = bm.checkUpdate(iUseCaseID);
                if (err.length() > 0) {
                    Constants.checkModelLog(log, bm, err);
                    event.setLastErrorCode(ErrorInfo.EnumErrorCode.EC_WrongFormatForInputField);
                    event.setLastErrorMessage(err);
                    event.setStatus(BaseEvent.EnumEventStatus.EES_SQLite_NoAction);
                    event.setEventProcessed(true);
                    return false;
                }
            }
        }

        return updateNAsync(iUseCaseID, bmList, event);
    }
}
