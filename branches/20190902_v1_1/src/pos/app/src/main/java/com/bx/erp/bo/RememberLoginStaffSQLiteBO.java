package com.bx.erp.bo;

import android.content.Context;

import com.bx.erp.common.GlobalController;
import com.bx.erp.event.UI.BaseSQLiteEvent;
import com.bx.erp.model.BaseModel;

import org.apache.log4j.Logger;

import java.util.List;

public class RememberLoginStaffSQLiteBO extends BaseSQLiteBO {
    private Logger log = Logger.getLogger(this.getClass());

    public RememberLoginStaffSQLiteBO(Context ctx, BaseSQLiteEvent sEvent) {
        context = ctx;
        sqLiteEvent = sEvent;
    }

    @Override
    protected boolean applyServerDataListAsync(List<BaseModel> bmList) {
        return false;
    }

    @Override
    protected boolean applyServerDataAsync(BaseModel bmNew) {
        return false;
    }

    @Override
    public boolean updateAsync(int iUseCaseID, BaseModel bm) {
        return false;
    }

    @Override
    protected boolean applyServerDataUpdateAsync(BaseModel bmNew) {
        return false;
    }

    @Override
    protected boolean applyServerDataUpdateAsyncN(List<?> bmList) {
        return false;
    }

    @Override
    protected boolean applyServerDataDeleteAsync(BaseModel bmDelete) {
        return false;
    }

    @Override
    protected boolean applyServerListDataAsync(List<BaseModel> bmList) {
        return false;
    }

    @Override
    protected boolean applyServerListDataAsyncC(List<BaseModel> bmList) {
        return false;
    }

    @Override
    public boolean deleteAsync(int iUseCaseID, BaseModel bm) {
        return false;
    }

    @Override
    public List<?> retrieveNSync(int iUseCaseID, BaseModel bm) {
        return GlobalController.getInstance().getRememberLoginStaffPresenter().retrieveNObjectSync(iUseCaseID, bm);
    }

    @Override
    public boolean createAsync(int iUseCaseID, BaseModel bm) {
        log.debug("xxxxxxxxxxxxxx   RememberLoginStaffSQLiteBO  createAsync");
        if (bm != null) {
            String error = bm.checkCreate(BaseSQLiteBO.INVALID_CASE_ID);
            if (!error.equals("")) {
                log.error("checkCreate失败，错误信息：" + error);
            } else {
                if (GlobalController.getInstance().getRememberLoginStaffPresenter().createObjectAsync(iUseCaseID, bm, sqLiteEvent)) {
                    return true;
                } else {
                    log.error("记住密码失败");
                }
            }
        }
        return false;
    }
}
