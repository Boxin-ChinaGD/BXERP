package wpos.http.request;

import wpos.http.HttpRequestUnit;

public class RetrieveNCouponC extends HttpRequestUnit {
    @Override
    public HttpRequestUnit.EnumRequestType getEnumRequestType() {
        return HttpRequestUnit.EnumRequestType.ERT_Coupon_RetrieveNC;
    }
}
