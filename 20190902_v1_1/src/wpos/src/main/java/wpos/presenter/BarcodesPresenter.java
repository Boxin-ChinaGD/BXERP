package wpos.presenter;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Component;
import wpos.utils.EventBus;
import wpos.bo.NtpHttpBO;
import wpos.dao.BarcodesMapper;
import wpos.event.BaseEvent;
import wpos.event.UI.BaseSQLiteEvent;
import wpos.model.Barcodes;
import wpos.model.BaseModel;
import wpos.model.ErrorInfo;

import javax.annotation.Resource;
import javax.persistence.Query;
import javax.persistence.Table;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static wpos.bo.BaseSQLiteBO.CASE_Barcodes_DeleteNByConditions;
import static wpos.bo.BaseSQLiteBO.CASE_Barcodes_RetrieveNByConditions;

@Component("barcodesPresenter")
public class BarcodesPresenter extends BasePresenter {
    private Log log = LogFactory.getLog(this.getClass());

//    protected static ReentrantReadWriteLock lock = new ReentrantReadWriteLock();

//    public BarcodesPresenter(BarcodesMapper barcodesMapper) {
//        super(barcodesMapper);
//    }

    public static final String QUERY_Barcodes_TABLE = "SELECT F_ID," +
            "F_CommodityID," +
            "F_Barcode," +
            "F_CreateDatetime," +
            "F_UpdateDatetime," +
            "F_SyncDatetime," +
            "F_SyncType " +
            "FROM T_Barcodes ";

    @Resource
    private BarcodesMapper barcodesMapper;

    @Override
    protected List<BaseModel> createNSync(int iUseCaseID, final List<?> list) {
        log.info("正在进行BarcodesPresenter的createNSync，list=" + (list == null ? null : list.toString()));

        switch (iUseCaseID) {
            default:
                globalWriteLock.writeLock().lock();
                List<BaseModel> barcodes = new ArrayList<BaseModel>();
                try {
                    for (int i = 0; i < list.size(); i++) {
                        Barcodes b = (Barcodes) list.get(i);
                        b.setSyncType(BasePresenter.SYNC_Type_C);
                        b = (Barcodes) createSync(iUseCaseID, b);
                        if (b != null) {
                            barcodes.add(b);
                            lastErrorCode = ErrorInfo.EnumErrorCode.EC_NoError;
                        }
                    }
                } catch (Exception e) {
                    log.info("createNSync出现异常，错误信息为" + e.getMessage());
                    e.printStackTrace();
                    lastErrorCode = ErrorInfo.EnumErrorCode.EC_OtherError;
                }
                globalWriteLock.writeLock().unlock();
                return barcodes;
        }
    }

    @Override
    protected BaseModel createSync(int iUseCaseID, BaseModel bm) {
        log.info("正在进行BarcodesPresenter的createSync，bm=" + (bm == null ? null : bm.toString()));

        globalWriteLock.writeLock().lock();
        try {
            bm.setSyncType(BasePresenter.SYNC_Type_C);
            barcodesMapper.create((Barcodes) bm);

            String sql;
            if (bm.getID() != null) {
                sql = "WHERE F_ID = " + bm.getID();
            } else {
                sql = "WHERE F_ID = (SELECT MAX(F_ID) FROM T_Barcodes)";
            }
            Query query = entityManager.createNativeQuery(QUERY_Barcodes_TABLE + sql, Barcodes.class);
            List<Barcodes> barcodes = query.getResultList();
            if (barcodes != null && barcodes.size() > 0) {
                lastErrorCode = ErrorInfo.EnumErrorCode.EC_NoError;
                globalWriteLock.writeLock().unlock();
                barcodes.get(0).fillNonDBFieldValue(iUseCaseID,bm);
                return barcodes.get(0);
            } else {
                lastErrorCode = ErrorInfo.EnumErrorCode.EC_OtherError;
            }
        } catch (Exception e) {
            log.info("createSync出现异常，错误信息为" + e.getMessage());
            e.printStackTrace();
            lastErrorCode = ErrorInfo.EnumErrorCode.EC_OtherError;
        }

        globalWriteLock.writeLock().unlock();
        return null;
    }

