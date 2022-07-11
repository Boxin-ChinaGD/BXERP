package wpos.http.request;

import wpos.http.HttpRequestUnit;

public class Retrieve1PurchasingOrder extends HttpRequestUnit{
    @Override
    public EnumRequestType getEnumRequestType() {
        return EnumRequestType.ERT_PurchasingOrder_Retrieve1;
    }
}
