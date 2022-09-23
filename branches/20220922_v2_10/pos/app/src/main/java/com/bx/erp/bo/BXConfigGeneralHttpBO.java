package com.bx.erp.bo;

import android.content.Context;

import com.bx.erp.common.GlobalController;
import com.bx.erp.event.BaseEvent;
import com.bx.erp.event.BaseHttpEvent;
import com.bx.erp.event.UI.BaseSQLiteEvent;
import com.bx.erp.helper.Configuration;
import com.bx.erp.http.HttpRequestManager;
import com.bx.erp.http.HttpRequestUnit;
import com.bx.erp.http.request.RetrieveNCBXConfigGeneral;
import com.bx.erp.http.request.RetrieveNCConfigGeneral;
import com.bx.erp.model.BXConfigGeneral;
import com.bx.erp.model.BaseModel;
import com.bx.erp.model.ConfigGeneral;

import org.apache.log4j.Logger;

import java.util.List;

import okhttp3.Request;

public class BXConfigGeneralHttpBO extends BaseHttpBO{
    private Logger log = Logger.getLogger(this.getClass());

    public BXConfigGeneralHttpBO(Context ctx, BaseSQLiteEvent sEvent, BaseHttpEvent hEvent) {
        context = ctx;
        sqLiteEvent = sEvent;
        httpEvent = hEvent;
    }

    @Override
    protected boolean doCreateNSync(int iUseCaseID, List<BaseModel> list) {
        return false;
    }

    @Override
    protected boolean doCreateNAsync(int iUseCaseID, List<BaseModel> list) {
        return false;
    }

    @Override
    protected boolean doCreateAsyncC(int iUseCaseID, BaseModel bm) {
        return false;
    }

    @Override
    protected boolean doCreateAsync(int iUseCaseID, BaseModel bm) {
        return false;
    }

    @Override
    protected boolean doUpdateAsync(int iUseCaseID, BaseModel bm) {
        return false;
    }

    @Override
    protected boolean doRetrieveNAsync(int iUseCaseID, BaseModel bm) {
        return false;
    }

    @Override
    protected boolean doDeleteAsync(int iUseCaseID, BaseModel bm) {
        return false;
    }

    @Override
    public boolean feedback(String feedbackIDs) {
        return false;
    }

    @Override
    protected boolean doRetrieveNAsyncC(int iUseCaseID, BaseModel bm) {
        log.info("正在执行BXConfigGeneralHttpBO的retrieveNAsyncC，bm=" +(bm == null ? null : bm.toString()));

        Request req = new Request.Builder()
                .url(Configuration.HTTP_IP + BXConfigGeneral.HTTP_ConfigGeneral_RETRIEVEN)
                .addHeader(BaseHttpBO.COOKIE, GlobalController.getInstance().getSessionID())
                .build();
        HttpRequestUnit hru = new RetrieveNCBXConfigGeneral();

        hru.setRequest(req);
        hru.setTimeout(TIME_OUT);
        hru.setbPostEventToUI(true);
        httpEvent.setEventProcessed(false);
        httpEvent.setStatus(BaseEvent.EnumEventStatus.EES_Http_ToDo);
        hru.setEvent(httpEvent);
        HttpRequestManager.getCache(HttpRequestManager.EnumDomainType.EDT_Communication).pushHttpRequest(hru);

        log.info("正在请求所有的BXConfigGeneral");

        return true;
    }

    @Override
    protected boolean doRetrieve1AsyncC(int iUseCaseID, BaseModel bm) {
        return false;
    }


}
