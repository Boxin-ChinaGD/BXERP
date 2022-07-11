package wpos.presenter;


import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Component;
import wpos.utils.EventBus;
import wpos.bo.BaseSQLiteBO;
import wpos.dao.ConfigCacheSizeMapper;
import wpos.event.BaseEvent;
import wpos.event.UI.BaseSQLiteEvent;
import wpos.model.BaseModel;
import wpos.model.ConfigCacheSize;
import wpos.model.ErrorInfo;

import javax.annotation.Resource;
import javax.persistence.Query;
import javax.persistence.Table;
import java.util.List;

@Component("configCacheSizePresenter")
public class ConfigCacheSizePresenter extends BasePresenter {
    private Log log = LogFactory.getLog(this.getClass());

    @Resource
    private ConfigCacheSizeMapper configCacheSizeMapper;

//    protected static ReentrantReadWriteLock lock = new ReentrantReadWriteLock();

    public static final String QUERY_ConfigCacheSize_TABLE = "SELECT F_ID,F_Name,F_Value FROM T_ConfigCacheSize ";

//    public ConfigCacheSizePresenter(final ConfigCacheSizeMapper configCacheSizeMapper) {
//        super(configCacheSizeMapper);
//    }

    @Override
    public void createTableSync() {
        configCacheSizeMapper.createTable();
    }

    @Override
    protected String getTableName() {
        if (ConfigCacheSize.class.isAnnotationPresent(Table.class)) {
            Table annotation = ConfigCacheSize.class.getAnnotation(Table.class);
            return annotation.name();
        } else {
            log.error("无法获取表名，未给" + ConfigCacheSize.class.getSimpleName() + "添加@Table。");
        }
        return "";
    }

    @Override
    protected String getQueryTable() {
        return QUERY_ConfigCacheSize_TABLE;
    }

    @Override
    protected BaseModel createSync(int iUseCaseID, BaseModel bm) {
        log.info("正在进行ConfigCacheSizePresenter的createSync，bm=" + (bm == null ? null : bm.toString()));

        globalWriteLock.writeLock().lock();
        BaseModel baseModel = null;
        try {
            configCacheSizeMapper.create((ConfigCacheSize) bm);

            String sql;
            if (bm.getID() != null) {
                sql = "WHERE F_ID = " + bm.getID();
            } else {
                sql = "WHERE F_ID = (SELECT MAX(F_ID) FROM T_ConfigCacheSize)";
            }
            Query query = entityManager.createNativeQuery(QUERY_ConfigCacheSize_TABLE + sql, ConfigCacheSize.class);
            List<ConfigCacheSize> configCacheSizes = query.getResultList();
            if (configCacheSizes != null && configCacheSizes.size() > 0) {
                lastErrorCode = ErrorInfo.EnumErrorCode.EC_NoError;
                baseModel = configCacheSizes.get(0);
            } else {
                lastErrorCode = ErrorInfo.EnumErrorCode.EC_OtherError;
            }
        } catch (Exception e) {
            log.error("执行createSync失败，错误信息为" + e.getMessage());
            e.printStackTrace();
            lastErrorCode = ErrorInfo.EnumErrorCode.EC_OtherError;
        }
        globalWriteLock.writeLock().unlock();
        return baseModel;
    }

    @Override
    protected BaseModel retrieve1Sync(int iUseCaseID, BaseModel bm) {
        log.info("正在进行ConfigCacheSizePresenter的retrieve1Sync，bm=" + (bm == null ? null : bm.toString()));

        try {
            lastErrorCode = ErrorInfo.EnumErrorCode.EC_NoError;
            return configCacheSizeMapper.findOne(bm.getID());
        } catch (Exception e) {
            log.error("执行retrieve1Sync失败，错误信息为" + e.getMessage());
            e.printStackTrace();
            lastErrorCode = ErrorInfo.EnumErrorCode.EC_OtherError;
            return null;
        }
    }

    @Override
    protected List<?> retrieveNSync(int iUseCaseID, BaseModel bm) {
        log.info("正在进行ConfigCacheSizePresenter的retrieveNSync，bm=" + (bm == null ? null : bm.toString()));

        switch (iUseCaseID) {
            case BaseSQLiteBO.CASE_ConfigCacheSize_RetrieveNByConditions:
                try {
                    lastErrorCode = ErrorInfo.EnumErrorCode.EC_NoError;
                    String sql = String.format(bm.getSql(), bm.getConditions());
                    Query query = entityManager.createNativeQuery(QUERY_ConfigCacheSize_TABLE + sql, ConfigCacheSize.class);
                    return query.getResultList();
                } catch (Exception e) {
                    log.info("执行retrieveNSync的CASE_ConfigCacheSize_RetrieveNByConditions失败，错误信息为" + e.getMessage());
                    e.printStackTrace();
                    lastErrorCode = ErrorInfo.EnumErrorCode.EC_OtherError;

                    return null;
                }
            default:
                try {
                    lastErrorCode = ErrorInfo.EnumErrorCode.EC_NoError;

                    return configCacheSizeMapper.findAll();
                } catch (Exception e) {
                    log.error("执行retrieveNSync失败，错误信息为" + e.getMessage());
                    e.printStackTrace();
                    lastErrorCode = ErrorInfo.EnumErrorCode.EC_OtherError;

                    return null;
                }
        }
    }

