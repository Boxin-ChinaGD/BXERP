
package com.bx.erp.presenter;

import com.bx.erp.bo.BaseSQLiteBO;
import com.bx.erp.di.PerActivity;
import com.bx.erp.event.BaseEvent;
import com.bx.erp.event.UI.BaseSQLiteEvent;
import com.bx.erp.helper.Constants;
import com.bx.erp.model.BaseModel;
import com.bx.erp.model.ConfigGeneral;
import com.bx.erp.model.DaoSession;
import com.bx.erp.model.ErrorInfo;
import com.silencedut.taskscheduler.TaskScheduler;

import org.apache.log4j.Logger;
import org.greenrobot.eventbus.EventBus;

import java.util.List;

import static com.bx.erp.bo.BaseSQLiteBO.CASE_ConfigGeneral_UpdateByServerData;

@PerActivity
public class ConfigGeneralPresenter extends BasePresenter {
    private Logger log = Logger.getLogger(this.getClass());

    public ConfigGeneralPresenter(DaoSession dao) {
        super(dao);
    }

    @Override
    protected BaseModel createSync(int iUseCaseID, BaseModel bm) {
        log.info("正在进行ConfigGeneralPresenter的createSync，bm=" + (bm == null ? null : bm.toString()));

        try {
            long id = dao.getConfigGeneralDao().insert((ConfigGeneral) bm);
            lastErrorCode = ErrorInfo.EnumErrorCode.EC_NoError;
            bm.setID(id);
            return bm;
        } catch (Exception e) {
            log.info("执行createSync失败，错误信息为" + e.getMessage());
            e.printStackTrace();
            lastErrorCode = ErrorInfo.EnumErrorCode.EC_OtherError;
            return null;
        }
    }

