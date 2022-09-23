package com.bx.erp.http.request;

import com.bx.erp.http.HttpRequestUnit;


public class UpdateBrand extends HttpRequestUnit {
    @Override
    public EnumRequestType getEnumRequestType() {
        return EnumRequestType.ERT_Brand_Update;
    }
}
