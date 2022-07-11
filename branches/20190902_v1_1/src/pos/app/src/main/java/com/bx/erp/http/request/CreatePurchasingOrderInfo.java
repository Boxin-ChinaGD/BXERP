package com.bx.erp.http.request;

import com.bx.erp.http.HttpRequestUnit;

public class CreatePurchasingOrderInfo extends HttpRequestUnit{
    @Override
    public EnumRequestType getEnumRequestType() {
        return EnumRequestType.ERT_PurchasingOrderInfo_Create;
    }
}
