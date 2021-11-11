package wpos.presenter;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Component;
import wpos.utils.EventBus;
import wpos.dao.PackageUnitMapper;
import wpos.event.UI.BaseSQLiteEvent;
import wpos.model.BaseModel;
import wpos.model.ErrorInfo;
import wpos.model.PackageUnit;

import javax.annotation.Resource;
import javax.persistence.Query;
import javax.persistence.Table;
import java.util.ArrayList;
import java.util.List;

import static wpos.bo.BaseSQLiteBO.CASE_PackageUnit_RetrieveNByConditions;

@Component("packageUnitPresenter")
public class PackageUnitPresenter extends BasePresenter {
    private Log log = LogFactory.getLog(this.getClass());

    @Resource
    private PackageUnitMapper packageUnitMapper;

    public static final String QUERY_PackageUnit_TABLE = "SELECT F_ID,F_Name,F_CreateDatetime,F_UpdateDatetime FROM T_PackageUnit ";

//    public PackageUnitPresenter(PackageUnitMapper packageUnitMapper) {
//        super(packageUnitMapper);
//    }

//    protected static ReentrantReadWriteLock lock = new ReentrantReadWriteLock();

    @Override
    public void createTableSync() {
        packageUnitMapper.createTable();
    }

    @Override
    protected String getTableName() {
        if (PackageUnit.class.isAnnotationPresent(Table.class)) {
            Table annotation = PackageUnit.class.getAnnotation(Table.class);
            return annotation.name();
        } else {
            log.error("无法获取表名，未给" + PackageUnit.class.getSimpleName() + "添加@Table。");
        }
        return "";
    }

    @Override
    protected String getQueryTable() {
        return QUERY_PackageUnit_TABLE;
    }

    @Override
    protected BaseModel createSync(int iUseCaseID, final BaseModel bm) {
        log.info("正在进行PackageUnitPresenter的createSync，bm=" + bm);
        globalWriteLock.writeLock().lock();
        PackageUnit packageUnit = null;
        try {
            packageUnitMapper.create((PackageUnit) bm);

            String sql;
            if (bm.getID() != null) {
                sql = "WHERE F_ID = " + bm.getID();
            } else {
                sql = "WHERE F_ID = (SELECT MAX(F_ID) FROM T_PackageUnit)";
            }
            Query query = entityManager.createNativeQuery(QUERY_PackageUnit_TABLE + sql, PackageUnit.class);
            List<PackageUnit> packageUnits = query.getResultList();
            if (packageUnits != null && packageUnits.size() > 0) {
                lastErrorCode = ErrorInfo.EnumErrorCode.EC_NoError;
                packageUnit = packageUnits.get(0);
            } else {
                lastErrorCode = ErrorInfo.EnumErrorCode.EC_OtherError;
            }
        } catch (Exception e) {
            log.error("执行createSync失败，错误信息=" + e.getMessage());
            e.printStackTrace();
            lastErrorCode = ErrorInfo.EnumErrorCode.EC_OtherError;
        }
        globalWriteLock.writeLock().unlock();
        return packageUnit;
    }

    @Override
    protected List<BaseModel> createNSync(int iUseCaseID, final List<?> list) {
        log.info("正在进行PackageUnitPresenter的createNSync，list=" + (list == null ? null : list.toString()));

        switch (iUseCaseID) {
            default:
                globalWriteLock.writeLock().lock();
                List<BaseModel> baseModels = new ArrayList<BaseModel>();
                try {
                    for (int i = 0; i < list.size(); i++) {
                        PackageUnit packageUnit = (PackageUnit) list.get(i);
                        BaseModel baseModel = createSync(iUseCaseID, packageUnit);
                        if (baseModel !=null) {
                            baseModels.add(baseModel);
                        }
                    }
                    if (baseModels.size() == list.size()) {
                        lastErrorCode = ErrorInfo.EnumErrorCode.EC_NoError;
                    } else {
                        lastErrorCode = ErrorInfo.EnumErrorCode.EC_PartSuccess;
                    }
                } catch (Exception e) {
                    log.error("执行createNSync失败，错误信息为" + e.getMessage());
                    e.printStackTrace();
                    lastErrorCode = ErrorInfo.EnumErrorCode.EC_OtherError;
                }
                globalWriteLock.writeLock().unlock();
                return baseModels;
        }
    }

