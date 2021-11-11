package wpos.presenter;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Component;
import wpos.bo.BaseHttpBO;
import wpos.utils.EventBus;
import wpos.bo.BaseSQLiteBO;
import wpos.dao.SmallSheetFrameMapper;
import wpos.dao.SmallSheetTextMapper;
import wpos.event.BaseEvent;
import wpos.event.UI.BaseSQLiteEvent;
import wpos.helper.Constants;
import wpos.model.*;

import javax.annotation.Resource;
import javax.persistence.Query;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static wpos.bo.BaseSQLiteBO.CASE_SmallSheetFrame_RetrieveNByConditions;
import static wpos.bo.BaseSQLiteBO.CASE_SmallSheetFrame_RetrieveNToUpload;

@Component("smallSheetFramePresenter")
public class SmallSheetFramePresenter extends BasePresenter {
    private Log log = LogFactory.getLog(this.getClass());

    /**
     * 仅测试代码能调用。=true，代表当前工作在测试环境中。=false，代表是App在运行
     */
    public static boolean bInTestMode = false;

    @Resource
    private SmallSheetFrameMapper smallSheetFrameMapper;

    @Resource
    private SmallSheetTextMapper smallSheetTextMapper;

//    protected static ReentrantReadWriteLock lock = new ReentrantReadWriteLock();

//    public SmallSheetFramePresenter(final SmallSheetFrameMapper smallSheetFrameMapper) {
//        super(smallSheetFrameMapper);
//    }

    public static final String QUERY_SMALLSHEETFRAME_TABLE = "SELECT F_ID,F_Logo,F_CreateDatetime,F_UpdateDatetime,F_CountOfBlankLineAtBottom,F_DelimiterToRepeat,F_SyncDatetime,F_SyncType, F_SlaveCreated FROM T_SmallSheetFrame ";

    @Override
    public void createTableSync() {
        smallSheetFrameMapper.createTable();
    }

    @Override
    protected String getTableName() {
//        return smallSheetFrameMapper.getTablename();
        return "T_SmallSheetFrame";
    }

    @Override
    protected String getQueryTable() {
        return QUERY_SMALLSHEETFRAME_TABLE;
    }

    @Override
    protected List<BaseModel> createNSync(int iUseCaseID, final List<?> list) {
        log.info("正在进行SmallSheetFramePresenter的createNSync，list=" + (list == null ? null : list.toString()));

        globalWriteLock.writeLock().lock();
        List<BaseModel> baseModels = new ArrayList<BaseModel>();
        try {
            for (int i = 0; i < list.size(); i++) {
//                ((SmallSheetFrame) list.get(i)).setSyncDatetime(new Date(System.currentTimeMillis() + NtpHttpBO.TimeDifference));
                ((SmallSheetFrame) list.get(i)).setSyncType(BasePresenter.SYNC_Type_C);
                BaseModel baseModel = createSync(iUseCaseID, ((SmallSheetFrame) list.get(i)));
                if (baseModel != null) {
                    baseModels.add(baseModel);
                }
            }
        } catch (Exception e) {
            log.error("执行createNSync时出错，错误信息：" + e.getMessage());
            e.printStackTrace();
            lastErrorCode = ErrorInfo.EnumErrorCode.EC_OtherError;
        }
        globalWriteLock.writeLock().unlock();
        return baseModels;
    }

    @Override
    protected BaseModel createSync(int iUseCaseID, BaseModel bm) {
        log.info("正在进行SmallSheetFramePresenter的createSync，bm=" + bm);
        globalWriteLock.writeLock().lock();
        SmallSheetFrame smallSheetFrame = null;
        try {
            if (bm.getSyncDatetime() == null) {
                bm.setSyncDatetime(Constants.getDefaultSyncDatetime2());
            }
            bm.setSyncType(BasePresenter.SYNC_Type_C);

            smallSheetFrameMapper.create((SmallSheetFrame) bm);
            String sql = "";
            if (bm.getID() != null) {
                bm.setSql("WHERE F_ID = %s");
                bm.setConditions(new String[]{String.valueOf(bm.getID())});
                sql = String.format(bm.getSql(), bm.getConditions());
            } else {
                sql = "WHERE F_ID = (select MAX(F_ID) from T_SmallSheetFrame)";
            }

            Query dataQuery = entityManager.createNativeQuery(QUERY_SMALLSHEETFRAME_TABLE + sql, SmallSheetFrame.class);
            List<?> ssfList = dataQuery.getResultList();
            if (ssfList != null && ssfList.size() > 0) {
                smallSheetFrame = (SmallSheetFrame) ssfList.get(0);
                smallSheetFrame.fillNonDBFieldValue(BaseSQLiteBO.INVALID_CASE_ID, bm);
                bm.setID(((SmallSheetFrame) ssfList.get(0)).getID());
            }
            lastErrorCode = ErrorInfo.EnumErrorCode.EC_NoError;
        } catch (Exception e) {
            log.info("执行createSync时出错，错误信息：" + e.getMessage());
            e.printStackTrace();
            lastErrorCode = ErrorInfo.EnumErrorCode.EC_OtherError;
        }
        globalWriteLock.writeLock().unlock();
        return smallSheetFrame;
    }

    @Override
    protected boolean updateSync(int iUseCaseID, BaseModel bm) {
        log.info("正在进行SmallSheetFramePresenter的updateSync，bm=" + bm);
        globalWriteLock.writeLock().lock();
        try {
            bm.setSyncDatetime(Constants.getDefaultSyncDatetime2());
            bm.setSyncType(BasePresenter.SYNC_Type_U);
            smallSheetFrameMapper.save((SmallSheetFrame) bm);
            lastErrorCode = ErrorInfo.EnumErrorCode.EC_NoError;
            globalWriteLock.writeLock().unlock();
            return true;
        } catch (Exception e) {
            log.info("执行updateSync时出错，错误信息：" + e.getMessage());
            e.printStackTrace();
            lastErrorCode = ErrorInfo.EnumErrorCode.EC_OtherError;
            globalWriteLock.writeLock().unlock();
            return false;
        }
    }

