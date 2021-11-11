package wpos.presenter;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Component;
import wpos.utils.EventBus;
import wpos.bo.BaseSQLiteBO;
import wpos.bo.NtpHttpBO;
import wpos.dao.BrandMapper;
import wpos.event.BaseEvent;
import wpos.event.UI.BaseSQLiteEvent;
import wpos.helper.Constants;
import wpos.model.BaseModel;
import wpos.model.Brand;
import wpos.model.ErrorInfo;

import javax.annotation.Resource;
import javax.persistence.Query;
import javax.persistence.Table;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


import static wpos.bo.BaseSQLiteBO.CASE_Brand_RetrieveNByConditions;

@Component("brandPresenter")
public class BrandPresenter extends BasePresenter {
    private Log log = LogFactory.getLog(this.getClass());

    public static final String QUERY_Brand_TABLE = "SELECT F_ID,F_Name,F_SyncDatetime,F_SyncType FROM T_Brand ";

    @Resource
    private BrandMapper brandMapper;

//    protected static ReentrantReadWriteLock lock = new ReentrantReadWriteLock();

//    public BrandPresenter(final BrandMapper brandMapper) {
//        super(brandMapper);
//    }

    @Override
    protected List<BaseModel> createNSync(int iUseCaseID, final List<?> list) {
        log.info("正在进行BrandPresenter的createNSync，list=" + (list == null ? null : list.toString()));

        switch (iUseCaseID) {
            default:
                globalWriteLock.writeLock().lock();

                List<BaseModel> brands = new ArrayList<BaseModel>();
                try {
                    for (Object brand : list) {
                        Brand b = (Brand) createSync(iUseCaseID, (BaseModel) brand);
                        if (b != null) {
                            brands.add(b);
                        }
                    }
                    if (brands.size() != list.size()) {
                        lastErrorCode = ErrorInfo.EnumErrorCode.EC_PartSuccess;
                    } else {
                        lastErrorCode = ErrorInfo.EnumErrorCode.EC_NoError;
                    }
                } catch (Exception e) {
                    log.info("执行createNSync时出现异常，错误信息：" + e.getMessage());
                    e.printStackTrace();
                    lastErrorCode = ErrorInfo.EnumErrorCode.EC_OtherError;
                }

                globalWriteLock.writeLock().unlock();
                return brands;
        }
    }

    @Override
    protected BaseModel createSync(int iUseCaseID, BaseModel bm) {
        log.info("正在进行BrandPresenter的createSync，bm=" + (bm == null ? null : bm.toString()));

        globalWriteLock.writeLock().lock();
        try {
            bm.setSyncDatetime(new Date(System.currentTimeMillis() + NtpHttpBO.TimeDifference));
            bm.setSyncType(BasePresenter.SYNC_Type_C);
            brandMapper.create((Brand) bm);

            String sql;
            if (bm.getID() != null) {
                sql = "WHERE F_ID = " + bm.getID();
            } else {
                sql = "WHERE F_ID = (SELECT MAX(F_ID) FROM T_Brand)";
            }
            Query query = entityManager.createNativeQuery(QUERY_Brand_TABLE + sql, Brand.class);
            List<Brand> brands = query.getResultList();
            if (brands != null && brands.size() > 0) {
                lastErrorCode = ErrorInfo.EnumErrorCode.EC_NoError;
                globalWriteLock.writeLock().unlock();
                return brands.get(0);
            } else {
                lastErrorCode = ErrorInfo.EnumErrorCode.EC_OtherError;
            }
        } catch (Exception e) {
            log.info("执行createSync时出现异常，错误信息：" + e.getMessage());
            e.printStackTrace();
            lastErrorCode = ErrorInfo.EnumErrorCode.EC_OtherError;
        }

        globalWriteLock.writeLock().unlock();
        return null;
    }

    @Override
    protected boolean updateSync(int iUseCaseID, BaseModel bm) {
        log.info("正在进行BrandPresenter的updateSync，bm=" + (bm == null ? null : bm.toString()));
        globalWriteLock.writeLock().lock();
        try {
            bm.setSyncType(BasePresenter.SYNC_Type_U);
            bm.setSyncDatetime(new Date(System.currentTimeMillis() + NtpHttpBO.TimeDifference));
            brandMapper.save((Brand) bm);
            lastErrorCode = ErrorInfo.EnumErrorCode.EC_NoError;
            globalWriteLock.writeLock().unlock();
            return true;
        } catch (Exception e) {
            log.info("执行updateSync时出现异常，错误信息：" + e.getMessage());
            e.printStackTrace();
            lastErrorCode = ErrorInfo.EnumErrorCode.EC_NoError;
            globalWriteLock.writeLock().unlock();
            return false;
        }
    }

