package wpos.http.request;

import wpos.http.HttpRequestUnit;

public class CreatePurchasingOrderInfo extends HttpRequestUnit{
    @Override
    public EnumRequestType getEnumRequestType() {
        return EnumRequestType.ERT_PurchasingOrderInfo_Create;
    }
}
