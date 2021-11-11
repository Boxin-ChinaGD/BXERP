package com.bx.erp.presenter;

import com.bx.erp.event.UI.BaseSQLiteEvent;
import com.bx.erp.model.BaseModel;
import com.bx.erp.model.DaoSession;
import com.bx.erp.model.ErrorInfo;
import com.bx.erp.model.RememberLoginStaff;
import com.silencedut.taskscheduler.TaskScheduler;

import org.apache.log4j.Logger;

import java.util.List;

import javax.inject.Inject;

public class RememberLoginStaffPresenter extends BasePresenter {
    private Logger log = Logger.getLogger(this.getClass());

    @Inject
    public RememberLoginStaffPresenter(DaoSession dao) {
        super(dao);
    }

    @Override
    protected boolean createAsync(int iUseCaseID, final BaseModel bm, BaseSQLiteEvent event) {
        log.debug("xxxxxxxxxxxxx  RememberLoginStaffPresenter  createAsync");

        TaskScheduler.execute(new Runnable() {
            @Override
            public void run() {
                // 因为只记住一个账号密码，所以删除了表中的其他账号密码，再插入新的，以后需要记住多个账号密码需要修改 deleteAll()；
                try {
                    lastErrorCode = ErrorInfo.EnumErrorCode.EC_NoError;
                    lastErrorMessage = "";

                    dao.getRememberLoginStaffDao().deleteAll();
                    dao.getRememberLoginStaffDao().insert((RememberLoginStaff) bm);
                } catch (Exception e) {
                    log.info("执行RememberLoginStaffPresenter.createAsync()时出错，错误信息：" + e.getMessage());
                    e.printStackTrace();
                    lastErrorCode = ErrorInfo.EnumErrorCode.EC_OtherError;
                    lastErrorMessage = e.getMessage();
                    //... 由于记住密码失败也无所谓，所以此处不需要post event 出去给UI层接收。无非要求收银员下次再输入多一次密码而已
                }
            }
        });

        return true;
    }

    @Override
    protected List<?> retrieveNSync(int iUseCaseID, BaseModel bm) {
        log.debug("xxxxxxxxxxxxx  RememberLoginStaffPresenter  retrieveNSync");

        try {
            lastErrorCode = ErrorInfo.EnumErrorCode.EC_NoError;
            lastErrorMessage = "";
            return dao.getRememberLoginStaffDao().loadAll();
        } catch (Exception e) {
            log.info("执行RememberLoginStaffPresenter.retrieveNSync()时出错，错误信息：" + e.getMessage());
            e.printStackTrace();
            lastErrorCode = ErrorInfo.EnumErrorCode.EC_OtherError;
            lastErrorMessage = e.getMessage();
        }
        return null;
    }

}