    @Override
    protected BaseModel retrieve1Sync(int iUseCaseID, BaseModel bm) {
        log.info("正在进行BrandPresenter的retrieve1Sync，bm=" + (bm == null ? null : bm.toString()));

        try {
            lastErrorCode = ErrorInfo.EnumErrorCode.EC_NoError;
            if (bm.getID() != null) {
                return brandMapper.findOne(bm.getID());
            } else {
                return null;
            }
        } catch (Exception e) {
            log.info("执行retrieve1Sync时出现异常，错误信息：" + e.getMessage());

            e.printStackTrace();
            lastErrorCode = ErrorInfo.EnumErrorCode.EC_OtherError;

            return null;
        }
    }

    @Override
    protected List<?> retrieveNSync(int iUseCaseID, BaseModel bm) {
        log.info("正在进行BrandPresenter的retrieveNSync，bm=" + (bm == null ? null : bm.toString()));

        switch (iUseCaseID) {
            case CASE_Brand_RetrieveNByConditions:
                try {
                    lastErrorCode = ErrorInfo.EnumErrorCode.EC_NoError;
                    String sql = String.format(bm.getSql(), bm.getConditions());
                    Query dataQuery = entityManager.createNativeQuery(QUERY_Brand_TABLE + sql, Brand.class);
                    return dataQuery.getResultList();
                } catch (Exception e) {
                    log.info("执行retrieveNSync的CASE_Brand_RetrieveNByConditions分支时出现异常，错误信息：" + e.getMessage());
                    e.printStackTrace();
                    lastErrorCode = ErrorInfo.EnumErrorCode.EC_OtherError;

                    return null;
                }
            default:
                try {
                    lastErrorCode = ErrorInfo.EnumErrorCode.EC_NoError;

                    return brandMapper.findAll();
                } catch (Exception e) {
                    log.info("执行retrieveNSync时出现异常，错误信息：" + e.getMessage());
                    e.printStackTrace();
                    lastErrorCode = ErrorInfo.EnumErrorCode.EC_NoError;

                    return null;
                }
        }
    }

    @Override
    protected BaseModel deleteSync(int iUseCaseID, BaseModel bm) {
        log.info("正在进行BrandPresenter的deleteSync，bm=" + (bm == null ? null : bm.toString()));
        globalWriteLock.writeLock().lock();
        try {
            brandMapper.delete(bm.getID());
            lastErrorCode = ErrorInfo.EnumErrorCode.EC_NoError;
        } catch (Exception e) {
            log.info("执行deleteSync失败，错误信息为" + e.getMessage());
            e.printStackTrace();
            lastErrorCode = ErrorInfo.EnumErrorCode.EC_OtherError;
        }
        globalWriteLock.writeLock().unlock();
        return null;
    }

    @Override
    protected BaseModel deleteNSync(int iUserCaseID, BaseModel bm) {
        log.info("正在进行BrandPresenter的deleteNSync，bm=" + (bm == null ? null : bm.toString()));
        globalWriteLock.writeLock().lock();
        try {
            brandMapper.deleteAllInBatch();
            lastErrorCode = ErrorInfo.EnumErrorCode.EC_NoError;
        } catch (Exception e) {
            log.info("执行deleteNSync失败，错误信息为" + e.getMessage());

            e.printStackTrace();
            lastErrorCode = ErrorInfo.EnumErrorCode.EC_OtherError;
        }
        globalWriteLock.writeLock().unlock();
        return null;
    }

