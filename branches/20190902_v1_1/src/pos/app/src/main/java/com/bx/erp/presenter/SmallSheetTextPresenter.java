package com.bx.erp.presenter;

import android.util.Log;

import com.bx.erp.bo.BaseSQLiteBO;
import com.bx.erp.di.PerActivity;
import com.bx.erp.event.UI.BaseSQLiteEvent;
import com.bx.erp.helper.Constants;
import com.bx.erp.model.BaseModel;
import com.bx.erp.model.DaoSession;
import com.bx.erp.model.ErrorInfo;
import com.bx.erp.model.SmallSheetText;
import com.silencedut.taskscheduler.TaskScheduler;

import org.apache.log4j.Logger;
import org.greenrobot.eventbus.EventBus;

import java.util.List;

import javax.inject.Inject;

@PerActivity
public class SmallSheetTextPresenter extends BasePresenter {
    private Logger log = Logger.getLogger(this.getClass());

    @Inject
    public SmallSheetTextPresenter(final DaoSession dao) {
        super(dao);
    }

    @Override
    protected String getTableName() {
        return dao.getSmallSheetTextDao().getTablename();
    }


    @Override
    protected List<BaseModel> createNSync(int iUseCaseID, final List<?> list) {
        log.info("正在进行SmallSheetTextPresenter的createNSync，list=" + (list == null ? null : list.toString()));

        try {
            for (int i = 0; i < list.size(); i++) {
                long id = dao.getSmallSheetTextDao().insert((SmallSheetText) list.get(i));
                ((SmallSheetText) list.get(i)).setID(id);
                lastErrorCode = ErrorInfo.EnumErrorCode.EC_NoError;
            }
        } catch (Exception e) {
            log.info("执行createNSync失败，错误信息为" + e.getMessage());
            e.printStackTrace();
            lastErrorCode = ErrorInfo.EnumErrorCode.EC_OtherError;
        }
        return (List<BaseModel>) list;
    }

    @Override
    protected BaseModel createSync(int iUseCaseID, BaseModel bm) {
        log.info("正在进行SmallSheetTextPresenter的createSync，bm=" + (bm == null ? null : bm.toString()));

        try {
            SmallSheetText smallSheetText = (SmallSheetText) bm;
            smallSheetText.setSyncType(BasePresenter.SYNC_Type_C);
            if (smallSheetText.getSyncDatetime() == null) {
                smallSheetText.setSyncDatetime(Constants.getDefaultSyncDatetime2());
            }
            long id = dao.getSmallSheetTextDao().insert(smallSheetText);
            bm.setID(id);
            lastErrorCode = ErrorInfo.EnumErrorCode.EC_NoError;
        } catch (Exception e) {
            log.info("执行createSync失败，错误信息为" + e.getMessage());
            e.printStackTrace();
            lastErrorCode = ErrorInfo.EnumErrorCode.EC_OtherError;
        }

        return bm;
    }

    @Override
    protected boolean updateSync(int iUseCaseID, BaseModel bm) {
        log.info("正在进行SmallSheetTextPresenter的updateSync，bm=" + (bm == null ? null : bm.toString()));

        try {
            dao.getSmallSheetTextDao().update((SmallSheetText) bm);
            lastErrorCode = ErrorInfo.EnumErrorCode.EC_NoError;

            return true;
        } catch (Exception e) {
            log.info("执行updateSync失败，错误信息为" + e.getMessage());
            e.printStackTrace();
            lastErrorCode = ErrorInfo.EnumErrorCode.EC_OtherError;
            return false;
        }
    }

