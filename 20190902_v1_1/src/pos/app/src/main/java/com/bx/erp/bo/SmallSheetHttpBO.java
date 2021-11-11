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
import com.bx.erp.http.request.CreatingSmallSheet;
import com.bx.erp.http.request.DeleteSmallSheet;
import com.bx.erp.http.request.FeedBack;
import com.bx.erp.http.request.RetrieveNSmallSheet;
import com.bx.erp.http.request.RetrieveNSmallSheetC;
import com.bx.erp.http.request.UpdateSmallSheet;
import com.bx.erp.model.BaseModel;
import com.bx.erp.model.ErrorInfo;
import com.bx.erp.model.SmallSheetFrame;
import com.bx.erp.model.SmallSheetText;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.apache.log4j.Logger;

import java.util.Date;
import java.util.List;

import okhttp3.FormBody;
import okhttp3.Request;
import okhttp3.RequestBody;

public class SmallSheetHttpBO extends BaseHttpBO {
    private Logger log = Logger.getLogger(this.getClass());

    public SmallSheetHttpBO(Context ctx, BaseSQLiteEvent sEvent, BaseHttpEvent hEvent) {
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
        log.info("正在执行SmallSheetHttpBO的createAsync，bm=" + (bm == null ? null : bm.toString()));

        if (bm != null) {
            String checkMsg = bm.checkCreate(BaseSQLiteBO.INVALID_CASE_ID);
            if (!"".equals(checkMsg)) {
                log.info(checkMsg);
                httpEvent.setLastErrorCode(ErrorInfo.EnumErrorCode.EC_WrongFormatForInputField);
                httpEvent.setLastErrorMessage(checkMsg);
                httpEvent.setStatus(BaseEvent.EnumEventStatus.EES_SQLite_Done);
                return false;
            }
        }

        SmallSheetFrame ssf = (SmallSheetFrame) bm;

        Gson gson = new GsonBuilder()
                .setDateFormat(Constants.DATE_FORMAT_Default)
                .create();
        String json = gson.toJson(ssf.getListSlave1());

        RequestBody body = new FormBody.Builder()
                .add(SmallSheetText.HTTP_REQ_PARAMETER_Create, json)
                .add(ssf.field.getFIELD_NAME_logo(), ssf.getLogo() == null ? "" : ssf.getLogo())
                .add(ssf.field.getFIELD_NAME_countOfBlankLineAtBottom(), String.valueOf(ssf.getCountOfBlankLineAtBottom()))
                .add(ssf.field.getFIELD_NAME_delimiterToRepeat(), ssf.getDelimiterToRepeat())
                .add(ssf.field.getFIELD_NAME_createDatetime(), Constants.getSimpleDateFormat().format((ssf.getCreateDatetime() == null ? new Date(System.currentTimeMillis() + NtpHttpBO.TimeDifference) : ssf.getCreateDatetime())))
                .add(ssf.field.getFIELD_NAME_returnObject(), String.valueOf(ssf.getReturnObject()))
                .build();
        Request req = new Request.Builder()
                .url(Configuration.HTTP_IP + SmallSheetFrame.HTTP_REQ_URL_CreateC)
                .addHeader(BaseHttpBO.COOKIE, GlobalController.getInstance().getSessionID())
                .post(body)
                .build();
        HttpRequestUnit hru = new CreatingSmallSheet();
        hru.setRequest(req);
        hru.setTimeout(TIME_OUT);
        hru.setbPostEventToUI(true);
        httpEvent.setEventProcessed(false);
        httpEvent.setStatus(BaseEvent.EnumEventStatus.EES_Http_ToDo);
        hru.setEvent(httpEvent);
        HttpRequestManager.getCache(HttpRequestManager.EnumDomainType.EDT_Communication).pushHttpRequest(hru);

        log.info("正在上传小票格式(for 创建)...");

        return true;
    }

    @Override
    protected boolean doUpdateAsync(int iUseCaseID, BaseModel bm) {
        log.info("正在执行SmallSheetHttpBO的updateAsync，bm=" + (bm == null ? null : bm.toString()));

        if (bm != null) {
            String checkMsg = bm.checkUpdate(BaseSQLiteBO.INVALID_CASE_ID);
            if (!"".equals(checkMsg)) {
                log.info(checkMsg);
                httpEvent.setLastErrorCode(ErrorInfo.EnumErrorCode.EC_WrongFormatForInputField);
                httpEvent.setLastErrorMessage(checkMsg);
                httpEvent.setStatus(BaseEvent.EnumEventStatus.EES_SQLite_Done);
                return false;
            }
        }

        SmallSheetFrame ssf = (SmallSheetFrame) bm;
        //
        Gson gson = new GsonBuilder()
                .setDateFormat(Constants.DATE_FORMAT_Default)
                .create();

        String sTextListJson = gson.toJson(ssf.getListSlave1());
        RequestBody body = new FormBody.Builder()
                .add(SmallSheetText.HTTP_REQ_PARAMETER_Update, sTextListJson)
                .add(ssf.field.getFIELD_NAME_ID(), String.valueOf(ssf.getID()))
                .add(ssf.field.getFIELD_NAME_logo(), ssf.getLogo() == null ? "" : ssf.getLogo())
                .add(ssf.field.getFIELD_NAME_countOfBlankLineAtBottom(), String.valueOf(ssf.getCountOfBlankLineAtBottom()))
                .add(ssf.field.getFIELD_NAME_delimiterToRepeat(), ssf.getDelimiterToRepeat())
                .add(ssf.field.getFIELD_NAME_returnObject(), String.valueOf(ssf.getReturnObject()))
                .add(ssf.field.getFIELD_NAME_createDatetime(), Constants.getSimpleDateFormat().format((ssf.getCreateDatetime() == null ? new Date(System.currentTimeMillis() + NtpHttpBO.TimeDifference) : ssf.getCreateDatetime()))) //不需要传递此字段
                .build();
        Request req = new Request.Builder()
                .url(Configuration.HTTP_IP + SmallSheetFrame.HTTP_REQ_URL_UpdateC)
                .addHeader(BaseHttpBO.COOKIE, GlobalController.getInstance().getSessionID())
                .post(body)
                .build();
        HttpRequestUnit hru = new UpdateSmallSheet();
        hru.setRequest(req);
        hru.setTimeout(TIME_OUT);
        hru.setbPostEventToUI(true);
        httpEvent.setEventProcessed(false);
        httpEvent.setStatus(BaseEvent.EnumEventStatus.EES_Http_ToDo);
        hru.setEvent(httpEvent);
        HttpRequestManager.getCache(HttpRequestManager.EnumDomainType.EDT_Communication).pushHttpRequest(hru);

        log.info("正在上传小票格式(for 修改)...");

        return true;
    }