    @Override
    protected BaseModel retrieve1Sync(int iUseCaseID, BaseModel bm) {
        log.info("正在进行SmallSheetFramePresenter的retrieve1Sync，bm=" + bm);

        try {
            SmallSheetFrame smallSheetFrame = (SmallSheetFrame) smallSheetFrameMapper.findOne(bm.getID());
            if (smallSheetFrame != null) {
                bm.setSql("where F_FrameID = '%s'");
                bm.setConditions(new String[]{String.valueOf(smallSheetFrame.getID())});

                String sql = String.format(bm.getSql(), bm.getConditions());
                Query query = entityManager.createNativeQuery(SmallSheetTextPresenter.QUERY_SMALLSHEETTEXT_TABLE + sql, SmallSheetText.class);
                List<SmallSheetText> smallSheetTexts = query.getResultList();
                smallSheetFrame.setListSlave1(smallSheetTexts);
            }
            lastErrorCode = ErrorInfo.EnumErrorCode.EC_NoError;

            return smallSheetFrame;
        } catch (Exception e) {
            log.info("执行retrieve1Sync时出错，错误信息：" + e.getMessage());
            e.printStackTrace();
            lastErrorCode = ErrorInfo.EnumErrorCode.EC_OtherError;

            return null;
        }
    }

    @Override
    protected List<?> retrieveNSync(int iUseCaseID, BaseModel bm) {
        log.info("正在进行SmallSheetFramePresenter的retrieveNSync，bm=" + bm);
        List<SmallSheetFrame> smallSheetFrameList;
        SmallSheetFrame smallSheetFrame = new SmallSheetFrame();
        switch (iUseCaseID) {
            case CASE_SmallSheetFrame_RetrieveNToUpload:
                smallSheetFrame.setSql("where F_SyncDatetime = %s and F_SlaveCreated = %s");
                smallSheetFrame.setConditions(new String[]{"0", String.valueOf(BaseModel.EnumBoolean.EB_Yes.getIndex())});
                try {
                    lastErrorCode = ErrorInfo.EnumErrorCode.EC_NoError;
                    String sql = String.format(smallSheetFrame.getSql(), smallSheetFrame.getConditions());
                    Query query = entityManager.createNativeQuery(QUERY_SMALLSHEETFRAME_TABLE + sql, SmallSheetFrame.class);
                    smallSheetFrameList =  query.getResultList();
                    for (int i = 0; i < smallSheetFrameList.size(); i++) {
                        SmallSheetText smallSheetText = new SmallSheetText();
                        smallSheetText.setSql("where F_FrameID = %s");
                        smallSheetText.setConditions(new String[]{String.valueOf(smallSheetFrameList.get(i).getID())});
                        String sqlSST = String.format(smallSheetText.getSql(), smallSheetText.getConditions());
                        Query querysqlSST = entityManager.createNativeQuery(SmallSheetTextPresenter.QUERY_SMALLSHEETTEXT_TABLE + sqlSST, SmallSheetText.class);
                        List<SmallSheetText> smallSheetTextList = querysqlSST.getResultList();
                        //
                        smallSheetFrameList.get(i).setListSlave1(smallSheetTextList);
                        smallSheetFrameList.get(i).setSyncDatetime(Constants.getDefaultSyncDatetime2());
                        smallSheetFrameList.get(i).setReturnObject(1);
                    }
                    lastErrorCode = ErrorInfo.EnumErrorCode.EC_NoError;
                    return smallSheetFrameList;
                } catch (Exception e) {
                    log.info("SmallSheetPresenter.retrieveNSync()发生异常：" + e.getMessage());
                    e.printStackTrace();
                    lastErrorCode = ErrorInfo.EnumErrorCode.EC_OtherError;

                    return null;
                }
            case CASE_SmallSheetFrame_RetrieveNByConditions:
                try {
                    lastErrorCode = ErrorInfo.EnumErrorCode.EC_NoError;
                    String sql = String.format(bm.getSql(), bm.getConditions());
                    Query query = entityManager.createNativeQuery(QUERY_SMALLSHEETFRAME_TABLE + sql, SmallSheetFrame.class);
                    return query.getResultList();
                } catch (Exception e) {
                    log.info("执行retrieveNSync时出错，错误信息：" + e.getMessage());
                    e.printStackTrace();
                    lastErrorCode = ErrorInfo.EnumErrorCode.EC_OtherError;

                    return null;
                }
            default:
                try {
                    lastErrorCode = ErrorInfo.EnumErrorCode.EC_NoError;

                    return smallSheetFrameMapper.findAll();
                } catch (Exception e) {
                    log.info("执行retrieveNSync时出错，错误信息：" + e.getMessage());
                    e.printStackTrace();
                    lastErrorCode = ErrorInfo.EnumErrorCode.EC_OtherError;

                    return null;
                }
        }
    }

    @Override
    protected BaseModel deleteSync(int iUseCaseID, BaseModel bm) {
        log.info("正在进行SmallSheetFramePresenter的deleteSync，bm=" + bm);
        globalWriteLock.writeLock().lock();
        try {
            smallSheetFrameMapper.delete(bm.getID());
            lastErrorCode = ErrorInfo.EnumErrorCode.EC_NoError;
        } catch (Exception e) {
            log.info("执行deleteSync时出错，错误信息：" + e.getMessage());
            e.printStackTrace();
            lastErrorCode = ErrorInfo.EnumErrorCode.EC_OtherError;
        }
        globalWriteLock.writeLock().unlock();
        return null;
    }