    @Override
    protected BaseModel retrieve1Sync(int iUseCaseID, BaseModel bm) {
        log.info("正在进行SmallSheetTextPresenter的retrieve1Sync，bm=" + (bm == null ? null : bm.toString()));

        try {
            lastErrorCode = ErrorInfo.EnumErrorCode.EC_NoError;

            return dao.getSmallSheetTextDao().load(bm.getID());
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

                    return dao.getSmallSheetTextDao().queryRaw(bm.getSql(), bm.getConditions());
                } catch (Exception e) {
                    log.info("执行retrieveNSync失败，错误信息为" + e.getMessage());
                    e.printStackTrace();
                    lastErrorCode = ErrorInfo.EnumErrorCode.EC_OtherError;

                    return null;
                }
            default:
                try {
                    lastErrorCode = ErrorInfo.EnumErrorCode.EC_NoError;

                    return dao.getSmallSheetTextDao().loadAll();
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

        try {
            dao.clear();

            dao.getSmallSheetTextDao().deleteByKey(bm.getID());
            lastErrorCode = ErrorInfo.EnumErrorCode.EC_NoError;
        } catch (Exception e) {
            log.info("执行deleteSync失败，错误信息为" + e.getMessage());
            e.printStackTrace();
            lastErrorCode = ErrorInfo.EnumErrorCode.EC_OtherError;
        }

        return null;
    }

    @Override
    protected BaseModel deleteNSync(int iUseCaseID, final BaseModel bm) {
        log.info("正在进行SmallSheetTextPresenter的deleteNSync，bm=" + (bm == null ? null : bm.toString()));

        try {
            dao.getSmallSheetTextDao().deleteAll();
            lastErrorCode = ErrorInfo.EnumErrorCode.EC_NoError;
        } catch (Exception e) {
            log.info("执行deleteNSync失败，错误信息为" + e.getMessage());
            e.printStackTrace();
            lastErrorCode = ErrorInfo.EnumErrorCode.EC_OtherError;
        }

        return null;
    }

    @Override
    protected boolean createNAsync(int iUseCaseID, final List<?> list, final BaseSQLiteEvent event) {
        log.info("正在进行SmallSheetTextPresenter的createNAsync，list=" + (list == null ? null : list.toString()));

        switch (iUseCaseID) {
            default:
                TaskScheduler.execute(new Runnable() {
                    @Override
                    public void run() {
//                        event.setEventTypeSQLite(BaseSQLiteEvent.EnumSQLiteEventType.ESET_SmallSheetText_CreateNAsync);
                        //
                        try {
                            for (int i = 0; i < list.size(); i++) {
                                String checkMsg = ((SmallSheetText)list.get(i)).checkRetrieveN(BaseSQLiteBO.INVALID_CASE_ID);
                                if (!"".equals(checkMsg)){
                                    log.info(checkMsg);
                                    event.setLastErrorCode(ErrorInfo.EnumErrorCode.EC_WrongFormatForInputField);
                                    event.setLastErrorMessage(checkMsg);
                                    return;
                                }

                                long id = dao.getSmallSheetTextDao().insert((SmallSheetText) list.get(i));
                                ((SmallSheetText) list.get(i)).setID(id);
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
                    }
                });
                break;
        }
        return true;
    }

    @Override
    protected boolean retrieveNAsync(int iUseCaseID, final BaseModel bm, final BaseSQLiteEvent event) {
        log.info("正在进行SmallSheetTextPresenter的retrieveNAsync，bm=" + (bm == null ? null : bm.toString()));

        switch (iUseCaseID) {
            case BaseSQLiteBO.CASE_SmallSheetText_RetrieveNByConditions:
                TaskScheduler.execute(new Runnable() {
                    @Override
                    public void run() {
                        List<SmallSheetText> result = null;
//                        event.setEventTypeSQLite(BaseSQLiteEvent.EnumSQLiteEventType.ESET_SmallSheetText_RetrieveNAsync);
                        //
                        try {
                            String checkMsg = bm.checkRetrieveN(BaseSQLiteBO.INVALID_CASE_ID);
                            if (!"".equals(checkMsg)){
                                log.info(checkMsg);
                                event.setLastErrorCode(ErrorInfo.EnumErrorCode.EC_WrongFormatForInputField);
                                event.setLastErrorMessage(checkMsg);
                                return;
                            }

                            result = dao.getSmallSheetTextDao().queryRaw(bm.getSql(), bm.getConditions());
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
                });
                break;
            default:
                TaskScheduler.execute(new Runnable() {
                    @Override
                    public void run() {
                        List<SmallSheetText> result = null;
//                        event.setEventTypeSQLite(BaseSQLiteEvent.EnumSQLiteEventType.ESET_SmallSheetText_RetrieveNAsync);
                        //
                        try {
                            String checkMsg = bm.checkRetrieveN(BaseSQLiteBO.INVALID_CASE_ID);
                            if (!"".equals(checkMsg)){
                                log.info(checkMsg);
                                event.setLastErrorCode(ErrorInfo.EnumErrorCode.EC_WrongFormatForInputField);
                                event.setLastErrorMessage(checkMsg);
                                return;
                            }

                            result = dao.getSmallSheetTextDao().loadAll();//...
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
                });
                break;
        }

        return true;
    }

    @Override
    protected boolean createAsync(int iUseCaseID, final BaseModel bm, final BaseSQLiteEvent event) {
        log.info("正在进行SmallSheetTextPresenter的createAsync，bm=" + (bm == null ? null : bm.toString()));

        switch (iUseCaseID) {
            default:
                TaskScheduler.execute(new Runnable() {
                    @Override
                    public void run() {
//                        event.setEventTypeSQLite(BaseSQLiteEvent.EnumSQLiteEventType.ESET_SmallSheetText_CreateAsync);
                        //
                        try {
                            String checkMsg = bm.checkCreate(BaseSQLiteBO.INVALID_CASE_ID);
                            if (!"".equals(checkMsg)){
                                log.info(checkMsg);
                                event.setLastErrorCode(ErrorInfo.EnumErrorCode.EC_WrongFormatForInputField);
                                event.setLastErrorMessage(checkMsg);
                                return;
                            }

                            long id = dao.getSmallSheetTextDao().insert((SmallSheetText) bm);
                            ((SmallSheetText) bm).setID(id);
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
                    }
                });
                break;
        }

        return true;
    }

    @Override
    protected boolean retrieve1Async(int iUseCaseID, final BaseModel bm, final BaseSQLiteEvent event) {
        log.info("正在进行SmallSheetTextPresenter的retrieve1Async，bm=" + (bm == null ? null : bm.toString()));

        switch (iUseCaseID) {
            default:
                TaskScheduler.execute(new Runnable() {
                    @Override
                    public void run() {
                        SmallSheetText result = null;
//                        event.setEventTypeSQLite(BaseSQLiteEvent.EnumSQLiteEventType.ESET_SmallSheetText_Retrieve1Async);
                        //
                        try {
                            result = dao.getSmallSheetTextDao().load(((SmallSheetText) bm).getID());//...
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
                });
                break;
        }

        return true;
    }

    @Override
    protected boolean updateAsync(int iUseCaseID, final BaseModel bm, final BaseSQLiteEvent event) {
        log.info("正在进行SmallSheetTextPresenter的updateAsync，bm=" + (bm == null ? null : bm.toString()));

        switch (iUseCaseID) {
            default:
                TaskScheduler.execute(new Runnable() {
                    @Override
                    public void run() {
//                        event.setEventTypeSQLite(BaseSQLiteEvent.EnumSQLiteEventType.ESET_SmallSheetText_UpdateAsync);
                        //
                        try {
                            String checkMsg = bm.checkUpdate(BaseSQLiteBO.INVALID_CASE_ID);
                            if (!"".equals(checkMsg)){
                                log.info(checkMsg);
                                event.setLastErrorCode(ErrorInfo.EnumErrorCode.EC_WrongFormatForInputField);
                                event.setLastErrorMessage(checkMsg);
                                return;
                            }

                            dao.getSmallSheetTextDao().update((SmallSheetText) bm);
                            event.setLastErrorCode(ErrorInfo.EnumErrorCode.EC_NoError);
                        } catch (Exception e) {
                            log.info("更新SmallSheetText时出错，错误信息：" + e.getMessage());
                            event.setLastErrorCode(ErrorInfo.EnumErrorCode.EC_OtherError);
                        }
                        //
                        event.setBaseModel1(bm);
                        //
                        EventBus.getDefault().post(event);
                    }
                });
                break;
        }

        return true;
    }

    @Override
    protected boolean deleteAsync(int iUseCaseID, final BaseModel bm, final BaseSQLiteEvent event) {
        log.info("正在进行SmallSheetTextPresenter的deleteAsync，bm=" + (bm == null ? null : bm.toString()));

        switch (iUseCaseID) {
            default:
                TaskScheduler.execute(new Runnable() {
                    @Override
                    public void run() {
//                        event.setEventTypeSQLite(BaseSQLiteEvent.EnumSQLiteEventType.ESET_SmallSheetText_UpdateAsync);
                        //
                        try {
                            String checkMsg = bm.checkDelete(BaseSQLiteBO.INVALID_CASE_ID);
                            if (!"".equals(checkMsg)){
                                log.info(checkMsg);
                                event.setLastErrorCode(ErrorInfo.EnumErrorCode.EC_WrongFormatForInputField);
                                event.setLastErrorMessage(checkMsg);
                                return;
                            }

                            dao.getSmallSheetTextDao().deleteByKey(bm.getID());
                            event.setLastErrorCode(ErrorInfo.EnumErrorCode.EC_NoError);
                        } catch (Exception e) {
                            log.info("更新SmallSheetText时出错，错误信息：" + e.getMessage());
                            event.setLastErrorCode(ErrorInfo.EnumErrorCode.EC_OtherError);
                        }
                        //
                        event.setBaseModel1(bm);
                        //
                        EventBus.getDefault().post(event);
                    }
                });
                break;
        }

        return true;
    }

    public void dropTable() {
        try {
            dao.getSmallSheetTextDao().dropTable(dao.getSmallSheetTextDao().getDatabase(), true);
        } catch (Exception e) {
            Log.e("输出：", "删除SQLite的表SmallSheetText时出错，错误信息：" + e.getMessage());
        }
    }
}