    @Override
    protected BaseModel deleteNSync(int iUseCaseID, BaseModel bm) {
        log.info("正在进行PackageUnitPresenter的deleteNSync，bm=" + bm);
        globalWriteLock.writeLock().lock();
        try {
            packageUnitMapper.deleteAll();
            lastErrorCode = ErrorInfo.EnumErrorCode.EC_NoError;
        } catch (Exception e) {
            log.error("执行deleteNSync失败，错误信息为" + e.getMessage());
            e.printStackTrace();
            lastErrorCode = ErrorInfo.EnumErrorCode.EC_OtherError;
        }
        globalWriteLock.writeLock().unlock();
        return null;
    }

    @Override
    protected BaseModel retrieve1Sync(int iUSeCaseID, BaseModel bm) {
        log.info("正在进行PackageUnitPresenter的retrieve1Sync，bm=" + bm);

        try {
            lastErrorCode = ErrorInfo.EnumErrorCode.EC_NoError;

            return packageUnitMapper.findOne(bm.getID());
        } catch (Exception e) {
            log.error("执行retrieve1Sync失败，错误信息为" + e.getMessage());
            e.printStackTrace();
            lastErrorCode = ErrorInfo.EnumErrorCode.EC_OtherError;

            return null;
        }
    }

    @Override
    protected List<?> retrieveNSync(int iUSeCaseID, BaseModel bm) {
        log.info("正在进行PackageUnitPresenter的retrieveNSync，bm=" + bm);

        switch (iUSeCaseID) {
            case CASE_PackageUnit_RetrieveNByConditions:
                try {
                    lastErrorCode = ErrorInfo.EnumErrorCode.EC_NoError;
                    String sql = String.format(bm.getSql(), bm.getConditions());
                    Query query = entityManager.createNativeQuery(QUERY_PackageUnit_TABLE + sql, PackageUnit.class);
                    return query.getResultList();
                } catch (Exception e) {
                    log.info("执行retrieveNSync的CASE_PackageUnit_RetrieveNByConditions失败，错误信息为" + e.getMessage());
                    e.printStackTrace();
                    lastErrorCode = ErrorInfo.EnumErrorCode.EC_OtherError;

                    return null;
                }
            default:
                try {
                    lastErrorCode = ErrorInfo.EnumErrorCode.EC_NoError;

                    return packageUnitMapper.findAll();
                } catch (Exception e) {
                    log.error("执行retrieveNSync失败，错误信息为" + e.getMessage());
                    e.printStackTrace();
                    lastErrorCode = ErrorInfo.EnumErrorCode.EC_OtherError;

                    return null;
                }
        }
    }

    @Override
    protected boolean refreshByServerDataAsyncC(final int iUseCaseID, final List<?> bmNewList, final BaseSQLiteEvent event) {
        log.info("正在进行PackageUnitPresenter的refreshByServerDataAsyncC，bmNewList=" + (bmNewList == null ? null : bmNewList.toString()));

//        event.setStatus(BaseEvent.EnumEventStatus.EES_SQLite_ToApplyServerData);
        switch (iUseCaseID) {
            default:
                (new Thread() {
                    @Override
                    public void run() {
                        globalWriteLock.writeLock().lock();
                        log.info("已经得到服务器返回的所有PackageUnit数据，准备进行同步...");

//                        event.setEventTypeSQLite(BaseSQLiteEvent.EnumSQLiteEventType.ESET_PackageUnit_RefreshByServerDataAsync_Done);

                        //删除本地所有的数据
                        deleteNSync(iUseCaseID, null);
                        if (getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError) {
                            //同步所有服务器返回的数据
                            createNSync(iUseCaseID, bmNewList);
                            if (getLastErrorCode() != ErrorInfo.EnumErrorCode.EC_NoError) {
                                event.setLastErrorCode(getLastErrorCode());
                            } else {
                                event.setLastErrorCode(ErrorInfo.EnumErrorCode.EC_NoError);
                            }
                        } else {
                            event.setLastErrorCode(ErrorInfo.EnumErrorCode.EC_OtherError);
                        }
                        globalWriteLock.writeLock().unlock();
                        //
                        EventBus.getDefault().post(event);
                    }
                }).start();
                break;
        }
        return true;
    }
}
