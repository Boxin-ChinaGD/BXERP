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
import com.bx.erp.http.request.CreatePos;
import com.bx.erp.http.request.DeletePos;
import com.bx.erp.http.request.FeedBack;
import com.bx.erp.http.request.Retrieve1BySNPos;
import com.bx.erp.http.request.RetrieveNPos;
import com.bx.erp.http.request.RetrieveNPosC;
import com.bx.erp.model.BaseModel;
import com.bx.erp.model.Pos;

import org.apache.log4j.Logger;

import java.util.List;

import okhttp3.FormBody;
import okhttp3.Request;
import okhttp3.RequestBody;

public class PosHttpBO extends BaseHttpBO {
    private Logger log = Logger.getLogger(this.getClass());

    public PosHttpBO(Context ctx, BaseSQLiteEvent sEvent, BaseHttpEvent hEvent) {
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
        log.info("正在执行PosHttpBO的createAsync，bm=" + (bm == null ? null : bm.toString()));

        Pos pos = (Pos) bm;

        RequestBody body = new FormBody.Builder()
                .add(pos.field.getFIELD_NAME_pwdEncrypted(), pos.getPwdEncrypted())
                .add(pos.field.getFIELD_NAME_shopID(), String.valueOf(pos.getShopID()))
                .add(pos.field.getFIELD_NAME_pos_SN(), pos.getPos_SN())
                .add(pos.field.getFIELD_NAME_returnObject(), String.valueOf(pos.getReturnObject()))
                .add(pos.field.getFIELD_NAME_companySN(), pos.getCompanySN())
                .build();
        Request req = new Request.Builder()
                .url(Configuration.HTTP_IP + Pos.HTTP_Pos_Create)
                .addHeader(BaseHttpBO.COOKIE, GlobalController.getInstance().getSessionID())
                .post(body)
                .build();
        HttpRequestUnit hru = new CreatePos();
        hru.setRequest(req);
        hru.setTimeout(TIME_OUT);
        hru.setbPostEventToUI(true);
        httpEvent.setEventProcessed(false);
        httpEvent.setStatus(BaseEvent.EnumEventStatus.EES_Http_ToDo);
        hru.setEvent(httpEvent);
        HttpRequestManager.getCache(HttpRequestManager.EnumDomainType.EDT_Communication).pushHttpRequest(hru);

        log.info("正在请求服务器创建Pos...");

        return true;
    }

    @Override
    protected boolean doUpdateAsync(int iUseCaseID, BaseModel bm) {
        return false;
    }

    @Override
    protected boolean doRetrieveNAsync(int iUseCaseID, BaseModel bm) {
        log.info("正在执行PosHttpBO的retrieveNAsync，bm=" + (bm == null ? null : bm.toString()));

        Request req = new Request.Builder()
                .url(Configuration.HTTP_IP + Pos.HTTP_Pos_RetrieveN)
                .addHeader(BaseHttpBO.COOKIE, GlobalController.getInstance().getSessionID())
                .build();
        HttpRequestUnit hru = new RetrieveNPos();
        hru.setRequest(req);
        hru.setTimeout(TIME_OUT);
        hru.setbPostEventToUI(true);
        httpEvent.setEventProcessed(false);
        httpEvent.setStatus(BaseEvent.EnumEventStatus.EES_Http_ToDo);
        hru.setEvent(httpEvent);
        HttpRequestManager.getCache(HttpRequestManager.EnumDomainType.EDT_Communication).pushHttpRequest(hru);

        log.info("正在发送POS RN请求到服务器...");

        return true;
    }

    @Override
    protected boolean doDeleteAsync(int iUseCaseID, BaseModel bm) {
        log.info("正在执行PosHttpBO的deleteAsync，bm=" + (bm == null ? null : bm.toString()));

        Request req = new Request.Builder()
                .url(Configuration.HTTP_IP + Pos.HTTP_Pos_Delete + bm.getID())
                .addHeader(BaseHttpBO.COOKIE, GlobalController.getInstance().getSessionID())
                .build();
        HttpRequestUnit hru = new DeletePos();
        hru.setRequest(req);
        hru.setTimeout(TIME_OUT);
        hru.setbPostEventToUI(true);
        httpEvent.setEventProcessed(false);
        httpEvent.setStatus(BaseEvent.EnumEventStatus.EES_Http_ToDo);
        hru.setEvent(httpEvent);
        HttpRequestManager.getCache(HttpRequestManager.EnumDomainType.EDT_Communication).pushHttpRequest(hru);

        log.info("正在发送deleteEx给服务器...");

        return true;
    }