    @Override
    protected BaseModel retrieve1Sync(int iUseCaseID, BaseModel bm) {
        log.info("正在进行BarcodesPresenter的retrieve1Sync，bm=" + (bm == null ? null : bm.toString()));

        try {
            if (bm.getID() != null) {
                lastErrorCode = ErrorInfo.EnumErrorCode.EC_NoError;
                return barcodesMapper.findOne(bm.getID().intValue());
            } else {
                lastErrorCode = ErrorInfo.EnumErrorCode.EC_OtherError;
                return null;
            }
        } catch (Exception e) {
            log.info("retrieve1Sync出现异常，错误信息为" + e.getMessage());
            e.printStackTrace();
            lastErrorCode = ErrorInfo.EnumErrorCode.EC_OtherError;

            return null;
        }
    }

    @Override
    protected List<?> retrieveNSync(int iUseCaseID, BaseModel bm) {
        log.info("正在进行BarcodesPresenter的retrieveNSync，bm=" + (bm == null ? null : bm.toString()));

        switch (iUseCaseID) {
            case CASE_Barcodes_RetrieveNByConditions:
                try {
                    lastErrorCode = ErrorInfo.EnumErrorCode.EC_NoError;
                    String sql = String.format(bm.getSql(), bm.getConditions());
                    Query dataQuery = entityManager.createNativeQuery(QUERY_Barcodes_TABLE + sql, Barcodes.class);
                    return dataQuery.getResultList();
                } catch (Exception e) {
                    log.info("执行retrieveNSync的CASE_Barcodes_RetrieveNByConditions分支时出现异常，错误信息为" + e.getMessage());
                    e.printStackTrace();
                    lastErrorCode = ErrorInfo.EnumErrorCode.EC_OtherError;

                    return null;
                }
            default:
                try {
                    lastErrorCode = ErrorInfo.EnumErrorCode.EC_NoError;

                    return barcodesMapper.findAll();
                } catch (Exception e) {
                    log.info("执行retrieveNSync时出现异常，错误信息为" + e.getMessage());
                    e.printStackTrace();
                    lastErrorCode = ErrorInfo.EnumErrorCode.EC_NoError;
                    log.info("错误为" + lastErrorCode);
                    return null;
                }
        }
    }

    @Override
    protected BaseModel deleteSync(int iUseCaseID, BaseModel bm) {
        log.info("正在进行BarcodesPresenter的deleteSync，bm=" + (bm == null ? null : bm.toString()));
        globalWriteLock.writeLock().lock();
        try {
            barcodesMapper.delete(bm.getID().intValue());
            lastErrorCode = ErrorInfo.EnumErrorCode.EC_NoError;
        } catch (Exception e) {
            log.info("执行deleteSync时出现异常，错误信息为" + e.getMessage());
            e.printStackTrace();
            lastErrorCode = ErrorInfo.EnumErrorCode.EC_OtherError;
        }
        globalWriteLock.writeLock().unlock();

        return null;
    }

    @Override
    protected BaseModel deleteNSync(int iUserCaseID, BaseModel bm) {
        log.info("正在进行BarcodesPresenter的deleteNSync，bm=" + (bm == null ? null : bm.toString()));
        globalWriteLock.writeLock().lock();
        switch (iUserCaseID) {
            case CASE_Barcodes_DeleteNByConditions:
                try {
                    String sql = String.format(bm.getSql(), bm.getConditions());
                    entityManager.createNativeQuery(sql, Barcodes.class);
                    lastErrorCode = ErrorInfo.EnumErrorCode.EC_NoError;
                } catch (Exception e) {
                    log.info("执行deleteNSync时出现异常，错误信息为" + e.getMessage());
                    e.printStackTrace();
                    lastErrorCode = ErrorInfo.EnumErrorCode.EC_OtherError;
                }
                break;
            default:
                try {
                    barcodesMapper.deleteAllInBatch();
                    lastErrorCode = ErrorInfo.EnumErrorCode.EC_NoError;
                } catch (Exception e) {
                    log.info("执行deleteNSync时出现异常，错误信息为" + e.getMessage());
                    e.printStackTrace();
                    lastErrorCode = ErrorInfo.EnumErrorCode.EC_OtherError;
                }
                break;
        }
        globalWriteLock.writeLock().unlock();
        return null;
    }

