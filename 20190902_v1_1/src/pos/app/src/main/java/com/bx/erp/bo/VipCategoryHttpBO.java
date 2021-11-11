package com.bx.erp.bo;

import android.content.Context;

import com.bx.erp.common.GlobalController;
import com.bx.erp.event.BaseEvent;
import com.bx.erp.event.BaseHttpEvent;
import com.bx.erp.event.UI.BaseSQLiteEvent;
import com.bx.erp.helper.Configuration;
import com.bx.erp.http.HttpRequestManager;
import com.bx.erp.http.HttpRequestUnit;
import com.bx.erp.http.request.CreateVipCategory;
import com.bx.erp.http.request.RetrieveNVipCategoryC;
import com.bx.erp.http.request.UpdateVipCategory;
import com.bx.erp.model.BaseModel;
import com.bx.erp.model.VipCategory;

import org.apache.log4j.Logger;

import java.util.List;

import okhttp3.FormBody;
import okhttp3.Request;
import okhttp3.RequestBody;

public class VipCategoryHttpBO extends BaseHttpBO {
    private Logger log = Logger.getLogger(this.getClass());

    public VipCategoryHttpBO(Context ctx, BaseSQLiteEvent sEvent, BaseHttpEvent hEvent) {
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
        log.info("正在执行VipCategoryHttpBO的createAsyncC，bm=" + bm);

        VipCategory vipCategory = (VipCategory) bm;

        RequestBody body = new FormBody.Builder()
                .add(vipCategory.field.getFIELD_NAME_name(), vipCategory.getName())
                .add(vipCategory.field.getFIELD_NAME_returnObject(), String.valueOf(vipCategory.getReturnObject()))
                .build();
        Request req = new Request.Builder()
                .url(Configuration.HTTP_IP + VipCategory.HTTP_VIPCATEGORY_CreateC)
                .addHeader(BaseHttpBO.COOKIE, GlobalController.getInstance().getSessionID())
                .post(body)
                .build();
        HttpRequestUnit hru = new CreateVipCategory();
        hru.setRequest(req);
        hru.setTimeout(TIME_OUT);
        hru.setbPostEventToUI(true);
        httpEvent.setEventProcessed(false);
        httpEvent.setStatus(BaseEvent.EnumEventStatus.EES_Http_ToDo);
        hru.setEvent(httpEvent);
        HttpRequestManager.getCache(HttpRequestManager.EnumDomainType.EDT_Communication).pushHttpRequest(hru);

        log.info("正在请求服务器创建VipCategory...");

        return true;
    }

    @Override
    protected boolean doCreateAsync(int iUseCaseID, BaseModel bm) {
        return false;
    }

    @Override
    protected boolean doUpdateAsync(int iUseCaseID, BaseModel bm) {
        log.info("正在执行VipCategoryHttpBO的updateAsync，bm=" +(bm == null ? null : bm.toString()));

        VipCategory vipCategory = (VipCategory) bm;

        RequestBody body = new FormBody.Builder()
                .add(vipCategory.field.getFIELD_NAME_ID(), String.valueOf(vipCategory.getID()))
                .add(vipCategory.field.getFIELD_NAME_name(), vipCategory.getName())
                .add(vipCategory.field.getFIELD_NAME_returnObject(), String.valueOf(vipCategory.getReturnObject()))
                .build();
        Request req = new Request.Builder()
                .url(Configuration.HTTP_IP + VipCategory.HTTP_VIPCATEGORY_UpdateC)
                .addHeader(BaseHttpBO.COOKIE, GlobalController.getInstance().getSessionID())
                .post(body)
                .build();
        HttpRequestUnit hru = new UpdateVipCategory();
        hru.setRequest(req);
        hru.setTimeout(TIME_OUT);
        hru.setbPostEventToUI(true);
        httpEvent.setEventProcessed(false);
        httpEvent.setStatus(BaseEvent.EnumEventStatus.EES_Http_ToDo);
        hru.setEvent(httpEvent);
        HttpRequestManager.getCache(HttpRequestManager.EnumDomainType.EDT_Communication).pushHttpRequest(hru);

        log.info("正在请求服务器修改VipCategory...");

        return true;
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
        log.info("正在执行VipCategoryHttpBO的retrieveNAsyncC，bm=" +(bm == null ? null : bm.toString()));
        
        RequestBody body = new FormBody.Builder()
                .build();
        Request req = new Request.Builder()
                .url(Configuration.HTTP_IP + VipCategory.HTTP_VIPCategoty_RETRIEVENC_PageIndex + bm.getPageIndex() + VipCategory.HTTP_VIPCategoty_RETRIEVENC_PageSize + bm.getPageSize())
                .addHeader(BaseHttpBO.COOKIE, GlobalController.getInstance().getSessionID())
                .post(body)
                .build();
        HttpRequestUnit hru = new RetrieveNVipCategoryC();
        hru.setRequest(req);
        hru.setTimeout(TIME_OUT);
        hru.setbPostEventToUI(true);
        httpEvent.setEventProcessed(false);
        httpEvent.setStatus(BaseEvent.EnumEventStatus.EES_Http_ToDo);
        hru.setEvent(httpEvent);
        HttpRequestManager.getCache(HttpRequestManager.EnumDomainType.EDT_Communication).pushHttpRequest(hru);

        log.info("正在发送RN VipCategory请求到服务器....");

        return true;
    }

    @Override
    protected boolean doRetrieve1AsyncC(int iUseCaseID, BaseModel bm) {
        return false;
    }
}
