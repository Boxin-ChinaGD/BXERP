package wpos.bo;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import wpos.common.GlobalController;
import wpos.event.BaseEvent;
import wpos.event.BaseHttpEvent;
import wpos.event.UI.BaseSQLiteEvent;
import wpos.helper.Configuration;
import wpos.helper.Constants;
import wpos.http.HttpRequestManager;
import wpos.http.HttpRequestUnit;
import wpos.http.request.CreateNRetailTradePromoting;
import wpos.http.request.CreateRetailTradePromoting;
import wpos.model.BaseModel;
import wpos.model.RetailTradePromoting;

import java.util.List;

import okhttp3.FormBody;
import okhttp3.Request;
import okhttp3.RequestBody;

@Component("retailTradePromotingHttpBO")
@Scope("prototype")
public class RetailTradePromotingHttpBO extends BaseHttpBO {
    private Log log = LogFactory.getLog(this.getClass());
    
    public RetailTradePromotingHttpBO(BaseSQLiteEvent sEvent, BaseHttpEvent hEvent) {
        sqLiteEvent = sEvent;
        httpEvent = hEvent;
    }

    public RetailTradePromotingHttpBO(){}

    @Override
    protected boolean doCreateNSync(int iUseCaseID, List<BaseModel> list) {
        return false;
    }

    @Override
    protected boolean doCreateNAsync(int iUseCaseID, List<BaseModel> list) {
        log.info("正在执行RetailTradePromotingHttpBO的createNAsync，list=" + list.toString());

        Gson gson = new GsonBuilder()
                .setDateFormat(Constants.DATE_FORMAT_Default)
                .create();

        for (int i = 0; i < list.size(); i++) {
            log.info("上传的RetailTradePromoting的TradeID为：" + ((RetailTradePromoting) list.get(i)).getTradeID());
        }

        RetailTradePromoting retailTradePromoting = new RetailTradePromoting();
        String str = retailTradePromoting.removeJSONArrayofAttribute(gson.toJson(list));

        RequestBody body = new FormBody.Builder()
                .add(RetailTradePromoting.JSON_OBJECTLIST_KEY, str)
                .add(retailTradePromoting.field.getFIELD_NAME_returnObject(), "1") //...
                .build();

        Request req = new Request.Builder()
                .url(Configuration.HTTP_IP + RetailTradePromoting.HTTP_RetailTradePromoting_CreateN)
//                .addHeader(BaseHttpBO.COOKIE, GlobalController.getInstance().getSessionID())
                .post(body)
                .build();

        HttpRequestUnit hru = new CreateNRetailTradePromoting();
        hru.setRequest(req);
        hru.setTimeout(TIME_OUT);
        hru.setbPostEventToUI(true);
        httpEvent.setEventProcessed(false);
        httpEvent.setStatus(BaseEvent.EnumEventStatus.EES_Http_ToDo);
        hru.setEvent(httpEvent);
        HttpRequestManager.getCache(HttpRequestManager.EnumDomainType.EDT_Communication).pushHttpRequest(hru);

        log.info("正在向服务器请求批量创建RetailTradePromoting...");

        return true;
    }

    @Override
    protected boolean doCreateAsyncC(int iUseCaseID, BaseModel bm) {
        log.info("正在执行RetailTradePromotingHttpBO的createAsyncC，bm=" +(bm == null ? null : bm.toString()));

        RetailTradePromoting rtp = (RetailTradePromoting) bm;
        Gson gson = new GsonBuilder().create();
        String json = gson.toJson(rtp);

        RequestBody body = new FormBody.Builder()
                .add(rtp.HTTP_REQ_PARAMETER_JSON, json)
                .build();
        Request req = new Request.Builder()
                .url(Configuration.HTTP_IP + rtp.HTTP_RetailTradePromoting_Create)
//                .addHeader(BaseHttpBO.COOKIE, GlobalController.getInstance().getSessionID())
                .post(body)
                .build();
        HttpRequestUnit hru = new CreateRetailTradePromoting();
        hru.setRequest(req);
        hru.setTimeout(TIME_OUT);
        hru.setbPostEventToUI(true);
        httpEvent.setEventProcessed(false);
        httpEvent.setStatus(BaseEvent.EnumEventStatus.EES_Http_ToDo);
        hru.setEvent(httpEvent);
        HttpRequestManager.getCache(HttpRequestManager.EnumDomainType.EDT_Communication).pushHttpRequest(hru);

        log.info("正在请求服务器创建RetailTradePromoting...");

        return true;
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
}
