package com.bx.erp.http.request;

import com.bx.erp.http.HttpRequestUnit;

public class CreatePromotion extends HttpRequestUnit {
    @Override
    public EnumRequestType getEnumRequestType() {
        return EnumRequestType.ERT_Promotion_Create;
    }
}