    @Override
    protected boolean refreshByServerDataAsync(final int iUseCaseID, final List<?> bmNewList, final BaseSQLiteEvent event) {
        log.info("正在进行ConfigCacheSizePresenter的refreshByServerDataAsync，bmNewList=" + bmNewList == null ? null : bmNewList.toString());

        event.setStatus(BaseEvent.EnumEventStatus.EES_SQLite_ToApplyServerData);
        switch (iUseCaseID) {
            default:
                (new Thread() {
                    @Override
                    public void run() {
                        globalWriteLock.writeLock().lock();
                        log.info("已经得到服务器返回的需要同步的ConfigCacheSize数据, 准备进行同步...");
                        List<BaseModel> bmList = (List<BaseModel>) bmNewList;
//                        event.setEventTypeSQLite(BaseSQLiteEvent.EnumSQLiteEventType.ESET_ConfigCacheSize_RefreshByServerDataAsync_Done);
                        try {
                            if (bmList != null) {
                                for (int i = 0; i < bmList.size(); i++) {

                                    if (bmList.get(i) != null && bmList.get(i).getID() != null) {
                                        deleteSync(BaseSQLiteBO.INVALID_CASE_ID, bmList.get(i));
                                    }
                                    //
                                    createSync(iUseCaseID, bmList.get(i));
                                    log.info("服务器返回的C型ConfigCacheSize数据成功同步到SQLite中!");
                                }
                            }
                            event.setLastErrorCode(ErrorInfo.EnumErrorCode.EC_NoError);
                            event.setListMasterTable(bmList);
                        } catch (Exception e) {
                            log.error("执行refreshByServerDataAsync失败，错误信息为" + e.getMessage());
                            e.printStackTrace();
                            event.setLastErrorCode(ErrorInfo.EnumErrorCode.EC_OtherError);
                        }
//                        event.setStatus(BaseEvent.EnumEventStatus.EES_SQLite_DoneApplyServerData);
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
    protected boolean refreshByServerDataAsyncC(final int iUseCaseID, final List<?> bmNewList, final BaseSQLiteEvent event) {
        log.info("正在进行ConfigCacheSizePresenter的refreshByServerDataAsyncC，bmNewList=" + bmNewList == null ? null : bmNewList.toString());

        event.setStatus(BaseEvent.EnumEventStatus.EES_SQLite_ToApplyServerData);
        switch (iUseCaseID) {
            default:
                (new Thread() {
                    @Override
                    public void run() {
                        log.info("已经得到服务器返回的需要进行同步的ConfigCacheSize数据，准备进行同步!");
                        globalWriteLock.writeLock().lock();
                        try {
                            List<ConfigCacheSize> configCacheSizeList = (List<ConfigCacheSize>) bmNewList;
                            //根据返回的数据，查找本地SQLite中有没有记录的F_Name与之相同，若有，则删除再进行插入。若无，直接插入
                            for (int i = 0; i < configCacheSizeList.size(); i++) {
                                ConfigCacheSize c = new ConfigCacheSize();
                                c.setSql("where F_Name = '%s'");
                                c.setConditions(new String[]{configCacheSizeList.get(i).getName()});
//                                List<ConfigCacheSize> cList = configCacheSizeMapper.queryRaw(c.getSql(), c.getConditions());
                                String sql = String.format(c.getSql(), c.getConditions());
                                Query query = entityManager.createNativeQuery(sql, ConfigCacheSize.class);
                                List<ConfigCacheSize> cList = query.getResultList();
                                if (cList != null) {
                                    for (int j = 0; j < cList.size(); j++) {
                                        configCacheSizeMapper.delete(cList.get(j).getID());
                                    }
                                }
                                configCacheSizeMapper.create(configCacheSizeList.get(i));

                                event.setLastErrorCode(ErrorInfo.EnumErrorCode.EC_NoError);
                                event.setListMasterTable(bmNewList);
                            }
                        } catch (Exception e) {
                            log.error("执行refreshByServerDataAsyncC失败，错误信息为" + e.getMessage());
                            e.printStackTrace();
                            event.setLastErrorCode(ErrorInfo.EnumErrorCode.EC_OtherError);
                        }
//                        event.setStatus(BaseEvent.EnumEventStatus.EES_SQLite_DoneApplyServerData);
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
    protected BaseModel deleteSync(int iUseCaseID, BaseModel bm) {
        log.info("正在进行ConfigCacheSizePresenter的deleteSync，bm=" + (bm == null ? null : bm.toString()));
        globalWriteLock.writeLock().lock();
        try {
            configCacheSizeMapper.delete(bm.getID());
            lastErrorCode = ErrorInfo.EnumErrorCode.EC_NoError;
        } catch (Exception e) {
            log.error("执行deleteSync失败，错误信息为" + e.getMessage());
            e.printStackTrace();
            lastErrorCode = ErrorInfo.EnumErrorCode.EC_OtherError;
        }
        globalWriteLock.writeLock().unlock();
        return null;
    }

}