    @Override
    protected BaseModel deleteNSync(int iUseCaseID, final BaseModel bm) {
        log.info("正在进行SmallSheetFramePresenter的deleteNSync，bm=" + bm);
        globalWriteLock.writeLock().lock();
        try {
            smallSheetFrameMapper.deleteAll();
            lastErrorCode = ErrorInfo.EnumErrorCode.EC_NoError;
        } catch (Exception e) {
            log.info("执行deleteNSync时出错，错误信息：" + e.getMessage());
            e.printStackTrace();
            lastErrorCode = ErrorInfo.EnumErrorCode.EC_OtherError;
        }
        globalWriteLock.writeLock().unlock();
        return null;
    }

    @Override
    protected boolean createMasterSlaveAsync(final int iUseCaseID, final BaseModel bmMaster, final BaseSQLiteEvent event) {
        log.info("正在进行SmallSheetFramePresenter的createMasterSlaveAsync，bmMaster=" + bmMaster);

        event.setStatus(BaseEvent.EnumEventStatus.EES_SQLite_ToDo);
        switch (event.getEventTypeSQLite()) {
            case ESET_SmallSheet_CreateMasterSlaveAsync_Done:
                log.info("准备向本地SQLite插入主从表数据....");

                new Thread() {
                    @Override
                    public void run() {
//                        event.setEventTypeSQLite(BaseSQLiteEvent.EnumSQLiteEventType.ESET_SmallSheet_CreateMasterSlaveAsync_Done);
                        globalWriteLock.writeLock().lock();
                        try {
                            bmMaster.setSyncType(BasePresenter.SYNC_Type_C);
                            bmMaster.setSyncDatetime(Constants.getDefaultSyncDatetime2());
                            createSync(iUseCaseID, bmMaster);
                            for (int i = 0; i < ((SmallSheetFrame) bmMaster).getListSlave1().size(); i++) {
                                SmallSheetText sst = ((SmallSheetText) (bmMaster.getListSlave1()).get(i));
                                sst.setSyncType(BasePresenter.SYNC_Type_C);
                                sst.setSyncDatetime(Constants.getDefaultSyncDatetime2());
                                sst.setFrameId((long) bmMaster.getID());
                                smallSheetTextMapper.create(sst);
                            }
                            if (!bInTestMode) {
                                ((SmallSheetFrame) bmMaster).setSlaveCreated(BaseModel.EnumBoolean.EB_Yes.getIndex());
                                smallSheetFrameMapper.save((SmallSheetFrame) bmMaster);
                            }
                            //
                            event.setLastErrorCode(lastErrorCode);
                            event.setBaseModel1(bmMaster);

                            log.info("向本地SQLite插入主从表数据成功！FrameID=" + bmMaster.getID() + "  "
                                    + (((SmallSheetFrame) bmMaster).getListSlave1().size() > 0 ? ((SmallSheetText) bmMaster.getListSlave1().get(0)).getID() + "~" //打印第1个ID
                                    + ((SmallSheetText) bmMaster.getListSlave1().get(bmMaster.getListSlave1().size() - 1)).getID() : "")//打印最后1个ID
                            );

                            event.setTmpMasterTableObj(bmMaster);
                        } catch (Exception e) {
                            log.info("执行createMasterSlaveAsync时出错，错误信息：" + e.getMessage());
                            e.printStackTrace();
                            event.setLastErrorCode(ErrorInfo.EnumErrorCode.EC_OtherError);

                            log.info("向本地SQLite插入主从表数据失败！原因：" + e.getMessage());
                        }
                        event.setStatus(BaseEvent.EnumEventStatus.EES_SQLite_Done);
                        //
                        globalWriteLock.writeLock().unlock();
                        EventBus.getDefault().post(event);
                    }
                }.start();
                break;
            default:
                log.info("未定义的事件！");
                throw new RuntimeException("未定义的事件！");
        }
        return true;
    }

