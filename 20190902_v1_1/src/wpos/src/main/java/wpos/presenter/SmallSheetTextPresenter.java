package wpos.presenter;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Component;
import wpos.utils.EventBus;
import wpos.bo.BaseSQLiteBO;
import wpos.dao.SmallSheetTextMapper;
import wpos.event.UI.BaseSQLiteEvent;
import wpos.helper.Constants;
import wpos.model.BaseModel;
import wpos.model.ErrorInfo;
import wpos.model.SmallSheetText;

import javax.annotation.Resource;
import javax.persistence.Query;
import java.util.ArrayList;
import java.util.List;

@Component("smallSheetTextPresenter")
public class SmallSheetTextPresenter extends BasePresenter {
    private Log log = LogFactory.getLog(this.getClass());

    @Resource
    private SmallSheetTextMapper smallSheetTextMapper;

//    public SmallSheetTextPresenter(final SmallSheetTextMapper smallSheetTextMapper) {
//        super(smallSheetTextMapper);
//    }

    public static final String QUERY_SMALLSHEETTEXT_TABLE = "SELECT F_ID,F_Content,F_Size,F_Bold,F_Gravity,F_FrameID,F_SyncDatetime,F_SyncType FROM T_SmallSheetText ";

    @Override
    public void createTableSync() {
        smallSheetTextMapper.createTable();
    }

    @Override
    protected String getTableName() {
//        return smallSheetTextMapper.getTablename();
        return "T_SmallSheetText";
    }

    @Override
    protected Class getClassName() {
        return SmallSheetText.class;
    }


