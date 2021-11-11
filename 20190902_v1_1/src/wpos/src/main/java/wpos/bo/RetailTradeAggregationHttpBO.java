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
import wpos.http.request.CreateRetailTradeAggregation;
import wpos.model.BaseModel;
import wpos.model.RetailTradeAggregation;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.List;

import okhttp3.FormBody;
import okhttp3.Request;
import okhttp3.RequestBody;

@Component("retailTradeAggregationHttpBO")
@Scope("prototype")
public class RetailTradeAggregationHttpBO extends BaseHttpBO {
    private Log log = LogFactory.getLog(this.getClass());

    DateFormat bf = new SimpleDateFormat("yyyy/MM/dd hh:mm:ss");

    public RetailTradeAggregationHttpBO(BaseSQLiteEvent sEvent, BaseHttpEvent hEvent) {
        sqLiteEvent = sEvent;
        httpEvent = hEvent;
    }

    public RetailTradeAggregationHttpBO(){}

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
        log.info("正在执行RetailTradeAggregationHttpBO的createAsync，bm=" + (bm == null ? null : bm.toString()));

        RequestBody body = new FormBody.Builder()
                .add(((RetailTradeAggregation) bm).field.getFIELD_NAME_staffID(), String.valueOf(((RetailTradeAggregation) bm).getStaffID()))
                .add(((RetailTradeAggregation) bm).field.getFIELD_NAME_posID(), String.valueOf(((RetailTradeAggregation) bm).getPosID()))
                .add(((RetailTradeAggregation) bm).field.getFIELD_NAME_workTimeStart(), Constants.getSimpleDateFormat().format(((RetailTradeAggregation) bm).getWorkTimeStart()))
                .add(((RetailTradeAggregation) bm).field.getFIELD_NAME_workTimeEnd(), Constants.getSimpleDateFormat().format(((RetailTradeAggregation) bm).getWorkTimeEnd()))
                .add(((RetailTradeAggregation) bm).field.getFIELD_NAME_tradeNO(), String.valueOf(((RetailTradeAggregation) bm).getTradeNO()))
                .add(((RetailTradeAggregation) bm).field.getFIELD_NAME_amount(), String.valueOf(((RetailTradeAggregation) bm).getAmount()))
                .add(((RetailTradeAggregation) bm).field.getFIELD_NAME_reserveAmount(), String.valueOf(((RetailTradeAggregation) bm).getReserveAmount()))
                .add(((RetailTradeAggregation) bm).field.getFIELD_NAME_cashAmount(), String.valueOf(((RetailTradeAggregation) bm).getCashAmount()))
                .add(((RetailTradeAggregation) bm).field.getFIELD_NAME_wechatAmount(), String.valueOf(((RetailTradeAggregation) bm).getWechatAmount()))
                .add(((RetailTradeAggregation) bm).field.getFIELD_NAME_alipayAmount(), String.valueOf(((RetailTradeAggregation) bm).getAlipayAmount()))
                .add(((RetailTradeAggregation) bm).field.getFIELD_NAME_amount1(), String.valueOf(((RetailTradeAggregation) bm).getAmount1()))
                .add(((RetailTradeAggregation) bm).field.getFIELD_NAME_amount2(), String.valueOf(((RetailTradeAggregation) bm).getAmount2()))
                .add(((RetailTradeAggregation) bm).field.getFIELD_NAME_amount3(), String.valueOf(((RetailTradeAggregation) bm).getAmount3()))
                .add(((RetailTradeAggregation) bm).field.getFIELD_NAME_amount4(), String.valueOf(((RetailTradeAggregation) bm).getAmount4()))
                .add(((RetailTradeAggregation) bm).field.getFIELD_NAME_amount5(), String.valueOf(((RetailTradeAggregation) bm).getAmount5()))
                .build();
        Request req = new Request.Builder()
                .url(Configuration.HTTP_IP + RetailTradeAggregation.HTTP_URL_CREATE)
                .addHeader(BaseHttpBO.COOKIE, GlobalController.getInstance().getSessionID())
                .post(body)
                .build();
        HttpRequestUnit hru = new CreateRetailTradeAggregation();
        hru.setRequest(req);
        hru.setTimeout(TIME_OUT);
        hru.setbPostEventToUI(true);
        httpEvent.setEventProcessed(false);
        httpEvent.setStatus(BaseEvent.EnumEventStatus.EES_Http_ToDo);
        hru.setEvent(httpEvent);
        HttpRequestManager.getCache(HttpRequestManager.EnumDomainType.EDT_Communication).pushHttpRequest(hru);

        log.info("收银汇总sessionID : " + GlobalController.getInstance().getSessionID());
        log.info("正在上传交班表(For创建)...");

        return true;
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
