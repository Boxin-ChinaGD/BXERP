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
import wpos.http.request.RequestWXPayMicroPay;
import wpos.http.request.RequestWXPayRefund;
import wpos.http.request.RequestWXPayReverse;
import wpos.model.BaseModel;
import wpos.model.ErrorInfo;
import wpos.model.RetailTrade;
import wpos.model.WXPayInfo;
import java.util.List;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import okhttp3.FormBody;
import okhttp3.Request;
import okhttp3.RequestBody;

@Component("wXPayHttpBO")
@Scope("prototype")
public class WXPayHttpBO extends BaseHttpBO {
    private Log log = LogFactory.getLog(this.getClass());

    public WXPayHttpBO(BaseSQLiteEvent sEvent, BaseHttpEvent hEvent) {
        sqLiteEvent = sEvent;
        httpEvent = hEvent;
    }

    public WXPayHttpBO(){}

    public boolean refundAsync(BaseModel bm) {
        log.info("正在执行WXPayHttpBO的refundAsync，bm=" + (bm == null ? null : bm.toString()));

        RetailTrade rt = (RetailTrade) bm;
        //
        RequestBody body = new FormBody.Builder()
                .add(rt.field.getFIELD_NAME_amount(), String.valueOf(rt.getAmount())) //退款金额...
//                .add(rt.getFIELD_NAME_int1(), "1") //...
                .add(rt.getFIELD_NAME_wxOrderSN(), rt.getWxOrderSN() == null ? "" : rt.getWxOrderSN())
                .add(rt.getFIELD_NAME_wxTradeNO(), rt.getWxTradeNO() == null ? "" : rt.getWxTradeNO())
                //TODO 沙箱环境可能不会返回sub_mch_id，所以如果为空就先写死；真实环境必定会返回sub_mch_id。
                .add(rt.getFIELD_NAME_wxRefundSubMchID(), rt.getWxRefundSubMchID() == null ? Constants.submchid : rt.getWxRefundSubMchID())
                .add(rt.getFIELD_NAME_wxRefundDesc(), rt.getWxRefundDesc() == null ? "" : rt.getWxRefundDesc())
                .build();

        Request req = new Request.Builder()
                .url(Configuration.HTTP_IP + WXPayInfo.HTTP_WXPayInfo_Refund)
                .addHeader(BaseHttpBO.COOKIE, GlobalController.getInstance().getSessionID())
                .post(body)
                .build();
        HttpRequestUnit hru = new RequestWXPayRefund();

        hru.setRequest(req);
        hru.setTimeout(TIME_OUT);
        hru.setbPostEventToUI(true);
        httpEvent.setEventProcessed(false);
        httpEvent.setStatus(BaseEvent.EnumEventStatus.EES_Http_ToDo);
        hru.setEvent(httpEvent);
        HttpRequestManager.getCache(HttpRequestManager.EnumDomainType.EDT_Communication).pushHttpRequest(hru);

        return true;
    }

    public boolean reverseAsync(BaseModel bm) {
        log.info("正在执行WXPayHttpBO的reverseAsync，bm=" + (bm == null ? null : bm.toString()));

        RetailTrade rt = (RetailTrade) bm;

        RequestBody body = new FormBody.Builder()
                .add(rt.field.getFIELD_NAME_vipID(), String.valueOf(rt.getVipID()))
                .build();

        Request req = new Request.Builder()
                .url(Configuration.HTTP_IP + WXPayInfo.HTTP_WXPayInfo_Reverse)
                .addHeader(BaseHttpBO.COOKIE, GlobalController.getInstance().getSessionID())
                .post(body)
                .build();
        HttpRequestUnit hru = new RequestWXPayReverse();

        hru.setRequest(req);
        hru.setTimeout(TIME_OUT);
        hru.setbPostEventToUI(true);
        httpEvent.setEventProcessed(false);
        httpEvent.setStatus(BaseEvent.EnumEventStatus.EES_Http_ToDo);
        hru.setEvent(httpEvent);
        HttpRequestManager.getCache(HttpRequestManager.EnumDomainType.EDT_Communication).pushHttpRequest(hru);

        return true;
    }

