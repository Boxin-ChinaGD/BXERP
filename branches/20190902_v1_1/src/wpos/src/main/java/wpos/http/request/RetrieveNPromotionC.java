package wpos.http.request;

import wpos.http.HttpRequestUnit;

public class RetrieveNPromotionC extends HttpRequestUnit {
    @Override
    public EnumRequestType getEnumRequestType() {
        return EnumRequestType.ERT_Promotion_RetrieveNC;
    }
}
