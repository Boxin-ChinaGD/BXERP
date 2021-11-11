package wpos.bo;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import wpos.common.GlobalController;
import wpos.event.BaseEvent;
import wpos.event.BaseHttpEvent;
import wpos.event.UI.BaseSQLiteEvent;
import wpos.helper.Configuration;
import wpos.http.HttpRequestManager;
import wpos.http.HttpRequestUnit;
import wpos.http.request.CreateCategory;
import wpos.http.request.FeedBack;
import wpos.http.request.RetrieveNCategory;
import wpos.http.request.RetrieveNCategoryC;
import wpos.http.request.UpdateCategory;
import wpos.model.BaseModel;
import wpos.model.CommodityCategory;

import java.util.List;

import okhttp3.FormBody;
import okhttp3.Request;
import okhttp3.RequestBody;

@Component("commodityCategoryHttpBO")
@Scope("prototype")
public class CommodityCategoryHttpBO extends BaseHttpBO {
    private Log log = LogFactory.getLog(this.getClass());

    public CommodityCategoryHttpBO(){}

    public CommodityCategoryHttpBO(BaseSQLiteEvent sEvent, BaseHttpEvent hEvent) {
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
        log.info("正在执行CommodityCategoryHttpBO的createAsync，bm=" +(bm == null ? null : bm.toString()));

        CommodityCategory commodityCategory = (CommodityCategory) bm;

        RequestBody body = new FormBody.Builder()
                .add(commodityCategory.field.getFIELD_NAME_name(), commodityCategory.getName())
                .add(commodityCategory.field.getFIELD_NAME_parentID(), String.valueOf(commodityCategory.getParentID()))
                .add(commodityCategory.field.getFIELD_NAME_returnObject(), String.valueOf(commodityCategory.getReturnObject()))
                .build();
        Request req = new Request.Builder()
                .url(Configuration.HTTP_IP + CommodityCategory.HTTP_CATEGORY_Create)
                .addHeader(BaseHttpBO.COOKIE, GlobalController.getInstance().getSessionID())
                .post(body)
                .build();
        HttpRequestUnit hru = new CreateCategory();
        hru.setRequest(req);
        hru.setTimeout(TIME_OUT);
        hru.setbPostEventToUI(true);
        httpEvent.setEventProcessed(false);
        httpEvent.setStatus(BaseEvent.EnumEventStatus.EES_Http_ToDo);
        hru.setEvent(httpEvent);
        HttpRequestManager.getCache(HttpRequestManager.EnumDomainType.EDT_Communication).pushHttpRequest(hru);

        log.info("正在请求服务器创建Categoty...");

        return true;
    }

    @Override
    protected boolean doUpdateAsync(int iUseCaseID, BaseModel bm) {
        log.info("正在执行CommodityCategoryHttpBO的updateAsync，bm=" + (bm == null ? null : bm.toString()));

        CommodityCategory commodityCategory = (CommodityCategory) bm;

        RequestBody body = new FormBody.Builder()
                .add(commodityCategory.field.getFIELD_NAME_ID(), String.valueOf(commodityCategory.getID()))
                .add(commodityCategory.field.getFIELD_NAME_name(), commodityCategory.getName())
                .add(commodityCategory.field.getFIELD_NAME_parentID(), String.valueOf(commodityCategory.getParentID()))
                .add(commodityCategory.field.getFIELD_NAME_returnObject(), String.valueOf(commodityCategory.getReturnObject()))
                .build();
        Request req = new Request.Builder()
                .url(Configuration.HTTP_IP + CommodityCategory.HTTP_CATEGORY_Update)
                .addHeader(BaseHttpBO.COOKIE, GlobalController.getInstance().getSessionID())
                .post(body)
                .build();
        HttpRequestUnit hru = new UpdateCategory();
        hru.setRequest(req);
        hru.setTimeout(TIME_OUT);
        hru.setbPostEventToUI(true);
        httpEvent.setEventProcessed(false);
        httpEvent.setStatus(BaseEvent.EnumEventStatus.EES_Http_ToDo);
        hru.setEvent(httpEvent);
        HttpRequestManager.getCache(HttpRequestManager.EnumDomainType.EDT_Communication).pushHttpRequest(hru);

        log.info("正在请求服务器修改Category...");

        return true;
    }

    @Override
    protected boolean doRetrieveNAsync(int iUseCaseID, BaseModel bm) {
        log.info("正在执行CommodityCategoryHttpBO的retrieveNAsync，bm=" + (bm == null ? null : bm.toString()));

        Request req = new Request.Builder()
                .url(Configuration.HTTP_IP + CommodityCategory.HTTP_CATEGORY_RetrieveN)
                .addHeader(BaseHttpBO.COOKIE, GlobalController.getInstance().getSessionID())
                .build();
        HttpRequestUnit hru = new RetrieveNCategory();
        hru.setRequest(req);
        hru.setTimeout(TIME_OUT);
        hru.setbPostEventToUI(true);
        httpEvent.setEventProcessed(false);
        httpEvent.setStatus(BaseEvent.EnumEventStatus.EES_Http_ToDo);
        hru.setEvent(httpEvent);
        HttpRequestManager.getCache(HttpRequestManager.EnumDomainType.EDT_Communication).pushHttpRequest(hru);

        log.info("正在发送CommodityCategory RN请求到服务器...");

        return true;
    }

    @Override
    protected boolean doDeleteAsync(int iUseCaseID, BaseModel bm) {
        return false;
    }

    @Override
    public boolean feedback(String feedbackIDs) {
        log.info("正在执行CommodityCategoryHttpBO的feedback，feedbackIDs=" + feedbackIDs);

        Request req = new Request.Builder()
                .url(Configuration.HTTP_IP + CommodityCategory.feedbackURL_FRONT + feedbackIDs + CommodityCategory.feedbackURL_BEHINE)
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

        log.info("通知服务器该POS机已经同步了商品类别。ID:" + feedbackIDs);

        return true;
    }

    @Override
    protected boolean doRetrieveNAsyncC(int iUseCaseID, BaseModel bm) {
        log.info("正在执行CommodityCategoryHttpBO的retrieveNAsyncC，bm=" + (bm == null ? null : bm.toString()));

        RequestBody body = new FormBody.Builder()
                .build();
        Request req = new Request.Builder()
                .url(Configuration.HTTP_IP + CommodityCategory.HTTP_Categoty_RETRIEVENC_PageIndex + bm.getPageIndex() + CommodityCategory.HTTP_Categoty_RETRIEVENC_PageSize + bm.getPageSize() + CommodityCategory.HTTP_Categoty_RETRIEVENC_TypeStatus)
                .addHeader(BaseHttpBO.COOKIE, GlobalController.getInstance().getSessionID())
                .post(body)
                .build();
        HttpRequestUnit hru = new RetrieveNCategoryC();
        hru.setRequest(req);
        hru.setTimeout(TIME_OUT);
        hru.setbPostEventToUI(true);
        httpEvent.setEventProcessed(false);
        httpEvent.setStatus(BaseEvent.EnumEventStatus.EES_Http_ToDo);
        hru.setEvent(httpEvent);
        HttpRequestManager.getCache(HttpRequestManager.EnumDomainType.EDT_Communication).pushHttpRequest(hru);

        log.info("正在发送RN Category请求到服务器....");

        return true;
    }

    @Override
    protected boolean doRetrieve1AsyncC(int iUseCaseID, BaseModel bm) {
        return false;
    }
}