    @Override
    protected boolean refreshByServerDataAsync(final int iUseCaseID, final List<?> bmNewList, final BaseSQLiteEvent event) {
        log.info("正在进行BarcodesPresenter的refreshByServerDataAsync，bmNewList=" + (bmNewList == null ? null : bmNewList.toString()));

        event.setStatus(BaseEvent.EnumEventStatus.EES_SQLite_ToApplyServerData);
        switch (iUseCaseID) {
            default:
                new Thread() {
                    public void run() {
                        globalWriteLock.writeLock().lock();
                        log.info("已经得到服务器返回的需要同步的Barcodes数据, 准备进行同步...");
                        List<BaseModel> bmList = (List<BaseModel>) bmNewList;
//                        event.setEventTypeSQLite(BaseSQLiteEvent.EnumSQLiteEventType.ESET_Barcodes_RefreshByServerDataAsync_Done);
                        try {
                            if (bmList != null) {
                                for (int i = 0; i < bmList.size(); i++) {
                                    String BarcodesType = bmList.get(i).getSyncType();

                                    if (BasePresenter.SYNC_Type_C.equals(BarcodesType)) {
                                        Barcodes barcodes = (Barcodes) retrieve1Sync(iUseCaseID, bmList.get(i));
                                        if (barcodes != null) {
                                            barcodesMapper.delete(barcodes.getID().intValue());
                                        }
                                        //
                                        barcodesMapper.create((Barcodes) bmList.get(i));

                                        log.info("服务器返回的C型Barcodes数据成功同步到SQLite中!");
                                    } else if (BasePresenter.SYNC_Type_D.equals(BarcodesType)) {
                                        Barcodes barcodes = (Barcodes) retrieve1Sync(iUseCaseID, bmList.get(i));
                                        if (barcodes != null) {
                                            barcodesMapper.delete(barcodes.getID().intValue());
                                        }

                                        log.info("服务器返回的D型Barcodes数据成功同步到SQLite中!");
                                    }
                                    event.setLastErrorCode(ErrorInfo.EnumErrorCode.EC_NoError);
                                    event.setListMasterTable(bmList);
                                }
                            } else {
                                event.setLastErrorCode(ErrorInfo.EnumErrorCode.EC_NoError);
                                event.setListMasterTable(bmList);
                            }
                        } catch (Exception e) {
                            log.info("执行refreshByServerDataAsync时出现异常，错误信息为" + e.getMessage());
                            e.printStackTrace();
                            event.setLastErrorCode(ErrorInfo.EnumErrorCode.EC_OtherError);
                        }
                        //
                        globalWriteLock.writeLock().unlock();
                        EventBus.getDefault().post(event);
                    }
                }.start();
                break;
        }
        return true;
    }