    @Override
    protected boolean updateMasterSlaveAsync(final int iUseCaseID, final BaseModel bmMaster, final BaseSQLiteEvent event) {
        log.info("正在进行SmallSheetFramePresenter的updateMasterSlaveAsync，bmMaster=" + (bmMaster == null ? null : bmMaster.toString()));

        event.setStatus(BaseEvent.EnumEventStatus.EES_SQLite_ToDo);
        switch (event.getEventTypeSQLite()) {
            case ESET_SmallSheet_UpdateMasterSlaveAsync:
                log.info("修改本地SQLite主从表数据....");
                new Thread() {
                    @Override
                    public void run() {
                        globalWriteLock.writeLock().lock();
                        switch (iUseCaseID) {
                            default:
                                //修改小票格式主从表,设置SyncDatetime和SyncType。下一步是触发网络请求
                                try {
                                    // 如果是临时数据，则不需要设置同步类型
                                    SmallSheetFrame ssf = (SmallSheetFrame) smallSheetFrameMapper.findOne(((SmallSheetFrame) bmMaster).getID());
                                    if (ssf.getSyncDatetime().compareTo(Constants.getDefaultSyncDatetime2()) == 0) {
                                        bmMaster.setSyncType(BasePresenter.SYNC_Type_C);
                                    } else {
                                        bmMaster.setSyncType(BasePresenter.SYNC_Type_U);
                                    }
                                    bmMaster.setSyncDatetime(Constants.getDefaultSyncDatetime2());
                                    updateSync(iUseCaseID, bmMaster);
                                    for (int i = 0; i < ((SmallSheetFrame) bmMaster).getListSlave1().size(); i++) {
                                        ((SmallSheetText) bmMaster.getListSlave1().get(i)).setSyncDatetime(Constants.getDefaultSyncDatetime2());
                                        ((SmallSheetText) bmMaster.getListSlave1().get(i)).setSyncType(BasePresenter.SYNC_Type_U);
                                        smallSheetTextMapper.save(((SmallSheetText) bmMaster.getListSlave1().get(i)));
                                    }
                                    event.setLastErrorCode(ErrorInfo.EnumErrorCode.EC_NoError);
                                    event.setBaseModel1(bmMaster);
                                } catch (Exception e) {
                                    log.info("执行updateMasterSlaveAsync时出错，错误信息：" + e.getMessage());
                                    e.printStackTrace();
                                    event.setLastErrorCode(ErrorInfo.EnumErrorCode.EC_OtherError);
                                }
                                event.setTmpMasterTableObj(bmMaster);
                                EventBus.getDefault().post(event);
                                break;
                        }
                        globalWriteLock.writeLock().unlock();
                    }
                }.start();
                break;
            case ESET_SmallSheet_UpdateMasterSlaveByServerDataAsync_Done:
                new Thread() {
                    @Override
                    public void run() {
                        globalWriteLock.writeLock().lock();
                        switch (iUseCaseID) {
                            case BaseSQLiteBO.CASE_SmallSheet_UpdateByServerData:
                                //修改了小票格式，也上传到服务器，通过服务器返回的SyncDatetime设置本地SQLite的SyncDatetime
                                event.setSyncType(BasePresenter.SYNC_Type_U);
                                try {
                                    updateSync(iUseCaseID, bmMaster);
                                    // 删除小票格式临时从表信息
                                    bmMaster.setSql("where F_FrameID = '%s'");
                                    bmMaster.setConditions(new String[]{String.valueOf(bmMaster.getID())});

                                    String sql = String.format(bmMaster.getSql(), bmMaster.getConditions());
                                    Query query = entityManager.createNativeQuery(SmallSheetTextPresenter.QUERY_SMALLSHEETTEXT_TABLE + sql, SmallSheetText.class);
                                    List<SmallSheetText> smallSheetTexts = query.getResultList();
                                    for (int i = 0; i < smallSheetTexts.size(); i++) {
                                        smallSheetTextMapper.delete(smallSheetTexts.get(i).getID());
                                    }
                                    //
                                    for (int i = 0; i < ((SmallSheetFrame) bmMaster).getListSlave1().size(); i++) {
                                        smallSheetTextMapper.save(((SmallSheetText) bmMaster.getListSlave1().get(i)));
                                    }
                                    event.setLastErrorCode(ErrorInfo.EnumErrorCode.EC_NoError);
                                    event.setBaseModel1(bmMaster);
                                    log.info("修改本地SQLite主从表数据成功！FrameID=" + bmMaster.getID());
                                    event.setData("UPDATE_DONE");
                                } catch (Exception e) {
                                    e.printStackTrace();
                                    event.setLastErrorCode(ErrorInfo.EnumErrorCode.EC_OtherError);
                                    log.info("向本地SQLite插入主从表数据失败！原因：" + e.getMessage());
                                }
                                event.setTmpMasterTableObj(bmMaster);
                                EventBus.getDefault().post(event);
                                break;
                        }
                        globalWriteLock.writeLock().unlock();
                    }
                }.start();
                break;
            case ESET_SmallSheet_BeforeDeleteMasterSlaveAsync_Done:
                //在删除副武器的小票格式之前，设置SyncType和SyncDatetime
                globalWriteLock.writeLock().lock();
                try {
                    bmMaster.setSyncDatetime(Constants.getDefaultSyncDatetime2());
                    bmMaster.setSyncType(BasePresenter.SYNC_Type_D);
                    smallSheetFrameMapper.save((SmallSheetFrame) bmMaster);
                    event.setLastErrorCode(ErrorInfo.EnumErrorCode.EC_NoError);
                    event.setBaseModel1(bmMaster);
                } catch (Exception e) {
                    e.printStackTrace();
                    log.info("删除小票格式之前设置SyncType和SyncDatetime出错，错误信息：" + e.getMessage());
                    event.setLastErrorCode(ErrorInfo.EnumErrorCode.EC_OtherError);
                }
                //
                EventBus.getDefault().post(event);
                globalWriteLock.writeLock().unlock();
                break;
            default:
                throw new RuntimeException("未定义的事件！");
        }

        return true;
    }

    @Override
    protected boolean deleteMasterSlaveAsync(final int iUseCaseID, final BaseModel bm, final BaseSQLiteEvent event) {
        log.info("正在进行SmallSheetFramePresenter的deleteMasterSlaveAsync，bm=" + bm);

        event.setStatus(BaseEvent.EnumEventStatus.EES_SQLite_ToDo);
        switch (event.getEventTypeSQLite()) {
            case ESET_SmallSheet_DeleteMasterSlaveAsync_Done:
                log.info("删除本地SQLite主从表数据");

                new Thread() {
                    @Override
                    public void run() {
                        globalWriteLock.writeLock().lock();
                        if (bm != null) {
                            try {
                                for (int i = 0; i < ((SmallSheetFrame) bm).getListSlave1().size(); i++) {
                                    smallSheetTextMapper.delete(((SmallSheetText) bm.getListSlave1().get(i)).getID());
                                }
                                deleteSync(iUseCaseID, bm);
                                //
                                event.setLastErrorCode(ErrorInfo.EnumErrorCode.EC_NoError);

                                log.info("删除本地SQLite主从表数据成功! FrameID=" + bm.getID());

                                event.setBaseModel1(bm);
                            } catch (Exception e) {
                                e.printStackTrace();
                                event.setLastErrorCode(ErrorInfo.EnumErrorCode.EC_OtherError);

                                log.info("删除本地SQLite主从表数据失败! 原因: " + e.getMessage());
                            }
                        }
                        //
                        EventBus.getDefault().post(event);
                        globalWriteLock.writeLock().unlock();
                    }
                }.start();
                break;
            default:
                log.info("未定义的事件！");
                throw new RuntimeException("未定义的事件! ");
        }

        return true;
    }

