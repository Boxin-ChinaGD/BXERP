package com.bx.erp.http.request;

import com.bx.erp.http.HttpRequestUnit;

public class RetrieveNCConfigGeneral extends HttpRequestUnit {
    @Override
    public EnumRequestType getEnumRequestType() {
        return EnumRequestType.ERT_ConfigGeneral_RetrieveNC;
    }
}
