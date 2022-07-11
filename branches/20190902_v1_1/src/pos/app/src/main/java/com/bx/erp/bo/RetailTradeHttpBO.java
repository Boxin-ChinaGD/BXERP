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
import com.bx.erp.http.request.CreateNRetailTrade;
import com.bx.erp.http.request.CreateRetailTrade;
import com.bx.erp.http.request.RetrieveNOldRetailTrade;
import com.bx.erp.http.request.RetrieveNRetailTradeC;
import com.bx.erp.model.BaseModel;
import com.bx.erp.model.RetailTrade;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.apache.log4j.Logger;

import java.util.Date;
import java.util.List;

import okhttp3.FormBody;
import okhttp3.Request;
import okhttp3.RequestBody;

public class RetailTradeHttpBO extends BaseHttpBO {
    private Logger log = Logger.getLogger(this.getClass());

    public RetailTradeHttpBO(Context ctx, BaseSQLiteEvent sEvent, BaseHttpEvent hEvent) {
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
        log.info("正在执行RetailTradeHttpBO的createNAsync，list=" + (list == null ? null : list.toString()));

        for (BaseModel bm : list) {
            RetailTrade rt = (RetailTrade) bm;
            if (rt.getSourceID() > 0) {
                rt.setListSlave2(null);
                rt.setListSlave3(null);
            }
            // gson不能转换日期为null的对象
            if(rt.getHoldBillTime() == null) {
                rt.setHoldBillTime(new Date());
            }
        }

        Gson gson = new GsonBuilder()
                .setDateFormat(Constants.DATE_FORMAT_Default2)
                .create();

        RetailTrade retailTrade = new RetailTrade();
        String str = retailTrade.removeJSONArrayofAttribute(gson.toJson(list));

        RequestBody body = new FormBody.Builder()
                .add(RetailTrade.JSON_OBJECTLIST_KEY, str)
                .build();

        Request req = new Request.Builder()
                .url(Configuration.HTTP_IP + RetailTrade.HTTP_REQ_URL_CreateN)
                .addHeader(BaseHttpBO.COOKIE, GlobalController.getInstance().getSessionID())
                .post(body)
                .build();

        HttpRequestUnit hru = new CreateNRetailTrade();
        hru.setRequest(req);
        hru.setTimeout(TIME_OUT);
        hru.setbPostEventToUI(true);
        httpEvent.setEventProcessed(false);
        httpEvent.setStatus(BaseEvent.EnumEventStatus.EES_Http_ToDo);
        hru.setEvent(httpEvent);
        HttpRequestManager.getCache(HttpRequestManager.EnumDomainType.EDT_Communication).pushHttpRequest(hru);

        log.info("正在向服务器请求创建零售单...");

        return true;
    }

    @Override
    protected boolean doCreateAsyncC(int iUseCaseID, BaseModel bm) {
        return false;
    }

