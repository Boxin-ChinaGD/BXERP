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
import com.bx.erp.http.request.RequestWXPayMicroPay;
import com.bx.erp.http.request.RequestWXPayRefund;
import com.bx.erp.http.request.RequestWXPayReverse;
import com.bx.erp.model.BaseModel;
import com.bx.erp.model.ErrorInfo;
import com.bx.erp.model.RetailTrade;
import com.bx.erp.model.WXPayInfo;
import com.bx.erp.view.activity.BaseActivity;

import org.apache.log4j.Logger;

import java.util.List;

import okhttp3.FormBody;
import okhttp3.Request;
import okhttp3.RequestBody;

public class WXPayHttpBO extends BaseHttpBO {
    private Logger log = Logger.getLogger(this.getClass());

    public WXPayHttpBO(Context ctx, BaseSQLiteEvent sEvent, BaseHttpEvent hEvent) {
        context = ctx;
        sqLiteEvent = sEvent;
        httpEvent = hEvent;
    }

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
                .add(WXPayInfo.field.getFIELD_NAME_couponCode(), (wxPayInfo.getCouponCode()) == null ? "" : wxPayInfo.getCouponCode()) //
                .add(WXPayInfo.field.getFIELD_NAME_bonusIsChanged(), String.valueOf(wxPayInfo.getBonusIsChanged())) //
                .add(WXPayInfo.field.getFIELD_NAME_vipID(),  String.valueOf(wxPayInfo.getVipID())) //
                .add(WXPayInfo.field.getFIELD_NAME_wxVipID(),  String.valueOf(wxPayInfo.getWxVipID())) //
                .add(WXPayInfo.field.getFIELD_NAME_wxVipCardDetailID(),  String.valueOf(wxPayInfo.getWxVipCardDetailID())) //
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
//                    orderQueryData.put("sub_mch_id", BaseActivity.submchid);
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