    @Override
    protected boolean refreshByServerDataAsync(final int iUseCaseID, final List<?> bmNewList, final BaseSQLiteEvent event) {
        log.info("正在进行BrandPresenter的refreshByServerDataAsync，bmNewList=" + (bmNewList == null ? null : bmNewList.toString()));

        switch (iUseCaseID) {
            default:
                new Thread() {
                    @Override
                    public void run() {
                        log.info("已经得到服务器返回的需要同步的Brand数据, 准备进行同步...");
                        List<BaseModel> bmList = (List<BaseModel>) bmNewList;
//                        event.setEventTypeSQLite(BaseSQLiteEvent.EnumSQLiteEventType.ESET_Brand_RefreshByServerDataAsync_Done);
                        event.setLastErrorCode(ErrorInfo.EnumErrorCode.EC_NoError);
                        try {
                            if (bmList != null) {
                                for (int i = 0; i < bmList.size(); i++) {
                                    String BrandType = bmList.get(i).getSyncType();

                                    if (BasePresenter.SYNC_Type_C.equals(BrandType)) {
                                        Brand brand = (Brand) retrieve1Sync(iUseCaseID, bmList.get(i));
                                        if (brand != null) {
                                            deleteSync(BaseSQLiteBO.INVALID_CASE_ID, brand);
                                        }
                                        //
                                        createSync(iUseCaseID, bmList.get(i));
                                        if (getLastErrorCode() != ErrorInfo.EnumErrorCode.EC_NoError) {
                                            event.setLastErrorCode(ErrorInfo.EnumErrorCode.EC_OtherError);
                                            break;
                                        }
                                        log.info("服务器返回的C型Brand数据成功同步到SQLite中!");
                                    } else if (BasePresenter.SYNC_Type_U.equals(BrandType)) {
                                        updateSync(iUseCaseID, bmList.get(i));
                                        if (getLastErrorCode() != ErrorInfo.EnumErrorCode.EC_NoError) {
                                            event.setLastErrorCode(ErrorInfo.EnumErrorCode.EC_OtherError);
                                            break;
                                        }
                                        log.info("服务器返回的U型Brand数据成功同步到SQLite中!");
                                    } else if (BasePresenter.SYNC_Type_D.equals(BrandType)) {
                                        Brand brand = (Brand) retrieve1Sync(iUseCaseID, bmList.get(i));
                                        if (brand != null) {
                                            deleteSync(BaseSQLiteBO.INVALID_CASE_ID, brand);
                                        }
                                        if (getLastErrorCode() != ErrorInfo.EnumErrorCode.EC_NoError) {
                                            event.setLastErrorCode(ErrorInfo.EnumErrorCode.EC_OtherError);
                                            break;
                                        }
                                        log.info("服务器返回的D型Brand数据成功同步到SQLite中!");
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
                            log.info("brand同步的时候出错，错误信息：" + e.getMessage());
                            event.setLastErrorCode(ErrorInfo.EnumErrorCode.EC_OtherError);
                        }
                        event.setStatus(BaseEvent.EnumEventStatus.EES_SQLite_DoneApplyServerData);
                        //
                        EventBus.getDefault().post(event);
                    }
                }.start();
                break;
        }
        return true;
    }


    @Override
    protected boolean refreshByServerDataAsyncC(final int iUseCaseID, final List<?> bmNewList, final BaseSQLiteEvent event) {
        log.info("正在进行BrandPresenter的refreshByServerDataAsyncC，bmNewList=" + (bmNewList == null ? null : bmNewList.toString()));

        switch (iUseCaseID) {
            default:
                new Thread() {
                    public void run() {
                        log.info("已经得到服务器返回的需要同步的Brand数据, 准备进行同步...");

//                        event.setEventTypeSQLite(BaseSQLiteEvent.EnumSQLiteEventType.ESET_Brand_RefreshByServerDataAsync_Done);
                        event.setLastErrorCode(ErrorInfo.EnumErrorCode.EC_NoError);
                        //删除本地所有的数据
                        deleteNSync(iUseCaseID, null);
                        if (getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError) {
                            //同步所有服务器返回的数据
                            createNSync(iUseCaseID, bmNewList);
                            if (getLastErrorCode() != ErrorInfo.EnumErrorCode.EC_NoError) {
                                event.setLastErrorCode(ErrorInfo.EnumErrorCode.EC_OtherError);
                            }
                        } else {
                            event.setLastErrorCode(ErrorInfo.EnumErrorCode.EC_OtherError);
                        }
//                        event.setLastErrorCode(ErrorInfo.EnumErrorCode.EC_NoError);
                        event.setStatus(BaseEvent.EnumEventStatus.EES_SQLite_DoneApplyServerData);
                        //
                        EventBus.getDefault().post(event);
                    }
                }.start();
                break;
        }
        return true;
    }

    @Override
    protected boolean createNAsync(int iUseCaseID, final List<?> list, final BaseSQLiteEvent event) {
        log.info("正在进行BrandPresenter的createNAsync，list=" + (list == null ? null : list.toString()));

        switch (iUseCaseID) {
            default:
                new Thread() {
                    @Override
                    public void run() {
//                        event.setEventTypeSQLite(BaseSQLiteEvent.EnumSQLiteEventType.ESET_Brand_CreateNAsync);
                        //
                        globalWriteLock.writeLock().lock();
                        List<Brand> brands = new ArrayList<Brand>();
                        try {
                            for (int i = 0; i < list.size(); i++) {
                                ((Brand) list.get(i)).setSyncType(BasePresenter.SYNC_Type_C);
                                ((Brand) list.get(i)).setSyncDatetime(new Date(System.currentTimeMillis() + NtpHttpBO.TimeDifference));
                                 Brand b = (Brand) createSync(iUseCaseID, ((Brand) list.get(i)));
                                 if (b != null) {
                                     brands.add(b);
                                 }
                            }
                            if (brands.size() == list.size()) {
                                event.setLastErrorCode(ErrorInfo.EnumErrorCode.EC_NoError);
                            } else {
                                event.setLastErrorCode(ErrorInfo.EnumErrorCode.EC_PartSuccess);
                            }
                        } catch (Exception e) {
                            log.info("创建Brand时出错，错误信息：" + e.getMessage());
                            event.setLastErrorCode(ErrorInfo.EnumErrorCode.EC_OtherError);
                        }
                        //
                        event.setListMasterTable(brands);
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
        log.info("正在进行BrandPresenter的createAsync，bm=" + (bm == null ? null : bm.toString()));

        switch (iUseCaseID) {
            default:
                new Thread() {
                    @Override
                    public void run() {
//                        event.setEventTypeSQLite(BaseSQLiteEvent.EnumSQLiteEventType.ESET_Brand_CreateAsync);
                        //
                        globalWriteLock.writeLock().lock();
                        try {
                            bm.setSyncType(BasePresenter.SYNC_Type_C);
                            bm.setSyncDatetime(Constants.getDefaultSyncDatetime2());
                            brandMapper.create((Brand) bm);

                            String sql;
                            if (bm.getID() != null) {
                                sql = "WHERE F_ID = " + bm.getID();
                            } else {
                                sql = "WHERE F_ID = (SELECT MAX(F_ID) FROM T_Brand)";
                            }
                            Query query = entityManager.createNativeQuery(QUERY_Brand_TABLE + sql, Brand.class);
                            List<Brand> brands = query.getResultList();
                            if (brands != null && brands.size() > 0) {
                                event.setLastErrorCode(ErrorInfo.EnumErrorCode.EC_NoError);
                                event.setBaseModel1(brands.get(0));
                            } else {
                                event.setLastErrorCode(ErrorInfo.EnumErrorCode.EC_OtherError);
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                            log.error("创建Brand时出错，错误信息：" + e.getMessage());
                            event.setLastErrorCode(ErrorInfo.EnumErrorCode.EC_OtherError);
                        }
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
        log.info("正在进行BrandPresenter的deleteAsync，bm=" + (bm == null ? null : bm.toString()));

        switch (iUseCaseID) {
            default:
                new Thread() {
                    @Override
                    public void run() {
//                        event.setEventTypeSQLite(BaseSQLiteEvent.EnumSQLiteEventType.ESET_Brand_DeleteAsync);
                        //
                        globalWriteLock.writeLock().lock();
                        try {
                            brandMapper.delete(bm.getID());
                            event.setLastErrorCode(ErrorInfo.EnumErrorCode.EC_NoError);
                        } catch (Exception e) {
                            log.error("删除Brand时出错，错误信息：" + e.getMessage());
                            event.setLastErrorCode(ErrorInfo.EnumErrorCode.EC_OtherError);
                        }
                        //
                        event.setBaseModel1(bm);
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
    protected boolean updateAsync(int iUseCaseID, final BaseModel bm, final BaseSQLiteEvent event) {
        switch (iUseCaseID) {
            default:
                new Thread() {
                    @Override
                    public void run() {
//                        event.setEventTypeSQLite(BaseSQLiteEvent.EnumSQLiteEventType.ESET_Brand_UpdateAsync);
                        //
                        globalWriteLock.writeLock().lock();
                        try {
                            bm.setSyncDatetime(new Date(System.currentTimeMillis() + NtpHttpBO.TimeDifference));
                            bm.setSyncType(BasePresenter.SYNC_Type_U);
                            brandMapper.save((Brand) bm);
                            event.setLastErrorCode(ErrorInfo.EnumErrorCode.EC_NoError);
                        } catch (Exception e) {
                            log.info("更新Brand时出错，错误信息：" + e.getMessage());
                            event.setLastErrorCode(ErrorInfo.EnumErrorCode.EC_OtherError);
                        }
                        //
                        event.setBaseModel1(bm);
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
    protected String getTableName() {
        if (Brand.class.isAnnotationPresent(Table.class)) {
            Table annotation = Brand.class.getAnnotation(Table.class);
            return annotation.name();
        } else {
            log.error("无法获取表名，未给" + Brand.class.getSimpleName() + "添加@Table。");
        }
        return "";
    }

    @Override
    protected String getQueryTable() {
        return QUERY_Brand_TABLE;
    }

    @Override
    public void createTableSync() {
        brandMapper.createTable();
    }
}
