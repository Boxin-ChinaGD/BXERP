package wpos.http.request;


import wpos.http.HttpRequestUnit;

public class RetrieveNCouponCodeC extends HttpRequestUnit {
    @Override
    public EnumRequestType getEnumRequestType() {
        return HttpRequestUnit.EnumRequestType.ERT_CouponCode_RetrieveNC;
    }
}
