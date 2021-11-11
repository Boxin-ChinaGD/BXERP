package com.bx.erp.presenter;

import com.bx.erp.di.PerActivity;
import com.bx.erp.event.UI.BaseSQLiteEvent;
import com.bx.erp.model.BaseModel;
import com.bx.erp.model.Company;
import com.bx.erp.model.DaoSession;
import com.bx.erp.model.ErrorInfo;
import com.silencedut.taskscheduler.TaskScheduler;

import org.apache.log4j.Logger;
import org.greenrobot.eventbus.EventBus;

import java.util.List;

import javax.inject.Inject;

@PerActivity
public class CompanyPresenter extends BasePresenter {
    private Logger log = Logger.getLogger(this.getClass());

    @Inject
    public CompanyPresenter(final DaoSession dao) {
        super(dao);
    }

    @Override
    protected String getTableName() {
        return dao.getCompanyDao().getTablename();
    }

    @Override
    public BaseModel createSync(int iUseCaseID, BaseModel bm) {
        log.info("正在进行CompanyPresenter的createSync，bm=" +(bm == null ? null : bm.toString()));

        try {
//            if (bm.getSyncDatetime() == null) {
//                bm.setSyncDatetime(Constants.getDefaultSyncDatetime2());
//            }
//            bm.setSyncType(Constants.SYNC_Type_C);
            long id = dao.getCompanyDao().insert((Company) bm);
            bm.setID(id);
            lastErrorCode = ErrorInfo.EnumErrorCode.EC_NoError;
        } catch (Exception e) {
            log.info("执行createSync出现错误，错误信息为" + e.getMessage());
            e.printStackTrace();
            lastErrorCode = ErrorInfo.EnumErrorCode.EC_OtherError;
        }
        return bm;
    }

    @Override
    protected boolean retrieveNAsync(int iUseCaseID, final BaseModel bm, final BaseSQLiteEvent event) {
        log.info("正在进行CompanyPresenter的retrieveNAsync，bm=" +(bm == null ? null : bm.toString()));

        //eventTypeSQLite:ESET_Company_RetrieveNAsync
        switch (iUseCaseID) {
            default:
                TaskScheduler.execute(new Runnable() {
                    @Override
                    public void run() {
                        List<Company> companyList = null;
                        //
                        try {
                            companyList = dao.getCompanyDao().loadAll();
                            if (companyList.size() > 0) {
                                Company company = new Company();
                                company = companyList.get(0);
                                if (company.getSN() != null && !"".equals(company.getSN())) {
                                    event.setBaseModel1(company);
                                } else {
                                    event.setBaseModel1(null);
                                }
                            } else {
                                event.setBaseModel1(null);
                            }
                            event.setLastErrorCode(ErrorInfo.EnumErrorCode.EC_NoError);
                        } catch (Exception e) {
                            e.printStackTrace();
                            event.setLastErrorCode(ErrorInfo.EnumErrorCode.EC_OtherError);
                            log.info("在本地查询所有Company出错，错误信息为：" + e.getMessage());
                        }
                        event.setListMasterTable(companyList);
                        //
                        EventBus.getDefault().post(event);
                    }
                });
                break;
        }
        return true;
    }

    public boolean createAsync(final int iUseCaseID, final BaseModel bm, final BaseSQLiteEvent event) {
        log.info("正在进行CompanyPresenter的createAsync，bm=" +(bm == null ? null : bm.toString()));

        //eventTypeSQLite:ESET_Company_CreateAsync
        switch (iUseCaseID) {
            default:
                TaskScheduler.execute(new Runnable() {
                    @Override
                    public void run() {
                        Company c = (Company) bm;
                        try {
                            c.setSql("where F_SN = ?");
                            c.setConditions(new String[]{c.getSN()});
                            List<Company> companyList = dao.getCompanyDao().queryRaw(c.getSql(), c.getConditions());
                            if (companyList.size() > 0) {
                                dao.getCompanyDao().delete(c);
                                dao.getCompanyDao().insert(c);
                            } else {
                                dao.getCompanyDao().insert(c);
                            }
                            event.setLastErrorCode(ErrorInfo.EnumErrorCode.EC_NoError);
                        } catch (Exception e) {
                            log.info("执行createAsync失败，错误信息为" + e.getMessage());
                            e.printStackTrace();
                            event.setLastErrorCode(ErrorInfo.EnumErrorCode.EC_OtherError);
                        }
                        event.setBaseModel1(c);
                        //
                        EventBus.getDefault().post(event);
                    }
                });
                break;
        }
        return true;
    }

    @Override
    protected BaseModel deleteNSync(int iUseCaseID, final BaseModel bm) {
        log.info("正在进行CompanyPresenter的deleteNSync，bm=" +(bm == null ? null : bm.toString()));

        try {
            dao.getCompanyDao().deleteAll();
            lastErrorCode = ErrorInfo.EnumErrorCode.EC_NoError;
        } catch (Exception e) {
            log.info("执行deleteNSync出现异常，错误信息为" + e.getMessage());
            e.printStackTrace();
            lastErrorCode = ErrorInfo.EnumErrorCode.EC_OtherError;
        }
        return null;
    }
}
