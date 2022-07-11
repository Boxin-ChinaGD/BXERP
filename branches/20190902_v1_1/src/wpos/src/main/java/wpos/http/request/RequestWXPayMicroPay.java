package wpos.http.request;

import wpos.http.HttpRequestUnit;

public class RequestWXPayMicroPay extends HttpRequestUnit {
    @Override
    public HttpRequestUnit.EnumRequestType getEnumRequestType() {
        return HttpRequestUnit.EnumRequestType.ERT_WXPay_MicroPay;
    }
}
