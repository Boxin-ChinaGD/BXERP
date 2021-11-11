package wpos.presenter;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Component;
import wpos.utils.EventBus;
import wpos.bo.BaseSQLiteBO;
import wpos.bo.NtpHttpBO;
import wpos.dao.PosMapper;
import wpos.event.BaseEvent;
import wpos.event.UI.BaseSQLiteEvent;
import wpos.helper.Constants;
import wpos.model.BaseModel;
import wpos.model.ErrorInfo;
import wpos.model.Pos;

import javax.annotation.Resource;
import javax.persistence.Query;
import javax.persistence.Table;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Component("posPresenter")
public class PosPresenter extends BasePresenter {
    private Log log = LogFactory.getLog(this.getClass());

    @Resource
    private PosMapper posMapper;

//    protected static ReentrantReadWriteLock lock = CompanyPresenter.lock;


    public static final String QUERY_Pos_TABLE = "SELECT F_ID,F_POS_SN,F_ShopID,F_pwdEncrypted,F_Salt,F_PasswordInPOS,F_Status,F_SyncDatetime,F_SyncType FROM T_Pos ";

//    public PosPresenter(PosMapper posMapper) {
//        super(posMapper);
//    }

    @Override
    public void createTableSync() {
        posMapper.createTable();
    }

