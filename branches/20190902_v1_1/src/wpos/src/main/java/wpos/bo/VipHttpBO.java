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
import wpos.http.request.*;
import wpos.model.BaseModel;
import wpos.model.ErrorInfo;
import wpos.model.Vip;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.util.List;

import okhttp3.FormBody;
import okhttp3.Request;
import okhttp3.RequestBody;

@Component("vipHttpBO")
@Scope("prototype")
public class VipHttpBO extends BaseHttpBO {
    private Log log = LogFactory.getLog(this.getClass());

    public VipHttpBO(BaseSQLiteEvent sEvent, BaseHttpEvent hEvent) {
        sqLiteEvent = sEvent;
        httpEvent = hEvent;
    }

    public VipHttpBO(){}

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
        throw new RuntimeException("不能在Pos机上创建Vip！");
    }

    @Override
    protected boolean doUpdateAsync(int iUseCaseID, BaseModel bm) {
        log.info("正在执行VipHttpBO的updateAsync，bm=" +(bm == null ? null : bm.toString()));

        Vip vip = (Vip) bm;

        RequestBody body = new FormBody.Builder()
                .add(vip.field.getFIELD_NAME_ID(), String.valueOf(vip.getID()))
                .add(vip.field.getFIELD_NAME_name(), vip.getName())
                .add(vip.field.getFIELD_NAME_email(), vip.getEmail())
                .add(vip.field.getFIELD_NAME_birthday(), Constants.getSimpleDateFormat().format(vip.getBirthday()))
                .add(vip.field.getFIELD_NAME_returnObject(), String.valueOf(vip.getReturnObject()))//...
                .build();
        Request req = new Request.Builder()
                .url(Configuration.HTTP_IP + Vip.HTTP_Vip_UPDATE)
                .addHeader(BaseHttpBO.COOKIE, GlobalController.getInstance().getSessionID())
                .post(body)
                .build();
        HttpRequestUnit hru = new UpdateVip();
        hru.setRequest(req);
        hru.setTimeout(TIME_OUT);
        hru.setbPostEventToUI(true);
        httpEvent.setEventProcessed(false);
        httpEvent.setStatus(BaseEvent.EnumEventStatus.EES_Http_ToDo);
        hru.setEvent(httpEvent);
        HttpRequestManager.getCache(HttpRequestManager.EnumDomainType.EDT_Communication).pushHttpRequest(hru);

        log.info("正在请求服务器修改Vip...");

        return true;
    }

    public boolean  retrieveNByMobileOrVipCardSNEx(BaseModel bm) {
        log.info("正在执行VipHttpBO的retrieveNByMobileOrVipCardSNEx，bm=" + (bm == null ? null : bm.toString()));
        //
        String err = bm.checkRetrieveN(BaseHttpBO.CASE_Vip_retrieveNByMobileOrVipCardSNEx);
        if (err.length() > 0) {
            Constants.checkModelLog(log, bm, err);
            httpEvent.setLastErrorCode(ErrorInfo.EnumErrorCode.EC_WrongFormatForInputField);
            httpEvent.setLastErrorMessage(err);
            System.out.println("错误信息："+err);
            return false;
        }

        Vip vip = (Vip) bm;
        RequestBody body = new FormBody.Builder()
                .add(vip.field.getFIELD_NAME_mobile(), vip.getMobile() == null ? "" : vip.getMobile())
                .add(vip.field.getFIELD_NAME_vipCardSN(), vip.getVipCardSN() == null ? "" : vip.getVipCardSN())
                .build();
        Request req = new Request.Builder()
                .url(Configuration.HTTP_IP + Vip.HTTP_Vip_RetrieveNByMobileOrVipCardSNEx)
                .addHeader(BaseHttpBO.COOKIE, GlobalController.getInstance().getSessionID())
                .post(body)
                .build();
        HttpRequestUnit hru = new RetrieveNByMobileOrVipCardSN();
        hru.setRequest(req);
        hru.setTimeout(TIME_OUT);
        hru.setbPostEventToUI(true);
        httpEvent.setEventProcessed(false);
        httpEvent.setStatus(BaseEvent.EnumEventStatus.EES_Http_ToDo);
        hru.setEvent(httpEvent);
        HttpRequestManager.getCache(HttpRequestManager.EnumDomainType.EDT_Communication).pushHttpRequest(hru);

        log.info("正在请求会员相关信息");

        return true;
    }

    @Override
    protected boolean doRetrieveNAsync(int iUseCaseID, BaseModel bm) {
        log.info("正在执行VipHttpBO的retrieveNAsync，bm=" +(bm == null ? null : bm.toString()));
        log.info("asdasdasdasdasdsadasdas");

        Request req = new Request.Builder()
                .url(Configuration.HTTP_IP + Vip.HTTP_Vip_RETRIEVEN)
                .addHeader(BaseHttpBO.COOKIE, GlobalController.getInstance().getSessionID())
                .build();
        HttpRequestUnit hru = new RetrieveNVip();
        hru.setRequest(req);
        hru.setTimeout(TIME_OUT);
        hru.setbPostEventToUI(true);
        httpEvent.setEventProcessed(false);
        httpEvent.setStatus(BaseEvent.EnumEventStatus.EES_Http_ToDo);
        hru.setEvent(httpEvent);
        HttpRequestManager.getCache(HttpRequestManager.EnumDomainType.EDT_Communication).pushHttpRequest(hru);

        log.info("正在请求需要同步的Vip");

        return true;
    }

    @Override
    protected boolean doRetrieveNAsyncC(int iUseCaseID, BaseModel bm) {
        log.info("正在执行VipHttpBO的retrieveNAsyncC，bm=" +(bm == null ? null : bm.toString()));

        Vip vip = (Vip) bm;

        RequestBody body = new FormBody.Builder()
                .add(vip.field.getFIELD_NAME_mobile(), vip.getMobile())
                .add(vip.field.getFIELD_NAME_category(), String.valueOf(BaseSQLiteBO.INVALID_ID))
                .build();

        Request req = new Request.Builder()
                .url(Configuration.HTTP_IP + Vip.HTTP_Vip_RETRIEVENC)
                .addHeader(BaseHttpBO.COOKIE, GlobalController.getInstance().getSessionID())
                .post(body)
                .build();
        HttpRequestUnit hru = new RetrieveNCVip();
        hru.setRequest(req);
        hru.setTimeout(TIME_OUT);
        hru.setbPostEventToUI(true);
        httpEvent.setEventProcessed(false);
        httpEvent.setStatus(BaseEvent.EnumEventStatus.EES_Http_ToDo);
        hru.setEvent(httpEvent);
        HttpRequestManager.getCache(HttpRequestManager.EnumDomainType.EDT_Communication).pushHttpRequest(hru);

        log.info("正在请求需要查询的Vip");

        return true;
    }

    @Override
    protected boolean doRetrieve1AsyncC(int iUseCaseID, BaseModel bm) {
        return false;
    }

    public boolean retrieveNVipConsumeHistoryEx(BaseModel bm) {
        log.info("正在执行VipHttpBO的retrieveNVipConsumeHistoryEx，bm=" +(bm == null ? null : bm.toString()));

        Vip v = (Vip) bm;
        Request req = new Request.Builder()
                .url(Configuration.HTTP_IP + Vip.HTTP_Vip_retrieveNVipConsumeHistoryEx + v.getID() + Vip.HTTP_Vip_retrieveNVipConsumeHistoryEx_startRetailTreadeIDInSQLite + v.getStartRetailTreadeIDInSQLite() + Vip.HTTP_Vip_retrieveNVipConsumeHistoryEx_bQuerySmallerThanStartID + v.getbQuerySmallerThanStartID())
                .addHeader(BaseHttpBO.COOKIE, GlobalController.getInstance().getSessionID())
                .build();
        HttpRequestUnit hru = new RetrieveNVipConsumeHistory();
        hru.setRequest(req);
        hru.setTimeout(TIME_OUT);
        hru.setbPostEventToUI(true);
        httpEvent.setEventProcessed(false);
        httpEvent.setStatus(BaseEvent.EnumEventStatus.EES_Http_ToDo);
        hru.setEvent(httpEvent);
        HttpRequestManager.getCache(HttpRequestManager.EnumDomainType.EDT_Communication).pushHttpRequest(hru);

        log.info("正在请求会员的消费历史");

        return true;
    }

    @Override
    protected boolean doDeleteAsync(int iUseCaseID, BaseModel bm) {
        log.info("正在执行VipHttpBO的deleteAsync，bm=" +(bm == null ? null : bm.toString()));

        Request req = new Request.Builder()
                .url(Configuration.HTTP_IP + Vip.HTTP_Vip_DELETE + bm.getID())
                .addHeader(BaseHttpBO.COOKIE, GlobalController.getInstance().getSessionID())
                .build();
        HttpRequestUnit hru = new DeleteVip();
        hru.setRequest(req);
        hru.setTimeout(TIME_OUT);
        hru.setbPostEventToUI(true);
        httpEvent.setEventProcessed(false);
        httpEvent.setStatus(BaseEvent.EnumEventStatus.EES_Http_ToDo);
        hru.setEvent(httpEvent);
        HttpRequestManager.getCache(HttpRequestManager.EnumDomainType.EDT_Communication).pushHttpRequest(hru);

        httpEvent.setBaseModel1(bm);

        log.info("正在请求删除Vip对象...");
        return true;
    }

    @Override
    public boolean feedback(String feedbackIDs) {
        log.info("正在执行VipHttpBO的feedback，feedbackIDs=" + feedbackIDs);

        Request req = new Request.Builder()
                .url(Configuration.HTTP_IP + Vip.HTTP_Vip_FEEDBACK_URL_FRONT + feedbackIDs + Vip.HTTP_Vip_FEEDBACK_URL_BEHINE)
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

        log.info("通知服务器改POS机已经同步了vip。ID : " + feedbackIDs);

        return true;
    }

}
