package wpos.http.request;

import wpos.http.HttpRequestUnit;

public class CreatePromotion extends HttpRequestUnit {
    @Override
    public EnumRequestType getEnumRequestType() {
        return EnumRequestType.ERT_Promotion_Create;
    }
}
