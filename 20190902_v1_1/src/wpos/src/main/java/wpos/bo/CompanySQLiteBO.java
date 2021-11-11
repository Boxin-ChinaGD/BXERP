package wpos.bo;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import wpos.common.GlobalController;
import wpos.event.BaseHttpEvent;
import wpos.event.CompanyHttpEvent;
import wpos.event.UI.BaseSQLiteEvent;
import wpos.event.UI.CompanySQLiteEvent;
import wpos.model.BaseModel;
import wpos.presenter.BasePresenter;
import wpos.presenter.CompanyPresenter;

import javax.annotation.Resource;
import java.util.List;

@Component("companySQLiteBO")
@Scope("prototype")
public class CompanySQLiteBO extends BaseSQLiteBO {
    public CompanySQLiteBO(BaseSQLiteEvent sEvent, BaseHttpEvent hEvent) {
        sqLiteEvent = sEvent;
        httpEvent = hEvent;
    }

    public CompanySQLiteBO(){
    }


    
    @Resource
    private CompanyPresenter companyPresenter;


    @Override
    public boolean createAsync(int iUseCaseID, BaseModel bm) {
        sqLiteEvent.setEventTypeSQLite(BaseSQLiteEvent.EnumSQLiteEventType.ESET_Company_CreateAsync);
        return companyPresenter.createObjectAsync(BaseSQLiteBO.INVALID_CASE_ID, bm, sqLiteEvent);
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
        return null;
    }

    @Override
    public boolean retrieveNASync(int iUseCaseID, BaseModel bm) {
        return companyPresenter.retrieveNObjectAsync(iUseCaseID, null, sqLiteEvent);
    }

    @Override
    public void createTableSync() {
        companyPresenter.createTableSync();
    }
}
