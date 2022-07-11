package com.bx.erp.bo;

import android.content.Context;

import com.bx.erp.common.GlobalController;
import com.bx.erp.event.BaseEvent;
import com.bx.erp.event.BaseHttpEvent;
import com.bx.erp.event.UI.BaseSQLiteEvent;
import com.bx.erp.helper.Configuration;
import com.bx.erp.helper.Constants;
import com.bx.erp.http.HttpRequestManager;
import com.bx.erp.http.HttpRequestUnit;
import com.bx.erp.http.request.CreateShop;
import com.bx.erp.http.request.DeleteShop;
import com.bx.erp.model.BaseModel;
import com.bx.erp.model.Shop;

import org.apache.log4j.Logger;

import java.util.List;

import okhttp3.FormBody;
import okhttp3.Request;
import okhttp3.RequestBody;

public class ShopHttpBO  extends BaseHttpBO{
    private Logger log = Logger.getLogger(this.getClass());
    
    public ShopHttpBO(Context ctx, BaseSQLiteEvent sEvent, BaseHttpEvent hEvent) {
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
        log.info("正在执行ShopHttpBO的createAsync，bm=" +(bm == null ? null : bm.toString()));
        
        Shop shop = (Shop) bm;

        RequestBody body = new FormBody.Builder()
                .add(shop.field.getFIELD_NAME_name(), shop.getName())//
                .add(shop.field.getFIELD_NAME_companyID(), String.valueOf(shop.getCompanyID()))//
                .add(shop.field.getFIELD_NAME_address(), shop.getAddress())//
                .add(shop.field.getFIELD_NAME_status(), String.valueOf(shop.getStatus()))//
                .add(shop.field.getFIELD_NAME_longitude(), String.valueOf(shop.getLongitude()))//
                .add(shop.field.getFIELD_NAME_latitude(), String.valueOf(shop.getLatitude()))//
                .add(shop.field.getFIELD_NAME_key(), shop.getKey())//
                .build();

        Request req = new Request.Builder()
                .url(Configuration.HTTP_IP + shop.HTTP_Shop_Create)
                .addHeader(BaseHttpBO.COOKIE, GlobalController.getInstance().getSessionID())
                .post(body)
                .build();
        HttpRequestUnit hru = new CreateShop();
        hru.setRequest(req);
        hru.setTimeout(TIME_OUT);
        hru.setbPostEventToUI(true);
        httpEvent.setStatus(BaseEvent.EnumEventStatus.EES_Http_ToDo);
        httpEvent.setHttpBO(this);
        hru.setEvent(httpEvent);
        HttpRequestManager.getCache(HttpRequestManager.EnumDomainType.EDT_Communication).pushHttpRequest(hru);

        log.info("正在发送Create_Shop请求到服务器....");

        return true;
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
        log.info("正在执行ShopHttpBO的deleteAsync，bm=" +(bm == null ? null : bm.toString()));
        
        Shop shop = (Shop) bm;

        RequestBody body = new FormBody.Builder()
                .add(shop.field.getFIELD_NAME_ID(), String.valueOf(shop.getID()))
                .build();

        Request req = new Request.Builder()
                .url(Configuration.HTTP_IP + shop.HTTP_Shop_Delete)
                .addHeader(BaseHttpBO.COOKIE, GlobalController.getInstance().getSessionID())
                .post(body)
                .build();
        HttpRequestUnit hru = new DeleteShop();
        hru.setRequest(req);
        hru.setTimeout(TIME_OUT);
        hru.setbPostEventToUI(true);
        httpEvent.setStatus(BaseEvent.EnumEventStatus.EES_Http_ToDo);
        httpEvent.setHttpBO(this);
        hru.setEvent(httpEvent);
        HttpRequestManager.getCache(HttpRequestManager.EnumDomainType.EDT_Communication).pushHttpRequest(hru);

        log.info("正在发送Delete_Shop请求到服务器....");

        return true;
    }

    @Override
    public boolean feedback(String feedbackIDs) {
        return false;
    }

    @Override
    protected boolean doRetrieveNAsyncC(int iUseCaseID, BaseModel bm) {
        return false;
    }

    @Override
    protected boolean doRetrieve1AsyncC(int iUseCaseID, BaseModel bm) {
        return false;
    }
}
