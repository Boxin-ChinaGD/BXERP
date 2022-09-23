package com.bx.erp.bo;

import android.content.Context;

import com.bx.erp.common.GlobalController;
import com.bx.erp.event.BaseEvent;
import com.bx.erp.event.BaseHttpEvent;
import com.bx.erp.helper.Configuration;
import com.bx.erp.helper.Constants;
import com.bx.erp.http.HttpRequestManager;
import com.bx.erp.http.HttpRequestUnit;
import com.bx.erp.http.request.NtpSync;
import com.bx.erp.model.BaseModel;
import com.bx.erp.model.Ntp;

import org.apache.log4j.Logger;

import java.util.List;

import okhttp3.Request;

public class NtpHttpBO extends BaseHttpBO {
    private Logger log = Logger.getLogger(this.getClass());

    /**
     * 运行ntp协议时，http的响应只需要为ntp服务，不需要做其它操作如写DB
     * TODO 可能需要重构
     */
    public static boolean bForNtpOnly = false;

    /**
     * 同步Ntp时，服务器与POS之间的时差
     */
    public static long TimeDifference;

    public NtpHttpBO(Context ctx, BaseHttpEvent hEvent) {
        context = ctx;
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
        return false;
    }

    @Override
    protected boolean doRetrieve1AsyncC(int iUseCaseID, BaseModel bm) {
        return false;
    }

    public boolean syncTime(long timeStamp) {
        log.info("正在执行NtpHttpBO的syncTime，timeStamp=" + timeStamp);

        Request req = new Request.Builder()
                .url(Configuration.HTTP_IP + Ntp.HTTP_Ntp_SyncTime + timeStamp)
                .addHeader(BaseHttpBO.COOKIE, GlobalController.getInstance().getSessionID())
                .build();
        HttpRequestUnit hru = new NtpSync();
        hru.setRequest(req);
        hru.setTimeout(TIME_OUT);
        hru.setbPostEventToUI(true);
        httpEvent.setEventProcessed(false);
        httpEvent.setStatus(BaseEvent.EnumEventStatus.EES_Http_ToDo);
        hru.setEvent(httpEvent);
        NtpHttpBO.bForNtpOnly = true;
        HttpRequestManager.getCache(HttpRequestManager.EnumDomainType.EDT_Communication).pushHttpRequest(hru);

        log.info("正在请求服务器同步Ntp...");

        return true;
    }
}