    @Override
    protected BaseModel retrieve1Sync(int iUseCaseID, BaseModel bm) {
        log.info("正在进行ConfigGeneralPresenter的retrieve1Sync，bm=" + (bm == null ? null : bm.toString()));

        try {
            lastErrorCode = ErrorInfo.EnumErrorCode.EC_NoError;

            return dao.getConfigGeneralDao().load(bm.getID());
        } catch (Exception e) {
            log.info("执行retrieve1Sync失败，错误信息为" + e.getMessage());
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

                    return dao.getConfigGeneralDao().queryRaw(bm.getSql(), bm.getConditions());
                } catch (Exception e) {
                    log.info("执行retrieveNSync的CASE_ConfigGeneral_RetrieveNByConditions失败，错误信息为" + e.getMessage());

                    e.printStackTrace();
                    lastErrorCode = ErrorInfo.EnumErrorCode.EC_OtherError;

                    return null;
                }
            default:
                try {
                    lastErrorCode = ErrorInfo.EnumErrorCode.EC_NoError;

                    return dao.getConfigGeneralDao().loadAll();
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
        log.info("正在进行ConfigGeneralPresenter的createNSync，list=" + (list == null ? null : list.toString()));

        switch (iUseCaseID) {
            default:
                try {
                    for (int i = 0; i < list.size(); i++) {
                        long id = dao.getConfigGeneralDao().insert((ConfigGeneral) list.get(i));
                        ((ConfigGeneral) list.get(i)).setID(id);
                    }
                    lastErrorCode = ErrorInfo.EnumErrorCode.EC_NoError;
                } catch (Exception e) {
                    log.info("执行createNSync失败，错误信息为" + e.getMessage());
                    e.printStackTrace();
                    lastErrorCode = ErrorInfo.EnumErrorCode.EC_OtherError;
                }
                return (List<BaseModel>) list;
        }
    }

    @Override
    protected boolean updateAsync(int iUseCaseID, final BaseModel bm, final BaseSQLiteEvent event) {
        log.info("正在进行ConfigGeneralPresenter的updateAsync，bm=" + (bm == null ? null : bm.toString()));

        switch (iUseCaseID) {
            case CASE_ConfigGeneral_UpdateByServerData:
                TaskScheduler.execute(new Runnable() {
                    @Override
                    public void run() {
                        log.info("开始根据服务器返回的数据Update ConfigGeneral");
                        event.setSyncType(BasePresenter.SYNC_Type_U);
//                        event.setEventTypeSQLite(ESET_ConfigGeneral_UpdateByServerDataAsync);
                        //
                        try {
                            dao.getConfigGeneralDao().update((ConfigGeneral) bm);
                            event.setLastErrorCode(ErrorInfo.EnumErrorCode.EC_NoError);
                            event.setBaseModel1(bm);
                            log.info("根据服务器返回的数据Update ConfigGeneral成功！");
                            event.setData("UPDATE_DONE");
                        } catch (Exception e) {
                            e.printStackTrace();
                            log.info("根据服务器返回的数据Update ConfigGeneral出错，错误信息：" + e.getMessage());
                            event.setLastErrorCode(ErrorInfo.EnumErrorCode.EC_OtherError);
                        }
                        //
                        event.setBaseModel1(bm);
                        event.setStatus(BaseEvent.EnumEventStatus.EES_SQLite_DoneApplyServerData);
                        //
                        EventBus.getDefault().post(event);
                    }
                });
                break;
            default:
                TaskScheduler.execute(new Runnable() {
                    @Override
                    public void run() {
                        log.info("开始update ConfigGeneral");
//                        event.setEventTypeSQLite(ESET_ConfigGeneral_UpdateAsync);
                        //
                        try {
                            dao.getConfigGeneralDao().update((ConfigGeneral) bm);
                            event.setLastErrorCode(ErrorInfo.EnumErrorCode.EC_NoError);
                            log.info("update ConfigGeneral成功");
                        } catch (Exception e) {
                            e.printStackTrace();
                            log.info("update ConfigGeneral出错，错误信息为：" + e.getMessage());
                            event.setLastErrorCode(ErrorInfo.EnumErrorCode.EC_OtherError);
                        }
                        //
                        event.setBaseModel1(bm);
                        event.setStatus(BaseEvent.EnumEventStatus.EES_SQLite_Done);
                        //
                        EventBus.getDefault().post(event);
                    }
                });
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
                TaskScheduler.execute(new Runnable() {
                    @Override
                    public void run() {
                        log.info("已经得到服务器返回的需要进行同步的ConfigGeneral数据，准备进行同步！");

                        try {
                            List<ConfigGeneral> configGeneralList = (List<ConfigGeneral>) bmNewList;
                            //根据返回的数据，查找本地数据库中有没有记录的F_Name与之相同，若有，则删除再进行插入。若无，直接插入
                            for (int i = 0; i < configGeneralList.size(); i++) {
                                ConfigGeneral c = new ConfigGeneral();
                                c.setSql("where F_Name = ?");
                                c.setConditions(new String[]{configGeneralList.get(i).getName()});
                                List<ConfigGeneral> cList = dao.getConfigGeneralDao().queryRaw(c.getSql(), c.getConditions());
                                if (cList != null) {
                                    for (int j = 0; j < cList.size(); j++) {
                                        dao.getConfigGeneralDao().delete(cList.get(j));
                                    }
                                }
                                dao.getConfigGeneralDao().insert(configGeneralList.get(i));

                                event.setLastErrorCode(ErrorInfo.EnumErrorCode.EC_NoError);
                                event.setListMasterTable(bmNewList);
                            }
                        } catch (Exception e) {
                            log.info("执行refreshByServerDataAsyncC失败，错误信息为" + e.getMessage());
                            e.printStackTrace();
                            event.setLastErrorCode(ErrorInfo.EnumErrorCode.EC_OtherError);
                        }
                        event.setStatus(BaseEvent.EnumEventStatus.EES_SQLite_DoneApplyServerData);
                        //
                        EventBus.getDefault().post(event);
                    }
                });
                break;
        }
        return true;
    }

    @Override
    protected BaseModel deleteNSync(int iUserCaseID, BaseModel bm) {
        log.info("正在进行ConfigGeneralPresenter的deleteNSync，bm=" + (bm == null ? null : bm.toString()));

        try {
            dao.getConfigGeneralDao().deleteAll();
            lastErrorCode = ErrorInfo.EnumErrorCode.EC_NoError;
        } catch (Exception e) {
            log.info("执行deleteNSync失败，错误信息为" + e.getMessage());
            e.printStackTrace();
            lastErrorCode = ErrorInfo.EnumErrorCode.EC_OtherError;
        }

        return null;
    }

    @Override
    protected String getTableName() {
        return dao.getConfigGeneralDao().getTablename();
    }
}