    @Override
    public boolean feedback(String feedbackIDs) {
        log.info("正在执行PosHttpBO的feedback，feedbackIDs=" + feedbackIDs);

        Request req = new Request.Builder()
                .url(Configuration.HTTP_IP + Pos.feedbackURL_FRONT + feedbackIDs + Pos.feedbackURL_BEHINE)
                .addHeader(BaseHttpBO.COOKIE, GlobalController.getInstance().getSessionID())
                .build();
        HttpRequestUnit hru = new FeedBack();
        hru.setRequest(req);
        hru.setTimeout(TIME_OUT);
        hru.setbPostEventToUI(true);
        httpEvent.setEventProcessed(false);
        httpEvent.setStatus(BaseEvent.EnumEventStatus.EES_Http_ToDo);
        hru.setEvent(httpEvent);
        HttpRequestManager.getCache(HttpRequestManager.EnumDomainType.EDT_Communication).pushHttpRequest(hru);

        log.info("通知服务器该POS机已经同步了Pos。ID:" + feedbackIDs);

        return true;
    }

    @Override
    protected boolean doRetrieveNAsyncC(int iUseCaseID, BaseModel bm) {
        log.info("正在执行PosHttpBO的retrieveNAsyncC，bm=" + (bm == null ? null : bm.toString()));
        Pos pos = (Pos) bm;

        Request req = new Request.Builder()
                .url(Configuration.HTTP_IP + Pos.HTTP_Pos_RetrieveNC + pos.getPageIndex() + Pos.HTTP_Pos_PageSize + pos.getPageSize() + "&shopID=" + pos.getShopID())
                .addHeader(BaseHttpBO.COOKIE, GlobalController.getInstance().getSessionID())
                .build();
        HttpRequestUnit hru = new RetrieveNPosC();
        hru.setRequest(req);
        hru.setTimeout(TIME_OUT);
        hru.setbPostEventToUI(true);
        httpEvent.setEventProcessed(false);
        httpEvent.setStatus(BaseEvent.EnumEventStatus.EES_Http_ToDo);
        hru.setEvent(httpEvent);
        HttpRequestManager.getCache(HttpRequestManager.EnumDomainType.EDT_Communication).pushHttpRequest(hru);

        log.info("正在发送RN到服务器....");

        return true;
    }

    /**
     * 由于查询POS信息和公司信息时，还未进行登录操作，所以不需要检查SessionID
     */
    @Override
    public boolean retrieve1AsyncC(int iUseCaseID, BaseModel bm) {
        if (!checkRetrieve1(iUseCaseID, bm)) {
            return false;
        }
        return doRetrieve1AsyncC(iUseCaseID, bm);
    }

    @Override
    protected boolean doRetrieve1AsyncC(int iUseCaseID, BaseModel bm) {
        log.info("正在执行PosHttpBO的retrieve1AsyncC，bm=" + (bm == null ? null : bm.toString()));

        Pos pos = (Pos) bm;

        Request req = new Request.Builder()
                .url(Configuration.HTTP_IP + Pos.HTTP_Pos_Retrieve1BySN + Constants.MyPosSN + "&" + pos.field.getFIELD_NAME_companySN() + "=" + pos.getCompanySN())
                .build();
        log.info("POS的SN ：" + Constants.MyPosSN);
        HttpRequestUnit hru = new Retrieve1BySNPos();
        hru.setRequest(req);
        hru.setTimeout(TIME_OUT);
        hru.setbPostEventToUI(true);
        httpEvent.setEventProcessed(false);
        httpEvent.setStatus(BaseEvent.EnumEventStatus.EES_Http_ToDo);
        hru.setEvent(httpEvent);
        HttpRequestManager.getCache(HttpRequestManager.EnumDomainType.EDT_Communication).pushHttpRequest(hru);

        log.info("正在发送R1BySN到服务器....");

        return true;
    }
}
