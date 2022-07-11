package wpos.bo;

import org.apache.log4j.Logger;

import java.util.List;

import okhttp3.FormBody;
import okhttp3.Request;
import okhttp3.RequestBody;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import wpos.common.GlobalController;
import wpos.event.BaseEvent;
import wpos.event.BaseHttpEvent;
import wpos.event.UI.BaseSQLiteEvent;
import wpos.helper.Configuration;
import wpos.http.HttpRequestManager;
import wpos.http.HttpRequestUnit;
import wpos.http.request.RetrieveNCouponCodeC;
import wpos.model.BaseModel;
import wpos.model.CouponCode;

@Component("couponCodeHttpBO")
@Scope("prototype")
public class CouponCodeHttpBO extends BaseHttpBO{
    private Logger log = Logger.getLogger(this.getClass());

    public CouponCodeHttpBO(BaseSQLiteEvent sEvent, BaseHttpEvent hEvent) {
        sqLiteEvent = sEvent;
        httpEvent = hEvent;
    }

    public CouponCodeHttpBO(){}

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
        log.info("正在执行CouponCodeHttpBO的retrieveNAsyncC，bm=" +(bm == null ? null : bm.toString()));

        CouponCode couponCode = (CouponCode) bm;
        RequestBody body =  new FormBody.Builder()
                .add(CouponCode.field.getFIELD_NAME_vipID(), String.valueOf(couponCode.getVipID()))
                .add(CouponCode.field.getFIELD_NAME_status(), String.valueOf(couponCode.getStatus()))
                .build();

        Request req = new Request.Builder()
                .url(Configuration.HTTP_IP + CouponCode.HTTP_CouponCode_RetrieveNC)
                .addHeader(BaseHttpBO.COOKIE, GlobalController.getInstance().getSessionID())
                .post(body)
                .build();
        HttpRequestUnit hru = new RetrieveNCouponCodeC();
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