    @Override
    protected boolean refreshByServerDataAsyncC(final int iUseCaseID, final List<?> bmNewList, final BaseSQLiteEvent event) {
        log.info("正在进行BarcodesPresenter的refreshByServerDataAsyncC，bmNewList=" + (bmNewList == null ? null : bmNewList.toString()));

        event.setStatus(BaseEvent.EnumEventStatus.EES_SQLite_ToApplyServerData);
        switch (iUseCaseID) {
            default:
                new Thread() {
                    public void run() {
                        globalWriteLock.writeLock().lock();
                        log.info("已经得到服务器返回的需要同步的Barcodes数据, 准备进行同步...");
                        event.setLastErrorCode(ErrorInfo.EnumErrorCode.EC_NoError);
//                        event.setEventTypeSQLite(BaseSQLiteEvent.EnumSQLiteEventType.ESET_Barcodes_RefreshByServerDataAsyncC_Done);

                        try {
                            List<Barcodes> barcodesList = (List<Barcodes>) bmNewList;
                            // TODO 可能存在没删除掉的情况
                            //根据返回的数据，查找本地SQLite是否存该条数据，如果存在就删除再插入，否则就直接插入
                            if (barcodesList != null && barcodesList.size() > 0) {
                                Barcodes deleteConditionsBarcode = new Barcodes();
                                if (event.getPageIndex() != null && event.getPageIndex().equals(Barcodes.PAGEINDEX_START)) {
//                                    String sql = "delete from " + getTableName() + " where F_ID > %s ";//
//                                    String[] conditions = new String[]{String.valueOf(barcodesList.get(0).getID())};
//                                    deleteConditionsBarcode.setSql(sql);
//                                    deleteConditionsBarcode.setConditions(conditions);
//                                    deleteConditionsBarcode.setSubUseCaseID(BaseModel.EnumSubUseCaseID.ESUC_Long);
//                                    deleteNObjectSync(CASE_Barcodes_DeleteNByConditions, deleteConditionsBarcode);
//                                    if (lastErrorCode != ErrorInfo.EnumErrorCode.EC_NoError) {
//                                        event.setLastErrorCode(lastErrorCode);
//                                        event.setStatus(BaseEvent.EnumEventStatus.EES_SQLite_DoneApplyServerData);
//                                        lock.writeLock().unlock();
//                                        EventBus.getDefault().post(event);
//                                        return;
//                                    }
                                    // TODO
                                    try {
                                        barcodesMapper.deleteBiggerIDs(barcodesList.get(0).getID());
                                    } catch (Exception e) {
                                        log.error(e.getMessage());
                                        event.setLastErrorCode(lastErrorCode);
                                        event.setStatus(BaseEvent.EnumEventStatus.EES_SQLite_DoneApplyServerData);
                                        globalWriteLock.writeLock().unlock();
                                        EventBus.getDefault().post(event);
                                        return;
                                    }
                                } else if (event.getPageIndex() != null && event.getPageIndex().equals(Barcodes.PAGEINDEX_END)) {
//                                    String sql = "delete from " + getTableName() + " where F_ID < %s ";//
//                                    String[] conditions = new String[]{String.valueOf(barcodesList.get(barcodesList.size() - 1).getID())};
//                                    deleteConditionsBarcode.setSql(sql);
//                                    deleteConditionsBarcode.setConditions(conditions);
//                                    deleteConditionsBarcode.setSubUseCaseID(BaseModel.EnumSubUseCaseID.ESUC_Long);
//                                    deleteNObjectSync(CASE_Barcodes_DeleteNByConditions, deleteConditionsBarcode);
//                                    if (lastErrorCode != ErrorInfo.EnumErrorCode.EC_NoError) {
//                                        event.setLastErrorCode(lastErrorCode);
//                                        event.setStatus(BaseEvent.EnumEventStatus.EES_SQLite_DoneApplyServerData);
//                                        lock.writeLock().unlock();
//                                        EventBus.getDefault().post(event);
//                                        return;
//                                    }
                                    try {
                                        barcodesMapper.deleteSmallerIDs(barcodesList.get(barcodesList.size() - 1).getID());
                                    } catch (Exception e) {
                                        log.error(e.getMessage());
                                        event.setLastErrorCode(lastErrorCode);
                                        event.setStatus(BaseEvent.EnumEventStatus.EES_SQLite_DoneApplyServerData);
                                        globalWriteLock.writeLock().unlock();
                                        EventBus.getDefault().post(event);
                                        return;
                                    }
                                }
                                //
                                event.setPageIndex("");
//                                String sql = "delete from " + getTableName() + " where F_ID >= ? AND F_ID <= ?";
//                                String[] conditions = new String[]{String.valueOf(barcodesList.get(barcodesList.size() - 1).getID()), String.valueOf(barcodesList.get(0).getID())};
//                                //先删除本地数据库原有的数据
//                                deleteConditionsBarcode.setSql(sql);
//                                deleteConditionsBarcode.setConditions(conditions);
//                                deleteConditionsBarcode.setSubUseCaseID(BaseModel.EnumSubUseCaseID.ESUC_Long);
//                                deleteNObjectSync(CASE_Barcodes_DeleteNByConditions, deleteConditionsBarcode);
//                                if (lastErrorCode != ErrorInfo.EnumErrorCode.EC_NoError) {
//                                    event.setLastErrorCode(lastErrorCode);
//                                    event.setStatus(BaseEvent.EnumEventStatus.EES_SQLite_DoneApplyServerData);
//                                    lock.writeLock().unlock();
//                                    EventBus.getDefault().post(event);
//                                    return;
//                                }
                                try {
                                    barcodesMapper.deleteByRangeID(barcodesList.get(barcodesList.size() - 1).getID(), barcodesList.get(0).getID());
                                } catch (Exception e) {
                                    event.setLastErrorCode(lastErrorCode);
                                    event.setStatus(BaseEvent.EnumEventStatus.EES_SQLite_DoneApplyServerData);
                                    globalWriteLock.writeLock().unlock();
                                    EventBus.getDefault().post(event);
                                    return;
                                }

                                createNSync(iUseCaseID, bmNewList);

                            }
                            event.setLastErrorCode(lastErrorCode);
                            event.setListMasterTable(bmNewList);
                        } catch (Exception e) {
                            log.info("执行refreshByServerDataAsyncC时出现异常，错误信息为" + e.getMessage());

                            e.printStackTrace();
                            event.setLastErrorCode(ErrorInfo.EnumErrorCode.EC_OtherError);
                        }
                        event.setStatus(BaseEvent.EnumEventStatus.EES_SQLite_DoneApplyServerData);
                        //
                        globalWriteLock.writeLock().unlock();
                        EventBus.getDefault().post(event);
                    }
                }.start();
                break;
        }
        return true;
    }