    public boolean microPayAsync(BaseModel bm) {//TODO 暂没有指定session
        log.info("正在执行WXPayHttpBO的microPayAsync");
        // 判断session是否为空，是否为断网情况
        if (GlobalController.getInstance().getSessionID() == null) {
            httpEvent.setLastErrorCode(ErrorInfo.EnumErrorCode.EC_InvalidSession);
            return false;
        }
        WXPayInfo wxPayInfo = (WXPayInfo) bm;
        RequestBody body = new FormBody.Builder()
                .add(WXPayInfo.field.getFIELD_NAME_auth_code(), wxPayInfo.getAuth_code()) //
                .add(WXPayInfo.field.getFIELD_NAME_total_fee(), wxPayInfo.getTotal_fee()) //
                .build();
        Request req = new Request.Builder()
                .url(Configuration.HTTP_IP + WXPayInfo.HTTP_WXPayInfo_MicroPay)
                .addHeader(BaseHttpBO.COOKIE, GlobalController.getInstance().getSessionID())
                .post(body)
                .build();
        HttpRequestUnit hru = new RequestWXPayMicroPay();
        hru.setRequest(req);
        hru.setTimeout(TIME_OUT);
        hru.setbPostEventToUI(true);
        httpEvent.setEventProcessed(false);
        httpEvent.setStatus(BaseEvent.EnumEventStatus.EES_Http_ToDo);
        hru.setEvent(httpEvent);
        HttpRequestManager.getCache(HttpRequestManager.EnumDomainType.EDT_Communication).pushHttpRequest(hru);

        return true;
    }
//没用到，先注释掉
//    public boolean orderQueryAsync() {
//        httpEvent.setEventProcessed(false);
//        TaskScheduler.execute(new Runnable() {
//            @Override
//            public void run() {
//                try {
//                    WXPayConfigImpl config = new WXPayConfigImpl(Configuration.appid, Configuration.mchid, Configuration.secert);
//
//                    WXPay wxpay = new WXPay(config, WXPayConstants.SignType.MD5, Configuration.bUseSandBox);
//                    Map<String, String> orderQueryData = new HashMap<String, String>();
//
//                    String nonce_str = WXPayUtil.generateNonceStr();
//                    String out_trade_no = String.valueOf(System.currentTimeMillis() % 1000000) + nonce_str.substring(6);
//                    String sign = WXPayUtil.generateSignature(orderQueryData, config.getKey(), WXPayConstants.SignType.MD5);
//
//                    orderQueryData.put("sub_mch_id", BaseStage.submchid);
//                    orderQueryData.put("nonce_str", nonce_str);
//                    orderQueryData.put("sign", sign);
//                    orderQueryData.put("out_trade_no", out_trade_no);
//
//                    Map<String, String> rsp = wxpay.orderQuery(orderQueryData);
//                    log.info("orderQueryData：" + rsp);
//
//                    if (rsp.get("return_code").equals("SUCCESS") && rsp.get("result_code").equals("SUCCESS") && rsp.get("trade_state").equals("SUCCESS")) {
//                        log.info("查询订单成功！！！");
//                        httpEvent.setLastErrorCode(ErrorInfo.EnumErrorCode.EC_NoError);
//                    } else {
//                        log.info("查询订单失败！！！");
//                        httpEvent.setLastErrorCode(ErrorInfo.EnumErrorCode.EC_OtherError);
//                    }
//
//                    httpEvent.setResponseData(rsp.toString());
//                    httpEvent.setRequestType(HttpRequestUnit.EnumRequestType.ERT_WXPay_OrderQuery);
//                } catch (Exception e) {
//                    log.info("httpBO异常：" + e.getMessage());
//                    httpEvent.setLastErrorCode(ErrorInfo.EnumErrorCode.EC_OtherError);
//                    e.printStackTrace();
//                }
//                EventBus.getDefault().post(httpEvent);
//            }
//        });
//        return true;
//    }

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
}
