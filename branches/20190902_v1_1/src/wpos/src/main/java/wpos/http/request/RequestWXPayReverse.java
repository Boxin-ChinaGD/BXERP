package wpos.http.request;

import wpos.http.HttpRequestUnit;

public class RequestWXPayReverse extends HttpRequestUnit {
    @Override
    public EnumRequestType getEnumRequestType() {
        return EnumRequestType.ERT_WXPay_Reverse;
    }
}
