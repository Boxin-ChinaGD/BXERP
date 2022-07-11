package wpos.bo;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import wpos.common.GlobalController;
import wpos.event.BaseHttpEvent;
import wpos.event.UI.BaseSQLiteEvent;
import wpos.model.BaseModel;
import wpos.presenter.BasePresenter;
import wpos.presenter.ConfigGeneralPresenter;

import javax.annotation.Resource;
import java.util.List;

@Component("configGeneralSQLiteBO")
@Scope("prototype")
public class ConfigGeneralSQLiteBO extends BaseSQLiteBO {
    private Log log = LogFactory.getLog(this.getClass());

    public ConfigGeneralSQLiteBO(BaseSQLiteEvent sEvent, BaseHttpEvent hEvent) {
        sqLiteEvent = sEvent;
        httpEvent = hEvent;
    }

    public ConfigGeneralSQLiteBO(){

    }
    
    @Resource
    private ConfigGeneralPresenter configGeneralPresenter;

    public void setConfigGeneralPresenter(ConfigGeneralPresenter configGeneralPresenter) {
        this.configGeneralPresenter = configGeneralPresenter;
    }

    @Override
    public boolean createAsync(int iUseCaseID, BaseModel bm) {
        return false;
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
        return configGeneralPresenter.updateObjectAsync(iUseCaseID, bm, sqLiteEvent);
    }

    @Override
    protected boolean applyServerDataUpdateAsync(BaseModel bmNew) {
        sqLiteEvent.setEventTypeSQLite(BaseSQLiteEvent.EnumSQLiteEventType.ESET_ConfigGeneral_UpdateByServerDataAsync);
        return configGeneralPresenter.updateObjectAsync(CASE_ConfigGeneral_UpdateByServerData, bmNew, sqLiteEvent);
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
        sqLiteEvent.setEventTypeSQLite(BaseSQLiteEvent.EnumSQLiteEventType.ESET_ConfigGeneral_RefreshByServerDataAsyncC_Done);
        return configGeneralPresenter.refreshByServerDataObjectsAsyncC(BaseSQLiteBO.INVALID_CASE_ID, bmList, sqLiteEvent);
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
    public void createTableSync() {
        configGeneralPresenter.createTableSync();
    }
}