    @Override
    protected boolean doRetrieveNAsync(int iUseCaseID, BaseModel bm) {
        return false;
    }

    @Override
    protected boolean doDeleteAsync(int iUseCaseID, BaseModel bm) {
        try {
            log.info("正在执行SmallSheetHttpBO的deleteAsync，bm=" + (bm == null ? null : bm.toString()));

            if (bm != null) {
                String checkMsg = bm.checkDelete(BaseSQLiteBO.INVALID_CASE_ID);
                if (!"".equals(checkMsg)) {
                    log.info(checkMsg);
                    httpEvent.setLastErrorCode(ErrorInfo.EnumErrorCode.EC_WrongFormatForInputField);
                    httpEvent.setLastErrorMessage(checkMsg);
                    httpEvent.setStatus(BaseEvent.EnumEventStatus.EES_SQLite_Done);
                    return false;
                }
            }

            Request req = new Request.Builder()
                    .url(Configuration.HTTP_IP + SmallSheetFrame.HTTP_REQ_URL_DeleteC + bm.getID())
                    .addHeader(BaseHttpBO.COOKIE, GlobalController.getInstance().getSessionID())
                    .build();
            HttpRequestUnit hru = new DeleteSmallSheet();
            hru.setRequest(req);
            hru.setTimeout(TIME_OUT);
            hru.setbPostEventToUI(true);
            httpEvent.setEventProcessed(false);
            httpEvent.setStatus(BaseEvent.EnumEventStatus.EES_Http_ToDo);
            hru.setEvent(httpEvent);
            HttpRequestManager.getCache(HttpRequestManager.EnumDomainType.EDT_Communication).pushHttpRequest(hru);

            httpEvent.setBaseModel1(bm);

            log.info("正在请求删除对象...");
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean feedback(String feedbackIDs) {
        log.info("正在执行SmallSheetHttpBO的feedback，feedbackIDs=" + feedbackIDs);

        Request req = new Request.Builder()
                .url(Configuration.HTTP_IP + SmallSheetFrame.smallSheetfeedbackURL_FRONT + feedbackIDs + SmallSheetFrame.smallSheetfeedbackURL_BEHIND)
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

        log.info("通知服务器该POS机已经同步了小票格式。ID:" + feedbackIDs);

        return true;
    }

    @Override
    protected boolean doRetrieveNAsyncC(int iUseCaseID, BaseModel bm) {
        log.info("正在执行SmallSheetHttpBO的retrieveNAsyncC，bm=" + (bm == null ? null : bm.toString()));

        if (bm != null) {
            String checkMsg = bm.checkRetrieveN(BaseSQLiteBO.INVALID_CASE_ID);
            if (!"".equals(checkMsg)) {
                log.info(checkMsg);
                httpEvent.setLastErrorCode(ErrorInfo.EnumErrorCode.EC_WrongFormatForInputField);
                httpEvent.setLastErrorMessage(checkMsg);
                httpEvent.setStatus(BaseEvent.EnumEventStatus.EES_SQLite_Done);
                return false;
            }
        }

        Request req = new Request.Builder()
                .url(Configuration.HTTP_IP + SmallSheetFrame.HTTP_REQ_URL_RetrieveNC + bm.getPageIndex() + SmallSheetFrame.HTTP_REQ_PageSize + bm.getPageSize())
                .addHeader(BaseHttpBO.COOKIE, GlobalController.getInstance().getSessionID())
                .build();
        HttpRequestUnit hru = new RetrieveNSmallSheetC();
        hru.setRequest(req);
        hru.setTimeout(TIME_OUT);
        hru.setbPostEventToUI(true);
        httpEvent.setEventProcessed(false);
        httpEvent.setStatus(BaseEvent.EnumEventStatus.EES_Http_ToDo);
        hru.setEvent(httpEvent);
        HttpRequestManager.getCache(HttpRequestManager.EnumDomainType.EDT_Communication).pushHttpRequest(hru);

        log.info("正在请求所有的小票格式");

        return true;
    }

    @Override
    protected boolean doRetrieve1AsyncC(int iUseCaseID, BaseModel bm) {
        return false;
    }
}
