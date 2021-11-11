package com.bx.erp.event;

import com.alibaba.fastjson.JSON;
import com.bx.erp.http.HttpRequestUnit;
import com.bx.erp.model.ErrorInfo;
import com.bx.erp.utils.StringUtils;

import org.apache.log4j.Logger;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class WXPayHttpEvent extends BaseHttpEvent {
    private Logger log = Logger.getLogger(this.getClass());

    protected Map<String, String> microPayResponse;
    protected Map<String, String> refundResponse;
    protected Map<String, String> reverseResponse;
    protected Map<String, String> orderQueryResponse;

    public Map<String, String> getMicroPayResponse() {
        return microPayResponse;
    }

    public void setMicroPayResponse(Map<String, String> microPayResponse) {
        this.microPayResponse = microPayResponse;
    }

    public Map<String, String> getRefundResponse() {
        return refundResponse;
    }

    public void setRefundResponse(Map<String, String> refundResponse) {
        this.refundResponse = refundResponse;
    }

    public String getWxPayStatus() {
        return wxPayStatus;
    }

    public void setWxPayStatus(String wxPayStatus) {
        this.wxPayStatus = wxPayStatus;
    }

    protected String wxPayStatus; // 微信支付的状态

    @Override
    public void onEvent() {
        StringUtils.printCurrentFunction("getRequestType()", getRequestType());

        do {
            if (lastErrorCode != ErrorInfo.EnumErrorCode.EC_NoError) {
                log.info("网络请求错误，" + lastErrorCode);
                break;
            }

            JSONObject jsonObject = parseError(getResponseData());
            if (jsonObject == null || getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_DuplicatedSession) {
                break;
            }

            String rsp = getResponseData();
            switch (getRequestType()) {
                case ERT_WXPay_Refund:
                    Map<String, String> refundData = (Map<String, String>) JSON.parse(rsp);
                    log.info("微信申请退款返回的数据：" + refundData);

                    if ("SUCCESS".equals(refundData.get("return_code")) && "SUCCESS".equals(refundData.get("result_code"))) {// refundData.get("result_code")可能为null
                        setLastErrorCode(ErrorInfo.EnumErrorCode.EC_NoError);
                    } else {
                        setLastErrorCode(ErrorInfo.EnumErrorCode.EC_OtherError);
                    }

                    if (getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError) {
                        // 返回退款成功UI
                        refundResponse = refundData;
                        log.info("WXPayHttpEvent ErrorCode：" + getLastErrorCode());
                        setWxPayStatus("WXRefund");
                    } else {
                        // 返回退款失败UI
                        setWxPayStatus("WXRefundFail");
                        log.info("WXPayHttpEvent ErrorCode：" + getLastErrorCode());
                        log.error("微信退款失败！错误码为：" + refundData.get("err_code") + ";错误信息为：" + refundData.get("err_code_des"));
                    }
                    break;
                case ERT_WXPay_MicroPay: //TODO 是否需要去掉微信扫码支付的event.因为在BO层判断了其支付是否成功
                    if (getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError) {
                        // 返回支付完成UI
                        Map<String, String> microPayData = (Map<String, String>) JSON.parse(rsp);
                        microPayResponse = microPayData;
                        log.info("WXPayHttpEvent ErrorCode：" + getLastErrorCode());
                    } else {
                        // 返回未支付状态UI
                        log.info("WXPayHttpEvent ErrorCode：" + getLastErrorCode());
                    }
                    break;
                case ERT_WXPay_Reverse:
                    Map<String, String> reverseData = StringUtils.mapStringToMap(rsp);
                    log.info("微信支付 --> 撤销订单返回的数据：" + reverseData);
                    if ("SUCCESS".equals(reverseData.get("return_code")) && "SUCCESS".equals(reverseData.get("result_code"))) {
                        setLastErrorCode(ErrorInfo.EnumErrorCode.EC_NoError);
                    } else {
                        setLastErrorCode(ErrorInfo.EnumErrorCode.EC_OtherError);
                    }

                    if (getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError) {
                        // 返回撤销订单完成UI
                        reverseResponse = reverseData;
                        log.info("WXPayHttpEvent ErrorCode：" + getLastErrorCode());
                    } else {
                        // 返回撤销订单失败UI
                        log.info("WXPayHttpEvent ErrorCode：" + getLastErrorCode());
                    }
                case ERT_WXPay_OrderQuery:
                    Map<String, String> orderQueryData = StringUtils.mapStringToMap(rsp);
                    log.info("微信支付 --> 查询订单返回的数据：" + orderQueryData);

                    if ("SUCCESS".equals(orderQueryData.get("return_code")) && "SUCCESS".equals(orderQueryData.get("result_code"))) {
                        setLastErrorCode(ErrorInfo.EnumErrorCode.EC_NoError);
                    } else {
                        setLastErrorCode(ErrorInfo.EnumErrorCode.EC_OtherError);
                    }

                    if (getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError) {
                        // 返回查询订单完成UI
                        orderQueryResponse = orderQueryData;
                        log.info("WXPayHttpEvent ErrorCode：" + getLastErrorCode());
                    } else {
                        // 返回查询订单失败UI
                        log.info("WXPayHttpEvent ErrorCode：" + getLastErrorCode());
                    }

                default:
                    log.info("Not yet implemented!");
                    throw new RuntimeException("Not yet implemented!");
            }
        } while (false);
        setStatus(BaseEvent.EnumEventStatus.EES_Http_Done);
        setEventProcessed(true);
    }
}