    @Override
    protected boolean createNAsync(int iUseCaseID, final List<?> list, final BaseSQLiteEvent event) {
        log.info("正在进行BarcodesPresenter的CreateNAsync，list=" + (list == null ? null : list.toString()));

        switch (iUseCaseID) {
            default:
                new Thread() {
                    public void run() {
//                        event.setEventTypeSQLite(BaseSQLiteEvent.EnumSQLiteEventType.ESET_Barcodes_CreateNAsync);
                        //
                        globalWriteLock.writeLock().lock();
                        List<Barcodes> barcodes = new ArrayList<Barcodes>();
                        try {
                            for (int i = 0; i < list.size(); i++) {
                                Barcodes c = (Barcodes) list.get(i);
                                c.setSyncDatetime(new Date(System.currentTimeMillis() + NtpHttpBO.TimeDifference));
                                c.setSyncType(BasePresenter.SYNC_Type_C);
                                c = (Barcodes) createSync(iUseCaseID, c);
                                if (c != null) {
                                    barcodes.add(c);
                                }
                            }
                            if (barcodes.size() == list.size()) {
                                event.setLastErrorCode(ErrorInfo.EnumErrorCode.EC_NoError);
                            } else {
                                event.setLastErrorCode(ErrorInfo.EnumErrorCode.EC_PartSuccess);
                            }
                        } catch (Exception e) {
                            log.info("创建Barcodes时出错，错误信息：" + e.getMessage());
                            event.setLastErrorCode(ErrorInfo.EnumErrorCode.EC_OtherError);
                        }
                        //
                        event.setListMasterTable(barcodes);
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
    protected boolean createAsync(int iUseCaseID, final BaseModel bm, final BaseSQLiteEvent event) {
        log.info("正在进行BarcodesPresenter的createAsync，bm=" + (bm == null ? null : bm.toString()));

        switch (iUseCaseID) {
            default:
                new Thread() {
                    public void run() {
//                        event.setEventTypeSQLite(BaseSQLiteEvent.EnumSQLiteEventType.ESET_Barcodes_CreateAsync);
                        //
                        globalWriteLock.writeLock().lock();
                        Barcodes barcodes = null;
                        try {
                            bm.setSyncType(BasePresenter.SYNC_Type_C);
                            bm.setSyncDatetime(new Date(System.currentTimeMillis() + NtpHttpBO.TimeDifference));
                            barcodes = (Barcodes) createSync(iUseCaseID, bm);
                            event.setLastErrorCode(lastErrorCode);
                            barcodes.fillNonDBFieldValue(iUseCaseID,bm);
                        } catch (Exception e) {
                            log.error("创建Barcodes时出错，错误信息：" + e.getMessage());
                            event.setLastErrorCode(ErrorInfo.EnumErrorCode.EC_OtherError);
                        }
                        //
                        event.setBaseModel1(barcodes);
                        //
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
    protected boolean deleteAsync(int iUseCaseID, final BaseModel bm, final BaseSQLiteEvent event) {
        log.info("正在进行BarcodesPresenter的deleteAsync，bm=" + (bm == null ? null : bm.toString()));

        switch (iUseCaseID) {
            default:
                new Thread() {
                    public void run() {
                        globalWriteLock.writeLock().lock();
                        event.setEventTypeSQLite(BaseSQLiteEvent.EnumSQLiteEventType.ESET_Barcodes_DeleteAsync);
                        //
                        try {
                            barcodesMapper.delete(bm.getID().intValue());
                            event.setLastErrorCode(ErrorInfo.EnumErrorCode.EC_NoError);
                        } catch (Exception e) {
                            log.error("删除Barcodes时出错，错误信息：" + e.getMessage());
                            event.setLastErrorCode(ErrorInfo.EnumErrorCode.EC_OtherError);
                        }
                        //
                        event.setBaseModel1(bm);
                        //
                        globalWriteLock.writeLock().unlock();
                        EventBus.getDefault().post(event);
                    }
                }.start();
                break;
        }

        return true;
    }

    @Override
    protected boolean retrieveNAsync(int iUseCaseID, final BaseModel bm, final BaseSQLiteEvent event) {
        log.info("正在进行BarcodesPresenter的retrieveNAsync，bm=" + (bm == null ? null : bm.toString()));

        switch (iUseCaseID) {
            default:
                new Thread() {
                    public void run() {
                        List<Barcodes> result = null;
                        event.setEventTypeSQLite(BaseSQLiteEvent.EnumSQLiteEventType.ESET_Barcodes_RetrieveNAsync);
                        try {
                            Thread.sleep(1); //令event.setID()的ID惟一
                            result = barcodesMapper.findAll();//...
                            event.setLastErrorCode(ErrorInfo.EnumErrorCode.EC_NoError);
                        } catch (Exception e) {
                            log.info("查询SQLite的表Barcodes的所有记录时出错，错误信息：" + e.getMessage());
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
    protected String getTableName() {
        if (Barcodes.class.isAnnotationPresent(Table.class)) {
            Table annotation = Barcodes.class.getAnnotation(Table.class);
            return annotation.name();
        } else {
            log.error("无法获取表名，未给" + Barcodes.class.getSimpleName() + "添加@Table。");
        }
        return "";
    }

    @Override
    protected String getQueryTable() {
        return QUERY_Barcodes_TABLE;
    }

    @Override
    public void createTableSync() {
        barcodesMapper.createTable();
    }
}