    @Override
    protected boolean createReplacerAsync(final int iUseCaseID, final BaseModel bmOld, final BaseModel bmNew, final BaseSQLiteEvent event) {
        log.info("正在进行SmallSheetFramePresenter的createReplacerAsync，bmOld=" + bmOld + ",bmNew=" + bmNew);

        event.setStatus(BaseEvent.EnumEventStatus.EES_SQLite_ToApplyServerData);

        switch (iUseCaseID) {
            default:
                new Thread() {
                    @Override
                    public void run() {
                        globalWriteLock.writeLock().lock();
                        log.info("已经得到服务器返回的主表和从表数据，准备删除本地的旧数据，并插入服务器的数据...");//...

                        try {
                            for (int i = 0; i < ((SmallSheetFrame) bmOld).getListSlave1().size(); i++) {
                                // 测试类并不会创建从表数据，所以需要判断数据库是否有数据
                                if(smallSheetTextMapper.findOne(((SmallSheetText) bmOld.getListSlave1().get(i)).getID()) != null) {
                                    smallSheetTextMapper.delete(((SmallSheetText) bmOld.getListSlave1().get(i)).getID());
                                }
                            }
                            deleteSync(iUseCaseID, bmOld);
                            log.info("删除本地的旧数据成功！FrameID=" + bmOld.getID() + "  "
                                    + (((SmallSheetFrame) bmOld).getListSlave1().size() > 0 ? ((SmallSheetText) bmOld.getListSlave1().get(0)).getID() + "~" //打印第1个ID
                                    + ((SmallSheetText) bmOld.getListSlave1().get(bmOld.getListSlave1().size() - 1)).getID() : "")//打印最后1个ID
                            );
                            //
                            Date syncDatetime = bmNew.getSyncDatetime();
                            createSync(iUseCaseID, bmNew);


                            for (int i = 0; i < ((SmallSheetFrame) bmNew).getListSlave1().size(); i++) {
                                SmallSheetText sst = (SmallSheetText) bmNew.getListSlave1().get(i);
                                sst.setFrameId((long) bmNew.getID()); //必须设置FrameID，否则无法关联主表
                                smallSheetTextMapper.create(sst);
                            }
                            //
                            event.setLastErrorCode(lastErrorCode);
                            event.setBaseModel1(bmNew);

                            log.info("插入服务器返回的主表和从表数据成功！FrameID=" + bmNew.getID() + "  "
                                    + (((SmallSheetFrame) bmNew).getListSlave1().size() > 0 ? ((SmallSheetText) bmNew.getListSlave1().get(0)).getID() + "~" //打印第1个ID
                                    + ((SmallSheetText) bmNew.getListSlave1().get(bmNew.getListSlave1().size() - 1)).getID() : "")//打印最后1个ID
                            );
                        } catch (Exception e) {
                            log.info("createReplacerAsync()异常：" + e.getMessage());
                            e.printStackTrace();
                            event.setLastErrorCode(ErrorInfo.EnumErrorCode.EC_OtherError);
                        }
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
    protected boolean createReplacerNAsync(final int iUseCaseID, final List<?> bmOldList, final List<?> bmNewList, final BaseSQLiteEvent event) {
        log.info("正在进行SmallSheetFramePresenter的createReplacerNAsync，bmOldList=" + (bmOldList == null ? null : bmOldList.toString())//
                + ",bmNewList=" + (bmNewList == null ? null : bmNewList.toString()));

        event.setStatus(BaseEvent.EnumEventStatus.EES_SQLite_ToApplyServerData);
        switch (iUseCaseID) {
            default:
                new Thread() {
                    @Override
                    public void run() {
                        globalWriteLock.writeLock().lock();
                        log.info("已经得到服务器返回的主从表的所有数据, 准备删除本地的所有数据, 并插入服务器的数据...");

//                        event.setEventTypeSQLite(BaseSQLiteEvent.EnumSQLiteEventType.ESET_SmallSheet_CreateReplacerNAsync_Done);
                        try {
                            List<SmallSheetFrame> oldSmallSheetList = (List<SmallSheetFrame>) bmOldList;
                            List<SmallSheetFrame> newSmallSheetList = (List<SmallSheetFrame>) bmNewList;
                            for (int i = 0; i < oldSmallSheetList.size(); i++) {
                                for (int j = 0; j < oldSmallSheetList.get(i).getListSlave1().size(); j++) {
                                    smallSheetTextMapper.delete(((SmallSheetText) oldSmallSheetList.get(i).getListSlave1().get(j)).getID());
                                }
                                deleteSync(BaseSQLiteBO.INVALID_CASE_ID, oldSmallSheetList.get(i));
                            }
                            //
                            log.info("删除的小票格式有：" + oldSmallSheetList.get(0).getID() + "~" + oldSmallSheetList.get(oldSmallSheetList.size() - 1).getID());

                            createNSync(iUseCaseID, bmNewList);
                            for (int i = 0; i < bmNewList.size(); i++) {
                                List<BaseModel> baseModels = (List<BaseModel>) ((SmallSheetFrame) bmNewList.get(i)).getListSlave1();
                                for (BaseModel bm : baseModels) {
                                    smallSheetTextMapper.create((SmallSheetText) bm);
                                }
                            }
                            //
                            event.setLastErrorCode(ErrorInfo.EnumErrorCode.EC_NoError);
                            event.setListMasterTable(bmNewList);

                            log.info("插入服务器返回的主从表所有数据成功!");
                        } catch (Exception e) {
                            log.info("执行createReplacerNAsync时出错，错误信息：" + e.getMessage());
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
        log.info("正在进行SmallSheetFramePresenter的refreshByServerDataAsyncC，bmOldList=" + (bmNewList == null ? null : bmNewList.toString()));

        event.setStatus(BaseEvent.EnumEventStatus.EES_SQLite_ToApplyServerData);
        switch (iUseCaseID) {
            default:
                new Thread() {
                    @Override
                    public void run() {
                        globalWriteLock.writeLock().lock();
                        try {
                            log.info("已经得到服务器返回的需要同步的SmallSheet数据, 准备进行同步...");

//                        event.setEventTypeSQLite(BaseSQLiteEvent.EnumSQLiteEventType.ESET_SmallSheet_RefreshByServerDataAsync_Done);

                            //删除本地所有的数据
                            event.setLastErrorCode(ErrorInfo.EnumErrorCode.EC_NoError);
                            smallSheetTextMapper.deleteAll();
                            deleteNSync(iUseCaseID, null);
                            if (getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError) {
                                //同步所有服务器返回的数据
                                for (int i = 0; i < bmNewList.size(); i++) {
                                    createSync(iUseCaseID, (SmallSheetFrame) bmNewList.get(i));
                                    for (int j = 0; j < ((SmallSheetFrame) bmNewList.get(i)).getListSlave1().size(); j++) {
                                        smallSheetTextMapper.create((SmallSheetText) ((SmallSheetFrame) bmNewList.get(i)).getListSlave1().get(j));
                                    }
                                }
                            } else {
                                event.setLastErrorCode(ErrorInfo.EnumErrorCode.EC_OtherError);
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                            event.setLastErrorCode(ErrorInfo.EnumErrorCode.EC_OtherError);
                        }
//                        event.setStatus(BaseEvent.EnumEventStatus.EES_SQLite_DoneApplyServerData);
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
    protected boolean refreshByServerDataAsync(final int iUseCaseID, final List<?> bmNewList, final BaseSQLiteEvent event) {
        log.info("正在进行SmallSheetFramePresenter的refreshByServerDataAsync，bmOldList=" + (bmNewList == null ? null : bmNewList.toString()));

        event.setStatus(BaseEvent.EnumEventStatus.EES_SQLite_ToApplyServerData);
        switch (iUseCaseID) {
            default:
                new Thread() {
                    @Override
                    public void run() {
                        globalWriteLock.writeLock().lock();
                        log.info("已经得到服务器返回的需要同步到SQLite的主从表的所有数据, 准备进行同步...");

                        event.setEventTypeSQLite(BaseSQLiteEvent.EnumSQLiteEventType.ESET_SmallSheet_RefreshByServerDataAsync_Done);
                        try {
                            List<BaseModel> bmList = (List<BaseModel>) bmNewList;
                            if (bmList != null) {
                                for (int i = 0; i < bmList.size(); i++) {
                                    String frameType = bmList.get(i).getSyncType();

                                    if (BasePresenter.SYNC_Type_C.equals(frameType)) {//假如返回的数据中String1 == CommodityCategory , 在SQLite对主从表进行查找,如果不存在就进行create操作,否则先删除再Create
                                        SmallSheetFrame frame = (SmallSheetFrame) retrieve1Sync(BaseSQLiteBO.INVALID_CASE_ID, bmList.get(i));
                                        if (frame != null) {
                                            for (int j = 0; j < ((SmallSheetFrame) bmList.get(i)).getListSlave1().size(); j++) {
                                                smallSheetTextMapper.delete(((SmallSheetText) bmList.get(i).getListSlave1().get(j)).getID());
                                            }
                                            deleteSync(BaseSQLiteBO.INVALID_CASE_ID, frame);
                                        }
                                        //
                                        createSync(iUseCaseID, bmList.get(i));
                                        List<SmallSheetText> smallSheetTexts = (List<SmallSheetText>) ((SmallSheetFrame) bmList.get(i)).getListSlave1();
                                        for (SmallSheetText sst : smallSheetTexts) {
                                            smallSheetTextMapper.create(sst);
                                        }

                                        log.info("服务器返回的C型数据成功同步到本地SQLite中!");
                                    } else if (BasePresenter.SYNC_Type_U.equals(frameType)) {//假如返回的string1==U,则修改SQLite中对应的数据
                                        for (int j = 0; j < bmList.size(); j++) {
                                            smallSheetTextMapper.save(((SmallSheetText) bmList.get(i).getListSlave1().get(j)));
                                        }
                                        updateSync(iUseCaseID, bmList.get(i));

                                        log.info("服务器返回的U型数据成功同步到本地SQLite中!");
                                    } else if (BasePresenter.SYNC_Type_D.equals(frameType)) {//假如返回的string1==D,则删除SQLite中对应的数据
                                        for (int j = 0; j < ((SmallSheetFrame) bmList.get(i)).getListSlave1().size(); j++) {
                                            smallSheetTextMapper.delete(((SmallSheetText) bmList.get(i).getListSlave1().get(j)).getID());
                                        }
                                        deleteSync(iUseCaseID, bmList.get(i));

                                        log.info("服务器返回的D型数据成功同步到本地SQLite中!");
                                    }
                                    event.setLastErrorCode(ErrorInfo.EnumErrorCode.EC_NoError);
                                    event.setListMasterTable(bmList);
                                }
                            } else {
                                event.setLastErrorCode(ErrorInfo.EnumErrorCode.EC_NoError);
                                event.setListMasterTable(bmList);
                            }
                        } catch (Exception e) {
                            log.info("执行refreshByServerDataAsync时出错，错误信息：" + e.getMessage());
                            e.printStackTrace();
                            event.setLastErrorCode(ErrorInfo.EnumErrorCode.EC_OtherError);
                        }
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
        log.info("正在进行SmallSheetFramePresenter的createNAsync，list=" + (list == null ? null : list.toString()));

        switch (iUseCaseID) {
            default:
                new Thread() {
                    @Override
                    public void run() {
//                        event.setEventTypeSQLite(BaseSQLiteEvent.EnumSQLiteEventType.ESET_SmallSheetFrame_CreateNAsync);
                        //
                        globalWriteLock.writeLock().lock();
                        try {
                            for (int i = 0; i < list.size(); i++) {
//                                ((SmallSheetFrame) list.get(i)).setSyncDatetime(new Date(System.currentTimeMillis() + NtpHttpBO.TimeDifference));
                                ((SmallSheetFrame) list.get(i)).setSyncType(BasePresenter.SYNC_Type_C);

                                String checkMsg = ((SmallSheetFrame) list.get(i)).checkCreate(BaseSQLiteBO.INVALID_CASE_ID);
                                if (!"".equals(checkMsg)) {
                                    log.info(checkMsg);
                                    event.setLastErrorCode(ErrorInfo.EnumErrorCode.EC_WrongFormatForInputField);
                                    event.setLastErrorMessage(checkMsg);
                                    return;
                                }

                                createSync(iUseCaseID, (SmallSheetFrame) list.get(i));
//                                ((SmallSheetFrame) list.get(i)).setID(id);
                            }
                            event.setLastErrorCode(lastErrorCode);
                        } catch (Exception e) {
                            log.info("创建SmallSheetFrame时出错，错误信息：" + e.getMessage());
                            event.setLastErrorCode(ErrorInfo.EnumErrorCode.EC_OtherError);
                            log.info("创建SmallSheetFrame时出错2，错误信息：" + event.getLastErrorCode());
                        }
                        //
                        event.setListMasterTable(list);
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
        log.info("正在进行SmallSheetFramePresenter的retrieveNAsync，bm=" + bm);

        switch (iUseCaseID) {
            case CASE_SmallSheetFrame_RetrieveNByConditions:
                new Thread() {
                    @Override
                    public void run() {
                        List<SmallSheetFrame> result = null;
                        event.setEventTypeSQLite(BaseSQLiteEvent.EnumSQLiteEventType.ESET_SmallSheetFrame_RetrieveNAsync);
                        //
                        try {
                            if (bm != null) {
                                String checkMsg = bm.checkRetrieveN(BaseSQLiteBO.INVALID_CASE_ID);
                                if (!"".equals(checkMsg)) {
                                    log.info(checkMsg);
                                    event.setLastErrorCode(ErrorInfo.EnumErrorCode.EC_WrongFormatForInputField);
                                    event.setLastErrorMessage(checkMsg);
                                    return;
                                }
                            }
                            String sql = String.format(bm.getSql(), bm.getConditions());
                            Query query = entityManager.createNativeQuery(QUERY_SMALLSHEETFRAME_TABLE + sql, SmallSheetFrame.class);
                            result = query.getResultList();
                            event.setLastErrorCode(ErrorInfo.EnumErrorCode.EC_NoError);
                        } catch (Exception e) {
                            log.info("RetrieveNAsync SmallSheetFrame出错，错误信息：" + e.getMessage());
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
                        List<SmallSheetFrame> result = null;
                        event.setEventTypeSQLite(BaseSQLiteEvent.EnumSQLiteEventType.ESET_SmallSheetFrame_RetrieveNAsync);
                        //
                        try {
                            if (bm != null) {
                                String checkMsg = bm.checkRetrieveN(BaseSQLiteBO.INVALID_CASE_ID);
                                if (!"".equals(checkMsg)) {
                                    log.info(checkMsg);
                                    event.setLastErrorCode(ErrorInfo.EnumErrorCode.EC_WrongFormatForInputField);
                                    event.setLastErrorMessage(checkMsg);
                                    return;
                                }
                            }

                            result = smallSheetFrameMapper.findAll();//...
                            event.setLastErrorCode(ErrorInfo.EnumErrorCode.EC_NoError);
                        } catch (Exception e) {
                            log.info("查询SQLite的表SmallSheetFrame的所有记录时出错，错误信息：" + e.getMessage());
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
        log.info("正在进行SmallSheetFramePresenter的createAsync，bm=" + bm);

        event.setStatus(BaseEvent.EnumEventStatus.EES_SQLite_ToDo);
        switch (iUseCaseID) {
            default:
                new Thread() {
                    @Override
                    public void run() {
                        globalWriteLock.writeLock().lock();
//                        event.setEventTypeSQLite(BaseSQLiteEvent.EnumSQLiteEventType.ESET_SmallSheetFrame_CreateAsync);
                        //
                        try {
                            bm.setSyncDatetime(Constants.getDefaultSyncDatetime2());
                            bm.setSyncType(BasePresenter.SYNC_Type_C);

                            String checkMsg = bm.checkCreate(BaseSQLiteBO.INVALID_CASE_ID);
                            if (!"".equals(checkMsg)) {
                                log.info(checkMsg);
                                event.setLastErrorCode(ErrorInfo.EnumErrorCode.EC_WrongFormatForInputField);
                                event.setLastErrorMessage(checkMsg);
                                return;
                            }

                            createSync(iUseCaseID, (SmallSheetFrame) bm);
//                            ((SmallSheetFrame) bm).setID(id);
                            event.setLastErrorCode(lastErrorCode);
                        } catch (Exception e) {
                            log.info("创建SmallSheetFrame时出错，错误信息：" + e.getMessage());
                            event.setLastErrorCode(ErrorInfo.EnumErrorCode.EC_OtherError);
                            log.info("创建SmallSheetFrame时出错2，错误信息：" + event.getLastErrorCode());
                        }
                        //
                        if (!bInTestMode) {
                            ((SmallSheetFrame) bm).setSlaveCreated(BaseModel.EnumBoolean.EB_Yes.getIndex());
                            smallSheetFrameMapper.save((SmallSheetFrame) bm);
                        }
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
    protected boolean retrieve1Async(int iUseCaseID, final BaseModel bm, final BaseSQLiteEvent event) {
        log.info("正在进行SmallSheetFramePresenter的retrieve1Async，bm=" + bm);

        switch (iUseCaseID) {
            default:
                new Thread() {
                    @Override
                    public void run() {
                        SmallSheetFrame result = null;
                        event.setEventTypeSQLite(BaseSQLiteEvent.EnumSQLiteEventType.ESET_SmallSheetFrame_Retrieve1Async);
                        //
                        try {
                            result = (SmallSheetFrame) smallSheetFrameMapper.findOne(((SmallSheetFrame) bm).getID());//...
                            event.setLastErrorCode(ErrorInfo.EnumErrorCode.EC_NoError);
                        } catch (Exception e) {
                            log.info("Retrieve1 SmallSheetFrame时出错，错误信息：" + e.getMessage());
                            event.setLastErrorCode(ErrorInfo.EnumErrorCode.EC_OtherError);
                        }
                        //
                        event.setBaseModel1(result);
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
        log.info("正在进行SmallSheetFramePresenter的updateAsync，bm=" + bm);
        SmallSheetFrame smallSheetFrame = (SmallSheetFrame) bm;
        switch (iUseCaseID) {
            default:
                new Thread() {
                    @Override
                    public void run() {
                        globalWriteLock.writeLock().lock();
//                        event.setEventTypeSQLite(BaseSQLiteEvent.EnumSQLiteEventType.ESET_SmallSheetFrame_UpdateAsync);
                        //
                        try {
                            smallSheetFrame.setSyncDatetime(Constants.getDefaultSyncDatetime2());
                            smallSheetFrame.setSyncType(BasePresenter.SYNC_Type_U);
                            smallSheetFrame.setSlaveCreated(BaseModel.EnumBoolean.EB_Yes.getIndex());

                            String checkMsg = smallSheetFrame.checkUpdate(BaseSQLiteBO.INVALID_CASE_ID);
                            if (!"".equals(checkMsg)) {
                                log.info(checkMsg);
                                event.setLastErrorCode(ErrorInfo.EnumErrorCode.EC_WrongFormatForInputField);
                                event.setLastErrorMessage(checkMsg);
                                return;
                            }

                            smallSheetFrameMapper.save((SmallSheetFrame) smallSheetFrame);
                            event.setLastErrorCode(ErrorInfo.EnumErrorCode.EC_NoError);
                        } catch (Exception e) {
                            log.info("更新SmallSheetFrame时出错，错误信息：" + e.getMessage());
                            event.setLastErrorCode(ErrorInfo.EnumErrorCode.EC_OtherError);
                        }
                        //
                        event.setBaseModel1(smallSheetFrame);
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
    protected boolean deleteAsync(int iUseCaseID, final BaseModel bm, final BaseSQLiteEvent event) {
        log.info("正在进行SmallSheetFramePresenter的deleteAsync，bm=" + bm);
        SmallSheetFrame smallSheetFrame = (SmallSheetFrame) bm;
        switch (iUseCaseID) {
            default:
                new Thread() {
                    @Override
                    public void run() {
                        globalWriteLock.writeLock().lock();
                        event.setEventTypeSQLite(BaseSQLiteEvent.EnumSQLiteEventType.ESET_SmallSheetFrame_DeleteAsync);
                        //
                        try {
                            smallSheetFrame.setSyncDatetime(Constants.getDefaultSyncDatetime2());
                            smallSheetFrame.setSyncType(BasePresenter.SYNC_Type_D);
                            smallSheetFrame.setSlaveCreated(BaseModel.EnumBoolean.EB_Yes.getIndex());

                            String checkMsg = smallSheetFrame.checkDelete(BaseSQLiteBO.INVALID_CASE_ID);
                            if (!"".equals(checkMsg)) {
                                log.info(checkMsg);
                                event.setLastErrorCode(ErrorInfo.EnumErrorCode.EC_WrongFormatForInputField);
                                event.setLastErrorMessage(checkMsg);
                                return;
                            }

                            smallSheetFrameMapper.save(smallSheetFrame);
                            event.setLastErrorCode(ErrorInfo.EnumErrorCode.EC_NoError);
                        } catch (Exception e) {
                            log.info("更新SmallSheetFrame时出错，错误信息：" + e.getMessage());
                            event.setLastErrorCode(ErrorInfo.EnumErrorCode.EC_OtherError);
                        }
                        //
                        event.setBaseModel1(smallSheetFrame);
                        //
                        EventBus.getDefault().post(event);
                        globalWriteLock.writeLock().unlock();
                    }
                }.start();
                break;
        }

        return true;
    }

    public void dropTable() {
        try {
//            smallSheetFrameMapper.dropTable(smallSheetFrameMapper.getDatabase(), true);
        } catch (Exception e) {
            log.error("删除SQLite的表SmallSheetFrame时出错，错误信息：" + e.getMessage());
        }
    }
}