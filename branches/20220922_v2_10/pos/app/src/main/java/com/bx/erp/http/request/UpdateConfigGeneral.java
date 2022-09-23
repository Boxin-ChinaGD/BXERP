package com.bx.erp.http.request;

import com.bx.erp.http.HttpRequestUnit;

public class UpdateConfigGeneral extends HttpRequestUnit {
    @Override
    public EnumRequestType getEnumRequestType() {
        return EnumRequestType.ERT_ConfigGeneral_Update;
    }
}