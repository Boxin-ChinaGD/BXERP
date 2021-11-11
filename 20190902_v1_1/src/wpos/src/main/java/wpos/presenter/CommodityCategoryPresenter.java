package wpos.presenter;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Component;
import wpos.utils.EventBus;
import wpos.bo.BaseSQLiteBO;
import wpos.bo.NtpHttpBO;
import wpos.dao.CommodityCategoryMapper;
import wpos.event.BaseEvent;
import wpos.event.UI.BaseSQLiteEvent;
import wpos.model.*;

import javax.annotation.Resource;
import javax.persistence.Query;
import javax.persistence.Table;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static wpos.bo.BaseSQLiteBO.CASE_Category_RetrieveNByConditions;

@Component("commodityCategoryPresenter")
public class CommodityCategoryPresenter extends BasePresenter {
    private Log log = LogFactory.getLog(this.getClass());

    @Resource
    protected CommodityCategoryMapper commodityCategoryMapper;

//    protected static ReentrantReadWriteLock lock = new ReentrantReadWriteLock();

    public final static String QUERY_CommodityCategory_TABLE = "SELECT F_ID,F_Name,F_ParentID,F_CreateDatetime,F_UpdateDatetime,F_SyncType,F_SyncDatetime FROM T_CommodityCategory ";
//    public CommodityCategoryPresenter(final CommodityCategoryMapper commodityCategoryMapper) {
//        super(commodityCategoryMapper);
//    }

    @Override
    public void createTableSync() {
        commodityCategoryMapper.createTable();
    }

