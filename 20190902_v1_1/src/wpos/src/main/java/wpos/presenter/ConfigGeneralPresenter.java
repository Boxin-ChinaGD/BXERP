
package wpos.presenter;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Component;
import wpos.utils.EventBus;
import wpos.bo.BaseSQLiteBO;
import wpos.dao.ConfigGeneralMapper;
import wpos.event.BaseEvent;
import wpos.event.UI.BaseSQLiteEvent;
import wpos.model.BaseModel;
import wpos.model.ConfigGeneral;
import wpos.model.ErrorInfo;

import javax.annotation.Resource;
import javax.persistence.Query;
import javax.persistence.Table;
import java.util.ArrayList;
import java.util.List;

import static wpos.bo.BaseSQLiteBO.CASE_ConfigGeneral_UpdateByServerData;

@Component("configGeneralPresenter")
public class ConfigGeneralPresenter extends BasePresenter {
    private Log log = LogFactory.getLog(this.getClass());

    @Resource
    private ConfigGeneralMapper configGeneralMapper;

//    protected static ReentrantReadWriteLock lock = new ReentrantReadWriteLock();

    //    public ConfigGeneralPresenter(ConfigGeneralMapper configGeneralMapper) {
//        super(configGeneralMapper);
//    }
    public static final String QUERY_ConfigGeneral_TABLE = "SELECT F_ID,F_Name,F_Value FROM T_ConfigGeneral ";

    @Override
    public void createTableSync() {
        configGeneralMapper.createTable();
    }