    @Override
    protected List<BaseModel> createNSync(int iUseCaseID, final List<?> list) {
        log.info("正在进行PosPresenter的createNSync，list=" + (list == null ? null : list.toString()));

        switch (iUseCaseID) {
            default:
                globalWriteLock.writeLock().lock();
                List<BaseModel> baseModels = new ArrayList<BaseModel>();
                try {
                    for (int i = 0; i < list.size(); i++) {
                        ((Pos) list.get(i)).setSyncType(BasePresenter.SYNC_Type_C);
                        ((Pos) list.get(i)).setSyncDatetime(new Date(System.currentTimeMillis() + NtpHttpBO.TimeDifference));
                        BaseModel baseModel = createSync(iUseCaseID, ((Pos) list.get(i)));
                        if (baseModel != null) {
                            baseModels.add(baseModel);
                        }
                    }
//                    if (baseModels.size() == list.size()) {
//                        lastErrorCode = ErrorInfo.EnumErrorCode.EC_NoError;
//                    } else {
//                        lastErrorCode = ErrorInfo.EnumErrorCode.EC_PartSuccess;
//                    }
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
    public BaseModel createSync(int iUseCaseID, BaseModel bm) {
        log.info("正在进行PosPresenter的createSync，bm=" + bm);

        globalWriteLock.writeLock().lock();
        BaseModel pos = null;
        try {
            bm.setSyncDatetime(new Date(System.currentTimeMillis() + NtpHttpBO.TimeDifference));
            bm.setSyncType(BasePresenter.SYNC_Type_C);
            posMapper.create((Pos) bm);

            String sql;
            if (bm.getID() != null) {
                sql = "WHERE F_ID = " + bm.getID();
            } else {
                sql = "WHERE F_ID = (SELECT MAX(F_ID) FROM T_Pos)";
            }
            Query query = entityManager.createNativeQuery(QUERY_Pos_TABLE + sql, Pos.class);
            List<Pos> posList = query.getResultList();
            if (posList != null && posList.size() > 0) {
                lastErrorCode = ErrorInfo.EnumErrorCode.EC_NoError;
                bm.setID(posList.get(0).getID());
                pos = posList.get(0);
            } else {
                lastErrorCode = ErrorInfo.EnumErrorCode.EC_OtherError;
            }
        } catch (Exception e) {
            log.error("执行createSync失败，错误信息为" + e.getMessage());
            e.printStackTrace();
            lastErrorCode = ErrorInfo.EnumErrorCode.EC_OtherError;
        }
        globalWriteLock.writeLock().unlock();
        return pos;
    }

    @Override
    protected boolean updateSync(int iUseCaseID, BaseModel bm) {
        log.info("正在进行PosPresenter的updateSync，bm=" + bm);
        globalWriteLock.writeLock().lock();
        try {
            bm.setSyncDatetime(new Date(System.currentTimeMillis() + NtpHttpBO.TimeDifference));
            bm.setSyncType(BasePresenter.SYNC_Type_U);
            Pos pos = posMapper.save((Pos) bm);
            bm.setID(pos.getID());
            lastErrorCode = ErrorInfo.EnumErrorCode.EC_NoError;
            globalWriteLock.writeLock().unlock();
            return true;
        } catch (Exception e) {
            log.error("执行updateSync失败，错误信息为" + e.getMessage());
            e.printStackTrace();
            lastErrorCode = ErrorInfo.EnumErrorCode.EC_OtherError;
            globalWriteLock.writeLock().unlock();
            return false;
        }
    }

    @Override
    protected BaseModel retrieve1Sync(int iUseCaseID, BaseModel bm) {
        log.info("正在进行PosPresenter的retrieve1Sync，bm=" + bm);

        try {
            lastErrorCode = ErrorInfo.EnumErrorCode.EC_NoError;

            return posMapper.findOne(bm.getID());
        } catch (Exception e) {
            log.error("执行retrieve1Sync失败，错误信息为" + e.getMessage());
            e.printStackTrace();
            lastErrorCode = ErrorInfo.EnumErrorCode.EC_OtherError;

            return null;
        }
    }

    @Override
    protected List<?> retrieveNSync(int iUseCaseID, BaseModel bm) {
        log.info("正在进行PosPresenter的retrieveNSync，bm=" + bm);

        switch (iUseCaseID) {
            case BaseSQLiteBO.CASE_Pos_RetrieveNByConditions:
                try {
                    lastErrorCode = ErrorInfo.EnumErrorCode.EC_NoError;
                    String sql = String.format(bm.getSql(), bm.getConditions());
                    Query query = entityManager.createNativeQuery(QUERY_Pos_TABLE + sql, Pos.class);
                    return query.getResultList();
                } catch (Exception e) {
                    log.error("执行retrieveNSync的CASE_Pos_RetrieveNByConditions失败，错误信息为" + e.getMessage());
                    e.printStackTrace();
                    lastErrorCode = ErrorInfo.EnumErrorCode.EC_OtherError;

                    return null;
                }
            default:
                try {
                    lastErrorCode = ErrorInfo.EnumErrorCode.EC_NoError;

                    return posMapper.findAll();
                } catch (Exception e) {
                    log.error("执行retrieveNSync失败，错误信息为" + e.getMessage());
                    e.printStackTrace();
                    lastErrorCode = ErrorInfo.EnumErrorCode.EC_OtherError;

                    return null;
                }
        }
    }

    @Override
    protected BaseModel deleteSync(int iUseCaseID, BaseModel bm) {
        log.info("正在进行PosPresenter的deleteSync，bm=" + bm);
        globalWriteLock.writeLock().lock();
        try {
            posMapper.delete(bm.getID());
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
    protected boolean createAsync(int iUseCaseID, final BaseModel bm, final BaseSQLiteEvent event) {
        log.info("正在进行PosPresenter的createAsync，bm=" + bm);

        switch (iUseCaseID) {
            case BaseSQLiteBO.CASE_POS_Retrieve1ForIdentity:
                (new Thread() {
                    @Override
                    public void run() {
//                        event.setEventTypeSQLite(BaseSQLiteEvent.EnumSQLiteEventType.ESET_Pos_CreateAsync);
                        //
                        globalWriteLock.writeLock().lock();
                        BaseModel pos = null;
                        if (bm != null) {
                            try {
                                pos = createSync(iUseCaseID, bm);
                                event.setLastErrorCode(lastErrorCode);
                                log.info("创建POSMODEL成功！");
                            } catch (Exception e) {
                                log.error("创建PosModel时出错，错误信息：" + e.getMessage());
                                event.setLastErrorCode(ErrorInfo.EnumErrorCode.EC_OtherError);
                            }
                        }
                        //
                        event.setBaseModel1(pos);
                        globalWriteLock.writeLock().unlock();
                        //
                        EventBus.getDefault().post(event);
                    }
                }).start();
                break;
            default:
                (new Thread() {
                    @Override
                    public void run() {
//                        event.setEventTypeSQLite(BaseSQLiteEvent.EnumSQLiteEventType.ESET_Pos_CreateAsync);
                        //
                        globalWriteLock.writeLock().lock();
                        BaseModel pos = null;
                        try {

                            ((Pos) bm).setSyncDatetime(new Date(System.currentTimeMillis() + NtpHttpBO.TimeDifference));
                            ((Pos) bm).setSyncType(BasePresenter.SYNC_Type_C);
                            pos = createSync(iUseCaseID, bm);
                            event.setLastErrorCode(lastErrorCode);
                        } catch (Exception e) {
                            log.error("创建PosModel时出错，错误信息：" + e.getMessage());
                            event.setLastErrorCode(ErrorInfo.EnumErrorCode.EC_OtherError);
                        }
                        //
                        event.setBaseModel1(pos);
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
    protected boolean updateAsync(int iUseCaseID, final BaseModel bm, final BaseSQLiteEvent event) {
        log.info("正在进行PosPresenter的updateAsync，bm=" + bm);

        switch (iUseCaseID) {
            default:
                (new Thread() {
                    @Override
                    public void run() {
                        //event.setEventTypeSQLite(BaseSQLiteEvent.EnumSQLiteEventType.ESET_Pos_UpdateAsync);
                        //
                        globalWriteLock.writeLock().lock();
                        try {
                            bm.setSyncDatetime(new Date(System.currentTimeMillis() + NtpHttpBO.TimeDifference));
                            bm.setSyncType(BasePresenter.SYNC_Type_U);
                            posMapper.save((Pos) bm);
                            event.setLastErrorCode(ErrorInfo.EnumErrorCode.EC_NoError);
                        } catch (Exception e) {
                            log.error("更新PosModel时出错，错误信息：" + e.getMessage());
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
    protected boolean retrieveNAsync(int iUseCaseID, final BaseModel bm, final BaseSQLiteEvent event) {
        log.info("正在进行PosPresenter的retrieveNAsync，bm=" + bm);

        switch (iUseCaseID) {
            case BaseSQLiteBO.CASE_Pos_RetrieveNByConditions:
                (new Thread() {
                    @Override
                    public void run() {
                        List<Pos> result = null;
                        //event.setID(new Date(System.currentTimeMillis() + NtpHttpBO.TimeDifference).getTime());
                        //event.setEventTypeSQLite(BaseSQLiteEvent.EnumSQLiteEventType.ESET_Pos_RetrieveNAsync);
                        //
                        try {
                            //Thread.sleep(1); //令event.setID()的ID惟一
                            String sql = String.format(bm.getSql(), bm.getConditions());
                            Query query = entityManager.createNativeQuery(QUERY_Pos_TABLE + sql, Pos.class);
                            result = query.getResultList();
                            event.setLastErrorCode(ErrorInfo.EnumErrorCode.EC_NoError);
                        } catch (Exception e) {
                            log.error("RetrieveNAsync PosModel出错，错误信息：" + e.getMessage());
                            event.setLastErrorCode(ErrorInfo.EnumErrorCode.EC_OtherError);
                        }
                        //
                        event.setListMasterTable(result);

                        EventBus.getDefault().post(event);
                    }
                }).start();
            default:
                (new Thread() {
                    @Override
                    public void run() {
                        List<Pos> result = null;
                        //                        event.setID(new Date(System.currentTimeMillis() + NtpHttpBO.TimeDifference).getTime());
//                        event.setEventTypeSQLite(BaseSQLiteEvent.EnumSQLiteEventType.ESET_Pos_RetrieveNAsync);
                        //
                        try {
                            Thread.sleep(1); //令event.setID()的ID惟一
                            result = posMapper.findAll();//...
                            event.setLastErrorCode(ErrorInfo.EnumErrorCode.EC_NoError);
                        } catch (Exception e) {
                            log.error("查询SQLite的表PosModel的所有记录时出错，错误信息：" + e.getMessage());
                            event.setLastErrorCode(ErrorInfo.EnumErrorCode.EC_OtherError);
                        }
                        //
                        event.setListMasterTable(result);
                        //
                        EventBus.getDefault().post(event);
                    }
                }).start();
                break;
        }

        return true;
    }

    @Override
    protected boolean deleteAsync(int iUseCaseID, final BaseModel bm, final BaseSQLiteEvent event) {
        log.info("正在进行PosPresenter的deleteAsync，bm=" + bm);

        switch (iUseCaseID) {
            default:
                (new Thread() {
                    @Override
                    public void run() {
//                        event.setEventTypeSQLite(BaseSQLiteEvent.EnumSQLiteEventType.ESET_Pos_DeleteAsync);
                        //
                        globalWriteLock.writeLock().lock();
                        try {
                            posMapper.delete(bm.getID());
                            event.setLastErrorCode(ErrorInfo.EnumErrorCode.EC_NoError);
                        } catch (Exception e) {
                            log.error("删除PosModel时出错，错误信息：" + e.getMessage());
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
    protected boolean retrieve1Async(int iUseCaseID, final BaseModel bm, final BaseSQLiteEvent event) {
        log.info("正在进行PosPresenter的retrieve1Async，bm=" + bm);

        switch (iUseCaseID) {
            case BaseSQLiteBO.CASE_POS_Retrieve1ForIdentity:
                (new Thread() {
                    @Override
                    public void run() {
                        //event.setEventTypeSQLite(BaseSQLiteEvent.EnumSQLiteEventType.ESET_Pos_Retrieve1Async);
                        event.setBaseModel1(null);
                        //通过机器自身的SN码去进行SQLite查找。
                        try {
//                            Pos pos = new Pos();
//                            String pos_SN = Constants.MyPosSN;
//                            pos.setSql("where F_POS_SN = '%s'");
//                            pos.setConditions(new String[]{pos_SN});
//
//                            String sql = String.format(pos.getSql(), pos.getConditions());
//                            Query query = entityManager.createNativeQuery(QUERY_Pos_TABLE + sql, Pos.class);
//                            List<Pos> bmList = query.getResultList();
                            List<Pos> bmList = posMapper.findAll();
                            event.setLastErrorCode(ErrorInfo.EnumErrorCode.EC_NoError);

                            if (bmList.size() > 0) {
                                Pos pos2 = bmList.get(0);
                                log.info("SQLite中查找到本机的身份：" + pos2);
                                Constants.posID = pos2.getID().intValue();
                                if (pos2.getPasswordInPOS() != null && !"".equals(pos2.getPasswordInPOS())) {
                                    event.setBaseModel1(pos2);
                                } else {
                                    event.setBaseModel1(null);
                                }
                            }
                        } catch (Exception e) {
                            log.error("Retrieve1 PosModel时出错，错误信息：" + e.getMessage());
                            event.setLastErrorCode(ErrorInfo.EnumErrorCode.EC_OtherError);
                        }
                        EventBus.getDefault().post(event);
                    }
                }).start();
                break;
            default:
                (new Thread() {
                    @Override
                    public void run() {
                        Pos result = null;
//                        event.setEventTypeSQLite(BaseSQLiteEvent.EnumSQLiteEventType.ESET_Pos_Retrieve1Async);
                        //
                        try {
                            result = (Pos) posMapper.findOne(bm.getID());//...
                            event.setLastErrorCode(ErrorInfo.EnumErrorCode.EC_NoError);
                        } catch (Exception e) {
                            log.error("Retrieve1 PosModel时出错，错误信息：" + e.getMessage());
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
    protected boolean createNAsync(int iUseCaseID, final List<?> list, final BaseSQLiteEvent event) {
        log.info("正在进行PosPresenter的createNAsync，list=" + (list == null ? null : list.toString()));

        switch (iUseCaseID) {
            default:
                (new Thread() {
                    @Override
                    public void run() {
//                        event.setEventTypeSQLite(BaseSQLiteEvent.EnumSQLiteEventType.ESET_Pos_CreateNAsync);
                        //
                        globalWriteLock.writeLock().lock();
                        List<BaseModel> baseModels = new ArrayList<BaseModel>();
                        try {
                            for (int i = 0; i < list.size(); i++) {
                                ((Pos) list.get(i)).setSyncDatetime(new Date(System.currentTimeMillis() + NtpHttpBO.TimeDifference));
                                ((Pos) list.get(i)).setSyncType(BasePresenter.SYNC_Type_C);
                                BaseModel baseModel = createSync(iUseCaseID, ((Pos) list.get(i)));
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
                            log.error("创建POS时出错，错误信息：" + e.getMessage());
                            event.setLastErrorCode(ErrorInfo.EnumErrorCode.EC_OtherError);
                        }
                        //
                        event.setListMasterTable(baseModels);
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
        log.info("正在进行PosPresenter的refreshByServerDataAsyncC，bmNewList=" + (bmNewList == null ? null : bmNewList.toString()));

        event.setStatus(BaseEvent.EnumEventStatus.EES_SQLite_ToApplyServerData);
        switch (iUseCaseID) {
            default:
                (new Thread() {
                    @Override
                    public void run() {
                        log.info("已经得到服务器返回的需要同步的Pos数据, 准备进行同步...");
                        globalWriteLock.writeLock().lock();
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
                        event.setLastErrorCode(ErrorInfo.EnumErrorCode.EC_NoError);
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
        log.info("正在进行PosPresenter的refreshByServerDataAsync，bmNewList=" + (bmNewList == null ? null : bmNewList.toString()));

        event.setStatus(BaseEvent.EnumEventStatus.EES_SQLite_ToApplyServerData);
        switch (iUseCaseID) {
            default:
                (new Thread() {
                    @Override
                    public void run() {
                        globalWriteLock.writeLock().lock();
                        log.info("已经得到服务器返回的需要同步的POS数据, 准备进行同步...");
                        List<BaseModel> bmList = (List<BaseModel>) bmNewList;
//                        event.setEventTypeSQLite(BaseSQLiteEvent.EnumSQLiteEventType.ESET_Pos_RefreshByServerDataAsync_Done);
                        try {
                            if (bmList != null) {
                                for (int i = 0; i < bmList.size(); i++) {
                                    String PosType = bmList.get(i).getSyncType();

                                    if (BasePresenter.SYNC_Type_C.equals(PosType)) {
                                        Pos pos = (Pos) retrieve1Sync(iUseCaseID, bmList.get(i));
                                        if (pos != null) {
                                            deleteSync(BaseSQLiteBO.INVALID_CASE_ID, pos);
                                        }
                                        //
                                        createSync(iUseCaseID, bmList.get(i));

                                        log.info("服务器返回的C型POS数据成功同步到SQLite中!");
                                    } else if (BasePresenter.SYNC_Type_U.equals(PosType)) {
                                        updateSync(iUseCaseID, bmList.get(i));

                                        log.info("服务器返回的U型POS数据成功同步到SQLite中!");
                                    } else if (BasePresenter.SYNC_Type_D.equals(PosType)) {
                                        Pos pos = (Pos) retrieve1Sync(iUseCaseID, bmList.get(i));
                                        if (pos != null) {
                                            deleteSync(BaseSQLiteBO.INVALID_CASE_ID, pos);
                                        }

                                        log.info("服务器返回的D型POS数据成功同步到SQLite中!");
                                    }
                                }
                                event.setListMasterTable(bmList);
                                event.setLastErrorCode(lastErrorCode);
                            } else {
                                event.setLastErrorCode(ErrorInfo.EnumErrorCode.EC_NoError);
                                event.setListMasterTable(bmList);
                            }
                        } catch (Exception e) {
                            log.info("执行refreshByServerDataAsync失败，错误信息为" + e.getMessage());
                            e.printStackTrace();
                            event.setLastErrorCode(ErrorInfo.EnumErrorCode.EC_OtherError);
                            event.setLastErrorMessage(e.getMessage());
                        }
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
        log.info("正在进行PosPresenter的deleteNSync，bm=" + bm);
        globalWriteLock.writeLock().lock();
        try {
            posMapper.deleteAll();
            lastErrorCode = ErrorInfo.EnumErrorCode.EC_NoError;
        } catch (Exception e) {
            log.info("执行deleteNSync失败，错误信息为" + e.getMessage());
            e.printStackTrace();
            lastErrorCode = ErrorInfo.EnumErrorCode.EC_OtherError;
        }
        globalWriteLock.writeLock().unlock();
        return null;
    }

    public void drop() {
        try {
//            posMapper.dropTable(posMapper.getDatabase(), true);
            lastErrorCode = ErrorInfo.EnumErrorCode.EC_NoError;
        } catch (Exception e) {
            log.error("删除SQLite中的表RetailTrade时出错，错误信息：" + e.getMessage());
            lastErrorCode = ErrorInfo.EnumErrorCode.EC_OtherError;
        }
    }

    @Override
    protected boolean createOrReplaceAsync(int iUseCaseID, final BaseModel baseModel, final BaseSQLiteEvent event) {
        log.info("正在进行PosPresenter的createOrReplaceAsync，baseModel=" + baseModel);

        switch (iUseCaseID) {
            default:
                (new Thread() {
                    @Override
                    public void run() {
                        globalWriteLock.writeLock().lock();
                        Pos pos = (Pos) baseModel;
                        try {
                            if (pos == null) {
                                //...
                            }
                            //服务器返回的pos,如果本地存在，就删除掉，重新插入服务器返回的
                            pos.setSql("where F_POS_SN = '%s'");
                            pos.setConditions(new String[]{pos.getPos_SN()});
                            String sql = String.format(pos.getSql(), pos.getConditions());
                            Query query = entityManager.createNativeQuery(QUERY_Pos_TABLE + sql, Pos.class);
                            List<Pos> posList = query.getResultList();
                            if (posList.size() > 0) {
                                deleteSync(BaseSQLiteBO.INVALID_CASE_ID, posList.get(0));
                            }
                            pos = (Pos) createSync(BaseSQLiteBO.INVALID_CASE_ID, pos);
                            //服务器返回的Company，如果本地存在，就删除在重新插入，否则直接插入到本地SQLite


                            event.setLastErrorCode(ErrorInfo.EnumErrorCode.EC_NoError);
                        } catch (Exception e) {
                            e.printStackTrace();
                            log.info("插入新的Pos信息出错，错误信息为：" + e.getMessage());
                            event.setLastErrorCode(ErrorInfo.EnumErrorCode.EC_OtherError);
                        }
                        event.setBaseModel1(pos);
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
    protected String getTableName() {
        if (Pos.class.isAnnotationPresent(Table.class)) {
            Table annotation = Pos.class.getAnnotation(Table.class);
            return annotation.name();
        } else {
            log.error("无法获取表名，未给" + Pos.class.getSimpleName() + "添加@Table。");
        }
        return "";
    }

    @Override
    protected String getQueryTable() {
        return QUERY_Pos_TABLE;
    }

}
