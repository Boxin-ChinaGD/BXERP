package wpos.bo;


import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import wpos.common.GlobalController;
import wpos.event.BaseEvent;
import wpos.event.BaseHttpEvent;
import wpos.event.UI.BaseSQLiteEvent;
import wpos.helper.Configuration;
import wpos.http.HttpRequestManager;
import wpos.http.HttpRequestUnit;
import wpos.http.request.RetrieveNCConfigCacheSize;
import wpos.http.request.RetrieveNConfigCacheSize;
import wpos.model.BaseModel;
import wpos.model.ConfigCacheSize;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.util.List;

import okhttp3.Request;

@Component("configCacheSizeHttpBO")
@Scope("prototype")
public class ConfigCacheSizeHttpBO extends BaseHttpBO {
    private Log log = LogFactory.getLog(this.getClass());

    public ConfigCacheSizeHttpBO(BaseSQLiteEvent sEvent, BaseHttpEvent hEvent) {
        sqLiteEvent = sEvent;
        httpEvent = hEvent;
    }

    public ConfigCacheSizeHttpBO(){}

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
        log.info("正在执行ConfigCacheSizeHttpBO的retrieveNAsync，bm=" +(bm == null ? null : bm.toString()));

        Request req = new Request.Builder()
                .url(Configuration.HTTP_IP + ConfigCacheSize.HTTP_ConfigCacheSize_RetrieveN)
                .addHeader(BaseHttpBO.COOKIE, GlobalController.getInstance().getSessionID())
                .build();
        HttpRequestUnit hru = new RetrieveNConfigCacheSize();
        hru.setRequest(req);
        hru.setTimeout(TIME_OUT);
        hru.setbPostEventToUI(true);
        httpEvent.setEventProcessed(false);
        httpEvent.setStatus(BaseEvent.EnumEventStatus.EES_Http_ToDo);
        hru.setEvent(httpEvent);
        HttpRequestManager.getCache(HttpRequestManager.EnumDomainType.EDT_Communication).pushHttpRequest(hru);

        log.info("正在请求所有的配置文件");

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
        log.info("正在执行ConfigCacheSizeHttpBO的retrieveNAsyncC，bm=" +(bm == null ? null : bm.toString()));

        Request req = new Request.Builder()
                .url(Configuration.HTTP_IP + ConfigCacheSize.HTTP_ConfigCacheSize_RetrieveNC)
                .addHeader(BaseHttpBO.COOKIE, GlobalController.getInstance().getSessionID())
                .build();
        HttpRequestUnit hru = new RetrieveNCConfigCacheSize();
        hru.setRequest(req);
        hru.setTimeout(TIME_OUT);
        hru.setbPostEventToUI(true);
        httpEvent.setEventProcessed(false);
        httpEvent.setStatus(BaseEvent.EnumEventStatus.EES_Http_ToDo);
        hru.setEvent(httpEvent);
        HttpRequestManager.getCache(HttpRequestManager.EnumDomainType.EDT_Communication).pushHttpRequest(hru);

        log.info("正在请求所有的ConfigCacheSize...");

        return true;
    }

    @Override
    protected boolean doRetrieve1AsyncC(int iUseCaseID, BaseModel bm) {
        return false;
    }
}
