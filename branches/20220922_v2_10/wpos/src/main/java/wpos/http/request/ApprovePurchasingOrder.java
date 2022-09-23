package wpos.http.request;

import wpos.http.HttpRequestUnit;

public class ApprovePurchasingOrder extends HttpRequestUnit {
    @Override
    public HttpRequestUnit.EnumRequestType getEnumRequestType() {
        return HttpRequestUnit.EnumRequestType.ERT_ApprovePurchasingOrder;
    }
}
