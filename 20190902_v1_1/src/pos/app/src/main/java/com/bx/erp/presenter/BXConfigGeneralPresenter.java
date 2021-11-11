package com.bx.erp.presenter;


import com.bx.erp.bo.BaseSQLiteBO;
import com.bx.erp.event.BaseEvent;
import com.bx.erp.event.UI.BaseSQLiteEvent;
import com.bx.erp.model.BXConfigGeneral;
import com.bx.erp.model.BaseModel;
import com.bx.erp.model.ConfigGeneral;
import com.bx.erp.model.DaoSession;
import com.bx.erp.model.ErrorInfo;
import com.silencedut.taskscheduler.TaskScheduler;

import org.apache.log4j.Logger;
import org.greenrobot.eventbus.EventBus;

import java.util.List;

public class BXConfigGeneralPresenter extends BasePresenter{
    private Logger log = Logger.getLogger(this.getClass());

    public BXConfigGeneralPresenter(DaoSession dao) {
        super(dao);
    }

    @Override
    public boolean refreshByServerDataAsyncC(final int iUseCaseID, final List<?> bmNewList, final BaseSQLiteEvent event) {
        log.info("正在进行BXConfigGeneralPresenter的refreshByServerDataAsyncC，bmNewList=" + (bmNewList == null ? null : bmNewList.toString()));

        event.setStatus(BaseEvent.EnumEventStatus.EES_SQLite_ToApplyServerData);
        switch (iUseCaseID) {
            default:
                TaskScheduler.execute(new Runnable() {
                    @Override
                    public void run() {
                        log.info("已经得到服务器返回的需要进行同步的ConfigGeneral数据，准备进行同步！");

                        try {
                            List<BXConfigGeneral> bxConfigGeneralList = (List<BXConfigGeneral>) bmNewList;
                            //根据返回的数据，查找本地数据库中有没有记录的F_Name与之相同，若有，则删除再进行插入。若无，直接插入
                            for (int i = 0; i < bxConfigGeneralList.size(); i++) {
                                BXConfigGeneral c = new BXConfigGeneral();
                                c.setSql("where F_Name = ?");
                                c.setConditions(new String[]{bxConfigGeneralList.get(i).getName()});
                                List<BXConfigGeneral> cList = dao.getBXConfigGeneralDao().queryRaw(c.getSql(), c.getConditions());
                                if (cList != null) {
                                    for (int j = 0; j < cList.size(); j++) {
                                        dao.getBXConfigGeneralDao().delete(cList.get(j));
                                    }
                                }
                                dao.getBXConfigGeneralDao().insert(bxConfigGeneralList.get(i));

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
    protected BaseModel createSync(int iUseCaseID, BaseModel bm) {
        log.info("正在进行BXConfigGeneralPresenter的createSync，bm=" + (bm == null ? null : bm.toString()));

        try {
            long id = dao.getBXConfigGeneralDao().insert((BXConfigGeneral) bm);
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
        log.info("正在进行BXConfigGeneralPresenter的retrieve1Sync，bm=" + (bm == null ? null : bm.toString()));

        try {
            lastErrorCode = ErrorInfo.EnumErrorCode.EC_NoError;

            return dao.getBXConfigGeneralDao().load(bm.getID());
        } catch (Exception e) {
            log.info("执行retrieve1Sync失败，错误信息为" + e.getMessage());
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

                    return dao.getBXConfigGeneralDao().queryRaw(bm.getSql(), bm.getConditions());
                } catch (Exception e) {
                    log.info("执行retrieveNSync的CASE_BXConfigGeneral_RetrieveNByConditions失败，错误信息为" + e.getMessage());

                    e.printStackTrace();
                    lastErrorCode = ErrorInfo.EnumErrorCode.EC_OtherError;

                    return null;
                }
            default:
                try {
                    lastErrorCode = ErrorInfo.EnumErrorCode.EC_NoError;

                    return dao.getBXConfigGeneralDao().loadAll();
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
                try {
                    for (int i = 0; i < list.size(); i++) {
                        long id = dao.getBXConfigGeneralDao().insert((BXConfigGeneral) list.get(i));
                        ((BXConfigGeneral) list.get(i)).setID(id);
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
    protected BaseModel deleteNSync(int iUserCaseID, BaseModel bm) {
        log.info("正在进行BXConfigGeneralPresenter的deleteNSync，bm=" + (bm == null ? null : bm.toString()));

        try {
            dao.getBXConfigGeneralDao().deleteAll();
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
        return dao.getBXConfigGeneralDao().getTablename();
    }

}
