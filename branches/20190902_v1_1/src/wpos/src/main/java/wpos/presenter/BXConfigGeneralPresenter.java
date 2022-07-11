package wpos.presenter;


import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Component;
import wpos.utils.EventBus;
import wpos.bo.BaseSQLiteBO;
import wpos.dao.BXConfigGeneralMapper;
import wpos.event.BaseEvent;
import wpos.event.UI.BaseSQLiteEvent;
import wpos.model.*;

import javax.annotation.Resource;
import javax.persistence.Query;
import javax.persistence.Table;
import java.util.ArrayList;
import java.util.List;

@Component("bXConfigGeneralPresenter")
public class BXConfigGeneralPresenter extends BasePresenter {
    private Log log = LogFactory.getLog(this.getClass());

    @Resource
    protected BXConfigGeneralMapper bxConfigGeneralMapper;

    public static final String QUERY_BXConfigGeneral_TABLE = "SELECT F_ID,F_Name,F_Value,F_SyncDatetime FROM T_BXConfigGeneral ";

//    protected static ReentrantReadWriteLock lock = new ReentrantReadWriteLock();

    //    public BXConfigGeneralPresenter(BXConfigGeneralMapper bxConfigGeneralMapper) {
//        super(bxConfigGeneralMapper);
//    }

    @Override
    public void createTableSync() {
        bxConfigGeneralMapper.createTable();
    }