    @Override
    protected List<BaseModel> createNSync(int iUseCaseID, final List<?> list) {
        globalWriteLock.writeLock().lock();
        log.info("正在进行SmallSheetTextPresenter的createNSync，list=" + (list == null ? null : list.toString()));

        List<BaseModel> baseModels = new ArrayList<BaseModel>();
        try {
            for (int i = 0; i < list.size(); i++) {
                BaseModel baseModel = createSync(iUseCaseID, ((SmallSheetText) list.get(i)));
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

    @Override
    protected BaseModel createSync(int iUseCaseID, BaseModel bm) {
        log.info("正在进行SmallSheetTextPresenter的createSync，bm=" + (bm == null ? null : bm.toString()));
        globalWriteLock.writeLock().lock();
        SmallSheetText sst = null;
        try {
            SmallSheetText smallSheetText = (SmallSheetText) bm;
            smallSheetText.setSyncType(BasePresenter.SYNC_Type_C);
            if (smallSheetText.getSyncDatetime() == null) {
                smallSheetText.setSyncDatetime(Constants.getDefaultSyncDatetime2());
            }
            smallSheetTextMapper.create(smallSheetText);

            String sql = "";
            if (bm.getID() != null) {
                bm.setSql("WHERE F_ID = %s");
                bm.setConditions(new String[]{String.valueOf(bm.getID())});
                sql = String.format(bm.getSql(), bm.getConditions());
            } else {
                sql = "WHERE F_ID = (select MAX(F_ID) from T_SmallSheetText)";
            }

            Query dataQuery = entityManager.createNativeQuery(QUERY_SMALLSHEETTEXT_TABLE + sql, SmallSheetText.class);
            List<?> sstList = dataQuery.getResultList();
            if (sstList != null && sstList.size() > 0) {
                sst = (SmallSheetText) sstList.get(0);
                bm.setID(sst.getID());
            }
            lastErrorCode = ErrorInfo.EnumErrorCode.EC_NoError;
        } catch (Exception e) {
            log.info("执行createSync失败，错误信息为" + e.getMessage());
            e.printStackTrace();
            lastErrorCode = ErrorInfo.EnumErrorCode.EC_OtherError;
        }
        globalWriteLock.writeLock().unlock();
        return sst;
    }

    @Override
    protected boolean updateSync(int iUseCaseID, BaseModel bm) {
        log.info("正在进行SmallSheetTextPresenter的updateSync，bm=" + (bm == null ? null : bm.toString()));
        globalWriteLock.writeLock().lock();
        try {
            smallSheetTextMapper.save((SmallSheetText) bm);
            lastErrorCode = ErrorInfo.EnumErrorCode.EC_NoError;
            globalWriteLock.writeLock().unlock();
            return true;
        } catch (Exception e) {
            log.info("执行updateSync失败，错误信息为" + e.getMessage());
            e.printStackTrace();
            lastErrorCode = ErrorInfo.EnumErrorCode.EC_OtherError;
            globalWriteLock.writeLock().unlock();
            return false;
        }
    }

    @Override
    protected BaseModel retrieve1Sync(int iUseCaseID, BaseModel bm) {
        log.info("正在进行SmallSheetTextPresenter的retrieve1Sync，bm=" + (bm == null ? null : bm.toString()));

        try {
            lastErrorCode = ErrorInfo.EnumErrorCode.EC_NoError;

            return smallSheetTextMapper.findOne(bm.getID());
        } catch (Exception e) {
            log.info("执行retrieve1Sync失败，错误信息为" + e.getMessage());
            e.printStackTrace();
            lastErrorCode = ErrorInfo.EnumErrorCode.EC_OtherError;

            return null;
        }
    }

    @Override
    protected List<?> retrieveNSync(int iUseCaseID, BaseModel bm) {
        log.info("正在进行SmallSheetTextPresenter的retrieveNSync，bm=" + (bm == null ? null : bm.toString()));

        switch (iUseCaseID) {
            case BaseSQLiteBO.CASE_SmallSheetText_RetrieveNByConditions:
                try {
                    lastErrorCode = ErrorInfo.EnumErrorCode.EC_NoError;
                    String sql = String.format(bm.getSql(), bm.getConditions());

                    return entityManager.createNativeQuery(QUERY_SMALLSHEETTEXT_TABLE + sql, SmallSheetText.class).getResultList();
                } catch (Exception e) {
                    log.info("执行retrieveNSync失败，错误信息为" + e.getMessage());
                    e.printStackTrace();
                    lastErrorCode = ErrorInfo.EnumErrorCode.EC_OtherError;

                    return null;
                }
            default:
                try {
                    lastErrorCode = ErrorInfo.EnumErrorCode.EC_NoError;

                    return smallSheetTextMapper.findAll();
                } catch (Exception e) {
                    log.info("执行retrieveNSync失败，错误信息为" + e.getMessage());
                    e.printStackTrace();
                    lastErrorCode = ErrorInfo.EnumErrorCode.EC_OtherError;

                    return null;
                }
        }
    }

    @Override
    protected BaseModel deleteSync(int iUseCaseID, BaseModel bm) {
        log.info("正在进行SmallSheetTextPresenter的deleteSync，bm=" + (bm == null ? null : bm.toString()));
        globalWriteLock.writeLock().lock();
        try {
            smallSheetTextMapper.delete(bm.getID());
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
    protected BaseModel deleteNSync(int iUseCaseID, final BaseModel bm) {
        log.info("正在进行SmallSheetTextPresenter的deleteNSync，bm=" + (bm == null ? null : bm.toString()));
        globalWriteLock.writeLock().lock();
        try {
            smallSheetTextMapper.deleteAll();
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
    protected boolean createNAsync(int iUseCaseID, final List<?> list, final BaseSQLiteEvent event) {
        log.info("正在进行SmallSheetTextPresenter的createNAsync，list=" + (list == null ? null : list.toString()));

        switch (iUseCaseID) {
            default:
                new Thread() {
                    @Override
                    public void run() {
                        globalWriteLock.writeLock().lock();
//                        event.setEventTypeSQLite(BaseSQLiteEvent.EnumSQLiteEventType.ESET_SmallSheetText_CreateNAsync);
                        //
                        try {
                            for (int i = 0; i < list.size(); i++) {
                                String checkMsg = ((SmallSheetText) list.get(i)).checkRetrieveN(BaseSQLiteBO.INVALID_CASE_ID);
                                if (!"".equals(checkMsg)) {
                                    log.info(checkMsg);
                                    event.setLastErrorCode(ErrorInfo.EnumErrorCode.EC_WrongFormatForInputField);
                                    event.setLastErrorMessage(checkMsg);
                                    return;
                                }

                                smallSheetTextMapper.create((SmallSheetText) list.get(i));
//                                ((SmallSheetText) list.get(i)).setID(id);
                            }
                            event.setLastErrorCode(ErrorInfo.EnumErrorCode.EC_NoError);
                        } catch (Exception e) {
                            log.info("创建SmallSheetText时出错，错误信息：" + e.getMessage());
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
        log.info("正在进行SmallSheetTextPresenter的retrieveNAsync，bm=" + (bm == null ? null : bm.toString()));

        switch (iUseCaseID) {
            case BaseSQLiteBO.CASE_SmallSheetText_RetrieveNByConditions:
                new Thread() {
                    @Override
                    public void run() {
                        List<SmallSheetText> result = null;
//                        event.setEventTypeSQLite(BaseSQLiteEvent.EnumSQLiteEventType.ESET_SmallSheetText_RetrieveNAsync);
                        //
                        try {
                            String checkMsg = bm.checkRetrieveN(BaseSQLiteBO.INVALID_CASE_ID);
                            if (!"".equals(checkMsg)) {
                                log.info(checkMsg);
                                event.setLastErrorCode(ErrorInfo.EnumErrorCode.EC_WrongFormatForInputField);
                                event.setLastErrorMessage(checkMsg);
                                return;
                            }

                            String sql = String.format(bm.getSql(), bm.getConditions());
                            result = entityManager.createNativeQuery(QUERY_SMALLSHEETTEXT_TABLE + sql, SmallSheetText.class).getResultList();
                            event.setLastErrorCode(ErrorInfo.EnumErrorCode.EC_NoError);
                        } catch (Exception e) {
                            log.info("RetrieveNAsync SmallSheetText出错，错误信息：" + e.getMessage());
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
                        List<SmallSheetText> result = null;
//                        event.setEventTypeSQLite(BaseSQLiteEvent.EnumSQLiteEventType.ESET_SmallSheetText_RetrieveNAsync);
                        //
                        try {
                            String checkMsg = bm.checkRetrieveN(BaseSQLiteBO.INVALID_CASE_ID);
                            if (!"".equals(checkMsg)) {
                                log.info(checkMsg);
                                event.setLastErrorCode(ErrorInfo.EnumErrorCode.EC_WrongFormatForInputField);
                                event.setLastErrorMessage(checkMsg);
                                return;
                            }

                            result = (List<SmallSheetText>) smallSheetTextMapper.findAll();//...
                            event.setLastErrorCode(ErrorInfo.EnumErrorCode.EC_NoError);
                        } catch (Exception e) {
                            log.info("查询SQLite的表SmallSheetText的所有记录时出错，错误信息：" + e.getMessage());
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
        log.info("正在进行SmallSheetTextPresenter的createAsync，bm=" + (bm == null ? null : bm.toString()));

        switch (iUseCaseID) {
            default:
                new Thread() {
                    @Override
                    public void run() {
                        globalWriteLock.writeLock().lock();
//                        event.setEventTypeSQLite(BaseSQLiteEvent.EnumSQLiteEventType.ESET_SmallSheetText_CreateAsync);
                        //
                        try {
                            String checkMsg = bm.checkCreate(BaseSQLiteBO.INVALID_CASE_ID);
                            if (!"".equals(checkMsg)) {
                                log.info(checkMsg);
                                event.setLastErrorCode(ErrorInfo.EnumErrorCode.EC_WrongFormatForInputField);
                                event.setLastErrorMessage(checkMsg);
                                globalWriteLock.writeLock().unlock();
                                return;
                            }

                            smallSheetTextMapper.create((SmallSheetText) bm);
//                            ((SmallSheetText) bm).setID(id);
                            event.setLastErrorCode(ErrorInfo.EnumErrorCode.EC_NoError);
                        } catch (Exception e) {
                            log.info("创建SmallSheetText时出错，错误信息：" + e.getMessage());
                            event.setLastErrorCode(ErrorInfo.EnumErrorCode.EC_OtherError);
                            log.info("创建SmallSheetText时出错2，错误信息：" + event.getLastErrorCode());
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
    protected boolean retrieve1Async(int iUseCaseID, final BaseModel bm, final BaseSQLiteEvent event) {
        log.info("正在进行SmallSheetTextPresenter的retrieve1Async，bm=" + (bm == null ? null : bm.toString()));

        switch (iUseCaseID) {
            default:
                new Thread() {
                    @Override
                    public void run() {
                        SmallSheetText result = null;
//                        event.setEventTypeSQLite(BaseSQLiteEvent.EnumSQLiteEventType.ESET_SmallSheetText_Retrieve1Async);
                        //
                        try {
                            result = (SmallSheetText) smallSheetTextMapper.findOne(bm.getID());//...
                            event.setLastErrorCode(ErrorInfo.EnumErrorCode.EC_NoError);
                        } catch (Exception e) {
                            log.info("Retrieve1 SmallSheetText时出错，错误信息：" + e.getMessage());
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
        log.info("正在进行SmallSheetTextPresenter的updateAsync，bm=" + (bm == null ? null : bm.toString()));

        switch (iUseCaseID) {
            default:
                new Thread() {
                    @Override
                    public void run() {
                        globalWriteLock.writeLock().unlock();
//                        event.setEventTypeSQLite(BaseSQLiteEvent.EnumSQLiteEventType.ESET_SmallSheetText_UpdateAsync);
                        //
                        try {
                            String checkMsg = bm.checkUpdate(BaseSQLiteBO.INVALID_CASE_ID);
                            if (!"".equals(checkMsg)) {
                                log.info(checkMsg);
                                event.setLastErrorCode(ErrorInfo.EnumErrorCode.EC_WrongFormatForInputField);
                                event.setLastErrorMessage(checkMsg);
                                globalWriteLock.writeLock().unlock();
                                return;
                            }

                            smallSheetTextMapper.save((SmallSheetText) bm);
                            event.setLastErrorCode(ErrorInfo.EnumErrorCode.EC_NoError);
                        } catch (Exception e) {
                            log.info("更新SmallSheetText时出错，错误信息：" + e.getMessage());
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
    protected boolean deleteAsync(int iUseCaseID, final BaseModel bm, final BaseSQLiteEvent event) {
        log.info("正在进行SmallSheetTextPresenter的deleteAsync，bm=" + (bm == null ? null : bm.toString()));

        switch (iUseCaseID) {
            default:
                new Thread() {
                    @Override
                    public void run() {
//                        event.setEventTypeSQLite(BaseSQLiteEvent.EnumSQLiteEventType.ESET_SmallSheetText_UpdateAsync);
                        //
                        globalWriteLock.writeLock().lock();
                        try {
                            String checkMsg = bm.checkDelete(BaseSQLiteBO.INVALID_CASE_ID);
                            if (!"".equals(checkMsg)) {
                                log.info(checkMsg);
                                event.setLastErrorCode(ErrorInfo.EnumErrorCode.EC_WrongFormatForInputField);
                                event.setLastErrorMessage(checkMsg);
                                globalWriteLock.writeLock().unlock();
                                return;
                            }

                            smallSheetTextMapper.delete(bm.getID());
                            event.setLastErrorCode(ErrorInfo.EnumErrorCode.EC_NoError);
                        } catch (Exception e) {
                            log.info("更新SmallSheetText时出错，错误信息：" + e.getMessage());
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

    public void dropTable() {
        try {
//            smallSheetTextMapper.dropTable(smallSheetTextMapper.getDatabase(), true);
        } catch (Exception e) {
            log.error("删除SQLite的表SmallSheetText时出错，错误信息：" + e.getMessage());
        }
    }

    @Override
    protected String getQueryTable() {
        return QUERY_SMALLSHEETTEXT_TABLE;
    }
}
