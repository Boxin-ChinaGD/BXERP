package wpos.bo;

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
import wpos.http.request.CreatePromotion;
import wpos.http.request.FeedBack;
import wpos.http.request.RetrieveNPromotion;
import wpos.http.request.RetrieveNPromotionC;
import wpos.http.request.UpdatePromotion;
import wpos.model.BaseModel;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import java.util.List;

import okhttp3.FormBody;
import okhttp3.Request;
import okhttp3.RequestBody;
import wpos.model.promotion.Promotion;

@Component("promotionHttpBO")
@Scope("prototype")
public class PromotionHttpBO extends BaseHttpBO {
    private Log log = LogFactory.getLog(this.getClass());
    
    public PromotionHttpBO(BaseSQLiteEvent sEvent, BaseHttpEvent hEvent) {
        sqLiteEvent = sEvent;
        httpEvent = hEvent;
    }

    public PromotionHttpBO(){}

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
        log.info("正在执行PromotionHttpBO的createAsync，bm=" +(bm == null ? null : bm.toString()));

        Promotion promotion = (Promotion) bm;

        RequestBody body = new FormBody.Builder()
                .add(promotion.field.getFIELD_NAME_name(), promotion.getName())
                .add(promotion.field.getFIELD_NAME_status(), String.valueOf(promotion.getStatus()))
                .add(promotion.field.getFIELD_NAME_type(), String.valueOf(promotion.getType()))
                .add(promotion.field.getFIELD_NAME_datetimeStart(),  Constants.getSimpleDateFormat().format(promotion.getDatetimeStart()))
                .add(promotion.field.getFIELD_NAME_datetimeEnd(),  Constants.getSimpleDateFormat().format(promotion.getDatetimeEnd()))
                .add(promotion.field.getFIELD_NAME_excecutionThreshold(), String.valueOf(promotion.getExcecutionThreshold()))
                .add(promotion.field.getFIELD_NAME_excecutionAmount(), String.valueOf(promotion.getExcecutionAmount()))
                .add(promotion.field.getFIELD_NAME_excecutionDiscount(), String.valueOf(promotion.getExcecutionDiscount()))
                .add(promotion.field.getFIELD_NAME_scope(), String.valueOf(promotion.getScope()))//参与商品的范围：0代表全部商品.1代表部分商品.
                .add(promotion.field.getFIELD_NAME_staff(),String.valueOf(promotion.getStaff()))
                .add(promotion.field.getFIELD_NAME_returnObject(), String.valueOf(promotion.getReturnObject()))
                .add(promotion.field.getFIELD_NAME_commodityIDs(), promotion.getCommodityIDs() == null ? "" : promotion.getCommodityIDs()) //代表参与该促销的指定商品
                .build();
        Request req = new Request.Builder()
                .url(Configuration.HTTP_IP + Promotion.HTTP_Promotion_Create)
                .addHeader(BaseHttpBO.COOKIE, GlobalController.getInstance().getSessionID())
                .post(body)
                .build();
        HttpRequestUnit hru = new CreatePromotion();
        hru.setRequest(req);
        hru.setTimeout(TIME_OUT);
        hru.setbPostEventToUI(true);
        httpEvent.setEventProcessed(false);
        httpEvent.setStatus(BaseEvent.EnumEventStatus.EES_Http_ToDo);
        hru.setEvent(httpEvent);
        HttpRequestManager.getCache(HttpRequestManager.EnumDomainType.EDT_Communication).pushHttpRequest(hru);

        log.info("正在请求服务器创建promotion...");