    @Override
    public boolean refreshByServerDataAsyncC(final int iUseCaseID, final List<?> bmNewList, final BaseSQLiteEvent event) {
        log.info("正在进行BXConfigGeneralPresenter的refreshByServerDataAsyncC，bmNewList=" + (bmNewList == null ? null : bmNewList.toString()));

        event.setStatus(BaseEvent.EnumEventStatus.EES_SQLite_ToApplyServerData);
        switch (iUseCaseID) {
            default:
                new Thread() {
                    @Override
                    public void run() {
                        log.info("已经得到服务器返回的需要进行同步的ConfigGeneral数据，准备进行同步！");
                        globalWriteLock.writeLock().lock();
                        try {
                            List<BXConfigGeneral> bxConfigGeneralList = (List<BXConfigGeneral>) bmNewList;
                            //根据返回的数据，查找本地数据库中有没有记录的F_Name与之相同，若有，则删除再进行插入。若无，直接插入
                            for (int i = 0; i < bxConfigGeneralList.size(); i++) {
                                BXConfigGeneral c = new BXConfigGeneral();
                                c.setSql("where F_Name = '%s'");
                                c.setConditions(new String[]{bxConfigGeneralList.get(i).getName()});
                                String sql = String.format(c.getSql(), c.getConditions());
//                                List<BXConfigGeneral> cList = dao.getBXConfigGeneralDao().queryRaw(c.getSql(), c.getConditions());
                                Query query = entityManager.createNativeQuery(QUERY_BXConfigGeneral_TABLE + sql, BXConfigGeneral.class);
                                List<BXConfigGeneral> cList = query.getResultList();
//                                List<BXConfigGeneral> cList = new ArrayList<BXConfigGeneral>();
                                if (cList != null) {
                                    for (int j = 0; j < cList.size(); j++) {
                                        bxConfigGeneralMapper.delete(cList.get(j).getID());
                                    }
                                }
                                bxConfigGeneralMapper.create(bxConfigGeneralList.get(i));

                                event.setLastErrorCode(ErrorInfo.EnumErrorCode.EC_NoError);
                                event.setListMasterTable(bmNewList);
                            }
                        } catch (Exception e) {
                            log.error("执行refreshByServerDataAsyncC失败，错误信息为" + e.getMessage());
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
    protected BaseModel createSync(int iUseCaseID, BaseModel bm) {
        log.info("正在进行BXConfigGeneralPresenter的createSync，bm=" + (bm == null ? null : bm.toString()));

        globalWriteLock.writeLock().lock();
        BXConfigGeneral bxConfigGeneral = null;
        try {
            bxConfigGeneralMapper.create((BXConfigGeneral) bm);

            String sql;
            if (bm.getID() != null) {
                sql = "WHERE F_ID = " + bm.getID();
            } else {
                sql = "WHERE F_ID = (SELECT MAX(F_ID) FROM T_BXConfigGeneral)";
            }
            Query query = entityManager.createNativeQuery(QUERY_BXConfigGeneral_TABLE + sql, BXConfigGeneral.class);
            List<BXConfigGeneral> bxConfigGenerals = query.getResultList();
            if (bxConfigGenerals != null && bxConfigGenerals.size() > 0) {
                lastErrorCode = ErrorInfo.EnumErrorCode.EC_NoError;
                bxConfigGeneral = bxConfigGenerals.get(0);
            } else {
                lastErrorCode = ErrorInfo.EnumErrorCode.EC_OtherError;
            }
        } catch (Exception e) {
            log.error("执行createSync失败，错误信息为" + e.getMessage());
            e.printStackTrace();
            lastErrorCode = ErrorInfo.EnumErrorCode.EC_OtherError;
        }
        globalWriteLock.writeLock().unlock();
        return bxConfigGeneral;
    }

    @Override
    protected BaseModel retrieve1Sync(int iUseCaseID, BaseModel bm) {
        log.info("正在进行BXConfigGeneralPresenter的retrieve1Sync，bm=" + (bm == null ? null : bm.toString()));

        try {
            lastErrorCode = ErrorInfo.EnumErrorCode.EC_NoError;

            return bxConfigGeneralMapper.findOne(bm.getID());
        } catch (Exception e) {
            log.error("执行retrieve1Sync失败，错误信息为" + e.getMessage());
            e.printStackTrace();
            lastErrorCode = ErrorInfo.EnumErrorCode.EC_OtherError;

            return null;
        }
    }

    @Override
    protected List<?> retrieveNSync(int iUseCaseID, BaseModel bm) {
        log.info("正在进行BXConfigGeneralPresenter的retrieveNSync，bm=" + (bm == null ? null : bm.toString()));

        switch (iUseCaseID) {
            case BaseSQLiteBO.CASE_BXConfigGeneral_RetrieveNByConditions:
                try {
                    lastErrorCode = ErrorInfo.EnumErrorCode.EC_NoError;
                    String sql = String.format(bm.getSql(), bm.getConditions());
                    Query query = entityManager.createNativeQuery(QUERY_BXConfigGeneral_TABLE + sql, BXConfigGeneral.class);
                    return query.getResultList();
                } catch (Exception e) {
                    log.info("执行retrieveNSync的CASE_BXConfigGeneral_RetrieveNByConditions失败，错误信息为" + e.getMessage());

                    e.printStackTrace();
                    lastErrorCode = ErrorInfo.EnumErrorCode.EC_OtherError;

                    return null;
                }
            default:
                try {
                    lastErrorCode = ErrorInfo.EnumErrorCode.EC_NoError;

                    return bxConfigGeneralMapper.findAll();
                } catch (Exception e) {
                    log.info("执行retrieveNSync失败，错误信息为" + e.getMessage());
                    e.printStackTrace();
                    lastErrorCode = ErrorInfo.EnumErrorCode.EC_OtherError;

                    return null;
                }
        }
    }

    @Override
    protected List<BaseModel> createNSync(int iUseCaseID, final List<?> list) {
        log.info("正在进行BXConfigGeneralPresenter的createNSync，list=" + (list == null ? null : list.toString()));

        switch (iUseCaseID) {
            default:
                globalWriteLock.writeLock().lock();
                List<BXConfigGeneral> bxConfigGenerals = new ArrayList<BXConfigGeneral>();
                try {
                    for (Object object : list) {
                        BXConfigGeneral bxConfigGeneral = (BXConfigGeneral) createSync(iUseCaseID, (BaseModel) object);
                        if (bxConfigGeneral != null) {
                            bxConfigGenerals.add(bxConfigGeneral);
                        }
                    }
                    if (bxConfigGenerals.size() == list.size()) {
                        lastErrorCode = ErrorInfo.EnumErrorCode.EC_NoError;
                    } else {
                        lastErrorCode = ErrorInfo.EnumErrorCode.EC_PartSuccess;
                    }
                } catch (Exception e) {
                    log.info("执行createNSync失败，错误信息为" + e.getMessage());
                    e.printStackTrace();
                    lastErrorCode = ErrorInfo.EnumErrorCode.EC_OtherError;
                }
                globalWriteLock.writeLock().unlock();
                return (List<BaseModel>) list;
        }
    }

    @Override
    protected BaseModel deleteNSync(int iUserCaseID, BaseModel bm) {
        log.info("正在进行BXConfigGeneralPresenter的deleteNSync，bm=" + (bm == null ? null : bm.toString()));
        globalWriteLock.writeLock().lock();
        try {
//            dao.getBXConfigGeneralDao().deleteAll();
            bxConfigGeneralMapper.deleteAll();
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
    protected String getQueryTable() {
        return QUERY_BXConfigGeneral_TABLE;
    }

    @Override
    protected String getTableName() {
        if (BXConfigGeneral.class.isAnnotationPresent(Table.class)) {
            Table annotation = BXConfigGeneral.class.getAnnotation(Table.class);
            return annotation.name();
        } else {
            log.error("无法获取表名，未给" + BXConfigGeneral.class.getSimpleName() + "添加@Table。");
        }
        return "";
    }

}