    @Override
    protected List<BaseModel> createNSync(int iUseCaseID, final List<?> list) {
        log.info("正在进行CommodityCategoryPresenter的createNSync，list=" + (list == null ? null : list.toString()));

        switch (iUseCaseID) {
            default:
                globalWriteLock.writeLock().lock();
                List<BaseModel> baseModels = new ArrayList<BaseModel>();
                try {
                    for (Object object : list) {
                        BaseModel bm = createSync(iUseCaseID, (BaseModel) object);
                        if (bm != null) {
                            baseModels.add(bm);
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
    protected BaseModel createSync(int iUseCaseID, BaseModel bm) {
        log.info("正在进行CommodityCategoryPresenter的createSync，bm=" + (bm == null ? null : bm.toString()));
        globalWriteLock.writeLock().lock();
        BaseModel category = null;
        try {
            bm.setSyncType(BasePresenter.SYNC_Type_C);
            bm.setSyncDatetime(new Date(System.currentTimeMillis() + NtpHttpBO.TimeDifference));
            commodityCategoryMapper.create((CommodityCategory) bm);

            String sql;
            if (bm.getID() != null) {
                sql = "WHERE F_ID = " + bm.getID();
            } else {
                sql = "WHERE F_ID = (SELECT MAX(F_ID) FROM T_CommodityCategory)";
            }
            Query query = entityManager.createNativeQuery(QUERY_CommodityCategory_TABLE + sql, CommodityCategory.class);
            List<CommodityCategory> commodityCategories = query.getResultList();
            if (commodityCategories != null && commodityCategories.size() > 0) {
                lastErrorCode = ErrorInfo.EnumErrorCode.EC_NoError;
                category = commodityCategories.get(0);
            } else {
                lastErrorCode = ErrorInfo.EnumErrorCode.EC_OtherError;
            }
        } catch (Exception e) {
            log.error("执行createSync失败，错误信息为" + e.getMessage());
            e.printStackTrace();
            lastErrorCode = ErrorInfo.EnumErrorCode.EC_OtherError;
        }
        globalWriteLock.writeLock().unlock();
        return category;
    }

    @Override
    protected boolean updateSync(int iUseCaseID, BaseModel bm) {
        log.info("正在进行CommodityCategoryPresenter的updateSync，bm=" + (bm == null ? null : bm.toString()));
        globalWriteLock.writeLock().lock();
        try {
            bm.setSyncDatetime(new Date(System.currentTimeMillis() + NtpHttpBO.TimeDifference));
            bm.setSyncType(BasePresenter.SYNC_Type_U);
            commodityCategoryMapper.save((CommodityCategory) bm);
            lastErrorCode = ErrorInfo.EnumErrorCode.EC_NoError;
            globalWriteLock.writeLock().unlock();
            return true;
        } catch (Exception e) {
            log.error("执行updateSync失败，错误信息为" + e.getMessage());
            e.printStackTrace();
            lastErrorCode = ErrorInfo.EnumErrorCode.EC_NoError;
            globalWriteLock.writeLock().unlock();
            return false;
        }
    }

    @Override
    protected BaseModel retrieve1Sync(int iUseCaseID, BaseModel bm) {
        log.info("正在进行CommodityCategoryPresenter的retrieve1Sync，bm=" + (bm == null ? null : bm.toString()));

        try {
            lastErrorCode = ErrorInfo.EnumErrorCode.EC_NoError;

            return commodityCategoryMapper.findOne(bm.getID());
        } catch (Exception e) {
            log.error("执行retrieve1Sync失败，错误信息为" + e.getMessage());
            e.printStackTrace();
            lastErrorCode = ErrorInfo.EnumErrorCode.EC_OtherError;

            return null;
        }
    }

    @Override
    protected List<?> retrieveNSync(int iUseCaseID, BaseModel bm) {
        log.info("正在进行CommodityCategoryPresenter的retrieveNSync，bm=" + (bm == null ? null : bm.toString()));

        switch (iUseCaseID) {
            case CASE_Category_RetrieveNByConditions:
                try {
                    lastErrorCode = ErrorInfo.EnumErrorCode.EC_NoError;
                    String sql = String.format(bm.getSql(), bm.getConditions());
                    Query query = entityManager.createNativeQuery(QUERY_CommodityCategory_TABLE + sql, CommodityCategory.class);
                    return query.getResultList();
//                    return dao.getCommodityCategoryDao().queryRaw(bm.getSql(), bm.getConditions());
                } catch (Exception e) {
                    log.info("执行retrieveNSync的CASE_Category_RetrieveNByConditions分支时出现异常，错误信息为" + e.getMessage());
                    e.printStackTrace();
                    lastErrorCode = ErrorInfo.EnumErrorCode.EC_OtherError;

                    return null;
                }
            default:
                try {
                    lastErrorCode = ErrorInfo.EnumErrorCode.EC_NoError;

                    return commodityCategoryMapper.findAll();
                } catch (Exception e) {
                    log.error("执行retrieveNSync失败，错误信息为" + e.getMessage());
                    e.printStackTrace();
                    lastErrorCode = ErrorInfo.EnumErrorCode.EC_NoError;

                    return null;
                }
        }
    }

    @Override
    protected BaseModel deleteSync(int iUseCaseID, BaseModel bm) {
        log.info("正在进行CommodityCategoryPresenter的deleteSync，bm=" + (bm == null ? null : bm.toString()));
        globalWriteLock.writeLock().lock();
        try {
            commodityCategoryMapper.delete(bm.getID());
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
    protected BaseModel deleteNSync(int iUserCaseID, BaseModel bm) {
        log.info("正在进行CommodityCategoryPresenter的deleteNSync，bm=" + (bm == null ? null : bm.toString()));
        globalWriteLock.writeLock().lock();
        try {
//            dao.getCommodityCategoryDao().deleteAll();
            commodityCategoryMapper.deleteAllInBatch();
            lastErrorCode = ErrorInfo.EnumErrorCode.EC_NoError;
        } catch (Exception e) {
            log.error("执行deleteNSync时失败，错误信息为" + e.getMessage());
            e.printStackTrace();
            lastErrorCode = ErrorInfo.EnumErrorCode.EC_OtherError;
        }
        globalWriteLock.writeLock().unlock();
        return null;
    }

    @Override
    protected boolean refreshByServerDataAsync(final int iUseCaseID, final List<?> bmNewList, final BaseSQLiteEvent event) {
        log.info("正在进行CommodityCategoryPresenter的refreshByServerDataAsync，bmNewList=" + (bmNewList == null ? null : bmNewList.toString()));

        event.setStatus(BaseEvent.EnumEventStatus.EES_SQLite_ToApplyServerData);
        switch (iUseCaseID) {
            default:
                new Thread() {
                    @Override
                    public void run() {
                        globalWriteLock.writeLock().lock();
                        log.info("已经得到服务器返回的需要同步的Category数据, 准备进行同步...");
                        List<BaseModel> bmList = (List<BaseModel>) bmNewList;
                        try {
                            if (bmList != null) {
                                for (int i = 0; i < bmList.size(); i++) {
                                    String CategoryType = bmList.get(i).getSyncType();

                                    if (BasePresenter.SYNC_Type_C.equals(CategoryType)) {
                                        CommodityCategory commodityCategory = (CommodityCategory) retrieve1Sync(iUseCaseID, bmList.get(i));
                                        if (commodityCategory != null) {
                                            deleteSync(BaseSQLiteBO.INVALID_CASE_ID, commodityCategory);
                                        }
                                        //
                                        createSync(iUseCaseID, bmList.get(i));

                                        log.info("服务器返回的C型Category数据成功同步到SQLite中!");
                                    } else if (BasePresenter.SYNC_Type_U.equals(CategoryType)) {
                                        updateSync(iUseCaseID, bmList.get(i));

                                        log.info("服务器返回的U型Category数据成功同步到SQLite中!");
                                    } else if (BasePresenter.SYNC_Type_D.equals(CategoryType)) {
                                        CommodityCategory commodityCategory = (CommodityCategory) retrieve1Sync(iUseCaseID, bmList.get(i));
                                        if (commodityCategory != null) {
                                            deleteSync(BaseSQLiteBO.INVALID_CASE_ID, commodityCategory);
                                        }

                                        log.info("服务器返回的D型Category数据成功同步到SQLite中!");
                                    }
                                    event.setLastErrorCode(ErrorInfo.EnumErrorCode.EC_NoError);
                                    event.setListMasterTable(bmList);
                                }
                            } else {
                                event.setLastErrorCode(ErrorInfo.EnumErrorCode.EC_NoError);
                                event.setListMasterTable(bmList);
                            }
                        } catch (Exception e) {
                            log.info("同步Category失败，错误信息为" + e.getMessage());
                            e.printStackTrace();
                            event.setLastErrorCode(ErrorInfo.EnumErrorCode.EC_OtherError);
                        }
                        event.setStatus(BaseEvent.EnumEventStatus.EES_SQLite_DoneApplyServerData);
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
    protected boolean refreshByServerDataAsyncC(final int iUseCaseID, final List<?> bmNewList, final BaseSQLiteEvent event) {
        log.info("正在进行CommodityCategoryPresenter的refreshByServerDataAsyncC，bmNewList=" + (bmNewList == null ? null : bmNewList.toString()));

        event.setStatus(BaseEvent.EnumEventStatus.EES_SQLite_ToApplyServerData);
        switch (iUseCaseID) {
            default:
                new Thread() {
                    @Override
                    public void run() {
                        globalWriteLock.writeLock().lock();
                        log.info("已经得到服务器返回的需要同步的Category数据, 准备进行同步...");

//                        event.setEventTypeSQLite(BaseSQLiteEvent.EnumSQLiteEventType.ESET_CommodityCategory_RefreshByServerDataAsyncC_Done);

                        //删除本地所有的数据
                        deleteNSync(iUseCaseID, null);
                        if (getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError) {
                            //同步所有服务器返回的数据
                            createNSync(iUseCaseID, bmNewList);
                            if (getLastErrorCode() != ErrorInfo.EnumErrorCode.EC_NoError) {
                                event.setLastErrorCode(ErrorInfo.EnumErrorCode.EC_OtherError);
                            } else {
                                event.setLastErrorCode(ErrorInfo.EnumErrorCode.EC_NoError);
                            }

                        } else {
                            event.setLastErrorCode(ErrorInfo.EnumErrorCode.EC_OtherError);
                        }
                        event.setStatus(BaseEvent.EnumEventStatus.EES_SQLite_DoneApplyServerData);
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
    protected boolean createNAsync(int iUseCaseID, final List<?> list, final BaseSQLiteEvent event) {
        log.info("正在进行CommodityCategoryPresenter的createNAsync，list=" + (list == null ? null : list.toString()));

        switch (iUseCaseID) {
            default:
                new Thread() {
                    @Override
                    public void run() {
//                        event.setEventTypeSQLite(BaseSQLiteEvent.EnumSQLiteEventType.ESET_CommodityCategory_CreateNAsync);
                        //
                        globalWriteLock.writeLock().lock();
                        List<BaseModel> commodityCategories = new ArrayList<BaseModel>();
                        try {
                            for (int i = 0; i < list.size(); i++) {
                                CommodityCategory c = (CommodityCategory) list.get(i);
                                c.setSyncDatetime(new Date(System.currentTimeMillis() + NtpHttpBO.TimeDifference));
                                c.setSyncType(BasePresenter.SYNC_Type_C);
                            }

                            commodityCategories = createNSync(iUseCaseID, list);
                            event.setLastErrorCode(lastErrorCode);
                        } catch (Exception e) {
                            log.error("创建Category时出错，错误信息：" + e.getMessage());
                            event.setLastErrorCode(ErrorInfo.EnumErrorCode.EC_OtherError);
                        }
                        //
                        event.setListMasterTable(commodityCategories);
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
    protected boolean createAsync(int iUseCaseID, final BaseModel bm, final BaseSQLiteEvent event) {
        log.info("正在进行CommodityCategoryPresenter的createAsync，bm=" + (bm == null ? null : bm.toString()));

        switch (iUseCaseID) {
            default:
                new Thread() {
                    @Override
                    public void run() {
//                        event.setEventTypeSQLite(BaseSQLiteEvent.EnumSQLiteEventType.ESET_CommodityCategory_CreateAsync);
                        //
                        globalWriteLock.writeLock().lock();
                        try {
                            bm.setSyncType(BasePresenter.SYNC_Type_C);
                            bm.setSyncDatetime(new Date(System.currentTimeMillis() + NtpHttpBO.TimeDifference));
                            CommodityCategory commodityCategoryCreate = (CommodityCategory) createSync(iUseCaseID, bm);
                            event.setLastErrorCode(lastErrorCode);
                            event.setBaseModel1(commodityCategoryCreate);
                        } catch (Exception e) {
                            log.error("创建Category时出错，错误信息：" + e.getMessage());
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
        log.info("正在进行CommodityCategoryPresenter的deleteAsync，bm=" + (bm == null ? null : bm.toString()));

        switch (iUseCaseID) {
            default:
                new Thread() {
                    @Override
                    public void run() {
                        globalWriteLock.writeLock().lock();
                        event.setEventTypeSQLite(BaseSQLiteEvent.EnumSQLiteEventType.ESET_CommodityCategory_DeleteAsync);
                        //
                        try {
                            commodityCategoryMapper.delete(bm.getID());
                            event.setLastErrorCode(ErrorInfo.EnumErrorCode.EC_NoError);
                        } catch (Exception e) {
                            log.error("删除Category时出错，错误信息：" + e.getMessage());
                            event.setLastErrorCode(ErrorInfo.EnumErrorCode.EC_OtherError);
                        }
                        //
                        event.setBaseModel1(bm);
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
    protected boolean updateAsync(int iUseCaseID, final BaseModel bm, final BaseSQLiteEvent event) {
        log.info("正在进行CommodityCategoryPresenter的updateAsync，bm=" + (bm == null ? null : bm.toString()));

        switch (iUseCaseID) {
            default:
                new Thread() {
                    @Override
                    public void run() {
                        globalWriteLock.writeLock().lock();
                        event.setEventTypeSQLite(BaseSQLiteEvent.EnumSQLiteEventType.ESET_CommodityCategory_UpdateAsync);
                        //
                        try {
                            bm.setSyncType(BasePresenter.SYNC_Type_U);
                            bm.setSyncDatetime(new Date(System.currentTimeMillis() + NtpHttpBO.TimeDifference));
                            commodityCategoryMapper.save((CommodityCategory) bm);
                            event.setLastErrorCode(ErrorInfo.EnumErrorCode.EC_NoError);
                        } catch (Exception e) {
                            log.info("更新Category时出错，错误信息：" + e.getMessage());
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
    protected boolean retrieveNAsync(int iUseCaseID, final BaseModel bm, final BaseSQLiteEvent event) {
        log.info("正在进行CommodityCategoryPresenter的retrieveNAsync，bm=" + (bm == null ? null : bm.toString()));

        switch (iUseCaseID) {
            default:
                new Thread() {
                    @Override
                    public void run() {
                        List<CommodityCategory> result = null;
                        event.setEventTypeSQLite(BaseSQLiteEvent.EnumSQLiteEventType.ESET_CommodityCategory_RetrieveNAsync);
                        try {
                            Thread.sleep(1); //令event.setID()的ID惟一
                            result = commodityCategoryMapper.findAll();
                            event.setLastErrorCode(ErrorInfo.EnumErrorCode.EC_NoError);
                        } catch (Exception e) {
                            log.info("查询SQLite的表Category的所有记录时出错，错误信息：" + e.getMessage());
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
    protected String getQueryTable() {
        return QUERY_CommodityCategory_TABLE;
    }

    @Override
    protected String getTableName() {
        if (CommodityCategory.class.isAnnotationPresent(Table.class)) {
            Table annotation = CommodityCategory.class.getAnnotation(Table.class);
            return annotation.name();
        } else {
            log.error("无法获取表名，未给" + CommodityCategory.class.getSimpleName() + "添加@Table。");
        }
        return "";
    }
}