        return true;
    }

    @Override
    protected boolean doUpdateAsync(int iUseCaseID, BaseModel bm) { //... nbr已无对应的接口暂无需修改
        log.info("正在执行PromotionHttpBO的updateAsync，bm=" +(bm == null ? null : bm.toString()));

        Promotion promotion = (Promotion) bm;

        RequestBody body = new FormBody.Builder()
                .add(promotion.field.getFIELD_NAME_ID(), promotion.getID() + "")
                .add(promotion.field.getFIELD_NAME_name(), promotion.getName())
                .add(promotion.field.getFIELD_NAME_status(), String.valueOf(promotion.getStatus()))
                .add(promotion.field.getFIELD_NAME_type(), String.valueOf(promotion.getType()))
                .add(promotion.field.getFIELD_NAME_datetimeStart(), Constants.getSimpleDateFormat().format(promotion.getDatetimeStart()))
                .add(promotion.field.getFIELD_NAME_datetimeEnd(), Constants.getSimpleDateFormat().format(promotion.getDatetimeEnd()))
                .add(promotion.field.getFIELD_NAME_excecutionThreshold(),String.valueOf(promotion.getExcecutionThreshold()))
                .add(promotion.field.getFIELD_NAME_excecutionAmount(), String.valueOf(promotion.getExcecutionAmount()))
                .add(promotion.field.getFIELD_NAME_scope(), String.valueOf(promotion.getScope()))
                .add(promotion.field.getFIELD_NAME_staff(),String.valueOf(promotion.getStaff()))
                .add(promotion.field.getFIELD_NAME_returnObject(), String.valueOf(promotion.getReturnObject()))
                .build();
        Request req = new Request.Builder()
                .url(Configuration.HTTP_IP + promotion.HTTP_Promotion_Update)
                .addHeader(BaseHttpBO.COOKIE, GlobalController.getInstance().getSessionID())
                .post(body)
                .build();
        HttpRequestUnit hru = new UpdatePromotion();
        hru.setRequest(req);
        hru.setTimeout(TIME_OUT);
        hru.setbPostEventToUI(true);
        httpEvent.setEventProcessed(false);
        httpEvent.setStatus(BaseEvent.EnumEventStatus.EES_Http_ToDo);
        hru.setEvent(httpEvent);
        HttpRequestManager.getCache(HttpRequestManager.EnumDomainType.EDT_Communication).pushHttpRequest(hru);

        log.info("正在请求服务器修改promotion...");

        return true;
    }

    @Override
    protected boolean doRetrieveNAsync(int iUseCaseID, BaseModel bm) {
        log.info("正在执行PromotionHttpBO的retrieveNAsync，bm=" +(bm == null ? null : bm.toString()));

        Request req = new Request.Builder()
                .url(Configuration.HTTP_IP + Promotion.HTTP_Promotion_RetrieveN)
                .addHeader(BaseHttpBO.COOKIE, GlobalController.getInstance().getSessionID())
                .build();
        HttpRequestUnit hru = new RetrieveNPromotion();
        hru.setRequest(req);
        hru.setTimeout(TIME_OUT);
        hru.setbPostEventToUI(true);
        httpEvent.setEventProcessed(false);
        httpEvent.setStatus(BaseEvent.EnumEventStatus.EES_Http_ToDo);
        hru.setEvent(httpEvent);
        HttpRequestManager.getCache(HttpRequestManager.EnumDomainType.EDT_Communication).pushHttpRequest(hru);

        log.info("正在发送Promotion RN请求到服务器...");

        return true;
    }

    @Override
    protected boolean doDeleteAsync(int iUseCaseID, BaseModel bm) {
        return false;
    }

    @Override
    public boolean feedback(String feedbackIDs) {
        log.info("正在执行PromotionHttpBO的feedback，feedbackIDs=" + feedbackIDs);

        Request req = new Request.Builder()
                .url(Configuration.HTTP_IP + Promotion.feedbackURL_FRONT + feedbackIDs + Promotion.feedbackURL_BEHINE)
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

        log.info("通知服务器该POS机已经同步了品牌。ID:" + feedbackIDs);

        return true;
    }

    @Override
    protected boolean doRetrieveNAsyncC(int iUseCaseID, BaseModel bm) {
        log.info("正在执行PromotionHttpBO的retrieveNAsyncC，bm=" +(bm == null ? null : bm.toString()));

        RequestBody body = new FormBody.Builder()
                .build();
        Request req = new Request.Builder()
                .url(Configuration.HTTP_IP + Promotion.HTTP_Promotion_RetrieveNC + ((Promotion)bm).getSubStatusOfStatus() + Promotion.HTTP_Promotion_RETRIEVENC_PageIndex + bm.getPageIndex() + Promotion.HTTP_Promotion_RETRIEVENC_PageSize + bm.getPageSize())
                .addHeader(BaseHttpBO.COOKIE, GlobalController.getInstance().getSessionID())
                .post(body)
                .build();
        HttpRequestUnit hru = new RetrieveNPromotionC();
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

    @Override
    protected boolean doRetrieve1AsyncC(int iUseCaseID, BaseModel bm) {
        return false;
    }
}