    @Override
    protected boolean doCreateAsync(int iUseCaseID, BaseModel bm) {
        log.info("正在执行RetailTradeHttpBO的createAsync，bm=" + (bm == null ? null : bm.toString()));

        RetailTrade rt = (RetailTrade) bm;
        rt.setReturnObject(1);
        if (rt.getSourceID() > 0) {
            rt.setListSlave2(null);
            rt.setListSlave3(null);
        }
        // gson不能转换日期为null的对象
        if(rt.getHoldBillTime() == null) {
            rt.setHoldBillTime(new Date());
        }
        //
        Gson gson = new GsonBuilder()
                .setDateFormat(Constants.DATE_FORMAT_Default2)
                .create();
        String json = gson.toJson(rt);
        //
        RequestBody body = new FormBody.Builder()
                .add(RetailTrade.HTTP_REQ_PARAMETER_Create, rt.removeJSONofAttribute(json))
                .build();
        Request req = new Request.Builder()
                .url(Configuration.HTTP_IP + RetailTrade.HTTP_REQ_URL_Create)
                .addHeader(BaseHttpBO.COOKIE, GlobalController.getInstance().getSessionID())
                .post(body)
                .build();

        HttpRequestUnit hru = new CreateRetailTrade();
        hru.setRequest(req);
        hru.setTimeout(TIME_OUT);
        hru.setbPostEventToUI(true);
        httpEvent.setEventProcessed(false);
        httpEvent.setStatus(BaseEvent.EnumEventStatus.EES_Http_ToDo);
        hru.setEvent(httpEvent);
        HttpRequestManager.getCache(HttpRequestManager.EnumDomainType.EDT_Communication).pushHttpRequest(hru);

        log.info("正在向服务器请求创建零售单...");

        return true;
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
        log.info("正在执行RetailTradeHttpBO的retrieveNAsyncC，bm=" + (bm == null ? null : bm.toString()));

        Request req = null;
        HttpRequestUnit hru = null;
        httpEvent.setEventProcessed(false);

        RetailTrade rt = (RetailTrade) bm;

        switch (iUseCaseID) {
            case CASE_RetailTrade_RetrieveNOldRetailTradeAsyncC:
                try {
                    RequestBody body = new FormBody.Builder()
                            .add(rt.field.getFIELD_NAME_queryKeyword(), rt.getQueryKeyword() == null ? "" : rt.getQueryKeyword())
                            .add(rt.field.getFIELD_NAME_datetimeStart(), rt.getDatetimeStart() == null ? Constants.getSimpleDateFormat().format(Constants.getDefaultSyncDatetime()) : Constants.getSimpleDateFormat().format(rt.getDatetimeStart()))
                            .add(rt.field.getFIELD_NAME_datetimeEnd(), rt.getDatetimeEnd() == null ? Constants.getSimpleDateFormat().format(new Date(System.currentTimeMillis() + NtpHttpBO.TimeDifference)) : Constants.getSimpleDateFormat().format(rt.getDatetimeEnd()))
                            .build();

                    req = new Request.Builder()
                            .url(Configuration.HTTP_IP + RetailTrade.HTTP_REQ_URL_RetrieveNC + RetailTrade.HTTP_REQ_PageIndex + bm.getPageIndex() + RetailTrade.HTTP_REQ_PageSize + bm.getPageSize())
                            .addHeader(BaseHttpBO.COOKIE, GlobalController.getInstance().getSessionID())
                            .post(body)
                            .build();
                    hru = new RetrieveNOldRetailTrade();
                    hru.setRequest(req);
                    hru.setTimeout(TIME_OUT);
                    hru.setbPostEventToUI(true);
                    httpEvent.setStatus(BaseEvent.EnumEventStatus.EES_Http_ToDo);
                    hru.setEvent(httpEvent);
                    HttpRequestManager.getCache(HttpRequestManager.EnumDomainType.EDT_Communication).pushHttpRequest(hru);

                    log.info("正在请求查询旧单零售单...");

                } catch (Exception e) {
                    e.printStackTrace();
                    log.info("查询旧单零售单失败，错误信息" + e.getMessage());
                    return false;
                }
                break;
            default:
                try {
                    RequestBody body = new FormBody.Builder()
                            .add(rt.field.getFIELD_NAME_iPageIndex(), rt.getPageIndex())
                            .add(rt.field.getFIELD_NAME_iPageSize(), rt.getPageSize())
                            .add(rt.field.getFIELD_NAME_vipID(), String.valueOf(rt.getVipID()))
                            .add(rt.field.getFIELD_NAME_queryKeyword(), rt.getQueryKeyword() == null ? "" : rt.getQueryKeyword())
                            .build();


                    if (rt.getVipID() > 0) {
                        httpEvent.setBaseModel2(rt);
                    }

                    req = new Request.Builder()
                            .url(Configuration.HTTP_IP + RetailTrade.HTTP_REQ_URL_RetrieveNC)
                            .addHeader(BaseHttpBO.COOKIE, GlobalController.getInstance().getSessionID())
                            .post(body)
                            .build();
                    hru = new RetrieveNRetailTradeC();
                    hru.setRequest(req);
                    hru.setTimeout(TIME_OUT);
                    hru.setbPostEventToUI(true);
                    httpEvent.setStatus(BaseEvent.EnumEventStatus.EES_Http_ToDo);
                    hru.setEvent(httpEvent);
                    HttpRequestManager.getCache(HttpRequestManager.EnumDomainType.EDT_Communication).pushHttpRequest(hru);

                    log.info("正在请求所有零售单...");
                } catch (Exception e) {
                    e.printStackTrace();
                    log.info("查询所有零售单失败，错误信息" + e.getMessage());
                    return false;
                }

                break;
        }

        return true;
    }

    @Override
    protected boolean doRetrieve1AsyncC(int iUseCaseID, BaseModel bm) {
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

}