    @Override
    protected BaseModel createSync(int iUseCaseID, BaseModel bm) {
        log.info("正在进行ConfigGeneralPresenter的createSync，bm=" + (bm == null ? null : bm.toString()));

        globalWriteLock.writeLock().lock();
        BaseModel baseModel = null;
        try {
            configGeneralMapper.create((ConfigGeneral) bm);
            String sql;
            if (bm.getID() != null) {
                sql = "WHERE F_ID = " + bm.getID();
            } else {
                sql = "WHERE F_ID = (SELECT MAX(F_ID) FROM T_ConfigGeneral)";
            }
            Query query = entityManager.createNativeQuery(QUERY_ConfigGeneral_TABLE + sql, ConfigGeneral.class);
            List<ConfigGeneral> configGenerals = query.getResultList();
            if (configGenerals != null && configGenerals.size() > 0) {
                lastErrorCode = ErrorInfo.EnumErrorCode.EC_NoError;
                baseModel = configGenerals.get(0);
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
        log.info("正在进行ConfigGeneralPresenter的retrieve1Sync，bm=" + (bm == null ? null : bm.toString()));

        try {
            lastErrorCode = ErrorInfo.EnumErrorCode.EC_NoError;

            return configGeneralMapper.findOne(bm.getID());
        } catch (Exception e) {
            log.error("执行retrieve1Sync失败，错误信息为" + e.getMessage());
            e.printStackTrace();
            lastErrorCode = ErrorInfo.EnumErrorCode.EC_OtherError;

            return null;
        }
    }

    @Override
    protected List<?> retrieveNSync(int iUseCaseID, BaseModel bm) {
        log.info("正在进行ConfigGeneralPresenter的retrieveNSync，bm=" + (bm == null ? null : bm.toString()));

        switch (iUseCaseID) {
            case BaseSQLiteBO.CASE_ConfigGeneral_RetrieveNByConditions:
                try {
                    lastErrorCode = ErrorInfo.EnumErrorCode.EC_NoError;

                    String sql = String.format(bm.getSql(), bm.getConditions());
                    Query query = entityManager.createNativeQuery(QUERY_ConfigGeneral_TABLE + sql, ConfigGeneral.class);
                    return query.getResultList();
                } catch (Exception e) {
                    log.info("执行retrieveNSync的CASE_ConfigGeneral_RetrieveNByConditions失败，错误信息为" + e.getMessage());

                    e.printStackTrace();
                    lastErrorCode = ErrorInfo.EnumErrorCode.EC_OtherError;

                    return null;
                }
            default:
                try {
                    lastErrorCode = ErrorInfo.EnumErrorCode.EC_NoError;

                    return configGeneralMapper.findAll();
                } catch (Exception e) {
                    log.error("执行retrieveNSync失败，错误信息为" + e.getMessage());
                    e.printStackTrace();
                    lastErrorCode = ErrorInfo.EnumErrorCode.EC_OtherError;

                    return null;
                }
        }
    }

    @Override
    protected List<BaseModel> createNSync(int iUseCaseID, final List<?> list) {
        log.info("正在进行ConfigGeneralPresenter的createNSync，list=" + (list == null ? null : list.toString()));

        switch (iUseCaseID) {
            default:
                globalWriteLock.writeLock().lock();
                List<BaseModel> baseModels = new ArrayList<BaseModel>();
                try {
                    for (Object object : list) {
                        BaseModel baseModel = createSync(iUseCaseID, (BaseModel) object);
                        if (baseModel != null) {
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
    protected boolean updateAsync(int iUseCaseID, final BaseModel bm, final BaseSQLiteEvent event) {
        log.info("正在进行ConfigGeneralPresenter的updateAsync，bm=" + (bm == null ? null : bm.toString()));

        switch (iUseCaseID) {
            case CASE_ConfigGeneral_UpdateByServerData:
                (new Thread() {
                    @Override
                    public void run() {
                        globalWriteLock.writeLock().lock();
                        log.info("开始根据服务器返回的数据Update ConfigGeneral");
                        event.setSyncType(BasePresenter.SYNC_Type_U);
//                        event.setEventTypeSQLite(ESET_ConfigGeneral_UpdateByServerDataAsync);
                        //
                        try {
                            configGeneralMapper.save((ConfigGeneral) bm);
                            event.setLastErrorCode(ErrorInfo.EnumErrorCode.EC_NoError);
                            event.setBaseModel1(bm);
                            log.info("根据服务器返回的数据Update ConfigGeneral成功！");
                            event.setData("UPDATE_DONE");
                        } catch (Exception e) {
                            e.printStackTrace();
                            log.error("根据服务器返回的数据Update ConfigGeneral出错，错误信息：" + e.getMessage());
                            event.setLastErrorCode(ErrorInfo.EnumErrorCode.EC_OtherError);
                        }
                        //
                        event.setBaseModel1(bm);
                        event.setStatus(BaseEvent.EnumEventStatus.EES_SQLite_DoneApplyServerData);
                        //
                        EventBus.getDefault().post(event);
                        globalWriteLock.writeLock().unlock();
                    }
                }).start();
                break;
            default:
                (new Thread() {
                    @Override
                    public void run() {
                        log.info("开始update ConfigGeneral");
//                        event.setEventTypeSQLite(ESET_ConfigGeneral_UpdateAsync);
                        //
                        globalWriteLock.writeLock().lock();
                        try {
                            configGeneralMapper.save((ConfigGeneral) bm);
                            event.setLastErrorCode(ErrorInfo.EnumErrorCode.EC_NoError);
                            log.info("update ConfigGeneral成功");
                        } catch (Exception e) {
                            e.printStackTrace();
                            log.error("update ConfigGeneral出错，错误信息为：" + e.getMessage());
                            event.setLastErrorCode(ErrorInfo.EnumErrorCode.EC_OtherError);
                        }
                        //
                        event.setBaseModel1(bm);
                        event.setStatus(BaseEvent.EnumEventStatus.EES_SQLite_Done);
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
    public boolean refreshByServerDataAsyncC(final int iUseCaseID, final List<?> bmNewList, final BaseSQLiteEvent event) {
        log.info("正在进行ConfigGeneralPresenter的refreshByServerDataAsyncC，bmNewList=" + (bmNewList == null ? null : bmNewList.toString()));

        event.setStatus(BaseEvent.EnumEventStatus.EES_SQLite_ToApplyServerData);
        switch (iUseCaseID) {
            default:
                (new Thread() {
                    @Override
                    public void run() {
                        globalWriteLock.writeLock().lock();
                        log.info("已经得到服务器返回的需要进行同步的ConfigGeneral数据，准备进行同步！");

                        try {
                            List<ConfigGeneral> configGeneralList = (List<ConfigGeneral>) bmNewList;
                            //根据返回的数据，查找本地数据库中有没有记录的F_Name与之相同，若有，则删除再进行插入。若无，直接插入
                            for (int i = 0; i < configGeneralList.size(); i++) {
                                ConfigGeneral c = new ConfigGeneral();
                                c.setSql("where F_Name = '%s'");
                                c.setConditions(new String[]{configGeneralList.get(i).getName()});
//                                List<ConfigGeneral> cList = configGeneralMapper.queryRaw(c.getSql(), c.getConditions());
                                String sql = String.format(c.getSql(), c.getConditions());
                                Query query = entityManager.createNativeQuery(QUERY_ConfigGeneral_TABLE + sql, ConfigGeneral.class);
                                List<ConfigGeneral> cList = query.getResultList();
                                if (cList != null) {
                                    for (int j = 0; j < cList.size(); j++) {
                                        configGeneralMapper.delete(cList.get(j).getID());
                                    }
                                }
                                createSync(iUseCaseID, configGeneralList.get(i));

                                event.setLastErrorCode(lastErrorCode);
                                event.setListMasterTable(bmNewList);
                            }
                        } catch (Exception e) {
                            log.info("执行refreshByServerDataAsyncC失败，错误信息为" + e.getMessage());
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
    protected BaseModel deleteNSync(int iUserCaseID, BaseModel bm) {
        log.info("正在进行ConfigGeneralPresenter的deleteNSync，bm=" + (bm == null ? null : bm.toString()));
        globalWriteLock.writeLock().lock();
        try {
            configGeneralMapper.deleteAllInBatch();
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
    protected String getTableName() {
        if (ConfigGeneral.class.isAnnotationPresent(Table.class)) {
            Table annotation = ConfigGeneral.class.getAnnotation(Table.class);
            return annotation.name();
        } else {
            log.error("无法获取表名，未给" + ConfigGeneral.class.getSimpleName() + "添加@Table。");
        }
        return "";
    }

    @Override
    protected String getQueryTable() {
        return QUERY_ConfigGeneral_TABLE;
    }
}
