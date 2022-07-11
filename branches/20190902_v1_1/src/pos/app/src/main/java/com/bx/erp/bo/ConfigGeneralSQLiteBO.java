package com.bx.erp.bo;

import android.content.Context;

import com.bx.erp.common.GlobalController;
import com.bx.erp.event.BaseHttpEvent;
import com.bx.erp.event.UI.BaseSQLiteEvent;
import com.bx.erp.helper.Constants;
import com.bx.erp.model.BaseModel;

import org.apache.log4j.Logger;

import java.util.List;

public class ConfigGeneralSQLiteBO extends BaseSQLiteBO {
    private Logger log = Logger.getLogger(this.getClass());

    public ConfigGeneralSQLiteBO(Context ctx, BaseSQLiteEvent sEvent, BaseHttpEvent hEvent) {
        context = ctx;
        sqLiteEvent = sEvent;
        httpEvent = hEvent;
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
        return GlobalController.getInstance().getConfigGeneralPresenter().updateObjectAsync(iUseCaseID, bm, sqLiteEvent);
    }

    @Override
    protected boolean applyServerDataUpdateAsync(BaseModel bmNew) {
        sqLiteEvent.setEventTypeSQLite(BaseSQLiteEvent.EnumSQLiteEventType.ESET_ConfigGeneral_UpdateByServerDataAsync);
        return GlobalController.getInstance().getConfigGeneralPresenter().updateObjectAsync(CASE_ConfigGeneral_UpdateByServerData, bmNew, sqLiteEvent);
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
        return GlobalController.getInstance().getConfigGeneralPresenter().refreshByServerDataObjectsAsyncC(BaseSQLiteBO.INVALID_CASE_ID, bmList, sqLiteEvent);
    }

    @Override
    public boolean deleteAsync(int iUseCaseID, BaseModel bm) {
        return false;
    }

    @Override
    public List<?> retrieveNSync(int iUseCaseID, BaseModel bm) {
        return null;
    }
}
