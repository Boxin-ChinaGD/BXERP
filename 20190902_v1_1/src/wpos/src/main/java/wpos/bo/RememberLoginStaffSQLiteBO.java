package wpos.bo;


import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import wpos.common.GlobalController;
import wpos.event.UI.BaseSQLiteEvent;
import wpos.model.BaseModel;
import wpos.presenter.BasePresenter;
import wpos.presenter.RememberLoginStaffPresenter;

import javax.annotation.Resource;
import java.util.List;

@Component("rememberLoginStaffSQLiteBO")
@Scope("prototype")
public class RememberLoginStaffSQLiteBO extends BaseSQLiteBO {
    private Log log = LogFactory.getLog(this.getClass());

    @Resource
    private RememberLoginStaffPresenter rememberLoginStaffPresenter;

    public RememberLoginStaffSQLiteBO(BaseSQLiteEvent sEvent) {
        sqLiteEvent = sEvent;
    }

    public RememberLoginStaffSQLiteBO() {
        
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
        return rememberLoginStaffPresenter.retrieveNObjectSync(iUseCaseID, bm);
    }

    @Override
    public void createTableSync() {
        rememberLoginStaffPresenter.createTableSync();
    }

    @Override
    public boolean createAsync(int iUseCaseID, BaseModel bm) {
//        log.debug("xxxxxxxxxxxxxx   RememberLoginStaffSQLiteBO  createAsync");
        if (bm != null) {
            String error = bm.checkCreate(BaseSQLiteBO.INVALID_CASE_ID);
            if (!error.equals("")) {
                log.error("checkCreate失败，错误信息：" + error);
            } else {
                //todo GlobalController.getInstance()暂时还没好
                if (rememberLoginStaffPresenter.createObjectAsync(iUseCaseID, bm, sqLiteEvent)) {
                    return true;
                } else {
                    log.error("记住密码失败");
                }
            }
        }
        return false;
    }
}
